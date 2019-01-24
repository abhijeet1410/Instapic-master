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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nileshp.multiphotopicker.R;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;
import com.nileshp.multiphotopicker.photopicker.model.ImageModel;
import com.nileshp.multiphotopicker.photopicker.myinterface.OnListAlbum;

import java.util.ArrayList;

public class ListAlbumAdapterRV extends RecyclerView.Adapter<ListAlbumAdapterRV.ViewHolder> {

    private Context context;
    private ArrayList<ImageModel> data;
    private ArrayList<ImageModel> selected;
    private int layoutResourceId;
    private OnListAlbum onListAlbum;
    private int pHeightItem = 0;


    public ListAlbumAdapterRV(Context context, int layoutResourceId, ArrayList<ImageModel> data,ArrayList<ImageModel> selected) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 3;
        this.selected = selected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(layoutResourceId,parent,false);

        RelativeLayout rlSelectedPhoto = view.findViewById(R.id.rlSelectedPhoto);
//        ImageView ivImageItem = view.findViewById(R.id.imageItem);
        ImageView ivClick = view.findViewById(R.id.click);
        RelativeLayout rlLayoutRoot = view.findViewById(R.id.layoutRoot);

        rlLayoutRoot.getLayoutParams().height = this.pHeightItem;
        rlSelectedPhoto.getLayoutParams().width = this.pHeightItem;
        rlSelectedPhoto.getLayoutParams().height = this.pHeightItem;
        ivClick.getLayoutParams().width = this.pHeightItem;
        ivClick.getLayoutParams().height = this.pHeightItem;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ImageModel item = this.data.get(position);
        boolean flag = false;
        for(int i=0; i < selected.size(); i++){
            if(selected.get(i).getPathFile().equals(item.getPathFile())){
                flag = true;
                break;
            }
        }

        if(flag){
            holder.rlSelectedPhoto.setBackgroundResource(android.R.color.white);
        }else {
            holder.rlSelectedPhoto.setBackgroundResource(android.R.color.transparent);
        }

        Glide.with(context).asBitmap().load(item.getPathFile())
                .apply(new RequestOptions().placeholder(R.drawable.piclist_icon_default))
                .into(holder.ivImageItem);

        holder.ivClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ListAlbumAdapterRV.this.onListAlbum != null) {
                    if(selected.size() < PickImageActivity.limitImageMax) {
                        item.setSelected(true);
                        holder.rlSelectedPhoto.setBackgroundResource(android.R.color.white);
                    }
                    ListAlbumAdapterRV.this.onListAlbum.OnItemListAlbumClick(item);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivClick, ivImageItem;
        private RelativeLayout rlLayoutRoot, rlSelectedPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            ivClick = itemView.findViewById(R.id.click);
            ivImageItem = itemView.findViewById(R.id.imageItem);
            rlLayoutRoot = itemView.findViewById(R.id.layoutRoot);
            rlSelectedPhoto = itemView.findViewById(R.id.rlSelectedPhoto);
        }
    }

    public OnListAlbum getOnListAlbum() {
        return this.onListAlbum;
    }

    public void setOnListAlbum(OnListAlbum onListAlbum) {
        this.onListAlbum = onListAlbum;
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
