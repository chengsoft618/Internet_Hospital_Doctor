package cn.longmaster.hospital.doctor.core.entity.common;

import android.os.Bundle;

/**
 * DCP服务端返回的数据
 * Created by ddc on 2015-07-20.
 */
public class DcpResultInfo {

    public static final String KEY_USERID = "KEY_USERID";
    public static final String KEY_LOGIN_AUTH_KEY = "KEY_LOGIN_AUTH_KEY";
    public static final String KEY_PES_ADDR = "KEY_PES_ADDR";
    public static final String KEY_PES_IP = "KEY_PES_IP";
    public static final String KEY_PES_PORT = "KEY_PES_PORT";

    public static final String KEY_PHONE_NUM = "KEY_PHONE_NUM";
    public static final String KEY_REQUEST_TYPE = "KEY_REQUEST_TYPE";
    public static final String KEY_VERIFY_CODE = "KEY_VERIFY_CODE";

    public static final String KEY_CONFIG_STATE = "KEY_CONFIG_STATE";
    public static final String KEY_CLIENT_VERSION_LIMIT = "KEY_CLIENT_VERSION_LIMIT";
    public static final String KEY_CLIENT_VERSION_LATEST = "KEY_CLIENT_VERSION_LATEST";
    public static final String KEY_CLIENT_VERSION_LATEST_WIZARD = "KEY_CLIENT_VERSION_LATEST_WIZARD";
    public static final String KEY_CLIENT_AUDIO_MODE_VALUE = "KEY_CLIENT_AUDIO_MODE_VALUE";

    //onRegAccount 接口返回字段  说明：注册账号返回时的回调
    public int _userID; //_userID	uint32_t	用户ID
    public String _loginAuthKey; // _loginAuthKey	string	登陆鉴权Key
    public String _pesAddr;//_pesAddr	string	对应的pes的地址
    public long _pesIP;//_pesIP	uint32_t	pes的ip
    public short _pesPort;//_pesPort	uint16_t	pes的端口

    //onCheckVerifyCode 接口返回字段  说明：返回验证码是否成功
    public String _phoneNum;//	string	用户账号
    //public int _userID;//uint32_t	用户ID
    //public String _loginAuthKey;//	string	登陆鉴权Key
    //public String _pesAddr;//	string	对应的pes的地址
    // String short;//	uint32_t	pes的ip
    //public String _pesPort;//	uint16_t	pes的端口
    public byte _requestType;//	uint8_t	请求类型
    public String _verifyCode;//	string	用户验证码


    //onLogin 接口返回字段  说明：由服务器返回登录结果时触发的回调函数。
    public short _configState;// _configState	uint16_t	按位标记客户端是否打开某些开关，从低位到高位依次为：
    //                                0：是否要向服务器报告（IOS：设备token； Android：romVersion）；
    //                                1：是否开启活动专区；
    //                                2：是否开启应用商店；
    //public int _userID;// _userID uint32_t 服务器分配该登录用户的会员号
    public int _clientVersionLimit;// _clientVersionLimit uint32_t  客户端最低版本限制，低于此版本则不能使用客户端
    public int _clientVersionLatest;//_clientVersionLatest uint32_t 客户端最新版本
    public byte _clientVersionLatestWizard;//_clientVersionLatestWizard	uint8_t
    //                                         needWizard：
    //                                            按位标记提示方法，从低位到高位依次为
    //                                            0：本地标记；
    //                                            1：更新引导对话框；
    //                                            2：本地通知；
    //                                            说明：0表示不产生该类提示，1表示产生该类提示。
//    public int _clientAudioModeValue;//   _clientAudioModeValue uint32_t 客户端声音模式

    public DcpResultInfo() {
    }

    public DcpResultInfo(Bundle bundle) {
        if (bundle != null) {
            _userID = bundle.getInt(KEY_USERID);
            _loginAuthKey = bundle.getString(KEY_LOGIN_AUTH_KEY);
            _pesAddr = bundle.getString(KEY_PES_ADDR);
            _pesIP = bundle.getInt(KEY_PES_IP);
            _pesPort = bundle.getShort(KEY_PES_PORT);

            _phoneNum = bundle.getString(KEY_PHONE_NUM);
            _requestType = bundle.getByte(KEY_REQUEST_TYPE);
            _verifyCode = bundle.getString(KEY_VERIFY_CODE);

            _configState = bundle.getShort(KEY_CONFIG_STATE);
            _clientVersionLimit = bundle.getInt(KEY_CLIENT_VERSION_LIMIT);
            _clientVersionLatest = bundle.getInt(KEY_CLIENT_VERSION_LATEST);
            _clientVersionLatestWizard = bundle.getByte(KEY_CLIENT_VERSION_LATEST_WIZARD);
//            _clientAudioModeValue = bundle.getInt(KEY_CLIENT_AUDIO_MODE_VALUE);
        }
    }

    public Bundle buildBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_USERID, _userID);
        bundle.putString(KEY_LOGIN_AUTH_KEY, _loginAuthKey);
        bundle.putString(KEY_PES_ADDR, _pesAddr);
        bundle.putLong(KEY_PES_IP, _pesIP);
        bundle.putShort(KEY_PES_PORT, _pesPort);

        bundle.putString(KEY_PHONE_NUM, _phoneNum);
        bundle.putByte(KEY_REQUEST_TYPE, _requestType);
        bundle.putString(KEY_VERIFY_CODE, _verifyCode);

        bundle.putShort(KEY_CONFIG_STATE, _configState);
        bundle.putInt(KEY_CLIENT_VERSION_LIMIT, _clientVersionLimit);
        bundle.putInt(KEY_CLIENT_VERSION_LATEST, _clientVersionLatest);
        bundle.putByte(KEY_CLIENT_VERSION_LATEST_WIZARD, _clientVersionLatestWizard);
//        bundle.putInt(KEY_CLIENT_AUDIO_MODE_VALUE, _clientAudioModeValue);

        return bundle;
    }

    @Override
    public String toString() {
        return "DcpResultInfo{" +
                "_userID=" + _userID +
                ", _loginAuthKey='" + _loginAuthKey + '\'' +
                ", _pesAddr='" + _pesAddr + '\'' +
                ", _pesIP=" + _pesIP +
                ", _pesPort=" + _pesPort +
                ", _phoneNum='" + _phoneNum + '\'' +
                ", _requestType=" + _requestType +
                ", _verifyCode='" + _verifyCode + '\'' +
                ", _configState=" + _configState +
                ", _clientVersionLimit=" + _clientVersionLimit +
                ", _clientVersionLatest=" + _clientVersionLatest +
                ", _clientVersionLatestWizard=" + _clientVersionLatestWizard +
//                ", _clientAudioModeValue=" + _clientAudioModeValue +
                '}';
    }
}
