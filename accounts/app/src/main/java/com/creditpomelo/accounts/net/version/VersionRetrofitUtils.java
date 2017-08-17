package com.creditpomelo.accounts.net.version;

import com.android.common.net.RetrofitUtils;
import com.creditpomelo.accounts.BuildConfig;

/**
 * api 请求工具类
 * Created by fengyulong on 2017/4/6.
 */
public class VersionRetrofitUtils extends RetrofitUtils {


    private static VersionRetrofitUtils instance;

    private VersionRetrofitUtils() {
        super();
    }

    public static VersionRetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (VersionRetrofitUtils.class) {
                if (instance == null) {
                    instance = new VersionRetrofitUtils();
                }
            }
        }
        return instance;
    }

    @Override
    protected String url() {
        return BuildConfig.VERSION_CHECK_SERVER_URL;
    }
}
