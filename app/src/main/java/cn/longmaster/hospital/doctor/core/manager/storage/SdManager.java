package cn.longmaster.hospital.doctor.core.manager.storage;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.log.Logger;


/**
 * SD卡文件管理类
 * Created by yangyong on 2015/7/13.
 */
public class SdManager {
    private final String TAG = SdManager.class.getSimpleName();

    private static SdManager mSdManager;
    /**
     * 主目录
     */
    private String DIR_DOCTOR = "/hospdoctor/";
    /**
     * 临时目录
     */
    private final String DIR_TEMP = "temp/";
    /**
     * crash目录
     */
    private final String DIR_CRASH = "crash/";
    /**
     * 头像
     */
    private final String DIR_AVATAR = "avatar/";
    /**
     * 启动页
     */
    private final String DIR_START_PAGE = "start_page/";
    /**
     * 首页banner
     */
    private final String DIR_BANNER = "banner/";

    /**
     * 科室相关
     */
    private final String DIR_DEPARTMENT = "department/";

    /**
     * 销售代表功能
     */
    private final String DIR_REPRESENT = "represent/";
    /**
     * 医生签名图片
     */
    private final String DIR_SIGN = "sign/";
    /**
     * 预约语音、图片、视频文件
     */
    private final String DIR_ORDER = "order/";
    private final String DIR_VOICE = "voice/";
    private final String DIR_PIC = "pic/";
    private final String DIR_VIDEO = "video/";
    /**
     * log目录
     */
    private final String DIR_LOG = "log/";
    /**
     * 下载目录
     */
    private final String DIR_DOWNLOAD = "download/";
    /**
     * 消息图片目录
     */
    private final String DIR_SMS = "sms/pic/";
    private final String DIR_IM_PIC = "im/pic/";
    private final String DIR_IM_VOICE = "im/voice/";
    private final String DIR_PAYMENT_PIC = "payment/pic/";
    private final String DIR_COLLEGE = "college/pic/";
    private final String PICTURES = "/Pictures/";
    private String mTempPath;
    private String mAvatarPath;
    private String mStartPagePath;
    private String mBannerPath;
    private String mOrderPath;
    private String mLogPath;
    private String mDownloadPath;
    private String mDepartmentPath;
    private String mRepresentPath;
    private String mCrashPath;
    private String mSmsPath;
    private String mSign;
    private String mIMPic;
    private String mIMVoice;
    private String mPaymentPic;
    private String mCollege;

    private String mAlbumPath;
    private String mDoctorRootPath;

    private final String DIR_ASSISTANT = "assistant/";

    public final static SdManager getInstance() {
        if (mSdManager == null) {
            synchronized (SdManager.class) {
                if (mSdManager == null) {
                    mSdManager = new SdManager();
                }
            }
        }
        return mSdManager;
    }

    private SdManager() {
        mDoctorRootPath = getDirectoryPath() + DIR_DOCTOR;
        mAlbumPath = getDirectoryPath() + PICTURES;
        mTempPath = mDoctorRootPath + DIR_TEMP;
        mAvatarPath = mDoctorRootPath + DIR_AVATAR;
        mStartPagePath = mDoctorRootPath + DIR_START_PAGE;
        mBannerPath = mDoctorRootPath + DIR_BANNER;
        mOrderPath = mDoctorRootPath + DIR_ORDER;
        mLogPath = mDoctorRootPath + DIR_LOG;
        mDownloadPath = mDoctorRootPath + DIR_DOWNLOAD;
        mDepartmentPath = mDoctorRootPath + DIR_DEPARTMENT;
        mRepresentPath = mDoctorRootPath + DIR_REPRESENT;
        mCrashPath = mDoctorRootPath + DIR_CRASH;
        mSmsPath = mDoctorRootPath + DIR_SMS;
        mSign = mDoctorRootPath + DIR_SIGN;
        mIMPic = mDoctorRootPath + DIR_IM_PIC;
        mIMVoice = mDoctorRootPath + DIR_IM_VOICE;
        mPaymentPic = mDoctorRootPath + DIR_PAYMENT_PIC;
        mCollege = mDoctorRootPath + DIR_COLLEGE;
        mkdir(mTempPath, mAvatarPath, mStartPagePath, mBannerPath, mOrderPath, mLogPath, mDepartmentPath, mRepresentPath, mCrashPath, mSmsPath, mSign, mIMPic, mIMVoice, mPaymentPic, mCollege);
    }

    public void init() {
        hintMediaFile(mTempPath, mAvatarPath, mStartPagePath, mBannerPath, mOrderPath, mLogPath, mDownloadPath, mDepartmentPath, mRepresentPath, mCrashPath, mSmsPath, mSign, mIMPic, mIMVoice, mPaymentPic, mCollege);
    }

    private void hintMediaFile(String... filePaths) {
        for (String filePath : filePaths) {
            try {
                if (filePath == null) {
                    break;
                }

                if (!filePath.endsWith(File.separator)) {
                    filePath = filePath + File.separatorChar;
                }

                File file = new File(filePath + ".nomedia");

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final String getTempPath() {
        return mTempPath;
    }

    /**
     * 获得头像目录路径
     *
     * @return
     */
    public final String getAvatarPath() {
        return mAvatarPath;
    }

    /**
     * 获得图片目录路径
     *
     * @return 图片目录路径
     */
    public final String getPicturePath() {
        return mStartPagePath;
    }

    /**
     * 得到手机相册地址
     *
     * @return
     */
    public final String getAlbumPath() {
        return mAlbumPath;
    }

    /**
     * 得到指定用户id目录下该用户的头像路径
     *
     * @param userId 用户id
     * @return 头像路径
     */
    public final String getAppointAvatarFilePath(String userId) {
        String result = mAvatarPath + MD5Util.md5(userId);
        return result;
    }

    /**
     * 获得首页banner路径
     *
     * @return 首页banner路径
     */
    public final String getBannerPath() {
        return mBannerPath;
    }

    /**
     * 获取预约文件根目录路径
     *
     * @return 预约文件根目录路径
     */
    public final String getOrderDirPath() {
        return mOrderPath;
    }

    /**
     * 获取预约语音文件路径
     *
     * @param fileName      语音文件名（上传成功后服务器返回）
     * @param appointmentId 预约ID
     * @return 文件存放在本地的路径
     */
    public final String getOrderVoicePath(String fileName, String appointmentId) {
        return mOrderPath + appointmentId + File.separatorChar + DIR_VOICE + fileName;
    }

    /**
     * 获取预约辅助资料图片文件路径
     *
     * @param fileName      图片文件名（上传成功后服务器返回）
     * @param appointmentId 预约ID
     * @return 文件存放在本地的路径
     */
    public final String getOrderPicPath(String fileName, String appointmentId) {
        return mOrderPath + appointmentId + File.separatorChar + DIR_PIC + fileName;
    }

    /**
     * 获取预约辅助资料视频文件路径
     *
     * @param fileName      图片文件名（上传成功后服务器返回）
     * @param appointmentId 预约ID
     * @return 文件存放在本地的路径
     */
    public final String getOrderVideoPath(String fileName, String appointmentId) {
        return mOrderPath + appointmentId + File.separatorChar + DIR_VIDEO + fileName;
    }

    public final String getDepartmentPath(String fileName) {
        return mDepartmentPath + fileName;
    }

    public final String getRepresentPath(int id) {
        return mRepresentPath + id;
    }

    public String getCrashPath() {
        return mCrashPath;
    }

    /**
     * 获取消息图片目录
     *
     * @return
     */
    public String getSmsPath(String picName) {
        return mSmsPath + picName;
    }

    /**
     * 获取医生助理头像文件路径
     *
     * @param fileName
     * @return
     */
    public final String getAssistantPath(String fileName) {
        return mAvatarPath + DIR_ASSISTANT + fileName;
    }

    /**
     * 获取医生签名件路径
     *
     * @param fileName
     * @return
     */
    public final String getSignPath(String fileName) {
        return mSign + fileName;
    }

    /**
     * 获取log文件目录
     *
     * @return log文件目录
     */
    public final String getLogPath() {
        return mLogPath;
    }

    public final String getDownloadPath() {
        return mDownloadPath;
    }

    public String getIMPic() {
        return mIMPic;
    }

    public String getIMVoice() {
        return mIMVoice;
    }

    public String getPaymentPic() {
        return mPaymentPic;
    }

    public final String getCollege(String fileName) {
        return mCollege + fileName;
    }

    // *****************************神奇分割线*****************************

    /**
     * 获得系统sdcard路径
     *
     * @return
     */
    public final static String getDirectoryPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 检测sdcard是否可用
     *
     * @return
     */
    public final static boolean available() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建文件目录
     *
     * @param filePaths
     * @return
     */
    public final synchronized boolean mkdir(String... filePaths) {
        try {
            for (String filePath : filePaths) {
                Logger.logD(Logger.COMMON, TAG + "->mkdir()->filePath:" + filePath);

                if (filePath == null) {
                    return false;
                }
                if (!filePath.endsWith(File.separator)) {
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
                }
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                } else if (file.isFile()) {
                    file.delete();
                    file.mkdirs();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean mkdirs(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            return true;
        } else {
            if (file.exists()) {
                file.delete();
            }
            file.mkdirs();
            return true;
        }
    }

    private void cleanFilePath(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
            File[] delFile = f.listFiles();
            int i = delFile.length;
            for (int j = 0; j < i; j++) {
                if (delFile[j].isDirectory()) {
                    cleanFilePath(delFile[j].getAbsolutePath());
                }
                delFile[j].delete();
            }
        }
    }

    public String getAllFileSize() {
        long allFileSize = FileUtil.getFolderSize(new File(mDoctorRootPath));
        if (allFileSize < 1048576) {
            return "0" + new DecimalFormat("#.0").format((double) allFileSize / 1048576);
        } else {
            return new DecimalFormat("#.0").format((double) allFileSize / 1048576);
        }
    }

    public void cleanAllFilePath(SdManagerCallback sdManagerCallback) {
//        cleanFilePath(mTempPath);
//        cleanFilePath(mAvatarPath);
//        cleanFilePath(mStartPagePath);
//        cleanFilePath(mBannerPath);
//        cleanFilePath(mOrderPath);
//        cleanFilePath(mLogPath);
//        cleanFilePath(mDepartmentPath);
//        cleanFilePath(mRepresentPath);
//        cleanFilePath(mCrashPath);
//        cleanFilePath(mSmsPath);
//        cleanFilePath(mSign);
        cleanFilePath(mDoctorRootPath);
        if (sdManagerCallback != null) {
            sdManagerCallback.callback();
        }
    }

    public static String getMaterialSmallPicLocalPath(String picServerName, String appointmentId) {
        String smallPicName = picServerName.substring(0, picServerName.lastIndexOf(".")) + "_s" + picServerName.substring(picServerName.lastIndexOf("."));
        return SdManager.getInstance().getOrderPicPath(smallPicName, appointmentId);
    }

    public static String getMaterialOriginalPicLocalPath(String picServerName, String appointmentId) {
        return SdManager.getInstance().getOrderPicPath(picServerName, appointmentId);
    }

    public interface SdManagerCallback {
        void callback();
    }

}
