package cn.longmaster.hospital.doctor.core.upload.simple;

import android.support.annotation.NonNull;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.upload.NginxUploadTask;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * Created by lm-pc on 16/8/25.
 */
public abstract class BaseFileUploader {
    private final String TAG = getClass().getSimpleName();
    private OnNginxUploadStateCallback callback;

    protected abstract String getUrl();

    protected abstract int getOpType();

    protected abstract String getTaskId();

    protected abstract String getFilePath();

    protected abstract void onPutParams(Map<String, Object> params);

    protected void putParams(Map<String, Object> params) {
        onPutParams(params);
    }

    public BaseFileUploader(@NonNull OnNginxUploadStateCallback callback) {
        this.callback = callback;
    }

    public void startUpload() {
        String url = getUrl();
        Map<String, Object> params = new HashMap<>();
        initBaseParams(params);
        putParams(params);

        JSONObject jsonObject = new JSONObject(params);
        RequestParams requestParams = new RequestParams();
        requestParams.put("json", jsonObject);
        url = url + "?" + requestParams.toString();
        Logger.logI(Logger.COMMON, TAG + "#url:" + url);
        NginxUploadTask nginxUploadTask = new NginxUploadTask(url, getFilePath(), MD5Util.md5(getFilePath().hashCode() + ""), callback);
        Logger.logD(TAG, Thread.currentThread().getName());
        nginxUploadTask.startUpload();
    }

    protected void initBaseParams(Map<String, Object> params) {
        params.put("op_type", getOpType());
        params.put("task_id", getTaskId());
        params.put("c_ver", AppConfig.CLIENT_VERSION);
        params.put("c_type", "1");
        UserInfo userInfo = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo();
        params.put("user_id", userInfo.getUserId());
        params.put("gender", "0");
        if (userInfo.getUserId() != 0) {
            params.put("sid", userInfo.getLoginAuthKey());
        }
    }

    public abstract static class DefulteUploadStateCallback implements OnNginxUploadStateCallback{
        @Override
        public abstract void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes);
        @Override
        public abstract void onUploadComplete(String sessionId, int code, String content);
        @Override
        public abstract void onUploadCancle(String sessionId);
        @Override
        public abstract void onUploadException(String sessionId, Exception exception);
    }

    public class UploadResult{

        /**
         * op_type : 3004
         * task_id : 3004
         * code : 0
         * user_id : 1005055
         * md5 : bb5f1175836f9458ba1c4563aec4b50a
         * file_name : 202001.02..10-02-33_1577930553651691610850.jpg
         */

        @JsonField("op_type")
        private String opType;
        @JsonField("task_id")
        private String taskId;
        @JsonField("code")
        private String code;
        @JsonField("userId")
        private String user_id;
        @JsonField("md5")
        private String md5;
        @JsonField("file_name")
        private String fileName;

        public String getOpType() {
            return opType;
        }

        public void setOpType(String opType) {
            this.opType = opType;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
