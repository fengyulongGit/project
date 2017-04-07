package com.creditpomelo.everest.main.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.gesturelock.GestureLockView;
import com.android.common.utils.AppManager;
import com.umeng.analytics.MobclickAgent;
import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.MainActivity;
import com.creditpomelo.everest.main.base.AppCompatActivity;
import com.creditpomelo.everest.utils.SharedPrefsUtils;

public class GestureLoginActivity extends AppCompatActivity {
    //
    private GestureLockView gestureLockView;
    private long exitTime = 0;
    private TextView textView_gesture_tip;
    private TextView textView_gesture_forget;
    private TextView textView_gesture_userOtherLogin;
    //
    //
    private String str_activity;// 更多隐私设置关闭时跳转手势密码传bundle值区分
    //
    /**
     * 密码错误次数限制
     */
    private int errorLimitCount = 5;
    /**
     * 密码错误次数
     */
    private int errorCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        initViewData();
    }

    private void initViewData() {
        setContentView(R.layout.gesturelogin_activity);
        // share里存的uid
        String username = "";
        TextView textView_gesture_user = (TextView) findViewById(R.id.textView_gesture_user);
        textView_gesture_user.setText(username);
        //
        gestureLockView = (GestureLockView) findViewById(R.id.gestureLockView_pwd);
        textView_gesture_tip = (TextView) findViewById(R.id.textView_gesture_tip);
        textView_gesture_tip.setVisibility(View.INVISIBLE);
        textView_gesture_userOtherLogin = (TextView) findViewById(R.id.textView_gesture_userOtherLogin);
        textView_gesture_userOtherLogin
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showDialog("是否用其他账号登录?", "取消", new ConfirmDialog.OnBtnLeftClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "确定", new ConfirmDialog.OnBtnRightClickListener() {
                            @Override
                            public void onClick(View v) {
//                                SharedPrefsUtils.putIsLogIn(false);
//                                SharedPrefsUtils.putUserName("");
//                                SharedPrefsUtils.putToken("token");
                                CookieManager cookieManager = CookieManager.getInstance();
                                cookieManager.removeAllCookie();
                                //点击事件
//                                Intent intent = new Intent(context, CMLoginActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("log", "intent");
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    }
                });
        textView_gesture_forget = (TextView) findViewById(R.id.textView_gesture_forget);
        textView_gesture_forget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //忘记手势密码
                showDialog("是否重新登录", "取消", new ConfirmDialog.OnBtnLeftClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }, "确定", new ConfirmDialog.OnBtnRightClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefsUtils.putIsOpen(false);
                        SharedPrefsUtils.putIsLogin(false);
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.removeAllCookie();
//                        Intent intent = new Intent(context, CMLoginActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("log", "intent");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
                    }
                });
            }
        });

        gestureLockView.setCallBack(new GestureLockView.GestureLockCallBack() {
            public void onStartGesture() {
                textView_gesture_tip.setTextColor(Color.WHITE);
            }

            public void onGesturecomplete(String pwd) {
                if (pwd.length() <= 0) {
                    return;
                }
                if (pwd.length() < 4) {
                    textView_gesture_tip.setVisibility(View.VISIBLE);
                    textView_gesture_tip.setTextColor(Color.RED);
                    textView_gesture_tip.setText("至少连接4个点，请重新绘制");
                    return;
                }
//                if (SecurityUtils.Md5(pwd).equals(SharedPrefsUtils.getPassword())) {// 密码正确
//                    // 手势密码正确，进行手势密码登录
//                    sendRequestByGesturePwd();
//                    EventBus.getDefault().post(new PrivacySettingEvent());// 是隐私设置要关闭
//                    //
//                    errorCount = 0;
//                } else {// 密码错误
//                    errorCount++;
//                    if (errorCount >= errorLimitCount) {
//                        textView_gesture_tip.setVisibility(View.VISIBLE);
//                        textView_gesture_tip.setTextColor(Color.RED);
//                        textView_gesture_tip.setText("手势密码已被锁定,请点击忘记手势密码重新登录");
//                        gestureLockView.setLock(true);
//                        // 锁定关闭手势密码
//                        SharedPrefsUtils.putIsOpen(false);
//                        return;
//                    }
//                    textView_gesture_tip.setVisibility(View.VISIBLE);
//                    textView_gesture_tip.setTextColor(Color.RED);
//                    textView_gesture_tip.setText("密码错误" + errorCount
//                            + "次，请重新绘制");
//                }

            }

            @Override
            public void onDrawGesture(boolean[][] selected) {

            }
        });

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            str_activity = bundle.getString("activity");
        }
    }

    @Override
    public void initView() {
    }

    @Override
    protected void initData() {
    }

    /**
     * 按两次退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this, true);
                MobclickAgent.onKillProcess(context);
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 手势密码登录
     */
    @SuppressLint("ShowToast")
    private void sendRequestByGesturePwd() {
        showToast("手势密码解锁成功！");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("splash")) {
                startActivity(new Intent(context, MainActivity.class));
                this.finish();
            }
        }
        this.finish();
    }
}
