package com.android.common.ui.password;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.android.common.R;

/**
 * Created by fengyulong on 2017/5/16.
 */

public class PasswordDialog {
    private Context context;

    private PasswordLayout et_password;
    private PasswordLayout.InputCompleteListener inputCompleteListener;

    public PasswordDialog(Context context) {
        this.context = context;
    }

    public Dialog create() {
        final Dialog dialog = new Dialog(context, com.android.common.R.style.Dialog_Fullscreen);
        dialog.setCanceledOnTouchOutside(false);//点击弹窗外面   弹窗消失

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.mystyle);  //添加动画
//        dialog.show();
        window.setContentView(R.layout.dialog_password_layout);

        et_password = (PasswordLayout) window.findViewById(R.id.et_password);
        et_password.setInputCompleteListener(new PasswordLayout.InputCompleteListener() {
            @Override
            public void inputComplete(String password) {
                dialog.dismiss();
                if (inputCompleteListener != null) {
                    inputCompleteListener.inputComplete(password);
                }
            }
        });

        return dialog;
    }

    public void setInputCompleteListener(PasswordLayout.InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public void setePassword(String password) {
        if (et_password != null) {
            et_password.setContent(password);
        }
    }
}
