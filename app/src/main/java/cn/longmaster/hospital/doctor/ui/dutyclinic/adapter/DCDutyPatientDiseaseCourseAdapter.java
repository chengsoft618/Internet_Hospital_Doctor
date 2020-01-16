package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteTypeInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.TimeUtils;

/**
 * @author:wangyang
 * @date:2019-12-18 20:02
 * @description:
 */
public class DCDutyPatientDiseaseCourseAdapter extends BaseQuickAdapter<DCDutyPatientDiseaseItemInfo, BaseViewHolder> {
    private DCManager dcManager;
    private String mHospitalName;

    public DCDutyPatientDiseaseCourseAdapter(int layoutResId, @Nullable List<DCDutyPatientDiseaseItemInfo> data) {
        super(layoutResId, data);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyPatientDiseaseItemInfo item) {
        View timeLineView = helper.getView(R.id.item_dc_duty_patient_disease_time_line_view);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) timeLineView.getLayoutParams();
        if (helper.getAdapterPosition() == 0) {
            if (LibCollections.size(getData()) >= 2) {
                layoutParams.setMargins(DisplayUtil.dp2px(22), DisplayUtil.dp2px(37), 0, 0);
            } else {
                layoutParams.setMargins(DisplayUtil.dp2px(22), DisplayUtil.dp2px(37), 0, DisplayUtil.dp2px(15));
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_patient_disease_top_content_ll);
            }
            helper.setImageResource(R.id.item_dc_duty_patient_disease_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_top_diagnostic_circular);
        } else if (helper.getAdapterPosition() < LibCollections.size(getData()) - 1) {
            helper.setImageResource(R.id.item_dc_duty_patient_disease_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_bottom_diagnostic_circular);
            layoutParams.setMargins(DisplayUtil.dp2px(22), 0, 0, 0);
        } else {
            layoutParams.setMargins(DisplayUtil.dp2px(22), 0, 0, DisplayUtil.dp2px(15));
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_patient_disease_top_content_ll);
            helper.setImageResource(R.id.item_dc_duty_patient_disease_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_bottom_diagnostic_circular);
        }
        timeLineView.setLayoutParams(layoutParams);
        helper.setText(R.id.item_dc_duty_patient_disease_treat_type_tv, item.getAttrDesc());
        helper.setText(R.id.item_dc_duty_patient_disease_treat_time_tv, TimeUtils.string2String(item.getRecordDt(), "yyyy-MM-dd HH:ss", "yyyy-MM-dd"));
        helper.setGone(R.id.item_dc_duty_patient_disease_diagnosis_ll, false);
        helper.setGone(R.id.item_dc_duty_patient_disease_survey_ll, false);
        helper.setGone(R.id.item_dc_duty_patient_disease_check_ll, false);
        helper.setGone(R.id.item_dc_duty_patient_disease_prescription_ll, false);
        helper.setGone(R.id.item_dc_duty_patient_disease_file_photo_ll, false);
        boolean hasDiagnosis = false;
        String diagnosisContent = "";
        boolean hasSurvey = false;
        String surveyContent = "";
        boolean hasCheck = false;
        String checkContent = "";
        boolean hasPrescription = false;
        String prescriptionContent = "";
        boolean hasDataPic = false;
        String dataPicContent = "";
        boolean hasAddProject = false;
        String addProjectContent = "";
        if (LibCollections.isNotEmpty(item.getPrognoteInfoList())) {
            for (DCDutyPrognoteInfo info : item.getPrognoteInfoList()) {
                if (info.getType() == 1) {
                    hasDiagnosis = true;
                    diagnosisContent = createDescContent(info.getDataTypeList(), 1);
                }
                if (info.getType() == 2) {
                    hasSurvey = true;
                    surveyContent = createDescContent(info.getDataTypeList(), 2);
                }
                if (info.getType() == 3) {
                    hasCheck = true;
                    checkContent = createDescContent(info.getDataTypeList(), 3);
                }
                if (info.getType() == 4) {
                    hasPrescription = true;
                    prescriptionContent = createDescContent(info.getDataTypeList(), 4);
                }
                if (info.getType() == 5) {
                    hasDataPic = true;
                    dataPicContent = createDescContent(info.getDataTypeList(), 5);
                }
                if (info.getType() == 100) {
                    hasAddProject = true;
                    addProjectContent = createDescContent(info.getDataTypeList(), 100);
                }
            }
        }
        if (hasDataPic || hasAddProject) {
            hasDiagnosis = false;
            hasSurvey = false;
            hasCheck = false;
            hasPrescription = false;
        }
        helper.setText(R.id.item_dc_duty_patient_disease_diagnosis_desc_tv, diagnosisContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_diagnosis_ll, hasDiagnosis);
        helper.setText(R.id.item_dc_duty_patient_disease_survey_desc_tv, surveyContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_survey_ll, hasSurvey);
        helper.setText(R.id.item_dc_duty_patient_disease_check_desc_tv, checkContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_check_ll, hasCheck);
        helper.setText(R.id.item_dc_duty_patient_disease_prescription_desc_tv, prescriptionContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_prescription_ll, hasPrescription);
        helper.setText(R.id.item_dc_duty_patient_disease_data_and_pic_tv, dataPicContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_file_photo_ll, hasDataPic);
        helper.setText(R.id.item_dc_duty_patient_disease_data_and_pic_tv, addProjectContent);
        helper.setGone(R.id.item_dc_duty_patient_disease_file_photo_ll, hasAddProject);
    }

    private String createDescContent(List<DCDutyPrognoteTypeInfo> dcDutyPrognoteTypeInfos, int type) {
        if (null == dcDutyPrognoteTypeInfos) {
            return "";
        }
        boolean hasPic = false;
        boolean hasVoice = false;
        for (DCDutyPrognoteTypeInfo info : dcDutyPrognoteTypeInfos) {
            if (type == 5) {
                if (null == info.getDataList()) {
                    return "";
                }
                for (DCDutyPrognoteDataInfo typeInfo : info.getDataList()) {
                    if (null != typeInfo) {
                        if (typeInfo.getDataType() == 1) {
                            return typeInfo.getDataVal();
                        } else {
                            if (typeInfo.getDataType() == 2) {
                                hasVoice = true;
                            }
                            if (typeInfo.getDataType() == 3) {
                                hasPic = true;
                            }
                        }
                    }
                }
            } else if (type == 100) {
                if (null == info.getDataList()) {
                    return "";
                }
                for (DCDutyPrognoteDataInfo typeInfo : info.getDataList()) {
                    if (null != typeInfo) {
                        if (typeInfo.getDataType() == 1) {
                            return typeInfo.getDataVal() + " " + mHospitalName;
                        } else {
                            if (typeInfo.getDataType() == 2) {
                                hasVoice = true;
                            }
                            if (typeInfo.getDataType() == 3) {
                                hasPic = true;
                            }
                        }
                    }
                }
            } else {
                if (null == info.getDataList()) {
                    return "";
                }
                for (DCDutyPrognoteDataInfo dataInfo : info.getDataList()) {
                    if (null != dataInfo) {
                        if (dataInfo.getDataType() == 1) {
                            return dataInfo.getDataVal();
                        } else {
                            if (dataInfo.getDataType() == 2) {
                                hasVoice = true;
                            }
                            if (dataInfo.getDataType() == 3) {
                                hasPic = true;
                            }
                        }
                    }
                }
            }
        }
        if (hasPic && hasVoice) {
            return "(语音)、(图片)";
        } else {
            if (hasVoice) {
                return "(语音)";
            }
            if (hasPic) {
                return "(图片)";
            }
        }
        return "";
    }

    public void setHospitalName(String mHospitalName) {
        this.mHospitalName = mHospitalName;
    }
}

