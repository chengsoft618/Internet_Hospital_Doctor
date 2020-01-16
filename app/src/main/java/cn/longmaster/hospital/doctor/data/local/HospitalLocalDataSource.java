package cn.longmaster.hospital.doctor.data.local;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;
import cn.longmaster.hospital.doctor.data.HospitalDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/7/29 15:04
 * @description:
 */
public class HospitalLocalDataSource implements HospitalDataSource {

    @Override
    public void loadHospitals(int pageIndex, int pageSize, String keyWords, OnResultCallback<List<HospitalInfo>> listener) {

    }

    @Override
    public void getHospital(int hospitalId, OnResultCallback<HospitalInfo> listener) {

    }

    @Override
    public void loadDepartments(int pageIndex, int pageSize, int hospitalId, OnResultCallback<List<DepartmentItem>> listener) {

    }

    @Override
    public void loadDepartments(int hospitalId, String departmentId, OnResultCallback<List<DepartmentItem>> listener) {

    }
}
