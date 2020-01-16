package cn.longmaster.doctorlibrary.util.log;

import android.support.annotation.StringDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 日志打印类，大医生应用所有日志必须采用本类来进行日志打印，以方便正式打包时对日志的控制
 * Created by yangyong on 2015/7/2.
 * Mod by Biao on 2019/9/2
 */
public class Logger {
    private static final boolean IS_DEBUG = true;
    public static final String USER = "tag_user";//用户信息
    public static final String DOCTOR = "tag_doctor";//医生
    public static final String APPOINTMENT = "tag_appointment";//预约
    public static final String ROOM = "tag_room";//诊室
    public static final String COMMON = "tag_common";//通用
    public static final String HTTP = "tag_http";//web服务器请求
    public static final String IM = "tag_im";//群组消息
    public static final String CAMERA = "tag_camera";//摄像头

    @StringDef({USER, DOCTOR, APPOINTMENT, ROOM, COMMON, HTTP, IM, CAMERA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoggerTag {
    }

    public static void logD(@LoggerTag String loggerTag, String msg) {
        if (IS_DEBUG) {
            Log.d(loggerTag, msg);
        }
    }

    public static void logI(@LoggerTag String loggerTag, String msg) {
        if (IS_DEBUG) {
            Log.i(loggerTag, msg);
        }
    }

    public static void logW(@LoggerTag String loggerTag, String msg) {
        if (IS_DEBUG) {
            Log.w(loggerTag, msg);
        }
    }

    public static void logW(@LoggerTag String loggerTag, String msg, Throwable throwable) {
        Log.w(loggerTag, msg, throwable);
    }

    public static void logE(@LoggerTag String loggerTag, String msg) {
        if (IS_DEBUG) {
            Log.e(loggerTag, msg);
        }
    }

    public static void logE(@LoggerTag String loggerTag, String msg, Throwable throwable) {
        Log.e(loggerTag, msg, throwable);
    }
}
