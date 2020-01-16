package cn.longmaster.hospital.doctor.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.utils.StringUtils;

/**
 * 添加账户
 */
public class AddAccountActivity extends BaseActivity {
    @FindViewById(R.id.activity_account_name_et)
    private EditText mAccountNameEt;
    @FindViewById(R.id.activity_open_bank_name_et)
    private EditText mOpenBankNameEt;
    @FindViewById(R.id.activity_bank_number_et)
    private EditText mBankNumberEt;
    @FindViewById(R.id.activity_addaccount_id_number_et)
    private EditText mIdNumberEt;
    @FindViewById(R.id.activity_add_bank_determine_btn)
    private Button mAddBankDetermineBtn;
    @AppApplication.Manager
    private AccountManager mAccountManager;
    private String mRealName;
    private String mBanName;
    private String mCarNum;
    private String mIdNum;
    private int mAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initDialog();
        ViewInjecter.inject(this);
        addBankNumberListener(mBankNumberEt);
    }

    private void initDialog() {
        mAccountId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, 0);
    }

    //设置账号输入框的输入格式为:4 4 4 4 3
    private void addBankNumberListener(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;
            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }
                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }
                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }
                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }
                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    @OnClick({R.id.activity_add_bank_determine_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_bank_determine_btn:
                if (checkInput() && !isFastClick()) {
                    addAccountRequester();
                }
                break;
        }
    }

    private boolean checkInput() {
        iniGetEdText();
        if (StringUtils.isEmpty(mRealName)) {
            showToast(getResources().getString(R.string.account_withdraw_open_account_name_tips));
            return false;

        }
        if (StringUtils.isEmpty(mBanName)) {
            showToast(getResources().getString(R.string.account_withdraw_open_bank_tips));
            return false;

        }
        if (StringUtils.isEmpty(mCarNum)) {
            showToast(getResources().getString(R.string.account_withdraw_account_number_tips));
            return false;

        }
        if (StringUtils.isEmpty(mIdNum)) {
            showToast(getResources().getString(R.string.account_withdraw_id_card_tips));
            return false;

        }
        return true;
    }

    private void iniGetEdText() {
        mRealName = mAccountNameEt.getText().toString();
        mBanName = mOpenBankNameEt.getText().toString();
        mCarNum = mBankNumberEt.getText().toString().replace(" ", "");
        mIdNum = mIdNumberEt.getText().toString();
    }

    /*private void getIDCardVerifyRequester() {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        IDCardVerifyRequester requester = new IDCardVerifyRequester(new OnResultListener<String>() {
            @Override
            public void onResult(BaseResult baseResult, String s) {
                progressDialog.cancel();
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    addAccountRequester();
                } else if (baseResult.getCode() == RESULT_FAILED) {
                    showToast(R.string.no_network_connection);
                } else {
                    showToast(R.string.patient_id_error_not_match);
                }
            }
        });
        requester.idCard = mIdNum;
        requester.realName = mRealName;
        requester.doPost();
    }*/

    private void addAccountRequester() {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        mAccountManager.addBankAccount(mCarNum, mRealName, mBanName, mIdNum, mAccountId, (baseResult, aVoid) -> {
            progressDialog.cancel();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                showToast(R.string.add_succeed_tips);
                setResult(RESULT_OK);
                finish();
            } else {
                showToast(R.string.no_network_connection);
            }
        });
    }
}
