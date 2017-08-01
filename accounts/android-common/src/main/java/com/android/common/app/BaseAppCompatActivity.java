package com.android.common.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.common.R;
import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;
import com.android.common.utils.AppManager;
import com.android.common.utils.ValidateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by fengyulong on 2016/7/22.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected List<Subscription> subscriptions;
    protected Context context;

    Unbinder unbinder;

    private LinearLayout base_title;
    private LinearLayout base_content;
    private LinearLayout base_error;

    /**
     * 加载的对话框
     */
    private CustomProgressDialog dialog;
    private DialogInterface.OnKeyListener mKeyListener;

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
        if (layoutContentResID() != 0) {
            setContentView(R.layout.activity_base);
            base_title = (LinearLayout) findViewById(R.id.base_title);
            base_content = (LinearLayout) findViewById(R.id.base_content);
            base_error = (LinearLayout) findViewById(R.id.base_error);

            if (layoutTitleResID() != 0) {
                LayoutInflater.from(context).inflate(layoutTitleResID(), base_title);
            }
            LayoutInflater.from(context).inflate(layoutContentResID(), base_content);

            if (layoutErrorResID() != 0) {
                LayoutInflater.from(context).inflate(layoutErrorResID(), base_error);
            }

            unbinder = ButterKnife.bind(this);
        }

        initTitleBar();

        initViewData();

        requestData();
    }

    protected abstract int layoutTitleResID();

    protected abstract int layoutContentResID();

    protected abstract int layoutErrorResID();

    protected abstract void initTitleBar();

    public void setVisibilityTitle(int visibility) {
        if (base_title != null) {
            base_title.setVisibility(visibility);
        }
    }

    public void setVisibilityContent(int visibility) {
        if (base_content != null) {
            base_content.setVisibility(visibility);
        }
    }

    public void setVisibilityError(int visibility) {
        if (base_error != null) {
            base_error.setVisibility(visibility);
        }
    }

    /**
     * 加载布局
     */
    protected abstract void initViewData();

    protected abstract void requestData();

    public void showProgressDialog(String content) {
        showProgressDialog(content, context);
    }

    public void showProgressDialog(String content, Context context) {
        if (dialog == null && context != null) {
            dialog = (CustomProgressDialog) DialogUtil.createProgressDialog(context, content);
            if (mKeyListener != null) {
                dialog.setOnKeyListener(mKeyListener);
            }
        }
        try {
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProgressDialogKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        mKeyListener = onKeyListener;
    }

    public void closeProgressDialog() {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String message) {
        showDialog(message, null);
    }

    public void showDialog(String message, String bottomBtnContent) {
        showDialog(message, bottomBtnContent, null);
    }

    public void showDialog(String message, String bottomBtnContent, ConfirmDialog.OnBtnBottomClickListener onBtnBottomClickListener) {
        DialogUtil.showDialog(context, message, bottomBtnContent, onBtnBottomClickListener);
    }

    public void showDialog(String message, String leftBtnContent, ConfirmDialog.OnBtnLeftClickListener onBtnLeftClickListener,
                           String rightBtnContent, ConfirmDialog.OnBtnRightClickListener onBtnRightClickListener) {
        DialogUtil.showDialog(context, message, leftBtnContent, onBtnLeftClickListener, rightBtnContent, onBtnRightClickListener);
    }

    /**
     * toast提示
     *
     * @param string
     */
    public void showToast(String string) {
        if (ValidateUtil.isEmpty(string)) {
            return;
        }
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {

        getWindow().closeAllPanels();


        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }

        AppManager.getAppManager().finishActivity(this);

        unsubscribe();
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    protected void unsubscribe() {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    public void openActivity(Class<?> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivityForResult(Class<?> cls, @Nullable Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void openActivityForResult(Class<?> cls, int requestCode) {
        openActivityForResult(cls, null, requestCode);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
