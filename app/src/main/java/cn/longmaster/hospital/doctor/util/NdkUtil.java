package cn.longmaster.hospital.doctor.util;

/**
 * NDK 测试
 * Created by Yang² on 2018/1/12.
 */

public class NdkUtil {
    public native String getString(String a);

    static {
        System.loadLibrary("ndkutil");
    }
}
