package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 值班门诊-发起门诊-选择医生
 * Created by yangyong on 2019-11-26.
 */
public class DCInputPatientInfoActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_dcinput_patient_info_doctor_avatar_civ)
    private CircleImageView doctorAvatarCiv;
    @FindViewById(R.id.activity_dcinput_patient_info_doctor_name_tv)
    private TextView doctorNameTv;
    @FindViewById(R.id.activity_dcinput_patient_info_doctor_level_tv)
    private TextView doctorLevelTv;
    @FindViewById(R.id.activity_dcinput_patient_info_doctor_hospital_tv)
    private TextView doctorHospitalTv;

    @FindViewById(R.id.activity_dcinput_patient_info_patient_name_et)
    private EditText patientNameEt;
    @FindViewById(R.id.activity_dcinput_patient_info_patient_phonenum_et)
    private EditText patientPhonenumEt;
    @FindViewById(R.id.activity_dcinput_patient_info_patient_cardno_et)
    private EditText patientCardnoEt;

    private DCDoctorInfo dcDoctorInfo;
    private int itemId;
    @AppApplication.Manager
    private DCManager dcManager;

    @Override
    protected void initDatas() {
        dcDoctorInfo = (DCDoctorInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_INFO);
        itemId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ITEM_ID, 0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dcinput_patient_info;
    }

    @Override
    protected void initViews() {
        GlideUtils.showDoctorAvatar(doctorAvatarCiv, this, AvatarUtils.getAvatar(false, dcDoctorInfo.getUserId(), "0"));
        doctorNameTv.setText(dcDoctorInfo.getRealName());
        doctorLevelTv.setText(dcDoctorInfo.getDoctorLevel());
        doctorHospitalTv.setText(dcDoctorInfo.getHospitalName());
    }

    @OnClick({R.id.activity_dcinput_patient_info_call_btn})
    public void onClick(View view) {
        String patientName = getString(patientNameEt);
        String patientPhonenum = getString(patientPhonenumEt);
        String patientCardNo = getString(patientCardnoEt);
        if (!StringUtils.isEmpty(patientPhonenum) && StringUtils.isEmpty(patientName)) {
            ToastUtils.showShort("请填写患者姓名");
            return;
        }
        if (!StringUtils.isEmpty(patientCardNo) && StringUtils.isEmpty(patientName)) {
            ToastUtils.showShort("请填写患者姓名");
            return;
        }
        if (!StringUtils.isEmpty(patientPhonenum) && patientPhonenum.length() != 11) {
            ToastUtils.showShort("请填写正确的手机号码");
            return;
        }
        if (!StringUtils.isEmpty(patientCardNo) && patientCardNo.length() != 18) {
            ToastUtils.showShort("请填写正确的身份证号码");
            return;
        }

        ProgressDialog dialog = createProgressDialog(getString(R.string.data_committing));
        dialog.show();
        dcManager.createDCRoomOrder(itemId, dcDoctorInfo.getUserId(), patientName, patientPhonenum, patientCardNo, new OnResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer orderId, BaseResult baseResult) {
                getDisplay().startDCRoomActivity(dcDoctorInfo, orderId, AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR, 0);
                finish();
            }

            @Override
            public void onFail(BaseResult baseResult) {
                if (baseResult.getCode() == 120) {
                    ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                } else if (baseResult.getCode() == 102) {
                    ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }
}
