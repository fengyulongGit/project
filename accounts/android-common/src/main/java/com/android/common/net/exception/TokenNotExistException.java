package com.android.common.net.exception;

import com.android.common.net.base.Response;

/**
 * token不存在错误
 * Created by fengyulong on 2017/4/6.
 */
public class TokenNotExistException extends RuntimeException {
    private Response response;

    public TokenNotExistException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
