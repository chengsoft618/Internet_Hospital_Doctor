package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 消息协议类
 * Created by yangyong on 2016/8/9.
 */
public final class GroupMessageContract {
    public GroupMessageContract() {
    }

    public static abstract class GroupMessageEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_group_message_info";
        /**
         * 序列号
         */
        public static final String COLUMN_NAME_SEQ_ID = "seq_id";
        /**
         * 发送者id
         */
        public static final String COLUMN_NAME_SENDER_ID = "sender_id";
        /**
         * 接收者id
         */
        public static final String COLUMN_NAME_RECVER_ID = "recver_id";
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
        /**
         * 群组id
         */
        public static final String COLUMN_NAME_IM_ID = "im_id";
        /**
         * 消息拥有者id，避免多个账号消息错乱
         */
        public static final String COLUMN_NAME_OWER_ID = "ower_id";

    }
}
