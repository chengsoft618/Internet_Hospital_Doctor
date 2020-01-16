package cn.longmaster.hospital.doctor.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataItem;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.PatientDataManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.PatientDataManagerAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/9 15:43
 * @description:
 */
public class PatientDataListActivity extends NewBaseActivity {
    @FindViewById(R.id.tool_bar_base)
    private Toolbar toolBarBase;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.include_search_clear_iv)
    private ImageView includeSearchClearIv;
    @FindViewById(R.id.include_search_operation_tv)
    private TextView includeSearchOperationTv;
    @FindViewById(R.id.activity_patient_data_srl)
    private SmartRefreshLayout activityPatientDataSrl;
    @FindViewById(R.id.activity_patient_data_rv)
    private RecyclerView activityPatientDataRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @FindViewById(R.id.include_new_no_data_iv)
    private ImageView includeNewNoDataIv;
    private PatientDataManagerAdapter patientDataManagerAdapter;
    @AppApplication.Manager
    PatientDataManager mPatientDataManager;
    private final int REQUEST_CODE_FOR_REFRESH = 132;

    @Override
    protected void initDatas() {
        patientDataManagerAdapter = new PatientDataManagerAdapter(R.layout.item_patient_data_manager, new ArrayList<>(0));
        patientDataManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            PatientDataItem item = (PatientDataItem) adapter.getItem(position);
            if (null != item) {
                getDisplay().startPatientDataDetailActivity(item.getMedicalId(), REQUEST_CODE_FOR_REFRESH);
            }
        });
        patientDataManagerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PatientDataItem item = (PatientDataItem) adapter.getItem(position);
            if (null != item) {
                switch (view.getId()) {
                    case R.id.item_patient_data_manager_patient_has_pics_ll:
                        //查看更多
                        getDisplay().startPatientDataDetailActivity(item.getMedicalId(), REQUEST_CODE_FOR_REFRESH);
                        break;
                    default:
                        break;
                }
            }
        });
        patientDataManagerAdapter.setOnPicsItemChildClickListener((baseAdapter, basePosition, adapter, view, position) -> {
            PatientDataItem item = (PatientDataItem) baseAdapter.getItem(basePosition);
            if (null != item) {
                List<SingleFileInfo> singleFileInfos = new ArrayList<>();
                List<String> serverFilePaths = new ArrayList<>();
                if (LibCollections.isNotEmpty(item.getDataList())) {
                    for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : item.getDataList()) {
                        SingleFileInfo singleFileInfo = new SingleFileInfo("0", SdManager.getInstance().getOrderPicPath(auxiliaryMaterialInfo.getMaterialPic(), auxiliaryMaterialInfo.getAppointmentId() + ""));
                        singleFileInfo.setLocalFileName(auxiliaryMaterialInfo.getMaterialPic());
                        singleFileInfo.setLocalFilePath(SdManager.getInstance().getOrderPicPath(auxiliaryMaterialInfo.getMaterialPic(), auxiliaryMaterialInfo.getAppointmentId() + ""));
                        singleFileInfo.setServerFileName(auxiliaryMaterialInfo.getMaterialPic());
                        singleFileInfo.setIsFromServer(true);
                        singleFileInfo.setAppointmentId(auxiliaryMaterialInfo.getAppointmentId());
                        singleFileInfo.setMaterialId(auxiliaryMaterialInfo.getMaterialId());
                        singleFileInfo.setCheckState(auxiliaryMaterialInfo.getCheckState());
                        singleFileInfo.setAuditDesc(auxiliaryMaterialInfo.getAuditDesc());
                        singleFileInfo.setMaterailType(auxiliaryMaterialInfo.getMaterialType());
                        singleFileInfo.setMediaType(auxiliaryMaterialInfo.getMediaType());
                        singleFileInfo.setDicom(auxiliaryMaterialInfo.getDicom());
                        singleFileInfo.setMaterialName(auxiliaryMaterialInfo.getMaterialName());
                        singleFileInfo.setMaterialResult(auxiliaryMaterialInfo.getMaterialResult());
                        singleFileInfo.setMaterialDt(auxiliaryMaterialInfo.getMaterialDt());
                        singleFileInfo.setMaterialHosp(auxiliaryMaterialInfo.getMaterialHosp());
                        singleFileInfos.add(singleFileInfo);
                        serverFilePaths.add(AppConfig.getMaterialDownloadUrl() + auxiliaryMaterialInfo.getMaterialPic());
                    }
                }
                BrowserPicEvent browserPicEvent = new BrowserPicEvent();
                browserPicEvent.setAppointInfoId(StringUtils.str2Integer(item.getMedicalId()));
                browserPicEvent.setAuxiliaryMaterialInfos(item.getDataList());
                browserPicEvent.setIndex(position);
                browserPicEvent.setSingleFileInfos(singleFileInfos);
                browserPicEvent.setServerFilePaths(serverFilePaths);
                //browserPicEvent.setAssistant(true);
                browserPicEvent.setRounds(true);
                browserPicEvent.setPass(true);
                getDisplay().startPicBrowseActivity(browserPicEvent, REQUEST_CODE_FOR_REFRESH);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_patient_data_list;
    }

    @Override
    protected void initViews() {
        includeSearchTitleEt.setHint("输入患者姓名/住院号/病例号");
        includeSearchOperationTv.setText("查找");
        activityPatientDataRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityPatientDataRv.setAdapter(patientDataManagerAdapter);
        activityPatientDataSrl.autoRefresh();
        tvToolBarTitle.setText("患者材料列表");
        initListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_REFRESH) {
                activityPatientDataSrl.autoRefresh();
            }
        }
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        includeSearchTitleEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activityPatientDataSrl.autoRefresh();
                return true;
            }
            return false;
        });
        includeSearchTitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isTrimEmpty(s)) {
                    includeSearchClearIv.setVisibility(View.GONE);
                } else {
                    includeSearchClearIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        includeSearchClearIv.setOnClickListener(v -> {
            includeSearchTitleEt.setText(null);
            activityPatientDataSrl.autoRefresh();
        });
        includeSearchOperationTv.setOnClickListener(v -> {
            activityPatientDataSrl.autoRefresh();
        });

        activityPatientDataSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX++;
                getPatientDataList(getString(includeSearchTitleEt), false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX = MIN_PAGE_INDEX_1;
                getPatientDataList(getString(includeSearchTitleEt), true, refreshLayout);
            }
        });
    }

    private void getPatientDataList(String keyWords, boolean isRefresh, RefreshLayout refreshLayout) {
        mPatientDataManager.getPatientDataList(PAGE_INDEX, PAGE_SIZE, keyWords, new DefaultResultCallback<List<PatientDataItem>>() {
            @Override
            public void onSuccess(List<PatientDataItem> patientDataItems, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    activityPatientDataSrl.finishLoadMoreWithNoMoreData();
                }
                if (PAGE_INDEX == MIN_PAGE_INDEX_1) {
                    if (LibCollections.isEmpty(patientDataItems)) {
                        activityPatientDataSrl.setVisibility(View.GONE);
                        includeNewNoDataLl.setVisibility(View.VISIBLE);
                    } else {
                        activityPatientDataSrl.setVisibility(View.VISIBLE);
                        includeNewNoDataLl.setVisibility(View.GONE);
                    }
                    patientDataManagerAdapter.setNewData(patientDataItems);
                } else {
                    patientDataManagerAdapter.addData(patientDataItems);
                }
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

    /**
     * 构建材料文件
     *
     * @param list 资料列表
     * @return
     */
    private List<SingleFileInfo> getFileInfos(List<AuxiliaryMaterialInfo> list) {
        List<SingleFileInfo> singleFileInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : list) {

        }
        return singleFileInfos;
    }
}
