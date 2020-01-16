package cn.longmaster.doctorlibrary.util.thread;

import android.os.Bundle;

/**
 * 异步任务执行完后返回UI的结果
 * Created by yangyong on 2015/7/13.
 */
public class AsyncResult<Data> {
    private int result;
    private Bundle bundle = new Bundle();
    private Data data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Bundle getBundle() {
        return bundle;
    }

    /**
     * 快速的从bundle中获取结果
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * 快速的放入一个结果到bundle中
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        bundle.putString(key, value);
    }

    /**
     * 快速的从bundle中获取结果
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return bundle.getInt(key);
    }

    /**
     * 快速的放入一个结果到bundle中
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        bundle.putInt(key, value);
    }
}
