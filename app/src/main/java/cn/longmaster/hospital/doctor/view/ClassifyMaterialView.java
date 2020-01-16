package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.MaterialClassify;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.ui.PicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.consult.VideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.AsyncImageGridViewAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.LibCollections;

/**
 * 材料分类视图
 * Created by Tengshuxiang on 2016-06-13.
 */
public class ClassifyMaterialView extends LinearLayout {
    private View mDividerV;
    private TextView mTitleTv;
    private ScrollGridView mPivGv;
    private MediaDownloadManager mMediaDownloadManager;

    private MaterialClassify mClassify;//材料分类
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos = new ArrayList<>();
    private boolean mIsVideoRoomEnter = false;//是否从视频诊室进入
    private AsyncImageGridViewAdapter mAsyncImageGridViewAdapter;
    private MediaDownloadInfo mDownloadInfo;

    public ClassifyMaterialView(Context context) {
        super(context);
        mMediaDownloadManager = AppApplication.getInstance().getManager(MediaDownloadManager.class);
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initView() {
        View container = LayoutInflater.from(getContext()).inflate(R.layout.layout_assist_examine_type_item, null);
        mDividerV = container.findViewById(R.id.layout_assist_examine_type_item_divider);
        mTitleTv = container.findViewById(R.id.layout_assist_examine_type_item_sub_title_tv);
        mPivGv = container.findViewById(R.id.layout_assist_examine_type_item_image_gv);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(params);
        addView(container);
    }

    public void showDivider(boolean isShow) {
        mDividerV.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setData(MaterialClassify classify, boolean isVideoRoomEnter, List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        mClassify = classify;
        mIsVideoRoomEnter = isVideoRoomEnter;
        mAuxiliaryMaterialInfos = auxiliaryMaterialInfos;
        fillData();
    }

    private void fillData() {
        if (mClassify == null) {
            throw new RuntimeException("data is null");
        }
        /*if (!("0".equals(mClassify.classifyName))) {
            mTitleTv.setText(mClassify.classifyName);
        }*/
        mTitleTv.setText(mClassify.classifyName);
        mAsyncImageGridViewAdapter = new AsyncImageGridViewAdapter(getContext(), mClassify.materialCheckInfos);
        mAsyncImageGridViewAdapter.setGridView(mPivGv);
        mPivGv.setAdapter(mAsyncImageGridViewAdapter);
        mPivGv.setOnItemClickListener((parent, view, position, id) -> {
            AuxiliaryMaterialInfo info = mClassify.materialCheckInfos.get(position);
            if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                startPicBrowser(mClassify.materialCheckInfos, position);
            } else if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA) {
                if (mIsVideoRoomEnter) {
                    Toast.makeText(getContext(), R.string.video_room_toast_cannot_play_video, Toast.LENGTH_SHORT).show();
                    return;
                }
                Logger.logD(Logger.APPOINTMENT, "onStartDownload--》info:" + info + ",info.getMediaDownloadInfo():" + info.getMediaDownloadInfo());
                if (info.getMediaType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                    if (info.getMediaDownloadInfo() != null) {
                        switch (info.getMediaDownloadInfo().getState()) {
                            case DownloadState.NOT_DOWNLOAD:
                                downloadMedia(info.getMediaDownloadInfo());
                                break;

                            case DownloadState.DOWNLOADING:
                                pauseDownload(info.getMaterialPic());
                                break;

                            case DownloadState.DOWNLOAD_SUCCESS:
                                playVideo(info);
                                break;

                            case DownloadState.DOWNLOAD_FAILED:
                                //重新下载;
                                Logger.logI(Logger.APPOINTMENT, "重新下载");
                                downloadMedia(info.getMediaDownloadInfo());
                                break;

                            case DownloadState.DOWNLOAD_PAUSE:
                                //继续下载;
                                Logger.logI(Logger.APPOINTMENT, "继续下载");
                                downloadMedia(info.getMediaDownloadInfo());
                                break;
                            default:
                                break;
                        }
                    }
                    return;
                }
                String path = "";
                String url = "";
                if (!TextUtils.isEmpty(info.getMaterialPic())) {
                    path = SdManager.getInstance().getOrderVideoPath(info.getMaterialPic(), info.getAppointmentId() + "");
                    url = AppConfig.getDfsUrl() + "3004/1/" + info.getMaterialPic();
                }
                Intent intent = new Intent();
                intent.setClass(getContext(), VideoPlayerActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, info.getMaterialName());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getMediaType());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setClass(getContext(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, mClassify.materialCheckInfos.get(position).getDicom());
                getContext().startActivity(intent);
            }
        });
    }

    private void startPicBrowser(List<AuxiliaryMaterialInfo> checkInfos, int position) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < LibCollections.size(mAuxiliaryMaterialInfos); i++) {
            urls.add(AppConfig.getMaterialDownloadUrl() + mAuxiliaryMaterialInfos.get(i).getMaterialPic());
        }
        Intent intent = new Intent(getContext(), PicBrowseActivity.class);
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setIndex(mAuxiliaryMaterialInfos.indexOf(checkInfos.get(position)));
        browserPicEvent.setAssistant(true);
        browserPicEvent.setServerFilePaths(urls);
        browserPicEvent.setAuxiliaryMaterialInfos(mAuxiliaryMaterialInfos);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO, browserPicEvent);
        getContext().startActivity(intent);
    }

    private void downloadMedia(final MediaDownloadInfo downloadInfo) {
        mMediaDownloadManager.fileDownload(downloadInfo);
    }

    private void pauseDownload(String fileName) {
        mMediaDownloadManager.pauseDownload(fileName);
    }

    private void playVideo(AuxiliaryMaterialInfo info) {
        String path = "";
        String url = "";
        if (!TextUtils.isEmpty(info.getMaterialPic())) {
            path = SdManager.getInstance().getOrderVideoPath(info.getMaterialPic(), info.getAppointmentId() + "");
            url = AppConfig.getDfsUrl() + "3004/1/" + info.getMaterialPic();
        }
        Logger.logD(Logger.APPOINTMENT, "-->path:" + path);
        Intent intent = new Intent();
        intent.setClass(getContext(), VideoPlayerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, info.getMaterialName());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getMediaType());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
        getContext().startActivity(intent);
    }

    public void onStartDownload(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadSuccessful");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOADING);
        mAsyncImageGridViewAdapter.updateItemData(mDownloadInfo);
    }

    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
        Logger.logI(Logger.APPOINTMENT, "onLoadProgressChange");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOADING);
        mDownloadInfo.setCurrentSize(currentSize);
        mDownloadInfo.setTotalSize(totalSize);
        mAsyncImageGridViewAdapter.updateItemData(mDownloadInfo);
    }

    public void onLoadFailed(String filePath, String reason) {
        Logger.logI(Logger.APPOINTMENT, "onLoadFailed");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_FAILED);
        mAsyncImageGridViewAdapter.updateItemData(mDownloadInfo);
    }

    public void onLoadSuccessful(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadSuccessful");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_SUCCESS);
        mAsyncImageGridViewAdapter.updateItemData(mDownloadInfo);
    }

    public void onLoadStopped(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadStopped");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
        mAsyncImageGridViewAdapter.updateItemData(mDownloadInfo);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
