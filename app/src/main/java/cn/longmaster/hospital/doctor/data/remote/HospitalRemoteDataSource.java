package cn.longmaster.hospital.doctor.data.remote;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.HospitalRequester;
import cn.longmaster.hospital.doctor.core.requests.hospital.GetDepartmentByHospitalRequest;
import cn.longmaster.hospital.doctor.data.HospitalDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/7/29 15:04
 * @description:
 */
public class HospitalRemoteDataSource implements HospitalDataSource {

    @Override
    public void loadHospitals(int pageIndex, int pageSize, String keyWords, OnResultCallback<List<HospitalInfo>> listener) {

    }

    @Override
    public void getHospital(int hospitalId, OnResultCallback<HospitalInfo> listener) {
        HospitalRequester requester = new HospitalRequester(new OnResultListener<HospitalInfo>() {
            @Override
            public void onResult(BaseResult baseResult, HospitalInfo hospitalInfo) {
                //listener.onResult(baseResult, hospitalInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && hospitalInfo != null) {
                    //saveData(baseResult.getOpType(), params.getHospitalId(), baseResult.getToken(), JsonHelper.toJSONObject(hospitalInfo).toString());
                }
            }
        });
        requester.hospitalId = hospitalId;
        requester.doPost();
    }

    @Override
    public void loadDepartments(int pageIndex, int pageSize, int hospitalId, OnResultCallback<List<DepartmentItem>> listener) {

    }

    @Override
    public void loadDepartments(int hospitalId, String departmentId, OnResultCallback<List<DepartmentItem>> listener) {
        GetDepartmentByHospitalRequest request = new GetDepartmentByHospitalRequest(new DefaultResultCallback<List<DepartmentItem>>() {
            @Override
            public void onSuccess(List<DepartmentItem> departmentItems, BaseResult baseResult) {

            }
        });
        request.setHospitalId(hospitalId);
        request.setDepartmentId(departmentId);
        request.start();
    }
}
