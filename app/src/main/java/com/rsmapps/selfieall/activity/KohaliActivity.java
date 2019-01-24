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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.fragment.HomeFragment;
import com.rsmapps.selfieall.fragment.IndustryListFragment;
import com.rsmapps.selfieall.model.Celebrity;
//import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import libs.imageCropper.ImageCropperActivity;
import libs.imageCropper.utils.ResourceManager;

public class KohaliActivity extends AppCompatActivity {

    public static final String TAG = KohaliActivity.class.getSimpleName();

    private String mSelectedImage = "";

    //public static String kohali_images[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadCelebrity();
        setContentView(R.layout.activity_kohali);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_top));
        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

        if(getIntent().hasExtra("request")) {
            //replaceFragment(new IndustryListFragment(), IndustryListFragment.class.getSimpleName(), false, true);
            replaceFragment(HomeFragment.newInstance("","Home"), HomeFragment.class.getSimpleName(), false, true);
        }else {
            replaceFragment(HomeFragment.newInstance("","Home"), HomeFragment.class.getSimpleName(), false, true);
        }
    }

    public static Celebrity getOfflineCelebrity() {
        //kohali_images = new String[73];
        Celebrity celebrity = new Celebrity();
        celebrity.setId("0");
        celebrity.setName("Celebrity");
        for (int i = 0; i < 10; i++) {
            int index = i + 1;
            if(index != 2 && index != 3 && index != 9)
                celebrity.getImages().add("celebrity/image (" + index + ").png");
        }

        return celebrity;
    }

    public void replaceFragment(Fragment fragment, String tag, Boolean addToBackStack, Boolean clearBackstack) {

        if (clearBackstack) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kohali_activity, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.mMySavedPhoto:
//                startActivity(new Intent(KohaliActivity.this, com.savedPhotos.activity.MyCreationActivity.class));
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final int SELECT_GALLERY = 201;
    private static final int REQUEST_TAKE_PHOTO = 202;

    private String mCurrentPhotoPath;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }

    public void requestGalleryPermission(String selectedUrl) {
        mSelectedImage = selectedUrl;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
                return;
            }
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
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
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY) {
            try{
                startCropActivity(data.getData());
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                startCropActivity(imageUri);
            }catch (Exception e) {
                return;
            }
//        } else if (requestCode == UCrop.REQUEST_CROP) {
//            handleCropResult(data);
        } else if(requestCode == ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY){
            handleCropResult(getImageUri(KohaliActivity.this, ResourceManager.bitmap));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCropActivity(@NonNull Uri uri) {
//        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
//        destinationFileName += ".png";
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//        uCrop.start(KohaliActivity.this);


        Intent startImageCropperActivity = new Intent(KohaliActivity.this, ImageCropperActivity.class);
        startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(uri));
        startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);
    }

    private void handleCropResult(Uri imageUri) {

        if(imageUri == null)
            return;

        String imgPath = imageUri.getPath();
        if(imgPath == null)
            return;
        if(!mSelectedImage.isEmpty()){
            //Intent intent = new Intent(KohaliActivity.this, ImagePreviewActivity.class);
            Intent intent = new Intent(KohaliActivity.this, EffectsFilterActivity.class);
            intent.putExtra("url", mSelectedImage);
            intent.putExtra("IMG_PATH", imgPath);
            startActivity(intent);
            //finish();
        }else if(getIntent().hasExtra("url")) {
            //Intent intent = new Intent(KohaliActivity.this, ImagePreviewActivity.class);
            Intent intent = new Intent(KohaliActivity.this, EffectsFilterActivity.class);
            intent.putExtra("url", getIntent().getStringExtra("url"));
            intent.putExtra("IMG_PATH", imgPath);
            startActivity(intent);
            //finish();
        }else {
            Intent intent = new Intent(KohaliActivity.this, EffectsFilterActivity.class);
            if (getIntent().hasExtra("url")) {
                intent.putExtra("url", getIntent().getStringExtra("url"));
            }
            intent.putExtra("IMG_PATH", imgPath);
            startActivity(intent);
            //finish();
        }
    }
}
