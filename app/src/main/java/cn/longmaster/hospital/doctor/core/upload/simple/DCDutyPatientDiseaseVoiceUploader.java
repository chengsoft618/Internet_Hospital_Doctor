package cn.longmaster.hospital.doctor.core.upload.simple;

import android.support.annotation.NonNull;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * @author ABiao_Abiao
 * @date 2020/1/2 9:17
 * @description:
 */
public class DCDutyPatientDiseaseVoiceUploader extends BaseFileUploader {
    private String filePath;
    private String ext = "amr";
    private int actType = 1;

    public DCDutyPatientDiseaseVoiceUploader(@NonNull OnNginxUploadStateCallback callback) {
        super(callback);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_UPLOAD_VOICE;
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
        params.put("ext", ext);
        params.put("file_name", filePath.substring(filePath.lastIndexOf("/") + 1));
        params.put("act_type", actType);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
