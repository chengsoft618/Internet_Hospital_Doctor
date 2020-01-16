package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDoctorListInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyDoctorListAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 21:21
 * @description: 项目医生列表
 */
public class DCDutyDoctorListActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dc_duty_doctor_list_aab)
    private AppActionBar actDcDutyDoctorListAab;
    @FindViewById(R.id.act_dc_duty_doctor_list_rv)
    private RecyclerView actDcDutyDoctorListRv;
    private DCDutyDoctorListAdapter dcDoctorListAdapter;
    private List<DCDoctorSectionInfo> dcDoctorSectionInfos = new ArrayList<>();
    @AppApplication.Manager
    private DCManager mDCManager;
    private int projectId;

    @Override
    protected void initDatas() {
        projectId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, 0);
        dcDoctorListAdapter = new DCDutyDoctorListAdapter(R.layout.item_dc_duty_dcotor_list, R.layout.layout_dc_duty_doctor_list_header, dcDoctorSectionInfos);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_doctor_list;
    }

    @Override
    protected void initViews() {
        actDcDutyDoctorListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyDoctorListRv.setAdapter(dcDoctorListAdapter);
        actDcDutyDoctorListAab.setLeftOnClickListener(v -> {
            onBackPressed();
        });
        getDoctorList(projectId);
    }

    private void getDoctorList(int itemId) {
        mDCManager.getDoctorList(itemId, 1, new DefaultResultCallback<List<DCProjectDoctorListInfo>>() {
            @Override
            public void onSuccess(List<DCProjectDoctorListInfo> dcProjectDoctorListInfos, BaseResult baseResult) {
                dcDoctorListAdapter.setNewData(mDCManager.createDoctorSectionInfoList(dcProjectDoctorListInfos));
            }
        });
    }
}
