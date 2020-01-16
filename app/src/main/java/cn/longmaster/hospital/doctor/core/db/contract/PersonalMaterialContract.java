package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 基本配置协议类
 */
public final class PersonalMaterialContract {
    public PersonalMaterialContract() {
    }

    public static abstract class PersonalMaterialEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_personal_material";//表名
        public static final String ID = "id";//id
        public static final String TASK_ID = "task_id";//id
        public static final String USER_ID = "user_id";//用户名
        public static final String MATERIAL_NAME = "material_name";//资料名
        public static final String LOCAL_FILE_NAME = "local_file_name";//本地文件名
        public static final String SVR_FILE_NAME = "svr_file_name";//服务器文件名
        public static final String UPLOAD_PROGRESS = "upload_progress";//文件上传进度
        public static final String UPLOAD_STATE = "upload_state";//文件上传状态
        public static final String FILE_TYPE = "file_type";//文件类型
        public static final String DOCTOR_ID = "doctor_id";//文件类型
        public static final String INSERT_DT = "insert_dt";//插入时间
    }
}