package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.rounds.MedicalFileInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsPatientInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * Created by W·H·K on 2018/9/12.
 * Mod by Biao on 2019/7/9
 */
public class RoundsMedicalAdapter extends BaseQuickAdapter<RoundsPatientInfo, BaseViewHolder> {
    private int mState;
    private boolean isExperts = false;
    private OnPicClickListener onPicClickListener;

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }

    public RoundsMedicalAdapter(int layoutResId, @Nullable List<RoundsPatientInfo> data) {
        super(layoutResId, data);
    }

    public void setExperts(boolean experts) {
        isExperts = experts;
    }

    public void setOrderState(int state) {
        mState = state;
    }

    @Override
    protected void convert(BaseViewHolder helper, RoundsPatientInfo item) {
        int medicalNum = helper.getLayoutPosition() + 1;
        helper.setText(R.id.item_order_details_medical_name_tv, "病例" + medicalNum);
        helper.setText(R.id.item_order_details_medical_num_desc_tv, item.getMedicalId() + "");
        helper.setText(R.id.item_order_details_medical_patient_name_tv, item.getPatientName());
        helper.setText(R.id.item_order_details_medical_patient_gender_tv, item.getGender() == 1 ? "男" : "女");
        helper.setText(R.id.item_order_details_medical_patient_age_tv, item.getAge());
        helper.setText(R.id.item_order_details_medical_patient_illness_tv, item.getPatientIllness());
        RecyclerView itemOrderDetailsMedicalRv = helper.getView(R.id.item_order_details_medical_rv);
        if (LibCollections.isNotEmpty(item.getMedicalFiles())) {
            helper.setVisible(R.id.item_order_details_medical_rv, true);
            displayMedicalFilesView(itemOrderDetailsMedicalRv, item.getMedicalFiles());
        } else {
            helper.setGone(R.id.item_order_details_medical_rv, false);
        }
        if (mState != AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED && mState != AppConstant.AppointmentState.APPOINTMENT_FINISHED) {
            helper.setVisible(R.id.item_order_details_delete_iv, true);
        } else {
            helper.setGone(R.id.item_order_details_delete_iv, false);
        }
        if (item.isImportant()) {
            helper.setVisible(R.id.item_order_details_is_serious_tv, true);
        } else {
            helper.setGone(R.id.item_order_details_is_serious_tv, false);
        }
        if (isExperts && !item.medicalIsFinish()) {
            helper.setGone(R.id.item_order_details_operation_ll, false);
            helper.setVisible(R.id.item_order_details_incomplete_tv, true);
        } else {
            helper.setGone(R.id.item_order_details_incomplete_tv, false);
            helper.setVisible(R.id.item_order_details_operation_ll, true);
            if (isExperts) {
                helper.setGone(R.id.item_order_details_data_management_tv, false);
                helper.setGone(R.id.item_order_details_data_management_v, false);
                helper.setVisible(R.id.item_order_details_data_details_tv, true);
            } else {
                if (item.medicalIsFinish()) {
                    helper.setVisible(R.id.item_order_details_data_management_tv, true);
                    helper.setVisible(R.id.item_order_details_data_management_v, true);
                    helper.setVisible(R.id.item_order_details_data_details_tv, true);
                } else {
                    helper.setVisible(R.id.item_order_details_data_management_tv, true);
                    helper.setGone(R.id.item_order_details_data_management_v, false);
                    helper.setGone(R.id.item_order_details_data_details_tv, false);
                }
            }
        }

        helper.addOnClickListener(R.id.item_order_details_data_details_tv)
                .addOnClickListener(R.id.item_order_details_delete_iv)
                .addOnClickListener(R.id.item_order_details_data_management_tv);
    }

    private void displayMedicalFilesView(RecyclerView recyclerView, final List<MedicalFileInfo> medicalFiles) {
        PicAdapter picAdapter = new PicAdapter(R.layout.item_medical_file, medicalFiles);
        recyclerView.setLayoutManager(RecyclerViewUtils.getFullyGridLayoutManager(mContext, 5));
        recyclerView.setAdapter(picAdapter);
        picAdapter.setOnItemClickListener((adapter, view, position) -> onPicClickListener.onItemClick(adapter, view, position, picAdapter.getServerUrl()));
    }

    class PicAdapter extends BaseQuickAdapter<MedicalFileInfo, BaseViewHolder> {
        private List<String> serverUrl = new ArrayList<>();

        public PicAdapter(int layoutResId, @Nullable List<MedicalFileInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MedicalFileInfo item) {
            ImageView mImageView = helper.getView(R.id.item_medical_file_iv);
            switch (item.getType()) {
                case AppConstant.MaterailType.MATERAIL_TYPE_PIC:
                    serverUrl.add(AppConfig.getMaterialDownloadUrl() + item.getFileName());
                    GlideUtils.showOrderMedicalRecordAdapterView(mImageView, mContext, AppConfig.getMaterialDownloadUrl() + item.getFileName());
                    break;
                case AppConstant.MaterailType.MATERAIL_TYPE_DICOM:
                    mImageView.setImageResource(R.drawable.ic_image_consult_dcm);
                    break;

                case AppConstant.MaterailType.MATERAIL_TYPE_WSI:
                    mImageView.setImageResource(R.drawable.ic_image_consult_wsi);
                    break;

                case AppConstant.MaterailType.MATERAIL_TYPE_MEDIA:
                    mImageView.setImageResource(R.drawable.ic_image_consult_voice);
                    break;
                default:
                    break;
            }
        }

        public List<String> getServerUrl() {
            return serverUrl;
        }
    }

    public interface OnPicClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, int position, List<String> serverUrls);
    }
}
