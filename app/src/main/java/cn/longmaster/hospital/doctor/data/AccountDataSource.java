package cn.longmaster.hospital.doctor.data;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.account.AccountFlowInfo;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.entity.account.CashListInfo;
import cn.longmaster.hospital.doctor.core.entity.common.AgentServiceInfo;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.entity.user.CheckAccountInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/8/27 14:18
 * @description:
 */
public interface AccountDataSource extends DataSource {
    /**
     * 账号激活请求
     *
     * @param account          用户名
     * @param pwd              密码
     * @param accountType      用户类型
     * @param onResultCallback
     */
    void doAccountActive(String account, String pwd, byte accountType, OnResultCallback<UserResultInfo> onResultCallback);

    /**
     * 请求代理服务器
     *
     * @param sign
     * @param onResultCallback
     */
    void doAgentService(String sign, OnResultCallback<AgentServiceInfo> onResultCallback);

    /**
     * 查询账号是否存在
     *
     * @param userName    用户账号
     * @param accountType 账号类型
     */
    void doCheckAccountExist(String userName, int accountType, OnResultCallback<CheckAccountInfo> onResultCallback);

    /**
     * 校验验证码请求
     *
     * @param userId
     * @param account
     * @param verifyCode
     * @param requestType
     * @param password
     */
    void doCheckCode(int userId, String account, String verifyCode, int requestType, String password, OnResultCallback<UserResultInfo> onResultCallback);

    /**
     * 查询账号是否存在
     *
     * @param userName
     * @param accountType
     * @param onResultCallback
     */
    void queryAccount(String userName, int accountType, OnResultCallback<UserResultInfo> onResultCallback);

    /**
     * 获取注册验证码
     *
     * @param account          账号
     * @param accountType      账号类型
     * @param onResultCallback
     */
    void getRegCode(String account, int accountType, OnResultCallback<Integer> onResultCallback);

    /**
     * 账号注册请求
     *
     * @param account
     * @param accountType
     * @param onResultCallback
     */
    void regAccount(String account, int accountType, OnResultCallback<Integer> onResultCallback);

    /**
     * 获取账户流水明细
     *
     * @param account          账户id
     * @param pageIndex
     * @param pageSize
     * @param onResultCallback
     */
    void getFlowList(int account, int pageIndex, int pageSize, OnResultCallback<List<AccountFlowInfo>> onResultCallback);

    /**
     * 获取就诊清单
     *
     * @param account          账户id
     * @param state            //101未结算 102已打款 103提现中 104待提现 201欠款未处理 202欠款处理中 203欠款已处理
     * @param pageIndex
     * @param pageSize
     * @param onResultCallback
     */
    void getAccountList(int account, int state, int pageIndex, int pageSize, OnResultCallback<List<AccountListInfo>> onResultCallback);

    /**
     * 根据账户id拉取账户可提现列表
     *
     * @param account          账户id
     * @param onResultCallback
     */
    void getCashList(int account, OnResultCallback<CashListInfo> onResultCallback);

    /**
     * 余额提现接口
     *
     * @param opValue          提现金额
     * @param bankNo           提现银行卡号
     * @param payType          银行账户类型 1：银行卡; 2：支付宝; 3：微信
     * @param settlementType   请人身份类型 1：后台管理员; 2：医生; 3：患者
     * @param account          账户
     * @param incomeIds
     * @param onResultCallback
     */
    void doCash(float opValue, String bankNo, int payType, int settlementType, int account, String incomeIds, OnResultCallback<Void> onResultCallback);

    /**
     * 根据合并提现流水号拉取合并提现详情列表
     *
     * @param serialId         提现流水号
     * @param onResultCallback
     */
    void getMergerList(String serialId, OnResultCallback<List<AccountListInfo>> onResultCallback);
}
