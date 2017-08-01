package com.android.common.version_check.activity;

import android.app.NotificationManager;
import android.content.Context;

import com.android.common.app.BaseAppCompatActivity;
import com.android.common.version_check.utils.VersionCheckUtil;

public class InstallAPKActivity extends BaseAppCompatActivity {

    @Override
    protected int layoutTitleResID() {
        return 0;
    }

    @Override
    protected int layoutContentResID() {
        return 0;
    }

    @Override
    protected int layoutErrorResID() {
        return 0;
    }

    @Override
    protected void initTitleBar() {
    }

    @Override
    protected void initViewData() {
        final int NOTIFICATION_ID = getIntent().getIntExtra("NOTIFICATION_ID", 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        final String fileName = getIntent().getStringExtra("fileName");
        final String filePath = getIntent().getStringExtra("filePath");

        VersionCheckUtil.installApk(this, filePath, fileName);

        finish();
    }

    @Override
    protected void requestData() {

    }
}
