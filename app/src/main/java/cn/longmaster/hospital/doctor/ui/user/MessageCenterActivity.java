package cn.longmaster.hospital.doctor.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.account.MyAccountActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.consultant.RepresentFunctionActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.MessageCenterAdapter;
import cn.longmaster.hospital.doctor.view.IconView;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 查看全部待处理事项
 * 首頁点击查看全部进入
 * Created by Yang² on 2016/6/2.
 */
public class MessageCenterActivity extends NewBaseActivity implements MessageStateChangeListener {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.activity_message_center_message_null_icv)
    private IconView activityMessageCenterMessageNullIcv;
    @FindViewById(R.id.activity_message_center_rv)
    private RecyclerView activityMessageCenterRv;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private LocalNotificationManager mLocalNotificationManager;

    private MessageCenterAdapter mMessageListAdapter;

    private List<BaseMessageInfo> mMessageInfoList;

    @Override
    protected void onResume() {
        super.onResume();
        SPUtils.getInstance().put(AppPreference.KEY_MESSAGE_CENTER_NEW_MESSAGE + mUserInfoManager.getCurrentUserInfo().getUserId(), false);
    }

    @Override
    protected void initDatas() {
        mMessageInfoList = new ArrayList<>();
        mMessageManager.setIsInMessageCenter(true);
        mLocalNotificationManager.cancleAllNotification();
        mMessageListAdapter = new MessageCenterAdapter(R.layout.item_message, mMessageInfoList);
        mMessageListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BaseMessageInfo info = (BaseMessageInfo) adapter.getItem(position);
            if (info != null) {
                mMessageManager.updateMessageToRead(mUserInfoManager.getCurrentUserInfo().getUserId(), info.getMsgId());
                info.setMsgState(MessageProtocol.MSG_STATE_READED);
                try {
                    Intent intent = new Intent();
                    switch (info.getMsgType()) {
                        case MessageProtocol.SMS_TYPE_DOCTOR_39_ACTIVITY:
                            JSONObject json = new JSONObject(info.getMsgContent());
                            intent.setClass(MessageCenterActivity.this, BrowserActivity.class);
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, json.optString("lu"));
                            startActivity(intent);
                            break;
                        case MessageProtocol.SMS_TYPE_DOCTOR_BALANCE_CHANGE:
                            intent.setClass(MessageCenterActivity.this, MyAccountActivity.class);
                            startActivity(intent);
                            break;

                        case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS:
                        case MessageProtocol.SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS:
                        case MessageProtocol.SMS_TYPE_DOCTOR_FAST_DIAGNOSIS:
                        case MessageProtocol.SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH:
                        case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE:
                        case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE:
                            intent.setClass(MessageCenterActivity.this, RepresentFunctionActivity.class);
                            startActivity(intent);
                            break;

                        case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE:
                        case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH:
                        case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH:
                            intent.setClass(MessageCenterActivity.this, MainActivity.class);
                            intent.putExtra("rounds", true);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyItemChanged(position);
            }
        });
        mMessageListAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            BaseMessageInfo info = (BaseMessageInfo) adapter.getItem(position);
            if (info != null) {
                showDeleteDialog(info);
            }
            adapter.remove(position);
            return false;
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initViews() {
        activityMessageCenterRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityMessageCenterRv.setAdapter(mMessageListAdapter);
        tvToolBarTitle.setText("消息列表");
        tvToolBarSub.setText("全部已读");
        tvToolBarSub.setVisibility(View.VISIBLE);
        initListener();
        getAllMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessageManager.setIsInMessageCenter(false);
        mMessageManager.unRegMsgStateChangeListener(this);
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        tvToolBarSub.setOnClickListener(v -> {
            mMessageManager.updateAllMessageToRead(mUserInfoManager.getCurrentUserInfo().getUserId(), true);
            for (int i = 0; i < mMessageInfoList.size(); i++) {
                if (MessageProtocol.MSG_STATE_UNREAD == mMessageInfoList.get(i).getMsgState()) {
                    mMessageInfoList.get(i).setMsgState(MessageProtocol.MSG_STATE_READED);
                }
            }
            mMessageListAdapter.notifyDataSetChanged();
        });
        mMessageManager.regMsgStateChangeListener(this);
    }

    /**
     * 获取所有消息
     */
    private void getAllMessage() {
        mMessageManager.getAllMessage(mUserInfoManager.getCurrentUserInfo().getUserId(), true, baseMessageInfos -> {
            Logger.logI(Logger.COMMON, "getAllMessage-baseMessageInfos" + baseMessageInfos);
            if (LibCollections.isNotEmpty(baseMessageInfos)) {
                activityMessageCenterMessageNullIcv.setVisibility(View.GONE);
                activityMessageCenterRv.setVisibility(View.VISIBLE);
            } else {
                activityMessageCenterMessageNullIcv.setVisibility(View.VISIBLE);
                activityMessageCenterRv.setVisibility(View.GONE);
            }
            mMessageInfoList = baseMessageInfos;
            mMessageListAdapter.setNewData(mMessageInfoList);
        });
    }

    private void showDeleteDialog(final BaseMessageInfo info) {
        View layout = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_delete_message_dialog, null);
        final Dialog dialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window win = dialog.getWindow();
        if (null != win) {
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            TextView mDelete = (TextView) layout.findViewById(R.id.layout_delete_message_text_tv);
            mDelete.setOnClickListener(v -> {
                mMessageManager.deleteMessage(mUserInfoManager.getCurrentUserInfo().getUserId(), info.getMsgId());
                ToastUtils.showShort(R.string.data_delete_success);
                dialog.dismiss();
            });
        }
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        Logger.logD(Logger.ROOM, "MessageCenterActivity-->onNewMessage()->baseMessageInfo:" + baseMessageInfo);
        switch (baseMessageInfo.getMsgType()) {
            case MessageProtocol.SMS_TYPE_DOCTOR_BALANCE_CHANGE:
            case MessageProtocol.SMS_TYPE_DOCTOR_APPOINTMENT_TODAY:
            case MessageProtocol.SMS_TYPE_DOCTOR_39_ACTIVITY:
            case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS:
            case MessageProtocol.SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS:
            case MessageProtocol.SMS_TYPE_DOCTOR_FAST_DIAGNOSIS:
            case MessageProtocol.SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH:
            case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE:
            case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH:
            case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH:
            case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE:
            case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE:
            case MessageProtocol.SMS_TYPE_MATERIAL_UPLOAD_NOTE:
                mMessageInfoList.add(0, baseMessageInfo);
                Collections.sort(mMessageInfoList);
                if (LibCollections.isEmpty(mMessageInfoList)) {
                    activityMessageCenterRv.setVisibility(View.GONE);
                    activityMessageCenterMessageNullIcv.setVisibility(View.VISIBLE);
                } else {
                    activityMessageCenterMessageNullIcv.setVisibility(View.GONE);
                    activityMessageCenterRv.setVisibility(View.VISIBLE);
                }
                mMessageListAdapter.setNewData(mMessageInfoList);
                break;
            default:
                break;
        }
    }
}
