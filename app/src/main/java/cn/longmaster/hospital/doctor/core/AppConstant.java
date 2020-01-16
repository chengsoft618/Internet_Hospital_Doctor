package cn.longmaster.hospital.doctor.core;


import android.support.annotation.IntDef;

/**
 * 常量类，用户声明定义应用所有常量
 * Created by yangyong on 2015/7/13.
 */
public class AppConstant {

    //requestCode extra key
    public static final String EXTRA_REQUEST_CODE = "extra_request_code";
    //duws服务器校验key
    public static final String DUWS_APP_KEY = "doctor_dtws_2015";
    //requestCode 注册
    public static final int REQUEST_CODE_REGISTER = 1000;
    //requestCode 重置密码
    public static final int REQUEST_CODE_RESET_PASSWORD = 1001;

    //访客用户id
    public static final int VISITOR_USER_ID = 120;//访客用户id

    //重发验证码的时间间隔
    public static final int KNOCK_CODE_MILLIS = 60 * 1000;

    //敲门时间间隔
    public static final int REG_CODE_MILLIS = 60 * 1000;

    // ================================= 消息常量定义=================================//
    public static final int CLIENT_MESSAGE_CODE_BASE = 0;//客户端消息码起始值,后面定义的消息码在此基础上递增
    public static final int CLIENT_MESSAGE_CODE_ON_APPOINT_STATE_CHANGED = CLIENT_MESSAGE_CODE_BASE + 0;// 预约状态发生变化
    public static final int CLIENT_MESSAGE_CODE_ON_DELETE_MATERIAL = CLIENT_MESSAGE_CODE_BASE + 1;//删除资料
    public static final int CLIENT_MESSAGE_CODE_DELETE_PASS_MATERIAL = CLIENT_MESSAGE_CODE_BASE + 2;//删除通过审核资料


    // ================================= 广播常量定义=================================//
    public static final String ACTION_ALARM_MANAGER = "cn.longmaster.doctorclient.action.alarm.manager";

    public static final int RECOMMENDED_EXPERTS_ID = -1;//医生列表 推荐专家 科室ID

    public static final int PAGE_TYPE_DOCTOR = 201;

    public static final int SEARCH_TYPE_DOCTOR = 1;
    public static final int SEARCH_TYPE_APPOINTMENT = 2;

    public static final int PAGE_TYPE_CHANGE = 0;
    public static final int PAGE_TYPE_FOUND = 1;

    public static final String ACTION_CHANGE_TAB = "action_change_tab";
    public static final String ACTION_START_LOGIN = "action_start_login";

    /**
     * 账号类型USER_ACCOUNT_TYPE
     */
    public final static class UserAccountType {
//        0：未定义
//        1：邮箱
//        2：手机号
//        3：用户自定义
//        4：第三方账号：QQ
//        5：第三方账号：新浪微博
//        6：来宾用户
//        7：39通行证（此时密码传空）

        /**
         * 2：手机号
         */
        public static final byte ACCOUNT_PHONE_NUMBER = 2;

        public static final byte ACCOUNT_3RD_QQ = 4;
        public static final byte ACCOUNT_3RD_WEIBO = 5;

        /**
         * 7：39通行证（此时密码传空）
         */
        public static final byte ACCOUNT_39 = 7;

    }

    /**
     * 医生类型 1、医生助理 2、会诊医师(上级专家) 3、申请医师(首诊医生)
     */
    public static final class DoctorType {
        public static final int DOCTOR_TYPE_ASSISTANT_DOCTOR = 1;//医生助理
        public static final int DOCTOR_TYPE_SUPERIOR_DOCTOR = 2;//会诊医师(上级专家)
        public static final int DOCTOR_TYPE_ATTENDING_DOCTOR = 3;//申请医师(首诊医生)
        public static final int DOCTOR_TYPE_ASSISTANT_DOCTOR_IPAD = 4;//医生助理(来自ipad的医生助理)
        //以下为自定义
        public static final int DOCTOR_TYPE_PATIENT = 0;//患者
        public static final int DOCTOR_TYPE_PATIENT_LINKMAN = -1;//患者联系人
    }

    /**
     * 视频用户类型 0、患者 1、首诊医生 2、管理员 3、上级专家 4、医生助理 5、会议人员 6、MDT医生 7、MDT患者家属
     * 视频诊室专用
     */
    public static final class UserType {
        public static final int USER_TYPE_PATIENT = 0;//患者
        public static final int USER_TYPE_ATTENDING_DOCTOR = 1;//申请医师(首诊医生)
        public static final int USER_TYPE_ADMINISTRATOR = 2;//管理员
        public static final int USER_TYPE_SUPERIOR_DOCTOR = 3;//会诊医师(上级专家)
        public static final int USER_TYPE_ASSISTANT_DOCTOR = 4;//医生助理
        public static final int USER_TYPE_CONFERENCE_STAFF = 5;//会议人员
        public static final int USER_TYPE_MDT_DOCTOR = 6;//MDT医生
        public static final int USER_TYPE_MDT_PATIENT = 7;//MDT患者家属
        public static final int USER_TYPE_CUSTOMER_SERVICE = 8;//客服
        public static final int USER_TYPE_CENTRAL_CONTROL = 11;//中控
        public static final int USER_TYPE_LIVE_BROADCAST_WATCH = 12;//直播观看者
    }

    @IntDef({UserType.USER_TYPE_PATIENT, UserType.USER_TYPE_ATTENDING_DOCTOR, UserType.USER_TYPE_ADMINISTRATOR,
            UserType.USER_TYPE_SUPERIOR_DOCTOR, UserType.USER_TYPE_ASSISTANT_DOCTOR, UserType.USER_TYPE_CONFERENCE_STAFF,
            UserType.USER_TYPE_MDT_DOCTOR, UserType.USER_TYPE_MDT_PATIENT, UserType.USER_TYPE_CUSTOMER_SERVICE,
            UserType.USER_TYPE_CENTRAL_CONTROL, UserType.USER_TYPE_LIVE_BROADCAST_WATCH})
    public @interface RoomUserType {
    }

    /**
     * 向服务器获取验证码类型
     */
    public final static class RegCodeType {
        /**
         * 注册重发
         */
        public static final byte REGISTER = 1;
        /**
         * 短信登录
         */
        public static final byte MSG_LOGIN = 2;
        /**
         * 重置密码
         */
        public static final byte RESET_PASSWORD = 3;

        /**
         * 查看账户信息
         */
        public static final byte ACCOUNT_INFORMATION = 5;
    }

    /**
     * 预约状态
     */
    public final static class AppointmentState {
        /**
         * 等待医学助理电话联系等待医学助理电话联系
         */
        public static final int WAIT_ASSISTANT_CALL = 1;
        /**
         * 等待用户支付
         */
        public static final int WAIT_USER_PAY = 2;
        /**
         * 等待上传资料(用户已经支付完成)
         */
        public static final int WAIT_UPLOAD_DATA = 3;
        /**
         * 资料上传过程，(资料上传项目不全，资料上传完成，资料审核未通过)
         */
        public static final int DATA_CHECK_FAIL = 4;
        /**
         * 资料审核完成
         */
        public static final int DATA_CHECK_SUCCESS = 5;
        /**
         * 等待医生接诊
         */
        public static final int WAIT_DOCTOR_RECEPTION = 6;
        /**
         * 医生拒绝接诊，等待重新分配医生（针对同意分配医生的患者）
         */
        public static final int DOCTOR_REFUSE_RECEPTION = 7;
        /**
         * 医生同意接诊
         */
        public static final int DOCTOR_AGREE_RECEPTION = 8;
        /**
         * 医生提出修改接诊时间（患者同意修改接诊时间，患者不同意修改就诊时间）
         */
        public static final int DOCTOR_CHANGE_RECEPTION_TIME = 9;
        /**
         * 医患已经进行就诊视频
         */
        public static final int DOCTOR_PATIENT_VIDEOED = 10;
        /**
         * 复诊补充资料
         */
        public static final int RECURE_ADD_MATERIAL = 11;
        /**
         * 医学助理已经录入就诊报告
         */
        public static final int ASSISTANT_LOGGING_REPORT = 12;
        /**
         * 医生已经在报告上签字
         */
        public static final int DOCTOR_SIGN_REPORT = 13;
        /**
         * 患者签收报告
         */
        public static final int PATIENT_ACCEPT_REPORT = 14;
        /**
         * 预约完成
         */
        public static final int APPOINTMENT_FINISHED = 15;
    }

    //预约状态备注
    public static final class StatReason {
        public static final int WAIT_ASSISTANT_CALL = 0;//等待电话联系
        public static final int WAIT_ASSISTANT_CALL_FINISH = 1;//电话联系完成，等待选择团队
    }

    //用户在线状态
    public static final byte ONLINE_STATE_OFFLINE = 0;
    public static final byte ONLINE_STATE_ONLINE = 1;
    public static final byte ONLINE_STATE_KICKOFF = 2;
    public static final byte ONLINE_STATE_UNREG = 3;
    public static final byte ONLINE_STATE_FORBIDDEN = 4;

    /**
     * 链接类型
     */
    public static final class LinkType {
        public static final int link_type_net = 1;//超链接
        public static final int link_type_module = 2;//模块
        public static final int link_type_video = 3;//视频
        public static final int link_type_guide = 4;//指南
    }

    public static final class BroadCast {
        public static final String SUICIDE = "suicide";
    }

    /**
     * 会诊类型
     */
    public static final class SchedulingType {
        public static final int SCHEDULING_TYPE_NO_SELECT = 0;//没选择类型
        public static final int SCHEDULING_TYPE_REMOTE_CONSULT = 1;//远程会诊
        public static final int SCHEDULING_TYPE_IMAGE_CONSULT = 2;//影像会诊
        public static final int SCHEDULING_TYPE_CONSULT_RETURN = 3;//复诊
    }

    /**
     * 权限类型
     */
    public static final class ServiceAuthorityType {
        public static final int SERVICE_AUTHORITY_TYPE_REMOTE = 1;//远程会诊
        public static final int SERVICE_AUTHORITY_TYPE_IMAGE = 2;//影像会诊
        public static final int SERVICE_AUTHORITY_TYPE_ATTENDING = 3;//首诊权限

    }

    /**
     * 排班服务类型
     */
    public static final class ServiceType {
        public static final int SERVICE_TYPE_NOT_SELECT = 0;//没选择
        public static final int SERVICE_TYPE_REMOTE_CONSULT = 101001;//远程会诊
        public static final int SERVICE_TYPE_MEDICAL_ADVICE = 101002;//医学咨询、远程咨询
        public static final int SERVICE_TYPE_RETURN_CONSULT = 101003;//远程会诊复诊
        public static final int SERVICE_TYPE_RETURN_ADVICE = 101004;//远程咨询复诊
        public static final int SERVICE_TYPE_REMOTE_OUTPATIENT = 101005;//远程门诊
        public static final int SERVICE_TYPE_REMOTE_WARDS = 101006;//远程查房
        public static final int SERVICE_TYPE_NEW_REMOTE_OUTPATIEN = 101007;//门诊
        public static final int SERVICE_TYPE_IMAGE_CONSULT = 102001;//远程影像会诊

        @IntDef({SERVICE_TYPE_NOT_SELECT, SERVICE_TYPE_REMOTE_CONSULT, SERVICE_TYPE_MEDICAL_ADVICE
                , SERVICE_TYPE_RETURN_CONSULT, SERVICE_TYPE_RETURN_ADVICE, SERVICE_TYPE_REMOTE_OUTPATIENT
                , SERVICE_TYPE_REMOTE_WARDS, SERVICE_TYPE_NEW_REMOTE_OUTPATIEN, SERVICE_TYPE_IMAGE_CONSULT})
        public @interface ScheduleServiceType {
        }
    }

    /**
     * 发起人类型 1：我收到的;2：我发起的;0：我收到和我发起的
     */
    public static final class PromoterType {
        public static final int PROMOTER_TYPE_RECEIVED = 1;//我收到的
        public static final int PROMOTER_TYPE_LAUNCHED = 2;//我发起的
        public static final int PROMOTER_TYPE_RECEIVED_AND_LAUNCHED = 0;//我收到和我发起的
    }

    /**
     * 是否销售代表
     */
    public static final class IsSale {
        public static final int IS = 1;//是
        public static final int NO = 0;//不是
    }


    /**
     * 0：不使用该条件 1：等待分诊 2：等待支付 3：支付超时 4：材料未审核 5：材料已审核 6：等待就诊
     * 7：等待医嘱 8：等待报告 9：就诊结束 10：就诊关闭 11：就诊完成
     */
    public static final class StatNum {
        public static final int STAT_NUM_NOT_SELECT = 0;//不使用该条件
        public static final int STAT_NUM_WAITING_TRIAGE = 1;//等待分诊
        public static final int STAT_NUM_WAITING_PAY = 2;//等待支付
        public static final int STAT_NUM_PAY_TIME_OUT = 3;//支付超时
        public static final int STAT_NUM_NOT_AUDIT = 4;//材料未审核
        public static final int STAT_NUM_ALREADY_AUDIT = 5;//材料已审核
        public static final int STAT_NUM_WAITING_VIDEO = 6;//等待就诊
        public static final int STAT_NUM_WAITING_ADVICE = 7;//等待医嘱
        public static final int STAT_NUM_REPORT_TIDY = 8;//等待报告
        public static final int STAT_NUM_VIEW_REPORT = 9;//就诊结束(查看报告)
        public static final int STAT_NUM_PAY_CONSULT_CLOSED = 10;//就诊关闭
        public static final int STAT_NUM_CONSULT_FINISH = 11;//就诊完成
    }

    /**
     * 是否复诊
     */
    public static final class Recure {
        public static final int RECURE_YES = 1;//是
        public static final int RECURE_NO = 2;//否
        public static final int RECURE_NOT_SELECT = 0;//不使用该条件
    }

    /**
     * 病史类型
     */
    public static final class HistoryType {
        //病史类型 1--现病史；2--过敏史；3--家族史；4,--既往史；5--手术史；6--月经史；7--备注；8--个人史；9--婚育史
        public static final int HISTORY_TYPE_PRESENT_MEDICAL = 1;//现病史
        public static final int HISTORY_TYPE_ALLERGIC = 2;//过敏史
        public static final int HISTORY_TYPE_FAMILY = 3;//家族史
        public static final int HISTORY_TYPE_PAST_MEDICAL = 4;//既往史
        public static final int HISTORY_TYPE_OPERATION = 5;//手术史
        public static final int HISTORY_TYPE_MENSTRUAL = 6;//月经史
        public static final int HISTORY_TYPE_REMARK = 7;//备注
        public static final int HISTORY_TYPE_PERSONAL = 8;//个人史
        public static final int HISTORY_TYPE_OBSTETRIC = 9;//婚育史
    }

    /**
     * 辅助材料审核状态
     */
    public final static class MaterialCheckState {
        //未上传
        public final static int STATE_UNUPLOAD = -1;
        //未审核
        public final static int STATE_UNCHECKED = 0;
        //审核成功
        public final static int STATE_CHECK_SUCCESS = 1;
        //审核失败
        public final static int STATE_CHECK_FAIL = 2;
        //审核中
        public final static int STATE_CHECKING = 3;
    }

    /**
     * 性别
     */
    public static final class GenderType {
        public static final int GENDERTYPE_MALE = 0;//男
        public static final int GENDERTYPE_FEMALE = 1;//女
    }

    /**
     * 文件类型
     * 0：图片1：语音2：病历截图
     */
    public static final class DiagnosisFileType {
        public static final int DIAGNOSIS_CONTENT_TYPE_PICTURE = 0;//图片
        public static final int DIAGNOSIS_CONTENT_TYPE_VOICE = 1;//语音
        public static final int DIAGNOSIS_CONTENT_TYPE_SCREENSHOT = 2;//病历截图
    }

    /**
     * 医嘱内容
     * 0:医嘱1:补充材料说明2:强制结束说明3：医嘱语音文件路径(附加说明:2: content(内容结构说明json):end_target:结束目标1:医生2:患者end_desc:取消原因)
     */
    public static final class DiagnosisContentType {
        public static final int DIAGNOSIS_CONTENT_TYPE_TEXT = 0;//医嘱
        public static final int DIAGNOSIS_CONTENT_TYPE_SUPPLEMENT = 1;//补充材料说明
        public static final int DIAGNOSIS_CONTENT_TYPE_FORCED_END = 2;//强制结束说明
        public static final int DIAGNOSIS_CONTENT_TYPE_FILE_PATH = 3;//医嘱语音文件路径
    }

    /**
     * 账单变化原因
     * 101：给医生打款；102：充值；103：消费；104：医生提现；105：医生提成；106: 消费失败；107: 消费者退款；108：医生补偿；109：更换医生；110：医生奖励；111：医生惩罚；200：惠来医保；201：六医垫付
     */
    public static final class BillReason {
        public static final int BILL_REASON_PAY_FOR = 101;//给医生打款
        public static final int BILL_REASON_TOP_UP = 102;//充值
        public static final int BILL_REASON_CONSUMPTION = 103;//消费
        public static final int BILL_REASON_WITHDRAW = 104;//医生提现
        public static final int BILL_REASON_COMMISSION = 105;//医生提成
        public static final int BILL_REASON_CONSUMPTION_FAIL = 106;//消费失败
        public static final int BILL_REASON_REFUND = 107;//消费者退款
        public static final int BILL_REASON_COMPENSATION = 108;//医生补偿
        public static final int BILL_REASON_CHANGE_DOCTOR = 109;//更换医生
        public static final int BILL_REASON_AWARD = 110;//医生奖励
        public static final int BILL_REASON_PUNISH = 111;//医生惩罚
        public static final int BILL_REASON_PATIENT_PAYMENT = 112;//给患者打款
        public static final int BILL_REASON_PATIENT_CASH = 113;//患者提现
        public static final int BILL_REASON_PAYMENT = 114;//垫付退款（取消垫付）
        public static final int BILL_REASON_SHARE_SUBSIDY = 197;//分成补贴
        public static final int BILL_REASON_PROPAGANDA_SUBSIDY = 198;//宣传补贴
        public static final int BILL_REASON_UPLOAD_SUBSIDY = 199;//上传补贴
        public static final int BILL_REASON_HEALTH_INSURANCE = 200;//惠来医保
        public static final int BILL_REASON_SIXTH_HOSPITAL = 201;//六医垫付
        public static final int BILL_REASON_BEI_LIU_HOSPITAL = 202;//北流人民医院
        public static final int BILL_REASON_HU_ZHU_HOSPITAL = 203;//互助人民医院
        public static final int BILL_REASON_MI_YANG_HOSPITAL = 204;//泌阳人民医院
        public static final int BILL_REASON_JUN_KANG_HOSPITAL = 205;//北医君康
        public static final int BILL_REASON_SCANNING_CODE_CASH = 206;//扫码付现
        public static final int BILL_REASON_GUANG_YUAN_HOSPITAL = 207;//广元朝天人医
        public static final int BILL_REASON_HUI_HANG_303_HOSPITAL = 208;//贵航303医院
        public static final int BILL_REASON_HUAI_BEI_KUANG_GONG_HOSPITAL = 209;//淮北矿工总院
        public static final int BILL_REASON_PU_YANG_HOSPITAL = 210;//濮阳油田总院
        public static final int BILL_REASON_WEI_SHAN_HU_HOSPITAL = 211;//微山湖医院
        public static final int BILL_REASON_HUAI_BEI_CHAO_YANG_HOSPITAL = 212;//淮北朝阳医院
        public static final int BILL_REASON_XIN_WEN_CENTER_HOSPITAL = 213;//新汶中心医院
        public static final int BILL_REASON_JIANG_HAN_YOU_TIAN_HOSPITAL = 214;//江汉油田总院
        public static final int BILL_REASON_FENG_ZHEN_ZHONG_MENG_HOSPITAL = 215;//丰镇中蒙医院
        public static final int BILL_REASON_XI_SHUI_HOSPITAL = 216;//习水人民医院
        public static final int BILL_REASON_39_INTERNET_HOSPITAL = 217;//39互联网医院
        public static final int BILL_REASON_OUTPATIENT_TEST_HOSPITAL = 218;//门诊测试医院1
        public static final int BILL_REASON_TIE_LI_HOSPITAL = 219;//铁力人民医院
        public static final int BILL_REASON_TIE_LING_CENTER_HOSPITAL = 220;//铁岭中心医院
        public static final int BILL_REASON_WU_WEI_HOSPITAL = 221;//无为人民医院
        public static final int BILL_REASON_MI_YANG_ZHONG_JING_HOSPITAL = 222;//泌阳仲景医院
        public static final int BILL_REASON_NEI_JIANG_SIXTH_HOSPITAL = 223;//内江第六人医
        public static final int BILL_REASON_XING_BING_HOSPITAL = 224;//兴宾人民医院
        public static final int BILL_REASON_WN_SI_XIAN_FENG_HOSPITAL = 225;//恩施咸丰人医
        public static final int BILL_REASON_HAI_DONG_HOSPITAL = 226;//海东人民医院
        public static final int BILL_REASON_LAI_BING_HOSPITAL = 227;//来宾人民医院
        public static final int BILL_REASON_WU_LA_TE_HOSPITAL = 228;//乌拉特前旗人医
        public static final int BILL_REASON_XI_XIAN_HOSPITAL = 229;//息县第二人医
        public static final int BILL_REASON_CANG_XI_HOSPITAL = 230;//苍溪人民医院
        public static final int BILL_REASON_HE_SHAN_HOSPITAL = 231;//合山人民医院
        public static final int BILL_REASON_GUI_YANG_FOURTH_HOSPITAL = 232;//贵阳第四人医
        public static final int BILL_REASON_YI_FANG_SCIENCE_HOSPITAL = 233;//医方科技
        public static final int BILL_REASON_JIAN_GE_HOSPITAL = 234;//剑阁人民医院
        public static final int BILL_REASON_HUI_RUI_HELP_HOSPITAL = 235;//辉瑞教学帮扶
        public static final int BILL_REASON_HEI_LONG_JIANG_HOSPITAL = 236;//黑龙江省医院
        public static final int BILL_REASON_HUI_RUI_HUI_MIN_HOSPITAL = 237;//辉瑞惠民工程
        public static final int BILL_REASON_NAN_NING_SECOND_HOSPITAL = 238;//南宁二院
        public static final int BILL_REASON_HAI_NAN_ZANG_ZU_HOSPITAL = 239;//海南藏族人医
        public static final int BILL_REASON_HE_BI_HOSPITAL = 240;//鹤壁人民医院
        public static final int BILL_REASON_VIDEO_HOSPITAL = 241;//视频问专家
        public static final int BILL_REASON_XIN_ZHOU_HOSPITAL = 242;//忻州人医
        public static final int BILL_REASON_BANK_TRANSFER = 243;//银行转账
        public static final int BILL_REASON_GUANG_YUAN_LI_ZHOU_HOSPITAL = 244;//广元利州中医
        public static final int BILL_REASON_BEI_LIU_WEI_JI_WEI = 245;//北流市卫计委
        public static final int BILL_REASON_GUANG_YUAN_CENTER_HOSPITAL = 246;//广元中心医院
        public static final int BILL_REASON_SOUTH_HOSPITAL = 247;//南部人民医院
    }

    /**
     * 支付类型
     */
    public static final class PayType {
        public static final int PAY_TYPE_BANK_CARD = 1;//银行卡
        public static final int PAY_TYPE_ALI_PAY = 2;//支付宝
        public static final int PAY_TYPE_WEI_CHAT = 3;//微信
    }

    /**
     * 康复情况
     */
    public static final class RecoverySituation {
        public static final int RECOVERY_SITUATION_GOOD = 1;//良好
        public static final int RECOVERY_SITUATION_GENERAL = 2;//一般
        public static final int RECOVERY_SITUATION_BAD = 3;//差
    }

    public static final class MaterailType {
        public static final int MATERAIL_TYPE_PIC = 0;//普通图片
        public static final int MATERAIL_TYPE_DICOM = 1;//dicom图片
        public static final int MATERAIL_TYPE_WSI = 2;//病理切片
        public static final int MATERAIL_TYPE_MEDIA = 3;//多媒体
    }

    public static final class MediaType {
        public static final int MEDIA_TYPE_VOICE = 1;//语音
        public static final int MEDIA_TYPE_VIDEO = 2;//视频
    }

    /**
     * 管理菜单
     */
    public static final class ManageMenu {
        public static final int MANAGE_MENU_MEDICAL_MANAGE = 1;//资料管理
        public static final int MANAGE_MENU_LANUCH_RECURE = 2;//发起复诊
        public static final int MANAGE_MENU_ISSURE_ORDER = 3;//出具医嘱
        public static final int MANAGE_MENU_ENTRY_VEDIO_ROOM = 4;//进入诊室
        public static final int MANAGE_MENU_IM_ROOM = 5;//聊天室
    }

    /**
     * 账户状态
     */
    public static final class AccountState {
        public static final int NORMAL_STATE = 0;//正常状态
        public static final int USING_STATE = 1;//使用状态
        public static final int DELETE_STATE = 2;//逻辑删除
    }

    /**
     * 申请人身份类型 1：后台管理员; 2：医生; 3：患者
     */
    public static final class IdentityType {
        public static final int IDENTITY_TYPE_ADMINISTRATOR = 1;//后台管理员
        public static final int IDENTITY_TYPE_DOCTOR = 2;//医生
        public static final int IDENTITY_TYPE_PATIENT = 3;//患者
    }

    public static final class SignalState {
        public static final int SIGNAL_BAD = 1;//信号 差
        public static final int SIGNAL_GENERAL = 2;//信号 一般
        public static final int SIGNAL_GOOD = 3;//信号 好
    }

    public static final class RoomState {
        public static final int STATE_DEFAULT = 0;//默认
        public static final int STATE_CALLING = 1;//正在呼叫
        public static final int STATE_BEING_VIDEO = 2;//正在视频
        public static final int STATE_MISS = 4;//没有接听
        public static final int STATE_BEING_VOICE = 3;//正在音频
    }

    /**
     * 自述类型
     */
    public static final class PatientDescType {
        public static final int DIAGNOSIS_AND_THERAPEUTIC = 0;//明确诊断及治疗方案
        public static final int DIAGNOSIS = 1;//明确诊断
        public static final int THERAPEUTIC = 2;//更佳治疗方案
        public static final int OTHER = 3;//其他
    }

    /**
     * IM群组状态- 0 初始状态， 1 接诊， 2 拒诊，3 关闭，4 随诊
     */
    public static final class IMGroupStatus {
        public static final int IM_GROUP_STATUS_ORIGINAL = 0;//初始状态
        public static final int IM_GROUP_STATUS_RECEPTION = 1;//接诊
        public static final int IM_GROUP_STATUS_REFUSE = 2;//拒诊
        public static final int IM_GROUP_STATUS_CLOSE = 3;//关闭
        public static final int IM_GROUP_STATUS_FOLLOW_UP = 4;//随诊
    }

    /**
     * 角色-0 患者，1 首诊医生，2 管理员，3 上级专家，4 医生助理，5 会议人员，6 MDT医生，7 MDT患者家属，255 系统自动
     */
    public static final class IMGroupRole {
        public static final int IM_GROUP_ROLE_PATIENT = 0;//患者
        public static final int IM_GROUP_ROLE_ATTENDING_DOCTOR = 1;//首诊医生
        public static final int IM_GROUP_ROLE_ADMINISTRATOR = 2;//管理员
        public static final int IM_GROUP_ROLE_SUPERIOR_DOCTOR = 3;//上级专家
        public static final int IM_GROUP_ROLE_ASSISTANT_DOCTOR = 4;//医生助理
        public static final int IM_GROUP_ROLE_CONFERENCE_STAFF = 5;//会议人员
        public static final int IM_GROUP_ROLE_MDT_DOCTOR = 6;//MDT医生
        public static final int IM_GROUP_ROLE_MDT_PATIENT = 7;//MDT患者家属
        public static final int IM_GROUP_ROLE_SYSTEM_AUTO = 255;//系统自动
    }

    /**
     * 1 屏幕共享，2 取消屏幕共享， 3 上级专家呼叫首诊， 4 首诊进入，5 首诊举手，6 上级处理首诊举手，7 上级专家点击下一个，
     * 8 设置主屏，9 踢人，10 禁言，11 解禁，12 主持人隐身，13 主持人解除隐身 14取消主屏，15播放视频文件，16停止播放视频文件
     */
    public static final class RoomOperationCode {
        public static final int ROOM_OPERATION_CODE_SCREEN_SHARE = 1;//屏幕共享
        public static final int ROOM_OPERATION_CODE_CANCLE_SCREEN_SHARE = 2;//取消屏幕共享
        public static final int ROOM_OPERATION_CODE_SWITCH_MEDICAL_RECORD = 4;//首诊进入
        public static final int ROOM_OPERATION_CODE_MAIN_SCREEN = 8;//设置主屏
        public static final int ROOM_OPERATION_CODE_KICK_OFF = 9;//踢人
        public static final int ROOM_OPERATION_CODE_FORBID_SPEAKING = 10;//禁言
        public static final int ROOM_OPERATION_CODE_RELIEVE_SPEAKING = 11;//解禁
        public static final int ROOM_OPERATION_CODE_HIDE = 12;//主持人隐身
        public static final int ROOM_OPERATION_CODE_DISPLAY = 13;//主持人解除隐身
        public static final int ROOM_OPERATION_CODE_CANCLE_MAIN_SCREEN = 14;//取消主屏
        public static final int ROOM_OPERATION_CODE_PLAY_VIDEO_FILE = 15;//播放视频文件
        public static final int ROOM_OPERATION_CODE_STOP_PLAY_VIDEO_FILE = 16;//停止播放视频文件
        public static final int ROOM_OPERATION_CODE_DOCTOR_ADVICE_MAKE_SURE = 31;//值班门诊-医嘱单确认
        public static final int ROOM_OPERATION_CODE_DOCTOR_PATIENT_ADD = 32;//值班门诊-更新患者


    }

    /**
     * status(uint32_t)
     * 第一位 屏幕共享，第二位 主屏，第三位 禁言，第四位 隐身，第五位 播放视频文件
     */
    public static final class VideoUintStatus {
        public static final int VIDEO_UINT_STATUS_SCREEN_SHARE = 1;//屏幕共享
        public static final int VIDEO_UINT_STATUS_MAIN_SCREEN = 2;//主屏
        public static final int VIDEO_UINT_STATUS_FORBID_SPEAKING = 4;//禁言
        public static final int VIDEO_UINT_STATUS_HIDE = 8;//隐身
        public static final int VIDEO_UINT_STATUS_PLAY_VIDEO_FILE = 16;//播放视频文件
    }

    public static final class H5MessageType {
        public static final int H5_MESSAGE_TYPE_FINISH = 0;//退出
        public static final int H5_MESSAGE_TYPE_SHARE_DOCTOR = 1;//分享医生
        public static final int H5_MESSAGE_TYPE_DATA_MANAGE = 2;//资料管理跳转
        public static final int H5_MESSAGE_TYPE_SHARE_URL = 3;//分享网页（PDF用）
        public static final int H5_MESSAGE_TYPE_SHARE_FILE = 4;//分享文件（PDF用）
        public static final int H5_MESSAGE_TYPE_ACCOUNT = 6;//我的医生跳转到我的账户页
        public static final int H5_MESSAGE_TYPE_ROUNDS_SHARE = 1001;//39互联网医院查房信息分享
        public static final int H5_MESSAGE_TYPE_IMG_DOWNLOAD = 1002;//图片下载

    }

    /**
     * 0/热点;1/wifi;2/2g;3/3g;4/4g;5/5g;128/未知;255/默认
     */
    public static final class NetWorkType {
        public static final int NETWORK_TYPE_HOT = 0;//热点
        public static final int NETWORK_TYPE_WIFI = 1;//wifi
        public static final int NETWORK_TYPE_2G = 2;//2g
        public static final int NETWORK_TYPE_3G = 3;//3g
        public static final int NETWORK_TYPE_4G = 4;//4g
        public static final int NETWORK_TYPE_5G = 5;//5g
        public static final int NETWORK_TYPE_UNKNOWN = 128;//未知
        public static final int NETWORK_TYPE_DEFAULT = 255;//默认
    }

    /**
     * 预约状态类型
     */
    public static final class ConsultantStatusType {
        public static final int APPOINTMENT_STATUS_TYPE_ALL = 0;//全部
        public static final int APPOINTMENT_STATUS_TYPE_TRIAGE = 1;//分诊中
        public static final int APPOINTMENT_STATUS_TYPE_CARRY_ON = 2;//进行
        public static final int APPOINTMENT_STATUS_TYPE_FINISH = 3;//完成
    }

    /**
     * 搜索类型
     */
    public static final class SearcType {
        public static final int SEARC_TYPE_NUM_TAB = 1;//预约编号
        public static final int SEARC_TYPE_DOCTOR_TAB = 3;//首诊姓名
    }

    public static final class PaymentType {
        public static final int PAYMENT_TYPE_RECORD = 1;//病历
        public static final int PAYMENT_TYPE_DURATION = 2;//时长
    }

    /**
     * 就诊类型
     */
    public static final class VisitType {
        public static final int VISIT_TYPE_VIDEO_CONSULTATION = 1;//视频会诊
        public static final int VISIT_TYPE_VIDEO_CONSULT = 2;//视频咨询
        public static final int VISIT_TYPE_IMAGE_CONSULTATION = 3;//影像会诊
        public static final int VISIT_TYPE_PATHOLOGY_CONSULTATION = 4;//病理会诊
        public static final int VISIT_TYPE_TURN_CONSULTATION = 5;//转诊
        public static final int VISIT_TYPE_VIDEO_CONSULTATION_FURTHER = 6;//视频会诊复诊
        public static final int VISIT_TYPE_OBSERVATION_FACE = 7;//面诊
        public static final int VISIT_TYPE_MULTIDISCIPLINARY_CONSULTATION = 8;//多学科会诊
        public static final int VISIT_TYPE_EXPATRIATE_CONSULTATION = 9;//外派会诊
        public static final int VISIT_TYPE_IMAGE_CONSULTATION_FURTHER = 10;//影像会诊复诊
        public static final int VISIT_TYPE_VIDEO_CONSULT_FURTHER = 11;//视频咨询复诊
        public static final int VISIT_TYPE_OBSERVATION_FACE_FURTHER = 12;//面诊复诊
        public static final int VISIT_TYPE_OUTPATIENT_SERVICE = 13;//门诊
        public static final int VISIT_TYPE_ROUNDS = 14;//查房
        public static final int VISIT_TYPE_IMAGE_CONSULTATION_BATCH = 15;//批量影像会诊
    }

    /**
     * 垫付结果
     */
    public static final class AdvanceResultType {
        public static final int RESULT_TYPE_FAIL = 1;
        public static final int RESULT_TYPE_SUC = 2;
    }

    /**
     * 呼叫页面类型
     */
    public static final class VideoCallPageType {
        public final static int PAGE_TYPE_CALL = 0;//页面状态：正在呼叫
        public final static int PAGE_TYPE_MISS_CALL = 1;//页面状态：未接听
        public final static int PAGE_TYPE_DOCTOR_ASSISTANT = 2;//导医页面状态：正在呼叫
    }

    /**
     * VideoWindow位置
     */
    public static final class VideoWindowPosition {
        public final static int ARROW_POSITION_TOP_LEFT = 0;
        public final static int ARROW_POSITION_TOP_CENTER = 1;
        public final static int ARROW_POSITION_TOP_RIGHT = 2;
        public final static int ARROW_POSITION_BOTTOM_LEFT = 3;
        public final static int ARROW_POSITION_BOTTOM_CENTER = 4;
        public final static int ARROW_POSITION_BOTTOM_RIGHT = 5;
    }

    public static final class CollegeCourseScreenType {
        public final static int COLLEGE_COURSE_SCREEN_TYPE_ALL = 0;
        public final static int COLLEGE_COURSE_SCREEN_TYPE_NEW = 1;
        public final static int COLLEGE_COURSE_SCREEN_TYPE_HOT = 2;
    }

    /**
     * 数据类型:
     * 1--标签模式；2--类型-video；3--分类-指南
     */
    public static final class CollegeModularType {
        public final static int COLLEGE_MODULAR_TYPE_LABEL = 1;
        public final static int COLLEGE_MODULAR_TYPE_VIDEO = 2;
        public final static int COLLEGE_MODULAR_TYPE_GUIDE = 3;
    }

    /**
     * 一键求助返回码21参数错误, 22无客服,23非工作时段
     */
    public static final class HelpCode {
        public final static int HELP_CODE_SUCCESS = 0;
        public final static int HELP_CODE_HAUVE_CUSTOM_SERVICE = 3;
        public final static int HELP_CODE_PARAMETER = 21;
        public final static int HELP_CODE_NO_CUSTOM_SERVICE = 22;
        public final static int HELP_CODE_NO_SCHEDULE = 23;
    }

    /**
     * 域名类型
     */
    public static final class DomainNameType {
        public final static int DOMAIN_NAME_TYPE_LOGIN = 1;//登录
        public final static int DOMAIN_NAME_TYPE_VIDEO = 2;//视频房间
    }

    /**
     * 上传病历首程图片状态
     */
    public static final class UploadFirstJourneyState {
        public final static int UPLOAD_FIRST_JOURNEY_STATE_WAIT = 0;//等待上传
        public final static int UPLOAD_FIRST_JOURNEY_STATE_ING = 1;//上传中
        public final static int UPLOAD_FIRST_JOURNEY_STATE_FAIL = 2;//上传失败
        public final static int UPLOAD_FIRST_JOURNEY_STATE_SUCCESS = 3;//上传成功
    }

    /**
     * 账户账单状态
     */
    public static final class AccountListState {
        public static final int ACCOUNT_LIST_PENDING_SETTLEMENT = 101;//未结算
        public static final int ACCOUNT_LIST_PAID = 102;//待提现
        public static final int ACCOUNT_LIST_CASHING = 103;//提现中
        public static final int ACCOUNT_LIST_WAIT_CASH = 104;//已打款
        public static final int ACCOUNT_LIST_WAIT_UNTREATED = 201;//欠款未处理
        public static final int ACCOUNT_LIST_UNTREATEDING = 202;//欠款处理中
        public static final int ACCOUNT_LIST_UNTREATED_FINISH = 203;//欠款已处理
    }

    /**
     * 账户账单状态
     * 角色身份 1专家组长，2专家组专家，3基层医生科主任，4基层医生，5管理员，6领衔专家，7首诊医生，8导医
     */
    public static final class CourseUserType {
        public static final int COURSE_USER_TYPE_GROUP = 1;//专家组长
        public static final int COURSE_USER_TYPE_SPECIALIST = 2;//专家组专家
        public static final int COURSE_USER_TYPE_DIRECTOR = 3;//基层医生科主任
        public static final int COURSE_USER_TYPE_SUBSTRATUM_DOCTOR = 4;//基层医生
        public static final int COURSE_USER_TYPE_ADMIN = 5;//管理员
        public static final int COURSE_USER_TYPE_LEAD_SPECIALIST = 6;//领衔专家
        public static final int COURSE_USER_TYPE_FIRST_DOCTOR = 7;//首诊医生
        public static final int COURSE_USER_TYPE_LEAD_DOCTOR = 8;//导医

    }

    /**
     * 自定义课程表tag：0 不可预约，1 可以预约，2 已经预约
     */
    public static final class CurrentDateState {
        public static final int NO_RESERVATION = 0;//不可预约课程
        public static final int CAN_RESERVATION = 1;//可以预约课程
        public static final int ALREADY_RESERVATION = 2;//已经预约课程
    }
}
