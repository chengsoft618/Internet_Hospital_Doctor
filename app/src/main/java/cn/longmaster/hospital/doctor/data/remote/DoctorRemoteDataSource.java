package cn.longmaster.hospital.doctor.data.remote;

import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.DoctorBaseRequester;
import cn.longmaster.hospital.doctor.data.DoctorDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/8/15 14:37
 * @description:
 */
public class DoctorRemoteDataSource implements DoctorDataSource {
    @Override
    public void getDoctorBaseInfo(int id, String token, OnResultCallback<DoctorBaseInfo> onResultCallback) {
        DoctorBaseRequester requester = new DoctorBaseRequester((baseResult, doctorBaseInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                onResultCallback.onSuccess(doctorBaseInfo);
            } else {
                onResultCallback.onFail(baseResult.getMsg());
            }
        });
        requester.token = token;
        requester.doctorId = id;
        requester.doPost();
    }

    @Override
    public void saveDoctorBaseInfo(DoctorBaseInfo doctorBaseInfo) {

    }
}
