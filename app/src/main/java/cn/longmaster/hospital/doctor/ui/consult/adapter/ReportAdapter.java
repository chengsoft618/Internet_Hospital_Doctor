package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lmmedia.PPAmrPlayer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisAllInfo;
import cn.longmaster.hospital.doctor.core.manager.VoiceDownloader;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.util.ConsultUtil;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 会诊意见Adapter
 * Created by Yang² on 2017/7/20.
 */

public class ReportAdapter extends BaseQuickAdapter<DoctorDiagnosisAllInfo, BaseViewHolder> {
    private PPAmrPlayer mPlayer;
    private View lastPlayView = null;
    private final int TYPE_OF_PIC = 0;
    private final int TYPE_OF_VIOCE = 1;
    private final int TYPE_OF_TXT = 2;

    public ReportAdapter(int layoutResId, @Nullable List<DoctorDiagnosisAllInfo> data, PPAmrPlayer ppAmrPlayer) {
        super(layoutResId, data);
        mPlayer = ppAmrPlayer;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorDiagnosisAllInfo item) {
        int position = helper.getLayoutPosition();
        switch (getFileType(item.getFileType())) {
            case TYPE_OF_PIC:
                helper.setGone(R.id.item_consult_report_voice_ll, false);
                helper.setGone(R.id.item_consult_report_txt_ll, false);
                helper.setVisible(R.id.item_consult_report_pic_ll, true);
                ImageView picture = helper.getView(R.id.item_report_picture_iv);
                GlideUtils.showImage(picture, mContext, AppConfig.getDfsUrl() + "3010/" + "0/" + item.getDiagnosisPicture() + "/" + item.getAppointmentId());
                helper.addOnClickListener(R.id.item_report_picture_iv);
                if (position != 0 && showLine(getData().get(position - 1))) {
                    helper.setVisible(R.id.item_report_picture_line_view, true);
                } else {
                    helper.setGone(R.id.item_report_picture_line_view, false);
                }
                break;
            case TYPE_OF_VIOCE:
                helper.setVisible(R.id.item_consult_report_voice_ll, true);
                helper.setGone(R.id.item_consult_report_txt_ll, false);
                helper.setGone(R.id.item_consult_report_pic_ll, false);
                helper.setText(R.id.item_report_voice_time_tv, ConsultUtil.formatSecond(item.getAudioTime()));
                if (position != 0 && showLine(getData().get(position - 1))) {
                    helper.setVisible(R.id.item_report_voice_line_view, true);
                } else {
                    helper.setGone(R.id.item_report_voice_line_view, false);
                }
                LinearLayout itemReportVoiceLayoutLl = helper.getView(R.id.item_report_voice_layout_ll);
                itemReportVoiceLayoutLl.setOnClickListener(v -> dealVoice(item, itemReportVoiceLayoutLl));
                break;
            case TYPE_OF_TXT:
                helper.setGone(R.id.item_consult_report_voice_ll, false);
                helper.setGone(R.id.item_consult_report_pic_ll, false);
                helper.setVisible(R.id.item_consult_report_txt_ll, true);
                helper.setText(R.id.item_report_text_tv, item.getContent());
                if (position != 0 && showLine(getData().get(position - 1))) {
                    helper.setVisible(R.id.item_report_text_line_view, true);
                } else {
                    helper.setGone(R.id.item_report_text_line_view, false);
                }
                break;
            default:
                break;
        }
    }

    private int getFileType(int type) {
        switch (type) {
            //添加图片医嘱
            case AppConstant.DiagnosisFileType.DIAGNOSIS_CONTENT_TYPE_PICTURE:
                //病历截图
            case AppConstant.DiagnosisFileType.DIAGNOSIS_CONTENT_TYPE_SCREENSHOT:
                return TYPE_OF_PIC;
            case AppConstant.DiagnosisFileType.DIAGNOSIS_CONTENT_TYPE_VOICE:
                return TYPE_OF_VIOCE;
            default:
                return TYPE_OF_TXT;
        }
    }

    private boolean showLine(DoctorDiagnosisAllInfo info) {
        return info.getFileType() != AppConstant.DiagnosisFileType.DIAGNOSIS_CONTENT_TYPE_PICTURE &&
                info.getFileType() != AppConstant.DiagnosisFileType.DIAGNOSIS_CONTENT_TYPE_SCREENSHOT;
    }

    /**
     * 播放语音
     *
     * @param doctorDiagnosisAllInfo
     * @param voiceLayout
     */
    private void dealVoice(DoctorDiagnosisAllInfo doctorDiagnosisAllInfo, final View voiceLayout) {
        //测试语音文件："201608.12.16-27-04_1470990424189711857076.amr";
        final String filePath = SdManager.getInstance().getOrderVoicePath(doctorDiagnosisAllInfo.getDiagnosisPicture(), doctorDiagnosisAllInfo.getAppointmentId() + "");
        if (FileUtil.isFileExist(filePath)) {
            Logger.logI(Logger.APPOINTMENT, "语音文件本地路径：" + filePath);
            if (!mPlayer.isPlaying()) {
                Logger.logI(Logger.APPOINTMENT, "之前无语音播放");
                mPlayer.setDataSource(filePath);
                mPlayer.start();
                toggleVoiceAnimation(voiceLayout, true);
                lastPlayView = voiceLayout;
            } else {
                Logger.logI(Logger.APPOINTMENT, "之前正在播放语音");
                if (lastPlayView == voiceLayout) {
                    Logger.logI(Logger.APPOINTMENT, "停止之前播放语音");
                    toggleVoiceAnimation(lastPlayView, false);
                    mPlayer.stop();
                } else {
                    Logger.logI(Logger.APPOINTMENT, "停止之前播放语音");
                    toggleVoiceAnimation(lastPlayView, false);
                    mPlayer.stop();
                    Logger.logI(Logger.APPOINTMENT, "开始播放新语音");
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mPlayer.setDataSource(filePath);
                    mPlayer.start();
                    toggleVoiceAnimation(voiceLayout, true);
                    lastPlayView = voiceLayout;
                }
            }
        } else {
            Logger.logI(Logger.APPOINTMENT, "本地无语音文件");
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                if (lastPlayView != null) {
                    toggleVoiceAnimation(lastPlayView, false);
                }
            }

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            new VoiceDownloader(fileName, doctorDiagnosisAllInfo.getAppointmentId(), () -> {
                mPlayer.setDataSource(filePath);
                mPlayer.start();
                AppHandlerProxy.runOnUIThread(() -> {
                    toggleVoiceAnimation(voiceLayout, true);
                    lastPlayView = voiceLayout;
                });
            }).start();
        }

        mPlayer.setOnStateListener(new PPAmrPlayer.OnStateListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onStop(String s) {
                if (TextUtils.equals(s, filePath)) {
                    toggleVoiceAnimation(voiceLayout, false);
                }
            }

            @Override
            public void onError(String s) {

            }
        });

    }

    private void toggleVoiceAnimation(View container, boolean isOpen) {
        ImageView mVoiceIconIv = container.findViewById(R.id.item_report_voice_icon_iv);
        if (isOpen) {
            mVoiceIconIv.setImageResource(R.drawable.ic_animation_voice);
            AnimationDrawable animDrawable = (AnimationDrawable) mVoiceIconIv.getDrawable();
            animDrawable.start();
        } else {
            mVoiceIconIv.setImageResource(R.drawable.ic_voice_high);
        }
    }

}
