package com.xiongxh.popularmovies.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.adapters.MovieReviewsAdapter;
import com.xiongxh.popularmovies.data.FetchMovieReviewsTask;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;
import com.xiongxh.popularmovies.utilities.NetworkUtils;

import static com.xiongxh.popularmovies.R.id.container;

public class MovieReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = MovieReviewsFragment.class.getSimpleName();

    public static final String DEFAULT_MOVIE_URI = "URI";
    public static final String DEFAULT_REVIEW_URI = "REVIEW_URI";

    private MovieReviewsAdapter mMovieReviewsAdapter;
    private Uri mMovieReviewsUri;
    private static final int REVIEWS_LOADER = 20;

    public MovieReviewsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.d(LOG_TAG, "Entering onCreateView");
        Bundle args = getArguments();

        if (args != null) {
            Log.d(LOG_TAG, "Entering args not null block");
            mMovieReviewsUri = args.getParcelable(DEFAULT_REVIEW_URI);
            //Log.d(LOG_TAG, "mMovieReviewsUri: " + mMovieReviewsUri.toString());

//            if (mMovieReviewsUri != null) {
//                Log.d(LOG_TAG, "Entering mMovieReviewsUri not null block");
                String movieId = mMovieReviewsUri.getLastPathSegment();

                Log.d(LOG_TAG, "movieId: " + movieId);

                if (savedInstanceState == null) {
                    FetchMovieReviewsTask fetchMovieReviewsTask = new FetchMovieReviewsTask(getActivity());
                    Log.d(LOG_TAG, "Begin to fetch movie reviews data. movieId: " + movieId);
                    fetchMovieReviewsTask.execute(movieId);
                    Log.d(LOG_TAG, "After executing fetchMovieReviewsTask.");
                }
            }

//        }

        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        //ListView movieReviewsListView = (ListView) rootView.findViewById(R.id.lv_movie_reviews);
        RecyclerView movieReviewRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_movie_reviews);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        movieReviewRecycleView.setLayoutManager(layoutManager);

        Log.d(LOG_TAG, "Before creating new MovieReciewsAdapter.");

        mMovieReviewsAdapter = new MovieReviewsAdapter(getActivity());

        Log.d(LOG_TAG, "Have created new MovieReciewsAdapter.");

        movieReviewRecycleView.setAdapter(mMovieReviewsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Entering onActivityCreated");
        getLoaderManager().initLoader(REVIEWS_LOADER, null, this);
        Log.d(LOG_TAG, "After loading reviews...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Entering onCreateLoader");
        if (id == REVIEWS_LOADER && mMovieReviewsUri != null){
            return new CursorLoader(
                    getActivity(),
                    mMovieReviewsUri,
                    ConstantsUtils.MOVIE_REVIEW_COLUMNS,
                    null,
                    null,
                    null);
        }
        Log.d(LOG_TAG, "Exit onCreateLoader");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Entering onLoadFinished.");
        if (loader.getId() == REVIEWS_LOADER){
            if (data != null && data.moveToFirst()){
                Log.d(LOG_TAG, "Inside onLoadFinished, data not null block.");
                mMovieReviewsAdapter.swapCursor(data);
            }
        }
        Log.d(LOG_TAG, "Exit onLoadFinished.");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Entering onLoaderReset.");

    }
}
