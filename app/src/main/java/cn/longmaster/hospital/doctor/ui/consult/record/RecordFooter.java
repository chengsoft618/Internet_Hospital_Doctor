package cn.longmaster.hospital.doctor.ui.consult.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 会诊意见Footer
 * Created by Yang² on 2017/7/20.
 */

public class RecordFooter extends LinearLayout {
    @FindViewById(R.id.layout_report_footer_signature_aiv)
    private ImageView mSignatureAiv;
    @FindViewById(R.id.layout_report_footer_date_tv)
    private TextView mDateTv;

    private Context mContext;

    public RecordFooter(Context context, ViewGroup root) {
        super(context);
        this.mContext = context;
        initView(root);
    }

    private void initView(ViewGroup root) {
        View container = LayoutInflater.from(getContext()).inflate(R.layout.layout_report_footer, root, false);
        ViewInjecter.inject(this, container);
        addView(container);
    }

    public void setDate(String date) {
        mDateTv.setText(date);
    }

    public void setSignature(String filePath, String url) {
        //mSignatureAiv.loadImage(filePath, url);
        GlideUtils.showImage(mSignatureAiv, mContext, url);
    }
}
