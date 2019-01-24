package com.aligohershabir.photocollage.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.aligohershabir.photocollage.model.Grid;
import com.aligohershabir.photocollage.utils.AssetUtils;
import com.aligohershabir.photocollage.R;

import java.util.ArrayList;

public class CollageAdapter extends RecyclerView.Adapter<CollageAdapter.ViewHolder> {

    private boolean smallGrid = false;
    private Activity activity;
    private ArrayList<Grid> grids;
    private OnItemClickedListener onItemClickedListener;

    public CollageAdapter(Activity activity, ArrayList<Grid> grids, OnItemClickedListener onItemClickedListener, boolean smallGrid) {
        this.activity = activity;
        this.grids = grids;
        this.onItemClickedListener = onItemClickedListener;
        this.smallGrid = smallGrid;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(smallGrid){
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_collage_grid_small,parent,false));
        }else
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_collage_grid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tvCellNumber.setText(String.valueOf(position));

        if(grids.get(position).getGridDrawable() != null)
            Glide.with(activity).load(grids.get(position).getGridDrawable())
                    .into(holder.ivGridImage);
        else
            Glide.with(activity).load(AssetUtils.getDrawableFromAsset(activity,grids.get(0).getFileDrawablePath()))
                    .into(holder.ivGridImage);

        holder.ivGridImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.onImageClicked(holder.getAdapterPosition()/*,grids.get(holder.getAdapterPosition()).getGridDrawable()*/);
            }
        });
    }

    @Override
    public int getItemCount() {
        return grids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivGridImage;
        private TextView tvCellNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            ivGridImage = itemView.findViewById(R.id.ivGridImage);

            tvCellNumber = itemView.findViewById(R.id.tvCellNumber);

        }
    }

    public interface OnItemClickedListener{
        void onImageClicked(int position/*, Drawable drawable*/);
    }

}
