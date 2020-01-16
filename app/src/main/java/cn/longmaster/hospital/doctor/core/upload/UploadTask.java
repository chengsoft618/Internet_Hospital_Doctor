package cn.longmaster.hospital.doctor.core.upload;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.upload.NginxUploadTask;
import cn.longmaster.upload.OnNginxUploadStateCallback;

/**
 * 上传文件task
 * (上传文件-->拿到服务器返回的文件名称-->再去调相关的业务接口)
 */
public class UploadTask implements OnNginxUploadStateCallback {

    private AbsTask mTaskInfo;
    private UploadTaskStateListener mListener;

    private NginxUploadTask mNginxUploadTask;
    private AtomicBoolean mIsCancel = new AtomicBoolean(false);

    // 上一次的进度
    private int mPreProgress;

    public UploadTask(AbsTask taskInfo, UploadTaskStateListener listener) {
        this.mTaskInfo = taskInfo;
        this.mListener = listener;
    }


    /**
     * 开始上传任务
     */
    /*package*/ void start() {
        log("start()->");
        SingleFileInfo uploadFile = null;
        for (SingleFileInfo file : mTaskInfo.getFileList()) {
            if (file == null) {
                continue;
            }
            if (file.getState() == TaskState.NOT_UPLOADED) {
                uploadFile = file;
                break;
            }
        }
        if (uploadFile != null) {
            uploadFile(uploadFile);
        } else {
            log("没有文件需要上传");
            mTaskInfo.allFileUploadSuccess(mListener);
        }
    }

    /**
     * 上传文件
     *
     * @param fileInfo SingleFileInfo
     */
    private void uploadFile(final SingleFileInfo fileInfo) {
        log("uploadFile()->" + (fileInfo == null ? "fileInfo is null" : fileInfo.toString()));
        if (fileInfo == null) {//异常，不处理
            return;
        }
        try {
            if (mTaskInfo.getFileList().indexOf(fileInfo) > 0) {//连续上传多个文件，暂停2秒
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String url = mTaskInfo.getUploadFileUrl(fileInfo);
            log(url);
            mNginxUploadTask = new NginxUploadTask(url, fileInfo.getLocalFilePath(), fileInfo.getSessionId(), this);
            mNginxUploadTask.startUpload();

            fileInfo.updateState(TaskState.UPLOADING);
            if (mListener != null) {
                mListener.onFileUploadStart(mTaskInfo, fileInfo);
            }
        } catch (IllegalArgumentException e) {
            if (mListener != null) {
                mListener.onTaskFinished(mTaskInfo, UploadTaskManager.CODE_ERROR, e);
            }
        }
    }

    /**
     * 取消任务上传
     */
    public void cancelUploadTask() {
        if (mNginxUploadTask != null) {
            mNginxUploadTask.cancle();
        }
        mIsCancel.set(true);
    }

    /**
     * 上传进度回调
     */
    @Override
    public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {
        float progressLength = (float) (successBytes + blockByte) / totalBytes * 100;
        log("onUploadProgresssChange()->" + progressLength);
        if (Math.abs(mPreProgress - (int) progressLength) > 1 || progressLength >= 99) {
            mPreProgress = (int) progressLength;
            SingleFileInfo fileInfo = mTaskInfo.getFileInfoById(sessionId);
            if (fileInfo != null) {
                fileInfo.setProgress(mPreProgress);
                if (System.currentTimeMillis() - fileInfo.getLastUpdateProgressTime() >= 200) {//避免更新界面太快
                    fileInfo.updateDB();
                    mListener.onFileUploadProgressChange(mTaskInfo, fileInfo);
                }
            }
        }
    }

    /**
     * 发帖产生异常
     */
    @Override
    public void onUploadException(String sessionId, Exception exception) {
        log("onUploadException()->");
        exception.printStackTrace();
        SingleFileInfo fileInfo = mTaskInfo.getFileInfoById(sessionId);
        if (fileInfo != null) {
            boolean isContinue = mTaskInfo.dealFileUploadFailed(sessionId, exception, mListener);
            if (isContinue) {
                start();//上传下一个文件
            }
        }
    }

    /**
     * 上传取消的回调
     */
    @Override
    public void onUploadCancle(String sessionId) {
        log("onUploadCancle()->" + sessionId);
        SingleFileInfo fileInfo = mTaskInfo.getFileInfoById(sessionId);
        if (fileInfo != null) {
            fileInfo.updateState(TaskState.UPLOAD_FAILED);
            if (mListener != null) {
                mListener.onFileUploadCancel(mTaskInfo, mTaskInfo.getFileInfoById(sessionId));
            }
        }
    }

    /**
     * 上传完成的回调
     */
    @Override
    public void onUploadComplete(String sessionId, int code, String content) {
        log("onUploadComplete() code->" + code + "->content->:" + content);
        Exception e = null;
        if (code == HttpURLConnection.HTTP_OK) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                int state = jsonObject.optInt(UploadTaskManager.CODE, UploadTaskManager.CODE_ERROR);
                if (state == UploadTaskManager.CODE_SUCCESS) {
                    if (mIsCancel.get()) {
                        onUploadCancle(sessionId);
                        //mListener.onFileUploadCancel(mTaskInfo, mTaskInfo.getFileInfoById(sessionId));
                    } else {
                        String file_name = jsonObject.optString("file_name");
                        boolean success = mTaskInfo.isFileUploadSuccess(sessionId, file_name, mListener);
                        if (success) {
                            start();//上传下一个文件
                        }
                    }
                    return;
                }
                String err = jsonObject.optString("notice");
                e = new Exception(err);
            } catch (JSONException e2) {
                e = e2;
            }
        }
        if (e != null) {
            e.printStackTrace();
        }
        boolean isContinue = mTaskInfo.dealFileUploadFailed(sessionId, e, mListener);
        if (isContinue) {
            start();//上传下一个文件
        }
    }

    private void log(String msg) {
        Logger.logD(Logger.COMMON, msg);
    }
}
