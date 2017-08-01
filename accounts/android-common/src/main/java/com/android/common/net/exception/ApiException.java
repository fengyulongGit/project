package com.android.common.net.exception;

import com.android.common.net.base.Response;

/**
 * 指定 API 的通用错误
 * Created by fengyulong on 2017/4/6.
 */
public class ApiException extends RuntimeException {
    private Response response;

    public ApiException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
