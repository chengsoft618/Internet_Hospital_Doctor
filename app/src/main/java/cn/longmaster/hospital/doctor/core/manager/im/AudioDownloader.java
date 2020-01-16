package cn.longmaster.hospital.doctor.core.manager.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载器
 *
 * @author YY 2015年1月26日
 */
public class AudioDownloader {
    private ExecutorService executorService;

    private Map<String, Entry> tasks = new HashMap<>();

    public AudioDownloader() {
        this(3);
    }

    public AudioDownloader(int downloadThreadSize) {
        executorService = Executors.newFixedThreadPool(downloadThreadSize);
    }

    public AudioDownLoadTask download(String filePath, String url, AudioDownloadListener listener) {
        Entry entry = tasks.get(filePath);
        if (entry == null) {
            AudioDownloadTask downloadTask = new AudioDownloadTask(filePath, url);
            entry = new Entry(filePath, downloadTask);
            tasks.put(filePath, entry);
            executorService.execute(downloadTask);
        }
        entry.add(listener);

        return new AudioDownLoadTask(entry, listener);
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
     */
    public class AudioDownLoadTask {
        private Entry entry;
        private AudioDownloadListener listener;

        public AudioDownLoadTask(Entry entry, AudioDownloadListener listener) {
            super();
            this.entry = entry;
            this.listener = listener;
        }

        /**
         * 取消下载，注意：只会取消未开始的下载，正在下载的任务不会被取消
         */
        public void cancel() {
            entry.remove(listener);
        }
    }

    public class Entry implements AudioDownloadListener {
        AudioDownloadTask downloadTask;
        List<AudioDownloadListener> downloadListeners = new ArrayList<>();
        private String localFilePath;

        public Entry(String filePath, AudioDownloadTask downloadTask) {
            this.localFilePath = filePath;
            this.downloadTask = downloadTask;
            this.downloadTask.setAudioDownloadListener(this);
        }

        public void remove(AudioDownloadListener listener) {
            if (downloadListeners.remove(listener)) {

                if (downloadListeners.size() == 0 && downloadTask.cancel()) {
                    // 取消未开始的任务成功
                    tasks.remove(localFilePath);
                }
            }
        }

        public void add(AudioDownloadListener imageDownloadListener) {
            downloadListeners.add(imageDownloadListener);
        }

        @Override
        public void onProgressChange(String filePath, int totalSize, int currentSize) {
            for (AudioDownloadListener listener : downloadListeners) {
                listener.onProgressChange(filePath, totalSize, currentSize);
            }
        }

        @Override
        public void onDownloadFinish(String filePath, DownloadResult downloadResult) {
            for (AudioDownloadListener listener : downloadListeners) {
                listener.onDownloadFinish(filePath, downloadResult);
            }
            tasks.remove(localFilePath);
        }
    }
}
