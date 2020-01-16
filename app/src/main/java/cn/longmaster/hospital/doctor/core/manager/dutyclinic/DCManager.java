package cn.longmaster.hospital.doctor.core.manager.dutyclinic;

import android.util.SparseArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCAdviceEditorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataType;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteType;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteTypeInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientMedical;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientMedicalSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyTreatType;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCInspectInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDoctorListInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCReferralDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCSuggestionDetailInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.CreateDCOrderRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.DeleteMedicalDataRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCDefaultDrugRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCDefaultInspectRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCDutyRoomPatientListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCDutyVisitPlantListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCDutyVisitPlantRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCOrderDetailRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCPatientDiseaseCourseListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCProjectDoctorListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCProjectListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCProjectPatientListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCReferralDetailRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDCSuggestionDetailRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.GetDcMedicalListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SearchDCProjectPatientListRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SendCommonMsgRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SendMsgForIssueAndSuggestRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SetDCAdviceEditStateRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SubmitDCReferralRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SubmitDCSuggestionRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.UpdateMedicalDataRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.UpdateMedicalDataStateRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.UpdateRoomPatientRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.UpdateVisitPlantsRequester;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * 值班门诊管理类
 * Created by yangyong on 2019-12-02.
 */
public class DCManager extends BaseManager {
    //患者详情
    private SparseArray<DCDutyPatientItemInfo> patientInfoDetails;
    private SparseArray<DCDutyRoomPatientItem> roomPatientInfoDetails;
    //患者病程详情
    private SparseArray<DCDutyPatientDiseaseItemInfo> patientDiseaseInfos;
    //患者随访计划详情
    private SparseArray<DCDutyVisitPlantItem> plantTempInfos;

    public List<DCDutyTreatType> getTreatTypeList() {
        List<DCDutyTreatType> dcDutyTreatTypeList = new ArrayList<>(12);
        for (int i = 1; i <= 11; i++) {
            DCDutyTreatType dcDutyTreatType = new DCDutyTreatType();
            dcDutyTreatType.setId(i);
            switch (i) {
                case 1:
                    dcDutyTreatType.setName("首诊");
                    break;
                case 2:
                    dcDutyTreatType.setName("复诊");
                    break;
                case 3:
                    dcDutyTreatType.setName("处方医嘱");
                    break;
                case 4:
                    dcDutyTreatType.setName("病例");
                    break;
                case 5:
                    dcDutyTreatType.setName("影像");
                    break;
                case 6:
                    dcDutyTreatType.setName("化验");
                    break;
                case 7:
                    dcDutyTreatType.setName("体征");
                    break;
                case 8:
                    dcDutyTreatType.setName("入院");
                    break;
                case 9:
                    dcDutyTreatType.setName("出院");
                    break;
                case 10:
                    dcDutyTreatType.setName("手术");
                    break;
                case 11:
                    dcDutyTreatType.setName("随访记录");
                    break;
                default:
                    break;
            }
            dcDutyTreatTypeList.add(dcDutyTreatType);
        }

        return dcDutyTreatTypeList;
    }

    public List<DCDutyPrognoteType> getPrognoteTypeList() {
        List<DCDutyPrognoteType> dcDutyTreatTypeList = new ArrayList<>(6);
        for (int i = 1; i <= 5; i++) {
            DCDutyPrognoteType dcDutyPrognoteType = new DCDutyPrognoteType();
            dcDutyPrognoteType.setId(i);
            switch (i) {
                case 1:
                    dcDutyPrognoteType.setName("疾病诊断");
                    break;
                case 2:
                    dcDutyPrognoteType.setName("病情摘要");
                    break;
                case 3:
                    dcDutyPrognoteType.setName("检查检验");
                    break;
                case 4:
                    dcDutyPrognoteType.setName("用药及处方");
                    break;
                case 5:
                    dcDutyPrognoteType.setName("资料图片");
                    break;
                default:
                    break;
            }
            dcDutyTreatTypeList.add(dcDutyPrognoteType);
        }
        return dcDutyTreatTypeList;
    }

    public List<DCDutyPrognoteDataType> getPrognoteDataTypeList() {
        List<DCDutyPrognoteDataType> dataTypes = new ArrayList<>(4);
        for (int i = 1; i <= 3; i++) {
            DCDutyPrognoteDataType dataType = new DCDutyPrognoteDataType();
            dataType.setId(i);
            switch (i) {
                case 1:
                    dataType.setName("文字");
                    break;
                case 2:
                    dataType.setName("语音");
                    break;
                case 3:
                    dataType.setName("图片");
                    break;
                default:
                    break;
            }
        }
        return dataTypes;
    }

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    public void setDCAdviceEditState(int orderId, int editState, OnResultListener<DCAdviceEditorInfo> callback) {
        SetDCAdviceEditStateRequester requester = new SetDCAdviceEditStateRequester(callback);
        requester.orderId = orderId;
        requester.editState = editState;
        requester.doPost();
    }

    public void createDCRoomOrder(int itemId, int doctorId, String patientName, String phoneNum, String cardNo, OnResultCallback<Integer> callback) {
        CreateDCOrderRequester requester = new CreateDCOrderRequester(callback);
        requester.itemId = itemId;
        requester.doctorId = doctorId;
        requester.patientName = patientName;
        requester.phoneNum = phoneNum;
        requester.cardNo = cardNo;
        requester.start();

    }

    /**
     * 获取值班门诊订单详情
     *
     * @param orderId  订单id
     * @param listener 回调接口
     */
    public void getDCOrderDetail(int orderId, OnResultCallback<DCOrderDetailInfo> listener) {
        GetDCOrderDetailRequester requester = new GetDCOrderDetailRequester(listener);
        requester.orderId = orderId;
        requester.start();
    }

    /**
     * 获取项目医生列表
     *
     * @param itemId   项目ID
     * @param type     0:拉取正在值班医生列表 1：拉取项目所有医生列表
     * @param callback 回调 回调
     */
    public void getDoctorList(int itemId, int type, OnResultCallback<List<DCProjectDoctorListInfo>> callback) {
        GetDCProjectDoctorListRequester requester = new GetDCProjectDoctorListRequester(callback);
        requester.setItemId(itemId);
        requester.setType(type);
        requester.start();
    }

    /**
     * 格式化项目医生列表支持多组合显示
     *
     * @param doctorListInfos 医生泪飙
     * @return 格式化之后的医生列表
     */
    public List<DCDoctorSectionInfo> createDoctorSectionInfoList(List<DCProjectDoctorListInfo> doctorListInfos) {
        if (LibCollections.isEmpty(doctorListInfos)) {
            return new ArrayList<>(0);
        }
        List<DCDoctorSectionInfo> dcDoctorSectionInfos = new ArrayList<>();
        for (DCProjectDoctorListInfo listInfo : doctorListInfos) {
            DCDoctorSectionInfo headerInfo = new DCDoctorSectionInfo(true, listInfo.getRoleName());
            dcDoctorSectionInfos.add(headerInfo);
            for (DCDoctorInfo doctorInfo : listInfo.getDoctorInfos()) {
                dcDoctorSectionInfos.add(new DCDoctorSectionInfo(false, "").initWithDoctorInfo(doctorInfo));
            }
        }
        return dcDoctorSectionInfos;
    }

    /**
     * @param type     0:拉取值班门诊项目 1：拉取慢病管理项目
     * @param callback 回调
     */
    public void getProjectList(int type, OnResultCallback<List<DCProjectInfo>> callback) {
        GetDCProjectListRequester requester = new GetDCProjectListRequester(callback);
        requester.setType(type);
        requester.start();
    }

    /**
     * 获取项目详情
     *
     * @param id       项目ID
     * @param callback 回调 回调
     */
    public void getProjectDetail(int id, OnResultCallback<DCProjectDetailInfo> callback) {

    }

    /**
     * 获取项目所有患者
     *
     * @param projectId 项目ID
     * @param type      患者类型 必传 0：全部患者 1：我的患者
     * @param pageIndex 页数
     * @param pageSize  页大小
     * @param callback  回调
     */
    public void getPatientList(int projectId, int type, int pageIndex, int pageSize, OnResultCallback<List<DCDutyPatientItemInfo>> callback) {
        GetDCProjectPatientListRequester requester = new GetDCProjectPatientListRequester(new OnResultCallback<List<DCDutyPatientItemInfo>>() {
            @Override
            public void onSuccess(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos, BaseResult baseResult) {
                callback.onSuccess(dcDutyPatientItemInfos, baseResult);
                patientInfoDetails = createDCDutyPatientDetailsInfos(dcDutyPatientItemInfos);
            }

            @Override
            public void onFail(BaseResult baseResult) {
                callback.onFail(baseResult);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
        requester.setProjectId(projectId);
        requester.setPageIndex(pageIndex);
        requester.setPageSize(pageSize);
        requester.setType(type);
        requester.start();
    }

    /**
     * 搜索项目患者
     *
     * @param projectId  项目ID
     * @param type       患者类型 必传 0：全部患者 1：我的患者
     * @param searchType 搜索类型 0：按患者姓名 1：按社区医院
     * @param keyWords   搜索条件
     * @param callback   回调
     */
    public void searchPatientList(int projectId, int type, int searchType, String keyWords, OnResultCallback<List<DCDutyPatientItemInfo>> callback) {
        SearchDCProjectPatientListRequester requester = new SearchDCProjectPatientListRequester(new OnResultCallback<List<DCDutyPatientItemInfo>>() {
            @Override
            public void onSuccess(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos, BaseResult baseResult) {
                callback.onSuccess(dcDutyPatientItemInfos, baseResult);
                patientInfoDetails = createDCDutyPatientDetailsInfos(dcDutyPatientItemInfos);
            }

            @Override
            public void onFail(BaseResult baseResult) {
                callback.onFail(baseResult);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
        requester.setProjectId(projectId);
        requester.setPatientType(type);
        requester.setSearchType(searchType);
        requester.setKeyWords(keyWords);
        requester.start();
    }

    private SparseArray<DCDutyPatientItemInfo> createDCDutyPatientDetailsInfos(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos) {
        if (LibCollections.isEmpty(dcDutyPatientItemInfos)) {
            return null;
        }
        SparseArray<DCDutyPatientItemInfo> patientItemInfoDetails = new SparseArray<>();
        for (DCDutyPatientItemInfo info : dcDutyPatientItemInfos) {
            patientItemInfoDetails.put(info.getUserId(), info);
        }
        return patientItemInfoDetails;
    }

    /**
     * 获取患者详细信息
     *
     * @param patientId 患者ID
     * @param callback  回调
     */
    public void getPatientInfo(int patientId, OnResultCallback<DCDutyPatientItemInfo> callback) {
        BaseResult baseResult = new BaseResult();
        if (LibCollections.isEmpty(patientInfoDetails) || null == patientInfoDetails.get(patientId)) {
            baseResult.setCode(-1);
            baseResult.setMsg("没有当前患者，请重试");
            callback.onFail(baseResult);
        } else {
            baseResult.setCode(0);
            baseResult.setMsg("查询成功");
            callback.onSuccess(patientInfoDetails.get(patientId), baseResult);
        }
        callback.onFinish();
    }

    /**
     * 根据病历id更新病历未读状态
     *
     * @param medicalId 病例ID
     * @param callback  回调
     */
    public void updatePatientDataState(int medicalId, OnResultCallback<Void> callback) {
        UpdateMedicalDataStateRequester requester = new UpdateMedicalDataStateRequester(callback);
        requester.setMedicalId(medicalId);
        requester.start();
    }

    /**
     * 根据病历id及病程id更新患者病程（当病程id为0时为新增病程）
     *
     * @param medicalId        病例ID
     * @param recordId         病程记录id
     * @param recordTime       病程记录时间
     * @param recordType       病程记录类型 1：首诊 2：复诊 3：处方医嘱 4：病例 5：影像 6：化验 7：体征  8：入院 9：出院 10：手术 11：随访记录
     * @param prognoteInfoList 病程记录详情
     * @param callback         回调
     */
    public void updateMedicalData(int medicalId, int recordId, String recordTime, int recordType, List<DCDutyPrognoteDataInfo> prognoteInfoList, OnResultCallback<Void> callback) {
        UpdateMedicalDataRequester requester = new UpdateMedicalDataRequester(callback);
        requester.setMedicalId(medicalId);
        requester.setRecordId(recordId);
        requester.setRecordDt(recordTime);
        requester.setRecordType(recordType);
        requester.setPrognoteList(createPrognoteInfos(prognoteInfoList));
        requester.start();
    }

    /**
     * 根据病历id及病程id删除病程
     *
     * @param medicalId 病例ID
     * @param recordId  病程ID
     * @param callback  回调
     */
    public void deleteMedicalData(int medicalId, int recordId, OnResultCallback<Void> callback) {
        DeleteMedicalDataRequester requester = new DeleteMedicalDataRequester(callback);
        requester.setMedicalId(medicalId);
        requester.setRecordId(recordId);
        requester.start();
    }

    /**
     * @param orderId
     * @param callback
     */
    public void getDCRoomMedicalList(int orderId, OnResultCallback<List<DCMedicalInfo>> callback) {
        GetDcMedicalListRequester requester = new GetDcMedicalListRequester(callback);
        requester.orderId = orderId;
        requester.start();
    }

    /**
     * 值班门诊-获取默认处方
     *
     * @param callback
     */
    public void getDCDefaultDrugList(OnResultCallback<List<DCDrugInfo>> callback) {
        GetDCDefaultDrugRequester requester = new GetDCDefaultDrugRequester(callback);
        requester.start();
    }

    /**
     * 值班门诊-订单转诊单详情
     *
     * @param medicalId 病例ID
     * @param callback  回调
     */
    public void getDCReferralDetail(int medicalId, OnResultCallback<DCReferralDetailInfo> callback) {
        GetDCReferralDetailRequester requester = new GetDCReferralDetailRequester(callback);
        requester.medicalId = medicalId;
        requester.start();
    }

    /**
     * 值班门诊-转诊单提交
     *
     * @param orderId
     * @param medicalId
     * @param patientName
     * @param phoneNum
     * @param cardNO
     * @param cureDt
     * @param sendHospitalId
     * @param receiveHospitalId
     * @param referralReason
     * @param dcInspectInfos
     * @param callback
     */
    public void submitDCReferral(int orderId, int medicalId, String patientName, String phoneNum, String cardNO, String cureDt, int sendHospitalId, int receiveHospitalId, String referralReason, List<DCInspectInfo> dcInspectInfos, OnResultCallback<Void> callback) {
        SubmitDCReferralRequester requester = new SubmitDCReferralRequester(callback);
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.patientName = patientName;
        requester.phoneNum = phoneNum;
        requester.cardNo = cardNO;
        requester.cureDt = cureDt;
        requester.referral = sendHospitalId;
        requester.receive = receiveHospitalId;
        requester.referralDesc = referralReason;
        requester.inspect = createInspectInfo(dcInspectInfos);
        requester.start();
    }

    private String createInspectInfo(List<DCInspectInfo> dcInspectInfos) {
        if (null != dcInspectInfos) {
            StringBuilder inspectSb = new StringBuilder();
            for (DCInspectInfo info : dcInspectInfos) {
                if (info.isChecked()) {
                    inspectSb.append(info.getInspect());
                    inspectSb.append(",");
                }
            }
            String inspectStr = inspectSb.toString();
            if (inspectStr.length() > 0) {
                inspectStr = inspectStr.substring(0, inspectStr.length() - 1);
            }
            Logger.logD(Logger.COMMON, "IssueReferralAndSuggestionDialog->submitReferral()->inspectStr:" + inspectStr);
            return inspectStr;
        }
        return null;
    }

    private JSONArray createDCDrugInfo(List<DCDrugInfo> dcDrugInfos) {
        if (null != dcDrugInfos) {
            List<DCDrugInfo> selectedDrugs = new ArrayList<>();
            for (DCDrugInfo drugInfo : dcDrugInfos) {
                if (drugInfo.isChecked()) {
                    selectedDrugs.add(drugInfo);
                }
            }
            if (LibCollections.isNotEmpty(selectedDrugs)) {
                return JsonHelper.toJSONArray(selectedDrugs);
            }
        }
        return null;
    }

    /**
     * 值班门诊-上传诊疗建议
     *
     * @param orderId
     * @param medicalId
     * @param patientName
     * @param phoneNum
     * @param cardNO
     * @param cureDt
     * @param diseaseName
     * @param diseaseDesc
     * @param dcDrugInfos
     * @param dcInspectInfos
     * @param callback
     */
    public void submitDCSuggestion(int orderId, int medicalId, String patientName, String phoneNum, String cardNO, String cureDt, String diseaseName, String diseaseDesc, List<DCDrugInfo> dcDrugInfos, List<DCInspectInfo> dcInspectInfos, OnResultCallback<Void> callback) {
        SubmitDCSuggestionRequester requester = new SubmitDCSuggestionRequester(callback);
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.patientName = patientName;
        requester.phoneNum = phoneNum;
        requester.cardNo = cardNO;
        requester.cureDt = cureDt;
        requester.diseaseName = diseaseName;
        requester.diseaseDesc = diseaseDesc;
        requester.inspect = createInspectInfo(dcInspectInfos);
        requester.presJsonArray = createDCDrugInfo(dcDrugInfos);
        requester.start();
    }


    /**
     * 值班门诊-拉取默认配置检查项
     *
     * @param callback 回调
     */
    public void getDCDefaultInspecList(OnResultCallback<List<DCInspectInfo>> callback) {
        GetDCDefaultInspectRequester requester = new GetDCDefaultInspectRequester(callback);
        requester.start();
    }

    /**
     * 值班门诊-拉取订单诊疗建议详情
     *
     * @param medicalId 病例ID
     * @param callback  回调
     */
    public void getDCSuggestionDetail(int medicalId, OnResultCallback<DCSuggestionDetailInfo> callback) {
        GetDCSuggestionDetailRequester requester = new GetDCSuggestionDetailRequester(callback);
        requester.medicalId = medicalId;
        requester.start();
    }

    /**
     * 根据订单id拉取值班门诊患者列表
     *
     * @param orderId  订单ID
     * @param callback 回调
     */
    public void getRoomPatientList(int orderId, OnResultCallback<List<DCDutyRoomPatientItem>> callback) {
        GetDCDutyRoomPatientListRequester requester = new GetDCDutyRoomPatientListRequester(new DefaultResultCallback<List<DCDutyRoomPatientItem>>() {
            @Override
            public void onSuccess(List<DCDutyRoomPatientItem> dcDutyRoomPatientItems, BaseResult baseResult) {
                roomPatientInfoDetails = createDCDutyRoomPatientDetailsInfos(dcDutyRoomPatientItems);
                callback.onSuccess(dcDutyRoomPatientItems, baseResult);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                callback.onFail(result);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
        requester.setOrderId(orderId);
        requester.setPageIndex(0);
        requester.setPageSize(0);
        requester.start();
    }

    private SparseArray<DCDutyRoomPatientItem> createDCDutyRoomPatientDetailsInfos(List<DCDutyRoomPatientItem> dcDutyRoomPatientItems) {
        if (LibCollections.isNotEmpty(dcDutyRoomPatientItems)) {
            SparseArray<DCDutyRoomPatientItem> patientItemInfoDetails = new SparseArray<>();
            for (DCDutyRoomPatientItem info : dcDutyRoomPatientItems) {
                patientItemInfoDetails.put(info.getUserId(), info);
            }
            return patientItemInfoDetails;
        }
        return null;
    }

    /**
     * 根据ID获取患者详情
     *
     * @param patientId 患者ID
     * @param callback  回调
     */
    public void getRoomPatientInfo(int patientId, OnResultCallback<DCDutyRoomPatientItem> callback) {
        BaseResult baseResult = new BaseResult();
        if (LibCollections.isEmpty(roomPatientInfoDetails) || null == roomPatientInfoDetails.get(patientId)) {
            baseResult.setCode(-1);
            baseResult.setMsg("查询不到当前患者,请重试");
            callback.onFail(baseResult);
        } else {
            baseResult.setCode(0);
            baseResult.setMsg("查询成功");
            callback.onSuccess(roomPatientInfoDetails.get(patientId), baseResult);
        }
        callback.onFinish();
    }

    /**
     * 更新值班门诊患者信息
     *
     * @param orderId   订单ID
     * @param medicalId 材料ID
     * @param name      患者名字
     * @param phoneNum  患者电话
     * @param cardNO    患者身份证ID
     * @param cureDate  就诊时间
     * @param fileList  文件列表
     * @param callback  回调
     */
    public void updateRoomPatient(int orderId, int medicalId, String name, String phoneNum, String cardNO, String cureDate, List<DCDutyRoomPatientMedical> fileList, OnResultCallback<String> callback) {
        UpdateRoomPatientRequester requester = new UpdateRoomPatientRequester(callback);
        requester.setOrderId(orderId);
        requester.setMedicalId(medicalId);
        requester.setPatientName(name);
        requester.setPhoneNum(phoneNum);
        requester.setCardNo(cardNO);
        requester.setCureDt(cureDate);
        requester.setFileList(createRoomPatientFiles(fileList));
        requester.start();
    }

    private JSONArray createRoomPatientFiles(List<DCDutyRoomPatientMedical> fileList) {
        if (null != fileList) {
            JSONArray result = new JSONArray();
            for (DCDutyRoomPatientMedical medical : fileList) {
                result.put(JsonHelper.toJSONObject(medical));
            }
            return result;
        }
        return null;
    }

    /**
     * 发送转账单/诊疗建议确认短信
     *
     * @param medicalId 病例ID
     * @param type      0：转诊单 1：诊疗建议
     * @param callback
     */
    public void sendMsgForIssueAndSuggest(int medicalId, int type, OnResultCallback<Void> callback) {
        SendMsgForIssueAndSuggestRequester requester = new SendMsgForIssueAndSuggestRequester(callback);
        requester.setMedicalId(medicalId);
        requester.setType(type);
        requester.start();
    }

    /**
     * 通用短信发送接口
     *
     * @param userId   接收人用户ID
     * @param msg      短信内容
     * @param callback 回调
     */
    public void sendCommendMsg(int userId, String msg, OnResultCallback<Void> callback) {
        SendCommonMsgRequester requester = new SendCommonMsgRequester(callback);
        requester.setReceiverId(userId);
        requester.setContent(msg);
        requester.start();
    }

    /**
     * 根据项目ID获取患者随访计划
     *
     * @param projectId 项目ID
     * @param callback  回调
     */
    public void getVisitPlantListByProject(int projectId, OnResultCallback<List<DCDutyVisitPlantItem>> callback) {
        GetDCDutyVisitPlantListRequester requester = new GetDCDutyVisitPlantListRequester(new OnResultCallback<List<DCDutyVisitPlantItem>>() {
            @Override
            public void onSuccess(List<DCDutyVisitPlantItem> dcDutyVisitPlantItems, BaseResult baseResult) {
                callback.onSuccess(dcDutyVisitPlantItems, baseResult);
                plantTempInfos = createDCDutyPatientVisitPlanDetailsInfos(dcDutyVisitPlantItems);
            }

            @Override
            public void onFail(BaseResult baseResult) {
                callback.onFail(baseResult);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
        requester.setProjectId(projectId);
        requester.start();
    }

    private SparseArray<DCDutyVisitPlantItem> createDCDutyPatientVisitPlanDetailsInfos(List<DCDutyVisitPlantItem> dcDutyVisitPlantItems) {
        if (LibCollections.isEmpty(dcDutyVisitPlantItems)) {
            return null;
        }
        SparseArray<DCDutyVisitPlantItem> dcDutyVisitPlanTempItemInfoDetails = new SparseArray<>();
        for (DCDutyVisitPlantItem info : dcDutyVisitPlantItems) {
            dcDutyVisitPlanTempItemInfoDetails.put(info.getTempId(), info);
        }
        return dcDutyVisitPlanTempItemInfoDetails;
    }

    /**
     * 根据病例ID获取患者随访计划
     *
     * @param medicalId 病历ID
     * @param callback  回调
     */
    public void getVisitPlantListByMedical(int medicalId, OnResultCallback<List<DCDutyVisitPlantTempItem>> callback) {
        GetDCDutyVisitPlantRequester requester = new GetDCDutyVisitPlantRequester(callback);
        requester.setMedicalId(medicalId);
        requester.start();
    }

    /**
     * 获取随访计划详情
     *
     * @param tempId   随访计划ID
     * @param callback 回调
     */
    public void getVisitPlantInfo(int tempId, OnResultCallback<DCDutyVisitPlantItem> callback) {
        BaseResult baseResult = new BaseResult();
        if (LibCollections.isEmpty(plantTempInfos) || null == plantTempInfos.get(tempId)) {
            baseResult.setCode(-1);
            baseResult.setMsg("没有当前计划模版，请重试");
            callback.onFail(baseResult);
        } else {
            baseResult.setCode(0);
            baseResult.setMsg("查询成功");
            callback.onSuccess(plantTempInfos.get(tempId), baseResult);
        }
        callback.onFinish();
    }

    /**
     * 根据病历id更新随访计划
     *
     * @param medicalId                 病历ID
     * @param tempId                    随访计划ID
     * @param dcDutyVisitPlantTempItems
     */
    public void updateVisitPlants(int medicalId, int tempId, String delListIds,List<DCDutyVisitPlantTempItem> dcDutyVisitPlantTempItems, OnResultCallback<Void> callback) {
        UpdateVisitPlantsRequester requester = new UpdateVisitPlantsRequester(callback);
        requester.setMedicalId(medicalId);
        requester.setTempId(tempId);
        requester.setDelListIds(delListIds);
        requester.setFollowupTempList(JsonHelper.toJSONArray(dcDutyVisitPlantTempItems));
        requester.start();
    }

    /**
     * 获取患者病程列表
     *
     * @param medicalId 病例ID
     * @param callback  回调
     */
    public void getDiseaseListInfo(int medicalId, OnResultCallback<List<DCDutyPatientDiseaseItemInfo>> callback) {
        GetDCPatientDiseaseCourseListRequester requester = new GetDCPatientDiseaseCourseListRequester(new OnResultCallback<List<DCDutyPatientDiseaseItemInfo>>() {
            @Override
            public void onSuccess(List<DCDutyPatientDiseaseItemInfo> dcDutyPatientDiseaseItemInfos, BaseResult baseResult) {
                callback.onSuccess(dcDutyPatientDiseaseItemInfos, baseResult);
                patientDiseaseInfos = createDCDutyPatientDiseaseDetailsInfos(dcDutyPatientDiseaseItemInfos);
            }

            @Override
            public void onFail(BaseResult baseResult) {
                callback.onFail(baseResult);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
        requester.setMedicalId(medicalId);
        requester.start();
    }


    /**
     * 获取患者病程详情
     *
     * @param diseaseId 患者病程ID
     * @param callback  回调
     */
    public void getDiseaseInfo(int diseaseId, OnResultCallback<DCDutyPatientDiseaseItemInfo> callback) {
        BaseResult baseResult = new BaseResult();
        if (LibCollections.isEmpty(patientDiseaseInfos) || null == patientDiseaseInfos.get(diseaseId)) {
            baseResult.setCode(-1);
            baseResult.setMsg("没有当前病程，请重试");
            callback.onFail(baseResult);
        } else {
            baseResult.setCode(0);
            baseResult.setMsg("查询成功");
            callback.onSuccess(patientDiseaseInfos.get(diseaseId), baseResult);
        }
        callback.onFinish();
    }

    private SparseArray<DCDutyPatientDiseaseItemInfo> createDCDutyPatientDiseaseDetailsInfos(List<DCDutyPatientDiseaseItemInfo> dcDutyPatientDiseaseItemInfos) {
        if (LibCollections.isEmpty(dcDutyPatientDiseaseItemInfos)) {
            return null;
        }
        SparseArray<DCDutyPatientDiseaseItemInfo> patientItemInfoDetails = new SparseArray<>();
        for (DCDutyPatientDiseaseItemInfo info : dcDutyPatientDiseaseItemInfos) {
            patientItemInfoDetails.put(info.getRecordId(), info);
        }
        return patientItemInfoDetails;
    }

    /**
     * 格式化
     *
     * @param prognoteInfos
     * @return
     */
    private JSONArray createPrognoteInfos(List<DCDutyPrognoteDataInfo> prognoteInfos) {
        if (null == prognoteInfos) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (DCDutyPrognoteDataInfo dataInfo : prognoteInfos) {
            jsonArray.put(JsonHelper.toJSONObject(dataInfo));
        }
        return jsonArray;
    }

    private JSONArray createVisitPlants(List<DCDutyVisitPlantTempItem> dcDutyVisitPlantTempItems) {
        if (null == dcDutyVisitPlantTempItems) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (DCDutyVisitPlantTempItem item : dcDutyVisitPlantTempItems) {
            jsonArray.put(JsonHelper.toJSONObject(item).toString());
        }

        return jsonArray;
    }

    /**
     * @param typeId
     * @return
     */
    public String getTreatType(int typeId) {
        for (DCDutyTreatType type : getTreatTypeList()) {
            if (typeId == type.getId()) {
                return type.getName();
            }
        }
        return "";
    }

    /**
     * @param typeId
     * @return
     */
    public String getPrognoteType(int typeId) {
        for (DCDutyPrognoteType type : getPrognoteTypeList()) {
            if (typeId == type.getId()) {
                return type.getName();
            }
        }
        return "";
    }

    public String getPrognoteDataType(int typeId) {
        for (DCDutyPrognoteDataType type : getPrognoteDataTypeList()) {
            if (typeId == type.getId()) {
                return type.getName();
            }
        }
        return "";
    }

    /**
     * @param diseaseItemInfo
     * @param type            获取类型 1疾病诊断 2病情摘要 3检查检验 4用药及处方 5资料图片
     * @return
     */
    public String getPrognoteContent(DCDutyPatientDiseaseItemInfo diseaseItemInfo, int type) {
        if (null != diseaseItemInfo.getPrognoteInfoList()) {
            for (DCDutyPrognoteInfo info : diseaseItemInfo.getPrognoteInfoList()) {
                if (type == 1 || type == 2 || type == 3 || type == 4) {
                    if (null != info.getDataTypeList()) {
                        if (type == info.getType()) {
                            for (DCDutyPrognoteTypeInfo typeInfo : info.getDataTypeList()) {
                                if (null != typeInfo) {
                                    if (typeInfo.getDataType() == 1) {
                                        if (null != typeInfo.getDataList()) {
                                            for (DCDutyPrognoteDataInfo dataInfo : typeInfo.getDataList()) {
                                                return dataInfo == null ? null : dataInfo.getDataVal();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (type == 5) {

                } else if (type == 6) {

                }
            }
        }
        return null;
    }

    /**
     * @param diseaseItemInfo
     * @param type            获取类型 1疾病诊断 2病情摘要 3检查检验 4用药及处方 5资料图片
     * @return
     */
    public List<DCDutyPrognoteDataInfo> getPrognotePics(DCDutyPatientDiseaseItemInfo diseaseItemInfo, int type) {
        if (null != diseaseItemInfo.getPrognoteInfoList()) {
            for (DCDutyPrognoteInfo info : diseaseItemInfo.getPrognoteInfoList()) {
                if (type == 1 || type == 2 || type == 3 || type == 4) {
                    if (null != info.getDataTypeList()) {
                        if (type == info.getType()) {
                            for (DCDutyPrognoteTypeInfo typeInfo : info.getDataTypeList()) {
                                if (null != typeInfo) {
                                    if (typeInfo.getDataType() == 3) {
                                        return typeInfo.getDataList();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param diseaseItemInfo
     * @param type            获取类型 1疾病诊断 2病情摘要 3检查检验 4用药及处方 5资料图片
     * @return
     */
    public List<DCDutyPrognoteDataInfo> getPrognoteVoices(DCDutyPatientDiseaseItemInfo diseaseItemInfo, int type) {
        if (null != diseaseItemInfo.getPrognoteInfoList()) {
            for (DCDutyPrognoteInfo info : diseaseItemInfo.getPrognoteInfoList()) {
                if (type == 1 || type == 2 || type == 3 || type == 4) {
                    if (null != info.getDataTypeList()) {
                        if (type == info.getType()) {
                            for (DCDutyPrognoteTypeInfo typeInfo : info.getDataTypeList()) {
                                if (null != typeInfo) {
                                    if (typeInfo.getDataType() == 2) {
                                        return typeInfo.getDataList();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<DCDutyRoomPatientMedicalSectionInfo> createRoomPatientPicData(List<DCDutyRoomPatientMedical> medicals) {
        if (null != medicals) {
            Collections.sort(medicals, new Comparator<DCDutyRoomPatientMedical>() {
                @Override
                public int compare(DCDutyRoomPatientMedical o1, DCDutyRoomPatientMedical o2) {
                    return (int) (TimeUtils.string2Millis(o1.getInsertDt()) - TimeUtils.string2Millis(o2.getInsertDt()));
                }
            });
            List<DCDutyRoomPatientMedicalSectionInfo> sectionInfoList = new ArrayList<>(LibCollections.size(medicals));
            String currentDate = "";
            for (DCDutyRoomPatientMedical medical : medicals) {
                DCDutyRoomPatientMedicalSectionInfo sectionInfo = new DCDutyRoomPatientMedicalSectionInfo(!StringUtils.equals(currentDate, medical.getInsertDt()), medical.getInsertDt());
                currentDate = medical.getInsertDt();
                sectionInfoList.add(sectionInfo);
            }
            return sectionInfoList;
        }
        return null;
    }
}
