package cn.longmaster.hospital.doctor.core.manager.storage;


import cn.longmaster.doctorlibrary.util.thread.AppAsyncTask;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;

/**
 * Created by yangyong on 2015/7/15.
 */
public class DBManager extends BaseManager {
    private DBHelper mDBHelper;

    @Override
    public void onManagerCreate(AppApplication application) {
        initDatabase();
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        AppAsyncTask<Void> task = new AppAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                if (mDBHelper != null) {
                    mDBHelper.close();
                }

                // 打开数据库
                mDBHelper = DBHelper.getInstance();
                mDBHelper.getWritableDatabase();
            }
        };

        task.execute();
    }

    /**
     * 提交数据库执行任务， 操纵数据的代码必须使用该方法， 禁止在UI线程中操作数据库
     *
     * @param databaseTask 需要执行的任务
     */
    public <D> void submitDatabaseTask(final DatabaseTask<D> databaseTask) {
        AppAsyncTask<D> task = new AppAsyncTask<D>() {
            @Override
            protected void runOnBackground(AsyncResult<D> asyncResult) {
                try {
                    asyncResult = databaseTask.runOnDBThread(asyncResult, mDBHelper);
                } catch (Exception e) {
                    if (AppConfig.IS_DEBUG_MODE) {
                        // 测试环境下，抛出崩溃
                        throw new RuntimeException("数据库异常，请处理", e);
                    }
                }
            }

            @Override
            protected void runOnUIThread(AsyncResult<D> asyncResult) {
                databaseTask.runOnUIThread(asyncResult);
            }
        };
        task.execute();
    }
}
