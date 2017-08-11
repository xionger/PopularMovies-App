package com.xiongxh.popularmovies;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xiongxh.popularmovies.fragments.MovieDetailFragment;
import com.xiongxh.popularmovies.fragments.MovieReviewsFragment;

public class MovieReviewsActivity extends AppCompatActivity{
    private final String LOG_TAG = MovieReviewsActivity.class.getSimpleName();

    private Uri mMovieReviewUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);

        if(savedInstanceState == null){
            Bundle args = new Bundle();
            //mMovieReviewUri = getIntent().getData();
            mMovieReviewUri = Uri.parse(getIntent().getStringExtra(MovieReviewsFragment.DEFAULT_REVIEW_URI));

            Log.d(LOG_TAG, "mMovieReviewUrl[0]: " + mMovieReviewUri.getPathSegments().get(0));

            Log.d(LOG_TAG, "mMovieReviewUrl[1]: " + mMovieReviewUri.getPathSegments().get(1));

            args.putParcelable(MovieReviewsFragment.DEFAULT_REVIEW_URI, mMovieReviewUri);

            Log.d(LOG_TAG, "Inside onCreate() method if block..., movieDetailUri: " + mMovieReviewUri);


            MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
            movieReviewsFragment.setArguments(args);

            if (mMovieReviewUri != null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_reviews_container, movieReviewsFragment)
                        .commit();
            }
        }
    }
}
