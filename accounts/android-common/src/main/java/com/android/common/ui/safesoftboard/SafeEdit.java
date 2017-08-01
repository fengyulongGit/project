package com.android.common.ui.safesoftboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow.OnDismissListener;

import com.android.common.ui.viewlib.PowerfulEditText;

public class SafeEdit extends PowerfulEditText implements OnDismissListener {

    private SoftKeyBoard softKeyBoard;
    private SoftKeyStatusListener softKeyStatusListener;

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
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (getCompoundDrawables()[2] != null) {

                boolean isTouched = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (!isTouched) {
                    showSoftKeyBoard();
                } else {
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getWindowToken(), 0);
                }
            } else {
                showSoftKeyBoard();
            }
        }
        requestFocus();
        return true;
    }

    private void showSoftKeyBoard() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getWindowToken(), 0);
        if (softKeyBoard == null) {
            softKeyBoard = new SoftKeyBoard(getContext());
            softKeyBoard.setEdit(this);
            softKeyBoard.setOnDismissListener(this);
            softKeyBoard.setSoftKeyStatusListener(softKeyStatusListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    softKeyBoard.show();
                }
            }, 300);
        }
    }

    public void closeInput() {
        try {
            softKeyBoard.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeleted() {
        softKeyBoard.onDeleted();
    }

    @Override
    public void onDismiss() {
        softKeyBoard.recycle();
        softKeyBoard = null;
    }

    public void setSoftKeyStatusListener(SoftKeyStatusListener softKeyStatusListener) {
        this.softKeyStatusListener = softKeyStatusListener;
    }
}
