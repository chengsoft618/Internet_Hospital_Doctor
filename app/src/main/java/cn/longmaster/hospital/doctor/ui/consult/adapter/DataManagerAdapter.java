package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.StringUtils;

/**
 * Created by YY on 18/1/2.
 * Mod by biao on 2019/9/19
 */
public class DataManagerAdapter extends BaseQuickAdapter<SingleFileInfo, BaseViewHolder> {
    public static final int TYPE_PIC = 2;
    public static final int TYPE_CHOOSE = 4;

    public DataManagerAdapter(@LayoutRes int layoutResId, @Nullable List<SingleFileInfo> data) {
        super(layoutResId, data);
    }

    @Override
    public void setNewData(@Nullable List<SingleFileInfo> data) {
        super.setNewData(data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CHOOSE;
        }
        return TYPE_PIC;
    }

    private boolean isShowAddItem(int position) {
        int size = getData().size() == 0 ? 0 : getData().size();
        return position == size;
    }

    @Override
    public void convert(BaseViewHolder helper, SingleFileInfo item) {
        ImageView imageView = helper.getView(R.id.item_data_manage_photo_img_iv);
        int position = helper.getLayoutPosition();
        if (getItemViewType(position) == TYPE_CHOOSE) {
            int padding = DisplayUtil.dp2px(20);
            imageView.setPadding(padding, padding, padding, padding);
            GlideUtils.showImage(imageView, mContext, R.mipmap.ic_pic_add);
            helper.setGone(R.id.item_data_manage_photo_delete_iv, false);
        } else {
            int padding = 0;
            imageView.setPadding(padding, padding, padding, padding);
            helper.addOnClickListener(R.id.item_data_manage_photo_delete_iv);
            if (item.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_DICOM) {
                GlideUtils.showImage(imageView, mContext, R.drawable.ic_image_consult_dcm);
            } else if (item.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_WSI) {
                GlideUtils.showImage(imageView, mContext, R.drawable.ic_image_consult_wsi);
            } else if (item.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA) {
                GlideUtils.showImage(imageView, mContext, R.drawable.ic_image_consult_voice);
                if (item.getMediaType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                    imageView.setVisibility(View.GONE);
                    helper.setVisible(R.id.item_data_manage_media_control_rl, true);
                    dealDownloadInfo(helper, item.getMediaDownloadInfo());
                }
            } else if (item.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                if (StringUtils.isEmpty(item.getServerFileName())) {
                    GlideUtils.showImage(imageView, mContext, R.drawable.ic_default_pic);
                } else {
                    if (item.getServerFileName().contains(".")) {
                        String fileServerName = item.getServerFileName();
                        String smallPicName = fileServerName.substring(0, fileServerName.lastIndexOf(".")) + "_s" + fileServerName.substring(fileServerName.lastIndexOf("."));
                        GlideUtils.showImage(imageView, mContext, AppConfig.getMaterialDownloadUrl() + smallPicName);
                    }
                }
            }
            if (item.isShowDel()) {
                helper.setVisible(R.id.item_data_manage_photo_delete_iv, true);
            } else {
                helper.setGone(R.id.item_data_manage_photo_delete_iv, false);
            }
        }
    }

    private void dealDownloadInfo(BaseViewHolder helper, MediaDownloadInfo info) {
        switch (info.getState()) {
            case DownloadState.NOT_DOWNLOAD:
                helper.setImageResource(R.id.item_data_manage_media_icon, R.drawable.ic_media_not_download);
                helper.setGone(R.id.item_data_manage_media_progress_bar, false);
                helper.setVisible(R.id.item_data_manage_media_control_btn, true);
                helper.setImageResource(R.id.item_data_manage_media_control_btn, R.drawable.ic_media_download_btn);
                break;

            case DownloadState.DOWNLOAD_SUCCESS:
                helper.setImageResource(R.id.item_data_manage_media_icon, R.drawable.ic_media_download_success);
                helper.setGone(R.id.item_data_manage_media_progress_bar, false);
                helper.setVisible(R.id.item_data_manage_media_control_btn, true);
                helper.setImageResource(R.id.item_data_manage_media_control_btn, R.drawable.ic_media_play);
                break;

            case DownloadState.DOWNLOADING:
                helper.setImageResource(R.id.item_data_manage_media_icon, R.drawable.ic_media_not_download);
                helper.setVisible(R.id.item_data_manage_media_progress_bar, true);
                helper.setGone(R.id.item_data_manage_media_control_btn, false);
                Logger.logI(Logger.APPOINTMENT, "getCurrentSize:" + info.getCurrentSize() + ",getTotalSize:" + info.getTotalSize());
                if (info.getTotalSize() > 0 && info.getCurrentSize() > 0) {
                    helper.setProgress(R.id.item_data_manage_media_progress_bar, (int) (info.getCurrentSize() / (info.getTotalSize() / 100)));
                } else {
                    helper.setProgress(R.id.item_data_manage_media_progress_bar, 0);
                }
                break;

            case DownloadState.DOWNLOAD_PAUSE:
                helper.setImageResource(R.id.item_data_manage_media_icon, R.drawable.ic_media_not_download);
                helper.setGone(R.id.item_data_manage_media_progress_bar, false);
                helper.setVisible(R.id.item_data_manage_media_control_btn, true);
                helper.setImageResource(R.id.item_data_manage_media_control_btn, R.drawable.ic_media_download_pause);
                break;

            case DownloadState.DOWNLOAD_FAILED:
                helper.setImageResource(R.id.item_data_manage_media_icon, R.drawable.ic_media_download_failed);
                helper.setGone(R.id.item_data_manage_media_progress_bar, false);
                helper.setVisible(R.id.item_data_manage_media_control_btn, true);
                helper.setImageResource(R.id.item_data_manage_media_control_btn, R.drawable.ic_media_download_retry);
                break;
        }
    }

    public void toggleDelete(int position) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            SingleFileInfo singleFileInfo = getData().get(i);
            if (i == position) {
                singleFileInfo.setShowDel(true);
            } else {
                singleFileInfo.setShowDel(false);
            }
        }
        notifyDataSetChanged();
    }
}
