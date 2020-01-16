package cn.longmaster.hospital.doctor.ui.doctor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsPatientAddActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.SPUtils;

/**
 * 搜索医生
 * Created by Yang² on 2016/8/2.
 */
public class SearchActivity extends BaseActivity {
    private final String TAG = SearchActivity.class.getCanonicalName();

    private final int REQUEST_CODE_SHARE_DOCTOR = 100;
    private final int mMaxCount = 15;
    public static final String KEY_TO_QUERY_ROOT_FILTER_TYPE = "_KEY_TO_QUERY_ROOT_FILTER_TYPE_";
    @FindViewById(R.id.activity_search_input_et)
    private EditText mInputEt;
    @FindViewById(R.id.activity_search_cancel_tv)
    private TextView mCancelTv;
    @FindViewById(R.id.activity_search_record_title_rl)
    private RelativeLayout mRecordTitleRl;
    @FindViewById(R.id.activity_search_record_fbl)
    private FlexboxLayout mRecordFbl;

    private int mSearchType;
    private String mSearchHint;
    private boolean mIsShare;
    private PatientBaseInfo mPatientBaseInfo;
    private boolean mIsadMedicalRecord;
    private boolean mIsRoundsSearch;
    private boolean mIsOdifyExpert;
    private int mRootFilterType;

    public static void startSearchActivityForResult(Activity activity, int searchType, String hint, int resultCode) {
        startSearchActivityForResult(activity, searchType, hint, resultCode, false, null);
    }

    public static void startSearchActivityForResult(Activity activity, int searchType, String hint, int resultCode, boolean isShare, PatientBaseInfo patientBaseInfo) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_TYPE, searchType);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_HINT, hint);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, isShare);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO, patientBaseInfo);
        activity.startActivityForResult(intent, resultCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewInjecter.inject(this);
        initData();
        initView();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.logI(Logger.COMMON, "onActivityResult-->requestCode:" + requestCode + ",resultCode:" + resultCode + ",mIsadMedicalRecord:" + mIsadMedicalRecord);
        if ((mIsShare || mIsOdifyExpert) && requestCode == REQUEST_CODE_SHARE_DOCTOR && resultCode == RESULT_OK) {
            int doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, 0);
            Intent intent = getIntent();
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, doctorId);
            setResult(RESULT_OK, intent);
            finish();
        } else if (mIsadMedicalRecord && requestCode == REQUEST_CODE_SHARE_DOCTOR && resultCode == RESULT_OK) {
            int doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, 0);
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, RoundsPatientAddActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, doctorId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick({R.id.activity_search_cancel_tv,
            R.id.activity_search_delete_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_search_cancel_tv:
                if (mCancelTv.getText().equals(getString(R.string.search))) {
                    if (checkInput()) {
                        startSearch(mInputEt.getText().toString().trim());
                    }
                } else {
                    finish();
                }
                break;

            case R.id.activity_search_delete_iv:
                showDeleteDialog();
                break;
            default:
                break;
        }
    }

    private void initData() {
        mIsShare = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, false);
        if (mIsShare) {
            mPatientBaseInfo = (PatientBaseInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO);
        }
        mSearchType = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_TYPE, AppConstant.SEARCH_TYPE_DOCTOR);
        mSearchHint = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_HINT);
        mIsadMedicalRecord = getIntent().getBooleanExtra("add_medical_record", false);
        mIsRoundsSearch = getIntent().getBooleanExtra("isRoundsSearch", false);
        mIsOdifyExpert = getIntent().getBooleanExtra("mIsOdifyExpert", false);
        mRootFilterType = getIntent().getIntExtra(KEY_TO_QUERY_ROOT_FILTER_TYPE, 0);
    }

    private void initView() {
        mInputEt.setHint(mSearchHint);
        if (mSearchType == AppConstant.SEARCH_TYPE_DOCTOR) {
            mRecordTitleRl.setVisibility(View.VISIBLE);
            mRecordFbl.setVisibility(View.VISIBLE);
            setSearchRecord();
        } else {
            mRecordTitleRl.setVisibility(View.GONE);
            mRecordFbl.setVisibility(View.GONE);
        }
    }


    private void initListener() {
        mInputEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (checkInput()) {
                    startSearch(mInputEt.getText().toString().trim());
                }
                return true;
            }
            return false;
        });
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mInputEt.getText().toString().length() > 0) {
                    mCancelTv.setText(R.string.search);
                } else {
                    mCancelTv.setText(R.string.cancel);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSearchRecord() {
        if (mSearchType != AppConstant.SEARCH_TYPE_DOCTOR) {
            return;
        }
        List<String> list = new ArrayList<>();
        if (AppPreference.getList(AppPreference.KEY_SEARCH_RECORD) != null) {
            list = AppPreference.getList(AppPreference.KEY_SEARCH_RECORD);
        }
        for (String s : list) {
            addRecordView(s, false);
        }
    }

    private void showDeleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getActivity());
        builder.setTitle(R.string.sure_delete_title)
                .setMessage(R.string.sure_delete_message)
                .setPositiveButton(R.string.cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {

                    }
                })
                .setNegativeButton(R.string.confirm, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        mRecordFbl.removeAllViews();
                        SPUtils.getInstance().remove(AppPreference.KEY_SEARCH_RECORD);
                    }
                })
                .show();

    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mInputEt.getText().toString().trim())) {
            showToast(R.string.search_no_content);
            return false;
        }
        return true;
    }

    /**
     * 启动搜索
     */
    private void startSearch(String keyWord) {
        switch (mSearchType) {
            case AppConstant.SEARCH_TYPE_DOCTOR:
                AppPreference.setList(AppPreference.KEY_SEARCH_RECORD, keyWord, mMaxCount);
                Intent intentDoctor = new Intent(this, DoctorSearchResultActivity.class);
                intentDoctor.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT, keyWord);
                intentDoctor.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, mIsShare);
                intentDoctor.putExtra("add_medical_record", mIsadMedicalRecord);
                intentDoctor.putExtra("mIsRoundsSearch", mIsRoundsSearch);
                intentDoctor.putExtra("mIsOdifyExpert", mIsOdifyExpert);
                intentDoctor.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO, mPatientBaseInfo);
                if (mIsShare || mIsadMedicalRecord || mIsOdifyExpert) {
                    startActivityForResult(intentDoctor, REQUEST_CODE_SHARE_DOCTOR);
                } else {
                    startActivity(intentDoctor);
                }
                mInputEt.setText("");
                upDateRecordView(keyWord);
                break;

            case AppConstant.SEARCH_TYPE_APPOINTMENT:
                Intent intentAppoint = new Intent();
                intentAppoint.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT, keyWord);
                setResult(RESULT_OK, intentAppoint);
                upDateRecordView(keyWord);
                finish();
                break;
            default:
                break;
        }
    }

    private void upDateRecordView(String keyWord) {
        for (int i = 0; i < mRecordFbl.getChildCount(); i++) {
            if (mRecordFbl.getChildAt(i).getTag().equals(keyWord)) {
                mRecordFbl.removeViewAt(i);
            }
        }
        if (mRecordFbl.getChildCount() >= mMaxCount) {
            mRecordFbl.removeViews(mMaxCount - 1, mRecordFbl.getChildCount() + 1 - mMaxCount);
        }
        addRecordView(keyWord, true);
    }

    /**
     * 添加搜索记录
     *
     * @param str   搜索内容
     * @param front 是否放到前面
     */
    private void addRecordView(final String str, boolean front) {
        View view = getLayoutInflater().inflate(R.layout.item_search_record, null);
        TextView textView = (TextView) view.findViewById(R.id.item_search_record_text_tv);
        textView.setText(str);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch(str);
            }
        });
        view.setTag(str);
        if (front) {
            mRecordFbl.addView(view, 0);
        } else {
            mRecordFbl.addView(view);
        }
    }

}
