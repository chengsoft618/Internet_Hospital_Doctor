package cn.longmaster.doctorlibrary.util.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 对Handle封装
 * Created by yangyong on 2015/7/2.
 */
public class MessageHandler extends Handler {

    private HandlerMessageListener mOnHandlerMessageListene;
    private Set<Integer> msgWhatSet = new HashSet<Integer>();

    public MessageHandler(HandlerMessageListener onHandlerMessageListener) {
        super(Looper.getMainLooper());
        this.mOnHandlerMessageListene = onHandlerMessageListener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (mOnHandlerMessageListene != null) {
            mOnHandlerMessageListene.handleMessage(msg);
        }
    }

    /**
     * 注册全局消息,注意:全局消息ID不能重复,否则会出现问题,</br>
     * 调用该方法后记得调用{@link #unregistMessage(int)} 或者一次性调用 {@link #unregistMessages()} 注销全局消息
     *
     * @param msgWhat
     */
    public void registMessage(int msgWhat) {
        if (msgWhatSet.add(msgWhat)) {
            MessageSender.addHandler(msgWhat, this);
        }
    }

    /**
     * 注销全局消息
     *
     * @param msgWhat
     */
    public void unregistMessage(int msgWhat) {
        if (msgWhatSet.remove(msgWhat)) {
            MessageSender.delHandler(msgWhat);
        }
    }

    /**
     * 注销所有注册过的全局消息
     */
    public void unregistMessages() {
        for (Iterator<Integer> iterator = msgWhatSet.iterator(); iterator.hasNext(); ) {
            Integer id = (Integer) iterator.next();
            MessageSender.delHandler(id);
        }
        msgWhatSet.clear();
    }

    /**
     * 注册HandlerMessageListener
     *
     * @param onHandlerMessageListener
     * @see HandlerMessageListener
     */
    public void setHandlerMessageListener(HandlerMessageListener onHandlerMessageListener) {
        mOnHandlerMessageListene = onHandlerMessageListener;
    }

    public static interface HandlerMessageListener {
        public void handleMessage(Message msg);
    }

}
