package com.android.common.version_check.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.RemoteViews;

import com.android.common.R;
import com.android.common.utils.FileUtils;
import com.android.common.version_check.activity.InstallAPKActivity;
import com.android.common.version_check.utils.VersionCheckUtil;

import org.apache.http.util.ByteArrayBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadApkService extends Service {

    public static final int DOWNLOAD_ING = 0;// 下载中
    public static final int DOWNLOAD_FAIL = 1; // 失败
    public static final int DOWNLOAD_FINISH = 2; // 完成
    public static boolean isDownloading = false;
    final int NOTIFICATION_ID = 1;
    final int MAX_PROGRESS = 100;
    final int STEP_PROGRESS = 5;
    String APKFilePath = "";
    private int AKT_PROGRESS = 0;
    private long allsize;
    private int percent;
    private String downloadUrl;
    private String fileName;
    private String title;
    private Notification nf;
    private RemoteViews rv;
    private NotificationManager notificationManager;
    private Handler uploadProgressBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bdata = msg.getData();
            int progress = bdata.getInt("progress");

            try {
                switch (msg.what) {
                    case DOWNLOAD_FAIL:
                        nf = new Notification(android.R.drawable.stat_sys_download_done, title + "下载失败！", System.currentTimeMillis());
                        rv = new RemoteViews(DownloadApkService.this.getPackageName(), R.layout.apk_install_notification);
                        rv.setImageViewResource(R.id.notifyImage, android.R.drawable.stat_sys_download_done);
                        rv.setTextViewText(R.id.notifyMsg, title + "下载失败！");
                        nf.contentView = rv;

                        Intent intent2 = new Intent();

                        PendingIntent contentIntent2 = PendingIntent.getActivity(DownloadApkService.this, 0, intent2, 0);
                        nf.contentIntent = contentIntent2;

                        notificationManager.notify(NOTIFICATION_ID, nf);
                        AKT_PROGRESS = 0;

                        stopSelf();
                        break;
                    case DOWNLOAD_FINISH: {
                        nf = new Notification(android.R.drawable.stat_sys_download_done, title + "下载完成，点击安装", System.currentTimeMillis());
                        rv = new RemoteViews(DownloadApkService.this.getPackageName(), R.layout.apk_install_notification);
                        rv.setImageViewResource(R.id.notifyImage, android.R.drawable.stat_sys_download_done);
                        rv.setTextViewText(R.id.notifyMsg, title + "下载完成，点击安装");
                        nf.contentView = rv;

                        Intent intent = new Intent(DownloadApkService.this, InstallAPKActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("fileName", fileName);
                        intent.putExtra("filePath", APKFilePath);
                        intent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);

                        PendingIntent contentIntent = PendingIntent.getActivity(DownloadApkService.this, 0, intent, 0);
                        nf.contentIntent = contentIntent;

                        notificationManager.notify(NOTIFICATION_ID, nf);
                        AKT_PROGRESS = 0;

                        VersionCheckUtil.installApk(DownloadApkService.this, APKFilePath, fileName);

                        stopSelf();
                        break;
                    }
                    case DOWNLOAD_ING: {
                        rv.setProgressBar(R.id.customProgressBar, MAX_PROGRESS, progress, false);
                        rv.setTextViewText(R.id.notifyText, progress + "%");
                        notificationManager.notify(NOTIFICATION_ID, nf);
                        break;
                    }
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (isDownloading) {
            return;
        }
        downloadUrl = intent.getStringExtra("downloadUrl");
        fileName = intent.getStringExtra("fileName");
        title = intent.getStringExtra("title");
        APKFilePath = intent.getStringExtra("filePath");
        new File(APKFilePath).getParentFile().mkdirs();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nf = new Notification(android.R.drawable.stat_sys_download, title + "下载中...", System.currentTimeMillis());
        rv = new RemoteViews(this.getPackageName(), R.layout.apk_download_notification);
        rv.setImageViewResource(R.id.notifyImage, android.R.drawable.stat_sys_download);
        rv.setProgressBar(R.id.customProgressBar, MAX_PROGRESS, AKT_PROGRESS, false);
        rv.setTextViewText(R.id.notifyText, "0%");
        rv.setTextViewText(R.id.notifyMsg, title + "下载中...");
        nf.contentView = rv;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        nf.contentIntent = contentIntent;

        notificationManager.notify(NOTIFICATION_ID, nf);

        DownThread downThread = new DownThread();
        downThread.start();

        super.onStart(intent, startId);
    }

    private void sendDownloadMsg(int state, long downloadSize) {
        int percent = (int) (downloadSize * 100.0 / allsize);
        if (this.percent != percent) {
            this.percent = percent;
            SendThread sendThread = new SendThread(state);
            sendThread.start();

        }
    }

    private void downloadAPK() {
        isDownloading = true;
        if (URLUtil.isNetworkUrl(downloadUrl)) {
            HttpURLConnection connection = null;
            URL url = null;
            InputStream in = null;
            ByteArrayBuffer baf = null;
            FileOutputStream fos = null;
            String contentType = null;

            try {
                url = new URL(downloadUrl);
                connection = (HttpURLConnection) url.openConnection();
                in = connection.getInputStream();

                contentType = connection.getContentType();
                if (contentType != null && contentType.startsWith("text/vnd.wap.wml")) {
                    throw new Exception("contentType is text/vnd.wap.wml");
                }

                String tmpFilePath = APKFilePath + ".tmp";

                fos = new FileOutputStream(new File(tmpFilePath));
                baf = new ByteArrayBuffer(in.available());
                byte[] bytes = new byte[1024];

                if (connection.getContentLength() > 0) {
                    allsize = connection.getContentLength();
                }
                int downloadSize = 0;
                int current = 0;

                while ((current = in.read(bytes)) != -1) {
                    baf.append(bytes, 0, current);
                    downloadSize += current;
                    sendDownloadMsg(DOWNLOAD_ING, downloadSize);
                }
                fos.write(baf.toByteArray());
                fos.flush();

                FileUtils.renameFile(tmpFilePath, APKFilePath);

                new SendThread(DOWNLOAD_FINISH).start();
            } catch (Exception e) {
                e.printStackTrace();
                new SendThread(DOWNLOAD_FAIL).start();
            } finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                    if (null != fos) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        isDownloading = false;
    }

    class DownThread extends Thread {
        public void run() {
            downloadAPK();
        }
    }

    class SendThread extends Thread {
        private int state;

        public SendThread(int state) {
            this.state = state;
        }

        public void run() {
            Bundle bdata = new Bundle();
            bdata.putInt("progress", percent);
            Message message = uploadProgressBarHandler.obtainMessage();
            message.what = state;
            message.setData(bdata);
            message.sendToTarget();
        }
    }

}
