package com.android.common.net.base;

/**
 * 错误分析
 * Created by fengyulong on 2017/4/13.
 */

public class ErrorMsg {
    private int code;
    private String message;
    private Throwable e;

    public ErrorMsg() {
        this.code = ErrorCode.NET_ERROR;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNetError() {
        return ErrorCode.NET_ERROR == code;
    }

    public boolean isNetNone() {
        return ErrorCode.NET_NONE == code;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public interface ErrorCode {
        int NET_NONE = Integer.MIN_VALUE;//无网络
        int NET_ERROR = NET_NONE + 1;//网络错误、服务端错误
    }
}
