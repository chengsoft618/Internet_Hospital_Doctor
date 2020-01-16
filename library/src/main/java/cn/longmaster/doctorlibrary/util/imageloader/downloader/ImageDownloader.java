package cn.longmaster.doctorlibrary.util.imageloader.downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载器
 *
 * @author zdxing 2015年1月26日
 */
public class ImageDownloader {
    private ExecutorService executorService;

    private Map<String, Entry> tasks = new HashMap<String, Entry>();

    public ImageDownloader() {
        this(3);
    }

    public ImageDownloader(int sownloadThreadSize) {
        executorService = Executors.newFixedThreadPool(sownloadThreadSize);
    }

    public ImageDownLoadTask download(String filePath, String url, ImageDownloadListener imageDownloadListener) {
        Entry entry = tasks.get(filePath);
        if (entry == null) {
            DownloadTask downloadTask = new DownloadTask(filePath, url);
            entry = new Entry(filePath, downloadTask);
            tasks.put(filePath, entry);
            executorService.execute(downloadTask);
        }
        entry.add(imageDownloadListener);

        ImageDownLoadTask imageDownLoadTask = new ImageDownLoadTask(entry, imageDownloadListener);
        return imageDownLoadTask;
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
    public class ImageDownLoadTask {
        private Entry entry;
        private ImageDownloadListener imageDownloadListener;

        public ImageDownLoadTask(Entry entry, ImageDownloadListener imageDownloadListener) {
            super();
            this.entry = entry;
            this.imageDownloadListener = imageDownloadListener;
        }

        /**
         * 取消下载，注意：只会取消未开始的下载，正在下载的任务不会被取消
         */
        public void cancle() {
            entry.remove(imageDownloadListener);
        }
    }

    public class Entry implements ImageDownloadListener {
        private String localFilePath;
        DownloadTask downloadTask;
        List<ImageDownloadListener> imageDownloadListeners = new ArrayList<ImageDownloadListener>();

        public Entry(String filePath, DownloadTask downloadTask) {
            this.localFilePath = filePath;
            this.downloadTask = downloadTask;
            this.downloadTask.setImageDownloadListener(this);
        }

        public void remove(ImageDownloadListener imageDownloadListener) {
            if (imageDownloadListeners.remove(imageDownloadListener)) {

                if (imageDownloadListeners.size() == 0 && downloadTask.cancle()) {
                    // 取消未开始的任务成功
                    tasks.remove(localFilePath);
                }
            }
        }

        public void add(ImageDownloadListener imageDownloadListener) {
            imageDownloadListeners.add(imageDownloadListener);
        }

        @Override
        public void onProgressChange(String filePath, int totalSize, int currentSize) {
            for (ImageDownloadListener imageDownloadListener : imageDownloadListeners) {
                imageDownloadListener.onProgressChange(filePath, totalSize, currentSize);
            }
        }

        @Override
        public void onDownloadFinish(String filePath, DownloadResult downloadResult) {
            for (ImageDownloadListener imageDownloadListener : imageDownloadListeners) {
                imageDownloadListener.onDownloadFinish(filePath, downloadResult);
            }
            tasks.remove(localFilePath);
        }
    }
}
