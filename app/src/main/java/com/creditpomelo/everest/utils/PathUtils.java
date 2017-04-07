package com.creditpomelo.everest.utils;

import android.os.Environment;

import com.android.common.utils.ValidateUtil;

import java.io.File;

/**
 * 文件夹路径规则
 * Created by fengyulong on 2016/8/19.
 */
public class PathUtils {

    private static String SD_CARD;

    public static String SdCard() {
        if (ValidateUtil.isEmpty(SD_CARD)) {
            SD_CARD = Environment.getExternalStorageDirectory().toString();
        }
        return SD_CARD;
    }

    public static void setSdCard(String sdCard) {
        SD_CARD = sdCard;
    }

    public static String ROOT() {
        return SdCard() + "/purse/";
    }

    public static String CACHE() {
        return ROOT() + "cache/";
    }

    public static String APK() {
        return ROOT() + "apk/";
    }

    public static String APKFilePath(String name) {
        return returnFilePath(APK(), name);
    }

    public static String CacheFilePath() {
        return returnFilePath(CACHE(), "");
    }

    private static String returnFilePath(String fileDir, String fileName) {
        try {
            File fontsDir = new File(fileDir);
            if (!fontsDir.exists()) {
                fontsDir.mkdirs();
            }
            return fileDir + fileName;
        } catch (Exception e) {
            return "";
        }
    }
}
