package cn.longmaster.hospital.doctor.core.personalmaterial;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.PersonalMaterialContract;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.upload.UploadUtils;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.utils.StringUtils;

/**
 * 上传个人资料管理器
 * <p>
 * Created by W·H·K on 2018/8/2.
 */
public class PersonalMaterialManager extends BaseManager implements PersonalMaterialUploadTask.MaterialUploadTaskStateChangeListener, NetStateReceiver.NetworkStateChangedListener {
    private final String TAG = MaterialTaskManager.class.getSimpleName();
    private static final int UPLOAD_QUEUE_MAX_COUNT = 1;
    private AppApplication mAppApplication;
    private DBManager mDBManager;
    private UserInfoManager mUserInfoManager;
    private List<UploadStateChangeListener> mUploadTaskStateChangeListeners = new ArrayList<>();
    private Map<String, PersonalMaterialUploadTask> mUploadTaskQueue = new HashMap();
    private List<PersonalMaterialInfo> mMaterialTaskInfos = Collections.synchronizedList(new ArrayList<PersonalMaterialInfo>());

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
     * 注册上传状态变化监听
     *
     * @param listener   监听器
     * @param isRegister 是否注册 true注册 false注销
     */
    public void registerUploadStateChangeListener(PersonalMaterialManager.UploadStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mUploadTaskStateChangeListeners.add(listener);
        } else {
            mUploadTaskStateChangeListeners.remove(listener);
        }
    }

    /**
     * 开始上传
     *
     * @param filePaths 文件路径
     */
    public void startUpload(final String filePaths, int doctorId) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->filePaths:" + filePaths);
        if (StringUtils.isEmpty(filePaths)) {
            return;
        }
        File file = new File(filePaths);
        if (TextUtils.isEmpty(filePaths) || !file.exists()) {
            Logger.logW(Logger.APPOINTMENT, TAG + "->startUpload()->本地文件路径为空或文件不存在！");
            return;
        }
        String fileName = filePaths.substring(filePaths.lastIndexOf("/") + 1);
        String extraName = filePaths.substring(filePaths.lastIndexOf(".") + 1);
        String newFilePath = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + new Random(0).nextInt(10000) + "." + extraName;
        copyFile(filePaths, newFilePath);
        PersonalMaterialInfo info = new PersonalMaterialInfo();
        if (extraName.equals("pdf")) {
            info.setFileType(1);
        } else if (extraName.equals("ppt") || extraName.equals("pptx")) {
            info.setFileType(3);
        }
        info.setLocalFileName(newFilePath);
        info.setMaterialName(fileName.substring(0, fileName.lastIndexOf(".")));
        info.setSvrFileName("");
        info.setUploadState(UploadState.NOT_UPLOADED);
        info.setUploadProgress(0);
        info.setTaskId(UploadUtils.applyTaskId());
        info.setUserId(mUserInfoManager.getCurrentUserInfo().getUserId());
        info.setInsertDt(System.currentTimeMillis() + "");
        info.setDoctorId(doctorId);
        addNewTask(info);
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 添加新任务
     *
     * @param taskInfo 任务信息
     */
    public void addNewTask(final PersonalMaterialInfo taskInfo) {
        saveMaterialTask(taskInfo);
        mMaterialTaskInfos.add(taskInfo);
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onNewTask(taskInfo);
                }
            }
        });
        startNextTask();
    }

    /**
     * 开始下一个任务
     */
    private void startNextTask() {
        if (mUploadTaskQueue.size() >= UPLOAD_QUEUE_MAX_COUNT) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->startNextTask()->有上传任务正在执行！");
            return;
        }

        PersonalMaterialInfo needStartTask = null;
        for (PersonalMaterialInfo task : mMaterialTaskInfos) {
            if (task.getUploadState() == UploadState.NOT_UPLOADED) {
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
    private void executeNewTask(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->taskInfo:" + taskInfo);
        if (mUploadTaskQueue.size() >= UPLOAD_QUEUE_MAX_COUNT) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->执行任务达到上限！");
            return;
        }
        PersonalMaterialUploadTask materialUploadTask = new PersonalMaterialUploadTask(taskInfo, PersonalMaterialManager.this);
        mUploadTaskQueue.put(taskInfo.getTaskId(), materialUploadTask);

        if (!NetStateReceiver.hasNetConnected(mAppApplication)) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->网络断开！");
            taskInfo.setUploadState(UploadState.UPLOAD_FAILED);
            updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getUploadState());
            handleMaterialFileUploadFailed(taskInfo);
            return;
        }

        if (taskInfo.getUploadState() != UploadState.UPLOAD_SUCCESS) {
            taskInfo.setUploadState(UploadState.NOT_UPLOADED);
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->after set state taskInfo:" + taskInfo);
        materialUploadTask.start();
        handleMaterialTaskStart(taskInfo);
    }

    /**
     * 处理资料任务开始上传
     *
     * @param taskInfo 资料任务
     */
    private void handleMaterialTaskStart(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskStart()->taskInfo:" + taskInfo);
        if (taskInfo == null || taskInfo.getUploadState() != UploadState.NOT_UPLOADED) {
            return;
        }

        taskInfo.setUploadState(UploadState.UPLOADING);
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getUploadState());
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskStart()->开始上传:" + taskInfo.toString());
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onTaskStart(taskInfo);
                }
            }
        });
    }

    /**
     * 删除单个任务
     *
     * @param taskInfo 任务id
     */
    public void deleteTask(PersonalMaterialInfo taskInfo) {
        if (mUploadTaskQueue.containsKey(taskInfo.getTaskId())) {
            for (Map.Entry<String, PersonalMaterialUploadTask> entry : mUploadTaskQueue.entrySet()) {
                entry.getValue().cancelUploadTask();
            }
            mUploadTaskQueue.remove(taskInfo.getTaskId());
        }
        deleteMaterialTask(taskInfo);
        mMaterialTaskInfos.remove(taskInfo);
        startNextTask();
    }

    /**
     * 重传任务
     *
     * @param taskInfo 任务信息
     */
    public void retryTask(PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->retryTask()->重新上传mMaterialTaskInfos:" + mMaterialTaskInfos);
        taskInfo.setUploadState(UploadState.NOT_UPLOADED);
        if (mMaterialTaskInfos != null && mMaterialTaskInfos.size() > 0) {
            for (PersonalMaterialInfo info : mMaterialTaskInfos) {
                if (info.getTaskId().equals(taskInfo.getTaskId())) {
                    if ((int) info.getUploadProgress() == 100 && !StringUtils.isEmpty(info.getSvrFileName())) {
                        taskInfo.setUploadProgress(info.getUploadProgress());
                        taskInfo.setSvrFileName(info.getSvrFileName());
                    }
                    mMaterialTaskInfos.remove(info);
                    break;
                }
            }
        }
        mMaterialTaskInfos.add(taskInfo);
        Logger.logD(Logger.APPOINTMENT, TAG + "->retryTask()->重新上传-->taskInfo:" + taskInfo + " ,mMaterialTaskInfos:" + mMaterialTaskInfos);
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getUploadState());
        for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
            listener.onFileUploadStateChanged(taskInfo);
        }
        if (mUploadTaskQueue.size() >= UPLOAD_QUEUE_MAX_COUNT) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->执行任务达到上限！");
            return;
        }
        executeNewTask(taskInfo);
    }

    /**
     * 保存资料任务
     *
     * @param taskInfo 资料任务
     */
    private void saveMaterialTask(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialTask()->taskInfo:" + taskInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.USER_ID, taskInfo.getUserId());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.TASK_ID, taskInfo.getTaskId());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.MATERIAL_NAME, taskInfo.getMaterialName());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.LOCAL_FILE_NAME, taskInfo.getLocalFileName());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.SVR_FILE_NAME, taskInfo.getSvrFileName());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS, taskInfo.getUploadProgress());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, taskInfo.getUploadState());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.FILE_TYPE, taskInfo.getFileType());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.DOCTOR_ID, taskInfo.getDoctorId());
                    values.put(PersonalMaterialContract.PersonalMaterialEntry.INSERT_DT, taskInfo.getInsertDt());
                    long ss = database.insert(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, null, values);
                    Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialTask()->ss:" + ss);
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
                Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialTask()->asyncResult:" + asyncResult);
            }
        });
    }

    /**
     * 获取已经上传的材料文件
     */
    public void getUploadingMaterialFiles(final int userId, final GetMaterialFileFlagCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<List<PersonalMaterialInfo>>() {
            @Override
            public AsyncResult<List<PersonalMaterialInfo>> runOnDBThread(AsyncResult<List<PersonalMaterialInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<PersonalMaterialInfo> personalMaterialInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME
                            + " WHERE "
                            + PersonalMaterialContract.PersonalMaterialEntry.USER_ID
                            + "=?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(userId)});
                    while (cursor.moveToNext()) {
                        PersonalMaterialInfo personalMaterialInfo = new PersonalMaterialInfo();
                        personalMaterialInfo.setUserId(userId);
                        personalMaterialInfo.setUploadProgress(cursor.getInt(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS)));
                        personalMaterialInfo.setUploadState(cursor.getInt(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE)));
                        personalMaterialInfo.setLocalFileName(cursor.getString(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.LOCAL_FILE_NAME)));
                        personalMaterialInfo.setMaterialName(cursor.getString(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.MATERIAL_NAME)));
                        personalMaterialInfo.setTaskId(cursor.getString(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.TASK_ID)));
                        personalMaterialInfo.setFileType(cursor.getInt(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.FILE_TYPE)));
                        personalMaterialInfo.setSvrFileName(cursor.getString(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.SVR_FILE_NAME)));
                        personalMaterialInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex(PersonalMaterialContract.PersonalMaterialEntry.DOCTOR_ID)));
                        personalMaterialInfos.add(personalMaterialInfo);
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
                asyncResult.setData(personalMaterialInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<PersonalMaterialInfo>> asyncResult) {
                callback.onGetMaterialFileFlagStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 处理资料文件上传开始
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialFileUploadStart(final PersonalMaterialInfo taskInfo) {
        updateMaterialFileInfo(taskInfo);
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onFileUploadProgressChange(taskInfo);
                }
            }
        });
    }

    /**
     * 处理资料文件上传成功
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialFileUploadSuccess(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialFileUploadSuccess()->taskInfo:" + taskInfo);
        if (taskInfo == null) {
            return;
        }
        for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
            listener.onFileUploadStateChanged(taskInfo);
        }
        mMaterialTaskInfos.remove(taskInfo);
        deleteMaterialTask(taskInfo);
        mUploadTaskQueue.remove(taskInfo.getTaskId());
        startNextTask();
        deleteFile(new File(taskInfo.getLocalFileName()));
    }

    /**
     * 处理资料文件上传失败
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialFileUploadFailed(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialFileUploadFailed()->taskInfo:" + taskInfo);
        for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
            listener.onFileUploadStateChanged(taskInfo);
        }
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getUploadState());
        mUploadTaskQueue.remove(taskInfo.getTaskId());
        startNextTask();
    }

    /**
     * 处理资料文件进度变化
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialFileProgressChange(final PersonalMaterialInfo taskInfo) {
        updateMaterialFileProgress(taskInfo);
        AppHandlerProxy.post(new Runnable() {
            @Override
            public void run() {
                for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onFileUploadProgressChange(taskInfo);
                }
            }
        });
    }

    /**
     * 上传文件成功后删除本地文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除

        if (!file.exists()) {
            System.out.println("删除文件失败:" + file + "不存在！");
            return false;
        }
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件成功！");
                return true;
            } else {
                System.out.println("删除单个文件失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：文件不存在！");
            return false;
        }
    }

    /**
     * 更新资料文件上传进度
     *
     * @param taskInfo 文件
     */
    private void updateMaterialFileProgress(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileProgress()->taskInfo:" + taskInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS, taskInfo.getUploadProgress());
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskInfo.getTaskId()};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
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
     * 更新资料文件信息
     *
     * @param taskInfo 文件
     */
    private void updateMaterialFileInfo(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileInfo()->updateMaterialFileInfo:" + taskInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.LOCAL_FILE_NAME, taskInfo.getLocalFileName());
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.SVR_FILE_NAME, taskInfo.getSvrFileName());
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS, taskInfo.getUploadProgress());
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, taskInfo.getUploadState());
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.FILE_TYPE, taskInfo.getFileType());
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskInfo.getTaskId()};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
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
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, state);
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskId};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
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
     * 删除任务
     *
     * @param taskInfo 任务信息
     */
    private void deleteMaterialTask(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->deleteMaterialTaskResult()->taskInfo:" + taskInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + " = ?";
                    String[] whereArgs = new String[]{String.valueOf(taskInfo.getTaskId())};
                    long ss = database.delete(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                    Logger.logD(Logger.APPOINTMENT, TAG + "->deleteMaterialTaskResult()->ss:" + ss);
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {
                for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                    listener.onDeleteTask(taskInfo);
                }
            }
        });
    }

    /**
     * 处理资料任务上传完成
     *
     * @param taskInfo 任务信息
     */
    private void handleMaterialTaskFinish(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->handleMaterialTaskFinish()->taskInfo:" + taskInfo);
        updateMaterialTaskState(taskInfo.getTaskId(), taskInfo.getUploadState());
        mUploadTaskQueue.remove(taskInfo.getTaskId());
        startNextTask();
    }


    /**
     * ----------------------------------------------------回调处理分割线---------------------------------------------------------------------
     */

    @Override
    public void onNetworkStateChanged(int netWorkType) {
        Logger.logD(Logger.APPOINTMENT, "->onNetworkStateChanged()->netWorkType:" + netWorkType);
        if (mUserInfoManager.getCurrentUserInfo().getUserId() == 0) {
            return;
        }

        /*if (netWorkType != NetStateReceiver.NET_OFF) {
            for (PersonalMaterialInfo taskInfo : mMaterialTaskInfos) {
                if (taskInfo.getUploadState() == UploadState.UPLOAD_FAILED) {
                    taskInfo.setUploadState(UploadState.NOT_UPLOADED);
                    executeNewTask(taskInfo);
                    for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
                        listener.onFileUploadStateChanged(taskInfo);
                    }
                }
            }
        }*/
    }

    @Override
    public void onMaterialFileUploadStart(PersonalMaterialInfo taskInfo) {
        handleMaterialFileUploadStart(taskInfo);
    }

    @Override
    public void onMaterialFileUploadSuccess(PersonalMaterialInfo taskInfo) {
        handleMaterialFileUploadSuccess(taskInfo);
    }

    @Override
    public void onMaterialFileUploadFaild(PersonalMaterialInfo taskInfo) {
        handleMaterialFileUploadFailed(taskInfo);
    }

    @Override
    public void onMaterialFileBindingRepetition(PersonalMaterialInfo taskInfo) {
        for (PersonalMaterialManager.UploadStateChangeListener listener : mUploadTaskStateChangeListeners) {
            listener.onFileBindingRepetition(taskInfo);
        }
        mMaterialTaskInfos.remove(taskInfo);
        deleteMaterialTask(taskInfo);
        mUploadTaskQueue.remove(taskInfo.getTaskId());
        startNextTask();
        deleteFile(new File(taskInfo.getLocalFileName()));
    }

    @Override
    public void onMaterialFileUploadCancle(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onMaterialFileProgressChange(PersonalMaterialInfo taskInfo) {
        handleMaterialFileProgressChange(taskInfo);
    }

    @Override
    public void onMaterialFileUploadComplete(PersonalMaterialInfo taskInfo) {
        updateMaterialFile(taskInfo);
    }

    @Override
    public void onMaterialFileException(PersonalMaterialInfo taskInfo, Exception exception) {
        handleMaterialFileUploadFailed(taskInfo);
    }

    private void updateMaterialFile(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->updateMaterialFileProgress()->taskInfo:" + taskInfo);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS, taskInfo.getUploadProgress());
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.SVR_FILE_NAME, taskInfo.getSvrFileName());
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskInfo.getTaskId()};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
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

    public interface UploadStateChangeListener {
        void onNewTask(PersonalMaterialInfo taskInfo);

        void onTaskStart(PersonalMaterialInfo taskInfo);

        void onDeleteTask(PersonalMaterialInfo taskInfo);

        void onFileUploadProgressChange(PersonalMaterialInfo taskInfo);

        void onFileUploadStateChanged(PersonalMaterialInfo taskInfo);

        void onFileBindingRepetition(PersonalMaterialInfo taskInfo);
    }

    public interface GetMaterialFileFlagCallback {
        void onGetMaterialFileFlagStateChanged(List<PersonalMaterialInfo> personalMaterialInfos);
    }
}
