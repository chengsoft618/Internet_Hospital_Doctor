package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.BasicMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAssociatedMedicalInfo;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetRoundsAssociatedMedicalRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.RecordMedicalAdapter;

/**
 * Created by W·H·K on 2019/2/15.
 */

public class RecordMedicalFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_record_medical_recycler_view)
    private RecyclerView mRecyclerView;

    //private List<RoundsAssociatedMedicalInfo> mRoundsAssociatedMedicalInfos = new ArrayList<>();
    private RecordMedicalAdapter mAdapter;

    public static RecordMedicalFragment getInstance(int medicalId) {
        RecordMedicalFragment recordMedicalFragment = new RecordMedicalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO, medicalId);
        recordMedicalFragment.setArguments(bundle);
        return recordMedicalFragment;

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mAdapter = new RecordMedicalAdapter(R.layout.item_rounds_record_medical, new ArrayList<>(0));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            RoundsAssociatedMedicalInfo info = (RoundsAssociatedMedicalInfo) adapter.getItem(position);
            if (null != info) {
                BasicMedicalInfo basicMedicalInfo = new BasicMedicalInfo();
                basicMedicalInfo.setOrderState(1);
                basicMedicalInfo.setMedicalId(info.getMedicalId());
                basicMedicalInfo.setRelateRecord(true);
                getDisplay().startRoundsPatientInfoActivity(basicMedicalInfo, 0);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_record_medical;
    }

    @Override
    public void initViews(View rootView) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        getRoundsAssociatedMedical(getMedicalId());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getRoundsAssociatedMedical(getMedicalId());
        }
    }


    public void getRoundsAssociatedMedical(int medicalId) {
        GetRoundsAssociatedMedicalRequester requester = new GetRoundsAssociatedMedicalRequester((baseResult, roundsAssociatedMedicalInfos) -> {
            Logger.logI(Logger.APPOINTMENT, "RoundsPatientInfoActivity->getRoundsAssociatedMedical:-->roundsAssociatedMedicalInfos:" + roundsAssociatedMedicalInfos);
            if (baseResult.getCode() == 0) {
                mAdapter.setNewData(roundsAssociatedMedicalInfos);
            }
        });
        requester.medicalId = medicalId;
        requester.doPost();
    }

    private int getMedicalId() {
        return getArguments() == null ? 0 : getArguments().getInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO);
    }
}
