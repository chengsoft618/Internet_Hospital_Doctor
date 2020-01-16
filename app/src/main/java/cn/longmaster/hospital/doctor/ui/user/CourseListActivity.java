package cn.longmaster.hospital.doctor.ui.user;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectCourseTypeInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.user.GetProjectCourseListRequester;
import cn.longmaster.hospital.doctor.core.requests.user.ReservationCourseRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.CourseListAdapter;
import cn.longmaster.utils.ToastUtils;

public class CourseListActivity extends NewBaseActivity {
    private final String TAG = CourseListActivity.class.getSimpleName();
    @FindViewById(R.id.activity_course_list_project_name_tv)
    private TextView mProjectNameTv;
    @FindViewById(R.id.activity_course_list_expand_elv)
    private ExpandableListView mExpandableListView;
    @FindViewById(R.id.activity_course_list_appointment_course_number_tv)
    private TextView mAppointmentCourseNumberTv;
    @FindViewById(R.id.activity_course_list_not_appointment_course_number_tv)
    private TextView mNotAppointmentCourseNumberTv;
    @FindViewById(R.id.activity_course_list_finish_course_number_tv)
    private TextView mFinishCourseNumberTv;
    @FindViewById(R.id.activity_course_list_empty_view)
    private RelativeLayout mEmptyView;

    private CourseListAdapter mCourseListAdapter;
    private List<ProjectCourseTypeInfo> mCourseTypeInfos = new ArrayList<>();
    private ProjectDetailsInfo mProjectDetailsInfo;
    private String mProjectName;
    private String mCurrentTime;

    @Override
    protected void initDatas() {
        mProjectDetailsInfo = (ProjectDetailsInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_STAGE_ID);
        mProjectName = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_NAME);
        mCurrentTime = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_APPOINTMENT_TIME);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_course_list;
    }

    @Override
    protected void initViews() {
        mProjectNameTv.setText(mProjectName);
        if (null == mProjectDetailsInfo) {
            ToastUtils.showShort("");
            return;
        }
        mAppointmentCourseNumberTv.setText(getString(R.string.user_class_hours, mProjectDetailsInfo.getClassAppNum() + ""));
        mNotAppointmentCourseNumberTv.setText(getString(R.string.user_class_num_no, mProjectDetailsInfo.getClassLastNum() + ""));
        mFinishCourseNumberTv.setText(getString(R.string.user_class__num_complete, mProjectDetailsInfo.getClassCompleteNum() + ""));
        mExpandableListView.setDivider(null);
        mExpandableListView.setGroupIndicator(null);
        mCourseListAdapter = new CourseListAdapter(this);
        mExpandableListView.setAdapter(mCourseListAdapter);
        mCourseListAdapter.setOnItemClickListener((projectCourseTypeInfo, groupPosition, childPosition) -> reservationCourse(projectCourseTypeInfo, childPosition));
        getCourseList(mProjectDetailsInfo.getStageId());
    }

    private void reservationCourse(ProjectCourseTypeInfo projectCourseTypeInfo, int childPosition) {
        ReservationCourseRequester requester = new ReservationCourseRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, TAG + "->reservationCourse()->baseResult:" + baseResult.getCode());
                ToastUtils.showShort("预约成功！");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.courseId = projectCourseTypeInfo.getCourseContentInfos().get(childPosition).getCourseId();
        requester.appointTime = mCurrentTime;
        requester.doPost();
    }

    private void getCourseList(int stageId) {
        GetProjectCourseListRequester requester = new GetProjectCourseListRequester((baseResult, projectCourseTypeInfos) -> {
            Logger.logD(Logger.COMMON, TAG + "->getCourseList()->baseResult:" + baseResult.getCode() + ", projectCourseTypeInfos:" + projectCourseTypeInfos);
            if (baseResult.getCode() == 0 && projectCourseTypeInfos != null) {
                mCourseTypeInfos = projectCourseTypeInfos;
                if (mCourseTypeInfos.size() > 0) {
                    mExpandableListView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    mCourseListAdapter.setData(mCourseTypeInfos);
                } else {
                    mExpandableListView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.stageId = stageId;
        requester.doPost();
    }
}
