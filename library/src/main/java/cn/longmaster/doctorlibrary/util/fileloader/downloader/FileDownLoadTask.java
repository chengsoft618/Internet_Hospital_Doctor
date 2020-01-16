package cn.longmaster.doctorlibrary.util.fileloader.downloader;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 文件下载任务
 * Created by JinKe on 2016-11-21.
 */
public class FileDownLoadTask {

    public static final int NOT_START = 0;
    public static final int LOADING = 1;
    public static final int LOAD_CANCEL = 2;
    public static final int LOAD_FAIL = 3;

    /**
     * 当前的加载状态
     */
    private int loadState = NOT_START;
    private FileLoadListener mFileLoadListener;
    private FileLoadOptions mFileLoadOptions;
    private FileDownloader mFileDownloader;
    private FileDownloader.FileDownLoadTask mFileDownloadTask;

    @IntDef({NOT_START, LOADING, LOAD_CANCEL, LOAD_FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadState {
    }


    public FileDownLoadTask(FileLoadOptions loadOptions, FileDownloader downloader, FileLoadListener listener) {
        this.mFileLoadOptions = loadOptions;
        this.mFileDownloader = downloader;
        this.mFileLoadListener = listener;
        loadState = NOT_START;
    }

    public void start() {
        if (loadState != NOT_START) {
            throw new IllegalStateException("该方法只能调用一次");
        }

        loadState = LOADING;
        // 开始网络加载
        FileDownLoaderListener fileDownloadListener = new FileDownLoaderListener() {
            @Override
            public void onProgressChange(String filePath, long totalSize, long currentSize) {
                if (loadState == LOAD_CANCEL) {
                    return;
                }

                mFileLoadListener.onLoadProgressChange(filePath, totalSize, currentSize);
            }

            @Override
            public void onDownloadFinish(String filePath, int downloadResult) {
                if (loadState == LOAD_CANCEL) {
                    return;
                }

                mFileDownloadTask = null;
                if (downloadResult == FileDownLoaderListener.SUCCESSFUL) {
                    mFileLoadListener.onLoadSuccessful(filePath);
                } else if (downloadResult == FileDownLoaderListener.STOPPED) {
                    mFileLoadListener.onLoadStopped(filePath);
                } else {
                    mFileLoadListener.onLoadFailed(filePath, "网络文件下载失败");
                    loadState = LOAD_FAIL;
                }
            }
        };

        String filePath = mFileLoadOptions.getFilePath();
        String url = mFileLoadOptions.getUrl();
//        if (FileUtil.isFileExist(filePath))
//            FileUtil.deleteFile(filePath);
        mFileDownloadTask = mFileDownloader.download(filePath, url, fileDownloadListener);
        mFileLoadListener.onStartDownload(filePath); // 通知， 开始下载了
    }

    public void stop() {
        mFileDownloader.stop(mFileLoadOptions.getFilePath());
    }
}
