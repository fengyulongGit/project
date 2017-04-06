package com.youxin.purse.main.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.common.app.BaseAppCompatActivity;
import com.android.common.receiver.SystemReceiver;
import com.android.common.utils.AppUtils;
import com.umeng.analytics.MobclickAgent;
import com.youxin.purse.main.login.activity.GestureLoginActivity;
import com.youxin.purse.utils.SharedPrefsUtils;

/**
 * 基础类
 */
public abstract class AppCompatActivity extends BaseAppCompatActivity {
    public static int standbyStart = 0;// 进入后台后开始计时
    public static int screenofftime = 0;
    private TimeThread thread;
    private SystemReceiver receiver;
    private Timerthread2 thread_screen;
    private boolean isScreenOn = false;

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    standbyStart++;
                    break;
                case 2:
                    screenofftime++;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // //透明状态栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // //透明导航栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 注册广播用于手势密码
        receiver = new SystemReceiver(this);
        receiver.requestScreenStateUpdate(new SystemReceiver.ScreenStateListener() {

            @Override
            public void onScreenOn() {
                if (SharedPrefsUtils.isLogin() && isScreenOn && screenofftime > App.timeInterval
                        && SharedPrefsUtils.isOpen() && App.isAlive) {// 程序在前台，cmapp.isalive=true,时显示手势密码
                    Intent i = new Intent(context, GestureLoginActivity.class);
                    startActivity(i);
                    isScreenOn = false;
                    mHandler.removeCallbacks(thread_screen);
                }
            }

            @Override
            public void onScreenOff() {
                screenofftime = 0;
                thread_screen = new Timerthread2();
                thread_screen.start();
                isScreenOn = true;
            }
        });
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(App.isAlive) && standbyStart > App.timeInterval && SharedPrefsUtils.isLogin()
                && SharedPrefsUtils.isOpen()) {// 从后台重新回到前台且时间大于间隔跳转手势密码页面
            Intent i = new Intent(this, GestureLoginActivity.class);
            startActivity(i);
            mHandler.removeCallbacks(thread);
        }
        if (AppUtils.isAppOnForeground(context)) {
            App.isAlive = true;
        } else {
            App.isAlive = false;
        }
        // 友盟统计
        MobclickAgent.onResume(context);// 时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground(context)) {// 进入后台，开启线程计时
            App.isAlive = false;
            standbyStart = 0;
            thread = new TimeThread();
            thread.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // 注销广播
        try {
            if (receiver != null) {
                receiver.stopScreenStateUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!(AppUtils.isAppOnForeground(context)));
        }
    }

    public class Timerthread2 extends Thread {
        @Override
        public void run() {

            while (isScreenOn) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
    }
}
