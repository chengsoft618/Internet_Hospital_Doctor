package cn.longmaster.hospital.doctor.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.LoginActivity;
import cn.longmaster.utils.SPUtils;

public class KickOffDialog extends BaseActivity {
    @FindViewById(R.id.layout_kickoff_dialog_title_tv)
    private TextView mTitle;
    @FindViewById(R.id.layout_kickoff_dialog_message_tv)
    private TextView mMessage;
    @FindViewById(R.id.layout_kickoff_dialog_positive_btn)
    private Button mPosiButton;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;

    private boolean popToLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kickoff_dialog);
        ViewInjecter.inject(this);
        initData();
        initView();
        regListener();
    }

    private void initData() {
        popToLogin = getIntent().getBooleanExtra("pop_to_login", true);
    }

    private void initView() {
        mTitle.setText(R.string.title_dialog_tip);
        mMessage.setText(R.string.account_login_at_other_place_tip);
    }

    private void regListener() {
        mPosiButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put(AppPreference.FLAG_BACKGROUND_KICKOFF, false);
                if (popToLogin) {
                    if (AppApplication.getInstance().isEnterVideoRoom()) {
                        Intent intent = new Intent();
                        intent.setAction(ExtraDataKeyConfig.ACTION_BROADCAST_RECEIVER_FINISH_VIDEO_ROOM);
                        sendBroadcast(intent);
                    }
                    Intent intent = new Intent(KickOffDialog.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
