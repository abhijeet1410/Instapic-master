package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rsmapps.selfieall.R;


/**
 * Created by Lincoln on 31/03/16.
 */

public class PaintAdapter extends RecyclerView.Adapter<PaintAdapter.MyViewHolder> {

    private Activity mContext;
    private String[] colorNames;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llyColor;

        public MyViewHolder(View view) {
            super(view);
            llyColor = (LinearLayout) view.findViewById(R.id.llyColor);
        }
    }

    public PaintAdapter(Activity context) {
        mContext = context;
    }

    public void setData(String[] colorNames) {
        this.colorNames = colorNames;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_paint, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TypedArray ta = mContext.getResources().obtainTypedArray(R.array.colors);
        int colorToUse = ta.getResourceId(position, 0);
        holder.llyColor.setBackgroundResource(colorToUse);
    }

    @Override
    public int getItemCount() {
        return colorNames.length;
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