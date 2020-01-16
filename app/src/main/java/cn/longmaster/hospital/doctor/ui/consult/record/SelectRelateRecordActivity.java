package cn.longmaster.hospital.doctor.ui.consult.record;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForSelectInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.record.BindRecordRequest;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.AppointmentRelateSelectAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 选择关联病历
 * Created by Yang² on 2016/7/11.
 */
public class SelectRelateRecordActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_select_relate_record_title_bar)
    private AppActionBar mAppactionBar;
    @FindViewById(R.id.act_select_relate_record_srl)
    private SmartRefreshLayout actSelectRelateRecordSrl;
    @FindViewById(R.id.act_select_relate_record_rv)
    private RecyclerView actSelectRelateRecordRv;
    @FindViewById(R.id.activity_select_relate_record_confirm_btn)
    private Button mConfirmBtn;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultManager mConsultManager;

    private AppointmentRelateSelectAdapter mAppointmentRelateSelectAdapter;
    private int mAppointId;
    private int patientId;
    //已关联的病例
    private List<Integer> mRelateRecordIds;
    //请求下一页索引
    private int mSymbol = MIN_PAGE_INDEX_0;

    @Override
    protected void initDatas() {
        mAppointId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        patientId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_ID, 0);
        mRelateRecordIds = (ArrayList<Integer>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RELATED_RECORD_IDS);
        if (mRelateRecordIds == null) {
            mRelateRecordIds = new ArrayList<>();
        }
        mAppointmentRelateSelectAdapter = new AppointmentRelateSelectAdapter(R.layout.item_select_relate_record, new ArrayList<>(0), mRelateRecordIds);

        mAppointmentRelateSelectAdapter.setOnCheckChangeListener(relateRecordInfos -> {
            mRelateRecordIds = relateRecordInfos;
            StringBuilder confirmTxt = new StringBuilder();
            confirmTxt.append("确认关联");
            confirmTxt.append("(");
            confirmTxt.append(LibCollections.size(relateRecordInfos));
            confirmTxt.append("个");
            confirmTxt.append(")");
            mConfirmBtn.setText(confirmTxt);
        });
        mAppointmentRelateSelectAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.item_consult_relate_consult_order_is_check_cb) {
                mAppointmentRelateSelectAdapter.toggleSelected(position);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_relate_record;
    }

    @Override
    protected void initViews() {
        actSelectRelateRecordSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getAppointmentIds(false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSymbol = MIN_PAGE_INDEX_0;
                getAppointmentIds(true, refreshLayout);
            }
        });
        actSelectRelateRecordRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actSelectRelateRecordRv.setAdapter(mAppointmentRelateSelectAdapter);
        mConfirmBtn.setText("确认关联(" + LibCollections.size(mRelateRecordIds) + "个)");
        mConfirmBtn.setOnClickListener(v -> {
            relateRecord();
        });
        actSelectRelateRecordSrl.autoRefresh();
    }

    /**
     * 获取患者预约id
     */
    private void getAppointmentIds(boolean isRefresh, RefreshLayout refreshLayout) {
        mConsultManager.getPatientRecord(patientId, mSymbol, PAGE_SIZE, new DefaultResultCallback<List<AppointmentItemForSelectInfo>>() {
            @Override
            public void onSuccess(List<AppointmentItemForSelectInfo> relateRecordInfos, BaseResult baseResult) {
                for (AppointmentItemForSelectInfo info : relateRecordInfos) {
                    if (info.getAppointmentId() == mAppointId) {
                        relateRecordInfos.remove(info);
                        break;
                    }
                }
                if (baseResult.isFinish()) {
                    actSelectRelateRecordSrl.finishLoadMoreWithNoMoreData();
                }
                if (mSymbol == MIN_PAGE_INDEX_0 && LibCollections.isEmpty(relateRecordInfos)) {
                    mAppointmentRelateSelectAdapter.setEmptyView(createEmptyListView("暂无患者"));
                }
                if (mSymbol == MIN_PAGE_INDEX_0) {
                    mAppointmentRelateSelectAdapter.setNewData(relateRecordInfos);
                } else {
                    mAppointmentRelateSelectAdapter.addData(relateRecordInfos);
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
                super.onFinish();
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    /**
     * 关联病历
     */
    private void relateRecord() {
        BindRecordRequest recordRequest = new BindRecordRequest((baseResult, s) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        recordRequest.appointmentId = mAppointId;
        StringBuilder idsTemp = new StringBuilder();
        if (null != mRelateRecordIds) {
            for (Integer id : mRelateRecordIds) {
                idsTemp.append(id);
                idsTemp.append(",");
            }
        }
        recordRequest.relationIds = StringUtils.substringBeforeLast(idsTemp.toString(), ",");
        recordRequest.doPost();
    }

}
