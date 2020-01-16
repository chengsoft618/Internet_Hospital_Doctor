package cn.longmaster.hospital.doctor.core.manager.user;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.DischargedSummaryInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataInfo;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataItem;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.user.DischargedSummaryGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PatientDataDetailGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PatientDataListGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PatientHospitalListGetRequest;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/16 13:47
 * @description: 患者材料
 */
public class PatientDataManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    /**
     * 100561
     */
    public void getPatientDataList(int pageIndex, int pageSize, String hospital, OnResultCallback<List<PatientDataItem>> listOnResultCallback) {
        PatientDataListGetRequest request = new PatientDataListGetRequest(listOnResultCallback);
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setKeyWords(hospital);
        request.start();
    }

    /**
     * 100562
     */
    public void getPatientDataById(String id, OnPatientDataLoadListener onPatientDataLoadListener) {
        PatientDataDetailGetRequest request = new PatientDataDetailGetRequest(new DefaultResultCallback<PatientDataInfo>() {
            @Override
            public void onSuccess(PatientDataInfo patientDataInfo, BaseResult baseResult) {
                onPatientDataLoadListener.onSuccess(patientDataInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                onPatientDataLoadListener.onFail(result.getMsg());
            }
        });
        request.setMedicalId(id);
        request.start();
    }

    /**
     * 100565
     */
    public void deletePlatform(String id, OnPatientDataChangeListener listener) {

    }

    public void savePlatform(PatientDataInfo info, OnPatientDataChangeListener listener) {
        if (StringUtils.isTrimEmpty(info.getMedicalId())) {
            createNewPlatform(info, listener);
        } else {
            updatePlatform(info, listener);
        }
    }

    /**
     * 100575
     * 查看当前病历下的出院小结
     *
     * @param id
     */
    public void getPatientSummaryDataById(String id, OnResultCallback<List<DischargedSummaryInfo>> onResultCallback) {
        DischargedSummaryGetRequest request = new DischargedSummaryGetRequest(onResultCallback);
        request.setMedicalId(id);
        request.start();
    }

    /**
     * 根据患者姓名和住院号获取医院
     *
     * @param name
     * @param inhospitalNum
     * @param onResultCallback
     */
    public void getPatientHospitalByName(String name, String inhospitalNum, OnResultCallback<List<HospitalInfo>> onResultCallback) {
        PatientHospitalListGetRequest request = new PatientHospitalListGetRequest(onResultCallback);
        request.setHospitalizaId(inhospitalNum);
        request.setPatientName(name);
        request.start();
    }

    private void updatePlatform(PatientDataInfo info, OnPatientDataChangeListener listener) {

    }

    private void createNewPlatform(PatientDataInfo info, OnPatientDataChangeListener listener) {

    }

    public interface OnPatientDataListLoadListener {
        void onSuccess(List<PatientDataItem> patientDataItems, boolean isFinish);

        void onFail(String msg);
    }

    public interface OnPatientDataLoadListener {
        void onSuccess(PatientDataInfo patientDataInfo);

        void onFail(String msg);
    }

    public interface OnPatientDataChangeListener {
        void onSuccess(String msg);

        void onFail(String msg);
    }
}
