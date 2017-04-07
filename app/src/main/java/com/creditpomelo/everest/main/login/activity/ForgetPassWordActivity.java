package com.creditpomelo.everest.main.login.activity;

import android.view.View;

import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.base.AppCompatActivity;

import butterknife.OnClick;

/**
 * 找回密码
 * Created by fengyulong on 2017/4/7.
 */

public class ForgetPassWordActivity extends AppCompatActivity {


    @Override
    protected void initView() {
        setContentView(R.layout.activity_froget_password);
        tv_title.setText("忘记密码");
    }

    @Override
    protected void initData() {

    }

    /**
     * 确定找回密码
     */
    @OnClick(R.id.tv_commit)
    void commit() {
        finish();
    }
}
