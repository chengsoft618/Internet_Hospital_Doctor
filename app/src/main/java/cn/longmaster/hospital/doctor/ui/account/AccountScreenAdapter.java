package cn.longmaster.hospital.doctor.ui.account;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/5/9.
 * Mod by biao on 2019/9/6
 */
public class AccountScreenAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private String mCurrentStr;

    public AccountScreenAdapter(int layoutResId, List<String> list, String currentStr) {
        this(layoutResId, list);
        mCurrentStr = currentStr;
    }

    public AccountScreenAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_account_screen_tv, item);
        if (StringUtils.equals(mCurrentStr, item)) {
            helper.setTextColor(R.id.item_account_screen_tv, ContextCompat.getColor(mContext, R.color.color_0096ff));
        } else {
            helper.setTextColor(R.id.item_account_screen_tv, ContextCompat.getColor(mContext, R.color.color_666666));
        }
    }
}
