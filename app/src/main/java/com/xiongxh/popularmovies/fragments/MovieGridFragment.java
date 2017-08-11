package com.xiongxh.popularmovies.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.SettingsActivity;
import com.xiongxh.popularmovies.adapters.MovieAdapter;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;
import com.xiongxh.popularmovies.sync.MovieSyncUtils;
import com.xiongxh.popularmovies.utilities.FakeMovieUtils;

import java.util.Vector;

public class MovieGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieGridFragment.class.getName();
    private static final int NUM_LIST_ITEMS = 100;
    private static final int LOADER_ID = 0;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mMoviesRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingIndicator;

    public MovieGridFragment() {
    }

    public interface Callback {
        public void onItemSelected(Uri movieUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "MoviesGridFragment starts!");
        super.onActivityCreated(savedInstanceState);
        //FakeMovieUtils.insertFakeData(getActivity());
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(LOG_TAG, "MoviesGridFragment onCreateView() called");

        //super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        mMoviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);

        View emptyView = rootView.findViewById(R.id.view_empty);

        /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */

        Log.d(LOG_TAG, "Will generate new MovieAdapter.");
        mMovieAdapter = new MovieAdapter(getActivity(), new MovieAdapter.MovieAdapterOnClickHandler(){
            @Override
            public void onClick(long movieId, MovieAdapter.MovieViewHolder movieViewHolder){
                mPosition = movieViewHolder.getAdapterPosition();

                Uri movieUri = MovieContract.MovieEntry.buildMovieUri(movieId);

                ((Callback) getActivity()).onItemSelected(movieUri);
            }
        });
        //mMovieAdapter = new MovieAdapter(getActivity(), this);

        //mMovieAdapter = new MovieAdapter(getActivity());

        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        Log.d(LOG_TAG, "onCreateView returning.");

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader starts with id: " + id);
        switch (id){
            case LOADER_ID: {
                //String sortOrder = MovieContract.MovieEntry.COLUMN_POP + " DESC";

                return new CursorLoader(
                        getActivity(),
                        MovieContract.MovieEntry.CONTENT_URI,
                        ConstantsUtils.MOVIE_COLUMNS,
                        null,
                        null,
                        //sortOrder);
                        null);
            }
            default:{
                throw  new RuntimeException("Loader not implemented: " + id);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(LOG_TAG, "Entering onLoadFinished...");

        if (data.moveToFirst()) {
            Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(data.getCount());

            do {
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(data, contentValues);
                contentValuesVector.add(contentValues);
            } while (data.moveToNext());

            mMovieAdapter.swapCursor(data);

            //updateEmptyView();

        }

        Log.d(LOG_TAG, "onLoadFinished: count is: " + mMovieAdapter.getItemCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d(LOG_TAG, "onLoaderReset");
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mMovieAdapter.getItemCount() == 0 || SettingsFragment.mPreferenceChanged){
            MovieSyncUtils.startImmediateSync(getActivity());
        }

    }
}
