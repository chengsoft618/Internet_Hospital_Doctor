package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.rounds.MedicalFileInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * Created by W·H·K on 2018/7/9.
 */

public class OrderMedicalRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<MedicalFileInfo> list;

    public OrderMedicalRecordAdapter(Context context, List<MedicalFileInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order_medical, null);
            vh = new ViewHolder();
            vh.mImageView = convertView.findViewById(R.id.item_order_iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        displayView(vh, position);
        return convertView;
    }

    private void displayView(final ViewHolder vh, final int position) {
        Logger.logI(Logger.COMMON, "MedicalRecordDataAdapte->displayView：" + AppConfig.getMaterialDownloadUrl() + list.get(position).getFileName());
        switch (list.get(position).getType()) {
            case AppConstant.MaterailType.MATERAIL_TYPE_PIC:
                GlideUtils.showOrderMedicalRecordAdapterView(vh.mImageView, mContext, AppConfig.getMaterialDownloadUrl() + list.get(position).getFileName());
                break;

            case AppConstant.MaterailType.MATERAIL_TYPE_DICOM:
                vh.mImageView.setImageResource(R.drawable.ic_image_consult_dcm);
                break;

            case AppConstant.MaterailType.MATERAIL_TYPE_WSI:
                vh.mImageView.setImageResource(R.drawable.ic_image_consult_wsi);
                break;

            case AppConstant.MaterailType.MATERAIL_TYPE_MEDIA:
                vh.mImageView.setImageResource(R.drawable.ic_image_consult_voice);
                break;
            default:
                break;
        }
    }

    class ViewHolder {
        private ImageView mImageView;
    }
}
