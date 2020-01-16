package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.BasicMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.HospitalAndDepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.MedicalFileInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderCureDtInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderFileInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.doctor.AssistantDoctorRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.CorrelationVisitRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetHospitalAndDepartmentInfoRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.OrderDetailsRequester;
import cn.longmaster.hospital.doctor.ui.PDFViewActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.VideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.rounds.FullyLayoutManager;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsConsultRoomActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDetailActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsRefusalActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsVideoPlayActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.ResourcesMaterialAdapter;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.RoundsMedicalAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 订单详情fragment
 * <p>
 * Created by W·H·K on 2019/2/13.
 */
public class OrderDetailsFragment extends NewBaseFragment {
    @FindViewById(R.id.fg_rounds_detail_appointment_num_tv)
    private TextView fgRoundsDetailAppointmentNumTv;
    @FindViewById(R.id.fg_rounds_detail_state_tv)
    private TextView mStateTv;
    @FindViewById(R.id.fg_rounds_detail_state_v)
    private View fgRoundsDetailStateV;
    @FindViewById(R.id.fg_rounds_detail_lecture_topics_tv)
    private TextView mLectureTopicsTv;
    @FindViewById(R.id.fg_rounds_detail_hospital_department_tv)
    private TextView mHospitalDepartmentTv;
    @FindViewById(R.id.fg_rounds_detail_time_tv)
    private TextView mRoundsTimeTv;
    @FindViewById(R.id.fg_rounds_detail_department_tv)
    private TextView mDepartmentTv;
    @FindViewById(R.id.fg_rounds_detail_time_length_tv)
    private TextView mTimeLengthTv;
    @FindViewById(R.id.fg_rounds_detail_need_ppt_tv)
    private TextView mNeedPPTTv;
    @FindViewById(R.id.fg_rounds_detail_doctor_assistant_tv)
    private TextView mDoctorAssistantTv;
    @FindViewById(R.id.fg_rounds_detail_appeal_tv)
    private TextView mRoundsAppealTv;
    @FindViewById(R.id.fg_rounds_detail_patient_ll)
    private LinearLayout fgRoundsDetailPatientLl;
    @FindViewById(R.id.fg_rounds_detail_patient_rv)
    private RecyclerView fgRoundsDetailPatientRv;
    @FindViewById(R.id.fg_rounds_detail_hospital_experts_tv)
    private TextView mHospitalExpertsTv;
    @FindViewById(R.id.fg_rounds_detail_intention_department_tr)
    private TableRow mIntentionDepartmentTab;
    @FindViewById(R.id.fg_rounds_detail_intention_department_line)
    private View mIntentionDepartmentLine;
    @FindViewById(R.id.fg_rounds_detail_teach_material_ll)
    private LinearLayout fgRoundsDetailTeachMaterialLl;
    @FindViewById(R.id.fg_rounds_detail_teach_material_rv)
    private RecyclerView fgRoundsDetailTeachMaterialRv;
    @FindViewById(R.id.fg_rounds_detail_add_patient_stv)
    private SuperTextView mAddPatientView;
    @FindViewById(R.id.fg_rounds_detail_receive_ll)
    private LinearLayout mBottomView;
    @FindViewById(R.id.fg_rounds_detail_room_iv)
    private ImageView mBottomRoomView;
    @FindViewById(R.id.act_rounds_detail_accept_doctor_info_rl)
    private RelativeLayout actRoundsDetailAcceptDoctorInfoRl;
    @FindViewById(R.id.fg_rounds_detail_receive_doctor_tv)
    private TextView mTopReceiveDoctorView;
    @FindViewById(R.id.act_rounds_detail_receive_time_desc_tv)
    private TextView mTopReceiveTimeTv;
    @FindViewById(R.id.act_rounds_detail_receive_time_tv)
    private TextView actRoundsDetailReceiveTimeTv;

    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private ArrayList<OrderCureDtInfo> mReceiveTimeList = new ArrayList<>();
    private int mAtthosId;
    private boolean mIsRoom;
    private boolean mIsHaveAuthority;
    private RoundsMedicalAdapter mMedicalAdapter;
    private OrderDetailsInfo mOrderDetailsInfo;
    private boolean isExperts = false;
    private String mDoctorHospitalName = "";
    private String mDoctorDepartmentName = "";
    private int mUserType;
    //添加患者
    private final int REQUEST_CODE_FOR_ADD_PATIENT = 256;
    private int currentDoctorId;

    public static OrderDetailsFragment getInstance(OrderDetailsInfo orderDetailsInfo) {
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO, orderDetailsInfo);
        orderDetailsFragment.setArguments(bundle);
        return orderDetailsFragment;
    }

    private OrderDetailsInfo getOrderDetailsInfo() {
        return (OrderDetailsInfo) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO);
    }

    private int getOrderId() {
        return getOrderDetailsInfo() == null ? 0 : getOrderDetailsInfo().getOrderId();
    }

    @Override
    protected void initDatas() {
        mAtthosId = getOrderDetailsInfo().getLaunchHospital();
        mIsRoom = ((RoundsDetailActivity) getBaseActivity()).getIsRoom();
        mIsHaveAuthority = ((RoundsDetailActivity) getBaseActivity()).getIsHaveAuthority();
        currentDoctorId = mUserInfoManager.getCurrentUserInfo().getUserId();
        mMedicalAdapter = new RoundsMedicalAdapter(R.layout.item_order_details_medical, new ArrayList<>(0));
        mMedicalAdapter.setOnPicClickListener((adapter, view, position, serverUrls) -> {
            MedicalFileInfo info = (MedicalFileInfo) adapter.getItem(position);
            if (null != info) {
                switch (info.getType()) {
                    case AppConstant.MaterailType.MATERAIL_TYPE_PIC:
                        getDisplay().startFirstJourneyPicBrowseActivity(serverUrls, null, false, position, 0, 0);
                        break;

                    case AppConstant.MaterailType.MATERAIL_TYPE_DICOM:
                    case AppConstant.MaterailType.MATERAIL_TYPE_WSI:
                        getDisplay().startBrowserActivity(AppConfig.getDfsUrl() + "3004/1/" + info.getFileName(), null, false, false, 0, 0);
                        break;

                    case AppConstant.MaterailType.MATERAIL_TYPE_MEDIA:
                        String path = "";
                        String url = "";
                        if (!TextUtils.isEmpty(info.getFileName())) {
                            path = SdManager.getInstance().getOrderVideoPath(info.getFileName(), info.getMedicalId() + "");
                            url = AppConfig.getDfsUrl() + "3004/1/" + info.getFileName();
                        }
           /* intent.setClass(mContext, VideoPlayerActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, "");
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getType());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
            mContext.startActivity(intent);*/
                        Logger.logD(Logger.COMMON, "RoundsMedicalAdapter-->playVideo:path:" + url);
                        Intent intent = new Intent(getBaseActivity(), RoundsVideoPlayActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, path);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        /*if (mOrderDetailsInfo != null) {
            mReceiveTimeList = mOrderDetailsInfo.getOrderCureDtInfos();
            displayView(mOrderDetailsInfo);
            getHospitalAndDepartmentInfo();
        }*/
        mMedicalAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            RoundsPatientInfo roundsPatientInfo = (RoundsPatientInfo) adapter.getItem(position);
            if (null != roundsPatientInfo) {
                switch (view.getId()) {
                    case R.id.item_order_details_delete_iv:
                        if (!mIsHaveAuthority && mOrderDetailsInfo.getDoctorId() != currentDoctorId && mOrderDetailsInfo.getLaunchDoctorId() != currentDoctorId) {
                            ToastUtils.showShort("您当前身份不能删除患者");
                        } else {
                            showDeleteDialog(roundsPatientInfo);
                        }
                        break;
                    case R.id.item_order_details_data_details_tv:
                        startRoundsPatientInfoActivity(mOrderDetailsInfo, roundsPatientInfo);
                        break;
                    case R.id.item_order_details_data_management_tv:
                        getDisplay().startRoundsDataManagerActivity(roundsPatientInfo.getMedicalId(), false, false, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_order_details_fragment;
    }

    @Override
    public void initViews(View rootView) {
        fgRoundsDetailPatientRv.setHasFixedSize(true);
        fgRoundsDetailPatientRv.setLayoutManager(new FullyLayoutManager(getActivity()));
        fgRoundsDetailPatientRv.setNestedScrollingEnabled(false);
        fgRoundsDetailPatientRv.setAdapter(mMedicalAdapter);

        mOrderDetailsInfo = getOrderDetailsInfo();
        mReceiveTimeList = (ArrayList<OrderCureDtInfo>) mOrderDetailsInfo.getOrderCureDtInfos();
        displayOrderDetails(mOrderDetailsInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_ADD_PATIENT) {

            }
        }
    }

    @OnClick({R.id.fg_rounds_detail_receive_tv,
            R.id.fg_rounds_detail_no_receive_tv,
            R.id.fg_rounds_detail_room_iv,
            R.id.fg_rounds_detail_add_patient_stv,
            R.id.fg_rounds_detail_hospital_department_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.fg_rounds_detail_receive_tv:
                getDisplay().startReceiveActivity(getOrderId(), mAtthosId, mReceiveTimeList);
                break;
            case R.id.fg_rounds_detail_room_iv:
                intent.setClass(getActivity(), RoundsConsultRoomActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, getOrderId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_USER_TYPE, mUserType);
                startActivity(intent);
                break;
            case R.id.fg_rounds_detail_no_receive_tv:
                intent.setClass(getActivity(), RoundsRefusalActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, getOrderId());
                startActivity(intent);
                break;

            case R.id.fg_rounds_detail_add_patient_stv:
                getDisplay().startWaitRoundsPatientActivity(getOrderId(), null, REQUEST_CODE_FOR_ADD_PATIENT);
                break;
            case R.id.fg_rounds_detail_hospital_department_tv:
                if (StringUtils.isEmpty(mHospitalDepartmentTv.getText().toString().trim())) {
                    return;
                }
                if (mHospitalExpertsTv.getText().toString().equals(getString(R.string.rounds_hospital_department))) {
                    showIntroductionDialog();
                } else {
                    intent.setClass(getActivity(), BrowserActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "doctor/info/doc_id/" + mOrderDetailsInfo.getDoctorId() + "/mark/1.html");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void getOrderDetails(int orderId) {
        if (orderId != 0) {
            OrderDetailsRequester requester = new OrderDetailsRequester(new DefaultResultCallback<OrderDetailsInfo>() {
                @Override
                public void onSuccess(OrderDetailsInfo orderDetailsInfo, BaseResult baseResult) {
                    Logger.logI(Logger.COMMON, "GetCommonlyDepartmentRequester：baseResult" + baseResult + " , orderDetailsInfo" + orderDetailsInfo);
                    if (null != orderDetailsInfo) {
                        mOrderDetailsInfo = orderDetailsInfo;
                        mReceiveTimeList = (ArrayList<OrderCureDtInfo>) orderDetailsInfo.getOrderCureDtInfos();
                        displayOrderDetails(mOrderDetailsInfo);
                    }
                }
            });
            requester.orderId = orderId;
            requester.doPost();
        }
    }

    private void displayOrderDetails(OrderDetailsInfo orderDetailsInfo) {
        displayView(orderDetailsInfo);
        setUserType(orderDetailsInfo);
    }

    private void setUserType(OrderDetailsInfo mOrderDetailsInfo) {
        if (currentDoctorId == mOrderDetailsInfo.getDoctorId()) {
            mUserType = AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;
        } else if (currentDoctorId == mOrderDetailsInfo.getLaunchDoctorId()) {
            mUserType = AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR;
        } else {
            mUserType = AppConstant.UserType.USER_TYPE_LIVE_BROADCAST_WATCH;
        }
    }

    private void displayView(final OrderDetailsInfo orderDetailsInfo) {
        fgRoundsDetailAppointmentNumTv.setText(orderDetailsInfo.getOrderId() + "");
        mNeedPPTTv.setText(orderDetailsInfo.isNeedPpt() ? R.string.yes : R.string.no);
        mRoundsAppealTv.setText(orderDetailsInfo.getVisitAppeal());
        mTimeLengthTv.setText(orderDetailsInfo.getIntentionDuration() + "小时");
        setThemeTv(orderDetailsInfo);
        setRoleView(orderDetailsInfo);
        setAssistDoctor(orderDetailsInfo.getDgwsUid());
        setMedicalView(orderDetailsInfo);
        setTimeView(orderDetailsInfo);
        setOrderState(orderDetailsInfo);
        displayOrderFileView(orderDetailsInfo);
        if (orderDetailsInfo.getOrderState() != AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED && orderDetailsInfo.getOrderState() != AppConstant.AppointmentState.APPOINTMENT_FINISHED) {
            if (!mIsHaveAuthority && orderDetailsInfo.getDoctorId() != currentDoctorId && orderDetailsInfo.getLaunchDoctorId() != currentDoctorId) {
                mAddPatientView.setVisibility(View.GONE);
            } else {
                mAddPatientView.setVisibility(View.VISIBLE);
            }
        } else {
            mAddPatientView.setVisibility(View.GONE);
        }
    }

    private void setThemeTv(OrderDetailsInfo orderDetailsInfo) {
        if (orderDetailsInfo.isNeedPpt()) {
            if (LibCollections.isEmpty(orderDetailsInfo.getRoundsPatientInfos())) {
                mLectureTopicsTv.setText(StringUtils.isEmpty(orderDetailsInfo.getOrderTitle()) ? "" : orderDetailsInfo.getOrderTitle() + "课件专题教学");
            } else {
                mLectureTopicsTv.setText(StringUtils.isEmpty(orderDetailsInfo.getOrderTitle()) ? "" : orderDetailsInfo.getOrderTitle() + "课件专题教学及"
                        + LibCollections.size(orderDetailsInfo.getRoundsPatientInfos()) + "例教学查房");
            }
        } else {
            if (LibCollections.isEmpty(orderDetailsInfo.getRoundsPatientInfos())) {
                mLectureTopicsTv.setText(StringUtils.isEmpty(orderDetailsInfo.getOrderTitle()) ? "" : orderDetailsInfo.getOrderTitle());
            } else {
                mLectureTopicsTv.setText(LibCollections.size(orderDetailsInfo.getRoundsPatientInfos()) + "例教学查房");
            }
        }
    }

    /**
     * 设置上级专家或首诊进入页面动态显示view
     *
     * @param orderDetailsInfo
     */
    private void setRoleView(OrderDetailsInfo orderDetailsInfo) {
        if (orderDetailsInfo.getDoctorId() == currentDoctorId) {//自己是上级专家
            isExperts = true;
            mHospitalExpertsTv.setText(R.string.rounds_hospital_department);
            mDoctorManager.getHospitalInfo(orderDetailsInfo.getLaunchHospital(), true, new DoctorManager.OnHospitalInfoLoadListener() {
                @Override
                public void onSuccess(HospitalInfo hospitalInfo) {
                    Logger.logI(Logger.COMMON, "getHospitalInfo：hospitalInfo" + hospitalInfo);
                    if (hospitalInfo != null) {
                        mDoctorHospitalName = hospitalInfo.getHospitalName();
                        mHospitalDepartmentTv.setText(getUnderLine(getString(R.string.rounds_consultation_doctor_info, StringUtils.isEmpty(mDoctorHospitalName) ? "" : mDoctorHospitalName, StringUtils.isEmpty(mDoctorDepartmentName) ? "" : mDoctorDepartmentName)));
                    }
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
            mDoctorManager.getDepartmentInfo(orderDetailsInfo.getLaunchHosdepId(), true, new DoctorManager.OnDepartmentInfoLoadListener() {
                @Override
                public void onSuccess(DepartmentInfo departmentInfo) {
                    Logger.logI(Logger.COMMON, "getHospitalInfo：departmentInfo" + departmentInfo);
                    if (departmentInfo != null) {
                        mDoctorDepartmentName = departmentInfo.getDepartmentName();
                        mHospitalDepartmentTv.setText(getUnderLine(getString(R.string.rounds_consultation_doctor_info, StringUtils.isEmpty(mDoctorHospitalName) ? "" : mDoctorHospitalName, StringUtils.isEmpty(mDoctorDepartmentName) ? "" : mDoctorDepartmentName)));
                    }
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
            mIntentionDepartmentTab.setVisibility(View.GONE);
            mIntentionDepartmentLine.setVisibility(View.GONE);
        } else {
            isExperts = false;
            mHospitalExpertsTv.setText(R.string.rounds_mould_info_rounds_expert);
            mDoctorManager.getDoctorInfo(orderDetailsInfo.getDoctorId(), true, new DoctorManager.OnDoctorInfoLoadListener() {
                @Override
                public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                    if (doctorBaseInfo != null) {
                        mHospitalDepartmentTv.setText(getUnderLine(doctorBaseInfo.getRealName()));
                    }
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
            if (LibCollections.isEmpty(orderDetailsInfo.getDepartmentListInfos())) {
                mIntentionDepartmentTab.setVisibility(View.GONE);
                mIntentionDepartmentLine.setVisibility(View.GONE);
            } else {
                mIntentionDepartmentTab.setVisibility(View.VISIBLE);
                mIntentionDepartmentLine.setVisibility(View.VISIBLE);
                setDepartmentView(orderDetailsInfo);
            }
        }
    }

    private void setDepartmentView(OrderDetailsInfo orderDetailsInfo) {
        String str = "";
        for (int i = 0; i < orderDetailsInfo.getDepartmentListInfos().size(); i++) {
            str += orderDetailsInfo.getDepartmentListInfos().get(i).getDepartmentName() + ",";
        }
        mDepartmentTv.setText(str.substring(0, str.length() - 1));
    }

    /**
     * 获取医生助理信息
     *
     * @param assistId
     */
    private void setAssistDoctor(int assistId) {
        AssistantDoctorRequester requester = new AssistantDoctorRequester(new DefaultResultCallback<AssistantDoctorInfo>() {
            @Override
            public void onSuccess(AssistantDoctorInfo assistantDoctorInfo, BaseResult baseResult) {
                mDoctorAssistantTv.setText(assistantDoctorInfo.getUserName());
            }
        });
        requester.assistantId = assistId;
        requester.doPost();
    }

    /**
     * 设置病历view
     *
     * @param orderDetailsInfo
     */
    private void setMedicalView(OrderDetailsInfo orderDetailsInfo) {
        if (orderDetailsInfo.getRoundsPatientInfos() != null) {
            mMedicalAdapter.setOrderState(orderDetailsInfo.getOrderState());
            mMedicalAdapter.setExperts(isExperts);
            if (LibCollections.isNotEmpty(orderDetailsInfo.getRoundsPatientInfos())) {
                fgRoundsDetailPatientRv.setVisibility(View.VISIBLE);
                fgRoundsDetailPatientLl.setVisibility(View.VISIBLE);
                mMedicalAdapter.setNewData(orderDetailsInfo.getRoundsPatientInfos());
            } else {
                fgRoundsDetailPatientLl.setVisibility(View.GONE);
                fgRoundsDetailPatientRv.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 设置意向时间view
     *
     * @param orderDetailsInfo
     */
    private void setTimeView(OrderDetailsInfo orderDetailsInfo) {
        if (orderDetailsInfo.getOrderCureDtInfos() != null && orderDetailsInfo.getOrderCureDtInfos().size() > 0) {
            String str = "";
            for (int i = 0; i < orderDetailsInfo.getOrderCureDtInfos().size(); i++) {
                if (orderDetailsInfo.getOrderCureDtInfos().get(i).getIsUse() != 1) {
                    str += displayTimeView(orderDetailsInfo.getOrderCureDtInfos().get(i).getCureDt(), false) + "\n";
                }
            }
            if (str.length() > 1) {
                mRoundsTimeTv.setText(str.substring(0, str.length() - 1));
            } else {
                mRoundsTimeTv.setText("");
            }
        } else {
            mRoundsTimeTv.setText("");
        }
    }

    /**
     * 设置订单状态view
     *
     * @param orderDetailsInfo
     */
    private void setOrderState(OrderDetailsInfo orderDetailsInfo) {
        if (AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED == orderDetailsInfo.getOrderState() || AppConstant.AppointmentState.APPOINTMENT_FINISHED == orderDetailsInfo.getOrderState()) {
            fgRoundsDetailStateV.setBackgroundResource(R.drawable.bg_solid_666666_radius_45);
            mStateTv.setTextColor(getCompatColor(R.color.color_666666));
        } else {
            fgRoundsDetailStateV.setBackgroundResource(R.drawable.bg_solid_fc8404_radius_45);
            mStateTv.setTextColor(getCompatColor(R.color.color_fc8404));
        }
        mStateTv.setText(getOrderState(orderDetailsInfo.getOrderState()));
        if ((orderDetailsInfo.getOrderState() == AppConstant.AppointmentState.WAIT_ASSISTANT_CALL ||
                orderDetailsInfo.getOrderState() == AppConstant.AppointmentState.DATA_CHECK_FAIL) &&
                orderDetailsInfo.getDoctorId() == currentDoctorId && !mIsRoom) {
            mBottomView.setVisibility(View.VISIBLE);
        } else {
            mBottomView.setVisibility(View.GONE);
        }
        if (orderDetailsInfo.getOrderState() != AppConstant.AppointmentState.WAIT_ASSISTANT_CALL &&
                orderDetailsInfo.getOrderState() != AppConstant.AppointmentState.DATA_CHECK_FAIL && !mIsRoom) {
            mBottomRoomView.setVisibility(View.VISIBLE);
        } else {
            mBottomRoomView.setVisibility(View.GONE);
        }

        if (orderDetailsInfo.getDoctorId() == 0 || orderDetailsInfo.getOrderState() == AppConstant.AppointmentState.WAIT_ASSISTANT_CALL || orderDetailsInfo.getOrderState() == AppConstant.AppointmentState.DATA_CHECK_FAIL) {
            actRoundsDetailAcceptDoctorInfoRl.setVisibility(View.GONE);
        } else {
            actRoundsDetailAcceptDoctorInfoRl.setVisibility(View.VISIBLE);
            mDoctorManager.getDoctorInfo(orderDetailsInfo.getDoctorId(), true, new DoctorManager.OnDoctorInfoLoadListener() {
                @Override
                public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                    if (doctorBaseInfo != null) {
                        mTopReceiveDoctorView.setText(doctorBaseInfo.getRealName() + "医生");
                    }
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
            for (int i = 0; i < orderDetailsInfo.getOrderCureDtInfos().size(); i++) {
                if (orderDetailsInfo.getOrderCureDtInfos().get(i).getIsUse() == 1) {
                    mTopReceiveTimeTv.setText(displayTimeView(orderDetailsInfo.getOrderCureDtInfos().get(i).getCureDt(), true));
                    break;
                }
            }
        }
    }

    /**
     * 设置教学资料view
     *
     * @param orderDetailsInfo
     */
    private void displayOrderFileView(final OrderDetailsInfo orderDetailsInfo) {
        if (LibCollections.isNotEmpty(orderDetailsInfo.getOrderFileInfos())) {
            fgRoundsDetailTeachMaterialLl.setVisibility(View.VISIBLE);
            fgRoundsDetailTeachMaterialRv.setVisibility(View.VISIBLE);
            ResourcesMaterialAdapter resourcesMaterialAdapter = new ResourcesMaterialAdapter(R.layout.item_rounds_tech_material, orderDetailsInfo.getOrderFileInfos());
            fgRoundsDetailTeachMaterialRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getBaseActivity(), 4));
            fgRoundsDetailTeachMaterialRv.setAdapter(resourcesMaterialAdapter);
            resourcesMaterialAdapter.setOnItemClickListener((adapter, view, position) -> {
                OrderFileInfo orderFileInfo = (OrderFileInfo) adapter.getItem(position);
                if (null != orderFileInfo) {
                    if (orderFileInfo.getType() == 1) {
                        if (orderFileInfo.getFileName().endsWith(".pdf")) {
                            PDFViewActivity.startActivity(getActivity(), orderFileInfo.getFileName(), orderFileInfo.getRemark());
                        } else {
                            Intent intent = new Intent(getActivity(), BrowserActivity.class);
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, orderFileInfo.getRemark());
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, true);
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, orderFileInfo.getFileName());
                            startActivity(intent);
                        }
                    } else {
                        playVideo(orderDetailsInfo.getOrderFileInfos().get(position));
                    }
                }
            });
        } else {
            fgRoundsDetailTeachMaterialLl.setVisibility(View.GONE);
            fgRoundsDetailTeachMaterialRv.setVisibility(View.GONE);
        }
    }

    private void playVideo(OrderFileInfo orderFileInfo) {
        String path = "";
        String url = "";
        if (!TextUtils.isEmpty(orderFileInfo.getFileName())) {
            path = SdManager.getInstance().getOrderVideoPath(orderFileInfo.getFileName(), orderFileInfo.getOrderId() + "");
            url = AppConfig.getDfsUrl() + "3004/1/" + orderFileInfo.getFileName();
        }
        Logger.logD(Logger.APPOINTMENT, "-->path:" + path + "  ,url:" + url);
        Intent intent = new Intent();
        intent.setClass(getActivity(), VideoPlayerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, orderFileInfo.getRemark());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, orderFileInfo.getType());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
        startActivity(intent);
    }

    private String displayTimeView(String cureDt, boolean isAlreadyReceived) {
        // TODO: 2019/6/24 简化代码
        String str = "";
        try {
            String timeStr = cureDt;
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm");
            Date date = format.parse(timeStr);//有异常要捕获
            SimpleDateFormat yearF = new SimpleDateFormat("MM月dd");
            String newD = yearF.format(date) + "日 ";
            SimpleDateFormat yearD = new SimpleDateFormat("HH:mm");
            str = newD + CommonlyUtil.getWeek(date, getActivity()) + " " + yearD.format(date);
            Logger.logI(Logger.COMMON, "shwDatePopupWindow-MyGridViewAdapter：时间转换" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public SpannableStringBuilder getUnderLine(String str) {
        if (StringUtils.isTrimEmpty(str)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableStringBuilder.setSpan(underlineSpan, 0, StringUtils.length(str), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private void startRoundsPatientInfoActivity(OrderDetailsInfo orderDetailsInfo, RoundsPatientInfo patientInfo) {
        BasicMedicalInfo info = new BasicMedicalInfo();
        info.setDoctorId(orderDetailsInfo.getDoctorId());
        info.setDgwsUid(orderDetailsInfo.getDgwsUid());
        info.setOrderId(getOrderId());
        info.setOrderState(orderDetailsInfo.getOrderState());
        info.setMedicalId(patientInfo.getMedicalId());
        info.setVisitUrl(patientInfo.getVisitUrl());
        info.setRoom(mIsRoom);
        info.setExperts(isExperts);
        info.setHaveAuthority(mIsHaveAuthority);
        info.setUserType(mUserType);
        getDisplay().startRoundsPatientInfoActivity(info, 0);
    }

    private void showDeleteDialog(RoundsPatientInfo patientInfo) {
        new CommonDialog.Builder(getActivity())
                .setMessage(R.string.rounds_medical_record_module_is_delete)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> relationVisitRequester(patientInfo.getMedicalId(), getOrderId()))
                .show();
    }

    private void relationVisitRequester(int medicalId, int orderid) {
        ProgressDialog mProgressDialog = createProgressDialog(getString(R.string.loading));
        mProgressDialog.show();
        CorrelationVisitRequester requester = new CorrelationVisitRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                getOrderDetails(orderid);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
        requester.actType = 1;
        requester.orderId = orderid;
        requester.medicalId = medicalId;
        requester.doPost();
    }

    private void showIntroductionDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_hospital_introduction, null);
        TextView departmentTitleTv = view.findViewById(R.id.dialog_introduction_department_title_tv);
        TextView introductionDepartmentTv = view.findViewById(R.id.dialog_introduction_department_tv);
        TextView hospitalTitleTv = view.findViewById(R.id.dialog_introduction_hospital_title_tv);
        TextView introductionHospitalTv = view.findViewById(R.id.dialog_introduction_hospital_tv);
        Button closeBtn = view.findViewById(R.id.dialog_introduction_close_btn);
        final Dialog mIntroductionDialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        mIntroductionDialog.show();
        mIntroductionDialog.setContentView(view);
        mIntroductionDialog.setCanceledOnTouchOutside(true);
        mIntroductionDialog.setCancelable(true);
        Window win = mIntroductionDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroductionDialog.dismiss();
            }
        });
        displayIntroductionView(introductionHospitalTv, introductionDepartmentTv, departmentTitleTv, hospitalTitleTv);
    }

    private void displayIntroductionView(final TextView introductionHospitalTv, final TextView introductionDepartmentTv, final TextView departmentTv, final TextView hospitalTv) {
        GetHospitalAndDepartmentInfoRequester requester = new GetHospitalAndDepartmentInfoRequester(new OnResultListener<HospitalAndDepartmentInfo>() {
            @Override
            public void onResult(BaseResult baseResult, HospitalAndDepartmentInfo hospitalAndDepartmentInfo) {
                Logger.logI(Logger.COMMON, "getHospitalAndDepartmentInfo：baseResult" + baseResult + " , hospitalAndDepartmentInfo" + hospitalAndDepartmentInfo);
                if (baseResult.getCode() == 0 && hospitalAndDepartmentInfo != null) {
                    if (hospitalAndDepartmentInfo.getDepartmentIntroductionInfo() != null) {
                        introductionDepartmentTv.setText(Html.fromHtml(hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getIntroduction()));
                        introductionDepartmentTv.setMovementMethod(LinkMovementMethod.getInstance());
                        String string = introductionDepartmentTv.getText().toString();
                        Logger.logI(Logger.COMMON, "displayIntroductionView：introductionDepartmentTv:" + string);
                        introductionDepartmentTv.setText(Html.fromHtml(string));
                        introductionDepartmentTv.setText(Html.fromHtml(introductionDepartmentTv.getText().toString()));
                    }
                    if (hospitalAndDepartmentInfo.getHospitalIntroductionInfo() != null) {
                        introductionHospitalTv.setText(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalDesc());
                        hospitalTv.setText(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName() + "简介");
                    }
                    departmentTv.setText(((hospitalAndDepartmentInfo.getHospitalIntroductionInfo() == null || StringUtils.isEmpty(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName())) ? "" : hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName()) +
                            " " + ((hospitalAndDepartmentInfo.getDepartmentIntroductionInfo() == null || StringUtils.isEmpty(hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getDepartmentName())) ? "" : hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getDepartmentName() + "简介"));
                }
            }
        });
        requester.hospitalId = mOrderDetailsInfo.getLaunchHospital();
        requester.hosdepId = mOrderDetailsInfo.getLaunchHosdepId();
        requester.doPost();
    }

    private String getOrderState(int state) {
        String orderState = "";
        switch (state) {
            case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                orderState = getString(R.string.rounds_waiting_diagnosis);
                break;

            case AppConstant.AppointmentState.DATA_CHECK_FAIL:
                orderState = getString(R.string.rounds_wait_receive);
                break;

            case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                orderState = getString(R.string.wait_for_consult);
                break;

            case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED:
            case AppConstant.AppointmentState.APPOINTMENT_FINISHED:
                orderState = getString(R.string.appoint_state_complete);
                break;
        }
        return orderState;
    }
}
