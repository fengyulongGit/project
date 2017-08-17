package com.creditpomelo.accounts.main.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.android.common.app.BaseApp;
import com.android.common.utils.CommonHelper;
import com.android.common.utils.LogUtils;
import com.creditpomelo.accounts.BuildConfig;
import com.creditpomelo.accounts.main.main.activity.MainActivity;
import com.creditpomelo.accounts.utils.PathUtils;
import com.creditpomelo.accounts.utils.SharedPrefsUtils;
import com.creditpomelo.accounts.utils.UmengPushUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * 继承Application
 */
public class App extends BaseApp {
    public static boolean isAlive;//是否激活触发手势密码
    public static final int timeInterval = 30;// 从后台到前台的时间间隔手势密码时间间隔

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置通用的异常处理
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable exp) {
                LogUtils.e("accounts-uncaught-exception", exp);
                System.exit(1);
            }
        });


        //友盟统计日志加密设置
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        MobclickAgent.setDebugMode(BuildConfig.LOG_DEBUG); //true为集成测试模式和普通测试模式，false为线上环境
        MobclickAgent.openActivityDurationTrack(false); //禁止默认的页面统计方式，这样将不会再自动统计Activity

        CommonHelper.setDebugMode(BuildConfig.LOG_DEBUG);
        CommonHelper.setMainClass(MainActivity.class);

        //Umeng推送
        UmengPushUtils.init(this);
        if (SharedPrefsUtils.isPush()) {
            UmengPushUtils.enable(this);
        } else {
            UmengPushUtils.disable(this);
        }
        if (SharedPrefsUtils.isLogin()) {
            UmengPushUtils.addAlias(this, SharedPrefsUtils.getUid());
        }

        DisplayImageOptions opt = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.ic_launcher)
//                .showImageForEmptyUri(R.drawable.ic_launcher)
//                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.PNG, 75, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000)
                // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(new File(PathUtils.CacheFilePath())))
                // 自定义缓存路径
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .defaultDisplayImageOptions(opt)
                .imageDownloader(new BaseImageDownloader(getBaseContext(), 5 * 1000, 30 * 1000))
                // connectTimeout(5 s),
                // readTimeout(30s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建

        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
