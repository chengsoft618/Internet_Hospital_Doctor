package cn.longmaster.hospital.doctor.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;


/**
 * 图片浏览适配器
 * Created by yangyong on 15/8/25.
 *
 * @see cn.longmaster.hospital.doctor.ui.rounds.adapter.FirstJourneyPicAdapter
 * 不再使用localFilePaths 来做缓存
 */
@Deprecated
public class PicBrowseAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mLocalFilePaths;
    private ArrayList<String> mServerUrls;
    private OnPhotoTapListener mOnPhotoTapListener;

    public PicBrowseAdapter(Context context, ArrayList<String> localFilePaths, ArrayList<String> serverUrls) {
        mContext = context;
        mLocalFilePaths = localFilePaths;
        mServerUrls = serverUrls;
    }

    public void addItem(String path, String url) {
        mLocalFilePaths.add(path);
        mServerUrls.add(url);
    }

    @Override
    public int getCount() {
        return mLocalFilePaths.size();
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
