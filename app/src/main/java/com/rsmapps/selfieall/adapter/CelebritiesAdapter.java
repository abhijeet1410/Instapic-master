package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.KohaliActivity;
import com.rsmapps.selfieall.fragment.CelebrityImagesFragment;
import com.rsmapps.selfieall.model.Celebrity;

import java.util.ArrayList;


/**
 * Created by Lincoln on 31/03/16.
 */

public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.MyViewHolder> {
    private ArrayList<Celebrity> mList = new ArrayList<>();
    private Activity mContext;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.title);
            icon = view.findViewById(R.id.icon);
            icon.setVisibility(View.GONE);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CelebrityImagesFragment fragment = CelebrityImagesFragment.newInstance(mList.get(getAdapterPosition()),"");
            ((KohaliActivity)mContext).replaceFragment(fragment,CelebrityImagesFragment.class.getSimpleName(),true,false);
        }
    }

    public CelebritiesAdapter(Activity context, ArrayList<Celebrity> list) {
        mContext = context;
        this.mList =  list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.industry_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}