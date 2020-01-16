package cn.longmaster.hospital.doctor.ui.doctor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.google.BarcodeFormat;
import cn.longmaster.hospital.doctor.view.google.EncodeHintType;
import cn.longmaster.hospital.doctor.view.google.WriterException;
import cn.longmaster.hospital.doctor.view.google.common.BitMatrix;
import cn.longmaster.hospital.doctor.view.google.qrcode.QRCodeWriter;

/**
 * 医生二维码
 * Created by Yang² on 2017/9/4.
 */

public class DoctorQRCodeActivity extends BaseActivity {
    @FindViewById(R.id.activity_doctor_qr_code_avatar_iv)
    private ImageView mAvatarAiv;
    @FindViewById(R.id.activity_doctor_qr_code_doctor_name_tv)
    private TextView mDoctorNameTv;
    @FindViewById(R.id.activity_doctor_qr_code_doctor_level_tv)
    private TextView mDoctorLevelTv;
    @FindViewById(R.id.activity_doctor_qr_code_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.activity_doctor_qr_code_img_iv)
    private ImageView mImgIv;

    private DoctorBaseInfo mDoctorBaseInfo;
    //private HospitalInfo mHospitalInfo;
    //private DepartmentInfo mDepartmentInfo;

    public static void startDoctorQRCodeActivity(Activity activity, DoctorBaseInfo doctorBaseInfo) {
        Intent intent = new Intent(activity, DoctorQRCodeActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_BASE_INFO, doctorBaseInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_qr_code);
        ViewInjecter.inject(this);
        initData();
        displayView();
    }

    private void initData() {
        mDoctorBaseInfo = (DoctorBaseInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_BASE_INFO);
    }

    private void displayView() {
        mDoctorNameTv.setText(TextUtils.isEmpty(mDoctorBaseInfo.getRealName()) ? "" : mDoctorBaseInfo.getRealName());
        mDoctorLevelTv.setText(TextUtils.isEmpty(mDoctorBaseInfo.getDoctorLevel()) ? "" : mDoctorBaseInfo.getDoctorLevel());
        mHospitalTv.setText((TextUtils.isEmpty(mDoctorBaseInfo.getHospitalName()) ? "" : mDoctorBaseInfo.getHospitalName())
                + "\t" + (TextUtils.isEmpty(mDoctorBaseInfo.getDepartmentName()) ? "" : mDoctorBaseInfo.getDepartmentName()));
        GlideUtils.showDoctorAvatar(mAvatarAiv, getActivity(), AvatarUtils.getAvatar(false, mDoctorBaseInfo.getUserId(), mDoctorBaseInfo.getAvaterToken()));

        Bitmap qrBitmap = generateBitmap(mDoctorBaseInfo.getWebDoctorUrl(), 400, 400);
        mImgIv.setImageBitmap(qrBitmap);
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
