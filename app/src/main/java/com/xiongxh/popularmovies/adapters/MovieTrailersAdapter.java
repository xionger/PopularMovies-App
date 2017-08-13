package com.xiongxh.popularmovies.adapters;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.data.MovieContract;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

import org.w3c.dom.Text;

import static android.R.attr.start;

/**
public class MovieTrailersAdapter extends CursorAdapter {
    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    public MovieTrailersAdapter(Context context, Cursor cursor, int i){
        super(context, cursor, i);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_trailer, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView trailerName = (TextView) view.findViewById(R.id.tv_movie_trailer_name);
        trailerName.setText(cursor.getString(ConstantsUtils.COLUMN_VIDEO_NAME));
    }

}
    */


public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {
    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;


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
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTrailerIdView;
        private TextView mTrailerKeyView;
        private TextView mTrailerTypeView;

        public TrailerViewHolder(View itemView){
            super(itemView);

            mTrailerIdView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_id);
            mTrailerKeyView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_key);
            mTrailerTypeView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_type);

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

