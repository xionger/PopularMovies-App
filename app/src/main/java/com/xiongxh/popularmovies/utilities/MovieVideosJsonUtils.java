package com.xiongxh.popularmovies.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.xiongxh.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieVideosJsonUtils {
    private static final String LOG_TAG = MovieVideosJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String MDB_MOVIE_ID = "id";
    private static final String MDB_VD_RESULT = "results";
    private static final String MDB_VD_ID = "id";
    private static final String MDB_VD_KEY = "key";
    private static final String MDB_VD_SITE = "site";
    private static final String MDB_VD_TYPE = "type";

    private boolean DEBUG = true;

    public static ContentValues[] getMovieVideoContentValuesFromJson(Context context, String videoJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");
        Log.d(LOG_TAG, "movieJsonStr: " + videoJsonStr);

        //create a JSONObject from the JSON response string
        JSONObject baseJsonResponse = new JSONObject(videoJsonStr);

        Long movieId = baseJsonResponse.getLong(MDB_MOVIE_ID);

        //extract the JSONArray associated with the key called "features"'
        //which represents a list of features(or reviews)
        JSONArray movieVideoArray = baseJsonResponse.getJSONArray(MDB_VD_RESULT);

        ContentValues[] movieVideoContentValues = new ContentValues[movieVideoArray.length()];

        int numOfYoutubeVideos = 0;

        for (int i = 0; i < movieVideoArray.length(); i++) {

            String videoId;
            String videoKey;
            String videoSite;
            String videoType;

            // Get the JSON object representing the review
            JSONObject movieVideoObject = movieVideoArray.getJSONObject(i);

            videoId = movieVideoObject.getString(MDB_VD_ID);
            videoKey = movieVideoObject.getString(MDB_VD_KEY);
            videoSite = movieVideoObject.getString(MDB_VD_SITE);
            videoType = movieVideoObject.getString(MDB_VD_TYPE);


            if (videoSite.equals("YouTube")) {
                numOfYoutubeVideos++;

                ContentValues videoValues = new ContentValues();

                videoValues.put(MovieContract.VideosEntry.COLUMN_VIDEO_ID, videoId);
                videoValues.put(MovieContract.VideosEntry.COLUMN_VIDEO_KEY, videoKey);
                videoValues.put(MovieContract.VideosEntry.COLUMN_VIDEO_SITE, videoSite);
                videoValues.put(MovieContract.VideosEntry.COLUMN_VIDEO_TYPE, videoType);
                videoValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

                movieVideoContentValues[i] = videoValues;
            }
        }

        ContentValues[] youtubeVideoContentValues = new ContentValues[numOfYoutubeVideos];
        int j = 0;

        for (int i = 0; i < movieVideoArray.length(); i++){
            if (movieVideoContentValues[i] != null){
                youtubeVideoContentValues[j] = movieVideoContentValues[i];
                j++;
            }
        }

        Log.d(LOG_TAG, "Exiting MovieReviewsJsonUtils.");

        return youtubeVideoContentValues;
    }
}
