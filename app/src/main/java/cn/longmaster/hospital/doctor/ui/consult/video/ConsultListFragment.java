package cn.longmaster.hospital.doctor.ui.consult.video;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.ui.consult.adapter.ConsultListAdapter;
import cn.longmaster.utils.RecyclerViewUtils;


/**
 * 诊室弹窗就诊列表
 * Created by Yang² on 2017/5/15.
 * Mod by biao on 2019/8/28
 */

public class ConsultListFragment extends DialogFragment {
    private static final String KEY_TO_QUERY_APP_ID = "KEY_TO_QUERY_APP_ID";
    private static final String KEY_TO_QUERY_APP_IDS = "KEY_TO_QUERY_APP_IDS";

    private TextView fragmentConsultListTitleTv;
    private ImageButton fragmentConsultListCloseIb;
    private RecyclerView fragmentConsultListListRv;
    private ConsultListAdapter mConsultListAdapter;
    private OnSelectListener mOnSelectListener;

    public static ConsultListFragment getInstance(int appointmentId, List<Integer> appointmentIds) {
        ConsultListFragment fragment = new ConsultListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TO_QUERY_APP_ID, appointmentId);
        bundle.putSerializable(KEY_TO_QUERY_APP_IDS, (Serializable) appointmentIds);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_consult_list, null);
        fragmentConsultListTitleTv = (TextView) rootView.findViewById(R.id.fragment_consult_list_title_tv);
        fragmentConsultListCloseIb = (ImageButton) rootView.findViewById(R.id.fragment_consult_list_close_ib);
        fragmentConsultListListRv = (RecyclerView) rootView.findViewById(R.id.fragment_consult_list_list_rv);
        mConsultListAdapter = new ConsultListAdapter(R.layout.item_consult_list, getAppointmentIds());
        mConsultListAdapter.setNewData(getAppointmentIds());
        mConsultListAdapter.setSelectAppointment(getAppointmentId());
        fragmentConsultListListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getActivity()));
        fragmentConsultListListRv.setAdapter(mConsultListAdapter);

        mConsultListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Integer appointmentId = (Integer) adapter.getItem(position);
                if (appointmentId != null) {
                    mConsultListAdapter.setSelectAppointment(appointmentId);
                    mOnSelectListener.onSelected(position, appointmentId, mConsultListAdapter.getSelectAppointment(appointmentId));
                    dismiss();
                }
            }
        });
        fragmentConsultListCloseIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(rootView);
        return builder.create();
    }

    public void setSelectAppointment(int appointmentId) {
        mConsultListAdapter.setSelectAppointment(appointmentId);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.mOnSelectListener = listener;
    }

    @OnClick({R.id.fragment_consult_list_close_ib})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_consult_list_close_ib:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnSelectListener {
        void onSelected(int position, int appointmentId, AppointmentInfo appointmentInfo);
    }

    private int getAppointmentId() {
        return getArguments() == null ? 0 : getArguments().getInt(KEY_TO_QUERY_APP_ID);
    }

    private List<Integer> getAppointmentIds() {
        return getArguments() == null ? null : (List<Integer>) getArguments().getSerializable(KEY_TO_QUERY_APP_IDS);
    }
}
