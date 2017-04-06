package com.android.common.utils;

/**
 * 防debug工具类
 * Created by fengyulong on 2016/12/5.
 */

public class EncryptUtil {
    static {
        System.loadLibrary("encrypt");
    }
}
