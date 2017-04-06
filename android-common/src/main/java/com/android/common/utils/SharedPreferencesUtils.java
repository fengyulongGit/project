package com.android.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.android.common.app.BaseApp;

import java.util.Map;
import java.util.Set;

/**
 * Created by sy on 2016/08/19.
 */

public class SharedPreferencesUtils {
    private SharedPreferencesUtils() {
    }

    public static Map<String, ?> getAll() {
        return getAll("");
    }

    public static Map<String, ?> getAll(String shareName) {
        return getSharedPreferences(shareName).getAll();
    }

    public static void putString(String key, String value) {
        putString("", key, value);
    }

    public static void putString(String shareName, String key, String value) {
        getEditor(shareName).putString(SecurityUtils.Md5(key), value).commit();
    }

    public static String getString(String key, String defaultValue) {
        return getString("", key, defaultValue);
    }

    public static String getString(String shareName, String key, String defaultValue) {
        return getSharedPreferences(shareName).getString(SecurityUtils.Md5(key), defaultValue);
    }

    public static void putStringSet(String key, Set<String> values) {
        putStringSet("", key, values);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(String shareName, String key, Set<String> values) {
        getEditor(shareName).putStringSet(SecurityUtils.Md5(key), values).commit();
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValues) {
        return getStringSet("", key, defaultValues);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(String shareName, String key, Set<String> defaultValues) {
        return getSharedPreferences(shareName).getStringSet(SecurityUtils.Md5(key), defaultValues);
    }

    public static void putInt(String key, int value) {
        putInt("", key, value);
    }

    public static void putInt(String shareName, String key, int value) {
        getEditor(shareName).putInt(SecurityUtils.Md5(key), value).commit();
    }

    public static int getInt(String key, int defaultValue) {
        return getInt("", key, defaultValue);
    }

    public static int getInt(String shareName, String key, int defaultValue) {
        return getSharedPreferences(shareName).getInt(SecurityUtils.Md5(key), defaultValue);
    }

    public static void putLong(String key, long value) {
        putLong("", key, value);
    }

    public static void putLong(String shareName, String key, long value) {
        getEditor(shareName).putLong(SecurityUtils.Md5(key), value).commit();
    }

    public static long getLong(String key, long defaultValue) {
        return getLong("", key, defaultValue);
    }

    public static long getLong(String shareName, String key, long defaultValue) {
        return getSharedPreferences(shareName).getLong(SecurityUtils.Md5(key), defaultValue);
    }

    public static void putFloat(String key, float value) {
        putFloat("", key, value);
    }

    public static void putFloat(String shareName, String key, float value) {
        getEditor(shareName).putFloat(SecurityUtils.Md5(key), value).commit();
    }

    public static float getFloat(String key, float defaultValue) {
        return getFloat("", key, defaultValue);
    }

    public static float getFloat(String shareName, String key, float defaultValue) {
        return getSharedPreferences(shareName).getFloat(SecurityUtils.Md5(key), defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        putBoolean("", key, value);
    }

    public static void putBoolean(String shareName, String key, boolean value) {
        getEditor(shareName).putBoolean(SecurityUtils.Md5(key), value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean("", key, defaultValue);
    }

    public static boolean getBoolean(String shareName, String key, boolean defaultValue) {
        return getSharedPreferences(shareName).getBoolean(SecurityUtils.Md5(key), defaultValue);
    }

    public static boolean contains(String key) {
        return contains("", key);
    }

    public static boolean contains(String shareName, String key) {
        return getSharedPreferences(shareName).contains(SecurityUtils.Md5(key));
    }

    public static void remove(String key) {
        remove("", key);
    }

    public static void remove(String shareName, String key) {
        getEditor(shareName).remove(SecurityUtils.Md5(key)).commit();
    }

    public static void clear() {
        clear("");
    }

    public static void clear(String shareName) {
        getEditor(shareName).clear().commit();
    }

    private static SharedPreferences.Editor getEditor() {
        return getEditor("");
    }

    private static SharedPreferences.Editor getEditor(String shareName) {
        return getSharedPreferences(shareName).edit();
    }

    private static SharedPreferences getSharedPreferences(String shareName) {
        if (ValidateUtil.isEmpty(shareName)) {
            return PreferenceManager.getDefaultSharedPreferences(BaseApp.getInstance().getBaseContext());
        } else {
            return BaseApp.getInstance().getBaseContext().getSharedPreferences(SecurityUtils.Md5(shareName), Context.MODE_PRIVATE);
        }
    }
}
