package cn.longmaster.doctorlibrary.util.imageloader;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.io.File;
import java.util.List;

import cn.longmaster.doctorlibrary.util.imageloader.cache.BitmapMemoryCache;
import cn.longmaster.doctorlibrary.util.imageloader.downloader.ImageDownloader;
import cn.longmaster.doctorlibrary.util.thread.AppAsyncTask;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.util.thread.SingleThreadPool;


public class ImageLoaderImp extends ImageLoader {
    private boolean isInit = false;

    /**
     * 内存缓存
     */
    private BitmapMemoryCache bitmapMemoryCache;

    /**
     * 磁盘缓存
     */
    private DiskCacheImageLoader diskCacheManager;

    /**
     * 磁盘图片加载器
     */
    private DiskImageLoader diskBitmapLoader;

    /**
     * 图片下载器
     */
    private ImageDownloader imageDownloader;

    /**
     * 图片解码线程池， 单任务
     */
    private SingleThreadPool executorService = new SingleThreadPool();

    ImageLoaderImp() {
        isInit = false;
    }

    @Override
    public void init(int memoryCacheSize, int diskCacheSize, String diskCacheDir, int downloadThreadSize) {
        // 初始化缓存
        bitmapMemoryCache = new BitmapMemoryCache(memoryCacheSize);
        diskCacheManager = new DiskCacheImageLoader(executorService, diskCacheSize, diskCacheDir);

        // 本地图片加载器
        diskBitmapLoader = new DiskImageLoader(executorService);

        /** 图片下载器 */
        imageDownloader = new ImageDownloader(downloadThreadSize);

        isInit = true;
    }

    @Override
    public void init(Context context) {
        int memoryCacheSize = getMemoryCacheSize(context);

        int diskCacheSize = 30 * 1024 * 1024;
        String sha1Key = Encoder.encodeBySHA1(getUIPName(context));
        String dirName = "doctor/imgCache_" + sha1Key;
        String diskCacheDir = new File(context.getCacheDir(), dirName).getAbsolutePath();

        this.init(memoryCacheSize, diskCacheSize, diskCacheDir, 2);
    }

    private static String getUIPName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private int getMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        if (hasHoneycomb() && isLargeHeap(context)) {
            memoryClass = getLargeMemoryClass(am);
        }
        return 1024 * 1024 * memoryClass / 8;
    }

    private static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isLargeHeap(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static int getLargeMemoryClass(ActivityManager am) {
        return am.getLargeMemoryClass();
    }

    @Override
    public ImageLoadTask loadImage(ImageLoadOptions imageLoadOptions, ImageloadListener imageloadListener) {
        ImageLoadTask imageLoadTask = new ImageLoadTask(imageLoadOptions, imageloadListener, bitmapMemoryCache,
                diskCacheManager, diskBitmapLoader, imageDownloader);
        imageLoadTask.start();
        return imageLoadTask;
    }

    @Override
    public boolean isInit() {
        return isInit;
    }

    @Override
    public int getMemoryCacheSize() {
        return bitmapMemoryCache.getCurrentSize();
    }

    @Override
    public int getDiskCacheSize() {
        return diskCacheManager.getSize();
    }

    @Override
    public void clear(final DiskCacheImageLoader.OnClearCacheListener onClearCacheListener) {
        AppAsyncTask<Void> xingAsyncTask = new AppAsyncTask<Void>() {
            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                bitmapMemoryCache.clear();
                diskCacheManager.clear(onClearCacheListener);
            }
        };
        xingAsyncTask.execute(executorService);
    }

    @Override
    public void removeCache(String filePath) {
        final String encodeKey = Encoder.encodeBySHA1(filePath);
        List<String> allKey = bitmapMemoryCache.getAllKey();
        for (String key : allKey) {
            if (key.startsWith(encodeKey)) {
                bitmapMemoryCache.remove(key);
            }
        }

        AppAsyncTask<Void> xingAsyncTask = new AppAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                List<String> allKey = diskCacheManager.getAllKey();
                for (String key : allKey) {
                    if (key.startsWith(encodeKey)) {
                        diskCacheManager.remove(key);
                    }
                }
            }

            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                super.runOnUIThread(asyncResult);
            }
        };
        xingAsyncTask.execute(executorService);
    }
}
