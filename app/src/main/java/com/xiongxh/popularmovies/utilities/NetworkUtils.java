package com.xiongxh.popularmovies.utilities;

import java.net.URL;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.xiongxh.popularmovies.BuildConfig;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.data.MoviePreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //private static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie?api_key={key}&query={David}";

    //private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";

    private static String BASE_URL;

    private static final String BASE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?";

    private static final String BASE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?";

    private static final String BASE_ITEM_URL = "https://api.themoviedb.org/3/movie/";

    public static final String IMAGE_BASE_URL =  "https://image.tmdb.org/t/p/w500/";

    private static final String API_KEY_PARAM = "api_key";


    public static URL getUrl(Context context){
        String sortStr = MoviePreferences.getPreferredSortType(context);
        //String sortStr = MoviePreferences.sortType;
        return buildUrlWithQuery(sortStr);
    }

    private static URL buildUrlWithQuery(String sortType){
        /**
        final String SORT_BY_PARAM = "sort_by";
        final String RELEASE_DATE_PARAM = "primary_release_date.gte";
        final String release_after = "2015-01-01";
        final String VOTE_COUNT_PARAM = "vote_count.gte";
        final String vote_count_threshold = "100";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, sortType)
                .appendQueryParameter(RELEASE_DATE_PARAM, release_after)
                .appendQueryParameter(VOTE_COUNT_PARAM, vote_count_threshold)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();
         */
        if (sortType.equals("top_rated")){
            BASE_URL = BASE_URL_TOP_RATED;
        } else {
            BASE_URL = BASE_URL_POPULAR;
        }

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            URL url = new URL(buildUri.toString());
            Log.d(LOG_TAG, "Calling url: " + url);
            return url;
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
    }

    public static URL getMovieReviewUrl(long movieId){
       // https://api.themoviedb.org/3/movie/321612/reviews?api_key=...
        Uri reviewUri = Uri.parse(BASE_ITEM_URL).buildUpon()
                .appendEncodedPath(Long.toString(movieId))
                .appendEncodedPath(MovieContract.PATH_REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            URL reviewUrl = new URL(reviewUri.toString());
            Log.d(LOG_TAG, "Calling url: " + reviewUrl);
            return reviewUrl;
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
    }

    public static URL getMovieVideoUrl(long movieId){
        // https://api.themoviedb.org/3/movie/321612/reviews?api_key=...
        Uri videoUri = Uri.parse(BASE_ITEM_URL).buildUpon()
                .appendEncodedPath(Long.toString(movieId))
                .appendEncodedPath(MovieContract.PATH_VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            URL videoUrl = new URL(videoUri.toString());
            Log.d(LOG_TAG, "Calling url: " + videoUrl);
            return videoUrl;
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000);
            //urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieJsonStr = buffer.toString();

            //Log.d(LOG_TAG, "Returning movie string: " + movieJsonStr);

            return movieJsonStr;

        }finally {
            urlConnection.disconnect();
        }
    }
}
