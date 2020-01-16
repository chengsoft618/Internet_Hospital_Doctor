package cn.longmaster.hospital.doctor.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentItemInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;
import cn.longmaster.hospital.doctor.util.ConsultUtil;
import cn.longmaster.utils.KeyboardUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2018/5/10.
 * Mod by Biao on 2019/7/8
 * 预约列表会诊
 */

public class MyPatientConsultFragment extends NewBaseFragment implements MessageStateChangeListener {
    private final String RECEIVE_STAT_NUM_NOT_SELECT = ConsultUtil.getStaNumReqParams(AppConstant.StatNum.STAT_NUM_WAITING_PAY,
            AppConstant.StatNum.STAT_NUM_WAITING_VIDEO, AppConstant.StatNum.STAT_NUM_WAITING_ADVICE, AppConstant.StatNum.STAT_NUM_REPORT_TIDY,
            AppConstant.StatNum.STAT_NUM_VIEW_REPORT, AppConstant.StatNum.STAT_NUM_PAY_CONSULT_CLOSED, AppConstant.StatNum.STAT_NUM_CONSULT_FINISH);

    private final int REQUEST_CODE_SEARCH = 100;

    @FindViewById(R.id.layout_my_patient_search_ll)
    private LinearLayout layoutMyPatientSearchLl;
    @FindViewById(R.id.fg_my_patient_consult_search_tv)
    private TextView fgMyPatientConsultSearchTv;
    @FindViewById(R.id.layout_my_patient_search_et)
    private EditText layoutMyPatientSearchEt;
    @FindViewById(R.id.layout_my_patient_search_clear_iv)
    private ImageView layoutMyPatientSearchClearIv;
    @FindViewById(R.id.layout_my_patient_srl)
    private SmartRefreshLayout layoutMyPatientSrl;
    @FindViewById(R.id.layout_my_patient_rv)
    private RecyclerView layoutMyPatientRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultManager mPatientManager;

    private String mKeyWords;// 关键字
    private int mSameDep = 1; // 是否显示同科室 默认显示及1
    private int mSymbol = MIN_PAGE_INDEX_0;
    private AppointmentAdapter mAppointmentAdapter;
    @AppApplication.Manager
    MessageManager messageManager;

    @Override
    protected void initDatas() {
        super.initDatas();
        mAppointmentAdapter = new AppointmentAdapter(R.layout.item_home_consult_layout, new ArrayList<>(0));
        mAppointmentAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppointmentItemInfo info = (AppointmentItemInfo) adapter.getItem(position);
            if (null != info) {
                Intent intent = new Intent(getActivity(), PatientInformationActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, info.getAppointmentId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_my_patient_consult;
    }

    @Override
    public void initViews(View rootView) {
        messageManager.regMsgStateChangeListener(this);
        layoutMyPatientRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        layoutMyPatientRv.setAdapter(mAppointmentAdapter);
        initListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (REQUEST_CODE_SEARCH == requestCode) {
                mKeyWords = data.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT);
                layoutMyPatientSrl.autoRefresh();
            }
        }
    }

    private void initListener() {
        fgMyPatientConsultSearchTv.setOnClickListener(v -> {
            layoutMyPatientSrl.autoRefresh();
        });
        layoutMyPatientSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                layoutMyPatientSrl.autoRefresh();
                KeyboardUtils.hideSoftInput(v);
                return true;
            }
            return false;
        });
        layoutMyPatientSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    layoutMyPatientSearchClearIv.setVisibility(View.VISIBLE);
                } else {
                    layoutMyPatientSearchClearIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        layoutMyPatientSearchClearIv.setOnClickListener(v -> {
            layoutMyPatientSearchEt.setText(null);
            layoutMyPatientSrl.autoRefresh();
        });
        layoutMyPatientSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getAppointmentList(false, mKeyWords, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSymbol = MIN_PAGE_INDEX_0;
                mKeyWords = getString(layoutMyPatientSearchEt);
                getAppointmentList(true, mKeyWords, refreshLayout);
            }
        });
        layoutMyPatientSrl.autoRefresh();
    }

    public void setSameDep(boolean isSameDep) {
        mSameDep = isSameDep ? 1 : 0;
        layoutMyPatientSrl.autoRefresh();
    }

    private void getAppointmentList(boolean isRefresh, String searchType, RefreshLayout refreshLayout) {
        int doctorId = mUserInfoManager.getCurrentUserInfo().getUserId();
        mPatientManager.getScreenAppointmentIDs(doctorId, AppConstant.PromoterType.PROMOTER_TYPE_RECEIVED_AND_LAUNCHED
                , AppConstant.SchedulingType.SCHEDULING_TYPE_NO_SELECT, AppConstant.ServiceType.SERVICE_TYPE_NOT_SELECT
                , ConsultUtil.getStaNumReqParams(AppConstant.StatNum.STAT_NUM_NOT_SELECT)
                , RECEIVE_STAT_NUM_NOT_SELECT, AppConstant.Recure.RECURE_NOT_SELECT, searchType, mSymbol, PAGE_SIZE, mSameDep
                , new DefaultResultCallback<List<AppointmentItemInfo>>() {
                    @Override
                    public void onSuccess(List<AppointmentItemInfo> integers, BaseResult baseResult) {
                        if (baseResult.isFinish()) {
                            layoutMyPatientSrl.finishLoadMoreWithNoMoreData();
                        }
                        if (mSymbol == 0 && LibCollections.isEmpty(integers)) {
                            includeNewNoDataLl.setVisibility(View.VISIBLE);
                            layoutMyPatientSrl.setVisibility(View.GONE);
                        } else {
                            layoutMyPatientSrl.setVisibility(View.VISIBLE);
                            includeNewNoDataLl.setVisibility(View.GONE);
                            if (mSymbol == 0) {
                                mAppointmentAdapter.setNewData(integers);
                            } else {
                                mAppointmentAdapter.addData(integers);
                            }
                        }
                        mSymbol = baseResult.getSymbol();
                    }

                    @Override
                    public void onFail(BaseResult result) {
                        super.onFail(result);
                        ToastUtils.showShort(R.string.no_network_connection);
                    }

                    @Override
                    public void onFinish() {
                        if (isRefresh) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        messageManager.unRegMsgStateChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        if (baseMessageInfo.getAppointmentId() != 0) {
            mAppointmentAdapter.removeLocalAppointment(baseMessageInfo.getAppointmentId());
            layoutMyPatientSrl.autoRefresh();
        }
    }
}
