package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.SearchScheduingInfo;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;

/**
 * 会诊管理搜索页
 * Created by W·H·K on 2017/12/21.
 */
public class ConsultationSearchActivity extends BaseActivity {
    private static final int mMaxCount = 15;
    @FindViewById(R.id.activity_consultation_search_pbl)
    private FlexboxLayout mSearchPbl;
    @FindViewById(R.id.activity_consultation_search_input_et)
    private TextView mInputEt;
    private SearchScheduingInfo mSearcheduingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_search);
        ViewInjecter.inject(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mSearcheduingInfo = (SearchScheduingInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_SCHEDULE_INFO);
    }

    private void initView() {
        if (mSearcheduingInfo != null) {
            mInputEt.setText(mSearcheduingInfo.getKeyWord());
        }
        setSearchRecord();
    }

    private void initListener() {
        /*mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (checkInput()) {
                        startSearch(mInputEt.getText().toString().trim(), searcType);
                    }
                    return true;
                }
                return false;
            }
        });*/
    }

    @OnClick({R.id.activity_consultation_search_back,
            R.id.activity_consultation_search_clear_history,
            R.id.activity_consultation_search_num_tab,
            R.id.activity_consultation_search_doctor_tab,
            R.id.activity_consultation_search_cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_consultation_search_back:
                finish();
                break;

            case R.id.activity_consultation_search_clear_history:
                showDeleteDialog();
                break;

            case R.id.activity_consultation_search_num_tab:
                displayNumTab(AppConstant.SearcType.SEARC_TYPE_NUM_TAB);
                break;

            case R.id.activity_consultation_search_doctor_tab:
                displayDoctorTab(AppConstant.SearcType.SEARC_TYPE_DOCTOR_TAB);
                break;
            case R.id.activity_consultation_search_cancel_tv:
                returnResult("", 0);
                break;
        }
    }

    private void displayNumTab(int searchType) {
        if (checkInput()) {
            startSearch(getString(R.string.advance_num) + mInputEt.getText().toString().trim(), searchType);
        }
    }

    private void displayDoctorTab(int searchType) {
        if (checkInput()) {
            startSearch(getString(R.string.advance_first_visit) + mInputEt.getText().toString().trim(), searchType);
        }
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
    private void startSearch(String keyWord, int searcType) {
        AppPreference.setList(AppPreference.KEY_SEARCH_CONSULTATION_RECORD, keyWord, mMaxCount);
        upDateRecordView(keyWord);
        returnResult(mInputEt.getText().toString().trim(), searcType);
    }

    private void returnResult(String keyWord, int searcType) {
        Intent mIntent = new Intent();
        SearchScheduingInfo info = new SearchScheduingInfo();
        info.setKeyWord(keyWord);
        info.setSearchType(searcType + "");
        mIntent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_SCHEDULE_INFO, info);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private void upDateRecordView(String keyWord) {
        for (int i = 0; i < mSearchPbl.getChildCount(); i++) {
            if (mSearchPbl.getChildAt(i).getTag().equals(keyWord)) {
                mSearchPbl.removeViewAt(i);
            }
        }
        if (mSearchPbl.getChildCount() >= mMaxCount) {
            mSearchPbl.removeViews(mMaxCount - 1, mSearchPbl.getChildCount() + 1 - mMaxCount);
        }
        addRecordView(keyWord, true);
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
                        mSearchPbl.removeAllViews();
                        SPUtils.getInstance().remove(AppPreference.KEY_SEARCH_CONSULTATION_RECORD);
                    }
                })
                .show();
    }

    /**
     * 添加搜索记录
     *
     * @param str   搜索内容
     * @param front 是否放到前面
     */
    private void addRecordView(final String str, boolean front) {
        View view = getLayoutInflater().inflate(R.layout.item_search_record, null);
        final TextView textView = (TextView) view.findViewById(R.id.item_search_record_text_tv);
        textView.setText(str);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textView.getText().toString();
                String result = string.substring(0, string.indexOf(":"));
                String keyword = string.substring(string.indexOf(":") + 1, string.length());
                int searcType = 0;
                if (StringUtils.equals(result, "编号")) {
                    searcType = 1;
                } else if (StringUtils.equals(result, "首诊")) {
                    searcType = 3;
                }
                returnResult(keyword, searcType);
            }
        });
        view.setTag(str);
        if (front) {
            mSearchPbl.addView(view, 0);
        } else {
            mSearchPbl.addView(view);
        }
    }

    private void setSearchRecord() {
        List<String> list = new ArrayList<>();
        if (AppPreference.getList(AppPreference.KEY_SEARCH_CONSULTATION_RECORD) != null) {
            list = AppPreference.getList(AppPreference.KEY_SEARCH_CONSULTATION_RECORD);
        }
        for (String s : list) {
            addRecordView(s, false);
        }
    }
}
