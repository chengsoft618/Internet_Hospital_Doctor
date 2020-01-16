package cn.longmaster.hospital.doctor.core.manager.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.timeout.TimeoutHelper;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.entity.doctor.FinanceStatisticInfo;
import cn.longmaster.hospital.doctor.core.entity.user.BankCardInfo;
import cn.longmaster.hospital.doctor.core.entity.user.UserBillInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.AddBankAccountRequester;
import cn.longmaster.hospital.doctor.core.requests.user.BalanceWithdrawalRequester;
import cn.longmaster.hospital.doctor.core.requests.user.BankCardRequester;
import cn.longmaster.hospital.doctor.core.requests.user.DefaultAccountRequester;
import cn.longmaster.hospital.doctor.core.requests.user.DeleteAccountRequester;
import cn.longmaster.hospital.doctor.core.requests.user.FinanceStatisticRequester;
import cn.longmaster.hospital.doctor.core.requests.user.UserBillRequester;

/**
 * 账户管理类
 * Created by yangyong on 15/8/19.
 */
public class AccountManager extends BaseManager implements TimeoutHelper.OnTimeoutCallback<Integer> {
    private static final String TAG = AccountManager.class.getSimpleName();
    private ArrayList<OnBalanceStateChangeListener> mRequests;
    private TimeoutHelper<Integer> mTimeoutHelper;

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
        mRequests = new ArrayList<>();
        mTimeoutHelper = new TimeoutHelper<>();
        mTimeoutHelper.setCallback(this);
    }

    /**
     * 用户财务统计（包括总金额、收入、平台扣款、总计）
     *
     * @param listener
     */
    public void getFinanceStatistic(int accountId, final OnResultListener<FinanceStatisticInfo> listener) {
        FinanceStatisticRequester financeStatisticRequester = new FinanceStatisticRequester(new OnResultListener<FinanceStatisticInfo>() {
            @Override
            public void onResult(BaseResult baseResult, FinanceStatisticInfo financeStatisticInfo) {
                listener.onResult(baseResult, financeStatisticInfo);
            }
        });
        financeStatisticRequester.reqType = 1;
        financeStatisticRequester.account = accountId;
        financeStatisticRequester.doPost();
    }

    /**
     * 账单明细
     *
     * @param listener
     */
    public void getUserBills(final OnResultListener<List<UserBillInfo>> listener) {
        UserBillRequester userBillRequester = new UserBillRequester(new OnResultListener<List<UserBillInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<UserBillInfo> userBillInfos) {
                listener.onResult(baseResult, userBillInfos);
            }
        });
        userBillRequester.reqType = 1;
        userBillRequester.doPost();
    }

    /**
     * 拉取银行卡信息
     *
     * @param listener
     */
    public void getBankCards(int account, final OnResultListener<List<BankCardInfo>> listener) {
        BankCardRequester bankCardRequester = new BankCardRequester(new OnResultListener<List<BankCardInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<BankCardInfo> bankCardInfos) {
                listener.onResult(baseResult, bankCardInfos);
            }
        });
        bankCardRequester.account = account;
        bankCardRequester.doPost();
    }

    /**
     * 设置账户为使用状态
     *
     * @param listener
     */
    public void setDefaultAccount(String cardNum, int account, final OnResultListener<Void> listener) {
        DefaultAccountRequester defaultAccountRequester = new DefaultAccountRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                listener.onResult(baseResult, aVoid);
            }
        });
        defaultAccountRequester.cardNum = cardNum;
        defaultAccountRequester.account = account;
        defaultAccountRequester.doPost();
    }

    /**
     * 添加账户
     *
     * @param listener
     */
    public void addBankAccount(String cardNum, String realName, String bankName, String idNumber, int accountId, final OnResultListener<Void> listener) {
        AddBankAccountRequester addBankAccountRequester = new AddBankAccountRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                listener.onResult(baseResult, aVoid);
            }
        });
        addBankAccountRequester.cardNum = cardNum;
        addBankAccountRequester.realName = realName;
        addBankAccountRequester.bankName = bankName;
        addBankAccountRequester.idNumber = idNumber;
        addBankAccountRequester.account = accountId;
        addBankAccountRequester.doPost();

    }

    /**
     * 删除账户
     *
     * @param listener
     */
    public void deleteBankAccount(String cardNum, int account, final OnResultListener<Void> listener) {
        DeleteAccountRequester deleteAccountRequester = new DeleteAccountRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                listener.onResult(baseResult, aVoid);
            }
        });
        deleteAccountRequester.cardNum = cardNum;
        deleteAccountRequester.account = account;
        deleteAccountRequester.doPost();
    }

    /**
     * 余额提现
     *
     * @param opValue        提现金额
     * @param bankNo         提现银行卡号
     * @param payType        银行账户类型 1：银行卡; 2：支付宝; 3：微信
     * @param settlementType 申请人身份类型 1：后台管理员; 2：医生; 3：患者
     * @param listener       回调
     */
    public void balanceWithdrawal(float opValue, String bankNo, int payType, int settlementType, final OnResultListener<Void> listener) {
        BalanceWithdrawalRequester balanceWithdrawalRequester = new BalanceWithdrawalRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                listener.onResult(baseResult, aVoid);
            }
        });
        balanceWithdrawalRequester.opValue = opValue;
        balanceWithdrawalRequester.bankNo = bankNo;
        balanceWithdrawalRequester.payType = payType;
        balanceWithdrawalRequester.settlementType = settlementType;
        balanceWithdrawalRequester.doPost();
    }

    /**
     * 查询余额
     *
     * @param listener 回调接口
     */
    public void inquireBalance(OnBalanceStateChangeListener listener) {
        if (mRequests.contains(listener)) {
            return;
        }
        mRequests.add(listener);
        getManager(DcpManager.class).inquireBalance();
    }

    /**
     * 查询余额回调
     *
     * @param result 返回码
     * @param json   服务器返回json数据
     */
    public void onInquireBalance(final int result, String json) {
        double totalValue = 0;
        double availaValue = 0;
        if (result == DcpErrorcodeDef.RET_SUCCESS) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                totalValue = jsonObject.optDouble("_totalValue", 0);
                availaValue = jsonObject.optDouble("_availaValue", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final double finalTotalValue = totalValue;
        final double finalAvailaValue = availaValue;
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mRequests != null && mRequests.size() > 0) {
                    for (OnBalanceStateChangeListener callback : mRequests) {
                        callback.onBalanceStateChanged(result, finalTotalValue, finalAvailaValue);
                    }
                    mRequests.clear();
                }
            }
        });
    }

    /**
     * 余额变化通知
     *
     * @param result 返回码
     * @param json   服务器返回json数据
     */
    public void onBalanceChangeNotification(int result, String json) {
        if (result == DcpErrorcodeDef.RET_SUCCESS) {
            try {
                JSONObject jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTimeout(TimeoutHelper<Integer> timeoutHelper, Integer taskId) {
        if (mRequests != null && mRequests.size() > 0) {
            for (OnBalanceStateChangeListener callback : mRequests) {
                callback.onBalanceStateChanged(-1, 0.00d, 0.00d);
            }
            mRequests.clear();
        }
    }
}
