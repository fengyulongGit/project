package com.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by fengyulong on 2016/8/19.
 */
public class FileUtils {

    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean copyFile(String src, String des) {
        try {
            return copyFile(new FileInputStream(src), new FileOutputStream(des));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean copyFileFromAssets(Context context, String src, String des) {
        try {
            return copyFile(context.getAssets().open(src), new FileOutputStream(des));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyFileToData(Context context, String src, String fileName) {
        try {
            return copyFile(new FileInputStream(src), context.openFileOutput(fileName, Activity.MODE_WORLD_READABLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyFileFromResources(Context context, int resourcesId, String des) {
        try {
            return copyFile(context.getResources().openRawResource(resourcesId), new FileOutputStream(des));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyFileFromData(Context context, String fileName, String des) {
        try {
            return copyFile(context.openFileInput(fileName), new FileOutputStream(des));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean copyFile(InputStream is, FileOutputStream os) throws Exception {
        byte[] bytes = new byte[1024];

        while (is.read(bytes) > 0) {
            os.write(bytes);
        }
        os.close();
        is.close();
        return true;
    }

    public static boolean renameFile(String willRenameFilePath, String newNameFilePath) {
        boolean res = false;
        try {
            if (willRenameFilePath == null || willRenameFilePath.length() == 0 || newNameFilePath == null || newNameFilePath.length() == 0) {
                return res;
            }
            File f = new File(willRenameFilePath);
            File f_1 = new File(newNameFilePath);
            res = f.renameTo(f_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
