package com.android.common.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.android.common.utils.EmojiUtil;

/**
 * 禁止输入Emoji表情
 * Created by fengyulong on 2017/4/14.
 */

public class NoEmojiEditText extends EditText {
    private String inputText = "";

    public NoEmojiEditText(Context context) {
        super(context);
        init();
    }

    public NoEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NoEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (EmojiUtil.containsEmoji(getTextTrim())) {
                    setText(inputText);
                    setSelection(inputText.length());
                } else {
                    inputText = getTextTrim();
                }
            }
        });
    }

    public String getTextTrim() {
        return getText().toString().trim();
    }
}
