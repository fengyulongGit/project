package com.android.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;
import com.android.common.utils.AppManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by fengyulong on 2016/7/22.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected List<Subscription> subscriptions;
    protected Context context;


    /**
     * 加载的对话框
     */
    private CustomProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        subscriptions = new ArrayList<>();

        //当log_debug状态为关闭时，开启监听是否被动态调试
        /**
         * 部分MIUI8系统的机器问题，暂时关闭该验证，否则闪退无法启动
         */
//        if (!CommonUtils.DEBUG()) {
//            new EncryptUtil();
//        }

        context = this;
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);
    }

    /**
     * 加载布局
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void initData();

    protected void showProgressDialog(String content) {
        showProgressDialog(content, context);
    }

    protected void showProgressDialog(String content, Context context) {
        if (dialog == null && context != null) {
            dialog = (CustomProgressDialog) DialogUtil.createProgressDialog(context, content);
        }
        try {
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeProgressDialog() {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showDialog(String message) {
        showDialog(message, null);
    }

    protected void showDialog(String message, String bottomBtnContent) {
        showDialog(message, bottomBtnContent, null);
    }

    protected void showDialog(String message, String bottomBtnContent, ConfirmDialog.OnBtnBottomClickListener onBtnBottomClickListener) {
        DialogUtil.showDialog(context, message, bottomBtnContent, onBtnBottomClickListener);
    }

    protected void showDialog(String message, String leftBtnContent, ConfirmDialog.OnBtnLeftClickListener onBtnLeftClickListener,
                              String rightBtnContent, ConfirmDialog.OnBtnRightClickListener onBtnRightClickListener) {
        DialogUtil.showDialog(context, message, leftBtnContent, onBtnLeftClickListener, rightBtnContent, onBtnRightClickListener);
    }

    /**
     * toast提示
     *
     * @param string
     */
    public void showToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {

        getWindow().closeAllPanels();

        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);

        unsubscribe();
    }

    protected void unsubscribe() {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
