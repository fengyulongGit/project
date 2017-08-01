package com.android.common.utils;

import android.content.pm.PackageManager;

import com.android.common.app.BaseApp;

/**
 * Created by fengyulong on 2016/7/22.
 */
public class CommonUtils {
    private static String versionName;
    private static int versionCode;
    private static String channel;
    private static boolean DEBUG = true;

    public static boolean DEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        CommonUtils.DEBUG = DEBUG;
    }

    public static String getVersionName() {
        if (ValidateUtil.isEmpty(versionName)) {
            try {
                versionName = BaseApp.getInstance().getPackageManager().getPackageInfo(BaseApp.getInstance().getPackageName(), 0).versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    public static int getVersionCode() {
        if (versionCode < 1) {
            try {
                versionCode = BaseApp.getInstance().getPackageManager().getPackageInfo(BaseApp.getInstance().getPackageName(), 0).versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }

    public static String getChannel() {
        if (ValidateUtil.isEmpty(channel)) {
            try {
                channel = BaseApp.getInstance().getPackageManager()
                        .getApplicationInfo(BaseApp.getInstance().getPackageName(), PackageManager.GET_META_DATA)
                        .metaData.getString("UMENG_CHANNEL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return channel;
    }
}
