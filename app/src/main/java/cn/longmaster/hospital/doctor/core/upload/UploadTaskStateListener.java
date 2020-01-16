package cn.longmaster.hospital.doctor.core.upload;


public interface UploadTaskStateListener {
    /**
     * 上传单个文件开始
     *
     * @param task
     * @param file
     */
    void onFileUploadStart(AbsTask task, SingleFileInfo file);

    /**
     * 取消
     *
     * @param task
     * @param file
     */
    void onFileUploadCancel(AbsTask task, SingleFileInfo file);

    /**
     * @param task
     * @param file
     */
    void onFileUploadProgressChange(AbsTask task, SingleFileInfo file);

    /**
     * @param task
     * @param file
     * @param exception
     */
    void onFileUploadException(AbsTask task, SingleFileInfo file, Exception exception);

    /**
     * 任务完成
     *
     * @param task
     * @param file
     * @param throwable
     */
    void onFileUploadComplete(AbsTask task, SingleFileInfo file, Throwable throwable);

    /**
     * 任务完成（成功或失败）
     *
     * @param task
     * @param code
     * @param response
     */
    void onTaskFinished(AbsTask task, int code, final Object response);
}
