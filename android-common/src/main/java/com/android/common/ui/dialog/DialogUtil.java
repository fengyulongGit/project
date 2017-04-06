package com.android.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * 对话框创建工具类
 */
public class DialogUtil {


    /**
     * 创建进度对话框
     *
     * @param context
     * @param content
     * @return
     */
    public static Dialog createProgressDialog(Context context, String content) {
        return new CustomProgressDialog(context, content);
    }

    public static void showDialog(Context context, String message, String bottomBtnContent) {
        showDialog(context, message, bottomBtnContent, null);
    }

    public static void showDialog(Context context, String message, String bottomBtnContent, ConfirmDialog.OnBtnBottomClickListener onBtnBottomClickListener) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context);
        confirmDialog.setMode(ConfirmDialog.Mode_BOTTOM);
        confirmDialog.setContent(message);
        confirmDialog.setBottomBtnContent(bottomBtnContent);
        confirmDialog.setOnBtnBottomClickListener(onBtnBottomClickListener);
        confirmDialog.create().show();
    }

    public static void showDialog(Context context, String message, String leftBtnContent, ConfirmDialog.OnBtnLeftClickListener onBtnLeftClickListener,
                                  String rightBtnContent, ConfirmDialog.OnBtnRightClickListener onBtnRightClickListener) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context);
        confirmDialog.setContent(message);
        confirmDialog.setLeftBtnContent(leftBtnContent);
        confirmDialog.setOnBtnLeftClickListener(onBtnLeftClickListener);
        confirmDialog.setRightBtnContent(rightBtnContent);
        confirmDialog.setOnBtnRightClickListener(onBtnRightClickListener);
        confirmDialog.create().show();
    }


}
