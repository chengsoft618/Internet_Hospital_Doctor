package cn.longmaster.hospital.doctor.core.manager;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;

import java.net.MalformedURLException;
import java.net.URL;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/6/13.
 */

public class HTTPDNSManager extends BaseManager {
    private Application mApplication;
    private static HttpDnsService httpdns;
    public static final String accountID = "134847";
    public static final String secretKey = "9a5d2dd5ee46d51d98a34e2ec0236903";
    public static final String formalHost = "login.39hospital.com";
    public static final String testHost = "test.39hospital.com";
    public static final String releaseHost = "issue.39hospital.com";
    private static final String HTTP_SCHEMA = "http://";

    @Override

    public void onManagerCreate(AppApplication application) {
        mApplication = application;
        initHTTPDNS();
    }

    private void initHTTPDNS() {
        httpdns = HttpDns.getService(mApplication, accountID);
        httpdns.setExpiredIPEnabled(true);
    }

    public Long getLongDnsIp(String originalHostUrl) {
        Logger.logD(Logger.COMMON, "->HTTPDNSManager()->originalHost:" + originalHostUrl);
        String ip = getStringDnsIp(originalHostUrl);
        if (!StringUtils.isEmpty(ip)) {
            Long IP = ipToNumber(numberToIp(ipToNumber(ip)));
            return IP;
        }
        return 0L;
    }

    public String getStringDnsIp(String originalHostUrl) {
        String originalUrl = HTTP_SCHEMA + originalHostUrl;
        URL url = null;
        try {
            url = new URL(originalUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String ip = "";
        for (int i = 0; i < 4; i++) {
            ip = httpdns.getIpByHostAsync(url.getHost());
            Logger.logD(Logger.COMMON, "->HTTPDNSManager()->for->ip:" + ip);
            if (!StringUtils.isEmpty(ip)) {
                break;
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.logD(Logger.COMMON, "->HTTPDNSManager()->ip:" + ip);
        return ip;
    }

    /**
     * IP string转成long
     *
     * @param ip
     * @return
     */
    private Long ipToNumber(String ip) {
        Long ips = 0L;
        String[] numbers = ip.split("\\.");
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(numbers[i]);
        }
        return ips;
    }

    /**
     * IP long转 string
     *
     * @param number
     * @return
     */
    private String numberToIp(Long number) {
        String ip = "";
        for (int i = 3; i >= 0; i--) {
            ip += String.valueOf((number & 0xff));
            if (i != 0) {
                ip += ".";
            }
            number = number >> 8;
        }
        return ip;
    }

    /**
     * @param type               域名类型
     * @param url                域名
     * @param ip                 pes的ip
     * @param onInsertIpListener 插入监听
     */
    public void insertIpData(final int type, final String url, final long ip, final OnInsertIpListener onInsertIpListener) {
        final DatabaseTask<String> dbTask = new DatabaseTask<String>() {
            @Override
            public AsyncResult<String> runOnDBThread(AsyncResult<String> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    boolean isExist = false;
                    String sql = "SELECT * FROM " + HTTPDNSIPContract.HTTPDNSEntry.TABLE_NAME
                            + " WHERE " + HTTPDNSIPContract.HTTPDNSEntry.TYPE + "=? ";
                    String[] selectionArgs = new String[]{String.valueOf(type)};
                    Cursor cursor = writableDatabase.rawQuery(sql, selectionArgs);
                    if (cursor != null) {
                        int count = cursor.getCount();
                        cursor.close();
                        isExist = count > 0;
                        Logger.logD(Logger.USER, "SQLiteDatabase->Ip->查询 type 为" + type + "的账号 count:" + count + ";isExist:" + isExist);
                    }
                    ContentValues addValues = new ContentValues();
                    addValues.put(HTTPDNSIPContract.HTTPDNSEntry.TYPE, type);
                    addValues.put(HTTPDNSIPContract.HTTPDNSEntry.URL, url);
                    addValues.put(HTTPDNSIPContract.HTTPDNSEntry.IP, ip);

                    if (isExist) {
                        String whereClause = HTTPDNSIPContract.HTTPDNSEntry.TYPE + " =? ";
                        String[] whereArgs = new String[]{String.valueOf(type)};
                        long rows = writableDatabase.update(HTTPDNSIPContract.HTTPDNSEntry.TABLE_NAME, addValues, whereClause, whereArgs);
                        Logger.logD(Logger.USER, "SQLiteDatabase->Ip->更新type  为" + type + "的 update rows:" + rows);
                    } else {
                        long rows = writableDatabase.insert(HTTPDNSIPContract.HTTPDNSEntry.TABLE_NAME, null, addValues);
                        Logger.logD(Logger.USER, "SQLiteDatabase->Ip->插入信息 type 为" + type + "的 insert rowID:" + rows);
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
            public void runOnUIThread(AsyncResult<String> asyncResult) {
                Logger.logD(Logger.COMMON, TAG + "#insertIpData#runOnUIThread:" + asyncResult.getData());
                if (onInsertIpListener != null) {
                    onInsertIpListener.onInsertIpListener(asyncResult.getData());
                }
            }
        };
        getManager(DBManager.class).submitDatabaseTask(dbTask);
    }

    /**
     * @param type                域名类型
     * @param onHttpDnsIpListener 获取DNS监听
     */
    public void getHttpDnsUrl(final int type, final OnHttpDnsIpListener onHttpDnsIpListener) {
        DatabaseTask<String> dbTask = new DatabaseTask<String>() {
            @Override
            public AsyncResult<String> runOnDBThread(AsyncResult<String> asyncResult, DBHelper dbHelper) {
                asyncResult.setData("");
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                String sql = "SELECT * FROM " + HTTPDNSIPContract.HTTPDNSEntry.TABLE_NAME + " WHERE " + HTTPDNSIPContract.HTTPDNSEntry.TYPE + "=? ";
                writableDatabase.beginTransaction();
                try {
                    cursor = writableDatabase.rawQuery(sql, new String[]{String.valueOf(type)});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            asyncResult.setData(cursor.getString(cursor.getColumnIndex(HTTPDNSIPContract.HTTPDNSEntry.URL)));
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
            public void runOnUIThread(AsyncResult<String> asyncResult) {
                Logger.logD(Logger.USER, TAG + "#getHttpDnsUrl#runOnUIThread:" + asyncResult.getData());
                String data = asyncResult.getData();
                if (onHttpDnsIpListener != null) {
                    onHttpDnsIpListener.onHttpDnsIpListener(data);
                }
            }
        };
        getManager(DBManager.class).submitDatabaseTask(dbTask);
    }

    public interface OnInsertIpListener {
        void onInsertIpListener(String url);
    }

    public interface OnHttpDnsIpListener {
        void onHttpDnsIpListener(String url);
    }
}
