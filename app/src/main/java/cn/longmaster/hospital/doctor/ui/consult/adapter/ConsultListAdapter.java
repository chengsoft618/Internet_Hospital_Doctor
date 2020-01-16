package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.util.ConsultUtil;

/**
 * 就诊列表
 * Created by Yang² on 2017/5/16.
 */

public class ConsultListAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    private SparseArray<AppointmentInfo> mAppointmentInfoMap;
    private SparseArray<PatientInfo> mPatientMap;
    private int mSelectAppointmentId = -1;

    public ConsultListAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
        mAppointmentInfoMap = new SparseArray<>();
        mPatientMap = new SparseArray<>();
    }

    public void setSelectAppointment(int appointmentId) {
        mSelectAppointmentId = appointmentId;
        notifyDataSetChanged();
    }

    public AppointmentInfo getSelectAppointment(int appointmentId) {
        return mAppointmentInfoMap.get(appointmentId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        helper.setBackgroundRes(R.id.item_consult_list_bg_layout_ll, mSelectAppointmentId == item ? R.drawable.bg_consult_list_item_select : R.drawable.bg_consult_list_item);
        if (mAppointmentInfoMap.get(item) != null) {
            displayAppointmentInfo(helper, mAppointmentInfoMap.get(item));
        } else {
            getAppointmentInfo(helper, item);
        }
        if (mPatientMap.get(item) != null) {
            displayPatientInfo(helper, mPatientMap.get(item));
        } else {
            getPatientInfo(helper, item);
        }
    }

    public class ViewHolder {
        @FindViewById(R.id.item_consult_list_bg_layout_ll)
        private LinearLayout mBgLayoutLl;
        @FindViewById(R.id.item_consult_list_disease_ll)
        private LinearLayout mDiseaseLl;
        @FindViewById(R.id.item_consult_list_serial_num_tv)
        private TextView mSerialNumTv;
        @FindViewById(R.id.item_consult_list_disease_tv)
        private TextView mDiseaseTv;
        @FindViewById(R.id.item_consult_list_patient_tv)
        private TextView mPatientTv;
        @FindViewById(R.id.item_consult_list_appoint_num_tv)
        private TextView mAppointNumTv;
        @FindViewById(R.id.item_consult_list_status_tv)
        private TextView mStatusTv;

        private void setViewTag(int appointmentId) {
            mBgLayoutLl.setTag(appointmentId);
            mSerialNumTv.setText("");
            mDiseaseTv.setText("");
            mPatientTv.setText("");
            mAppointNumTv.setText(mContext.getString(R.string.consult_room_appoint_id, ""));
            mStatusTv.setText("");
        }
    }

    /**
     * 获取预约信息
     *
     * @param appointmentId
     */
    private void getAppointmentInfo(BaseViewHolder helper, final int appointmentId) {
        AppApplication.getInstance().getManager(ConsultManager.class).getAppointmentInfo(appointmentId, new OnResultListener<AppointmentInfo>() {
            @Override
            public void onResult(BaseResult baseResult, AppointmentInfo appointmentInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS && appointmentInfo != null) {
                    mAppointmentInfoMap.put(appointmentId, appointmentInfo);
                    Logger.logD(Logger.APPOINTMENT, "getAppointmentInfo->appointmentInfo:" + appointmentInfo);
                    displayAppointmentInfo(helper, appointmentInfo);
                }
            }
        });
    }

    /**
     * 设置预约相关数据
     * 所有预约相关数据在此设置
     *
     * @param appointmentInfo
     */
    private void displayAppointmentInfo(BaseViewHolder helper, final AppointmentInfo appointmentInfo) {
        helper.setBackgroundRes(R.id.item_consult_list_disease_ll, appointmentInfo.getBaseInfo().getCaseLevel() == 0 ? R.drawable.bg_consult_list_case_general : R.drawable.bg_consult_list_case_worse);
        helper.setText(R.id.item_consult_list_disease_tv, appointmentInfo.getBaseInfo().getCaseLevel() == 0 ? R.string.case_level_general : R.string.case_level_worse);
        helper.setText(R.id.item_consult_list_serial_num_tv, appointmentInfo.getBaseInfo().getSerialNumber() + "");
        helper.setText(R.id.item_consult_list_appoint_num_tv, mContext.getString(R.string.consult_room_appoint_id, appointmentInfo.getBaseInfo().getAppointmentId() + ""));
        helper.setText(R.id.item_consult_list_status_tv, ConsultUtil.getAppointStateDesc(mContext, appointmentInfo));
    }

    /**
     * 获取患者信息并显示
     *
     * @param appointmentId
     */
    private void getPatientInfo(BaseViewHolder helper, final int appointmentId) {
        AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    mPatientMap.put(appointmentId, patientInfo);
                    displayPatientInfo(helper, patientInfo);
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

    /**
     * 设置患者姓名和疾病名称
     * 所有患者信息相关的 在此设置
     *
     * @param info
     */
    private void displayPatientInfo(BaseViewHolder helper, PatientInfo info) {
        if (null != info && null != info.getPatientBaseInfo()) {
            helper.setText(R.id.item_consult_list_patient_tv, info.getPatientBaseInfo().getRealName());
        }
    }
}
