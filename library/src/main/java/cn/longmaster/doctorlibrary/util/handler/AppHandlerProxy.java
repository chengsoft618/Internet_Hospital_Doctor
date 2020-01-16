package cn.longmaster.doctorlibrary.util.handler;

import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * Created by yangyong on 2015/7/2.
 */
public class AppHandlerProxy {

    private static OMMap<Integer, MessageHandler.HandlerMessageListener> mHandlerMessageListenerMap = new OMMap<Integer, MessageHandler.HandlerMessageListener>();

    private static MessageHandler.HandlerMessageListener handlerMessageListener = new MessageHandler.HandlerMessageListener() {
        @Override
        public void handleMessage(Message msg) {
            List<MessageHandler.HandlerMessageListener> handlerMessageListeners = mHandlerMessageListenerMap.get(msg.what);
            if (handlerMessageListeners != null) {
                for (MessageHandler.HandlerMessageListener handlerMessageListener : handlerMessageListeners) {
                    handlerMessageListener.handleMessage(msg);
                }
            }
        }
    };
    private static MessageHandler m_MessageHandler = new MessageHandler(handlerMessageListener);

    /**
     * 该类不允许拥有实例
     */
    private AppHandlerProxy() {

    }

    /**
     * 在UI线程中执行
     *
     * @param runnable 需要在UI中执行的Runable
     * @see #post(Runnable)
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isOnUIThread()) {
            // 在主线程中调用，直接执行
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /**
     * 判断当前线程是不是UI线程
     *
     * @return
     */
    public static boolean isOnUIThread() {
        boolean result;
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            // 在主线程中调用
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 发送消息
     *
     * @param what
     */
    public static void sendEmptyMessage(int what) {
        m_MessageHandler.sendEmptyMessage(what);
    }

    /**
     * obtainMessage
     */
    public static Message obtainMessage() {
        return m_MessageHandler.obtainMessage();
    }

    /**
     * sendEmptyMessageDelayed
     *
     * @param what
     */
    public static void sendEmptyMessageDelayed(int what, int delayMillis) {
        m_MessageHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * sendMessageDelayed
     */
    public static void sendMessageDelayed(Message message, int delayMillis) {
        m_MessageHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * 发送一个全局消息，所有注册了该消息ID的接收器都能收到该消息
     */
    public static void sendMessage(Message message) {
        m_MessageHandler.sendMessage(message);
    }

    /**
     * post一个代码段到UI执行
     *
     * @param runnable
     */
    public static void post(Runnable runnable) {
        m_MessageHandler.post(runnable);
    }

    public static void postDelayed(Runnable runnable, int delayMillis) {
        m_MessageHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 注册消息接受器
     * 方法注销，否则会造成内存泄露
     *
     * @param what                   消息ID
     * @param handlerMessageListener 消息接收器
     */
    public static void registHandlerMessageListener(int what, MessageHandler.HandlerMessageListener handlerMessageListener) {
        mHandlerMessageListenerMap.put(what, handlerMessageListener);
    }

    /**
     * 注销消息监听器
     *
     * @param what
     * @param handlerMessageListener
     */
    public static void unregistHandlerMessageListener(int what, MessageHandler.HandlerMessageListener handlerMessageListener) {
        mHandlerMessageListenerMap.remove(what, handlerMessageListener);
    }

}
