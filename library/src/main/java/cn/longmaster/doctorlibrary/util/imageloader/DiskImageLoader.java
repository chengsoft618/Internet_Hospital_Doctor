package cn.longmaster.doctorlibrary.util.imageloader;

import android.graphics.Bitmap;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.longmaster.doctorlibrary.util.thread.AppAsyncTask;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.util.thread.SingleThreadPool;


public class DiskImageLoader {
    private SingleThreadPool executorService;

    public DiskImageLoader() {
        this(new SingleThreadPool());
    }

    public DiskImageLoader(SingleThreadPool executorService) {
        this.executorService = executorService;
    }

    /**
     * 加载本地图片
     *
     * @param imageLoadOptions     加载参数
     * @param onloadResultListener 加载结果监听器
     * @return 加载任务
     */
    public LoadTask loadBitmap(ImageLoadOptions imageLoadOptions, OnloadResultListener onloadResultListener) {
        DiskLoadTask diskLoadTask = new DiskLoadTask(imageLoadOptions, onloadResultListener);
        Future<AsyncResult<Bitmap>> future = diskLoadTask.execute(executorService);
        diskLoadTask.future = future;
        return diskLoadTask;
    }

    /**
     * 加载结果监听器
     *
     * @author zdxing 2015年1月23日
     */
    public static interface OnloadResultListener {
        /**
         * 加载失败
         */
        public static final int RESULT_LOAD_FAAILED = -1;
        /**
         * 加载成功
         */
        public static final int RESULT_LOAD_SUCCESSFUN = 1;
        /**
         * 要加载的文件不存在
         */
        public static final int RESULT_FILE_NOT_EXISTS = -2;

        /**
         * 加载结果
         *
         * @param result 1表示加载成功
         * @param bitmap 加载成功的图片，当且仅当result=1时，该值有意义
         */
        void onLoadResult(int result, Bitmap bitmap);
    }

    public static interface LoadTask {
        public void cancle();

        public Bitmap get();
    }

    private class DiskLoadTask extends AppAsyncTask<Bitmap> implements LoadTask {
        private ImageLoadOptions imageLoadOptions;
        private OnloadResultListener onloadResultListener;
        private Future<AsyncResult<Bitmap>> future;

        public AtomicBoolean isCancle = new AtomicBoolean(false);

        public DiskLoadTask(ImageLoadOptions imageLoadOptions, OnloadResultListener onloadResultListener) {
            super();
            this.imageLoadOptions = imageLoadOptions;
            this.onloadResultListener = onloadResultListener;
        }

        @Override
        protected void runOnBackground(AsyncResult<Bitmap> asyncResult) {
            if (isCancle.get()) {
                return;
            }

            File file = new File(imageLoadOptions.getFilePath());
            if (file.exists()) {
                // 文件存在，开始加载
                int width = imageLoadOptions.getImageLoadSize().getWidth();
                int height = imageLoadOptions.getImageLoadSize().getHeight();

                Bitmap bitmap;
                if (width > 0 || height > 0) {
                    bitmap = BitmapUtils.decodeFile(file.getAbsolutePath(), width, height);
                    if (isCancle.get()) {
                        return;
                    }
                    if (bitmap != null) {
                        try {
                            ImageLoadSize imageLoadSize = imageLoadOptions.getImageLoadSize();
                            bitmap = BitmapUtils.scaleBitmap(bitmap, width, height, imageLoadSize.getImageScaleType());
                        } catch (Exception e) {
                            e.printStackTrace();
                            bitmap = null;
                        }
                    }
                } else {
                    bitmap = BitmapUtils.decodeFile(imageLoadOptions.getFilePath());
                }
                if (bitmap == null) {
                    asyncResult.setResult(OnloadResultListener.RESULT_LOAD_FAAILED);
                } else {
                    asyncResult.setResult(OnloadResultListener.RESULT_LOAD_SUCCESSFUN);
                    asyncResult.setData(bitmap);
                }
            } else {
                // 文件不存在
                asyncResult.setResult(OnloadResultListener.RESULT_FILE_NOT_EXISTS);
            }
        }

        @Override
        protected void runOnUIThread(AsyncResult<Bitmap> asyncResult) {
            if (!isCancle.get()) {
                onloadResultListener.onLoadResult(asyncResult.getResult(), asyncResult.getData());
            }
        }

        @Override
        public void cancle() {
            isCancle.set(true);
        }

        @Override
        public Bitmap get() {
            try {
                return future.get().getData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
