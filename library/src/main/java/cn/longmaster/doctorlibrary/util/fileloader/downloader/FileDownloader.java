package cn.longmaster.doctorlibrary.util.fileloader.downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.longmaster.doctorlibrary.util.log.Logger;

/**
 * 文件下载器
 * Created by JinKe on 2016-11-21.
 */
public class FileDownloader {

    private ExecutorService executorService;

    private Map<String, Entry> tasks = new HashMap<String, Entry>();

    public FileDownloader() {
        this(3);
    }

    public FileDownloader(int loadThreadSize) {
        executorService = Executors.newFixedThreadPool(loadThreadSize);
    }

    public FileDownLoadTask download(String filePath, String url, FileDownLoaderListener fileDownLoaderListener) {
        Entry entry = tasks.get(filePath);
        if (entry == null) {
            cn.longmaster.doctorlibrary.util.fileloader.downloader.DownloadTask downloadTask = new cn.longmaster.doctorlibrary.util.fileloader.downloader.DownloadTask(filePath, url);
            entry = new Entry(filePath, downloadTask);
            tasks.put(filePath, entry);
            executorService.execute(downloadTask);
        }
        entry.add(fileDownLoaderListener);

        FileDownLoadTask fileDownLoadTask = new FileDownLoadTask(entry, fileDownLoaderListener);
        return fileDownLoadTask;
    }

    public void stop(String filePath) {
        if (tasks.get(filePath) != null) {
            tasks.get(filePath).stopTask();
        }
    }

    /**
     * 是否正在下载
     *
     * @param filePath 文件的本地保存路径
     * @return true:当前文件正在下载
     */
    public boolean isDownloading(String filePath) {
        return tasks.containsKey(filePath);
    }

    /**
     * 下载任务
     *
     * @author zdxing 2015年1月26日
     */
    public class FileDownLoadTask {
        private Entry entry;
        private FileDownLoaderListener mFileDownLoaderListener;

        public FileDownLoadTask(Entry entry, FileDownLoaderListener fileDownLoaderListener) {
            super();
            this.entry = entry;
            this.mFileDownLoaderListener = fileDownLoaderListener;
        }

        /**
         * 取消下载，注意：只会取消未开始的下载，正在下载的任务不会被取消
         */
        public void cancle() {
            entry.remove(mFileDownLoaderListener);
        }
    }

    public class Entry implements FileDownLoaderListener {
        private String localFilePath;
        cn.longmaster.doctorlibrary.util.fileloader.downloader.DownloadTask downloadTask;
        List<FileDownLoaderListener> mFileDownLoaderListenerList = new ArrayList<FileDownLoaderListener>();

        public Entry(String filePath, cn.longmaster.doctorlibrary.util.fileloader.downloader.DownloadTask downloadTask) {
            this.localFilePath = filePath;
            this.downloadTask = downloadTask;
            this.downloadTask.setFileDownloadListener(this);
        }

        public void remove(FileDownLoaderListener fileDownLoaderListener) {
            if (mFileDownLoaderListenerList.remove(fileDownLoaderListener)) {

                if (mFileDownLoaderListenerList.size() == 0 && downloadTask.cancel()) {
                    // 取消未开始的任务成功
                    tasks.remove(localFilePath);
                }
            }
        }

        public void stopTask() {
            downloadTask.stopTask();
        }

        public void add(FileDownLoaderListener fileDownLoaderListener) {
            mFileDownLoaderListenerList.add(fileDownLoaderListener);
        }

        @Override
        public void onProgressChange(String filePath, long totalSize, long currentSize) {
            Logger.logD(Logger.APPOINTMENT, "->FileDownloader->onProgressChange->currentSize:" + currentSize);
            for (FileDownLoaderListener listener : mFileDownLoaderListenerList) {
                listener.onProgressChange(filePath, totalSize, currentSize);
            }
        }

        @Override
        public void onDownloadFinish(String filePath, int downloadResult) {
            for (FileDownLoaderListener listener : mFileDownLoaderListenerList) {
                listener.onDownloadFinish(filePath, downloadResult);
            }
            tasks.remove(localFilePath);
        }
    }
}
