package com.creditpomelo.everest.main.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.common.utils.AppManager;
import com.android.common.utils.SharedPreferencesUtils;
import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.MainActivity;
import com.creditpomelo.everest.main.base.AppCompatActivity;
import com.creditpomelo.everest.utils.SharedPrefsUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登录页面
 * Created by fengyulong on 2017/4/6.
 */

public class LoginActivity extends AppCompatActivity {
    private final int TYPE_PASSWORD = 1;//密码登录
    private final int TYPE_VALIDATE_CODE = 2;//验证码登录
    private int type = TYPE_PASSWORD;

    @Bind(R.id.ll_login_layout)
    View ll_login_layout;
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.rl_validate_code_layout)
    View rl_validate_code_layout;
    @Bind(R.id.et_validate_code)
    EditText et_validate_code;
    @Bind(R.id.tv_validate_code_send)
    TextView tv_validate_code_send;

    @Bind(R.id.tv_change_login_type)
    TextView tv_change_login_type;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        tv_title.setText("");
        tv_right.setText("注册");
        tv_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_right)
    void resgiter() {
        startActivity(new Intent(context, RegisterActivity.class));
        finish();
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_forget_password)
    void forgetPassWord() {
        startActivity(new Intent(context, ForgetPassWordActivity.class));
    }

    /**
     * 切换验证码/密码登录
     */
    @OnClick(R.id.tv_change_login_type)
    void changeLoginType() {
        switch (type) {
            case TYPE_PASSWORD: {
                type = TYPE_VALIDATE_CODE;
                tv_change_login_type.setText("验证码登录");
                et_password.setVisibility(View.VISIBLE);
                rl_validate_code_layout.setVisibility(View.GONE);
                break;
            }
            case TYPE_VALIDATE_CODE: {
                type = TYPE_PASSWORD;
                tv_change_login_type.setText("密码登录");
                et_password.setVisibility(View.GONE);
                rl_validate_code_layout.setVisibility(View.VISIBLE);
                break;
            }
        }

        et_password.setText("");
        et_validate_code.setText("");

        ll_login_layout.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_show_from_bottom);
        animation.setFillAfter(false);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_login_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_login_layout.startAnimation(animation);
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_validate_code_send)
    void sendValidateCode() {

    }

    /**
     * 登录
     */
    @OnClick(R.id.tv_login)
    void login() {
        loginSuccess();
    }

    /**
     * 登录成功
     */
    void loginSuccess() {
//        if (adjustIsGesturePwdOpen(context, uid)) {
//            if (log != null && log.equals("intent")) {
        SharedPrefsUtils.putIsLogin(true);
        AppManager.getAppManager().finishAllActivity();
        startActivity(new Intent(context, MainActivity.class));
        finish();
//            } else {
//                finish();
//            }
//        } else {
//            if (log != null && log.equals("intent")) {
//                Intent intent = new Intent(context, CMGestureSettingActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("log", "intent");
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
//            } else {
//                startActivity(new Intent(context, CMGestureSettingActivity.class));
//                finish();
//            }
//        }
    }
}
