package cn.longmaster.hospital.doctor.core.manager.user;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import cn.longmaster.doctorlibrary.util.imageloader.ImageloadListener;
import cn.longmaster.doctorlibrary.util.imageloader.view.AsyncImageView;
import cn.longmaster.doctorlibrary.util.imageloader.view.CircledDrawable;
import cn.longmaster.doctorlibrary.util.imageloader.view.ImageProcesser;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;

/**
 * 头像管理类
 * Created by yangyong on 2015/7/24.
 */
public class AvatarManager extends BaseManager {
    public static final String VISUALIZE_AVATAR_SUFFIX = "_a";//形象照后缀
    private AppApplication mApplication;

    @Override
    public void onManagerCreate(AppApplication application) {
        mApplication = application;
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    /**
     * 默认拉取医生头像
     *
     * @param displayParams
     */
    public void displayAvatar(final DisplayParams displayParams) {
        displayAvatar(false, displayParams);
    }

    /**
     * 显示头像
     *
     * @param isAssistant   是否医生助理
     * @param displayParams
     */
    public void displayAvatar(boolean isAssistant, final DisplayParams displayParams) {
        Logger.logD(Logger.USER, TAG + "->displayAvatar()->displayParams:" + displayParams.toString());

        if (displayParams.getAvatarView() == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("必须设置显示头像控件！"));
            }
            return;
        }
        String filePath = SdManager.getInstance().getAppointAvatarFilePath(displayParams.getUserId() + "");
        String url;
        if (displayParams.isVisualize()) {//形象照
            filePath = SdManager.getInstance().getAppointAvatarFilePath(displayParams.getUserId() + "") + VISUALIZE_AVATAR_SUFFIX;
            url = AppConfig.getDfsUrl() + "3001/" + displayParams.getAvatarToken() + "/" + displayParams.getUserId() + VISUALIZE_AVATAR_SUFFIX + ".png";
        } else {
            if (isAssistant) {
                url = AppConfig.getDfsUrl() + "3009/" + displayParams.getAvatarToken() + "/" + displayParams.getAssistantName() + "/" + displayParams.getUserId();
            } else {
                url = AppConfig.getDfsUrl() + "3001/" + displayParams.getAvatarToken() + "/" + displayParams.getUserId() + ".png";
            }
        }

        Logger.logD(Logger.USER, TAG + "->displayAvatar()->头像是否为圆形:" + displayParams.isRound());
        displayParams.getAvatarView().setImageProcesser(new ImageProcesser() {
            @Override
            public Drawable onProcessImage(ImageloadListener.BitmapSource bitmapSource, Bitmap bitmap) {
                Drawable drawable;
                if (displayParams.isRound()) {
                    if (displayParams.getStrokeWidth() != 0) {
                        drawable = new CircledDrawable(bitmap, displayParams.isGrey, displayParams.getStrokeWidth(), displayParams.getStrokeColor());
                    } else {
                        drawable = new CircledDrawable(bitmap, displayParams.isGrey);
                    }
                } else {
                    drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                }
                return drawable;
            }
        });
        Logger.logD(Logger.USER, TAG + "->displayAvatar()->文件路径:" + filePath + "头像服务器地址:" + url);
        displayParams.getAvatarView().setImageLoadingDrawable(ContextCompat.getDrawable(mApplication, displayParams.getLoadingAvatar()));
        displayParams.getAvatarView().setImageLoadFailedDrawable(ContextCompat.getDrawable(mApplication, displayParams.getFailedAvatar()));
        displayParams.getAvatarView().setDiskCacheEnable(false);
        displayParams.getAvatarView().setMemoryCacheEnable(true);
        displayParams.getAvatarView().loadImage(filePath, url);
    }


    public static class DisplayParams {
        //需要显示的头像的用户id
        private int userId;
        //头像token
        private String avatarToken;
        //头像显示控件
        private AsyncImageView avatarView;
        //是否圆形头像
        private boolean isRound = false;
        //加载中的头像
        private int loadingAvatar = R.drawable.ic_doctor_default_avatar;
        //加载失败的头像
        private int failedAvatar = R.drawable.ic_doctor_default_avatar;
        //描边宽度
        private int strokeWidth;
        //描边颜色
        private int strokeColor;
        //是否形象照
        private boolean isVisualize = false;
        //是否做灰色处理
        private boolean isGrey = false;
        //医生助理头像名称
        private String assistantName;

        public DisplayParams setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public int getUserId() {
            return userId;
        }

        public DisplayParams setAvatarToken(String avatarToken) {
            if (avatarToken == null || "".equals(avatarToken)) {
                avatarToken = "0";
            }

            this.avatarToken = avatarToken;
            return this;
        }

        public String getAvatarToken() {
            return avatarToken;
        }

        public DisplayParams setAvatarView(AsyncImageView avatarView) {
            this.avatarView = avatarView;
            return this;
        }

        public AsyncImageView getAvatarView() {
            return avatarView;
        }

        public DisplayParams setIsRound(boolean isRound) {
            this.isRound = isRound;
            return this;
        }

        public boolean isRound() {
            return isRound;
        }

        public int getLoadingAvatar() {
            return loadingAvatar;
        }

        public DisplayParams setLoadingAvatar(int loadingAvatar) {
            this.loadingAvatar = loadingAvatar;
            return this;
        }

        public DisplayParams setFailedAvatar(int failedAvatar) {
            this.failedAvatar = failedAvatar;
            return this;
        }

        public int getFailedAvatar() {
            return failedAvatar;
        }

        public int getStrokeWidth() {
            return strokeWidth;
        }

        public DisplayParams setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public int getStrokeColor() {
            return strokeColor;
        }

        public DisplayParams setStrokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public boolean isVisualize() {
            return isVisualize;
        }

        public DisplayParams setIsVisualize(boolean isVisualize) {
            this.isVisualize = isVisualize;
            return this;
        }

        public DisplayParams setIsGrey(boolean isGrey) {
            this.isGrey = isGrey;
            return this;
        }

        public DisplayParams setAssistantName(String assistantName) {
            this.assistantName = assistantName;
            return this;
        }

        public String getAssistantName() {
            return assistantName;
        }

        @Override
        public String toString() {
            return "DisplayParams{" +
                    "userId=" + userId +
                    ", avatarToken='" + avatarToken + '\'' +
                    ", avatarView=" + avatarView +
                    ", isRound=" + isRound +
                    ", loadingAvatar=" + loadingAvatar +
                    ", failedAvatar=" + failedAvatar +
                    ", strokeWidth=" + strokeWidth +
                    ", strokeColor=" + strokeColor +
                    ", isVisualize=" + isVisualize +
                    ", isGrey=" + isGrey +
                    '}';
        }
    }
}
