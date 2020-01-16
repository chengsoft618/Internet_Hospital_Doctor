package cn.longmaster.doctorlibrary.util.common;

import android.os.Environment;
import android.os.StatFs;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * 存储管理工具类
 * Created by yangyong on 2015/7/13.
 */
public class StorageUtil {

    /**
     * 检查是否有Sdcard
     *
     * @return
     */
    public static boolean checkSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SDCard剩余空间
     *
     * @param fileLength
     * @return boolean
     */
    public static boolean isSDCardAvailableBlocks(long fileLength) {
        File path = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(path.getPath());
        long availaBlock = statfs.getAvailableBlocksLong();
        long blockSize = statfs.getBlockSizeLong();

        if (fileLength < (availaBlock * blockSize)) {
            return true;
        }
        return false;
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}