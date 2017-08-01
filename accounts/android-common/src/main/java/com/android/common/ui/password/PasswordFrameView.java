package com.android.common.ui.password;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.common.R;


/**
 * describe: 单独一个自定义的支付密码框(是否显示密码点)
 * author: Went_Gone
 * create on: 2016/10/24
 */
public class PasswordFrameView extends RelativeLayout {
    View view_password_frame_password;
    private boolean isPassword;

    public PasswordFrameView(Context context) {
        this(context, null);
    }

    public PasswordFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.view_password_frame, this);

        view_password_frame_password = findViewById(R.id.view_password_frame_password);
    }
//
//    private void invalidateView(boolean isPassword) {
//        this.isPassword = isPassword;
//    }

    /**
     * 改变小点的状态
     *
     * @param isPassword
     */
    public void invalidatePassword(boolean isPassword) {
        view_password_frame_password.setVisibility(isPassword ? VISIBLE : GONE);
    }
}
