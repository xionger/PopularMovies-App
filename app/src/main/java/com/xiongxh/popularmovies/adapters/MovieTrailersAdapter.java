package com.xiongxh.popularmovies.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

import org.w3c.dom.Text;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {
    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    public MovieTrailersAdapter(Context context){
        mContext = context;
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
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{
        private TextView mTrailerIdView;
        private TextView mTrailerKeyView;
        private TextView mTrailerTypeView;

        public TrailerViewHolder(View itemView){
            super(itemView);

            mTrailerIdView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_id);
            mTrailerKeyView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_key);
            mTrailerTypeView = (TextView) itemView.findViewById(R.id.tv_movie_trailer_type);
        }

    }
}
