package cn.longmaster.hospital.doctor.core.personalmaterial;

import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.PersonDataUploader;
import cn.longmaster.hospital.doctor.core.requests.user.SubmissionPersonalDataRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.upload.NginxUploadTask;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/8/2.
 */

public class PersonalMaterialUploadTask extends Thread implements OnNginxUploadStateCallback {
    private final String TAG = PersonalMaterialUploadTask.class.getSimpleName();
    private MaterialUploadTaskStateChangeListener mListener;
    private PersonalMaterialInfo mTaskInfo;
    private int mPreProgress = 0;
    private NginxUploadTask mNginxUploadTask;

    public PersonalMaterialUploadTask(PersonalMaterialInfo taskInfo, PersonalMaterialUploadTask.MaterialUploadTaskStateChangeListener listener) {
        mTaskInfo = taskInfo;
        mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        startTask();
        Looper.loop();
    }

    void startTask() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->start()->mTaskInfo:" + mTaskInfo + ",nn:" + (mTaskInfo.getUploadProgress() == 100) + ",ss" + !StringUtils.isEmpty(mTaskInfo.getSvrFileName()));
        if (mTaskInfo.getUploadProgress() == 100 && !StringUtils.isEmpty(mTaskInfo.getSvrFileName())) {
            mListener.onMaterialFileUploadStart(mTaskInfo);
            sendUploadRequester();
        } else {
            uploadFile();
        }
    }

    private void uploadFile() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->文件名:" + mTaskInfo);
        File file = new File(mTaskInfo.getLocalFileName());
        if (!file.exists()) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->uploadFile()->文件不存在!文件路径：" + mTaskInfo.getLocalFileName());
            mTaskInfo.setUploadState(UploadState.UPLOAD_FAILED);
            mListener.onMaterialFileUploadFaild(mTaskInfo);
            return;
        }
        String ext = "";
        if (mTaskInfo.getFileType() == 1) {
            ext = "pdf";
        } else if (mTaskInfo.getFileType() == 3) {
            ext = "ppt";
        }
        PersonDataUploader request = new PersonDataUploader(new OnResultListener<String>() {
            @Override
            public void onResult(BaseResult baseResult, String s) {

            }
        });
        request.actType = 1;
        request.filePath = mTaskInfo.getLocalFileName();
        request.ext = ext;
        String url = request.getCompleteUrl();
        Logger.logD(Logger.APPOINTMENT, TAG + "->url()：" + url + ",LocalFileName:" + mTaskInfo.getLocalFileName());
        mNginxUploadTask = new NginxUploadTask(url, mTaskInfo.getLocalFileName(), mTaskInfo.getTaskId(), this);
        mNginxUploadTask.startUpload();
        mTaskInfo.setUploadState(UploadState.UPLOADING);
        mListener.onMaterialFileUploadStart(mTaskInfo);
    }

    private void sendUploadRequester() {
        Logger.logI(Logger.COMMON, "SubmissionPersonalDataRequester：sendUploadRequester：" + mTaskInfo.getUploadProgress());
        AppHandlerProxy.post(() -> {
            SubmissionPersonalDataRequester requester = new SubmissionPersonalDataRequester((baseResult, aVoid) -> {
                Logger.logI(Logger.COMMON, "SubmissionPersonalDataRequester：baseResult：" + baseResult);
                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                    mTaskInfo.setUploadState(UploadState.UPLOAD_SUCCESS);
                    mListener.onMaterialFileUploadSuccess(mTaskInfo);
                } else if (baseResult.getCode() == 102) {
                    mTaskInfo.setUploadState(UploadState.UPLOAD_SUCCESS);
                    mListener.onMaterialFileBindingRepetition(mTaskInfo);
                } else {
                    mTaskInfo.setUploadState(UploadState.UPLOAD_FAILED);
                    mListener.onMaterialFileUploadFaild(mTaskInfo);
                }
            });
            requester.fileName = mTaskInfo.getSvrFileName();
            requester.materialName = mTaskInfo.getMaterialName();
            requester.fileType = mTaskInfo.getFileType();
            requester.doctorId = mTaskInfo.getDoctorId();
            requester.doPost();
        });
    }

    public void cancelUploadTask() {
        if (mNginxUploadTask != null) {
            mNginxUploadTask.cancle();
        }
        interrupt();
    }

    @Override
    public void onUploadProgresssChange(String s, long l, long l1, long l2) {
        float progress = (float) l / l2 * 100;
        if (Math.abs(mPreProgress - (int) progress) > 1 || progress >= 99) {
            mPreProgress = (int) progress;
            if (mTaskInfo != null) {
                mTaskInfo.setUploadProgress(mPreProgress);
                mListener.onMaterialFileProgressChange(mTaskInfo);
            }
        }
    }

    @Override
    public void onUploadComplete(String s, int i, String s1) {
        Logger.logD(Logger.APPOINTMENT, TAG + "onUploadComplete()->s1:" + s1);
        try {
            JSONObject jsonObject = new JSONObject(s1);
            int state = jsonObject.optInt("code", -1);
            if (state == 0) {
                String fileName = jsonObject.optString("file_name");
                if (!StringUtils.isEmpty(fileName)) {
                    mTaskInfo.setSvrFileName(fileName);
                    mTaskInfo.setUploadProgress(100);
                    mListener.onMaterialFileUploadComplete(mTaskInfo);
                    sendUploadRequester();
                } else {
                    mTaskInfo.setUploadState(UploadState.UPLOAD_FAILED);
                    mListener.onMaterialFileUploadFaild(mTaskInfo);
                }
            } else {
                String error = jsonObject.optString("error");
                Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadComplete()->error:" + error);
                mTaskInfo.setUploadState(UploadState.UPLOAD_FAILED);
                mListener.onMaterialFileUploadFaild(mTaskInfo);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onUploadCancle(String s) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadCancle()->sessionId:" + s);
        if (mTaskInfo != null) {
            mTaskInfo.setUploadState(UploadState.UPLOAD_PAUSE);
            mListener.onMaterialFileUploadCancle(mTaskInfo);
        }
    }

    @Override
    public void onUploadException(String s, Exception e) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadException()->sessionId:" + e);
        e.printStackTrace();
        if (mTaskInfo.getUploadState() == UploadState.UPLOAD_SUCCESS) {
            return;
        }
        if (mTaskInfo != null) {
            mTaskInfo.setUploadState(UploadState.UPLOAD_FAILED);
            mListener.onMaterialFileException(mTaskInfo, e);
        }
    }

    public interface MaterialUploadTaskStateChangeListener {

        void onMaterialFileUploadStart(PersonalMaterialInfo taskInfo);

        void onMaterialFileUploadSuccess(PersonalMaterialInfo taskInfo);

        void onMaterialFileUploadFaild(PersonalMaterialInfo taskInfo);

        void onMaterialFileBindingRepetition(PersonalMaterialInfo taskInfo);

        void onMaterialFileUploadCancle(PersonalMaterialInfo taskInfo);

        void onMaterialFileProgressChange(PersonalMaterialInfo taskInfo);

        void onMaterialFileUploadComplete(PersonalMaterialInfo taskInfo);

        void onMaterialFileException(PersonalMaterialInfo taskInfo, Exception exception);
    }
}
