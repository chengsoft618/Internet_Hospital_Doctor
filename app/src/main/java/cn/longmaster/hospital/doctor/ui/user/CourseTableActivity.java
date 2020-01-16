package cn.longmaster.hospital.doctor.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectAppointTimeInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectListInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectOptionalTimeInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.CancelReservationCourseRequester;
import cn.longmaster.hospital.doctor.core.requests.user.GetProjectListRequester;
import cn.longmaster.hospital.doctor.core.requests.user.ProjectDetailsRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDetailActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.CalendarAdapter;
import cn.longmaster.hospital.doctor.ui.user.adapter.ProjectAdapter;
import cn.longmaster.hospital.doctor.view.SingleFlowPopuwindow;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.timetable.SpecialCalendar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Mod by biao on 2019/9/2
 */
public class CourseTableActivity extends NewBaseActivity {
    private final int REQUEST_CODE_APPOINTMENT = 100;
    private final int REQUEST_CODE_FILL_CONSULT_INFO = 101;
    private final String FORMAT_TYPE = "yyyy年MM月dd日";
    private final String FORMAT_TYPE_AND_WEEK = "yyyy年MM月dd日(EEEE)";
    private final String FORMAT_TYPE_WEEK = "EEEE";
    private final DateFormat dateFormat = new SimpleDateFormat(FORMAT_TYPE, Locale.getDefault());
    private final DateFormat dateFormatAndWeek = new SimpleDateFormat(FORMAT_TYPE_AND_WEEK, Locale.getDefault());
    private final DateFormat dateFormatWeek = new SimpleDateFormat(FORMAT_TYPE_WEEK, Locale.getDefault());
    @FindViewById(R.id.activity_course_table_project_name_tv)
    private TextView mProjectNameTv;
    @FindViewById(R.id.activity_course_table_appointment_course_number_tv)
    private TextView mAppointmentCourseNumberTv;
    @FindViewById(R.id.activity_course_table_not_appointment_course_number_tv)
    private TextView mNotAppointmentCourseNumberTv;
    @FindViewById(R.id.activity_course_table_finish_course_number_tv)
    private TextView mFinishCourseNumberTv;
    @FindViewById(R.id.activity_course_table_title_week_tv)
    private TextView mTitleWeekTv;
    @FindViewById(R.id.activity_course_table_date_tv)
    private TextView mTitleDateTv;
    @FindViewById(R.id.activity_course_table_course_already_appointment_view)
    private LinearLayout mAlreadyAppointmentView;
    @FindViewById(R.id.activity_course_table_no_reservation_view)
    private LinearLayout mNotReservationView;
    @FindViewById(R.id.activity_course_table_cannot_reservation_view)
    private LinearLayout mCannotReservationView;
    @FindViewById(R.id.activity_course_table_project_view)
    private LinearLayout mProjectView;
    @FindViewById(R.id.activity_course_table_project_choice_iv)
    private ImageView mSwitchIm;
    @FindViewById(R.id.activity_course_table_select_tim_tv)
    private TextView mSelectTimTv;
    @FindViewById(R.id.id_gv_select_item)
    private GridView mIdGvSelectItem;
    @FindViewById(R.id.activity_course_table_course_name_tv)
    private TextView mCourseNameTv;
    @FindViewById(R.id.activity_course_table_time_tv)
    private TextView mAppointmentTimeTv;
    @FindViewById(R.id.activity_course_table_state_tv)
    private TextView mAppointmentStateTv;
    @FindViewById(R.id.activity_course_table_tip_tv)
    private TextView mTipTv;
    @FindViewById(R.id.activity_course_table_bottom_btn)
    private TextView mBottomBtn;
    @FindViewById(R.id.activity_course_table_no_reservation_btn)
    private TextView mNoReservationBtn;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private final long mInitialTime = TimeUtils.getNowMills();
    private CalendarAdapter calendarAdapter;
    private ProjectAdapter projectAdapter;
    //专科课程弹窗
    private SingleFlowPopuwindow mPopupWindow;
    //当前选择时间
    private String mCurrentSelectTime;
    //当前时间星期几
    private String mCurrentWeek;
    //当前时间
    private String mCurrentTimeWithWeek;
    //专科项目列表
    private List<ProjectListInfo> mProjectListInfos;
    //当前专科项目index
    private int mCurrentProjectListInfoItemId = 0;
    //当前专科建设列表信息
    private ProjectListInfo mCurrentProjectListInfo = new ProjectListInfo();
    //当前专科建设详情
    private ProjectDetailsInfo mCurrentProjectDetailsInfo = new ProjectDetailsInfo();
    //专科建设已经预约时间信息
    private ProjectAppointTimeInfo mCurrentProjectAppointTimeInfo = new ProjectAppointTimeInfo();

    private List<Integer> mOptionalTimes = new ArrayList<>();//可选时间list
    private List<String> mAgreedTimes = new ArrayList<>();//已预约时间list
    private ProgressDialog mProgressDialog;

    private Calendar calendar;

    @OnClick({R.id.activity_course_table_project_choice_iv,
            R.id.activity_course_table_last_year,
            R.id.activity_course_table_last_month,
            R.id.activity_course_table_next_month,
            R.id.activity_course_table_next_year,
            R.id.activity_course_table_no_reservation_btn,
            R.id.activity_course_table_bottom_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_course_table_project_choice_iv:
                showProjectDialog(mProjectListInfos);
                break;
            case R.id.activity_course_table_last_year:
                displayLastNextYearView(-1);
                break;
            case R.id.activity_course_table_last_month:
                displayLastNextMonthView(-1);
                break;
            case R.id.activity_course_table_next_month:
                displayLastNextMonthView(1);
                break;
            case R.id.activity_course_table_next_year:
                displayLastNextYearView(1);
                break;
            case R.id.activity_course_table_no_reservation_btn:
                reservationCourse(mCurrentProjectDetailsInfo, mInitialTime, mCurrentSelectTime, mCurrentProjectListInfo.getUserType());
                break;
            case R.id.activity_course_table_bottom_btn:
                setBottomView(mCurrentProjectListInfo);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initDatas() {
        calendar = Calendar.getInstance();
        mCurrentSelectTime = TimeUtils.millis2String(mInitialTime, dateFormat);
        mCurrentTimeWithWeek = TimeUtils.millis2String(mInitialTime, dateFormatAndWeek);
        mCurrentWeek = TimeUtils.millis2String(mInitialTime, dateFormatWeek);
        calendarAdapter = new CalendarAdapter(this, mInitialTime);
        calendarAdapter.setOnItemClickListener((view, position) -> {
            TextView mTv = view.findViewById(R.id.id_tv_item_select_time_day);
            int tag = (int) mTv.getTag();
            mCurrentSelectTime = calendarAdapter.getItemTime(position);
            mTitleDateTv.setText(mCurrentSelectTime);
            mTitleWeekTv.setText(TimeUtils.string2String(mCurrentSelectTime, dateFormat, dateFormatWeek));
            mSelectTimTv.setText(TimeUtils.string2String(mCurrentSelectTime, dateFormat, dateFormatAndWeek));
            calendarAdapter.setCurrentPosition(position);
            displayCalendarItemView(tag, mCurrentSelectTime);
        });
        projectAdapter = new ProjectAdapter(R.layout.item_project_list, new ArrayList<>(0));
        projectAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProjectListInfo info = (ProjectListInfo) adapter.getItem(position);
            if (null == info) {
                ToastUtils.showShort("数据异常，请重新加载页面");
                return;
            }
            mPopupWindow.dismiss();
            mCurrentProjectListInfoItemId = position;
            mCurrentProjectListInfo = info;
            calendar.setTimeInMillis(mInitialTime);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            upDateData(year, month, day);
            getProjectDetails(info.getItemId());
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_time_table;
    }

    @Override
    protected void initViews() {
        mTitleDateTv.setText(mCurrentSelectTime);
        mSelectTimTv.setText(mCurrentTimeWithWeek);
        mTitleWeekTv.setText(mCurrentWeek);
        mIdGvSelectItem.setAdapter(calendarAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProjectList(mCurrentProjectListInfoItemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_APPOINTMENT:
            case REQUEST_CODE_FILL_CONSULT_INFO:
                if (resultCode == RESULT_OK) {
                    getProjectDetails(mProjectListInfos.get(mCurrentProjectListInfoItemId).getItemId());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void displayCalendarItemView(int tag, String time) {
        Logger.logI(Logger.APPOINTMENT, TAG + "->displayCalendarItemView->tag：" + tag);
        //可约view
        if (tag == AppConstant.CurrentDateState.CAN_RESERVATION) {
            mAlreadyAppointmentView.setVisibility(View.GONE);
            mNotReservationView.setVisibility(View.VISIBLE);
            mNoReservationBtn.setVisibility(View.VISIBLE);
            mCannotReservationView.setVisibility(View.GONE);
        } else if (tag == AppConstant.CurrentDateState.ALREADY_RESERVATION) {
            //已经预约view
            displayReservationView(time, mCurrentProjectDetailsInfo);
        } else {
            //不可预约view
            mAlreadyAppointmentView.setVisibility(View.GONE);
            mNotReservationView.setVisibility(View.GONE);
            mNoReservationBtn.setVisibility(View.GONE);
            mCannotReservationView.setVisibility(View.VISIBLE);
        }
        calendarAdapter.notifyDataSetChanged();
    }

    private void displayReservationView(String time, ProjectDetailsInfo projectDetailsInfo) {
        mAlreadyAppointmentView.setVisibility(View.VISIBLE);
        mNotReservationView.setVisibility(View.GONE);
        mNoReservationBtn.setVisibility(View.GONE);
        mCannotReservationView.setVisibility(View.GONE);
        for (int i = 0; i < LibCollections.size(projectDetailsInfo.getAppointTimeList()); i++) {
            ProjectAppointTimeInfo info = projectDetailsInfo.getAppointTimeList().get(i);
            String appointTime = info.getAppointTime();
            long tempTime = TimeUtils.string2Millis(appointTime);
            if (TimeUtils.millis2String(tempTime, dateFormat).equals(time)) {
                mCurrentProjectAppointTimeInfo = info;
                setReservationView(info);
                break;
            }
        }
    }

    private void setReservationView(ProjectAppointTimeInfo projectAppointTimeInfo) {
        mCourseNameTv.setText(projectAppointTimeInfo.getCourseName());
        if (StringUtils.isEmpty(projectAppointTimeInfo.getAppointTime())) {
            mAppointmentTimeTv.setVisibility(View.GONE);
        } else {
            mAppointmentTimeTv.setVisibility(View.VISIBLE);
            mAppointmentTimeTv.setText(projectAppointTimeInfo.getAppointTime());
        }
        if (StringUtils.isEmpty(projectAppointTimeInfo.getAppointmentStatRemark())) {
            mAppointmentStateTv.setVisibility(View.GONE);
        } else {
            mAppointmentStateTv.setVisibility(View.VISIBLE);
            mAppointmentStateTv.setText("课程状态: " + projectAppointTimeInfo.getAppointmentStatRemark());
        }
        if (projectAppointTimeInfo.getAppointmentId() == 0) {
            Logger.logI(Logger.COMMON, TAG + "-->reservationCourse-->AppointType：" + projectAppointTimeInfo.getAppointType() + " ,projectAppointTimeInfo:" + projectAppointTimeInfo);
            if (projectAppointTimeInfo.getAppointType() == 1) {
                mTipTv.setVisibility(View.GONE);
                int untilAppoint = mCurrentProjectDetailsInfo.getUntilAppoint();//提前预约天数
                String tempTime = SpecialCalendar.LongToString(mInitialTime, FORMAT_TYPE);
                long current = SpecialCalendar.StringToLong(mCurrentSelectTime, FORMAT_TYPE);
                long initial = SpecialCalendar.StringToLong(tempTime, FORMAT_TYPE);
                long diff = current - initial;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                Logger.logI(Logger.COMMON, TAG + "-->reservationCourse-->current：" + current + ",initial:" + initial + ",diff:" + diff + ".diffDays:" + diffDays);
                if (diffDays <= untilAppoint) {
                    mBottomBtn.setText(getString(R.string.initiate_appointment));
                } else {
                    mBottomBtn.setText(getString(R.string.cancel_appointment));
                }
            } else if (projectAppointTimeInfo.getAppointType() == 3) {
                mTipTv.setVisibility(View.GONE);
                mBottomBtn.setText(getString(R.string.cancel_appointment));
            } else {
                mTipTv.setVisibility(View.GONE);
                mBottomBtn.setText(getString(R.string.no_reservation_system_selected));
            }
        } else {
            mTipTv.setVisibility(View.VISIBLE);
            mTipTv.setText(getString(R.string.user_class_tip, mCurrentProjectDetailsInfo.getUntilAppoint() + ""));
            mBottomBtn.setText(getString(R.string.class_entry_details));
        }
    }

    /**
     * 获取专科项目列表
     */
    private void getProjectList(int currentItemId) {
        showProgressDialog();
        GetProjectListRequester requester = new GetProjectListRequester((baseResult, projectListInfos) -> {
            dismissProgressDialog();
            Logger.logD(Logger.COMMON, "->getProjectList()->baseResult:" + baseResult.getCode() + ", projectListInfos:" + projectListInfos);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && projectListInfos != null) {
                mProjectListInfos = projectListInfos;
                int projectListSize = LibCollections.size(projectListInfos);
                if (projectListSize < currentItemId) {
                    return;
                }
                mCurrentProjectListInfo = projectListInfos.get(currentItemId);
                getProjectDetails(mCurrentProjectListInfo.getItemId());
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.doPost();
    }

    /**
     * 获取项目详情
     *
     * @param id 项目ID
     */
    private void getProjectDetails(int id) {
        showProgressDialog();
        ProjectDetailsRequester requester = new ProjectDetailsRequester((baseResult, projectDetailsInfo) -> {
            Logger.logD(Logger.COMMON, TAG + "->getProjectDetails()->baseResult:" + baseResult.getCode() + ", projectDetailsInfo:" + projectDetailsInfo);
            dismissProgressDialog();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && projectDetailsInfo != null) {
                mCurrentProjectDetailsInfo = projectDetailsInfo;
                mAgreedTimes.clear();
                for (int i = 0; i < LibCollections.size(projectDetailsInfo.getAppointTimeList()); i++) {
                    ProjectAppointTimeInfo info = projectDetailsInfo.getAppointTimeList().get(i);
                    if (null == info) {
                        continue;
                    }
                    String tempTime = TimeUtils.string2String(info.getAppointTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), dateFormat);
                    mAgreedTimes.add(tempTime);
                    if (mCurrentSelectTime.equals(tempTime)) {
                        mCurrentProjectAppointTimeInfo = info;
                    }
                }
                calendarAdapter.setOptionalAgreedTime(mCurrentProjectDetailsInfo.getOptionalTimeList(), mAgreedTimes);
                displayCurrentTopView(projectDetailsInfo);
                displayCurrentBottomView(mCurrentSelectTime, mInitialTime, projectDetailsInfo.getAppointTimeList());
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.itemId = id;
        requester.doPost();
    }

    private void displayCurrentTopView(ProjectDetailsInfo projectDetailsInfo) {
        mProjectNameTv.setText(projectDetailsInfo.getFullDepartmentName());
        mAppointmentCourseNumberTv.setText(getString(R.string.user_class_hours, projectDetailsInfo.getClassAppNum() + ""));
        mNotAppointmentCourseNumberTv.setText(getString(R.string.user_class_num_no, projectDetailsInfo.getClassLastNum() + ""));
        mFinishCourseNumberTv.setText(getString(R.string.user_class__num_complete, projectDetailsInfo.getClassCompleteNum() + ""));
    }

    /**
     * @param mCurrentTime     当前选择时间
     * @param mInitialTime     当前时间
     * @param mAppointTimeList 已预约时间
     */
    private void displayCurrentBottomView(String mCurrentTime, long mInitialTime, List<ProjectAppointTimeInfo> mAppointTimeList) {
        int week = TimeUtils.getWeekNumByDate(mInitialTime);
        mAlreadyAppointmentView.setVisibility(View.GONE);
        mNotReservationView.setVisibility(View.GONE);
        mNoReservationBtn.setVisibility(View.GONE);
        mCannotReservationView.setVisibility(View.VISIBLE);
        for (int i = 0; i < LibCollections.size(mCurrentProjectDetailsInfo.getOptionalTimeList()); i++) {
            ProjectOptionalTimeInfo timeInfo = mCurrentProjectDetailsInfo.getOptionalTimeList().get(i);
            if (timeInfo.getWeek() == week) {
                long long1 = TimeUtils.string2Millis(mCurrentTime, dateFormat) + timeInfo.getHour() * 60 * 60 * 1000;
                if (long1 >= mInitialTime) {
                    mAlreadyAppointmentView.setVisibility(View.GONE);
                    mNotReservationView.setVisibility(View.VISIBLE);
                    mNoReservationBtn.setVisibility(View.VISIBLE);
                    mCannotReservationView.setVisibility(View.GONE);
                }
            }
        }
        if (mAgreedTimes.contains(mCurrentTime)) {
            mAlreadyAppointmentView.setVisibility(View.VISIBLE);
            mNotReservationView.setVisibility(View.GONE);
            mNoReservationBtn.setVisibility(View.GONE);
            mCannotReservationView.setVisibility(View.GONE);
            for (int i = 0; i < mAppointTimeList.size(); i++) {
                String appointTime = mAppointTimeList.get(i).getAppointTime();
                long tempTime = TimeUtils.string2Millis(appointTime);
                if (TimeUtils.millis2String(tempTime, dateFormat).equals(mCurrentTime)) {
                    setReservationView(mAppointTimeList.get(i));
                    break;
                }
            }
        }
    }

    private void showCancelAppointDialog(List<ProjectListInfo> projectListInfos, int currentItemId, int currentCourseId, String currentAppointmentTime) {
        new CommonDialog.Builder(this)
                .setMessage("确定取消预约")
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> cancelReservationCourse(projectListInfos, currentItemId, currentCourseId, currentAppointmentTime))
                .show();
    }

    /**
     * @param projectListInfos
     * @param currentItemId
     * @param currentCourseId
     * @param currentAppointmentTime
     */
    private void cancelReservationCourse(List<ProjectListInfo> projectListInfos, int currentItemId, int currentCourseId, String currentAppointmentTime) {
        CancelReservationCourseRequester requester = new CancelReservationCourseRequester((baseResult, aVoid) -> {
            Logger.logD(Logger.COMMON, TAG + "->cancelReservationCourse()->baseResult:" + baseResult.getCode());
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                getProjectDetails(projectListInfos.get(currentItemId).getItemId());
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.courseId = currentCourseId;
        requester.appointTime = currentAppointmentTime;
        requester.doPost();
    }

    private void showProjectDialog(List<ProjectListInfo> projectListInfos) {
        mSwitchIm.setEnabled(false);
        mSwitchIm.setClickable(false);
        mSwitchIm.setImageResource(R.drawable.ic_project_close);
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.dialog_account_screen, null);
        final RecyclerView mRecyclerView = contentView.findViewById(R.id.dialog_account_screen_recycler_view);
        mPopupWindow = new SingleFlowPopuwindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.showAsDropDown(mProjectView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getThisActivity()));
        mPopupWindow.setOnDismissListener(() -> {
            mSwitchIm.setImageResource(R.drawable.ic_project_open);
            mPopupWindow = null;
            new Handler().postDelayed(() -> {
                mSwitchIm.setEnabled(true);
                mSwitchIm.setClickable(true);
            }, 100);
        });

        mRecyclerView.setAdapter(projectAdapter);
        projectAdapter.setNewData(projectListInfos);
    }

    /**
     * 上一年/下一年
     */
    private void displayLastNextYearView(int increment) {
        long tempTime = TimeUtils.string2Millis(mCurrentSelectTime, dateFormat);
        calendar.setTimeInMillis(tempTime);
        int year = calendar.get(Calendar.YEAR) + increment;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Logger.logI(Logger.APPOINTMENT, "CourseTableActivity-->displayNextYear->year：" + year + ",month:" + month + ",day:" + day);
        upDateData(year, month, day);
    }

    private void displayLastNextMonthView(int increment) {
        long tempTime = TimeUtils.string2Millis(mCurrentSelectTime, dateFormat);
        calendar.setTimeInMillis(tempTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + increment + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month > 12) {
            year = year + 1;
            month = 1;
        } else if (month <= 0) {
            year = year - 1;
            month = 12;
        }
        upDateData(year, month, day);
    }

    private void upDateData(int year, int month, int day) {
        calendarAdapter.upData(year, month);
        calendarAdapter.notifyDataSetChanged();
        String str = year + "年" + SpecialCalendar.getBitStr(String.valueOf(month), 2) + "月" + SpecialCalendar.getBitStr(String.valueOf(day), 2) + "日";
        mCurrentSelectTime = str;
        mTitleDateTv.setText(str);
        mTitleWeekTv.setText(TimeUtils.string2String(str, dateFormat, dateFormatWeek));
        mSelectTimTv.setText(TimeUtils.string2String(str, dateFormat, dateFormatAndWeek));
        displayCurrentBottomView(mCurrentSelectTime, mInitialTime, mCurrentProjectDetailsInfo.getAppointTimeList());
    }

    /**
     * @param projectDetailsInfo 当前选择专科详情
     * @param initialTime        现在时间
     * @param currentSelectTime
     * @param userType
     */
    private void reservationCourse(ProjectDetailsInfo projectDetailsInfo, long initialTime, String currentSelectTime, int userType) {
        Logger.logD(Logger.COMMON, TAG + "-->reservationCourse-->userType:" + userType);
        long selectMillis = TimeUtils.string2Millis(currentSelectTime, dateFormat);
        if (userType != AppConstant.CourseUserType.COURSE_USER_TYPE_DIRECTOR &&
                userType != AppConstant.CourseUserType.COURSE_USER_TYPE_SUBSTRATUM_DOCTOR &&
                userType != AppConstant.CourseUserType.COURSE_USER_TYPE_FIRST_DOCTOR) {
            ToastUtils.showShort("您当前身份不可以预约课程");
            return;
        }
        //课程频率
        int courseFrequency = projectDetailsInfo.getCourseFrequency();
        //提前预约天数
        int untilAppoint = projectDetailsInfo.getUntilAppoint();

        int weekHour = 0;
        for (int i = 0; i < LibCollections.size(projectDetailsInfo.getOptionalTimeList()); i++) {
            ProjectOptionalTimeInfo projectOptionalTimeInfo = projectDetailsInfo.getOptionalTimeList().get(i);
            if (projectOptionalTimeInfo.getWeek() == TimeUtils.getWeekNumByDate(selectMillis)) {
                weekHour = projectOptionalTimeInfo.getHour();
                break;
            }
        }
        selectMillis += weekHour * 60 * 60 * 1000;
        long initialTempTime = initialTime + untilAppoint * 24 * 60 * 60 * 1000;
        if (selectMillis <= initialTempTime) {
            if (untilAppoint <= 0) {
                ToastUtils.showShort("今天已过预约时间");
            } else {
                ToastUtils.showShort("请提前" + untilAppoint + "天预约课程");
            }
            return;
        }
        int courseNum = 0;//本周已约课程数
        List<String> currentWeekList = SpecialCalendar.getWeekDayList(currentSelectTime, FORMAT_TYPE);
        for (int i = 0; i < mCurrentProjectDetailsInfo.getAppointTimeList().size(); i++) {
            String appointTime = mCurrentProjectDetailsInfo.getAppointTimeList().get(i).getAppointTime();
            String str = SpecialCalendar.LongToString(SpecialCalendar.StringToLong(appointTime, "yyyy-MM-dd HH:mm:ss"), FORMAT_TYPE);
            if (currentWeekList.contains(str) && StringUtils.isEmpty(mCurrentProjectDetailsInfo.getAppointTimeList().get(i).getUpdDt())) {
                courseNum++;
            }
        }
        Logger.logI(Logger.COMMON, TAG + "-->reservationCourse-->courseNum：" + courseNum + ",courseFrequency:" + courseFrequency + ",currentWeekList:" + currentWeekList);
        if (courseNum >= courseFrequency) {
            ToastUtils.showShort("本周最多可约" + courseFrequency + "节课,已约" + courseNum + "节课");
            return;
        }
        int week = TimeUtils.getWeekNumByDate(selectMillis);
        int hour = 0;
        for (int i = 0; i < projectDetailsInfo.getOptionalTimeList().size(); i++) {
            if (projectDetailsInfo.getOptionalTimeList().get(i).getWeek() == week) {
                hour = projectDetailsInfo.getOptionalTimeList().get(i).getHour();
            }
        }
        String str = TimeUtils.millis2String(selectMillis, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) + " " + getStr(String.valueOf(hour), 2) + ":00:00";
        Intent intent = new Intent(CourseTableActivity.this, CourseListActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_STAGE_ID, projectDetailsInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_APPOINTMENT_TIME, str);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_NAME, projectDetailsInfo.getFullDepartmentName());
        startActivityForResult(intent, REQUEST_CODE_APPOINTMENT);
    }

    private void setBottomView(ProjectListInfo projectListInfo) {
        Logger.logD(Logger.COMMON, TAG + "-->setBottomView-->mUserType:" + projectListInfo.getUserType());
        if (mBottomBtn.getText().toString().equals(getString(R.string.cancel_appointment))) {//取消预约
            if (projectListInfo.getUserType() != AppConstant.CourseUserType.COURSE_USER_TYPE_DIRECTOR &&
                    projectListInfo.getUserType() != AppConstant.CourseUserType.COURSE_USER_TYPE_SUBSTRATUM_DOCTOR &&
                    projectListInfo.getUserType() != AppConstant.CourseUserType.COURSE_USER_TYPE_FIRST_DOCTOR) {
                ToastUtils.showShort("您当前身份不可以取消已预约课程");
                return;
            }
            if (mCurrentProjectAppointTimeInfo.getUserId() != mUserInfoManager.getCurrentUserInfo().getUserId()) {
                ToastUtils.showShort("您不可以取消别人预约的课程");
                return;
            }
            showCancelAppointDialog(mProjectListInfos, mCurrentProjectListInfoItemId, mCurrentProjectAppointTimeInfo.getCourseId(), mCurrentProjectAppointTimeInfo.getAppointTime());
        } else if (mBottomBtn.getText().toString().equals(getString(R.string.no_reservation_system_selected))) {
            ToastUtils.showShort(getString(R.string.no_reservation_system_selected));
        } else {
            boolean isHaveAuthority = false;
            if (projectListInfo.getUserType() == AppConstant.CourseUserType.COURSE_USER_TYPE_DIRECTOR ||
                    projectListInfo.getUserType() == AppConstant.CourseUserType.COURSE_USER_TYPE_SUBSTRATUM_DOCTOR ||
                    projectListInfo.getUserType() == AppConstant.CourseUserType.COURSE_USER_TYPE_FIRST_DOCTOR) {
                isHaveAuthority = true;
            }
            Intent intent = new Intent();
            if (mCurrentProjectAppointTimeInfo.getAppointmentId() >= 500000) {
                intent.setClass(getThisActivity(), RoundsDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mCurrentProjectAppointTimeInfo.getAppointmentId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_IS_COURSE, isHaveAuthority);
                //intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, orderListItemInfo.getAtthosId());
                startActivity(intent);
            } else {
                intent.setClass(getThisActivity(), PatientInformationActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mCurrentProjectAppointTimeInfo.getAppointmentId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_IS_COURSE, isHaveAuthority);
                startActivity(intent);
            }
        }
    }

    public static String getStr(String file, int bit) {
        while (file.length() < bit) {
            file = "0" + file;
        }
        return file;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
