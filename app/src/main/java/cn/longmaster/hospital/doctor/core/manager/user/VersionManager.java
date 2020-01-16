package cn.longmaster.hospital.doctor.core.manager.user;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.common.StorageUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AppAsyncTask;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.UpdateDialog;
import cn.longmaster.utils.NotificationHelper;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.Utils;

/**
 * 版本管理类，用于管理应用版本信息
 * Created by yangyong on 2015/7/13.
 */
public class VersionManager {
    private final String TAG = VersionManager.class.getSimpleName();
    private final String FILE_PREFIX = "internet_hospital_doctor_Android_";
    public static final String DOWNLOAD_DIR_PATH = "/hospdoctor/download/";
    private static VersionManager mVersionManager;
    /**
     * 是否正在下载文件
     */
    private static boolean mIsDownloadingFile = false;
    /**
     * 当前文件大小
     */
    private static long mCurrentFileSize = 0;
    /**
     * 新版提示框
     */
    private static UpdateDialog mUpdateDialog;
    /**
     * 文件输入流
     */
    private static InputStream mInputStream;
    /**
     * 文件输出流
     */
    private static FileOutputStream mFileOutputStream;
    /**
     * 下载进度
     */
    private static int mPercent = 0;

    private static int mClientVersionLatest;

    private static int mClientVersionLimit;

    private boolean isMainActivityExit = false;

    public static VersionManager getInstance() {
        if (mVersionManager == null) {
            synchronized (VersionManager.class) {
                if (mVersionManager == null) {
                    mVersionManager = new VersionManager();
                }
            }
        }
        return mVersionManager;
    }

    private VersionManager() {
        mClientVersionLatest = SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LASTEST_VERSION, getCurentClientVersion());
        mClientVersionLimit = SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LIMIT_VERSION, 0);
    }

    /**
     * 获取客户端当前版本号
     *
     * @return 客户端当前版本号
     */
    public int getCurentClientVersion() {
        return AppConfig.CLIENT_VERSION;
    }

    public static int getClientVersionLatest() {
        return mClientVersionLatest;
    }

    public static void setClientVersionLatest(int mClientVersionLatest) {
        VersionManager.mClientVersionLatest = mClientVersionLatest;
        SPUtils.getInstance().put(AppPreference.KEY_SERVER_LASTEST_VERSION, mClientVersionLatest);
    }

    public static int getClientVersionLimit() {
        return mClientVersionLimit;
    }

    public static void setClientVersionLimit(int mClientVersionLimit) {
        VersionManager.mClientVersionLimit = mClientVersionLimit;
        SPUtils.getInstance().put(AppPreference.KEY_SERVER_LIMIT_VERSION, mClientVersionLimit);
    }

    /**
     * 设置主界面是否存在,不存在不弹出更新框否则会崩溃
     *
     * @param mainActivityExit 是否存在
     */
    public void setMainActivityExit(boolean mainActivityExit) {
        isMainActivityExit = mainActivityExit;
    }

    public void upgrade(final Context context, boolean isForeGround) {
        if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
            return;
        }

        AppAsyncTask<Void> task = new AppAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                setApkFileSize();
                setUpdateInfo();
            }

            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                showNewClientVersionDialog(context, isForeGround);
            }
        };
        task.execute();
    }

    public void showUpdateDialog(final Context context) {
        AppAsyncTask<Void> task = new AppAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                setApkFileSize();
            }

            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                buildDialog(context);
            }
        };
        task.execute();
    }

    public void buildDialog(final Context context) {
        new CommonDialog.Builder(context)
                .setMessage(R.string.version_dialog_sure_update_to_new_version)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> AppHandlerProxy.post(() -> startDownload(context)))
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    public void setApkFileSize() {
        long apkFileSize = getApkFileSize();
        SPUtils.getInstance().put(AppPreference.KEY_SERVER_LASTEST_VERSION_APK_SIZE, apkFileSize);
    }

    public void setUpdateInfo() {
        List<String> list = getUpdateInfo();
        if (list != null && list.size() > 1) {
            String feature = list.get(0);
            SPUtils.getInstance().put(AppPreference.KEY_SERVER_LASTEST_VERSION_FEATURE, feature);
            String md5 = list.get(1);
            SPUtils.getInstance().put(AppPreference.KEY_SERVER_LASTEST_VERSION_MD5, md5);
        }
    }

    private long getApkFileSize() {
        long fileSize = -1;
        try {
            String appFileUrl = AppConfig.getUpgradeXmlUrl() + getFullVersionName(FILE_PREFIX, mClientVersionLatest) + ".apk";
            URL url = new URL(appFileUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "NetFox");
            int responseCode = httpConnection.getResponseCode();

            if (responseCode >= 400) {
                return -1;
            }

            String header;
            for (int i = 1; ; i++) {
                header = httpConnection.getHeaderFieldKey(i);
                if (header != null) {
                    // 真机Content-Length，虚拟机content-length
                    if ("Content-Length".equals(header)) {
                        fileSize = Long.parseLong(httpConnection.getHeaderField(header));
                        break;
                    }
                    if ("content-length".equals(header)) {
                        fileSize = Long.parseLong(httpConnection.getHeaderField(header));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            fileSize = -1;
            e.printStackTrace();
        }
        return fileSize;
    }

    private List<String> getUpdateInfo() {
        List<String> updateInfos = new ArrayList<>();
        try {
            String xmlFileUrl = AppConfig.getUpgradeXmlUrl() + getFullVersionName(FILE_PREFIX, mClientVersionLatest) + ".xml";
            URL url = new URL(xmlFileUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                updateInfos = parserUpdateXml(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateInfos;
    }

    /**
     * 获取完整的版本名
     *
     * @param prefix  FILE_PREFIX
     * @param version 版本号，如1001
     * @return 完整的版本名如：doctor_Android_0.1.01
     */
    public String getFullVersionName(String prefix, int version) {
        int back = version % 1000;
        int centre = (version / 1000) % 100;
        int front = version / 100000;
        ////////////////////////////////////////////
        //版本规则改动？？？？？？？0.1.0x -> 0.1.x
        ////////////////////////////////////////////

        String versionName;
        if (back > 9) {
            versionName = prefix + front + "." + centre + "." + back;
        } else {
            versionName = prefix + front + "." + centre + ".0" + back;
        }

        return versionName;
    }

    /**
     * 解析版本更新xml文件
     *
     * @param inputStream 文件输入流
     * @return 新版特性及md5
     */
    public List<String> parserUpdateXml(InputStream inputStream) {
        XmlPullParser xmlParser = Xml.newPullParser();
        String feature = "";
        String md5 = "";
        try {
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                if (evtType == XmlPullParser.START_TAG) {
                    String xmlTag = xmlParser.getName();
                    if ("feature".equals(xmlTag)) {
                        feature = xmlParser.nextText();
                        if (!"".equals(md5)) {
                            break;
                        }
                    }

                    if ("md5".equals(xmlTag)) {
                        md5 = xmlParser.nextText();
                        if (!"".equals(feature)) {
                            break;
                        }
                    }
                }
                evtType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        List<String> list = new ArrayList<String>();
        list.add(feature);
        list.add(md5);
        return list;
    }

    /**
     * 显示新版客户端升级提示框
     *
     * @param context 上下文
     */
    public void showNewClientVersionDialog(final Context context, boolean isForeGround) {
        if (context == null) {
            return;
        }

        UpdateDialog.Builder builder = new UpdateDialog.Builder(context);

        // 版本
        builder.setVersion(getFullVersionName("", mClientVersionLatest));
        builder.setCancelable(false);
        // 是否可以关闭，强制升级不可关闭
        builder.setCloseable(getCurentClientVersion() >= mClientVersionLimit);
        builder.setOnUpdateClickListener(v -> startDownload(context));
        builder.setOnCloseClickListener(v -> {
        });

        if (mUpdateDialog != null && isForeGround) {
            mUpdateDialog.dismiss();
        }

        Logger.logD(Logger.COMMON, TAG + "->showNewClientVersionDialog()->isMainActivityExit:" + isMainActivityExit);
        if (isMainActivityExit) {
            mUpdateDialog = builder.show();
        }
    }

    private void showDownloadingNotification(final Context context) {
        Runnable runnable = () -> {
            Handler handler = new Handler(Looper.getMainLooper());
            final int notificationID = VersionManager.class.getCanonicalName().hashCode();
            NotificationHelper helper = new NotificationHelper(context);
            final NotificationCompat.Builder builder = helper.getNotification(NotificationHelper.IMPORTANCE_LOW)
                    .setSmallIcon(R.mipmap.ic_launcher_small)
                    .setContentTitle(context.getString(R.string.version_update_downloading))
                    .setOngoing(true)
                    .setAutoCancel(false);
            while (mIsDownloadingFile) {
                handler.post(() -> {
                    builder.setProgress(100, mPercent, false);
                    builder.setContentText(mPercent + "%");
                    helper.notify(notificationID, builder);
                });
                SystemClock.sleep(100);
            }
            helper.cancel(notificationID);
        };
        AppExecutors.getInstance().networkIO().execute(runnable);
    }

    public void startDownload(final Context context) {
        final long fileSize = SPUtils.getInstance().getLong(AppPreference.KEY_SERVER_LASTEST_VERSION_APK_SIZE, 0);
        // 判断有无SDCard
        if (!StorageUtil.checkSdcard()) {
            Toast.makeText(context, context.getResources().getString(R.string.update_insert_sdcard), Toast.LENGTH_SHORT).show();
            return;
        }

        // 判断SDCard空间大小
        if (!StorageUtil.isSDCardAvailableBlocks(fileSize)) {
            Toast.makeText(context, context.getResources().getString(R.string.update_sdcard_size_not_enough), Toast.LENGTH_SHORT).show();
            return;
        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!mIsDownloadingFile) {
                    //downLoadFile(context);
                    downloadApkFile(context);
                }
            }
        });
    }

    /**
     * 下载apk文件
     *
     * @param context 上下文
     */
    public void downloadApkFile(final Context context) {
        try {
            mIsDownloadingFile = true;
            new Handler(Looper.getMainLooper()).post(() -> showDownloadingNotification(context));

            // 创建文件夹
            File dowloadDirFilePath = new File(Environment.getExternalStorageDirectory() + DOWNLOAD_DIR_PATH);
            if (!dowloadDirFilePath.isDirectory()) {
                dowloadDirFilePath.delete();
            }
            // 判断有无文件夹
            if (!dowloadDirFilePath.exists()) {
                dowloadDirFilePath.mkdirs();
            }

            long serverSize = SPUtils.getInstance().getLong(AppPreference.KEY_SERVER_LASTEST_VERSION_APK_SIZE, 0);
            // 是否下载标记
            boolean isDownload = true;

            // 创建文件
            String upversionName = getFullVersionName(FILE_PREFIX, mClientVersionLatest) + ".apk";
            File file = new File(dowloadDirFilePath.getAbsolutePath(), upversionName);
            // 判断是否存在文件
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                mCurrentFileSize = fileInputStream.available();
                fileInputStream.close();
                if (serverSize == mCurrentFileSize) {
//                    mDownloadPb.cancel();
                    mIsDownloadingFile = false;
                    isDownload = false;
                }
            } else {
                mCurrentFileSize = 0;
                mPercent = 0;
                file.createNewFile();
            }

            if (isDownload) {
                mInputStream = null;
                mFileOutputStream = null;
                String appFileUrl = AppConfig.getUpgradeXmlUrl() + getFullVersionName(FILE_PREFIX, mClientVersionLatest) + ".apk";
                URL url = new URL(appFileUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("User-Agent", "NetFox");
                String property = "bytes=" + mCurrentFileSize + "-";
                httpURLConnection.setRequestProperty("RANGE", property);
                mInputStream = httpURLConnection.getInputStream();
                /* 将文件写入暂存盘 */
                mFileOutputStream = new FileOutputStream(file, true);
                byte[] bytes = new byte[1024 * 4];

                do {
                    int liReadLength = 0;
                    synchronized (mInputStream) {
                        if (mIsDownloadingFile) {
                            liReadLength = mInputStream.read(bytes);
                        }
                    }
                    if (liReadLength <= 0) {
                        mIsDownloadingFile = false;
                        break;
                    }
                    mCurrentFileSize += liReadLength;
                    double percent = ((double) mCurrentFileSize) / ((double) serverSize);
                    mPercent = (int) (percent * 100);
                    synchronized (mFileOutputStream) {
                        if (mIsDownloadingFile) {
                            mFileOutputStream.write(bytes, 0, liReadLength);
                        }
                    }
                    SystemClock.sleep(10);
                } while (mIsDownloadingFile);
                mFileOutputStream.flush();
            }

            if (mCurrentFileSize > serverSize) {
                file.delete();
                mPercent = 0;
                mCurrentFileSize = 0;
            } else if (mCurrentFileSize == serverSize) {
                mCurrentFileSize = 0;
                String md5 = MD5Util.md5(file);
                if (SPUtils.getInstance().getString(AppPreference.KEY_SERVER_LASTEST_VERSION_MD5, "").length() > 0) {
                    String server = SPUtils.getInstance().getString(AppPreference.KEY_SERVER_LASTEST_VERSION_MD5, "");
                    if (server.equalsIgnoreCase(md5)) {
                        Logger.logD(Logger.COMMON, "文件MD5值一致 客户端MD5:" + md5 + ",服务器MD5:" + server);
                        Utils.install(context, file);
                    } else {
                        Logger.logI(Logger.COMMON, "文件MD5值不一致客户端MD5:" + md5 + ",服务器MD5:" + server);
                        file.delete();
                        mPercent = 0;
                        mCurrentFileSize = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mIsDownloadingFile) {
                try {
                    if (mFileOutputStream != null) {
                        synchronized (mFileOutputStream) {
                            try {
                                if (mFileOutputStream != null) {
                                    mFileOutputStream.close();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (mInputStream != null) {
                        synchronized (mInputStream) {
                            try {
                                if (mInputStream != null) {
                                    mInputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mIsDownloadingFile = false;
            }
        }
    }
}
