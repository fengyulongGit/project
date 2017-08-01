package com.android.common.ui.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustScrollView extends ScrollView {
    private static final int MODE_IDLE = 0;
    private static final int MODE_HORIZONTAL = 1;
    private static final int MODE_VERTICAL = 2;
    boolean allowDragBottom = true; // 如果是true，则允许拖动至底部的下一页
    private int scrollMode;
    private float downX, downY;
    private boolean needConsumeTouch = false;

    public CustScrollView(Context arg0) {
        this(arg0, null);
    }

    public CustScrollView(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public CustScrollView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getRawX();
            downY = ev.getRawY();
            needConsumeTouch = true; // 默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
            scrollMode = MODE_IDLE;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (isAtBottom()) {
                // needConsumeTouch尚未被定性，此处给其定性
                // 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
                if (ev.getRawY() - downY < 2) {
                    // flag设置，由父类去消费
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return super.dispatchTouchEvent(ev);
                }
            } else if (isAtTop()) {
                if (ev.getRawY() - downY > 2) {
                    // flag设置，由父类去消费
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return super.dispatchTouchEvent(ev);
                }
            } else if (scrollMode == MODE_VERTICAL) {
                getParent().requestDisallowInterceptTouchEvent(true);
                return super.dispatchTouchEvent(ev);
            } else if (!needConsumeTouch || scrollMode == MODE_HORIZONTAL) {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

            if (scrollMode == MODE_IDLE) {
                float xDistance = Math.abs(downX - ev.getRawX());
                float yDistance = Math.abs(downY - ev.getRawY());
                if (xDistance > yDistance && xDistance > 4) {
                    scrollMode = MODE_HORIZONTAL;
                } else if (yDistance > xDistance && yDistance > 4) {
                    scrollMode = MODE_VERTICAL;
                }
            }
        }

        // 通知父view是否要处理touch事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    private boolean isAtBottom() {
        if (getScrollY() + getMeasuredHeight() >= computeVerticalScrollRange() - 2) {
            return true;
        }
        return false;
    }

    public boolean isAtTop() {
        return getScrollY() == 0;
    }
}
