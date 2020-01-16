package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 21:26
 * @description: 项目患者列表适配器
 */
public class DCDutyPatientAdapter extends BaseQuickAdapter<DCDutyPatientItemInfo, BaseViewHolder> {
    private boolean isMyPatient;

    public DCDutyPatientAdapter(int layoutResId, @Nullable List<DCDutyPatientItemInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyPatientItemInfo item) {
        helper.setText(R.id.item_dc_duty_pateint_list_patient_name_tv, item.getPatientName());
        helper.setText(R.id.item_dc_duty_pateint_list_patient_gender_tv, item.getGender() == 1 ? "男" : "女");

        if (isMyPatient) {
            helper.setGone(R.id.item_dc_duty_pateint_list_patient_time_tv, !item.isReaded());
            helper.setText(R.id.item_dc_duty_pateint_list_patient_time_tv, item.getReadType());
            helper.setGone(R.id.item_dc_duty_pateint_list_patient_time_v, !item.isReaded());
        } else {
            helper.setGone(R.id.item_dc_duty_pateint_list_patient_time_v, false);
            helper.setText(R.id.item_dc_duty_pateint_list_patient_time_tv, TimeUtils.string2String(item.getInsertDt(), "yyyy-MM-dd"));
        }

        helper.setText(R.id.item_dc_duty_pateint_list_patient_age_tv,item.getAge());
        helper.setText(R.id.item_dc_duty_pateint_list_patient_hospital_name_tv, item.getHospitalName());
        GlideUtils.showPatientChatAvatar(helper.getView(R.id.item_dc_duty_pateint_list_civ), mContext, item.getAvatar());
    }

    public boolean isMyPatient() {
        return isMyPatient;
    }

    public void setMyPatient(boolean myPatient) {
        isMyPatient = myPatient;
    }
}
