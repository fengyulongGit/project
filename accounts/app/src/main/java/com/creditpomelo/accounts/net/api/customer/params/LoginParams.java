package com.creditpomelo.accounts.net.api.customer.params;

import com.creditpomelo.accounts.net.api.base.APIRequestParams;

import java.util.Map;

/**
 * Created by fengyulong on 2017/8/10.
 */

public class LoginParams extends APIRequestParams {
    @Override
    public Map<String, Object> addParams() {
        params.put("userName", userName);
        params.put("password", password);
        return params;
    }

    private String userName;//登录名
    private String password;//密码

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
