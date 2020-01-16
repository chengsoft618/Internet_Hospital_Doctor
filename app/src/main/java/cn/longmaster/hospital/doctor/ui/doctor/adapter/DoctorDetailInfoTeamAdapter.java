package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorTeamItemInfo;
import cn.longmaster.utils.StringUtils;


/**
 * @author Mloong_Abiao
 * @date 2019/6/6 13:41
 * @description: 专家详情 团队Adapter
 */
public class DoctorDetailInfoTeamAdapter extends BaseQuickAdapter<DoctorTeamItemInfo, BaseViewHolder> {
    public DoctorDetailInfoTeamAdapter(int layoutResId, @Nullable List<DoctorTeamItemInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorTeamItemInfo item) {
        helper.setText(R.id.item_doctor_detail_doctor_team_name, item.getTeamName());
        if (!StringUtils.isTrimEmpty(item.getMemberCount())) {
            helper.setText(R.id.item_doctor_detail_doctor_team_no, item.getMemberCount() + "人");
        }
    }
}
