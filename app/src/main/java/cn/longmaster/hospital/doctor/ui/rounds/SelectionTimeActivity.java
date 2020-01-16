package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorVisitingItem;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorVisitingRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.AddIntentionTimeAdapter;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.picktime.WheelView;
import cn.longmaster.hospital.doctor.view.timetable.ReceptionScheduleView;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 选择时间页面
 */
public class SelectionTimeActivity extends NewBaseActivity {
    @FindViewById(R.id.layout_rounds_choice_time_recommend_view)
    private LinearLayout mRecommendView;
    @FindViewById(R.id.layout_rounds_choice_time_recommend_rsv)
    private ReceptionScheduleView layoutRoundsChoiceTimeRecommendRsv;
    @FindViewById(R.id.layout_rounds_choice_time_intention_rv)
    private RecyclerView mAddTimeRecyclerView;
    private int mDoctorId;
    private List<String> mAddIntentionList = new ArrayList<>();
    private AddIntentionTimeAdapter mAddIntentionTimeAdapter;
    private WheelView mYear;
    private WheelView mHour;
    private TextView mChoiceTime;
    private String mTime;
    private String mSelectDate;
    private boolean mIsModify;

    @Override
    protected void initDatas() {
        mDoctorId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
        mIsModify = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MODIFY_TIME, false);
        if (mIsModify) {
            mAddIntentionList = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_TIME);
        }
        if (mAddIntentionList == null) {
            mAddIntentionList = new ArrayList<>();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_rounds_choice_time_window;
    }

    @Override
    protected void initViews() {
        initAddTimeRecyclerView();
        getDoctorVisiting(mDoctorId);
    }

    private void initAddTimeRecyclerView() {
        mAddTimeRecyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        mAddIntentionTimeAdapter = new AddIntentionTimeAdapter(R.layout.item_add_intention_time, mAddIntentionList);
        mAddIntentionTimeAdapter.setCanDelete(true);
        mAddIntentionTimeAdapter.setOnItemChildClickListener((adapter, view, position) -> mAddIntentionTimeAdapter.remove(position));
        mAddTimeRecyclerView.setAdapter(mAddIntentionTimeAdapter);
    }

    private void shwDatePopupWindow() {
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.choice_date_pop_wind_three, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.showAtLocation(getThisActivity().getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView cancel = contentView.findViewById(R.id.cancel);
        TextView sure = contentView.findViewById(R.id.sure);
        mYear = contentView.findViewById(R.id.year);
        mHour = contentView.findViewById(R.id.hour);
        mChoiceTime = contentView.findViewById(R.id.choice_time);
        setPickTimeView(mChoiceTime);
        cancel.setOnClickListener(v -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        sure.setOnClickListener(v -> {
            mSelectDate = mYear.getSelectedItem();
            String hourItemStr = mHour.getSelectedItem();
            mTime = hourItemStr;
            Logger.logI(Logger.COMMON, "shwDatePopupWindow-选择的时间是：" + mSelectDate + " " + mTime);
            mPopupWindow.dismiss();
            if (LibCollections.size(mAddIntentionList) >= 5) {
                ToastUtils.showShort(R.string.rounds_most_five);
                return;
            }
            if (mAddIntentionList.contains(mSelectDate + " " + mTime)) {
                ToastUtils.showShort(R.string.rounds_time_repetition);
            } else {
                showNoticeDialog(mSelectDate, mSelectDate + " " + mTime);
            }
        });
    }

    public void setPickTimeView(final TextView title) {
        mYear.setVisibility(View.VISIBLE);
        mHour.setVisibility(View.VISIBLE);
        mChoiceTime.setText(getString(R.string.chat_please_choice_time));
        List<String> yearList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String date = getFetureDate(i);
            yearList.add(date);
        }
        List<String> hourList = new ArrayList<>();
        hourList.add(getString(R.string.morning));
        hourList.add(getString(R.string.noon));
        hourList.add(getString(R.string.afternoon));
        hourList.add(getString(R.string.other));
        mYear.setItems(yearList, 1);
        mHour.setItems(getHourList(), 0);
        title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem());
        mYear.setOnItemSelectedListener((selectedIndex, item) -> title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem()));
        mHour.setOnItemSelectedListener((selectedIndex, item) -> title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem()));
    }

    public String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + (24 * past));
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String result = CommonlyUtil.getWeek(today, getThisActivity()) + " " + format.format(today);
        Logger.logI(Logger.COMMON, "shwDatePopupWindow-当前的时间是：result" + result);
        return result;
    }

    private List<String> getHourList() {
        return Arrays.asList(getResources().getStringArray(R.array.all_day_hour_desc));
    }

    private void showNoticeDialog(String s, String time) {
        String tTime = s.substring(3, 8);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd");
        String cTime = sf.format(new Date());
        Logger.logD(Logger.APPOINTMENT, "setChoiceTimeView-->showNoticeDialog:tTime:" + tTime + " ,cTime :" + cTime);
        if (tTime.equals(cTime)) {
            showFinishDialog(time);
        } else {
            mAddIntentionTimeAdapter.addData(time);
            mAddTimeRecyclerView.scrollToPosition(mAddIntentionTimeAdapter.getItemCount());
        }
    }

    private void showFinishDialog(final String s) {
        new CommonDialog.Builder(getThisActivity())
                .setTitle("温馨提醒")
                .setMessage(R.string.rounds_notice)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> mAddIntentionTimeAdapter.addData(s))
                .show();
    }

    @OnClick({R.id.layout_rounds_choice_time_delete,
            R.id.layout_rounds_choice_time_determine,
            R.id.layout_rounds_choice_intention_time_stv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_rounds_choice_time_delete:
                finish();
                break;
            case R.id.layout_rounds_choice_time_determine:
                if (LibCollections.isEmpty(mAddIntentionList)) {
                    ToastUtils.showShort(R.string.rounds_expected_time);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, mDoctorId);
                intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, (ArrayList<String>) mAddIntentionList);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;

            case R.id.layout_rounds_choice_intention_time_stv:
                shwDatePopupWindow();
                break;
            default:
                break;
        }
    }

    private void getDoctorVisiting(int doctorId) {
        GetDoctorVisitingRequester requester = new GetDoctorVisitingRequester(new DefaultResultCallback<List<DoctorVisitingItem>>() {
            @Override
            public void onSuccess(List<DoctorVisitingItem> doctorVisitingItems, BaseResult baseResult) {
                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && LibCollections.isNotEmpty(doctorVisitingItems)) {
                    mRecommendView.setVisibility(View.VISIBLE);
                    layoutRoundsChoiceTimeRecommendRsv.setReceptionSchedules(doctorVisitingItems);
                } else {
                    mRecommendView.setVisibility(View.GONE);
                }
            }
        });
        requester.setDoctorId(doctorId);
        requester.start();
    }
}
