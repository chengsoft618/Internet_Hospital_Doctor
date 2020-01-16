//$_FILEHEADER_BEGIN ***************************
//版权声明: 贵阳朗玛信息技术股份有限公司版权所有
//Copyright (C) 2013 Longmaster Corporation. All Rights Reserved
//文件名称: CommonUtils.java
//创建日期: 2013/04/09
//创 建 人: 段凌云
//文件说明: 通用工具类
//$_FILEHEADER_END *****************************
package cn.longmaster.doctorlibrary.util.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.longmaster.doctorlibrary.util.UtilStatus;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.Utils;

@SuppressWarnings("ALL")
public class CommonUtils {
    private final static String TAG = "CommonUtils";
    private static int m_screenDPI = -1;
    private static int m_simCardState = -1;
    private static String m_sDeviceVersion = "";
    private static String m_sIMEI = "";
    private static int m_iSdkVersion = 0;
    private static float m_iDeviceVersion = 0;
    private static String m_strPhoneStyle = "";// 手机型号
    private static DisplayMetrics m_displayMetrics;
    private static int m_screenWidth = -1;
    private static int m_screenHeight = -1;
    private static String m_phoneVersion;// 手机版本号

    /**
     * 获得进程名字
     */
    public static String getUIPName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    // 获取版本号
    public static String getStringDeviceVersion() {
        m_sDeviceVersion = android.os.Build.VERSION.RELEASE;

        return m_sDeviceVersion;
    }

    // 获得数字版本号
    public static float getIntDeviceVersion() {
        m_sDeviceVersion = android.os.Build.VERSION.RELEASE;
        if (m_sDeviceVersion != null && m_sDeviceVersion.length() >= 3) {
            String spiltString = m_sDeviceVersion.substring(0, 3);
            Pattern pattern = Pattern.compile("^\\d+([\\.]?\\d+)?$");
            Matcher matcher = pattern.matcher(spiltString);
            boolean result = matcher.matches();
            if (result == true) {
                m_iDeviceVersion = Float.valueOf(spiltString);
            } else {
                m_iDeviceVersion = 0;
            }
        }
        return m_iDeviceVersion;

    }

    public static String getPhoneVersion() {
        m_phoneVersion = android.os.Build.VERSION.CODENAME;
        return m_phoneVersion;
    }

    public static int getDeviceSdk() {
        m_iSdkVersion = android.os.Build.VERSION.SDK_INT;
        return m_iSdkVersion;
    }

    // 获取MacAddress
    public static String getMacAddress() {

        String macAddress = "";
        WifiManager wifi = (WifiManager) UtilStatus.getApplication().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            // 如果Wifi关闭的时候，硬件设备可能无法返回MAC ADDRESS
            if (null != info) {
                macAddress = null == info.getMacAddress() ? "" : info.getMacAddress();
            }
        }
        return macAddress;
    }

    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) UtilStatus.getApplication().getSystemService(Service.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null && !"".equals(tm.getDeviceId())) {
            m_sIMEI = null == tm.getDeviceId() ? "" : tm.getDeviceId();
        }
        return m_sIMEI;

    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneStyle() {
        m_strPhoneStyle = android.os.Build.MODEL;
        return m_strPhoneStyle;
    }

    /**
     * 获取国际移动用户识别码
     *
     * @param context 上下文
     * @return 手机号码，取不到时返回空字符串
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (null == imsi) {
            imsi = "";
        }
        return imsi;
    }

    /**
     * 获取手机电话号码
     *
     * @param context 上下文
     * @return 手机号码，取不到时返回空字符串
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        return phoneNumber;
    }

    /**
     * 获取手机ip地址
     *
     * @return ip地址，没有获取到时返回空字符串
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static int getScreenDPI() {
        if (m_screenDPI == -1) {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wndMgr = (WindowManager) UtilStatus.getApplication().getSystemService(Context.WINDOW_SERVICE);
            wndMgr.getDefaultDisplay().getMetrics(metric);
            m_screenDPI = metric.densityDpi;
        }
        return m_screenDPI;
    }

    public static int getSIMCardState(Context context) {
        if (m_simCardState == -1) {
            TelephonyManager l_TelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            m_simCardState = l_TelephonyManager.getSimState();
        }
        return m_simCardState;
    }

    public static void showSoftInput(final Context context, final View focusView) {
        focusView.requestFocus();
        focusView.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.showSoftInput(focusView, 0);
            }
        }, 200);
    }

    public static void hideSoftInput(final Activity activity) {
        if (activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示输入法
     *
     * @param editText
     */
    public static void showSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void defaultHideSoftInput(final Activity activity) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMgr != null && activity != null) {
                    inputMgr.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 200);
    }

    /**
     * 获取文字高度和行高
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 检查邮箱地址是否符合规范
     *
     * @param a_strEmailAddress
     * @return
     */
    public static boolean checkEmailAddressFormat(String a_strEmailAddress) {
        if (a_strEmailAddress == null || "".equals(a_strEmailAddress.trim())) {
            return false;
        }

        if (!Pattern.matches("\\w(\\.?\\w)*\\@\\w+(\\.[\\w&&[\\D]]+)+", a_strEmailAddress)) {
            return false;
        }

        return true;
    }

    /**
     * 检查密码是否符合规范（规范为密码长度是6-20位，而且只能是字母或者是数字的组合）
     *
     * @param a_strPassword
     * @return
     */
    public static boolean checkPasswordFormat(String a_strPassword) {
        if (a_strPassword == null || "".equals(a_strPassword.trim())) {
            return false;
        }

        if (a_strPassword.length() < 6 || a_strPassword.length() > 20) {
            return false;
        }

        if (!Pattern.matches("[\\da-zA-Z]+", a_strPassword)) {
            return false;
        }

        return true;
    }

    public static int getScreenWidth() {
        if (m_screenWidth == -1) {
            initDisplayMetrics();
            m_screenWidth = m_displayMetrics.widthPixels;
        }
        return m_screenWidth;
    }

    public static int getScreenHeight() {
        if (m_screenHeight == -1) {
            initDisplayMetrics();
            m_screenHeight = m_displayMetrics.heightPixels;
        }
        return m_screenHeight;
    }

    private static void initDisplayMetrics() {
        if (m_displayMetrics == null) {
            m_displayMetrics = new DisplayMetrics();
            WindowManager wndMgr = (WindowManager) UtilStatus.getApplication().getSystemService(Context.WINDOW_SERVICE);
            wndMgr.getDefaultDisplay().getMetrics(m_displayMetrics);
        }
    }


    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    @Deprecated
    public static boolean isNumeric(String str) {
        return TextUtils.isDigitsOnly(str);
    }

    /**
     * 简单验证身份证号码
     *
     * @param id
     * @return true 正确;  false 不符合
     */
    public static boolean isIdCard(String id) {
        int length = id.length();
        if (length < 15) {
            return false;
        }
        Pattern p = null;
        if (length == 15) {
            p = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        } else if (length == 18) {
            p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9xX]$");
        }
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 判断输入内容是否只有数字和大小写字母
     *
     * @param input 输入字符
     * @return result
     * true 含有其他字符， false 不含其他字符
     */
    public static boolean hasSpecialCharacter(String input) {
        char[] temC = input.toCharArray();
        for (int i = 0; i < temC.length; i++) {
            int mid = temC[i];
            if (mid >= 48 && mid <= 57) {
                continue;
            }
            if (mid >= 65 && mid <= 90) {
                continue;
            }
            if (mid >= 97 && mid <= 122) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取运营商信息
     *
     * @return
     *         运营商信息
     */
    // public static int getOperateInfo()
    // {
    // TelephonyManager telManager = (TelephonyManager)
    // HApplication.getAppApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    // String operator = telManager.getSimOperator();
    // int result = Constants.OPERATETYPE_UNKNOW;
    //
    // if (operator != null)
    // {
    // if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007"))
    // {
    // result = Constants.OPERATETYPE_YD;
    // }
    // else if (operator.equals("46001"))
    // {
    // result = Constants.OPERATETYPE_LT;
    // }
    // else if (operator.equals("46003"))
    // {
    // result = Constants.OPERATETYPE_DX;
    // }
    // }
    // return result;
    // }

    /**
     * 生成随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int random(int min, int max) {
        return (int) Math.rint(Math.random() * (max - min) + min);
    }

    /**
     * 字符串装换成整型
     *
     * @param input 字符串
     * @return 如果字符串能够转换成整型就返回转换后的整型，否则返回0
     */
    public static int StringToInt(String input) {
        int result = 0;
        if (StringUtils.isDigitsOnly(input)) {
            return Integer.valueOf(input);
        }

        return result;
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     * @return
     */
    public static final boolean hideSoftPad(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            return ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 将byte数组转换成十六进制字符串
     *
     * @param paramArrayOfByte 字节数组
     * @param paramInt         长度
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] paramArrayOfByte, int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder("");
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0)) {
            return null;
        }

        for (int i = 0; i < paramInt; ++i) {
            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            str = str.toUpperCase();
            if (str.length() < 2) {
                localStringBuilder.append(0);
            }
            localStringBuilder.append(str);
        }
        return localStringBuilder.toString();
    }

    /**
     * 十六进制字符串转换成字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    /**
     * 将字符串转换成字节
     *
     * @param c 字符串
     * @return 字节
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 判断应用是否处于后台
     *
     * @param context 上下文
     * @return 是否处于后台，true ：后台， false：前台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断activity是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkActivityExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
