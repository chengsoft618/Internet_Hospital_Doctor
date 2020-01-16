package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;

/**
 * 垫付结果适配器
 * Created by W·H·K on 2018/1/9.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class AdvanceAdapter extends BaseQuickAdapter<Integer, AdvanceAdapter.MyViewHolder> {
    private Context mContext;
    private int mResultType;
    private List<VisitDetailsInfo> mAdvances;
    private List<Integer> mAdvanceData;
    private Map<Integer, PatientInfo> mPatientInfoMap = new HashMap<>();

    public AdvanceAdapter(Context context, int type, @LayoutRes int layoutResId, @Nullable List<Integer> data, List<VisitDetailsInfo> advances) {
        super(layoutResId, data);
        this.mContext = context;
        this.mResultType = type;
        this.mAdvances = advances;
        this.mAdvanceData = data;
    }

    @Override
    protected void convert(MyViewHolder helper, Integer item) {
        displayView(helper, item);
    }

    static class MyViewHolder extends BaseViewHolder {
        public LinearLayout mRecyclerViewItem;
        public TextView mAppointemtIdTv;
        public TextView mNameTv;
        public TextView mMoneyTv;
        public ImageView mResultIcn;

        public MyViewHolder(View view) {
            super(view);
            mRecyclerViewItem = (LinearLayout) view.findViewById(R.id.advance_reault);
            mAppointemtIdTv = (TextView) view.findViewById(R.id.item_advance_result_appointment_id_tv);
            mNameTv = (TextView) view.findViewById(R.id.item_advance_result_name_tv);
            mMoneyTv = (TextView) view.findViewById(R.id.item_advance_result_money_tv);
            mResultIcn = (ImageView) view.findViewById(R.id.item_advance_result_icn);
        }
    }

    public void displayView(AdvanceAdapter.MyViewHolder viewHolder, int integer) {
        viewHolder.mAppointemtIdTv.setText(integer + "");
        getPatientInfo(viewHolder, integer);
        if (mResultType == AppConstant.AdvanceResultType.RESULT_TYPE_FAIL) {
            viewHolder.mResultIcn.setImageResource(R.drawable.ic_advance_result_fail);
        } else {
            viewHolder.mResultIcn.setImageResource(R.drawable.ic_advance_result_suc);
        }
        for (int i = 0; i < mAdvances.size(); i++) {
            if (integer == mAdvances.get(i).getAppointmentId()) {
                viewHolder.mMoneyTv.setText(mContext.getString(R.string.advance_money, mAdvances.get(i).getPayValue() + ""));
                break;
            }
        }
    }

    public void getPatientInfo(final AdvanceAdapter.MyViewHolder viewHolder, final int appointmentId) {
        if (mPatientInfoMap.containsKey(appointmentId)) {
            viewHolder.mNameTv.setText(mPatientInfoMap.get(appointmentId).getPatientBaseInfo().getRealName());
        } else {
            AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                @Override
                public void onSuccess(PatientInfo patientInfo) {
                    if (patientInfo != null) {
                        mPatientInfoMap.put(appointmentId, patientInfo);
                        viewHolder.mNameTv.setText(patientInfo.getPatientBaseInfo().getRealName());
                        Logger.logD(Logger.COMMON, "->AdvanceActivity()->initData->patientInfo:" + patientInfo.getPatientBaseInfo().getRealName());
                    }
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
        }
    }
}
