package com.xiongxh.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xiongxh.popularmovies.fragments.MovieReviewsFragment;
import com.xiongxh.popularmovies.fragments.MovieTrailersFragment;


public class MovieTrailersActivity extends AppCompatActivity {
    private final String LOG_TAG = MovieTrailersActivity.class.getSimpleName();

    private Uri mMovieTrailerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailers);

        if(savedInstanceState == null){
            Bundle args = new Bundle();
            //mMovieReviewUri = getIntent().getData();
            mMovieTrailerUri = Uri.parse(getIntent().getStringExtra(MovieTrailersFragment.DEFAULT_TRAILER_URI));

            Log.d(LOG_TAG, "mMovieTrailerUrl[0]: " + mMovieTrailerUri.getPathSegments().get(0));

            Log.d(LOG_TAG, "mMovieTrailerUrl[1]: " + mMovieTrailerUri.getPathSegments().get(1));

            args.putParcelable(MovieTrailersFragment.DEFAULT_TRAILER_URI, mMovieTrailerUri);

            Log.d(LOG_TAG, "Inside onCreate() method if block..., movieTrailerUri: " + mMovieTrailerUri);


            MovieTrailersFragment movieTrailersFragment = new MovieTrailersFragment();
            movieTrailersFragment.setArguments(args);

            if (mMovieTrailerUri != null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_trailers_container, movieTrailersFragment)
                        .commit();
            }
        }
    }
}
