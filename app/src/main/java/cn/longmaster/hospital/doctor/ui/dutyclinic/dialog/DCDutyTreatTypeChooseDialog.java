package cn.longmaster.hospital.doctor.ui.dutyclinic.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyTreatType;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyTreatTypeAdapter;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 16:25
 * @description: 值班门诊选择就诊类型
 */
public class DCDutyTreatTypeChooseDialog extends Dialog {
    @FindViewById(R.id.dialog_dc_duty_treat_type_rv)
    private RecyclerView dialogDcDutyTreatTypeRv;
    @FindViewById(R.id.dialog_dc_duty_treat_type_close_iv)
    private ImageView dialogDcDutyTreatTypeCloseIv;
    private DCDutyTreatTypeAdapter dcDutyTreatTypeAdapter;
    private OnTreatTypeChooseListener onTreatTypeChooseListener;
    private DCManager dcpManager;
    public DCDutyTreatTypeChooseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        dcpManager = AppApplication.getInstance().getManager(DCManager.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dc_duty_treat_type_list);
        ViewInjecter.inject(this);
        AppApplication.getInstance().injectManager(this);
        dcDutyTreatTypeAdapter = new DCDutyTreatTypeAdapter(R.layout.item_dc_dcty_treat_type, dcpManager.getTreatTypeList());
        dcDutyTreatTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyTreatType type = (DCDutyTreatType) adapter.getItem(position);
            if (null != type) {
                onTreatTypeChooseListener.onTreatTypeChoose(type);
                dismiss();
            }
        });
        dialogDcDutyTreatTypeRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getContext()));
        dialogDcDutyTreatTypeRv.setAdapter(dcDutyTreatTypeAdapter);
        dialogDcDutyTreatTypeCloseIv.setOnClickListener(v -> {
            dismiss();
        });
    }

    public OnTreatTypeChooseListener getOnTreatTypeChooseListener() {
        return onTreatTypeChooseListener;
    }

    public void setOnTreatTypeChooseListener(OnTreatTypeChooseListener onTreatTypeChooseListener) {
        this.onTreatTypeChooseListener = onTreatTypeChooseListener;
    }

    public interface OnTreatTypeChooseListener {
        void onTreatTypeChoose(DCDutyTreatType dcDutyTreatType);
    }
}
