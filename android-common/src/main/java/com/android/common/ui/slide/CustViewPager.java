package com.android.common.ui.slide;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by chengyuchun on 2016/5/16 0016.
 */
public class CustViewPager extends ViewPager {
    private float mDownX;
    private float mDownY;

    public CustViewPager(Context context) {
        super(context);
    }

    public CustViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                requestDisallowInterceptTouchEvent(true); //自己不拦截，交给子view处理
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
                    requestDisallowInterceptTouchEvent(false); //自己拦截，自己处理
                } else {
                    requestDisallowInterceptTouchEvent(true); //自己不拦截，交给子view处理
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
