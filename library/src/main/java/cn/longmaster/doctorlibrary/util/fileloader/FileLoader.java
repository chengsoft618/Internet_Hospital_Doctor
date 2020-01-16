package cn.longmaster.doctorlibrary.util.fileloader;


import android.content.Context;

import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileDownLoadTask;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadOptions;

/**
 * 文件下载
 * Created by JinKe on 2016-11-21.
 */
public abstract class FileLoader {

    /**
     * 文件下载实例
     */
    public static FileLoader fileLoader;

    public static FileLoader getInstance() {
        if (fileLoader == null) {
            fileLoader = new FileLoaderImp();
        } else {
            if (!fileLoader.isInit()) {
                throw new IllegalStateException("fileLoader，请在Application.onCreate()中调用init方法初始化");
            }
        }
        return fileLoader;
    }

    public abstract void init(Context context);

    public abstract void init(int downloadThreadSize);

    /**
     * imageLoader是否初始化
     *
     * @return true：已经初始化， false：未初始化
     */
    public abstract boolean isInit();

    /**
     * 文件下载
     *
     * @param options
     * @param listener
     * @return
     */
    public abstract FileDownLoadTask fileDownLoad(FileLoadOptions options, FileLoadListener listener);

}
