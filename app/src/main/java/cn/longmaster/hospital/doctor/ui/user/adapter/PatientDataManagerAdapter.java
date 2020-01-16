package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataItem;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/4 11:39
 * @description: 患者材料管理
 */
public class PatientDataManagerAdapter extends BaseQuickAdapter<PatientDataItem, BaseViewHolder> {
    private OnPicsItemChildClickListener onPicsItemChildClickListener;

    public PatientDataManagerAdapter(int layoutResId, @Nullable List<PatientDataItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientDataItem item) {
        PatientDataPicManagerAdapter patientDataPicManagerAdapter = new PatientDataPicManagerAdapter(R.layout.item_patient_data_manager_pics, item.getDataList());
        patientDataPicManagerAdapter.setOnItemClickListener((adapter, view, position) -> onPicsItemChildClickListener.onItemClick(this, helper.getLayoutPosition(), adapter, view, position));
        if (LibCollections.isNotEmpty(item.getDataList())) {
            RecyclerView recyclerView = helper.getView(R.id.item_patient_data_manager_patient_pics_rv);
            recyclerView.getLayoutParams().height = DisplayUtil.dp2px(106);
            recyclerView.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(mContext, 4));
            recyclerView.setAdapter(patientDataPicManagerAdapter);
        }
        helper.setText(R.id.item_patient_data_manager_name_tv, item.getPatientName());
        helper.setText(R.id.item_patient_data_manager_hospital_name_tv, item.getHospitalName());
        helper.setText(R.id.item_patient_data_manager_patient_num_desc_tv, item.getMedicalId());
        helper.setText(R.id.item_patient_data_manager_in_hospital_num_desc_tv, StringUtils.isEmpty(item.getHospitalizaId()) ? "--" : item.getHospitalizaId());
        helper.addOnClickListener(R.id.item_patient_data_manager_patient_has_pics_ll);
    }

    public void setOnPicsItemChildClickListener(OnPicsItemChildClickListener onPicsItemChildClickListener) {
        this.onPicsItemChildClickListener = onPicsItemChildClickListener;
    }

    public interface OnPicsItemChildClickListener {
        void onItemClick(BaseQuickAdapter baseAdapter, int basePosition, BaseQuickAdapter adapter, View view, int position);
    }
}
