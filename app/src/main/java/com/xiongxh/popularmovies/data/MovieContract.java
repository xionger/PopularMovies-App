package com.xiongxh.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.xiongxh.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public  static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_LANGUAGE = "language";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_BACKDROP = "backdrop";

        public static final String COLUMN_POP = "popularity";

        public static final String COLUMN_VOTESCORE = "vote_score";

        public static final String COLUMN_VOTENUM = "vote_number";



        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";

        public static Uri buildReviewUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }

    public static final class VideosEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();

        public final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;

        public static final String TABLE_NAME = "videos";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_VIDEO_KEY = "key";
        public static final String COLUMN_VIDEO_SITE = "site";
        public static final String COLUMN_VIDEO_TYPE = "type";

        public static Uri buildVideoUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }
}
