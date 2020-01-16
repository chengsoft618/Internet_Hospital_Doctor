package cn.longmaster.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author Abiao
 * @version 0.01
 * @date 2018/10/16 15:49
 * @description: Toast工具
 * @since Version0.01
 */
public final class ToastUtils {

    private static Toast mToast;

    public static void showShort(final CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showShort(@StringRes int text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showLong(final CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showLong(@StringRes int text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showToast(@StringRes int text, int toastModel) {
        cancelToast();
        if (null != Utils.getTopActivityOrApp()) {
            mToast = Toast.makeText(Utils.getTopActivityOrApp(), text, toastModel);
            mToast.show();
        }
    }

    private static void showToast(final CharSequence text, int toastModel) {
        cancelToast();
        if (null != Utils.getTopActivityOrApp()) {
            mToast = Toast.makeText(Utils.getTopActivityOrApp(), text, toastModel);
            if (!TextUtils.isEmpty(text)) {
                mToast.show();
            }
        }
    }

    private static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
