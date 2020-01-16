package cn.longmaster.hospital.doctor.core.upload.newupload;

import java.io.Serializable;

/**
 * 支付确认单图片
 * Created by Yang² on 2017/12/29.
 */

public class UploadPicFileInfo implements Serializable {
    private String localFilePath;
    private String serverFileName;
    private String urlPath;
    private int state;
    private int progress;

    public UploadPicFileInfo() {
    }

    public UploadPicFileInfo(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public UploadPicFileInfo(String serverFileName, String urlPath, int state) {
        this.serverFileName = serverFileName;
        this.urlPath = urlPath;
        this.state = state;
    }

    public UploadPicFileInfo(String localFilePath, String serverFileName, String urlPath, int state, int progress) {
        this.localFilePath = localFilePath;
        this.serverFileName = serverFileName;
        this.urlPath = urlPath;
        this.state = state;
        this.progress = progress;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "UploadPicFileInfo{" +
                "localFilePath='" + localFilePath + '\'' +
                ", serverFileName='" + serverFileName + '\'' +
                ", urlPath='" + urlPath + '\'' +
                ", state=" + state +
                ", progress=" + progress +
                '}';
    }
}
