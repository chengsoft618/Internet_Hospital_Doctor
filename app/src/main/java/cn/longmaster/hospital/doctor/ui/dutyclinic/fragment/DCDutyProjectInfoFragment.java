package cn.longmaster.hospital.doctor.ui.dutyclinic.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.dialog.DCDutyProjectQRCodeDialog;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 18:43
 * @description:
 */
public class DCDutyProjectInfoFragment extends NewBaseFragment {
    private static String KEY_TO_QUERY_PROJECT_ID = "_KEY_TO_QUERY_PROJECT_ID_";
    @FindViewById(R.id.fg_dcduty_detail_name_tv)
    private TextView fgDcdutyDetailNameTv;
    @FindViewById(R.id.fg_dcduty_detail_desc_tv)
    private TextView fgDcdutyDetailDescTv;
    @FindViewById(R.id.fg_dcduty_detail_qrcode_iv)
    private ImageView fgDcdutyDetailQrcodeIv;
    @FindViewById(R.id.fg_dcduty_detail_doctor_list_cv)
    private CardView fgDcdutyDetailDoctorListCv;
    @FindViewById(R.id.fg_dcduty_detail_member_doctor_tv)
    private TextView fgDcdutyDetailMemberDoctorTv;
    @FindViewById(R.id.fg_dcduty_detail_member_doctor_num_desc_tv)
    private TextView fgDcdutyDetailMemberDoctorNumDescTv;
    @FindViewById(R.id.fg_dcduty_detail_member_doctor_desc_tv)
    private TextView fgDcdutyDetailMemberDoctorDescTv;
    @FindViewById(R.id.fg_dcduty_detail_patient_list_cv)
    private CardView fgDcdutyDetailPatientListCv;
    @FindViewById(R.id.fg_dcduty_detail_member_patient_tv)
    private TextView fgDcdutyDetailMemberPatientTv;
    @FindViewById(R.id.fg_dcduty_detail_member_patient_num_desc_tv)
    private TextView fgDcdutyDetailMemberPatientNumDescTv;
    @FindViewById(R.id.fg_dcduty_detail_member_patient_num_tv)
    private TextView fgDcdutyDetailMemberPatientNumTv;

    private DCProjectInfo dcProjectInfo;
    @AppApplication.Manager
    private DCManager dcManager;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_dc_duty_project_info;
    }

    @Override
    public void initViews(View rootView) {
        fgDcdutyDetailQrcodeIv.setOnClickListener(v -> {
            if (null != dcProjectInfo) {
                showProjectQRCode(dcProjectInfo);
            }
        });
        fgDcdutyDetailDoctorListCv.setOnClickListener(v -> {
            if (null != dcProjectInfo) {
                getDisplay().startDCDutyDoctorListActivity(dcProjectInfo.getItemId(), 0);
            }

        });
        fgDcdutyDetailPatientListCv.setOnClickListener(v -> {
            if (null != dcProjectInfo) {
                getDisplay().startDCDutyPatientListActivity(dcProjectInfo.getItemId(), 0);
            }

        });
        //getProjectDetails(dcProjectInfo);
    }

    private void showProjectQRCode(DCProjectInfo dcProjectInfo) {
        DCDutyProjectQRCodeDialog dialog = DCDutyProjectQRCodeDialog.getInstance(dcProjectInfo.getItemName(), dcProjectInfo.getQrcodeUrl());
        dialog.setOnDialogClickListener(view -> {
            String path = BitmapUtils.savePictureToLocal(BitmapUtils.getCacheBitmapFromView(view));
            ToastUtils.showShort("图片已保存至" + path);
        });
        dialog.show(getFragmentManager(), "DCDutyProjectQRCodeDialog");
    }

    private void showProjectDetails(@NonNull DCProjectInfo info) {
        GlideUtils.showImage(fgDcdutyDetailQrcodeIv, getBaseActivity(), info.getQrcodeUrl());
        fgDcdutyDetailDescTv.setText(info.getItemDesc());
        fgDcdutyDetailNameTv.setText(info.getItemName());
        fgDcdutyDetailMemberDoctorNumDescTv.setText(info.getDoctorCount() + "");
        fgDcdutyDetailMemberPatientNumDescTv.setText(info.getPatientCount() + "");
    }

    public void refresh(DCProjectInfo projectInfo) {
        dcProjectInfo = projectInfo;
        showProjectDetails(projectInfo);
    }

    public class PicDownLoadRunnable implements Runnable {
        private String mUrl;

        public PicDownLoadRunnable(String url) {
            mUrl = url;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    String path = BitmapUtils.savePictureToLocal(bitmap);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
