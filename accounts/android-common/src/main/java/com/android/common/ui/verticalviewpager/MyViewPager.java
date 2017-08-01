package com.android.common.ui.verticalviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.common.ui.CanSlideViewPager;

public class MyViewPager extends CanSlideViewPager {

    private OnViewPagerTouchEvent listener;

    public MyViewPager(Context context) {
        super(context);
    }

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent arg0) {
//		return true;
//	}

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.onTouchDown();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onTouchUp();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnViewPagerTouchEventListener(OnViewPagerTouchEvent l) {
        listener = l;
    }

    public interface OnViewPagerTouchEvent {
        void onTouchDown();

        void onTouchUp();
    }
}
