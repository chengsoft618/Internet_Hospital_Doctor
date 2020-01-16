package cn.longmaster.hospital.doctor.ui.doctor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorFeedBackInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorTeamItemInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorVisitingItem;
import cn.longmaster.hospital.doctor.core.entity.doctor.RecmdInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorFeedBackRequester;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorVisitingRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorDetailInfoTeamAdapter;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.hospital.doctor.view.timetable.ReceptionScheduleView;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author Mloong_Abiao
 * @date 2019/6/5 14:01
 * @description:
 */
public class DoctorDetailInfoFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_doctor_detail_doctor_skill_tv)
    private TextView fragmentDoctorDetailDoctorSkillTv;
    @FindViewById(R.id.fragment_doctor_detail_doctor_info_tv)
    private TextView fragmentDoctorDetailDoctorInfoTv;
    @FindViewById(R.id.fragment_doctor_detail_work_time_st)
    private ReceptionScheduleView fragmentDoctorDetailWorkTimeSt;
    @FindViewById(R.id.fragment_doctor_detail_service_fraction)
    private TextView fragmentDoctorDetailServiceFraction;
    @FindViewById(R.id.fragment_doctor_detail_doctor_skill_fraction_desc_tv)
    private TextView fragmentDoctorDetailDoctorSkillFractionDescTv;
    @FindViewById(R.id.fragment_doctor_detail_doctor_skill_level_desc_tv)
    private TextView fragmentDoctorDetailDoctorSkillLevelDescTv;
    @FindViewById(R.id.fragment_doctor_detail_doctor_service_level_fraction_tv)
    private TextView fragmentDoctorDetailDoctorServiceLevelFractionTv;
    @FindViewById(R.id.fragment_doctor_detail_doctor_service_level_desc_tv)
    private TextView fragmentDoctorDetailDoctorServiceLevelDescTv;
    @FindViewById(R.id.fragment_doctor_detail_doctor_service_fbl)
    private FlexboxLayout fragmentDoctorDetailDoctorServiceFbl;
    @FindViewById(R.id.fragment_doctor_detail_recommend_reason_tv)
    private TextView fragmentDoctorDetailRecommendReasonTv;
    @FindViewById(R.id.fragment_doctor_detail_belong_team_rv)
    private RecyclerView fragmentDoctorDetailBelongTeamRv;
    @FindViewById(R.id.include_doctor_no_data_ll)
    private LinearLayout includeDoctorNoDataLl;
    @FindViewById(R.id.fragment_doctor_detail_info_ll)
    private LinearLayout fragmentDoctorDetailInfoLl;
    @FindViewById(R.id.fragment_doctor_detail_feed_back_ll)
    private LinearLayout fragmentDoctorDetailFeedBackLl;
    @FindViewById(R.id.fragment_doctor_detail_recommend_reason_ll)
    private LinearLayout fragmentDoctorDetailRecommendReasonLl;
    @FindViewById(R.id.fragment_doctor_detail_work_time_ll)
    private LinearLayout fragmentDoctorDetailWorkTimeLl;
    @FindViewById(R.id.fragment_doctor_detail_belong_team_ll)
    private LinearLayout fragmentDoctorDetailBelongTeamLl;
    @FindViewById(R.id.fragment_doctor_detail_info_nsv)
    private NestedScrollView fragmentDoctorDetailInfoNsv;
    private DoctorBaseInfo mBaseDoctorInfo;

    private BitmapUtils.OnBitmapCreateListener onBitmapCreateListener;
    //医生基本信息
    private static final String KEY_TO_QUERY_DOCTOR_INFO = "_KEY_TO_QUERY_DOCTOR_INFO_";
    private DoctorDetailInfoTeamAdapter doctorTeamAdapter;

    public static DoctorDetailInfoFragment getInstance(DoctorBaseInfo doctorBaseInfo) {
        DoctorDetailInfoFragment doctorDetailInfoFragment = new DoctorDetailInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TO_QUERY_DOCTOR_INFO, doctorBaseInfo);
        doctorDetailInfoFragment.setArguments(bundle);
        return doctorDetailInfoFragment;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        doctorTeamAdapter = new DoctorDetailInfoTeamAdapter(R.layout.item_doctor_detail_doctor_team, new ArrayList<>(0));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_doctor_detail_info;
    }

    @Override
    public void initViews(View rootView) {
        fragmentDoctorDetailBelongTeamRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentDoctorDetailBelongTeamRv.setAdapter(doctorTeamAdapter);
        //createDoctorBaseInfoTestData(getDoctorBaseInfo());
        if (isEntering()) {
            populateDoctorInfo(getDoctorBaseInfo());
        } else {
            populateNoEnteringDoctorInfo(getDoctorBaseInfo());
        }
        getDoctorFeedBack(getDoctorBaseInfo().getUserId());
        getDoctorVisiting(getDoctorBaseInfo().getUserId());
        initListener();
    }

    private void initListener() {
        doctorTeamAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorTeamItemInfo doctorTeamItemInfo = (DoctorTeamItemInfo) adapter.getItem(position);
            if (null != doctorTeamItemInfo) {
                getDisplay().startBrowserActivity(createDoctorTeamUrl(doctorTeamItemInfo.getTeamId()), doctorTeamItemInfo.getTeamName()
                        , true, false, 0, 0);
            }
        });
    }

    private String createDoctorTeamUrl(String teamId) {
        return AppConfig.getAdwsUrl() + "ExpertTeam/expert_team_info/tid/" + teamId;
    }

    private DoctorBaseInfo getDoctorBaseInfo() {
        if (null == mBaseDoctorInfo) {
            mBaseDoctorInfo = (DoctorBaseInfo) getArguments().getSerializable(KEY_TO_QUERY_DOCTOR_INFO);
        }
        return mBaseDoctorInfo;
    }

    /**
     * 医生是否入住
     */
    private boolean isEntering() {
        return getDoctorBaseInfo().getUserId() > 0;
    }

    private void getDoctorFeedBack(int doctorId) {
        GetDoctorFeedBackRequester requester = new GetDoctorFeedBackRequester(new DefaultResultCallback<DoctorFeedBackInfo>() {
            @Override
            public void onSuccess(DoctorFeedBackInfo doctorFeedBackInfo, BaseResult baseResult) {
                if (null != doctorFeedBackInfo && !doctorFeedBackInfo.isEmpty()) {
                    populateDoctorFeedBackInfo(doctorFeedBackInfo);
                } else {
                    fragmentDoctorDetailFeedBackLl.setVisibility(View.GONE);
                }
            }
        });
        requester.setDoctorId(doctorId);
        requester.start();
    }

    private void populateNoEnteringDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        fragmentDoctorDetailWorkTimeLl.setVisibility(View.GONE);
        fragmentDoctorDetailFeedBackLl.setVisibility(View.GONE);
        fragmentDoctorDetailRecommendReasonLl.setVisibility(View.GONE);
        fragmentDoctorDetailBelongTeamLl.setVisibility(View.GONE);
        fragmentDoctorDetailDoctorSkillTv.setText(doctorBaseInfo.getDoctorSkill());
        if (null != doctorBaseInfo.getDoctorBriefInfo() && !StringUtils.isTrimEmpty(doctorBaseInfo.getDoctorBriefInfo().getIntroduce())) {
            fragmentDoctorDetailDoctorInfoTv.setText(doctorBaseInfo.getDoctorBriefInfo().getIntroduce());
        } else {
            fragmentDoctorDetailDoctorInfoTv.setText("该医生暂未上传资料");
        }
    }

    private void populateDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        fragmentDoctorDetailDoctorSkillTv.setText(doctorBaseInfo.getDoctorSkill());
        if (null != doctorBaseInfo.getDoctorBriefInfo()) {
            fragmentDoctorDetailDoctorInfoTv.setText(doctorBaseInfo.getDoctorBriefInfo().getIntroduce());
        } else {
            fragmentDoctorDetailDoctorInfoTv.setText("该医生暂未上传资料");
        }
        String recmd = formatRecmdInfo(doctorBaseInfo.getRecmdInfoList());
        if (StringUtils.isTrimEmpty(recmd)) {
            fragmentDoctorDetailRecommendReasonLl.setVisibility(View.GONE);
        } else {
            fragmentDoctorDetailRecommendReasonLl.setVisibility(View.VISIBLE);
            fragmentDoctorDetailRecommendReasonTv.setText(recmd);
        }
        List<DoctorTeamItemInfo> doctorTeamItemInfos = doctorBaseInfo.getTeamData();

        if (LibCollections.isEmpty(doctorTeamItemInfos)) {
            fragmentDoctorDetailBelongTeamLl.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentDoctorDetailBelongTeamRv.getLayoutParams();
            layoutParams.height = LibCollections.size(doctorTeamItemInfos) * DisplayUtil.dip2px(32);
            fragmentDoctorDetailBelongTeamRv.setLayoutParams(layoutParams);
            fragmentDoctorDetailBelongTeamLl.setVisibility(View.VISIBLE);
            doctorTeamAdapter.setNewData(doctorTeamItemInfos);
        }
    }

    private void getDoctorVisiting(int doctorId) {
        GetDoctorVisitingRequester requester = new GetDoctorVisitingRequester(new DefaultResultCallback<List<DoctorVisitingItem>>() {
            @Override
            public void onSuccess(List<DoctorVisitingItem> doctorVisitingItems, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(doctorVisitingItems)) {
                    fragmentDoctorDetailWorkTimeLl.setVisibility(View.VISIBLE);
                    fragmentDoctorDetailWorkTimeSt.setReceptionSchedules(doctorVisitingItems);
                } else {
                    fragmentDoctorDetailWorkTimeLl.setVisibility(View.GONE);
                }
            }
        });
        requester.setDoctorId(doctorId);
        requester.start();
    }

    /**
     * 用户反馈
     *
     * @param doctorFeedBackInfo
     */
    private void populateDoctorFeedBackInfo(DoctorFeedBackInfo doctorFeedBackInfo) {
        fragmentDoctorDetailFeedBackLl.setVisibility(View.VISIBLE);
        fragmentDoctorDetailServiceFraction.setText(doctorFeedBackInfo.getTotalScoreStr());
        if (null != doctorFeedBackInfo.getMedicalScore()) {
            fragmentDoctorDetailDoctorSkillFractionDescTv.setText(doctorFeedBackInfo.getMedicalScore().setScoreStr());
            fragmentDoctorDetailDoctorSkillLevelDescTv.setText(doctorFeedBackInfo.getMedicalScore().getScoreGrade());
        }
        if (null != doctorFeedBackInfo.getServiceScore()) {
            fragmentDoctorDetailDoctorServiceLevelFractionTv.setText(doctorFeedBackInfo.getServiceScore().setScoreStr());
            fragmentDoctorDetailDoctorServiceLevelDescTv.setText(doctorFeedBackInfo.getServiceScore().getScoreGrade());
        }

        if (LibCollections.isNotEmpty(doctorFeedBackInfo.getMedicalLabel())) {
            fragmentDoctorDetailDoctorServiceFbl.removeAllViews();
            for (DoctorFeedBackInfo.MedicalLabelBean medicalLabelBean : doctorFeedBackInfo.getMedicalLabel()) {
                if (null != medicalLabelBean) {
                    View view = getBaseActivity().getLayoutInflater().inflate(R.layout.item_fbl_doctor_detail_feed_back, null);
                    TextView tvOption = view.findViewById(R.id.item_fbl_doctor_detail_feed_back_option_tv);
                    TextView tvFraction = view.findViewById(R.id.item_fbl_doctor_detail_feed_back_option_fraction_tv);
                    tvOption.setText(medicalLabelBean.getLabelStr());
                    tvFraction.setText(medicalLabelBean.getLabelNumStr());
                    fragmentDoctorDetailDoctorServiceFbl.addView(view);
                }
            }
        }
    }

    /**
     * 推荐理由格式化
     *
     * @param recmdInfoList
     * @return
     */
    private String formatRecmdInfo(List<RecmdInfo> recmdInfoList) {
        if (LibCollections.isEmpty(recmdInfoList)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < LibCollections.size(recmdInfoList); i++) {
            builder.append(i == 0 ? "• " + recmdInfoList.get(i).getContent() : "\n" + "• " + recmdInfoList.get(i).getContent());
        }
        return builder.toString();
    }

    public void startShotView(BitmapUtils.OnBitmapCreateListener onBitmapCreateListener) {
        if (null != fragmentDoctorDetailInfoNsv && null != includeDoctorNoDataLl) {
            int h = 0;
            for (int i = 0; i < fragmentDoctorDetailInfoNsv.getChildCount(); i++) {
                h += fragmentDoctorDetailInfoNsv.getChildAt(i).getHeight();
                fragmentDoctorDetailInfoNsv.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                if (h == 0) {
                    onBitmapCreateListener.onSuccess(BitmapUtils.shotView(includeDoctorNoDataLl));
                } else {
                    onBitmapCreateListener.onSuccess(BitmapUtils.shotScrollView(fragmentDoctorDetailInfoNsv));
                }
            }
        }
    }
}
