package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.rounds.AuxiliaryInspectInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.RoundProgressBar;
import cn.longmaster.hospital.doctor.view.ScrollGridView;
import cn.longmaster.utils.DisplayUtil;

/**
 * 辅助材料适配器
 *
 * @author Tengshuxiang
 */
public class RoundsAsyncImageGridViewAdapter extends BaseAdapter {
    private final String TAG = RoundsAsyncImageGridViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<AuxiliaryInspectInfo> mData;
    private int mLength;
    private MediaDownloadManager mMediaDownloadManager;
    private ScrollGridView mGridView;
    private boolean mShowName;

    public RoundsAsyncImageGridViewAdapter(Context context, List<AuxiliaryInspectInfo> patient_auxiliary_check_list) {
        mContext = context;
        mShowName = true;
        mData = new ArrayList<>();
        if (patient_auxiliary_check_list != null) {
            mData.addAll(patient_auxiliary_check_list);
        }
        //根据布局中的padding spacing计算item的（正方形）边长
        mLength = (ScreenUtil.getScreenWidth() - DisplayUtil.dip2px(13.33f * 2 + 6.67f * 3)) / 4;
        mMediaDownloadManager = AppApplication.getInstance().getManager(MediaDownloadManager.class);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNameShow(boolean showName) {
        mShowName = showName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image, parent, false);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        }
        if (holder == null) {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initState();
        final AuxiliaryInspectInfo info = mData.get(position);
        if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
            try {
                String fileServerName = info.getMaterialPic();
                Logger.logI(Logger.USER, TAG + "->fileServerName:" + fileServerName);

                String smallPicName = fileServerName.substring(0, fileServerName.lastIndexOf(".")) + "_s" + fileServerName.substring(fileServerName.lastIndexOf("."));

                String filePath = SdManager.getInstance().getOrderPicPath(smallPicName, info.getMedicalId() + "");
                String url = AppConfig.getMaterialDownloadUrl() + smallPicName;

                Logger.logI(Logger.USER, TAG + "->filePath:" + filePath + "  url:" + url);
                GlideUtils.showImage(holder.aiv, mContext, url);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA) {
            holder.aiv.setVisibility(View.GONE);
            if (info.getMaterialType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                holder.rl.setVisibility(View.VISIBLE);
                final ViewHolder finalHolder = holder;
                if (info.getMediaDownloadInfo() == null) {
                    if (FileUtil.isFileExist(mMediaDownloadManager.getFilePath(info.getMaterialPic(), info.getMedicalId()))) {
                        info.setMediaDownloadInfo(new MediaDownloadInfo(info.getMedicalId(), DownloadState.DOWNLOAD_SUCCESS, info.getMaterialPic()));
                        dealDownloadInfo(finalHolder, info.getMediaDownloadInfo());
                    } else {
                        mMediaDownloadManager.getDownloadInfoFromDB(info.getMaterialPic(), new MediaDownloadManager.GetMediaDownloadCallback() {
                            @Override
                            public void onGetMediaDownload(MediaDownloadInfo mediaDownloadInfo) {
                                if (mediaDownloadInfo != null) {
                                    if (!FileUtil.isFileExist(mMediaDownloadManager.getTempPath(info.getMaterialPic(), info.getMedicalId()))) {
                                        mediaDownloadInfo.setState(mMediaDownloadManager.isDownLoading(info.getMaterialPic())
                                                ? DownloadState.DOWNLOADING : DownloadState.NOT_DOWNLOAD);
                                    } else {
                                        if (mediaDownloadInfo.getState() == DownloadState.DOWNLOADING && !mMediaDownloadManager.isDownLoading(info.getMaterialPic())) {
                                            mediaDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
                                        }
                                    }
                                } else {
                                    mediaDownloadInfo = new MediaDownloadInfo(info.getMedicalId(),
                                            mMediaDownloadManager.isDownLoading(info.getMaterialPic()) ? DownloadState.DOWNLOADING : DownloadState.NOT_DOWNLOAD,
                                            info.getMaterialPic());
                                }
                                info.setMediaDownloadInfo(mediaDownloadInfo);
                                dealDownloadInfo(finalHolder, info.getMediaDownloadInfo());
                            }
                        });
                    }
                } else {
                    dealDownloadInfo(finalHolder, info.getMediaDownloadInfo());
                }
            } else if (info.getMaterialType() == AppConstant.MediaType.MEDIA_TYPE_VOICE) {
                holder.iv.setVisibility(View.VISIBLE);
                holder.iv.setImageResource(R.drawable.ic_image_consult_voice);
            }
        } else {
            holder.aiv.setVisibility(View.GONE);
            if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_DICOM) {
                holder.iv.setImageResource(R.drawable.ic_image_consult_dcm);
            } else if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_WSI) {
                holder.iv.setImageResource(R.drawable.ic_image_consult_wsi);
            }
            holder.iv.setVisibility(View.VISIBLE);

        }
        if (mShowName) {
            holder.tv.setText(info.getMaterialName());
        } else {
            holder.tv.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView aiv;
        private ImageView iv;
        private TextView tv;
        private RelativeLayout rl;
        private RoundProgressBar progress;
        private ImageView icon;
        private ImageView control;

        private void initView(View view) {
            aiv = view.findViewById(R.id.item_grid_image_aiv);
            iv = view.findViewById(R.id.item_grid_dicom_iv);
            tv = view.findViewById(R.id.item_grid_name_tv);
            rl = view.findViewById(R.id.item_grid_media_control_rl);
            progress = view.findViewById(R.id.item_grid_media_progress_bar);
            icon = view.findViewById(R.id.item_grid_media_icon);
            control = view.findViewById(R.id.item_grid_media_control_btn);

            ViewGroup.LayoutParams lp = aiv.getLayoutParams();
            lp.height = mLength;
            lp.width = mLength;
            aiv.setLayoutParams(lp);

            lp = iv.getLayoutParams();
            lp.height = mLength;
            lp.width = mLength;
            iv.setLayoutParams(lp);
        }

        private void initState() {
            aiv.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            control.setVisibility(View.GONE);
        }
    }

    private void dealDownloadInfo(ViewHolder viewHolder, MediaDownloadInfo info) {
        Logger.logI(Logger.APPOINTMENT, "dealDownloadInfo-->info:" + info);
        switch (info.getState()) {
            case DownloadState.NOT_DOWNLOAD:
                viewHolder.icon.setImageResource(R.drawable.ic_media_not_download);
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.control.setVisibility(View.VISIBLE);
                viewHolder.control.setImageResource(R.drawable.ic_media_download_btn);
                break;

            case DownloadState.DOWNLOAD_SUCCESS:
                viewHolder.icon.setImageResource(R.drawable.ic_media_download_success);
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.control.setVisibility(View.VISIBLE);
                viewHolder.control.setImageResource(R.drawable.ic_media_play);
                break;

            case DownloadState.DOWNLOADING:
                viewHolder.icon.setImageResource(R.drawable.ic_media_not_download);
                viewHolder.progress.setVisibility(View.VISIBLE);
                viewHolder.control.setVisibility(View.GONE);
                Logger.logI(Logger.APPOINTMENT, "getCurrentSize:" + info.getCurrentSize() + ",getTotalSize:" + info.getTotalSize());
                if (info.getTotalSize() > 0 && info.getCurrentSize() > 0) {
                    viewHolder.progress.setProgress((int) (info.getCurrentSize() / (info.getTotalSize() / 100)));
                } else {
                    viewHolder.progress.setProgress(0);
                }
                break;

            case DownloadState.DOWNLOAD_PAUSE:
                viewHolder.icon.setImageResource(R.drawable.ic_media_not_download);
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.control.setVisibility(View.VISIBLE);
                viewHolder.control.setImageResource(R.drawable.ic_media_download_pause);
                break;

            case DownloadState.DOWNLOAD_FAILED:
                viewHolder.icon.setImageResource(R.drawable.ic_media_download_failed);
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.control.setVisibility(View.VISIBLE);
                viewHolder.control.setImageResource(R.drawable.ic_media_download_retry);
                break;

        }

    }

    /*********更新单条************/
    /**
     * 设置gridView对象
     *
     * @param gridView
     */
    public void setGridView(ScrollGridView gridView) {
        this.mGridView = gridView;
    }

    /**
     * update gridView 单条数据
     *
     * @param info 新数据对象
     */
    public void updateItemData(MediaDownloadInfo info) {
        Message msg = Message.obtain();
        int ids = -1;
        // 进行数据对比获取对应数据在list中的位置
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMaterialPic().equals(info.getLocalFileName())) {
                ids = i;
            }
        }
        msg.arg1 = ids;
        // 更新mDataList对应位置的数据
        if (ids == -1) {
            return;
        }
        if (mData.get(ids).getMediaDownloadInfo() == null) {
            return;
        }
        mData.get(ids).getMediaDownloadInfo().setState(info.getState());
        if (info.getCurrentSize() != 0) {
            mData.get(ids).getMediaDownloadInfo().setCurrentSize(info.getCurrentSize());
        }
        if (info.getTotalSize() != 0) {
            mData.get(ids).getMediaDownloadInfo().setTotalSize(info.getTotalSize());
        }
//        mData.set(ids, item);
        // handle刷新界面
        han.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateItem(msg.arg1);
        }
    };

    /**
     * 刷新指定item
     *
     * @param index item在gridView中的位置
     */
    private void updateItem(int index) {
        if (mGridView == null) {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = mGridView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = mGridView.getChildAt(index - visiblePosition);
        ViewHolder holder = new ViewHolder();
        holder.initView(view);
//        RoundProgressBar progress = (RoundProgressBar) view.findViewById(R.id.item_grid_media_progress_bar);
//        ImageView icon = (ImageView) view.findViewById(R.id.item_grid_media_icon);
//        ImageView control = (ImageView) view.findViewById(R.id.item_grid_media_control_btn);
        // 获取mDataList.set(ids, item);更新的数据
        // 重新设置界面显示数据
        dealDownloadInfo(holder, mData.get(index).getMediaDownloadInfo());
    }
}
