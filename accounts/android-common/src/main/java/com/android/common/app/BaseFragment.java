package com.android.common.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.common.R;
import com.android.common.net.base.ErrorMsg;
import com.android.common.net.base.Subscriber;
import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;
import com.android.common.utils.ValidateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fengyulong on 2016/8/4.
 */
public abstract class BaseFragment extends Fragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        subscriptions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        if (layoutContentResID() != 0) {
            view = inflater.inflate(R.layout.activity_base, container, false);

            base_title = (LinearLayout) view.findViewById(R.id.base_title);
            base_content = (LinearLayout) view.findViewById(R.id.base_content);
            base_error = (LinearLayout) view.findViewById(R.id.base_error);

            if (layoutTitleResID() != 0) {
                LayoutInflater.from(context).inflate(layoutTitleResID(), base_title);
            }
            LayoutInflater.from(context).inflate(layoutContentResID(), base_content);

            if (layoutErrorResID() != 0) {
                LayoutInflater.from(context).inflate(layoutErrorResID(), base_error);
            }

            unbinder = ButterKnife.bind(this, view);
        }


        return view;
    }

    protected abstract int layoutTitleResID();

    protected abstract int layoutContentResID();

    protected abstract int layoutErrorResID();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initTitleBar();

        initViewData(savedInstanceState);

        requestData();
    }


    @Override
    public void onDestroyView() {
        getActivity().getWindow().closeAllPanels();

        unsubscribe();

        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * 取消所有请求
     */
    protected void unsubscribe() {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Subscription subscription = Observable.timer(200, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        protected void showError(ErrorMsg errorMsg) {

                        }

                        @Override
                        public void onNext(Long aLong) {
                            userVisibleHint();
                        }
                    });
            addSubscription(subscription);
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    protected abstract void userVisibleHint();

    protected abstract void initTitleBar();

    protected abstract void initViewData(Bundle savedInstanceState);

    protected abstract void requestData();

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

    protected void showDialog(String title, String message) {
        showDialog(title, message, null);
    }

    protected void showDialog(String title, String message, String bottomBtnContent) {
        showDialog(title, message, bottomBtnContent, null);
    }

    protected void showDialog(String title, String message, String bottomBtnContent, ConfirmDialog.OnBtnBottomClickListener onBtnBottomClickListener) {
        DialogUtil.showDialog(context, title, message, bottomBtnContent, onBtnBottomClickListener);
    }

    protected void showDialog(String title, String message, String leftBtnContent, ConfirmDialog.OnBtnLeftClickListener onBtnLeftClickListener,
                              String rightBtnContent, ConfirmDialog.OnBtnRightClickListener onBtnRightClickListener) {
        DialogUtil.showDialog(context, title, message, leftBtnContent, onBtnLeftClickListener, rightBtnContent, onBtnRightClickListener);
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
}
