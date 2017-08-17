package com.android.common.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * base 的adapter
 * Created by fengyulong on 2017/4/15.
 */

public abstract class BaseAdapter extends android.widget.BaseAdapter {
    protected Context context;
    private List list = new ArrayList();

    /**
     * 加载的对话框
     */
    private CustomProgressDialog dialog;

    public BaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List list) {
        if (list == null) {
            list = new ArrayList();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(@NonNull List list) {
        this.list.addAll(list);
        notifyDataSetChanged();
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
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
