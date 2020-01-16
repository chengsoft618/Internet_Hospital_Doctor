package cn.longmaster.hospital.doctor.core.manager.rounds;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.config.PatientBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetRoundsAssociatedMedicalRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.ModifyPatientInfoRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.PatientAddRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.PatientDeleteRequester;

/**
 * @author ABiao_Abiao
 * @date 2019/7/15 18:21
 * @description: 患者信息管理类
 */
public class PatientManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    public void savePatient(RoundsMedicalInfo info, OnPatientSaveListener listener) {
        if (info.getMedicalId() == 0) {
            addPatientInfo(info, listener);
        } else {
            modPatientInfo(info, listener);
        }
    }

    public void deletePatient(String medicalId, OnPatientDeleteListener listener) {
        PatientDeleteRequester requester = new PatientDeleteRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("删除患者信息成功");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFail("删除患者信息失败");
            }
        });
        requester.setMedicalId(medicalId);
        requester.start();
    }

    public void getRoundsAssociatedMedical(int medicalId) {
        GetRoundsAssociatedMedicalRequester requester = new GetRoundsAssociatedMedicalRequester((baseResult, roundsAssociatedMedicalInfos) -> {
            Logger.logI(Logger.APPOINTMENT, "RoundsPatientInfoActivity->getRoundsAssociatedMedical:-->roundsAssociatedMedicalInfos:" + roundsAssociatedMedicalInfos);
            if (baseResult.getCode() == 0) {

            }
        });
        requester.medicalId = medicalId;
        requester.doPost();
    }

    /**
     * 新增患者信息
     *
     * @param info
     */
    private void addPatientInfo(RoundsMedicalInfo info, OnPatientSaveListener listener) {
        PatientAddRequester requester = new PatientAddRequester(new DefaultResultCallback<WaitRoundsPatientInfo>() {
            @Override
            public void onSuccess(WaitRoundsPatientInfo info, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "addPatientInfo-->baseResult:" + baseResult);
                if (info != null) {
                    listener.onSuccess("新增患者信息成功", false, info);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == 102) {
                    listener.onFail("该患者已存在", 102);
                } else if (result.getCode() == -100) {
                    listener.onFail("患者联系电话不能填写医生或者代表的电话", result.getCode());
                } else {
                    listener.onFail("提交失败，请重新提交", result.getCode());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listener.onComplete();
            }
        });
        requester.setOrderId(info.getOrderId());
        requester.setAge(info.getPatientNameAge());
        requester.setPatientName(info.getPatientName());
        requester.setGender(info.getPatientNameSex());
        requester.setPatientIllness(info.getPatientIllness());
        requester.setMedicalFileList(info.getMedicalFileList());
        requester.setAttdocId(info.getAttdocId());
        requester.setPhoneNum(info.getPhoneNumber());
        requester.setCardNo(info.getCardNumber());
        requester.setDiagnose(info.getDiagnosis());
        requester.setImportant(info.getImportant());
        requester.setHospitalizaId(info.getInHospitalNum());
        requester.doPost();
    }

    /**
     * 修改患者信息
     *
     * @param info
     */
    private void modPatientInfo(RoundsMedicalInfo info, OnPatientSaveListener listener) {
        ModifyPatientInfoRequester requester = new ModifyPatientInfoRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("修改患者信息成功", true, null);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == 102) {
                    listener.onFail("该患者已存在", 102);
                } else {
                    listener.onFail("提交失败，请稍后重试", result.getCode());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listener.onComplete();
            }
        });
        requester.setMedicalId(info.getMedicalId());
        requester.setAge(info.getPatientNameAge());
        requester.setPatientName(info.getPatientName());
        requester.setGender(info.getPatientNameSex());
        requester.setPatientIllness(info.getPatientIllness());
        requester.setAttdocId(info.getAttdocId());
        requester.setPhoneNum(info.getPhoneNumber());
        requester.setCardNo(info.getCardNumber());
        requester.setDiagnose(info.getDiagnosis());
        requester.setImportant(info.getImportant());
        requester.setMedicalFileList(info.getMedicalFileList());
        requester.setHospitalizaId(info.getInHospitalNum());
        requester.doPost();
    }

    public void getPatientInfo(int appointmentId, OnPatientInfoLoadListener loadListener) {
        PatientBaseRequester requester = new PatientBaseRequester(new DefaultResultCallback<PatientInfo>() {
            @Override
            public void onSuccess(PatientInfo patientInfo, BaseResult baseResult) {
                loadListener.onSuccess(patientInfo);
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
        requester.token = "0";
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    public interface OnPatientSaveListener {
        void onSuccess(String msg, boolean isMod, WaitRoundsPatientInfo info);

        void onFail(String msg, int code);

        void onComplete();
    }

    public interface OnPatientInfoLoadListener {
        void onSuccess(PatientInfo patientInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnPatientDeleteListener {
        void onSuccess(String msg);

        void onFail(String msg);
    }
}
