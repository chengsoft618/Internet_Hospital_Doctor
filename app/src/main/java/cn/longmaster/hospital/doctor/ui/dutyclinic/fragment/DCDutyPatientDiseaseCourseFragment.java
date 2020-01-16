package cn.longmaster.hospital.doctor.ui.dutyclinic.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyPatientDiseaseCourseAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 20:18
 * @description: 患者病程
 */
public class DCDutyPatientDiseaseCourseFragment extends NewBaseFragment {
    private final int REQUEST_CODE_FOR_DATA_UP_LOAD = 200;
    @FindViewById(R.id.fg_dc_duty_patient_disease_course_rv)
    private RecyclerView fgDcDutyPatientDiseaseCourseRv;
    @FindViewById(R.id.fg_dcduty_patient_disease_up_load_iv)
    private ImageView fgDcdutyPatientDiseaseUpLoadIv;
    /**
     * 患者病历ID
     */
    private int medicalId;
    /**
     * 患者所属社区卫生医院
     */
    private String hospitalName;
    @AppApplication.Manager
    private DCManager mDCManager;
    private static final String KEY_TO_QUERY_DOCTOR_INFO = "_KEY_TO_QUERY_DOCTOR_INFO_";
    private DCDutyPatientDiseaseCourseAdapter dcDutyPatientDiseaseCourseAdapter;

    @Override
    protected void initDatas() {
        super.initDatas();
        dcDutyPatientDiseaseCourseAdapter = new DCDutyPatientDiseaseCourseAdapter(R.layout.item_dc_duty_patient_disease_layout, new ArrayList<>(0));
        dcDutyPatientDiseaseCourseAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPatientDiseaseItemInfo info = (DCDutyPatientDiseaseItemInfo) adapter.getItem(position);
            if (null != info) {
                if (info.getOpType() == 5 || info.getOpType() == 6) {
                    getDisplay().startDCDutyPatientDiseaseCheckActivity(medicalId, info.getRecordId(), REQUEST_CODE_FOR_DATA_UP_LOAD);
                } else if (info.getOpType() == 3) {//操作人 -- 医生
                    getDisplay().startDCDutyPatientDataUpLoadActivity(this.medicalId, info.getRecordId(), REQUEST_CODE_FOR_DATA_UP_LOAD);
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_dc_duty_ppatient_disease_course;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_DATA_UP_LOAD) {
                refresh(medicalId,hospitalName);
            }
        }
    }

    @Override
    public void initViews(View rootView) {
        fgDcDutyPatientDiseaseCourseRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fgDcDutyPatientDiseaseCourseRv.setNestedScrollingEnabled(false);
        fgDcDutyPatientDiseaseCourseRv.setHasFixedSize(true);
        fgDcDutyPatientDiseaseCourseRv.setAdapter(dcDutyPatientDiseaseCourseAdapter);
        fgDcdutyPatientDiseaseUpLoadIv.setOnClickListener(v -> {
            Disposable disposable = PermissionHelper.init(this).addPermissions(Manifest.permission.RECORD_AUDIO).requestEachCombined()
                    .subscribe(permission -> {
                        if (!permission.granted) {
                            new CommonDialog.Builder(getBaseActivity())
                                    .setTitle("权限授予")
                                    .setCancelable(false)
                                    .setMessage("在设置-应用管理-权限中开启录音权限，才能正常使用该功能")
                                    .setPositiveButton("取消", () -> {
                                    })
                                    .setNegativeButton("确定", () -> {
                                        Utils.gotoAppDetailSetting();
                                    })
                                    .show();
                        } else {
                            getDisplay().startDCDutyPatientDataUpLoadActivity(medicalId, 0, REQUEST_CODE_FOR_DATA_UP_LOAD);
                        }
                    });
            compositeDisposable.add(disposable);
        });
//        getDiseaseListInfo(medicalId);
    }


    /**
     * //获取患者的病程信息
     *
     * @param medicalId 病例ID
     */
    private void getDiseaseListInfo(int medicalId) {
        if (null != mDCManager && 0 != medicalId) {
            mDCManager.getDiseaseListInfo(medicalId, new DefaultResultCallback<List<DCDutyPatientDiseaseItemInfo>>() {
                @Override
                public void onSuccess(List<DCDutyPatientDiseaseItemInfo> dcDutyPatientDiseaseItemInfos, BaseResult baseResult) {
                    dcDutyPatientDiseaseCourseAdapter.setHospitalName(hospitalName);
                    dcDutyPatientDiseaseCourseAdapter.setNewData(dcDutyPatientDiseaseItemInfos);
                }
            });
        }
    }

    public void refresh(int medicalId,String hospitalName) {
        this.medicalId = medicalId;
        this.hospitalName = hospitalName;
        getDiseaseListInfo(medicalId);
    }
}
