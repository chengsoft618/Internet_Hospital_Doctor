package cn.longmaster.hospital.doctor.core.manager.consult;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.CaseRemarkInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForRelateInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.AddRemarkRequester;
import cn.longmaster.hospital.doctor.core.requests.consult.CaseRemarkRequester;
import cn.longmaster.hospital.doctor.core.requests.consult.record.DoctorDiagnosisRequester;
import cn.longmaster.hospital.doctor.core.requests.consult.record.RelateRecordRequester;

/**
 * 病历管理
 * Created by Yang² on 2016/8/11.
 */
public class RecordManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    /**
     * 根据预约ID获取关联病历
     *
     * @param appointmentId
     * @param listener
     */
    public void getRelateRecords(int appointmentId, final OnResultListener<List<AppointmentItemForRelateInfo>> listener) {
        RelateRecordRequester relateRecordRequester = new RelateRecordRequester((baseResult, relateRecordInfos) -> listener.onResult(baseResult, relateRecordInfos));
        relateRecordRequester.appointmentId = appointmentId;
        relateRecordRequester.doPost();
    }

    /**
     * 根据预约ID拉取医嘱接口
     *
     * @param appointmentId
     * @param listener
     */
    public void getDoctorDiagnosis(int appointmentId, final OnResultListener<DoctorDiagnosisInfo> listener) {
        DoctorDiagnosisRequester doctorDiagnosisRequester = new DoctorDiagnosisRequester(new OnResultListener<DoctorDiagnosisInfo>() {
            @Override
            public void onResult(BaseResult baseResult, DoctorDiagnosisInfo doctorDiagnosisInfo) {
                listener.onResult(baseResult, doctorDiagnosisInfo);
            }
        });
        doctorDiagnosisRequester.appointmentId = appointmentId;
        doctorDiagnosisRequester.doPost();
    }

    /**
     * 获取病历备注列表
     *
     * @param appointmentId
     * @param listener
     */
    public void getCaseRemarks(int appointmentId, final OnResultListener<List<CaseRemarkInfo>> listener) {
        CaseRemarkRequester caseRemarkRequester = new CaseRemarkRequester(new OnResultListener<List<CaseRemarkInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<CaseRemarkInfo> caseRemarkInfos) {
                listener.onResult(baseResult, caseRemarkInfos);
            }
        });
        caseRemarkRequester.appointmentId = appointmentId;
        caseRemarkRequester.doPost();
    }

    /**
     * 给病历添加备注
     *
     * @param appointmentId
     * @param userType
     * @param remarkDesc
     * @param listener
     */

    public void AddRemark(int appointmentId, int userType, String remarkDesc, final OnResultListener<Void> listener) {
        AddRemarkRequester addRemarkRequester = new AddRemarkRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                listener.onResult(baseResult, aVoid);
            }
        });
        addRemarkRequester.appointmentId = appointmentId;
        addRemarkRequester.userType = userType;
        addRemarkRequester.remarkDesc = remarkDesc;
        addRemarkRequester.doPost();
    }
}
