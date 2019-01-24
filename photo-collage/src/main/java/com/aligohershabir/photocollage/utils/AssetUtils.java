package com.aligohershabir.photocollage.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public abstract class AssetUtils {

    public static final String FILE_PATTERN = "pattern";
    public static final String FILE_COLLAGE_FRAMES = "collage_frames";
    public static final String FILE_COLLAGE_SHAPES = "collage_shapes";
    public static final String FILE_COLLAGE_SHAPES_EFFECTS = "collage_shape_effects";
    public static final String FILE_COLLAGE_SHAPES_STROKE = "collage_shape_stroke";
    public static final String FILE_COLLAGE_SHAPES_MASK = "collage_masks";

    private AssetUtils(){

    }

    public static final String FILE_ONE_IMAGE_GRID = "collage/oneImageGrid";
    public static final String FILE_TWO_IMAGE_GRID = "collage/twoImageGrid";
    public static final String FILE_THREE_IMAGE_GRID = "collage/threeImageGrid";
    public static final String FILE_FOUR_IMAGE_GRID = "collage/fourImageGrid";
    public static final String FILE_FIVE_IMAGE_GRID = "collage/fiveImageGrid";
    public static final String FILE_SIX_IMAGE_GRID = "collage/sixImageGrid";
    public static final String FILE_SEVEN_IMAGE_GRID = "collage/sevenImageGrid";
    public static final String FILE_EIGHT_IMAGE_GRID = "collage/eightImageGrid";
    public static final String FILE_NINE_IMAGE_GRID = "collage/nineImageGrid";

    public enum FileType {
        ONE_IMAGE_GRID(1), TWO_IMAGE_GRID(2), THREE_IMAGE_GRID(3), FOUR_IMAGE_GRID(4), FIVE_IMAGE_GRID(5), SIX_IMAGE_GRID(6),
        SEVEN_IMAGE_GRID(7),EIGHT_IMAGE_GRID(8),NINE_IMAGE_GRID(9),PATTERN(10),FRAME(11),SHAPE(12),SHAPE_EFFECTS(13), SHAPE_STROKE(14)
        ,SHAPE_MASK(15);

        private int value;
        FileType(int value) {
            this.value = value;
        }
    }

    public static String[] getDataFromAsset(Activity activity, FileType fileType) {
        AssetManager assetManager = activity.getAssets();
        String[] files = null;
        try {
            // To get names of all files inside the "Files" folder
            if(fileType.value == FileType.ONE_IMAGE_GRID.value){
                files = assetManager.list(FILE_ONE_IMAGE_GRID);
            } else if(fileType.value == FileType.TWO_IMAGE_GRID.value){
                files = assetManager.list(FILE_TWO_IMAGE_GRID);
            } else if(fileType.value == FileType.THREE_IMAGE_GRID.value){
                files = assetManager.list(FILE_THREE_IMAGE_GRID);
            } else if(fileType.value == FileType.FOUR_IMAGE_GRID.value){
                files = assetManager.list(FILE_FOUR_IMAGE_GRID);
            } else if(fileType.value == FileType.FIVE_IMAGE_GRID.value){
                files = assetManager.list(FILE_FIVE_IMAGE_GRID);
            } else if(fileType.value == FileType.SIX_IMAGE_GRID.value){
                files = assetManager.list(FILE_SIX_IMAGE_GRID);
            } else if(fileType.value == FileType.SEVEN_IMAGE_GRID.value){
                files = assetManager.list(FILE_SEVEN_IMAGE_GRID);
            } else if(fileType.value == FileType.EIGHT_IMAGE_GRID.value){
                files = assetManager.list(FILE_EIGHT_IMAGE_GRID);
            } else if(fileType.value == FileType.NINE_IMAGE_GRID.value){
                files = assetManager.list(FILE_NINE_IMAGE_GRID);
            } else if(fileType.value == FileType.PATTERN.value){
                files = assetManager.list(FILE_PATTERN);
            } else if(fileType.value == FileType.FRAME.value){
                files = assetManager.list(FILE_COLLAGE_FRAMES);
            } else if(fileType.value == FileType.SHAPE.value){
                files = assetManager.list(FILE_COLLAGE_SHAPES);
            } else if(fileType.value == FileType.SHAPE_EFFECTS.value){
                files = assetManager.list(FILE_COLLAGE_SHAPES_EFFECTS);
            } else if(fileType.value == FileType.SHAPE_STROKE.value){
                files = assetManager.list(FILE_COLLAGE_SHAPES_STROKE);
            } else if(fileType.value == FileType.SHAPE_MASK.value){
                files = assetManager.list(FILE_COLLAGE_SHAPES_MASK);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Nullable
    public static Drawable getDrawableFromAsset(Activity activity, String fileName) {
        try {
            InputStream ims = activity.getAssets().open(fileName);
            return Drawable.createFromStream(ims, null);
        } catch (IOException ex) {
            return null;
        }
    }

}
