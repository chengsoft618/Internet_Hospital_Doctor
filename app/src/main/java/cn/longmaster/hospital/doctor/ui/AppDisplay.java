package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.entity.rounds.BasicMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyPicInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderCureDtInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.ui.account.AccountVerificationActivity;
import cn.longmaster.hospital.doctor.ui.account.MyAccountActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultAddActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.ui.consult.SubmitSuccessfulActivity;
import cn.longmaster.hospital.doctor.ui.consult.UploadPictureActivity;
import cn.longmaster.hospital.doctor.ui.consult.consultant.RepresentFunctionActivity;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;
import cn.longmaster.hospital.doctor.ui.consult.record.SelectRelateRecordActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DepartmentChooseActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorDetailActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorListActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorSearchActivity;
import cn.longmaster.hospital.doctor.ui.doctor.SearchActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDoctorListActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyDoctorListActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyListActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyPatientDetailActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyPatientDiseaseCheckActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyPatientDiseaseUpLoadActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyPatientListActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCDutyProjectDetailActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCInputPatientInfoActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DCRoomActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DcDutyVisitPlanDetailsActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DcDutyVisitPlanDetailsRedactActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.DcDutyVisitPlanListActivity;
import cn.longmaster.hospital.doctor.ui.hospital.HospitalChooseActivity;
import cn.longmaster.hospital.doctor.ui.hospital.HospitalFilterActivity;
import cn.longmaster.hospital.doctor.ui.hospital.HospitalFilterByCityActivity;
import cn.longmaster.hospital.doctor.ui.rounds.FirstJourneyPicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.IssueDoctorOrderActivity;
import cn.longmaster.hospital.doctor.ui.rounds.ReceiveActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsChoiceDoctorActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDataManagerActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsInfoConfirmActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsMouldInfoActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsPatientAddActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsPatientInfoActivity;
import cn.longmaster.hospital.doctor.ui.rounds.SelectionTimeActivity;
import cn.longmaster.hospital.doctor.ui.rounds.WaitRoundsPatientActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.ui.user.MessageCenterActivity;
import cn.longmaster.hospital.doctor.ui.user.MyAssessActivity;
import cn.longmaster.hospital.doctor.ui.user.PDFActivity;
import cn.longmaster.hospital.doctor.ui.user.PasswordChangeActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientDataDetailActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientDataListActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientDataUploadActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientManagerActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientMaterialManagerActivity;
import cn.longmaster.hospital.doctor.ui.user.PersonalMaterialActivity;
import cn.longmaster.hospital.doctor.ui.user.SettingActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/15 14:18
 * @description:
 */
public class AppDisplay implements Display {
    private FragmentActivity activity;
    private Fragment fragment;
    private final int START_ACTIVITY_NO_REQUESTCODE = 0;

    public AppDisplay(@NonNull FragmentActivity activity) {
        this.activity = activity;
        this.fragment = null;
    }

    public AppDisplay(@NonNull Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
    }

    @Override
    public void finish() {
        activity.finish();
    }

    @Override
    public void startDoctorDetailActivity(int doctorId, boolean isRounds, boolean isChooseDoctor, int requestCode) {
        Intent intent = new Intent(activity, DoctorDetailActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, doctorId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_IS_ROUNDS, isRounds);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_CHECK_IS_CHOOSE_DOCTOR, isChooseDoctor);
        if (START_ACTIVITY_NO_REQUESTCODE != requestCode) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startRoundsChoiceDoctorActivity(int requestCode) {
        Intent intent = new Intent(activity, RoundsChoiceDoctorActivity.class);
        startActivity(intent, requestCode);
    }

    @Override
    public void startBrowserActivity(String url, String title, boolean isUseTitle, boolean isMyData, int materialId, int requestCode) {
        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, url);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, title);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, isUseTitle);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MY_DATA, isMyData);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, materialId);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startHospitalFilterByCityActivity(int requestCode) {
        Intent intent = new Intent(activity, HospitalFilterByCityActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startDoctorFilterByDepartmentActivity(int requestCode) {
        Intent intent = new Intent(activity, DepartmentChooseActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startHospitalFilterActivity(String provinceName, String cityName, int requestCode) {
        Intent intent = new Intent(activity, HospitalFilterActivity.class);
        intent.putExtra(HospitalFilterActivity.KEY_TO_QUERY_PROVINCE, provinceName);
        intent.putExtra(HospitalFilterActivity.KEY_TO_QUERY_CITY, cityName);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startRoundsMouldInfoActivity(int doctorId, ArrayList<String> addIntentionList, ArrayList<String> departmentList, ArrayList<Integer> departmentIdList, boolean isDiagnosis, String remarks, int requestCode) {
        Intent intent = new Intent(activity, RoundsMouldInfoActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, doctorId);
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, addIntentionList);
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_LIST, departmentList);
        intent.putIntegerArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_ID_LIST, departmentIdList);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_DIAGNOSIS_CORE, isDiagnosis);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_REMARKS, remarks);
        if (START_ACTIVITY_NO_REQUESTCODE != requestCode) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }

    }

    @Override
    public void startRoundsMouldInfoActivity() {
        Intent intent = new Intent(activity, RoundsMouldInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void startSelectionTimeActivity(int doctorId, boolean isModify, ArrayList<String> addIntentionList, int requestCode) {
        Intent intent = new Intent(activity, SelectionTimeActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, doctorId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MODIFY_TIME, isModify);
        if (isModify) {
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_TIME, addIntentionList);
        }
        startActivity(intent, requestCode);
    }

    @Override
    public void startDoctorListActivity(boolean isShare, PatientBaseInfo patientBaseInfo, int requestCode) {
        Intent intent = new Intent(activity, DoctorListActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, isShare);
        if (isShare && patientBaseInfo != null) {
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO, patientBaseInfo);
        }
        if (requestCode != START_ACTIVITY_NO_REQUESTCODE) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startReceiveActivity(int orderId, int atthosId, ArrayList<OrderCureDtInfo> receiveTimeList) {
        Intent intent = new Intent(activity, ReceiveActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RECEIVE_TIME_LIST, receiveTimeList);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, atthosId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderId);
        startActivity(intent);
    }

    @Override
    public void startPasswordChangeActivity(int pageType) {
        Intent intent = new Intent(activity, PasswordChangeActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAGE_TYPE, pageType);
        startActivity(intent);
    }

    @Override
    public void startPatientManagerActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, PatientManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void startRoundsPatientAddActivity(int medicalId, int orderId, boolean isFromRounds, int requestCode) {
        Intent intent = new Intent(activity, RoundsPatientAddActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_ROUNDS, isFromRounds);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startHospitalChooseActivity(int requestCode) {
        Intent intent = new Intent(activity, HospitalChooseActivity.class);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startRoundsPatientInfoActivity(BasicMedicalInfo basicMedicalInfo, int requestCode) {
        Intent intent = new Intent(activity, RoundsPatientInfoActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO, basicMedicalInfo);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startPatientDataUploadActivity(int requestCode) {
        Intent intent = new Intent(activity, PatientDataUploadActivity.class);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startPatientDataDetailActivity(String medicalId, int requestCode) {
        Intent intent = new Intent(activity, PatientDataDetailActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_ID, medicalId);
        startActivity(intent);
    }

    @Override
    public void startWaitRoundsPatientActivity(int orderId, ArrayList<WaitRoundsPatientInfo> selectedMedicalRecords, int requestCode) {
        Intent intent = new Intent(activity, WaitRoundsPatientActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_LIST, selectedMedicalRecords);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startPicBrowseActivity(BrowserPicEvent browserPicEvent, int requestCode) {
        Intent intent = new Intent(activity, PicBrowseActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO, browserPicEvent);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startRoundsDataManagerActivity(int medicalId, boolean isShowSecondText, boolean isFromClinic, int requestCode) {
        Intent intent = new Intent(activity, RoundsDataManagerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, medicalId);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startIssueDoctorOrderActivity(AppointmentInfo appointmentInfo, int requestCode) {
        Intent intent = new Intent(activity, IssueDoctorOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, appointmentInfo);
        intent.putExtras(bundle);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startPatientDataListActivity(int requestCode) {
        Intent intent = new Intent(activity, PatientDataListActivity.class);
        if (requestCode == START_ACTIVITY_NO_REQUESTCODE) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startSettingActivity() {
        Intent intent = new Intent(activity, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMessageCenterActivity() {
        Intent intent = new Intent(activity, MessageCenterActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMyAssessActivity() {
        Intent intent = new Intent(activity, MyAssessActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMyAccountActivity(int accountId, boolean isFromMyDoctor, int requestCode) {
        Intent intent = new Intent(activity, MyAccountActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, accountId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_MY_DOCTOR, isFromMyDoctor);
        startActivity(intent, requestCode);
    }

    @Override
    public void startAccountVerificationActivity() {
        Intent intent = new Intent(activity, AccountVerificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void startRepresentFunctionActivity() {
        Intent intent = new Intent(activity, RepresentFunctionActivity.class);
        startActivity(intent);
    }

    @Override
    public void startPersonalMaterialActivity(String localPath, int doctorId, int requestCode) {
        Intent intent = new Intent(activity, PersonalMaterialActivity.class);
        if (!StringUtils.isTrimEmpty(localPath)) {
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH, localPath);
        }
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, doctorId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startPatientMaterialManagerActivity(String localPath) {
        Intent intent = new Intent(activity, PatientMaterialManagerActivity.class);
        if (!StringUtils.isTrimEmpty(localPath)) {
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH, localPath);
        }
        startActivity(intent);
    }

    @Override
    public void startUploadPictureActivity(boolean isHomePage, List<String> picturePaths, int appointmentId) {
        Intent intent = new Intent(activity, UploadPictureActivity.class);
        if (LibCollections.isNotEmpty(picturePaths)) {
            ArrayList<String> mPicturePaths = (ArrayList<String>) picturePaths;
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, mPicturePaths);
        }
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_HOME_PAGE, isHomePage);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
        startActivity(intent);
    }

    @Override
    public void startRoundsInfoConfirmActivity(RoundsAppointmentInfo roundsAppointmentInfo, int requestCode) {
        Intent intent = new Intent(activity, RoundsInfoConfirmActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_APPOINTMENT_INFO, roundsAppointmentInfo);
        startActivity(intent, requestCode);
    }

    @Override
    public void startConsultAddActivity(int doctorId, PatientInfo patientInfo, int requestCode) {
        Intent intent = new Intent(activity, ConsultAddActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, doctorId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, patientInfo);
        startActivity(intent, requestCode);
    }

    @Override
    public void startSubmitSuccessfulActivity(FormForConsult submitSuccessInfo, int requestCode) {
        Intent intent = new Intent(activity, SubmitSuccessfulActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SUBMIT_SUCCESS_INFO, submitSuccessInfo);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDoctorSearchActivity(int searchType, int requestCode) {
        Intent intent = new Intent(activity, DoctorSearchActivity.class);
        intent.putExtra(DoctorSearchActivity.KEY_TO_QUERY_ROOT_FILTER_TYPE, searchType);
        startActivity(intent, requestCode);
    }

    @Override
    public void startConsultDataManageActivity(boolean isShowSecondText, boolean isFromClinic, PatientInfo patientInfo, AppointmentInfo appointmentInfo, int appointInfoId, int requestCode) {
        Intent intent = new Intent(activity, ConsultDataManageActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_SECOND_TEXT, isShowSecondText);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_CLINIC, isFromClinic);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, patientInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, appointmentInfo);
        if (null == appointmentInfo) {
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointInfoId);
        }
        startActivity(intent, requestCode);
    }

    @Override
    public void startPatientInformationActivity(int appointmentId, boolean isFromVideoRoom, boolean isRelateRecord, boolean isClinic, boolean isHaveAuthority, int requestCode) {
        Intent intent = new Intent(activity, PatientInformationActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, isFromVideoRoom);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD, isRelateRecord);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_CLINIC, isClinic);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_IS_COURSE, isHaveAuthority);
        startActivity(intent, requestCode);
    }

    @Override
    public void startSearchActivityForResult(int searchType, String hint, boolean isShare, PatientBaseInfo patientBaseInfo, int resultCode) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_TYPE, searchType);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_HINT, hint);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, isShare);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO, patientBaseInfo);
        startActivity(intent, resultCode);
    }

    @Override
    public void startSelectRelateRecordActivity(int appointInfoId, int patientId, ArrayList<Integer> relateRecordInfoMap, int requestCode) {
        Intent intent = new Intent(activity, SelectRelateRecordActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointInfoId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_ID, patientId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RELATED_RECORD_IDS, relateRecordInfoMap);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDoctorListActivity() {
        Intent intent = new Intent(activity, DCDoctorListActivity.class);
        startActivity(intent);
    }

    @Override
    public void startDCInputPatientInfoActivity(int itemId, DCDoctorInfo doctorInfo) {
        Intent intent = new Intent(activity, DCInputPatientInfoActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_INFO, doctorInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ITEM_ID, itemId);
        startActivity(intent);
    }

    @Override
    public void startDCDutyListActivity() {
        Intent intent = new Intent(activity, DCDutyListActivity.class);
        startActivity(intent);
    }

    @Override
    public void startDCRoomActivity(DCDoctorInfo dcDoctorInfo, int orderId, int userType, int requestCode) {
        Intent intent = new Intent(activity, DCRoomActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_INFO, dcDoctorInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, orderId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, userType);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyProjectDetailActivity(int requestCode) {
        Intent intent = new Intent(activity, DCDutyProjectDetailActivity.class);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyDoctorListActivity(int projectId, int requestCode) {
        Intent intent = new Intent(activity, DCDutyDoctorListActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, projectId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyPatientListActivity(int projectId, int requestCode) {
        Intent intent = new Intent(activity, DCDutyPatientListActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, projectId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyPatientDetailActivity(int patientId, int projectId, int requestCode) {
        Intent intent = new Intent(activity, DCDutyPatientDetailActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PATIENT_ID, patientId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, projectId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyPatientDataUpLoadActivity(int medicalId, int diseaseId, int requestCode) {
        Intent intent = new Intent(activity, DCDutyPatientDiseaseUpLoadActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_DISEASE_ID, diseaseId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDCDutyPatientDiseaseCheckActivity(int medicalId, int diseaseId, int requestCode) {
        Intent intent = new Intent(activity, DCDutyPatientDiseaseCheckActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_DISEASE_ID, diseaseId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDcDutyVisitPlanListActivity(int projectId, int medicalId, int requestCode) {
        Intent intent = new Intent(activity, DcDutyVisitPlanListActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, projectId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDcDutyVisitPlanDetailsActivity(int medicalId, int plantId, int requestCode) {
        Intent intent = new Intent(activity, DcDutyVisitPlanDetailsActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_VISIT_PLANT_ID, plantId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startFirstJourneyPicBrowseActivity(List<String> serverPicUrls, List<FirstJourneyPicInfo> mDoctorOrderPicList, boolean mIsDoctorOrder, int position, int appointmentId, int requestCode) {
        Intent intent = new Intent(activity, FirstJourneyPicBrowseActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVER_URL, (Serializable) serverPicUrls);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_NAME, (Serializable) mDoctorOrderPicList);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ORDER, mIsDoctorOrder);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, position);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTINFO_ID, appointmentId);
        startActivity(intent, requestCode);
    }

    @Override
    public void startDcDutyVisitPlanDetailsRedactActivity(int medicalId, String hospitalName, int requestCode) {
        Intent intent = new Intent(activity, DcDutyVisitPlanDetailsRedactActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, medicalId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PATIENT_VISIT_PLAN_REDACT_HOSPITAL_NAME, hospitalName);
        startActivity(intent, requestCode);
    }

    @Override
    public void startPDFActivity(String url, String title, int medicalId, int requestCode) {
        Intent intent = new Intent(activity, PDFActivity.class);
        intent.putExtra("pdf_url", url);
        intent.putExtra("title", title);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, medicalId);
        startActivity(intent, requestCode);
    }

    private void showFragmentFromFrameLayout(@IdRes int containerViewId, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(containerViewId, fragment)
                .commitAllowingStateLoss();
    }

    private void showFragment(@IdRes int containerViewId, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    private void showFragmentByHide(@IdRes int containerViewId, Fragment hide, Fragment show) {
        activity.getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .hide(hide)
                .add(containerViewId, show, "TAG")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    private void startActivity(Intent intent, int requestCode) {
        if (0 == requestCode) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    private void startActivity(Intent intent, Bundle options) {
        if (null != fragment) {
            fragment.startActivity(intent, options);
        } else {
            ActivityCompat.startActivity(activity, intent, options);
        }
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (null != fragment) {
            fragment.startActivityForResult(intent, requestCode, null);
        } else {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
        }
    }

    private void startActivityForResult(Intent intent, int requestCode, String optionCode, Parcelable parcelable) {
        Bundle bundle = new Bundle();
        if (!Objects.equal(null, parcelable)) {
            bundle.putParcelable(optionCode, parcelable);
            intent.putExtras(bundle);
        }
        ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle);
    }

    private void startActivityForResult(@NonNull Fragment fragment, Intent intent, int requestCode, String optionCode, Parcelable parcelable) {
        Bundle bundle = new Bundle();
        if (!Objects.equal(null, parcelable)) {
            bundle.putParcelable(optionCode, parcelable);
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode, bundle);
    }

}
