package cn.longmaster.hospital.doctor.data.repository;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.account.AccountFlowInfo;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.entity.account.CashListInfo;
import cn.longmaster.hospital.doctor.core.entity.common.AgentServiceInfo;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.entity.user.CheckAccountInfo;
import cn.longmaster.hospital.doctor.data.AccountDataSource;
import cn.longmaster.hospital.doctor.data.local.AccountLocalDataSource;
import cn.longmaster.hospital.doctor.data.remote.AccountRemoteDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/8/27 14:36
 * @description:
 */
public class AccountRepository implements AccountDataSource {
    private static AccountRepository INSTANCE;
    private AccountDataSource localDataSource;
    private AccountDataSource remoteDataSource;

    private AccountRepository(AccountDataSource localDataSource, AccountDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static AccountRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new AccountRepository(new AccountLocalDataSource(), new AccountRemoteDataSource());
        }
        return INSTANCE;
    }

    @Override
    public void doAccountActive(String account, String pwd, byte accountType, OnResultCallback<UserResultInfo> onResultCallback) {

    }

    @Override
    public void doAgentService(String sign, OnResultCallback<AgentServiceInfo> onResultCallback) {

    }

    @Override
    public void doCheckAccountExist(String userName, int accountType, OnResultCallback<CheckAccountInfo> onResultCallback) {

    }

    @Override
    public void doCheckCode(int userId, String account, String verifyCode, int requestType, String password, OnResultCallback<UserResultInfo> onResultCallback) {

    }

    @Override
    public void queryAccount(String userName, int accountType, OnResultCallback<UserResultInfo> onResultCallback) {

    }

    @Override
    public void getRegCode(String account, int accountType, OnResultCallback<Integer> onResultCallback) {

    }

    @Override
    public void regAccount(String account, int accountType, OnResultCallback<Integer> onResultCallback) {

    }

    @Override
    public void getFlowList(int account, int pageIndex, int pageSize, OnResultCallback<List<AccountFlowInfo>> onResultCallback) {

    }

    @Override
    public void getAccountList(int account, int state, int pageIndex, int pageSize, OnResultCallback<List<AccountListInfo>> onResultCallback) {

    }

    @Override
    public void getCashList(int account, OnResultCallback<CashListInfo> onResultCallback) {

    }

    @Override
    public void doCash(float opValue, String bankNo, int payType, int settlementType, int account, String incomeIds, OnResultCallback<Void> onResultCallback) {

    }

    @Override
    public void getMergerList(String serialId, OnResultCallback<List<AccountListInfo>> onResultCallback) {

    }
}
