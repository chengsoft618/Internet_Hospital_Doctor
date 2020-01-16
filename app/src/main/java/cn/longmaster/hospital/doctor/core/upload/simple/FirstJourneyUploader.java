package cn.longmaster.hospital.doctor.core.upload.simple;

import android.support.annotation.NonNull;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * 上传图片到服务器地址
 * Created by W·H·K on 2018/5/16.
 * mod by biao on 2019/7/12
 */

public class FirstJourneyUploader extends BaseFileUploader {
    public String filePath;
    public int actType = 1;
    public String ext = "jpg";

    public FirstJourneyUploader(@NonNull OnNginxUploadStateCallback callback) {
        super(callback);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_FIRST_JOURNEY;
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
    }
}
