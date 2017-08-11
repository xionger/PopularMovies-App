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

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.adapters.MovieReviewsAdapter;
import com.xiongxh.popularmovies.adapters.MovieTrailersAdapter;
import com.xiongxh.popularmovies.data.FetchMovieReviewsTask;
import com.xiongxh.popularmovies.data.FetchMovieTrailersTask;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;


public class MovieTrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = MovieTrailersFragment.class.getSimpleName();

    public static final String DEFAULT_TRAILER_URI = "TRAILER_URI";

    private Uri mMovieTrailerUri;
    private MovieTrailersAdapter mMovieTrailersAdapter;

    private static final int TRAILERS_LOADER = 30;

    public MovieTrailersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.d(LOG_TAG, "Entering onCreateView");
        Bundle args = getArguments();

        if (args != null) {
            Log.d(LOG_TAG, "Entering args not null block");
            mMovieTrailerUri = args.getParcelable(DEFAULT_TRAILER_URI);
            Log.d(LOG_TAG, "mMovieTrailerUri: " + mMovieTrailerUri.toString());

//            if (mMovieReviewsUri != null) {
//                Log.d(LOG_TAG, "Entering mMovieReviewsUri not null block");
            String movieId = mMovieTrailerUri.getLastPathSegment();

            Log.d(LOG_TAG, "movieId: " + movieId);

            if (savedInstanceState == null) {
                FetchMovieTrailersTask fetchMovieTrailersTask = new FetchMovieTrailersTask(getActivity());
                Log.d(LOG_TAG, "Begin to fetch movie trailers data. movieId: " + movieId);
                fetchMovieTrailersTask.execute(movieId);
                Log.d(LOG_TAG, "After executing fetchMovieTrailersTask.");
            }
        }

//        }

        View rootView = inflater.inflate(R.layout.fragment_movie_trailers, container, false);

        //ListView movieReviewsListView = (ListView) rootView.findViewById(R.id.lv_movie_reviews);
        RecyclerView movieTrailerRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_movie_trailers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        movieTrailerRecycleView.setLayoutManager(layoutManager);

        Log.d(LOG_TAG, "Before creating new MovieTrailersAdapter.");

        mMovieTrailersAdapter = new MovieTrailersAdapter(getActivity());

        Log.d(LOG_TAG, "Have created new MovieTrailersAdapter.");

        movieTrailerRecycleView.setAdapter(mMovieTrailersAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Entering onActivityCreated");
        getLoaderManager().initLoader(TRAILERS_LOADER, null, this);
        Log.d(LOG_TAG, "After loading reviews...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Entering onCreateLoader");
        if (id == TRAILERS_LOADER && mMovieTrailerUri != null){
            return new CursorLoader(
                    getActivity(),
                    mMovieTrailerUri,
                    ConstantsUtils.MOVIE_VIDEO_COLUMNS,
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
        if (loader.getId() == TRAILERS_LOADER){
            if (data != null && data.moveToFirst()){
                Log.d(LOG_TAG, "Inside onLoadFinished, data not null block.");
                mMovieTrailersAdapter.swapCursor(data);
            }
        }
        Log.d(LOG_TAG, "Exit onLoadFinished.");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Entering onLoaderReset.");
    }
}
