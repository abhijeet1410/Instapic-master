package com.rsmapps.selfieall.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mohammed.Irfan on 10-11-2017.
 */

public class Utils {

    public static Drawable getDrawableFromAsset(Activity activity, String fileName) {
        try {
            InputStream ims = activity.getAssets().open(fileName);
            Drawable d = Drawable.createFromStream(ims, null);
            return d;
        } catch (IOException ex) {
            return null;
        }
    }

    public static Bitmap drawableFromUrl(Context context,String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        return x = BitmapFactory.decodeStream(input);
        //return new BitmapDrawable(context.getResources(),x);
    }

    public static Typeface getCustomTypeface(Activity activity, String fileName) {
        Typeface custom_font = Typeface.createFromAsset(activity.getAssets(),
                fileName);
        return custom_font;
    }

    public static void setFont(Activity activity, TextView textView, String fileName) {
        Typeface custom_font = Typeface.createFromAsset(activity.getAssets(),
                fileName);
        textView.setTypeface(custom_font);
    }

    public static Shader getShader(float size, int color1, int color2){
        Shader shader = new LinearGradient(0, 0, 0, size,
                new int[]{color1, color2},
                new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
        return shader;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Bitmap getTexture(Bitmap mTexture, String text) {
        Bitmap result = Bitmap.createBitmap(mTexture.getWidth(),
                mTexture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(200);
        paint.setARGB(255, 0, 0, 0);

        canvas.drawText(text, 200, 200, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mTexture, 0, 0, paint);
        paint.setXfermode(null);
         return result;
    }

    public static ArrayList<String> getLocalImages(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String existingPaths = sharedPreferences.getString("local_images","");
        String[] list = existingPaths.split("###");
        ArrayList<String> returnList = new ArrayList<>();
        for(String s:list){
            if(!s.isEmpty()){
              returnList.add(s);
            }
        }
        return returnList;
    }

    public static void addLocalImage(Activity activity,String path){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String existingPaths = sharedPreferences.getString("local_images","");
        existingPaths = existingPaths + "###" + path;
        sharedPreferences.edit().putString("local_images",existingPaths).commit();
    }
}
