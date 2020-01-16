package cn.longmaster.doctorlibrary.util.anim;

import android.app.Activity;


/**
 * 动画工具类
 * Created by JinKe on 2016-01-19.
 */
public class AnimationUtil {
    /**
     * 退出Activity的动画 : zoom 动画
     *
     * @param activity 上下文
     */
    public static void finishActivityAnimation(Activity activity, int enterAnim, int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * zoom 动画
     *
     * @param activity 上下文
     */
    public static void activityChangeAnimation(Activity activity, int enterAnim, int exitAnim) {
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * zoom B返回A动画
     *
     * @param activity 上下文
     */
    public static void activityBackAnimation(Activity activity, int enterAnim, int exitAnim) {
        activity.overridePendingTransition(enterAnim, exitAnim);
    }
}
