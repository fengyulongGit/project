package com.creditpomelo.accounts.net.version.params;

import com.android.common.utils.CommonUtils;
import com.creditpomelo.accounts.net.version.base.VersionRequestParams;

import java.util.Map;

/**
 * 版本检测升级
 * Created by fengyulong on 2017/4/13.
 */

public class VersionCheckParams extends VersionRequestParams {
    @Override
    public Map<String, Object> addParams() {
        params.put("versionNo", CommonUtils.getVersionName());
        params.put("channel", CommonUtils.getChannel());
        return params;
    }
}
