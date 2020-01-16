package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 辅助资料上传Task信息
 * Created by ddc on 2015-08-05.
 */
public final class MaterialTaskContract {
    private MaterialTaskContract() {
    }

    public static abstract class MaterialTaskEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_material_upload_task";
        /**
         * 用户id
         */
        public static final String COLUMN_NAME_USER_ID = "user_id";
        /**
         * task id
         */
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        /**
         * 上传状态
         */
        public static final String COLUMN_NAME_STATE = "state";
        /**
         * 预约号/病历号
         */
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";
        /**
         * 助检查项ID
         */
        public static final String COLUMN_NAME_MATERIAL_ID = "material_id";
        /**
         * 资料时间
         */
        public static final String COLUMN_NAME_MATERIAL_DATE = "material_date";
        /**
         * 复诊次数	初诊传0
         */
        public static final String COLUMN_NAME_RECUR_NUM = "recur_num";
        /**
         * 医生id
         */
        public static final String COLUMN_NAME_DOCTOR_ID = "doctor_id";
    }
}
