package com.android.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by fengyulong on 2016/8/4.
 */
public abstract class BaseFragment extends Fragment {
    protected List<Subscription> subscriptions;

    protected Context context;

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
        View view = initView(inflater, container);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitleBar(getView());
        initData(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getActivity().getWindow().closeAllPanels();

        super.onDestroy();
        unsubscribe();
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

    protected abstract View initView(LayoutInflater inflater, ViewGroup viewGroup);

    protected abstract void initTitleBar(View view);

    protected abstract void initData(Bundle savedInstanceState);

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
}
