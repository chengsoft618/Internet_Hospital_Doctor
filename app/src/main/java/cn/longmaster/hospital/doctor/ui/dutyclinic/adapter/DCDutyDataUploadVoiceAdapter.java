package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lmmedia.PPAmrPlayer;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.core.manager.im.AudioPlayManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;

/**
 * @author ABiao_Abiao
 * @date 2019/12/20 9:52
 * @description: 添加患者病程声音适配器
 */
public class DCDutyDataUploadVoiceAdapter extends BaseQuickAdapter<DCDutyPrognoteDataInfo, BaseViewHolder> {
    private AudioPlayManager audioPlayManager;

    public DCDutyDataUploadVoiceAdapter(int layoutResId, @Nullable List<DCDutyPrognoteDataInfo> data) {
        super(layoutResId, data);
        audioPlayManager = AppApplication.getInstance().getManager(AudioPlayManager.class);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyPrognoteDataInfo item) {
        helper.setText(R.id.item_dc_duty_data_up_load_voice_time_tv, getDuration(item.getDuration()));
        helper.addOnClickListener(R.id.item_dc_duty_data_up_load_voice_del_iv);
    }

    public void playVoice(int positon) {
        if (audioPlayManager.isPlaying()) {
            audioPlayManager.stopPlay();
        } else {
            audioPlayManager.startAudioPlay(SdManager.getInstance().getTempPath() + "msc/" + getData().get(positon).getMaterialPic(), AppConfig.getDfsUrl()+"3005/1/" + getData().get(positon).getMaterialPic(), new PPAmrPlayer.OnStateListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onStop(String s) {

                }

                @Override
                public void onError(String s) {

                }
            });
        }
//        ImageView mVoiceIconIv = (ImageView) getViewByPosition(positon, R.id.item_dc_duty_data_up_load_voice_iv);
//        if (null != mVoiceIconIv) {
//            if (mPlayer.isPlaying()) {
//                mVoiceIconIv.setImageResource(R.drawable.ic_animation_voice);
//                AnimationDrawable animDrawable = (AnimationDrawable) mVoiceIconIv.getDrawable();
//                animDrawable.start();
//            } else {
//                mVoiceIconIv.setImageResource(R.drawable.ic_voice_high);
//            }
//        }
    }

    private String getDuration(int dutation) {
        if (dutation < 60) {
            return dutation + "秒";
        }
        return dutation / 60 + "分" + dutation % 60;
    }
}
