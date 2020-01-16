package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderCureDtInfo;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;

/**
 * Created by W·H·K on 2018/5/9.
 */
public class ChoiceReceiveTimeAdapter extends BaseQuickAdapter<OrderCureDtInfo, BaseViewHolder> {

    public ChoiceReceiveTimeAdapter(int layoutResId, @Nullable List<OrderCureDtInfo> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderCureDtInfo item) {
        String timeStr = item.getCureDt();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm");
            Date date = format.parse(timeStr);//有异常要捕获
            SimpleDateFormat yearF = new SimpleDateFormat("MM月dd");
            String newD = yearF.format(date) + "日 ";
            SimpleDateFormat yearD = new SimpleDateFormat("HH:mm");
            timeStr = newD + CommonlyUtil.getWeek(date, mContext) + " " + yearD.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        helper.setText(R.id.item_receive_tv, timeStr);
    }
}
