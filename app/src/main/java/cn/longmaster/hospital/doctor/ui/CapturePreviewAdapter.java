package cn.longmaster.hospital.doctor.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.imageloader.ImageLoadOptions;
import cn.longmaster.doctorlibrary.util.imageloader.ImageLoadSize;
import cn.longmaster.doctorlibrary.util.imageloader.ImageScaleType;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 相机拍摄预览adapter
 * Created by Yang² on 2017/9/22.
 */

public class CapturePreviewAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mPhotoList;

    public CapturePreviewAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mPhotoList = list;
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(mContext);
        photoView.setImageResource(R.drawable.ic_loading_pic);
        ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(mPhotoList.get(position), null);
        builder.setDiskCacheEnable(false);
        builder.setMemoryCacheEnable(false);
        ImageLoadSize imageLoadSize = new ImageLoadSize((int) (ScreenUtil.getScreenWidth() * 1.25), (int) (ScreenUtil.getScreenHeight() * 1.25), ImageScaleType.CENTER_INSIDE);
        builder.setImageLoadSize(imageLoadSize);
        if (mPhotoList.size() > position && mPhotoList.get(position) != null) {
            GlideUtils.showImage(photoView, mContext, mPhotoList.get(position));
        }
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
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
