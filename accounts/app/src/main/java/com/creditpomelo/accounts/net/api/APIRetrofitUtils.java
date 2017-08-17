package com.creditpomelo.accounts.net.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.android.common.net.RetrofitUtils;
import com.android.common.net.base.Response;
import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.utils.AppManager;
import com.creditpomelo.accounts.BuildConfig;
import com.creditpomelo.accounts.main.base.App;
import com.creditpomelo.accounts.main.login.activity.LoginActivity;
import com.creditpomelo.accounts.net.api.base.converter.APIGsonConverterFactory;
import com.creditpomelo.accounts.utils.LoginUtils;
import com.creditpomelo.accounts.utils.SharedPrefsUtils;

import retrofit2.Converter;

/**
 * api 请求工具类
 * Created by fengyulong on 2017/4/6.
 */
public class APIRetrofitUtils extends RetrofitUtils {


    private static APIRetrofitUtils instance;

    private APIRetrofitUtils() {
        super();
    }

    public static APIRetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (APIRetrofitUtils.class) {
                if (instance == null) {
                    instance = new APIRetrofitUtils();
                }
            }
        }
        return instance;
    }

    private Dialog dialog;
    private Activity activity;

    @Override
    public void exitLogin(final Response response) {
        super.exitLogin(response);
        if (SharedPrefsUtils.isLogin() && activity != AppManager.getAppManager().currentActivity()) {
            activity = AppManager.getAppManager().currentActivity();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConfirmDialog confirmDialog = new ConfirmDialog(AppManager.getAppManager().currentActivity());
                    confirmDialog.setMode(ConfirmDialog.Mode_BOTTOM);
                    confirmDialog.setContent(response.getMessage());
                    confirmDialog.setBottomBtnContent("确定");
                    confirmDialog.setOnBtnBottomClickListener(new ConfirmDialog.OnBtnBottomClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginUtils.logout();
                            Intent intent = new Intent(App.getInstance().getBaseContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            App.getInstance().getBaseContext().startActivity(intent);
                            //Toast.makeText(App.getInstance().getBaseContext(), response.getMessage(), Toast.LENGTH_SHORT).show();

                            dialog = null;
                        }
                    });
                    dialog = confirmDialog.create();
                    dialog.show();
                }
            }, 500);
        }
    }

    @Override
    protected String url() {
        return BuildConfig.SERVER_URL;
    }

    @Override
    protected Converter.Factory gsonConverterFactory() {
        return APIGsonConverterFactory.create();
    }
}
