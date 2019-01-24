package com.nileshp.multiphotopicker.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


public class AlbumAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data = new ArrayList();
    int layoutResourceId;
    OnAlbum onItem;
    int pHeightItem = 0;
    int pWHIconNext = 0;

    static class RecordHolder {
        ImageView iconNext;
        ImageView imageItem;
        RelativeLayout layoutRoot;
        TextView txtPath;
        TextView txtTitle;
        RelativeLayout rlSelectedAlbum;

        RecordHolder() {
        }
    }

    public AlbumAdapter(Context context, int layoutResourceId, ArrayList<ImageModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
//        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 6;
        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 3;
        this.pWHIconNext = this.pHeightItem / 4;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.rlSelectedAlbum = row.findViewById(R.id.rlSelectedAlbum);
            holder.txtTitle = (TextView) row.findViewById(R.id.name_album);
            holder.txtPath = (TextView) row.findViewById(R.id.path_album);
            holder.imageItem = (ImageView) row.findViewById(R.id.icon_album);
            holder.iconNext = (ImageView) row.findViewById(R.id.iconNext);
            holder.layoutRoot = (RelativeLayout) row.findViewById(R.id.layoutRoot);

//            holder.layoutRoot.getLayoutParams().height = this.pHeightItem + 60;
            holder.imageItem.getLayoutParams().width = this.pHeightItem;
            holder.imageItem.getLayoutParams().height = this.pHeightItem;
            holder.iconNext.getLayoutParams().width = this.pWHIconNext;
            holder.iconNext.getLayoutParams().height = this.pWHIconNext;

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

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

        row.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AlbumAdapter.this.onItem != null) {
                    AlbumAdapter.this.onItem.OnItemAlbumClick(position);
                }
            }
        });
        return row;
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
