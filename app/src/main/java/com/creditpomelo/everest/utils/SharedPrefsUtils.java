package com.creditpomelo.everest.utils;

import com.android.common.utils.SecurityUtils;
import com.android.common.utils.SharedPreferencesUtils;

/**
 * Created by fengyulong on 2016/08/19.
 */

public class SharedPrefsUtils {
    public static void putIsLogin(boolean flag) {
        SharedPreferencesUtils.putBoolean("isLogin", flag);
    }

    public static boolean isLogin() {
        return SharedPreferencesUtils.getBoolean("isLogin", false);
    }

    public static void putIsOpen(boolean flag) {
        SharedPreferencesUtils.putBoolean(getUid(), "isOpen", flag);
    }

    public static boolean isOpen() {
        return SharedPreferencesUtils.getBoolean(getUid(), "isOpen", false);
    }

    public static void putUid(String value) {
        SharedPreferencesUtils.putString("uid", value);
    }

    public static String getUid() {
        return SharedPreferencesUtils.getString("uid", "0");
    }

    public static void putIsPush(boolean flag) {
        SharedPreferencesUtils.putBoolean("IsPush", flag);
    }

    public static boolean isPush() {
        return SharedPreferencesUtils.getBoolean("IsPush", true);
    }

    public static boolean isFirstStart() {
        return SharedPreferencesUtils.getBoolean("IsFirstStart", true);
    }

    public static void putIsFirstStart(boolean flag) {
        SharedPreferencesUtils.putBoolean("IsFirstStart", flag);
    }
}
