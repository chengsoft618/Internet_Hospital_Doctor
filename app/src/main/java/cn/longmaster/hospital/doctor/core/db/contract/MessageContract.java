package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 消息协议类
 * Created by yangyong on 2016/8/9.
 */
public final class MessageContract {
    public MessageContract() {
    }

    public static abstract class MessageEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_message_info";
        /**
         * 发送者id
         */
        public static final String COLUMN_NAME_SENDER_ID = "sender_id";
        /**
         * 接收者id
         */
        public static final String COLUMN_NAME_RECVER_ID = "recver_id";
        /**
         * 序列号
         */
        public static final String COLUMN_NAME_SEQ_ID = "seq_id";
        /**
         * 消息id
         */
        public static final String COLUMN_NAME_MSG_ID = "msg_id";
        /**
         * 发送时间
         */
        public static final String COLUMN_NAME_SEND_DT = "send_dt";
        /**
         * 消息类型
         */
        public static final String COLUMN_NAME_MSG_TYPE = "msg_type";
        /**
         * 消息内容
         */
        public static final String COLUMN_NAME_MSG_CONTENT = "msg_content";
        /**
         * 预约id
         */
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";
        /**
         * 消息状态
         */
        public static final String COLUMN_NAME_MSG_STATE = "msg_state";

        /** 8.2 版添加 */

//        /**
//         * 用户ID
//         */
//        public static final String COLUMN_NAME_USER_ID = "user_id";
    }
}
