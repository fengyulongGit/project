package com.android.common.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fengyulong on 2017/4/8.
 */

public class CanSlideViewPager extends ViewPager {
    private boolean isCanSlide = true;

    public CanSlideViewPager(Context context) {
        super(context);
    }

    public CanSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isCanSlide) {
            return false;
        }

        return super.onInterceptTouchEvent(event);
    }

    public void setCanSlide(boolean isCanSlide) {
        this.isCanSlide = isCanSlide;
    }
}
