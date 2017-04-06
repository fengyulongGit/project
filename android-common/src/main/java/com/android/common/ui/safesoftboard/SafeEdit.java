package com.android.common.ui.safesoftboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;

public class SafeEdit extends EditText implements OnDismissListener {

    private SoftKeyBoard softKeyBoard;

    public SafeEdit(Context context) {
        super(context);
        initSafeEdit(context);
    }

    public SafeEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSafeEdit(context);
    }

    public SafeEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSafeEdit(context);
    }

    public void initSafeEdit(Context context) {
        //setOnFocusChangeListener(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getWindowToken(), 0);
            if (softKeyBoard == null) {
                softKeyBoard = new SoftKeyBoard(getContext());
                softKeyBoard.setEdit(this);
                softKeyBoard.setOnDismissListener(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        softKeyBoard.show();
                    }
                }, 200);
            }
        }
        requestFocus();
        return true;
    }

    @Override
    public void onDismiss() {
        softKeyBoard.recycle();
        softKeyBoard = null;
    }
}
