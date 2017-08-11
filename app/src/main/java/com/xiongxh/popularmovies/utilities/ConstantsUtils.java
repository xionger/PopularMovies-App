package com.xiongxh.popularmovies.utilities;

import com.xiongxh.popularmovies.data.MovieContract;

public class ConstantsUtils {

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            //MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_BACKDROP,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_VOTESCORE,
            MovieContract.MovieEntry.COLUMN_VOTENUM,
            MovieContract.MovieEntry.COLUMN_POP
    };

    public static final String[] MOVIE_REVIEW_COLUMNS={
            MovieContract.ReviewsEntry.TABLE_NAME + "." + MovieContract.ReviewsEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewsEntry.COLUMN_REVIEW_ID,
            MovieContract.ReviewsEntry.COLUMN_REVIEW_CONTENT,
            MovieContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR
    };

    public static final String[] MOVIE_VIDEO_COLUMNS={
            MovieContract.VideosEntry.TABLE_NAME + "." + MovieContract.VideosEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.VideosEntry.COLUMN_VIDEO_ID,
            MovieContract.VideosEntry.COLUMN_VIDEO_KEY,
            MovieContract.VideosEntry.COLUMN_VIDEO_SITE,
            MovieContract.VideosEntry.COLUMN_VIDEO_TYPE
    };

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_TITLE = 2;
    public static final int COLUMN_OVERVIEW = 3;
    public static final int COLUMN_POSTER = 4;
    public static final int COLUMN_BACKDROP = 5;
    public static final int COLUMN_DATE = 6;
    public static final int COLUMN_LANGUAGE = 7;
    public static final int COLUMN_VOTESCORE = 8;
    public static final int COLUMN_VOTENUM = 9;
    public static final int COLUMN_POP = 10;

    public static final int COLUMN_REVIEW_ID = 2;
    public static final int COLUMN_REVIEW_CONTENT = 3;
    public static final int COLUMN_REVIEW_AUTHOR = 4;

    public static final int COLUMN_VIDEO_ID = 2;
    public static final int COLUMN_VIDEO_KEY = 3;
    public static final int COLUMN_VIDEO_SITE = 4;
    public static final int COLUMN_VIDEO_TYPE = 5;
}
