package com.android.common.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.common.R;

/**
 * 自定义圆形进度条对话框
 */
public class CustomProgressDialog extends ProgressDialog {

    private String content;
    private TextView progress_dialog_content;
    private Activity mParentActivity;

    public CustomProgressDialog(Context context, String content) {
        super(context);
        this.content = content;
        mParentActivity = (Activity) context;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setOnKeyListener(keyListener);
    }

    OnKeyListener keyListener = new OnKeyListener() {
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - progressShowTime < 1500) {
                        try {
                            Thread.sleep(500);
                            dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mParentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    CustomProgressDialog.super.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private long progressShowTime;

    @Override
    public void show() {
        super.show();
        progressShowTime = System.currentTimeMillis();
    }

    private void initData() {
        setContent(content);
    }

    public void setContent(String str) {
        progress_dialog_content.setText(str);
        progress_dialog_content.setVisibility(TextUtils.isEmpty(str) ? View.GONE : View.VISIBLE);
    }

    private void initView() {
        setContentView(R.layout.custom_progress_dialog);
        progress_dialog_content = (TextView) findViewById(R.id.progress_dialog_content);
    }

}
