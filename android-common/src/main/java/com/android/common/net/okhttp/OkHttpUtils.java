package com.android.common.net.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * OkHttp 工具类
 * Created by fengyulong on 2017/4/7.
 */

public class OkHttpUtils {
    private static OkHttpUtils instance;
    private OkHttpClient httpClient;

    private OkHttpUtils() {
    }

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LogInterceptor())//日志打印
//                    .addInterceptor(new TokenInterceptor())//token在header中自动刷新
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();
        }
        return httpClient;
    }
}
