package com.creditpomelo.accounts.net.api.base;

import com.android.common.net.base.RequestParam;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 参数父类--完成公共参数拼接和加密
 * Created by fengyulong on 2017/4/11.
 */

public abstract class APIRequestParams extends RequestParam {
    final String PLATFORM = "1";//平台类型 1：Android   2：ios   3：wechat

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = securityRequestParams(addPublicParams(addParams()));

        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (entry.getValue() == null) {
                //map.put(key, "奇数");   //ConcurrentModificationException
                //map.remove(key);      //ConcurrentModificationException
                it.remove();        //OK
            }
        }

        return params;
    }

    @Override
    public Map<String, Object> addPublicParams(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
//        params.put("platform", PLATFORM);
//        params.put("channel", CommonUtils.getChannel());
//        params.put("version", CommonUtils.getVersionName());
//        params.put("uid", DeviceUtils.getInstance().getDeviceId());
//        params.put("ts", System.currentTimeMillis() + "");
//        if (SharedPrefsUtils.isLogin()) {
//            params.put("token", SharedPrefsUtils.getAccessToken());
//        }
//        if (SharedPrefsUtils.isLogin()) {
//            params.put("userId", SharedPrefsUtils.getUid());
//        }
        return params;
    }

    @Override
    public Map<String, Object> securityRequestParams(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
//        String jsonStr = GsonUtils.toJson(params);
//        LogUtils.i(jsonStr);
//        String aesJsonStr = SecurityUtils.base64Encode(SecurityUtils.aes128Encrypt(jsonStr, BuildConfig.AES_KEY, BuildConfig.AES_IV));
//        HashMap<String, Object> reqMap = new HashMap<>();
//        reqMap.put("message", aesJsonStr);
//        reqMap.put("signature", SecurityUtils.md5(aesJsonStr + BuildConfig.AES_KEY));
        return params;
    }

}
