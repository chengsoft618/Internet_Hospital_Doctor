package cn.longmaster.hospital.doctor.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.DisplayUtil;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author Mloong_Abiao
 * @date 2019/6/5 15:29
 * @description:
 */
public class GlideUtils {
    public static void showHospitalLogo(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.mipmap.ic_hospital_head_default)
                .placeholder(R.mipmap.ic_hospital_head_default)
                .into(iv);
    }

    public static void showDoctorAvatar(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_doctor_default_head)
                .placeholder(R.mipmap.ic_doctor_default_head)
                .into(iv);
    }

    public static void showDoctorChatAvatar(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_chat_default_doctor_avatar)
                .placeholder(R.drawable.ic_chat_default_doctor_avatar)
                .into(iv);
    }

    public static void showDoctorOnLineAvatar(ImageView iv, Context ct, String url) {
        if (null == ct) {
            return;
        }
        Glide.with(ct).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_video_doctor_avatar_online)
                .placeholder(R.drawable.ic_video_doctor_avatar_online)
                .into(iv);
    }

    public static void showPatientChatAvatar(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_chat_default_patient_avatar)
                .placeholder(R.drawable.ic_chat_default_patient_avatar)
                .into(iv);
    }

    public static void showPatientOnLineAvatar(ImageView iv, Context ct, String url) {
        if (null == ct) {
            return;
        }
        Glide.with(ct).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_video_patient_avatar_online)
                .placeholder(R.drawable.ic_video_patient_avatar_online)
                .into(iv);
    }

    public static void showBannerView(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(DisplayUtil.dip2px(4), 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(iv);
    }

    /**
     * 显示医学院视频
     *
     * @param iv
     * @param context
     * @param url
     */
    public static void showCollegeCoverMap(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .error(R.drawable.ic_college_cover_map)
                .placeholder(R.drawable.ic_college_cover_map)
                .into(iv);
    }

    public static void showOrderMedicalRecordAdapterView(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(4, 0,
                        RoundedCornersTransformation.CornerType.BOTTOM)))
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showIssueDoctorOrderAdapterView(ImageView mImageView, Context context, String picPath) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(picPath)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(4, 0,
                        RoundedCornersTransformation.CornerType.BOTTOM)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mImageView);
    }

    public static void showChatBigImage(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showImage(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showImage(ImageView iv, Context context, Integer resourceId) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showImage(ImageView iv, Context context, String resourceId, RequestListener listener) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .listener(listener)
                .into(iv);
    }

    public static void showBrowserImage(ImageView iv, Context context, String url) {
        if (null == context) {
            return;
        }
        Glide.with(context).load(url)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_loading_pic)
                .into(iv);
    }


    public static void showViewIssueDoctorBannerView(Activity context, ImageView iv, String url) {
        if (isActivityDestory(context)) {
            return;
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showImage(ImageView iv, Activity context, String url) {
        if (isActivityDestory(context)) {
            return;
        }
        Glide.with(context).load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    public static void showImage(ImageView iv, Activity context, Integer resourceId) {
        if (isActivityDestory(context)) {
            return;
        }
        Glide.with(context).load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .into(iv);
    }

    private static boolean isActivityDestory(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}
