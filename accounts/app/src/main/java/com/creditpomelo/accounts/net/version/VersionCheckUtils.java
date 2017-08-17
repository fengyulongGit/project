package com.creditpomelo.accounts.net.version;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.common.net.base.ErrorMsg;
import com.android.common.net.base.Subscriber;
import com.android.common.ui.dialog.ConfirmDialog;
import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;
import com.android.common.utils.AppManager;
import com.android.common.utils.FileUtils;
import com.android.common.utils.ValidateUtil;
import com.android.common.version_check.utils.VersionCheckUtil;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.net.version.base.VersionConstants;
import com.creditpomelo.accounts.net.version.params.VersionCheckParams;
import com.creditpomelo.accounts.net.version.response.VersionCheckResponse;
import com.creditpomelo.accounts.net.version.service.VersionService;
import com.creditpomelo.accounts.utils.PathUtils;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fengyulong on 2017/4/13.
 */

public class VersionCheckUtils {
    private Context context;
    private Subscription subscription;
    private boolean isShowResultMessage = false;

    private VersionCheckUtils() {
    }

    public VersionCheckUtils(Context context) {
        this.context = context;
    }

    public static VersionCheckUtils getInstance(Context context) {
        return new VersionCheckUtils(context);
    }

    public VersionCheckUtils setShowResultMessage(boolean isShowResultMessage) {
        this.isShowResultMessage = isShowResultMessage;
        return this;
    }

    public void checkVersion() {
        unsubscribe();

        if (isShowResultMessage) {
            showProgressDialog("");
        }
        subscription = VersionRetrofitUtils.getInstance()
                .getProxy(VersionService.class)
                .versionCheck(new VersionCheckParams().params())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionCheckResponse>() {
                    @Override
                    protected void showError(ErrorMsg errorMsg) {
                        if (isShowResultMessage) {
                            if (errorMsg.isNetNone()) {
                                Toast.makeText(context, errorMsg.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                showDialog("当前已经是最新版本");
                            }
                        }

                        closeProgressDialog();
                    }

                    @Override
                    public void onNext(VersionCheckResponse versionCheckResponse) {
                        closeProgressDialog();

                        checkUp(versionCheckResponse);
                    }
                });
    }

    void checkUp(final VersionCheckResponse versionCheckResponse) {
        if (ValidateUtil.isNull(versionCheckResponse.getUpType())) {
            showDialog("当前已经是最新版本");
            return;
        }
        if (!VersionConstants.UP_TYPE.UN_UPDATE.equals(versionCheckResponse.getUpType())) {
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.setContent(versionCheckResponse.getContent());
            confirmDialog.setContentGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            confirmDialog.setLeftBtnContent("取消");
            confirmDialog.setOnBtnLeftClickListener(new ConfirmDialog.OnBtnLeftClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            confirmDialog.setRightBtnContent("更新");
            confirmDialog.setOnBtnRightClickListener(new ConfirmDialog.OnBtnRightClickListener() {
                @Override
                public void onClick(View v) {
                    String APKFilePath = PathUtils.APKFilePath(versionCheckResponse.getVersionNo());
                    if (FileUtils.isExist(APKFilePath)) {
                        VersionCheckUtil.installApk(context, APKFilePath, versionCheckResponse.getVersionNo());
                    } else {
                        VersionCheckUtil.downloadApk(context, context.getString(R.string.app_name), versionCheckResponse.getUrl(),
                                versionCheckResponse.getVersionNo(), APKFilePath);
                        Toast.makeText(context, "开始下载...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            confirmDialog.setOnDismissListener(new ConfirmDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface var1) {
                    if (VersionConstants.UP_TYPE.COMPEL.equals(versionCheckResponse.getUpType())) {
                        AppManager.getAppManager().AppExit(context, true);
                        MobclickAgent.onKillProcess(context);
                    }
                }
            });
            confirmDialog.create().show();
        } else {
            showDialog("当前已经是最新版本");
        }
    }

    private void showDialog(String message) {
        if (isShowResultMessage) {
            DialogUtil.showDialog(context, message, null, null);
        }
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * 加载的对话框
     */
    private CustomProgressDialog dialog;

    private void showProgressDialog(String content) {
        if (dialog == null && context != null) {
            dialog = (CustomProgressDialog) DialogUtil.createProgressDialog(context, content);
        }
        try {
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeProgressDialog() {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
