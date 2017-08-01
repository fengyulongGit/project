package com.android.common.net.proxy;

import com.android.common.net.base.IGlobalManager;
import com.android.common.net.exception.TokenInvalidException;
import com.android.common.net.exception.TokenNotExistException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.Observable;
import rx.functions.Func1;

/**
 * 代理
 * Created by fengyulong on 2017/4/6.
 */
public class ProxyHandler implements InvocationHandler {

    private Object mProxyObject;
    private IGlobalManager mGlobalManager;

    public ProxyHandler(Object proxyObject, IGlobalManager globalManager) {
        mProxyObject = proxyObject;
        mGlobalManager = globalManager;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(null).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                try {
                    try {
                        if (mGlobalManager.isTokenNeedRefresh()) {
                            mGlobalManager.updateMethodToken(method, args);
                        }
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof TokenInvalidException) {
                            return mGlobalManager.refreshTokenWhenTokenInvalid();
                        } else if (throwable instanceof TokenNotExistException) {
                            TokenNotExistException exception = (TokenNotExistException) throwable;
                            mGlobalManager.exitLogin(exception.getResponse());
                            return Observable.error(throwable);
                        }
                        return Observable.error(throwable);
                    }
                });
            }
        });
    }
}
