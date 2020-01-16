package cn.longmaster.hospital.doctor.ui.doctor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorStyleInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorStyleRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorDetailInfoArticleAdapter;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorDetailInfoClassAdapter;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorDetailInfoVideoAdapter;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * @author Mloong_Abiao
 * @date 2019/6/5 14:01
 * @description:
 */
public class DoctorExpertInfoFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_doctor_detail_expert_article_rv)
    private RecyclerView fragmentDoctorDetailExpertArticleRv;
    @FindViewById(R.id.fragment_doctor_detail_expert_video_rv)
    private RecyclerView fragmentDoctorDetailExpertVideoRv;
    @FindViewById(R.id.fragment_doctor_detail_expert_class_rv)
    private RecyclerView fragmentDoctorDetailExpertClassRv;
    @FindViewById(R.id.include_doctor_no_data_ll)
    private LinearLayout includeDoctorNoDataLl;
    @FindViewById(R.id.fragment_doctor_detail_expert_info_ll)
    private LinearLayout fragmentDoctorDetailExpertInfoLl;
    @FindViewById(R.id.fragment_doctor_detail_expert_article_ll)
    private LinearLayout fragmentDoctorDetailExpertArticleLl;
    @FindViewById(R.id.fragment_doctor_detail_expert_video_ll)
    private LinearLayout fragmentDoctorDetailExpertVideoLl;
    @FindViewById(R.id.fragment_doctor_detail_expert_class_ll)
    private LinearLayout fragmentDoctorDetailExpertClassLl;
    @FindViewById(R.id.fragment_doctor_detail_expert_info_nsv)
    private NestedScrollView fragmentDoctorDetailExpertInfoNsv;
    private DoctorDetailInfoArticleAdapter doctorDetailInfoArticleAdapter;
    private DoctorDetailInfoClassAdapter doctorDetailInfoClassAdapter;
    private DoctorDetailInfoVideoAdapter doctorDetailInfoVideoAdapter;
    private DoctorBaseInfo mDoctorBaseInfo;
    private static final String KEY_TO_QUERY_DOCTOR_INFO = "_KEY_TO_QUERY_DOCTOR_INFO_";
    @AppApplication.Manager
    UserInfoManager userInfoManager;

    public static DoctorExpertInfoFragment getInstance(DoctorBaseInfo doctorBaseInfo) {
        DoctorExpertInfoFragment doctorExpertInfoFragment = new DoctorExpertInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TO_QUERY_DOCTOR_INFO, doctorBaseInfo);
        doctorExpertInfoFragment.setArguments(bundle);
        return doctorExpertInfoFragment;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        doctorDetailInfoArticleAdapter = new DoctorDetailInfoArticleAdapter(R.layout.item_doctor_detail_doctor_article, new ArrayList<>(0));
        doctorDetailInfoClassAdapter = new DoctorDetailInfoClassAdapter(R.layout.item_doctor_detail_doctor_class, new ArrayList<>(0));
        doctorDetailInfoVideoAdapter = new DoctorDetailInfoVideoAdapter(R.layout.item_doctor_detail_doctor_video, new ArrayList<>(0));
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_doctor_expert_info;
    }

    @Override
    public void initViews(View rootView) {
        fragmentDoctorDetailExpertArticleRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentDoctorDetailExpertArticleRv.setNestedScrollingEnabled(false);
        fragmentDoctorDetailExpertArticleRv.setHasFixedSize(true);
        fragmentDoctorDetailExpertClassRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentDoctorDetailExpertClassRv.setNestedScrollingEnabled(false);
        fragmentDoctorDetailExpertClassRv.setHasFixedSize(true);
        fragmentDoctorDetailExpertVideoRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentDoctorDetailExpertVideoRv.setNestedScrollingEnabled(false);
        fragmentDoctorDetailExpertVideoRv.setHasFixedSize(true);
        fragmentDoctorDetailExpertArticleRv.setAdapter(doctorDetailInfoArticleAdapter);
        fragmentDoctorDetailExpertClassRv.setAdapter(doctorDetailInfoClassAdapter);
        fragmentDoctorDetailExpertVideoRv.setAdapter(doctorDetailInfoVideoAdapter);
        if (getDoctorBaseInfo().getUserId() > 0) {
            getDoctorStyle(getDoctorBaseInfo().getUserId());
        }
        initListener();
    }

    private void initListener() {
        doctorDetailInfoArticleAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorStyleInfo.ArticleDataBean articleDataBean = (DoctorStyleInfo.ArticleDataBean) adapter.getItem(position);
            doctorDetailInfoArticleAdapter.setSelected(position);
            if (null != articleDataBean) {
                getDisplay().startBrowserActivity(articleDataBean.getUrlLink(), articleDataBean.getContent(), false, false, 0, 0);
            }
        });
        doctorDetailInfoClassAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorStyleInfo.MaterialDataBean materialDataBean = (DoctorStyleInfo.MaterialDataBean) adapter.getItem(position);
            doctorDetailInfoClassAdapter.setSelected(position);
            if (null != materialDataBean) {
                int uploadUserId = materialDataBean.getUploadUserId();
                if (uploadUserId != userInfoManager.getCurrentUserInfo().getUserId()
                        && userInfoManager.getCurrentUserInfo().getUserId() != getDoctorBaseInfo().getUserId() && !materialDataBean.isSelfVisible()) {
                    ToastUtils.showShort("当前资料不可查看，请预约该专家查房");
                } else {
                    getDisplay().startBrowserActivity(materialDataBean.getContentUrl(), materialDataBean.getConstitutor(), false, false, 0, 0);
                }
            }
        });
        doctorDetailInfoVideoAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorStyleInfo.VideoDataBean videoDataBean = (DoctorStyleInfo.VideoDataBean) adapter.getItem(position);
            if (null != videoDataBean) {
                getDisplay().startBrowserActivity(videoDataBean.getUrlLink(), videoDataBean.getContent(), false, false, 0, 0);
            }
        });
    }

    private DoctorBaseInfo getDoctorBaseInfo() {
        if (null == mDoctorBaseInfo) {
            mDoctorBaseInfo = (DoctorBaseInfo) getArguments().getSerializable(KEY_TO_QUERY_DOCTOR_INFO);
        }
        return mDoctorBaseInfo;
    }

    private void getDoctorStyle(int doctorId) {
        GetDoctorStyleRequester requester = new GetDoctorStyleRequester(new DefaultResultCallback<DoctorStyleInfo>() {
            @Override
            public void onSuccess(DoctorStyleInfo doctorStyleInfo, BaseResult baseResult) {
                if (null != doctorStyleInfo && !doctorStyleInfo.isEmpty()) {
                    populateDoctorStyle(doctorStyleInfo);
                    includeDoctorNoDataLl.setVisibility(View.GONE);
                    fragmentDoctorDetailExpertInfoLl.setVisibility(View.VISIBLE);
                } else {
                    fragmentDoctorDetailExpertInfoLl.setVisibility(View.GONE);
                    includeDoctorNoDataLl.setVisibility(View.VISIBLE);
                }
//            createTestData(doctorStyleInfo);
//            populateDoctorStyle(doctorStyleInfo);
            }
        });
        requester.setDoctorId(doctorId);
        requester.start();
    }

    private void populateDoctorStyle(@NonNull DoctorStyleInfo doctorStyleInfo) {
        //createTestData(doctorStyleInfo);
        List<DoctorStyleInfo.ArticleDataBean> articleDataBeans = doctorStyleInfo.getArticleData();
        List<DoctorStyleInfo.VideoDataBean> videoDataBeans = doctorStyleInfo.getVideoData();
        List<DoctorStyleInfo.MaterialDataBean> materialDataBeans = doctorStyleInfo.getMaterialData();
        fragmentDoctorDetailExpertArticleLl.setVisibility(LibCollections.isEmpty(articleDataBeans) ? View.GONE : View.VISIBLE);
        fragmentDoctorDetailExpertVideoLl.setVisibility(LibCollections.isEmpty(videoDataBeans) ? View.GONE : View.VISIBLE);
        fragmentDoctorDetailExpertClassLl.setVisibility(LibCollections.isEmpty(materialDataBeans) ? View.GONE : View.VISIBLE);
        doctorDetailInfoArticleAdapter.setNewData(articleDataBeans);
        doctorDetailInfoClassAdapter.setNewData(materialDataBeans);
        doctorDetailInfoVideoAdapter.setNewData(videoDataBeans);
    }

    public void startShotView(BitmapUtils.OnBitmapCreateListener onBitmapCreateListener) {
        if (null != fragmentDoctorDetailExpertInfoNsv && null != includeDoctorNoDataLl) {
            int h = 0;
            for (int i = 0; i < fragmentDoctorDetailExpertInfoNsv.getChildCount(); i++) {
                h += fragmentDoctorDetailExpertInfoNsv.getChildAt(i).getHeight();
                fragmentDoctorDetailExpertInfoNsv.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                if (h == 0) {
                    onBitmapCreateListener.onSuccess(BitmapUtils.shotView(includeDoctorNoDataLl));
                } else {
                    onBitmapCreateListener.onSuccess(BitmapUtils.shotScrollView(fragmentDoctorDetailExpertInfoNsv));
                }
            }
        }
    }
}
