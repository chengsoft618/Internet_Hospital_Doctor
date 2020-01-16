package cn.longmaster.hospital.doctor.core.upload.simple;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 文件上传结果
 * Created by yangyong on 16/8/11.
 */
public class FileUploadResult {
    @JsonField("op_type")
    private int opType;
    @JsonField("task_id")
    private String taskId;
    @JsonField("code")
    private int code = -1;
    @JsonField("user_id")
    private int userId;
    @JsonField("md5")
    private String md5;
    @JsonField("file_name")
    private String fileName;
    @JsonField("notice")
    private String notice;

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "FileUploadResult{" +
                "opType=" + opType +
                ", taskId='" + taskId + '\'' +
                ", code=" + code +
                ", userId=" + userId +
                ", md5='" + md5 + '\'' +
                ", fileName='" + fileName + '\'' +
                ", notice='" + notice + '\'' +
                '}';
    }
}
