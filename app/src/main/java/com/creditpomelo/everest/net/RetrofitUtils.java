package com.creditpomelo.everest.net;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.android.common.net.okhttp.OkHttpUtils;
import com.creditpomelo.everest.BuildConfig;
import com.creditpomelo.everest.main.MainActivity;
import com.creditpomelo.everest.main.base.App;
import com.android.common.net.base.IGlobalManager;
import com.android.common.net.converter.GsonConverterFactory;
import com.android.common.net.proxy.ProxyHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * api 请求工具类
 * Created by fengyulong on 2017/4/6.
 */
public class RetrofitUtils implements IGlobalManager {

    private static final String API = BuildConfig.SERVER_URL;

    private final static String TOKEN = "token";

    private final static int REFRESH_TOKEN_VALID_TIME = 30;
    private static long tokenChangedTime = 0;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;

    private static RetrofitUtils instance;
    private Retrofit retrofit;

    private final static Object mRetrofitLock = new Object();

    private RetrofitUtils() {
        retrofit = new Retrofit.Builder().client(OkHttpUtils.getInstance().getHttpClient())
                .baseUrl(API)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    public <T> T get(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> tClass) {
        T t = retrofit.create(tClass);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[]{tClass}, new ProxyHandler(t, this));
    }

    @Override
    public void exitLogin() {
        // Token 不存在，执行退出登录的操作。（为了防止多个请求，都出现 Token 不存在的问题，这里需要取消当前所有的网络请求）
        // Cancel all the netWorkRequest
        OkHttpUtils.getInstance().getHttpClient().dispatcher().cancelAll();

        //FIXME 需要登录，启动登录页 Goto the home page
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(App.getInstance().getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                App.getInstance().getBaseContext().startActivity(intent);
//                Toast.makeText(BaseApplication.getContext(), "Token is not existed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    @Override
    public Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            // Have refreshed the token successfully in the valid time.
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                mIsTokenNeedRefresh = true;
                return Observable.just(true);
            } else {
                //FIXME 重新加载token call the refresh token api.
//                RetrofitUtil.getInstance().get(IApiService.class).refreshToken().subscribe(new Subscriber<TokenModel>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mRefreshTokenError = e;
//                    }
//
//                    @Override
//                    public void onNext(TokenModel model) {
//                        if (model != null) {
//                            mIsTokenNeedRefresh = true;
//                            tokenChangedTime = new Date().getTime();
//                            GlobalToken.updateToken(model.token);
//                            Log.d(TAG, "Refresh token success, time = " + tokenChangedTime);
//                        }
//                    }
//                });
                if (mRefreshTokenError != null) {
                    return Observable.error(mRefreshTokenError);
                } else {
                    return Observable.just(true);
                }
            }
        }
    }

    /**
     * Update the token of the args in the method.
     * <p>
     * PS： 因为这里使用的是 GET 请求，所以这里就需要对 Query 的参数名称为 token 的方法。
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    @Override
    public void updateMethodToken(Method method, Object[] args) {
//        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(GlobalToken.getToken())) {
//            Annotation[][] annotationsArray = method.getParameterAnnotations();
//            Annotation[] annotations;
//            if (annotationsArray != null && annotationsArray.length > 0) {
//                for (int i = 0; i < annotationsArray.length; i++) {
//                    annotations = annotationsArray[i];
//                    for (Annotation annotation : annotations) {
//                        if (annotation instanceof Query) {
//                            if (TOKEN.equals(((Query) annotation).value())) {
//                                args[i] = GlobalToken.getToken();
//                            }
//                        }
//                    }
//                }
//            }
//            mIsTokenNeedRefresh = false;
//        }
    }

    @Override
    public boolean isTokenNeedRefresh() {
        return mIsTokenNeedRefresh;
    }
}
