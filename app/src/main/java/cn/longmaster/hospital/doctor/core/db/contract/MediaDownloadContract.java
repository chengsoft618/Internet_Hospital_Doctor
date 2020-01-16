package cn.longmaster.hospital.doctor.core.db.contract;

import android.provider.BaseColumns;

/**
 * 视频下载标示表，用于标记视频下载状态
 * Created by Yang² on 2017/12/5.
 */

public class MediaDownloadContract {
    public MediaDownloadContract() {
    }

    public static abstract class MediaDownloadEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_media_download";//表名
        public static final String COLUMN_NAME_APPOINTMENT_ID = "appointment_id";//预约id
        public static final String COLUMN_NAME_FILE_NAME = "file_name";//文件名字
        public static final String COLUMN_NAME_DOWNLOAD_STATE = "download_state";//下载状态
        public static final String COLUMN_NAME_CURRENT_SIZE = "current_size";//下载文件大小
        public static final String COLUMN_NAME_TOTAL_SIZE = "total_size";//文件大小

    }
}
