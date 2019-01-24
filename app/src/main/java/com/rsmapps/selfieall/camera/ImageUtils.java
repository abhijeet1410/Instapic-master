package com.rsmapps.selfieall.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class ImageUtils {
	
	private Activity activity;
	private ImageLoadingUtils utils;
	private Bitmap bitmap;
	private String filePath;
	private static final String IMAGE_DIRECTORY_NAME = "CWFiWatch";
	
	public ImageUtils(Activity activity) {
		this.activity = activity;
		utils = new ImageLoadingUtils(activity);
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	 /**
     * Checking device has camera hardware or not
     * */
    public boolean isDeviceSupportCamera() {
        if (activity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
	
	public String compressImage(String imageUri,float maxHeight, float maxWidth) {
		
		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;						
		Bitmap bmp = BitmapFactory.decodeFile(filePath,options);
		
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
//		float maxHeight = 816.0f;
//		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;     
				
			}
		}
				
		options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16*1024];
			
		try{	
			bmp = BitmapFactory.decodeFile(filePath,options);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}
		try{
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}
						
		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float)options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;
			
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);
		
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			
			bitmap = scaledBitmap;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return filename;
	}
	
	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}
	
	private String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(),IMAGE_DIRECTORY_NAME);
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"+ System.currentTimeMillis() + ".jpg");
		return uriSting;
	}
}
