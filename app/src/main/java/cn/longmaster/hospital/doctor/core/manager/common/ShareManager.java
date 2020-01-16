package cn.longmaster.hospital.doctor.core.manager.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.upload.bitmap.BitmapAsyncTask;
import cn.longmaster.utils.ToastUtils;


/**
 * 分享 管理类
 * Created by Yang² on 2017/9/5.
 */

public class ShareManager {
    public static final int ITEM_WEXIN = 1;
    public static final int ITEM_WEXIN_CIRCLE = 2;
    public static final int ITEM_QQ = 3;
    public static final int ITEM_MY_CONSULT = 4;
    public static final int ITEM_COPY_LINK = 5;
    public static final int ITEM_QR_CODE = 6;
    public static final int ITEM_SAVE_IMG = 7;

    private static ShareManager mShareManager;

    private QQ mQQ;
    private WeChat mWeChat;
    private static IWXAPI sIWXAPI;

    public ShareManager(Activity activity) {
        init();
        mShareManager = this;
    }

    private void init() {
        initWeChat();
        initQQ();
    }

    private void initWeChat() {
        mWeChat = new WeChat();
    }

    private void initQQ() {
        mQQ = new QQ();
        mQQ.setIUiListener(new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {
                ToastUtils.showShort(uiError.errorMessage);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void shareToWeiChat(Activity activity, ShareEntity data) {
        if (getWeChatApi().isWXAppInstalled()) {
            mWeChat.share(activity, data, true);
        } else {
            ToastUtils.showShort(R.string.share_wechat_not_installed);
        }
    }

    public void shareToWeiCircle(Activity activity, ShareEntity data) {
        if (getWeChatApi().isWXAppInstalled()) {
            mWeChat.share(activity, data, false);
        } else {
            ToastUtils.showShort(R.string.share_wechat_not_installed);
        }
    }

    public void shareToQq(Activity activity, ShareEntity data) {
        mQQ.share(activity, data, true);
    }

    public static IWXAPI getWeChatApi() {
        if (sIWXAPI == null) {
            sIWXAPI = WXAPIFactory.createWXAPI(AppApplication.getInstance(), WeChat.APP_ID, true);
            sIWXAPI.registerApp(WeChat.APP_ID);
        }
        return sIWXAPI;
    }

    public void shareFileToWeiXin(ShareEntity data) {
        mWeChat.shareFile(data);
    }

    public void shareFileToQq(Activity activity, ShareEntity data) {
        Intent share = new Intent(Intent.ACTION_SEND);
        ComponentName component = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        share.setComponent(component);
        File file = new File(data.getPath());
        System.out.println("file " + file.exists());
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        share.setType("*/*");
        activity.startActivity(Intent.createChooser(share, data.getTitle()));
    }

    public static String getWeChatOath2Url(String respCode) {
        return WeChat.URL_OAUTH2_GET + "?appid=" + WeChat.APP_ID + "&secret=" + WeChat.SECRET_KEY + "&code=" + respCode + "&grant_type=authorization_code";
    }

    private class QQ extends Open3rdPlatform {
        public static final String APP_ID = "1105322169";
        public static final String APP_KEY = "ckNQKbZ77feB5vOa";
//        public static final String APP_ID = "1104808011";
//        public static final String APP_KEY = "HOz6DsMDkzN1P1W7";

        private Tencent mTencent;
        private IUiListener mIUiListener;

        private QQ() {
            init();
        }

        private void init() {
            mTencent = Tencent.createInstance(APP_ID, AppApplication.getInstance());
        }

        public void setIUiListener(IUiListener iUiListener) {
            mIUiListener = iUiListener;
        }

        @Override
        public void share(Activity activity, ShareEntity data, boolean isDefaultWay) {
            super.share(activity, data, isDefaultWay);
            Bundle bundle = new Bundle();
            //这条分享消息被好友点击后的跳转URL。
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, data.getUrl());
            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, data.getTitle());
            //分享的消息摘要，最长50个字
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, data.getContent());
            if (!TextUtils.isEmpty(data.getImgUrl()) && data.getImgUrl().startsWith("http")) {
                Logger.logD(Logger.COMMON, "->onQqClick()->data.getImgUrl():" + data.getImgUrl());
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, data.getImgUrl());
            } else {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConfig.getAdwsUrl() + "/Public/images/triage/share-ico.png");
            }
            if (null != mTencent) {
                mTencent.shareToQQ(activity, bundle, mIUiListener);
            }
        }
    }

    private class WeChat extends Open3rdPlatform {
        public static final String APP_ID = "wx06179ec254e7a126";
        public static final String SECRET_KEY = "74b216ca9519e2df0d918a0a80ac9be9";
        public static final String URL_OAUTH2_GET = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String MERCHANT_KEY = "longmasterwxpay39longmasterwxpay";

        private IWXAPI mApi;
        private ShareEntity mData;
        private boolean mIsDefaultWay;

        private WeChat() {
            init();
        }

        private void init() {
            mApi = WXAPIFactory.createWXAPI(AppApplication.getInstance(), APP_ID, true);
            sIWXAPI = mApi;
            mApi.registerApp(APP_ID);
        }

        private String buildTransaction(final String type) {
            return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
        }

        //        public Bitmap getLocalBitmap(String localPath) {
//            File file = new File(localPath);
//            if (file.exists()) {
//                try {
//                    return BitmapFactory.decodeFile(localPath, getBitmapOption(2));
//                } catch (OutOfMemoryError error) {
//                    error.printStackTrace();
//                }
//            }
//            return BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher);
//        }
        public Bitmap getLocalBitmap(String localPath) {
            //分享的图片URL
            File img = new File("/sdcard/doctor/ic_share.png");
            File dir = img.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (img.exists()) {
                img.delete();
            }
            return BitmapFactory.decodeFile(localPath, getBitmapOption(2));
        }

        private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
            if (needRecycle) {
                bmp.recycle();
            }

            byte[] result = output.toByteArray();
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void share(final Activity activity, ShareEntity data, boolean isDefaultWay) {
            super.share(activity, data, isDefaultWay);
            this.mData = data;
            this.mIsDefaultWay = isDefaultWay;
            if (!TextUtils.isEmpty(data.getImgUrl())) {
                if (data.getImgUrl().startsWith("http")) {
                    new BitmapAsyncTask(data.getImgUrl(), new BitmapAsyncTask.OnBitmapListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            share(bitmap);
                        }

                        @Override
                        public void onException(Exception exception) {
                            share(activity);
                        }
                    }).execute();
                } else {
                    //本地图片
                    if (getLocalBitmap(data.getImgUrl()) != null) {
                        share(getLocalBitmap(data.getImgUrl()));
                    } else {
                        share(activity);
                    }
                }
            } else if (data.getDrawableId() != 0) {
                BitmapDrawable drawable = null;
                try {
                    drawable = (BitmapDrawable) ContextCompat.getDrawable(activity, data.getDrawableId());
                } catch (Exception ignored) {
                }
                if (null != drawable) {
                    share(drawable.getBitmap());
                } else {
                    share(activity);
                }
            } else {
                share(activity);
            }
        }

        private void share(Activity activity) {
            share(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher_small));
        }

        private void share(Bitmap bitmap) {
            WXWebpageObject object = new WXWebpageObject();
            object.webpageUrl = mData.getUrl();
            WXMediaMessage msg = new WXMediaMessage(object);
            msg.title = mData.getTitle();
            msg.description = mData.getContent();
            msg.thumbData = bmpToByteArray(BitmapUtil.getCompressBitmap(bitmap, 150, 150), true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = mIsDefaultWay ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            mApi.sendReq(req);
        }

        private void shareFile(ShareEntity data) {
            WXFileObject fileObj = new WXFileObject();
            fileObj.setContentLengthLimit(1024 * 1024 * 10);
            fileObj.setFilePath(data.getPath());//设置文件本地地址

            //使用媒体消息分享
            WXMediaMessage msg = new WXMediaMessage(fileObj);
            msg.mediaObject = fileObj;
            msg.title = data.getTitle();

            //发送请求
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            //创建唯一标识
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            mApi.sendReq(req);
        }
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private abstract class Open3rdPlatform {
        protected void share(Activity activity, ShareEntity data, boolean isDefaultWay) {
        }
    }
}
