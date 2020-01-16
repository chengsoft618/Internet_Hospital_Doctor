package cn.longmaster.hospital.doctor.core.upload;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskFileContract;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;


/**
 * 上传单个文件的信息
 * Created by ddc on 2015-07-29.
 */
public class SingleFileInfo implements Serializable {

    private String taskId;// 任务ID
    private String sessionId;//唯一标识一个文件

    private TaskState state = TaskState.NOT_UPLOADED;//上传状态
    private int progress = 0;// 文件的上传进度
    private String materialDt;// 时间
    //-----------------------------------

    private String localFilePath;//本地绝对路径
    private String localFileName;//本地文件名
    private String localPostfix;//本地文件扩展名

    private String serverFileName;//上传成功后返回的文件名


    //=====================================================
    private long lastUpdateProgressTime = 0;//上一次更新界面上传进度显示的时间

    private boolean isFromServer = false;//是否从服务器拉取,默认为本地
    private int appointmentId;
    private int materialId;
    private int checkState;
    private String auditDesc;
    private int materailType;
    private int mediaType;
    private String dicom;
    private String materialName;
    private String materialResult;
    private String materialHosp;
    private MediaDownloadInfo mediaDownloadInfo;
    private boolean showDel;

    public SingleFileInfo(String taskId, String filePath) {
        this.localFilePath = filePath;
        if (!TextUtils.isEmpty(filePath)) {
            localFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());
            localPostfix = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        this.taskId = taskId;
        this.sessionId = UploadUtils.applySessionId(this);
        this.materialDt = String.valueOf(System.currentTimeMillis());
        this.state = TaskState.NOT_UPLOADED;
    }

    public SingleFileInfo(Cursor cursor) {
        if (cursor != null) {
            taskId = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID));
            sessionId = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID));
            state = TaskState.values()[cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE))];
            progress = cursor.getInt(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS));
            materialDt = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT));
            localFilePath = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH));
            localFileName = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME));
            localPostfix = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX));
            serverFileName = cursor.getString(cursor.getColumnIndex(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME));
        }
    }

    public void insertDB() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentValues addValues = new ContentValues();
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID, taskId);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID, sessionId);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE, state.ordinal());
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS, progress);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT, materialDt);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH, localFilePath);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME, localFileName);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX, localPostfix);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME, serverFileName);
            long rows = writableDatabase.insert(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, null, addValues);
            Logger.logD(Logger.COMMON, "插入 sessionId 为" + sessionId + "的文件 insert rowID:" + rows);

            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void updateDB() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentValues addValues = new ContentValues();
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_STATE, state.ordinal());
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_PROGRESS, progress);
            addValues.put(MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME, serverFileName);

            String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + " =? ";
            String[] whereArgs = new String[]{String.valueOf(sessionId)};
            long rows = writableDatabase.update(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, addValues, whereClause, whereArgs);
            Logger.logD(Logger.COMMON, "更新 sessionId 为" + sessionId + "的文件 update rows:" + rows);

            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public static List<SingleFileInfo> getAllFileList(final String taskId) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        List<SingleFileInfo> list = new ArrayList<SingleFileInfo>();
        String sql = "SELECT * FROM " + MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME
                + " WHERE " + MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + "=? ";
        writableDatabase.beginTransaction();
        try {
            cursor = writableDatabase.rawQuery(sql, new String[]{taskId});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new SingleFileInfo(cursor));
                }
                while (cursor.moveToNext());
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

    public static void deleteDB(String taskId) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        String whereClause = MaterialTaskFileContract.MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(taskId)};
        writableDatabase.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, whereClause, whereArgs);
    }

    public static void deleteDBAll() {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.delete(MaterialTaskFileContract.MaterialTaskFileEntry.TABLE_NAME, null, null);
    }


    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getLocalPostfix() {
        return localPostfix;
    }

    public void setLocalPostfix(String localPostfix) {
        this.localPostfix = localPostfix;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getMaterialDt() {
        return materialDt;
    }

    public void setMaterialDt(String materialDt) {
        this.materialDt = materialDt;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getLastUpdateProgressTime() {
        return lastUpdateProgressTime;
    }

    public void setLastUpdateProgressTime(long lastUpdateProgressTime) {
        this.lastUpdateProgressTime = lastUpdateProgressTime;
    }

    public boolean isFromServer() {
        return isFromServer;
    }

    public void setIsFromServer(boolean isFromServer) {
        this.isFromServer = isFromServer;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public int getMaterailType() {
        return materailType;
    }

    public void setMaterailType(int materailType) {
        this.materailType = materailType;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getDicom() {
        return dicom;
    }

    public void setDicom(String dicom) {
        this.dicom = dicom;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialResult() {
        return materialResult;
    }

    public void setMaterialResult(String materialResult) {
        this.materialResult = materialResult;
    }

    public String getMaterialHosp() {
        return materialHosp;
    }

    public void setMaterialHosp(String materialHosp) {
        this.materialHosp = materialHosp;
    }

    public MediaDownloadInfo getMediaDownloadInfo() {
        return mediaDownloadInfo;
    }

    public void setMediaDownloadInfo(MediaDownloadInfo mediaDownloadInfo) {
        this.mediaDownloadInfo = mediaDownloadInfo;
    }

    public boolean isShowDel() {
        return showDel;
    }

    public void setShowDel(boolean showDel) {
        this.showDel = showDel;
    }

    @Override
    public String toString() {
        return "SingleFileInfo{" +
                "taskId='" + taskId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", state=" + state +
                ", progress=" + progress +
                ", materialDt='" + materialDt + '\'' +
                ", localFilePath='" + localFilePath + '\'' +
                ", localFileName='" + localFileName + '\'' +
                ", localPostfix='" + localPostfix + '\'' +
                ", serverFileName='" + serverFileName + '\'' +
                ", lastUpdateProgressTime=" + lastUpdateProgressTime +
                ", isFromServer=" + isFromServer +
                ", appointmentId=" + appointmentId +
                ", materialId=" + materialId +
                ", checkState=" + checkState +
                ", auditDesc='" + auditDesc + '\'' +
                ", materailType=" + materailType +
                ", mediaType=" + mediaType +
                ", dicom='" + dicom + '\'' +
                ", materialName='" + materialName + '\'' +
                ", materialResult='" + materialResult + '\'' +
                ", materialHosp='" + materialHosp + '\'' +
                ", mediaDownloadInfo=" + mediaDownloadInfo +
                '}';
    }
}
