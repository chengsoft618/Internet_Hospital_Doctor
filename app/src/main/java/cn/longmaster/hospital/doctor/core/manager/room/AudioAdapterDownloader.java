package cn.longmaster.hospital.doctor.core.manager.room;


import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.utils.StringUtils;

/**
 * Created by yangyong on 2015/09/10.
 */
public class AudioAdapterDownloader extends Thread {
    private boolean isForced;
    private OnAudioAdapterDownloadListener onAudioAdapterDownloadListener;
    private File file;
    private String clientToken;

    @AppApplication.Manager
    private UserInfoManager userInfoManager;

    public AudioAdapterDownloader(boolean isForced, String clientToken, File file, OnAudioAdapterDownloadListener onAudioAdapterDownloadListener) {
        AppApplication.getInstance().injectManager(this);
        this.isForced = isForced;
        this.onAudioAdapterDownloadListener = onAudioAdapterDownloadListener;
        this.file = file;
        this.clientToken = clientToken;
    }

    private String getHeader(HttpURLConnection httpURLConnection, String key, String defaultKey) {
        String headerField = httpURLConnection.getHeaderField(key);
        Logger.logD(Logger.COMMON, "headerField_key:" + key + "->headerField:" + headerField);
        if (headerField == null) {
            return defaultKey;
        } else {
            return headerField;
        }
    }

    @Override
    public void run() {
        try {
            String json = getJson();
            Logger.logD(Logger.COMMON, "->json:" + json);
            String url = "http://app.amx.langma.cn/index.php?json=" + URLEncoder.encode(json, "UTF-8");
            Logger.logD(Logger.COMMON, "->url:" + url);
            HttpURLConnection httpURLConnection = null;
            OutputStream outputStream = null;
            boolean isNeedDeleteFile = false;
            try {
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.connect();

                int code = httpURLConnection.getResponseCode();
                String result = getHeader(httpURLConnection, "result", "");
                Logger.logD(Logger.COMMON, "->code:" + code + ",result:" + result);
                if (code == HttpURLConnection.HTTP_OK && StringUtils.equals(result, "0")) {
                    String serverToken = getHeader(httpURLConnection, "token", "");
                    if (!isForced && serverToken.equals(clientToken) && file.exists()) {
                        // 不强制下载 且 服务器和本地token相等 do nothing
                    } else {
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
                    onAudioAdapterDownloadSuccess(clientToken, file);

                } else {
                    Logger.logD(Logger.COMMON, "->onAudioAdapterDownloadFailed-->" + "->code:" + code + ",result:" + result);
                    onAudioAdapterDownloadFailed();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (outputStream != null) {
                    outputStream.flush();
                }
                if (isNeedDeleteFile) {
                    file.delete();
                }

                Logger.logD(Logger.COMMON, "->onAudioAdapterDownloadFailed-->IOException");
                onAudioAdapterDownloadFailed();
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
            Logger.logD(Logger.COMMON, "->onAudioAdapterDownloadFailed-->Exception");
            onAudioAdapterDownloadFailed();
        }
    }

    private String getJson() throws JSONException {
        UserInfo userInfo = userInfoManager.getCurrentUserInfo();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op_type", 6000);
        jsonObject.put("user_id", userInfo.getUserId());
        jsonObject.put("ver", AppConfig.CLIENT_VERSION);
        jsonObject.put("task_id", System.currentTimeMillis());
        jsonObject.put("c_type", 1);
        jsonObject.put("phone_type", Build.MODEL);
        jsonObject.put("rom_version", Build.DISPLAY);
        jsonObject.put("sdk_version", Build.VERSION.RELEASE);

        if (isForced) {
            jsonObject.put("client_token", "0");
        } else {
            jsonObject.put("client_token", clientToken);
        }

        jsonObject.put("app_id", 2);
        jsonObject.put("login_auth_key", userInfo.getLoginAuthKey());
        return jsonObject.toString();
    }

    private void onAudioAdapterDownloadSuccess(final String token, final File file) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                onAudioAdapterDownloadListener.onAudioAdapterDownloadSuccess(token, file);
            }
        });
    }

    private void onAudioAdapterDownloadFailed() {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                onAudioAdapterDownloadListener.onAudioAdapterDownloadFailed();
            }
        });
    }

    public interface OnAudioAdapterDownloadListener {
        void onAudioAdapterDownloadSuccess(String token, File file);

        void onAudioAdapterDownloadFailed();
    }
}
