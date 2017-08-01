package com.android.common.ui.verticalviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.common.R;

public class HorizonVerticalViewPager extends MyViewPager {
    float downX, downY;
    private boolean isVertical = false;
    private boolean isCanSlideTop = true;
    private boolean isCanSlideBottom = true;
    private boolean isCanSlideLeft = true;
    private boolean isCanSlideRight = true;

    public HorizonVerticalViewPager(Context context) {
        super(context);
        init();
    }

    public HorizonVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIsVertical(attrs, 0);
        init();
    }

    public HorizonVerticalViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initIsVertical(attrs, defStyle);
        init();
    }

    private void initIsVertical(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyViewPager, defStyle, 0);
        isVertical = a.getBoolean(R.styleable.MyViewPager_isVertical, false);
        System.out.println("isVertical=>" + isVertical);
        a.recycle();
    }

    private void init() {
        if (isVertical) {
            // The majority of the magic happens here
            setPageTransformer(true, new VerticalPageTransformer());
            // The easiest way to get rid of the overscroll drawing that happens on the left and right
            setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isVertical) {
                    if (ev.getRawY() - downY > 2 && !isCanSlideTop) {
                        //是否继续上滑
                        return false;
                    }

                    if (ev.getRawY() - downY < 2 && !isCanSlideBottom) {
                        //是否继续下滑
                        return false;
                    }
                } else {
                    if (ev.getRawX() - downX > 2 && !isCanSlideLeft) {
                        return false;
                    }

                    if (ev.getRawX() - downX > 2 && !isCanSlideRight) {
                        return false;
                    }
                    break;
                }
            }
        }

        if (isVertical) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
            return intercepted;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isVertical) {
            return super.onTouchEvent(swapXY(ev));
        } else {
            return super.onTouchEvent(ev);
        }
    }

    public void setCanSlideTop(boolean canSlideTop) {
        isCanSlideTop = canSlideTop;
    }

    public void setCanSlideBottom(boolean canSlideBottom) {
        isCanSlideBottom = canSlideBottom;
    }

    public void setCanSlideLeft(boolean canSlideLeft) {
        isCanSlideLeft = canSlideLeft;
    }

    public void setCanSlideRight(boolean canSlideRight) {
        isCanSlideRight = canSlideRight;
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
