package cn.longmaster.hospital.doctor.ui.department;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
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
 * @date 2019/6/4 20:20
 * @description: 科室搜索界面
 */
public class DepartmentSearchActivity extends NewBaseActivity {
    @FindViewById(R.id.include_search_title_back_iv)
    private ImageView includeSearchTitleBackIv;
    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.activity_department_search_record_title_rl)
    private RelativeLayout activityDepartmentSearchRecordTitleRl;
    @FindViewById(R.id.activity_department_search_clear_iv)
    private ImageView activityDepartmentSearchClearIv;
    @FindViewById(R.id.activity_department_search_record_fbl)
    private FlexboxLayout activityDepartmentSearchRecordFbl;
    private final int REQUEST_CODE_FOR_DEPARTMENT_CONFIRM = 1026;
    private final int mMaxCount = 15;
    private List<String> searchRecordList;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_department_search;
    }

    @Override
    protected void initViews() {
        includeSearchTitleEt.setHint("请输入科室名称");
        initListener();
    }

    private void initListener() {
        includeSearchTitleBackIv.setOnClickListener(view -> onBackPressed());
        includeSearchTitleEt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchStr = getString(includeSearchTitleEt);
                if (StringUtils.isTrimEmpty(searchStr)) {
                    ToastUtils.showShort("请输入搜索内容");
                    return false;
                }
                AppPreference.setList(AppPreference.KEY_SEARCH_DEPARTMENT_RECORD, searchStr, mMaxCount);
                startSearchActivity(searchStr);
                return true;
            }
            return false;
        });
        activityDepartmentSearchClearIv.setOnClickListener(view -> showDeleteDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchRecordList = AppPreference.getList(AppPreference.KEY_SEARCH_DEPARTMENT_RECORD);
        if (null == searchRecordList) {
            searchRecordList = new ArrayList<>();
        }
        initSearchRecord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_DEPARTMENT_CONFIRM) {
                Intent intent = new Intent();
                intent.putExtra("department", data.getStringExtra("department"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private void initSearchRecord() {
        if (LibCollections.isNotEmpty(searchRecordList)) {
            activityDepartmentSearchRecordFbl.removeAllViews();
            for (String s : searchRecordList) {
                addRecordView(s, false);
            }
        }
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity(str);
            }
        });
        view.setTag(str);
        if (front) {
            activityDepartmentSearchRecordFbl.addView(view, 0);
        } else {
            activityDepartmentSearchRecordFbl.addView(view);
        }
    }

    private void showDeleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
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
                        activityDepartmentSearchRecordFbl.removeAllViews();
                        SPUtils.getInstance().remove(AppPreference.KEY_SEARCH_DEPARTMENT_RECORD);
                    }
                })
                .show();

    }

    private void startSearchActivity(String str) {
        Intent intent = new Intent(DepartmentSearchActivity.this, DepartmentSearchResultActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_QUERY_DEPARTMENT, str);
        startActivityForResult(intent, REQUEST_CODE_FOR_DEPARTMENT_CONFIRM);
    }
}
