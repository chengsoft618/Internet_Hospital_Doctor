package cn.longmaster.hospital.doctor.core.upload;

import java.util.Random;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.common.OsUtil;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialFileInfo;

/**
 * 上传工具类
 * Created by Tengshuxiang on 2016-08-15.
 */
public class UploadUtils {
    public static String applyTaskId() {
        return MD5Util.md5(OsUtil.getIMEI() + new Random(System.currentTimeMillis()).nextLong());
    }

    public static String applySessionId(SingleFileInfo file) {
        return MD5Util.md5(file.getLocalFilePath() + applyTaskId());
    }

    public static String applySessionId(MaterialFileInfo file) {
        return MD5Util.md5(file.getLocalFilePath() + applyTaskId());
    }
}
