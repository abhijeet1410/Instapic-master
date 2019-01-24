package libs.imageCropper.utils;

import android.graphics.Bitmap;

public abstract class ResourceManager {
    public static final String TAG = ResourceManager.class.getSimpleName();

    private ResourceManager(){

    }

    /**
     * Use this bitmap to crop
     */
    public static Bitmap bitmap;
}
