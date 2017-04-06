package com.youxin.purse.main;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.youxin.purse.R;
import com.youxin.purse.main.base.AppCompatActivity;
import com.youxin.purse.utils.PathUtils;

public class SplashActivity extends AppCompatActivity {

    private long splashTime = 3000;//启动闪屏页停留时间
    private boolean isPaused = false;//是否切换到后台


    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            PathUtils.setSdCard(context.getFilesDir() + "/");
        } else {
            PathUtils.setSdCard(Environment.getExternalStorageDirectory().toString());
        }

        setContentView(R.layout.activity_splash);

        splashTimer.start();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PathUtils.setSdCard(Environment.getExternalStorageDirectory().toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MobclickAgent.onKillProcess(context);
        System.exit(0);
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

    Thread splashTimer = new Thread() {
        public void run() {
            try {
                long ms = 0;
                while (ms < splashTime) {
                    sleep(100);
                    if (!isPaused) {
                        ms += 100;
                    }
                }
                // 是否是第一次安装，是进入引导页否直接进首页
//                if (SharedPrefsUtils.isFirstStart()) {
//                    startActivity(new Intent(context, CMNewbieguideActivity.class));
//                } else {
//                    if (SharedPrefsUtils.isLogin() && SharedPrefsUtils.isOpen()) {
//                        Intent intent = new Intent(context, CMGestureLoginActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("splash", true);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
//                    }
//                }
            } catch (Exception ex) {
            } finally {
                finish();
            }
        }
    };
}
