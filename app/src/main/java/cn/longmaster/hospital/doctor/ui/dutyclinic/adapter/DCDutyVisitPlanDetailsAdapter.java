package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempDetailItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author:wangyang
 * @date:2019-12-20 15:41
 * @description: 选择随访计划详情编辑——adapter
 */
public class DCDutyVisitPlanDetailsAdapter extends BaseQuickAdapter<DCDutyVisitPlantTempItem, BaseViewHolder> {
    private View placeHolderView;
    private RecyclerView isRedacrChildRv;
    private OndeletedChildViewClickListener ondeletedChildViewClickListener;

    public void setOndeletedChildViewClickListener(OndeletedChildViewClickListener ondeletedChildViewClickListener) {
        this.ondeletedChildViewClickListener = ondeletedChildViewClickListener;
    }

    public DCDutyVisitPlanDetailsAdapter(int layoutResId, @Nullable List<DCDutyVisitPlantTempItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyVisitPlantTempItem item) {
        helper.setText(R.id.item_dc_duty_visit_plan_details_adpter_date_st, TimeUtils.getDayString(item.getInsertDt()));
        placeHolderView = helper.getView(R.id.item_dc_duty_paient_visit_plan_details_adapter_place_holder_view);
        isRedacrChildRv = (RecyclerView) helper.getView(R.id.item_dc_duty_visit_plan_details_adapter_redact_child_rv);
        displayVisitPlanChildView(isRedacrChildRv, item.getFollowupTempDetailList());
        helper.addOnClickListener(R.id.item_dc_duty_visit_plan_details_adpter_date_st)
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
                helper.setText(R.id.item_dc_duty_visit_plan_details_adapter_child_content, item.getContent());
                helper.addOnClickListener(R.id.item_dc_duty_paient_disease_redact_deleted);
                helper.setVisible(R.id.item_dc_duty_paient_disease_redact_deleted, true);
            }
        }

    }

    public interface OndeletedChildViewClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, int position);
    }

}
