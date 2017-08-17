package com.creditpomelo.accounts.net.api.customer.response;

import java.io.Serializable;

/**
 * Created by fengyulong on 2017/8/3.
 */

public class LoginResponse implements Serializable {

    private String id;//用户id
    private String roleName;//角色名称  leader：领导，finance：财务，accountant：会计
    private String orgName;//机构名称

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getOrgName() {
        return orgName;
    }
}
