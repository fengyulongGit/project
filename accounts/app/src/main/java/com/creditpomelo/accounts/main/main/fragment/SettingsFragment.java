package com.creditpomelo.accounts.main.main.fragment;

import android.os.Bundle;

import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.base.AppCompatActivity;
import com.creditpomelo.accounts.main.base.Fragment;
import com.creditpomelo.accounts.utils.LoginUtils;

import butterknife.OnClick;

/**
 * 视图-设置
 * Created by fengyulong on 2017/8/3.
 */

public class SettingsFragment extends Fragment {
    @Override
    protected int layoutTitleResID() {
        return R.layout.layout_title;
    }

    @Override
    protected int layoutContentResID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        tv_title.setText("设置");

    }

    @Override
    protected void userVisibleHint() {

    }

    @Override
    protected void requestData() {

    }

    /**
     * 修改密码
     */
    @OnClick(R.id.btn_modify_password)
    void modifyPassword() {
//        openActivity(ModifyPasswordActivity.class);
    }

    /**
     * 退出
     */
    @OnClick(R.id.btn_logout)
    void logout() {
        LoginUtils.logout((AppCompatActivity) getActivity());
    }
}
