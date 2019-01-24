package com.instapic.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.instapic.adapters.AdapterHoarding;
import com.instapic.adapters.EffectAdapter;
import com.instapic.dialogFragments.StickersDialog;
import com.instapic.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.instapic.dialogFragments.PhotoSelectDialog;
import com.instapic.utils.BitmapUtil;
import com.instapic.utils.GaussianBlur;
import libs.imageCropper.ImageCropperActivity;

import com.mirror.utils.BitmapUtils;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.instapic.adapters.BackgroundAdapter;
import com.rsmapps.selfieall.activity.EffectsFilterActivity;
import com.rsmapps.selfieall.activity.MainActivity;
import com.rsmapps.selfieall.helper.AppDialog;
import com.rsmapps.selfieall.helper.FontPojo;
import com.rsmapps.selfieall.helper.ResourceManager;
import com.rsmapps.selfieall.helper.TextEditer;
import com.rsmapps.selfieall.utility.AssetUtil;
import com.instapic.utils.Constant;
import com.theartofdev.edmodo.cropper.CropImage;
import com.xiaopo.flying.sticker.Sticker;

import java.util.ArrayList;

import bornander.SandboxViewTouchListener;
import bornander.gestures.SandboxView;
import sticker.StickerImgView;
import sticker.StickerViewTouchListener;

public class InstaPicActivity extends AppCompatActivity implements SandboxViewTouchListener, StickerViewTouchListener, View.OnClickListener, TextEditer {

    public static final String TAG = InstaPicActivity.class.getSimpleName();

    private static final int REQUEST_WRITE_PERMISSION = 102;

    private static final String CROPPED_IMAGE_NAME = "cropImage";
    private static final int GAUSSIAN_BLUR_RADIUS = 19;
    private static final int GAUSSIAN_BLUR_SIZE = 315;
    public static final String FILE_TYPE_PNG = ".png";

    private boolean isSave = false;

    private Toolbar tbShapes;
    private Bitmap bitmapOriginal, bitmapFilter, foregroundBitmap, for_save;
    private ImageView imgShapeBackground;

    private ArrayList<String> arrayListFrame, arrayListGlass;
    private AdapterHoarding mAdapter;
    private RecyclerView recyclerView, rvBackground;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView imgEffect, imgDummy;

    private String mFeature;
    private StickerImgView snickerView;

    private Bitmap bitmapShape, bitmapExtra;
    private Bitmap bitmap;

    private SandboxView sandb;
    private View view, mainStickerView, layoutShapesChooserView, layoutPatternChooser;
    private FrameLayout lay2;
    private StickerImgView stickerView;

    private LinearLayout llShapesFrames, llShapesStickers, llShapesAddText, llShapesFilter, llShapesForeground, llShapesBackgournd, llCrop;
    private ImageView ivSelectFrame,ivHideFrames, ivHideBackground, ivSelectBackground;
    private DisplayMetrics bundle;
    private int screenHeightPixels;
    private int screenWidthPixels;

    /**
     * View to save final result
     */
    private FrameLayout flSaveView;

    private ImageView imgSave;

    private void getInfo() {
        flSaveView = findViewById(R.id.flSaveView);

        imgSave = findViewById(R.id.imgSave);

        tbShapes = findViewById(R.id.tbShapes);

        layoutPatternChooser = findViewById(R.id.layoutPatternChooser);
        imgDummy = findViewById(R.id.imgShapeEffect2);

        recyclerView = findViewById(R.id.rvFrames);
        rvBackground = findViewById(R.id.rvPatterns);

        mainStickerView = findViewById(R.id.sticker_view);
        lay2 = findViewById(R.id.shape_fmLayout);

        try {
            if (getIntent().hasExtra(Constant.FEATURE)) {
                mFeature = getIntent().getExtras().getString(Constant.FEATURE);
//                Log.d("shape Feature :", mFeature + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgShapeBackground = findViewById(R.id.imgShapeBackground);
        imgEffect = findViewById(R.id.imgShapeEffect);
        layoutShapesChooserView = findViewById(R.id.layoutShapesChooserView);

        ivHideFrames = findViewById(R.id.ivHideFrames);
        ivSelectFrame = findViewById(R.id.ivSelectFrame);

        ivHideBackground = findViewById(R.id.ivHideBackground);
        ivSelectBackground = findViewById(R.id.ivSelectBackground);

        llShapesFrames = findViewById(R.id.llShapesFrames);
        llShapesStickers = findViewById(R.id.llShapesStickers);
        llShapesAddText = findViewById(R.id.llShapesAddText);
        llCrop = findViewById(R.id.llShapeCrop);
        llShapesFilter = findViewById(R.id.llShapesFilter);
        llShapesForeground = findViewById(R.id.llShapesForeground);
        llShapesBackgournd = findViewById(R.id.llShapesBackgournd);
    }

    private void setInfo() {

        /*
         * Setting Toolbar for Activities
         * PIP / Shapes
         */
        initToolbar();

        loadBackgroundData();
        setBackgroundView();

        initStickerView();
        initView();

        /*
         * Setting first frame
         */
        setCurrentFrame(0);

        ivHideFrames.setOnClickListener(this);
        ivSelectFrame.setOnClickListener(this);

        ivHideBackground.setOnClickListener(this);
        ivSelectBackground.setOnClickListener(this);

        llShapesFrames .setOnClickListener(this);
        llShapesStickers.setOnClickListener(this);
        llCrop.setOnClickListener(this);
        llShapesAddText.setOnClickListener(this);
        llShapesFilter.setOnClickListener(this);
        llShapesBackgournd.setOnClickListener(this);
        llShapesForeground.setOnClickListener(this);
        imgSave.setOnClickListener(this);
    }

    private void initToolbar() {
        setSupportActionBar(tbShapes);

        // TODO : Use this to set Toolbar Title if needed
//        try {
//            if (mFeature.equals(Constant.PIP)) {
//                getSupportActionBar().setTitle("PIP");
//            } else if (mFeature.equals(Constant.SHAPE)) {
//                getSupportActionBar().setTitle("Shapes");
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHideBackground:
                /*
                 * Setting first frame
                 */
                setCurrentFrame(selectedShapePosition);
                layoutPatternChooser.setVisibility(View.GONE);
                break;
            case R.id.ivSelectBackground:
                layoutPatternChooser.setVisibility(View.GONE);
                break;
            case R.id.ivHideFrames:
                layoutShapesChooserView.setVisibility(View.GONE);
                break;
            case R.id.ivSelectFrame:
                layoutShapesChooserView.setVisibility(View.GONE);
                break;
            case R.id.llShapesFrames:
                changeBackgroundColor(llShapesFrames);
                if(layoutShapesChooserView.getVisibility()==View.VISIBLE){
                    layoutShapesChooserView.setVisibility(View.GONE);
                }else{
                    layoutPatternChooser.setVisibility(View.GONE);
                    layoutShapesChooserView.setVisibility(View.VISIBLE);
//                    AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            super.onAdClosed();
//                            layoutShapesChooserView.setVisibility(View.VISIBLE);
//                            AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(int i) {
//                            super.onAdFailedToLoad(i);
//                            layoutShapesChooserView.setVisibility(View.VISIBLE);
//                            AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                        }
//                    });
                }
                break;
            case R.id.llShapesStickers:
                getStickersDialog();
                break;
            case R.id.llShapesAddText:
                changeBackgroundColor(llShapesAddText);
                if (mFeature.equals(Constant.PIP)){
                    openTextDialog();
//                    AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            super.onAdClosed();
//                            openTextDialog();
//                            AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(int i) {
//                            super.onAdFailedToLoad(i);
//                            openTextDialog();
//                            AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                        }
//                    });
                }else {
                    openTextDialog();
                }


                break;
            case R.id.llShapesFilter:
                changeBackgroundColor(llShapesFilter);
                AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        handleFiltersEvent();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        handleFiltersEvent();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }
                });

                break;
            case R.id.llShapesBackgournd:
                changeBackgroundColor(llShapesBackgournd);
                AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        handleBackgroundEvent();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        handleBackgroundEvent();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }
                });

                break;
            case R.id.llShapesForeground:
                changeBackgroundColor(llShapesForeground);
                PhotoSelectDialog.getPhotoSelectDialog(new PhotoSelectDialog.OnPhotoSelectDialogResult() {
                    @Override
                    public void onPhotoSelectDialogResult(final Bitmap bitmap, String bitmapType) {

                        if(bitmapType.split(",")[0].equals(PhotoSelectDialog.CAMERA_BITMAP)){
                            Glide.with(InstaPicActivity.this).asBitmap().load(bitmapType.split(",")[1])
                                    .listener(new RequestListener<Bitmap>() {
                                        @Override
                                        public boolean onLoadFailed(
                                                @Nullable GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(
                                                Bitmap resource, Object model, Target<Bitmap> target,
                                                DataSource dataSource, boolean isFirstResource) {
                                            dragview(resource);
                                            return true;
                                        }
                                    })
                                    .submit(screenWidthPixels,screenHeightPixels);
                        }else{
                            dragview(bitmap);
                        }
                    }
                }).show(getSupportFragmentManager(), PhotoSelectDialog.TAG);
                break;

            case R.id.llShapeCrop:
                changeBackgroundColor(llCrop);
//                startCropActivity(BitmapUtil.getBitmapUri(InstaPicActivity.this, bitmapOriginal));
                startCropActivity(foregroundBitmap);
                break;

            case R.id.imgSave:
                AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        requestStoragePermission();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        requestStoragePermission();
                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
                    }
                });
                break;
        }
    }

    private void getStickersDialog() {
        StickersDialog stickersDialog = new StickersDialog();

        stickersDialog.setOnStickerSelectedListener(new StickersDialog.OnStickerSelectedListener() {
            @Override
            public void onStickerSelected(String path) {
                stickerView.setUnLock();
                Drawable drawable = com.rsmapps.selfieall.helper.Utils
                        .getDrawableFromAsset(InstaPicActivity.this, path);
                stickerView.addSticker(drawable);
            }
        });
        stickersDialog.show(getSupportFragmentManager(), StickersDialog.TAG);
    }

    private void changeBackgroundColor(LinearLayout l) {
        llShapesAddText.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llShapesFilter.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llShapesBackgournd.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llShapesForeground.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llCrop.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        l.setBackgroundColor(getResources().getColor(R.color.seekBarBg));
    }

    private void openTextDialog() {
        new AppDialog(InstaPicActivity.this, this).show();
    }

    private void startCropActivity(/*Uri imageUri*/Bitmap bitmap) {
//        String destinationFileName = CROPPED_IMAGE_NAME + FILE_TYPE_PNG;
//        if(imageUri != null) {
//            File file = new File(getCacheDir(), destinationFileName);
//            UCrop uCrop = UCrop.of(imageUri, Uri.fromFile(file));
//
//            uCrop.start(InstaPicActivity.this);
//        }else
//            Log.e("mirror", "Invalid image Uri for Crop." + imageUri);

//        if (imageUri != null) {

        libs.imageCropper.utils.ResourceManager.bitmap = bitmap;
            Intent startImageCropperActivity = new Intent(InstaPicActivity.this, ImageCropperActivity.class);
//            startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(imageUri));
            startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);

//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this);
//        }
//            Log.e(TAG, "Invalid image Uri for Crop." + null);

    }

    public static String[] background_images;

    private void loadBackgroundData() {
        String[] patternFile = AssetUtil.getDataFromAsser(this, AssetUtil.FileType.OLD_PATTERN);
        background_images = new String[patternFile.length-1];
        for (int i = 1; i < patternFile.length; i++) {
            background_images[i-1] = "patternOld/" + patternFile[i];
        }
    }

    private void setBackgroundView() {

        BackgroundAdapter patternAdapter = new BackgroundAdapter(this, background_images);
        rvBackground.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setItemDivider(rvBackground);
        rvBackground.setAdapter(patternAdapter);
        patternAdapter.notifyDataSetChanged();
        rvBackground.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, rvBackground, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Drawable colorDrawable = Utils.getDrawableFromAsset(InstaPicActivity.this, background_images[position]);

                String url = null;
                String shape = null;
                String frame = null;
                String shape1 = null;
                if (mFeature.equals(Constant.PIP)) {

                    url = arrayListGlass.get(selectedShapePosition);
                    shape = url.replace("sample", "");
                    frame = url.replace("Glass/sample", "");
                    Drawable drawable = Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
                    Drawable drawable1 = Utils.getDrawableFromAsset(InstaPicActivity.this, "Glass/frm" + frame);

                    Bitmap result = combineTwoBitmaps(((BitmapDrawable) drawable).getBitmap(), makeMaskImage(resizeImage(
                            ((BitmapDrawable) colorDrawable).getBitmap()), ((BitmapDrawable) drawable).getBitmap()));
                    imgEffect.setImageBitmap(makeMaskImage(result, ((BitmapDrawable) drawable1).getBitmap()));

                } else if (mFeature.equals(Constant.SHAPE)) {
                    url = arrayListFrame.get(selectedShapePosition);
                    shape = url.replace("_sample", "");
                    shape1 = shape.replace("shape", "shap");

                    Drawable drawable = Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
                    Drawable border = Utils.getDrawableFromAsset(InstaPicActivity.this, shape1);

                    bitmapShape = ((BitmapDrawable) drawable).getBitmap();
                    bitmapExtra = ((BitmapDrawable) border).getBitmap();

                    bitmapFilter = makeMaskImage(combineTwoBitmaps(bitmapShape, makeMaskImage(
                            resizeImage(((BitmapDrawable) colorDrawable).getBitmap()), bitmapShape)), bitmapExtra);

                    imgEffect.setImageBitmap(bitmapFilter);

//                bitmapFilter = makeMaskImage(resizeImage(((BitmapDrawable)colorDrawable).getBitmap()),((BitmapDrawable)shapeDrawable).getBitmap());
//
//                imgEffect.setImageBitmap(bitmapFilter);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        layoutPatternChooser.setVisibility(View.GONE);
    }

    private void handleBackgroundEvent() {
        if (layoutPatternChooser.getVisibility() == View.VISIBLE) {
            layoutPatternChooser.setVisibility(View.GONE);
            layoutShapesChooserView.setVisibility(View.VISIBLE);
        } else {
            layoutShapesChooserView.setVisibility(View.GONE);
            layoutPatternChooser.setVisibility(View.VISIBLE);
        }
    }

    private void handleFiltersEvent() {
        Intent intent = new Intent(InstaPicActivity.this, EffectsFilterActivity.class);
        // TODO : Image path may not be right...
        try {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null && getIntent().hasExtra("IMG_PATH")) {
                intent.putExtra("IMG_PATH", bundle.getString("IMG_PATH"));

            }
            intent.putExtra("request", InstaPicActivity.TAG);


            ResourceManager.dummyBitmap = foregroundBitmap;

            startActivityForResult(intent, EffectsFilterActivity.EFFECTS_FILTER_ACTIVITY_OK);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_pic);

        bundle = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(bundle);
        this.screenHeightPixels = bundle.heightPixels;
        this.screenWidthPixels = bundle.widthPixels;

        getInfo();
        setInfo();

        AdsUtil.showBannerAd(InstaPicActivity.this, (AdView) findViewById(R.id.banner_adview_bottom));
    }

    public void dragview(Bitmap bm) {

        bm = BitmapUtils.resize(bm, 1080, 1920);
        foregroundBitmap = bm;

        view = sandb = new SandboxView(this, bm);
        sandb.setSandboxViewListener(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(params);

//        Log.d(TAG, "===width1:" + sandb.getWidth() + "-height1:" + sandb.getHeight());

        if (lay2.getChildCount() > 0) {
            lay2.removeAllViews();
        }
        lay2.addView(view);

        sandb.setEnabled(true);
    }

    private void initStickerView() {
        stickerView = new StickerImgView(this);
        stickerView.setStickerViewTouchListener(this);
    }

    private void initView() {
        try {
            String strImagePath;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && getIntent().hasExtra("IMG_PATH")) {
                strImagePath = bundle.getString("IMG_PATH");
                bitmapOriginal = BitmapFactory.decodeFile(strImagePath);

//                int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(strImagePath), strImagePath);
//                bitmapOriginal = BitmapUtil.rotateBitmap(bitmapOriginal, rotateAngle);

                //Resizing Start
                int orignalWidth = bitmapOriginal.getWidth();
                int orignalHeight = bitmapOriginal.getHeight();
                Rectangle orignalRect = new Rectangle();
                Rectangle targetRect = new Rectangle();
                orignalRect.setBounds(0, 0, orignalWidth, orignalHeight);
                targetRect.setBounds(0, 0, 1080, 1920);
                Rectangle output = getScaledDimension(orignalRect, targetRect);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapOriginal, output.width, output.height, true);
                //bitmapOriginal.recycle();
                bitmapOriginal = resizedBitmap;

                //imgShapeBackground.setImageBitmap(bitmapOriginal);
                //resizing end
            } else {
                bitmapOriginal = ResourceManager.bitmap;
            }

            // checking another trick for resizing image
           /* Display metrics = getWindowManager().getDefaultDisplay();
            int height = metrics.getHeight();
            int width = metrics.getWidth();

            bitmapOriginal = BitmapUtil.resize(bitmapOriginal,width,height);*/

            //Resizing Start
            int orignalWidth = bitmapOriginal.getWidth();
            int orignalHeight = bitmapOriginal.getHeight();
            Rectangle orignalRect = new Rectangle();
            Rectangle targetRect = new Rectangle();
            orignalRect.setBounds(0, 0, orignalWidth, orignalHeight);
            targetRect.setBounds(0, 0, 1080, 1920);
            Rectangle output = getScaledDimension(orignalRect, targetRect);

//            Log.d("rect widht", output.width + "");
//            Log.d("rect height", output.height + "");
            //Bitmap dragviewBitmap= bitmapOriginal;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapOriginal, output.width, output.height, true);
            //bitmapOriginal.recycle();

            bitmapOriginal = BitmapUtils.resize(bitmapOriginal, 1080, 1920);
            dragview(bitmapOriginal);
            bitmapOriginal = resizedBitmap;

            imgDummy.setImageBitmap(resizedBitmap);

            setupRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFrame() {
        //Load Frame
        arrayListFrame = new ArrayList<>();
        arrayListGlass = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            arrayListFrame.add("Shape/shape_sample" + i + ".png" + "");
            // Log.d("shape url :",arrayListFrame.get(0)+"");
        }
        for (int i = 1; i <= 21; i++) {
            arrayListGlass.add("Glass/sample" + i + ".png" + "");
            // Log.d("shape url :",arrayListFrame.get(0)+"");
        }
    }

    private int selectedShapePosition = 0;

    private void setupRecyclerView() {
        loadFrame();

        mAdapter = new AdapterHoarding(this, 4);
        if (mFeature.equals(Constant.PIP)) {
            mAdapter.setData(arrayListGlass, 3);
        } else if (mFeature.equals(Constant.SHAPE)) {
            mAdapter.setData(arrayListFrame, 3);
        }
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        setItemDivider(recyclerView);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, recyclerView, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                try {
                    selectedShapePosition = position;
                    setCurrentFrame(position);
                    //snickerView.addSticker(drawable);
                    //stickerView.addSticker(drawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setItemDivider(RecyclerView recyclerView) {
        try{
            DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.HORIZONTAL);
            divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));
            recyclerView.addItemDecoration(divider);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    //private static final int BACKGROUND_HEIGHT = 660;
    //private static final int BACKGROUND_WIDTH = 720;
    private static final int BACKGROUND_HEIGHT = 640;
    private static final int BACKGROUND_WIDTH = 640;

    private void setCurrentFrame(int position) {
        try {
            if (mFeature.equals(Constant.PIP)) {
                String url = arrayListGlass.get(position);
                String shape = url.replace("sample", "");
                String frame = url.replace("Glass/sample", "");
//                Log.d("image url", "Glass/frm" + frame);
                final Drawable drawable = Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
                final Drawable drawable1 = Utils.getDrawableFromAsset(InstaPicActivity.this, "Glass/frm" + frame);

                Glide.with(InstaPicActivity.this).asBitmap().load(bitmapOriginal)
                        .apply(new RequestOptions().centerCrop())
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

//                                Log.i(TAG, "setCurrentFrame: bitmapOriginal.getWidth(),bitmapOriginal.getHeight()" + " width :" + bitmapOriginal.getWidth() + " height :" + bitmapOriginal.getHeight());
//                                Log.i(TAG, "setCurrentFrame: bitmapOriginal.getWidth(),bitmapOriginal.getHeight()" + " width :" + resource.getWidth() + " height :" + resource.getHeight());

//                            imgEffect.setImageBitmap(resource);
//                            Bitmap squareBitmap = BitmapUtil.resize(resource, 300, 500);

//                            Bitmap squareBitmap = resizeBitmap(resource);
                                Bitmap result = combineTwoBitmaps(((BitmapDrawable) drawable).getBitmap(), makeMaskImage(resizeImage(GaussianBlur.with(InstaPicActivity.this)
                                        .radius(8).size(800)
                                        .render(resource)), ((BitmapDrawable) drawable).getBitmap()));

                                imgEffect.setImageBitmap(makeMaskImage(result, ((BitmapDrawable) drawable1).getBitmap()));

                                return true;
                            }
                        }).submit(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

            } else if (mFeature.equals(Constant.SHAPE)) {
//                Log.d("callback url :", arrayListFrame.get(position) + "");
                String url = arrayListFrame.get(position);
                String shape = url.replace("_sample", "");
                String shape1 = shape.replace("shape", "shap");
                Drawable drawable = Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
                Drawable border = Utils.getDrawableFromAsset(InstaPicActivity.this, shape1);

                bitmapShape = ((BitmapDrawable) drawable).getBitmap();
                bitmapExtra = ((BitmapDrawable) border).getBitmap();


//                Log.i(TAG, "setCurrentFrame: imgEffect.getWidth(),imgEffect.getHeight()" + " width :" + imgEffect.getWidth() + " height :" + imgEffect.getHeight());

                Glide.with(InstaPicActivity.this).asBitmap().load(bitmapOriginal)
                        .apply(new RequestOptions().centerCrop())
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

//                                Log.i(TAG, "setCurrentFrame: bitmapOriginal.getWidth(),bitmapOriginal.getHeight()" + " width :" + bitmapOriginal.getWidth() + " height :" + bitmapOriginal.getHeight());
//                                Log.i(TAG, "setCurrentFrame: bitmapOriginal.getWidth(),bitmapOriginal.getHeight()" + " width :" + resource.getWidth() + " height :" + resource.getHeight());
//                            imgEffect.setImageBitmap(resource);
//                            Bitmap squareBitmap = BitmapUtil.resize(resource, 300, 500);
//                            Bitmap squareBitmap = resizeBitmap(resource);
                                // bitmapFilter = makeMaskImage(combineTwoBitmaps(bitmapShape,makeMaskImage(resizeImage(GaussianBlur.with(InstaPicActivity.this).radius(12).size(800).render(resource)),bitmapShape)),bitmapExtra);
                                bitmapFilter = makeMaskImage(combineTwoBitmaps(bitmapShape, makeMaskImage(resizeImage(GaussianBlur.with(InstaPicActivity.this).radius(8).size(800).render(resource)), bitmapShape)), bitmapExtra);

                                imgEffect.setImageBitmap(bitmapFilter);
                                return true;
                            }
                        }).submit(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

                // bitmapFilter = makeMaskImage(bitmapOriginal,((BitmapDrawable)drawable).getBitmap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int height = BitmapFactory.decodeResource(getResources(), R.drawable.fr11).getHeight();
        int width = BitmapFactory.decodeResource(getResources(), R.drawable.fr11).getWidth();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        return createBitmap;
    }

    private Bitmap combineTwoBitmaps(Bitmap bitmap, Bitmap bitmap2) {

        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
        Bitmap bm = Bitmap.createScaledBitmap(bitmap,bitmap2.getWidth(),bitmap2.getHeight(),true);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        canvas.drawBitmap(bm, 0.0f, 0.0f, paint);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public Bitmap makeMaskImage(Bitmap bitmap, Bitmap bitmap2) {
//        Log.d("mask width :",bitmap.getWidth()+" HEIGHT = "+bitmap.getHeight());
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bm = Bitmap.createScaledBitmap(bitmap2,bitmap.getWidth(),bitmap.getHeight(),true);
        int width1 = bitmap.getWidth()/2;
        int height1 = bitmap.getHeight()/2;
        int width2 = bitmap2.getWidth()/2;
        int height2 = bitmap2.getHeight()/2;
        int top   = height1-height2;
        int left = width1-width2;

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bm, 0.0f, 0.0f, paint);

        paint.setXfermode(null);
        return bitmap;
    }
    public Rectangle getScaledDimension(Rectangle imgSize, Rectangle boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(0, 0, new_width, new_height);
        return rectangle;
    }

    @Override
    public void onStickerViewTouchTouch(View v, MotionEvent event) {
//        stickerView.setUnLock();
        /*if(recyclerView.getVisibility() == View.VISIBLE){
            recyclerView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onSandboxViewTouch(View v, MotionEvent event) {
        //stickerView.setUnLock();
    }

    @Override
    public void onTextEditingCompleted(FontPojo fontPojo) {
        if (fontPojo != null && fontPojo.getText() != null && fontPojo.getText().trim().length() > 0)
            stickerView.addTextSticker(InstaPicActivity.this, fontPojo);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.insta_pic_activity_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.mSave:
//
//                AdsUtil.showInterstitialAd(InstaPicActivity.this, new AdListener() {
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        requestStoragePermission();
//                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        requestStoragePermission();
//                        AdsUtil.preloadInterstitialAd(InstaPicActivity.this);
//                    }
//                });
//
//
//                break;
////            case R.id.mShare:
////                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
////                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            savePhoto();
        }
    }

    private void savePhoto() {

        layoutPatternChooser.setVisibility(View.GONE);
        layoutShapesChooserView.setVisibility(View.GONE);


        try {

            boolean isLocked = stickerView.isLock();

            stickerView.setLock();
//            String path = stickerView.save(InstaPicActivity.this,getFinalBitmap(mainStickerView));
            String path = stickerView.save(InstaPicActivity.this, getFinalBitmap(flSaveView));

            if(path != null) {

                if(isLocked) stickerView.setLock();else stickerView.setUnLock();

                layoutPatternChooser.setVisibility(View.VISIBLE);
                layoutShapesChooserView.setVisibility(View.VISIBLE);
                isSave = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setMessage("Do you want to save?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                layoutPatternChooser.setVisibility(View.VISIBLE);
                layoutShapesChooserView.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

    public Bitmap getFinalBitmap(View v) {
        Bitmap bitmapToSave = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmapToSave);
        v.draw(can);
        return bitmapToSave;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            savePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            // For Free-Hand Crop Library
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = CropImage.getActivityResult(data).getError();
                error.printStackTrace();
            }
            return;
        }

        switch (requestCode) {
            case EffectsFilterActivity.EFFECTS_FILTER_ACTIVITY_OK:
//                if (ResourceManager.bitmap != null){
                if (ResourceManager.dummyBitmap != null){

                    ResourceManager.bitmap = BitmapUtils.resize(ResourceManager.dummyBitmap,
                            1080, 1920);

                    dragview(ResourceManager.bitmap);
                }
                //else {
                //sandb.setBitmap(bitmapOriginal);
                //}

                break;
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:    // New  Crop library
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                Uri resultUri = result.getUri();
//                handleCropResult(resultUri);
            case ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY:    // New  Crop library
                handleCropResult();
                break;
//            case UCrop.REQUEST_CROP:    // Old  Crop library
//                handleCropResult(data);
//                break;
            default:
//                sandb.setBitmap(ResourceManager.bitmap);
                break;
        }

    }

    private void handleCropResult(){

        try {
            bitmap = libs.imageCropper.utils.ResourceManager.bitmap;

            Display metrics = getWindowManager().getDefaultDisplay();
            int height = metrics.getHeight();
            int width = metrics.getWidth();

            bitmap = BitmapUtil.resize(bitmap, width, height);

            dragview(bitmap);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleCropResult(Uri imageUri){

        if(imageUri == null) {
//            Log.e(TAG, "handleCropResult: imageUri is NULL");
            return;
        }

        String imgPath = imageUri.getPath();

        try {
            bitmap = BitmapFactory.decodeFile(imgPath);

            int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(imgPath), imgPath);
            bitmap = BitmapUtil.rotateBitmap(bitmap, rotateAngle);
            Display metrics = getWindowManager().getDefaultDisplay();
            int height = metrics.getHeight();
            int width = metrics.getWidth();

            bitmap = BitmapUtil.resize(bitmap, width, height);

            // imgPic.setImageBitmap(bitmap);

            dragview(bitmap);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    private void handleCropResult(Intent data){
//        if(data == null)
//            return;
//
//        final Uri resultUri = UCrop.getOutput(data);
//
//        if(resultUri == null)
//            return;
//
//        String imgPath = resultUri.getPath();
//
//        try {
//            bitmapOriginal = BitmapFactory.decodeFile(imgPath);
//
//            int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(imgPath), imgPath);
//            bitmapOriginal = BitmapUtil.rotateBitmap(bitmapOriginal, rotateAngle);
//
//            Display metrics = getWindowManager().getDefaultDisplay();
//            int height = metrics.getHeight();
//            int width = metrics.getWidth();
//
//            bitmapOriginal = BitmapUtil.resize(bitmapOriginal, width, height);
//
//            sandb.setBitmap(bitmapOriginal);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private Bitmap resizeImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int height = BitmapFactory.decodeResource(getResources(), R.drawable.fr11).getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, null, new Rect(0, 0, height, height), null);
        return createBitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = height > width ? width : height;
        int i2 = height > width ? height - (height - width) : height;
        int i3 = (width - height) / 2;
        if (i3 < 0) {
            i3 = 0;
        }
        width = (height - width) / 2;
        if (width < 0) {
            width = 0;
        }
        return Bitmap.createBitmap(bitmap, i3, width, i, i2);
    }

    @Override
    public void onBackPressed() {
        if (isSave){
            super.onBackPressed();
        }else {
            com.aligohershabir.photocollage.view.dialogFragments.AlertDialog alertDialog =
                    new com.aligohershabir.photocollage.view.dialogFragments.AlertDialog();

            alertDialog.getAlertDialog(InstaPicActivity.this, new com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.OnItemClickListener() {
                @Override
                public void onContinueClicked() {
                    InstaPicActivity.super.onBackPressed();
                }
            }).setTitle(getString(R.string.alert_title_warning))
                    .setDescription(getString(R.string.alert_desc_changes_will_be_lost))
                    .setSubtitle(getString(R.string.alert_subtitle_press_exit_or_cancel))
                    .show(getSupportFragmentManager(), com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.TAG);
        }

    }
}

