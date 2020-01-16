package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.CaseRemarkInfo;

/**
 * 备注Adapter
 * Created by Yang² on 2016/7/19.
 */
public class RemarkAdapter extends SimpleBaseAdapter<CaseRemarkInfo, RemarkAdapter.ViewHolder> {
    private Context mContext;

    public RemarkAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_remark;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, CaseRemarkInfo caseRemarkInfo, int position) {
        viewHolder.mTimeTv.setText(caseRemarkInfo.getRemarkDt());
        viewHolder.mContentTv.setText(caseRemarkInfo.getRemarkDesc());
        setUserTitle(viewHolder, caseRemarkInfo);

    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_remark_photo_aiv)
        private ImageView mPhotoAiv;
        @FindViewById(R.id.item_remark_title_tv)
        private TextView mTitleTv;
        @FindViewById(R.id.item_remark_time_tv)
        private TextView mTimeTv;
        @FindViewById(R.id.item_remark_content_tv)
        private TextView mContentTv;
    }

    private void setUserTitle(ViewHolder viewHolder, CaseRemarkInfo caseRemarkInfo) {
        switch (caseRemarkInfo.getUserType()) {
            case AppConstant.DoctorType.DOCTOR_TYPE_ASSISTANT_DOCTOR:
            case AppConstant.DoctorType.DOCTOR_TYPE_ASSISTANT_DOCTOR_IPAD:
                viewHolder.mTitleTv.setText(R.string.user_type_assistant_doctor);
                viewHolder.mPhotoAiv.setImageResource(R.drawable.bg_remark_photo_assistant_doctor);
                break;

            case AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR:
                viewHolder.mTitleTv.setText(R.string.user_type_superior_doctor);
                viewHolder.mPhotoAiv.setImageResource(R.drawable.bg_remark_photo_admin);
                break;

            case AppConstant.DoctorType.DOCTOR_TYPE_ATTENDING_DOCTOR:
                viewHolder.mTitleTv.setText(R.string.user_type_attending_doctor);
                viewHolder.mPhotoAiv.setImageResource(R.drawable.bg_remark_photo_attending_doctor);
                break;

        }
    }
}
