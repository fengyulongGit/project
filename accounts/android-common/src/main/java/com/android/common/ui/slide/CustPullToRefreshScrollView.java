package com.android.common.ui.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.android.common.R;
import com.android.common.ui.pulltorefresh.PullToRefreshScrollView;


/**
 * Created by chengyuchun on 2016/7/13 0013 15:07
 */

public class CustPullToRefreshScrollView extends PullToRefreshScrollView {
    private float downX, downY;
    private CustScrollViewForRefresh scrollView;

    public CustPullToRefreshScrollView(Context context) {
        super(context);
    }

    public CustPullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustPullToRefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public CustPullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {

        scrollView = new CustScrollViewForRefresh(context, attrs);

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    public CustScrollViewForRefresh getScrollView() {
        return scrollView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true); // 父类不拦截
                break;
            case MotionEvent.ACTION_MOVE:
                float xDistance = Math.abs(downX - ev.getRawX());
                float yDistance = Math.abs(downY - ev.getRawY());
                if (yDistance > xDistance && yDistance > 4
                        && ((downY - ev.getRawY()) > 0 && getScrollView().isAtBottom()) || (downY - ev.getRawY()) < 0 && getScrollView().isAtTop()) {
                    //在底部往上滑
                    getParent().requestDisallowInterceptTouchEvent(false); // 父类拦截处理
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true); // 自己拦截处理
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
