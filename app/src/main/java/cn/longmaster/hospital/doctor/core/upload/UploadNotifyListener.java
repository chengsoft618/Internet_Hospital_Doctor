package cn.longmaster.hospital.doctor.core.upload;

/**
 * 上传情况的回调函数
 */
public interface UploadNotifyListener {
    /**
     * 添加了新的任务
     *
     * @param taskInfo
     */
    void onNewTask(AbsTask taskInfo);


    /**
     * 开始上传附件
     *
     * @param taskInfo
     */
    void onTaskStart(AbsTask taskInfo);

    /**
     * 上传进度的回调（整个Task的进度）
     *
     * @param taskInfo
     */
    void onTaskProgressChange(AbsTask taskInfo);

    /**
     * 上传失败
     *
     * @param taskInfo 上传失败的帖子信息
     */
    void onTaskFailed(AbsTask taskInfo, Throwable e);

    /**
     * 上传成功
     *
     * @param taskInfo
     * @param response
     */
    void onTaskSuccessful(AbsTask taskInfo, Object response);

    /**
     * 删除的回调
     *
     * @param taskInfo
     */
    void onTaskDelete(AbsTask taskInfo);

    /**
     * 取消上传的回调
     *
     * @param taskInfo
     */
    void onTaskCancel(AbsTask taskInfo);

    void onFileUploadStart(AbsTask taskInfo, SingleFileInfo fileInfo);

    void onFileUploadProgressChange(AbsTask taskInfo, SingleFileInfo fileInfo);

    void onFileUploadFailed(AbsTask taskInfo, SingleFileInfo fileInfo, Throwable e);

    void onFileUploadSuccess(AbsTask taskInfo, SingleFileInfo fileInfo);
}