package com.android.common.ui.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by chengyuchun on 2016/7/21 0021.
 */
public class CustScrollViewForRefresh extends ScrollView {
    public CustScrollViewForRefresh(Context context) {
        super(context);
    }

    public CustScrollViewForRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustScrollViewForRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isAtBottom() {
        if (getScrollY() + getMeasuredHeight() >= computeVerticalScrollRange() - 2) {
            return true;
        }
        return false;
    }
}
