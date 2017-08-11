package com.xiongxh.popularmovies.data;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.xiongxh.popularmovies.sync.MovieSyncTask;

public class FetchMovieReviewsTask extends AsyncTask<String, Void, Void>{
    private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

    private final Context mContext;
    private long mMovieId;

    public FetchMovieReviewsTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        mMovieId = Long.valueOf(params[0]);
        Log.d(LOG_TAG, "Inside doInBackground..., mMovieId: " + mMovieId);

        MovieSyncTask.synMovieReview(mContext, mMovieId);

        return null;
    }
}
