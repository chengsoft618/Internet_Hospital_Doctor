package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlanIsRemindDetailsItemData;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.TimeUtils;

/**
 * @author:lm
 * @date: 2019-12-18 20:02
 * @description: 随访计划Fragment--已提醒adapter
 */
public class DCDutyVisitPlanIsRemindAdapter extends BaseQuickAdapter<DCDutyVisitPlanIsRemindDetailsItemData, BaseViewHolder> {
    private View timeLineView;
    private RelativeLayout isRemindToprl;
    private TextView isRemindDateTimetv;
    private RelativeLayout isRemindBottomContentrl;
    private TextView isRemindBottomContenttv;
    private DCDutyVisitPlanIsRemindDetailsItemData dataItem;

    public DCDutyVisitPlanIsRemindAdapter(int layoutResId, @Nullable List<DCDutyVisitPlanIsRemindDetailsItemData> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyVisitPlanIsRemindDetailsItemData item) {
        if (item != null) {
            timeLineView = helper.getView(R.id.ic_dc_duty_visit_plan_is_redmin_time_line);
            isRemindToprl = (RelativeLayout) helper.getView(R.id.ic_dc_duty_visit_plan_is_redmin_top_rl);
            isRemindDateTimetv = (TextView) helper.getView(R.id.ic_dc_duty_visit_plan_is_redmin_date_time_tv);
            isRemindBottomContentrl = (RelativeLayout) helper.getView(R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
            isRemindBottomContenttv = (TextView) helper.getView(R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_tv);
            setPosition(helper.getAdapterPosition(), getData());
        }
    }

    public void setPosition(int position, List<DCDutyVisitPlanIsRemindDetailsItemData> dataDetailsItem) {
        dataItem = dataDetailsItem.get(position);
        //时间轴竖线的layoutParams,用来动态的添加竖线
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) timeLineView.getLayoutParams();
        if (LibCollections.isNotEmpty(dataDetailsItem)) {
            if (position == 0) {
                if (LibCollections.size(dataDetailsItem) >= 2) {
                    if ((dataItem.getListId() != dataDetailsItem.get(position + 1).getListId())) {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(35), 0, 0);
                    } else {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(35), 0, 0);
                    }
                } else {
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(35), 0, DisplayUtil.dp2px(15));
                }
                isRemindToprl.setVisibility(View.VISIBLE);
                isRemindDateTimetv.setText(TimeUtils.string2String(dataItem.getTime(), "yyyy-MM-dd HH:ss", "yyyy年MM月dd日"));
                //代码设置是px
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_top_rl);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
            } else if (position < LibCollections.size(dataDetailsItem) - 1) {
                if (dataItem.getListId() == dataDetailsItem.get(position - 1).getListId()) {
                    if (dataItem.getListId() == dataDetailsItem.get(position + 1).getListId()) {
                        isRemindToprl.setVisibility(View.GONE);
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                    } else {
                        isRemindToprl.setVisibility(View.GONE);
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                    }

                } else {
                    if (dataItem.getListId() != dataDetailsItem.get(position + 1).getListId()) {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                    } else {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                    }
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                    isRemindToprl.setVisibility(View.VISIBLE);
                    isRemindDateTimetv.setText(TimeUtils.string2String(dataItem.getTime(), "yyyy-MM-dd HH:ss", "yyyy年MM月dd日"));
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_top_rl);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                }
            } else {
                if (dataItem.getListId() != dataDetailsItem.get(position - 1).getListId()) {
                    isRemindToprl.setVisibility(View.VISIBLE);
                    isRemindDateTimetv.setText(TimeUtils.string2String(dataItem.getTime(), "yyyy-MM-dd HH:ss", "yyyy年MM月dd日"));
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, DisplayUtil.dp2px(15));
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_top_rl);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                } else {
                    isRemindToprl.setVisibility(View.GONE);
                    isRemindDateTimetv.setText(TimeUtils.string2String(dataItem.getTime(), "yyyy-MM-dd HH:ss", "yyyy年MM月dd日"));
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, DisplayUtil.dp2px(15));
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ic_dc_duty_visit_plan_is_redmin_bottom_content_rl);
                }
            }
        }
        timeLineView.setLayoutParams(layoutParams);
        isRemindBottomContenttv.setText(dataItem.getContent());
    }

}