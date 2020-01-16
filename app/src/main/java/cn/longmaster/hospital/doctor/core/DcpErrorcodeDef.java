package cn.longmaster.hospital.doctor.core;

import cn.longmaster.hospital.doctor.R;

/**
 * Pes 服务器错误码定义
 * Created by ddc on 2015-07-20.
 */
public class DcpErrorcodeDef {
    public static final int ERROR_NET = -1;
    public static final int ERROR_TIME_OUT = -1000000;

    public static final int RET_ERROR = 0xFFFFFFFF;
    public static final int RET_SUCCESS = 0x0;

    public static final int ERROR_CODE_BASE = 1000000;
    public static final int ERROR_CODE_SYSTEM_BASE = ERROR_CODE_BASE + 10000;    //系统相关错误码基数
    public static final int ERROR_CODE_DB_BASE = ERROR_CODE_BASE + 20000;    //数据库相关错误码基数
    public static final int ERROR_CODE_USER_BASE = ERROR_CODE_BASE + 30000;    //用户相关错误码基数
    public static final int ERROR_CODE_ROOM_BASE = ERROR_CODE_BASE + 40000;   //诊疗室相关错误码基数

    /************************************************************************/
    /*                     系统相关错误码								    */
    /**
     * ********************************************************************
     */
    public static final int RET_COMMUNICATION_OVERTIME = ERROR_CODE_SYSTEM_BASE + 0; //通信超时
    public static final int RET_NOT_ENOUGH_MEMORY = ERROR_CODE_SYSTEM_BASE + 1; //内存不足
    public static final int RET_PARAMETER_ERROR = ERROR_CODE_SYSTEM_BASE + 2; //参数错误
    public static final int RET_PACK_FAILED = ERROR_CODE_SYSTEM_BASE + 3; //网络数据包打包失败
    public static final int RET_PACK_SEND_FAILED = ERROR_CODE_SYSTEM_BASE + 4; //网络数据包发送失败

    public static final int RET_DB_ERROR = ERROR_CODE_SYSTEM_BASE + 5; //数据库错误
    public static final int RET_DB_GET_CONNECT_FAILED = ERROR_CODE_SYSTEM_BASE + 6; //获取数据库连接失败
    public static final int RET_SYSTEM_ERROR = ERROR_CODE_SYSTEM_BASE + 7; //系统错误
    public static final int RET_UNPACK_FAULT = ERROR_CODE_SYSTEM_BASE + 8; //解包失败
    public static final int RET_PARAMETER_ILLEGAL = ERROR_CODE_SYSTEM_BASE + 9; //参数违法的, 不合规定的

    public static final int RET_CREATE_SHARE_MEM_FAILED = ERROR_CODE_SYSTEM_BASE + 10; //创建共享内存失败
    public static final int RET_ENOENT = ERROR_CODE_SYSTEM_BASE + 11; //共享内存不存在
    public static final int RET_EEXIST = ERROR_CODE_SYSTEM_BASE + 12; //共享内存已经存在，无法重新创建
    public static final int RET_ENFILE = ERROR_CODE_SYSTEM_BASE + 13; //打开文件太多
    public static final int RET_EACCES = ERROR_CODE_SYSTEM_BASE + 14; //没权限创建或者打开共享内存

    public static final int RET_ENOSPC = ERROR_CODE_SYSTEM_BASE + 15; //创建共享内存个数已经达到上限
    public static final int RET_EPERM = ERROR_CODE_SYSTEM_BASE + 16; //创建共享内存操作被拒绝
    public static final int RET_DT_SHAREMEM_ERROR = ERROR_CODE_SYSTEM_BASE + 17; //分离共享内存失败
    public static final int RET_DEL_SHAREMEM_ERROR = ERROR_CODE_SYSTEM_BASE + 18; //删除共享内存失败
    public static final int RET_GET_SHAREMEM_AT_ERROR = ERROR_CODE_SYSTEM_BASE + 19; //获取共享内存绑定数失败

    public static final int RET_NOT_ENOUGH_SOCKET = ERROR_CODE_SYSTEM_BASE + 20; //没有足够的套接字以响应php方法调用
    public static final int RET_UDP_RECV_NET_ERROR = ERROR_CODE_SYSTEM_BASE + 21; //UDP收包网络错误
    public static final int RET_UDP_RECV_SOCKET_ERROR = ERROR_CODE_SYSTEM_BASE + 22; //UDP收包SOCKET错误
    public static final int RET_UDP_RECV_UNPACK_ERROR = ERROR_CODE_SYSTEM_BASE + 23; //UDP收包底层解包失败
    public static final int RET_UDP_RECV_BUFFER_TOO_SMALL = ERROR_CODE_SYSTEM_BASE + 24; //UDP收包缓冲区太小

    public static final int RET_UDP_RECV_DATA_EMPTY = ERROR_CODE_SYSTEM_BASE + 25; //UDP收包接收数据为空
    public static final int RET_UDP_RECV_PACK_TOO_SHORT = ERROR_CODE_SYSTEM_BASE + 26; //UDP收包接收包太短
    public static final int RET_UDP_RECV_PACK_OTHER_ERR = ERROR_CODE_SYSTEM_BASE + 27; //UDP收包其它错误
    public static final int RET_NOT_FIND_PES = ERROR_CODE_SYSTEM_BASE + 28; //找不到对应PES
    public static final int RET_SERVER_BUSY = ERROR_CODE_SYSTEM_BASE + 29; //服务器忙

    public static final int RET_VERIFY_CODE_ERR = ERROR_CODE_SYSTEM_BASE + 30; //验证码错误
    public static final int RET_VERIFY_CODE_TIMEOUT = ERROR_CODE_SYSTEM_BASE + 31; //验证码超时
    public static final int RET_VERIFY_CODE_TIME_SHORT = ERROR_CODE_SYSTEM_BASE + 32; //验证码发送间隔不够

    /************************************************************************/
    /*                      数据库相关错误码                                  */
    /**
     * ********************************************************************
     */

    public static final int RET_DB_DATA_EMPTY = ERROR_CODE_DB_BASE + 0; //数据为空


    /************************************************************************/
    /*                      用户相关错误码                                  */
    /**
     * ********************************************************************
     */
    public static final int RET_ACCOUNT_NOT_EXISTS = ERROR_CODE_USER_BASE + 0; //账号不存在
    public static final int RET_ACCOUNT_ALREADY_EXISTS = ERROR_CODE_USER_BASE + 1; //账号已存在
    public static final int RET_ACCOUNT_PASSWORD_INCORRECT = ERROR_CODE_USER_BASE + 2; //账号密码不匹配
    public static final int RET_USER_NOT_EXIST = ERROR_CODE_USER_BASE + 3; //用户未登录
    public static final int RET_LOGIN_AUTHKEY_ERROR = ERROR_CODE_USER_BASE + 4; //登陆鉴权Key错误

    public static final int RET_USER_LIST_ERROR_DATA = ERROR_CODE_USER_BASE + 5; //无用户数据可用
    public static final int RET_USER_NOT_LOGIN = ERROR_CODE_USER_BASE + 6; //用户未登录
    public static final int RET_USER_REGISTER_FAILED = ERROR_CODE_USER_BASE + 7; //用户注册失败
    public static final int RET_USER_CLIENT_VERSION_TOO_LOWER = ERROR_CODE_USER_BASE + 8; //客户端版本太低
    public static final int RET_USER_PROPERTY_IS_LASTEST = ERROR_CODE_USER_BASE + 9; //客户端属性为最新

    public static final int RET_FETCH_CURRENCY_TOKEN_INCORRECT = ERROR_CODE_USER_BASE + 10; //领取币值Token不正确
    public static final int RET_USER_BLACK_LIST_DEVICE_TOKEN = ERROR_CODE_USER_BASE + 11; //ios　AppStore安装的卸载用户，拒绝推送
    public static final int RET_USER_COIN_REACH_TOP_LIMIT = ERROR_CODE_USER_BASE + 12; //用户金币达到上限（拥有金币值异常）
    public static final int RET_USER_COIN_REACH_AWARD_TOP_LIMIT = ERROR_CODE_USER_BASE + 13; //用户金币值超出系统赠送上限
    public static final int RET_USER_NO_ENOUGH_COIN = ERROR_CODE_USER_BASE + 14; //用户金币不足

    public static final int RET_USER_GRADE_REACH_TOP_LIMIT = ERROR_CODE_USER_BASE + 15; //用户积分达到上限（拥有积分值异常）
    public static final int RET_USER_NO_ENOUGH_GRADE = ERROR_CODE_USER_BASE + 16; //用户积分不足
    public static final int RET_USER_HAD_FETCH_DAILY_AWARD_COIN = ERROR_CODE_USER_BASE + 17; //已经领取过每日签到奖励
    public static final int RET_USER_RANDOM_COIN_NOT_FOUND = ERROR_CODE_USER_BASE + 18; //未找到获奖信息
    public static final int RET_USER_IAP_RECEIPT_ALREADY_EXISTS = ERROR_CODE_USER_BASE + 19; //金币订单已经存在=苹果商店;
    public static final int RET_USER_NOT_MULTIPLE_VALUE = ERROR_CODE_USER_BASE + 20; //申请兑换的值不是100的整数倍
    public static final int RET_USER_FETCH_TIMES_TOP_LIMIT = ERROR_CODE_USER_BASE + 21; //领取积分次数超过限制（5次）
    public static final int RET_USER_ALIPAY_RECEIPT_ALREADY_EXISTS = ERROR_CODE_USER_BASE + 22; //金币订单已经存在

    public static final int RET_USER_YQQ_INFO_NO_EXISTS = ERROR_CODE_USER_BASE + 23; //被查询的野球拳游戏不存在
    public static final int RET_USER_YQQ_ORDER_NO_EXISTS = ERROR_CODE_USER_BASE + 24; //订单不存在
    public static final int RET_USER_YQQ_NOT_BUY = ERROR_CODE_USER_BASE + 25; //用户没有购买过野球拳
    public static final int RET_USER_GRADE_POINTS_ALREADY_ADDED = ERROR_CODE_USER_BASE + 26; //点数已经加过

    public static final int RET_LOGIN_FORBIDDEN_ERROR = ERROR_CODE_USER_BASE + 27; //用户被封禁
    public static final int RET_USER_NO_ENOUGH_WEALTH = ERROR_CODE_USER_BASE + 28; //没有足够的财富值
    public static final int RET_USER_NO_ENOUGH_CHARM = ERROR_CODE_USER_BASE + 29; //没有足够的魅力值
    public static final int RET_USER_ORDER_NUMBER_WRONG = ERROR_CODE_USER_BASE + 30; //用户订单错误
    public static final int RET_USER_FANS_EMPTY = ERROR_CODE_USER_BASE + 31; //用户粉丝为空
    public static final int RET_USER_ORDER_REPEAT = ERROR_CODE_USER_BASE + 32; //用户订单重复
    public static final int RET_USER_FETCH_GOLD_ERROR = ERROR_CODE_USER_BASE + 33; //用户领取金币不在规定范围
    public static final int RET_USER_GAME_OVER_DONE = ERROR_CODE_USER_BASE + 34; //游戏已经结束
    public static final int RET_USER_GAME_ID_INVALID = ERROR_CODE_USER_BASE + 35; //游戏不是本人响应
    public static final int RET_USER_GAME_CANCEL_DONE = ERROR_CODE_USER_BASE + 36; //游戏已经被取消
    public static final int RET_USER_GAME_CREATE_DONE = ERROR_CODE_USER_BASE + 37; //已经发起了还未完成的游戏
    public static final int RET_USER_GAME_REFUSE_DONE = ERROR_CODE_USER_BASE + 38; //游戏已经被拒绝
    public static final int RET_USER_GAME_TIMEOUT_DONE = ERROR_CODE_USER_BASE + 39; //游戏已经超时取消

    public static final int RET_USER_VIP_INFO_CAN_NOT_FOUND = ERROR_CODE_USER_BASE + 40; //未查到该用户的vip信息
    public static final int RET_USER_VIP_IS_NOT_ACTIVE = ERROR_CODE_USER_BASE + 41; //该用户的vip权限不可用
    public static final int RET_USER_VIP_IS_OVERDUE = ERROR_CODE_USER_BASE + 42; //该用户的vip权限已过期
    public static final int RET_USER_VIP_IS_NOT_VIP = ERROR_CODE_USER_BASE + 43; //该用户不是vip用户
    public static final int RET_USER_VIP_QUEST_NUM_NOT_ENOUGH = ERROR_CODE_USER_BASE + 44; //VIP用户提问次数不足
    public static final int RET_USER_VIP_PASSWORD_IS_INVALID = ERROR_CODE_USER_BASE + 45; //VIP卡非法
    public static final int RET_USER_VIP_PASSWORD_IS_FAILUER = ERROR_CODE_USER_BASE + 46; //VIP卡密码已失效
    public static final int RET_USER_DOCTOR_ASK_REPLY_NO_EXISTS = ERROR_CODE_USER_BASE + 47; //医生没有答复
    public static final int RET_USER_DOCTOR_ADD_REPLY_FAILURE = ERROR_CODE_USER_BASE + 47; //增加医生答复记录失败

    public static final int RET_ACCOUNT_NOT_ACTIVE_STATE = ERROR_CODE_USER_BASE + 48; //账号未激活
    public static final int RET_ACCOUNT_NOT_IS_DOCTOR = ERROR_CODE_USER_BASE + 49; //账号不是医生


    public static final int RET_ROOM_USER_NO_POWER = ERROR_CODE_ROOM_BASE + 0; //用户没有权限进入
    public static final int RET_ROOM_TIME_INVALID = ERROR_CODE_ROOM_BASE + 1; //进入时间无效
    public static final int RET_ROOM_NO_PVS_ALIVE = ERROR_CODE_ROOM_BASE + 2; //没有pvs
    public static final int RET_ROOM_SERVER_DATA_INCALID = ERROR_CODE_ROOM_BASE + 3; //数据错误
    public static final int RET_ROOM_USER_HAVE_JOINED = ERROR_CODE_ROOM_BASE + 4; //已经加入
    public static final int RET_ROOM_PVS_RET_ERROR = ERROR_CODE_ROOM_BASE + 5; //没有pvs
    public static final int RET_ROOM_ROOM_NOT_EXIST = ERROR_CODE_ROOM_BASE + 6; //房间不存在
    public static final int RET_ROOM_MEMBER_NOT_EXIST = ERROR_CODE_ROOM_BASE + 7; //成员不在房间中
    public static final int RET_ROOM_RESERVE_NOT_EXIST = ERROR_CODE_ROOM_BASE + 8; //预约不存在


    public static int buildErrorMsg(int errorCode) {
        int msg = R.string.ret_unknown_errors;
        switch (errorCode) {
            case ERROR_NET:
            case ERROR_TIME_OUT:
                msg = R.string.ret_time_out;
                break;
            /************************************************************************/
            /*                     系统相关错误码								    */
            /**
             * ********************************************************************
             */
            case RET_COMMUNICATION_OVERTIME:
                msg = R.string.ret_communication_overtime;
                break;
            case RET_NOT_ENOUGH_MEMORY:
                msg = R.string.ret_not_enough_memory;
                break;
            case RET_PARAMETER_ERROR:
                msg = R.string.ret_parameter_error;
                break;
            case RET_PACK_FAILED:
                msg = R.string.ret_pack_failed;
                break;
            case RET_PACK_SEND_FAILED:
                msg = R.string.ret_pack_send_failed;
                break;

            case RET_DB_ERROR:
                msg = R.string.ret_db_error;
                break;
            case RET_DB_GET_CONNECT_FAILED:
                msg = R.string.ret_db_get_connect_failed;
                break;
            case RET_SYSTEM_ERROR:
                msg = R.string.ret_system_error;
                break;
            case RET_UNPACK_FAULT:
                msg = R.string.ret_unpack_fault;
                break;
            case RET_PARAMETER_ILLEGAL:
                msg = R.string.ret_parameter_illegal;
                break;

            case RET_CREATE_SHARE_MEM_FAILED:
                msg = R.string.ret_create_share_mem_failed;
                break;
            case RET_ENOENT:
                msg = R.string.ret_enoent;
                break;
            case RET_EEXIST:
                msg = R.string.ret_eexist;
                break;
            case RET_ENFILE:
                msg = R.string.ret_enfile;
                break;
            case RET_EACCES:
                msg = R.string.ret_eacces;
                break;

            case RET_ENOSPC:
                msg = R.string.ret_enospc;
                break;
            case RET_EPERM:
                msg = R.string.ret_eperm;
                break;
            case RET_DT_SHAREMEM_ERROR:
                msg = R.string.ret_dt_sharemem_error;
                break;
            case RET_DEL_SHAREMEM_ERROR:
                msg = R.string.ret_del_sharemem_error;
                break;
            case RET_GET_SHAREMEM_AT_ERROR:
                msg = R.string.ret_get_sharemem_at_error;
                break;

            case RET_NOT_ENOUGH_SOCKET:
                msg = R.string.ret_not_enough_socket;
                break;
            case RET_UDP_RECV_NET_ERROR:
                msg = R.string.ret_udp_recv_net_error;
                break;
            case RET_UDP_RECV_SOCKET_ERROR:
                msg = R.string.ret_udp_recv_socket_error;
                break;
            case RET_UDP_RECV_UNPACK_ERROR:
                msg = R.string.ret_udp_recv_unpack_error;
                break;
            case RET_UDP_RECV_BUFFER_TOO_SMALL:
                msg = R.string.ret_udp_recv_buffer_too_small;
                break;

            case RET_UDP_RECV_DATA_EMPTY:
                msg = R.string.ret_udp_recv_data_empty;
                break;
            case RET_UDP_RECV_PACK_TOO_SHORT:
                msg = R.string.ret_udp_recv_pack_too_short;
                break;
            case RET_UDP_RECV_PACK_OTHER_ERR:
                msg = R.string.ret_udp_recv_pack_other_err;
                break;
            case RET_NOT_FIND_PES:
                msg = R.string.ret_not_find_pes;
                break;
            case RET_SERVER_BUSY:
                msg = R.string.ret_server_busy;
                break;

            case RET_VERIFY_CODE_ERR:
                msg = R.string.ret_verify_code_err;
                break;
            case RET_VERIFY_CODE_TIMEOUT:
                msg = R.string.ret_verify_code_timeout;
                break;
            case RET_VERIFY_CODE_TIME_SHORT:
                msg = R.string.ret_verify_code_time_short;
                break;

            /************************************************************************/
            /*                      数据库相关错误码                                  */
            /**
             * ********************************************************************
             */


            case RET_DB_DATA_EMPTY:
                msg = R.string.ret_db_data_empty;
                break;

            /************************************************************************/
            /*                      用户相关错误码                                  */
            /**
             * ********************************************************************
             */

            case RET_ACCOUNT_NOT_EXISTS:
                msg = R.string.ret_account_not_exists;
                break;
            case RET_ACCOUNT_ALREADY_EXISTS:
                msg = R.string.ret_account_already_exists;
                break;
            case RET_ACCOUNT_PASSWORD_INCORRECT:
                msg = R.string.ret_account_password_incorrect;
                break;
            case RET_USER_NOT_EXIST:
                msg = R.string.ret_user_not_exist;
                break;
            case RET_LOGIN_AUTHKEY_ERROR:
                msg = R.string.ret_login_authkey_error;
                break;

            case RET_USER_LIST_ERROR_DATA:
                msg = R.string.ret_user_list_error_data;
                break;
            case RET_USER_NOT_LOGIN:
                msg = R.string.ret_user_list_error_data;
                break;
            case RET_USER_REGISTER_FAILED:
                msg = R.string.ret_user_register_failed;
                break;
            case RET_USER_CLIENT_VERSION_TOO_LOWER:
                msg = R.string.ret_user_client_version_too_lower;
                break;
            case RET_USER_PROPERTY_IS_LASTEST:
                msg = R.string.ret_user_property_is_lastest;
                break;

            case RET_FETCH_CURRENCY_TOKEN_INCORRECT:
                msg = R.string.ret_fetch_currency_token_incorrect;
                break;
            case RET_USER_BLACK_LIST_DEVICE_TOKEN:
                msg = R.string.ret_user_black_list_device_token;
                break;
            case RET_USER_COIN_REACH_TOP_LIMIT:
                msg = R.string.ret_user_coin_reach_top_limit;
                break;
            case RET_USER_COIN_REACH_AWARD_TOP_LIMIT:
                msg = R.string.ret_user_coin_reach_award_top_limit;
                break;
            case RET_USER_NO_ENOUGH_COIN:
                msg = R.string.ret_user_no_enough_coin;
                break;

            case RET_USER_GRADE_REACH_TOP_LIMIT:
                msg = R.string.ret_user_grade_reach_top_limit;
                break;
            case RET_USER_NO_ENOUGH_GRADE:
                msg = R.string.ret_user_no_enough_grade;
                break;
            case RET_USER_HAD_FETCH_DAILY_AWARD_COIN:
                msg = R.string.ret_user_had_fetch_daily_award_coin;
                break;
            case RET_USER_RANDOM_COIN_NOT_FOUND:
                msg = R.string.ret_user_random_coin_not_found;
                break;
            case RET_USER_IAP_RECEIPT_ALREADY_EXISTS:
                msg = R.string.ret_user_iap_receipt_already_exists;
                break;

            case RET_USER_NOT_MULTIPLE_VALUE:
                msg = R.string.ret_user_not_multiple_value;
                break;
            case RET_USER_FETCH_TIMES_TOP_LIMIT:
                msg = R.string.ret_user_fetch_times_top_limit;
                break;
            case RET_USER_ALIPAY_RECEIPT_ALREADY_EXISTS:
                msg = R.string.ret_user_alipay_receipt_already_exists;
                break;
            case RET_USER_YQQ_INFO_NO_EXISTS:
                msg = R.string.ret_user_yqq_info_no_exists;
                break;
            case RET_USER_YQQ_ORDER_NO_EXISTS:
                msg = R.string.ret_user_yqq_order_no_exists;
                break;

            case RET_USER_YQQ_NOT_BUY:
                msg = R.string.ret_user_yqq_not_buy;
                break;
            case RET_USER_GRADE_POINTS_ALREADY_ADDED:
                msg = R.string.ret_user_grade_points_already_added;
                break;
            case RET_LOGIN_FORBIDDEN_ERROR:
                msg = R.string.ret_login_forbidden_error;
                break;
            case RET_USER_NO_ENOUGH_WEALTH:
                msg = R.string.ret_user_no_enough_wealth;
                break;
            case RET_USER_NO_ENOUGH_CHARM:
                msg = R.string.ret_user_no_enough_charm;
                break;

            case RET_USER_ORDER_NUMBER_WRONG:
                msg = R.string.ret_user_order_number_wrong;
                break;
            case RET_USER_FANS_EMPTY:
                msg = R.string.ret_user_fans_empty;
                break;
            case RET_USER_ORDER_REPEAT:
                msg = R.string.ret_user_order_repeat;
                break;
            case RET_USER_FETCH_GOLD_ERROR:
                msg = R.string.ret_user_fetch_gold_error;
                break;
            case RET_USER_GAME_OVER_DONE:
                msg = R.string.ret_user_game_over_done;
                break;

            case RET_USER_GAME_ID_INVALID:
                msg = R.string.ret_user_game_id_invalid;
                break;
            case RET_USER_GAME_CANCEL_DONE:
                msg = R.string.ret_user_game_cancel_done;
                break;
            case RET_USER_GAME_CREATE_DONE:
                msg = R.string.ret_user_game_create_done;
                break;
            case RET_USER_GAME_REFUSE_DONE:
                msg = R.string.ret_user_game_refuse_done;
                break;
            case RET_USER_GAME_TIMEOUT_DONE:
                msg = R.string.ret_user_game_timeout_done;
                break;

            case RET_USER_VIP_INFO_CAN_NOT_FOUND:
                msg = R.string.ret_user_vip_info_can_not_found;
                break;
            case RET_USER_VIP_IS_NOT_ACTIVE:
                msg = R.string.ret_user_vip_is_not_active;
                break;
            case RET_USER_VIP_IS_OVERDUE:
                msg = R.string.ret_user_vip_is_overdue;
                break;
            case RET_USER_VIP_IS_NOT_VIP:
                msg = R.string.ret_user_vip_is_not_vip;
                break;
            case RET_USER_VIP_QUEST_NUM_NOT_ENOUGH:
                msg = R.string.ret_user_vip_quest_num_not_enough;
                break;

            case RET_USER_VIP_PASSWORD_IS_INVALID:
                msg = R.string.ret_user_vip_password_is_invalid;
                break;
            case RET_USER_VIP_PASSWORD_IS_FAILUER:
                msg = R.string.ret_user_vip_password_is_failuer;
                break;
            case RET_USER_DOCTOR_ASK_REPLY_NO_EXISTS:
                msg = R.string.ret_user_doctor_ask_reply_no_exists;
                break;
//            case RET_USER_DOCTOR_ADD_REPLY_FAILURE:
//                msg = "增加医生答复记录失败";
//                break;
            case RET_ACCOUNT_NOT_ACTIVE_STATE:
                msg = R.string.ret_account_not_active_state;
                break;

            case RET_ACCOUNT_NOT_IS_DOCTOR:
                msg = R.string.ret_account_not_is_doctor;
                break;
            case RET_ROOM_USER_NO_POWER:
                msg = R.string.ret_room_user_no_power;
                break;
            case RET_ROOM_TIME_INVALID:
                msg = R.string.ret_room_time_invalid;
                break;
            case RET_ROOM_NO_PVS_ALIVE:
                msg = R.string.ret_room_no_pvs_alive;
                break;
            case RET_ROOM_SERVER_DATA_INCALID:
                msg = R.string.ret_room_server_data_incalid;
                break;

            case RET_ROOM_USER_HAVE_JOINED:
                msg = R.string.ret_room_user_have_joined;
                break;
            case RET_ROOM_PVS_RET_ERROR:
                msg = R.string.ret_room_pvs_ret_error;
                break;
            case RET_ROOM_ROOM_NOT_EXIST:
                msg = R.string.ret_room_room_not_exist;
                break;
            case RET_ROOM_MEMBER_NOT_EXIST:
                msg = R.string.ret_room_member_not_exist;
                break;
            case RET_ROOM_RESERVE_NOT_EXIST:
                msg = R.string.ret_room_reserve_not_exist;
                break;
        }
        return msg;
    }

}
