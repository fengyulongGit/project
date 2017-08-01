package com.creditpomelo.accounts.main;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.android.common.net.base.ErrorMsg;
import com.android.common.net.base.Subscriber;
import com.creditpomelo.accounts.main.base.AppCompatActivity;
import com.creditpomelo.accounts.main.main.activity.MainActivity;
import com.creditpomelo.accounts.utils.PathUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private final int splashTime = 3000;//启动闪屏页停留时间
    private final int period = 100;
    private int interval = 0;
    private boolean isPaused = false;//是否切换到后台


    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    @Override
    protected int layoutContentResID() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            PathUtils.setSdCard(Environment.getDataDirectory() + "/");
        } else {
            PathUtils.setSdCard(Environment.getExternalStorageDirectory().toString());
        }

//        setContentView(R.layout.activity_splash);

        countdown();
    }

    @Override
    protected void initViewData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PathUtils.setSdCard(Environment.getExternalStorageDirectory().toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    /**
     * 更新配置
     */
    @Override
    protected void requestData() {
        //获取客服电话
//        APIRetrofitUtils.getInstance()
//                .getProxy(APIConfigService.class)
//                .getConfig(new EmptyParams().params())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<GetConfigResponse>() {
//                    @Override
//                    protected void showError(ErrorMsg errorMsg) {
//                    }
//
//                    @Override
//                    public void onNext(GetConfigResponse response) {
//                        if (ValidateUtil.isNotNull(response.getServiceHotLineText())) {
//                            SharedPrefsUtils.putServiceHotLineText(response.getServiceHotLineText());
//                        }
//                        if (ValidateUtil.isNotNull(response.getServiceHotLineTelphone())) {
//                            SharedPrefsUtils.putServiceHotLineTelphone(response.getServiceHotLineTelphone());
//                        }
//                        if (ValidateUtil.isNotNull(response.getWebUrl())) {
//                            H5ServerUrlUtils.setH5ServerUrl(response.getWebUrl());
//                        }
//                    }
//                });
    }

    /**
     * 倒计时
     */
    void countdown() {
        Subscription subscription = Observable.interval(0, period, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(splashTime / period)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return increaseTime.intValue();
                    }
                }).subscribe(new Subscriber<Integer>() {
                    @Override
                    protected void showError(ErrorMsg errorMsg) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        startApp();
                    }
                });
        addSubscription(subscription);

    }

    /**
     * 打开APP
     */
    void startApp() {
        if (!isPaused) {
            interval += period;
        }
        if (interval == splashTime) {
            interval += period;//避免多次调用
            // 是否是第一次安装，是进入引导页否直接进首页
//            if (SharedPrefsUtils.isFirstStart()) {
//                openActivity(GuidePageActivity.class);
//            } else {
//                if (SharedPrefsUtils.isLogin() && SharedPrefsUtils.isGestureOpen()) {
//                    openActivity(GestureLoginActivity.class);
//                } else {
            openActivity(MainActivity.class);
//                }
//            }
            finish();
        }
    }
}
