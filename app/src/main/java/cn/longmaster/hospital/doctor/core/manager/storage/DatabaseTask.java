package cn.longmaster.hospital.doctor.core.manager.storage;


import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.db.DBHelper;

/**
 * 数据库执行任务， 所有操作数据库的方法都应当使用本接口， 禁止在UI线程中操作数据库， 禁止使用本接口发起网络请求
 * Created by yangyong on 2015/7/16.
 */
public interface DatabaseTask<D> {
    /**
     * 本方法在子线程中执行， 用于操作数据库，查询的结果通过asyncResult传送到UI线程
     *
     * @param asyncResult 需要返回UI线程的数据
     * @param dbHelper    客户端数据库， 为当前登录用户的数据库
     * @return 需要返回UI线程的数据
     */
    public AsyncResult<D> runOnDBThread(AsyncResult<D> asyncResult, DBHelper dbHelper);

    /**
     * 本方法在UI线程中执行，用于回调界面，更新UI
     *
     * @param asyncResult 从子线程中返回的数据
     */
    public void runOnUIThread(AsyncResult<D> asyncResult);

    public static abstract class SimpleDatabaseTask<D> implements DatabaseTask<D> {
        /**
         * 提交数据库执行
         */
        public void submit() {
            AppApplication application = AppApplication.getInstance();
            DBManager databaseManager = application.getManager(DBManager.class);
            databaseManager.submitDatabaseTask(this);
        }
    }
}
