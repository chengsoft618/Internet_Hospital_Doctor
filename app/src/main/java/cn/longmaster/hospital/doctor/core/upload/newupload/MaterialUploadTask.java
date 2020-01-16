package cn.longmaster.hospital.doctor.core.upload.newupload;

import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadFileMaterialRequest;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadMaterialMakeSureRequest;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsUploadFileMaterialRequest;
import cn.longmaster.hospital.doctor.core.upload.UploadTaskManager;
import cn.longmaster.hospital.doctor.core.upload.UploadUtils;
import cn.longmaster.upload.NginxUploadTask;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * Created by YY on 17/9/25.
 */

public class MaterialUploadTask extends Thread implements OnNginxUploadStateCallback {
    private final String TAG = MaterialUploadTask.class.getSimpleName();
    private NginxUploadTask mNginxUploadTask;
    private MaterialUploadTaskStateChangeListener mListener;
    private MaterialTaskInfo mTaskInfo;
    private List<MaterialFileInfo> mMaterialFileInfos;
    private AtomicBoolean mIsCancel = new AtomicBoolean(false);
    private int mPreProgress;
    private long mLastUpdateProgressTime;

    public MaterialUploadTask(MaterialTaskInfo taskInfo, MaterialUploadTaskStateChangeListener listener) {
        mTaskInfo = taskInfo;
        mMaterialFileInfos = mTaskInfo.getMaterialFileInfos();
        mListener = listener;
    }

    public MaterialTaskInfo getTaskInfo() {
        return mTaskInfo;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        startTask();
        Looper.loop();
    }

    /*package*/ void startTask() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->start()->mTaskInfo:" + mTaskInfo + ", mMaterialFileInfos:" + mMaterialFileInfos);
        if (mTaskInfo == null || LibCollections.isEmpty(mMaterialFileInfos)) {
            mListener.onMaterialUploadTaskFinish(mTaskInfo);
            return;
        }

        if (mIsCancel.get()) {
            Logger.logW(Logger.APPOINTMENT, TAG + "->start()->任务已经取消!");
            return;
        }

        MaterialFileInfo materialFileInfo = null;
        for (MaterialFileInfo fileInfo : mMaterialFileInfos) {
            if (fileInfo.getState() == UploadState.NOT_UPLOADED) {
                if (fileInfo.getProgress() == 100 && !StringUtils.isEmpty(fileInfo.getServerFileName())) {
                    mListener.onMaterialFileUploadStart(mTaskInfo, fileInfo, false);
                    sendUploadRequester(fileInfo);
                    return;
                }
                materialFileInfo = fileInfo;
                break;
            }
        }

        if (materialFileInfo == null) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->start()->没有文件需要上传!");
            mListener.onMaterialUploadTaskFinish(mTaskInfo);
        } else {
            uploadFile(materialFileInfo);
        }
    }

    /*package*/ void restart() {
        mIsCancel.set(false);
        for (MaterialFileInfo fileInfo : mMaterialFileInfos) {
            if (fileInfo.getState() == UploadState.UPLOAD_PAUSE) {
                fileInfo.setState(UploadState.NOT_UPLOADED);
            }
        }
        startTask();
    }

    private void uploadFile(final MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->文件名:" + fileInfo);
        if (mIsCancel.get()) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->任务已取消!");
            return;
        }

        File file = new File(fileInfo.getLocalFilePath());
        if (!file.exists()) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->文件不存在!文件路径：" + fileInfo.getLocalFilePath());
            fileInfo.setState(UploadState.UPLOAD_FAILED);
            mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
            startTask();
            return;
        }

        try {
            if (mTaskInfo.getMaterialFileInfos().indexOf(fileInfo) > 0) {//连续上传多个文件，暂停2秒
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            boolean needUpdateFileInfo = false;
            Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->getLocalFilePath:" + fileInfo.getLocalFilePath() + ",SdManager.getOrderDirPath():" + SdManager.getInstance().getOrderDirPath());
            if (!fileInfo.getLocalFilePath().contains(SdManager.getInstance().getOrderDirPath())) {
                needUpdateFileInfo = true;
                String newFileName = MD5Util.md5(System.currentTimeMillis() + "" + Math.random() * 1000) + ".jpg";
                String newFilePath = SdManager.getInstance().getOrderPicPath(newFileName, fileInfo.getAppointmentId() + "");
                String extraName = fileInfo.getFilePostfix();
                Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->文件后缀名:" + extraName);
                //需要将png或者jpeg格式的图片保存为jpg格式,不然服务器无法生成缩略图
                if (extraName.matches("[pP][nN][gG]")
                        || extraName.matches("[jJ][pP][eE][gG]")
                        || extraName.matches("[jJ][pP][gG]")) {
                    boolean isSaveSuccess = true;
                    try {
                        BitmapUtil.savePng2JpegWithException(fileInfo.getLocalFilePath(), newFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        isSaveSuccess = false;
                        /*mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                        Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->保存文件失败");
                        return;*/
                    } finally {
                        if (!isSaveSuccess) {
                            // mTaskInfo.setState(UploadState.UPLOAD_FAILED);
                            mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                            Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->保存文件失败");
                        }
                    }
                } else {
                    try {
                        FileUtil.copyFileWithException(fileInfo.getLocalFilePath(), newFilePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                        return;
                    }
                }
                //如果图片来自相机拍摄，删除原拍摄图片
                if (fileInfo.isDelete()) {
                    FileUtil.deleteFile(fileInfo.getLocalFilePath());
                }
                //图片压缩
                try {
                    BitmapUtil.compressImageFileWithException(newFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                    return;
                }
                //删除缓存

                fileInfo.setFilePostfix("jpg");
                fileInfo.setLocalFileName(newFileName);
                fileInfo.setLocalFilePath(newFilePath);
            }

            if (mIsCancel.get()) {
                return;
            }
            UploadFileMaterialRequest request = new UploadFileMaterialRequest(new OnResultListener<String>() {
                @Override
                public void onResult(BaseResult baseResult, String s) {
                    Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()-UploadFileMaterialRequest--》>baseResult:" + baseResult + ", s:" + s);
                }
            });
            request.ext = fileInfo.getFilePostfix();
            request.fileName = fileInfo.getLocalFileName();
            request.appointmentId = fileInfo.getAppointmentId();
            String url = request.getCompleteUrl();
            Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->url:" + url + ",LocalFilePath： " + fileInfo.getLocalFilePath());
            mNginxUploadTask = new NginxUploadTask(url, fileInfo.getLocalFilePath(), fileInfo.getSessionId(), this);
            mNginxUploadTask.startUpload();

            fileInfo.setState(UploadState.UPLOADING);
            mListener.onMaterialFileUploadStart(mTaskInfo, fileInfo, needUpdateFileInfo);
        } catch (IllegalArgumentException e) {
            mListener.onMaterialFileException(mTaskInfo, fileInfo, e);
        }
    }

    public void cancelUploadTask() {
        if (mNginxUploadTask != null) {
            mNginxUploadTask.cancle();
        }
        for (MaterialFileInfo fileInfo : mMaterialFileInfos) {
            if (fileInfo.getState() == UploadState.NOT_UPLOADED) {
                fileInfo.setState(UploadState.UPLOAD_PAUSE);
            }
        }
        mIsCancel.set(true);
        interrupt();
    }

    private MaterialFileInfo getMaterialFileInfo(String sessionId) {
        for (MaterialFileInfo fileInfo : mTaskInfo.getMaterialFileInfos()) {
            if (fileInfo.getSessionId().equals(sessionId)) {
                return fileInfo;
            }
        }
        return null;
    }

    @Override
    public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {
        float progress = (float) (successBytes + blockByte) / totalBytes * 100;
//        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadProgresssChange()->progress:" + progress);
        if (Math.abs(mPreProgress - (int) progress) > 1 || progress >= 99) {
            mPreProgress = (int) progress;
            MaterialFileInfo fileInfo = getMaterialFileInfo(sessionId);
            if (fileInfo != null) {
                fileInfo.setProgress(mPreProgress);
                if (System.currentTimeMillis() - mLastUpdateProgressTime >= 200) {//避免更新界面太快
                    mLastUpdateProgressTime = System.currentTimeMillis();
                    mListener.onMaterialFileProgressChange(mTaskInfo, fileInfo);
                }
            }
        }
    }

    @Override
    public void onUploadException(String sessionId, Exception exception) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadException()->sessionId:" + sessionId);
        exception.printStackTrace();
        MaterialFileInfo fileInfo = getMaterialFileInfo(sessionId);
        if (fileInfo != null) {
            fileInfo.setState(UploadState.UPLOAD_FAILED);
            mListener.onMaterialFileException(mTaskInfo, fileInfo, exception);
        }
        startTask();
    }

    @Override
    public void onUploadCancle(String sessionId) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadCancle()->sessionId:" + sessionId);
        MaterialFileInfo fileInfo = getMaterialFileInfo(sessionId);
        if (fileInfo != null) {
            fileInfo.setState(UploadState.UPLOAD_PAUSE);
            mListener.onMaterialFileUploadCancle(mTaskInfo, fileInfo);
        }
    }

    @Override
    public void onUploadComplete(String sessionId, int code, String content) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadComplete()->sessionId:" + sessionId + ", code:" + code + ", content:" + content);
        MaterialFileInfo fileInfo = getMaterialFileInfo(sessionId);
        if (code == HttpURLConnection.HTTP_OK) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                int state = jsonObject.optInt(UploadTaskManager.CODE, UploadTaskManager.CODE_ERROR);
                if (state == UploadTaskManager.CODE_SUCCESS) {
                    if (mIsCancel.get()) {
                        onUploadCancle(sessionId);
                    } else {
                        String fileName = jsonObject.optString("file_name");
                        if (!StringUtils.isEmpty(fileName)) {
                            fileInfo.setServerFileName(fileName);
                            fileInfo.setProgress(100);
                            sendUploadRequester(fileInfo);
                        } else {
                            fileInfo.setState(UploadState.UPLOAD_FAILED);
                            mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                        }
                    }
                } else {
                    String error = jsonObject.optString("error");
                    Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadComplete()->error:" + error);
                    if (StringUtils.equals(error, "未找到文件流")) {
                        String newFileName = MD5Util.md5(System.currentTimeMillis() + "" + Math.random() * 1000) + ".jpg";
                        String newFilePath = SdManager.getInstance().getOrderPicPath(newFileName, fileInfo.getAppointmentId() + "");
                        FileUtil.copyFile(fileInfo.getLocalFilePath(), newFilePath);
                        fileInfo.setLocalFileName(newFileName);
                        fileInfo.setLocalFilePath(newFilePath);
                        fileInfo.setNewSessionId(UploadUtils.applySessionId(fileInfo));
                        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadComplete()->未找到文件流->拷贝生成新文件:" + fileInfo);
                    }
                    fileInfo.setState(UploadState.UPLOAD_FAILED);
                    mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                    startTask();
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        } else {
            fileInfo.setState(UploadState.UPLOAD_FAILED);
            mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
            startTask();
        }
    }

    private void sendUploadRequester(final MaterialFileInfo fileInfo) {
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                if (fileInfo.getAppointmentId() >= 500000) {
                    RoundsUploadFileMaterialRequest request = new RoundsUploadFileMaterialRequest(new OnResultListener<String>() {
                        @Override
                        public void onResult(BaseResult baseResult, String s) {
                            Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialFileUploadSuccess()->baseResult:" + baseResult + ", s:" + s);
                            if (baseResult.getCode() == RESULT_SUCCESS) {
                                fileInfo.setState(UploadState.UPLOAD_SUCCESS);
                                mListener.onMaterialFileUploadSuccess(mTaskInfo, fileInfo);
                            } else {
                                fileInfo.setState(UploadState.UPLOAD_FAILED);
                                mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                            }
                            startTask();
                        }
                    });
                    request.fileName = fileInfo.getServerFileName();
                    request.medicalId = mTaskInfo.getAppointmentId();
                    request.doPost();
                } else {
                    UploadMaterialMakeSureRequest request = new UploadMaterialMakeSureRequest(new OnResultListener<Integer>() {
                        @Override
                        public void onResult(BaseResult baseResult, Integer integer) {
                            if (baseResult.getCode() == RESULT_SUCCESS) {
                                fileInfo.setState(UploadState.UPLOAD_SUCCESS);
                                mListener.onMaterialFileUploadSuccess(mTaskInfo, fileInfo);
                            } else {
                                fileInfo.setState(UploadState.UPLOAD_FAILED);
                                mListener.onMaterialFileUploadFaild(mTaskInfo, fileInfo);
                            }
                            startTask();
                        }
                    });
                    request.appointmentId = mTaskInfo.getAppointmentId();
                    request.checkState = 0;
                    request.uploadType = 0;
                    request.materialPic = fileInfo.getServerFileName();
                    request.userId = mTaskInfo.getDoctorId();
                    request.doPost();
                }
            }
        });
    }

    public interface MaterialUploadTaskStateChangeListener {
        void onMaterialUploadTaskFinish(MaterialTaskInfo taskInfo);

        void onMaterialFileUploadStart(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo, boolean updateFileInfo);

        void onMaterialFileUploadSuccess(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);

        void onMaterialFileUploadFaild(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);

        void onMaterialFileUploadCancle(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);

        void onMaterialFileProgressChange(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);

        void onMaterialFileException(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo, Exception exception);
    }
}
