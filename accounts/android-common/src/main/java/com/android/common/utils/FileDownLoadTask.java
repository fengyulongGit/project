package com.android.common.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.android.common.ui.dialog.CustomProgressDialog;
import com.android.common.ui.dialog.DialogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <br>包名：com.yingu.jr.utils
 * <br>项目名称：jr
 * <br>描述：文件下载任务
 * <br>创建人：BaoZhi
 * <br>创建时间：2016/6/8 0008 15:09
 */

/**
 * pdf reader 工具类
 * Created by fengyulong on 2016/11/19.
 */
public class FileDownLoadTask extends AsyncTask<String, Integer, File> {

    private OnFileDownloadListener fileDownloadListener;
    private CustomProgressDialog loadingDialog;
    private Context mContext;
    private long expired = 30 * 60 * 1000;

    public FileDownLoadTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (fileDownloadListener != null) {
            fileDownloadListener.onFileDownloadStart();
        }
        if (loadingDialog == null && mContext != null) {
            loadingDialog = (CustomProgressDialog) DialogUtil.createProgressDialog(mContext, "");
        }
        loadingDialog.show();
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);

        if (fileDownloadListener != null) {
            if (file != null) {
                fileDownloadListener.onFileDownloadFinish(file);
            } else {
                fileDownloadListener.onFileDownloadError(new Exception("文件下载失败"));
            }
        }
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (fileDownloadListener != null) {
            fileDownloadListener.onFileDownloadProgress(values[0], values[1]);
        }
    }

    public void setFileDownloadListener(OnFileDownloadListener fileDownloadListener) {
        this.fileDownloadListener = fileDownloadListener;
    }

    @Override
    protected File doInBackground(String... params) {

        String url = params[0];
        String filePath = params[1];
        String fileName = params[2];
        try {
            File fileSave = new File(filePath + fileName);
            long modifiedTime = fileSave.lastModified();
            long currentTime = System.currentTimeMillis();
            if (fileSave.exists() && currentTime - modifiedTime < expired) {
                return fileSave;
            } else {
                File fileDir = new File(filePath);
                HttpURLConnection connection = getConnection(url);
                connection.connect();
                InputStream is = connection.getInputStream();
                int fileSize = getLength(connection);

                if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
                if (is == null) throw new RuntimeException("stream is null");

                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                if (!fileSave.exists()) {
                    fileSave.createNewFile();
                } else {
                    fileSave.delete();
                }

                FileOutputStream fos = new FileOutputStream(fileSave);
                //把数据存入路径+文件名
                byte buf[] = new byte[1024];
                int downLoadFileSize = 0;

                publishProgress(fileSize, downLoadFileSize);

                do {
                    //循环读取
                    int numread = is.read(buf);
                    if (numread == -1) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                    downLoadFileSize += numread;

                    publishProgress(fileSize, downLoadFileSize);
                } while (true);

                //完毕，关闭所有链接
                fos.close();
                is.close();

                return fileSave;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * 获取http连接处理类HttpURLConnection
    */
    private HttpURLConnection getConnection(String urlstr) throws IOException {
        URL url;
        HttpURLConnection urlcon = null;
        url = new URL(urlstr);
        urlcon = (HttpURLConnection) url.openConnection();
        return urlcon;
    }

    /*
    * 获取连接文件长度。
    */
    public int getLength(HttpURLConnection urlcon) {
        return urlcon.getContentLength();
    }

    public interface OnFileDownloadListener {
        public void onFileDownloadStart();

        public void onFileDownloadProgress(int total, int downloadSize);

        public void onFileDownloadFinish(File file);

        public void onFileDownloadError(Exception e);
    }
}
