package cn.longmaster.doctorlibrary.util.fileloader;

import android.content.Context;

import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileDownLoadTask;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileDownloader;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadOptions;

/**
 * 文件下载实例
 * Created by JinKe on 2016-11-21.
 */
public class FileLoaderImp extends FileLoader {
    private boolean isInit = false;

    private FileDownloader mFileDownloader;

    public FileLoaderImp() {
        this.isInit = false;
    }

    @Override
    public void init(Context context) {
        this.init(3);
    }

    @Override
    public void init(int downloadThreadSize) {
        mFileDownloader = new FileDownloader(downloadThreadSize);
        isInit = true;
    }

    @Override
    public boolean isInit() {
        return isInit;
    }

    @Override
    public FileDownLoadTask fileDownLoad(FileLoadOptions options, FileLoadListener listener) {
        FileDownLoadTask task = new FileDownLoadTask(options, mFileDownloader, listener);
        task.start();
        return task;
    }
}
