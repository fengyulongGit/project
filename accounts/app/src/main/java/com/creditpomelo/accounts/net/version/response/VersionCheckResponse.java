package com.creditpomelo.accounts.net.version.response;

/**
 * 版本检测升级
 * Created by fengyulong on 2017/4/13.
 */
public class VersionCheckResponse {

    private String content;//升级内容
    private String upType;//升级类型0不升级1静默升级2强制升级
    private String url;//下载地址
    private String versionNo;//最新版本号

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpType() {
        return upType;
    }

    public void setUpType(String upType) {
        this.upType = upType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}
