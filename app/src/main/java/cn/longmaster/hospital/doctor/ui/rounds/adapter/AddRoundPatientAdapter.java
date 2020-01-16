package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.MedicalFileInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.view.MyGridView;

/**
 * @author ABiao_Abiao
 * @date 2019/11/26 9:11
 * @description: 添加查房添加患者
 */
public class AddRoundPatientAdapter extends BaseQuickAdapter<WaitRoundsPatientInfo, BaseViewHolder> {
    private OnPicClickListener onPicClickListener;
    private SparseArray<RoundsMedicalDetailsInfo> roundsMedicalDetailsInfos = new SparseArray<>();

    public AddRoundPatientAdapter(int layoutResId, @Nullable List<WaitRoundsPatientInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaitRoundsPatientInfo item) {
        helper.setText(R.id.item_rounds_mould_medical_id, item.getMedicalId() + "");
        getPatientDetail(item.getMedicalId(), new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                roundsMedicalDetailsInfos.put(roundsMedicalDetailsInfo.getMedicalId(), roundsMedicalDetailsInfo);
                helper.setVisible(R.id.item_rounds_mould_medical_gv, true);
                displayPics(helper, roundsMedicalDetailsInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                helper.setGone(R.id.item_rounds_mould_medical_gv, false);
            }
        });
    }

    private void displayPics(BaseViewHolder helper, RoundsMedicalDetailsInfo info) {
        int medicalNum = helper.getLayoutPosition() + 1;
        helper.setText(R.id.item_rounds_mould_medical_num, "病例" + medicalNum);
        helper.addOnClickListener(R.id.item_rounds_mould_delete_iv);
        helper.setText(R.id.item_rounds_mould_medical_patient_name, info.getPatientName());
        helper.setText(R.id.item_rounds_mould_medical_patient_gender, info.getGender() == 1 ? "男" : "女");
        helper.setText(R.id.item_rounds_mould_medical_patient_age, info.getAge() + "");
        helper.setText(R.id.item_rounds_mould_medical_illness, info.getPatientIllness());
        if (info.getImportant() == 1) {
            helper.setVisible(R.id.item_rounds_mould_important_view, true);
        } else {
            helper.setGone(R.id.item_rounds_mould_important_view, false);
        }

        final MyGridView myGridView = helper.getView(R.id.item_rounds_mould_medical_gv);
        final List<String> serverUrls = new ArrayList<>();
        List<MedicalFileInfo> medicalFileInfos = info.getMedicalFileInfos();
        if (null != medicalFileInfos) {
            for (MedicalFileInfo medicalFileInfo : medicalFileInfos) {
                serverUrls.add(AppConfig.getMaterialDownloadUrl() + medicalFileInfo.getFileName());
            }
        }
        OrderMedicalRecordAdapter adapter = new OrderMedicalRecordAdapter(mContext, medicalFileInfos);
        myGridView.setAdapter(adapter);
        myGridView.setOnItemClickListener((parent, view, position, id) -> {
            onPicClickListener.onPicItemClick(parent, view, serverUrls, position);
        });
    }

    public List<RoundsMedicalDetailsInfo> getRoundsMedicalDetailsInfos() {
        List<RoundsMedicalDetailsInfo> infos = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            WaitRoundsPatientInfo info = getItem(i);
            if (null != info) {
                infos.add(roundsMedicalDetailsInfos.get(info.getMedicalId()));
            }
        }
        return infos;
    }

    private void getPatientDetail(int medicalId, OnResultCallback<RoundsMedicalDetailsInfo> resultCallback) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(resultCallback);
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }

    public interface OnPicClickListener {
        void onPicItemClick(AdapterView<?> picAdapter, View view, List<String> serverUrls, int position);
    }
}
