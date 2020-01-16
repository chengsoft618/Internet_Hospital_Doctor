package cn.longmaster.hospital.doctor.core.upload.newupload;

import android.support.annotation.IntDef;

/**
 * Created by YY on 17/9/21.
 */

public class UploadState {
    public static final int NOT_UPLOADED = 0;//没有上传
    public static final int UPLOADING = 1;//上传中
    public static final int UPLOAD_SUCCESS = 2;//上传成功
    public static final int UPLOAD_FAILED = 3;//上传失败
    public static final int UPLOAD_PAUSE = 4;//上传暂停

    @IntDef({NOT_UPLOADED, UPLOADING, UPLOAD_SUCCESS, UPLOAD_FAILED, UPLOAD_PAUSE})
    public @interface UploadStates {
    }
}
