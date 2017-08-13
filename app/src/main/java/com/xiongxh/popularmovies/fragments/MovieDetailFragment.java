package com.xiongxh.popularmovies.fragments;


import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import com.xiongxh.popularmovies.MovieDetailActivity;
import com.xiongxh.popularmovies.MovieReviewsActivity;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.adapters.MovieAdapter;
import com.xiongxh.popularmovies.adapters.MovieTrailersAdapter;
import com.xiongxh.popularmovies.data.FetchMovieTrailersTask;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;
import com.xiongxh.popularmovies.utilities.NetworkUtils;

import static android.R.attr.data;
import static android.R.attr.logoDescription;
import static com.xiongxh.popularmovies.data.MovieContract.CONTENT_AUTHORITY;
import static com.xiongxh.popularmovies.data.MovieContract.PATH_REVIEWS;
import static com.xiongxh.popularmovies.data.MovieContract.PATH_VIDEOS;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String DEFAULT_MOVIE_URI = "URI";
    public static final String DEFAULT_REVIEW_URI = "REVIEW_URI";
    public static final String DEFAULT_TRAILER_URI = "TRAILER_URI";

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int DETAIL_LOADER_ID = 10;
    private static final int REVIEW_LOADER_ID = 20;
    private static final int VIDEO_LOADER_ID = 30;

    private Uri mMovieDetailUri;
    private Uri mMovieReviewsUri;
    private Uri mVideosUri;
    private Cursor mCursor = null;

    private String mMovieIdStr;

    private MovieTrailersAdapter mMovieTrailersAdapter;

    private ListView mReviewsListView;
    private ListView mTrailersListView;

    /**
    private ScrollView mMovieDetailLayout;
    private TextView mMovieTitleView;
    private TextView mMovieVoteView;
    private TextView mMovieReleaseDateView;
    private ImageView mPosterView;
    private ImageView mBackdropView;
    private TextView mMovieOverview;
    private Button mReviewButton;
    private Button mTrailerButton;
     */
    @BindView(R.id.movie_detail_layout) ScrollView mMovieDetailLayout;
    @BindView(R.id.tv_movie_detail_title) TextView mMovieTitleView;
    @BindView(R.id.tv_movie_average_vote) TextView mMovieVoteView;
    @BindView(R.id.tv_movie_release_date) TextView mMovieReleaseDateView;
    @BindView(R.id.iv_movie_detail_poster) ImageView mPosterView;
    @BindView(R.id.iv_movie_detail_backdrop) ImageView mBackdropView;
    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.button_read_reviews) Button mReviewButton;

    //@BindView(R.id.lv_movie_trailers) ListView mMovieTrailersView;
    @BindView(R.id.tv_trailers_empty) TextView mTrailersEmptyView;

    // @BindView(R.id.button_watch_trailers) Button mTrailerButton;

    private ListView mReviewsView;

    private MovieAdapter mMovieDetailAdapter;

    public MovieDetailFragment(){}

    public interface Callback {
        public void onItemSelected(String trailerKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.d(LOG_TAG, "Entering onCreateView method...");
        Bundle arguments = getArguments();

        if (arguments != null){
            mMovieDetailUri = arguments.getParcelable(DEFAULT_MOVIE_URI);
            Log.d(LOG_TAG, "Inside arguments != null, mMovieUri: " + mMovieDetailUri);

            mMovieIdStr = mMovieDetailUri.getPathSegments().get(1);

            Log.d(LOG_TAG, "mMovieIdStr: " + mMovieIdStr);
//
//            mMovieReviewsUri = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS + "/" + movieIdStr);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        //mMovieDetailLayout = (ScrollView) rootView.findViewById(R.id.movie_detail_layout);

        //mMovieDetailAdapter = new MovieAdapter(getActivity());

        //mMovieTitleView = (TextView) rootView.findViewById(R.id.tv_movie_detail_title);
        //mMovieVoteView = (TextView) rootView.findViewById(R.id.tv_movie_average_vote);
        //mMovieReleaseDateView = (TextView) rootView.findViewById(R.id.tv_movie_release_date);
        //mPosterView = (ImageView) rootView.findViewById(R.id.iv_movie_detail_poster);
        //mBackdropView = (ImageView) rootView.findViewById(R.id.iv_movie_detail_backdrop);
        //mMovieOverview = (TextView) rootView.findViewById(R.id.tv_movie_overview);

        //mReviewsView = (ListView) rootView.findViewById(R.id.lv_movie_reviews);

        //mReviewButton = (Button) rootView.findViewById(R.id.button_read_reviews);


        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCursor == null || !mCursor.moveToFirst()){
                    return;
                }

                String movieIdStr = mCursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);
                mMovieReviewsUri = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS + "/" + movieIdStr);

                Log.d(LOG_TAG, "mMovieReviewsUri: " + mMovieReviewsUri.toString());

                Intent intent = new Intent(getActivity(), MovieReviewsActivity.class);
                intent.putExtra(DEFAULT_REVIEW_URI, mMovieReviewsUri.toString());
                startActivity(intent);
            }
        });


        //mTrailerButton = (Button) rootView.findViewById(R.id.button_watch_trailers);
        /**
        mTrailerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mCursor == null || !mCursor.moveToFirst()){
                    return;
                }

                String movieIdStr = mCursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);
                Log.d(LOG_TAG, "movieIdStr: " + movieIdStr);

                mMovieTrailersUri = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS + "/" + movieIdStr);

                Log.d(LOG_TAG, "mMovieTrailersUri: " + mMovieTrailersUri.toString());

                Intent intent = new Intent(getActivity(), MovieTrailersActivity.class);
                intent.putExtra(DEFAULT_TRAILER_URI, mMovieTrailersUri.toString());
                startActivity(intent);
            }
        });
         */


        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //mTrailersListView = (ListView) rootView.findViewById(R.id.lv_movie_trailers);

        RecyclerView movieTrailerRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_movie_trailers);


        movieTrailerRecycleView.setLayoutManager(trailerLayoutManager);
        Log.d(LOG_TAG, "Before trailer callback");



        mMovieTrailersAdapter = new MovieTrailersAdapter(getActivity(), new MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler() {
            @Override
            public void onClick(String trailerKey, MovieTrailersAdapter.TrailerViewHolder trailerViewHolder) {
                mPosition = trailerViewHolder.getAdapterPosition();

                ((Callback) getActivity()).onItemSelected(trailerKey);
            }
        });

        Log.d(LOG_TAG, "Trailer adapter count: " + mMovieTrailersAdapter.getItemCount());
        Log.d(LOG_TAG, "After trailer callback");

        movieTrailerRecycleView.setAdapter(mMovieTrailersAdapter);

        Log.d(LOG_TAG, "Trailer adapter count: " + mMovieTrailersAdapter.getItemCount());
        Log.d(LOG_TAG, "Exiting onCreated method.");

        //mMovieTrailersAdapter = new MovieTrailersAdapter(getActivity(), null, 0);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Entering onActivityCreated...");
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        Log.d(LOG_TAG, "After loading detail...");
        //getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
        //Log.d(LOG_TAG, "After loading reviews...");
        //getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
        //Log.d(LOG_TAG, "After loading trailers...");
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovieDetailView(Cursor cursor){

        String movieTitle = cursor.getString(ConstantsUtils.COLUMN_TITLE);
        mMovieTitleView.setText(movieTitle);
        Log.d(LOG_TAG, "Inside updateDetailView, movieTile: " + movieTitle);

        String movieVote = cursor.getString(ConstantsUtils.COLUMN_VOTESCORE) + getString(R.string.average_rating_label);
        mMovieVoteView.setText(movieVote);

        String movieReleaseDate =  getString(R.string.release_date) + cursor.getString(ConstantsUtils.COLUMN_DATE);
        mMovieReleaseDateView.setText(movieReleaseDate);

        String posterPath = cursor.getString(ConstantsUtils.COLUMN_POSTER);

        Log.d(LOG_TAG, "Inside updateDetailView, posterPath: " + posterPath);

        String posterUrl = NetworkUtils.IMAGE_BASE_URL + posterPath;

        Picasso.with(getActivity()).load(posterUrl).into(mPosterView);
        mPosterView.setAdjustViewBounds(true);

        String backdropPath = cursor.getString(ConstantsUtils.COLUMN_BACKDROP);

        Log.d(LOG_TAG, "Inside updateDetailView, backdropPath: " + backdropPath);

        String backdropUrl = NetworkUtils.IMAGE_BASE_URL + backdropPath;

        Picasso.with(getActivity()).load(backdropUrl).into(mBackdropView);
        mBackdropView.setAdjustViewBounds(true);

        String movieOverview = cursor.getString(ConstantsUtils.COLUMN_OVERVIEW);
        mMovieOverview.setText(movieOverview);

        String currMovieIdStr = cursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);

        getTrailers(currMovieIdStr);

    }

    private void getTrailers(String currentMovieIdStr){
        LoaderManager loaderManager = getLoaderManager();

        if (mMovieIdStr == null || !mMovieIdStr.equals(currentMovieIdStr)){
            mVideosUri = MovieContract.VideosEntry.buildVideosUriByMovieId(mMovieIdStr);

            FetchMovieTrailersTask fetchMovieTrailersTask = new FetchMovieTrailersTask(getActivity());
            fetchMovieTrailersTask.execute(mMovieIdStr);

            if (loaderManager.getLoader(VIDEO_LOADER_ID) != null){
                loaderManager.restartLoader(VIDEO_LOADER_ID, null, this);

                return;
            }
        }

        loaderManager.initLoader(VIDEO_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs){
        Log.d(LOG_TAG, "Entering onCreateLoader(), mMovieUri: " + mMovieDetailUri);

        switch (loaderId){
            case DETAIL_LOADER_ID:{
                return new CursorLoader(
                        getActivity(),
                        mMovieDetailUri,
                        ConstantsUtils.MOVIE_COLUMNS,
                        null,
                        null,
                        null
                );
            }

            case VIDEO_LOADER_ID: {
                if (mVideosUri != null){
                    return new CursorLoader(
                            getActivity(),
                            mVideosUri,
                            ConstantsUtils.MOVIE_VIDEO_COLUMNS,
                            null,
                            null,
                            null);
                }
            }

            default:{
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        /**
        boolean cursorHasVilidData = false;

        if (data != null && data.moveToFirst()){
            mCursor = data;
            cursorHasVilidData =true;
            Log.d(LOG_TAG, "Inside onLoadFinished, has valid cursor");
        }

        if (!cursorHasVilidData){
            Log.d(LOG_TAG, "Inside onLoadFinished, has not valid cursor");
            return;
        }

        updateMovieDetailView(data);

        mMovieTrailersAdapter.swapCursor(data);
         */

        //mMovieDetailAdapter.swapCursor(data);
         /**
        if (loader.getId() == DETAIL_LOADER_ID && data != null && data.moveToFirst()){
            updateMovieDetailView(data);
        } else if (loader.getId() == VIDEO_LOADER_ID && data != null && data.moveToFirst()){
            mMovieTrailersAdapter.swapCursor(data);
        }*/

        int currLoader = loader.getId();
        if (currLoader == DETAIL_LOADER_ID && data.moveToFirst()){
            updateMovieDetailView(data);
        } else if (currLoader == VIDEO_LOADER_ID){
            if (data !=null && data.moveToFirst()){
                mTrailersEmptyView.setVisibility(View.GONE);
            } else {
                mTrailersEmptyView.setVisibility(View.VISIBLE);
            }
            mMovieTrailersAdapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        if (loader.getId() == VIDEO_LOADER_ID){
            mMovieTrailersAdapter.swapCursor(null);
        }
    }

    @Override
    public void onResume(){

        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }
}
