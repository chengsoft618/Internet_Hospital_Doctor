package cn.longmaster.hospital.doctor.ui.doctor;

import android.content.Intent;
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

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 搜索医生
 * Created by Yang² on 2016/8/2.
 */
public class DoctorSearchActivity extends NewBaseActivity {
    private int ROOT_FILTER_BY_NAME = 1234;
    private int ROOT_FILTER_BY_SICK = 1235;
    private final int mMaxCount = 15;
    public static final String KEY_TO_QUERY_ROOT_FILTER_TYPE = "_KEY_TO_QUERY_ROOT_FILTER_TYPE_";
    //搜索类型 ROOT_FILTER_BY_NAME、ROOT_FILTER_BY_SICK
    //搜索历史记录类型
    private String KEY_SEARCH_DOCTOR_RECORD = AppPreference.KEY_SEARCH_DOCTOR_BY_NAME_RECORD;
    private int mSearchType;
    @FindViewById(R.id.activity_search_input_et)
    private EditText mInputEt;
    @FindViewById(R.id.activity_search_cancel_tv)
    private TextView mCancelTv;
    @FindViewById(R.id.activity_search_record_title_rl)
    private RelativeLayout mRecordTitleRl;
    @FindViewById(R.id.activity_search_record_fbl)
    private FlexboxLayout mRecordFbl;

    @Override
    protected void initDatas() {
        mSearchType = getIntent().getIntExtra(KEY_TO_QUERY_ROOT_FILTER_TYPE, 0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_search;
    }

    @Override
    protected void initViews() {
        mRecordTitleRl.setVisibility(View.VISIBLE);
        mRecordFbl.setVisibility(View.VISIBLE);
        if (mSearchType == ROOT_FILTER_BY_SICK) {
            mInputEt.setHint("请输入疾病名称");
            KEY_SEARCH_DOCTOR_RECORD = AppPreference.KEY_SEARCH_DOCTOR_BY_SICK_RECORD;
        } else {
            mInputEt.setHint("请输入医生姓名");
            KEY_SEARCH_DOCTOR_RECORD = AppPreference.KEY_SEARCH_DOCTOR_BY_NAME_RECORD;
        }
        setSearchRecord();
        initListener();
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
        List<String> list = new ArrayList<>();
        if (AppPreference.getList(KEY_SEARCH_DOCTOR_RECORD) != null) {
            list = AppPreference.getList(KEY_SEARCH_DOCTOR_RECORD);
        }
        for (String s : list) {
            addRecordView(s, false);
        }
    }

    private void showDeleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getThisActivity());
        builder.setTitle(R.string.sure_delete_title)
                .setMessage(R.string.sure_delete_message)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> {
                    mRecordFbl.removeAllViews();
                    SPUtils.getInstance().remove(KEY_SEARCH_DOCTOR_RECORD);
                })
                .show();

    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mInputEt.getText().toString().trim())) {
            ToastUtils.showShort(R.string.search_no_content);
            return false;
        }
        return true;
    }

    /**
     * 启动搜索
     */
    private void startSearch(String keyWord) {
        AppPreference.setList(KEY_SEARCH_DOCTOR_RECORD, keyWord, mMaxCount);
        Intent intentAppoint = new Intent();
        intentAppoint.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT, keyWord);
        setResult(RESULT_OK, intentAppoint);
        upDateRecordView(keyWord);
        finish();
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
