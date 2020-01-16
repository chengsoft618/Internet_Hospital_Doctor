package cn.longmaster.hospital.doctor.ui.consult.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsVideoPlayActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.VideoPlaybackAdapter;

/**
 * @author ABiao_Abiao
 * @date 2019/11/1 14:12
 * @description:
 */
public class ConsultVideoPlaybackFragment extends NewBaseFragment {
    @FindViewById(R.id.layout_video_playback_grid_view)
    private GridView mGridView;
    private VideoPlaybackAdapter mVideoPlaybackAdapter;

    @AppApplication.Manager
    private ConsultManager mConsultManager;

    private OrderDetailsInfo mOrderDetailsInfo;
    private List<ReviewVideoInfo> mReviewVideoInfos = new ArrayList<>();
    private AppointmentInfo mAppointmentInfo;
    private int mOrderId;
    private int mAppointmentId;

    public static ConsultVideoPlaybackFragment getInstance(AppointmentInfo appointmentInfo) {
        ConsultVideoPlaybackFragment videoPlaybackFragment = new ConsultVideoPlaybackFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, appointmentInfo);
        videoPlaybackFragment.setArguments(bundle);
        return videoPlaybackFragment;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOrderDetailsInfo = (OrderDetailsInfo) bundle.getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO);
            mAppointmentInfo = (AppointmentInfo) bundle.getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO);
            Logger.logI(Logger.COMMON, "mOrderDetailsInfo:" + mOrderDetailsInfo);
        }
        if (mOrderDetailsInfo != null) {
            mReviewVideoInfos = mOrderDetailsInfo.getReviewVideoInfo();
            mOrderId = mOrderDetailsInfo.getOrderId();
        } else if (mAppointmentInfo != null) {
            mReviewVideoInfos = mAppointmentInfo.getReviewVideoInfo();
            mAppointmentId = mAppointmentInfo.getBaseInfo().getAppointmentId();
        }
        mVideoPlaybackAdapter = new VideoPlaybackAdapter(getActivity(), mReviewVideoInfos);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_video_playback_fragment;
    }

    @Override
    public void initViews(View rootView) {
        mGridView.setAdapter(mVideoPlaybackAdapter);
        mVideoPlaybackAdapter.setOnItemVideoPlayListener((view, position) -> playVideo(position));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getAppointmentInfo(mAppointmentId);
        }
    }

    /**
     * 获取预约信息
     *
     * @param appointmentId
     */
    private void getAppointmentInfo(int appointmentId) {
        mConsultManager.getAppointmentInfo(appointmentId, (baseResult, appointmentInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && appointmentInfo != null) {
                Logger.logI(Logger.APPOINTMENT, "VideoPlaybackFragment-->getAppointmentInfo->appointmentInfo:" + appointmentInfo);
                mReviewVideoInfos = appointmentInfo.getReviewVideoInfo();
                mVideoPlaybackAdapter.setData(mReviewVideoInfos);
            }
        });
    }

    private void playVideo(int position) {
        String path = mReviewVideoInfos.get(position).getFilePath();
        Logger.logD(Logger.COMMON, "VideoPlaybackFragment-->playVideo:path:" + path);
        Intent intent = new Intent();
        intent.setClass(getActivity(), RoundsVideoPlayActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, path);
        startActivity(intent);
    }
}
