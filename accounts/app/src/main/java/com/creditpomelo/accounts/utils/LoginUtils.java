package com.creditpomelo.accounts.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;

import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.utils.AppManager;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.base.App;
import com.creditpomelo.accounts.main.base.AppCompatActivity;
import com.creditpomelo.accounts.main.login.activity.LoginActivity;
import com.creditpomelo.accounts.main.main.activity.MainActivity;
import com.creditpomelo.accounts.main.web.activity.WebActivity;
import com.creditpomelo.accounts.net.api.customer.response.LoginResponse;

/**
 * 登录/退出方法
 * Created by fengyulong on 2017/5/12.
 */

public class LoginUtils {
    //必须登录的类型
    public static final String LOGIN_TYPE = "login_type";
    //必须需要登录才可以使用的功能
    public static final String INVITE_FRIEND_CREDIT_CARD = "invite_friend_credit_card";
    public static final String SHARE_FRIEND_REGISTER = "share_friend_register";

    public static void login(AppCompatActivity activity, LoginResponse response, Bundle bundle) {
        UmengPushUtils.addAlias(activity, response.getId());

        SharedPrefsUtils.putIsLogin(true);
//        SharedPrefsUtils.putAccessToken(response.getToken());
        SharedPrefsUtils.putUid(response.getId());
//        SharedPrefsUtils.putLoginUserInfo(response);
//        SharedPrefsUtils.putLastLoginMobile(response.getMobile());
//        SharedPrefsUtils.putFaceVerifyStatus(response.getFaceVerifyStatus());

//        if (SharedPrefsUtils.isGestureOpen() && ValidateUtil.isEmpty(SharedPrefsUtils.getGesturePassword())) {
//            //手势开启，但是未设置密码，则需要跳转至设置
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("is_start", true);
//            openActivity(GestureSettingActivity.class, bundle);
//        } else {
        if (bundle != null && (INVITE_FRIEND_CREDIT_CARD.equals(bundle.getString(LOGIN_TYPE)) ||
                SHARE_FRIEND_REGISTER.equals(bundle.getString(LOGIN_TYPE)))) {
            activity.openActivity(WebActivity.class, bundle);
        } else {
            activity.openActivity(MainActivity.class);
        }
//        }
        AppManager.getAppManager().finishAllActivity();
        activity.showToast("登录成功");
        activity.finish();
    }

    public static void logout(final AppCompatActivity activity) {
        activity.showDialog("", "您确定要离开" + activity.getString(R.string.app_name) + "吗？",
                "离开", new ConfirmDialog.OnBtnLeftClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();

                        activity.showToast("退出成功");
                        activity.finish();
                    }
                }, "留下", null);
    }

    public static void logout() {
        UmengPushUtils.removeAlias(App.getInstance().getBaseContext(), SharedPrefsUtils.getUid());

        SharedPrefsUtils.putIsLogin(false);
        SharedPrefsUtils.putAccessToken("");
        SharedPrefsUtils.putUid("");

        try {
            CookieManager cookieManager = CookieManager.getInstance();
            if (AppManager.getAppManager().containActivity(WebActivity.class.getName())) {
                cookieManager.removeAllCookie();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(App.getInstance().getBaseContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        App.getInstance().getBaseContext().startActivity(intent);

    }
}
