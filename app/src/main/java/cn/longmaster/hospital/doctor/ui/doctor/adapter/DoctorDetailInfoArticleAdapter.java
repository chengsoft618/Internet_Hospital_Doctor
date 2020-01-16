package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseBooleanArray;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorStyleInfo;
import cn.longmaster.utils.StringUtils;


/**
 * @author Mloong_Abiao
 * @date 2019/6/6 13:41
 * @description:
 */
public class DoctorDetailInfoArticleAdapter extends BaseQuickAdapter<DoctorStyleInfo.ArticleDataBean, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public DoctorDetailInfoArticleAdapter(int layoutResId, @Nullable List<DoctorStyleInfo.ArticleDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorStyleInfo.ArticleDataBean item) {
        TextView tvArticleName = helper.getView(R.id.item_doctor_detail_doctor_article_name);
        String articleName = item.getContent();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(articleName);
        if (null != array && array.get(helper.getLayoutPosition())) {
            UnderlineSpan span = new UnderlineSpan();
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_45aef8));
            spannableStringBuilder.setSpan(span, 0, StringUtils.length(articleName), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(colorSpan, 0, StringUtils.length(articleName), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_333333));
            spannableStringBuilder.setSpan(colorSpan, 0, StringUtils.length(articleName), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tvArticleName.setText(spannableStringBuilder);
    }

    public void setSelected(int position) {
        array = new SparseBooleanArray(getData().size());
        array.put(position, true);
        notifyDataSetChanged();
    }
}
