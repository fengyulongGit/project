package com.creditpomelo.accounts.utils;

import android.os.Environment;

import com.android.common.utils.SecurityUtils;
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

    private static String ROOT() {
        return SdCard() + "/accounts/";
    }

    private static String CACHE() {
        return ROOT() + "cache/";
    }

    private static String IMAGE() {
        return CACHE() + "image/";
    }

    private static String CONFIG() {
        return ROOT() + "config/";
    }

    private static String APK() {
        return ROOT() + "apk/";
    }

    public static String APKFilePath(String name) {
        return returnFilePath(APK(), name);
    }

    public static String CacheFilePath() {
        return returnFilePath(CACHE(), "");
    }

    public static String ImageFilePath() {
        return returnFilePath(IMAGE(), System.currentTimeMillis() + "");
    }

    public static String ConfigFilePath(String url, String ts) {
        return returnFilePath(CONFIG(), SecurityUtils.md5(url) + "." + ts);
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
