package com.android.common.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {

    public static final boolean allowD;
    public static final boolean allowE;
    public static final boolean allowI;
    public static final boolean allowV;
    public static final boolean allowW;
    public static final boolean allowWtf;
    public static String customTagPrefix = "";
    public static CustomLogger customLogger;

    static {
        if (CommonUtils.DEBUG()) {
            allowD = true;
            allowE = true;
            allowI = true;
            allowV = true;
            allowW = true;
            allowWtf = true;
        } else {
            allowD = false;
            allowE = false;
            allowI = false;
            allowV = false;
            allowW = false;
            allowWtf = false;
        }
    }

    private LogUtils() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag + "-----";
    }

    public static void d(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowD) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowD) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowE) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowE) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowI) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowI) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowV) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowV) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowW) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowW) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowWtf) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (ValidateUtil.isEmpty(content)) {
            content = "";
        }
        if (!allowWtf) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
            Log.wtf(tag, tr);
        }
    }

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

}
