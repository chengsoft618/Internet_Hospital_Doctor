package cn.longmaster.hospital.doctor.ui.dutyclinic.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientItem;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyRoomPatientAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/25 14:02
 * @description: 患者列表
 */
public class DCDutyRoomPatientListDialog extends DialogFragment {
    private SmartRefreshLayout dialogDcDutyRoomPatientListSrl;
    private RecyclerView dialogDcDutyRoomPatientListRv;
    private ImageView dialogDcDutyRoomPatientListCloseIv;
    private TextView dialogDcDutyRoomPatientListAddTv;
    private int orderId;
    @AppApplication.Manager
    DCManager dcManager;
    private DCDutyRoomPatientAdapter dcDutyRoomPatientAdapter;
    private OnDismissListener dismissListener;
    private View emptyView;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
        emptyView = createEmptyListView("当前暂无患者资料，请点击添加患者并上传患者资料", R.mipmap.bg_dc_duty_room_no_patient);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_dc_duty_room_patient_info_list, null);
        dialogDcDutyRoomPatientListAddTv = (TextView) rootView.findViewById(R.id.dialog_dc_duty_room_patient_list_add_tv);
        dialogDcDutyRoomPatientListCloseIv = (ImageView) rootView.findViewById(R.id.dialog_dc_duty_room_patient_list_close_iv);
        dialogDcDutyRoomPatientListSrl = (SmartRefreshLayout) rootView.findViewById(R.id.dialog_dc_duty_room_patient_list_srl);
        dialogDcDutyRoomPatientListRv = (RecyclerView) rootView.findViewById(R.id.dialog_dc_duty_room_patient_list_rv);

        dcDutyRoomPatientAdapter = new DCDutyRoomPatientAdapter(R.layout.item_dc_duty_room_patient, new ArrayList<>(0));
        dcDutyRoomPatientAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyRoomPatientItem info = (DCDutyRoomPatientItem) adapter.getItem(position);
            if (null != info) {
                showPatientInfo(info);
            }
        });
        dialogDcDutyRoomPatientListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getActivity()));
        dialogDcDutyRoomPatientListRv.setAdapter(dcDutyRoomPatientAdapter);
        dialogDcDutyRoomPatientListSrl.setEnableLoadMore(false);
        dialogDcDutyRoomPatientListSrl.setOnRefreshListener(refreshLayout -> dcManager.getRoomPatientList(getOrderId(), new DefaultResultCallback<List<DCDutyRoomPatientItem>>() {
            @Override
            public void onSuccess(List<DCDutyRoomPatientItem> dcDutyPatientItemInfos, BaseResult baseResult) {
                if (LibCollections.isEmpty(dcDutyPatientItemInfos)) {
                    if (null != emptyView) {
                        dcDutyRoomPatientAdapter.setEmptyView(emptyView);
                    }
                } else {
                    dcDutyRoomPatientAdapter.setNewData(dcDutyPatientItemInfos);
                }
            }

            @Override
            public void onFinish() {
                refreshLayout.finishRefresh();
            }
        }));
        dialogDcDutyRoomPatientListAddTv.setOnClickListener(v -> showPatientInfo(null));
        dialogDcDutyRoomPatientListCloseIv.setOnClickListener(v -> {
            dismiss();
        });
        dialogDcDutyRoomPatientListSrl.autoRefresh();
        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogDcDutyRoomPatientListSrl.autoRefresh();
    }

    private void showPatientInfo(DCDutyRoomPatientItem info) {
        DCDutyRoomPatientInfoDialog dialog = new DCDutyRoomPatientInfoDialog();
        dialog.setOnDismissListener(new DCDutyRoomPatientInfoDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogDcDutyRoomPatientListSrl.autoRefresh();
            }
        });
        dialog.setPatientItem(info);
        dialog.setOrderId(getOrderId());
        dialog.show(getFragmentManager(), "DCDutyRoomPatientInfoDialog");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissListener.onDismiss();
    }

    public OnDismissListener getDismissListener() {
        return dismissListener;
    }

    public void setDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    protected View createEmptyListView(String errorMsg, @DrawableRes int resId) {
        View view = getLayoutInflater().inflate(R.layout.include_new_no_data_list, null);
        ImageView includeNewNoDataIv = view.findViewById(R.id.include_new_no_data_iv);
        TextView includeNewNoDataTv = view.findViewById(R.id.include_new_no_data_tv);
        if (!StringUtils.isEmpty(errorMsg)) {
            includeNewNoDataTv.setText(errorMsg);
        }
        if (0 != resId) {
            includeNewNoDataIv.setImageResource(resId);
        }
        return view;
    }

}
