package com.aligohershabir.photocollage.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final String TAG = "FileUtil";
    public static final String FOLDER_NAME = "Selfie with All Celebrity";

    public static String getFolderName(String name) {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        name);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }

    /**
     * 判断sd卡是否可以用
     */
    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static File getNewFile(Context context, String folderName) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

        String timeStamp = simpleDateFormat.format(new Date());

        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return new File(path);
    }

    public static File[] getFileList(Context context) {

        String path;
        if (isSDAvailable()) {
            path = getFolderName(FOLDER_NAME);
        } else {
            path = context.getFilesDir().getPath();
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files == null && files.length == 0) {
            return null;
        }
        return files;
    }
}

