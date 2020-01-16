package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

import cn.longmaster.hospital.doctor.core.AppConstant;


/**
 * 用户信息协议类
 * Created by yangyong on 2015/7/15.
 */
public final class UserInfoContract {
    public UserInfoContract() {
    }

    public static abstract class UserInfoEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_user_info";
        /**
         * 用户id
         */
        public static final String COLUMN_NAME_USER_ID = "user_id";
        /**
         * 账户类型{@link AppConstant.UserAccountType}
         */
        public static final String COLUMN_NAME_ACCOUNT_TYPE = "account_type";
        /**
         * 账号
         */
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        /**
         * 电话号码
         */
        public static final String COLUMN_NAME_PHONE_NUM = "phone_num";
        /**
         * 登录鉴权key
         */
        public static final String COLUMN_NAME_LOGIN_AUTH_KEY = "login_auth_key";
        /**
         * 对应的pes的地址
         */
        public static final String COLUMN_NAME_PES_ADDR = "pes_addr";
        /**
         * pes的ip
         */
        public static final String COLUMN_NAME_PES_IP = "pes_ip";
        /**
         * Pes端口
         */
        public static final String COLUMN_NAME_PES_PORT = "pes_port";
        /**
         * 是否当前正在使用账号,为0时代表该账号不是当前使用的账号，为1时代表是当前使用账号
         */
        public static final String COLUMN_NAME_IS_USING = "is_using";
        /**
         * 最近一次登录时间
         */
        public static final String COLUMN_NAME_LAST_LOGIN_DT = "last_login_dt";
        /**
         * 是否激活
         */
        public static final String COLUMN_NAME_IS_ACTIVITY = "is_activity";

    }
}
