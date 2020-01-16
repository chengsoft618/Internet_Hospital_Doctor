package cn.longmaster.hospital.doctor.core.upload.simple;

import android.support.annotation.NonNull;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * 排班就诊支付确认单图片上传
 * Created by Yang² on 2017/12/28.
 */

public class PaymentPictureUploader extends BaseFileUploader {
    public String filePath;
    public int actType = 1;
    public String ext = "jpg";

//    {"op_type":"3026","user_id":"1000","task_id":"12","c_type":"1","sid":"0","c_ver":"0","act_type":"1","ext":"jpg","gender":"0","file_name":"123.jpg"}

    public PaymentPictureUploader(@NonNull OnNginxUploadStateCallback callback) {
        super(callback);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_PAYMENT_PIC;
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
