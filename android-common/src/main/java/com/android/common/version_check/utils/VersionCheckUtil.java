package com.android.common.version_check.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.android.common.utils.FileUtils;
import com.android.common.version_check.service.DownloadApkService;

import java.io.File;

public class VersionCheckUtil {
    // 调用系统组件安装apk
    public static void installApk(Context context, String filePath, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName) || TextUtils.isEmpty(filePath)) {
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (FileUtils.isExist(filePath)) {
                File file = new File(filePath);
                if (file.length() > 0) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(context, "com.android.common.fileprovider", file);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                }
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            if (FileUtils.isExist(filePath)) {
                File file = new File(filePath);
                if (file.length() > 0) {
                    intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                }
            }
        } else {
            FileUtils.copyFileToData(context, filePath, fileName);
            intent.setDataAndType(Uri.parse("file://" + context.getFilesDir().getPath() + "/" + fileName), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

    // 下载apk服务启动
    public static void downloadApk(Context context, String title, String url, String fileName, String filePath) {
        Intent intent = new Intent(context, DownloadApkService.class);
        intent.putExtra("title", title);
        intent.putExtra("downloadUrl", url);
        intent.putExtra("fileName", fileName);
        intent.putExtra("filePath", filePath);
        context.startService(intent);
    }
}
