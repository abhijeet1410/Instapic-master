package com.rsmapps.selfieall.utility;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by A1VSZHT2 on 3/27/2018.
 */

public class AssetUtil {
    public enum FileType {
        CELEBRITY(1), EFFECT1(2), EFFECT2(3), FONT(4), STICKER1(5), STICKER2(6),
        FILE_TYPE_PNG(7),PATTERN(8), SELFIE_BACKGROUNDS(9), OLD_PATTERN(10);

        private int value;
        FileType(int value) {
            this.value = value;
        }
    }

    public static String[] getDataFromAsser(Activity activity, FileType fileType) {
        AssetManager assetManager = activity.getAssets();
        String[] files = null;
        try {
            // To get names of all files inside the "Files" folder
            if(fileType.value == FileType.CELEBRITY.value){
                files = assetManager.list("celebrity");
            } else if(fileType.value == FileType.EFFECT1.value){
                files = assetManager.list("effect1");
            } else if(fileType.value == FileType.EFFECT2.value){
                files = assetManager.list("effect2");
            } else if(fileType.value == FileType.FONT.value){
                files = assetManager.list("fonts");
            } else if(fileType.value == FileType.STICKER1.value){
                files = assetManager.list("sticker1");
            } else if(fileType.value == FileType.STICKER2.value){
                files = assetManager.list("sticker2");
            }else if(fileType.value == FileType.PATTERN.value){
                files = assetManager.list("pattern");
            }else if(fileType.value == FileType.SELFIE_BACKGROUNDS.value){
                files = assetManager.list("selfieBackgrounds");
            }else if(fileType.value == FileType.OLD_PATTERN.value){
                files = assetManager.list("patternOld");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static List<String> getDataFromAsser(Activity activity, String path) {
        AssetManager assetManager = activity.getAssets();
        List<String> fileList = null;
        try {
            fileList = getAllFileFromAssetFolder(path,assetManager.list(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private static List<String> getAllFileFromAssetFolder(String folderName, String[] files){
        List<String> fileList = new ArrayList<>();
        for (int i = 0; i <files.length ; i++) {
            fileList.add(folderName+"/"+files[i]);
        }
        return fileList;
    }
}
