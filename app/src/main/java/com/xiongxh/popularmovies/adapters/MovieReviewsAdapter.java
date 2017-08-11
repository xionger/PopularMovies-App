package com.xiongxh.popularmovies.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder>{
    private static final String TAG = MovieReviewsAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    public MovieReviewsAdapter (Context context){
        mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        if (viewGroup instanceof RecyclerView) {
            int layoutIdForListItem = R.layout.list_item_review;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            view.setFocusable(true);

            ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

            return reviewViewHolder;

        }else {
            throw new RuntimeException("Not bound to Recyclerview.");
        }
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewHolder, int position) {
        Log.d(TAG, "#" + position);
        if (! mCursor.moveToFirst()){
            return;
        }

        mCursor.moveToPosition(position);

        String reviewContent = mCursor.getString(ConstantsUtils.COLUMN_REVIEW_CONTENT);
        reviewHolder.mReviewContentView.setText(reviewContent);

        String reviewAuthor = mCursor.getString(ConstantsUtils.COLUMN_REVIEW_AUTHOR);
        reviewHolder.mReviewAuthorView.setText(reviewAuthor);

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


    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewContentView;
        private TextView mReviewAuthorView;

        public ReviewViewHolder(View itemView){
            super(itemView);

            mReviewContentView = (TextView) itemView.findViewById(R.id.tv_movie_review_content);

            mReviewAuthorView = (TextView) itemView.findViewById(R.id.tv_movie_review_author);
        }
    }
}

