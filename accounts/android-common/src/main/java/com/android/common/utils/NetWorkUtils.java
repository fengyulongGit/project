package com.android.common.utils;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NetWorkUtils {
    public static Observable donwloadFile(String url, String filePath) {
        return donwloadFile(url, filePath, null);
    }

    public static Observable donwloadFile(final String url, final String filePath, final Handler handler) {
        return Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean s) {
                        return NetWorkUtils.downloadFile(url, filePath, handler);
                    }
                });
    }

    private static boolean downloadFile(String url, String filePath, Handler handler) {
        URL htmlUrl = null;
        InputStream inStream = null;

        String tmpFilePath = filePath + ".tmp";


        boolean flag = false;

        byte[] buffer = new byte[1024];
        int byteread = 0, size = 0;

        try {
            FileUtils.makeDirs(new File(filePath).getParent());

            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            inStream = httpConnection.getInputStream();
            FileOutputStream fs = new FileOutputStream(tmpFilePath);

            size = httpConnection.getContentLength();

            int downloadSize = 0;

            if (handler != null) {
                handler.handleMessage(handler.obtainMessage(0, size));
            }

            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
                if (handler != null) {
                    downloadSize += byteread;
                    handler.handleMessage(handler.obtainMessage(1, downloadSize));
                }
            }

            if (handler != null) {
                handler.handleMessage(handler.obtainMessage(2));
            }

            fs.flush();
            fs.close();

            flag = FileUtils.renameFile(tmpFilePath, filePath);
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null) {
                handler.handleMessage(handler.obtainMessage(2));
            }
        }

        return flag;
    }

}
