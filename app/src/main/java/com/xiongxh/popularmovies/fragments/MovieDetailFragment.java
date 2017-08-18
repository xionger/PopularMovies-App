package com.xiongxh.popularmovies.fragments;


import android.content.ContentValues;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import com.xiongxh.popularmovies.MovieDetailActivity;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.adapters.MovieAdapter;
import com.xiongxh.popularmovies.adapters.MovieReviewsAdapter;
import com.xiongxh.popularmovies.adapters.MovieTrailersAdapter;
import com.xiongxh.popularmovies.data.FetchMovieReviewsTask;
import com.xiongxh.popularmovies.data.FetchMovieTrailersTask;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.data.MovieContract.MovieEntry;
import com.xiongxh.popularmovies.data.MoviePreferences;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;
import com.xiongxh.popularmovies.utilities.NetworkUtils;

import static android.R.attr.actionModeCopyDrawable;
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

    private int mTrailerPosition = RecyclerView.NO_POSITION;

    private static final int DETAIL_LOADER_ID = 10;
    private static final int REVIEW_LOADER_ID = 20;
    private static final int VIDEO_LOADER_ID = 30;
    private static final int FAVORITE_LOADER_ID = 40;

    private Uri mMovieDetailUri;
    private Uri mReviewsUri;
    private Uri mVideosUri;
    private Uri mMovieUri;
    private Cursor mCursor = null;

    private String mMovieDetailIdStr;

    private MovieReviewsAdapter mMovieReviewsAdapter;
    private MovieTrailersAdapter mMovieTrailersAdapter;

    private ListView mReviewsListView;
    private ListView mTrailersListView;

    //private Button mTrailerButton;

    @BindView(R.id.movie_detail_layout) ScrollView mMovieDetailLayout;
    @BindView(R.id.tv_movie_detail_title) TextView mMovieTitleView;
    @BindView(R.id.tv_movie_average_vote) TextView mMovieVoteView;
    @BindView(R.id.tv_movie_release_date) TextView mMovieReleaseDateView;
    @BindView(R.id.iv_movie_detail_poster) ImageView mPosterView;
    @BindView(R.id.iv_movie_detail_backdrop) ImageView mBackdropView;
    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.btn_favorite) ImageButton mFavoriteButton;

    //@BindView(R.id.button_read_reviews) Button mReviewButton;

    //@BindView(R.id.lv_movie_trailers) ListView mMovieTrailersView;
    @BindView(R.id.tv_trailers_empty) TextView mTrailersEmptyView;
    @BindView(R.id.tv_reviews_empty) TextView mReviewsEmptyView;

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

            mMovieDetailIdStr = mMovieDetailUri.getPathSegments().get(1);

            Log.d(LOG_TAG, "mMovieIdStr: " + mMovieDetailIdStr);
//
//            mMovieReviewsUri = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS + "/" + movieIdStr);
        }

        if (savedInstanceState != null){
            String movieIdString = savedInstanceState.getString("movie_id");
            Log.d(LOG_TAG, "original movie Id: " + movieIdString);

        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        mFavoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Toast.makeText(getActivity(), "favorite button is clicked", Toast.LENGTH_SHORT).show();

                if (mCursor == null || !mCursor.moveToFirst()){
                    Log.d(LOG_TAG, "Favorite click null");
                    return;
                }

                String movieIdStr = mCursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);
                String movieTitle = mCursor.getString(ConstantsUtils.COLUMN_TITLE);
                String favoriteTag = mCursor.getString(ConstantsUtils.COLUMN_FAVORITE);

                //mMovieUri = MovieEntry.buildMovieUribyIdStr(movieIdStr);

                Log.d(LOG_TAG, "Before Click Favorite Button... favoriteTag: " + mCursor.getString(ConstantsUtils.COLUMN_FAVORITE));
                Log.d(LOG_TAG, "mMovieDetailUri: " + mMovieDetailUri);

                ContentValues movieValues = getContentValues(mCursor);

                 int updatedRows;

                updatedRows = getContext().getContentResolver().update(mMovieDetailUri, movieValues, null, null);
                //getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, movieValues);

                Log.d(LOG_TAG, "Favorite update rows: " + updatedRows);

                if (updatedRows == 1){
                    MoviePreferences.updateFavoriteMessage(favoriteTag, movieTitle, getContext());
                    Log.d(LOG_TAG, "Before onFavorite()... favoriteTag: " + mCursor.getString(ConstantsUtils.COLUMN_FAVORITE));
                    onFavorite();

                    Log.d(LOG_TAG, "After onFavorite()... favoriteTag: " + mCursor.getString(ConstantsUtils.COLUMN_FAVORITE));
                }

            }
        });

        //mReviewsView = (ListView) rootView.findViewById(R.id.lv_movie_reviews);

        //mReviewButton = (Button) rootView.findViewById(R.id.button_read_reviews);


        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView movieReviewRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_movie_reviews);
        movieReviewRecycleView.setLayoutManager(reviewLayoutManager);

        mMovieReviewsAdapter = new MovieReviewsAdapter(getContext());

        movieReviewRecycleView.setAdapter(mMovieReviewsAdapter);

        //mTrailerButton = (Button) rootView.findViewById(R.id.button_watch_trailers);


        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView movieTrailerRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_movie_trailers);
        movieTrailerRecycleView.setLayoutManager(trailerLayoutManager);

        //Log.d(LOG_TAG, "Before trailer callback");

        mMovieTrailersAdapter = new MovieTrailersAdapter(getActivity(), new MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler() {
            @Override
            public void onClick(String trailerKey, MovieTrailersAdapter.TrailerViewHolder trailerViewHolder) {
                mTrailerPosition = trailerViewHolder.getAdapterPosition();

                ((Callback) getActivity()).onItemSelected(trailerKey);
            }
        });

        //Log.d(LOG_TAG, "Trailer adapter count: " + mMovieTrailersAdapter.getItemCount());
        //Log.d(LOG_TAG, "After trailer callback");

        movieTrailerRecycleView.setAdapter(mMovieTrailersAdapter);

        //Log.d(LOG_TAG, "Trailer adapter count: " + mMovieTrailersAdapter.getItemCount());
        //Log.d(LOG_TAG, "Exiting onCreated method.");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Entering onActivityCreated...");
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        Log.d(LOG_TAG, "After loading detail...");
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

        String favoriteTag = cursor.getString(ConstantsUtils.COLUMN_FAVORITE);

        Log.d(LOG_TAG, "Inside updateDetailView, before updateFavorite label... favoriteTag: " + favoriteTag);

        updateFavoriteLabel(favoriteTag);

        String currMovieIdStr = cursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);

        Log.d(LOG_TAG, "Inside updateDetailView, movie id: " + currMovieIdStr);

        //String currMovieIdStr = mMovieIdStr;

        getReviews(currMovieIdStr);
        getTrailers(currMovieIdStr);
    }

    private void getReviews(String currentMovieIdStr){
        Log.d(LOG_TAG, "Before loading reviews... MovieId: " + currentMovieIdStr);
        LoaderManager loaderManager = getLoaderManager();

        if (mMovieDetailIdStr == null || !mMovieDetailIdStr.equals(currentMovieIdStr)){
            mReviewsUri = MovieContract.ReviewsEntry.buildReviewUriByMovieId(currentMovieIdStr);
            Log.d(LOG_TAG, "Inside getReviews()..., mReviewsUri: " + mReviewsUri);

            FetchMovieReviewsTask fetchMovieReviewsTask = new FetchMovieReviewsTask(getActivity());
            fetchMovieReviewsTask.execute(currentMovieIdStr);

            if (loaderManager.getLoader(REVIEW_LOADER_ID) != null){
                loaderManager.restartLoader(REVIEW_LOADER_ID, null, this);

                return;
            }
        }

        loaderManager.initLoader(REVIEW_LOADER_ID, null, this);

        Log.d(LOG_TAG, "After loading reviews... MovieId: " + currentMovieIdStr);
    }

    private void getTrailers(String currentMovieIdStr){

        Log.d(LOG_TAG, "Before loading videos... MovieId: " + currentMovieIdStr);

        LoaderManager loaderManager = getLoaderManager();

        if (mMovieDetailIdStr == null || !mMovieDetailIdStr.equals(currentMovieIdStr)){
            mVideosUri = MovieContract.VideosEntry.buildVideosUriByMovieId(currentMovieIdStr);

            FetchMovieTrailersTask fetchMovieTrailersTask = new FetchMovieTrailersTask(getActivity());
            fetchMovieTrailersTask.execute(currentMovieIdStr);

            if (loaderManager.getLoader(VIDEO_LOADER_ID) != null){
                loaderManager.restartLoader(VIDEO_LOADER_ID, null, this);

                return;
            }
        }

        loaderManager.initLoader(VIDEO_LOADER_ID, null, this);
        Log.d(LOG_TAG, "After loading videos... MovieId: " + currentMovieIdStr);
    }

    private ContentValues getContentValues(Cursor cursor){

        String movieIdStr = cursor.getString(ConstantsUtils.COLUMN_MOVIE_ID);
        String movieTitle = cursor.getString(ConstantsUtils.COLUMN_TITLE);
        String movieOverview = cursor.getString(ConstantsUtils.COLUMN_OVERVIEW);
        String movieLanguage = cursor.getString(ConstantsUtils.COLUMN_LANGUAGE);
        String posterPath = cursor.getString(ConstantsUtils.COLUMN_POSTER);
        String backdropPath = cursor.getString(ConstantsUtils.COLUMN_BACKDROP);
        String releaseTime = cursor.getString(ConstantsUtils.COLUMN_DATE);
        String voteNumber = cursor.getString(ConstantsUtils.COLUMN_VOTENUM);
        String voteScore = cursor.getString(ConstantsUtils.COLUMN_VOTESCORE);
        String moviePopularity = cursor.getString(ConstantsUtils.COLUMN_POP);
        String favoriteTag = cursor.getString(ConstantsUtils.COLUMN_FAVORITE);

        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieEntry.COLUMN_TITLE, movieTitle);
        movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieIdStr);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, movieOverview);
        movieValues.put(MovieEntry.COLUMN_LANGUAGE, movieLanguage);
        movieValues.put(MovieEntry.COLUMN_POSTER, posterPath);
        movieValues.put(MovieEntry.COLUMN_BACKDROP, backdropPath);
        movieValues.put(MovieEntry.COLUMN_DATE, releaseTime);
        movieValues.put(MovieEntry.COLUMN_VOTENUM, voteNumber);
        movieValues.put(MovieEntry.COLUMN_VOTESCORE, voteScore);
        movieValues.put(MovieEntry.COLUMN_POP, moviePopularity);

        if (favoriteTag.equals(ConstantsUtils.FAVORITE_TAG)){
            Log.d(LOG_TAG, "Change to unfavorite");
            movieValues.put(MovieEntry.COLUMN_FAVORITE, ConstantsUtils.UNFAVORITE_TAG);

        } else {
            Log.d(LOG_TAG, "Change to favorite");
            movieValues.put(MovieEntry.COLUMN_FAVORITE, ConstantsUtils.FAVORITE_TAG);
        }

        return movieValues;
    }

    private void onFavorite(){
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    private void updateFavoriteLabel(String favoriteTag){
        if (favoriteTag != null && favoriteTag.equals(ConstantsUtils.FAVORITE_TAG)){
            mFavoriteButton.setImageResource(R.drawable.icon_heart_red_50);
        } else {
            mFavoriteButton.setImageResource(R.drawable.icon_heart_gray_50);
        }
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

            case REVIEW_LOADER_ID: {
                if (mReviewsUri != null){
                    return new CursorLoader(
                            getActivity(),
                            mReviewsUri,
                            ConstantsUtils.MOVIE_REVIEW_COLUMNS,
                            null,
                            null,
                            null);
                }
            }

            case VIDEO_LOADER_ID: {
                if (mVideosUri != null) {
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

        Log.d(LOG_TAG, "Entering onLoadFinished...");

//        if (data != null && data.moveToFirst()){
//            mCursor = data;
//        }

        int currLoader = loader.getId();

        if (currLoader == DETAIL_LOADER_ID && data.moveToFirst()){
            mCursor = data;
            updateMovieDetailView(data);

        } else if (currLoader == REVIEW_LOADER_ID){
            if (data != null && data.moveToFirst()){
                mReviewsEmptyView.setVisibility(View.GONE);
            } else {
                mReviewsEmptyView.setVisibility(View.VISIBLE);
            }
            Log.d(LOG_TAG, "Inside onLoadFinished... reviews");
            mMovieReviewsAdapter.swapCursor(data);

        } else if (currLoader == VIDEO_LOADER_ID){
            if (data !=null && data.moveToFirst()){
                mTrailersEmptyView.setVisibility(View.GONE);
            } else {
                mTrailersEmptyView.setVisibility(View.VISIBLE);
            }

            Log.d(LOG_TAG, "Inside onLoadFinished... videos");
            mMovieTrailersAdapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        if (loader.getId() == VIDEO_LOADER_ID){
            mMovieTrailersAdapter.swapCursor(null);
        }

        if (loader.getId() == REVIEW_LOADER_ID){
            mMovieReviewsAdapter.swapCursor(null);
        }
    }

    @Override
    public void onResume(){

        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }
}
