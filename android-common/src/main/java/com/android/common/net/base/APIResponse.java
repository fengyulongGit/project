package com.android.common.net.base;

import com.google.gson.annotations.SerializedName;

/**
 * 公共返回类
 * Created by fengyulong on 2017/4/7.
 */

public class APIResponse<T> {

    private boolean success;
    @SerializedName("error_code")
    private int errorCode;

    public T data;

    interface ErrorCode {
        int TOKEN_NOT_EXIST = 1000;
        int TOKEN_INVALID = 1001;
    }

    public boolean isTokenNotExist() {
        return errorCode == ErrorCode.TOKEN_NOT_EXIST;
    }

    public boolean isTokenInvalid() {
        return errorCode == ErrorCode.TOKEN_INVALID;
    }

    public boolean isSuccess() {
        return success;
    }
}
