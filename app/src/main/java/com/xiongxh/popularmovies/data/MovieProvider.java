package com.xiongxh.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiongxh.popularmovies.data.MovieContract.MovieEntry;
import com.xiongxh.popularmovies.data.MovieContract.ReviewsEntry;
import com.xiongxh.popularmovies.data.MovieContract.VideosEntry;

public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG =MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_IDX = 101;

    private static final int REVIEWS = 200;
    private static final int REVIEW_IDX = 201;

    private static final int VIDEOS = 300;
    private static final int VIDEO_IDX = 301;

    private static final String mReviewMovieIdSelection =
            ReviewsEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID + "=?";
    private static final String mVideoMovieIdSelection =
            VideosEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID + "=?";

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_IDX);

        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/#", REVIEW_IDX);

        matcher.addURI(authority, MovieContract.PATH_VIDEOS, VIDEOS);
        matcher.addURI(authority, MovieContract.PATH_VIDEOS + "/#", VIDEO_IDX);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "Query: calling the URI: " + uri);

        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        Log.d(LOG_TAG, "Query matches: " + match);

        switch (match){
            case MOVIES: {

                returnCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MOVIE_IDX: {

                String movieIndexStr = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{movieIndexStr};

                returnCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case REVIEW_IDX: {
                Log.d(LOG_TAG, "1: Error here?");
                String movieIndexStr = uri.getPathSegments().get(1);
                //String mSelection = "_id=?";
                Log.d(LOG_TAG, "movieIndexStr: " + movieIndexStr);
                String[] mSelectionArgs = new String[]{movieIndexStr};
                Log.d(LOG_TAG, "2: Error here?");
                returnCursor = db.query(
                        ReviewsEntry.TABLE_NAME,
                        projection,
                        mReviewMovieIdSelection,
                        //mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                Log.d(LOG_TAG, "3: Error here?");
                break;

            }
            case VIDEO_IDX: {
                Log.d(LOG_TAG, "1: Error here?");
                String movieIndexStr = uri.getPathSegments().get(1);
                //String mSelection = "_id=?";
                Log.d(LOG_TAG, "movieIndexStr: " + movieIndexStr);
                String[] mSelectionArgs = new String[]{movieIndexStr};
                Log.d(LOG_TAG, "2: Error here?");
                returnCursor = db.query(
                        VideosEntry.TABLE_NAME,
                        projection,
                        mVideoMovieIdSelection,
                        //mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                Log.d(LOG_TAG, "3: Error here?");
                break;

            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues contentValues) {
        Log.d(LOG_TAG, "Entering insert, calling Uri: " + uri);

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Log.d(LOG_TAG, "Match: " + match);

        Uri returnUri;
        long _id;

        switch (match){
            case MOVIES: {
                _id = db.insert(
                        MovieEntry.TABLE_NAME,
                        null,
                        contentValues);

                if (_id > 0){
                    returnUri = MovieEntry.buildMovieUri(_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }

        if (_id > 0){
            Log.d(LOG_TAG, "Insert: Setting notifychange with Uri: " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else{
            Log.d(LOG_TAG, "Insert nothing");
        }

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValues){
        Log.d(LOG_TAG, "Entering bulkInsert..., at Uri: " + uri);
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsInserted = 0;

        switch (match){
            case MOVIES: {
                db.beginTransaction();

                try{
                    for(ContentValues value: contentValues){
                        long _id = db.insert(MovieEntry.TABLE_NAME, null, value);

                        if (_id != -1){
                            rowsInserted ++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0){
                    Log.d(LOG_TAG, "Movies Bulkinsert: Setting notifychange with Uri: " + uri);
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                Log.d(LOG_TAG, "Exiting movies bulkInsert, rows inserted: " + rowsInserted);
                return rowsInserted;
            }
            case REVIEWS:{
                db.beginTransaction();

                try{
                    for (ContentValues value: contentValues){
                        long _id = db.insert(ReviewsEntry.TABLE_NAME, null, value);

                        if (_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0){
                    Log.d(LOG_TAG, "Reviews Bulkinsert: Setting notifychange with Uri: " + uri);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                Log.d(LOG_TAG, "Exiting reviews bulkInsert, rows inserted: " + rowsInserted);
                return rowsInserted;
            }
            case VIDEOS:{
                db.beginTransaction();

                try{
                    for (ContentValues value: contentValues){
                        long _id = db.insert(VideosEntry.TABLE_NAME, null, value);

                        if (_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0){
                    Log.d(LOG_TAG, "Videos Bulkinsert: Setting notifychange with Uri: " + uri);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                Log.d(LOG_TAG, "Exiting videos bulkInsert, rows inserted: " + rowsInserted);
                return rowsInserted;
            }
            default: {
                return super.bulkInsert(uri, contentValues);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        //String selectionCriteria;

        if (null == selection) {
            selection = "1";
        }

        switch (match){
            case MOVIES: {
                rowsDeleted = db.delete(
                        MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_IDX:{
                String movieIndexStr = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{movieIndexStr};

                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            }
            case REVIEWS: {
                rowsDeleted = db.delete(
                        ReviewsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case REVIEW_IDX: {
                String reviewIndexStr = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[] {reviewIndexStr};

                rowsDeleted = db.delete(ReviewsEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            }
            case VIDEOS: {
                rowsDeleted = db.delete(
                        VideosEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case VIDEO_IDX: {
                String videoIndexStr = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[] {videoIndexStr};

                rowsDeleted = db.delete(VideosEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unkown Uri: " + uri);
            }
        }

        if (rowsDeleted !=0){
            Log.d(LOG_TAG, "Delete, setting notifychange with Uri: " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        }else{
            Log.d(LOG_TAG, "Delete nothing");
        }
        Log.d(LOG_TAG, "Exiting delete, rowsDeleted: " + rowsDeleted);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;
        //String selectionCriteria;

        switch (match){
            case MOVIES: {
                rowsUpdated = db.update(
                        MovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_IDX: {
                String movieIndexStr = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{movieIndexStr};

                rowsUpdated = db.update(
                        MovieEntry.TABLE_NAME,
                        contentValues,
                        mSelection,
                        mSelectionArgs);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsUpdated != 0){
            Log.d(LOG_TAG, "Update, setting notifychange with Uri: " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        }else{
            Log.d(LOG_TAG, "Update nothing");
        }

        Log.d(LOG_TAG, "Exiting update, rowsUpdated: " + rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES:{
                return MovieEntry.CONTENT_TYPE;
            }
            case MOVIE_IDX:{
                return MovieEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unkown Uri: " + uri);
            }
        }
    }

}