package cn.longmaster.hospital.doctor.ui.rounds.fragment;

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
import cn.longmaster.hospital.doctor.core.entity.consult.GetOrderInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.rounds.OrderDetailsRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsVideoPlayActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.VideoPlaybackAdapter;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2019/2/13.
 */

public class VideoPlaybackFragment extends NewBaseFragment {
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

    public static VideoPlaybackFragment getInstance(OrderDetailsInfo orderDetailsInfo) {
        VideoPlaybackFragment videoPlaybackFragment = new VideoPlaybackFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO, orderDetailsInfo);
        videoPlaybackFragment.setArguments(bundle);
        return videoPlaybackFragment;
    }

    public static VideoPlaybackFragment getInstance(GetOrderInfo getOrderInfo) {
        VideoPlaybackFragment videoPlaybackFragment = new VideoPlaybackFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, getOrderInfo);
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
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_video_playback_fragment;
    }

    @Override
    public void initViews(View rootView) {
        mVideoPlaybackAdapter = new VideoPlaybackAdapter(getActivity(), mReviewVideoInfos);
        mGridView.setAdapter(mVideoPlaybackAdapter);
        mVideoPlaybackAdapter.setOnItemVideoPlayListener((view, position) -> playVideo(position));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getReviewVideoInfo();
        }
    }

    private void getReviewVideoInfo() {
        if (mOrderId != 0) {
            getOrderDetails();
        } else if (mAppointmentId != 0) {
            getAppointmentInfo(mAppointmentId);
        }
    }

    private void getOrderDetails() {
        OrderDetailsRequester requester = new OrderDetailsRequester(new DefaultResultCallback<OrderDetailsInfo>() {
            @Override
            public void onSuccess(OrderDetailsInfo orderDetailsInfo, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "VideoPlaybackFragment：baseResult" + baseResult + " , orderDetailsInfo" + orderDetailsInfo);
                if (orderDetailsInfo != null) {
                    mReviewVideoInfos = orderDetailsInfo.getReviewVideoInfo();
                    mVideoPlaybackAdapter.setData(mReviewVideoInfos);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.orderId = mOrderId;
        requester.doPost();
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
