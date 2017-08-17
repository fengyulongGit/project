package com.creditpomelo.accounts.net.base;

import com.creditpomelo.accounts.net.api.base.APIRequestParams;

import java.util.Map;

/**
 * Created by fengyulong on 2017/4/13.
 */

public class EmptyParams extends APIRequestParams {
    @Override
    public Map<String, Object> addParams() {
        return params;
    }
}
