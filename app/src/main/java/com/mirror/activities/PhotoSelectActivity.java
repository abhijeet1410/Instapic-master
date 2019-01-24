package com.mirror.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.mirror.activities.base.BaseActivity;
import com.mirror.utils.BitmapUtils;
import com.mirror.utils.Constant;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.MainActivity;
import com.rsmapps.selfieall.helper.ResourceManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.InjectView;
import libs.imageCropper.ImageCropperActivity;

public class PhotoSelectActivity extends BaseActivity {

    public static final String TAG = PhotoSelectActivity.class.getSimpleName();

    private static final int RQ_EFFECTS_FILTER_ACTIVITY = 303;
    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final int SELECT_GALLERY = 201;
    private static final int REQUEST_TAKE_PHOTO = 202;
    private static final int REQUEST_APPLY_FILTER = 203;
    private static final String[] CAMERA_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final String[] GALLERY_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String mCurrentPhotoPath;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    @InjectView(R.id.llyGallery)
    LinearLayout llyGallery;
    @InjectView(R.id.llyCamera)
    LinearLayout llyCamera;
    @InjectView(R.id.tvTitle)
    TextView tvTitle;
    @InjectView(R.id.llBackground)
    LinearLayout llBackground;
    @InjectView(R.id.ablToolbar)
    AppBarLayout ablToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selfieCall = getIntent().getStringExtra("request");
        String callType = getIntent().getStringExtra(Constant.FEATURE);
        String mirrorCall = getIntent().getStringExtra("MIRROR");

        //if(selfieCall != null){
        setTheme(R.style.PhotoSelectActivityTheme);
        //}
        setContentView(R.layout.activity_photo_select_mirror);

        if(callType != null) {
            switch (callType) {
                case Constant.PIP:
                    if (mirrorCall != null)
                        tvTitle.setText("Select Mirror Effects");
                    else
                        tvTitle.setText("Select PIP Camera");
                    break;
                case Constant.SHAPE:
                    tvTitle.setText("Select Shapes");
                    break;
            }
        }else if(selfieCall != null){
            tvTitle.setText("Select Selfie Camera");
            ablToolbar.setVisibility(View.GONE);
//            llBackground.setBackgroundColor(getResources().getColor(R.color.selfieWithCelebrityThemeColor));
        }

        llyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGalleryPermission();
            }
        });
        llyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    requestCameraPermission();
                }else{
                    Toast.makeText(PhotoSelectActivity.this, "This feature requires Camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_top));
        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

        //showFullScreenAdd();
    }

    private void showFullScreenAdd() {

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

        if(requestCode == REQUEST_READ_PERMISSION && hasPermissions(GALLERY_PERMISSIONS)){
            openGallery();
        }

        if(requestCode == REQUEST_CAMERA_PERMISSION && hasPermissions(CAMERA_PERMISSIONS)){
            startCamera();
        }

    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(GALLERY_PERMISSIONS, REQUEST_READ_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
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

                if (photoFile != null) {
                    //Uri photoURI = Uri.fromFile(createImageFile());
                    //Uri photoURI = FileProviderssss.getUriForFile(PhotoSelectActivity.this, PhotoSelectActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);

                    Uri photoURI = null;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        photoURI = FileProvider.getUriForFile(PhotoSelectActivity.this, PhotoSelectActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);
                    }else {
                        photoURI = Uri.fromFile(createImageFile());
                    }

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {


        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode) {
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:    // New  Crop library
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                Uri resultUri = result.getUri();
//                handleCropResult(resultUri);
//                break;
            case ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY:    // New  Crop library
                handleCropResult();
                break;
//            case UCrop.REQUEST_CROP:    // Old  Crop library
//                handleCropResult(data);
//                break;
            case RQ_EFFECTS_FILTER_ACTIVITY:
                if(getIntent().getStringExtra("request").equals(MainActivity.TAG)) {
                    setResult(RESULT_OK);
                    finish();
                }else{
                    startActivity(new Intent(PhotoSelectActivity.this, MainActivity.class));
                    finish();
                }
                break;
            case SELECT_GALLERY:
                try {
                    Uri uri = data.getData();
                    getBitmapFromUri(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSelectionType();
                break;
            case REQUEST_TAKE_PHOTO:
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        if (bm != null)
                            ResourceManager.bitmap  = BitmapUtils.getScaledBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    return;
                }
                getSelectionType();
                break;
        }
    }

    private void getSelectionType() {
        if (getIntent().hasExtra("MIRROR")) {
            Intent intent = new Intent(PhotoSelectActivity.this, MirrorActivity.class);
           /* if (getIntent().hasExtra("url")) {
                intent.putExtra("url", getIntent().getStringExtra("url"));
            }

            if (getIntent().hasExtra(Constant.FEATURE)){
                String feature = getIntent().getExtras().getString(Constant.FEATURE);

                intent.putExtra(Constant.FEATURE,feature);
            }*/
            //intent.putExtra("IMG_PATH", imgPath);

            startActivity(intent);
        }else if (getIntent().hasExtra("request")){
//            Intent intent = new Intent();
//            setResult(RESULT_OK,intent);
//            finish();

//            if(getIntent().getStringExtra("request").equals(MainActivity.TAG)) {
//
//            }
//            startCropActivity(BitmapUtils.getBitmapUri(PhotoSelectActivity.this, ResourceManager.bitmap));
            startCropActivity(ResourceManager.bitmap);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(-2);
        finish();
    }

//    private void startCropActivity(@NonNull Uri uri) {
//        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
//        destinationFileName += ".png";
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//        uCrop.start(PhotoSelectActivity.this);
//    }

    private void startCropActivity(/*Uri imageUri*/Bitmap bitmap){
//        String destinationFileName = CROPPED_IMAGE_NAME + FILE_TYPE_PNG;
//
//        if(imageUri != null) {
//            File file = new File(getCacheDir(), destinationFileName);
//            UCrop uCrop = UCrop.of(imageUri, Uri.fromFile(file));
//
//            uCrop.start(InstaPicActivity.this);
//        }else
//            Log.e(TAG, "Invalid image Uri for Crop." + imageUri);
//        if(imageUri != null) {

        libs.imageCropper.utils.ResourceManager.bitmap = bitmap;
            Intent startImageCropperActivity = new Intent(PhotoSelectActivity.this, ImageCropperActivity.class);
//            startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(imageUri));
            startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);

//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this);
//        }
//            Log.e(TAG, "Invalid image Uri for Crop." + null);


    }

    private void handleCropResult(){

        Bitmap bitmap = libs.imageCropper.utils.ResourceManager.bitmap;
        if(getIntent().getStringExtra("request").equals("100")){
            Intent intent = new Intent(PhotoSelectActivity.this,MainActivity.class);
            ResourceManager.bitmap  = bitmap;
            startActivity(intent);
        }else if(getIntent().getStringExtra("request").equals(MainActivity.TAG)) {
            ResourceManager.bitmap  = bitmap;
            setResult(RESULT_OK);
        }

        finish();

    }

    private void handleCropResult(Uri imageUri){
        if (imageUri == null)
            return;

        String imgPath = imageUri.getPath();
        if (imgPath == null)
            return;

        if(getIntent().getStringExtra("request").equals("100")){
            Intent intent = new Intent(PhotoSelectActivity.this,MainActivity.class);
            intent.putExtra("IMG_PATH", imgPath);
            getBitmapFromUri(imageUri);
            startActivity(intent);
        }else if(getIntent().getStringExtra("request").equals(MainActivity.TAG)) {
            getBitmapFromUri(imageUri);
            setResult(RESULT_OK);
        }

        finish();
        //startActivityForResult(intent, RQ_EFFECTS_FILTER_ACTIVITY);
    }

    private void getBitmapFromUri(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            if(bitmap != null)
                ResourceManager.bitmap  = bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        Intent intent = new Intent(PhotoSelectActivity.this, EffectsFilterActivity.class);
//        intent.putExtra("IMG_PATH", imgPath);
//        startActivity(intent);
//        finish();
//    }
}
