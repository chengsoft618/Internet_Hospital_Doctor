package cn.longmaster.hospital.doctor.core.entity.consult.video;

import android.opengl.GLSurfaceView;
import android.widget.RelativeLayout;

import com.longmaster.video.LMVideoMgr;
import com.longmaster.video.VideoRendererGui;

import cn.longmaster.hospital.doctor.core.AppConstant;

/**
 * 视频信息
 * Created by JinKe on 2016-12-28.
 */
public class VideoInfo {
    private int useId;
    private String useName;
    private String hospitalName;
    private int userType;
    private int videoState = AppConstant.RoomState.STATE_BEING_VIDEO;
    private int signalState = AppConstant.SignalState.SIGNAL_GOOD;
    private GLSurfaceView glSurfaceView;
    private VideoRendererGui rendererGui;
    private long ssrc;
    private RelativeLayout parentRl;
    private LMVideoMgr.LMVideoCaptureConfig captureConfig;
    private LMVideoMgr.LMVideoDisplayConfig displayConfig;
    private int type;

    public int getUseId() {
        return useId;
    }

    public void setUseId(int useId) {
        this.useId = useId;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getVideoState() {
        return videoState;
    }

    public void setVideoState(int videoState) {
        this.videoState = videoState;
    }

    public int getSignalState() {
        return signalState;
    }

    public void setSignalState(int signalState) {
        this.signalState = signalState;
    }

    public GLSurfaceView getGlSurfaceView() {
        return glSurfaceView;
    }

    public void setGlSurfaceView(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
    }

    public VideoRendererGui getRendererGui() {
        return rendererGui;
    }

    public void setRendererGui(VideoRendererGui rendererGui) {
        this.rendererGui = rendererGui;
    }

    public long getSsrc() {
        return ssrc;
    }

    public void setSsrc(long ssrc) {
        this.ssrc = ssrc;
    }

    public RelativeLayout getParentRl() {
        return parentRl;
    }

    public void setParentRl(RelativeLayout parentRl) {
        this.parentRl = parentRl;
    }

    public LMVideoMgr.LMVideoCaptureConfig getCaptureConfig() {
        return captureConfig;
    }

    public void setCaptureConfig(LMVideoMgr.LMVideoCaptureConfig captureConfig) {
        this.captureConfig = captureConfig;
    }

    public LMVideoMgr.LMVideoDisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(LMVideoMgr.LMVideoDisplayConfig displayConfig) {
        this.displayConfig = displayConfig;
    }

    public void destoryRendererGui() {
        rendererGui = null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "useId=" + useId +
                ", useName='" + useName + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", userType=" + userType +
                ", videoState=" + videoState +
                ", signalState=" + signalState +
                ", glSurfaceView=" + glSurfaceView +
                ", rendererGui=" + rendererGui +
                ", ssrc=" + ssrc +
                ", parentRl=" + parentRl +
                ", captureConfig=" + captureConfig +
                ", displayConfig=" + displayConfig +
                ", type=" + type +
                '}';
    }
}
