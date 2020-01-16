package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectListInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/9/6 14:13
 * @description:
 */
public class ProjectAdapter extends BaseQuickAdapter<ProjectListInfo, BaseViewHolder> {
    public ProjectAdapter(int layoutResId, @Nullable List<ProjectListInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectListInfo item) {
        helper.setText(R.id.item_project_name_tv, item.getFullDepartmentName());
    }
}
