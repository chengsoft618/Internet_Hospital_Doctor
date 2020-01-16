package cn.longmaster.hospital.doctor.ui.consult.record;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lmmedia.PPAmrPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.ApplyDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisFileInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisAllInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorPictureInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.consult.RecordManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.PicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.adapter.ReportAdapter;
import cn.longmaster.hospital.doctor.ui.rounds.IssueDoctorOrderActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 会诊意见fragment
 * Created by Yang² on 2016/7/18.
 */
public class ConsultReportFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_report_chat_issue_advise_btn)
    private TextView mIssueAdviseBtn;
    @FindViewById(R.id.fragment_report_list_rv)
    private RecyclerView fragmentReportListRv;

    @AppApplication.Manager
    private ConsultManager mConsultManager;
    @AppApplication.Manager
    private RecordManager mRecordManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;


    private PPAmrPlayer mPlayer;
    private ProgressDialog mProgressDialog;
    private ReportAdapter mReportAdapter;
    private ReportHeader mReportHeader;
    private RecordFooter mRecordFooter;

    public static ConsultReportFragment getInstance(AppointmentInfo appointmentInfo, boolean isExperts, boolean isRelateRecord) {
        ConsultReportFragment consultReportFragment = new ConsultReportFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentInfo);
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_EXPERTS, isExperts);
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD, isRelateRecord);
        consultReportFragment.setArguments(bundle);
        return consultReportFragment;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mPlayer = new PPAmrPlayer();
        mReportAdapter = new ReportAdapter(R.layout.item_consult_report, new ArrayList<>(0), mPlayer);
        mReportAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DoctorDiagnosisAllInfo doctorDiagnosisAllInfo = (DoctorDiagnosisAllInfo) adapter.getItem(position);
            if (null != doctorDiagnosisAllInfo) {
                switch (view.getId()) {
                    case R.id.item_report_picture_iv:
                        Intent intent = new Intent(getActivity(), PicBrowseActivity.class);
                        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
                        List<DoctorDiagnosisAllInfo> doctorDiagnosisAllInfos = adapter.getData();
                        List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos = new ArrayList<>(adapter.getItemCount());
                        List<String> urlList = new ArrayList<>(adapter.getItemCount());
                        for (DoctorDiagnosisAllInfo info : doctorDiagnosisAllInfos) {
                            String url = AppConfig.getDfsUrl() + "3010/" + "0/" + info.getDiagnosisPicture() + "/" + info.getAppointmentId();
                            AuxiliaryMaterialInfo auxiliaryMaterialInfo = new AuxiliaryMaterialInfo();
                            auxiliaryMaterialInfo.setMaterialPic(url);
                            auxiliaryMaterialInfos.add(auxiliaryMaterialInfo);
                            urlList.add(url);
                        }
                        browserPicEvent.setAuxiliaryMaterialInfos(auxiliaryMaterialInfos);
                        browserPicEvent.setServerFilePaths(urlList);
                        //browserPicEvent.setAssistant(true);
                        //browserPicEvent.setSingleFileInfos(null);
                        browserPicEvent.setIndex(position);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO, browserPicEvent);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }

        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_consult_report;
    }

    @Override
    public void initViews(View rootView) {
        fragmentReportListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        mReportHeader = new ReportHeader(getActivity(), fragmentReportListRv);
        mReportHeader.setModificationView(isIsExperts(), isRelateRecord());
        mReportHeader.setOnModificationViewClickListener(view -> {
            Intent intent = new Intent(getActivity(), IssueDoctorOrderActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, getAppointmentInfo());
            startActivity(intent);
        });
        mRecordFooter = new RecordFooter(getActivity(), fragmentReportListRv);
        mReportAdapter.addHeaderView(mReportHeader);
        mReportAdapter.addFooterView(mRecordFooter);
        fragmentReportListRv.setAdapter(mReportAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDoctorDiagnosis(getAppointmentId());
        if (null != getAppointmentInfo()) {
            displayAppointmentInfo(getAppointmentInfo());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer = null;
    }

    @OnClick({R.id.fragment_report_chat_issue_advise_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_report_chat_issue_advise_btn:
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), IssueDoctorOrderActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, getAppointmentInfo());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getDoctorDiagnosis(int appointmentId) {
        mRecordManager.getDoctorDiagnosis(appointmentId, (baseResult, doctorDiagnosisInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && doctorDiagnosisInfo != null) {
                List<DoctorDiagnosisAllInfo> doctorDiagnosisAllInfoList = new ArrayList<>();
                if (null != doctorDiagnosisInfo.getDiagnosisFileList()) {
                    for (DiagnosisFileInfo diagnosisFileInfo : doctorDiagnosisInfo.getDiagnosisFileList()) {
                        DoctorDiagnosisAllInfo doctorDiagnosisAllInfo = new DoctorDiagnosisAllInfo(diagnosisFileInfo);
                        doctorDiagnosisAllInfoList.add(doctorDiagnosisAllInfo);
                    }
                }
                for (int i = 0; i < LibCollections.size(doctorDiagnosisInfo.getDiagnosisContentList()); i++) {
                    if (i + 1 == doctorDiagnosisInfo.getDiagnosisContentList().size()) {
                        DoctorDiagnosisAllInfo doctorDiagnosisAllInfo = new DoctorDiagnosisAllInfo(doctorDiagnosisInfo.getDiagnosisContentList().get(i));
                        doctorDiagnosisAllInfoList.add(doctorDiagnosisAllInfo);
                    }
                }
                Logger.logI(Logger.APPOINTMENT, "getDoctorDiagnosis->doctorDiagnosisAllInfoList:" + doctorDiagnosisAllInfoList);

                if (LibCollections.isNotEmpty(doctorDiagnosisAllInfoList)) {
                    mReportAdapter.setNewData(doctorDiagnosisAllInfoList);
                    mIssueAdviseBtn.setVisibility(View.GONE);
                    fragmentReportListRv.setVisibility(View.VISIBLE);
                } else {
                    mIssueAdviseBtn.setVisibility(View.VISIBLE);
                    fragmentReportListRv.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
    }

    /**
     * 获取预约信息
     */
    private void displayAppointmentInfo(AppointmentInfo appointmentInfo) {
        Logger.logI(Logger.APPOINTMENT, "ConsultReportFragment-->getAppointmentInfo->appointmentInfo:" + appointmentInfo);
        if (appointmentInfo.getAppDialogInfo() != null) {
            mRecordFooter.setDate(appointmentInfo.getAppDialogInfo().getGuideReportDt());
        }

        if (appointmentInfo.getApplyDoctorInfoList().size() > 0) {
            for (ApplyDoctorInfo applyDoctorInfo : appointmentInfo.getApplyDoctorInfoList()) {
                if (applyDoctorInfo.getDoctorType() == AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR) {
                    Logger.logI(Logger.APPOINTMENT, "ConsultReportFragment-->getAppointmentInfo->applyDoctorInfo:" + applyDoctorInfo);
                    mDoctorManager.getDoctorInfo(applyDoctorInfo.getDoctorUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
                        @Override
                        public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                            if (doctorBaseInfo != null) {
                                if (LibCollections.isNotEmpty(doctorBaseInfo.getDoctorPictureInfoList())) {
                                    setSignature(doctorBaseInfo.getDoctorPictureInfoList());
                                }
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
        }
    }

    /**
     * 设置签名
     *
     * @param doctorPictureInfoList
     */
    private void setSignature(List<DoctorPictureInfo> doctorPictureInfoList) {
        Collections.sort(doctorPictureInfoList);
        Logger.logI(Logger.APPOINTMENT, "setSignature-->doctorPictureInfoList:" + doctorPictureInfoList);
        for (DoctorPictureInfo doctorPictureInfo : doctorPictureInfoList) {
            if (doctorPictureInfo.getPictureType() == 7) {
                String filePath = SdManager.getInstance().getSignPath(doctorPictureInfo.getPictureName());
                String time = doctorPictureInfo.getInsertDt();
                String year = time.substring(0, 4);
                String month = time.substring(5, 7);
                String day = time.substring(8, 10);
                String url = AppConfig.getDfsUrl() + "3017/" + "0/" + doctorPictureInfo.getPictureName() + "/" + year + month + "/" + day;
                Logger.logI(Logger.APPOINTMENT, "url:" + url);
                mRecordFooter.setSignature(filePath, url);
                return;
            }
        }
    }


    private AppointmentInfo getAppointmentInfo() {
        return getArguments() == null ? null : (AppointmentInfo) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID);
    }

    private boolean isIsExperts() {
        return getArguments() != null && getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_EXPERTS);
    }

    private boolean isRelateRecord() {
        return getArguments() != null && getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD);
    }

    private int getAppointmentId() {
        if (getAppointmentInfo() != null) {
            return getAppointmentInfo().getBaseInfo() == null ? 0 : getAppointmentInfo().getBaseInfo().getAppointmentId();
        } else {
            return 0;
        }
    }
}
