package com.android.common.net.base;

/**
 * 公共返回类
 * Created by fengyulong on 2017/4/7.
 */

public class Response<T> {

    public T data;
    private int code;
    private String message;

    public boolean isTokenNotExist() {
        return code == ErrorCode.TOKEN_NOT_EXIST;
    }

    public boolean isTokenOtherDeviceLogin() {
        return code == ErrorCode.TOKEN_OTHER_DEVICE_LOGIN;
    }

    public boolean isTokenInvalid() {
        return code == ErrorCode.TOKEN_INVALID;
    }

    public boolean isSuccess() {
        return code == ErrorCode.SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public interface ErrorCode {
        int SUCCESS = 0;
        int TOKEN_NOT_EXIST = 111;
        int TOKEN_OTHER_DEVICE_LOGIN = 6007;
        int TOKEN_INVALID = Integer.MIN_VALUE;
        int COMMIT_REOEAT = 2004;
    }
}
