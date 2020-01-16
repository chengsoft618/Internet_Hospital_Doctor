package cn.longmaster.hospital.doctor.core.upload;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;


/**
 * 带有文件上传task
 * (上传文件-->拿到服务器返回的文件名称-->再去调相关的业务接口)
 * Created by ddc on 2015-07-29.
 */
public abstract class AbsTask implements Serializable {
    private String userId;
    private String taskId;//任务ID
    private TaskState state = TaskState.NOT_UPLOADED;//发送状态

    private List<SingleFileInfo> fileList;//文件列表

    public AbsTask() {
        setUserId(String.valueOf(AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId()));
        setTaskId(UploadUtils.applyTaskId());
        setState(TaskState.NOT_UPLOADED);
    }

    /**
     * 获得上传文件的url路径
     *
     * @param fileInfo
     * @return
     */
    public abstract String getUploadFileUrl(SingleFileInfo fileInfo);

    /**
     * 处理单个文件上传成功
     *
     * @param sessionId
     * @param fileName
     * @param listener
     * @return true，继续上传未上传的文件，false，不继续上传未上传的文件，结束task
     */
    public boolean isFileUploadSuccess(String sessionId, String fileName, UploadTaskStateListener listener) {
        boolean b;
        SingleFileInfo fileInfo = getFileInfoById(sessionId);
        if (fileInfo == null) {
            b = dealFileUploadFailed(fileInfo, new Exception("fileInfo is null"), listener);
        } else if (TextUtils.isEmpty(fileName)) {
            b = dealFileUploadFailed(fileInfo, new Exception("fileName is empty"), listener);
        } else {
            fileInfo.setServerFileName(fileName);
            fileInfo.setProgress(100);
            b = true;
            dealFileUploadSuccess(fileInfo, listener);
        }
        return b;
    }

    protected abstract void dealFileUploadSuccess(SingleFileInfo fileInfo, UploadTaskStateListener listener);

    /**
     * 处理单个文件上传失败
     *
     * @param sessionId
     * @param e
     * @param listener
     * @return true，继续上传未上传的文件，false，不继续上传未上传的文件，结束task
     */
    public boolean dealFileUploadFailed(String sessionId, Exception e, UploadTaskStateListener listener) {
        SingleFileInfo fileInfo = getFileInfoById(sessionId);
        return dealFailed(fileInfo, e, listener);
    }

    /**
     * 处理单个文件上传失败
     *
     * @param fileInfo
     * @param e
     * @param listener
     * @return true，继续上传未上传的文件，false，不继续上传未上传的文件，结束task
     */
    protected boolean dealFailed(SingleFileInfo fileInfo, Exception e, UploadTaskStateListener listener) {
        if (fileInfo != null) {
            fileInfo.updateState(TaskState.UPLOAD_FAILED);
            fileInfo.setProgress(0);
        }
        if (listener != null) {
            listener.onFileUploadComplete(this, fileInfo, e);
        }
        return dealFileUploadFailed(fileInfo, e, listener);
    }

    /**
     * 处理单个文件上传失败
     *
     * @param fileInfo
     * @param e
     * @param listener
     * @return true，继续上传未上传的文件，false，不继续上传未上传的文件，结束task
     */
    protected abstract boolean dealFileUploadFailed(SingleFileInfo fileInfo, Exception e, UploadTaskStateListener listener);

    /**
     * 当所有文件都上传成功后，调用相应的业务接口处理方法
     *
     * @param l
     */
    public abstract void allFileUploadSuccess(UploadTaskStateListener l);

    /**
     * @param sessionId
     * @return
     */
    public SingleFileInfo getFileInfoById(String sessionId) {
        SingleFileInfo fileInfo = null;
        List<SingleFileInfo> list = getFileList();
        if (list == null || TextUtils.isEmpty(sessionId)) {
            return null;
        }
        for (SingleFileInfo info : list) {
            if (sessionId.equals(info.getSessionId())) {
                fileInfo = info;
                break;
            }
        }
        return fileInfo;
    }

    /**
     * 获得附件数量
     *
     * @return
     */
    public int getFileListSize() {
        return fileList == null ? 0 : fileList.size();
    }

    /**
     * 获得上传进度
     *
     * @return
     */
    public float getUploadProgress() {
        float progress = 0f;
        int size = getFileListSize();
        for (int i = 0; i < size; i++) {
            SingleFileInfo fileInfo = getFileList().get(i);
            progress += (((float) 1 / size) * fileInfo.getProgress());
        }
        return progress;
    }

    public String getFileName() {
        StringBuilder sb = new StringBuilder();

        List<SingleFileInfo> list = getFileList();
        for (SingleFileInfo fileInfo : list) {
            if (fileInfo == null || TextUtils.isEmpty(fileInfo.getServerFileName())) {
                continue;
            }
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(fileInfo.getServerFileName());
        }
        return sb.toString();
    }

    public List<SingleFileInfo> getFileList() {
        if (fileList == null) {
            fileList = new ArrayList<SingleFileInfo>();
        }
        return fileList;
    }


    public void insertDB() {
    }

    /**
     * 更新数据库
     */
    protected void updateDB() {
    }

    public void deleteDB() {
    }

    public void deleteDBAll() {
    }

    public void setFileList(List<SingleFileInfo> fileList) {
        this.fileList = fileList;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void updateState(TaskState state) {
        setState(state);
        updateDB();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AbsTaskInfo{" +
                "fileList=" + fileList +
                ", userId='" + userId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", state=" + state +
                '}';
    }
}
