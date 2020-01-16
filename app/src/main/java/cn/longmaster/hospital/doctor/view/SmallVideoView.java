package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoTagInfo;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.utils.DisplayUtil;

/**
 * 视频房间小视屏视图
 * Created by JinKe on 2016-12-28.
 */
public class SmallVideoView extends RelativeLayout {
    private RelativeLayout videoViewRl;
    private RelativeLayout videoParentRl;
    private TextView nameTv;
    private ImageView avatar;
    private ImageView signalIv;
    private ImageView voiceIv;
    private ImageView stateLayerIv;
    private TextView stateLayerTv;

    private onSmallVideoClickListener smallVideoClickListener;

    public SmallVideoView(Context context) {
        this(context, null);
    }

    public SmallVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_small_video, this, false);
        initView(rootView);
        addView(rootView);
        regListener();
        setRoomState(ConsultRoomManager.AV_TYPE_VIDEO, AppConstant.RoomState.STATE_DEFAULT);
    }

    public RelativeLayout getVideoParentRl() {
        return videoParentRl;
    }

    private void initView(View view) {
        this.videoViewRl = (RelativeLayout) view.findViewById(R.id.view_small_video_video_rl);
        this.videoParentRl = (RelativeLayout) view.findViewById(R.id.view_small_video_parent_rl);
        this.nameTv = (TextView) view.findViewById(R.id.view_small_video_name_tv);
        this.avatar = (ImageView) view.findViewById(R.id.view_small_video_avatar_iv);
        this.signalIv = (ImageView) view.findViewById(R.id.view_small_video_video_signal_icon_iv);
        this.voiceIv = (ImageView) view.findViewById(R.id.view_small_video_video_voice_icon_iv);
        this.stateLayerIv = (ImageView) view.findViewById(R.id.view_small_video_video_state_iv);
        this.stateLayerTv = (TextView) view.findViewById(R.id.view_small_video_video_state_tv);
    }

    private void regListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                smallVideoClickListener.onSmallVideoClicked((SmallVideoView) v);
            }
        });
    }

    /**
     * 初始化视屏房间
     * 切换视频时调用
     *
     * @param roomState
     * @param videoInfo
     */
    public void initSmallVideo(int roomState, VideoInfo videoInfo) {
        Logger.logD(Logger.ROOM, "initSmallVideo：" + videoInfo);
        if (videoInfo == null) {
            return;
        }
        addSurfaceView(videoInfo);
        setAvatar(videoInfo.getUserType());
        if (videoInfo.getUserType() != AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
            if (videoInfo.getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                if (videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                    setName(getResources().getString(R.string.consult_help_customer_service_personnel));
                } else {
                    setName(videoInfo.getUseName());
                }
            } else {
                setName(getResources().getString(R.string.consult_room_screen_share));
            }
        }
        setShowVoice(false);
        setRoomState(roomState, videoInfo.getVideoState());
    }

    public void setSmallVideoClickListener(onSmallVideoClickListener smallVideoClickListener) {
        this.smallVideoClickListener = smallVideoClickListener;
    }

    /**
     * 添加GLSurfaceView
     *
     * @param videoInfo
     */
    public void addSurfaceView(VideoInfo videoInfo) {
        Logger.logD(Logger.ROOM, "addSurfaceView");
        adjustSmallVideoSize(videoInfo);
        videoViewRl.addView(videoInfo.getParentRl());
    }

    /**
     * 调整surfaceView大小
     *
     * @param videoInfo
     */
    private void adjustSmallVideoSize(final VideoInfo videoInfo) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                int videoWidth;
                int videoHeight;
                if (videoInfo.getGlSurfaceView().getWidth() == 0 || videoInfo.getGlSurfaceView().getHeight() == 0) {
                    videoWidth = DisplayUtil.dip2px(100);
                    videoHeight = DisplayUtil.dip2px(75);
                } else {
                    videoWidth = videoInfo.getGlSurfaceView().getWidth();
                    videoHeight = videoInfo.getGlSurfaceView().getHeight();
                }
                int rawWidth = DisplayUtil.dip2px(100);
                int rawHeight = DisplayUtil.dip2px(75);
                int newWidth;
                int newHeight;

                if (rawWidth * videoHeight > videoWidth * rawHeight) {
                    newHeight = rawHeight;
                    newWidth = videoWidth * newHeight / videoHeight;
                } else {
                    newWidth = rawWidth;
                    newHeight = videoHeight * newWidth / videoWidth;
                }
                RelativeLayout.LayoutParams parentLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                parentLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                videoInfo.getParentRl().setLayoutParams(parentLp);

                RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(newWidth, newHeight);
                viewLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                videoInfo.getGlSurfaceView().setLayoutParams(viewLp);

            }
        });
    }

    /**
     * 移除GLSurfaceView
     *
     * @param parentRl
     */
    public void removeSurfaceView(RelativeLayout parentRl) {
        videoViewRl.removeView(parentRl);
    }

    /**
     * 设置是否显示语音图标
     *
     * @param isShow true 显示
     */
    public void setShowVoice(boolean isShow) {
        this.voiceIv.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    /**
     * 设置名字
     */
    public void setName(String name) {
        this.nameTv.setText(name);
    }

    public void setAvatar(int userType) {
        if (userType == AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR
                || userType == AppConstant.UserType.USER_TYPE_MDT_DOCTOR
                || userType == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
            avatar.setImageResource(R.drawable.ic_small_video_super_expert);
        } else if (userType == AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR) {
            avatar.setImageResource(R.drawable.ic_small_video_first_expert);
        } else if (userType == AppConstant.UserType.USER_TYPE_PATIENT
                || userType == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR
                || userType == AppConstant.UserType.USER_TYPE_MDT_PATIENT) {
            avatar.setImageResource(R.drawable.ic_small_video_patient_icon);
        } else {
            avatar.setImageResource(R.drawable.ic_small_video_patient_icon);
        }
    }

    /**
     * 设置信号图标
     *
     * @param signalState
     */
    public void setSignal(int signalState) {
        if (signalState == AppConstant.SignalState.SIGNAL_BAD) {
            signalIv.setImageResource(R.drawable.ic_small_video_signal_bad);
        } else if (signalState == AppConstant.SignalState.SIGNAL_GENERAL) {
            signalIv.setImageResource(R.drawable.ic_small_video_signal_general);
        } else {
            signalIv.setImageResource(R.drawable.ic_small_video_signal_good);
        }
    }

    /**
     * 设置视频状态
     *
     * @param state
     */
    public void setRoomState(int roomState, int state) {
        Logger.logD(Logger.ROOM, "setRoomState->roomState:" + roomState + ",roomState:" + state);
        switch (state) {
            case AppConstant.RoomState.STATE_CALLING:
                stateLayerTv.setVisibility(INVISIBLE);
                stateLayerIv.setImageResource(R.drawable.ic_small_video_calling);
                stateLayerIv.setVisibility(VISIBLE);
                videoViewRl.setVisibility(INVISIBLE);
                break;

            case AppConstant.RoomState.STATE_MISS:
                stateLayerTv.setVisibility(INVISIBLE);
                stateLayerIv.setImageResource(R.drawable.ic_small_video_refusled);
                stateLayerIv.setVisibility(VISIBLE);
                videoViewRl.setVisibility(INVISIBLE);
                break;

            case AppConstant.RoomState.STATE_BEING_VIDEO:
            case AppConstant.RoomState.STATE_DEFAULT:
                if (roomState == ConsultRoomManager.AV_TYPE_VOICE) {
                    stateLayerTv.setVisibility(VISIBLE);
                    stateLayerIv.setVisibility(INVISIBLE);
                    videoViewRl.setVisibility(INVISIBLE);
                } else {
                    stateLayerIv.setVisibility(INVISIBLE);
                    stateLayerTv.setVisibility(INVISIBLE);
                    videoViewRl.setVisibility(VISIBLE);
                }
                break;

            case AppConstant.RoomState.STATE_BEING_VOICE:
                stateLayerTv.setVisibility(VISIBLE);
                stateLayerIv.setVisibility(INVISIBLE);
                videoViewRl.setVisibility(INVISIBLE);
                break;

            default:
                stateLayerIv.setVisibility(INVISIBLE);
                break;
        }
    }

    public RelativeLayout getVideoParent() {
        return videoParentRl;
    }

    public interface onSmallVideoClickListener {
        void onSmallVideoClicked(SmallVideoView view);
    }
}
