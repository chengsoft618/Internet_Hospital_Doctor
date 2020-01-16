package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;

/**
 * 值班门诊-值班列表适配器
 * Created by yangyong on 2019-11-29.
 */
public class DCDutyListAdapter extends BaseQuickAdapter<DCProjectInfo, BaseViewHolder> {
    private OnDutySwitchIconClickListener dutySwitchIconClickListener;

    public DCDutyListAdapter(int layoutResId, @Nullable List<DCProjectInfo> data) {
        super(layoutResId, data);
    }

    public void setDutySwitchIconClickListener(OnDutySwitchIconClickListener dutySwitchIconClickListener) {
        this.dutySwitchIconClickListener = dutySwitchIconClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, DCProjectInfo item) {
        helper.setText(R.id.item_dcduty_list_name_tv, item.getItemName());
        if (item.getDutyState() == 1) {
            helper.setImageResource(R.id.item_dcduty_list_switch_iv, R.drawable.bg_switch_blue_white);
            helper.setText(R.id.item_dcduty_list_switch_tv, "正在值班");
            helper.setTextColor(R.id.item_dcduty_list_switch_tv, AppApplication.getInstance().getResources().getColor(R.color.color_049eff));
        } else {
            helper.setImageResource(R.id.item_dcduty_list_switch_iv, R.drawable.bg_switch_gray_white);
            helper.setText(R.id.item_dcduty_list_switch_tv, "尚未值班");
            helper.setTextColor(R.id.item_dcduty_list_switch_tv, AppApplication.getInstance().getResources().getColor(R.color.color_cccccc));
        }

        helper.setOnClickListener(R.id.item_dcduty_list_switch_iv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dutySwitchIconClickListener != null) {
                    dutySwitchIconClickListener.onDutySwitchIconClickListener(item);
                }
            }
        });
    }

    public interface OnDutySwitchIconClickListener {
        void onDutySwitchIconClickListener(DCProjectInfo projectInfo);
    }
}
