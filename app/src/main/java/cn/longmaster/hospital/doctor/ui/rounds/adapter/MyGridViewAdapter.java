package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;

/**
 * Created by W·H·K on 2018/5/9.
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;
    private OnTimeItemClickListener mOnTimeItemClickListener;

    public MyGridViewAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    public void setOnTimeItemClickListener(OnTimeItemClickListener listener) {
        mOnTimeItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recommend_layout, null);
            vh = new ViewHolder();
            vh.timeTv = (TextView) convertView.findViewById(R.id.item_recommend_text);
            vh.itemView = (RelativeLayout) convertView.findViewById(R.id.item_time_view);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        displayView(vh, position);
        return convertView;
    }

    private void displayView(ViewHolder vh, final int position) {
        try {
            String timeStr = mList.get(position);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            final Date date = format.parse(timeStr);//有异常要捕获
            vh.timeTv.setText(getWeek(date) + " " + getHour(date));
            Logger.logI(Logger.COMMON, "shwDatePopupWindow-MyGridViewAdapter：时间转换" + getWeek(date) + " " + getHour(date));
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTimeItemClickListener != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                        mOnTimeItemClickListener.onTimeItemClickListener(position, week_index);
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getHour(Date date) {
        SimpleDateFormat yearF = new SimpleDateFormat("HH");
        String newD = yearF.format(date);
        String titleText = "";
        if (CommonlyUtil.getMorningTimeList().contains(newD)) {
            titleText = mContext.getString(R.string.morning);
        } else if (CommonlyUtil.getNoonTimeList().contains(newD)) {
            titleText = mContext.getString(R.string.noon);
        } else if (CommonlyUtil.getAfternoonTimeList().contains(newD)) {
            titleText = mContext.getString(R.string.afternoon);
        } else if (CommonlyUtil.getOtherTimeList().contains(newD)) {
            titleText = mContext.getString(R.string.other);
        }
        return titleText;
    }

    public String getWeek(Date date) {
        String[] weeks = mContext.getResources().getStringArray(R.array.week);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    class ViewHolder {
        TextView timeTv;
        RelativeLayout itemView;
    }

    public interface OnTimeItemClickListener {
        void onTimeItemClickListener(int position, int week);
    }
}


