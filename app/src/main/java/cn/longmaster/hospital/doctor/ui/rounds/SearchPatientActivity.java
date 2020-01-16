package cn.longmaster.hospital.doctor.ui.rounds;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.SPUtils;

public class SearchPatientActivity extends BaseActivity {
    private final int REQUEST_CODE_SEARCH_PATIENT = 100;
    private final int mMaxCount = 10;
    @FindViewById(R.id.activity_search_patient_et)
    private EditText mInputEt;
    @FindViewById(R.id.activity_search_patient_cancel_tv)
    private TextView mCancelTv;
    @FindViewById(R.id.activity_search_patient_record_title_rl)
    private RelativeLayout mRecordTitleRl;
    @FindViewById(R.id.activity_search_patient_record_fbl)
    private FlexboxLayout mRecordFbl;

    private int mOrderId;
    private boolean mIsAddPatient;
    private boolean mIsMouldAddPatient;
    private List<Integer> mMedicalRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);
        ViewInjecter.inject(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        mIsAddPatient = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADD_PATIENT, false);
        mIsMouldAddPatient = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MOULD_ADD_PATIENT, false);
        mMedicalRecords = (List<Integer>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_LIST);
    }

    private void initView() {
        mRecordTitleRl.setVisibility(View.VISIBLE);
        mRecordFbl.setVisibility(View.VISIBLE);
        setSearchRecord();
    }

    @OnClick({R.id.activity_search_patient_cancel_tv,
            R.id.activity_search_patient_delete_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_search_patient_cancel_tv:
                if (mCancelTv.getText().equals(getString(R.string.search))) {
                    if (checkInput()) {
                        startSearch(mInputEt.getText().toString().trim());
                    }
                } else {
                    finish();
                }
                break;
            case R.id.activity_search_patient_delete_iv:
                showDeleteDialog();
                break;
        }
    }

    private void initListener() {
        mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (checkInput()) {
                        startSearch(mInputEt.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
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
        List<String> list = new ArrayList<>();
        if (AppPreference.getList(AppPreference.KEY_SEARCH_PATIENT_RECORD) != null) {
            list = AppPreference.getList(AppPreference.KEY_SEARCH_PATIENT_RECORD);
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
                        SPUtils.getInstance().remove(AppPreference.KEY_SEARCH_PATIENT_RECORD);
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
        AppPreference.setList(AppPreference.KEY_SEARCH_PATIENT_RECORD, keyWord, mMaxCount);
        upDateRecordView(keyWord);
        mInputEt.setText("");
        Intent intent = new Intent(SearchPatientActivity.this, SearchPatientResultActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT, keyWord);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mOrderId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADD_PATIENT, mIsAddPatient);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MOULD_ADD_PATIENT, mIsMouldAddPatient);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_LIST, (Serializable) mMedicalRecords);
        startActivityForResult(intent, REQUEST_CODE_SEARCH_PATIENT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.APPOINTMENT, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_PATIENT:
                    int mMedicalId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
                    Intent intent = getIntent();
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, mMedicalId);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
