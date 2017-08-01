package com.android.common.net.base;

import com.android.common.app.BaseApp;
import com.android.common.net.exception.ApiException;
import com.android.common.net.exception.TokenNotExistException;
import com.android.common.utils.LogUtils;
import com.android.common.utils.NetworkProber;

/**
 * 自定义
 * Created by fengyulong on 2017/4/12.
 */

public abstract class Subscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        LogUtils.e("", e);
//        if (e instanceof SocketTimeoutException) {
//            showError("当前网络不稳定，请重试");
//        } else if (e instanceof UnknownHostException) {
//
//        } else if (e instanceof ApiException) {
//
//        } else {
//            showError(e.getMessage());
//        }
        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setE(e);
        if (!NetworkProber.getDeviceNetwork(BaseApp.getInstance().getBaseContext())) {
            errorMsg.setCode(ErrorMsg.ErrorCode.NET_NONE);
            errorMsg.setMessage("好像没有网络哦");
        } else if (e instanceof ApiException) {
            //ApiException处理
            Response response = ((ApiException) e).getResponse();

            errorMsg.setCode(response.getCode());
            errorMsg.setMessage(Response.ErrorCode.COMMIT_REOEAT == response.getCode() ? "" : response.getMessage());
        } else if (e instanceof TokenNotExistException) {
            errorMsg.setMessage("");
        } else {
            /*if (CommonUtils.DEBUG()) {
                errorMsg.setMessage(e.getMessage());
            } else {
                errorMsg.setMessage("当前网络不稳定，请稍后重试");
            }*/
            errorMsg.setMessage("当前网络不稳定，请稍后重试");
        }

        showError(errorMsg);
    }

    @Override
    public void onCompleted() {

    }

    protected abstract void showError(ErrorMsg errorMsg);
}
