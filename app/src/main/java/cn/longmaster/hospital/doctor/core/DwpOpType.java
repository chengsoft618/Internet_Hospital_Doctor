package cn.longmaster.hospital.doctor.core;

/**
 * DWP接口操作类型定义类
 * Created by yangyong on 2015/7/18.
 */
public class DwpOpType {
    /*////////////////////////////////////// 1 /////////////////////////////////////////*/
    //首页幻灯片图片拉取
    public static final String HOME_PAGE_BANNER = "1001";
    //拉取优秀医生列表
    public static final String GOOD_DOCTOR = "1002";
    //根据医生ID拉取医生详情
    public static final String DOCTOR_DETAIL = "1003";
    //拉取所有科室列表
    public static final String ALL_DEPARTMENTS = "1004";
    //根据二级科室id拉取医生列表
    public static final String PULL_SUB_DOCTOR = "1005";
    //根据医生名字或医院名字搜索专家
    public static final String SEARCH_SPECIALIST_DOCTOR = "1006";
    //提交预约订单
    public static final String SUBMIT_RESERVATION_ORDER = "1007";
    //上传病历文件
    public static final String UPLOAD_MEDICAL_RECORDS = "1008";
    //拉取患者病历 (未完成)
    public static final String PULL_PATIENT_RECORDS = "1009";
    //拉取所有疾病
    public static final String PULL_ALL_DISEASE = "1010";
    //拉取病历详情
    public static final String APPOINTMENT_DETAIL = "1011";
    //关键字搜索疾病
    public static final String SEARCH_DISEASE = "1012";
    //拉取最新预约
    public static final String LATEST_APPOINTMENT = "1013";
    //拉取团队医生
    public static final String GROUT_DOCTORS = "1014";
    //拉取单项辅助资料列表
    public static final String SINGLE_MATERIAL_LIST = "1015";
    //删除未审核辅助资料
    public static final String DEL_MATERIAL_PIC = "1016";
    //设置团队
    public static final String SET_TEAM = "1017";
    //团队信息
    public static final String TEAM_INFO = "1018";
    //完结预约
    public static final String FINISH_APPOINTMENT = "1019";
    //拉取预约信息列表
    public static final String APPOINTMENT_LIST = "1020";
    //领衔专家
    public static final String LEAD_SPECIALIST = "1021";
    //预约详情
    public static final String APPOINTMENT_DETAIL_NEW = "1023";
    //预约id所有复诊信息
    public static final String RECURE_INFO_LIST = "1024";
    //添加预约
    public static final String ADD_ORDER = "1025";
    //材料列表 upload
    public static final String MATERIAL_INFO_LIST = "1026";
    //报告列表
    public static final String REPORT_LIST = "1027";
    //材料列表 medical
    public static final String MEDICAL_MATERIAL_INFO_LIST = "1028";
    //消息列表
    public static final String MESSAGE_LIST = "1029";
    //版本信息
    public static final String VERSION_INFO = "1030";
    //获取科室详情
    public static final String DEPARTMENT_INFO = "1032";
    //根据科室id,医生区域等级拉取医生列表
    public static final String DOCTOR_LIST = "1033";
    //医生排班信息
    public static final String DOCTOR_SCHEDULE = "1034";
    //转班
    public static final String CHANGE_SCHEDULE = "1035";

    /*////////////////////////////////////// 2 /////////////////////////////////////////*/
    //记录视频网络日志
    public static final String RECORD_NET_LOG = "2033";

    /*////////////////////////////////////// 3 /////////////////////////////////////////*/
    //下载启动图片
    public static final String DOWNLOAD_APP_START = "3002";

    /*////////////////////////////////////// 4 /////////////////////////////////////////*/
    //拉取补充资料
    public static final String PULL_ADD_MATERIAL = "4001";
    //支付预约
    public static final String PAY_APPOINTMENT = "4002";
    //支付预约确认/退款
    public static final String PAY_CONFIRM_OR_REFUND = "4003";
    //拉取复诊列表
    public static final String RECURE_LIST = "4004";

    /*////////////////////////////////////// 5 /////////////////////////////////////////*/
    // 上传日志文件
    public static final String UPLOAD_LOG = "5007";
    //拉取流程配置
    public static final String APPOINTMENT_ACTIONS = "5011";
    //拉取医生助理
    public static final String ASSISTANT_DOCTOR = "5013";
    //提交退款
    public static final String REFUND_SUBMIT = "5014";
    //拉取退款状态
    public static final String REFUND_STATE = "5015";
    //游客提交预约订单
    public static final String VISITOR_SUBMIT_ORDER = "5016";
    //拉取材料审核信息
    public static final String MATERIAL_STATE = "5017";
    //拉取套餐信息
    public static final String SERVICE_INFO = "5019";
    //拉取退款须知
    public static final String REFUND_NOTICE = "5020";
    //拉取医嘱图片
    public static final String DOCTOR_MESSAGE_PICTURE = "5021";
    //拉取所有材料
    public static final String APPOINTMENT_AND_MATERIAL = "5023";
    //提交资料审核
    public static final String COMMIT_MATERIAL = "5024";

    /*////////////////////////////////////// 6 /////////////////////////////////////////*/
    //注册
    public static final String REG_ACCOUNT = "6001";
    //验证码校验
    public static final String CHECK_VERIFY_CODE = "6002";
    //获取验证码
    public static final String REG_CODE = "6004";
    //账号激活
    public static final String ACTIVE_ACCOUNT = "6006";
    //第三方登录
    public static final String QUERY_ACCOUNT = "6007";
    //密码修改
    public static final String PASSWORD_CHANGE = "6008";
    //短信注册
    public static final String MESSAGE_REGISTER = "6009";
    //发送短信
    public static final String SEND_MESSAGE = "6010";
    //获取AppKey
    public static final String APP_KEY = "6101";
    //PayEco生成订单
    public static final String PAY_ECO_NEW_ORDER = "6102";
    //支付宝生成订单
    public static final String PAY_ALIPAY_NEW_ORDER = "6104";
    //强制确认订单已支付
    public static final String PAYMENT_ORDER_IS_PAYED = "6105";

    /*////////////////////////////////////// 7 /////////////////////////////////////////*/
    //就诊改签须知
    public static final String VISIT_ALTER_NOTICE = "7001";
    //验证码
    public static final String VERIFICATION_CODE = "7002";
    //验证身份证信息(带验证码)
    public static final String ID_CHECK = "7004";
}
