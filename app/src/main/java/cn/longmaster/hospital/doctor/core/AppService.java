package cn.longmaster.hospital.doctor.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.manager.common.AppAlarmManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;

/**
 * 应用主服务
 * Created by yangyong on 2015/7/2.
 */
public class AppService extends JobIntentService {

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, AppService.class, 1, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //这个id不要和应用内的其他同志id一样，不行就写 int.maxValue()        //context.startForeground(SERVICE_ID, builder.getNotification());
//            startForeground(1, new Notification());
//        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.logW(Logger.COMMON, "开始启动服务。");

        // 等待程序初始化完全后开始
        ((AppApplication) getApplicationContext()).waitToInit();

        // 每次启动程序设置GK
        AppApplication.getInstance().getManager(DcpManager.class).openClient();
        AppApplication.getInstance().getManager(DcpManager.class).setGKDomain();
        AppApplication.getInstance().getManager(UserInfoManager.class).loadUserInfo(info -> {
            AppApplication.getInstance().getManager(UserInfoManager.class).getAgentService(info.getPesIp(), info.getPesPort());
            AppApplication.getInstance().getManager(MaterialTaskManager.class).uploadLocalTasks();
        });
        AppApplication.getInstance().setAppStarted(true);
        AppAlarmManager.getInstance().register();
        Logger.logW(Logger.COMMON, "服务启动完成");
    }
}
