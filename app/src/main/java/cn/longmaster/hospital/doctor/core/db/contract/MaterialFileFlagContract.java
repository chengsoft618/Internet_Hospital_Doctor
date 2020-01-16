package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 材料文件标示表，用于标记本地文件上传状态
 * Created by YY on 17/10/17.
 */

public class MaterialFileFlagContract {
    private MaterialFileFlagContract() {
    }

    public static abstract class MaterialFileFlagEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_material_file_flag";//表名
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";//预约id
        public static final String COLUMN_NAME_LOCAL_FILE_PATH = "local_file_path";//文件本地路径
        public static final String COLUMN_NAME_SESSION_ID = "session_id";//上传sessionid
        public static final String COLUMN_NAME_UPLOAD_STATE = "upload_state";//上传状态

    }
}
