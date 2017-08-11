package com.xiongxh.popularmovies.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xiongxh.popularmovies.data.MovieContract.MovieEntry;

import java.util.Vector;

public class MovieJsonUtils {

    private static final String LOG_TAG = MovieJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String MDB_RESULT = "results";
    private static final String MDB_MOVIE_ID = "id";
    private static final String MDB_TITLE = "title";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_LANGUAGE = "original_language";
    private static final String MDB_POSTER = "poster_path";
    private static final String MDB_BACKDROP = "backdrop_path";
    private static final String MDB_RELEASE = "release_date";
    private static final String MDB_VOTENUM = "vote_count";
    private static final String MDB_VOTESCORE = "vote_average";
    private static final String MDB_POPULARITY = "popularity";

    private boolean DEBUG = true;

    public static ContentValues[] getMovieContentValuesFromJson(Context context, String movieJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");

        //create a JSONObject from the JSON response string
        JSONObject baseJsonResponse = new JSONObject(movieJsonStr);

        //extract the JSONArray associated with the key called "features"'
        //which represents a list of features(or earthquakes)
        JSONArray movieArray = baseJsonResponse.getJSONArray(MDB_RESULT);

        ContentValues[] movieContentValues = new ContentValues[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            String movieTitle;
            String movieOverview;
            String movieLanguage;
            String posterPath;
            String backdropPath;
            String releaseTime;
            long movieId;
            int voteNumber;
            double voteScore;
            double moviePopularity;


            // Get the JSON object representing the day
            JSONObject movieObject = movieArray.getJSONObject(i);

            // description is in a child array called "weather", which is 1 element long.

            movieTitle = movieObject.getString(MDB_TITLE);
            movieOverview = movieObject.getString(MDB_OVERVIEW);
            movieLanguage = movieObject.getString(MDB_LANGUAGE);
            posterPath = movieObject.getString(MDB_POSTER);
            backdropPath = movieObject.getString(MDB_BACKDROP);
            releaseTime = movieObject.getString(MDB_RELEASE);
            movieId = movieObject.getLong(MDB_MOVIE_ID);
            voteNumber = movieObject.getInt(MDB_VOTENUM);
            voteScore = movieObject.getDouble(MDB_VOTESCORE);
            moviePopularity = movieObject.getDouble(MDB_POPULARITY);


            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_TITLE, movieTitle);
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, movieOverview);
            movieValues.put(MovieEntry.COLUMN_LANGUAGE, movieLanguage);
            movieValues.put(MovieEntry.COLUMN_POSTER, posterPath);
            movieValues.put(MovieEntry.COLUMN_BACKDROP, backdropPath);
            movieValues.put(MovieEntry.COLUMN_DATE, releaseTime);
            movieValues.put(MovieEntry.COLUMN_VOTENUM, voteNumber);
            movieValues.put(MovieEntry.COLUMN_VOTESCORE, voteScore);
            movieValues.put(MovieEntry.COLUMN_POP, moviePopularity);

            movieContentValues[i]= movieValues;
        }

        return movieContentValues;
    }
}
