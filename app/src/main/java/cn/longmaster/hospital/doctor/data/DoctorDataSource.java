package cn.longmaster.hospital.doctor.data;

import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/8/15 14:34
 * @description:
 */
public interface DoctorDataSource extends DataSource {
    void getDoctorBaseInfo(int id, String token, OnResultCallback<DoctorBaseInfo> onResultCallback);

    void saveDoctorBaseInfo(DoctorBaseInfo doctorBaseInfo);
}
