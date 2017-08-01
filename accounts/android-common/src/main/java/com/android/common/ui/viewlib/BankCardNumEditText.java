package com.android.common.ui.viewlib;

import android.content.Context;
import android.util.AttributeSet;

import com.android.common.ui.NoEmojiEditText;

/**
 * 银行卡号输入框
 *
 * @author Administrator
 */
public class BankCardNumEditText extends NoEmojiEditText {

    public BankCardNumEditText(Context context) {
        super(context);

        this.addTextChangedListener(new NumberTextWatcher(this, ' '));
    }

    public BankCardNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.addTextChangedListener(new NumberTextWatcher(this, ' '));
    }

    /**
     * 获取真实的text（去掉空格）
     *
     * @return
     */
    public String getTextWithoutSpace() {
        String text = super.getText().toString();
        if (android.text.TextUtils.isEmpty(text)) {
            return "";
        } else {
            return text.replace(" ", "");
        }
    }

}