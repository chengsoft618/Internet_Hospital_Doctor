package cn.longmaster.hospital.doctor.core.manager.user;

import android.util.SparseArray;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.DepartmentRequester;
import cn.longmaster.hospital.doctor.core.requests.config.DoctorBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.config.HospitalRequester;
import cn.longmaster.hospital.doctor.core.requests.doctor.AssistantDoctorRequester;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetNoEnteringDoctorDetailRequester;

/**
 * 医生信息管理类
 * Created by Tengshuxiang on 2016-08-25.
 */
public class DoctorManager extends BaseManager {
    private SparseArray<DoctorBaseInfo> cacheForDoctor = new SparseArray<>(100);

    @Override
    public void onManagerCreate(AppApplication application) {
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    /**
     * 获取医生详细信息
     *
     * @param doctorId 医生ID
     * @param listener 回调
     */
    public void getDoctorInfo(int doctorId, OnDoctorInfoLoadListener listener) {
        if (cacheForDoctor.get(doctorId) != null) {
            listener.onSuccess(cacheForDoctor.get(doctorId));
            listener.onFinish();
        }else {
            if (doctorId > 0) {
                DoctorBaseRequester requester = new DoctorBaseRequester((baseResult, doctorBaseInfo) -> {
                    if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && doctorBaseInfo != null) {
                        cacheForDoctor.put(doctorId, doctorBaseInfo);
                        listener.onSuccess(doctorBaseInfo);
                    } else {
                        listener.onFailed(baseResult.getCode(), baseResult.getMsg());
                    }
                    listener.onFinish();
                });
                requester.token = "0";
                requester.doctorId = doctorId;
                requester.doPost();
            } else if (doctorId < 0) {
                GetNoEnteringDoctorDetailRequester requester = new GetNoEnteringDoctorDetailRequester(new DefaultResultCallback<DoctorBaseInfo>() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo, BaseResult baseResult) {
                        if (null != doctorBaseInfo) {
                            cacheForDoctor.put(doctorId, doctorBaseInfo);
                            listener.onSuccess(doctorBaseInfo);
                        }
                    }

                    @Override
                    public void onFail(BaseResult result) {
                        super.onFail(result);
                        listener.onFailed(result.getCode(), result.getMsg());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        listener.onFinish();
                    }
                });
                requester.setDoctorId(doctorId);
                requester.start();
            }
        }
    }

    /**
     * @param doctorId
     * @param forceUpdate
     * @param listener
     */
    public void getDoctorInfo(final int doctorId, final boolean forceUpdate, final OnDoctorInfoLoadListener listener) {
        getDoctorInfo(doctorId, listener);
    }

    /**
     * 获取医生信息
     *
     * @param id                  医生id
     * @param onGetDoctorListener 回调{@link OnDoctorInfoLoadListener}
     */
    public void getDoctor(final int id, final OnDoctorInfoLoadListener onGetDoctorListener) {
        getDoctorInfo(id, onGetDoctorListener);
    }

    /**
     * 获取医生助理信息
     *
     * @param assistId
     * @param loadListener
     */
    public void getAssistantDoctorInfo(int assistId, OnAssistantDoctorLoadListener loadListener) {
        AssistantDoctorRequester requester = new AssistantDoctorRequester(new DefaultResultCallback<AssistantDoctorInfo>() {
            @Override
            public void onSuccess(AssistantDoctorInfo assistantDoctorInfo, BaseResult baseResult) {
                loadListener.onSuccess(assistantDoctorInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                loadListener.onFailed(result.getCode(), result.getMsg());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadListener.onFinish();
            }
        });
        requester.assistantId = assistId;
        requester.doPost();
    }

    /**
     * 刷新医生缓存信息
     *
     * @param doctorId 医生ID
     */
    public void refreshDoctorInfo(int doctorId) {
        cacheForDoctor.remove(doctorId);
    }

    /**
     * @param hospitalId
     * @param forceUpdate
     * @param listener
     */
    public void getHospitalInfo(final int hospitalId, final boolean forceUpdate, final OnHospitalInfoLoadListener listener) {
        HospitalRequester requester = new HospitalRequester((baseResult, hospitalInfo) -> {
            listener.onFinish();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                listener.onSuccess(hospitalInfo);
            } else {
                listener.onFailed(baseResult.getCode(), baseResult.getMsg());
            }
        });
        requester.token = "0";
        requester.hospitalId = hospitalId;
        requester.doPost();
    }

    /**
     * @param departmentId
     * @param forceUpdate
     * @param listener
     */
    public void getDepartmentInfo(final int departmentId, final boolean forceUpdate, final OnDepartmentInfoLoadListener listener) {
        DepartmentRequester requester = new DepartmentRequester((baseResult, departmentInfo) -> {
            listener.onFinish();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && departmentInfo != null) {
                listener.onSuccess(departmentInfo);
            } else {
                listener.onFailed(baseResult.getCode(), baseResult.getMsg());
            }
        });
        requester.token = "0";
        requester.departmentId = departmentId;
        requester.doPost();
    }

    public interface OnDoctorInfoLoadListener {
        void onSuccess(DoctorBaseInfo doctorBaseInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnAssistantDoctorLoadListener {
        void onSuccess(AssistantDoctorInfo assistantDoctorInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnHospitalInfoLoadListener {
        void onSuccess(HospitalInfo hospitalInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnDepartmentInfoLoadListener {
        void onSuccess(DepartmentInfo departmentInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }
}
