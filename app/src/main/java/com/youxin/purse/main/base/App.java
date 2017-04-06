package com.youxin.purse.main.base;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.common.app.BaseApp;
import com.android.common.utils.CommonHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.youxin.purse.BuildConfig;
import com.youxin.purse.R;
import com.youxin.purse.main.MainActivity;
import com.youxin.purse.utils.PathUtils;
import com.youxin.purse.utils.SharedPrefsUtils;

import java.io.File;
import java.util.Map;

/**
 * 继承Application
 */
public class App extends BaseApp {
    public static boolean isAlive;//是否激活触发手势密码
    public static final int timeInterval = 30;// 从后台到前台的时间间隔手势密码时间间隔

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 全局异常捕获注册crashHandler
//		CMCrashHandler crashHandler = CMCrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());

        CommonHelper.setDebugMode(BuildConfig.LOG_DEBUG);
        CommonHelper.setMainClass(MainActivity.class);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(BuildConfig.LOG_DEBUG);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context,
                                              final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        UTrack.getInstance(getApplicationContext())
                                .trackMsgClick(msg);
                        // 推送的是自定义消息时在这处理
                    }
                });
            }

            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                                context);
                        RemoteViews myNotificationView = new RemoteViews(
                                context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title,
                                msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text,
                                msg.text);
                        myNotificationView.setImageViewBitmap(
                                R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(
                                R.id.notification_small_icon,
                                getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        Notification mNotification = builder.build();
                        // 由于Android
                        // v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;

                    default:
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(final Context context, UMessage msg) {

                super.launchApp(context, msg);
                final Map<String, String> map = msg.extra;

                String title = msg.title;

//                if (map.get("type").equals("jpList")) {
//                    if (AppUtils.isAppOnForeground(context)) {
//                        AppManager.getAppManager().finishAllActivity();
//                        EventBus.getDefault().post(new MainActivityChangeEvent(MainActivityChangeEvent.Event.InvestList));
//                    } else {
//                        EventBus.getDefault().post(new MainActivityChangeEvent(MainActivityChangeEvent.Event.InvestList_JING));
//                    }
//                } else if (map.get("type").equals("sbList")) {
//                    if (AppUtils.isAppOnForeground(context)) {
//                        AppManager.getAppManager().finishAllActivity();
//                        EventBus.getDefault().post(new MainActivityChangeEvent(MainActivityChangeEvent.Event.InvestList));
//                    } else {
//                        EventBus.getDefault().post(new MainActivityChangeEvent(MainActivityChangeEvent.Event.InvestList_SAN));
//                    }
//                } else if (map.get("type").equals("proDetail")) {
//                    String productCode = map.get("proCode");
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("productCode", productCode);
//                    bundle.putString("productType", "精品理财");
//                    intent.putExtras(bundle);
////					intent.setClass(context,
////							CMCompetitiveProDetailsActivity.class);
//                    intent.setClass(context,
//                            CMCompetitiveProDetailsRevActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else if (map.get("type").equals("spread")) {
//                    String url = map.get("url");
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", url);
//                    bundle.putString("activity", "CMApp");
//                    intent.putExtras(bundle);
//                    intent.setClass(context, CMShowWebViewActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        if (SharedPrefsUtils.isPush()) {
            mPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(String s, String s1) {
                }
            });
        } else {
            mPushAgent.disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(String s, String s1) {
                }
            });
        }

        DisplayImageOptions opt = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
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
