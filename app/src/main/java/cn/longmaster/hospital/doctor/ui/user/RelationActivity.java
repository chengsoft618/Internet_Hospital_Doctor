package cn.longmaster.hospital.doctor.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.RelationMedicalInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.RelationMedicalListRequester;
import cn.longmaster.hospital.doctor.core.requests.user.RelationMedicalRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.college.view.RefreshRecyclerView;
import cn.longmaster.hospital.doctor.ui.user.adapter.AlreadyRelationAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.StringUtils;

public class RelationActivity extends BaseActivity {
    @FindViewById(R.id.activity_relation_recycler_view)
    private RefreshRecyclerView mRecyclerView;
    @FindViewById(R.id.activity_relation_empty_layout)
    private LinearLayout mEmptyLayout;
    @FindViewById(R.id.activity_relation_edt)
    private EditText mEditText;

    private int mMedicalId;
    private AlreadyRelationAdapter mAdapter;
    private int mPageIndex = 1;
    private int mIsFinish;
    private ProgressDialog mProgressDialog;
    private List<RelationMedicalInfo> mRelationMedicalInfos = new ArrayList<>();
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);
        ViewInjecter.inject(this);
        initData();
        initView();
        getRelationMedicalList(false);
    }

    private void initData() {
        mMedicalId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AlreadyRelationAdapter(this, mRelationMedicalInfos);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnCancelRelationClickListener((view, position) -> {
            mPosition = position;
            showCancelRelationDialog(position);
        });

        mRecyclerView.setOnLoadMoreListener(() -> {
            if (mIsFinish == 1) {
                mRecyclerView.setLoadMoreEnable(true);
                mPageIndex++;
                getRelationMedicalList(true);
            } else {
                mRecyclerView.setLoadMoreEnable(false);
            }
        });
    }

    @OnClick(R.id.activity_relation_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_relation_btn:
                if (StringUtils.isEmpty(mEditText.getText().toString().trim())) {
                    showToast(getString(R.string.user_please_input_num));
                    return;
                }
                int appointmentId = Integer.valueOf(mEditText.getText().toString()).intValue();
                relationMedical(appointmentId, 0);
                break;
        }
    }

    private void getRelationMedicalList(final boolean isLoadMore) {
        RelationMedicalListRequester requester = new RelationMedicalListRequester((baseResult, relationMedicalInfos) -> {
            Logger.logI(Logger.COMMON, "RelationActivity：-->RelationMedicalListRequester-->baseResult:" + baseResult + ",relationMedicalInfos:" + relationMedicalInfos);
            mRecyclerView.setLoadMoreEnable(true);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mIsFinish = baseResult.getIsFinish();
                Logger.logD(Logger.COMMON, "RelationActivity-->mIsFinish:" + mIsFinish);
                if (isLoadMore) {
                    mRelationMedicalInfos.addAll(relationMedicalInfos);
                    mAdapter.addData(relationMedicalInfos);
                    mRecyclerView.notifyData();
                } else {
                    mRelationMedicalInfos = relationMedicalInfos;
                    mAdapter.setData(relationMedicalInfos);
                    mRecyclerView.notifyData();
                }
                if (mRelationMedicalInfos.size() == 0) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                showToast(R.string.no_network_connection);
            }
        });
        requester.materialId = mMedicalId;
        requester.pageindex = mPageIndex;
        requester.pagesize = 10;
        requester.doPost();
    }

    private void showCancelRelationDialog(final int position) {
        new CommonDialog.Builder(getActivity())
                .setTitle(getString(R.string.user_confirm_cancel))
                .setMessage(getString(R.string.user_sure_cancel_num, mRelationMedicalInfos.get(position).getAppointmentId() + ""))
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {
                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> relationMedical(mRelationMedicalInfos.get(position).getAppointmentId(), 1))
                .show();
    }

    private void relationMedical(final int position, final int opt) {
        showProgressDialog();
        RelationMedicalRequester requester = new RelationMedicalRequester((baseResult, aVoid) -> {
            dismissProgressDialog();
            Logger.logD(Logger.COMMON, "RelationMedicalRequester->BaseResult:" + baseResult);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mPageIndex = 1;
                getRelationMedicalList(false);
                if (opt == 1) {
                    if (mRelationMedicalInfos.size() == 0) {
                        mEmptyLayout.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    showToast(getString(R.string.relation_success, position + ""));
                    mEditText.setText("");
                }
            } else {
                if (opt == 1) {
                    showToast(R.string.no_network_connection);
                } else {
                    showToast(getString(R.string.relation_fail));
                }
            }
        });
        requester.appointmentId = position;
        requester.materialId = mMedicalId;
        requester.actType = opt;
        requester.doPost();
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
}
