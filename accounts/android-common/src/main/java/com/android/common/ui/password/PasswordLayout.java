package com.android.common.ui.password;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.common.R;
import com.android.common.ui.safesoftboard.SafeEdit;

/**
 * describe:  密码输入框的控件布局
 * author: Went_Gone
 * create on: 2016/10/24
 */
public class PasswordLayout extends RelativeLayout {
    private SafeEdit editText;
    private PasswordFrameView[] PasswordFrameViews;//使用一个数组存储密码框
    private InputCompleteListener inputCompleteListener;

    public PasswordLayout(Context context) {
        this(context, null);
    }

    public PasswordLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PasswordFrameViews = new PasswordFrameView[6];

        View.inflate(context, R.layout.layout_password, this);

        editText = (SafeEdit) findViewById(R.id.item_edittext);
        PasswordFrameViews[0] = (PasswordFrameView) findViewById(R.id.passwordframeView0);
        PasswordFrameViews[1] = (PasswordFrameView) findViewById(R.id.passwordframeView1);
        PasswordFrameViews[2] = (PasswordFrameView) findViewById(R.id.passwordframeView2);
        PasswordFrameViews[3] = (PasswordFrameView) findViewById(R.id.passwordframeView3);
        PasswordFrameViews[4] = (PasswordFrameView) findViewById(R.id.passwordframeView4);
        PasswordFrameViews[5] = (PasswordFrameView) findViewById(R.id.passwordframeView5);

        editText.setCursorVisible(false);//将光标隐藏
        setListener();
    }

    private void setListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText.length() == 6) {
                    //文字长度位6   则调用完成输入的监听
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editText.closeInput();
                        }
                    }, 200);

                    if (inputCompleteListener != null) {
                        inputCompleteListener.inputComplete(getStrPassword());
                    }
                }

                for (int i = 0; i < PasswordFrameViews.length; i++) {
                    PasswordFrameViews[i].invalidatePassword(i < editText.getText().length());
                }
            }
        });
    }

    public void onKeyDelete() {
        editText.onDeleted();
    }

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public SafeEdit getEditText() {
        return editText;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getStrPassword() {
        return getEditText().getText().toString().trim();
    }

    public void setContent(String content) {
        editText.setText(content);
    }

    public interface InputCompleteListener {
        void inputComplete(String password);
    }
}
