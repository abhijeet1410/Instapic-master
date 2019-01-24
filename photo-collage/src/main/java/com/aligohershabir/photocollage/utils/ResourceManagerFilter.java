package com.aligohershabir.photocollage.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.aligohershabir.photocollage.model.Path;

import java.util.List;

public abstract class ResourceManagerFilter {

    private ResourceManagerFilter(){

    }

    /*
     * Images selected from the image-picker-activity(library) for CollageActivity
     */
//    public static ArrayList<Image> images;

    public static List<Path> paths;

    /*
     * Selected Grid drawable from SelectGridTypeActivity.
     *
     * This will be used in CollageActivity.
     */
    public static Drawable drawable;

    public static Bitmap bitmap;
}
