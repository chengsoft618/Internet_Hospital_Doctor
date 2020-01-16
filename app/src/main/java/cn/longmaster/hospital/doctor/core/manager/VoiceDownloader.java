package cn.longmaster.hospital.doctor.core.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.utils.SPUtils;

/**
 * 语音医嘱下载
 * Created by Tengshuxiang on 2015-09-21.
 */
public class VoiceDownloader extends Thread {
    public final static String TAG = VoiceDownloader.class.getSimpleName();

    private String mFileName;
    private int mAppointId;
    private OnDownloadFinishedListener mOnDownloadFinishedListener;

    public VoiceDownloader(String fileName, int appointId, OnDownloadFinishedListener onDownloadFinishedListener) {
        mFileName = fileName;
        mAppointId = appointId;
        mOnDownloadFinishedListener = onDownloadFinishedListener;
    }

    private String getHeader(HttpURLConnection httpURLConnection, String key, String defaultKey) {
        String headerField = httpURLConnection.getHeaderField(key);
        if (headerField == null) {
            return defaultKey;
        } else {
            return headerField;
        }
    }

    @Override
    public void run() {
        String filePath = "";
        try {
            String token = SPUtils.getInstance().getString(mFileName + "token", "0");
            String url = AppConfig.getDfsUrl() + "3005/" + token + "/" + mFileName;
            Logger.logD(Logger.APPOINTMENT, "语音医嘱：" + url);
            HttpURLConnection httpURLConnection = null;
            OutputStream outputStream = null;
            boolean isNeedDeleteFile = false;
            filePath = SdManager.getInstance().getOrderVoicePath(mFileName, mAppointId + "");
            File file = new File(filePath);
            try {
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                int code = httpURLConnection.getResponseCode();
                String result = getHeader(httpURLConnection, "code", "");

                if (code == HttpURLConnection.HTTP_OK && result.equals("0")) {
                    String serverToken = getHeader(httpURLConnection, "token", "");
                    if (serverToken.equals(token) && file.exists()) {
                        // 不强制下载 且 服务器和本地token相等 do nothing
                    } else {
                        SPUtils.getInstance().put(mFileName + "token", serverToken);
                        isNeedDeleteFile = true;
                        file.delete();
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                        outputStream = new FileOutputStream(file, false);

                        InputStream inputStream = httpURLConnection.getInputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.flush();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                if (outputStream != null) {
                    outputStream.flush();
                }
                if (isNeedDeleteFile) {
                    file.delete();
                }
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.logD(Logger.APPOINTMENT, "FileUtil.isFileExist(filePath)：" + FileUtil.isFileExist(filePath));
        Logger.logD(Logger.APPOINTMENT, "filePath：" + filePath);
        if (FileUtil.isFileExist(filePath)) {
            mOnDownloadFinishedListener.onFinished();
        }
    }

    public interface OnDownloadFinishedListener {
        void onFinished();
    }
}
