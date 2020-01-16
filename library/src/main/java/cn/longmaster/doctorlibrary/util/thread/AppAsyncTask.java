package cn.longmaster.doctorlibrary.util.thread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * DServer线程池任务的模板类，封装线程间的任务调度。是简易的{@link AsyncTask}，与{@link AsyncTask}
 * 的区别是该任务执行的线程在DServer中，并且按照先进先出的方式执行任务。
 * <p>
 * <strong>注意:禁止用该类发起网络请求（下载和上传），只可以用于处理数据库逻辑、本地IO、耗时逻辑、字符串替换等阻塞UI的操作。</strong>
 * </p>
 * <p>
 * 最后记得执行{@link #execute()}方法
 * </p>
 * <p/>
 * Created by yangyong on 2015/7/13.
 */
public class AppAsyncTask<Ddta> implements Callable<AsyncResult<Ddta>> {
    private static SingleThreadPool singleThreadPool = new SingleThreadPool(60, TimeUnit.SECONDS, "SingleThreadPool",
            Thread.NORM_PRIORITY - 1);
    private static Handler handler = new Handler(Looper.getMainLooper());

    private AsyncResult<Ddta> mResult = new AsyncResult<Ddta>();

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<Ddta>> execute() {
        return execute(singleThreadPool);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<Ddta>> execute(SingleThreadPool executor) {
        return executor.submit(this);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<Ddta>> execute(ExecutorService executor) {
        return executor.submit(this);
    }

    @Override
    public AsyncResult<Ddta> call() throws Exception {
        runOnBackground(mResult);
        post();
        return mResult;
    }

    private void post() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUIThread(mResult);
            }
        });
    }

    /**
     * 该函数在线程中执行
     */
    protected void runOnBackground(AsyncResult<Ddta> asyncResult) {

    }

    /**
     * 该函数在UI线程中执行
     *
     * @param {@link #doInBackground()}执行后返回的结果
     */
    protected void runOnUIThread(AsyncResult<Ddta> asyncResult) {
    }
}