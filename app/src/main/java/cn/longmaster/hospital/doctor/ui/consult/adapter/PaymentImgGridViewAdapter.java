package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.imageloader.view.AsyncImageView;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadPicFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;


/**
 * 支付确认单图片列表Adapter
 * Created by Yang² on 2017/12/26.
 */

public class PaymentImgGridViewAdapter extends SimpleBaseAdapter<UploadPicFileInfo, PaymentImgGridViewAdapter.ViewHolder> {
    private int mSelectPosition = -1;
    private Context mContext;
    private onItemDeleteClickListener mOnItemDeleteClickListener;
    private onItemAddClickListener mOnItemAddClickListener;
    private onItemClickListener mOnItemClickListener;
    private boolean mClickable = true;

    public PaymentImgGridViewAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_payment_img;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public void setOnItemDeleteClickListener(onItemDeleteClickListener onItemDeleteClickListener) {
        this.mOnItemDeleteClickListener = onItemDeleteClickListener;
    }

    public void setOnItemAddClickListener(onItemAddClickListener onItemAddClickListener) {
        this.mOnItemAddClickListener = onItemAddClickListener;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    @Override
    protected void bindView(final ViewHolder viewHolder, final UploadPicFileInfo uploadPicFileInfo, final int position) {
        final boolean isAddBtn = position == getCount() - 1 && TextUtils.isEmpty(uploadPicFileInfo.getLocalFilePath()) && TextUtils.isEmpty(uploadPicFileInfo.getUrlPath());
        viewHolder.displayView(uploadPicFileInfo, isAddBtn);

        viewHolder.deleteIv.setVisibility(mSelectPosition == position ? View.VISIBLE : View.GONE);
        viewHolder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mClickable) {
                    return;
                }
                if (!isAddBtn) {
                    if (mOnItemDeleteClickListener != null) {
                        mOnItemDeleteClickListener.onItemDelete(position, uploadPicFileInfo);
                    }
                }
            }
        });
        viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mClickable) {
                    return false;
                }
                if (!isAddBtn && uploadPicFileInfo.getState() != UploadState.NOT_UPLOADED && uploadPicFileInfo.getState() != UploadState.UPLOADING) {
                    setSelectPosition(mSelectPosition == position ? -1 : position);
                    notifyDataSetChanged();
                }
                return false;
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mClickable) {
//                    return;
//                }
                if (!isAddBtn) {
                    mOnItemClickListener.onItemClick(position, uploadPicFileInfo);
                } else {
                    if (mOnItemAddClickListener != null) {
                        mOnItemAddClickListener.onItemAdd();
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_payment_img_iv)
        private AsyncImageView imageView;
        @FindViewById(R.id.item_payment_img_pb)
        private ProgressBar progressBar;
        @FindViewById(R.id.item_payment_img_status_tv)
        private TextView statusTv;
        @FindViewById(R.id.item_payment_img_delete_iv)
        private ImageView deleteIv;

        private void displayView(UploadPicFileInfo uploadPicFileInfo, boolean isAddBtn) {
            if (isAddBtn) {
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_btn_payment_img_add));
                progressBar.setVisibility(View.GONE);
                statusTv.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(uploadPicFileInfo.getLocalFilePath())) {
                    String filePath = SdManager.getInstance().getPaymentPic() + uploadPicFileInfo.getServerFileName();
                    imageView.loadImage(filePath, uploadPicFileInfo.getUrlPath());
                } else {
                    imageView.loadImage(uploadPicFileInfo.getLocalFilePath(), "");
                }
                if (!TextUtils.isEmpty(uploadPicFileInfo.getLocalFilePath())) {
                    switch (uploadPicFileInfo.getState()) {
                        case UploadState.NOT_UPLOADED:
                        case UploadState.UPLOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            statusTv.setVisibility(View.GONE);
                            break;

                        case UploadState.UPLOAD_SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            statusTv.setVisibility(View.VISIBLE);
                            statusTv.setBackgroundResource(R.drawable.bg_upload_success);
                            statusTv.setText(R.string.data_upload_success);
                            break;

                        case UploadState.UPLOAD_FAILED:
                            progressBar.setVisibility(View.GONE);
                            statusTv.setVisibility(View.VISIBLE);
                            statusTv.setBackgroundResource(R.drawable.bg_upload_fail);
                            statusTv.setText(R.string.data_upload_fail);
                            break;

                        default:
                            progressBar.setVisibility(View.GONE);
                            statusTv.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    statusTv.setVisibility(View.GONE);
                }
            }
        }

    }

    public interface onItemDeleteClickListener {
        void onItemDelete(int position, UploadPicFileInfo uploadPicFileInfo);
    }

    public interface onItemAddClickListener {
        void onItemAdd();
    }

    public interface onItemClickListener {
        void onItemClick(int position, UploadPicFileInfo uploadPicFileInfo);
    }
}
