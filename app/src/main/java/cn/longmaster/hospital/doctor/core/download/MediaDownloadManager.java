package cn.longmaster.hospital.doctor.core.download;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.fileloader.FileLoader;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileDownLoadTask;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadOptions;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.MediaDownloadContract;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;

/**
 * 多媒体管理类
 * Created by JinKe on 2016-11-18.
 */
public class MediaDownloadManager extends BaseManager {
    private final String TAG = MediaDownloadManager.class.getSimpleName();

    private AppApplication mAppApplication;
    private DBManager mDBManager;
    private Map<String, FileLoadListener> mDownloadListenerOMap;//控制界面回调
    private Map<String, FileLoadListener> mLoadListenersMap;//控制下载回调
    private List<FileLoadListener> mLoadListeners;//控制下载回调集合
    private Map<String, FileDownLoadTask> mDownLoadQueue;

    @Override
    public void onManagerCreate(AppApplication application) {
        mAppApplication = application;
        mDownloadListenerOMap = new HashMap<>();
        mLoadListenersMap = new HashMap<>();
        mDownLoadQueue = new HashMap<>();
        mLoadListeners = new ArrayList();
    }

    @Override
    public void onAllManagerCreated() {
        mDBManager = mAppApplication.getManager(DBManager.class);
    }

    /**
     * 是否正在下载
     *
     * @param path
     * @return
     */
    public boolean isLoading(String path) {
        return mLoadListenersMap.containsKey(path);
    }

    /**
     * 是否正在下载
     *
     * @param fileName
     * @return
     */
    public boolean isDownLoading(String fileName) {
        return mDownLoadQueue.containsKey(fileName);
    }

    public void addLoadListener(String filePath, FileLoadListener listener) {
        mDownloadListenerOMap.put(filePath, listener);
        Logger.logI(Logger.APPOINTMENT, "addLoadListener-->" + filePath);
    }

    public void regLoadListener(FileLoadListener listener) {
        mLoadListeners.add(listener);
    }

    public void unRegLoadListener(FileLoadListener listener) {
        mLoadListeners.remove(listener);
    }

    public void removeLoadListener(String filePath) {
        mDownloadListenerOMap.remove(filePath);
    }

    public void fileDownload(String localPath, String url, FileLoadListener listener) {
        if (TextUtils.isEmpty(localPath) || TextUtils.isEmpty(url)) {
            return;
        }
        if (mLoadListenersMap.containsKey(localPath)) {
            mLoadListenersMap.remove(localPath);
            mLoadListenersMap.put(localPath, listener);
            return;
        }
        mLoadListenersMap.put(localPath, listener);
        addLoadListener(localPath, listener);

        FileLoadListener fileLoadListener = new FileLoadListener() {
            @Override
            public void onStartDownload(String filePath) {
                if (mDownloadListenerOMap.get(filePath) == null) {
                    return;
                }
                mDownloadListenerOMap.get(filePath).onStartDownload(filePath);
            }

            @Override
            public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
                if (mDownloadListenerOMap.get(filePath) == null) {
                    return;
                }
                mDownloadListenerOMap.get(filePath).onLoadProgressChange(filePath, totalSize, currentSize);
            }

            @Override
            public void onLoadFailed(String filePath, String reason) {
                mLoadListenersMap.remove(filePath);
                if (mDownloadListenerOMap.get(filePath) == null) {
                    return;
                }
                mDownloadListenerOMap.get(filePath).onLoadFailed(filePath, reason);
                mDownloadListenerOMap.remove(filePath);
            }

            @Override
            public void onLoadSuccessful(String filePath) {
                String destFileDir = filePath.substring(0, filePath.lastIndexOf("/"));
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                String destFiledPath = destFileDir.substring(0, destFileDir.lastIndexOf("/")) + "/" + fileName;
                if (FileUtil.isFileExist(filePath)) {
                    FileUtil.copyFile(filePath, destFiledPath);
                    FileUtil.deleteFile(filePath);
                }
                mLoadListenersMap.remove(filePath);
                if (mDownloadListenerOMap.get(filePath) == null) {
                    return;
                }
                mDownloadListenerOMap.get(filePath).onLoadSuccessful(filePath);
                mDownloadListenerOMap.remove(filePath);
            }

            @Override
            public void onLoadStopped(String filePath) {

            }
        };
        FileLoader loader = FileLoader.getInstance();
        FileLoadOptions options = new FileLoadOptions();
        options.setFilePath(localPath);
        options.setUrl(url);
        loader.fileDownLoad(options, fileLoadListener);
    }

    public void fileDownload(final MediaDownloadInfo downloadInfo) {
        if (TextUtils.isEmpty(downloadInfo.getLocalFileName())) {
            return;
        }
        String tempPath = getTempPath(downloadInfo.getLocalFileName(), downloadInfo.getAppointmentId());
        String url = getUrl(downloadInfo);
        Logger.logI(Logger.APPOINTMENT, "fileDownload-->url:" + url);
        FileLoadListener fileLoadListener = new FileLoadListener() {
            @Override
            public void onStartDownload(String filePath) {
                downloadInfo.setState(DownloadState.DOWNLOADING);
                saveDownloadInfoToDB(downloadInfo);
                for (FileLoadListener listener : mLoadListeners) {
                    listener.onStartDownload(filePath);
                }
            }

            @Override
            public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
                downloadInfo.setState(DownloadState.DOWNLOADING);
                downloadInfo.setCurrentSize(currentSize);
                downloadInfo.setTotalSize(totalSize);
                saveDownloadInfoToDB(downloadInfo);
                for (FileLoadListener listener : mLoadListeners) {
                    listener.onLoadProgressChange(filePath, totalSize, currentSize);
                }
            }

            @Override
            public void onLoadFailed(String filePath, String reason) {
                downloadInfo.setState(DownloadState.DOWNLOAD_FAILED);
                saveDownloadInfoToDB(downloadInfo);
                for (FileLoadListener listener : mLoadListeners) {
                    listener.onLoadFailed(filePath, reason);
                }
                if (mDownLoadQueue.containsKey(getFileName(filePath))) {
                    mDownLoadQueue.remove(getFileName(filePath));
                }
            }

            @Override
            public void onLoadSuccessful(String filePath) {
                deleteDownloadInfoToDB(downloadInfo.getLocalFileName());
                String destFiledPath = getFilePath(downloadInfo.getLocalFileName(), downloadInfo.getAppointmentId());
                if (FileUtil.isFileExist(filePath)) {
                    FileUtil.copyFile(filePath, destFiledPath);
                    FileUtil.deleteFile(filePath);
                }
                for (FileLoadListener listener : mLoadListeners) {
                    listener.onLoadSuccessful(filePath);
                }
                if (mDownLoadQueue.containsKey(getFileName(filePath))) {
                    mDownLoadQueue.remove(getFileName(filePath));
                }
            }

            @Override
            public void onLoadStopped(String filePath) {
                downloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
                saveDownloadInfoToDB(downloadInfo);
                for (FileLoadListener listener : mLoadListeners) {
                    listener.onLoadStopped(filePath);
                }
                if (mDownLoadQueue.containsKey(getFileName(filePath))) {
                    mDownLoadQueue.remove(getFileName(filePath));
                }
            }
        };
        FileLoader loader = FileLoader.getInstance();
        FileLoadOptions options = new FileLoadOptions();
        options.setFilePath(tempPath);
        options.setUrl(url);
        mDownLoadQueue.put(downloadInfo.getLocalFileName(), loader.fileDownLoad(options, fileLoadListener));
    }

    public void pauseDownload(String fileName) {
        if (mDownLoadQueue.containsKey(fileName)) {
            mDownLoadQueue.get(fileName).stop();
            mDownLoadQueue.remove(fileName);
        }
    }

    public String getTempPath(String fileName, int appointmentId) {
        return SdManager.getInstance().getOrderVideoPath("temp/" + fileName, appointmentId + "");
    }

    public String getUrl(MediaDownloadInfo downloadInfo) {
        return AppConfig.getDfsUrl() + "3004/1/" + downloadInfo.getLocalFileName();
    }

    public String getFilePath(String fileName, int appointmentId) {
        return SdManager.getInstance().getOrderVideoPath(fileName, appointmentId + "");
    }

    public String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 保存下载信息到数据库
     *
     * @param downloadInfo 下载信息
     */
    private void saveDownloadInfoToDB(final MediaDownloadInfo downloadInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->saveMaterialTask()->taskInfo:" + downloadInfo.toString());
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    Cursor cursor = database.query(MediaDownloadContract.MediaDownloadEntry.TABLE_NAME, null,
                            MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME + " = ?", new String[]{downloadInfo.getLocalFileName()}, null, null, null, null);
                    if (cursor.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID, downloadInfo.getAppointmentId());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME, downloadInfo.getLocalFileName());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_DOWNLOAD_STATE, downloadInfo.getState());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_CURRENT_SIZE, downloadInfo.getCurrentSize());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_TOTAL_SIZE, downloadInfo.getTotalSize());
                        String whereClause = MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME
                                + " = ?";
                        String[] whereArgs = new String[]{downloadInfo.getLocalFileName()};
                        database.update(MediaDownloadContract.MediaDownloadEntry.TABLE_NAME, values, whereClause, whereArgs);
                        cursor.close();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID, downloadInfo.getAppointmentId());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME, downloadInfo.getLocalFileName());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_DOWNLOAD_STATE, downloadInfo.getState());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_CURRENT_SIZE, downloadInfo.getCurrentSize());
                        values.put(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_TOTAL_SIZE, downloadInfo.getTotalSize());
                        database.insert(MediaDownloadContract.MediaDownloadEntry.TABLE_NAME, null, values);
                    }
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

    public void getDownloadInfoFromDB(final String fileName, final GetMediaDownloadCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<MediaDownloadInfo>() {
            @Override
            public AsyncResult<MediaDownloadInfo> runOnDBThread(AsyncResult<MediaDownloadInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                try {
                    String sql = "SELECT * FROM "
                            + MediaDownloadContract.MediaDownloadEntry.TABLE_NAME
                            + " WHERE "
                            + MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{fileName});
                    MediaDownloadInfo mediaDownloadInfo = null;
                    while (cursor.moveToNext()) {
                        mediaDownloadInfo = new MediaDownloadInfo();
                        mediaDownloadInfo.setLocalFileName(fileName);
                        mediaDownloadInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        mediaDownloadInfo.setState(cursor.getInt(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_DOWNLOAD_STATE)));
                        mediaDownloadInfo.setTotalSize(cursor.getLong(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_TOTAL_SIZE)));
                        mediaDownloadInfo.setCurrentSize(cursor.getLong(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_CURRENT_SIZE)));
                    }
                    Logger.logI(Logger.APPOINTMENT, "getDownloadInfoFromDB-->" + mediaDownloadInfo.getCurrentSize() + "/" + mediaDownloadInfo.getTotalSize());
                    asyncResult.setData(mediaDownloadInfo);
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
            public void runOnUIThread(AsyncResult<MediaDownloadInfo> asyncResult) {
                callback.onGetMediaDownload(asyncResult.getData());
            }
        });
    }

    public void getDownloadInfosFromDB(final int appointmentId, final GetMediaDownloadListCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Map<String, MediaDownloadInfo>>() {
            @Override
            public AsyncResult<Map<String, MediaDownloadInfo>> runOnDBThread(AsyncResult<Map<String, MediaDownloadInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                Map<String, MediaDownloadInfo> mediaDownloadInfos = new HashMap<>();
                try {
                    String sql = "SELECT * FROM "
                            + MediaDownloadContract.MediaDownloadEntry.TABLE_NAME
                            + " WHERE "
                            + MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID
                            + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(appointmentId)});
                    MediaDownloadInfo mediaDownloadInfo;
                    while (cursor.moveToNext()) {
                        mediaDownloadInfo = new MediaDownloadInfo();
                        mediaDownloadInfo.setLocalFileName(cursor.getString(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME)));
                        mediaDownloadInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        mediaDownloadInfo.setState(cursor.getInt(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_DOWNLOAD_STATE)));
                        mediaDownloadInfo.setTotalSize(cursor.getLong(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_TOTAL_SIZE)));
                        mediaDownloadInfo.setCurrentSize(cursor.getLong(cursor.getColumnIndex(MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_CURRENT_SIZE)));
                        mediaDownloadInfos.put(mediaDownloadInfo.getLocalFileName(), mediaDownloadInfo);
                    }
                    asyncResult.setData(mediaDownloadInfos);
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
            public void runOnUIThread(AsyncResult<Map<String, MediaDownloadInfo>> asyncResult) {
                callback.onGetMediaDownloadList(asyncResult.getData());
            }
        });
    }

    /**
     * 删除数据库中下载信息记录
     *
     * @param fileName
     */
    public void deleteDownloadInfoToDB(final String fileName) {
        mDBManager.submitDatabaseTask(new DatabaseTask<MediaDownloadInfo>() {
            @Override
            public AsyncResult<MediaDownloadInfo> runOnDBThread(AsyncResult<MediaDownloadInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    database.delete(MediaDownloadContract.MediaDownloadEntry.TABLE_NAME,
                            MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME + " =?",
                            new String[]{fileName});
                    database.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<MediaDownloadInfo> asyncResult) {
            }
        });
    }

    public interface GetMediaDownloadCallback {
        void onGetMediaDownload(MediaDownloadInfo mediaDownloadInfo);
    }

    public interface GetMediaDownloadListCallback {
        void onGetMediaDownloadList(Map<String, MediaDownloadInfo> mediaDownloadInfos);
    }
}
