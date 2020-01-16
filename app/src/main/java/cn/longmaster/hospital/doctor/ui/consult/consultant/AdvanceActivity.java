package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceAppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceResultInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.AdvanceRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.AdvanceView;
import cn.longmaster.hospital.doctor.view.AppActionBar;

/**
 * 垫付页面
 * Created by W·H·K on 2017/12/26.
 */
public class AdvanceActivity extends BaseActivity {
    @FindViewById(R.id.activity_advance_list_view)
    private LinearLayout mView;
    @FindViewById(R.id.activity_advance_bar)
    private AppActionBar mActionBar;

    private List<VisitDetailsInfo> mAdvances;
    private Map<Integer, Integer> mHospitalIdMap = new HashMap<>();
    private List<Integer> mAppointments = new ArrayList<>();
    private List<AdvanceInfo> mAdvanceInfos = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    public static void startAdvanceActivity(Activity activity, List<VisitDetailsInfo> advances) {
        Intent intent = new Intent(activity, AdvanceActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, (Serializable) advances);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        ViewInjecter.inject(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        if (mAdvances != null) {
            mAdvances.clear();
        }
        mAdvances = (List<VisitDetailsInfo>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID);
        Logger.logD(Logger.COMMON, "->AdvanceActivity()->initData->mAdvances:" + mAdvances);
        for (VisitDetailsInfo info : mAdvances) {
            mAppointments.add(info.getAppointmentId());
            if (!mHospitalIdMap.containsKey(info.getRecommendHospital())) {
                mHospitalIdMap.put(info.getRecommendHospital(), info.getRecommendHospital());
            }
        }
        Logger.logD(Logger.COMMON, "->AdvanceActivity()->initData->mHospitalIdMap:" + mHospitalIdMap);
        for (Integer key : mHospitalIdMap.keySet()) {
            AdvanceInfo info = new AdvanceInfo();
            info.setHospitalId(key);
            mAdvanceInfos.add(info);
        }

        for (int i = 0; i < mAdvanceInfos.size(); i++) {
            List<AdvanceAppointmentInfo> list = new ArrayList<>();
            for (int j = 0; j < mAdvances.size(); j++) {
                if (mAdvanceInfos.get(i).getHospitalId() == mAdvances.get(j).getRecommendHospital()) {
                    AdvanceAppointmentInfo info = new AdvanceAppointmentInfo();
                    info.setAppointmentId(mAdvances.get(j).getAppointmentId());
                    info.setPatientName(mAdvances.get(j).getPatientRealName());
                    info.setPayValue(mAdvances.get(j).getPayValue());
                    info.setHospitalId(mAdvanceInfos.get(i).getHospitalId());
                    list.add(info);
                }
                mAdvanceInfos.get(i).setAppointmentInfos(list);
            }
        }
        Logger.logD(Logger.COMMON, "->AdvanceActivity()->initData->mAdvanceInfos:" + mAdvanceInfos);
    }

    private void initView() {
        for (int i = 0; i < mAdvanceInfos.size(); i++) {
            AdvanceView listView = new AdvanceView(this);
            listView.setData(mAdvanceInfos.get(i));
            mView.addView(listView);
        }
    }

    private void initListener() {
        mActionBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.activity_advance_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_advance_tv:
                advance();
                break;
        }
    }

    private void advance() {
        showProgressDialog();
        AdvanceRequester requester = new AdvanceRequester(new OnResultListener<AdvanceResultInfo>() {
            @Override
            public void onResult(BaseResult baseResult, AdvanceResultInfo advanceResultInfo) {
                Logger.logD(Logger.COMMON, "->AdvanceActivity()->advance-->advanceResultInfo:" + advanceResultInfo + ", baseResult.getCode():" + baseResult.getCode());
                dismissProgressDialog();
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    AdvanceResultActivity.startAdvanceResultActivity(getActivity(), advanceResultInfo, mAdvances);
                    finish();
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.appointmentIdList = mAppointments;
        requester.doPost();
    }

    public void rightClick(View view) {
        Intent intent = new Intent(getActivity(), SchedulingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return true;
    }

    private void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
