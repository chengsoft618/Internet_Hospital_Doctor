package cn.longmaster.hospital.doctor.core.manager.common;

import android.support.annotation.NonNull;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentItemInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.HospitalRequester;
import cn.longmaster.hospital.doctor.core.requests.config.RequestParams;
import cn.longmaster.hospital.doctor.core.requests.department.DepartmentListNewRequester;
import cn.longmaster.hospital.doctor.core.requests.hospital.GetDepartmentByHospitalRequest;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 21:08
 * @description:
 */
public class HospitalManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    /**
     * 医院信息
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getHospitalInfo(final RequestParams params, @NonNull final OnResultListener listener) {
        HospitalRequester requester = new HospitalRequester(new OnResultListener<HospitalInfo>() {
            @Override
            public void onResult(BaseResult baseResult, HospitalInfo hospitalInfo) {
                listener.onResult(baseResult, hospitalInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && hospitalInfo != null) {
                    //saveData(baseResult.getOpType(), params.getHospitalId(), baseResult.getToken(), JsonHelper.toJSONObject(hospitalInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.hospitalId = params.getHospitalId();
        requester.doPost();
    }

    public void getDepartment(int hospitalId, String departmentId, OnDepartmentListLoadListener listLoadListener) {
        boolean isFistDepartment = StringUtils.isTrimEmpty(departmentId) || StringUtils.equals(departmentId, "0");
        GetDepartmentByHospitalRequest request = new GetDepartmentByHospitalRequest(new DefaultResultCallback<List<DepartmentItem>>() {
            @Override
            public void onSuccess(List<DepartmentItem> departmentItems, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(departmentItems)) {
                    listLoadListener.onSuccess("获取科室成功", isFistDepartment, departmentItems);
                } else {
                    listLoadListener.onFail(isFistDepartment ? "该医院暂无科室" : "该一级科室下无二级科室");
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listLoadListener.onFail(isFistDepartment ? "获取一级科室失败" : "获取二级科室失败");
            }
        });
        request.setHospitalId(hospitalId);
        request.setDepartmentId(departmentId);
        request.start();
    }

    public void getDeparmentList(OnResultCallback<List<DepartmentItemInfo>> callback) {
        DepartmentListNewRequester requester = new DepartmentListNewRequester(callback);
        requester.doPost();
    }

    public interface OnDepartmentListLoadListener {
        void onSuccess(String msg, boolean isFistDepartment, List<DepartmentItem> departmentItems);

        void onFail(String msg);
    }
}
