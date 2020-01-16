package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 上传图片适配器
 * <p>
 * Created by WangHaiKun on 2017/10/9.
 * Mod by biao on 2019/11/30
 */
public class UploadPictureAdapter extends BaseQuickAdapter<MaterialFileInfo, BaseViewHolder> {
    private Map<Integer, PatientInfo> mPatientInfos = new HashMap<>();
    private OnItemRemoveClickListener mOnItemRemoveClickListener;
    private OnItemUploadFailClickListener mOnItemUploadFailClickListener;
    private OnItemPauseClickListener mOnItemPauseClickListener;
    private OnItemRecoveryPauseClickListener mOnItemRecoveryPauseClickListener;

    public UploadPictureAdapter(@LayoutRes int layoutResId, @Nullable List<MaterialFileInfo> memberListInfos) {
        super(layoutResId, memberListInfos);
    }

    public void setOnItemRemoveClickListener(OnItemRemoveClickListener Listener) {
        mOnItemRemoveClickListener = Listener;
    }

    public void setOnItemUploadFailClickListener(OnItemUploadFailClickListener Listener) {
        mOnItemUploadFailClickListener = Listener;
    }

    public void setOnItemPauseClickListener(OnItemPauseClickListener Listener) {
        mOnItemPauseClickListener = Listener;
    }

    public void setOnItemRecoveryPauseClickListener(OnItemRecoveryPauseClickListener Listener) {
        mOnItemRecoveryPauseClickListener = Listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialFileInfo item) {
        int position = helper.getAdapterPosition();
        ImageView mItemImg = helper.getView(R.id.upload_item_img);
        TextView mItemAppointmentId = helper.getView(R.id.upload_item_appointment_id);
        TextView mItemName = helper.getView(R.id.upload_item_name);
        TextView mItemUploadType = helper.getView(R.id.upload_item_upload_type);
        ImageView mItemTypeImg = helper.getView(R.id.upload_item_type_img);
        ImageView mItemRemove = helper.getView(R.id.upload_item_remove);
        ProgressBar mProgressBar = helper.getView(R.id.upload_item_progress_bar);
        View mShortView = helper.getView(R.id.upload_item_short_view);
        View mLongView = helper.getView(R.id.upload_item_long_view);

        GlideUtils.showImage(mItemImg, mContext, item.getLocalFilePath());
        mItemAppointmentId.setText(item.getAppointmentId() + "");
        mItemRemove.setImageResource(R.drawable.bg_upload_close);
        mProgressBar.setProgress(item.getProgress());
        if (getItemCount() == position + 1) {
            mShortView.setVisibility(View.GONE);
            mLongView.setVisibility(View.VISIBLE);
        } else {
            mShortView.setVisibility(View.VISIBLE);
            mLongView.setVisibility(View.GONE);
        }
        if (item.getAppointmentId() >= 500000) {
            RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
                @Override
                public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                    Logger.logI(Logger.APPOINTMENT, "baseResult:" + baseResult + ",roundsMedicalDetailsInfo：" + roundsMedicalDetailsInfo);
                    mItemName.setText(roundsMedicalDetailsInfo.getPatientName());
                }
            });
            requester.setMedicalId(item.getAppointmentId());
            requester.doPost();
        } else {
            if (mPatientInfos.containsKey(item.getAppointmentId())) {
                mItemName.setText(mPatientInfos.get(item.getAppointmentId()).getPatientBaseInfo().getRealName());
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(item.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfos.put(item.getAppointmentId(), patientInfo);
                            mItemName.setText(patientInfo.getPatientBaseInfo().getRealName());
                        }
                    }

                    @Override
                    public void onFailed(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        }
        switch (item.getState()) {
            case UploadState.NOT_UPLOADED:
                mItemUploadType.setText(mContext.getString(R.string.upload_line_up));
                mItemUploadType.setTextColor(ContextCompat.getColor(mContext, R.color.color_45aef8));
                mItemTypeImg.setImageResource(R.drawable.ic_upload_wait);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case UploadState.UPLOADING:
                mItemUploadType.setText(mContext.getString(R.string.upload_ing));
                mItemUploadType.setTextColor(ContextCompat.getColor(mContext, R.color.color_45aef8));
                mItemTypeImg.setImageResource(R.drawable.ic_upload_ing);
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case UploadState.UPLOAD_SUCCESS:
                mItemUploadType.setText(mContext.getString(R.string.data_upload_success));
                mItemUploadType.setTextColor(ContextCompat.getColor(mContext, R.color.color_39d210));
                mItemTypeImg.setImageResource(R.drawable.ic_upload_success);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case UploadState.UPLOAD_FAILED:
                mItemUploadType.setText(mContext.getString(R.string.data_upload_fail));
                mItemUploadType.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff3a3a));
                mItemTypeImg.setImageResource(R.drawable.ic_upload_fail);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case UploadState.UPLOAD_PAUSE:
                mItemUploadType.setText(mContext.getString(R.string.upload_queue_pause));
                mItemUploadType.setTextColor(ContextCompat.getColor(mContext, R.color.color_45aef8));
                mItemTypeImg.setImageResource(R.drawable.ic_upload_pause);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        switch (item.getState()) {
            case UploadState.NOT_UPLOADED:
            case UploadState.UPLOADING:
                mItemTypeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemPauseClickListener != null) {
                            mOnItemPauseClickListener.onItemPauseClickListener(item, position);
                        }
                    }
                });
                break;

            case UploadState.UPLOAD_FAILED:
                mItemTypeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemUploadFailClickListener != null) {
                            mOnItemUploadFailClickListener.onItemUploadFailClickListener(item, position);
                        }
                    }
                });
                break;

            case UploadState.UPLOAD_PAUSE:
                mItemTypeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemRecoveryPauseClickListener != null) {
                            mOnItemRecoveryPauseClickListener.onItemRecoveryPauseClickListener(item, position);
                        }
                    }
                });
                break;

            case UploadState.UPLOAD_SUCCESS:
                mItemTypeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //拦截事件
                    }
                });
                break;
            default:
                break;
        }
        mItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemRemoveClickListener != null) {
                    mOnItemRemoveClickListener.onItemRemoveClickListener(item, position);
                }
            }
        });
    }

    public interface OnItemRemoveClickListener {
        void onItemRemoveClickListener(MaterialFileInfo item, int position);
    }

    public interface OnItemUploadFailClickListener {
        void onItemUploadFailClickListener(MaterialFileInfo materialFileInfo, int position);
    }

    public interface OnItemRecoveryPauseClickListener {
        void onItemRecoveryPauseClickListener(MaterialFileInfo materialFileInfo, int position);
    }

    public interface OnItemPauseClickListener {
        void onItemPauseClickListener(MaterialFileInfo materialFileInfo, int position);
    }
}
