package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;

/**
 * Created by W·H·K on 2019/2/14.
 */
public class VideoPlaybackAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReviewVideoInfo> mCourseListInfos;
    private OnItemVideoPlayListener mOnItemVideoPlayListener;

    public VideoPlaybackAdapter(Context context, List<ReviewVideoInfo> list) {
        mContext = context;
        mCourseListInfos = list;
    }

    public void setOnItemVideoPlayListener(OnItemVideoPlayListener listener) {
        this.mOnItemVideoPlayListener = listener;
    }

    public void setData(List<ReviewVideoInfo> list) {
        mCourseListInfos.clear();
        mCourseListInfos = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCourseListInfos.size();
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
            convertView = View.inflate(mContext, R.layout.item_rounds_video_playback, null);
            vh = new ViewHolder();
            vh.videoPlayView = (ImageView) convertView.findViewById(R.id.item_rounds_video_play_iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        displayView(vh, position);
        return convertView;
    }

    private void displayView(ViewHolder vh, final int position) {
        vh.videoPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemVideoPlayListener != null) {
                    mOnItemVideoPlayListener.onItemVideoPlayListener(v, position);
                }
            }
        });
    }

    class ViewHolder {
        ImageView videoPlayView;
    }

    public interface OnItemVideoPlayListener {
        void onItemVideoPlayListener(View view, int position);
    }
}
