package com.creditpomelo.accounts.net.api.customer.service;

import com.creditpomelo.accounts.net.api.customer.response.LoginResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by fengyulong on 2017/8/10.
 */

public interface APICustomerService {
    /**
     * 用户登陆接口
     */
    @FormUrlEncoded
    @POST("customer/login")
    Observable<LoginResponse> login(@FieldMap Map<String, Object> params);
}
