package cn.longmaster.doctorlibrary.util.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

/**
 * 消息发送器
 * Created by yangyong on 2015/7/2.
 */
public class MessageSender {

    private static SparseArray<Handler> mHandlerArray = new SparseArray<Handler>();

    public static void addHandler(Integer what, Handler handler) {
        mHandlerArray.put(what, handler);
    }

    public static void delHandler(Integer what) {
        if (mHandlerArray.get(what) != null) {
            mHandlerArray.remove(what);
        }
    }

    public static boolean sendEmptyMessage(int what) {
        Handler handler = mHandlerArray.get(what);
        if (handler == null) {
            return false;
        }

        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);

        return true;
    }

    public static boolean sendMessage(int what, Bundle data) {
        Handler handler = mHandlerArray.get(what);
        if (handler == null) {
            return false;
        }

        Message msg = new Message();
        msg.what = what;
        if (data != null) {
            msg.setData(data);
        }
        handler.sendMessage(msg);

        return true;
    }

    public static boolean sendMessage(Message msg) {
        Handler handler = mHandlerArray.get(msg.what);
        if (handler == null) {
            return false;
        }

        handler.sendMessage(msg);
        return true;
    }

    public static boolean sendMessageDelayed(Message msg, long delayMillis) {
        Handler handler = mHandlerArray.get(msg.what);
        if (handler == null) {
            return false;
        }

        handler.sendMessageDelayed(msg, delayMillis);
        return true;
    }

    public static boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Handler handler = mHandlerArray.get(what);
        if (handler == null) {
            return false;
        }

        handler.sendEmptyMessageDelayed(what, delayMillis);
        return true;
    }

}
