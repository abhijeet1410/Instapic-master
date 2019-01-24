package com.aligohershabir.photocollage.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aligohershabir.photocollage.utils.BitmapUtils;
import com.aligohershabir.photocollage.view.activity.CollageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.aligohershabir.photocollage.model.GridImage;
import com.aligohershabir.photocollage.R;

import com.aligohershabir.photocollage.utils.bornander.SandboxViewTouchListener;
import com.aligohershabir.photocollage.utils.bornander.gestures.SandboxView;

//import android.util.Log;

public class CollageDragViewFragment extends Fragment {
    public static final String TAG = CollageDragViewFragment.class.getSimpleName();

    private Context context;

    /*
     * Path of image to be displayed in this fragment.
     *
     * This will be set from the calling Activity (CollageActivity)
     */
    private String imagePath;

    public CollageDragViewFragment setImagePath(String imagePath){
        this.imagePath = imagePath;
        return this;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public void setShape(Drawable shapeDrawable, Drawable shapeStrokeDrawable){

        ivShapeForeground.setImageDrawable(shapeDrawable);
        ivShapeStroke.setImageDrawable(shapeStrokeDrawable);
    }

    public void setEnableStroke(boolean enable){
        if(enable)
            ivShapeStroke.setVisibility(View.VISIBLE);
        else
            ivShapeStroke.setVisibility(View.INVISIBLE);
    }

    public void setStrokeColor(int color){
        ivShapeStroke.setColorFilter(color);
    }

    public void setShapeBackgroundColor(int color,Drawable backgroundDrawable){
        if(color != -1) {
            ivShapeForeground.setColorFilter(color);
        }else{
            // TODO : handle background-feature here.
//            Log.i(TAG, "setShapeBackgroundColor: "+ backgroundDrawable.toString());
            /*
             * backgroundDrawable is actually masked foreground drawable.
             */
            ivShapeForeground.setColorFilter(android.R.color.transparent);
            ivShapeForeground.setImageDrawable(backgroundDrawable);
        }
    }

    private FrameLayout frameLayout;

    public void setContainer(FrameLayout frameLayout){
        this.frameLayout = frameLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private SandboxView sandb;

    private View view;
    private FrameLayout flMainDragView;

    private ImageView ivShapeForeground, ivShapeStroke;

    private RelativeLayout rlCollageFragment;

    private void getInfo(View view){
        rlCollageFragment = view.findViewById(R.id.rlCollageFragment);
        flMainDragView = view.findViewById(R.id.flMainDragView);

        ivShapeForeground = view.findViewById(R.id.ivShapeForeground);
        ivShapeStroke = view.findViewById(R.id.ivShapeStroke);
    }

    private void setInfo(){

        initSandBoxView();

    }

    private OnItemClickListener onItemClickListener;
    boolean doubleClicked = false;

    private void getDoubleClickedPressed() {
        this.doubleClicked = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleClicked =false;
            }
        }, 1000);
    }

    public CollageDragViewFragment getInstance(OnItemClickListener onItemClickListener){
        CollageDragViewFragment collageDragViewFragment = new CollageDragViewFragment();
        collageDragViewFragment.onItemClickListener = onItemClickListener;
        return collageDragViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collage_drag_view,container,false);
        getInfo(view);
        setInfo();
        return view;
    }

    public RelativeLayout getRelativeLayout(){
        return rlCollageFragment;
    }

    private Bitmap bitmap;

    private GridImage gridImage;

    public GridImage getGridImage() {
        return gridImage;
    }

    public CollageDragViewFragment setGridImage(GridImage gridImage) {
        this.gridImage = gridImage;
        return this;
    }

    public CollageDragViewFragment setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public Bitmap getBitmap(){
        return this.gridImage.getBitmap();
    }

    private void initSandBoxView() {

        try{


//            Log.d(TAG, "CollageDragViewFragment Image-Path : " + imagePath);

//            bitmap = BitmapFactory.decodeFile(imagePath);

//            Display metrics = getActivity().getWindowManager().getDefaultDisplay();
//            int height = metrics.getHeight();
//            int width = metrics.getWidth();
//
//            Log.d(TAG, "Default-Display-Height : " + height);
//            Log.d(TAG, "Default-Display-Width : " + width);

            /*
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    // Get size of image without loading into memory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imagePath, options);

                    //Calculate scale factor with image’s size.
                    options.inSampleSize = calculateInSampleSize(options, options.outWidth, options.outHeight);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(imagePath, options);

                    bitmap = getCompressedBitmap(bitmap, 20);

                    //Load bitmap into memory with calculated values.
                    dragview(bitmap);
                }
            });
            */

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imagePath, options);
//
//            int outWidth = options.outWidth;
//            int outHeight = options.outHeight;


            dragview(gridImage.getBitmap());

            /*Glide.with(context).asBitmap()
                    .load()
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

//                            int width = resource.getWidth();
//                            int height = resource.getHeight();

//                            bitmap = getResizedBitmap(resource,200);

//                            bitmap = getCompressedBitmap(resource, 20);

//                            bitmap = Bitmap.createScaledBitmap(resource, width, height, true);//BitmapUtil.resize(bitmap, width, height);

                            dragview(resource);

                            return true;
                        }
                    }).submit();
                    */

//            bitmap = getResizedBitmap(bitmap,300);
//            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);//BitmapUtil.resize(bitmap, width, height);
//            dragview(bitmap);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setBitmap(String imagePath){

//        Bitmap newBitmap = BitmapFactory.decodeFile(imagePath);

        Display metrics = getActivity().getWindowManager().getDefaultDisplay();
        int height = metrics.getHeight();
        int width = metrics.getWidth();

//        newBitmap = BitmapUtil.resize(newBitmap,width,height);

        /*
         * Getting Bitmap from imagePath and
         * Setting Final Bitmap in dragView.
         */
        Glide.with(context).asBitmap().load(imagePath)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        dragview(resource);

                        return true;
                    }
                }).submit(width,height);
    }

    /**
     * Max allowed duration for a "click", in milliseconds.
     */
    private static final int MAX_CLICK_DURATION = 1000;

    /**
     * Max allowed distance to move during a "click", in DP.
     */
    private static final int MAX_CLICK_DISTANCE = 15;

    private long pressStartTime;
    private float pressedX;
    private float pressedY;

    private SandboxViewTouchListener sandboxViewTouchListener = new SandboxViewTouchListener() {
        @Override
        public void onSandboxViewTouch(View v, MotionEvent e) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    pressStartTime = System.currentTimeMillis();
                    pressedX = e.getX();
                    pressedY = e.getY();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    long pressDuration = System.currentTimeMillis() - pressStartTime;
                    if (pressDuration < MAX_CLICK_DURATION && distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                        // Click event has occurred
                        if(doubleClicked) {
                            onItemClickListener.onDoubleClicked(CollageDragViewFragment.this);
                        }else
                            getDoubleClickedPressed();
                    }
                }
            }
        }
    };

    private boolean RESIZED = false;

    public void dragview(Bitmap bm, boolean FROM_FILTER_EFFECT){
        RESIZED = false;
        dragview(bm);
    }

    public void dragview(Bitmap bm) {

//        Log.d(TAG, "Setting SandBoxView...");
        if(!RESIZED){
//            bm = BitmapUtils.resize(bm,view.getWidth()+1800, view.getHeight()+1800);

//            bm = Bitmap.createScaledBitmap(bm,(sandb.width+100),(sandb.height+100), false);

            this.RESIZED = true;
            dragview(bm);
        }else{

            sandb = new SandboxView(context, bm);

            view =  sandb.getRootView();

            sandb.setSandboxViewListener(sandboxViewTouchListener);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);

            if (flMainDragView.getChildCount() > 0) {
                flMainDragView.removeAllViews();
            }

            flMainDragView.addView(view);
            sandb.setEnabled(true);
        }

//        flMainDragView.addView(view);
//
//        sandb.setEnabled(true);
    }

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx);
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    public interface OnItemClickListener{
        void onDoubleClicked(CollageDragViewFragment fragment);
    }
}
