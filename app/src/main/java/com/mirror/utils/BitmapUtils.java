package com.mirror.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public abstract class BitmapUtils {

    private BitmapUtils(){

    }

    public static Uri getBitmapUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Nullable
    public static Bitmap getBitmapFromUri(AppCompatActivity activity, Intent data){
        Uri imageUri = data.getData();
        try {
            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getScaledBitmap(Bitmap orignalBitmap){
        int nh = (int) ( orignalBitmap.getHeight() * (512.0 / orignalBitmap.getWidth()) );
        return Bitmap.createScaledBitmap(orignalBitmap, 512, nh, true);
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }

//            Matrix m = new Matrix();
//            RectF drawableRect = new RectF(0, 0, image.getWidth(), image.getHeight());
//            RectF viewRect = new RectF(0, 0, width, height);
//            m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
//            image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath)throws Exception{
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

//            Log.i("RotateImage", "Exif orientation: " + orientation);
//            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)throws Exception {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
