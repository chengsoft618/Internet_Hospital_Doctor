package cn.longmaster.hospital.doctor.core.upload;

import android.os.Looper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AppAsyncTask;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;

/**
 * 有上传文件的接口请求管理类
 * Created by ddc on 2015-07-29.
 */
public class UploadTaskManager extends BaseManager implements UploadTaskStateListener, NetStateReceiver.NetworkStateChangedListener {
    public static final String TAG = "UploadTask";
    public static final String CODE = "code";
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;

    /**
     * 任务列表
     */
    private List<AbsTask> mUploadTaskList = Collections.synchronizedList(new ArrayList<AbsTask>());
    /**
     * 每个任务对应的上传文件的任务列表
     */
    private Map<String, UploadTask> mFileTaskMap = new ConcurrentHashMap<String, UploadTask>();
    /**
     * 当前是否有视频上传
     */
    private AtomicBoolean mIsUploading = new AtomicBoolean(false);
    /**
     * 当前网络类型
     */
    private int mCurrentNetType;
    private List<UploadNotifyListener> mNotifyListenerList = new ArrayList<UploadNotifyListener>();

    public UploadTaskManager() {
    }

    @Override
    public void onManagerCreate(AppApplication application) {
        mCurrentNetType = NetStateReceiver.getCurrentNetType(application);
        NetStateReceiver.setOnNetworkStateChangedListener(this);
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
        initTask();
    }

    /**
     * 初始化
     */
    private void initTask() {
        log("initTask() mIsUploading:" + mIsUploading.get());
        new AppAsyncTask<Void>() {
            @Override
            public void runOnBackground(AsyncResult<Void> result) {
                log("initTask() runOnBackground mIsUploading:" + mIsUploading.get());
                if (mIsUploading.get()) {// 取消正在上传
                    for (AbsTask uploadTaskInfo : mUploadTaskList) {
                        if (uploadTaskInfo.getState() == TaskState.UPLOADING) {
                            UploadTask task = mFileTaskMap.get(uploadTaskInfo.getTaskId());
                            if (task != null) {
                                task.cancelUploadTask();
                            }
                            //break;
                        }
                    }
                    mIsUploading.set(false);
                }
                resetTask();
            }
        }.execute();
    }

    private void resetTask() {
        log("resetTask() mIsUploading:" + mIsUploading.get());
        mUploadTaskList.clear();
        mFileTaskMap.clear();

        MaterialTask.getAllTask(new MaterialTask.GetAllMaterialTaskCallback() {
            @Override
            public void onGetAllTaskList(List<MaterialTask> list) {
                if (list != null) {
                    for (MaterialTask task : list) {
                        if (task == null) {
                            continue;
                        }
                        if (task.getState() == TaskState.UPLOADING) {
                            task.updateState(TaskState.NOT_UPLOADED);
                        }
                        List<SingleFileInfo> fileInfoList = task.getFileList();
                        if (fileInfoList == null) {
                            continue;
                        }
                        for (SingleFileInfo fileInfo : fileInfoList) {
                            if (fileInfo == null) {
                                continue;
                            }
                            if (fileInfo.getState() == TaskState.UPLOADING) {
                                fileInfo.updateState(TaskState.NOT_UPLOADED);
                            }
                        }
                        mUploadTaskList.add(task);
                    }
                    startNext();
                }
            }
        });
    }

    private void startNext() {
        log("startNext()");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (mIsUploading.get()) {
                    // 当前有上传任务
                    log("startNext() -- > 当前有上传任务");
                    return;
                }

                AbsTask needStartTask = null;
                for (AbsTask task : mUploadTaskList) {
                    // 未上传
                    if (task.getState() == TaskState.NOT_UPLOADED) {
                        needStartTask = task;
                        break;
                    }
                }
                if (needStartTask != null) {
                    start(needStartTask);
                }
                Looper.loop();
            }
        };
        AppExecutors.getInstance().diskIO().execute(runnable);
    }

    private void start(final AbsTask task) {
        log("start() task:" + (task == null ? "task is null" : task.toString()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mIsUploading.get()) {
                    // 当前有上传任务
                    log("start() -- > 当前有上传任务， 返回");
                    return;
                }
                if (!checkEnable()) {
                    log("start() -- > 当前网络不通不能上传， 返回");
                    if (task != null) {
                        task.updateState(TaskState.UPLOAD_FAILED);
                        List<SingleFileInfo> fileList = task.getFileList();
                        for (int i = fileList.size() - 1; i >= 0; i--) {
                            final SingleFileInfo fileInfo = fileList.get(i);
                            if (fileInfo == null) {
                                fileList.remove(i);
                                continue;
                            }
                            if (fileInfo.getState() == TaskState.UPLOADING || fileInfo.getState() == TaskState.NOT_UPLOADED) {
                                fileInfo.updateState(TaskState.UPLOAD_FAILED);
                            }
                            AppHandlerProxy.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (UploadNotifyListener listener : mNotifyListenerList) {
                                        listener.onFileUploadFailed(task, fileInfo, new Exception("Network unknown"));
                                    }
                                }
                            });
                        }
                    }

                    onTaskFinished(task, UploadTaskManager.CODE_ERROR, new Exception("Network unknown"));
                    return;
                }
                if (task != null && (task.getState() == TaskState.NOT_UPLOADED)) {
                    mIsUploading.set(true);
                    task.updateState(TaskState.UPLOADING);

                    log("start() -- > task:" + task.toString());
                    startUploadFile(task);

                    // 通知任务开始
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                listener.onTaskStart(task);
                            }
                        }
                    });
                }
            }
        };
        AppExecutors.getInstance().diskIO().execute(runnable);
    }


    /**
     * 上传文件
     *
     * @param task 带文件上传的task
     */
    private void startUploadFile(AbsTask task) {
        log("uploadFile() -- > task:" + task.toString());

        UploadTask uploadTask = new UploadTask(task, this);
        mFileTaskMap.put(task.getTaskId(), uploadTask);
        uploadTask.start();
    }

    /**
     * 取消任务上传
     */
    public void cancelUploadTask(String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return;
        }
        for (AbsTask task : mUploadTaskList) {
            if (task == null) {
                continue;
            }
            if (taskId.equals(task.getTaskId())) {
                mUploadTaskList.remove(task);
                task.deleteDB();
                break;
            }
        }
        UploadTask task = mFileTaskMap.get(taskId);
        if (task != null) {
            task.cancelUploadTask();
            mIsUploading.set(false);
        }
    }

    @Override
    public void onNetworkStateChanged(int netWorkType) {
        log("BlogUploadManager->onNetworkStateChanged() netWorkType :" + netWorkType);
        mCurrentNetType = netWorkType;
        startNext();
    }

    @Override
    public void onFileUploadStart(final AbsTask task, final SingleFileInfo file) {
        new AppAsyncTask<AbsTask>() {
            @Override
            public void runOnBackground(AsyncResult<AbsTask> result) {
                result.setResult(CODE_ERROR);
                if (task != null && file != null) {
                    result.setResult(CODE_SUCCESS);
                    file.setProgress(0);
                    result.setData(task);
                }
            }

            @Override
            public void runOnUIThread(final AsyncResult<AbsTask> result) {
                if (result.getResult() == CODE_SUCCESS) {
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                listener.onFileUploadStart(task, file);
                            }
                        }
                    });
                }
            }
        }.execute();
    }

    @Override
    public void onFileUploadCancel(final AbsTask task, final SingleFileInfo file) {
        log("onBlogPublishCancel() task = " + (task == null ? "task == null" : task.toString()));

        AppAsyncTask<AbsTask> asyncTask = new AppAsyncTask<AbsTask>() {
            @Override
            public void runOnBackground(AsyncResult<AbsTask> result) {
                mIsUploading.set(false);
                if (task != null) {
                    mFileTaskMap.remove(task.getTaskId());
                    mUploadTaskList.remove(task);
                    if (task.getState() == TaskState.UPLOADING) {
                        task.deleteDB();//cancelUploadTask 方法中已删除

                        mIsUploading.set(false);
                        startNext();

                        result.setResult(CODE_SUCCESS);
                        result.setData(task);
                    }
                } else {
                    result.setResult(UploadTaskManager.CODE_ERROR);
                }
            }

            @Override
            public void runOnUIThread(final AsyncResult<AbsTask> result) {
                if (result.getResult() == CODE_SUCCESS) {
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                listener.onTaskCancel(task);
                            }
                        }
                    });
                }
            }
        };
        asyncTask.execute();
    }

    /**
     * 单个附件上传进度
     */
    @Override
    public void onFileUploadProgressChange(final AbsTask task, final SingleFileInfo file) {
        log("onFileUploadProgressChange()->" + (file == null ? "file == null" : file.toString()));
        new AppAsyncTask<AbsTask>() {
            @Override
            public void runOnBackground(AsyncResult<AbsTask> result) {
                result.setResult(CODE_ERROR);
                if (task != null && file != null) {
                    result.setResult(CODE_SUCCESS);
                    result.setData(task);
                }
            }

            @Override
            public void runOnUIThread(final AsyncResult<AbsTask> result) {
                if (result.getResult() == CODE_SUCCESS) {
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                listener.onFileUploadProgressChange(task, file);
                                listener.onTaskProgressChange(task);
                            }
                        }
                    });
                }
            }
        }.execute();
    }

    @Override
    public void onFileUploadException(final AbsTask task, final SingleFileInfo file, final Exception exception) {
        new AppAsyncTask<AbsTask>() {
            @Override
            public void runOnBackground(AsyncResult<AbsTask> result) {
                result.setResult(CODE_ERROR);
                if (task != null && file != null) {
                    result.setResult(CODE_SUCCESS);
                    result.setData(task);
                }
            }

            @Override
            public void runOnUIThread(final AsyncResult<AbsTask> result) {
                if (result.getResult() == CODE_SUCCESS) {
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                listener.onFileUploadFailed(task, file, exception);
                            }
                        }
                    });
                }
            }
        }.execute();
    }

    @Override
    public void onFileUploadComplete(final AbsTask task, final SingleFileInfo file, final Throwable throwable) {
        new AppAsyncTask<AbsTask>() {
            @Override
            public void runOnBackground(AsyncResult<AbsTask> result) {
                result.setResult(CODE_ERROR);
                if (task != null && file != null) {
                    result.setResult(CODE_SUCCESS);
                    result.setData(task);
                }
            }

            @Override
            public void runOnUIThread(final AsyncResult<AbsTask> result) {
                if (result.getResult() == CODE_SUCCESS) {
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            for (UploadNotifyListener listener : mNotifyListenerList) {
                                if (file.getState() == TaskState.UPLOAD_SUCCESS) {
                                    listener.onFileUploadSuccess(task, file);
                                } else {
                                    listener.onFileUploadFailed(task, file, throwable);
                                }
                                listener.onTaskProgressChange(task);
                            }
                        }
                    });
                }
            }
        }.execute();
    }


    /**
     * 任务完成
     */
    @Override
    public void onTaskFinished(final AbsTask task, final int code, final Object response) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (task != null) {
                    if (code == CODE_SUCCESS && response != null) {
                        boolean success = true;
                        if (task.getFileList() != null) {
                            for (SingleFileInfo fileInfo : task.getFileList()) {
                                if (fileInfo == null) {
                                    continue;
                                }
                                if (fileInfo.getState() != TaskState.UPLOAD_SUCCESS) {
                                    success = false;
                                }
                            }
                        }
                        if (success) { // 上传成功
                            task.updateState(TaskState.UPLOAD_SUCCESS);
                            task.deleteDB();//上传成功后，删除数据库中的记录
                        } else {
                            task.updateState(TaskState.UPLOAD_FAILED);
                        }

                        // 通知上传成功
                        AppHandlerProxy.post(new Runnable() {
                            @Override
                            public void run() {
                                for (UploadNotifyListener listener : mNotifyListenerList) {
                                    listener.onTaskSuccessful(task, response);
                                }
                            }
                        });
                    } else {
                        if (task.getState() == TaskState.UPLOADING || task.getState() == TaskState.UPLOAD_FAILED) {
                            task.updateState(TaskState.UPLOAD_FAILED);

                            // 通知上传失败
                            AppHandlerProxy.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (UploadNotifyListener listener : mNotifyListenerList) {
                                        log("UploadNotifyListener --> onTaskFinished:" + task.toString());
                                        listener.onTaskFailed(task, response instanceof Throwable ? (Throwable) response : null);
                                    }
                                }
                            });
                        }
                    }
                    mFileTaskMap.remove(task.getTaskId());
                    mUploadTaskList.remove(task);
                }
                mIsUploading.set(false);
                // 开始下一个任务
                startNext();
            }
        };
        AppExecutors.getInstance().diskIO().execute(runnable);
    }

    /**
     * 检查现在是否能发送
     *
     * @return false：不能发送
     * true:发送可用
     */
    private boolean checkEnable() {
        if (mCurrentNetType == NetStateReceiver.NET_OFF) {
            // 没有网络 或者 未登录 或者 已经关闭上传开关 do nothing
            log("checkEnable() 没有网络 或者 未登录 或者 已经关闭上传开关 do nothing!");
            return false;
        } else {
            return true;
        }
    }

    private void log(String msg) {
        Logger.logD(Logger.COMMON, msg);
    }
}
