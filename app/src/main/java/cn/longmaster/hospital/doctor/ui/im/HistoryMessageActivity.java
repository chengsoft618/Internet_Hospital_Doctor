package cn.longmaster.hospital.doctor.ui.im;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatMsgInfo;
import cn.longmaster.hospital.doctor.core.entity.im.PictureGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatHistoryList;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.DisplayUtil;

/**
 * 历史消息界面
 * Created by YY on 17/9/6.
 */

public class HistoryMessageActivity extends BaseActivity implements ChatListAdapter.OnPictureGroupMessageClickListener {
    private final String TAG = HistoryMessageActivity.class.getSimpleName();
    private final int PAGE_SIZE = 30;

    @FindViewById(R.id.history_message_list_refresh)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @FindViewById(R.id.history_message_list)
    private ListView mMessageListView;
    @AppApplication.Manager
    private GroupMessageManager mMessageManager;

    private PopupWindow mPopupWindow;
    private boolean mBigPicture;
    private String mPhotoFileName;
    private ProgressDialog mProgressDialog;

    private ChatListAdapter mChatListAdapter;
    private int mAppointmentId;
    private int mGroupId;
    private List<BaseGroupMessageInfo> mBaseGroupMessageInfos = new ArrayList<>();
    private int mMsgId = 0;
    private String mMsgDt = "";
    private int mIsFinish;//0：结束；1：未结束

    public static void startHistoryMessageActivity(Context context, int appointmentId, int groupId) {
        Intent intent = new Intent(context, HistoryMessageActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_message);
        ViewInjecter.inject(this);
        initData();
        initView();
        initAdapter();
        getChatHistoryList();
    }

    private void initData() {
        mAppointmentId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        mGroupId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_GROUP_ID, 0);
    }

    private void initView() {
        //手动调用,通知系统去测量
        mSwipeRefreshLayout.measure(0, 0);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initAdapter() {
        mChatListAdapter = new ChatListAdapter(this);
        mChatListAdapter.setData(mBaseGroupMessageInfos);
        mChatListAdapter.setOnPictureGroupMessageClickListener(this);
        mMessageListView.setAdapter(mChatListAdapter);
        View view = new View(this);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(5));
        view.setLayoutParams(layoutParams);
        mMessageListView.addFooterView(view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mIsFinish == 0) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } else {
                getChatHistoryList();
            }
        });
    }


    private void getChatHistoryList() {
        GetChatHistoryList getChatHistoryList = new GetChatHistoryList((baseResult, chatMsgList) -> {
            Logger.logI(Logger.IM, TAG + "-->getChatHistoryList-->chatMsgList-->" + chatMsgList);
            if (chatMsgList == null || chatMsgList.getChatMsgList().size() == 0) {
                return;
            }

            mMsgId = chatMsgList.getChatMsgList().get(chatMsgList.getChatMsgList().size() - 1).getMsgId();
            mMsgDt = chatMsgList.getChatMsgList().get(chatMsgList.getChatMsgList().size() - 1).getInsertDt();
            mIsFinish = chatMsgList.getIsFinish();
            mMessageListView.post(() -> {
                int scrollIndex = 0;
                for (ChatMsgInfo chatMsgInfo : chatMsgList.getChatMsgList()) {
                    if (chatMsgInfo.getMsgType() != BaseGroupMessageInfo.MEMBER_EXIT_MSG) {
                        scrollIndex++;
                        BaseGroupMessageInfo baseGroupMessageInfo = mMessageManager.getBaseGroupMessageInfo(chatMsgInfo);
                        if (baseGroupMessageInfo != null) {
                            mBaseGroupMessageInfos.add(0, baseGroupMessageInfo);
                        }
                    }
                }
                mChatListAdapter.setData(mBaseGroupMessageInfos);
                if (mBaseGroupMessageInfos.size() <= PAGE_SIZE) {
                    mMessageListView.setSelection(scrollIndex - 1);
                } else {
                    mMessageListView.setSelection(scrollIndex);
                }
            });
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        getChatHistoryList.groupId = mGroupId;
        getChatHistoryList.msgId = mMsgId;
        getChatHistoryList.msgDt = mMsgDt;
        getChatHistoryList.pageSize = PAGE_SIZE;
        getChatHistoryList.doPost();
    }

    @Override
    public void onPictureGroupMessageClicked(View v, final PictureGroupMessageInfo pictureGroupMessageInfo) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.big_pic_popwindow, null);
        ImageView bigImg = contentView.findViewById(R.id.pop_window_big_pic);
        ImageView keepPic = contentView.findViewById(R.id.pop_window_keep_pic);
        final RelativeLayout loadWait = contentView.findViewById(R.id.pop_window_load_wait);
        final LinearLayout saveSuccess = contentView.findViewById(R.id.pop_window_keep_success);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        loadWait.setVisibility(View.VISIBLE);
        String url = AppConfig.getImPictureDownloadUrl() + pictureGroupMessageInfo.getPictureName();
        GlideUtils.showImage(bigImg, getActivity(), url, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                loadWait.setVisibility(View.GONE);
                return false;
            }
        });

        loadWait.setOnClickListener(v1 -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        bigImg.setOnClickListener(v12 -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        keepPic.setOnClickListener(v13 -> {
            if (mBigPicture) {
                new Thread(new MyRunnable(AppConfig.getImPictureDownloadUrl() + pictureGroupMessageInfo.getPictureName())).start();
                saveSuccess.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> saveSuccess.setVisibility(View.GONE), 3000);
            }
        });
    }

    public class MyRunnable implements Runnable {
        private String mUrl;

        public MyRunnable(String url) {
            this.mUrl = url;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                try (InputStream inputStream = httpURLConnection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    savePictureToLocal(bitmap);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
