package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.MedicalAuxiliaryInspectRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.VideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.AsyncImageGridViewAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.ScrollGridView;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 查房患者信息 基础病历fragment
 */
public class RoundsBasicMedicalFragment extends NewBaseFragment implements FileLoadListener {
    @FindViewById(R.id.fragment_rounds_base_record_medical_id_tv)
    private TextView mMedicalIdTv;
    @FindViewById(R.id.fragment_rounds_base_record_in_hospital_id_tv)
    private TextView fragmentRoundsBaseRecordInHospitalIdTv;
    @FindViewById(R.id.fragment_rounds_base_record_patient_name_tv)
    private TextView mPatientNameTv;
    @FindViewById(R.id.fragment_rounds_base_record_gender_tv)
    private TextView mPatientGenderTv;
    @FindViewById(R.id.fragment_rounds_base_record_age_tv)
    private TextView mPatientAgeTv;
    @FindViewById(R.id.fragment_rounds_base_record_diagnosis_tv)
    private TextView mDiagnosisTv;
    @FindViewById(R.id.fragment_rounds_base_record_abstract_tv)
    private TextView mAbstractTv;
    @FindViewById(R.id.fragment_rounds_base_record_fragment_line_view)
    private View mLineView;
    @FindViewById(R.id.fragment_rounds_base_doctor_order_view)
    private LinearLayout mDoctorOrderView;
    @FindViewById(R.id.fragment_rounds_base_doctor_order_grid_view)
    private ScrollGridView mDoctorOrderGridView;
    @FindViewById(R.id.fragment_rounds_base_medical_view)
    private LinearLayout mMedicalView;
    @FindViewById(R.id.fragment_rounds_base_medical_grid_view)
    private ScrollGridView mMedicalGridView;
    @FindViewById(R.id.fragment_rounds_base_record_fragment_layout_fl)
    private FrameLayout mFrameLayout;

    @AppApplication.Manager
    UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;

    //private BasicMedicalInfo mBasicMedicalInfo;
    private RoundsAuxiliaryAssistExamineFragment mAssistExamineFragment;
    private List<AuxiliaryMaterialInfo> mMedicalAuxilirayInfos = new ArrayList<>();
    private List<AuxiliaryMaterialInfo> mDoctorOrderAuxilirayInfos = new ArrayList<>();
    private AsyncImageGridViewAdapter mDoctorOrderAuxilirayAdapter;
    private AsyncImageGridViewAdapter mMedicalAuxilirayAdapter;
    private MediaDownloadInfo mDownloadInfo;
    private int mMediaDownloadType = 0;//0为空闲状态，1病历视频下载，2医嘱单视频下载

    public static RoundsBasicMedicalFragment getInstance(RoundsMedicalDetailsInfo medicalDetailsInfo, boolean isRoom) {
        RoundsBasicMedicalFragment roundsBasicMedicalFragment = new RoundsBasicMedicalFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO, medicalDetailsInfo);
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_ROOM, isRoom);
        roundsBasicMedicalFragment.setArguments(bundle);
        return roundsBasicMedicalFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rounds_base_record;
    }

    @Override
    public void initViews(View rootView) {
        mMediaDownloadManager.regLoadListener(this);
        getData(getBasicMedicalInfo().getMedicalId());
        getMedicalAuxiliary(getBasicMedicalInfo().getMedicalId());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData(getBasicMedicalInfo().getMedicalId());
            getMedicalAuxiliary(getBasicMedicalInfo().getMedicalId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaDownloadManager.unRegLoadListener(this);
    }

    private void getData(int medicalId) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                Logger.logI(Logger.APPOINTMENT, "baseResult:" + baseResult + ",roundsMedicalDetailsInfo：" + roundsMedicalDetailsInfo);
                if (isAdded()) {
                    displayView(roundsMedicalDetailsInfo);
                }
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    private void displayView(RoundsMedicalDetailsInfo info) {
        mMedicalIdTv.setText(getString(R.string.rounds_medical_id, info.getMedicalId() + ""));
        fragmentRoundsBaseRecordInHospitalIdTv.setText(getString(R.string.rounds_in_hospital_id, StringUtils.isTrimEmpty(info.getHospitalizaId()) ? "--" : info.getHospitalizaId()));
        mPatientNameTv.setText(getString(R.string.rounds_name, info.getPatientName()));
        mPatientGenderTv.setText(getString(R.string.rounds_gender, info.getGender() == 1 ? "男" : "女"));
        mPatientAgeTv.setText(getString(R.string.rounds_age, info.getAge()));
        mDiagnosisTv.setText(info.getAttendingDisease());
        mAbstractTv.setText(info.getPatientIllness());
    }

    /**
     * 拉取检查资料
     */
    private void getMedicalAuxiliary(int medicalId) {
        MedicalAuxiliaryInspectRequester requester = new MedicalAuxiliaryInspectRequester(new DefaultResultCallback<List<AuxiliaryMaterialInfo>>() {
            @Override
            public void onSuccess(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(auxiliaryMaterialInfos)) {
                    auxiliaryMaterialInfos = extractCheckSuccessMaterials(auxiliaryMaterialInfos);
                    if (LibCollections.isNotEmpty(auxiliaryMaterialInfos)) {
                        displayAuxiTlirayView(auxiliaryMaterialInfos);
                    } else {
                        mLineView.setVisibility(View.GONE);
                        mFrameLayout.setVisibility(View.GONE);
                    }
                } else {
                    mLineView.setVisibility(View.GONE);
                    mFrameLayout.setVisibility(View.GONE);
                }
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    /**
     * 提取审核通过的图片
     *
     * @param materialInfos 所有材料
     * @return 审核通过的材料
     */
    private List<AuxiliaryMaterialInfo> extractCheckSuccessMaterials(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : materialInfos) {
            if (materialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                newMaterialInfos.add(materialInfo);
            }
        }
        return newMaterialInfos;
    }

    private void displayAuxiTlirayView(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        List<AuxiliaryMaterialInfo> tempList = auxiliaryMaterialInfos;
        mDoctorOrderAuxilirayInfos.clear();
        mMedicalAuxilirayInfos.clear();
        for (AuxiliaryMaterialInfo info : tempList) {
            if (info.getMaterialCfgName().equals("医嘱单")) {
                mDoctorOrderAuxilirayInfos.add(info);
            }
            if (info.getMaterialCfgName().equals("病历")) {
                mMedicalAuxilirayInfos.add(info);
            }
        }
        if (LibCollections.isNotEmpty(mDoctorOrderAuxilirayInfos)) {
            tempList.removeAll(mDoctorOrderAuxilirayInfos);
            mDoctorOrderView.setVisibility(View.VISIBLE);
            mDoctorOrderAuxilirayAdapter = new AsyncImageGridViewAdapter(getActivity(), mDoctorOrderAuxilirayInfos);
            mDoctorOrderAuxilirayAdapter.setNameShow(false);
            mDoctorOrderAuxilirayAdapter.setGridView(mDoctorOrderGridView);
            mDoctorOrderGridView.setAdapter(mDoctorOrderAuxilirayAdapter);
            mDoctorOrderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setOnItemClic(mDoctorOrderAuxilirayInfos, position, 2);
                }
            });
        } else {
            mDoctorOrderView.setVisibility(View.GONE);
        }
        if (LibCollections.isNotEmpty(mMedicalAuxilirayInfos)) {
            mMedicalView.setVisibility(View.VISIBLE);
            tempList.removeAll(mMedicalAuxilirayInfos);
            mMedicalAuxilirayAdapter = new AsyncImageGridViewAdapter(getActivity(), mMedicalAuxilirayInfos);
            mMedicalAuxilirayAdapter.setNameShow(false);
            mMedicalAuxilirayAdapter.setGridView(mMedicalGridView);
            mMedicalGridView.setAdapter(mMedicalAuxilirayAdapter);
            mMedicalGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setOnItemClic(mMedicalAuxilirayInfos, position, 12);
                }
            });
        } else {
            mMedicalView.setVisibility(View.GONE);
        }
        if (LibCollections.isNotEmpty(tempList)) {
            initAuxiliaryAssistExamineFragment();
        } else {
            mLineView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.GONE);
        }
    }

    private void setOnItemClic(List<AuxiliaryMaterialInfo> mDoctorOrderAuxilirayInfos, int position, int type) {
        AuxiliaryMaterialInfo info = mDoctorOrderAuxilirayInfos.get(position);
        if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
            List<AuxiliaryMaterialInfo> picList = new ArrayList<>();
            for (int i = 0; i < LibCollections.size(mDoctorOrderAuxilirayInfos); i++) {
                if (mDoctorOrderAuxilirayInfos.get(i).getMediaType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                    picList.add(mDoctorOrderAuxilirayInfos.get(i));
                }
            }
            startPicBrowser(picList, position);
        } else if (info.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA) {
            if (isRoom()) {
                Toast.makeText(getActivity(), R.string.video_room_toast_cannot_play_video, Toast.LENGTH_SHORT).show();
                return;
            }
            Logger.logD(Logger.APPOINTMENT, "onStartDownload--》info:" + info + ",info.getMediaDownloadInfo():" + info.getMediaDownloadInfo());
           /* if (info.getMediaType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                if (info.getMediaDownloadInfo() != null) {
                    switch (info.getMediaDownloadInfo().getState()) {
                        case DownloadState.NOT_DOWNLOAD:
                            downloadMedia(info.getMediaDownloadInfo(), type);
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
                            downloadMedia(info.getMediaDownloadInfo(), type);
                            break;

                        case DownloadState.DOWNLOAD_PAUSE:
                            //继续下载;
                            Logger.logI(Logger.APPOINTMENT, "继续下载");
                            downloadMedia(info.getMediaDownloadInfo(), type);
                            break;
                    }
                }
                return;
            }*/
            String path = "";
            String url = "";
            if (!TextUtils.isEmpty(info.getMaterialPic())) {
                path = SdManager.getInstance().getOrderVideoPath(info.getMaterialPic(), info.getAppointmentId() + "");
                url = AppConfig.getDfsUrl() + "3004/1/" + info.getMaterialPic();
            }
            Intent intent = new Intent();
            intent.setClass(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, info.getMaterialName());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getMediaType());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, mDoctorOrderAuxilirayInfos.get(position).getDicom());
            startActivity(intent);
        }
    }

    private void startPicBrowser(List<AuxiliaryMaterialInfo> checkInfos, int position) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < LibCollections.size(checkInfos); i++) {
            picUrls.add(AppConfig.getMaterialDownloadUrl() + checkInfos.get(i).getMaterialPic());
        }
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setIndex(position);
        browserPicEvent.setAssistant(true);
        browserPicEvent.setServerFilePaths(picUrls);
        browserPicEvent.setAuxiliaryMaterialInfos(checkInfos);
        getDisplay().startPicBrowseActivity(browserPicEvent, 0);
    }

    private void downloadMedia(final MediaDownloadInfo downloadInfo, int type) {
        if (mMediaDownloadType != 0) {
            ToastUtils.showShort("有视频正在下载中，请稍后重试");
            return;
        }
        mMediaDownloadType = type;
        mMediaDownloadManager.fileDownload(downloadInfo);
    }

    private void pauseDownload(String fileName) {
        mMediaDownloadType = 0;
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
        intent.setClass(getActivity(), VideoPlayerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, info.getMaterialName());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getMediaType());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
        startActivity(intent);
    }

    private void initAuxiliaryAssistExamineFragment() {
        if (null != getFragmentManager()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (mAssistExamineFragment == null) {
                mAssistExamineFragment = RoundsAuxiliaryAssistExamineFragment.getInstance(getBasicMedicalInfo());
                transaction.add(R.id.fragment_rounds_base_record_fragment_layout_fl, mAssistExamineFragment);
            }
            transaction.show(mAssistExamineFragment);
            transaction.commit();
        }
    }

    @Override
    public void onStartDownload(String filePath) {
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOADING);
        updateItemData(mDownloadInfo);
    }

    @Override
    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
        Logger.logI(Logger.APPOINTMENT, "onLoadProgressChange");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOADING);
        mDownloadInfo.setCurrentSize(currentSize);
        mDownloadInfo.setTotalSize(totalSize);
        updateItemData(mDownloadInfo);
    }

    @Override
    public void onLoadFailed(String filePath, String reason) {
        Logger.logI(Logger.APPOINTMENT, "onLoadFailed");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_FAILED);
        updateItemData(mDownloadInfo);
    }

    @Override
    public void onLoadSuccessful(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadSuccessful");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_SUCCESS);
        updateItemData(mDownloadInfo);
        mMediaDownloadType = 0;
    }

    @Override
    public void onLoadStopped(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadStopped");
        mDownloadInfo = new MediaDownloadInfo();
        mDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
        updateItemData(mDownloadInfo);
    }

    private void updateItemData(MediaDownloadInfo mDownloadInfo) {
       /* if (mMediaDownloadType == 1) {
            mMedicalAuxilirayAdapter.updateItemData(mDownloadInfo);
        } else if (mMediaDownloadType == 1) {
            mDoctorOrderAuxilirayAdapter.updateItemData(mDownloadInfo);
        }*/
    }

    private RoundsMedicalDetailsInfo getBasicMedicalInfo() {
        return (RoundsMedicalDetailsInfo) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO);
    }

    private boolean isRoom() {
        return getArguments() != null && getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_ROOM);
    }
}
