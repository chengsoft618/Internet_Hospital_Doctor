package cn.longmaster.hospital.doctor.core.manager.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.StorageUtil;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.utils.NotificationHelper;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;


/**
 * 文件下载管理类
 * Created by Yang² on 2018/4/4.
 */

public class FileDownLoadManager extends BaseManager {
    private Map<String, FileDownLoadListener> mFileDownLoadMap;

    @Override
    public void onManagerCreate(AppApplication application) {
        mFileDownLoadMap = new HashMap<>();
    }

    public boolean downloadingFile(String url) {
        return mFileDownLoadMap.containsKey(url);
    }

    public void startDownload(final Context context, final String fileUrl, final String title, final FileDownLoadListener fileDownLoadListener) {
        // 判断有无SDCard
        if (!StorageUtil.checkSdcard()) {
            ToastUtils.showShort(R.string.update_insert_sdcard);
            return;
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (!downloadingFile(fileUrl)) {
                downloadFile(context, fileUrl, title, null, fileDownLoadListener);
            }
        });
    }

    /**
     * @param context
     * @param fileUrl
     * @param title
     * @param filePath
     * @param fileDownLoadListener
     */
    public void startDownload(final Context context, final String fileUrl, final String title, final String filePath, final FileDownLoadListener fileDownLoadListener) {
        // 判断有无SDCard
        if (!StorageUtil.checkSdcard()) {
            ToastUtils.showShort(R.string.update_insert_sdcard);
            return;
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (!downloadingFile(fileUrl)) {
                downloadFile(context, fileUrl, title, filePath, fileDownLoadListener);
            }
        });
    }

    /**
     * 下载apk文件
     *
     * @param context 上下文
     */
    public void downloadFile(final Context context, final String fileUrl, final String title, String filePath, FileDownLoadListener fileDownLoadListener) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String fileName;
        if (StringUtils.isTrimEmpty(filePath)) {
            fileName = getFilePath(fileUrl);
        } else {
            fileName = filePath + getFileName(fileUrl);
        }
        try {
            mFileDownLoadMap.put(fileUrl, fileDownLoadListener);
            final FileDownloadPercentInfo percentInfo = new FileDownloadPercentInfo();
//            mPercentInfoMap.put(fileUrl,percentInfo);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showDownloadingNotification(context, fileUrl, title, percentInfo);
                }
            });

            // 创建文件夹
            File dowloadDirFilePath = new File(SdManager.getInstance().getDownloadPath());
            // 判断有无文件夹
            if (!dowloadDirFilePath.exists()) {
                dowloadDirFilePath.mkdirs();
            }

            // 创建文件
            File file = new File(fileName);
            // 判断是否存在文件
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                percentInfo.currentFileSize = fileInputStream.available();
                fileInputStream.close();
            } else {
                percentInfo.currentFileSize = 0;
                percentInfo.percent = 0;
                file.createNewFile();
            }

            URL url = new URL(fileUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "NetFox");
            String property = "bytes=" + percentInfo.currentFileSize + "-";
            httpURLConnection.setRequestProperty("RANGE", property);
            inputStream = httpURLConnection.getInputStream();
            /* 将文件写入暂存盘 */
            fileOutputStream = new FileOutputStream(file, true);
            byte[] bytes = new byte[1024 * 4];

            do {
                int serverSize = httpURLConnection.getContentLength();
                int liReadLength = 0;
                synchronized (inputStream) {
                    if (downloadingFile(fileUrl)) {
                        liReadLength = inputStream.read(bytes);
                    }
                }
                if (liReadLength <= 0) {
                    if (fileDownLoadListener != null) {
                        fileDownLoadListener.onFileDownLoadFinished(fileUrl, fileName);
                    }
                    mFileDownLoadMap.remove(fileUrl);
                    break;
                }
                percentInfo.currentFileSize += liReadLength;
                double percent = ((double) percentInfo.currentFileSize) / ((double) serverSize);
                percentInfo.percent = (int) (percent * 100);
                synchronized (fileOutputStream) {
                    if (downloadingFile(fileUrl)) {
                        fileOutputStream.write(bytes, 0, liReadLength);
                        if (fileDownLoadListener != null) {
                            fileDownLoadListener.onProgressChanged(fileUrl, percentInfo.percent);
                        }
                    }
                }
                SystemClock.sleep(10);
            } while (downloadingFile(fileUrl));
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (downloadingFile(fileUrl)) {
                try {
                    if (fileOutputStream != null) {
                        synchronized (fileOutputStream) {
                            try {
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (inputStream != null) {
                        synchronized (inputStream) {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fileDownLoadListener != null) {
                    fileDownLoadListener.onFileDownLoadFinished(fileUrl, fileName);
                }
                mFileDownLoadMap.remove(fileUrl);
            }
        }
    }

    public void showDownloadingNotification(final Context context, final String fileUrl, final String title, final FileDownloadPercentInfo percentInfo) {
        Runnable runnable = () -> {
            Handler handler = new Handler(Looper.getMainLooper());
            NotificationHelper helper = new NotificationHelper(context);
            final int notificationID = fileUrl.hashCode();
            final NotificationCompat.Builder builder = helper.getNotification(NotificationHelper.IMPORTANCE_LOW)
                    .setSmallIcon(R.mipmap.ic_launcher_small)
                    .setContentTitle(context.getString(R.string.file_download_downloading, title))
                    .setOngoing(true)
                    .setAutoCancel(false);
            while (downloadingFile(fileUrl)) {
                handler.post(() -> {
                    builder.setProgress(100, percentInfo.percent, false);
                    builder.setContentText(percentInfo.percent + "%");
                    helper.notify(notificationID, builder);
                });
                SystemClock.sleep(100);
            }
            helper.cancel(notificationID);
        };
        AppExecutors.getInstance().networkIO().execute(runnable);
    }

    public String getFileName(String fileUrl) {
        return (fileUrl.substring(fileUrl.lastIndexOf("/") + 1).replace(".materialfile.", ""));
    }

    public String getFilePath(String fileUrl) {
        return SdManager.getInstance().getDownloadPath() + getFileName(fileUrl);
    }

    public interface FileDownLoadListener {
        void onProgressChanged(String fileUrl, int percent);

        void onFileDownLoadFinished(String fileUrl, String downLoadPath);
    }

    class FileDownloadPercentInfo {
        long currentFileSize;
        int percent;
    }
}
