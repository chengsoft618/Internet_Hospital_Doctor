package cn.longmaster.hospital.doctor.ui.hospital;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author Mloong_Abiao
 * @date 2019/6/4 14:31
 * @description: 医生搜索界面
 */
public class HospitalSearchActivity extends NewBaseActivity {
    @FindViewById(R.id.include_search_title_back_iv)
    private ImageView includeSearchTitleBackIv;
    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.activity_hospital_search_record_title_ll)
    private LinearLayout activityHospitalSearchRecordTitleRl;
    @FindViewById(R.id.activity_hospital_search_clear_iv)
    private ImageView activityHospitalSearchClearIv;
    @FindViewById(R.id.activity_hospital_search_record_fbl)
    private FlexboxLayout activityHospitalSearchRecordFbl;
    private List<String> searchRecordList;
    private int mMaxCount = 15;
    private final int REQUEST_CODE_FOR_HOSPITAL_CONFIRM = 1028;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_hospital_search;
    }

    @Override
    protected void initViews() {
        includeSearchTitleEt.setHint("请输入医院名称");
        includeSearchTitleBackIv.setOnClickListener(view -> onBackPressed());
        includeSearchTitleEt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchStr = getString(includeSearchTitleEt);
                if (StringUtils.isTrimEmpty(searchStr)) {
                    ToastUtils.showShort("请输入搜索内容");
                    return false;
                }
                AppPreference.setList(AppPreference.KEY_SEARCH_HOSPITAL_RECORD, searchStr, mMaxCount);
                startSearchActivity(searchStr);
                return true;
            }
            return false;
        });
        activityHospitalSearchClearIv.setOnClickListener(view -> showDeleteDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSearchRecord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQUEST_CODE_FOR_HOSPITAL_CONFIRM == requestCode) {
                String hospitalName = data.getStringExtra("hospital");
                if (null != hospitalName) {
                    Intent intent = new Intent();
                    intent.putExtra("hospital", hospitalName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    private void initSearchRecord() {
        searchRecordList = AppPreference.getList(AppPreference.KEY_SEARCH_HOSPITAL_RECORD);
        if (LibCollections.isNotEmpty(searchRecordList)) {
            activityHospitalSearchRecordFbl.removeAllViews();
            for (String s : searchRecordList) {
                addRecordView(s, false);
            }
        }
    }

    private void upDateRecordView(String keyWord) {
        for (int i = 0; i < activityHospitalSearchRecordFbl.getChildCount(); i++) {
            if (activityHospitalSearchRecordFbl.getChildAt(i).getTag().equals(keyWord)) {
                activityHospitalSearchRecordFbl.removeViewAt(i);
            }
        }
        if (activityHospitalSearchRecordFbl.getChildCount() >= mMaxCount) {
            activityHospitalSearchRecordFbl.removeViews(mMaxCount - 1, activityHospitalSearchRecordFbl.getChildCount() + 1 - mMaxCount);
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
        View view = getLayoutInflater().inflate(R.layout.item_fbl_hospital_search_history, null);
        TextView textView = view.findViewById(R.id.item_hospital_search_history_tv);
        textView.setText(str);
        textView.setOnClickListener(v -> startSearchActivity(str));
        view.setTag(str);
        if (front) {
            activityHospitalSearchRecordFbl.addView(view, 0);
        } else {
            activityHospitalSearchRecordFbl.addView(view);
        }
    }

    private void showDeleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setTitle(R.string.sure_delete_title)
                .setMessage(R.string.sure_delete_message)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> {
                    activityHospitalSearchRecordFbl.removeAllViews();
                    SPUtils.getInstance().remove(AppPreference.KEY_SEARCH_HOSPITAL_RECORD);
                })
                .show();
    }

    private void startSearchActivity(String str) {
        upDateRecordView(str);
        Intent intent = new Intent(HospitalSearchActivity.this, HospitalSearchResultActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_QUERY_HOSPITAL, str);
        startActivityForResult(intent, REQUEST_CODE_FOR_HOSPITAL_CONFIRM);
    }
}
