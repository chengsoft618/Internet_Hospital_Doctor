package cn.longmaster.doctorlibrary.util.fileloader.downloader;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 文件下载监听
 * Created by JinKe on 2016-11-21.
 */
public interface FileDownLoaderListener {
    public static final int SUCCESSFUL = 0;//成功
    public static final int FAILED = 1;//失败
    public static final int URL_EMPTY = 2;//url为null
    public static final int FILE_EXISTS = 3;//文件已存在
    public static final int STOPPED = 4;//停止

    @IntDef({SUCCESSFUL, FAILED, URL_EMPTY, FILE_EXISTS, STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadResult {
    }

    /**
     * 进度变换
     *
     * @param filePath    文件路径
     * @param totalSize   文件总大小
     * @param currentSize 文件当前下载大小
     */
    public void onProgressChange(String filePath, long totalSize, long currentSize);

    /**
     * 文件下载结束
     *
     * @param filePath       文件路径
     * @param downloadResult 文件下载结果
     */
    public void onDownloadFinish(String filePath, @DownloadResult int downloadResult);
}
