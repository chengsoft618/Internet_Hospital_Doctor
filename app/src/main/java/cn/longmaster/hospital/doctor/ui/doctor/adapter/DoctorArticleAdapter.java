package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorStyleInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 16:52
 * @description:
 */
public class DoctorArticleAdapter extends BaseQuickAdapter<DoctorStyleInfo.ArticleDataBean, BaseViewHolder> {
    public DoctorArticleAdapter(int layoutResId, @Nullable List<DoctorStyleInfo.ArticleDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorStyleInfo.ArticleDataBean item) {

    }
}
