package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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
 * @author:wangyang
 * @date:2019-12-20 15:41
 * @description: 选择随访计划详情编辑——adapter
 */
public class DCDutyVisitPlanDetailsIsRedactAdapter extends BaseMultiItemQuickAdapter<DCDutyVisitPlantTempItem, BaseViewHolder> {
    private String[] contentType = new String[]{"复诊提醒", "用药提醒", "资料上传提醒"};
    private View topTimeLineView;
    private View bottomTineView;
    private View placeHolderView;
    private RecyclerView isRedacrChildRv;
    private OndeletedChildViewClickListener ondeletedChildViewClickListener;

    public void setOndeletedChildViewClickListener(OndeletedChildViewClickListener ondeletedChildViewClickListener) {
        this.ondeletedChildViewClickListener = ondeletedChildViewClickListener;
    }

    public DCDutyVisitPlanDetailsIsRedactAdapter(List<DCDutyVisitPlantTempItem> data) {
        super(data);
        addItemType(DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_LAYOUT, R.layout.item_dc_duty_visit_plan_details_adapter_redact_layout);
        addItemType(DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_ADD_LAYOUT, R.layout.item_dc_duty_visit_plan_details_adapter_add_redact_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyVisitPlantTempItem item) {
        if (item != null) {
            switch (helper.getItemViewType()) {
                case DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_LAYOUT:
                    topTimeLineView = helper.getView(R.id.item_dc_duty_visit_plan_details_adapter_redact_time_line_view_top);
                    bottomTineView = helper.getView(R.id.item_dc_duty_visit_plan_details_adapter_redact_time_line_view_bottom);
                    placeHolderView = helper.getView(R.id.item_dc_duty_paient_visit_plan_details_adapter_place_holder_view);
                    isRedacrChildRv = (RecyclerView) helper.getView(R.id.item_dc_duty_visit_plan_details_adapter_redact_child_rv);
                    if (LibCollections.isNotEmpty(getData())) {
                        if (helper.getAdapterPosition() == 0) {
                            topTimeLineView.setVisibility(View.GONE);
                            bottomTineView.setVisibility(View.VISIBLE);
                            helper.setImageResource(R.id.item_dc_duty_visit_plan_details_adapter_top_diagnostic_circular, R.mipmap.ic_dc_duty_patient_disease_top_diagnostic_circular);
                        } else {
                            topTimeLineView.setVisibility(View.VISIBLE);
                            bottomTineView.setVisibility(View.VISIBLE);
                            helper.setImageResource(R.id.item_dc_duty_visit_plan_details_adapter_top_diagnostic_circular, R.mipmap.ic_dc_duty_paient_disease_redact_deleted);
                            helper.addOnClickListener(R.id.item_dc_duty_visit_plan_details_adapter_top_diagnostic_circular);
                        }
                    }
                    SuperTextView superTextView = (SuperTextView) helper.getView(R.id.item_dc_duty_visit_plan_details_adpter_date_st);
                    superTextView.setCenterString(TimeUtils.string2String(item.getListDt(), "yyyy-MM-dd HH:ss", "yyyy年MM月dd"));
                    if (LibCollections.isNotEmpty(item.getFollowupTempDetailList())) {
                        placeHolderView.setVisibility(View.GONE);
                        isRedacrChildRv.setVisibility(View.VISIBLE);
                        displayVisitPlanChildView(isRedacrChildRv, item.getFollowupTempDetailList());
                    }
                    break;
                case DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_ADD_LAYOUT:
                    View addTimeLineView = helper.getView(R.id.item_dc_duty_visit_plan_details_adapter_add_redact_time_line_view);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addTimeLineView.getLayoutParams();
                    if (LibCollections.size(getData()) >=2 ){
                        addTimeLineView.setVisibility(View.VISIBLE);
                        params.setMargins(DisplayUtil.dp2px(7),0,0,DisplayUtil.dp2px(7));
                        addTimeLineView.setLayoutParams(params);
                    }else {
                        addTimeLineView.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
        helper.addOnClickListener(R.id.item_dc_duty_visit_plan_details_adapter_top_diagnostic_circular)
                .addOnClickListener(R.id.item_dc_duty_visit_plan_details_adpter_date_st)
                .addOnClickListener(R.id.item_dc_duty_visit_plan_details_adapter_add_iv)
                .addOnClickListener(R.id.item_dc_duty_visit_plan_details_adapter_add_redact_layout_addtv);
    }

    private void displayVisitPlanChildView(RecyclerView recyclerView, final List<DCDutyVisitPlantTempDetailItem> followupTempDetailListBeans) {
        SendMessageAdapter sendMessageAdapter = new SendMessageAdapter(R.layout.item_dc_duty_visit_plan_details_adapter_redact_child_layout, followupTempDetailListBeans);
        recyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(((Activity) mContext)));
        sendMessageAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ondeletedChildViewClickListener.onItemClick(adapter, view, position);
            }
        });
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
                helper.setVisible(R.id.item_dc_duty_paient_disease_redact_deleted, true);
                helper.addOnClickListener(R.id.item_dc_duty_paient_disease_redact_deleted);
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

    public interface OndeletedChildViewClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, int position);
    }

}
