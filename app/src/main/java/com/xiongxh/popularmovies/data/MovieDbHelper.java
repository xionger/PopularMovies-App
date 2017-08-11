package com.xiongxh.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiongxh.popularmovies.data.MovieContract.MovieEntry;
import com.xiongxh.popularmovies.data.MovieContract.ReviewsEntry;
import com.xiongxh.popularmovies.data.MovieContract.VideosEntry;



public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "popmovies.db";

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_DATE + " TEXT, " +
                MovieEntry.COLUMN_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_POSTER + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP + " TEXT, " +
                MovieEntry.COLUMN_POP + " REAL, " +
                MovieEntry.COLUMN_VOTESCORE + " REAL, " +
                MovieEntry.COLUMN_VOTENUM + " INTEGER, " +
                " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewsEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                ReviewsEntry.COLUMN_REVIEW_CONTENT + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                "UNIQUE (" + ReviewsEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + MovieContract.VideosEntry.TABLE_NAME + " (" +
                VideosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VideosEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL, " +
                VideosEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                VideosEntry.COLUMN_VIDEO_SITE + " TEXT, " +
                VideosEntry.COLUMN_VIDEO_TYPE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                "UNIQUE (" + VideosEntry.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideosEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
