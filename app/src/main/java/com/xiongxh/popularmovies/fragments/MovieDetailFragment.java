package com.xiongxh.popularmovies.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.xiongxh.popularmovies.MovieTrailersActivity;
import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.adapters.MovieAdapter;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;
import com.xiongxh.popularmovies.utilities.NetworkUtils;

import static com.xiongxh.popularmovies.data.MovieContract.CONTENT_AUTHORITY;
import static com.xiongxh.popularmovies.data.MovieContract.PATH_REVIEWS;
import static com.xiongxh.popularmovies.data.MovieContract.PATH_VIDEOS;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String DEFAULT_MOVIE_URI = "URI";
    public static final String DEFAULT_REVIEW_URI = "REVIEW_URI";
    public static final String DEFAULT_TRAILER_URI = "TRAILER_URI";

    private static final int DETAIL_LOADER_ID = 10;
    private static final int REVIEW_LOADER_ID = 20;

    private Uri mMovieDetailUri;
    private Uri mMovieReviewsUri;
    private Uri mMovieTrailersUri;
    private Cursor mCursor = null;

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
    @BindView(R.id.button_watch_trailers) Button mTrailerButton;

    private ListView mReviewsView;

    private MovieAdapter mMovieDetailAdapter;

    public MovieDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.d(LOG_TAG, "Entering onCreateView method...");
        Bundle arguments = getArguments();

        if (arguments != null){
            mMovieDetailUri = arguments.getParcelable(DEFAULT_MOVIE_URI);
            Log.d(LOG_TAG, "Inside arguments != null, mMovieUri: " + mMovieDetailUri);

//            String movieIdStr = mMovieDetailUri.getPathSegments().get(1);
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

        Log.d(LOG_TAG, "Exiting onCreated method.");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
//        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
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

            default:{
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
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

        //mMovieDetailAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }

    @Override
    public void onResume(){

        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }
}
