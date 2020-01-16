package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDoctorList;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitScreenInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.VisitDoctorListRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.VisitDoctorStarRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.VisitScreenDoctorAdapter;


/**
 * 顾问筛选条件界面
 * Created by Yang² on 2018/1/3.
 */

public class VisitScreenActivity extends BaseActivity {
    @FindViewById(R.id.activity_visit_screen_time_rg)
    private RadioGroup mTimeRg;
    @FindViewById(R.id.activity_visit_screen_two_month_rb)
    private RadioButton mTwoMonRb;
    @FindViewById(R.id.activity_visit_screen_three_month_rb)
    private RadioButton mThreeMonthRb;
    @FindViewById(R.id.activity_visit_screen_half_year_rb)
    private RadioButton mHalfYearRb;
    @FindViewById(R.id.activity_visit_screen_one_year_rb)
    private RadioButton mOneYearRb;
    @FindViewById(R.id.activity_visit_screen_end_time_tv)
    private TextView mEndTimeTv;
    @FindViewById(R.id.activity_visit_screen_start_time_tv)
    private TextView mStartTimeTv;
    @FindViewById(R.id.activity_visit_screen_doctor_rg)
    private RadioGroup mDoctorRg;
    @FindViewById(R.id.activity_visit_screen_no_doctor_rb)
    private RadioButton mNoDoctorRb;
    @FindViewById(R.id.activity_visit_screen_all_rb)
    private RadioButton mAllRb;
    @FindViewById(R.id.activity_visit_screen_swiperefreshlayout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @FindViewById(R.id.activity_visit_screen_recycleview)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.activity_visit_screen_no_data)
    private TextView mNoDataTv;
    @FindViewById(R.id.activity_visit_screen_reset)
    private Button mResetBtn;
    @FindViewById(R.id.activity_visit_screen_submit)
    private Button mSubmitBtn;

    private VisitScreenDoctorAdapter mScreenWindowAdapter;
    private int mPageIndex = 1;
    private VisitScreenInfo mVisitScreenInfo;

    public static void startScreenActivityForResult(Activity activity, VisitScreenInfo visitScreenInfo, int resultCode) {
        Intent intent = new Intent(activity, VisitScreenActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCREEN, visitScreenInfo);
        activity.startActivityForResult(intent, resultCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_screen);
        ViewInjecter.inject(this);
        initData();
        initRecycleView();
        setView();
        addListener();
        getVisitDoctorList();
    }

    private void initData() {
        mVisitScreenInfo = (VisitScreenInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCREEN);
    }

    private void initRecycleView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mScreenWindowAdapter = new VisitScreenDoctorAdapter(getActivity(), R.layout.item_visit_screen_doctor);
        mRecyclerView.setAdapter(mScreenWindowAdapter);
        mRecyclerView.getItemAnimator().setChangeDuration(0);//解决更新闪烁问题
        //首次加载菊花
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void setView() {
        if (mVisitScreenInfo != null) {
            if (!TextUtils.isEmpty(mVisitScreenInfo.getBeginDt())) {
                mStartTimeTv.setText(DateUtil.getTime(mVisitScreenInfo.getBeginDt(), "yyy-MM-dd"));
            }
            if (!TextUtils.isEmpty(mVisitScreenInfo.getEndDt())) {
                mEndTimeTv.setText(DateUtil.getTime(mVisitScreenInfo.getEndDt(), "yyy-MM-dd"));
            }
            if (mVisitScreenInfo.getDoctorId() == 0) {
                mAllRb.setChecked(true);
            } else if (mVisitScreenInfo.getDoctorId() == -1) {
                mNoDoctorRb.setChecked(true);
            }
            mScreenWindowAdapter.setSelectId(mVisitScreenInfo.getDoctorId());
            mTimeRg.check(mVisitScreenInfo.getSelectTimeResId());
        }
    }

    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mScreenWindowAdapter.replaceData(new ArrayList<VisitDoctorInfo>());
                mNoDataTv.setVisibility(View.GONE);
                mPageIndex = 1;
                mScreenWindowAdapter.setEnableLoadMore(true);
                getVisitDoctorList();
            }
        });
        mScreenWindowAdapter.setOnScreenItemClickListener(new VisitScreenDoctorAdapter.OnScreenItemClickListener() {
            @Override
            public void onItemClick(VisitDoctorInfo visitDoctorInfo) {
                mDoctorRg.clearCheck();
            }

            @Override
            public void onStarClick(VisitDoctorInfo visitDoctorInfo, int position) {
                doctorStarRequester(visitDoctorInfo.getUserId(), TextUtils.isEmpty(visitDoctorInfo.getStartDt()) ? 1 : 2, position);
            }
        });

        mScreenWindowAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageIndex++;
                getVisitDoctorList();
            }
        }, mRecyclerView);

        mTimeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mTwoMonRb.isChecked()) {
                    mStartTimeTv.setText(DateUtil.getMonthAgo(new Date(System.currentTimeMillis()), 1));
                    mEndTimeTv.setText(DateUtil.getTime(new Date(System.currentTimeMillis()), "yyy-MM-dd"));
                } else if (mThreeMonthRb.isChecked()) {
                    mStartTimeTv.setText(DateUtil.getMonthAgo(new Date(System.currentTimeMillis()), 2));
                    mEndTimeTv.setText(DateUtil.getTime(new Date(System.currentTimeMillis()), "yyy-MM-dd"));
                } else if (mHalfYearRb.isChecked()) {
                    mStartTimeTv.setText(DateUtil.getMonthAgo(new Date(System.currentTimeMillis()), 5));
                    mEndTimeTv.setText(DateUtil.getTime(new Date(System.currentTimeMillis()), "yyy-MM-dd"));
                } else if (mOneYearRb.isChecked()) {
                    mStartTimeTv.setText(DateUtil.getMonthAgo(new Date(System.currentTimeMillis()), 11));
                    mEndTimeTv.setText(DateUtil.getTime(new Date(System.currentTimeMillis()), "yyy-MM-dd"));
                }
            }
        });

        mDoctorRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mNoDoctorRb.isChecked()) {
                    mScreenWindowAdapter.setSelectId(-1);
                } else if (mAllRb.isChecked()) {
                    mScreenWindowAdapter.setSelectId(0);
                }
            }
        });
    }

    @OnClick({R.id.activity_visit_screen_reset
            , R.id.activity_visit_screen_submit
            , R.id.activity_visit_screen_start_time_tv
            , R.id.activity_visit_screen_end_time_tv
            , R.id.activity_visit_screen_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_visit_screen_reset:
                resetData();
                break;

            case R.id.activity_visit_screen_submit:
                saveData();
                break;

            case R.id.activity_visit_screen_start_time_tv:
                datePicker(mStartTimeTv);
                break;

            case R.id.activity_visit_screen_end_time_tv:
                datePicker(mEndTimeTv);
                break;

            case R.id.activity_visit_screen_no_data:
                //首次加载菊花
                mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
                mSwipeRefreshLayout.setRefreshing(true);
                mScreenWindowAdapter.replaceData(new ArrayList<VisitDoctorInfo>());
                mNoDataTv.setVisibility(View.GONE);
                mPageIndex = 1;
                mScreenWindowAdapter.setEnableLoadMore(true);
                getVisitDoctorList();
                break;
        }
    }

    private void resetData() {
        mTimeRg.clearCheck();
        mStartTimeTv.setText("");
        mEndTimeTv.setText("");
        mAllRb.setChecked(true);
        mScreenWindowAdapter.setSelectId(0);
        Intent intent = new Intent();
        VisitScreenInfo visitScreenInfo = new VisitScreenInfo();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCREEN, visitScreenInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveData() {
        VisitScreenInfo visitScreenInfo = new VisitScreenInfo();
        visitScreenInfo.setBeginDt(TextUtils.isEmpty(mStartTimeTv.getText().toString()) ? "" : mStartTimeTv.getText().toString() + " 00:00:00");
        visitScreenInfo.setEndDt(TextUtils.isEmpty(mEndTimeTv.getText().toString()) ? "" : mEndTimeTv.getText().toString() + " 23:59:59");
        visitScreenInfo.setDoctorId(mScreenWindowAdapter.getSelectId());
        visitScreenInfo.setSelectTimeResId(mTimeRg.getCheckedRadioButtonId());
        Intent intent = new Intent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCREEN, visitScreenInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getVisitDoctorList() {
        VisitDoctorListRequester requester = new VisitDoctorListRequester(new OnResultListener<VisitDoctorList>() {
            @Override
            public void onResult(BaseResult baseResult, final VisitDoctorList visitDoctorList) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (mPageIndex == 1) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mScreenWindowAdapter.replaceData(visitDoctorList.getVisitDoctorList());
                        mNoDataTv.setVisibility(visitDoctorList.getVisitDoctorList().size() == 0 ? View.VISIBLE : View.GONE);
                        mSwipeRefreshLayout.setVisibility(visitDoctorList.getVisitDoctorList().size() == 0 ? View.GONE : View.VISIBLE);
                    } else {
                        mScreenWindowAdapter.addData(visitDoctorList.getVisitDoctorList());
                        mNoDataTv.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                    if (visitDoctorList.getIsFinish() == 0) {
                        mScreenWindowAdapter.loadMoreEnd();
                    } else {
                        mScreenWindowAdapter.loadMoreComplete();
                    }
                } else {
                    if (mPageIndex == 1) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mNoDataTv.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        mNoDataTv.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        mScreenWindowAdapter.loadMoreFail();
                    }
                }
            }
        });
        requester.pageIndex = mPageIndex;
        requester.pageSize = 24;
        requester.doPost();
    }

    private void doctorStarRequester(int doctorId, final int editType, final int position) {
        VisitDoctorStarRequester requester = new VisitDoctorStarRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mScreenWindowAdapter.getData().get(position).setStartDt(editType == 1 ? System.currentTimeMillis() + "" : "");
                    mScreenWindowAdapter.notifyItemChanged(position);
                }
            }
        });
        requester.doctorId = doctorId;
        requester.editType = editType;
        requester.doPost();
    }

    private void datePicker(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        String date = textView.getText().toString();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(new Date(DateUtil.dateToMillisecond(date + " 00:00:00")));
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                textView.setText(DateFormat.format("yyy-MM-dd", c));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
