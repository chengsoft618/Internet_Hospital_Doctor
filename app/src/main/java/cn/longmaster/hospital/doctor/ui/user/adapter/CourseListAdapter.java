package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectCourseTypeInfo;

public class CourseListAdapter extends BaseExpandableListAdapter {
    private List<ProjectCourseTypeInfo> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public CourseListAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onChildItemClickListener) {
        mOnItemClickListener = onChildItemClickListener;
    }

    @Override
    public int getGroupCount() {

        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getCourseContentInfos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getCourseContentInfos().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setData(List<ProjectCourseTypeInfo> hospitalRankInfoList) {
        mList.clear();
        mList.addAll(hospitalRankInfoList);
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupVieHolder groupVieHolder;
        if (convertView == null) {
            groupVieHolder = new GroupVieHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_course_list_group, null);
            groupVieHolder.groupTv = (TextView) convertView.findViewById(R.id.fragment_home_department_title_tv);
            groupVieHolder.groupImg = (ImageView) convertView.findViewById(R.id.fragment_home_department_detail_icon_iv);
            convertView.setTag(groupVieHolder);
        } else {
            groupVieHolder = (GroupVieHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupVieHolder.groupImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_course_list_arrow_open));
        } else {
            groupVieHolder.groupImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_expandable_list_view_arrow_close));
        }
        groupVieHolder.groupTv.setText(mList.get(groupPosition).getCourseType());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_course_list_son, null);
            childViewHolder.courseNameTv = (TextView) convertView.findViewById(R.id.item_course_list_children_name_iv);
            childViewHolder.courseAppointmentTv = (TextView) convertView.findViewById(R.id.item_course_list_children_iv);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ProjectCourseTypeInfo projectCourseTypeInfo = mList.get(groupPosition);
        childViewHolder.courseNameTv.setText(projectCourseTypeInfo.getCourseContentInfos().get(childPosition).getCourseName() + "(" +
                (projectCourseTypeInfo.getCourseContentInfos().get(childPosition).getAppointNum() +
                        "/" + projectCourseTypeInfo.getCourseContentInfos().get(childPosition).getClassNum()) + ")");
        childViewHolder.courseAppointmentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClicked(projectCourseTypeInfo, groupPosition, childPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupVieHolder {
        TextView groupTv;
        ImageView groupImg;
    }

    private class ChildViewHolder {
        TextView courseNameTv;
        TextView courseAppointmentTv;
    }

    public interface OnItemClickListener {
        void onItemClicked(ProjectCourseTypeInfo hospitalRankInfo, int groupPosition, int childPosition);
    }
}
