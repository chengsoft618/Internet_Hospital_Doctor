package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyPicInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * Created by W·H·K on 2018/7/6.
 * Mod by Biao on 2019/7/8
 */

public class FirstCourseMedicalAdapter extends BaseAdapter {
    private Context mContext;
    private List<FirstJourneyPicInfo> list;
    private LayoutInflater layoutInflater;
    private boolean mIsIssueDoctorOrder = false;
    private OnPicItemClickListener mOnPicItemClickListener;
    private OnRetryUploadClickListener mOnRetryUploadClickListener;
    private OnDeletePicClickListener mOnDeletePicClickListener;


    public FirstCourseMedicalAdapter(Context context, List<FirstJourneyPicInfo> list) {
        this.mContext = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnPicItemClickListener(OnPicItemClickListener listener) {
        mOnPicItemClickListener = listener;
    }

    public void setOnRetryUploadClickListener(OnRetryUploadClickListener listener) {
        mOnRetryUploadClickListener = listener;
    }

    public void setOnDeletePicClickListener(OnDeletePicClickListener listener) {
        mOnDeletePicClickListener = listener;
    }

    public void setIssueDoctorOrder(boolean issue) {
        mIsIssueDoctorOrder = issue;
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_first_course_medical, null);
            vh = new ViewHolder();
            vh.mItemView = (LinearLayout) convertView.findViewById(R.id.item_v);
            vh.mImageView = (ImageView) convertView.findViewById(R.id.item_iv);
            vh.mUploadSuccessView = (LinearLayout) convertView.findViewById(R.id.item_upload_success_view);
            vh.mUploadFailView = (RelativeLayout) convertView.findViewById(R.id.item_upload_fail_view);
            vh.mUploadIngView = (RelativeLayout) convertView.findViewById(R.id.item_upload_ing_view);
            vh.mRoundProgressBar = (ProgressBar) convertView.findViewById(R.id.item_upload_media_progress_bar);
            vh.mDeleteIv = (ImageView) convertView.findViewById(R.id.item_delete);
            vh.mFailIv = (ImageView) convertView.findViewById(R.id.item_upload_fail_iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        displayView(vh, position);
        return convertView;
    }

    private void displayView(final ViewHolder vh, final int position) {
        goneView(vh);
        if (position < list.size()) {
            GlideUtils.showImage(vh.mImageView, mContext, list.get(position).getPicPath());
            switch (list.get(position).getUpLoadState()) {
                case AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING:
                case AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT:
                    vh.mUploadIngView.setVisibility(View.VISIBLE);
                    vh.mRoundProgressBar.setVisibility(View.VISIBLE);
                    //vh.mRoundProgressBar.setProgress((int) list.get(position).getProgress());
                    break;

                case AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_FAIL:
                    vh.mUploadFailView.setVisibility(View.VISIBLE);
                    vh.mFailIv.setOnClickListener(v -> {
                        if (mOnRetryUploadClickListener != null) {
                            mOnRetryUploadClickListener.onRetryUploadClickListener(position);
                        }
                    });
                    break;
                case AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS:
                    String url = AppConfig.getFirstJourneyUrl() + list.get(position).getServiceUrl();
                    GlideUtils.showImage(vh.mImageView, mContext, url);
                    vh.mUploadSuccessView.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            vh.mImageView.setOnLongClickListener(v -> {
                if (mIsIssueDoctorOrder) {
                    return true;
                } else {
                    boolean isUploadIng = false;
                    for (FirstJourneyPicInfo info : list) {
                        if (info.getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING || info.getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT) {
                            isUploadIng = true;
                            break;
                        }
                    }
                    if (!isUploadIng) {
                        vh.mDeleteIv.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });

            vh.mDeleteIv.setOnClickListener(v -> {
                if (mOnDeletePicClickListener != null) {
                    mOnDeletePicClickListener.onDeletePicClickListener(position);
                }
            });
        } else {
            GlideUtils.showImage(vh.mImageView, mContext, R.drawable.ic_upload_first_course_medical);
            vh.mImageView.setOnLongClickListener(v -> false);
        }
        vh.mImageView.setOnClickListener(v -> {
            if (vh.mDeleteIv.getVisibility() == View.VISIBLE) {
                vh.mDeleteIv.setVisibility(View.GONE);
            } else {
                if (mOnPicItemClickListener != null) {
                    mOnPicItemClickListener.onPicItemClickListener(position);
                }
            }
        });
    }

    private void goneView(ViewHolder vh) {
        vh.mUploadSuccessView.setVisibility(View.GONE);
        vh.mUploadIngView.setVisibility(View.GONE);
        vh.mUploadFailView.setVisibility(View.GONE);
        vh.mDeleteIv.setVisibility(View.GONE);
        vh.mRoundProgressBar.setVisibility(View.GONE);
    }

    class ViewHolder {
        private ImageView mImageView;
        private LinearLayout mUploadSuccessView;
        private RelativeLayout mUploadFailView;
        private LinearLayout mItemView;
        private RelativeLayout mUploadIngView;
        private ImageView mDeleteIv;
        private ImageView mFailIv;
        private ProgressBar mRoundProgressBar;
    }

    public interface OnPicItemClickListener {
        void onPicItemClickListener(int position);
    }

    public interface OnRetryUploadClickListener {
        void onRetryUploadClickListener(int position);
    }

    public interface OnDeletePicClickListener {
        void onDeletePicClickListener(int position);
    }
}
