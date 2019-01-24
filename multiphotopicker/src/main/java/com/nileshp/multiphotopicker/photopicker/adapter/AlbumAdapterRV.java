package com.nileshp.multiphotopicker.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nileshp.multiphotopicker.R;
import com.nileshp.multiphotopicker.photopicker.model.ImageModel;
import com.nileshp.multiphotopicker.photopicker.myinterface.OnAlbum;

import java.io.File;
import java.util.ArrayList;

public class AlbumAdapterRV extends RecyclerView.Adapter<AlbumAdapterRV.ViewHolder> {

    private Context context;
    private ArrayList<ImageModel> data;
    private int layoutResourceId;
    private OnAlbum onItem;
    private int pHeightItem = 0;
    private int pWHIconNext = 0;

    public AlbumAdapterRV(Context context, ArrayList<ImageModel> data, int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
//        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 6;
        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 3;
        this.pWHIconNext = this.pHeightItem / 4;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(layoutResourceId,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ImageModel item = (ImageModel) this.data.get(position);
        holder.txtTitle.setText(item.getName());
        holder.txtPath.setText(item.getPathFolder());

        /*
         * Setting seleted folder Background Color
         */
        if(data.get(position).getSelected()){
            holder.rlSelectedAlbum.setBackgroundResource(R.color.appColor);
        }else{
            holder.rlSelectedAlbum.setBackgroundResource(android.R.color.transparent);
        }

        Glide.with(this.context).load(new File(item.getPathFile()))
                .apply(new RequestOptions().placeholder(R.drawable.piclist_icon_default))
                .into(holder.imageItem);

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (AlbumAdapterRV.this.onItem != null) {
                    AlbumAdapterRV.this.onItem.OnItemAlbumClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iconNext, imageItem;
        private RelativeLayout layoutRoot, rlSelectedAlbum;
        private TextView txtPath, txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            iconNext = itemView.findViewById(R.id.iconNext);
            imageItem = itemView.findViewById(R.id.icon_album);
            layoutRoot = itemView.findViewById(R.id.layoutRoot);
            rlSelectedAlbum = itemView.findViewById(R.id.rlSelectedAlbum);
            txtPath = itemView.findViewById(R.id.path_album);
            txtTitle = itemView.findViewById(R.id.name_album);

        }
    }

    public OnAlbum getOnItem() {
        return this.onItem;
    }

    public void setOnItem(OnAlbum onItem) {
        this.onItem = onItem;
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
