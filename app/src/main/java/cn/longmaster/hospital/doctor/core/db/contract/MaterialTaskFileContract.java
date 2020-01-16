package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 辅助资料上传文件上传进度信息
 * Created by ddc on 2015-08-05.
 */
public class MaterialTaskFileContract {
    private MaterialTaskFileContract() {
    }

    public static abstract class MaterialTaskFileEntry implements BaseColumns {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "t_material_upload_file";
        /**
         * task id
         */
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        /**
         * 唯一标识一个文件
         */
        public static final String COLUMN_NAME_SESSION_ID = "sessionId";
        /**
         * 上传状态
         */
        public static final String COLUMN_NAME_STATE = "state";
        /**
         * 上传进度
         */
        public static final String COLUMN_NAME_PROGRESS = "progress";
        /**
         * 图片时间
         */
        public static final String COLUMN_NAME_MATERIAL_DT = "material_dt";
        /**
         * 本地绝对路径
         */
        public static final String COLUMN_NAME_LOCAL_FILE_PATH = "local_file_path";
        /**
         * 本地文件名
         */
        public static final String COLUMN_NAME_LOCAL_FILE_NAME = "local_file_name";
        /**
         * 复本地文件扩展名
         */
        public static final String COLUMN_NAME_LOCAL_POSTFIX = "local_postfix";
        /**
         * 上传成功后返回的文件名
         */
        public static final String COLUMN_NAME_SERVER_FILE_NAME = "server_file_name";
    }
}
