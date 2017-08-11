package com.xiongxh.popularmovies;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xiongxh.popularmovies.fragments.MovieDetailFragment;
import com.xiongxh.popularmovies.fragments.MovieReviewsFragment;

import static com.xiongxh.popularmovies.data.MovieContract.CONTENT_AUTHORITY;
import static com.xiongxh.popularmovies.data.MovieContract.PATH_REVIEWS;

public class MovieDetailActivity extends AppCompatActivity{

    public static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private Uri mMovieDetailUri = null;
    private Uri mMovieReviewUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Entering onCreate() method...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null){
            Bundle argsDetail = new Bundle();
            mMovieDetailUri = getIntent().getData();

            //Log.d(LOG_TAG, "mMovieDetailUrl[0]: " + mMovieDetailUri.getPathSegments().get(0));

            //Log.d(LOG_TAG, "mMovieDetailUrl[1]: " + mMovieDetailUri.getPathSegments().get(1));

            argsDetail.putParcelable(MovieDetailFragment.DEFAULT_MOVIE_URI, mMovieDetailUri);

//            args.putParcelable(MovieDetailFragment.DEFAULT_MOVIE_URI,
//                    getIntent().getParcelableExtra(MovieDetailFragment.DEFAULT_MOVIE_URI));

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(argsDetail);

            Log.d(LOG_TAG, "Inside onCreate() method if block..., movieDetailUri: " + mMovieDetailUri);

            if (mMovieDetailUri != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, movieDetailFragment)
                        .commit();
            }

        }
    }


}
