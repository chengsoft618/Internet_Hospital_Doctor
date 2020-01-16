package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/3 15:14
 * @description: 患者管理
 */
public class WaitRoundsPatientInfoAdapter extends BaseQuickAdapter<WaitRoundsPatientInfo, BaseViewHolder> {
    //已选择患者ID
    private List<WaitRoundsPatientInfo> waitRoundsPatientInfoSparseArray;

    public WaitRoundsPatientInfoAdapter(int layoutResId, @Nullable List<WaitRoundsPatientInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaitRoundsPatientInfo item) {
        if (LibCollections.isEmpty(waitRoundsPatientInfoSparseArray)) {
            helper.setChecked(R.id.item_patient_wait_rounds_choose_cb, false);
        } else {
            helper.setChecked(R.id.item_patient_wait_rounds_choose_cb, waitRoundsPatientInfoSparseArray.contains(item));
        }
        helper.addOnClickListener(R.id.item_patient_wait_rounds_choose_cb);
        helper.setText(R.id.item_patient_wait_rounds_name_tv, item.getPatientName());
        helper.setText(R.id.item_patient_wait_rounds_gender_tv, item.getGender() == 1 ? "男" : "女");
        helper.setText(R.id.item_patient_wait_rounds_age_tv, item.getAge());
        helper.setText(R.id.item_patient_wait_rounds_in_hospital_num_desc_tv, StringUtils.isTrimEmpty(item.getHospitalizaId()) ? "--" : item.getHospitalizaId());
        helper.setText(R.id.item_patient_wait_rounds_patient_num_desc_tv, item.getMedicalId() + "");
        helper.setText(R.id.item_patient_wait_rounds_confirm_sick_desc_tv, item.getAttendingDisease());
    }

    /**
     * @param position
     */
    public void toggleSelected(int position) {
        if (null == waitRoundsPatientInfoSparseArray) {
            waitRoundsPatientInfoSparseArray = new ArrayList<>();
        }
        WaitRoundsPatientInfo info = getItem(position);
        if (null != info) {
            if (waitRoundsPatientInfoSparseArray.contains(info)) {
                waitRoundsPatientInfoSparseArray.remove(info);
            } else {
                waitRoundsPatientInfoSparseArray.add(info);
            }
        }
        notifyDataSetChanged();
    }

    public List<WaitRoundsPatientInfo> getSelectedDatas() {
        return waitRoundsPatientInfoSparseArray;
    }

    public void setSelectedMedicals(List<WaitRoundsPatientInfo> medicals) {
        waitRoundsPatientInfoSparseArray = medicals;
        notifyDataSetChanged();
    }
}
