package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.model.Celebrity;


/**
 * Created by Lincoln on 31/03/16.
 */

public class KohaliAdapter extends RecyclerView.Adapter<KohaliAdapter.MyViewHolder> {
    private Activity mContext;
    public Celebrity celebrity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public KohaliAdapter(Activity context, Celebrity celebrity) {
        mContext = context;
        this.celebrity = celebrity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_kohali, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (celebrity.getImages().get(position).startsWith("celebrity/image")){
            Drawable drawable = Utils.getDrawableFromAsset(mContext, celebrity.getImages().get(position));
            holder.thumbnail.setImageDrawable(drawable);
        }else {
            Glide.with(mContext).load(celebrity.getImages().get(position)).apply(new RequestOptions().fitCenter().placeholder(R.drawable.actor_placeholder).error(R.drawable.actor_placeholder)).into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        //return KohaliActivity.kohali_images.length;
        return celebrity.getImages().size();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}