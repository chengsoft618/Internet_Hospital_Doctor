package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 预约信息协议类
 * Created by JinKe on 2016-08-03.
 */
public final class AppointmentContract {
    public AppointmentContract() {
    }

    public static abstract class AppointmentEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_appointment_list";
        /**
         * 预约id
         */
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";
        /**
         * 预约状态
         */
        public static final String COLUMN_NAME_APPOINTMENT_STATE = "appointment_state";
        /**
         * 预约副状态
         */
        public static final String COLUMN_NAME_STATE_REASON = "state_reason";
        /**
         * 服务类型
         */
        public static final String COLUMN_NAME_SERVICE_TYPE = "service_type";
        /**
         * 排班类型
         */
        public static final String COLUMN_NAME_SCHEDULE_TYPE = "schedule_type";
        /**
         * 资料审核是否通过
         */
        public static final String COLUMN_NAME_CHECK_STATE = "material_check_state";
        /**
         * 支付时间
         */
        public static final String COLUMN_NAME_PAY_DT = "pay_dt";
        /**
         * 内容
         */
        public static final String COLUMN_NAME_CONTENT = "content";
    }
}

