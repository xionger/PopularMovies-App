package com.xiongxh.popularmovies.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.MovieJsonUtils;
import com.xiongxh.popularmovies.utilities.MovieReviewsJsonUtils;
import com.xiongxh.popularmovies.utilities.MovieVideosJsonUtils;
import com.xiongxh.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context){
        try{
            //URL movieRequestUrl = NetworkUtils.getUrlSortByPop();
            URL movieRequestUrl = NetworkUtils.getUrl(context);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues[] movieValues = MovieJsonUtils.getMovieContentValuesFromJson(context, jsonMovieResponse);

            if (movieValues != null && movieValues.length != 0){
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    synchronized public static void synMovieReview(Context context, long movieId){
        try{
            URL movieReviewRequestUrl = NetworkUtils.getMovieReviewUrl(movieId);

            String jsonMovieReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewRequestUrl);

            ContentValues[] movieReviewValues = MovieReviewsJsonUtils.getMovieReviewContentValuesFromJson(context, jsonMovieReviewResponse);

            if (movieReviewValues != null && movieReviewValues.length != 0){
                ContentResolver movieReviewContentResolver = context.getContentResolver();

                movieReviewContentResolver.delete(MovieContract.ReviewsEntry.CONTENT_URI,
                        null,
                        null);

                movieReviewContentResolver.bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI, movieReviewValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    synchronized public static void synMovieVideos(Context context, long movieId){
        try{
            URL movieVideoRequestUrl = NetworkUtils.getMovieVideoUrl(movieId);

            String jsonMovieVideoResponse = NetworkUtils.getResponseFromHttpUrl(movieVideoRequestUrl);

            ContentValues[] movieVideoValues = MovieVideosJsonUtils.getMovieVideoContentValuesFromJson(context, jsonMovieVideoResponse);

            if (movieVideoValues != null && movieVideoValues.length != 0){
                ContentResolver movieVideoContentResolver = context.getContentResolver();

                movieVideoContentResolver.delete(MovieContract.VideosEntry.CONTENT_URI,
                        null,
                        null);

                movieVideoContentResolver.bulkInsert(MovieContract.VideosEntry.CONTENT_URI, movieVideoValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
