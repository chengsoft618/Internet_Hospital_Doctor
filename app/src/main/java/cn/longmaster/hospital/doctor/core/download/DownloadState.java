package cn.longmaster.hospital.doctor.core.download;

/**
 * 视频下载状态
 * Created by Yang² on 2017/12/7.
 */

public class DownloadState {
    public static final int NOT_DOWNLOAD = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    public static final int DOWNLOAD_FAILED = 3;
    public static final int DOWNLOAD_PAUSE = 4;
}
