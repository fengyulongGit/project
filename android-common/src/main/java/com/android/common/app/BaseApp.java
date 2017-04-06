package com.android.common.app;

import android.app.Application;

/**
 * Created by fengyulong on 2016/7/22.
 */
public class BaseApp extends Application {
    private static BaseApp instance;

    public static BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
