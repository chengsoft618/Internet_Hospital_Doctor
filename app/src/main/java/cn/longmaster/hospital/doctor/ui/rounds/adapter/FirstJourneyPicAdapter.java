package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.LibCollections;

/**
 * @author ABiao_Abiao
 * @date 2019/12/4 17:30
 * @description:
 */
public class FirstJourneyPicAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mServerUrls;
    private OnPhotoTapListener mOnPhotoTapListener;

    public FirstJourneyPicAdapter(Context context, List<String> serverUrls) {
        mContext = context;
        mServerUrls = serverUrls;
    }

    public void addItem(String url) {
        mServerUrls.add(url);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return LibCollections.size(mServerUrls);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageResource(R.drawable.ic_loading_pic);
        GlideUtils.showImage(photoView, mContext, mServerUrls.get(position));
        if (mOnPhotoTapListener != null) {
            photoView.setOnPhotoTapListener(mOnPhotoTapListener);
        }
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mOnPhotoTapListener = listener;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
