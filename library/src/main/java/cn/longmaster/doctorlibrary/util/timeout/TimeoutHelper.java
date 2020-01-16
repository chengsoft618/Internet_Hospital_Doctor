package cn.longmaster.doctorlibrary.util.timeout;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 记时器，超时回调，注意：超时回调在线程中执行
 * Created by yangyong on 2015/7/3.
 *
 * @param <T>
 */
public class TimeoutHelper<T> {

    private static Timer timer = new Timer();
    private Map<T, TimerTask> timeroutMap = new HashMap<T, TimerTask>();
    private OnTimeoutCallback<T> callback;
    private int id;

    public TimeoutHelper() {
    }

    public void setCallback(OnTimeoutCallback<T> onTimeoutCallback) {
        this.callback = onTimeoutCallback;
    }

    /**
     * 取消超时
     *
     * @param t
     * @return
     */
    public boolean cancel(T t) {
        boolean result = false;
        synchronized (timeroutMap) {
            TimerTask task = timeroutMap.remove(t);
            if (task != null) {
                task.cancel();
                result = true;
            }
        }
        return result;
    }

    /**
     * 判断是否正在计时中。。。。
     *
     * @param t
     * @return
     */
    public boolean isRequest(T t) {
        boolean result = false;
        synchronized (timeroutMap) {
            if (timeroutMap.containsKey(t)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 启动超时
     *
     * @param t           记时ID
     * @param timeoutTime 超时时间
     */
    public void request(T t, int timeoutTime) {
        synchronized (timeroutMap) {
            if (timeroutMap.containsKey(t)) {
                return;
            } else {
                TimeoutTask task = new TimeoutTask(t);
                timeroutMap.put(t, task);
                timer.schedule(task, timeoutTime);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private class TimeoutTask extends TimerTask {
        T taskId;

        public TimeoutTask(T taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            TimerTask timerTask;
            synchronized (timeroutMap) {
                timerTask = timeroutMap.remove(taskId);
            }

            if (timerTask != null && callback != null) {
                callback.onTimeout(TimeoutHelper.this, taskId);
            }
        }
    }

    public interface OnTimeoutCallback<T> {
        /**
         * 超时的回调，注意：该方法在线程中执行，不要直接更新UI，且不要长时间占用该线程
         */
        void onTimeout(TimeoutHelper<T> timeoutHelper, T taskId);
    }

}
