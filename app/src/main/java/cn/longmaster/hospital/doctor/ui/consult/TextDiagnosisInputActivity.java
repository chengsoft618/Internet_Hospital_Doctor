package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisContentInfo;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;

/**
 * 文字输入
 * Created by Yang² on 2016/6/6.
 */
public class TextDiagnosisInputActivity extends BaseActivity {
    @FindViewById(R.id.activity_text_input_title_bar)
    private AppActionBar mActionBar;
    @FindViewById(R.id.activity_text_input_et)
    private EditText mTextInputEt;

    private String mDefaultText;
    private DiagnosisContentInfo mDiagnosisContentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);
        ViewInjecter.injectView(this);
        initData();
        initView();
    }

    private void initData() {
        mDefaultText = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DEFAULT_TEXT);
        mDiagnosisContentInfo = (DiagnosisContentInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_CONTENT_INFO);
    }

    private void initView() {
        if (!TextUtils.isEmpty(mDefaultText)) {
            mTextInputEt.setText(mDefaultText);
            mTextInputEt.setSelection(mDefaultText.length());
        }
    }

    public void leftClick(View v) {
        finish();
    }

    public void rightClick(View v) {
        if (checkInput()) {
            save();
        }
    }

    private void save() {
        Intent intent = getIntent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DEFAULT_TEXT, mTextInputEt.getText().toString().trim());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_CONTENT_INFO, mDiagnosisContentInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mTextInputEt.getText().toString().trim())) {
            showToast(R.string.text_input_empty);
            return false;
        }
        return true;
    }


}
