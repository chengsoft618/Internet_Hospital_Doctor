package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 辅助资料上传文件上传进度信息
 * Created by ddc on 2015-08-05.
 */
public class MaterialTaskResultContract {
    private MaterialTaskResultContract() {
    }

    public static abstract class MaterialTaskResultEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_material_result";
        /**
         * 预约 id
         */
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";
        /**
         * 用户id
         */
        public static final String COLUMN_NAME_USER_ID = "user_id";
        /**
         * 成功数
         */
        public static final String COLUMN_NAME_SUCCESS_COUNT = "success_count";
        /**
         * 失败数
         */
        public static final String COLUMN_NAME_FAILED_COUNT = "failed_count";
    }
}
