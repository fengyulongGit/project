package com.android.common.app;

import android.app.Application;

import com.android.common.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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

        //设置默认字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .setDefaultFontPath("fonts/PingFang_Regular.ttf")
                .build()
        );
    }
}
