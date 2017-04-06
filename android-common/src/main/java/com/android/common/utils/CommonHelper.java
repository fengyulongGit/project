package com.android.common.utils;

/**
 * Created by fengyulong on 2016/7/25.
 */
public class CommonHelper {

    public static void setDebugMode(boolean debugMode) {
        CommonUtils.setDEBUG(debugMode);
    }

    public static void setMainClass(Class<?> mainClass) {
        AppManager.getAppManager().setMainClass(mainClass);
    }
}
