package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyPicInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.DeleteConsultPicRequester;
import cn.longmaster.hospital.doctor.ui.HackyViewPager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.FirstJourneyPicAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * 首程图片预览Activity
 * mod by biao on2019/7/17
 */
public class FirstJourneyPicBrowseActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_first_journey_browse_viewpager)
    private HackyViewPager mHackyViewPager;
    @FindViewById(R.id.activity_first_journey_browse_title_bar)
    private AppActionBar mAppActionBar;
    private FirstJourneyPicAdapter mPicBrowseAdapter;
    private ArrayList<String> mServerUrls = new ArrayList<>();
    private List<FirstJourneyPicInfo> mDoctorOrderPicList = new ArrayList<>();
    private int mIndex;
    private int mAppointmentId = 0;
    private boolean mIsDoctorOrder;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mServerUrls = (ArrayList<String>) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVER_URL);
        mIndex = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, 0);
        mDoctorOrderPicList = (ArrayList<FirstJourneyPicInfo>) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_NAME);
        mIsDoctorOrder = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ORDER, false);
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTINFO_ID, 0);
    }

    @Override
    protected void initDatas() {
        mPicBrowseAdapter = new FirstJourneyPicAdapter(this, mServerUrls);
        mPicBrowseAdapter.setOnPhotoTapListener((view, x, y) -> toggleFullScreen());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_first_journey_pic_browse;
    }

    @Override
    protected void initViews() {
        mAppActionBar.setTitle(mIndex + 1 + "/" + LibCollections.size(mServerUrls));
        if (mIsDoctorOrder) {
            mAppActionBar.addFunction(AppActionBar.FUNCTION_RIGHT_TEXT);
            mAppActionBar.setRightText(getString(R.string.rounds_medical_record_module_delete));
        }
        mAppActionBar.setLeftOnClickListener(v -> onBackPressed());
        mHackyViewPager.setAdapter(mPicBrowseAdapter);
        mHackyViewPager.setCurrentItem(mIndex);
        mHackyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                mAppActionBar.setTitle(mIndex + 1 + "/" + LibCollections.size(mServerUrls));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAppActionBar.setRightTextOnClickListener(v -> {
            deleteConsultPic();
        });
    }

    /**
     * 开关全屏
     */
    private void toggleFullScreen() {
        if (mAppActionBar.getVisibility() == View.GONE) {
            mAppActionBar.setVisibility(View.VISIBLE);
        } else {
            mAppActionBar.setVisibility(View.GONE);
        }
    }

    /**
     * 删除医嘱图片
     */
    private void deleteConsultPic() {
        if (mDoctorOrderPicList.get(mIndex).getUpLoadState() != AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS) {
            ToastUtils.showShort("图片正在上传不能删除");
            return;
        }
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        DeleteConsultPicRequester requester = new DeleteConsultPicRequester((baseResult, aVoid) -> {
            Logger.logD(Logger.APPOINTMENT, "deleteConsultPic->baseResult:" + baseResult.getCode());
            progressDialog.cancel();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                ToastUtils.showShort("删除成功！");
                Intent intent = new Intent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, mIndex);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_NAME, mDoctorOrderPicList.get(mIndex).getPicName());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showShort(R.string.consult_del_voice_file_field);
            }
        });
        requester.appointmentId = mAppointmentId;
        requester.diagnosisPicture = mDoctorOrderPicList.get(mIndex).getPicName();
        requester.doPost();
    }
}
