package cn.longmaster.utils;

/**
 * @author ABiao_Abiao
 * @date 2020/1/13 13:55
 * @description:
 */
public class AppUtils {
    public static void addOnAppStatusChangedListener(final Object object,
                                              final Utils.OnAppStatusChangedListener listener) {
        Utils.getActivityLifecycle().addOnAppStatusChangedListener(object, listener);
    }
}
