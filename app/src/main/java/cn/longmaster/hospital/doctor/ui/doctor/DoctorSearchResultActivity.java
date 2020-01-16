package cn.longmaster.hospital.doctor.ui.doctor;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.SearchDoctorInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.SearchDoctorRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.SelectionTimeActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.SearchDoctorAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.ShareDoctorCardDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 搜索医生结果
 * Created by Yang² on 2016/8/2.
 */
public class DoctorSearchResultActivity extends NewBaseActivity {
    private final int REQUEST_CODE_SEARCH_DOCTOR = 100;
    @FindViewById(R.id.activity_doctor_search_bar)
    private AppActionBar mDoctorSearchBar;
    @FindViewById(R.id.activity_doctor_search_prv)
    private RecyclerView mDoctorSearchPrv;
    @FindViewById(R.id.activity_doctor_search_no_result_ll)
    private LinearLayout mNoResultLl;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    private SearchDoctorAdapter mSearchDoctorAdapter;

    private String search;
    private boolean mIsShare;
    private PatientBaseInfo mPatientBaseInfo;
    private boolean mAddmedicalRecord;
    private boolean mIsRoundsSearch;
    private boolean mIsOdifyExpert;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mIsShare = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, false);
        if (mIsShare) {
            mPatientBaseInfo = (PatientBaseInfo) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO);
        }
        search = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT);

        mAddmedicalRecord = intent.getBooleanExtra("add_medical_record", false);
        mIsRoundsSearch = intent.getBooleanExtra("mIsRoundsSearch", false);
        mIsOdifyExpert = intent.getBooleanExtra("mIsOdifyExpert", false);
    }

    @Override
    protected void initDatas() {
        mSearchDoctorAdapter = new SearchDoctorAdapter(R.layout.item_doctor_list_layout, new ArrayList<>(0));
        mSearchDoctorAdapter.setIsRounds(mIsRoundsSearch);
        mSearchDoctorAdapter.setOnItemClickListener((adapter, view, position) -> {
            SearchDoctorInfo info = (SearchDoctorInfo) adapter.getItem(position);
            DoctorBaseInfo doctorBaseInfo = mSearchDoctorAdapter.getDoctorBaseInfo(info.getDoctorId());
            if (null == doctorBaseInfo) {
                return;
            }
            if (mIsShare) {
                showDoctorCardDialog(doctorBaseInfo.getRealName(), doctorBaseInfo.getHospitalName(), info.getDoctorId());
            } else if (mAddmedicalRecord || mIsOdifyExpert) {
                Intent intent = getIntent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, info.getDoctorId());
                setResult(RESULT_OK, intent);
                finish();
            } else if (mIsRoundsSearch) {
                if (mUserInfoManager.getCurrentUserInfo().getUserId() == info.getDoctorId()) {
                    ToastUtils.showShort(getString(R.string.rounds_info_not_to_oneself));
                    return;
                }
                Intent intent = new Intent(getThisActivity(), SelectionTimeActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, info.getDoctorId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getThisActivity(), DoctorDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, info.getDoctorId());
                startActivityForResult(intent, 201);
            }
        });
        mSearchDoctorAdapter.setOnChoiceDoctorBtnClickListener((view, doctorId, position) -> {
            if (mIsOdifyExpert) {
                Intent intent = new Intent(getThisActivity(), DoctorDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, doctorId);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_IS_ROUNDS, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MODIFY_EXPERT, true);
                startActivityForResult(intent, REQUEST_CODE_SEARCH_DOCTOR);
            } else {
                Intent intent = new Intent(getThisActivity(), DoctorDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, doctorId);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_IS_ROUNDS, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_search_result;
    }

    @Override
    protected void initViews() {
        mDoctorSearchBar.setTitle(search, 8);
        mDoctorSearchPrv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        mDoctorSearchPrv.setAdapter(mSearchDoctorAdapter);
        getDoctorList();
    }

    private void getDoctorList() {
        SearchDoctorRequester searchDoctorRequester = new SearchDoctorRequester(new DefaultResultCallback<List<SearchDoctorInfo>>() {
            @Override
            public void onSuccess(List<SearchDoctorInfo> searchDoctorInfos, BaseResult baseResult) {
                mSearchDoctorAdapter.setNewData(searchDoctorInfos);
                if (LibCollections.isEmpty(searchDoctorInfos)) {
                    mDoctorSearchPrv.setVisibility(View.GONE);
                    mNoResultLl.setVisibility(View.VISIBLE);
                } else {
                    mDoctorSearchPrv.setVisibility(View.VISIBLE);
                    mNoResultLl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.data_upload_faild);
            }
        });
        searchDoctorRequester.realName = search;
        searchDoctorRequester.doPost();
    }

    private void showDoctorCardDialog(String doctorName, String hospitalName, final int doctorId) {
        ShareDoctorCardDialog shareDoctorCardDialog = new ShareDoctorCardDialog(getThisActivity());
        shareDoctorCardDialog.setOnDoctorCardClickListener(new ShareDoctorCardDialog.OnDoctorCardClickListener() {
            @Override
            public void onSendClick(String suggestText) {
                Intent intent = getIntent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, doctorId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        shareDoctorCardDialog.show();
        shareDoctorCardDialog.displayDialog(mPatientBaseInfo, doctorName, hospitalName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SEARCH_DOCTOR) {
                int doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
                Intent intent = getThisActivity().getIntent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, doctorId);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
