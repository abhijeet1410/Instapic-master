package com.rsmapps.selfieall.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.rsmapps.selfieall.R;


public class ImageLoadingUtils {
	private Context context;
	public Bitmap icon;
	
	public ImageLoadingUtils(Context context){
		this.context = context;
		icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    final float totalPixels = width * height;
	    final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
	    return inSampleSize;
	}
}
