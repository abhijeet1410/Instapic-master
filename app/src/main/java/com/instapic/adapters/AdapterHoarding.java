package com.instapic.adapters;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirror.utils.Utils;
import com.rsmapps.selfieall.R;

import java.util.ArrayList;

public class AdapterHoarding extends RecyclerView.Adapter<AdapterHoarding.MyViewHolder> {

    private Activity mContext;
    //public String[] effect_images;
    private ArrayList<String> arrayListSticker;
    private int scaleType;
    private int viewtype = 0;
    private int previous = -1;
    private LinearLayout previousLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        LinearLayout thumbnailLayout;
        public TextView txtSongTitle,txtArtist,txtMovie;

        public MyViewHolder(final View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            thumbnailLayout = (LinearLayout) view.findViewById(R.id.thumbnailLayout);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewtype == 4){
                        if (previous == -1){
                            previous = getAdapterPosition();
                            previousLayout = thumbnailLayout;
                            thumbnailLayout.setBackgroundColor(mContext.getResources().getColor(R.color.Red));
                        }else {
                            previousLayout.setBackgroundColor(mContext.getResources().getColor(R.color.White));
                            thumbnailLayout.setBackgroundColor(mContext.getResources().getColor(R.color.Red));
                            previousLayout = thumbnailLayout;

                        }

                    }



                }
            });

        }
    }

//    public void setData(String[] effect_images){
//        this.arrayListSticker = effect_images;
//    }

    public AdapterHoarding(Activity context) {
        mContext = context;
    }

    public AdapterHoarding(Activity context, int viewtype) {
        mContext = context;
        this.viewtype  = viewtype;
    }

    public AdapterHoarding(Activity context, int scaleType,String param) {
        mContext = context;
        this.scaleType  = scaleType;
    }

    public void setData(ArrayList<String> arrayListSticker, int scaleType){
        this.arrayListSticker = arrayListSticker;
        this.scaleType = scaleType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

//        Log.d("scale type :",scaleType+"");
        if(scaleType == 3){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_hoarding_hoarding_sticker, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_hoarding, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Drawable drawable = Utils.getDrawableFromAsset(mContext, arrayListSticker.get(position));
        holder.thumbnail.setImageDrawable(drawable);

        //holder.thumbnailLayout.setTag(position+"");
        if(scaleType == 1){
            holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else  if(scaleType == 2){
            holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_START);
        }else  if(scaleType == 3){
            //holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
            //holder.thumbnail.setPadding(3,3,3,3);
        }else  if(scaleType == 4){
            holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListSticker.size();
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
