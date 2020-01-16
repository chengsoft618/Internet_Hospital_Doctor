package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 基本配置协议类
 * Created by JinKe on 2016-07-26.
 */
public final class BaseConfigContract {
    public BaseConfigContract() {
    }

    public static abstract class BaseConfigEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_base_config";
        /**
         * 数据类型
         */
        public static final String COLUMN_NAME_TYPE = "type";
        /**
         * 数据id
         */
        public static final String COLUMN_NAME_DATE_ID = "date_id";
        /**
         * 数据更新token
         */
        public static final String COLUMN_NAME_TOKEN = "token";
        /**
         * 内容
         */
        public static final String COLUMN_NAME_CONTENT = "content";
        /**
         *
         */
        public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_NAME_TYPE + " = ? AND " + COLUMN_NAME_DATE_ID + " = ?";
    }
}