package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;

/**
 * Created by W·H·K on 2018/5/9.
 */
public class AddIntentionTimeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean canDelete;

    public AddIntentionTimeAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_add_intention_tv, item);
        if (isCanDelete()) {
            helper.setVisible(R.id.item_add_intention_delete, true);
            helper.addOnClickListener(R.id.item_add_intention_delete);
        } else {
            helper.setGone(R.id.item_add_intention_delete, false);
        }
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
