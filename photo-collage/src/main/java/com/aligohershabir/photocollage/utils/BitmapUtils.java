package com.aligohershabir.photocollage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.aligohershabir.photocollage.model.Path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

//import android.util.Log;

public abstract class BitmapUtils {
    public static final String TAG = BitmapUtils.class.getSimpleName();
    private BitmapUtils(){

    }

    public static final int MAX_IMAGE_RESIZE = 1280;
    public static final int MAX_IMAGE_SIZE = 400;
    public static final int MAX_IMAGE_QUALITY = 80;

    public static Drawable resizeDrawable(Context context, Drawable drawable,int height,int width){
        return new BitmapDrawable(context.getResources(),
                Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), width, height, true));
    }

    public static Drawable getDrawableFromBimap(Context context, Bitmap bitmap){
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getCompressedBitmap(Bitmap bitmap, int quality){

//        Log.i(TAG, "Compressing Bitmap...");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    public static Bitmap getInSampleSizedBitmapFromPath(Path path, int maxImageSize) {
        // Get size of image without loading into memory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path.getImage_url(), options);

//        Log.i(TAG, "initSelectedImageBitmap ===options.outWidth: " + options.outWidth+", ===options.outHeight: "+ options.outHeight);

        //Calculate scale factor with image’s size.
        options.inSampleSize = calculateInSampleSize(options, maxImageSize, maxImageSize);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        return BitmapFactory.decodeFile(path.getImage_url(), options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
