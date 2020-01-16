package cn.longmaster.hospital.doctor.ui.college.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by W·H·K on 2018/3/19.
 */
public class VideoListAdapter extends BaseQuickAdapter<CourseListInfo, BaseViewHolder> {

    public VideoListAdapter(int layoutResId, @Nullable List<CourseListInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseListInfo item) {
        CircleImageView avatar = helper.getView(R.id.item_video_list_ic);
        GlideUtils.showDoctorAvatar(avatar, mContext, AvatarUtils.getAvatar(false, item.getDoctorId(), "0"));
        helper.setText(R.id.item_video_list_name, item.getDoctorName());
        helper.setText(R.id.item_video_list_department, item.getDepartmentName());
        helper.setText(R.id.item_video_list_title, item.getHospitalName());
        helper.setText(R.id.item_video_list_likes, " " + item.getPlayTotal());
        helper.setText(R.id.item_video_list_introduce, item.getCourseTitle());

        String thumbnail = item.getThumbnail();
        String str = thumbnail.replace("/", ".");
        String url = AppConfig.getDfsUrl() + "3031/0/" + str;
        Logger.logI(Logger.COMMON, "VideoListAdapter->item：" + item + ",SdManager.getInstance().getCollege(thumbnail):" + SdManager.getInstance().getCollege(thumbnail) + ",url:" + url);
        ImageView videoCover = helper.getView(R.id.item_video_list_cover);
        GlideUtils.showCollegeCoverMap(videoCover, mContext, url);

        if (item.getIsRecommend() == 1) {
            helper.setVisible(R.id.item_video_list_recommend, true);
        } else {
            helper.setGone(R.id.item_video_list_recommend, false);
        }
    }
}
