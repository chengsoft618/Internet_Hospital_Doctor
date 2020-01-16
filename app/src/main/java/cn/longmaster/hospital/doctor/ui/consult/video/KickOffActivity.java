package cn.longmaster.hospital.doctor.ui.consult.video;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.ui.BaseActivity;

/**
 * 被移出房间
 * Created by Y² on 2016-12-16.
 */
public class KickOffActivity extends BaseActivity implements MessageStateChangeListener {
    @FindViewById(R.id.activity_kick_off_content_tv)
    private TextView mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_off);
        ViewInjecter.inject(this);
        AppApplication.getInstance().getManager(MessageManager.class).regMsgStateChangeListener(this);
        initData();
    }

    private void initData() {
        boolean mIsMeetingRoom = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MEETING_ROOM, false);
        if (mIsMeetingRoom) {
            mContentTv.setText(getString(R.string.video_room_kick_off_meeting_room));
        } else {
            mContentTv.setText(getString(R.string.video_room_kick_off_tip));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppApplication.getInstance().getManager(MessageManager.class).unRegMsgStateChangeListener(this);
    }

    @OnClick({R.id.activity_kick_off_confirm_tv})
    public void onClick(View v) {
        if (v.getId() == R.id.activity_kick_off_confirm_tv) {
            finish();
        }
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_CALL ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_ASSISTANT_CALL) {
            finish();
        }
    }
}
