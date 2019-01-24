package com.instapic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static Drawable getDrawableFromAsset(Activity activity, String fileName) {
        // load image
        try {
            // get input stream
            InputStream ims = activity.getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            // mImage.setImageDrawable(d);
            return d;
        } catch (IOException ex) {
            return null;
        }

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

}

