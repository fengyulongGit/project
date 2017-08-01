package com.android.common.net.exception;

import com.android.common.net.base.Response;

/**
 * token过期错误
 * Created by fengyulong on 2017/4/6.
 */
public class TokenInvalidException extends RuntimeException {
    private Response response;

    public TokenInvalidException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
