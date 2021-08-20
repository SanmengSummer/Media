package com.landmark.mediasessiondemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Author: chenhuaxia
 * Description:
 * Date: 2021/8/13 11:17
 **/

@SuppressLint("NotifyDataSetChanged")
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseRecyclerViewHolder> {
    private final MainActivity mainActivity;

    RecyclerViewAdapter(MainActivity mainActivity, ArrayList<MediaBrowserCompat.MediaItem> mediaItemList) {
        this.mainActivity = mainActivity;
        mMediaItemList = mediaItemList;
    }

    ArrayList<MediaBrowserCompat.MediaItem> mMediaItemList;

    public void setData(ArrayList<MediaBrowserCompat.MediaItem> mediaItemList) {
        mMediaItemList = mediaItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item, parent, false);
        return new BaseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        MediaBrowserCompat.MediaItem mediaItem = mMediaItemList.get(position);
        MediaDescriptionCompat description = mediaItem.getDescription();
        Bitmap iconBitmap = description.getIconBitmap();
        holder.setImageResource(R.id.image, iconBitmap);
        holder.setText(R.id.textView,
                "Title: " + mediaItem.getDescription().getTitle()
                        + "\n Artist: " + mediaItem.getDescription().getSubtitle()
                        + "\n Album: " + mediaItem.getDescription().getDescription()
        );
    }

    @Override
    public int getItemCount() {
        return mMediaItemList.size();
    }

    public static class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;

        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView textView = getView(viewId);
            textView.setText(text);
        }

        public void setText(int viewId, int textId) {
            TextView textView = getView(viewId);
            textView.setText(textId);
        }

        public void setImageResource(int viewId, int resId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(resId);
        }

        public void setImageResource(int viewId, Bitmap resId) {
            ImageView imageView = getView(viewId);
            imageView.setImageBitmap(resId);
        }
    }
}
