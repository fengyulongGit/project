package com.artifex.mupdfdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.artifex.mupdfdemo.MuPDFActivity;

import java.io.File;
import java.security.MessageDigest;

/**
 * pdf reader 工具类
 * Created by fengyulong on 2016/11/19.
 */
public class PDFReaderUtils {

    /**
     * 从网络打开pdf
     *
     * @param pdfUrl   pdf网络地址
     * @param mContext context
     */
    public static void loadPDF(String pdfUrl, final Context mContext) {
//        PDFReaderUtils.loadPDF("https://jr-mobile.yingu.com/h5/protocol/join", context);
        System.out.println("pdfUrl" + pdfUrl);

        Task task = new Task(mContext);
        task.setFileDownloadListener(new Task.OnFileDownloadListener() {
            @Override
            public void onFileDownloadStart() {

            }

            @Override
            public void onFileDownloadProgress(int total, int downloadSize) {

            }

            @Override
            public void onFileDownloadFinish(File file) {

                Uri uri = Uri.parse(file.getAbsolutePath());
                Intent intent = new Intent(mContext, MuPDFActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                mContext.startActivity(intent);
            }

            @Override
            public void onFileDownloadError(Exception e) {

            }
        });
        task.execute(pdfUrl, mContext.getFilesDir().getAbsolutePath() + File.separator, md5(pdfUrl));

    }

    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * MD5摘要
     */
    private static String md5(String str) {
        if (str == null) {
            return null;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return byte2HexStr(md5.digest(str.getBytes(DEFAULT_CHARSET)));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 将二进制转换成16进制
     */
    private static String byte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
