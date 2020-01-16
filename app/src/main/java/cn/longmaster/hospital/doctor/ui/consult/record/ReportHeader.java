package cn.longmaster.hospital.doctor.ui.consult.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 会诊意见Header
 * Created by Yang² on 2017/7/20.
 */

public class ReportHeader extends LinearLayout {
    private OnModificationViewClickListener mOnModificationViewClickListener;
    private TextView mModificationView;

    public ReportHeader(Context context, ViewGroup root) {
        super(context);
        initView(root);
    }

    public void setOnModificationViewClickListener(OnModificationViewClickListener listener) {
        mOnModificationViewClickListener = listener;
    }

    private void initView(ViewGroup root) {
        View container = LayoutInflater.from(getContext()).inflate(R.layout.layout_report_header, root, false);
        ViewInjecter.inject(this, container);
        mModificationView = (TextView) container.findViewById(R.id.layout_report_header_modification_view);
        mModificationView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnModificationViewClickListener != null) {
                    mOnModificationViewClickListener.onModificationViewClickListener(v);
                }
            }
        });
        addView(container);
    }

    public void setModificationView(boolean mIsExperts, boolean mIsRelateRecord) {
        if (mIsExperts && !mIsRelateRecord) {
            mModificationView.setVisibility(VISIBLE);
        } else {
            mModificationView.setVisibility(GONE);
        }
    }

    public interface OnModificationViewClickListener {
        void onModificationViewClickListener(View view);
    }
}
