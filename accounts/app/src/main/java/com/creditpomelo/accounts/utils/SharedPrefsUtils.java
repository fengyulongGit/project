package com.creditpomelo.accounts.utils;

import com.android.common.utils.SharedPreferencesUtils;

/**
 * 存储类
 * Created by fengyulong on 2016/08/19.
 */

public class SharedPrefsUtils {
    public static void putLastLoginMobile(String flag) {
        SharedPreferencesUtils.putString("last_login_mobile", flag);
    }

    public static String getLastLoginMobile() {
        return SharedPreferencesUtils.getString("last_login_mobile", "");
    }

    /**
     * 设置登录状态
     */
    public static void putIsLogin(boolean flag) {
        SharedPreferencesUtils.putBoolean("isLogin", flag);
    }

    /**
     * 登录状态
     */
    public static boolean isLogin() {
        return SharedPreferencesUtils.getBoolean("isLogin", false);
    }

    /**
     * 设置活体认证状态
     */
    public static void putFaceVerifyStatus(String flag) {
        SharedPreferencesUtils.putString("isFaceVerifyStatus", flag);
    }

    /**
     * 活体认证状态
     */
    public static String getFaceVerifyStatus() {
        return SharedPreferencesUtils.getString("isFaceVerifyStatus", "");
    }

    /**
     * 访问token
     */
    public static void putAccessToken(String flag) {
        SharedPreferencesUtils.putString("access_token", flag);
    }

    /**
     * 访问token
     */
    public static String getAccessToken() {
        return SharedPreferencesUtils.getString("access_token", "");
    }

    /**
     * 手势密码状态
     */
    public static void putIsGestureOpen(boolean flag) {
        SharedPreferencesUtils.putBoolean(getUid(), "is_Gesture_Open", flag);
    }

    /**
     * 手势密码状态
     */
    public static boolean isGestureOpen() {
        return SharedPreferencesUtils.getBoolean(getUid(), "is_Gesture_Open", false);
    }

    /**
     * 手势密码
     */
    public static String getGesturePassword() {
        return SharedPreferencesUtils.getString(getUid(), "gesture_password", "");
    }

    /**
     * 手势密码
     */
    public static void putGesturePassword(String value) {
        SharedPreferencesUtils.putString(getUid(), "gesture_password", value);
    }

    /**
     * 用户唯一标识
     */
    public static void putUid(String value) {
        SharedPreferencesUtils.putString("uid", value);
    }

    /**
     * 用户唯一标识
     */
    public static String getUid() {
        return SharedPreferencesUtils.getString("uid", "0");
    }

    /**
     * 推送状态
     */
    public static void putIsPush(boolean flag) {
        SharedPreferencesUtils.putBoolean("IsPush", flag);
    }

    /**
     * 推送状态
     */
    public static boolean isPush() {
        return SharedPreferencesUtils.getBoolean("IsPush", true);
    }

    /**
     * 首次启动
     */
    public static boolean isFirstStart() {
        return SharedPreferencesUtils.getBoolean("IsFirstStart", true);
    }

    /**
     * 首次启动
     */
    public static void putIsFirstStart(boolean flag) {
        SharedPreferencesUtils.putBoolean("IsFirstStart", flag);
    }

    //客服热线-文本-保存
    public static void putServiceHotLineText(String flag) {
        SharedPreferencesUtils.putString("serviceHotLine_text", flag);
    }

    //客服热线-文本
    public static String getServiceHotLineText() {
        return SharedPreferencesUtils.getString("serviceHotLine_text", "欢迎拨打热线电话4000580900");
    }

    //客服热线-电话-保存
    public static void putServiceHotLineTelphone(String flag) {
        SharedPreferencesUtils.putString("serviceHotLine_telphone", flag);
    }

    //客服热线-电话
    public static String getServiceHotLineTelphone() {
        return SharedPreferencesUtils.getString("serviceHotLine_telphone", "4000580900");
    }

    /**
     * 产品利率
     */
    public static void putRate(String flag) {
        SharedPreferencesUtils.putString("rate", flag);
    }

    /**
     * 产品利率
     */
    public static String getRate() {
        return SharedPreferencesUtils.getString("rate", "");
    }

    /**
     * 提前还款手续费费率
     */
    public static void putRateFactorage(String flag) {
        SharedPreferencesUtils.putString("rate_factorage", flag);
    }

    /**
     * 提前还款手续费费率
     */
    public static String getRateFactorage() {
        return SharedPreferencesUtils.getString("rate_factorage", "");
    }

    /**
     * 数据字典时间戳
     */
    public static void putSysDictTs(String flag) {
        SharedPreferencesUtils.putString("sys_dict_ts", flag);
    }

    /**
     * 数据字典时间戳
     */
    public static String getSysDictTs() {
        return SharedPreferencesUtils.getString("sys_dict_ts", null);
    }

    /**
     * 数据字典url
     */
    public static void putSysDictUrl(String flag) {
        SharedPreferencesUtils.putString("sys_dict_url", flag);
    }

    /**
     * 数据字典url
     */
    public static String getSysDictUrl() {
        return SharedPreferencesUtils.getString("sys_dict_url", null);
    }


    /**
     * 行政区时间戳
     */
    public static void putSysAreaTs(String flag) {
        SharedPreferencesUtils.putString("sys_area_ts", flag);
    }

    /**
     * 行政区时间戳
     */
    public static String getSysAreaTs() {
        return SharedPreferencesUtils.getString("sys_area_ts", null);
    }


    /**
     * 行政区Url
     */
    public static void putSysAreaUrl(String flag) {
        SharedPreferencesUtils.putString("sys_area_url", flag);
    }

    /**
     * 行政区Url
     */
    public static String getSysAreaUrl() {
        return SharedPreferencesUtils.getString("sys_area_url", null);
    }

    /**
     * 新消息
     */
    public static void putIsNewMessage(boolean flag) {
        SharedPreferencesUtils.putBoolean(getUid(), "is_new_message", flag);
    }

    /**
     * 新消息
     */
    public static boolean isNewMessage() {
        return SharedPreferencesUtils.getBoolean(getUid(), "is_new_message", false);
    }
}
