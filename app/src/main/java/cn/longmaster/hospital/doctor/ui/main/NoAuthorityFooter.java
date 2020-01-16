package cn.longmaster.hospital.doctor.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 首页没有权限底部
 * Created by Yang² on 2017/3/31.
 */

public class NoAuthorityFooter extends LinearLayout {

    private Context mContext;

    private OnClickListener mOnClickListener;

    public NoAuthorityFooter(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_authority_footer, null);
        ViewInjecter.inject(this, view);
        addView(view);
    }

    @OnClick({R.id.layout_no_authority_footer_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_no_authority_footer_iv:
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(view);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
