package cn.longmaster.hospital.doctor;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.core.AppConstant;
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

/**
 * @author ABiao_Abiao
 * @date 2019/6/15 14:17
 * @description:
 */
public interface Display {
    void finish();

    /**
     * @param doctorId       医生ID
     * @param isRounds       是否查房
     * @param isChooseDoctor 是否选择专家
     * @param requestCode    是否startActivityForResult 0: 不是
     */
    void startDoctorDetailActivity(int doctorId, boolean isRounds, boolean isChooseDoctor, int requestCode);

    /**
     * @param requestCode
     */
    void startRoundsChoiceDoctorActivity(int requestCode);

    /**
     * 打开webActivity
     *
     * @param url         加载地址
     * @param title       标题
     * @param isUseTitle  是否显示标题
     * @param isMyData    是否是我的数据
     * @param materialId
     * @param requestCode
     */
    void startBrowserActivity(String url, String title, boolean isUseTitle, boolean isMyData, int materialId, int requestCode);

    /**
     * @param requestCode
     */
    void startHospitalFilterByCityActivity(int requestCode);

    /**
     * @param requestCode
     */
    void startDoctorFilterByDepartmentActivity(int requestCode);

    /**
     * @param provinceName 省名称
     * @param cityName     城市名称
     * @param requestCode  请求code
     */
    void startHospitalFilterActivity(String provinceName, String cityName, int requestCode);

    /**
     * @param doctorId
     * @param addIntentionList
     * @param departmentList
     * @param departmentIdList
     * @param isDiagnosis
     * @param remarks
     * @param requestCode
     */
    void startRoundsMouldInfoActivity(int doctorId, ArrayList<String> addIntentionList, ArrayList<String> departmentList, ArrayList<Integer> departmentIdList, boolean isDiagnosis, String remarks, int requestCode);

    /**
     *
     */
    void startRoundsMouldInfoActivity();

    /**
     * @param doctorId
     * @param isModify
     * @param addIntentionList
     */
    void startSelectionTimeActivity(int doctorId, boolean isModify, ArrayList<String> addIntentionList, int requestCode);


    /**
     * @param isShare         是否分享
     * @param patientBaseInfo 患者信息
     * @param requestCode
     */
    void startDoctorListActivity(boolean isShare, PatientBaseInfo patientBaseInfo, int requestCode);

    /**
     * @param orderId
     * @param atthosId
     * @param receiveTimeList
     */
    void startReceiveActivity(int orderId, int atthosId, ArrayList<OrderCureDtInfo> receiveTimeList);

    /**
     * @param pageType
     */
    void startPasswordChangeActivity(int pageType);

    /**
     * 患者管理
     */
    void startPatientManagerActivity();

    /**
     * 添加患者
     *
     * @param medicalId
     * @param orderId
     * @param isFromRounds 是否从查房预约信息填写触发
     * @param requestCode
     */
    void startRoundsPatientAddActivity(int medicalId, int orderId, boolean isFromRounds, int requestCode);

    /**
     * 添加患者选择医院
     */
    void startHospitalChooseActivity(int requestCode);

    /**
     * 患者详情
     *
     * @param basicMedicalInfo
     * @param requestCode
     */
    void startRoundsPatientInfoActivity(BasicMedicalInfo basicMedicalInfo, int requestCode);

    /**
     * 上传患者资料
     */
    void startPatientDataUploadActivity(int requestCode);

    /**
     * 患者资料详情
     *
     * @param medicalId   病例号
     * @param requestCode
     */
    void startPatientDataDetailActivity(String medicalId, int requestCode);

    /**
     * @param orderId                查房详情ID
     * @param selectedMedicalRecords 已选择列表
     * @param requestCode
     */
    void startWaitRoundsPatientActivity(int orderId, ArrayList<WaitRoundsPatientInfo> selectedMedicalRecords, int requestCode);

    /**
     * 查看图片
     *
     * @param browserPicEvent
     * @param requestCode
     */
    void startPicBrowseActivity(BrowserPicEvent browserPicEvent, int requestCode);

    /**
     * 查房患者材料管理
     *
     * @param medicalId        病例号
     * @param isFromClinic
     * @param isShowSecondText
     * @param requestCode
     */
    void startRoundsDataManagerActivity(int medicalId, boolean isShowSecondText, boolean isFromClinic, int requestCode);

    /**
     * 出具医嘱
     *
     * @param appointmentInfo 医嘱信息
     * @param requestCode
     */
    void startIssueDoctorOrderActivity(AppointmentInfo appointmentInfo, int requestCode);

    /**
     * 患者材料列表
     *
     * @param requestCode
     */
    void startPatientDataListActivity(int requestCode);

    /**
     * 设置
     */
    void startSettingActivity();

    /**
     * 我的消息
     */
    void startMessageCenterActivity();

    /**
     * 我的评估
     */
    void startMyAssessActivity();

    /**
     * @param accountId      用户ID 不传为零
     * @param isFromMyDoctor 是否来之我的医生 不传为false
     * @param requestCode    请求码
     */
    void startMyAccountActivity(int accountId, boolean isFromMyDoctor, int requestCode);

    /**
     * 我账户验证
     */
    void startAccountVerificationActivity();

    /**
     * 销售代表功能
     */
    void startRepresentFunctionActivity();

    /**
     * 上传队列
     *
     * @param localPath   本地地址UploadPictureActivity
     * @param doctorId    医生ID
     * @param requestCode
     */
    void startPersonalMaterialActivity(String localPath, int doctorId, int requestCode);

    /**
     * 材料管理
     *
     * @param localPath 本地地址
     */
    void startPatientMaterialManagerActivity(String localPath);

    /**
     * 上传队列
     *
     * @param isHomePage    是否是首页
     * @param picturePaths  图片地址
     * @param appointmentId
     */
    void startUploadPictureActivity(boolean isHomePage, List<String> picturePaths, int appointmentId);

    /**
     * 查房信息提交确认
     *
     * @param roundsAppointmentInfo
     * @param requestCode
     */
    void startRoundsInfoConfirmActivity(RoundsAppointmentInfo roundsAppointmentInfo, int requestCode);

    /**
     * 会诊新增
     *
     * @param doctorId
     * @param patientInfo
     * @param requestCode
     */
    void startConsultAddActivity(int doctorId, PatientInfo patientInfo, int requestCode);

    /**
     * 会诊提交成功显示界面
     *
     * @param submitSuccessInfo
     * @param requestCode
     */
    void startSubmitSuccessfulActivity(FormForConsult submitSuccessInfo, int requestCode);

    /**
     * 搜索医生
     *
     * @param searchType
     * @param requestCode
     */
    void startDoctorSearchActivity(int searchType, int requestCode);

    /**
     * @param isShowSecondText
     * @param isFromClinic
     * @param patientInfo
     * @param appointmentInfo
     * @param appointInfoId
     */
    void startConsultDataManageActivity(boolean isShowSecondText, boolean isFromClinic, PatientInfo patientInfo, AppointmentInfo appointmentInfo, int appointInfoId, int requestCode);

    /**
     * @param appointmentId   会诊ID
     * @param isFromVideoRoom 是否由视频房间进入
     * @param isRelateRecord  是否关联
     * @param isClinic
     * @param isHaveAuthority 是否有权限
     */
    void startPatientInformationActivity(int appointmentId, boolean isFromVideoRoom, boolean isRelateRecord, boolean isClinic, boolean isHaveAuthority, int requestCode);

    /**
     * @param searchType
     * @param hint
     * @param isShare
     * @param patientBaseInfo
     * @param resultCode
     */
    void startSearchActivityForResult(int searchType, String hint, boolean isShare, PatientBaseInfo patientBaseInfo, int resultCode);

    /**
     * @param appointInfoId       预约ID
     * @param patientId           患者ID
     * @param relateRecordInfoMap 已关联病例ID
     * @param requestCode
     */
    void startSelectRelateRecordActivity(int appointInfoId, int patientId, ArrayList<Integer> relateRecordInfoMap, int requestCode);

    /**
     * 打开值班门诊-发起门诊-选择医生
     */
    void startDCDoctorListActivity();

    /**
     * 打开值班门诊-发起门诊-填写患者信息
     *
     * @param itemId     项目id
     * @param doctorInfo 医生信息
     */
    void startDCInputPatientInfoActivity(int itemId, DCDoctorInfo doctorInfo);

    /**
     * 打开值班门诊-值班列表
     */
    void startDCDutyListActivity();

    /**
     * @param dcDoctorInfo 医生信息
     * @param orderId      订单ID
     * @param userType     用户类型
     * @param requestCode
     */
    void startDCRoomActivity(DCDoctorInfo dcDoctorInfo, int orderId, @AppConstant.RoomUserType int userType, int requestCode);

    /**
     * 打开项目管理
     */
    void startDCDutyProjectDetailActivity(int requestCode);

    /**
     * 打开项目参与医生列表
     *
     * @param projectId   项目ID
     * @param requestCode
     */
    void startDCDutyDoctorListActivity(int projectId, int requestCode);

    /**
     * 打开项目参与患者列表
     *
     * @param projectId   项目ID
     * @param requestCode
     */
    void startDCDutyPatientListActivity(int projectId, int requestCode);

    /**
     * @param patientId   患者ID
     * @param projectId  项目ID
     * @param requestCode
     */
    void startDCDutyPatientDetailActivity(int patientId, int projectId, int requestCode);

    /**
     * 患者病程上传
     *
     * @param medicalId   病例ID
     * @param diseaseId   患者病程ID 不传表示新增
     * @param requestCode
     */
    void startDCDutyPatientDataUpLoadActivity(int medicalId, int diseaseId, int requestCode);

    /**
     * 患者病程(患者/导医)上传
     *
     * @param medicalId   病例ID
     * @param diseaseId   病程ID
     * @param requestCode
     */
    void startDCDutyPatientDiseaseCheckActivity(int medicalId, int diseaseId, int requestCode);
    /**
     * 随访计划列表
     *
     * @param medicalId   病例ID
     * @param projectId   项目ID
     * @param requestCode
     */
    void startDcDutyVisitPlanListActivity(int projectId, int medicalId, int requestCode);

    /**
     * 打开随访计划详情
     *
     * @param plantId     随访计划ID
     * @param medicalId   病例ID
     * @param requestCode
     */
    void startDcDutyVisitPlanDetailsActivity(int medicalId, int plantId, int requestCode);

    /**
     * @param serverPicUrls
     * @param mDoctorOrderPicList
     * @param mIsDoctorOrder
     * @param position
     * @param appointmentId
     * @param requestCode
     */
    void startFirstJourneyPicBrowseActivity(List<String> serverPicUrls, List<FirstJourneyPicInfo> mDoctorOrderPicList, boolean mIsDoctorOrder, int position, int appointmentId, int requestCode);

    /**
     * @param medicalId
     * @param hospitalName
     * @param requestCode
     */
    void startDcDutyVisitPlanDetailsRedactActivity(int medicalId, String hospitalName, int requestCode);

    /**
     * @param url         链接
     * @param title       标题
     * @param medicalId   病例ID
     * @param requestCode
     */
    void startPDFActivity(String url, String title, int medicalId, int requestCode);
}
