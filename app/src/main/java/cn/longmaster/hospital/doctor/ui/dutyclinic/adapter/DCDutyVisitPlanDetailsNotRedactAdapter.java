package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempDetailItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author: wangyang
 * @date: 2019-12-20 15:41
 * @description: 选择随访计划详情——adapter
 */
public class DCDutyVisitPlanDetailsNotRedactAdapter extends BaseQuickAdapter<DCDutyVisitPlantTempItem, BaseViewHolder> {
    private String[] contentType = new String[]{"复诊提醒", "用药提醒", "资料上传提醒"};
    private View timeLineView;
    private RecyclerView notRedactChildRv;

    public DCDutyVisitPlanDetailsNotRedactAdapter(int layoutResId, @Nullable List<DCDutyVisitPlantTempItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyVisitPlantTempItem item) {
        if (item != null) {
            timeLineView = helper.getView(R.id.item_dc_duty_visit_plan_details_time_line_view);
            notRedactChildRv = (RecyclerView) helper.getView(R.id.item_dc_duty_visit_plan_details_child_rv);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) timeLineView.getLayoutParams();
            if (LibCollections.isNotEmpty(getData())) {
                if (helper.getAdapterPosition() == 0) {
                    if (LibCollections.size(getData()) > 2) {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(22), 0, 0);
                        //代码设置是px
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_visit_plan_details_bottom_content_ll);
                    } else {
                        layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(22), 0, 28);
                        //代码设置是px
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                    }
                } else if (helper.getAdapterPosition() < LibCollections.size(getData()) - 1) {
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, 0);
                    //代码设置是px
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_visit_plan_details_bottom_content_ll);
                    helper.setImageResource(R.id.item_dc_duty_visit_plan_details_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_bottom_diagnostic_circular);
                } else {
                    layoutParams.setMargins(DisplayUtil.dp2px(7), DisplayUtil.dp2px(0), 0, DisplayUtil.dp2px(28));
                    //代码设置是px
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_dc_duty_visit_plan_details_top_content_rl);
                    helper.setImageResource(R.id.item_dc_duty_visit_plan_details_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_bottom_diagnostic_circular);
                }
            }
            helper.setText(R.id.item_dc_duty_visit_plan_details_top_content_date_tv, TimeUtils.string2String(item.getListDt(),"yyyy-MM-dd HH:ss","yyyy年MM月dd日"));
            notRedactChildRv.setVisibility(View.VISIBLE);
            displayVisitPlanChildView(notRedactChildRv, item.getFollowupTempDetailList());
        }
    }

    private void displayVisitPlanChildView(RecyclerView recyclerView, final List<DCDutyVisitPlantTempDetailItem> dcDutyVisitPlanSendMessageInfos) {
        SendMessageAdapter sendMessageAdapter = new SendMessageAdapter(R.layout.item_dc_duty_visit_plan_details_adapter_redact_child_layout, dcDutyVisitPlanSendMessageInfos);
        recyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(((Activity) mContext)));
        recyclerView.setAdapter(sendMessageAdapter);
    }

    class SendMessageAdapter extends BaseQuickAdapter<DCDutyVisitPlantTempDetailItem, BaseViewHolder> {

        public SendMessageAdapter(int layoutResId, @Nullable List<DCDutyVisitPlantTempDetailItem> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, DCDutyVisitPlantTempDetailItem item) {
            if (item != null) {
                helper.setText(R.id.item_dc_duty_visit_plan_details_adapter_child_content, getVisitPlanMessageContent().get(item.getType() + "") + ": " + item.getContent());
            }
        }
    }

    private Map<String, String> getVisitPlanMessageContent() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 1; i <= contentType.length; i++) {
            map.put(i + "", contentType[i - 1]);
        }
        return map;
    }

}
