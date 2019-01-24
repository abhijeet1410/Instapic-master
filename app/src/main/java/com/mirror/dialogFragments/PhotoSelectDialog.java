package com.mirror.dialogFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.rsmapps.selfieall.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PhotoSelectDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = PhotoSelectDialog.class.getSimpleName();

    public static final String GALLERY_BITMAP = "galleryBitmap";
    public static final String CAMERA_BITMAP = "cameraBitmap";
    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final int SELECT_GALLERY = 201;
    private static final int REQUEST_TAKE_PHOTO = 202;

    private static final String[] CAMERA_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final String[] GALLERY_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String mCurrentPhotoPath;

    private Context context;
    private OnPhotoSelectDialogResult onPhotoSelectDialogResult;

    public static PhotoSelectDialog getPhotoSelectDialog(OnPhotoSelectDialogResult onPhotoSelectDialogResult){
        PhotoSelectDialog photoSelectDialog = new PhotoSelectDialog();
        photoSelectDialog.onPhotoSelectDialogResult = onPhotoSelectDialogResult;
        return photoSelectDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private LinearLayout llyGallery, llyCamera;

    private void getInfo(View view){
        llyGallery = view.findViewById(R.id.llyGallery);
        llyCamera = view.findViewById(R.id.llyCamera);
    }

    private void setInfo(){
        llyGallery.setOnClickListener(this);
        llyCamera.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_photo_select,container,false);
        getInfo(view);
        setInfo();
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llyCamera:
                requestCameraPermission();
                break;
            case R.id.llyGallery:
                requestGalleryPermission();
                break;
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
            } else {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = Uri.fromFile(createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }

            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void requestGalleryPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(GALLERY_PERMISSIONS, REQUEST_READ_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent select = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(select, "Select Picture"), SELECT_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == SELECT_GALLERY) {
                try {
                    onPhotoSelectDialogResult.onPhotoSelectDialogResult(
                            MediaStore.Images.Media.getBitmap(
                                    context.getContentResolver(),
                                    data.getData()
                            ),GALLERY_BITMAP);
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == REQUEST_TAKE_PHOTO){
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                try {
                    onPhotoSelectDialogResult.onPhotoSelectDialogResult(
                            MediaStore.Images.Media.getBitmap(
                                    context.getContentResolver(),
                                    imageUri)
                            ,CAMERA_BITMAP+","+imageUri);
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public interface OnPhotoSelectDialogResult{
        void onPhotoSelectDialogResult(Bitmap bitmap, String bitmapSource);
    }
}
