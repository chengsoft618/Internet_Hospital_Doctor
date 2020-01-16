package cn.longmaster.hospital.doctor.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 图片浏览适配器
 * Created by yangyong on 15/8/25.
 */
public class PicMaterialAdapter extends PagerAdapter {
    private final String TAG = PicMaterialAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> mAuxilirayMaterialList;
    private OnPhotoTapListener mOnPhotoTapListener;

    public PicMaterialAdapter(Context context, List<String> materialList) {
        mContext = context;
        mAuxilirayMaterialList = materialList;
    }

    public void addItem(String info) {
        mAuxilirayMaterialList.add(info);
    }

    @Override
    public int getCount() {
        return mAuxilirayMaterialList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());
        GlideUtils.showBrowserImage(photoView, mContext, mAuxilirayMaterialList.get(position));
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
