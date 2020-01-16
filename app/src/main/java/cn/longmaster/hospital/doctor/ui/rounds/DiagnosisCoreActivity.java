package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.department.DepartmentListRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.picktime.WheelView;
import cn.longmaster.utils.StringUtils;

/**
 * 查房预约信息填写界面
 */
public class DiagnosisCoreActivity extends BaseActivity {
    /* @FindViewById(R.id.activity_diagnosis_core_recycler_view)
     private WrapRecyclerView mDepartmentView;*/
    @FindViewById(R.id.activity_diagnosis_core_time_view)
    private LinearLayout mIntentionTimeView;
    @FindViewById(R.id.activity_diagnosis_core_department_view)
    private LinearLayout mDepartmentView;
    @FindViewById(R.id.activity_diagnosis_add_intention_time_requirement_et)
    private EditText mRequirementEt;

    private List<String> mDiagnosisTimeList = new ArrayList<>();
    private List<String> mSelectDepartmentList = new ArrayList<>();
    private List<String> mTempDepartmentList = new ArrayList<>();
    private List<DepartmentListInfo> mDepartmentList = new ArrayList<>();
    //private List<String> mDepartmentList;
    private WheelView mYear;
    private WheelView mHour;
    private TextView mChoiceTime;
    private String mTime;
    private String mSelectDate;
    private String mDisplayHour;
    private int mPosition;
    private boolean mModificationTime;
    private String mDiagnosisTip;
    private HashMap<Integer, View> mTimeViewMap = new HashMap<>();
    private HashMap<Integer, View> mDepartmentViewMap = new HashMap<>();
    private int mTimeViewTag = 0;
    private int mDepartmentViewTag = 0;
    private PopupWindow mTimePopupWindow;
    private WheelView mDepartmentWheelView;
    private PopupWindow mDepartmentPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_core);
        ViewInjecter.inject(this);
        initData();
        initView();
    }

    private void initData() {
        List<String> mTempTimeList = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_TIME);
        List<String> mTempDepartmentList = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_DEPARTMENT);
        mModificationTime = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MODIFY_EXPERT, false);
        mDiagnosisTip = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_TIP);
        if (mModificationTime) {
            mDiagnosisTimeList = mTempTimeList;
            mSelectDepartmentList = mTempDepartmentList;
        }
        displayDepartmentWheelView();
    }

    private void initView() {
        if (mDiagnosisTimeList.size() > 0) {
            for (int i = 0; i < mDiagnosisTimeList.size(); i++) {
                addIntentionTimeView(mDiagnosisTimeList.get(i));
            }
        }
        if (mSelectDepartmentList.size() > 0) {
            for (int i = 0; i < mSelectDepartmentList.size(); i++) {
                addDepartmentView(mSelectDepartmentList.get(i));
            }
        }
        if (!StringUtils.isEmpty(mDiagnosisTip)) {
            mRequirementEt.setText(mDiagnosisTip);
        }
    }

    @OnClick({R.id.activity_diagnosis_core_determine,
            R.id.activity_diagnosis_add_intention_time_view,
            R.id.activity_diagnosis_add_intention_department_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_diagnosis_core_determine:
                determine();
                break;

            case R.id.activity_diagnosis_add_intention_time_view:
                shwTimePopupWindow();
                break;

            case R.id.activity_diagnosis_add_intention_department_view:
                shwDepartmentPopupWindow();
                break;
        }
    }

    private void determine() {
        if (mDiagnosisTimeList.size() == 0) {
            showToast(getString(R.string.rounds_expected_time));
            return;
        } else if (mSelectDepartmentList.size() == 0) {
            showToast(getString(R.string.rounds_expected_department));
            return;
        }
        //根据科室名拿到科室ID
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < mSelectDepartmentList.size(); i++) {
            for (int j = 0; j < mDepartmentList.size(); j++) {
                if (mSelectDepartmentList.get(i).equals(mDepartmentList.get(j).getDepartmentName())) {
                    tempList.add(mDepartmentList.get(j).getDepartmentId());
                }
            }
        }
        Logger.logI(Logger.COMMON, "DiagnosisCoreActivity-->determine-->tempList" + tempList);
        if (mModificationTime) {
            Intent intent = getIntent();
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_REMARKS, mRequirementEt.getText().toString());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_DIAGNOSIS_CORE, true);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, (ArrayList<String>) mDiagnosisTimeList);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_LIST, (ArrayList<String>) mSelectDepartmentList);
            intent.putIntegerArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_ID_LIST, (ArrayList<Integer>) tempList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent(DiagnosisCoreActivity.this, RoundsMouldInfoActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_REMARKS, mRequirementEt.getText().toString());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_DIAGNOSIS_CORE, true);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, (ArrayList<String>) mDiagnosisTimeList);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_LIST, (ArrayList<String>) mSelectDepartmentList);
            intent.putIntegerArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_DEPARTMENT_ID_LIST, (ArrayList<Integer>) tempList);
            startActivity(intent);
        }

    }

    /**
     * -------初始化意向时间PopupWindow-------
     */
    private void shwTimePopupWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.choice_date_pop_wind_three, null);
        mTimePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mTimePopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);
        TextView sure = (TextView) contentView.findViewById(R.id.sure);
        final TextView title = (TextView) contentView.findViewById(R.id.choice_time);
        mYear = (WheelView) contentView.findViewById(R.id.year);
        mHour = (WheelView) contentView.findViewById(R.id.hour);
        mChoiceTime = (TextView) contentView.findViewById(R.id.choice_time);
        setPickTimeView(title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimePopupWindow != null) {
                    mTimePopupWindow.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSureView();
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
        List<String> hourList = Arrays.asList(getResources().getStringArray(R.array.all_day_hour_desc));
        for (int i = 0; i < hourList.size(); i++) {
            if (hourList.get(i).equals(mDisplayHour)) {
                mPosition = i;
                break;
            }
        }
        mYear.setItems(yearList, 1);
        mHour.setItems(hourList, 0);
        title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem());
        mYear.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem());
            }
        });
        mHour.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                title.setText(mYear.getSelectedItem() + " " + mHour.getSelectedItem());
            }
        });
    }

    public String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + (24 * past));
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String result = CommonlyUtil.getWeek(today, getActivity()) + " " + format.format(today);
        Logger.logI(Logger.COMMON, "shwDatePopupWindow-当前的时间是：result" + result);
        if (past == 0) {
            SimpleDateFormat f = new SimpleDateFormat("HH");
            mDisplayHour = f.format(today);
        }
        return result;
    }

    private void setSureView() {
        mSelectDate = mYear.getSelectedItem();
        String hourItemStr = mHour.getSelectedItem();
        mTime = hourItemStr;
        Logger.logI(Logger.COMMON, "shwDatePopupWindow-选择的时间是：" + mSelectDate + " " + mTime);
        if (mTimePopupWindow != null) {
            mTimePopupWindow.dismiss();
        }
        if (mDiagnosisTimeList.size() == 5) {
            showToast(getString(R.string.rounds_most_five));
            return;
        }
        if (mDiagnosisTimeList.contains(mSelectDate + " " + mTime)) {
            showToast(getString(R.string.rounds_time_repetition));
        } else {
            showNoticeDialog(mSelectDate, mSelectDate + " " + mTime);
        }
    }

    private void showNoticeDialog(String s, String time) {
        String tTime = s.substring(3, 8);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd");
        String cTime = sf.format(new Date());
        Logger.logD(Logger.APPOINTMENT, "setChoiceTimeView-->showNoticeDialog:tTime:" + tTime + " ,cTime :" + cTime);
        if (tTime.equals(cTime)) {
            showFinishDialog(time);
        } else {
            mDiagnosisTimeList.add(time);
            addIntentionTimeView(time);
        }
    }

    private void showFinishDialog(final String time) {
        new CommonDialog.Builder(getActivity())
                .setTitle("温馨提醒")
                .setMessage(R.string.rounds_notice)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {

                    }
                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        mDiagnosisTimeList.add(time);
                        addIntentionTimeView(time);
                    }
                })
                .show();
    }

    private void addIntentionTimeView(String time) {
        View timeView = LayoutInflater.from(this).inflate(R.layout.item_add_intention_time, null, false);
        TextView timeTextView = (TextView) timeView.findViewById(R.id.item_add_intention_tv);
        ImageView deleteView = (ImageView) timeView.findViewById(R.id.item_add_intention_delete);
        deleteView.setTag(mTimeViewTag);
        timeTextView.setText(time);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTimeView(v);
            }
        });
        mIntentionTimeView.addView(timeView);
        mTimeViewMap.put(mTimeViewTag, timeView);
        mTimeViewTag++;
    }

    private void removeTimeView(View v) {
        int tag = (int) v.getTag();
        View view = mTimeViewMap.get(tag);
        mIntentionTimeView.removeView(view);
        TextView textView = (TextView) view.findViewById(R.id.item_add_intention_tv);
        for (int i = 0; i < mDiagnosisTimeList.size(); i++) {
            if (textView.getText().toString().equals(mDiagnosisTimeList.get(i))) {
                mDiagnosisTimeList.remove(i);
                break;
            }
        }
        mTimeViewMap.remove(tag);
    }

    /**
     * -------初始化科室PopupWindow-------
     */
    private void shwDepartmentPopupWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_diagnosis_department_pop_wind, null);
        mDepartmentPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mDepartmentPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView cancel = (TextView) contentView.findViewById(R.id.layout_cancel);
        TextView sure = (TextView) contentView.findViewById(R.id.layout_sure);
        final TextView title = (TextView) contentView.findViewById(R.id.layout_time);
        mDepartmentWheelView = (WheelView) contentView.findViewById(R.id.layout_department_view);
        if (mTempDepartmentList.size() > 0) {
            mDepartmentWheelView.setItems(mTempDepartmentList, 2);
        } else {
            displayDepartmentWheelView();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDepartmentPopupWindow != null) {
                    mDepartmentPopupWindow.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentStr = mDepartmentWheelView.getSelectedItem();
                if (mSelectDepartmentList.contains(departmentStr)) {
                    showToast(getString(R.string.rounds_department_election));
                    if (mDepartmentPopupWindow != null) {
                        mDepartmentPopupWindow.dismiss();
                    }
                } else {
                    mSelectDepartmentList.add(departmentStr);
                    addDepartmentView(departmentStr);
                    if (mDepartmentPopupWindow != null) {
                        mDepartmentPopupWindow.dismiss();
                    }
                }
            }
        });
    }

    private void displayDepartmentWheelView() {
        DepartmentListRequester requester = new DepartmentListRequester(new OnResultListener<List<DepartmentListInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, final List<DepartmentListInfo> departments) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mDepartmentList = departments;
                    AppHandlerProxy.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < departments.size(); i++) {
                                mTempDepartmentList.add(departments.get(i).getDepartmentName());
                            }
                            if (mDepartmentPopupWindow != null) {
                                mDepartmentWheelView.setItems(mTempDepartmentList, 2);
                            }
                        }
                    });
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.doPost();
    }

    private void addDepartmentView(String str) {
        View departmentView = LayoutInflater.from(this).inflate(R.layout.item_add_intention_time, null, false);
        TextView timeTextView = (TextView) departmentView.findViewById(R.id.item_add_intention_tv);
        ImageView deleteView = (ImageView) departmentView.findViewById(R.id.item_add_intention_delete);
        deleteView.setTag(mDepartmentViewTag);
        timeTextView.setText(str);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDepartmentView(v);
            }
        });
        mDepartmentView.addView(departmentView);
        mDepartmentViewMap.put(mDepartmentViewTag, departmentView);
        mDepartmentViewTag++;
    }

    private void removeDepartmentView(View v) {
        int tag = (int) v.getTag();
        View view = mDepartmentViewMap.get(tag);
        mDepartmentView.removeView(view);
        TextView textView = (TextView) view.findViewById(R.id.item_add_intention_tv);
        for (int i = 0; i < mSelectDepartmentList.size(); i++) {
            if (textView.getText().toString().equals(mSelectDepartmentList.get(i))) {
                mSelectDepartmentList.remove(i);
                break;
            }
        }
    }
}
