package com.android.common.net.base;

import java.lang.reflect.Method;

import rx.Observable;

/**
 * token机制监听
 * Created by fengyulong on 2017/4/6.
 */

public interface IGlobalManager {
    /**
     * Exit the login state.
     */
    void exitLogin(Response response);

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    Observable<?> refreshTokenWhenTokenInvalid();

    /**
     * Update the token of the args in the method.
     * <p>
     * PS： 因为这里使用的是 GET 请求，所以这里就需要对 Query 的参数名称为 token 的方法。
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    void updateMethodToken(Method method, Object[] args);

    /**
     * 是否需要刷新token
     *
     * @return
     */
    boolean isTokenNeedRefresh();
}
