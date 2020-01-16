package cn.longmaster.hospital.doctor.core;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.utils.SPUtils;

/**
 * SharePreference管理类
 * Created by yangyong on 2016/4/18.
 */
public class AppPreference {
    public static final String TAG = AppPreference.class.getSimpleName();

    public static final String KEY_CURRENT_USER_IDENTITY = "_key_current_user_identity_";
    public static final String SHARED_PREFERENCE_FILE_NAME = AppApplication.getInstance().getPackageName() + ".sharedpreference";

    /**
     * 服务器最新版本号key
     */
    public static final String KEY_SERVER_LASTEST_VERSION = "key_server_lastest_version";
    /**
     * 服务器最新版本新版特性key
     */
    public static final String KEY_SERVER_LASTEST_VERSION_FEATURE = "key_server_lastest_version_feature";
    /**
     * 服务器最新版本apk文件大小key
     */
    public static final String KEY_SERVER_LASTEST_VERSION_APK_SIZE = "key_server_lastest_version_apk_size";
    /**
     * 服务器最新版本apk文件MD5key
     */
    public static final String KEY_SERVER_LASTEST_VERSION_MD5 = "key_server_lastest_version_md5";
    /**
     * 服务器提示版本key
     */
    public static final String KEY_SERVER_ALERT_VERSION = "key_server_alert_version";
    /**
     * 服务器限制最低版本key
     */
    public static final String KEY_SERVER_LIMIT_VERSION = "key_server_limit_version";

    public static final String KEY_CLIENT_VERSION = TAG + ".KEY_CLIENT_VERSION";
    /**
     * 首次进入软件标志
     */
    public static final String KEY_GUIDE_PAGE_CURRENT_VERSION = "key_guide_page_current_version";
    /**
     * 发送激活包时间
     */
    public static final String KEY_SEND_ACTION_DT = "key_send_action_dt";
    /**
     * 是否请求预约信息成功
     */
    public static final String KEY_FLAG_REQUEST_APPOINTMENT_SUCCESS = "key_flag_request_appointment_success";
    /**
     * 用户后台被踢下线
     */
    public static final String FLAG_BACKGROUND_KICKOFF = "flag_background_kickoff";
    /**
     * 适配xml文件下载token
     */
    public static final String KEY_AMX_TOKEN = "key_amx_token";
    /**
     * 启动页图片token
     */
    public static final String TOKEN_APP_START_PICTURE = "token_app_start_picture";
    /**
     * 用户正在发起的就诊类型
     */
    public static final String KEY_USER_APPOINT_GOING_TYPE = "key_user_appoint_going_type";
    /**
     * 首次添加排班
     */
    public static final String KEY_FIRST_ADD_SCHEDULE = "key_first_add_schedule";
    /**
     * 首次修改排班
     */
    public static final String KEY_FIRST_CHANGE_SCHEDULE = "key_first_change_schedule";
    /**
     * 是否首次出具医嘱
     */
    public static final String KEY_IS_FIRST_GIVE_ADVICE = "key_is_first_give_advice";
    /**
     * 是否首次我的账户
     */
    public static final String KEY_IS_FIRST_MY_ACCOUNT = "key_is_first_my_account";

    /**
     * 首次进入视频诊室
     */
    public static final String KEY_IS_FIRST_ENTER_VIDEO_ROOM = "key_is_first_enter_video_room";
    /**
     * 有新消息,"消息中心"红点提示
     */
    public static final String KEY_MESSAGE_CENTER_NEW_MESSAGE = "key_message_center_new_message";
    /**
     * 有新评估消息,"我的评估"红点提示
     */
    public static final String KEY_NEW_ASSESS = "key_new_assess_";
    public static final String KEY_IS_SHOW_NEWBIE_GUIDE = "key_is_show_newbie_guide";

    /**
     * 服务器地址
     */
    public static final String KEY_SERVICE_ADDRESS = "key_service_address";
    /**
     * 用户鉴权信息
     */
    public static final String KEY_AUTHENTICATION_AUTH = "key_authentication_auth";
    /**
     * 用户鉴权信息有效时长
     */
    public static final String KEY_AUTHENTICATION_OUT_TIME = "key_authentication_out_time";
    /**
     * 历史记录
     */
    public static final String KEY_SEARCH_RECORD = "key_search_record";
    /**
     * 医院搜索历史记录
     */
    public static final String KEY_SEARCH_HOSPITAL_RECORD = "key_search_hospital_record";
    /**
     * 科室搜索历史记录
     */
    public static final String KEY_SEARCH_DEPARTMENT_RECORD = "key_search_department_record";
    /**
     * 按姓名搜索医生记录
     */
    public static final String KEY_SEARCH_DOCTOR_BY_NAME_RECORD = "key_search_doctor_by_name_record";
    /**
     * 按疾病搜索医生记录
     */
    public static final String KEY_SEARCH_DOCTOR_BY_SICK_RECORD = "key_search_doctor_by_sick_record";
    /**
     * 搜索患者历史记录
     */
    public static final String KEY_SEARCH_PATIENT_RECORD = "key_search_patient_record";
    /**
     * 销售代表排班页的搜索历史记录
     */
    public static final String KEY_SEARCH_CONSULTATION_RECORD = "key_search_consultation_record";
    /**
     * 销售代表预约页的搜索历史记录
     */
    public static final String KEY_SEARCH_VISIT_RECORD = "key_search_visit_record";
    /**
     * 上传任务是否暂停
     */
    public static final String KEY_UPLOAD_PAUSE = "key_upload_pause_";
    /**
     * 新的上传结果
     */
    public static final String KEY_NEW_UPLOAD_RESULT = "key_new_upload_result_";
    /**
     * 销售代表功能token
     */
    public static final String KEY_REPRESENT_FUNCTION_TOKEN = "key_represent_function_token";
    /**
     * 销售代表功能权限
     */
    public static final String KEY_REPRESENT_FUNCTION_AUTHORITY = "key_represent_function_authority";
    /**
     * 销售代表功能详情
     */
    public static final String KEY_REPRESENT_FUNCTION_CONTENT = "key_represent_function_content";
    /**
     * 是否调用检查用户权限接口成功
     */
    public static final String KEY_REPRESENT_CHECK_ADMIN_INFO = "key_represent_check_admin_info";
    /**
     * 用户此次登录是否查询过账户信息
     */
    public static final String KEY_INQUIRY_ACCOUNT_INFO = "key_inquiry_account_info";

    /**
     * 存储List集合
     *
     * @param key  存储的键
     * @param list 存储的集合
     */
    private static void putList(String key, List<? extends Serializable> list) {
        try {
            put(key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置存储的list集合数据
     * 存储搜索历史记录
     *
     * @param key      存储的键
     * @param obj      存储的数据
     * @param maxCount 存储的数据的最大长度
     */
    public static void setList(String key, Serializable obj, int maxCount) {
        List<Serializable> list;
        if (getList(key) != null) {
            list = getList(key);
        } else {
            list = new ArrayList<>();
        }
        //如果数据相同，则移到最前面
        if (list.size() > 0) {
            for (Serializable s : list) {
                if (obj.equals(s)) {
                    list.remove(s);
                    break;
                }
            }
        }
        //保持最多存储maxCount个数据
        if (list.size() >= maxCount) {
            list.subList(maxCount - 1, list.size()).clear();
        }
        list.add(0, obj);
        putList(key, list);
    }

    /**
     * 获取List集合
     *
     * @param key 键
     * @param <E> 指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(String key) {
        try {
            return (List<E>) get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储对象
     */
    private static void put(String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();
        SPUtils.getInstance().put(key, objectStr);
    }

    /**
     * 获取对象
     */
    private static Object get(String key)
            throws IOException, ClassNotFoundException {
        String wordBase64 = SPUtils.getInstance().getString(key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
}
