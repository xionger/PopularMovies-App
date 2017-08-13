package com.xiongxh.popularmovies.adapters;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

import org.w3c.dom.Text;

import static android.R.attr.start;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {
    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    public static final String BASE_YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_THUMBNAIL_SUFFIX = "/0.jpg";


    private MovieTrailerAdapterOnClickHandler mMovieTrailerOnClickHandler;

    public static interface MovieTrailerAdapterOnClickHandler{
        void onClick(String s, TrailerViewHolder trailerViewHolder);
    }

    public MovieTrailersAdapter(Context context, MovieTrailerAdapterOnClickHandler trailerOnClickHandaler){
        mContext = context;
        mMovieTrailerOnClickHandler = trailerOnClickHandaler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        if (viewGroup instanceof RecyclerView) {
            int layoutIdForListItem = R.layout.list_item_trailer;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            view.setFocusable(true);

            TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);

            return trailerViewHolder;

        }else {
            throw new RuntimeException("Not bound to Recyclerview.");
        }

    }

    @Override
    public void onBindViewHolder(TrailerViewHolder trailerHolder, int position) {
        Log.d(TAG, "#" + position);

        final Context context = trailerHolder.mView.getContext();

        if (! mCursor.moveToFirst()){
            return;
        }

        mCursor.moveToPosition(position);

        String trailerId = mCursor.getString(ConstantsUtils.COLUMN_VIDEO_ID);
        trailerHolder.mTrailerIdView.setText(trailerId);

        String trailerKey = mCursor.getString(ConstantsUtils.COLUMN_VIDEO_KEY);
        trailerHolder.mTrailerKeyView.setText(trailerKey);

        String trailerType = mCursor.getString(ConstantsUtils.COLUMN_VIDEO_TYPE);
        trailerHolder.mTrailerTypeView.setText(trailerType);

        String thumbnailUrl = BASE_YOUTUBE_THUMBNAIL_URL + trailerKey + YOUTUBE_THUMBNAIL_SUFFIX;
        Log.d(TAG, "trailer youtube thumbnail url: " + thumbnailUrl);

        Picasso.with(context)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(trailerHolder.mTrailerThumbnailView);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        Log.d(TAG, "Trailer numbers: " + mCursor.getCount());
        return mCursor.getCount();
    }


    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
        Log.d(TAG, "Exiting swapCursor...");
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;

        private TextView mTrailerIdView;
        private TextView mTrailerKeyView;
        private TextView mTrailerTypeView;
        private ImageView mTrailerThumbnailView;

        public TrailerViewHolder(View itemView){
            super(itemView);

            mTrailerIdView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_id);
            mTrailerKeyView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_key);
            mTrailerTypeView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_type);
            mTrailerThumbnailView = (ImageView) itemView.findViewById(R.id.iv_movie_trailer_thum);

            mView = itemView;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            if (mCursor != null){
                //int columnTrailerIndex = mCursor.getColumnIndex(MovieContract.VideosEntry._ID);
                //long trailerId = mCursor.getLong(columnTrailerIndex);

                //mMovieTrailerOnClickHandler.onClick(trailerId, this);

                String trailerKey = mCursor.getString(ConstantsUtils.COLUMN_VIDEO_KEY);

                mMovieTrailerOnClickHandler.onClick(trailerKey, this);

            }

        }

    }
}

