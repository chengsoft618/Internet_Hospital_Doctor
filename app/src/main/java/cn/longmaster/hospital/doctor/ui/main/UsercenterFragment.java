package cn.longmaster.hospital.doctor.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.GetOrderInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.RoomListInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.user.UsercenterMenu;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.consult.VideoLiveCheckRequest;
import cn.longmaster.hospital.doctor.core.requests.rounds.OrderDetailsRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.LoginActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.video.ConsultRoomActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsConsultRoomActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.ui.user.MeetingRoomActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户中心fragment
 * Created by Yang² 2016/5/30.
 * Mod by biao on 2019/7/1
 */
public class UsercenterFragment extends NewBaseFragment implements MessageStateChangeListener {
    @FindViewById(R.id.fragment_user_center_doctor_photo_iv)
    private CircleImageView mDoctorPhotoIv;
    @FindViewById(R.id.fragment_user_center_doctor_name_tv)
    private TextView mDoctorNameTv;
    @FindViewById(R.id.fragment_user_center_doctor_account_tv)
    private TextView mDoctorAccountTv;
    @FindViewById(R.id.fg_user_center_doctor_real_state_iv)
    private ImageView fgUserCenterDoctorRealStateIv;
    @FindViewById(R.id.fragment_user_toast)
    private LinearLayout mToastV;
    @FindViewById(R.id.fragment_user_toast_progress_bar)
    private ProgressBar mProgressBar;
    @FindViewById(R.id.fragment_user_toast_content)
    private TextView mTostContentTv;

    @FindViewById(R.id.fg_user_center_menu_rv)
    private RecyclerView fgUserCenterMenuRv;
    @FindViewById(R.id.fg_user_center_expand_menu_rv)
    private RecyclerView fgUserCenterExpandMenuRv;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private DcpManager mDcpManager;
    @AppApplication.Manager
    private AccountManager mAccountManager;
    @AppApplication.Manager
    private AuthenticationManager mAuthenticationManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;
    @AppApplication.Manager
    private DCManager dcManager;
    private String mPhotoUrl;
    private ProgressDialog mProgressDialog;
    private int mIdentity = -100;
    private boolean isShowing = false;
    private Dialog mMeetingDialog;
    private Dialog mMeetingPasswordDialog;
    private Dialog mLoadingDialog;
    private EditText mMeetingInputEdt;
    private TextView mInputCancel;
    private Button mInputGoIn;
    private UsercenterMenuAdapter usercenterMenuAdapter;
    private UsercenterMenuAdapter usercenterExpandMenuAdapter;
    @AppApplication.Manager
    private DoctorManager doctorManager;
    private List<DCProjectInfo> mDcProjectInfos;
    private List<DCProjectInfo> mDcProjectManagerInfos;

    @Override
    protected void initDatas() {
        super.initDatas();
        mMessageManager.regMsgStateChangeListener(this);
        usercenterMenuAdapter = new UsercenterMenuAdapter(R.layout.item_user_center_menu, new ArrayList<>(0));
        usercenterMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
            UsercenterMenu menu = (UsercenterMenu) adapter.getItem(position);
            if (null != menu) {
                openMenu(menu);
            }
        });
        usercenterExpandMenuAdapter = new UsercenterMenuAdapter(R.layout.item_user_center_menu, new ArrayList<>(0));
        usercenterExpandMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
            UsercenterMenu menu = (UsercenterMenu) adapter.getItem(position);
            if (null != menu) {
                openMenu(menu);
            }
        });
    }

    private void openMenu(UsercenterMenu menu) {
        Display display = getDisplay();
        if (null == display) {
            return;
        }
        switch (menu.getId()) {
            case R.string.user_center_menu_assess:
                display.startMyAssessActivity();
                break;
            case R.string.user_center_menu_data_center:
                display.startBrowserActivity(AppConfig.getAdwsUrl() + "VsHosmgt/index/user_id/" + mUserInfoManager.getCurrentUserInfo().getUserId() + "/c_auth/" + SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH)
                        , null, false, false, 0, 0);
                break;
            case R.string.user_center_menu_live_video:
                displayInputWindow();
                break;
            case R.string.user_center_menu_meeting_room:
                showMeetingDialog();
                break;
            case R.string.user_center_menu_message:
                display.startMessageCenterActivity();
                break;
            case R.string.user_center_menu_my_account:
                boolean inquiryAccount = SPUtils.getInstance().getBoolean(AppPreference.KEY_INQUIRY_ACCOUNT_INFO, false);
                if (inquiryAccount) {
                    display.startMyAccountActivity(mUserInfoManager.getCurrentUserInfo().getUserId(), false, 0);
                } else {
                    display.startAccountVerificationActivity();
                }
                break;
            case R.string.user_center_menu_patient_manager:
                display.startPatientManagerActivity();
                break;
            case R.string.user_center_menu_represent:
                display.startRepresentFunctionActivity();
                break;
            case R.string.user_center_menu_set:
                display.startSettingActivity();
                break;
            case R.string.user_center_menu_upload_material:
                display.startUploadPictureActivity(true, null, 0);
                break;
            case R.string.user_center_menu_duty_clinic:
                display.startDCDutyListActivity();
                break;
            case R.string.user_center_menu_duty_manager:
                display.startDCDutyProjectDetailActivity(0);
                break;
            default:
                break;
        }
    }

    private List<UsercenterMenu> getUserCenterMenu(int identity) {
        List<UsercenterMenu> usercenterMenus = new ArrayList<>();
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_message));
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_assess));
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_patient_manager));
        if ((identity & 1) == 1) {
            usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_live_video));
        }
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_data_center));
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_my_account));
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_set));
        return usercenterMenus;
    }

    private List<UsercenterMenu> getUserCenterExpandMenu(int identity) {
        List<UsercenterMenu> usercenterMenus = new ArrayList<>();
        if ((identity & 32) != 0) {
            usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_represent));
        }
        if ((identity & 4) != 0) {
            usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_meeting_room));
        }
        usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_upload_material));
        if (LibCollections.isNotEmpty(mDcProjectManagerInfos)) {
            usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_duty_manager));
        }
        if (LibCollections.isNotEmpty(mDcProjectInfos)) {
            usercenterMenus.add(UsercenterMenuHelper.getInstance(getBaseActivity()).getMenuById(R.string.user_center_menu_duty_clinic));
        }
        return usercenterMenus;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_usercenter;
    }

    @Override
    public void initViews(View rootView) {
        Logger.logI(Logger.USER, "regMsgStateChangeListener--->");
        fgUserCenterMenuRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getBaseActivity(), 4));
        fgUserCenterMenuRv.setNestedScrollingEnabled(false);
        fgUserCenterMenuRv.setHasFixedSize(true);
        fgUserCenterMenuRv.setAdapter(usercenterMenuAdapter);

        fgUserCenterExpandMenuRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getBaseActivity(), 4));
        fgUserCenterExpandMenuRv.setNestedScrollingEnabled(false);
        fgUserCenterExpandMenuRv.setHasFixedSize(true);
        fgUserCenterExpandMenuRv.setAdapter(usercenterExpandMenuAdapter);
        fgUserCenterDoctorRealStateIv.setOnClickListener(v -> {
            ProgressDialog progressDialog = createProgressDialog("加载中...");
            progressDialog.show();
            mAuthenticationManager.getAuthenticationInfo(new AuthenticationManager.GetAuthenticationListener() {
                @Override
                public void onGetAuthentication() {
                    String stringBuilder = AppConfig.getQualificationUrl() + "?user_id=" +
                            mUserInfoManager.getCurrentUserInfo().getUserId() +
                            "&c_auth=" +
                            SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH);
                    getDisplay().startBrowserActivity(stringBuilder, "", false, false, 0, 0);
                    progressDialog.dismiss();
                }

                @Override
                public void onTimeOut() {
                    progressDialog.dismiss();
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mIdentity = AppApplication.getInstance().getCurrentDoctorIdentity();
        usercenterMenuAdapter.setNewData(getUserCenterMenu(mIdentity));
        usercenterExpandMenuAdapter.setNewData(getUserCenterExpandMenu(mIdentity));
        getProjectList(0);
        getProjectList(1);
        checkRedPoint();
        getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId());
        checkAdminInfo();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.logI(Logger.USER, "unRegMsgStateChangeListener--->");
        mMessageManager.unRegMsgStateChangeListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getProjectList(0);
            getProjectList(1);
            getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId());
            checkAdminInfo();
            mIdentity = AppApplication.getInstance().getCurrentDoctorIdentity();
            usercenterMenuAdapter.setNewData(getUserCenterMenu(mIdentity));
            usercenterExpandMenuAdapter.setNewData(getUserCenterExpandMenu(mIdentity));
        }
    }

    private void getDoctorInfo(int userId) {
        doctorManager.getDoctorInfo(userId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mPhotoUrl = doctorBaseInfo.getWebDoctorUrl();
                displayDoctorInfo(doctorBaseInfo);
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void checkAdminInfo() {
        showLoadToast();
        Disposable disposable = Observable.create((ObservableOnSubscribe<Integer>) emitter -> mUserInfoManager.getAdminInfo((result, identity1) -> {
            Logger.logD(TAG + "create:", Thread.currentThread().getName());
            emitter.onNext(result);
        })).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result == DcpErrorcodeDef.RET_SUCCESS) {
                        showSucToast();
                    } else {
                        showFailToast();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void showLoadToast() {
//        SnackbarUtils.with(mToastV)
//                .setMessage(getString(R.string.user_acquisition))
//                .setMessageColor(getCompatColor(R.color.color_white))
//                .setBgColor(getCompatColor(R.color.color_7f000000))
//                .show();
        mToastV.setBackgroundResource(R.color.color_7f000000);
        mProgressBar.setVisibility(View.VISIBLE);
        mTostContentTv.setText(R.string.user_acquisition);
        isShowToast(true);
        new Handler().postDelayed(() -> {
            if (mIdentity == -100) {
                showFailToast();
            }
        }, 10 * 1000);
    }

    private void showSucToast() {
        mToastV.setBackgroundResource(R.color.color_cc65ed8f);
        mProgressBar.setVisibility(View.GONE);
        if (isAdded()) {
            mTostContentTv.setText(getString(R.string.user_suc));
        }
        new Handler().postDelayed(() -> isShowToast(false), 3 * 1000);
    }

    private void showFailToast() {
        mToastV.setBackgroundResource(R.color.color_80A64343);
        mProgressBar.setVisibility(View.GONE);
        if (isAdded()) {
            mTostContentTv.setText(getString(R.string.user_fail));
        }
        new Handler().postDelayed(() -> isShowToast(false), 3 * 1000);
    }

    public void isShowToast(final boolean isShow) {
        final int marinTop = 0;
        final View view = mToastV;
        final int selfHeight = DisplayUtil.dip2px(20);//当前控件高度
        int yDeltaTemp = marinTop + selfHeight;
        if (!isShow) {
            if (view.getY() != marinTop) {
                return;
            }
            yDeltaTemp = -yDeltaTemp;
        } else {
            if (view.getY() != -selfHeight) {
                return;
            }
        }
        final int yDelta = yDeltaTemp;
        final TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, yDelta);
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (isShow) {
                    view.setY(marinTop);
                    isShowing = true;
                } else {
                    view.setY(-selfHeight);
                    isShowing = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AppHandlerProxy.runOnUIThread(() -> view.startAnimation(translateAnimation));
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 显示医生信息
     */
    private void displayDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        String phoneNum = doctorBaseInfo.getPhoneNum();
        if (phoneNum.startsWith("86")) {
            phoneNum = phoneNum.substring(2);
        }
        mDoctorAccountTv.setText(String.format(getString(R.string.user_account), phoneNum));
        mDoctorNameTv.setText(String.format(getString(R.string.user_welcome), doctorBaseInfo.getRealName()));
        GlideUtils.showDoctorAvatar(mDoctorPhotoIv, getBaseActivity(), AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
        if (doctorBaseInfo.isReal()) {
            fgUserCenterDoctorRealStateIv.setImageResource(R.mipmap.ic_user_center_is_verified);
        } else {
            fgUserCenterDoctorRealStateIv.setImageResource(R.mipmap.ic_user_center_no_verified);
        }
    }

    @OnClick({R.id.fragment_user_center_suggest_tv,
            R.id.fragment_user_center_doctor_photo_iv,
            R.id.fragment_user_center_log_out,})
    public void onClick(View view) {
        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.fragment_user_center_suggest_tv:
                intent.setClass(getActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "index/register.html");
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SETTLED, true);
                startActivity(intent);
                break;
            case R.id.fragment_user_center_doctor_photo_iv:
                if (!TextUtils.isEmpty(mPhotoUrl)) {
                    intent.setClass(getActivity(), BrowserActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, mPhotoUrl);
                    startActivity(intent);
                }
                break;
            case R.id.fragment_user_center_log_out:
                showLogoutDialog();
                break;

            default:
                break;
        }
    }

    private void displayInputWindow() {
        mDcpManager.getRoomListInfo(2);
        View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_live_video_input, null);
        EditText inputEdt = inputView.findViewById(R.id.layout_live_video_input_edt);
        TextView inputCancel = inputView.findViewById(R.id.layout_live_video_input_cancel);
        TextView inputGoIn = inputView.findViewById(R.id.layout_live_video_input_go_in);
        final Dialog dialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(inputView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setInputViewListener(inputCancel, inputGoIn, inputEdt, dialog);
    }

    private void setInputViewListener(TextView inputCancel, TextView inputGoIn, final EditText inputEdt, final Dialog dialog) {
        inputCancel.setOnClickListener(v -> dialog.dismiss());
        inputGoIn.setOnClickListener(v -> joinVideoRoom(inputEdt, dialog));
    }

    private void joinVideoRoom(final EditText inputEdt, final Dialog dialog) {
        final String str = inputEdt.getText().toString().trim();
        if ("".equals(str)) {
            ToastUtils.showShort(R.string.user_please_input_num);
            return;
        }
        final int appointmentId = Integer.valueOf(str).intValue();
        showProgressDialog();
        if (appointmentId >= 500000) {//查房预约
            checkRoundsAppointment(appointmentId, dialog);
        } else {//会诊预约
            VideoLiveCheckRequest request = new VideoLiveCheckRequest((baseResult, getOrderInfo) -> {
                dismissProgressDialog();
                if (baseResult.getCode() == -103) {//预约不存在
                    ToastUtils.showShort(R.string.ret_room_reserve_not_exist);
                } else if (baseResult.getCode() == 0) {
                    Logger.logD(Logger.APPOINTMENT, "UsercenterFragment->baseResult:" + baseResult.getCode() + ",getOrderInfo:"
                            + getOrderInfo + ",getServiceTypeInfo:" + getOrderInfo.getServiceTypeInfo().getServiceType() +
                            ",getClinicInfo:" + getOrderInfo.getClinicInfo());
                    intentVideoRoom(getOrderInfo, appointmentId);
                    dialog.dismiss();
                } else {
                    ToastUtils.showShort(R.string.consult_room_state_net_bad);
                }
            });
            request.appointmentId = appointmentId;
            request.doPost();
        }
    }

    private void checkRoundsAppointment(final int appointmentId, final Dialog dialog) {
        final OrderDetailsRequester requester = new OrderDetailsRequester(new DefaultResultCallback<OrderDetailsInfo>() {
            @Override
            public void onSuccess(OrderDetailsInfo orderDetailsInfo, BaseResult baseResult) {

                Logger.logI(Logger.COMMON, "UsercenterFragment：-OrderDetailsRequester-->baseResult" + baseResult + " , orderDetailsInfo" + orderDetailsInfo);
                if (orderDetailsInfo != null) {
                    if (mUserInfoManager.getCurrentUserInfo().getUserId() != orderDetailsInfo.getDoctorId() &&
                            mUserInfoManager.getCurrentUserInfo().getUserId() != orderDetailsInfo.getLaunchDoctorId()) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), RoundsConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, appointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_LIVE_VIDEO, true);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        ToastUtils.showShort(R.string.user_relevant);
                    }
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == -102) {
                    ToastUtils.showShort(R.string.ret_room_reserve_not_exist);
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        });
        requester.orderId = appointmentId;
        requester.doPost();
    }

    private void intentVideoRoom(GetOrderInfo orderInfo, int appointmentId) {
        if (mUserInfoManager.getCurrentUserInfo().getUserId() != orderInfo.getPatientId() &&
                mUserInfoManager.getCurrentUserInfo().getUserId() != orderInfo.getAttendingDoctorUserId() &&
                mUserInfoManager.getCurrentUserInfo().getUserId() != orderInfo.getDoctorUserId()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            Intent intent = new Intent();
            intent.setClass(getActivity(), ConsultRoomActivity.class);
            if (orderInfo.getServiceTypeInfo().getServiceType() == AppConstant.ServiceType.SERVICE_TYPE_NEW_REMOTE_OUTPATIEN) {
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO, orderInfo);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_CLINIC, true);
            }
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_LIVE_VIDEO, true);
            startActivity(intent);
        } else {
            ToastUtils.showShort(R.string.user_relevant);
        }
    }

    /**
     * 显示退出登录对话框
     */
    private void showLogoutDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getActivity());
        builder.setMessage(R.string.sure_to_log_out_tip)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> {
                    UserInfo userInfo = mUserInfoManager.getCurrentUserInfo();
                    mDcpManager.logout(userInfo.getUserId());
                    mDcpManager.disconnect();
                    mUserInfoManager.removeUserInfo(null);
                    mMaterialTaskManager.reset();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }).show();
    }

    private void showMeetingDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_meeting_view, null);
        RecyclerView mRecyclerView = view.findViewById(R.id.dialog_meeting_view_recycler_view);
        mMeetingDialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        mMeetingDialog.show();
        mMeetingDialog.setContentView(view);
        mMeetingDialog.setCanceledOnTouchOutside(true);
        mMeetingDialog.setCancelable(true);
        Window win = mMeetingDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        initRecyclerView(mRecyclerView);
    }

    private void initRecyclerView(RecyclerView mRecyclerView) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final MeetingViewAdapter meetingViewAdapter = new MeetingViewAdapter(R.layout.item_meeting_view, new ArrayList<>(0));
        mRecyclerView.setAdapter(meetingViewAdapter);
        meetingViewAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            RoomListInfo roomListInfo = (RoomListInfo) adapter.getItem(position);
            if (null != roomListInfo) {
                showMeetingPasswordWindow(roomListInfo);
            }
        });
        mUserInfoManager.setOnGetRoomListInfoListener((result, list) -> {
            Logger.logD(Logger.USER, "->setOnGetRoomListInfoListener()->result:" + result + ", list:" + list);
            AppHandlerProxy.runOnUIThread(() -> {
                if (result == 0) {
                    meetingViewAdapter.setNewData(list);
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            });
        });
    }

    private void showMeetingPasswordWindow(RoomListInfo roomListInfo) {
        Logger.logD(Logger.USER, "->showMeetingPasswordWindow()");
        if (mMeetingPasswordDialog != null && mMeetingPasswordDialog.isShowing()) {
            return;
        } else if (mMeetingPasswordDialog != null && !mMeetingPasswordDialog.isShowing()) {
            mMeetingPasswordDialog.show();
            if (mMeetingInputEdt != null) {
                mMeetingInputEdt.setText("");
            }
        } else {
            View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_meeting_password_view, null);
            mMeetingInputEdt = inputView.findViewById(R.id.dialog_meeting_input_edt);
            mInputCancel = inputView.findViewById(R.id.dialog_meeting_input_cancel);
            mInputGoIn = inputView.findViewById(R.id.dialog_meeting_input_go_in);
            mMeetingPasswordDialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
            mMeetingPasswordDialog.show();
            mMeetingPasswordDialog.setContentView(inputView);
            mMeetingPasswordDialog.setCanceledOnTouchOutside(false);
            mMeetingPasswordDialog.setCancelable(true);
            Window win = mMeetingPasswordDialog.getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
        }
        initMeetingPasswordView(mInputCancel, mInputGoIn, mMeetingInputEdt, mMeetingPasswordDialog, roomListInfo);
    }

    private void initMeetingPasswordView(TextView inputCancel, final Button inputGoIn, final EditText inputEdt, final Dialog dialog, RoomListInfo roomListInfo) {
        inputGoIn.setOnClickListener(v -> {
            if (StringUtils.isEmpty(inputEdt.getText().toString())) {
                ToastUtils.showShort(R.string.user_input_meeting_pa);
                return;
            }
            Logger.logD(Logger.USER, "->initMeetingPasswordView()->");
            if (NetStateReceiver.NETWORK_TYPE_NONE == NetStateReceiver.getCurrentNetType(getActivity())) {
                ToastUtils.showShort(R.string.no_network_connection);
                return;
            }
            showProgress();
            new Handler().postDelayed(() -> {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                inputGoIn.setEnabled(true);
            }, 60 * 1000);
            inputGoIn.setEnabled(false);
            mUserInfoManager.setOnCheckPwdInfoListener(1, roomListInfo.getRoomID(), inputEdt.getText().toString().trim(), result -> {
                Logger.logD(Logger.USER, "->setOnCheckPwdInfoListener()->result:" + result);
                AppHandlerProxy.runOnUIThread(() -> {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        mLoadingDialog.dismiss();
                    }
                    inputGoIn.setEnabled(true);
                    if (result == 0) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), MeetingRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_METING_ROOM_ID, roomListInfo.getRoomID());
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_METING_ROOM_NAME, roomListInfo.getRoomName());
                        startActivity(intent);
                        if (mMeetingDialog != null) {
                            mMeetingDialog.dismiss();
                        }
                    } else {
                        ToastUtils.showShort(R.string.user_password_error);
                    }
                });
            });
        });
        inputCancel.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        int msgType = baseMessageInfo.getMsgType();
        if (msgType == MessageProtocol.SMS_TYPE_DOCTOR_BALANCE_CHANGE ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_APPOINTMENT_TODAY ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_39_ACTIVITY ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_FAST_DIAGNOSIS ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE ||
                msgType == MessageProtocol.SMS_TYPE_MATERIAL_UPLOAD_NOTE ||
                msgType == MessageProtocol.SMS_TYPE_DOCTOR_ASSESS) {
            checkRedPoint();
        }
    }

    public void checkRedPoint() {
        if (SPUtils.getInstance().getBoolean(AppPreference.KEY_MESSAGE_CENTER_NEW_MESSAGE + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
            usercenterMenuAdapter.showDot(R.string.user_center_menu_message);
        } else {
            usercenterMenuAdapter.hideDot(R.string.user_center_menu_message);
        }
        if (SPUtils.getInstance().getBoolean(AppPreference.KEY_NEW_ASSESS + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
            usercenterMenuAdapter.showDot(R.string.user_center_menu_assess);
        } else {
            usercenterMenuAdapter.hideDot(R.string.user_center_menu_assess);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgress() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        } else {
            View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_meeting_progress, null);
            mLoadingDialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
            mLoadingDialog.show();
            mLoadingDialog.setContentView(inputView);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setCancelable(false);
            Window win = mLoadingDialog.getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
        }
    }

    private void getProjectList(int type) {
        dcManager.getProjectList(type, new DefaultResultCallback<List<DCProjectInfo>>() {
            @Override
            public void onSuccess(List<DCProjectInfo> dcProjectInfos, BaseResult baseResult) {
                if (type == 1) {
                    mDcProjectManagerInfos = dcProjectInfos;
                } else {
                    mDcProjectInfos = dcProjectInfos;
                    for (DCProjectInfo info : dcProjectInfos) {
                        if (info.getDutyState() == 1) {
                            usercenterExpandMenuAdapter.showDot(R.string.user_center_menu_duty_clinic);
                            return;
                        }
                    }
                    usercenterExpandMenuAdapter.hideDot(R.string.user_center_menu_duty_clinic);
                }
                usercenterExpandMenuAdapter.setNewData(getUserCenterExpandMenu(mIdentity));
            }
        });
    }
}
