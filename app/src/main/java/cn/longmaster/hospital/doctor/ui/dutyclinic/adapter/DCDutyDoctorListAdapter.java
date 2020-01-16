package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 值班门诊-选择医生列表适配器
 * Created by yangyong on 2019-11-27.
 */
public class DCDutyDoctorListAdapter extends BaseSectionQuickAdapter<DCDoctorSectionInfo, BaseViewHolder> {
    public DCDutyDoctorListAdapter(int layoutResId, int sectionHeadResId, List<DCDoctorSectionInfo> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, DCDoctorSectionInfo item) {
        helper.setText(R.id.layout_dcdoctor_list_header_title_tv, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, DCDoctorSectionInfo item) {
        helper.setText(R.id.item_dcdcotor_list_doctor_name_tv, item.getRealName());
        helper.setText(R.id.item_dcdcotor_list_doctor_title_tv, item.getDoctorLevel());
        helper.setText(R.id.item_dcdcotor_list_doctor_hospital_department_tv, item.getHospitalName() + "    " + item.getDepartmentName());

        GlideUtils.showDoctorAvatar(helper.getView(R.id.item_dcdcotor_list_civ), AppApplication.getInstance().getApplicationContext(), AvatarUtils.getAvatar(false, item.getUserId(), "0"));
    }

}
