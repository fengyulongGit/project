package com.creditpomelo.accounts.main.base;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.common.app.BaseFragment;
import com.android.common.net.base.ErrorMsg;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.login.activity.LoginActivity;
import com.creditpomelo.accounts.utils.SharedPrefsUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * @author fengyulong
 * @date 创建时间：2016年7月12日 下午12:41:14
 */
public abstract class Fragment extends BaseFragment {

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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }

    @Optional
    @OnClick(R.id.tv_left)
    void onLeft() {
        left();
    }

    public void left() {
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
    void netSetting() {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.btn_data_error_refresh)
    void dataRefresh() {
        requestData();
    }

    public void showContent() {
        setVisibilityContent(View.VISIBLE);
        setVisibilityError(View.GONE);
    }

    public void showNetError() {
        setVisibilityContent(View.GONE);
        setVisibilityError(View.VISIBLE);

        view_net_error.setVisibility(View.VISIBLE);
        view_data_error.setVisibility(View.GONE);
    }

    public void showDataError() {
        setVisibilityContent(View.GONE);
        setVisibilityError(View.VISIBLE);

        view_net_error.setVisibility(View.GONE);
        view_data_error.setVisibility(View.VISIBLE);
    }

    protected void showErrorView(final ErrorMsg errorMsg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMsg.isNetNone()) {
                    showNetError();
                } else if (errorMsg.isNetError()) {
                    showDataError();
                } else {
                    showDataError();
                }
            }
        });
    }

    @Override
    protected int layoutTitleResID() {
        return 0;
    }

    @Override
    protected int layoutErrorResID() {
        return R.layout.layout_error;
    }

    @Override
    protected void initTitleBar() {
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
//        if (tv_left != null) {
//            tv_left.setVisibility(View.VISIBLE);
//        }
    }

    public void setTitleBackground(int color) {
        if (layout_title != null) {
            layout_title.setBackgroundColor(getResources().getColor(color));
        }
//        if (toolbar != null) {
//            toolbar.setBackgroundColor(getResources().getColor(color));
//        }
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
