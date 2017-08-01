package com.creditpomelo.accounts.main.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.common.app.BaseAppCompatActivity;
import com.android.common.net.base.ErrorMsg;
import com.android.common.receiver.SystemReceiver;
import com.android.common.utils.AppUtils;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.login.activity.LoginActivity;
import com.creditpomelo.accounts.utils.SharedPrefsUtils;
import com.creditpomelo.accounts.utils.UmengPushUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

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


    @BindView(R.id.layout_title)
    @Nullable
    protected View layout_title;

    @BindView(R.id.tv_left)
    @Nullable
    protected TextView tv_left;

    @BindView(R.id.tv_title)
    @Nullable
    protected TextView tv_title;

    @BindView(R.id.tv_right)
    @Nullable
    protected TextView tv_right;

    @BindView(R.id.toolbar)
    @Nullable
    protected Toolbar toolbar;

    @BindView(R.id.view_net_error)
    @Nullable
    LinearLayout view_net_error;

    @BindView(R.id.view_data_error)
    @Nullable
    LinearLayout view_data_error;

    @BindView(R.id.view_data_empty)
    @Nullable
    LinearLayout view_data_empty;

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
                        && SharedPrefsUtils.isGestureOpen() && App.isAlive) {// 程序在前台，cmapp.isalive=true,时显示手势密码
//                    openActivity(GestureLoginActivity.class);
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

        //在所有的Activity中添加统计应用启动数据
        UmengPushUtils.onAppStart(this);

    }

    @Optional
    @OnClick(R.id.tv_left)
    void onLeft() {
        left();
    }

    public void left() {
        finish();
    }

    @Optional
    @OnClick(R.id.tv_right)
    void onRight() {
        right();
    }

    public void right() {
    }

    @Optional
    @OnClick(R.id.btn_net_error_setting)
    void netErrorSetting() {
//        Intent intent = null;
//        //判断手机系统的版本  即API大于10 就是3.0或以上版本
//        if (android.os.Build.VERSION.SDK_INT > 10) {
//            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//        } else {
//            intent = new Intent();
//            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
//            intent.setComponent(component);
//            intent.setAction("android.intent.action.VIEW");
//        }
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Optional
    @OnClick({R.id.btn_data_error_refresh, R.id.btn_data_empty_refresh})
    void dataErrorRefresh() {
        refresh();
    }

    public void refresh() {
        requestData();
    }

    public void showContent() {
        setVisibilityContent(View.VISIBLE);
        setVisibilityError(View.GONE);
    }

    private void showNetError() {
        setVisibilityContent(View.GONE);
        setVisibilityError(View.VISIBLE);

        view_net_error.setVisibility(View.VISIBLE);
        view_data_error.setVisibility(View.GONE);
        view_data_empty.setVisibility(View.GONE);
    }

    private void showDataError() {
        setVisibilityContent(View.GONE);
        setVisibilityError(View.VISIBLE);

        view_net_error.setVisibility(View.GONE);
        view_data_error.setVisibility(View.VISIBLE);
        view_data_empty.setVisibility(View.GONE);
    }

    public void showDataEmpty() {
        setVisibilityContent(View.GONE);
        setVisibilityError(View.VISIBLE);

        view_net_error.setVisibility(View.GONE);
        view_data_error.setVisibility(View.GONE);
        view_data_empty.setVisibility(View.VISIBLE);
    }

    protected void showErrorView(final ErrorMsg errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMsg.isNetNone()) {
                    showToast(errorMsg.getMessage());
                    showNetError();
                } else if (errorMsg.isNetError()) {
                    showDataError();
                } else {
                    showToast(errorMsg.getMessage());
                    showDataError();
                }
            }
        });
    }

    @Override
    protected int layoutTitleResID() {
        return R.layout.layout_title;
    }

    @Override
    protected int layoutErrorResID() {
        return R.layout.layout_error;
    }

    @Override
    protected void initTitleBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (tv_left != null) {
            tv_left.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleBackground(int color) {
        if (layout_title != null) {
            layout_title.setBackgroundColor(getResources().getColor(color));
        }
//        if (toolbar != null) {
//            toolbar.setBackgroundColor(getResources().getColor(color));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(App.isAlive) && standbyStart > App.timeInterval && SharedPrefsUtils.isLogin()
                && SharedPrefsUtils.isGestureOpen()) {// 从后台重新回到前台且时间大于间隔跳转手势密码页面
//            openActivity(GestureLoginActivity.class);
            mHandler.removeCallbacks(thread);
        }
        if (AppUtils.isAppOnForeground(context)) {
            App.isAlive = true;
        } else {
            App.isAlive = false;
        }
        // 友盟统计
        MobclickAgent.onPageStart(getLocalClassName());
        MobclickAgent.onResume(context);// 时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getLocalClassName());
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

    public boolean isNotLoginAndJump2() {
        boolean flag = SharedPrefsUtils.isLogin();
        if (!flag) {
            openActivity(LoginActivity.class);
            return true;
        }
        return false;
    }
}
