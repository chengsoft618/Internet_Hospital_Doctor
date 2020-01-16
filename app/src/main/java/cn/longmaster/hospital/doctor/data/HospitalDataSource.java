package cn.longmaster.hospital.doctor.data;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;

/**
 * @author ABiao_Abiao
 * @date 2019/7/29 15:02
 * @description:
 */
public interface HospitalDataSource extends DataSource {
    void loadHospitals(int pageIndex, int pageSize, String keyWords, OnResultCallback<List<HospitalInfo>> listener);

    void getHospital(int hospitalId, OnResultCallback<HospitalInfo> listener);

    void loadDepartments(int pageIndex, int pageSize, int hospitalId, OnResultCallback<List<DepartmentItem>> listener);

    void loadDepartments(int hospitalId, String departmentId, OnResultCallback<List<DepartmentItem>> listener);
}
