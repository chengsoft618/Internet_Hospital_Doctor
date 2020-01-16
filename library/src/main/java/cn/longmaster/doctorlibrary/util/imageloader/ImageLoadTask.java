package cn.longmaster.doctorlibrary.util.imageloader;

import android.graphics.Bitmap;

import cn.longmaster.doctorlibrary.util.imageloader.cache.BitmapMemoryCache;
import cn.longmaster.doctorlibrary.util.imageloader.downloader.ImageDownloadListener;
import cn.longmaster.doctorlibrary.util.imageloader.downloader.ImageDownloader;


/**
 * 图片加载任务
 *
 * @author zdxing 2015年1月26日
 */
public class ImageLoadTask {

    private ImageLoadOptions imageLoadOptions;
    private ImageloadListener imageloadListener;

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
     * 当前的加载状态
     */
    private LoadState loadState = LoadState.未开始;

    /**
     * 当前状态 汉字枚举，我喜欢。。。。
     */
    public enum LoadState {
        加载完成, 加载磁盘缓存中, 加载磁盘中, 加载网络中, 未开始, 加载取消, 加载失败;
    }

    public ImageLoadTask(ImageLoadOptions imageLoadOptions, ImageloadListener imageloadListener,
                         BitmapMemoryCache bitmapMemoryCache, DiskCacheImageLoader diskCacheManager,
                         DiskImageLoader diskBitmapLoader, ImageDownloader imageDownloader) {
        this.imageLoadOptions = imageLoadOptions;
        this.imageloadListener = imageloadListener;
        this.bitmapMemoryCache = bitmapMemoryCache;
        this.diskCacheManager = diskCacheManager;
        this.diskBitmapLoader = diskBitmapLoader;
        this.imageDownloader = imageDownloader;
        loadState = LoadState.未开始;
    }

    /**
     * 开始加载
     */
    void start() {
        if (loadState != LoadState.未开始) {
            throw new IllegalStateException("该方法只能调用一次");
        }


        if (imageLoadOptions.isMemoryCacheEnable()) {
            Bitmap memoryBitmap = bitmapMemoryCache.get(imageLoadOptions.getKey());
            if (memoryBitmap != null) {
                imageloadListener.onLoadSuccessful(ImageloadListener.BitmapSource.MEMORY_CACHE, memoryBitmap);
                loadState = LoadState.加载完成;
                return;
            }
        }

        if (imageLoadOptions.isDiskCacheEnable()) {
            loadState = LoadState.加载磁盘缓存中;
            diskCacheManager.getCache(imageLoadOptions.getKey(), new DiskCacheImageLoader.OnGetDiskCacheListener() {
                @Override
                public void onGetDiskCache(String key, Bitmap bitmap) {
                    if (loadState == LoadState.加载取消) {
                        return;
                    }

                    if (bitmap != null) {
                        if (imageLoadOptions.isMemoryCacheEnable()) {
                            bitmapMemoryCache.put(imageLoadOptions.getKey(), bitmap);
                        }
                        imageloadListener.onLoadSuccessful(ImageloadListener.BitmapSource.DISK_CACHE, bitmap);
                        loadState = LoadState.加载完成;
                    } else {
                        loadSource();
                    }
                }
            });
        } else {
            loadSource();
        }

    }

    private void loadSource() {
        if (imageLoadOptions.hasUrl() && imageDownloader.isDownloading(imageLoadOptions.getFilePath())) {
            loadBitmapFromNet();
        } else {
            loadBitmapFromDisk(false);
        }
    }

    private DiskImageLoader.LoadTask loadTask;

    private void loadBitmapFromDisk(final boolean ifFromNet) {
        loadState = LoadState.加载磁盘中;
        loadTask = diskBitmapLoader.loadBitmap(imageLoadOptions, new DiskImageLoader.OnloadResultListener() {
            @Override
            public void onLoadResult(int result, Bitmap bitmap) {
                if (loadState == LoadState.加载取消) {
                    return;
                }

                loadTask = null;
                if (result == DiskImageLoader.OnloadResultListener.RESULT_LOAD_SUCCESSFUN) {
                    String key = imageLoadOptions.getKey();
                    if (imageLoadOptions.isDiskCacheEnable()) {
                        diskCacheManager.putCache(key, bitmap);
                    }

                    if (imageLoadOptions.isMemoryCacheEnable()) {
                        bitmapMemoryCache.put(key, bitmap);
                    }
                    if (ifFromNet) {
                        imageloadListener.onLoadSuccessful(ImageloadListener.BitmapSource.NETWORK, bitmap);
                    } else {
                        imageloadListener.onLoadSuccessful(ImageloadListener.BitmapSource.FILE, bitmap);
                    }
                    loadState = LoadState.加载完成;
                } else if (result == DiskImageLoader.OnloadResultListener.RESULT_FILE_NOT_EXISTS && imageLoadOptions.hasUrl()
                        && !ifFromNet) {
                    // 加载网络图片
                    loadBitmapFromNet();
                } else {
                    imageloadListener.onLoadFailed("本地图片文件加载失败！");
                    loadState = LoadState.加载失败;
                }
            }
        });
    }

    private ImageDownloader.ImageDownLoadTask imageDownLoadTask;

    private void loadBitmapFromNet() {
        loadState = LoadState.加载网络中;
        // 开始网络加载
        ImageDownloadListener imageDownloadListener = new ImageDownloadListener() {
            @Override
            public void onProgressChange(String filePath, int totalSize, int currentSize) {
                if (loadState == LoadState.加载取消) {
                    return;
                }

                imageloadListener.onLoadProgressChange(totalSize, currentSize);
            }

            @Override
            public void onDownloadFinish(String filePath, DownloadResult downloadResult) {
                if (loadState == LoadState.加载取消) {
                    return;
                }

                imageDownLoadTask = null;
                if (downloadResult == ImageDownloadListener.DownloadResult.SUCCESSFUL || downloadResult == DownloadResult.FILE_EXISTS) {
                    loadBitmapFromDisk(true);
                } else {
                    imageloadListener.onLoadFailed("网络图片加载失败");
                    loadState = LoadState.加载失败;
                }
            }
        };

        String filePath = imageLoadOptions.getFilePath();
        String url = imageLoadOptions.getUrl();
        imageDownLoadTask = imageDownloader.download(filePath, url, imageDownloadListener);
        imageloadListener.onStartDownload(); // 通知， 开始下载了
    }

    public void cancle() {
        if (loadState != LoadState.加载取消 && loadState != LoadState.加载完成 && loadState != LoadState.加载失败) {
            if (loadState == LoadState.加载磁盘中) {
                loadTask.cancle();
                loadTask = null;
            } else if (loadState == LoadState.加载网络中) {
                imageDownLoadTask.cancle();
                imageDownLoadTask = null;
            }
            loadState = LoadState.加载取消;
        }
    }

    /**
     * 获取加载状态
     */
    public LoadState getLoadState() {
        return loadState;
    }
}
