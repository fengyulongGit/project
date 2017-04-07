package com.creditpomelo.everest.main.login.activity;

import android.content.Intent;

import com.android.common.utils.AppManager;
import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.MainActivity;
import com.creditpomelo.everest.main.base.AppCompatActivity;

import butterknife.OnClick;

/**
 * 注册页
 * Created by fengyulong on 2017/4/7.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        tv_title.setText("注册");
    }

    @Override
    protected void initData() {

    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_register)
    void register() {
        registerSuccess();
    }

    /**
     * 登录成功
     */
    void registerSuccess() {
//        if (adjustIsGesturePwdOpen(context, uid)) {
//            if (log != null && log.equals("intent")) {
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
