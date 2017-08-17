package com.creditpomelo.accounts.net.version.base;

/**
 * 版本检测公共参数
 * Created by fengyulong on 2017/4/13.
 */

public interface VersionConstants {

    /**
     * 新版本升级类型
     */
    interface UP_TYPE {
        /**
         * 不更新
         */
        String UN_UPDATE = "0";
        /**
         * 静默更新
         */
        String SILENCE = "1";
        /**
         * 强制更新
         */
        String COMPEL = "2";
    }
}
