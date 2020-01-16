package cn.longmaster.hospital.doctor.core.requests;

/**
 * 接口配置
 * Created by yangyong on 16/7/14.
 */
public class OpTypeConfig {
    //duws
    private static final int DUWS_OPTYPE_BASE = 6000;
    public static final int DUWS_OPTYPE_REG_ACCOUNT = DUWS_OPTYPE_BASE + 1;//注册
    public static final int DUWS_OPTYPE_CHECK_VERIFY_CODE = DUWS_OPTYPE_BASE + 2;//验证码校验
    public static final int DUWS_OPTYPE_REG_CODE = DUWS_OPTYPE_BASE + 4;//获取验证码
    public static final int DUWS_OPTYPE_ACTIVE_ACCOUNT = DUWS_OPTYPE_BASE + 6;//账号激活
    public static final int DUWS_OPTYPE_QUERY_ACCOUNT = DUWS_OPTYPE_BASE + 7;//第三方登录
    public static final int DUWS_OPTYPE_PASSWORD_CHANGE = DUWS_OPTYPE_BASE + 8; //密码修改
    public static final int DUWS_OPTYPE_MESSAGE_REGISTER = DUWS_OPTYPE_BASE + 9;//短信注册
    public static final int DUWS_OPTYPE_SEND_MESSAGE = DUWS_OPTYPE_BASE + 10;//发送短信
    public static final int DUWS_OPTYPE_AGENT_SERVICE = DUWS_OPTYPE_BASE + 16;//获取代理服务器
    public static final int DUWS_OPTYPE_CHECK_ACCOUNT_EXIST = DUWS_OPTYPE_BASE + 11;//查询账号是否存在
    public static final int DUWS_OPTYPE_APP_KEY = DUWS_OPTYPE_BASE + 101;//获取AppKey

    //dws
    private static final int DWS_OPTYPE_BASE = 1000;
    public static final int DWS_OPTYPE_UPLOAD_MATERIAL = DWS_OPTYPE_BASE + 8;//上传病历文件
    public static final int DWS_OPTYPE_DEL_MATERIAL = DWS_OPTYPE_BASE + 16;//删除未审核辅助资料
    public static final int DWS_OPTYPE_COMMIT_MATERIAL = 5024;//上传病历文件
    public static final int DWS_OPTYPE_VIDEO_LIVE_CHECK = 2005;//根据病历号拉取指定订单的详细信息

    //dfs
    private static final int NGINX_OPTYPE_BASE = 3000;
    public static final int NGINX_OPTYPE_UPLOAD_MATERIAL = NGINX_OPTYPE_BASE + 4;//上传病历文件
    public static final int NGINX_OPTYPE_UPLOAD_VOICE = NGINX_OPTYPE_BASE + 5;//上传语音文件
    public static final int NGINX_OPTYPE_UPLOAD_CONSULT = NGINX_OPTYPE_BASE + 10;//上传/下载医嘱图片
    public static final int NGINX_OPTYPE_UPLOAD_PERSON_DATA = NGINX_OPTYPE_BASE + 21;//上传/下载个人资料
    public static final int NGINX_OPTYPE_IM_PIC = NGINX_OPTYPE_BASE + 24;//上传/下载群组图片
    public static final int NGINX_OPTYPE_IM_VOICE = NGINX_OPTYPE_BASE + 25;//上传/下载群组语音
    public static final int NGINX_OPTYPE_PAYMENT_PIC = NGINX_OPTYPE_BASE + 26;//上传/下载排班就诊支付确认单文件下载/平台服务费/确认单
    public static final int NGINX_OPTYPE_FIRST_JOURNEY = NGINX_OPTYPE_BASE + 35;//上传首程

    //ivws
    private static final int IVWS_OPTYPE_BASE = 7000;
    public static final int IVWS_OPTYPE_IDCARD_VERIFY = IVWS_OPTYPE_BASE + 1;//身份证验证

    //ndws
    public static final int DWS_UPLOAD_MATERIAL_MAKE_SURE = 8004;//提交材料信息确认

    //caws
    public static final int CAWS_OPTYPE_BASE = 9000;//
    public static final int CAWS_OPTYPE_RECIPE_SYNC = CAWS_OPTYPE_BASE + 1;//处方同步接口
    public static final int CAWS_OPTYPE_SIGNATURE_VERIFY = CAWS_OPTYPE_BASE + 4;//签章信息验证接口

    //clientApi
    private static final int CLIENTAPI_OPTYE_BASE = 100000;
    public static final int CLIENTAPI_OPTYE_HOSPITAL_INFO = CLIENTAPI_OPTYE_BASE + 1;//医院信息
    public static final int CLIENTAPI_OPTYE_MATERIAL_INFO = CLIENTAPI_OPTYE_BASE + 2;//系统材料配置
    public static final int CLIENTAPI_OPTYE_DEPARTMENT_INFO = CLIENTAPI_OPTYE_BASE + 3;//科室信息
    public static final int CLIENTAPI_OPTYE_TITLE_GRADE_INFO = CLIENTAPI_OPTYE_BASE + 4;//医生职称等级配置
    public static final int CLIENTAPI_OPTYE_AREA_GRADE_INFO = CLIENTAPI_OPTYE_BASE + 5;//医生区域等级配置
    public static final int CLIENTAPI_OPTYE_PACKAGE_INFO = CLIENTAPI_OPTYE_BASE + 6;//套餐信息配置
    public static final int CLIENTAPI_OPTYE_GRADE_PRICE_INFO = CLIENTAPI_OPTYE_BASE + 7;//价格档位配置
    public static final int CLIENTAPI_OPTYE_RUTINE_CONFIG = CLIENTAPI_OPTYE_BASE + 8;//常规配置
    public static final int CLIENTAPI_OPTYE_DOCTOR_BASE_INFO = CLIENTAPI_OPTYE_BASE + 9;//医生基本信息
    public static final int CLIENTAPI_OPTYE_PATIENT_BASE_INFO = CLIENTAPI_OPTYE_BASE + 10;//患者基本信息
    public static final int CLIENTAPI_OPTYE_BANNER_AND_QUICK_ENTRY = CLIENTAPI_OPTYE_BASE + 11;//客户端Banner与快捷入口配置
    public static final int CLIENTAPI_OPTYE_NATION_INFO_CONFIG = CLIENTAPI_OPTYE_BASE + 12;//民族信息配置
    public static final int CLIENTAPI_OPTYE_PRIVINCE_CITY_INFO_CONFIG = CLIENTAPI_OPTYE_BASE + 13;//省份城市信息配置

    public static final int CLIENTAPI_OPTYE_BANK_CARD = CLIENTAPI_OPTYE_BASE + 101;//用户银行卡列表
    public static final int CLIENTAPI_OPTYE_DEPARTMENT_RELATE = CLIENTAPI_OPTYE_BASE + 102;//科室关联信息
    public static final int CLIENTAPI_OPTYE_ISSUE_INVOICE = CLIENTAPI_OPTYE_BASE + 103;//开具发票
    public static final int CLIENTAPI_OPTYE_SERVICE_AUTHORITY = CLIENTAPI_OPTYE_BASE + 104;//根据医生ID获取服务权限
    public static final int CLIENTAPI_OPTYE_DOCTOR_GRADE_PRICE = CLIENTAPI_OPTYE_BASE + 105;//根据医生ID获取关联的价格档位
    public static final int CLIENTAPI_OPTYE_ALL_DEPARTMENT_LIST = CLIENTAPI_OPTYE_BASE + 106;//获取全部科室列表
    public static final int CLIENTAPI_OPTYE_ASSISTANT_DOCTOR = CLIENTAPI_OPTYE_BASE + 107;//拉取助理医师信息
    public static final int CLIENTAPI_OPTYE_AUXILIARY_MATERIAL_LIST = CLIENTAPI_OPTYE_BASE + 108;//获取辅助资料列表
    public static final int CLIENTAPI_OPTYE_GET_RECORD = CLIENTAPI_OPTYE_BASE + 109;//根据预约ID获取关联病历
    public static final int CLIENTAPI_OPTYE_DOCTOR_DIAGNOSIS = CLIENTAPI_OPTYE_BASE + 110;//根据预约ID拉取医嘱
    public static final int CLIENTAPI_OPTYE_ADD_REMARK = CLIENTAPI_OPTYE_BASE + 111;//给病历添加备注
    public static final int CLIENTAPI_OPTYE_USER_BILL_DETAIL = CLIENTAPI_OPTYE_BASE + 112;//拉取用户账单明细
    public static final int CLIENTAPI_OPTYE_DOCTOR_BY_DEPARTMENT = CLIENTAPI_OPTYE_BASE + 113;//按科室ID拉取医生列表
    public static final int CLIENTAPI_OPTYE_SEARCH_DOCTOR_BY_NAME = CLIENTAPI_OPTYE_BASE + 114;//根据医生姓名搜索医生
    public static final int CLIENTAPI_OPTYE_FINISH_APPOINT = CLIENTAPI_OPTYE_BASE + 115;//修改预约（视频完成）
    public static final int CLIENTAPI_OPTYE_DOCTOR_SCHEDULE = CLIENTAPI_OPTYE_BASE + 116;//获取医生排班信息
    public static final int CLIENTAPI_OPTYE_SCHEDULING_DOCTOR = CLIENTAPI_OPTYE_BASE + 117;//获取有排班的医生
    public static final int CLIENTAPI_OPTYE_FINISHED_SCHEDULE_LIST = CLIENTAPI_OPTYE_BASE + 118;//获取已完成排班列表
    public static final int CLIENTAPI_OPTYE_DOCTOR_CONSULT_STATISTIC = CLIENTAPI_OPTYE_BASE + 119;//统计医生的排班、影像服务、就诊、复诊数量
    public static final int CLIENTAPI_OPTYE_DOCTOR_FINANCE_STATISTIC = CLIENTAPI_OPTYE_BASE + 120;//医生财务统计
    public static final int CLIENTAPI_OPTYE_SCHEDULING_BY_DATE = CLIENTAPI_OPTYE_BASE + 121;//根据日期获取排班
    public static final int CLIENTAPI_OPTYE_SKIP_APPOINT = CLIENTAPI_OPTYE_BASE + 122;//跳过预约
    public static final int CLIENTAPI_OPTYE_RELATE_RECORDER = CLIENTAPI_OPTYE_BASE + 123;//关联病历
    public static final int CLIENTAPI_OPTYE_SUPER_CONSULT_BY_RECURE = CLIENTAPI_OPTYE_BASE + 124;//根据复诊获取上级就诊信息
    public static final int CLIENTAPI_OPTYE_SCREEN_APPOINTMENT = CLIENTAPI_OPTYE_BASE + 125;//根据条件获取筛选预约信息
    public static final int CLIENTAPI_OPTYE_ISSUE_DOCTOR_ADVICE = CLIENTAPI_OPTYE_BASE + 126;//出具医嘱
    public static final int CLIENTAPI_OPTYE_SET_DEFAULT_ACCOUNT = CLIENTAPI_OPTYE_BASE + 127;//设置用户银行账户为使用状态
    public static final int CLIENTAPI_OPTYE_WRITE_CONSULT_ADVICE = CLIENTAPI_OPTYE_BASE + 128;//给预约填写会诊意见，结束预约
    public static final int CLIENTAPI_OPTYE_DELETE_VOICE_RECORD = CLIENTAPI_OPTYE_BASE + 129;//删除会诊意见录音
    public static final int CLIENTAPI_OPTYE_DELETE_CONSULT_ADVICE_PICTURE = CLIENTAPI_OPTYE_BASE + 130;//删除会诊意见图片
    public static final int CLIENTAPI_OPTYE_SET_PARTICIPATE_RECONSULT = CLIENTAPI_OPTYE_BASE + 131;//设置首诊医生是否参与复诊
    public static final int CLIENTAPI_OPTYE_LAUNCH_CONSULT = CLIENTAPI_OPTYE_BASE + 132;//发起会诊
    public static final int CLIENTAPI_OPTYE_GET_DOCTOR_LIST = CLIENTAPI_OPTYE_BASE + 133;//获取全部医生列表
    public static final int CLIENTAPI_OPTYE_GET_SCHEDULING_BY_DEPARTMENT = CLIENTAPI_OPTYE_BASE + 134;//根据科室ID和日期区间获取排班/影像服务列表
    public static final int CLIENTAPI_OPTYE_ADD_SCHEDULING = CLIENTAPI_OPTYE_BASE + 135;//新增排班
    public static final int CLIENTAPI_OPTYE_REVISE_SCHEDULE = CLIENTAPI_OPTYE_BASE + 136;//修改排班
    public static final int CLIENTAPI_OPTYE_ADD_SERVICE = CLIENTAPI_OPTYE_BASE + 137;//新增影像服务
    public static final int CLIENTAPI_OPTYE_REVISE_IMAGE_SERVICE = CLIENTAPI_OPTYE_BASE + 138;//修改影像服务
    public static final int CLIENTAPI_OPTYE_OPEN_SCHEDULING = CLIENTAPI_OPTYE_BASE + 139;//开启排班/影像服务
    public static final int CLIENTAPI_OPTYE_CLOSE_SERVICE = CLIENTAPI_OPTYE_BASE + 140;//关闭排班/影像服务
    public static final int CLIENTAPI_OPTYE_DELETE_SCHEDULING = CLIENTAPI_OPTYE_BASE + 141;//删除排班/影像服务
    public static final int CLIENTAPI_OPTYE_SHCEDULE_OR_IMAGE = CLIENTAPI_OPTYE_BASE + 142;//获取排班/影像服务项目
    public static final int CLIENTAPI_OPTYE_SHCEDULE_APPOINTMENT_LIST = CLIENTAPI_OPTYE_BASE + 144;//获取排班预约列表
    public static final int CLIENTAPI_OPTYE_SERVICE_APPOINTMENT = CLIENTAPI_OPTYE_BASE + 145;//获取影像服务预约列表
    public static final int CLIENTAPI_OPTYE_PATIENT_CONSULT_LIST = CLIENTAPI_OPTYE_BASE + 146;//根据状态获取患者就诊列表
    public static final int CLIENTAPI_OPTYE_BALANCE_WITHDRAWAL = CLIENTAPI_OPTYE_BASE + 147;//余额提现接口
    public static final int CLIENTAPI_OPTYE_CASE_REMARK_LIST = CLIENTAPI_OPTYE_BASE + 148;//获取病历备注列表
    public static final int CLIENTAPI_OPTYE_APPOINTMENT_BY_ID = CLIENTAPI_OPTYE_BASE + 149;//根据预约ID获取预约信息
    public static final int CLIENTAPI_OPTYE_APPOINTMENT_LIST_BY_DOCTOR = CLIENTAPI_OPTYE_BASE + 150;//根据医生ID和日期及发起人类型拉取医生的预约列表
    public static final int CLIENTAPI_OPTYE_APPOINTMENT_ORDER = CLIENTAPI_OPTYE_BASE + 151;//根据预约ID拉取预约订单信息
    public static final int CLIENTAPI_OPTYE_SCHEDULING_STATISTIC = CLIENTAPI_OPTYE_BASE + 152;//根据医生ID和服务模式统计排班模式价格区间
    public static final int CLIENTAPI_OPTYE_BIND_RECORD = CLIENTAPI_OPTYE_BASE + 153;//关联病历
    public static final int CLIENTAPI_OPTYE_COMMIT_MATERIAL = CLIENTAPI_OPTYE_BASE + 154;//根据预约ID提交材料审核
    public static final int CLIENTAPI_OPTYE_ADD_BANK_ACCOUNT = CLIENTAPI_OPTYE_BASE + 155;//添加银行账户
    public static final int CLIENTAPI_OPTYE_DELETE_BANK_ACCOUNT = CLIENTAPI_OPTYE_BASE + 156;//删除银行账户
    public static final int CLIENTAPI_OPTYE_GET_HOSPITAL_DEPARTMENT_INFO = CLIENTAPI_OPTYE_BASE + 165;//根据医院id及医院所属科室id拉取医院及科室信息
    public static final int CLIENTAPI_OPTYE_GET_PROVINCE_LIST = CLIENTAPI_OPTYE_BASE + 166;//获取省份和城市列表(二维数组)
    public static final int CLIENTAPI_OPTYE_GET_HOSPITAL_LIST = CLIENTAPI_OPTYE_BASE + 167;//根据省份城市分页获取医院
    public static final int CLIENTAPI_OPTYE_MESSAGE_LIST = CLIENTAPI_OPTYE_BASE + 205;//根据用户拉取通知信息列表
    public static final int CLIENTAPI_OPTYE_INFORMATION_VERIFY = CLIENTAPI_OPTYE_BASE + 207;//根据预约ID和患者姓名验证信息正确性
    public static final int CLIENTAPI_OPTYE_GET_GUIDE_DEPARTMENT_LIST = CLIENTAPI_OPTYE_BASE + 212;//获取指南资料科室列表
    public static final int CLIENTAPI_OPTYE_GET_GROUP_LIST = CLIENTAPI_OPTYE_BASE + 225;//分页获取相应的群组列表
    public static final int CLIENTAPI_OPTYE_GET_CHAT_HISTORY_LIST = CLIENTAPI_OPTYE_BASE + 226;//根据不同条件获取相应的历史聊天记录
    public static final int CLIENTAPI_OPTYE_CHAT_GET_MEMBER_LIST = CLIENTAPI_OPTYE_BASE + 227;//根据预约ID获取群成员列表
    public static final int CLIENTAPI_OPTYE_CHAT_ADD_MEDICAL_RECORD = CLIENTAPI_OPTYE_BASE + 228;//根IM群组控制-将IM聊天中的指定的图片复制到患者材料目录
    public static final int CLIENTAPI_OPTYE_CHECK_VERSION = CLIENTAPI_OPTYE_BASE + 229;//检查更新
    public static final int CLIENTAPI_OPTYE_REPRESENT_FUNCTION = CLIENTAPI_OPTYE_BASE + 231;//获取代表功能列表
    public static final int CLIENTAPI_OPTYE_GET_VISIT_LIST = CLIENTAPI_OPTYE_BASE + 232;//销售代表根据自己的账号ID获取向关联的预约列表
    public static final int CLIENTAPI_OPTYE_GET_CONSULTAN_LIST = CLIENTAPI_OPTYE_BASE + 233;//销售代表根据自己的账号ID获取相关联的排班列表
    public static final int CLIENTAPI_OPTYE_VISIT_DOCTOR_STAR = CLIENTAPI_OPTYE_BASE + 234;//销售代表根据自己的账号ID管理自己的医生星号标记
    public static final int CLIENTAPI_OPTYE_VISIT_DOCTOR_LIST = CLIENTAPI_OPTYE_BASE + 235;//销售代表根据自己的账号获取自己所管理的医生列表
    public static final int CLIENTAPI_OPTYE_GET_SCHEDULE_PAYMENT = CLIENTAPI_OPTYE_BASE + 236;//获取排班相关的支付确认单信息
    public static final int CLIENTAPI_OPTYE_MODIFY_SCHEDULE_PAYMENT = CLIENTAPI_OPTYE_BASE + 237;//编辑排班相关的支付确认单信息
    public static final int CLIENTAPI_OPTYE_ADVANCE = CLIENTAPI_OPTYE_BASE + 238;//根据预约列表进行垫付进行支付操作
    public static final int CLIENTAPI_OPTYE_GET_COLLEGE_CLASSIFICATION_CONFIG = CLIENTAPI_OPTYE_BASE + 258;//医学院拉取分类配置
    public static final int CLIENTAPI_OPTYE_COURSE_LIST = CLIENTAPI_OPTYE_BASE + 259;//医学院根据分类标签、最新最热条件拉取分类课程列表
    public static final int CLIENTAPI_OPTYE_GET_COURSE_INFO = CLIENTAPI_OPTYE_BASE + 260;//根据课程id拉取课程详情接口
    public static final int CLIENTAPI_OPTYE_GET_INTERACTION_LIST = CLIENTAPI_OPTYE_BASE + 261;//拉取评论列表
    public static final int CLIENTAPI_OPTYE_SUBMIT_CONTENT = CLIENTAPI_OPTYE_BASE + 262;//提交评论
    public static final int CLIENTAPI_OPTYE_COURSE_LIKE = CLIENTAPI_OPTYE_BASE + 263;//点赞
    public static final int CLIENTAPI_OPTYE_GET_GUIDE_DATA = CLIENTAPI_OPTYE_BASE + 264;//医学院根据科室ID、疾病名称和年份获取指南资料信息
    public static final int CLIENTAPI_OPTYE_GET_YEAR = CLIENTAPI_OPTYE_BASE + 265;//获取年份
    public static final int CLIENTAPI_OPTYE_UPLOAD_LOCATION_NETWORK = CLIENTAPI_OPTYE_BASE + 266;//视频诊室上传ip地址及经纬度信息
    public static final int CLIENTAPI_OPTYE_SUBMIT_DOMAIN_NAME = CLIENTAPI_OPTYE_BASE + 269;//httpdns域名解析ip结果上报
    public static final int CLIENTAPI_OPTYE_SUBMISSION_PERSONAL_DATA = CLIENTAPI_OPTYE_BASE + 279;//提交个人资料文献
    public static final int CLIENTAPI_OPTYE_GET_PERSONAL_DATA = CLIENTAPI_OPTYE_BASE + 280;//拉取个人资料文献
    public static final int CLIENTAPI_OPTYE_GDELETE_PERSONAL_DATA = CLIENTAPI_OPTYE_BASE + 281;//删除个人资料文献
    public static final int CLIENTAPI_OPTYE_GET_RELATION_MEDICAL_LIST = CLIENTAPI_OPTYE_BASE + 282;//拉取资料关联病历列表
    public static final int CLIENTAPI_OPTYE_RELATION_MEDICAL = CLIENTAPI_OPTYE_BASE + 283;//病历关联/取消关联资料
    public static final int CLIENTAPI_OPTYE_GET_ACCOUNT_FLOW = CLIENTAPI_OPTYE_BASE + 290;//根据用户id拉取账户流水明细
    public static final int CLIENTAPI_OPTYE_GET_ACCOUNT_LIST = CLIENTAPI_OPTYE_BASE + 291;//根据账户id及状态分页拉取就诊清单
    public static final int CLIENTAPI_OPTYE_GET_ACCOUNT_CAN_CASH_LIST = CLIENTAPI_OPTYE_BASE + 292;//根据账户id拉取账户可提现列表
    public static final int CLIENTAPI_OPTYE_GET_ACCOUNT_CASH = CLIENTAPI_OPTYE_BASE + 293;//提现
    public static final int CLIENTAPI_OPTYE_GET_ACCOUNT_MERGER_CASH_LIST = CLIENTAPI_OPTYE_BASE + 294;//根据合并提现流水号拉取合并提现详情列表


    //查房
    public static final int CLIENTAPI_OPTYE_GET_COMMONLY_EXPERT_LIST = CLIENTAPI_OPTYE_BASE + 501;//根据首诊医生id和科室id拉取常用专家列表
    public static final int CLIENTAPI_OPTYE_GET_COMMONLY_DEPARTMENT = CLIENTAPI_OPTYE_BASE + 502;//根据首诊医生id拉取常用科室列表
    public static final int CLIENTAPI_OPTYE_GET_RECOMMEND_RECEPTION_TIME = CLIENTAPI_OPTYE_BASE + 503;//根据医生id拉取推荐接诊时间
    public static final int CLIENTAPI_OPTYE_UPLOAD_FIRST_JOURNEY = CLIENTAPI_OPTYE_BASE + 504;//上传首程
    public static final int CLIENTAPI_OPTYE_SUBMISSION_ORDER = CLIENTAPI_OPTYE_BASE + 505;//提交订单
    public static final int CLIENTAPI_OPTYE_GET_ORDER_LIST = CLIENTAPI_OPTYE_BASE + 506;//根据订单状态分页拉取订单列表
    public static final int CLIENTAPI_OPTYE_GET_ORDER_DETAILS = CLIENTAPI_OPTYE_BASE + 507;//根据订单id拉取订单详情
    public static final int CLIENTAPI_OPTYE_GET_MEDICAL_RECORD = CLIENTAPI_OPTYE_BASE + 508;//根据病历id拉取病历详情
    public static final int CLIENTAPI_OPTYE_RECEIVE = CLIENTAPI_OPTYE_BASE + 509;//接诊
    public static final int CLIENTAPI_OPTYE_REFUSAL = CLIENTAPI_OPTYE_BASE + 510;//暂不接诊
    public static final int CLIENTAPI_OPTYE_GET_MEDICAL_RECORD_MATERIAL = CLIENTAPI_OPTYE_BASE + 511;//据病历id拉取病历辅助检查
    public static final int CLIENTAPI_OPTYE_UPLOAD_MEDICAL_RECORD_MATERIAL = CLIENTAPI_OPTYE_BASE + 512;//上传病历资料
    public static final int CLIENTAPI_OPTYE_DELETE_MEDICAL_RECORD_MATERIAL = CLIENTAPI_OPTYE_BASE + 513;//删除病历资料
    public static final int CLIENTAPI_OPTYE_FINISH_ORDER_ROOM_VISIT = CLIENTAPI_OPTYE_BASE + 514;//完成订单视频就诊
    public static final int CLIENTAPI_OPTYE_GET_RECOMMEND_DOCTOR_DEPARTMENT = CLIENTAPI_OPTYE_BASE + 515;//根据科室id拉取科室推荐医生列表。
    public static final int CLIENTAPI_OPTYE_GET_RECOMMEND_DOCTOR_LIST = CLIENTAPI_OPTYE_BASE + 516;//拉取推荐医生列表
    public static final int CLIENTAPI_OPTYE_ADD_PATIENT = CLIENTAPI_OPTYE_BASE + 517;//添加患者
    public static final int CLIENTAPI_OPTYE_ADD_PATIENT_APPEAL_CONFIGURE_FILE = CLIENTAPI_OPTYE_BASE + 518;//查房诉求配置列表
    public static final int CLIENTAPI_OPTYE_INTENTION_TIME_CONFIGURE = CLIENTAPI_OPTYE_BASE + 519;//查房意向时间配置列表拉取
    public static final int CLIENTAPI_OPTYE_CHECK_ROUNDS_MEDICAL = CLIENTAPI_OPTYE_BASE + 521;//校验查房预约的病历编号及患者姓名。
    public static final int CLIENTAPI_OPTYE_MODIFY_INFORMATION = CLIENTAPI_OPTYE_BASE + 525;//修改患者信息
    public static final int CLIENTAPI_OPTYE_RELATED_MEDICAL_RECORDS = CLIENTAPI_OPTYE_BASE + 526;//病历关联/取消关联
    public static final int CLIENTAPI_OPTYE_DELETE_MEDICAL_RECORDS = CLIENTAPI_OPTYE_BASE + 527;//删除患者
    public static final int CLIENTAPI_OPTYE_GET_WAIT_ROUNDS_PATIENT = CLIENTAPI_OPTYE_BASE + 528;//获取待查房患者列表。
    public static final int CLIENTAPI_OPTYE_GET_DATA_CENTER_AUTHORITY = CLIENTAPI_OPTYE_BASE + 529;//获取数据中心入口权限。
    public static final int CLIENTAPI_OPTYE_GET_ROUNDS_ASSOCIATED_MEDICAL = CLIENTAPI_OPTYE_BASE + 531;//拉取查房病历关联病历接口
    public static final int CLIENTAPI_OPTYE_GET_SPECIALTY_CONSTRUCTION_PROJECT_LIST = CLIENTAPI_OPTYE_BASE + 532;//获取用户专科项目列表。
    public static final int CLIENTAPI_OPTYE_GET_PROJECT_DETAILS = CLIENTAPI_OPTYE_BASE + 533;//根据专科建设项目ID获取详情。
    public static final int CLIENTAPI_OPTYE_GET_PROJECT_COURSE_LIST = CLIENTAPI_OPTYE_BASE + 534;//根据专科建设项目阶段下面的课程表。
    public static final int CLIENTAPI_OPTYE_RESERVATION_COURSE = CLIENTAPI_OPTYE_BASE + 535;//预约课程。
    public static final int CLIENTAPI_OPTYE_CANCEL_COURSE = CLIENTAPI_OPTYE_BASE + 536;//取消预约课程。
    public static final int CLIENTAPI_OPTYE_BINDING_APPOINT_TO_COURSE = CLIENTAPI_OPTYE_BASE + 540;//课程预约添加就诊编号
    public static final int CLIENTAPI_OPTYE_GET_ASSESS_INFO = CLIENTAPI_OPTYE_BASE + 547;//获取评估信息
}
