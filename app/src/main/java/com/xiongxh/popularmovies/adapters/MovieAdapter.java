package com.xiongxh.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.fragments.MovieGridFragment;
import com.xiongxh.popularmovies.utilities.NetworkUtils;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

import butterknife.BindViews;
import butterknife.ButterKnife;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    //final String IMAGE_BASE_URL =  "https://image.tmdb.org/t/p/w500/";

    private final Context mContext;
    private Cursor mCursor;

    private MovieAdapterOnClickHandler mMovieOnClickHandler;

    private View mEmptyView;

    private static int viewHolderCount;


    public static interface MovieAdapterOnClickHandler{
        void onClick(long l, MovieViewHolder movieViewHolder);
    }

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
    //public MovieAdapter(@NonNull Context context) {
        mContext = context;
        mMovieOnClickHandler = movieAdapterOnClickHandler;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        if (viewGroup instanceof RecyclerView) {
            int layoutIdForListItem = R.layout.list_item_movie;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            view.setFocusable(true);

            MovieViewHolder viewHolder = new MovieViewHolder(view);

            return viewHolder;
        }else {
            throw new RuntimeException("Not bound to Recyclerview.");
        }
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieViewHolder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {
        Log.d(TAG, "#" + position);
        //holder.bind(position);
        if (! mCursor.moveToFirst()){
            return;
        }

        //if (mCursor == null || mCursor.getCount() == 0) {

        mCursor.moveToPosition(position);

        String movieTitle = mCursor.getString(ConstantsUtils.COLUMN_TITLE);

        //String moviePopular = mCursor.getString(ConstantsUtils.COLUMN_POP);

        String posterPath = mCursor.getString(ConstantsUtils.COLUMN_POSTER);

        //movieViewHolder.movieTitleView.setText(movieTitle);

        //movieViewHolder.mMoviePopularView.setText(moviePopular);

        String posterUrl = NetworkUtils.IMAGE_BASE_URL + posterPath;

        Picasso.with(mContext).cancelRequest(movieViewHolder.moviePosterView);
        Picasso.with(mContext).load(posterUrl).resize(375, 600).into(movieViewHolder.moviePosterView);

        movieViewHolder.moviePosterView.setAdjustViewBounds(true);

        String movieVote = mCursor.getString(ConstantsUtils.COLUMN_VOTESCORE);

        movieViewHolder.mMovieVoteView.setText(movieVote);

        for (int i=0; i<5; i++){
            movieViewHolder.mVoteStarsView.get(i).setImageResource(R.drawable.icon_star_empty_48);
        }

        if (movieVote != null && !movieVote.isEmpty()) {

            float voteScore = Float.valueOf(movieVote)/2;

            int voteScoreInt = (int) voteScore;

            for (int i=0; i<voteScoreInt; i++){

                movieViewHolder.mVoteStarsView.get(i).setImageResource(R.drawable.icon_star_filled_48);
            }

            if (Math.round(voteScore)>voteScoreInt){

                movieViewHolder.mVoteStarsView.get(voteScoreInt).setImageResource(R.drawable.icon_star_half_empty_48);
            }

        } else {

            movieViewHolder.mMovieVoteView.setVisibility(View.GONE);
        }

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCursor){
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        //final TextView movieTitleView;
        final TextView mMovieVoteView;
        //final TextView mMoviePopularView;

        // Will display which ViewHolder is displaying this data
        final ImageView moviePosterView;

        final CardView mCardView;

        @BindViews({R.id.voting_first_star, R.id.voting_second_star, R.id.voting_third_star, R.id.voting_fourth_star, R.id.voting_fifth_star})
        List<ImageView> mVoteStarsView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //movieTitleView = (TextView) itemView.findViewById(R.id.tv_movie_title);

            mMovieVoteView = (TextView) itemView.findViewById(R.id.tv_movie_vote);

            //mMoviePopularView = (TextView) itemView.findViewById(R.id.tv_movie_popular);

            // Use itemView.findViewById to get a reference to tv_view_holder_instance
            moviePosterView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);

            //ButterKnife.apply(mVoteStarsView);

            mCardView = (CardView) itemView.findViewById(R.id.card_view);

            itemView.setOnClickListener(this);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            //int columnMovieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int columnMovieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);

            long movieId = mCursor.getLong(columnMovieIdIndex);

            mMovieOnClickHandler.onClick(movieId, this);
        }

    }
}
