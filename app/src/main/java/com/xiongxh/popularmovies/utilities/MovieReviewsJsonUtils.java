package com.xiongxh.popularmovies.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.xiongxh.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieReviewsJsonUtils {
    private static final String LOG_TAG = MovieReviewsJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String MDB_MOVIE_ID = "id";
    private static final String MDB_RV_RESULT = "results";
    private static final String MDB_RV_ID = "id";
    private static final String MDB_RV_AUTHOR = "author";
    private static final String MDB_RV_CONTENT = "content";

    private boolean DEBUG = true;

    public static ContentValues[] getMovieReviewContentValuesFromJson(Context context, String movieJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");
        Log.d(LOG_TAG, "movieJsonStr: " + movieJsonStr);

        //create a JSONObject from the JSON response string
        JSONObject baseJsonResponse = new JSONObject(movieJsonStr);

        Long movieId = baseJsonResponse.getLong(MDB_MOVIE_ID);

        //extract the JSONArray associated with the key called "features"'
        //which represents a list of features(or reviews)
        JSONArray movieReviewArray = baseJsonResponse.getJSONArray(MDB_RV_RESULT);

        ContentValues[] movieReviewContentValues = new ContentValues[movieReviewArray.length()];

        for (int i = 0; i < movieReviewArray.length(); i++) {

            String reviewId;
            String reviewAuthor;
            String reviewContent;

            // Get the JSON object representing the review
            JSONObject movieReviewObject = movieReviewArray.getJSONObject(i);

            reviewId = movieReviewObject.getString(MDB_RV_ID);
            reviewAuthor = movieReviewObject.getString(MDB_RV_AUTHOR);
            reviewContent = movieReviewObject.getString(MDB_RV_CONTENT);


            ContentValues reviewValues = new ContentValues();

            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_ID, reviewId);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR, reviewAuthor);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_CONTENT, reviewContent);
            reviewValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

            movieReviewContentValues[i]= reviewValues;
        }

        Log.d(LOG_TAG, "Exiting MovieReviewsJsonUtils.");

        return movieReviewContentValues;
    }
}
