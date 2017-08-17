package com.creditpomelo.accounts.net.version.service;


import com.creditpomelo.accounts.net.version.response.VersionCheckResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 版本-接口
 * Created by fengyulong on 2017/4/13.
 */

public interface VersionService {
    /**
     * 检测更新接口
     */
    @FormUrlEncoded
    @POST("android")
    Observable<VersionCheckResponse> versionCheck(@FieldMap Map<String, Object> params);
}
