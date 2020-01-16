package cn.longmaster.hospital.doctor.core.manager.user;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.util.timeout.TimeoutHelper;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.UserInfoContract;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.RoomListInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.HTTPDNSManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.login.AccountActiveRequester;
import cn.longmaster.hospital.doctor.core.requests.login.AgentServiceRequester;
import cn.longmaster.hospital.doctor.core.requests.login.CheckCodeRequester;
import cn.longmaster.hospital.doctor.core.requests.login.RegCodeRequester;
import cn.longmaster.hospital.doctor.core.requests.user.SubmitDomainNameRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.view.dialog.KickOffDialog;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.Utils;

/**
 * 用户信息管理类
 * Created by yangyong on 16/7/26.
 */
public class UserInfoManager extends BaseManager {
    private final String TAG = UserInfoManager.class.getSimpleName();
    private final String PHONE_NUMBER_PREFIX = "86";//手机号码前缀
    private static final int MAX_WAITING_TIME = 15 * 1000;//最长等待时间

    private UserInfo mCurrentUserInfo, mTempUser;
    private CopyOnWriteArrayList<LoginStateChangeListener> mLoginStateChangeListeners = new CopyOnWriteArrayList<>();
    private TimeoutHelper<Integer> mTimeoutHelper;
    private AppApplication mApplication;
    private CopyOnWriteArrayList<VersionChangeListener> mVersionChangeListeners = new CopyOnWriteArrayList<>();
    private int queryPesTime = 0;//请求pes次数
    private int mIdentity = 0;
    private CopyOnWriteArrayList<RoomListInfo> mRoomListInfos;
    private OnCheckAdminInfoListener mOnCheckAdminInfoListener;
    private OnGetRoomListInfoListener mOnGetRoomListInfoListener;
    private OnCheckPwdInfoListener mOnCheckPwdInfoListener;
    private DcpManager mDcpManager;

    @Override
    public void onManagerCreate(AppApplication application) {
        mTimeoutHelper = new TimeoutHelper<Integer>();
        mTimeoutHelper.setCallback(callback);
        mApplication = application;
        mDcpManager = getManager(DcpManager.class);
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    public void regVersionChangeListener(VersionChangeListener listenter) {
        mVersionChangeListeners.add(listenter);
    }

    public void unRegVersionChangeListener(VersionChangeListener listenter) {
        mVersionChangeListeners.remove(listenter);
    }

    public UserInfo getCurrentUserInfo() {
        if (mCurrentUserInfo == null) {
            mCurrentUserInfo = new UserInfo();
        }
        Logger.logD(Logger.USER, TAG + "->getCurrentUserInfo()->CurrentUserInfo:" + mCurrentUserInfo);
        return mCurrentUserInfo;
    }

    public void reLogin(LoginStateChangeListener listener) {
        if (mLoginStateChangeListeners.contains(listener)) {
            mLoginStateChangeListeners.add(listener);
            return;
        }
        mLoginStateChangeListeners.add(listener);
        getManager(DcpManager.class).setPesInfo(mCurrentUserInfo.getPesIp(), mCurrentUserInfo.getPesPort(), false);
        mTimeoutHelper.request(mCurrentUserInfo.getUserId(), MAX_WAITING_TIME);
    }

    /**
     * 获取会议室列表
     *
     * @param listInfoListener
     */
    public void setOnGetRoomListInfoListener(OnGetRoomListInfoListener listInfoListener) {
        mOnGetRoomListInfoListener = listInfoListener;
        mDcpManager.getRoomListInfo(1);
    }

    /**
     * 验证会议室密码
     *
     * @param listInfoListener
     */
    public void setOnCheckPwdInfoListener(int actionType, int roomId, String password, OnCheckPwdInfoListener listInfoListener) {
        mOnCheckPwdInfoListener = listInfoListener;
        mDcpManager.checkPwdInfo(actionType, roomId, password);
    }

    /**
     * 判断是否是管理员
     * 第一位表示管理员
     */
    public boolean isAdmin() {
        return (mIdentity & 1) == 1;
    }

    /**
     * 判断是否是顾问
     * 第六位表示顾问查询权限
     */
    public boolean isAdviser() {
        return (mIdentity & 32) != 0;
    }

    /**
     * 账号激活
     *
     * @param account     账号
     * @param accountType 帐户类型
     * @param pwd         密码
     */
    public void activeAccount(String account, final byte accountType, String pwd, final LoginStateChangeListener listener) {
        if (mLoginStateChangeListeners.contains(listener)) {
            mLoginStateChangeListeners.add(listener);
            return;
        }
        account = PHONE_NUMBER_PREFIX + account;
        pwd = MD5Util.md5(pwd);
        final String finalAccount = account;
        AccountActiveRequester requester = new AccountActiveRequester(new DefaultResultCallback<UserResultInfo>() {
            @Override
            public void onSuccess(UserResultInfo userResultInfo, BaseResult baseResult) {
                Logger.logD(Logger.USER, TAG + "->activeAccount()->baseResult:" + baseResult + ", userResultInfo:" + userResultInfo);
                mLoginStateChangeListeners.add(listener);
                mTempUser = new UserInfo();
                mTempUser.setUserId(userResultInfo.getUserID());
                mTempUser.setAccountType(accountType);
                mTempUser.setPhoneNum(finalAccount);
                mTempUser.setLoginAuthKey(userResultInfo.getLoginAuthKey());
                mTempUser.setPesAddr(userResultInfo.getPesAddr());
                mTempUser.setPesIp(userResultInfo.getPesIP());
                mTempUser.setPesPort(userResultInfo.getPesPort());
                mTempUser.setIsDoctor(userResultInfo.getIsDoctor());
                saveUserInfo(mTempUser);
                mTimeoutHelper.request(userResultInfo.getUserID(), MAX_WAITING_TIME);
                getManager(HTTPDNSManager.class).insertIpData(AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN, userResultInfo.getUrl(), userResultInfo.getPesIP(), new HTTPDNSManager.OnInsertIpListener() {
                    @Override
                    public void onInsertIpListener(String url) {
                        getManager(DcpManager.class).setPesInfo(userResultInfo.getPesIP(), userResultInfo.getPesPort(), true);
                    }
                });
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onLoginStateChanged(result.getCode(), DcpErrorcodeDef.buildErrorMsg(result.getReson()));
            }
        });
        requester.account = account;
        requester.pwd = pwd;
        requester.accountType = accountType;
        requester.doPost();
    }

    /**
     * 获取验证码
     *
     * @param account        账号
     * @param requestType    请求类型
     * @param resultListener 回调
     */
    public void regCode(String account, byte requestType, final OnResultListener resultListener) {
        account = PHONE_NUMBER_PREFIX + account;
        RegCodeRequester requester = new RegCodeRequester(new OnResultListener<Integer>() {
            @Override
            public void onResult(BaseResult baseResult, Integer userId) {
                Logger.logD(Logger.USER, TAG + "->regCode()->baseResult:" + baseResult + ", userId:" + userId);
                resultListener.onResult(baseResult, userId);
            }
        });
        requester.account = account;
        requester.requestType = requestType;
        requester.doPost();
    }

    /**
     * 验证码验证
     *
     * @param userId      用户id
     * @param account     账号
     * @param verifyCode  验证码
     * @param requestType 请求类型, 1 是注册验证，2 是短信登陆
     * @param pwd         密码,短信登录时传空字符串
     * @param listener    回调接口
     */
    public void checkVerifyCode(int userId, String account, String verifyCode, byte requestType, String pwd, final LoginStateChangeListener listener) {
        if (mLoginStateChangeListeners.contains(listener)) {
            mLoginStateChangeListeners.add(listener);
            return;
        }
        account = PHONE_NUMBER_PREFIX + account;
        final String finalAccount = account;
        CheckCodeRequester requester = new CheckCodeRequester(new OnResultListener<UserResultInfo>() {
            @Override
            public void onResult(BaseResult baseResult, UserResultInfo userResultInfo) {
                Logger.logD(Logger.USER, TAG + "->checkVerifyCode()->baseResult:" + baseResult + ", userResultInfo:" + userResultInfo);
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mTempUser = new UserInfo();
                    mTempUser.setUserId(userResultInfo.getUserID());
                    mTempUser.setPhoneNum(finalAccount);
                    mTempUser.setLoginAuthKey(userResultInfo.getLoginAuthKey());
                    mTempUser.setPesAddr(userResultInfo.getPesAddr());
                    mTempUser.setPesIp(userResultInfo.getPesIP());
                    mTempUser.setPesPort(userResultInfo.getPesPort());
                    mTempUser.setIsDoctor(userResultInfo.getIsDoctor());
                    Logger.logD(Logger.USER, TAG + "->checkVerifyCode()->getCurrentUserInfo:" + getCurrentUserInfo().getUserId() + ", userResultInfo:" + userResultInfo.getUserID());
                    if (getCurrentUserInfo().getUserId() == userResultInfo.getUserID()) {
                        mTempUser.setIsUsing(UserInfo.INUSE);
                        mCurrentUserInfo = mTempUser;
                        saveUserInfo(mTempUser);
                        listener.onLoginStateChanged(baseResult.getCode(), R.string.change_password_success);
                        return;
                    }
                    mLoginStateChangeListeners.add(listener);
                    mTimeoutHelper.request(userResultInfo.getUserID(), MAX_WAITING_TIME);
                    getManager(DcpManager.class).setPesInfo(userResultInfo.getPesIP(), userResultInfo.getPesPort(), true);
                } else {
                    listener.onLoginStateChanged(baseResult.getCode(), DcpErrorcodeDef.buildErrorMsg(baseResult.getReson()));
                }
            }
        });
        requester.userId = userId;
        requester.account = account;
        requester.verifyCode = verifyCode;
        requester.requestType = requestType;
        requester.password = MD5Util.md5(pwd);
        requester.doPost();
    }

    /**
     * 接收pes服务器回调数据
     *
     * @param result     请求结果码
     * @param funcAction 操作类型
     * @param json       毁掉数据
     */
    public void onRecvData(final int result, int funcAction, final String json) {
        Logger.logD(Logger.USER, TAG + "->onRecvData()->result:" + result + ", funcAction:" + funcAction + ", json:" + json);
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo.getUserId() == 0 && mTempUser != null) {
            userInfo = mTempUser;
        }
        mTimeoutHelper.cancel(userInfo.getUserId());
        if (result == DcpErrorcodeDef.RET_SUCCESS) {
            AppApplication.getInstance().setOnlineState(AppConstant.ONLINE_STATE_ONLINE);
            if (funcAction == DcpFuncConfig.ACTION_TYPE_SETPESINFO) {
                getManager(DcpManager.class).login(userInfo.getUserId(), userInfo.getLoginAuthKey());
                AppApplication.getInstance().setIsLogin(true);//设置是否调用了login
            } else if (funcAction == DcpFuncConfig.ACTION_TYPE_LOGIN) {
                if (!SPUtils.getInstance().getBoolean(AppPreference.KEY_FIRST_ADD_SCHEDULE, false)) {
                    getManager(DcpManager.class).checkAdminInfo();
                }
                userInfo.setIsUsing(UserInfo.INUSE);
                userInfo.setIsActivity(UserInfo.HAS_ACTIVATED);
                userInfo.setLastLoginDt(System.currentTimeMillis());
                saveUserInfo(userInfo);
                mCurrentUserInfo = userInfo;
                checkVersion(json);
                AppApplication.getInstance().setIsOnLogin(true);
                submitDomainNameRequester();
                // getManager(DcpManager.class).checkAdminInfo();
                // 拉取离线消息
                Logger.logD(Logger.USER, TAG + "->onRecvData()->getMessage()");
                getManager(DcpManager.class).getMessage(userInfo.getUserId());
                // 获取消息中心离线消息
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        getManager(MessageManager.class).getMessageList(mCurrentUserInfo.getUserId());
                        //获取鉴权信息
                        getManager(AuthenticationManager.class).getAuthenticationInfo();
                    }
                });
            }
        } else if (result == DcpErrorcodeDef.RET_LOGIN_AUTHKEY_ERROR) {//被踢下线
            handleKickoff();
        } else if (result == DcpErrorcodeDef.RET_USER_CLIENT_VERSION_TOO_LOWER) {//强制升级
            checkVersion(json);
        }

        if ((funcAction == DcpFuncConfig.ACTION_TYPE_SETPESINFO && result != DcpErrorcodeDef.RET_SUCCESS) || funcAction == DcpFuncConfig.ACTION_TYPE_LOGIN) {
            responseResult(result, DcpErrorcodeDef.buildErrorMsg(result));
        }
    }

    private void submitDomainNameRequester() {
        if (getManager(DcpManager.class).getIp() != -1) {
            getManager(HTTPDNSManager.class).getHttpDnsUrl(AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN, url -> {
                final long httpDnsIp = getManager(HTTPDNSManager.class).getLongDnsIp(url);
                Logger.logD(Logger.COMMON, TAG + "->submitDomainNameRequester()->url:" + url + ", getHttpDnsUrl:" + httpDnsIp);
                SubmitDomainNameRequester requester = new SubmitDomainNameRequester(new OnResultListener<Void>() {
                    @Override
                    public void onResult(BaseResult baseResult, Void aVoid) {
                        Logger.logD(Logger.COMMON, TAG + "->setPesInfo()->SubmitDomainNameRequester:" + baseResult);
                    }
                });
                requester.ipAddress = httpDnsIp + "";
                requester.type = AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN;
                requester.doPost();
            });
        }
    }

    /**
     * 检查版本号码
     *
     * @param json 服务器回调json
     */
    private void checkVersion(final String json) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int limiteVersion = jsonObject.optInt("_clientVersionLimit", AppConfig.CLIENT_VERSION);
                    int latestVersion = jsonObject.optInt("_clientVersionLatest", AppConfig.CLIENT_VERSION);
                    int currentVersion = VersionManager.getInstance().getCurentClientVersion();
                    if (currentVersion < limiteVersion && SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LIMIT_VERSION, 0) < limiteVersion) {
                        VersionManager.setClientVersionLimit(limiteVersion);
                        for (VersionChangeListener listener : mVersionChangeListeners) {
                            listener.onVersionLimited();
                        }
                    } else if (currentVersion < latestVersion && SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LASTEST_VERSION, 0) < latestVersion) {
                        VersionManager.setClientVersionLatest(latestVersion);
                        for (VersionChangeListener listener : mVersionChangeListeners) {
                            listener.onNewVersion();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //请求超时处理
    private TimeoutHelper.OnTimeoutCallback callback = new TimeoutHelper.OnTimeoutCallback() {
        @Override
        public void onTimeout(TimeoutHelper timeoutHelper, Object taskId) {
            if (mTempUser == null || mTempUser.getUserId() == (int) taskId) {
                responseResult(-1, R.string.no_network_connection);
            }
        }
    };

    /**
     * 回调结果
     *
     * @param result 返回码
     * @param msg    提示信息
     */
    private void responseResult(final int result, final int msg) {
        Logger.logD(Logger.USER, TAG + "->responseResult()->result:" + result + ", msg:" + msg);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (LoginStateChangeListener listener : mLoginStateChangeListeners) {
                    listener.onLoginStateChanged(result, msg);
                }
                mLoginStateChangeListeners.clear();
            }
        });
    }

    public void getAdminInfo(OnCheckAdminInfoListener listener) {
        mOnCheckAdminInfoListener = listener;
        getManager(DcpManager.class).checkAdminInfo();
    }

    /**
     * 检测管理员回调
     *
     * @param result 返回码
     * @param json   回调数据
     */
    public void onCheckAdminInfo(int result, String json) {
        if (result == DcpErrorcodeDef.RET_SUCCESS) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int userId = jsonObject.optInt("_userID", 0);
                if (userId == getCurrentUserInfo().getUserId()) {
                    mIdentity = jsonObject.optInt("_isAdmin", 0);
                    AppApplication.getInstance().setCurrentIdentity(mIdentity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Logger.logD(Logger.USER, TAG + "->onCheckAdminInfo()->result:" + result);
        }
        if (mOnCheckAdminInfoListener != null) {
            mOnCheckAdminInfoListener.onCheckAdminInfoListener(result, mIdentity);
        }
    }

    public void onGetRoomListInfo(int result, String json) {
        Logger.logD(Logger.USER, TAG + "->onGetRoomListInfo()->result:" + result + "json:" + json);
        if (result == DcpErrorcodeDef.RET_SUCCESS) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int actionType = jsonObject.getInt("_actionType");
                JSONArray jsonArray = jsonObject.getJSONArray("_roomList");
                mRoomListInfos = new CopyOnWriteArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    RoomListInfo info = new RoomListInfo();
                    if (actionType == 2) {
                        String reserveID = jsonObj.getString("_reserveID");
                        info.setReserveID(Integer.valueOf(reserveID).intValue());
                    }
                    String roomID = jsonObj.getString("_roomID");
                    String roomName = jsonObj.getString("_roomName");
                    info.setRoomID(Integer.valueOf(roomID).intValue());
                    info.setRoomName(roomName);
                    mRoomListInfos.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Logger.logD(Logger.USER, TAG + "->onGetRoomListInfo()->result:" + result);
        }
        if (mOnGetRoomListInfoListener != null) {
            mOnGetRoomListInfoListener.onGetRoomListInfoListener(result, mRoomListInfos);
        }
    }

    public List<RoomListInfo> getRoomListInfo() {
        return mRoomListInfos;
    }

    /**
     * 检测会议室密码回调
     *
     * @param result 返回码
     * @param json   回调数据
     */
    public void onCheckPwdInfo(int result, String json) {
        Logger.logD(Logger.USER, TAG + "->onCheckAdminInfo()->result:" + result + " json:" + json);
        if (mOnCheckPwdInfoListener != null) {
            mOnCheckPwdInfoListener.onCheckPwdInfoListener(result);
        }
    }

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     */
    public void saveUserInfo(final UserInfo userInfo) {
        Logger.logD(Logger.USER, TAG + "->saveUserInfo()->userInfo:" + userInfo);
        DatabaseTask<Void> dbTask = new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    //1.将所有账号置为不在使用-----------------------------------------------------------------
                    ContentValues unusedValues = new ContentValues();
                    unusedValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_USING, UserInfo.UNUSED);
                    long rows = writableDatabase.update(UserInfoContract.UserInfoEntry.TABLE_NAME, unusedValues, null, null);
                    Logger.logD(Logger.USER, "将所有账号置为不在使用update rows:" + rows);

                    //2.查询对应userid的账号是否已存在----------------------------------------------------------
                    boolean isExist = false;
                    String sql = "SELECT * FROM " + UserInfoContract.UserInfoEntry.TABLE_NAME
                            + " WHERE " + UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_ID + "=? ";
                    String[] selectionArgs = new String[]{String.valueOf(userInfo.getUserId())};
                    Cursor cursor = writableDatabase.rawQuery(sql, selectionArgs);
                    if (cursor != null) {
                        int count = cursor.getCount();
                        cursor.close();
                        isExist = count > 0;
                        Logger.logD(Logger.USER, "查询 userid 为" + userInfo.getUserId() + "的账号 count:" + count + ";isExist:" + isExist);
                    }

                    //3.插入或更新账号信息----------------------------------------------------------
                    ContentValues addValues = new ContentValues();
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_ACCOUNT_TYPE, userInfo.getAccountType());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_ID, userInfo.getUserId());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_NAME, userInfo.getUserName());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_PHONE_NUM, userInfo.getPhoneNum());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_LOGIN_AUTH_KEY, userInfo.getLoginAuthKey());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_ADDR, userInfo.getPesAddr());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_IP, userInfo.getPesIp());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_PORT, userInfo.getPesPort());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_USING, userInfo.getIsUsing());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_LAST_LOGIN_DT, userInfo.getLastLoginDt());
                    addValues.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_ACTIVITY, userInfo.getIsActivity());

                    if (isExist) {
                        String whereClause = UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_ID + " =? ";
                        String[] whereArgs = new String[]{String.valueOf(userInfo.getUserId())};
                        rows = writableDatabase.update(UserInfoContract.UserInfoEntry.TABLE_NAME, addValues, whereClause, whereArgs);
                        Logger.logD(Logger.USER, "更新账号信息 userid 为" + userInfo.getUserId() + "的账号 update rows:" + rows);
                    } else {
                        rows = writableDatabase.insert(UserInfoContract.UserInfoEntry.TABLE_NAME, null, addValues);
                        Logger.logD(Logger.USER, "插入账号信息 userid 为" + userInfo.getUserId() + "的账号 insert rowID:" + rows);
                    }
                    writableDatabase.setTransactionSuccessful();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    writableDatabase.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {
            }
        };
        getManager(DBManager.class).submitDatabaseTask(dbTask);
    }

    /**
     * 从数据库中获取当前正在使用的账号信息
     */
    public void loadUserInfo(final LoadUserInfoFished loadUserInfoFished) {
        DatabaseTask<UserInfo> dbTask = new DatabaseTask<UserInfo>() {
            @Override
            public AsyncResult<UserInfo> runOnDBThread(AsyncResult<UserInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                String sql = "SELECT * FROM " + UserInfoContract.UserInfoEntry.TABLE_NAME + " WHERE " + UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_USING + "=? ";
                writableDatabase.beginTransaction();
                try {
                    cursor = writableDatabase.rawQuery(sql, new String[]{String.valueOf(UserInfo.INUSE)});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUserId(cursor.getInt(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_ID)));
                            userInfo.setAccountType(cursor.getInt(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_ACCOUNT_TYPE)));
                            userInfo.setUserName(cursor.getString(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_NAME)));
                            userInfo.setPhoneNum(cursor.getString(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_PHONE_NUM)));
                            userInfo.setLoginAuthKey(cursor.getString(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_LOGIN_AUTH_KEY)));
                            userInfo.setPesAddr(cursor.getString(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_ADDR)));
                            userInfo.setPesIp(cursor.getLong(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_IP)));
                            userInfo.setPesPort(cursor.getInt(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_PES_PORT)));
                            userInfo.setLastLoginDt(cursor.getLong(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_LAST_LOGIN_DT)));
                            userInfo.setIsUsing(cursor.getInt(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_USING)));
                            userInfo.setIsActivity(cursor.getInt(cursor.getColumnIndex(UserInfoContract.UserInfoEntry.COLUMN_NAME_IS_ACTIVITY)));
                            asyncResult.setData(userInfo);
                        }
                    }
                    writableDatabase.setTransactionSuccessful();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    writableDatabase.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<UserInfo> asyncResult) {
                mCurrentUserInfo = asyncResult.getData();
                Logger.logD(Logger.USER, TAG + "->loadUserInfo()->CurrentUserInfo:" + mCurrentUserInfo);
                if (loadUserInfoFished != null && mCurrentUserInfo != null) {
                    loadUserInfoFished.onLoadUserInfoFished(mCurrentUserInfo);
                }
            }
        };
        getManager(DBManager.class).submitDatabaseTask(dbTask);
    }

    public interface LoadUserInfoFished {
        void onLoadUserInfoFished(UserInfo userInfo);
    }

    /**
     * 转为访客身份
     */
    public void removeUserInfo(final OnRemoveUserCallback callback) {
        DatabaseTask<Integer> dbTask = new DatabaseTask<Integer>() {
            @Override
            public AsyncResult<Integer> runOnDBThread(AsyncResult<Integer> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                int rows = 0;
                try {
                    rows = writableDatabase.delete(UserInfoContract.UserInfoEntry.TABLE_NAME,
                            UserInfoContract.UserInfoEntry.COLUMN_NAME_USER_ID + " = ?",
                            new String[]{String.valueOf(getCurrentUserInfo().getUserId())});
                    writableDatabase.setTransactionSuccessful();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    writableDatabase.endTransaction();
                }
                asyncResult.setData(rows);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Integer> asyncResult) {
                mCurrentUserInfo = null;
                if (callback != null) {
                    callback.onRemoveUser();
                }
            }
        };
        getManager(DBManager.class).submitDatabaseTask(dbTask);
    }

    /***********************************************其它pes用户状态变化回调处理***********************************************/
    /**
     * 收到被踢下线通知
     *
     * @param result 返回码
     * @param json   返回数据
     */
    public void onKickOff(int result, String json) {
        Logger.logD(Logger.USER, TAG + "->onKickOff()->result:" + result + ", json:" + json);
        handleKickoff();
    }

    /**
     * 收到离线通知
     *
     * @param result 返回码
     * @param json   返回数据
     */
    public void onOffline(int result, String json) {
        Logger.logD(Logger.USER, TAG + "->onOffline()->currentstate:" + AppApplication.getInstance().getOnlineState());
        if (AppApplication.getInstance().getOnlineState() != AppConstant.ONLINE_STATE_KICKOFF) {
            AppApplication.getInstance().setOnlineState(AppConstant.ONLINE_STATE_OFFLINE);
        }
    }

    /**
     * 收到发送活跃包结果
     *
     * @param result 返回码
     * @param json   返回数据
     */
    public void onSendAction(int result, String json) {
        Logger.logD(Logger.USER, TAG + "->onSendAction()->result:" + result + ", json:" + json);
        if (result == 0 && !StringUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int actionType = jsonObject.optInt("_actionType", 0);
                int returnResult = jsonObject.optInt("_result", 0);
                if (returnResult == 0 && actionType == 0) {
                    SPUtils.getInstance().put(AppPreference.KEY_SEND_ACTION_DT + getCurrentUserInfo().getUserId(), System.currentTimeMillis());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取代理服务器信息
     *
     * @param pesIp
     * @param pesPort
     */
    public void getAgentService(final long pesIp, final int pesPort) {
        getManager(DcpManager.class).setPesInfo(pesIp, pesPort, true);
        final AgentServiceRequester requester = new AgentServiceRequester((baseResult, agentServiceInfo) -> {
            Logger.logD(Logger.USER, TAG + "->getAgentService()->baseResult:" + baseResult + ", agentServiceInfo:" + agentServiceInfo);
            if (baseResult.getCode() != OnResultListener.RESULT_SUCCESS || agentServiceInfo == null || StringUtils.isEmpty(agentServiceInfo.getUrl())) {
                return;
            }
            getManager(HTTPDNSManager.class).getHttpDnsUrl(AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN, url -> {
                if (!agentServiceInfo.getUrl().equals(url)) {
                    getManager(HTTPDNSManager.class).insertIpData(AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN, agentServiceInfo.getUrl(), pesIp, new HTTPDNSManager.OnInsertIpListener() {
                        @Override
                        public void onInsertIpListener(String url) {
                            getManager(DcpManager.class).setPesInfo(pesIp, pesPort, true);
                        }
                    });
                }
            });
        });
        requester.doPost();
    }

    /**
     * 处理被踢下线
     */
    public void handleKickoff() {
        Logger.logD(Logger.USER, TAG + "->handleKickoff()");
        AppApplication.getInstance().setOnlineState(AppConstant.ONLINE_STATE_KICKOFF);
        getManager(DcpManager.class).logout(getCurrentUserInfo().getUserId());
        getManager(DcpManager.class).disconnect();
        getManager(LocalNotificationManager.class).cancleAllNotification();
        getManager(MaterialTaskManager.class).reset();
        SPUtils.getInstance().put(AppPreference.FLAG_BACKGROUND_KICKOFF, true);
        removeUserInfo(() -> {
            if (Utils.isAppRunningForeground(mApplication, mApplication.getPackageName())) {
                Intent intent = new Intent(mApplication, KickOffDialog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplication.startActivity(intent);
                Logger.logD(Logger.USER, TAG + "->程序在前端运行()");
            } else {
                SPUtils.getInstance().put(AppPreference.FLAG_BACKGROUND_KICKOFF, true);
                Logger.logD(Logger.USER, TAG + "->程序不在前端运行()");
            }
        });
    }

    public interface OnRemoveUserCallback {
        void onRemoveUser();
    }

    public interface OnCheckAdminInfoListener {
        void onCheckAdminInfoListener(int result, int identity);
    }

    public interface OnGetRoomListInfoListener {
        void onGetRoomListInfoListener(int result, List<RoomListInfo> list);
    }

    public interface OnCheckPwdInfoListener {
        void onCheckPwdInfoListener(int result);
    }
}
