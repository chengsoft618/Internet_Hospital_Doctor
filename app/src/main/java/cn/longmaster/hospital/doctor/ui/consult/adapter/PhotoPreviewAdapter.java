package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import cn.longmaster.doctorlibrary.util.photo.Photo;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 图片多选预览适配器
 * Created by Yang² on 2016/11/18.
 * Mod by biao on 2019/8/17
 */

public class PhotoPreviewAdapter extends PagerAdapter {
    private Context mContext;
    private List<Photo> mPhoneList;

    public PhotoPreviewAdapter(Context context, List<Photo> list) {
        mContext = context;
        mPhoneList = list;
    }

    @Override
    public int getCount() {
        return mPhoneList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(mContext);
        photoView.setImageResource(R.drawable.ic_loading_pic);
        GlideUtils.showImage(photoView, mContext, mPhoneList.get(position).getPath());
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
