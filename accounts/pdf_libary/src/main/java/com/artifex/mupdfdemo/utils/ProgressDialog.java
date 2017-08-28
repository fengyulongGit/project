package com.artifex.mupdfdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.artifex.mupdfdemo.R;

/**
 * 自定义圆形进度条对话框
 */
public class ProgressDialog extends android.app.ProgressDialog {

    private String content;
    private TextView progress_dialog_content;
    private Activity mParentActivity;
    private LoadView progress_dialog_loadingview;

    public ProgressDialog(Context context, String content) {
        super(context);
        this.content = content;
        mParentActivity = (Activity) context;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void dismiss() {
        if (mParentActivity != null && !mParentActivity.isFinishing()) {
            super.dismiss();    //调用超类对应方法
        }
    }

    private void initData() {
        progress_dialog_content.setText(content);
    }

    public void setContent(String str) {
        progress_dialog_content.setText(str);
        progress_dialog_loadingview.setLoadingText(str);
    }

    private void initView() {
        setContentView(R.layout.progress_dialog);
        progress_dialog_content = (TextView) findViewById(R.id.progress_dialog_content);
        progress_dialog_loadingview = (LoadView) findViewById(R.id.progress_dialog_loadingview);
    }

}
