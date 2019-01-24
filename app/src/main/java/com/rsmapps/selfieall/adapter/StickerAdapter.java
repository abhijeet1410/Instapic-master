package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.R;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyViewHolder> {

    private Boolean FOR_DIALOG = false;
    private Activity mContext;
    private List<String> arrayListSticker;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public StickerAdapter(Activity context, Boolean FOR_DIALOG) {
        mContext = context;
        this.FOR_DIALOG = FOR_DIALOG;
    }

    public void setData(List<String> arrayListSticker) {
        this.arrayListSticker = arrayListSticker;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if(FOR_DIALOG){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_item_sticker_dialog, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_sticker, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drawable drawable = Utils.getDrawableFromAsset(mContext, arrayListSticker.get(position));
        holder.thumbnail.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return arrayListSticker.size();
    }

    public String getSelectedPath(int position){
        return arrayListSticker.get(position);
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