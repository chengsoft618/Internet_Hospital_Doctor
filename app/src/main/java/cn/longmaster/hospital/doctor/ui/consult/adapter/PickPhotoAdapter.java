package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.photo.OnItemCheckListener;
import cn.longmaster.doctorlibrary.util.photo.OnItemClickListener;
import cn.longmaster.doctorlibrary.util.photo.Photo;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 选择图片上传adapter
 * <p>
 * Created by WangHaiKun on 2017/9/21.
 */
public class PickPhotoAdapter extends RecyclerView.Adapter<PickPhotoAdapter.PhotoViewHolder> {
    private Context mContext;
    private List<Photo> mPhotosList;
    private int mSelectCount = 0;
    private int mMaxSelectCount = 1;
    private OnItemCheckListener mOnItemCheckListener;
    private OnItemClickListener mOnItemClickListener;

    public PickPhotoAdapter(Context context, int maxSelectCount) {
        this.mContext = context;
        this.mMaxSelectCount = maxSelectCount;
        this.mSelectCount = 0;
        mPhotosList = new ArrayList<>();
    }

    public void setData(List<Photo> list, int selectCount) {
        this.mPhotosList.clear();
        this.mPhotosList.addAll(list);
        this.mSelectCount = selectCount;
        notifyDataSetChanged();
    }

    public void setData(List<Photo> list) {
        setData(list, 0);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_picker_upload_photo, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        final Photo photo = mPhotosList.get(position);
        final boolean isChecked = photo.isSelect();
        displayStateView(holder, photo);
        GlideUtils.showImage(holder.mPhotoIv, mContext, photo.getPath());
        holder.mSelectedIv.setVisibility(mMaxSelectCount == 1 ? View.GONE : View.VISIBLE);
        holder.mMaskIv.setSelected(isChecked);
        holder.mSelectedIv.setSelected(isChecked);

        holder.mSelectedIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCheckListener != null) {
                    mOnItemCheckListener.OnItemCheck(position, photo, isChecked);
                }
                if (isChecked) {
                    mSelectCount--;
                    mPhotosList.get(position).setSelect(false);
                    holder.mMaskIv.setSelected(false);
                } else {
                    if (mSelectCount < mMaxSelectCount) {
                        mPhotosList.get(position).setSelect(true);
                        mSelectCount++;
                        holder.mMaskIv.setSelected(true);
                    }
                }
                notifyItemChanged(position);
            }
        });
        holder.mMaskIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, photo);
                }
            }
        });
    }

    private void displayStateView(PhotoViewHolder holder, Photo photo) {
        Logger.logI(Logger.COMMON, "displayStateView-->photo()" + photo.getUploadState());
        if (photo.getUploadState() == 100) {
            holder.mUploadPicIngView.setVisibility(View.GONE);
            holder.mUploadPicSuccessView.setVisibility(View.GONE);
            holder.mMaskView.setVisibility(View.GONE);
        } else {
            if (photo.getUploadState() == UploadState.UPLOAD_SUCCESS) {
                holder.mUploadPicIngView.setVisibility(View.GONE);
                holder.mUploadPicSuccessView.setVisibility(View.VISIBLE);
            } else {
                holder.mUploadPicIngView.setVisibility(View.VISIBLE);
                holder.mUploadPicSuccessView.setVisibility(View.GONE);
            }
            holder.mMaskView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mPhotosList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @FindViewById(R.id.item_upload_photo_pic_iv)
        private ImageView mPhotoIv;
        @FindViewById(R.id.item_upload_photo_mask_iv)
        private ImageView mMaskIv;
        @FindViewById(R.id.item_upload_photo_selected_iv)
        private ImageView mSelectedIv;
        @FindViewById(R.id.item_upload_photo_pic_success_view)
        private LinearLayout mUploadPicSuccessView;
        @FindViewById(R.id.item_upload_photo_pic_ing_view)
        private LinearLayout mUploadPicIngView;
        @FindViewById(R.id.item_upload_photo_view)
        private View mMaskView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoIv = itemView.findViewById(R.id.item_upload_photo_pic_iv);
            mMaskIv = itemView.findViewById(R.id.item_upload_photo_mask_iv);
            mSelectedIv = itemView.findViewById(R.id.item_upload_photo_selected_iv);
            mUploadPicSuccessView = itemView.findViewById(R.id.item_upload_photo_pic_success_view);
            mUploadPicIngView = itemView.findViewById(R.id.item_upload_photo_pic_ing_view);
            mMaskView = itemView.findViewById(R.id.item_upload_photo_view);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.mOnItemCheckListener = onItemCheckListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
