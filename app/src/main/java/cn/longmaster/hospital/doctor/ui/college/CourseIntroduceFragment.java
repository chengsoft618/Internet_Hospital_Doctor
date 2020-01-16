package cn.longmaster.hospital.doctor.ui.college;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.college.CourseLikeRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 课程介绍Fragment
 * Created by Yang² on 2018/3/27.
 */

public class CourseIntroduceFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_course_introduce_title)
    private TextView mTitleView;
    @FindViewById(R.id.fragment_course_introduce_play_num)
    private TextView mPlayNumView;
    @FindViewById(R.id.fragment_course_introduce_likes)
    private TextView mLikesView;
    @FindViewById(R.id.fragment_course_introduce_praise_btn)
    private TextView mPraiseBtn;
    @FindViewById(R.id.fragment_course_introduce_introduce)
    private TextView mIntroduceTextView;
    @FindViewById(R.id.fragment_course_introduce_layout)
    private LinearLayout mIntroduceLayout;
    @FindViewById(R.id.fragment_course_introduce_doctor_avatar)
    private CircleImageView mDoctorAvatarView;
    @FindViewById(R.id.fragment_course_introduce_doctor_name)
    private TextView mDoctorNameView;
    @FindViewById(R.id.fragment_course_introduce_doctor_title)
    private TextView mDoctorTitleView;
    @FindViewById(R.id.fragment_course_introduce_hospital)
    private TextView mHospitalView;
    @FindViewById(R.id.fragment_course_introduce_department)
    private TextView mDepartmentView;
    @FindViewById(R.id.fragment_course_introduce_doctor_level)
    private TextView mDoctorLevelView;
    @FindViewById(R.id.fragment_course_introduce_doctor_introduce)
    private TextView mDoctorIntroduceView;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    private CourseListInfo mCourseInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_course_introduce;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        displayCourseInfo();
        //1003771 1002422 1000457
//        getDoctorInfo(1000457);
        if (mCourseInfo.getDoctorId() != 0) {
            getDoctorInfo(mCourseInfo.getDoctorId());
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCourseInfo = (CourseListInfo) bundle.getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_INFO);
        }
    }

    @OnClick({R.id.fragment_course_introduce_praise_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_course_introduce_praise_btn:
                courseLike();
                break;
        }
    }

    private void displayCourseInfo() {
        mTitleView.setText(mCourseInfo.getCourseTitle());
        mPlayNumView.setText(String.valueOf(mCourseInfo.getPlayTotal()));
        mLikesView.setText(String.valueOf(mCourseInfo.getLikesTotal()));
        mPraiseBtn.setText(getString(R.string.medical_college_praise_num, mCourseInfo.getLikesTotal() > 999 ? 999 : mCourseInfo.getLikesTotal()));
        if (!TextUtils.isEmpty(mCourseInfo.getCourseIntroduce())) {
            mIntroduceTextView.setText(mCourseInfo.getCourseIntroduce());
        } else {
            mIntroduceLayout.setVisibility(View.GONE);
        }
    }

    private void getDoctorInfo(int doctorId) {
        mDoctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                displayDoctor(doctorBaseInfo);
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void displayDoctor(DoctorBaseInfo doctorBaseInfo) {
        mHospitalView.setText(doctorBaseInfo.getHospitalName());
        mDepartmentView.setText(doctorBaseInfo.getDepartmentName());
        mDoctorNameView.setText(doctorBaseInfo.getRealName());
        mDoctorTitleView.setText(doctorBaseInfo.getDoctorTitle());
        mDoctorLevelView.setText(doctorBaseInfo.getDoctorLevel());
        if (doctorBaseInfo.getDoctorBriefInfo() != null && !TextUtils.isEmpty(doctorBaseInfo.getDoctorBriefInfo().getIntroduce())) {
            mDoctorIntroduceView.setText(doctorBaseInfo.getDoctorBriefInfo().getIntroduce());
        }
        GlideUtils.showDoctorAvatar(mDoctorAvatarView, getBaseActivity(), AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
    }


    private void courseLike() {
        CourseLikeRequester requester = new CourseLikeRequester((baseResult, likeInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mLikesView.setText(String.valueOf(likeInfo.getLikesTotal()));
                mPraiseBtn.setText(getString(R.string.medical_college_praise_num, likeInfo.getLikesTotal() > 999 ? 999 : likeInfo.getLikesTotal()));
            }
        });
        requester.courseId = mCourseInfo.getCourseId();
        requester.doPost();
    }

}
