package com.savedPhotos.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.utility.BitmapUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class AdapterMyCreation extends RecyclerView.Adapter<AdapterMyCreation.MyViewHolder> {


    private Context mContext;
    private List<String> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, imgMore;
        public TextView txtSongTitle,txtArtist,txtMovie;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            imgMore = view.findViewById(R.id.imgMore);


            imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeletedialog(getAdapterPosition());
                }
            });

        }
    }

    public AdapterMyCreation(Context context) {
        mContext = context;
    }

    public void  setData(List<String> arrayList) {
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_hoarding_my_creation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //Image image = images.get(position);
//        Movies movie = listMovies.get(position);
//
//        holder.txtSongTitle.setText(movie.getSongTitle());
//        holder.txtArtist.setText(movie.getArtistName());
//        holder.txtMovie.setText(movie.getMovieName());
//
//        Glide.with(mContext).load(arrayList.get(position))
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.thumbnail);

        //holder.thumbnail.setBackgroundResource(FrameActivity.frame_Images[position]);


        try {
            /*Bitmap b = BitmapFactory.decodeFile(arrayList.get(position));*/
            Glide.with(mContext).asBitmap()
                    .load(Uri.parse(arrayList.get(position)))
                    .apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            holder.thumbnail.setImageBitmap(resource);
                            return true;
                        }
                    })
                    .submit(300,300);
//            holder.thumbnail.setImageBitmap(Bitmap.createScaledBitmap(b, 300, 300, false));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "No Saved Images", Toast.LENGTH_SHORT).show();
            if(this.arrayList == null){

                this.arrayList = new ArrayList<>();
            }else if(this.arrayList.size() < 1){
                this.arrayList.removeAll(this.arrayList);
            }
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
    private void showDeletedialog(final int position){
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
            alertDialogBuilder.setMessage("D0 you want to Delete?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            /*ResourceManager.bitmap.recycle();
                            ResourceManager.bitmap = null;*/
                            //saveHoarding();
                            Log.d("image location :",arrayList.get(position));

                            arrayList.remove(position);
                           // deleteFileFromGallery(arrayList.get(position));
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //navigateToBack();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}