package com.android.common.net.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数
 * Created by fengyulong on 2017/4/11.
 */

public abstract class RequestParam {
    public Map<String, Object> params = new HashMap<>();

    public abstract Map<String, Object> params();

    public abstract Map<String, Object> addParams();

    public abstract Map<String, Object> addPublicParams(Map<String, Object> params);

    public abstract Map<String, Object> securityRequestParams(Map<String, Object> params);
}
