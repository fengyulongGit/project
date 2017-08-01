package com.android.common.utils;

import android.content.res.Resources;

import com.android.common.app.BaseApp;

/**
 * 屏幕相关和单位转换
 */
public class DisplayUtils {
    /**
     * dip转换为px
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().density;// 密度
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转换为dip
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换为px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale = BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换为sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float fontScale = BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 屏幕宽
     *
     * @return
     */
    public static int getScreenWidth() {
        return BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 屏幕高
     *
     * @return
     */
    public static int getScreenHeight() {
        return BaseApp.getInstance().getBaseContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 通知栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height",
                        "dimen", "android"));
    }

}
