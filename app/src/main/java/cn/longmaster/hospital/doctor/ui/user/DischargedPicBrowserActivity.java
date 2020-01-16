package cn.longmaster.hospital.doctor.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.DischargedSummaryInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.user.DischargedSummaryDeleteRequest;
import cn.longmaster.hospital.doctor.ui.HackyViewPager;
import cn.longmaster.hospital.doctor.ui.PicBrowseNewAdapter;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/23 11:03
 * @description: 出院小结图片预览
 */
public class DischargedPicBrowserActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_pic_browse_viewpager)
    private HackyViewPager activityPicBrowseViewpager;
    @FindViewById(R.id.activity_pic_browse_title_bar)
    private AppActionBar activityPicBrowseTitleBar;
    private static final String KEY_TO_QUERY_DISCHARGED_SUMMARY = "_KEY_TO_QUERY_DISCHARGED_SUMMARY_";
    private static final String KEY_TO_QUERY_DISCHARGED_SUMMARY_MEDICAL_ID = "_KEY_TO_QUERY_DISCHARGED_SUMMARY_MEDICAL_ID_";
    private static final String KEY_TO_QUERY_DISCHARGED_SUMMARY_POSITION = "_KEY_TO_QUERY_DISCHARGED_SUMMARY_POSITION_";
    private List<DischargedSummaryInfo> dischargedSummaryInfos;
    private PicBrowseNewAdapter mPicBrowseAdapter;
    private int mIndex;
    private List<String> mServerUrls;
    private String mMedicalId;

    public static void start(Activity context, ArrayList<DischargedSummaryInfo> dischargedSummaryInfos, String medicalId, int position, int resultCode) {
        Intent intent = new Intent(context, DischargedPicBrowserActivity.class);
        intent.putExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY_POSITION, position);
        intent.putExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY_MEDICAL_ID, medicalId);
        intent.putExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY, dischargedSummaryInfos);
        context.startActivityForResult(intent, resultCode);
    }

    @Override
    protected void initDatas() {
        dischargedSummaryInfos = getIntent().getParcelableArrayListExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY);
        mIndex = getIntent().getIntExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY_POSITION, 0);
        mMedicalId = getIntent().getStringExtra(KEY_TO_QUERY_DISCHARGED_SUMMARY_MEDICAL_ID);
        mServerUrls = new ArrayList<>(LibCollections.size(dischargedSummaryInfos));
        for (int i = 0; i < LibCollections.size(dischargedSummaryInfos); i++) {
            mServerUrls.add(AppConfig.getDfsUrl() + "3037/0/" + dischargedSummaryInfos.get(i).getFileName());
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dischaged_pic_browser;
    }

    @Override
    protected void initViews() {
        activityPicBrowseTitleBar.setLeftOnClickListener(v -> {
            onBackPressed();
        });
        activityPicBrowseTitleBar.setRightTextOnClickListener(v -> {
            if (LibCollections.size(dischargedSummaryInfos) > mIndex) {
                new CommonDialog.Builder(getThisActivity())
                        .setMessage(R.string.is_delete_diagnosis_pic)
                        .setPositiveButton(R.string.cancel, () -> {
                        })
                        .setNegativeButton(R.string.confirm, () -> deleteDischargedPic(mMedicalId, mIndex))
                        .show();
            }
        });
        mPicBrowseAdapter = new PicBrowseNewAdapter(this, mServerUrls);
        mPicBrowseAdapter.setOnPhotoTapListener((view, x, y) -> toggleFullScreen());
        activityPicBrowseViewpager.setAdapter(mPicBrowseAdapter);
        activityPicBrowseViewpager.setCurrentItem(mIndex);
        activityPicBrowseViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Logger.logD(TAG, "#onPageScrolled:" + i);
                mIndex = i;
            }

            @Override
            public void onPageSelected(int position) {
                Logger.logD(TAG, "#onPageSelected:" + position);
                mIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Logger.logD(TAG, "#onPageScrollStateChanged:" + i);
            }
        });
    }

    private void toggleFullScreen() {
        if (activityPicBrowseTitleBar.getVisibility() == View.GONE) {
            activityPicBrowseTitleBar.setVisibility(View.VISIBLE);
        } else {
            activityPicBrowseTitleBar.setVisibility(View.GONE);
        }
    }

    /**
     * 删除图片
     *
     * @param medicalId
     * @param index
     */
    private void deleteDischargedPic(String medicalId, int index) {
        String fileName = dischargedSummaryInfos.get(index).getFileName();
        DischargedSummaryDeleteRequest request = new DischargedSummaryDeleteRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                ToastUtils.showShort(R.string.data_delete_success);
                mPicBrowseAdapter.remove(index);
                dischargedSummaryInfos.remove(index);
                if (LibCollections.size(dischargedSummaryInfos) <= 0) {
                    getThisActivity().setResult(Activity.RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.data_delete_fail);
            }
        });
        request.setMedicalId(medicalId);
        request.setFileName(fileName);
        request.start();
    }
}
