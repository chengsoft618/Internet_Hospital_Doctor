package cn.longmaster.hospital.doctor.core.upload.newupload;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialFileFlagContract;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskContract;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskFileContract;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskResultContract;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.upload.UploadUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;

/**
 * 上传管理类
 * Created by YY on 17/9/22.
 */

public class MaterialTaskManager extends BaseManager implements MaterialUploadTask.MaterialUploadTaskStateChangeListener, NetStateReceiver.NetworkStateChangedListener {
    private final String TAG = MaterialTaskManager.class.getSimpleName();
    private static final int UPLOAD_QUEUE_MAX_COUNT = 2;

    private AppApplication mAppApplication;
    private DBManager mDBManager;
    private UserInfoManager mUserInfoManager;
    private List<MaterialTaskInfo> mMaterialTaskInfos = Collections.synchronizedList(new ArrayList<MaterialTaskInfo>());
    private Map<String, MaterialUploadTask> mUploadTaskQueue = new HashMap();
    private List<MaterialTaskResultInfo> mMaterialTaskResultInfos = Collections.synchronizedList(new ArrayList<MaterialTaskResultInfo>());
    private List<UploadStateChangeListener> mUploadTaskStateChangeListeners = new ArrayList<>();
    private List<MaterialTaskResultStateChangeListener> mMaterialTaskResultStateChangeListeners = new ArrayList<>();

    @Override
    public void onManagerCreate(AppApplication application) {
        mAppApplication = application;
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
        mDBManager = mAppApplication.getManager(DBManager.class);
        mUserInfoManager = mAppApplication.getManager(UserInfoManager.class);
        NetStateReceiver.setOnNetworkStateChangedListener(this);
    }

    /**
     * 重置
     */
    public void reset() {
        mMaterialTaskInfos.clear();
        mMaterialTaskResultInfos.clear();
        for (Map.Entry<String, MaterialUploadTask> entry : mUploadTaskQueue.entrySet()) {
            entry.getValue().cancelUploadTask();
        }
        mUploadTaskQueue.clear();
    }

    /**
     * 注册上传状态变化监听
     *
     * @param listener   监听器
     * @param isRegister 是否注册 true注册 false注销
     */
    public void registerUploadStateChangeListener(UploadStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mUploadTaskStateChangeListeners.add(listener);
        } else {
            mUploadTaskStateChangeListeners.remove(listener);
        }
    }

    /**
     * 注册任务结果状态变化回调
     *
     * @param listener   回调接口
     * @param isRegister 是否注册 true注册 false注销
     */
    public void registerMaterialTaskResultStateChangeListener(MaterialTaskResultStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mMaterialTaskResultStateChangeListeners.add(listener);
        } else {
            mMaterialTaskResultStateChangeListeners.remove(listener);
        }
    }

    /**
     * 加载本地任务
     */
    public void uploadLocalTasks() {
        if (mUserInfoManager.getCurrentUserInfo().getUserId() == 0) {
            Logger.logW(Logger.APPOINTMENT, TAG + "->uploadLocalTasks()->未登录！");
            return;
        }

        getAllMaterialTaskReuslts(new GetAllMaterialTaskResultCallback() {
            @Override
            public void onGetAllMaterialTaskResultStateChanged(List<MaterialTaskResultInfo> taskResultInfos) {
                Logger.logD(Logger.APPOINTMENT, TAG + "->uploadLocalTasks()->taskResultInfos:" + taskResultInfos);
                mMaterialTaskResultInfos.addAll(taskResultInfos);

                getAllMaterialTask(new GetAllMaterialTaskCallback() {
                    @Override
                    public void onGetAllMaterialTaskStateChanged(List<MaterialTaskInfo> materialTaskInfos) {
                        for (MaterialTaskInfo taskInfo : materialTaskInfos) {
                            if (taskInfo.getState() == UploadState.UPLOADING
                                    || taskInfo.getState() == UploadState.UPLOAD_FAILED) {
                                taskInfo.setState(UploadState.NOT_UPLOADED);
                                updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
                                for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                                    if (fileInfo.getState() == UploadState.UPLOADING
                                            || fileInfo.getState() == UploadState.UPLOAD_FAILED) {
                                        fileInfo.setState(UploadState.NOT_UPLOADED);
                                        updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                                        for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                                            listener.onFileUploadStateChanged(taskInfo, fileInfo);
                                        }
                                    }
                                }
                            }
                        }
                        mMaterialTaskInfos.addAll(materialTaskInfos);
                        Logger.logD(Logger.APPOINTMENT, TAG + "->uploadLocalTasks()->mMaterialTaskInfos:" + mMaterialTaskInfos);
                        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
                            if (taskInfo.getState() == UploadState.NOT_UPLOADED) {
                                executeNewTask(taskInfo);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取所有资料任务列表
     *
     * @return 资料任务列表
     */
    public List<MaterialTaskInfo> getAllMaterialTasks() {
        return mMaterialTaskInfos;
    }

    /**
     * 暂停所有任务
     */
    public void pauseAllTasks() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->pauseAllTasks()");
        SPUtils.getInstance().put(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), true);

        for (Map.Entry<String, MaterialUploadTask> entry : mUploadTaskQueue.entrySet()) {
            entry.getValue().cancelUploadTask();
        }
        mUploadTaskQueue.clear();

        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
            if (taskInfo.getState() == UploadState.UPLOADING
                    || taskInfo.getState() == UploadState.NOT_UPLOADED) {
                taskInfo.setState(UploadState.UPLOAD_PAUSE);
                updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
                for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                    if (fileInfo.getState() == UploadState.UPLOADING
                            || fileInfo.getState() == UploadState.NOT_UPLOADED) {
                        fileInfo.setState(UploadState.UPLOAD_PAUSE);
                        updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                    }
                }
            }
        }
    }

    /**
     * 恢复所有任务
     */
    public void recoverAllTasks() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->recoverAllTasks()");
        SPUtils.getInstance().put(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false);
        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
            if (taskInfo.getState() == UploadState.UPLOAD_PAUSE) {
                taskInfo.setState(UploadState.NOT_UPLOADED);
                updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
                for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                    if (fileInfo.getState() == UploadState.UPLOAD_PAUSE) {
                        fileInfo.setState(UploadState.NOT_UPLOADED);
                        updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                    }
                }
                executeNewTask(taskInfo);
            }
        }
    }

    /**
     * 清空所有任务
     */
    public void clearAllTasks() {
        List<MaterialTaskInfo> successMaterialTaskInfos = new ArrayList<>();
        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
            if (taskInfo.getState() == UploadState.UPLOAD_SUCCESS) {
                successMaterialTaskInfos.add(taskInfo);
            }
        }
        for (MaterialTaskInfo taskInfo : successMaterialTaskInfos) {
            mMaterialTaskInfos.remove(taskInfo);
            deleteMaterialTasksAndMaterialFiles(taskInfo.getTaskId());
        }
    }

    /**
     * 暂停单个任务
     *
     * @param taskId 任务id
     */
    public void pauseTask(String taskId) {
        if (mUploadTaskQueue.containsKey(taskId)) {
            mUploadTaskQueue.get(taskId).cancelUploadTask();
            mUploadTaskQueue.remove(taskId);
        }

        MaterialTaskInfo materialTaskInfo = getMaterialTaskInfo(taskId);
        if (materialTaskInfo != null) {
            materialTaskInfo.setState(UploadState.UPLOAD_PAUSE);
            updateMaterialTaskState(materialTaskInfo.getTaskId(), materialTaskInfo.getState());
            for (MaterialFileInfo fileInfo : materialTaskInfo.getMaterialFileInfos()) {
                MaterialFileInfo materialFileInfo = getMaterialFileInfo(materialTaskInfo, fileInfo.getSessionId());
                if (materialFileInfo.getState() == UploadState.NOT_UPLOADED) {
                    materialFileInfo.setState(UploadState.UPLOAD_PAUSE);
                    updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                }
            }
        }
        startNextTask();
    }

    /**
     * 恢复单个任务
     *
     * @param taskId 任务id
     */
    public void recoverTask(String taskId) {
        MaterialTaskInfo materialTaskInfo = getMaterialTaskInfo(taskId);
        if (materialTaskInfo != null) {
            materialTaskInfo.setState(UploadState.NOT_UPLOADED);
            updateMaterialTaskState(materialTaskInfo.getTaskId(), materialTaskInfo.getState());
            for (MaterialFileInfo fileInfo : materialTaskInfo.getMaterialFileInfos()) {
                MaterialFileInfo materialFileInfo = getMaterialFileInfo(materialTaskInfo, fileInfo.getSessionId());
                if (materialFileInfo.getState() == UploadState.UPLOAD_PAUSE) {
                    materialFileInfo.setState(UploadState.NOT_UPLOADED);
                    updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                }
            }
            executeNewTask(materialTaskInfo);
        }
    }

    /**
     * 重传任务
     *
     * @param taskId 任务id
     */
    public void retryTask(String taskId) {
        MaterialTaskInfo materialTaskInfo = getMaterialTaskInfo(taskId);
        if (materialTaskInfo != null) {
            materialTaskInfo.setState(UploadState.NOT_UPLOADED);
            updateMaterialTaskState(materialTaskInfo.getTaskId(), materialTaskInfo.getState());
            for (MaterialFileInfo fileInfo : materialTaskInfo.getMaterialFileInfos()) {
                MaterialFileInfo materialFileInfo = getMaterialFileInfo(materialTaskInfo, fileInfo.getSessionId());
                if (materialFileInfo.getState() == UploadState.UPLOAD_FAILED) {
                    materialFileInfo.setState(UploadState.NOT_UPLOADED);
                    updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                    for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                        listener.onFileUploadStateChanged(materialTaskInfo, fileInfo);
                    }
                }
            }
            executeNewTask(materialTaskInfo);
        }
    }

    /**
     * 重传任务
     *
     * @param appointmentId 预约id
     */
    public void retryTask(int appointmentId) {
        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
            if (taskInfo.getAppointmentId() == appointmentId
                    && taskInfo.getState() == UploadState.UPLOAD_FAILED) {
                taskInfo.setState(UploadState.NOT_UPLOADED);
                updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
                for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                    if (fileInfo.getState() == UploadState.UPLOAD_FAILED) {
                        fileInfo.setState(UploadState.NOT_UPLOADED);
                        updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
                    }
                }
                executeNewTask(taskInfo);
            }
        }
    }

    /**
     * 删除单个任务
     *
     * @param taskId 任务id
     */
    public void deleteTask(String taskId) {
        if (mUploadTaskQueue.containsKey(taskId)) {
            for (Map.Entry<String, MaterialUploadTask> entry : mUploadTaskQueue.entrySet()) {
                entry.getValue().cancelUploadTask();
            }
            mUploadTaskQueue.remove(taskId);
        }

        startNextTask();
        MaterialTaskInfo taskInfo = getMaterialTaskInfo(taskId);
        mMaterialTaskInfos.remove(taskInfo);
        deleteMaterialTasksAndMaterialFiles(taskId);

        if (LibCollections.isNotEmpty(taskInfo.getMaterialFileInfos())) {
            for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                if (fileInfo.getState() != UploadState.UPLOAD_SUCCESS) {
                    deleteMaterFileFlagInfo(fileInfo.getSessionId());
                }
            }
        }
    }

    /**
     * 获取所有材料任务结果
     *
     * @return 所有材料任务结果
     */
    public List<MaterialTaskResultInfo> getAllMaterialTaskResultInfos() {
        return mMaterialTaskResultInfos;
    }

    /**
     * 删除任务结果
     *
     * @param resultInfo 任务结果
     */
    public void deleteMaterialTaskResultInfo(MaterialTaskResultInfo resultInfo) {
        mMaterialTaskResultInfos.remove(resultInfo);
        deleteMaterialTaskResult(resultInfo);
    }

    /**
     * 清空上传结果
     */
    public void clearMaterialTaskResultInfo() {
        for (MaterialTaskResultInfo resultInfo : mMaterialTaskResultInfos) {
            deleteMaterialTaskResult(resultInfo);
        }
        mMaterialTaskResultInfos.clear();
    }

    /**
     * 开始上传
     *
     * @param appointmentId 预约id
     * @param filePaths     文件路径
     * @param isDelete      是否需要删除
     */
    public void startUpload(final int appointmentId, final List<String> filePaths, final boolean isDelete) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->filePaths:" + filePaths);
        if (null != filePaths) {
            for (String path : filePaths) {
                File file = new File(path);
                if (StringUtils.isEmpty(path) || !file.exists()) {
                    Logger.logW(Logger.APPOINTMENT, TAG + "->startUpload()->本地文件路径为空或文件不存在！");
                    continue;
                }
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                String extraName = path.substring(path.lastIndexOf(".") + 1);

                final MaterialTaskInfo materialTaskInfo = new MaterialTaskInfo();
                materialTaskInfo.setUserId(mUserInfoManager.getCurrentUserInfo().getUserId());
                materialTaskInfo.setAppointmentId(appointmentId);
                materialTaskInfo.setMaterialDt(String.valueOf(System.currentTimeMillis()));
                if (SPUtils.getInstance().getBoolean(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
                    materialTaskInfo.setState(UploadState.UPLOAD_PAUSE);
                } else {
                    materialTaskInfo.setState(UploadState.NOT_UPLOADED);
                }
                materialTaskInfo.setMaterialId(0);
                materialTaskInfo.setRecureNum(0);
                materialTaskInfo.setDoctorId(0);
                materialTaskInfo.setTaskId(UploadUtils.applyTaskId());

                MaterialFileInfo materialFileInfo = new MaterialFileInfo();
                materialFileInfo.setTaskId(materialTaskInfo.getTaskId());
                if (SPUtils.getInstance().getBoolean(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
                    materialFileInfo.setState(UploadState.UPLOAD_PAUSE);
                } else {
                    materialFileInfo.setState(UploadState.NOT_UPLOADED);
                }
                materialFileInfo.setLocalFilePath(path);
                materialFileInfo.setLocalFileName(fileName);
                materialFileInfo.setFilePostfix(extraName);
                materialFileInfo.setMaterialDt(String.valueOf(System.currentTimeMillis()));
                materialFileInfo.setProgress(0);
                materialFileInfo.setServerFileName("");
                materialFileInfo.setAppointmentId(appointmentId);
                materialFileInfo.setSessionId(UploadUtils.applySessionId(materialFileInfo));
                materialFileInfo.setDelete(isDelete);
                List<MaterialFileInfo> materialFileInfos = new ArrayList<>();
                materialFileInfos.add(materialFileInfo);
                materialTaskInfo.setMaterialFileInfos(materialFileInfos);
                addNewTask(materialTaskInfo);
            }
        }
    }

    /**
     * 添加新任务
     *
     * @param taskInfo 任务信息
     */
    public void addNewTask(MaterialTaskInfo taskInfo) {
        saveMaterialTask(taskInfo);
        for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
            saveMaterialFile(fileInfo);

            MaterialFileFlagInfo materialFileFlagInfo = new MaterialFileFlagInfo();
            materialFileFlagInfo.setAppointmentId(taskInfo.getAppointmentId());
            materialFileFlagInfo.setSessionId(fileInfo.getSessionId());
            materialFileFlagInfo.setLocalFilePath(fileInfo.getLocalFilePath());
            materialFileFlagInfo.setUploadState(fileInfo.getState());
            saveMaterialFileFlagInfo(materialFileFlagInfo);
        }
        mMaterialTaskInfos.add(taskInfo);
        for (MaterialTaskManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
            listener.onNewTask(taskInfo);
        }
        startNextTask();
    }

    /**
     * 开始下一个任务
     */
    private void startNextTask() {
        if (SPUtils.getInstance().getBoolean(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->startNextTask()->当前处于暂停状态！");
            return;
        }

        if (mUploadTaskQueue.size() >= UPLOAD_QUEUE_MAX_COUNT) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->startNextTask()->有上传任务正在执行！");
            return;
        }

        MaterialTaskInfo needStartTask = null;
        for (MaterialTaskInfo task : mMaterialTaskInfos) {
            if (task.getState() == UploadState.NOT_UPLOADED) {
                needStartTask = task;
                break;
            }
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->startNextTask()->needStartTask:" + needStartTask);
        if (needStartTask != null) {
            executeNewTask(needStartTask);
        }
    }

    /**
     * 执行新任务
     *
     * @param taskInfo 任务信息
     */
    private void executeNewTask(final MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->taskInfo:" + taskInfo);

        if (mUploadTaskQueue.size() >= UPLOAD_QUEUE_MAX_COUNT) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->执行任务达到上限！");
            return;
        }

        MaterialUploadTask materialUploadTask = new MaterialUploadTask(taskInfo, MaterialTaskManager.this);
        mUploadTaskQueue.put(taskInfo.getTaskId(), materialUploadTask);

        if (!NetStateReceiver.hasNetConnected(mAppApplication)) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->网络断开！");
            taskInfo.setState(UploadState.UPLOAD_FAILED);
            updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
            for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                fileInfo.setState(UploadState.UPLOAD_FAILED);
                handleMaterialFileUploadFailed(taskInfo, fileInfo);
            }
            handleMaterialTaskFinish(taskInfo);
            return;
        }

        taskInfo.setState(UploadState.NOT_UPLOADED);
        for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
            if (fileInfo.getState() != UploadState.UPLOAD_SUCCESS) {
                fileInfo.setState(UploadState.NOT_UPLOADED);
            }
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->after set state taskInfo:" + taskInfo);
        materialUploadTask.start();
        handleMaterialTaskStart(taskInfo);
    }

    @Override
    public void onMaterialUploadTaskFinish(MaterialTaskInfo taskInfo) {
        handleMaterialTaskFinish(taskInfo);
    }

    @Override
    public void onMaterialFileUploadStart(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo, boolean needUpdateFileInfo) {
        handleMaterialFileUploadStart(taskInfo, fileInfo, needUpdateFileInfo);
    }

    @Override
    public void onMaterialFileUploadSuccess(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo) {
        handleMaterialFileUploadSuccess(taskInfo, fileInfo);
    }

    @Override
    public void onMaterialFileUploadFaild(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo) {
        handleMaterialFileUploadFailed(taskInfo, fileInfo);

    }

    @Override
    public void onMaterialFileUploadCancle(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo) {
        handleMaterialFileUploadCancle(taskInfo, fileInfo);
    }

    @Override
    public void onMaterialFileProgressChange(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo) {
        handleMaterialFileProgressChange(taskInfo, fileInfo);
    }

    @Override
    public void onMaterialFileException(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo, Exception exception) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onMaterialFileException()->taskInfo:" + taskInfo + ", fileInfo:" + fileInfo + ", exception:" + exception);
        handleMaterialFileUploadFailed(taskInfo, fileInfo);
    }

    /**
     * 处理资料任务开始上传
     *
     * @param taskInfo 资料任务
     */
    private void handleMaterialTaskStart(final MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskStart()->taskInfo:" + taskInfo);
        if (taskInfo == null
                || taskInfo.getState() != UploadState.NOT_UPLOADED) {
            return;
        }

        taskInfo.setState(UploadState.UPLOADING);
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskStart()->开始上传:" + taskInfo.toString());
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onTaskStart(taskInfo);
                }
            }
        });
    }

    /**
     * 处理资料任务上传完成
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialTaskFinish(final MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->taskInfo:" + taskInfo);
        boolean isSuccess = true;
        for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
            if (fileInfo.getState() != UploadState.UPLOAD_SUCCESS) {
                isSuccess = false;
            }
        }
        taskInfo.setState(isSuccess ? UploadState.UPLOAD_SUCCESS : UploadState.UPLOAD_FAILED);
        getMaterialTaskInfo(taskInfo.getTaskId()).setState(taskInfo.getState());
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getState());
        if (isSuccess) {
            AppHandlerProxy.post(new Runnable() {
                @Override
                public void run() {
                    for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                        listener.onTaskSuccessful(taskInfo);
                    }
                }
            });
        } else {
            AppHandlerProxy.post(new Runnable() {
                @Override
                public void run() {
                    for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->onTaskFailed:");
                        listener.onTaskFailed(taskInfo);
                    }
                }
            });
        }

        boolean newResult = true;
        for (MaterialTaskInfo tempTaskInfo : mMaterialTaskInfos) {
            if (tempTaskInfo.getAppointmentId() != taskInfo.getAppointmentId()) {
                continue;
            }

            if (tempTaskInfo.getState() != UploadState.UPLOAD_SUCCESS
                    && tempTaskInfo.getState() != UploadState.UPLOAD_FAILED) {
                newResult = false;
                break;
            }
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->newResult:" + newResult);
        if (newResult) {
            MaterialTaskResultInfo resultInfo = null;
            for (MaterialTaskResultInfo tempResultInfo : mMaterialTaskResultInfos) {
                if (tempResultInfo.getAppointmentId() == taskInfo.getAppointmentId()) {
                    resultInfo = tempResultInfo;
                    break;
                }
            }
            Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->是否存在结果:" + (resultInfo != null));
            if (resultInfo == null) {
                resultInfo = new MaterialTaskResultInfo();
                resultInfo.setUserId(taskInfo.getUserId());
                resultInfo.setAppointmentId(taskInfo.getAppointmentId());
                int successCount = 0;
                int failedCount = 0;
                for (MaterialTaskInfo tempTaskInfo : mMaterialTaskInfos) {
                    if (tempTaskInfo.getAppointmentId() != taskInfo.getAppointmentId()) {
                        continue;
                    }
                    for (MaterialFileInfo fileInfo : tempTaskInfo.getMaterialFileInfos()) {
                        if (fileInfo.getState() == UploadState.UPLOAD_SUCCESS) {
                            successCount += 1;
                        } else {
                            failedCount += 1;
                        }
                    }
                }
                resultInfo.setSuccessCount(successCount);
                resultInfo.setFailedCount(failedCount);
                MaterialTaskResultInfo tempInfo = null;
                for (MaterialTaskResultInfo info : mMaterialTaskResultInfos) {
                    if (info.getAppointmentId() == resultInfo.getAppointmentId()) {
                        tempInfo = info;
                        break;
                    }
                }
                if (tempInfo != null) {
                    mMaterialTaskResultInfos.remove(tempInfo);
                }
                mMaterialTaskResultInfos.add(resultInfo);
                updateMaterialTaskResult(resultInfo);
                final MaterialTaskResultInfo finalResultInfo = resultInfo;
                AppHandlerProxy.post(new Runnable() {
                    @Override
                    public void run() {
                        for (MaterialTaskResultStateChangeListener listener : mMaterialTaskResultStateChangeListeners) {
                            listener.onNewMaterialTaskResult(finalResultInfo);
                        }
                    }
                });
            } else {
                Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->结果存在->resultInfo:" + resultInfo);
                int successCount = 0;
                int failedCount = 0;
                for (MaterialTaskInfo tempTaskInfo : mMaterialTaskInfos) {
                    if (tempTaskInfo.getAppointmentId() != taskInfo.getAppointmentId()) {
                        continue;
                    }
                    for (MaterialFileInfo fileInfo : tempTaskInfo.getMaterialFileInfos()) {
                        if (fileInfo.getState() == UploadState.UPLOAD_SUCCESS) {
                            successCount += 1;
                        } else {
                            failedCount += 1;
                        }
                    }
                }
                resultInfo.setSuccessCount(successCount);
                resultInfo.setFailedCount(failedCount);

                updateMaterialTaskResult(resultInfo);
                final MaterialTaskResultInfo finalResultInfo = resultInfo;
                AppHandlerProxy.post(new Runnable() {
                    @Override
                    public void run() {
                        for (MaterialTaskResultStateChangeListener listener : mMaterialTaskResultStateChangeListeners) {
                            listener.onMaterialTaskResultStateChanged(finalResultInfo);
                        }
                    }
                });
            }
            SPUtils.getInstance().put(AppPreference.KEY_NEW_UPLOAD_RESULT + mUserInfoManager.getCurrentUserInfo().getUserId(), true);
        }
        mUploadTaskQueue.remove(taskInfo.getTaskId());
        startNextTask();
    }

    /**
     * 处理资料文件上传开始
     *
     * @param taskInfo 任务信息
     * @param fileInfo 文件信息
     */
    private void handleMaterialFileUploadStart(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo, boolean needUpdateFileInfo) {
        MaterialFileInfo tempFileInfo = getMaterialFileInfo(taskInfo, fileInfo.getSessionId());
        tempFileInfo.setState(fileInfo.getState());
        updateMaterialFileState(tempFileInfo.getSessionId(), tempFileInfo.getState());
        if (needUpdateFileInfo) {
            tempFileInfo.setLocalFileName(fileInfo.getLocalFileName());
            tempFileInfo.setLocalFilePath(fileInfo.getLocalFilePath());
            tempFileInfo.setFilePostfix(fileInfo.getFilePostfix());
            updateMaterialFileInfo(tempFileInfo);
        }
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onFileUploadProgressChange(taskInfo, fileInfo);
                }
            }
        });
    }

    /**
     * 处理资料文件上传成功
     *
     * @param taskInfo 任务信息
     * @param fileInfo 文件信息
     */
    private void handleMaterialFileUploadSuccess(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialFileUploadSuccess()->taskInfo:" + taskInfo + ", fileInfo:" + fileInfo);
        if (fileInfo == null) {
            return;
        }
        updateMaterialFileProgress(fileInfo.getSessionId(), fileInfo.getProgress());
        getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setProgress(fileInfo.getProgress());
        getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setProgress(fileInfo.getState());
        updateMaterialFileState(fileInfo.getSessionId(), fileInfo.getState());
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onFileUploadStateChanged(taskInfo, fileInfo);
                }
            }
        });
    }

    /**
     * 处理资料文件上传失败
     *
     * @param taskInfo 任务信息
     * @param fileInfo 文件信息
     */
    private void handleMaterialFileUploadFailed(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialFileUploadFailed()->taskInfo:" + taskInfo + ", fileInfo:" + fileInfo);
        getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setState(fileInfo.getState());
        //当服务器返回未找到流错误时需要重新生成文件并更新文件名
        if (!getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).getLocalFileName().equals(fileInfo.getLocalFileName())) {
            getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setLocalFileName(fileInfo.getLocalFileName());
        }
        updateMaterialFileInfo(fileInfo);
        if (!StringUtils.isEmpty(fileInfo.getNewSessionId())) {
            //特定当服务器返回未找到文件流时使用
            updateMaterialFileInfoSessionId(fileInfo, new OperateDBFinishedCallback() {
                @Override
                public void onOperateDBFinished() {
                    getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setSessionId(fileInfo.getNewSessionId());
                    fileInfo.setNewSessionId("");
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                                listener.onFileUploadStateChanged(taskInfo, fileInfo);
                            }
                        }
                    });
                }
            });
        } else {
            AppHandlerProxy.post(new Runnable() {
                @Override
                public void run() {
                    for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                        listener.onFileUploadStateChanged(taskInfo, fileInfo);
                    }
                }
            });
        }
    }

    /**
     * 处理资料文件上传取消
     *
     * @param taskInfo 任务信息
     * @param fileInfo 文件信息
     */
    private void handleMaterialFileUploadCancle(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo) {

    }

    /**
     * 处理资料文件进度变化
     *
     * @param taskInfo 任务信息
     * @param fileInfo 文件信息
     */
    private void handleMaterialFileProgressChange(final MaterialTaskInfo taskInfo, final MaterialFileInfo fileInfo) {
        getMaterialFileInfo(taskInfo, fileInfo.getSessionId()).setProgress(fileInfo.getProgress());
        updateMaterialFileProgress(fileInfo.getSessionId(), fileInfo.getProgress());
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onFileUploadProgressChange(taskInfo, fileInfo);
                }
            }
        });
    }

    /**
     * 获取资料任务信息
     *
     * @param taskId 任务id
     * @return 资料任务信息
     */
    private MaterialTaskInfo getMaterialTaskInfo(String taskId) {
        for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
            if (taskInfo.getTaskId().equals(taskId)) {
                return taskInfo;
            }
        }
        return new MaterialTaskInfo();
    }

    /**
     * 获取资料文件信息
     *
     * @param taskInfo  资料任务信息
     * @param sessionId 资料文件id
     * @return 资料文件信息
     */
    private MaterialFileInfo getMaterialFileInfo(MaterialTaskInfo taskInfo, String sessionId) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->getMaterialFileInfo()->taskInfo:" + taskInfo + ", sessionId:" + sessionId);
        for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
            if (fileInfo.getSessionId().equals(sessionId)) {
                return fileInfo;
            }
        }
        return new MaterialFileInfo();
    }

    /**
     * 获取所有任务结果
     *
     * @param callback 回调接口
     */
    private void getAllMaterialTaskReuslts(final GetAllMaterialTaskResultCallback callback) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->getAllMaterialTaskReuslts()");
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialTaskResultInfo>>() {
            @Override
            public AsyncResult<List<MaterialTaskResultInfo>> runOnDBThread(AsyncResult<List<MaterialTaskResultInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<MaterialTaskResultInfo> materialTaskResultInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())});
                    while (cursor.moveToNext()) {
                        MaterialTaskResultInfo resultInfo = new MaterialTaskResultInfo();
                        resultInfo.setUserId(cursor.getInt(cursor.getColumnIndex(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID)));
                        resultInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        resultInfo.setSuccessCount(cursor.getInt(cursor.getColumnIndex(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_SUCCESS_COUNT)));
                        resultInfo.setFailedCount(cursor.getInt(cursor.getColumnIndex(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_FAILED_COUNT)));
                        materialTaskResultInfos.add(resultInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialTaskResultInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialTaskResultInfo>> asyncResult) {
                callback.onGetAllMaterialTaskResultStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 更新任务结果，结果不存在时会新增一条记录
     *
     * @param materialTaskResultInfo 任务结果
     */
    public void updateMaterialTaskResult(final MaterialTaskResultInfo materialTaskResultInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->addMaterialTaskResult()->materialTaskResultInfo:" + materialTaskResultInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID
                            + " = ? AND "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ?";
                    String[] whereArgs = new String[]{String.valueOf(materialTaskResultInfo.getUserId()), String.valueOf(materialTaskResultInfo.getAppointmentId())};
                    cursor = database.rawQuery(sql, whereArgs);
                    String whereClause = MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID
                            + " = ? AND "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ?";
                    ContentValues contentValues = new ContentValues();
                    if (cursor.getCount() > 0) {
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_SUCCESS_COUNT, materialTaskResultInfo.getSuccessCount());
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_FAILED_COUNT, materialTaskResultInfo.getFailedCount());
                        database.update(MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    } else {
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID, materialTaskResultInfo.getUserId());
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID, materialTaskResultInfo.getAppointmentId());
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_SUCCESS_COUNT, materialTaskResultInfo.getSuccessCount());
                        contentValues.put(MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_FAILED_COUNT, materialTaskResultInfo.getFailedCount());
                        database.insert(MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME, null, contentValues);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 删除任务结果记录
     *
     * @param taskResultInfo 任务结果记录
     */
    private void deleteMaterialTaskResult(final MaterialTaskResultInfo taskResultInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->deleteMaterialTaskResult()->taskResultInfo:" + taskResultInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID
                            + " = ? AND "
                            + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ?";
                    String[] whereArgs = new String[]{String.valueOf(taskResultInfo.getUserId()), String.valueOf(taskResultInfo.getAppointmentId())};
                    database.delete(MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 获取所有资料任务
     *
     * @param callback 回调接口
     */
    private void getAllMaterialTask(final GetAllMaterialTaskCallback callback) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->getAllMaterialTask()");
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialTaskInfo>>() {
            @Override
            public AsyncResult<List<MaterialTaskInfo>> runOnDBThread(AsyncResult<List<MaterialTaskInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                List<MaterialTaskInfo> materialTaskInfos = new ArrayList<MaterialTaskInfo>();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())});
                    while (cursor.moveToNext()) {
                        MaterialTaskInfo materialTaskInfo = new MaterialTaskInfo();
                        materialTaskInfo.setUserId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID)));
                        materialTaskInfo.setTaskId(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID)));
                        materialTaskInfo.setState(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE)));
                        materialTaskInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        materialTaskInfo.setMaterialId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID)));
                        materialTaskInfo.setMaterialDt(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE)));
                        materialTaskInfo.setRecureNum(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM)));
                        materialTaskInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID)));
                        materialTaskInfos.add(materialTaskInfo);
                    }

                    for (MaterialTaskInfo taskInfo : materialTaskInfos) {
                        sql = "SELECT * FROM "
                                + MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME
                                + " WHERE "
                                + MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID
                                + " = ?";
                        cursor = database.rawQuery(sql, new String[]{taskInfo.getTaskId()});
                        List<MaterialFileInfo> materialFileInfos = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            MaterialFileInfo materialFileInfo = new MaterialFileInfo();
                            materialFileInfo.setTaskId(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID)));
                            materialFileInfo.setSessionId(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID)));
                            materialFileInfo.setState(cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE)));
                            materialFileInfo.setProgress(cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS)));
                            materialFileInfo.setMaterialDt(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT)));
                            materialFileInfo.setLocalFilePath(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH)));
                            materialFileInfo.setLocalFileName(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME)));
                            materialFileInfo.setFilePostfix(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX)));
                            materialFileInfo.setServerFileName(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME)));
                            materialFileInfo.setAppointmentId(taskInfo.getAppointmentId());
                            materialFileInfos.add(materialFileInfo);
                        }
                        taskInfo.setMaterialFileInfos(materialFileInfos);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialTaskInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialTaskInfo>> asyncResult) {
                callback.onGetAllMaterialTaskStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 获取资料任务
     *
     * @param taskId   任务id
     * @param callback 回调接口
     */
    private void getMaterialTask(final String taskId, final GetMaterialTaskCallback callback) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->getMaterialTask()->taskId:" + taskId);
        mDBManager.submitDatabaseTask(new DatabaseTask<MaterialTaskInfo>() {
            @Override
            public AsyncResult<MaterialTaskInfo> runOnDBThread(AsyncResult<MaterialTaskInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                MaterialTaskInfo materialTaskInfo = null;
                try {
                    String sql = "SELECT * FROM "
                            + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{taskId});
                    while (cursor.moveToNext()) {
                        materialTaskInfo = new MaterialTaskInfo();
                        materialTaskInfo.setUserId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID)));
                        materialTaskInfo.setTaskId(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID)));
                        materialTaskInfo.setState(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE)));
                        materialTaskInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        materialTaskInfo.setMaterialId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID)));
                        materialTaskInfo.setMaterialDt(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE)));
                        materialTaskInfo.setRecureNum(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM)));
                        materialTaskInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID)));
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialTaskInfo);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<MaterialTaskInfo> asyncResult) {
                callback.onGetMaterialTaskStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 通过资料任务id获取资料文件列表
     *
     * @param taskId   资料任务id
     * @param callback 回调接口
     */
    private void getMaterialFileByTaskId(final String taskId, final GetMaterialFileCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialFileInfo>>() {
            @Override
            public AsyncResult<List<MaterialFileInfo>> runOnDBThread(AsyncResult<List<MaterialFileInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                List<MaterialFileInfo> materialFileInfos = new ArrayList<>();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{taskId});
                    while (cursor.moveToNext()) {
                        MaterialFileInfo materialFileInfo = new MaterialFileInfo();
                        materialFileInfo.setTaskId(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID)));
                        materialFileInfo.setSessionId(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID)));
                        materialFileInfo.setState(cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE)));
                        materialFileInfo.setProgress(cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS)));
                        materialFileInfo.setMaterialDt(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT)));
                        materialFileInfo.setLocalFilePath(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH)));
                        materialFileInfo.setLocalFileName(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME)));
                        materialFileInfo.setFilePostfix(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX)));
                        materialFileInfo.setServerFileName(cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME)));
                        materialFileInfos.add(materialFileInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialFileInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialFileInfo>> asyncResult) {
                callback.onGetMaterialFileStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 保存资料任务
     *
     * @param taskInfo 资料任务
     */
    private void saveMaterialTask(final MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialTask()->taskInfo:" + taskInfo.toString());
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID, taskInfo.getUserId());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID, taskInfo.getTaskId());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE, taskInfo.getState());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID, taskInfo.getAppointmentId());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID, taskInfo.getMaterialId());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE, taskInfo.getMaterialDt());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM, taskInfo.getRecureNum());
                    values.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID, taskInfo.getDoctorId());
                    database.insert(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, null, values);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 清空任务
     */
    private void clearMaterialTasks() {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())};
                    database.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 清空任务及对应任务文件
     */
    private void clearMaterialTasksAndMaterialFiles() {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                database.beginTransaction();
                try {
                    List<MaterialTaskInfo> materialTaskInfos = new ArrayList<>();
                    String sql = "SELECT * FROM "
                            + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())});
                    while (cursor.moveToNext()) {
                        MaterialTaskInfo materialTaskInfo = new MaterialTaskInfo();
                        materialTaskInfo.setUserId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID)));
                        materialTaskInfo.setTaskId(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID)));
                        materialTaskInfo.setState(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE)));
                        materialTaskInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        materialTaskInfo.setMaterialId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID)));
                        materialTaskInfo.setMaterialDt(cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE)));
                        materialTaskInfo.setRecureNum(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM)));
                        materialTaskInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID)));
                        materialTaskInfos.add(materialTaskInfo);
                    }

                    for (MaterialTaskInfo taskInfo : materialTaskInfos) {
                        String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + "=?";
                        String[] whereArgs = new String[]{taskInfo.getTaskId()};
                        database.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, whereClause, whereArgs);
                    }

                    String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())};
                    database.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 根据任务id删除任务及文件
     *
     * @param taskId 任务id
     */
    private void deleteMaterialTasksAndMaterialFiles(final String taskId) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                database.beginTransaction();
                try {
                    String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskId};
                    database.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, whereClause, whereArgs);

                    String fileWhereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + "=?";
                    database.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, fileWhereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 更新任务状态
     *
     * @param taskId 任务id
     * @param state  任务状态
     */
    private void updateMaterialTaskState(final String taskId, final int state) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialTaskState()->taskId:" + taskId + ", state:" + state);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE, state);
                    String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskId};
                    database.update(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 保存资料文件
     *
     * @param fileInfo 资料文件
     */
    private void saveMaterialFile(final MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialFile()->fileInfo:" + fileInfo.toString());
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID, fileInfo.getTaskId());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID, fileInfo.getSessionId());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE, fileInfo.getState());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS, fileInfo.getProgress());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT, fileInfo.getMaterialDt());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH, fileInfo.getLocalFilePath());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME, fileInfo.getLocalFileName());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX, fileInfo.getFilePostfix());
                    values.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME, fileInfo.getServerFileName());
                    database.insert(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, null, values);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 更新资料文件状态
     *
     * @param sessionId 文件id
     * @param state     文件状态
     */
    private void updateMaterialFileState(final String sessionId, final int state) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileState()->sessionId:" + sessionId + ", state:" + state);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE, state);
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{sessionId};
                    database.update(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });

        updateMaterialFileFlagUploadState(sessionId, state);
    }

    /**
     * 更新资料文件信息
     *
     * @param materialFileInfo 文件
     */
    private void updateMaterialFileInfo(final MaterialFileInfo materialFileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileInfo()->updateMaterialFileInfo:" + materialFileInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME, materialFileInfo.getLocalFileName());
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH, materialFileInfo.getLocalFilePath());
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX, materialFileInfo.getFilePostfix());
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{materialFileInfo.getSessionId()};
                    database.update(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 更新文件sessionid，特定当服务器返回未找到文件流时使用
     *
     * @param materialFileInfo 文件信息
     * @param callback         回调接口
     */
    private void updateMaterialFileInfoSessionId(final MaterialFileInfo materialFileInfo, final OperateDBFinishedCallback callback) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileInfoSessionId()->materialFileInfo:" + materialFileInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID, materialFileInfo.getNewSessionId());
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{materialFileInfo.getSessionId()};
                    database.update(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {
                callback.onOperateDBFinished();
            }
        });
    }

    /**
     * 更新资料文件上传进度
     *
     * @param sessionId 文件id
     * @param progress  上传进度
     */
    private void updateMaterialFileProgress(final String sessionId, final int progress) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileProgress()->sessionId:" + sessionId + ", progress:" + progress);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS, progress);
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{sessionId};
                    database.update(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 通过资料文件id删除资料文件
     *
     * @param sessionId 文件id
     */
    private void deleteMaterialFileBySessionId(final String sessionId) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{sessionId};
                    database.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 通过任务id删除资料文件
     *
     * @param taskId 任务id
     */
    private void deleteMaterialFileByTaskId(final String taskId) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskId};
                    database.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 插入材料文件本地标示
     *
     * @param materialFileFlagInfo 材料文件本地标示
     */
    private void saveMaterialFileFlagInfo(final MaterialFileFlagInfo materialFileFlagInfo) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_APPOINTMENT_ID, materialFileFlagInfo.getAppointmentId());
                    contentValues.put(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_LOCAL_FILE_PATH, materialFileFlagInfo.getLocalFilePath());
                    contentValues.put(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID, materialFileFlagInfo.getSessionId());
                    contentValues.put(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE, materialFileFlagInfo.getUploadState());
                    database.insert(MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME, null, contentValues);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 更新材料文件本地标示上传状态
     *
     * @param sessionId 上传文件sessionId
     * @param state     上传状态
     */
    private void updateMaterialFileFlagUploadState(final String sessionId, final int state) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE, state);
                    String whereClause = MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{sessionId};
                    database.update(MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 获取材料文件本地记录
     *
     * @param appointmentId 预约id
     * @param callback      回调接口
     */
    public void getMaterialFileFlags(final int appointmentId, final GetMaterialFileFlagCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialFileFlagInfo>>() {
            @Override
            public AsyncResult<List<MaterialFileFlagInfo>> runOnDBThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<MaterialFileFlagInfo> materialFileFlagInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(appointmentId)});
                    while (cursor.moveToNext()) {
                        MaterialFileFlagInfo materialFileFlagInfo = new MaterialFileFlagInfo();
                        materialFileFlagInfo.setAppointmentId(appointmentId);
                        materialFileFlagInfo.setLocalFilePath(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_LOCAL_FILE_PATH)));
                        materialFileFlagInfo.setSessionId(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID)));
                        materialFileFlagInfo.setUploadState(cursor.getInt(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE)));
                        materialFileFlagInfos.add(materialFileFlagInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialFileFlagInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult) {
                callback.onGetMaterialFileFlagStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 获取已经上传的材料文件
     *
     * @param appointmentId 预约id
     * @param callback      回调接口
     */
    public void getUploadedMaterialFiles(final int appointmentId, final GetMaterialFileFlagCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialFileFlagInfo>>() {
            @Override
            public AsyncResult<List<MaterialFileFlagInfo>> runOnDBThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<MaterialFileFlagInfo> materialFileFlagInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ? AND "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(appointmentId), String.valueOf(UploadState.UPLOAD_SUCCESS)});
                    while (cursor.moveToNext()) {
                        MaterialFileFlagInfo materialFileFlagInfo = new MaterialFileFlagInfo();
                        materialFileFlagInfo.setAppointmentId(appointmentId);
                        materialFileFlagInfo.setLocalFilePath(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_LOCAL_FILE_PATH)));
                        materialFileFlagInfo.setSessionId(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID)));
                        materialFileFlagInfo.setUploadState(cursor.getInt(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE)));
                        materialFileFlagInfos.add(materialFileFlagInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialFileFlagInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult) {
                callback.onGetMaterialFileFlagStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 获取已经上传的材料文件
     *
     * @param appointmentId 预约id
     * @param callback      回调接口
     */
    public void getUploadingMaterialFiles(final int appointmentId, final GetMaterialFileFlagCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<List<MaterialFileFlagInfo>>() {
            @Override
            public AsyncResult<List<MaterialFileFlagInfo>> runOnDBThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<MaterialFileFlagInfo> materialFileFlagInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME
                            + " WHERE "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ? AND "
                            + MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE
                            + " != ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(appointmentId), String.valueOf(UploadState.UPLOAD_SUCCESS)});
                    while (cursor.moveToNext()) {
                        MaterialFileFlagInfo materialFileFlagInfo = new MaterialFileFlagInfo();
                        materialFileFlagInfo.setAppointmentId(appointmentId);
                        materialFileFlagInfo.setLocalFilePath(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_LOCAL_FILE_PATH)));
                        materialFileFlagInfo.setSessionId(cursor.getString(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID)));
                        materialFileFlagInfo.setUploadState(cursor.getInt(cursor.getColumnIndex(MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE)));
                        materialFileFlagInfos.add(materialFileFlagInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(materialFileFlagInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialFileFlagInfo>> asyncResult) {
                callback.onGetMaterialFileFlagStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 删除本地标示上传状态
     *
     * @param sessionId 文件sessionId
     */
    private void deleteMaterFileFlagInfo(final String sessionId) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = MaterialFileFlagContract.MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID + "=?";
                    String[] whereArgs = new String[]{sessionId};
                    database.delete(MaterialFileFlagContract.MaterialFileFlagEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    @Override
    public void onNetworkStateChanged(int netWorkType) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onNetworkStateChanged()->netWorkType:" + netWorkType);
        if (mUserInfoManager.getCurrentUserInfo().getUserId() == 0) {
            return;
        }

        if (netWorkType != NetStateReceiver.NET_OFF) {
            for (MaterialTaskInfo taskInfo : mMaterialTaskInfos) {
                if (taskInfo.getState() == UploadState.UPLOAD_FAILED) {
                    taskInfo.setState(UploadState.NOT_UPLOADED);
                    for (MaterialFileInfo fileInfo : taskInfo.getMaterialFileInfos()) {
                        fileInfo.setState(UploadState.NOT_UPLOADED);
                        executeNewTask(taskInfo);
                        for (UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                            listener.onFileUploadStateChanged(taskInfo, fileInfo);
                        }
                    }
                }
            }
        }
    }

    public interface UploadStateChangeListener {
        void onNewTask(MaterialTaskInfo taskInfo);

        void onTaskStart(MaterialTaskInfo taskInfo);

        void onTaskProgressChange(MaterialTaskInfo taskInfo);

        void onTaskFailed(MaterialTaskInfo taskInfo);

        void onTaskSuccessful(MaterialTaskInfo taskInfo);

        void onTaskDelete(MaterialTaskInfo taskInfo);

        void onTaskCancle(MaterialTaskInfo taskInfo);

        void onFileUploadProgressChange(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);

        void onFileUploadStateChanged(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo);
    }

    public interface MaterialTaskResultStateChangeListener {
        void onNewMaterialTaskResult(MaterialTaskResultInfo resultInfo);

        void onMaterialTaskResultStateChanged(MaterialTaskResultInfo resultInfo);
    }

    public interface GetAllMaterialTaskResultCallback {
        void onGetAllMaterialTaskResultStateChanged(List<MaterialTaskResultInfo> taskResultInfos);
    }

    public interface GetAllMaterialTaskCallback {
        void onGetAllMaterialTaskStateChanged(List<MaterialTaskInfo> materialTaskInfos);
    }

    public interface GetMaterialTaskCallback {
        void onGetMaterialTaskStateChanged(MaterialTaskInfo materialTaskInfo);
    }

    public interface GetMaterialFileCallback {
        void onGetMaterialFileStateChanged(List<MaterialFileInfo> materialFileInfos);
    }

    public interface GetMaterialFileFlagCallback {
        void onGetMaterialFileFlagStateChanged(List<MaterialFileFlagInfo> materialFileFlagInfos);
    }

    public interface OperateDBFinishedCallback {
        void onOperateDBFinished();
    }
}
