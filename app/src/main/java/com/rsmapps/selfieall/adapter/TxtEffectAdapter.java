package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.R;

import java.util.ArrayList;


/**
 * Created by Lincoln on 31/03/16.
 */

public class TxtEffectAdapter extends RecyclerView.Adapter<TxtEffectAdapter.MyViewHolder> {

    private Activity mContext;
    private  ArrayList<String> arrayListFont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtData;

        public MyViewHolder(View view) {
            super(view);
            txtData = (TextView) view.findViewById(R.id.txtData);
        }
    }

    public TxtEffectAdapter(Activity context) {
        mContext = context;
    }

    public void setData(ArrayList<String> arrayListFont) {
        this.arrayListFont = arrayListFont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_text_effect, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtData.setText("Aa");
        Utils.setFont(mContext,holder.txtData,arrayListFont.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayListFont.size();
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