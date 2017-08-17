package com.creditpomelo.accounts.net.version.base;

import com.android.common.net.base.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数父类--完成公共参数拼接和加密
 * Created by fengyulong on 2017/4/11.
 */

public abstract class VersionRequestParams extends RequestParam {

    @Override
    public Map<String, Object> params() {
        return securityRequestParams(addPublicParams(addParams()));
    }

    @Override
    public Map<String, Object> addPublicParams(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    @Override
    public Map<String, Object> securityRequestParams(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
