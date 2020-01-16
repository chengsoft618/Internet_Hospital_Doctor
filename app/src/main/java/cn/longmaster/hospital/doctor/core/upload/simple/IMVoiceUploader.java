package cn.longmaster.hospital.doctor.core.upload.simple;

import android.support.annotation.NonNull;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * 图片医嘱上传类
 * Created by yangyong on 16/8/25.
 */
public class IMVoiceUploader extends BaseFileUploader {
    public String filePath;
    public int actType = 1;
    public String ext = "amr";
    public int groupId;

    public IMVoiceUploader(@NonNull OnNginxUploadStateCallback callback) {
        super(callback);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_IM_VOICE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("act_type", actType);
        params.put("ext", ext);
        params.put("file_name", filePath.substring(filePath.lastIndexOf("/") + 1));
        params.put("group_id", groupId);
    }
}
