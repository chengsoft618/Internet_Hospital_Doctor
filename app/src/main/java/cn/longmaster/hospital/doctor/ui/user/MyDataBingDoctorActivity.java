package cn.longmaster.hospital.doctor.ui.user;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.MyDataDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.SearchDoctorInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.SearchDoctorRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.MyDataSearchDoctorAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

public class MyDataBingDoctorActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_my_data_bing_doctor)
    private RelativeLayout activityMyDataBingDoctor;
    @FindViewById(R.id.act_bing_doctor_aab)
    private AppActionBar actBingDoctorAab;
    @FindViewById(R.id.act_bing_doctor_search_doctor_ll)
    private LinearLayout actBingDoctorSearchDoctorLl;
    @FindViewById(R.id.act_bing_doctor_search_doctor_iv)
    private ImageView actBingDoctorSearchDoctorIv;
    @FindViewById(R.id.act_bing_doctor_search_doctor_et)
    private EditText actBingDoctorSearchDoctorEt;
    @FindViewById(R.id.act_bing_doctor_search_doctor_v)
    private View actBingDoctorSearchDoctorV;
    @FindViewById(R.id.act_bing_doctor_srl)
    private SmartRefreshLayout actBingDoctorSrl;
    @FindViewById(R.id.activity_bing_doctor_rv)
    private RecyclerView activityBingDoctorRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @FindViewById(R.id.include_new_no_data_iv)
    private ImageView includeNewNoDataIv;
    @FindViewById(R.id.include_new_no_data_tv)
    private TextView includeNewNoDataTv;
    @FindViewById(R.id.act_bing_doctor_determine_tv)
    private TextView actBingDoctorDetermineTv;

    private MyDataSearchDoctorAdapter mSearchDoctorAdapter;
    private List<MyDataDoctorInfo> mSearchDoctorList = new ArrayList<>();
    private String mLocalPath;
    private int mDoctorId = 0;

    @Override
    protected void initDatas() {
        mLocalPath = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH);
        mSearchDoctorAdapter = new MyDataSearchDoctorAdapter(R.layout.item_my_data_search_doctor, new ArrayList<>(0));
        mSearchDoctorAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyDataDoctorInfo info = (MyDataDoctorInfo) adapter.getItem(position);
            if (null == info) {
                return;
            }
            if (mDoctorId == info.getDoctorId()) {
                mSearchDoctorList.get(position).setElection(false);
                mDoctorId = 0;
            } else {
                mDoctorId = info.getDoctorId();
                for (int i = 0; i < mSearchDoctorList.size(); i++) {
                    if (position == i) {
                        mSearchDoctorList.get(i).setElection(true);
                    } else {
                        mSearchDoctorList.get(i).setElection(false);
                    }
                }
            }
            mSearchDoctorAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_data_bing_doctor;
    }

    @Override
    protected void initViews() {
        activityBingDoctorRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityBingDoctorRv.setAdapter(mSearchDoctorAdapter);
        initListener();
    }

    private void initListener() {
        actBingDoctorSearchDoctorEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MyDataBingDoctorActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (StringUtils.isEmpty(getString(actBingDoctorSearchDoctorEt))) {
                    ToastUtils.showShort(getString(R.string.search_doctor));
                } else {
                    actBingDoctorSrl.autoRefresh();
                }
                return true;
            }
            return false;
        });
        actBingDoctorSrl.setOnRefreshListener(refreshLayout -> {
            mSearchDoctorAdapter.setNewData(null);
            getDoctorList(getString(actBingDoctorSearchDoctorEt), refreshLayout);
        });
        actBingDoctorSrl.setEnableLoadMore(false);
        actBingDoctorDetermineTv.setOnClickListener(v -> {
            if (mDoctorId == 0) {
                ToastUtils.showShort(R.string.user_selection_doctor);
                return;
            }
            Intent intent = new Intent(getThisActivity(), PersonalMaterialActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH, mLocalPath);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, mDoctorId);
            startActivity(intent);
            finish();
        });
    }

    private void getDoctorList(String name, RefreshLayout refreshLayout) {
        SearchDoctorRequester searchDoctorRequester = new SearchDoctorRequester(new DefaultResultCallback<List<SearchDoctorInfo>>() {
            @Override
            public void onSuccess(List<SearchDoctorInfo> searchDoctorInfos, BaseResult baseResult) {
                mDoctorId = 0;
                mSearchDoctorList.clear();
                for (int i = 0; i < searchDoctorInfos.size(); i++) {
                    MyDataDoctorInfo info = new MyDataDoctorInfo();
                    info.setDoctorId(searchDoctorInfos.get(i).getDoctorId());
                    info.setElection(false);
                    mSearchDoctorList.add(info);
                }
                mSearchDoctorAdapter.setNewData(mSearchDoctorList);
                if (LibCollections.isEmpty(mSearchDoctorList)) {
                    mSearchDoctorAdapter.setEmptyView(createEmptyListView());
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.data_upload_faild);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshLayout.finishRefresh();
            }
        });
        searchDoctorRequester.realName = name;
        searchDoctorRequester.doPost();
    }

    public void leftClick(View view) {
        showExitDialog();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setTitle(getString(R.string.rounds_title_cancel_upload))
                .setMessage(R.string.rounds_content_cancel_upload)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {

                    }
                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        if (isExistMainActivity(MainActivity.class)) {
                            Intent i = new Intent(MyDataBingDoctorActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
