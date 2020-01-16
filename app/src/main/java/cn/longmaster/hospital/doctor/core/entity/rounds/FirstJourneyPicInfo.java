package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/7/7.
 */

public class FirstJourneyPicInfo implements Serializable {
    private String picPath;
    private String picName;
    private String serviceUrl;
    private int upLoadState;
    private float progress;
    private boolean oldData;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public int getUpLoadState() {
        return upLoadState;
    }

    public void setUpLoadState(int upLoadState) {
        this.upLoadState = upLoadState;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isOldData() {
        return oldData;
    }

    public void setOldData(boolean oldData) {
        this.oldData = oldData;
    }

    @Override
    public String toString() {
        return "FirstJourneyPicInfo{" +
                "picPath='" + picPath + '\'' +
                ", picName='" + picName + '\'' +
                ", serviceUrl='" + serviceUrl + '\'' +
                ", upLoadState=" + upLoadState +
                ", progress=" + progress +
                ", oldData=" + oldData +
                '}';
    }
}
