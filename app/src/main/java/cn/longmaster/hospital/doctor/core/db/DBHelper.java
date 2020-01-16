package cn.longmaster.hospital.doctor.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.db.contract.AppointmentContract.AppointmentEntry;
import cn.longmaster.hospital.doctor.core.db.contract.BaseConfigContract.BaseConfigEntry;
import cn.longmaster.hospital.doctor.core.db.contract.GroupMessageContract;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialFileFlagContract.MaterialFileFlagEntry;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskContract.MaterialTaskEntry;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskFileContract.MaterialTaskFileEntry;
import cn.longmaster.hospital.doctor.core.db.contract.MaterialTaskResultContract;
import cn.longmaster.hospital.doctor.core.db.contract.MediaDownloadContract;
import cn.longmaster.hospital.doctor.core.db.contract.MessageContract;
import cn.longmaster.hospital.doctor.core.db.contract.PersonalMaterialContract;
import cn.longmaster.hospital.doctor.core.db.contract.UserInfoContract.UserInfoEntry;
import cn.longmaster.hospital.doctor.core.manager.HTTPDNSIPContract;

/**
 * 数据库操作类
 * Created by yangyong on 2016/4/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String ALTER_TABLE = "ALTER TABLE ";
    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String AUTO_INCREMENT = " AUTOINCREMENT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ", ";
    public static final String ADD = " ADD ";
    public static final String SPACE = " ";

    public static final String DB_NAME = "internet_hospital_doctor.db";
    public static final int DB_VERSION = 9;

    private static DBHelper sDBHelper;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance() {
        if (sDBHelper == null) {
            synchronized (DBHelper.class) {
                if (sDBHelper == null) {
                    sDBHelper = new DBHelper(AppApplication.getInstance());
                }
            }
        }
        return sDBHelper;
    }

    private static final String SQL_CREATE_TABLE_USER_INFO = CREATE_TABLE + UserInfoEntry.TABLE_NAME +
            " (" +
            UserInfoEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_ACCOUNT_TYPE + INTEGER_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_PHONE_NUM + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_LOGIN_AUTH_KEY + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_PES_ADDR + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_PES_IP + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_PES_PORT + INTEGER_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_IS_USING + INTEGER_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_LAST_LOGIN_DT + TEXT_TYPE + COMMA_SEP +
            UserInfoEntry.COLUMN_NAME_IS_ACTIVITY + INTEGER_TYPE +
            ")";

    /**
     * 基本配置协议 sql
     *
     * @param db
     */
    private static final String SQL_CREATE_TABLE_BASE_CONFIG = CREATE_TABLE + BaseConfigEntry.TABLE_NAME +
            " (" +
            BaseColumns._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
            BaseConfigEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
            BaseConfigEntry.COLUMN_NAME_DATE_ID + TEXT_TYPE + COMMA_SEP +
            BaseConfigEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP +
            BaseConfigEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +
            ")";

    /**
     * 预约协议 sql
     *
     * @param db
     */
    private static final String SQL_CREATE_TABLE_APPOINTMENT_INFO = CREATE_TABLE + AppointmentEntry.TABLE_NAME +
            " (" +
            AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID + TEXT_TYPE + PRIMARY_KEY + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_APPOINTMENT_STATE + TEXT_TYPE + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_STATE_REASON + TEXT_TYPE + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_SERVICE_TYPE + TEXT_TYPE + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_SCHEDULE_TYPE + TEXT_TYPE + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_CHECK_STATE + TEXT_TYPE + COMMA_SEP +
            AppointmentEntry.COLUMN_NAME_PAY_DT + TEXT_TYPE + COMMA_SEP +
            BaseConfigEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +
            ")";

    /**
     * 创建消息信息表sql语句
     */
    private static final String SQL_CREATE_MESSAGE_INFO_TABLE =
            CREATE_TABLE + MessageContract.MessageEntry.TABLE_NAME + "(" +
                    MessageContract.MessageEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_SENDER_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_RECVER_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_SEQ_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_MSG_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_SEND_DT + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_MSG_TYPE + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_MSG_CONTENT + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_APPOINTMENT_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessageEntry.COLUMN_NAME_MSG_STATE + INTEGER_TYPE + ")";

    /**
     * 创建辅助资料上传信息表sql语句
     */
    private static final String SQL_CREATE_MATERIAL_TASK_TABLE =
            CREATE_TABLE + MaterialTaskEntry.TABLE_NAME + "(" +
                    MaterialTaskEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_TASK_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_STATE + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_APPOINTMENT_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_MATERIAL_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_MATERIAL_DATE + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_RECUR_NUM + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskEntry.COLUMN_NAME_DOCTOR_ID + TEXT_TYPE + ")";

    /**
     * 创建辅助资料上传文件上传进度信息表sql语句
     */
    private static final String SQL_CREATE_MATERIAL_TASK_FILE_TABLE =
            CREATE_TABLE + MaterialTaskFileEntry.TABLE_NAME + "(" +
                    MaterialTaskFileEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_TASK_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_SESSION_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_STATE + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_PROGRESS + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_MATERIAL_DT + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_LOCAL_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_LOCAL_POSTFIX + TEXT_TYPE + COMMA_SEP +
                    MaterialTaskFileEntry.COLUMN_NAME_SERVER_FILE_NAME + TEXT_TYPE + ")";

    /**
     * 创建群组消息表sql语句
     */
    private static final String SQL_CREATE_GROUP_MESSAGE_INFO_TABLE =
            CREATE_TABLE + GroupMessageContract.GroupMessageEntry.TABLE_NAME + "(" +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SENDER_ID + INTEGER_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_RECVER_ID + INTEGER_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_ID + TEXT_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT + TEXT_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_TYPE + INTEGER_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT + TEXT_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_APPOINTMENT_ID + TEXT_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID + INTEGER_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_OWER_ID + INTEGER_TYPE + COMMA_SEP +
                    GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE + INTEGER_TYPE + ")";

    private static final String SQL_CREATE_MATERIAL_RESULT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + MaterialTaskResultContract.MaterialTaskResultEntry.TABLE_NAME + "(" +
                    MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_SUCCESS_COUNT + INTEGER_TYPE + COMMA_SEP +
                    MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_FAILED_COUNT + INTEGER_TYPE + COMMA_SEP +
                    "PRIMARY KEY(" + MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_USER_ID + COMMA_SEP +
                    MaterialTaskResultContract.MaterialTaskResultEntry.COLUMN_NAME_APPOINTMENT_ID + "))";

    /**
     * 创建辅助资料上传文件上传进度信息表sql语句
     */
    private static final String SQL_CREATE_MATERIAL_FILE_FLAG_TABLE =
            CREATE_TABLE + MaterialFileFlagEntry.TABLE_NAME + "(" +
                    MaterialFileFlagEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    MaterialFileFlagEntry.COLUMN_NAME_APPOINTMENT_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialFileFlagEntry.COLUMN_NAME_LOCAL_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    MaterialFileFlagEntry.COLUMN_NAME_SESSION_ID + TEXT_TYPE + COMMA_SEP +
                    MaterialFileFlagEntry.COLUMN_NAME_UPLOAD_STATE + TEXT_TYPE + ")";

    /**
     * 创建视频下载进度信息表sql语句
     */
    private static final String SQL_CREATE_MEDIA_DOWNLOAD_TABLE =
            CREATE_TABLE + MediaDownloadContract.MediaDownloadEntry.TABLE_NAME + "(" +
                    MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_APPOINTMENT_ID + INTEGER_TYPE + COMMA_SEP +
                    MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_DOWNLOAD_STATE + INTEGER_TYPE + COMMA_SEP +
                    MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_CURRENT_SIZE + TEXT_TYPE + COMMA_SEP +
                    MediaDownloadContract.MediaDownloadEntry.COLUMN_NAME_TOTAL_SIZE + TEXT_TYPE + ")";
    /**
     * 创建httpdns ip信息表sql语句
     */
    private static final String SQL_CREATE_HTTPDNS_TABLE =
            CREATE_TABLE + HTTPDNSIPContract.HTTPDNSEntry.TABLE_NAME + "(" +
                    HTTPDNSIPContract.HTTPDNSEntry.TYPE + INTEGER_TYPE + COMMA_SEP +
                    HTTPDNSIPContract.HTTPDNSEntry.URL + TEXT_TYPE + COMMA_SEP +
                    HTTPDNSIPContract.HTTPDNSEntry.IP + INTEGER_TYPE + ")";
    /**
     * 创建个人资料上传表表sql语句
     */
    private static final String SQL_CREATE_PERSONAL_MATERIAL_TABLE =
            CREATE_TABLE + PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME + "(" +
                    PersonalMaterialContract.PersonalMaterialEntry.ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.USER_ID + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.MATERIAL_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.LOCAL_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.SVR_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_PROGRESS + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.FILE_TYPE + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.DOCTOR_ID + TEXT_TYPE + COMMA_SEP +
                    PersonalMaterialContract.PersonalMaterialEntry.INSERT_DT + TEXT_TYPE + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER_INFO);
        db.execSQL(SQL_CREATE_TABLE_BASE_CONFIG);
        db.execSQL(SQL_CREATE_TABLE_APPOINTMENT_INFO);
        db.execSQL(SQL_CREATE_MESSAGE_INFO_TABLE);
        db.execSQL(SQL_CREATE_MATERIAL_TASK_TABLE);
        db.execSQL(SQL_CREATE_MATERIAL_TASK_FILE_TABLE);

        db.execSQL(SQL_CREATE_GROUP_MESSAGE_INFO_TABLE);
        db.execSQL(SQL_CREATE_MATERIAL_RESULT_TABLE);
        db.execSQL(SQL_CREATE_MATERIAL_FILE_FLAG_TABLE);
        db.execSQL(SQL_CREATE_MEDIA_DOWNLOAD_TABLE);
        db.execSQL(SQL_CREATE_HTTPDNS_TABLE);
        db.execSQL(SQL_CREATE_PERSONAL_MATERIAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL(SQL_CREATE_GROUP_MESSAGE_INFO_TABLE);
        }

        if (oldVersion < 5) {
            db.execSQL(SQL_CREATE_MATERIAL_RESULT_TABLE);
            db.execSQL(SQL_CREATE_MATERIAL_FILE_FLAG_TABLE);
        }

        if (oldVersion < 6) {
            db.execSQL(SQL_CREATE_MEDIA_DOWNLOAD_TABLE);
        }
        if (oldVersion < 7) {
            db.execSQL(SQL_CREATE_HTTPDNS_TABLE);
        }
        if (oldVersion < 8) {
            db.execSQL(SQL_CREATE_PERSONAL_MATERIAL_TABLE);
        }
        if (oldVersion < 9) {
            addColumn(db, PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, PersonalMaterialContract.PersonalMaterialEntry.DOCTOR_ID, TEXT_TYPE);
        }
    }

    private void addColumn(SQLiteDatabase db, String tableName, String columnName, String dataType) {
        db.execSQL("ALTER TABLE " + tableName + " ADD " + columnName + dataType);
    }
}
