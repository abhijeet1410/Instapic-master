package com.rsmapps.selfieall.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.instapic.activities.InstaPicActivity;
import com.instapic.adapters.BackgroundAdapter;
import com.instapic.utils.Constant;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.dialogFragments.StickersDialog;
import com.rsmapps.selfieall.helper.AppDialog;
import com.rsmapps.selfieall.helper.FontPojo;
import com.rsmapps.selfieall.helper.TextEditer;
import com.rsmapps.selfieall.utility.AssetUtil;
import com.rsmapps.selfieall.utility.BitmapUtil;
import com.rsmapps.selfieall.utility.NetworkUtils;
//import com.yalantis.ucrop.UCrop;

import bornander.SandboxViewTouchListener;

import com.rsmapps.selfieall.adapter.EffectAdapter;
import com.rsmapps.selfieall.helper.ResourceManager;
import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.R;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import bornander.gestures.SandboxView;
import butterknife.InjectView;
import libs.imageBackgroundEraser.BackgroundEraserActivity;
import libs.imageBackgroundEraser.utils.Resource;
import libs.imageCropper.ImageCropperActivity;
import sticker.StickerImgView;
import sticker.StickerViewTouchListener;

public class MainActivity extends BaseActivity implements OnClickListener,
        SandboxViewTouchListener, StickerViewTouchListener, TextEditer {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private static final String STICKER_SELECTED_ITEM = "sticker selected item";
    private static final String PATH = "path";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_BACKGROUND_ERASER = 619;
    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final int SELECT_GALLERY = 104;
    private static final int SELECT_CAMERA = 105;
    private static final int SELECT_KOHALI = 106;
    private static final int SELECT_PHOTO = 109;
    private static final int SELECT_STICKER = 107;
    private static final int SELECT_TEXT = 108;

    private View view;
    private SandboxView sandb;

    private Bitmap for_save;
    private EffectAdapter mAdapter;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static Uri fileUri;
    private boolean isPhoto;
    private boolean IS_SAVED = false;

    private StickerImgView snickerView;
    public static String[] effect_images, effect_images2;
    private String mCurrentPhotoPath;
    private int stickerSelectedItem = R.id.llyText;

    @InjectView(R.id.sticker_view)
    StickerView sticker_view;
    @InjectView(R.id.llyMain)
    FrameLayout llyMain;
    @InjectView(R.id.layout)
    FrameLayout lay2;
    @InjectView(R.id.opacity)
    SeekBar barOpacity;
    @InjectView(R.id.imgBack)
    ImageView imgBack;
    @InjectView(R.id.imgSave)
    ImageView imgSave;
    @InjectView(R.id.imgLockUnlock)
    ImageView imgLockUnlock;
    @InjectView(R.id.imgEffect)
    ImageView imgEffect;
    @InjectView(R.id.llyGallery)
    LinearLayout llyGallery;
    @InjectView(R.id.llyKohli)
    LinearLayout llyKohli;
    @InjectView(R.id.llyEffect)
    LinearLayout llyEffect;
    @InjectView(R.id.llyText)
    LinearLayout llyText;
    @InjectView(R.id.llySnicker)
    LinearLayout llySnicker;
    @InjectView(R.id.llBackground)
    LinearLayout llBackground;
    @InjectView(R.id.llBackgroundEraser)
    LinearLayout llBackgroundEraser;

    @InjectView(R.id.ivBackground)
    ImageView ivBackground;
    @InjectView(R.id.llPatternChooserView)
    LinearLayout llPatternChooserView;

    @InjectView(R.id.hint_layout)
    RelativeLayout rlHint;

    @InjectView(R.id.hint_lock)
    ImageView hintLock;

    @InjectView(R.id.hint_add_photo)
    ImageView hintAddPhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initVariable();
        //drawView(ResourceManager.bitmap);
        //isPhoto = true;
        //imgLockUnlock.callOnClick();

        if (getIntent().hasExtra("url")) {
            if (getIntent().getStringExtra("url").startsWith("celebrity/image")) {
                Drawable drawable = Utils.getDrawableFromAsset(this, getIntent().getStringExtra("url"));
                snickerView.addSticker(drawable);
            } else {
                new DownloadFilesTask().execute(getIntent().getStringExtra("url"));
            }
        }

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

//        if (sharedPreferences.getString("show_hints", "1").equalsIgnoreCase("1")) {
//            findViewById(R.id.banner_adview_bottom).setVisibility(View.GONE);
//            rlHint.setVisibility(View.VISIBLE);
//            hintLock.setVisibility(View.VISIBLE);
//            hintAddPhoto.setVisibility(View.VISIBLE);
//            rlHint.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //sharedPreferences.edit().putString("show_hints","0").commit();
//                    //rlHint.setVisibility(View.GONE);
//                }
//            });
//            hintLock.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    hintLock.setVisibility(View.GONE);
//                    if (hintLock.getVisibility() == View.GONE && hintAddPhoto.getVisibility() == View.GONE) {
//                        sharedPreferences.edit().putString("show_hints", "0").commit();
//                        rlHint.setVisibility(View.GONE);
//                    }
//                }
//            });
//
//            hintAddPhoto.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    hintAddPhoto.setVisibility(View.GONE);
//                    if (hintLock.getVisibility() == View.GONE && hintAddPhoto.getVisibility() == View.GONE) {
//                        sharedPreferences.edit().putString("show_hints", "0").commit();
//                        rlHint.setVisibility(View.GONE);
//                    }
//                }
//            });
//
//        } else {
            findViewById(R.id.banner_adview_bottom).setVisibility(View.VISIBLE);
            rlHint.setVisibility(View.GONE);
            AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
//        }
        AdsUtil.preloadInterstitialAd(this);
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));


    }

    @Override
    public void onBackPressed() {
        if (llPatternChooserView.getVisibility() == View.VISIBLE) {
            llPatternChooserView.setVisibility(View.GONE);
        } else {
            if(!IS_SAVED){
                com.aligohershabir.photocollage.view.dialogFragments.AlertDialog alertDialog =
                        new com.aligohershabir.photocollage.view.dialogFragments.AlertDialog();

                alertDialog.getAlertDialog(this, new com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.OnItemClickListener() {
                    @Override
                    public void onContinueClicked() {
                        MainActivity.super.onBackPressed();
                    }
                }).setTitle(getString(R.string.alert_title_warning))
                        .setDescription(getString(R.string.alert_desc_changes_will_be_lost))
                        .setSubtitle(getString(R.string.alert_subtitle_press_exit_or_cancel))
                        .show(getSupportFragmentManager(), com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.TAG);
            }else{
                super.onBackPressed();
            }
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
//            alertDialogBuilder.setMessage("Are you sure want to exit?");
//            alertDialogBuilder.setPositiveButton("Yes",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            MainActivity.this.finish();
//                        }
//                    });
//
//            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//            recyclerView.setVisibility(View.GONE);
//            barOpacity.setVisibility(View.GONE);
        }
    }

    private void initComponents() {

        barOpacity.setOnSeekBarChangeListener(barOpacityOnSeekBarChangeListener);
        imgLockUnlock.setBackgroundResource(R.drawable.btn_unlock_bg);

        imgBack.setOnClickListener(this);
        imgSave.setOnClickListener(this);
        imgLockUnlock.setOnClickListener(this);
        llyGallery.setOnClickListener(this);
        llyKohli.setOnClickListener(this);
        llyEffect.setOnClickListener(this);
        llyText.setOnClickListener(this);
        llySnicker.setOnClickListener(this);
        llBackground.setOnClickListener(this);
        llBackgroundEraser.setOnClickListener(this);

//        loadEffectData();
//        setupRecyclerView();

        /*
         * Getting Patterns images from asstes
         */
        loadBackgroundData();
        setBackgroundView();
    }

    private void initVariable() {
        snickerView = new StickerImgView(this);
        snickerView.setStickerViewTouchListener(this);
    }

    private void loadEffectData() {

        String[] file1 = AssetUtil.getDataFromAsser(this, AssetUtil.FileType.EFFECT1);
        String[] file2 = AssetUtil.getDataFromAsser(this, AssetUtil.FileType.EFFECT2);
        effect_images = new String[file1.length];
        effect_images2 = new String[file1.length];
        for (int i = 0; i < file1.length; i++) {
            effect_images[i] = "effect1/" + file1[i];
            effect_images2[i] = "effect2/" + file2[i];
        }
    }

    public static String[] background_images;

    private void loadBackgroundData() {
        String[] selfieBackgrounds = AssetUtil.getDataFromAsser(this, AssetUtil.FileType.SELFIE_BACKGROUNDS);
        background_images = new String[selfieBackgrounds.length];
        for (int i = 0; i < selfieBackgrounds.length; i++) {
            background_images[i] = "selfieBackgrounds/" + selfieBackgrounds[i];
        }
    }

    private void setBackgroundView() {
        BackgroundAdapter patternAdapter = new BackgroundAdapter(this, background_images);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(patternAdapter);
        patternAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new com.instapic.adapters.EffectAdapter.RecyclerTouchListener(this, recyclerView,
                new com.instapic.adapters.EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Drawable colorDrawable = com.instapic.utils.Utils.getDrawableFromAsset(MainActivity.this, background_images[position]);

//                Glide.with(MainActivity.this)
//                        .load(colorDrawable)
//                        .into(imgEffect);

                if(position < 1){
                    sticker_view.setBackground(new ColorDrawable(getResources().getColor(android.R.color.white)));
                }else{
                    int height = sticker_view.getHeight();
                    int width = sticker_view.getWidth();

                    sticker_view.setBackground(new BitmapDrawable(getResources(),
                            resizeImage(((BitmapDrawable) colorDrawable).getBitmap(), width, height)));
                }

//                String url = null;
//                String shape = null;
//                String frame = null;
//                String shape1 = null;
//                if (mFeature.equals(Constant.PIP)) {
//
//                    url = arrayListGlass.get(selectedShapePosition);
//                    shape = url.replace("sample", "");
//                    frame = url.replace("Glass/sample", "");
//                    Drawable drawable = com.instapic.utils.Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
//                    Drawable drawable1 = com.instapic.utils.Utils.getDrawableFromAsset(InstaPicActivity.this, "Glass/frm" + frame);
//
//                    Bitmap result = combineTwoBitmaps(((BitmapDrawable) drawable).getBitmap(), makeMaskImage(resizeImage(
//                            ((BitmapDrawable) colorDrawable).getBitmap()), ((BitmapDrawable) drawable).getBitmap()));
//                    imgEffect.setImageBitmap(makeMaskImage(result, ((BitmapDrawable) drawable1).getBitmap()));
//
//                } else if (mFeature.equals(Constant.SHAPE)) {
//                    url = arrayListFrame.get(selectedShapePosition);
//                    shape = url.replace("_sample", "");
//                    shape1 = shape.replace("shape", "shap");
//
//                    Drawable drawable = com.instapic.utils.Utils.getDrawableFromAsset(InstaPicActivity.this, shape);
//                    Drawable border = com.instapic.utils.Utils.getDrawableFromAsset(InstaPicActivity.this, shape1);
//
//                    bitmapShape = ((BitmapDrawable) drawable).getBitmap();
//                    bitmapExtra = ((BitmapDrawable) border).getBitmap();
//
//                    bitmapFilter = makeMaskImage(combineTwoBitmaps(bitmapShape, makeMaskImage(
//                            resizeImage(((BitmapDrawable) colorDrawable).getBitmap()), bitmapShape)), bitmapExtra);
//
//                    imgEffect.setImageBitmap(bitmapFilter);
//
////                bitmapFilter = makeMaskImage(resizeImage(((BitmapDrawable)colorDrawable).getBitmap()),((BitmapDrawable)shapeDrawable).getBitmap());
////
////                imgEffect.setImageBitmap(bitmapFilter);
//
//                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }

        if(width < 1 || height < 1){
//            Log.e(TAG, "Width : " + width + ", Height : " + height);
            return bitmap;
        }

        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        return createBitmap;
    }

    SeekBar.OnSeekBarChangeListener barOpacityOnSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
//                    imgEffect.setImageAlpha(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    private void setupRecyclerView() {
        mAdapter = new EffectAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, recyclerView, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Drawable drawable = Utils.getDrawableFromAsset(MainActivity.this, MainActivity.effect_images[position]);
                imgEffect.setImageDrawable(drawable);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.setVisibility(View.GONE);
        barOpacity.setVisibility(View.GONE);
    }

    private void requestStoragePermission1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            savePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults.length > 0) {
            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted)
                openGallery();
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted)
                startCamera();
        } else if (requestCode == REQUEST_WRITE_PERMISSION && grantResults.length > 0) {
            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted)
                savePhoto();
        }

    }

    private void openGallery() {
        Intent select = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(select, "Select Picture"), SELECT_GALLERY);
    }

    void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case REQUEST_IMAGE_BACKGROUND_ERASER:

                snickerView.setUnLock();

                /**
                 * Replacing current image with new image (with removed background)
                 */
                ResourceManager.bitmap = Resource.bitmap;
//                drawView(ResourceManager.bitmap);
                snickerView.addSticker(new BitmapDrawable(getResources(), ResourceManager.bitmap), true, false);
                isPhoto = true;

                // Use this to add updated image as sticker
//                snickerView.addSticker(new BitmapDrawable(getResources(), Resource.bitmap));
                break;
            case RC_EFFECT_FILTER_ACTIVITY:

                try {



//                    drawView(ResourceManager.bitmap);
                    snickerView.addSticker(new BitmapDrawable(getResources(), ResourceManager.bitmap), true, false);
//                    snickerView.addSticker(new BitmapDrawable(getResources(), ResourceManager.bitmap));
                    isPhoto = true;
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case SELECT_GALLERY:
                try {
                    if (data != null && data.getData() != null) {
                        startCropActivity(data.getData());
                        isPhoto = true;
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                break;
            case REQUEST_TAKE_PHOTO:
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    isPhoto = true;
                    startCropActivity(imageUri);

                } catch (Exception e) {
                    return;
                }
                break;
                case SELECT_KOHALI:
            //int pos = data.getIntExtra("id", 0);
            //Drawable drawable = Utils.getDrawableFromAsset(this, KohaliActivity.kohali_images[pos]);
            //snickerView.addSticker(drawable);
            if (data.getStringExtra("url").startsWith("celebrity/image")) {
                Drawable drawable = Utils.getDrawableFromAsset(this, data.getStringExtra("url"));
                snickerView.addSticker(drawable);
            } else {
                new DownloadFilesTask().execute(data.getStringExtra("url"));
            }
                    break;
            case SELECT_PHOTO:
                try {
///*                String strImagePath = data.getStringExtra("url");
//                Bitmap bitmapOriginal = BitmapFactory.decodeFile(strImagePath);
//                int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(strImagePath), strImagePath);
//                bitmapOriginal = BitmapUtil.rotateBitmap(bitmapOriginal, rotateAngle);
//                ResourceManager.bitmap = bitmapOriginal;*/
//                    drawView(ResourceManager.bitmap);
//                    snickerView.addSticker(new BitmapDrawable(getResources(), ResourceManager.bitmap));
                    snickerView.addSticker(new BitmapDrawable(getResources(), ResourceManager.bitmap), true, true);
                    isPhoto = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SELECT_TEXT:
                snickerView.addTextSticker(MainActivity.this, TextEffectActivity.textEffectPojo);
                break;
            case SELECT_STICKER:
                snickerView.setUnLock();
                String path = data.getStringExtra(PATH);
                stickerSelectedItem = data.getIntExtra(STICKER_SELECTED_ITEM, R.id.llyText);
                Drawable drawable = Utils.getDrawableFromAsset(this, path);
                snickerView.addSticker(drawable);
                break;
//            case UCrop.REQUEST_CROP:
//                handleCropResult(data);
//                break;
        }
    }

    public void drawView(Bitmap bm) {
        if(sandb == null){
            lay2 = findViewById(R.id.layout);
            view = sandb = new SandboxView(this, bm);
            sandb.setSandboxViewListener(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);

            if (lay2.getChildCount() > 0) {
                lay2.removeAllViews();
            }
            lay2.addView(view);
            sandb.setEnabled(true);
        }else{
            sandb.setBitmap(bm);
        }
    }

    private void savePhoto() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setMessage("Do you want to save?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            llPatternChooserView.setVisibility(View.GONE);
                            snickerView.setLock();
                            bm_forSave(llyMain);
                            String path = snickerView.save(MainActivity.this, for_save);
                            if (path != null) {
                                IS_SAVED = true;
/*
                                //TODO Open rating and review screen as mentioned in docs
                                //Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                                Intent intent = new Intent(MainActivity.this, MyCreationActivity.class);
                                //intent.putExtra("Path", path);
                                intent.putExtra(MyCreationActivity.FROM, MainActivity.class.getSimpleName());
                                startActivity(intent);
                                //finish();
*/
                                AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        startSavedActivity();
                                        AdsUtil.preloadInterstitialAd(MainActivity.this);
                                    }

                                    @Override
                                    public void onAdFailedToLoad(int i) {
                                        super.onAdFailedToLoad(i);
                                        startSavedActivity();
                                        AdsUtil.preloadInterstitialAd(MainActivity.this);
                                    }
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startSavedActivity() {

        Intent savedActivityIntent = new Intent(MainActivity.this,
                com.savedPhotos.activity.MyCreationActivity.class);
        savedActivityIntent.putExtra(com.savedPhotos.activity.MyCreationActivity.FROM, TAG);
        startActivity(savedActivityIntent);

//        Intent intent = new Intent(MainActivity.this, MyCreationActivity.class);
//        intent.putExtra(MyCreationActivity.FROM, TAG);
//        startActivity(intent);
    }

    public void bm_forSave(View v) {
        for_save = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(for_save);
        v.draw(can);
    }

    public void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setMessage("Please add a Photo");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private static final int RC_EFFECT_FILTER_ACTIVITY = 123;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgSave:
//                recyclerView.setVisibility(View.GONE);
                llPatternChooserView.setVisibility(View.GONE);
                barOpacity.setVisibility(View.GONE);
                if (isPhoto) {
                    requestStoragePermission1();
                } else {
                    alert();
                }
                break;
            case R.id.imgLockUnlock:
                snickerView.setLock();
                if (isPhoto) {
                    if (sandb != null) {
                        if (sandb.isEnabled()) {
                            imgLockUnlock.setBackgroundResource(R.drawable.btn_lock_bg);
                            sandb.setEnabled(false);
                        } else {
                            imgLockUnlock.setBackgroundResource(R.drawable.btn_unlock_bg);
                            sandb.setEnabled(true);
                        }
                    }
                } else {
                    alert();
                }
                break;
            case R.id.llyGallery:
                Intent intentPhotoSelectActivity = new Intent(this,PhotoSelectActivity.class);
                intentPhotoSelectActivity.putExtra("request","100");
                startActivityForResult(intentPhotoSelectActivity,SELECT_PHOTO);
                //           AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    Intent intent = new Intent(MainActivity.this, PhotoSelectActivity.class);
//                    intent.putExtra("request", "100");
//                    startActivityForResult(intent, SELECT_PHOTO);
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    Intent intent = new Intent(MainActivity.this, PhotoSelectActivity.class);
//                    intent.putExtra("request", "100");
//                    startActivityForResult(intent, SELECT_PHOTO);
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//            });
                break;
            case R.id.llyKohli:
                snickerView.setLock();
//                recyclerView.setVisibility(View.GONE);
                llPatternChooserView.setVisibility(View.GONE);
                barOpacity.setVisibility(View.GONE);
                Intent intentKohaliActivity = new Intent(MainActivity.this, KohaliActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentKohaliActivity.putExtra("request","100");
                startActivityForResult(intentKohaliActivity, SELECT_KOHALI);
                //           AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    snickerView.setLock();
//                    recyclerView.setVisibility(View.GONE);
//                    barOpacity.setVisibility(View.GONE);
//                    Intent intent = new Intent(MainActivity.this, KohaliActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("request", "100");
//                    startActivityForResult(intent, SELECT_KOHALI);
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    snickerView.setLock();
//                    recyclerView.setVisibility(View.GONE);
//                    barOpacity.setVisibility(View.GONE);
//                    Intent intent = new Intent(MainActivity.this, KohaliActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("request", "100");
//                    startActivityForResult(intent, SELECT_KOHALI);
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//            });
                break;
            case R.id.llyEffect:
                snickerView.setLock();

                if(!isPhoto){
                    alert();
                }else{
                    Intent intentEffectFilterActivity = new Intent(MainActivity.this, EffectsFilterActivity.class);
                    intentEffectFilterActivity.putExtra("request", MainActivity.TAG);
                    startActivityForResult(intentEffectFilterActivity, RC_EFFECT_FILTER_ACTIVITY);
                }

                /*if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    barOpacity.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    barOpacity.setVisibility(View.VISIBLE);
                }*/

                //            AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    snickerView.setLock();
//                    if (recyclerView.getVisibility() == View.VISIBLE) {
//                        recyclerView.setVisibility(View.GONE);
//                        barOpacity.setVisibility(View.GONE);
//                    } else {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        barOpacity.setVisibility(View.VISIBLE);
//                    }
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    snickerView.setLock();
//                    if (recyclerView.getVisibility() == View.VISIBLE) {
//                        recyclerView.setVisibility(View.GONE);
//                        barOpacity.setVisibility(View.GONE);
//                    } else {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        barOpacity.setVisibility(View.VISIBLE);
//                    }
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//            });
                break;
            case R.id.llyText:
                snickerView.setLock();
//                recyclerView.setVisibility(View.GONE);
                llPatternChooserView.setVisibility(View.GONE);
                barOpacity.setVisibility(View.GONE);
                new AppDialog(this,this).show();
                //            AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    snickerView.setLock();
//                    recyclerView.setVisibility(View.GONE);
//                    barOpacity.setVisibility(View.GONE);
//                    new AppDialog(MainActivity.this, MainActivity.this).show();
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    snickerView.setLock();
//                    recyclerView.setVisibility(View.GONE);
//                    barOpacity.setVisibility(View.GONE);
//                    new AppDialog(MainActivity.this, MainActivity.this).show();
//                    AdsUtil.preloadInterstitialAd(MainActivity.this);
//                }
//            });
                break;
            case R.id.llySnicker:
                snickerView.setLock();
//                recyclerView.setVisibility(View.GONE);
                llPatternChooserView.setVisibility(View.GONE);
                barOpacity.setVisibility(View.GONE);

                getStickersDialog();
//                getMultiStickersDialog();


//                Intent intentStickerActivity = new Intent(MainActivity.this, StickerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intentStickerActivity.putExtra(STICKER_SELECTED_ITEM,stickerSelectedItem);
//                startActivityForResult(intentStickerActivity, SELECT_STICKER);
                break;
            case R.id.llBackground:
                llPatternChooserView.setVisibility((llPatternChooserView.getVisibility() == View.VISIBLE)?View.GONE:View.VISIBLE);
                break;
            case R.id.llBackgroundEraser:
                if(!isPhoto){
                    alert();
                }else{
                    snickerView.setLock();
                    Intent intent = new Intent(MainActivity.this, BackgroundEraserActivity.class);
                    Resource.bitmap = ResourceManager.bitmap;
                    startActivityForResult(intent,REQUEST_IMAGE_BACKGROUND_ERASER);
                }
                break;
        }
    }

    private void getMultiStickersDialog() {
        StickersDialog stickersDialog = new StickersDialog();
        stickersDialog.setOnStickerSelectedListener(new StickersDialog.OnStickerSelectedListener() {
            @Override
            public void onStickerSelected(String path) {
                snickerView.setUnLock();
                Drawable drawable = Utils
                        .getDrawableFromAsset(MainActivity.this, path);
                snickerView.addSticker(drawable);
            }
        });
        stickersDialog.show(getSupportFragmentManager(), StickersDialog.TAG);
    }

    private void getStickersDialog() {
        com.instapic.dialogFragments.StickersDialog stickersDialog = new com.instapic.dialogFragments.StickersDialog();

        stickersDialog.setOnStickerSelectedListener(new com.instapic.dialogFragments.StickersDialog.OnStickerSelectedListener() {
            @Override
            public void onStickerSelected(String path) {
                snickerView.setUnLock();
                Drawable drawable = com.rsmapps.selfieall.helper.Utils
                        .getDrawableFromAsset(MainActivity.this, path);
                snickerView.addSticker(drawable);
            }
        });
        stickersDialog.show(getSupportFragmentManager(), com.instapic.dialogFragments.StickersDialog.TAG);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                return;
            }
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onSandboxViewTouch(View v, MotionEvent event) {
        snickerView.setUnLock();
        llPatternChooserView.setVisibility(View.GONE);
//        if (recyclerView.getVisibility() == View.VISIBLE) {
//            recyclerView.setVisibility(View.GONE);
//            barOpacity.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onStickerViewTouchTouch(View v, MotionEvent event) {
        snickerView.setUnLock();
        llPatternChooserView.setVisibility(View.GONE);
//        if (recyclerView.getVisibility() == View.VISIBLE) {
//            recyclerView.setVisibility(View.GONE);
//            barOpacity.setVisibility(View.GONE);
//        }
    }

    private void startCropActivity(@NonNull Uri uri) {
//        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
//        destinationFileName += ".png";
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//        uCrop.start(MainActivity.this);

        Intent startImageCropperActivity = new Intent(MainActivity.this, ImageCropperActivity.class);
        startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(uri));
        startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);
    }

//    private void handleCropResult(@NonNull Intent result) {
//        if (result == null)
//            return;
//        final Uri resultUri = UCrop.getOutput(result);
//        if (resultUri == null)
//            return;
//        String imgPath = resultUri.getPath();
//        if (imgPath == null)
//            return;
//
//        Intent intent = new Intent(MainActivity.this, EffectsFilterActivity.class);
//        intent.putExtra("IMG_PATH", imgPath);
//        startActivity(intent);
//        finish();
//    }

    @Override
    public void onTextEditingCompleted(final FontPojo fontPojo) {

        AdsUtil.showInterstitialAd(MainActivity.this, new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (fontPojo != null && fontPojo.getText() != null && fontPojo.getText().trim().length() > 0)
                    snickerView.addTextSticker(MainActivity.this, fontPojo);
                AdsUtil.preloadInterstitialAd(MainActivity.this);

            }

            @Override
            public void onAdClosed() {
                super.onAdLoaded();
                if (fontPojo != null && fontPojo.getText() != null && fontPojo.getText().trim().length() > 0)
                    snickerView.addTextSticker(MainActivity.this, fontPojo);
                AdsUtil.preloadInterstitialAd(MainActivity.this);
            }
        });
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Drawable> {

        NetworkUtils networkUtils;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkUtils = new NetworkUtils(MainActivity.this);
            networkUtils.showProgress();
        }

        protected Drawable doInBackground(String... urls) {
            Drawable bitmapDrawable = null;
            try {

                Bitmap bitmap = null;
                if (!Patterns.WEB_URL.matcher(urls[0]).matches()) {
                    bitmap = getBitmapFromPath(urls[0]);
                } else {
                    bitmap = Utils.drawableFromUrl(MainActivity.this, urls[0]);
                }
                bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                if (!isLocalCopyExist(urls[0])) {
                    String filePath = saveToInternalStorage(bitmap, new URL(urls[0]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapDrawable;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Drawable result) {
            networkUtils.hideProgress();
            if (result != null) {
                snickerView.addSticker(result);
            }
        }


    }

    @NonNull
    private String saveToInternalStorage(Bitmap bitmapImage, URL webUrl) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = getLocalStorageDir();
        // Create imageDir
        File mypath = new File(directory, FilenameUtils.getName(webUrl.getPath()));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Utils.addLocalImage(MainActivity.this, mypath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private Boolean isLocalCopyExist(String path) {
        URL webUrl = null;
        try {
            webUrl = new URL(path);
        } catch (Exception e) {
            return true;
        }
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = getLocalStorageDir();
        File mypath = new File(directory, FilenameUtils.getName(webUrl.getPath()));
        if (mypath.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public Bitmap getBitmapFromPath(String path) {
        Bitmap bitmap = null;
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public File getLocalStorageDir() {
        File file = new File(getFilesDir(), "celebrity");
        if (!file.mkdirs()) {
        }
        return file;
    }

}

