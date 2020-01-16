package cn.longmaster.hospital.doctor.core.upload;

import java.io.Serializable;

/**
 * 文件上传认为状态，单个file和整个task任务
 * Created by ddc on 2015-08-03.
 */
public enum TaskState implements Serializable {
    NOT_UPLOADED,
    UPLOADING,
    UPLOAD_SUCCESS,
    UPLOAD_FAILED
}
