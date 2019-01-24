package com.rsmapps.selfieall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.helper.ResourceManager;
//import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.Display;

import com.rsmapps.selfieall.R;

import butterknife.InjectView;
import libs.imageCropper.ImageCropperActivity;

/**
 * Created by Mohammed.Irfan on 06-12-2017.
 */

public class PhotoSelectActivity extends BaseActivity {

    public static final String TAG = PhotoSelectActivity.class.getSimpleName();

    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final int SELECT_GALLERY = 201;
    private static final int REQUEST_TAKE_PHOTO = 202;
    private static final int REQUEST_APPLY_FILTER = 203;

    private String mCurrentPhotoPath;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    @InjectView(R.id.llyGallery)
    LinearLayout llyGallery;
    @InjectView(R.id.llyCamera)
    LinearLayout llyCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);

        llyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGalleryPermission();
            }
        });
        llyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_top));
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));


        AdsUtil.showInterstitialAd(PhotoSelectActivity.this, new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                AdsUtil.preloadInterstitialAd(PhotoSelectActivity.this);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                AdsUtil.preloadInterstitialAd(PhotoSelectActivity.this);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_READ_PERMISSION && grantResults.length > 0) {
            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer == PackageManager.PERMISSION_GRANTED) {

                } else {
                    granted = false;
                }
            }
            if (granted)
                openGallery();
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer == PackageManager.PERMISSION_GRANTED) {

                } else {
                    granted = false;
                }
            }
            if (granted)
                startCamera();
        }

    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            } else {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent select = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(select, "Select Picture"), SELECT_GALLERY);
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            if (photoFile != null) {
                //Uri photoURI = Uri.fromFile(createImageFile());
                //Uri photoURI = FileProvider.getUriForFile(PhotoSelectActivity.this, PhotoSelectActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);

                Uri photoURI = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoURI = FileProvider.getUriForFile(PhotoSelectActivity.this, PhotoSelectActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);
                } else {
                    photoURI = Uri.fromFile(createImageFile());
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
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
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.toURI().toString();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case SELECT_GALLERY:
                try {
                    startCropActivity(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_TAKE_PHOTO:
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    startCropActivity(imageUri);
                } catch (Exception e) {
                    return;
                }
                break;
            case ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY:
                handleCropResult();
                break;
                /*
            case UCrop.REQUEST_CROP:
                handleCropResult(data);
                //           AdsUtil.showInterstitialAd(PhotoSelectActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    handleCropResult(data);
//                    AdsUtil.preloadInterstitialAd(PhotoSelectActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    handleCropResult(data);
//                    AdsUtil.preloadInterstitialAd(PhotoSelectActivity.this);
//                }
//            });
                break;*/
            case REQUEST_APPLY_FILTER:
            case RC_EFFECT_FILTER_ACTIVITY:
                Intent full = new Intent();
                setResult(RESULT_OK, full);
                finish();
                break;
        }

    }

    private void startCropActivity(@NonNull Uri uri) {
//        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
//        destinationFileName += ".png";
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//        uCrop.start(PhotoSelectActivity.this);

        Intent startImageCropperActivity = new Intent(PhotoSelectActivity.this, ImageCropperActivity.class);
        startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(uri));
        startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);
    }

    private static final int RC_EFFECT_FILTER_ACTIVITY = 321;

    private void handleCropResult(){

        /**
         * 1. libs.imageCropper.utils.ResourceManager.bitmap
         * use this bitmap for EffectFilterActivity
         */

        Intent intentEffectFilterActivity = new Intent(PhotoSelectActivity.this, EffectsFilterActivity.class);
        intentEffectFilterActivity.putExtra("request", PhotoSelectActivity.TAG);
        startActivityForResult(intentEffectFilterActivity, RC_EFFECT_FILTER_ACTIVITY);

    }

//    private void handleCropResult(@NonNull Intent result) {
//        if (result == null)
//            return;
//
//        final Uri resultUri = UCrop.getOutput(result);
//        if (resultUri == null)
//            return;
//
//        String imgPath = resultUri.getPath();
//        if (imgPath == null)
//            return;
//
//        if (getIntent().hasExtra("url")) {
//            Intent intent = new Intent(PhotoSelectActivity.this, ImagePreviewActivity.class);
//            if (getIntent().hasExtra("url")) {
//                intent.putExtra("url", getIntent().getStringExtra("url"));
//            }
//            intent.putExtra("IMG_PATH", imgPath);
//            startActivity(intent);
//            finish();
//        }/*else if(getIntent().hasExtra("request")){
//            Intent full = new Intent();
//            full.putExtra("id", 0);
//            full.putExtra("url", imgPath);
//            setResult(RESULT_OK, full);
//            finish();
//        } */ else if (getIntent().hasExtra("request")) {
//            Intent intent = new Intent(PhotoSelectActivity.this, EffectsFilterActivity.class);
//            if (getIntent().hasExtra("url")) {
//                intent.putExtra("url", getIntent().getStringExtra("url"));
//            }
//            intent.putExtra("IMG_PATH", imgPath);
//            startActivityForResult(intent, REQUEST_APPLY_FILTER);
//            //finish();
//        }
//    }


}
