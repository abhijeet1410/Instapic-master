package com.aligohershabir.photocollage.utils;

import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public abstract class JsonUtils {

    private JsonUtils(){

    }

    public static String loadJSONFromAsset(AppCompatActivity activity, String fileAddress) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(fileAddress);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
