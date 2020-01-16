package cn.longmaster.doctorlibrary.util.fileloader.downloader;

/**
 * 文件下载监听
 * Created by JinKe on 2016-11-21.
 */
public interface FileLoadListener {
    /**
     * 图片开始下载了，只有本地图片不存在时，本方法才会被执行
     */
    public void onStartDownload(String filePath);

    /**
     * 进度监听
     *
     * @param totalSize   总大小
     * @param currentSize 当前大小
     */
    void onLoadProgressChange(String filePath, long totalSize, long currentSize);

    /**
     * 加载失败的回调
     *
     * @param reason 失败原因
     */
    public void onLoadFailed(String filePath, String reason);

    /**
     * 图片加载成功的回调
     */
    public void onLoadSuccessful(String filePath);

    /**
     * 下载停止
     *
     * @param filePath
     */
    public void onLoadStopped(String filePath);

}
