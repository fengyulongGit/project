package com.android.common.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.android.common.R;

/**
 * 自定义圆形进度条对话框
 */
public class CustomProgressDialog extends ProgressDialog {

    private String content;
    private TextView progress_dialog_content;
    private Activity mParentActivity;
    OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                dismiss();
                if (mParentActivity != null) {
                    mParentActivity.finish();
                }
                return true;
            } else {
                return false;
            }
        }
    };
    private LoadingView progress_dialog_loadingview;

    public CustomProgressDialog(Context context, String content) {
        super(context);
        this.content = content;
        mParentActivity = (Activity) context;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setOnKeyListener(keyListener);
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
        setContentView(R.layout.custom_progress_dialog);
        progress_dialog_content = (TextView) findViewById(R.id.progress_dialog_content);
        progress_dialog_loadingview = (LoadingView) findViewById(R.id.progress_dialog_loadingview);
    }

}
