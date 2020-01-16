package cn.longmaster.hospital.doctor.core.upload;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskContract;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskFileContract;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadFileMaterialRequest;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadMaterialRequest;


/**
 * 上传辅助资料task
 * Created by ddc on 2015-07-29.
 */
public class MaterialTask extends AbsTask {

    private int appointmentId;//	预约号/病历号
    private int materialId;//辅助检查项ID
    //private String materialPic;//资料图片 多张图片请用分号(;)隔开
    private String materialDt;//资料时间
    private int recurNum;//复诊次数	初诊传0
    private int docUserId;//医生id


    public MaterialTask(int appointmentId, int materialId) {
        this.appointmentId = appointmentId;
        this.materialId = materialId;
        this.materialDt = String.valueOf(System.currentTimeMillis());
    }

    public MaterialTask(Cursor cursor) {
        if (cursor != null) {
            String userId = cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID));
            String taskId = cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID));
            TaskState state = TaskState.values()[cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE))];
            appointmentId = cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID));
            materialId = cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID));
            materialDt = cursor.getString(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE));
            recurNum = cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM));
            docUserId = cursor.getInt(cursor.getColumnIndex(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID));

            List<SingleFileInfo> fileList = SingleFileInfo.getAllFileList(taskId);

            setUserId(userId);
            setTaskId(taskId);
            setState(state);
            setFileList(fileList);
        }
    }

    @Override
    public String getUploadFileUrl(SingleFileInfo fileInfo) {
        if (fileInfo == null) {
            return "";
        }
        UploadFileMaterialRequest request = new UploadFileMaterialRequest(new OnResultListener<String>() {
            @Override
            public void onResult(BaseResult baseResult, String s) {

            }
        });
        request.ext = fileInfo.getLocalPostfix();
        request.fileName = fileInfo.getLocalFileName();
        request.appointmentId = getAppointmentId();
        return request.getCompleteUrl();
    }

    @Override
    protected void dealFileUploadSuccess(final SingleFileInfo fileInfo, final UploadTaskStateListener l) {
        final CountDownLatch latch = new CountDownLatch(1);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                UploadMaterialRequest request = new UploadMaterialRequest(new OnResultListener<String>() {
                    @Override
                    public void onResult(BaseResult baseResult, String s) {
                        if (baseResult.getCode() != RESULT_SUCCESS) {
                            if (l != null) {
                                fileInfo.updateState(TaskState.UPLOAD_FAILED);
                                l.onFileUploadComplete(MaterialTask.this, fileInfo, new Error("code == " + baseResult.getCode()));
                            }
                        } else {
                            if (l != null) {
                                fileInfo.updateState(TaskState.UPLOAD_SUCCESS);
                                Logger.logD(Logger.COMMON, "更新文件上传成功");
                                l.onFileUploadComplete(MaterialTask.this, fileInfo, null);
                            }
                        }
                        latch.countDown();
                    }
                });
                request.appointmentId = appointmentId;
                request.materialId = materialId;
                request.materialDt = materialDt;
                request.recureNum = recurNum;
                request.materialPic = fileInfo.getServerFileName();
                request.docUserId = docUserId;
                request.doPost();
            }
        });
        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fileInfo.updateState(TaskState.UPLOAD_FAILED);
        }
    }

    /**
     * 处理单个文件上传失败
     *
     * @param fileInfo
     * @param e
     * @param listener
     * @return true，继续上传未上传的文件，false，不继续上传未上传的文件，结束task
     */
    @Override
    protected boolean dealFileUploadFailed(SingleFileInfo fileInfo, Exception e, UploadTaskStateListener listener) {
        return true;
    }

    @Override
    public void allFileUploadSuccess(final UploadTaskStateListener l) {
        if (l != null) {
            l.onTaskFinished(MaterialTask.this, UploadTaskManager.CODE_SUCCESS, "");
        }
    }

    /**
     * 获取正在上传的任务
     *
     * @param appointmentId
     * @param materialId
     * @param callback
     */
    public static void getTask(final int appointmentId, final int materialId, final GetAllMaterialTaskCallback callback) {
        DBManager dbManager = AppApplication.getInstance().getManager(DBManager.class);
        if (dbManager == null) {
            return;
        }
        DatabaseTask<List<MaterialTask>> dbTask = new DatabaseTask<List<MaterialTask>>() {
            @Override
            public AsyncResult<List<MaterialTask>> runOnDBThread(AsyncResult<List<MaterialTask>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                String sql = "SELECT * FROM " + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME + " WHERE "
                        + MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID + "=? AND "
                        + MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID + "=? ";

                List<MaterialTask> list = readDb(writableDatabase, sql, new String[]{appointmentId + "", materialId + ""});
                asyncResult.setData(list);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialTask>> asyncResult) {
                callback.onGetAllTaskList(asyncResult.getData());
            }
        };
        dbManager.submitDatabaseTask(dbTask);
    }

    public static List<MaterialTask> getAllTask() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM " + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME;
        return readDb(writableDatabase, sql, null);
    }

    /**
     * 从数据库中读取保存的上传文件任务
     *
     * @param callback
     * @return
     */
    public static void getAllTask(final GetAllMaterialTaskCallback callback) {
        DBManager dbManager = AppApplication.getInstance().getManager(DBManager.class);
        if (dbManager == null) {
            return;
        }
        DatabaseTask<List<MaterialTask>> dbTask = new DatabaseTask<List<MaterialTask>>() {
            @Override
            public AsyncResult<List<MaterialTask>> runOnDBThread(AsyncResult<List<MaterialTask>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                String sql = "SELECT * FROM " + MaterialTaskContract.MaterialTaskEntry.TABLE_NAME;
                List<MaterialTask> list = readDb(writableDatabase, sql, null);
                asyncResult.setData(list);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<MaterialTask>> asyncResult) {
                callback.onGetAllTaskList(asyncResult.getData());
            }
        };
        dbManager.submitDatabaseTask(dbTask);
    }

    private static List<MaterialTask> readDb(SQLiteDatabase writableDatabase, String sql, String[] selectionArgs) {
        List<MaterialTask> list = new ArrayList<MaterialTask>();
        writableDatabase.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = writableDatabase.rawQuery(sql, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new MaterialTask(cursor));
                } while (cursor.moveToNext());
            }
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            writableDatabase.endTransaction();
        }
        return list;
    }

    @Override
    public void insertDB() {
        deleteDB();

        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentValues addValues = new ContentValues();
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_USER_ID, getUserId());
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID, getTaskId());
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE, getState().ordinal());
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID, appointmentId);
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID, materialId);
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE, materialDt);
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_RECUR_NUM, recurNum);
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID, docUserId);

            long rows = writableDatabase.insert(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, null, addValues);
            Logger.logD(Logger.COMMON, "插入 taskId 为" + getTaskId() + "的task insert rowID:" + rows);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }

        for (SingleFileInfo fileInfo : getFileList()) {
            if (fileInfo == null) {
                continue;
            }
            fileInfo.insertDB();
        }
    }

    @Override
    public void updateDB() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentValues addValues = new ContentValues();
            addValues.put(MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_STATE, getState().ordinal());

            String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID + " =? ";
            String[] whereArgs = new String[]{getTaskId()};
            long rows = writableDatabase.update(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, addValues, whereClause, whereArgs);
            Logger.logD(Logger.COMMON, "更新 taskId 为" + getTaskId() + "的task update rows:" + rows);

            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @Override
    public void deleteDB() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID + "=?";
        String[] whereArgs = new String[]{getTaskId()};
        writableDatabase.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, whereClause, whereArgs);

        SingleFileInfo.deleteDB(getTaskId());
    }

    /**
     * 删除任务
     *
     * @param localFileName 本地文件名
     */
    public static void delTask(final String localFileName) {
        DatabaseTask<Void> task = new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                try {
                    database.beginTransaction();
                    cursor = database.query(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, null, MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME + " = ?",
                            new String[]{localFileName}, null, null, null);

                    if (cursor == null || cursor.getCount() == 0)//如果取不到文件,直接返回
                    {
                        return asyncResult;
                    }

                    String taskId = "";
                    while (cursor.moveToNext()) {
                        taskId = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID));
                    }

                    //删除文件信息表中的文件
                    database.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME,
                            MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME + " = ?",
                            new String[]{localFileName});

                    if (cursor.getCount() == 1) {//如果该上传任务只有一个文件删除上传任务
                        String whereClause = MaterialTaskContract.MaterialTaskEntry.COLUMN_NAME_TASK_ID + "=?";
                        String[] whereArgs = new String[]{taskId};
                        database.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, whereClause, whereArgs);
                    }

                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
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
        };
        AppApplication.getInstance().getManager(DBManager.class).submitDatabaseTask(task);
    }

    @Override
    public void deleteDBAll() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.delete(MaterialTaskContract.MaterialTaskEntry.TABLE_NAME, null, null);
        SingleFileInfo.deleteDBAll();
    }

    public interface GetAllMaterialTaskCallback {
        void onGetAllTaskList(List<MaterialTask> list);

    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMaterialDt() {
        return materialDt;
    }

    public void setMaterialDt(String materialDt) {
        this.materialDt = materialDt;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getRecurNum() {
        return recurNum;
    }

    public void setRecurNum(int recurNum) {
        this.recurNum = recurNum;
    }

    public int getDocUserId() {
        return docUserId;
    }

    public void setDocUserId(int docUserId) {
        this.docUserId = docUserId;
    }

    @Override
    public String toString() {
        return "MaterialTask{" +
                "appointmentId='" + appointmentId + '\'' +
                ", materialId='" + materialId + '\'' +
                ", materialDt='" + materialDt + '\'' +
                ", recurNum='" + recurNum + '\'' +
                ", docUserId='" + docUserId + '\'' +
                '}';
    }
}
