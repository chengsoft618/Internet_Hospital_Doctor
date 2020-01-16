package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorItemInfo;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 选择医生Adapter
 * Created by Yang² on 2016/6/8.
 * mod by biao on 2019/6/11
 */
public class DoctorListNewAdapter extends BaseQuickAdapter<DoctorItemInfo, BaseViewHolder> {
    public DoctorListNewAdapter(int layoutResId, @Nullable List<DoctorItemInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, DoctorItemInfo item) {
        CircleImageView civAvatar = helper.getView(R.id.item_doctor_list_photo_iv);
        GlideUtils.showDoctorAvatar(civAvatar, mContext, AvatarUtils.getAvatar(false, item.getDoctorId(), item.getAvaterToken()));
        if (StringUtils.isTrimEmpty(item.getDoctorSkill())) {
            helper.setText(R.id.item_doctor_list_good_desc_tv, "暂无");
        } else {
            helper.setText(R.id.item_doctor_list_good_desc_tv, item.getDoctorSkill());
        }
        helper.setText(R.id.item_doctor_list_name_tv, item.getRealName());
        helper.setText(R.id.item_doctor_list_level_tv, item.getDoctorLevel());
        helper.setText(R.id.item_doctor_list_department_tv, item.getDepartmentName());
        helper.setText(R.id.item_doctor_list_hospital_tv, item.getHospitalName());
        if (item.getDoctorId() > 0) {
            helper.setGone(R.id.item_doctor_list_no_enter_tv, false);
            helper.setVisible(R.id.item_doctor_list_score_desc_tv, true);
            helper.setVisible(R.id.item_doctor_list_score_tv, true);
            helper.setVisible(R.id.item_doctor_list_diagnosis, true);
            helper.setVisible(R.id.item_doctor_list_diagnosis_times, true);
            if (item.getReceptionNum() >= 0) {
                helper.setText(R.id.item_doctor_list_diagnosis_times, item.getReceptionNum() + "例");
            } else {
                helper.setText(R.id.item_doctor_list_diagnosis_times, "暂无");
            }
            if (item.getTotalScore() > 0) {
                helper.setText(R.id.item_doctor_list_score_desc_tv, "" + item.getTotalScore());
            } else {
                helper.setText(R.id.item_doctor_list_score_desc_tv, "暂无");
            }

        } else {
            helper.setVisible(R.id.item_doctor_list_no_enter_tv, true);
            helper.setGone(R.id.item_doctor_list_score_desc_tv, false);
            helper.setGone(R.id.item_doctor_list_score_tv, false);
            helper.setGone(R.id.item_doctor_list_diagnosis, false);
            helper.setGone(R.id.item_doctor_list_diagnosis_times, false);
        }

        if (StringUtils.isTrimEmpty(item.getReceptionDt())) {
            helper.setGone(R.id.item_doctor_list_visit_time_ll, false);
        } else {
            helper.setVisible(R.id.item_doctor_list_visit_time_ll, true);
            TextView itemDoctorListVisitTime1aTv = helper.getView(R.id.item_doctor_list_visit_time_1a_tv);
            TextView itemDoctorListVisitTime1pTv = helper.getView(R.id.item_doctor_list_visit_time_1p_tv);
            TextView itemDoctorListVisitTime2aTv = helper.getView(R.id.item_doctor_list_visit_time_2a_tv);
            TextView itemDoctorListVisitTime2pTv = helper.getView(R.id.item_doctor_list_visit_time_2p_tv);
            TextView itemDoctorListVisitTime3aTv = helper.getView(R.id.item_doctor_list_visit_time_3a_tv);
            TextView itemDoctorListVisitTime3pTv = helper.getView(R.id.item_doctor_list_visit_time_3p_tv);
            TextView itemDoctorListVisitTime4aTv = helper.getView(R.id.item_doctor_list_visit_time_4a_tv);
            TextView itemDoctorListVisitTime4pTv = helper.getView(R.id.item_doctor_list_visit_time_4p_tv);
            TextView itemDoctorListVisitTime5aTv = helper.getView(R.id.item_doctor_list_visit_time_5a_tv);
            TextView itemDoctorListVisitTime5pTv = helper.getView(R.id.item_doctor_list_visit_time_5p_tv);
            initVisitState(itemDoctorListVisitTime1aTv);
            initVisitState(itemDoctorListVisitTime1pTv);
            initVisitState(itemDoctorListVisitTime2aTv);
            initVisitState(itemDoctorListVisitTime2pTv);
            initVisitState(itemDoctorListVisitTime3aTv);
            initVisitState(itemDoctorListVisitTime3pTv);
            initVisitState(itemDoctorListVisitTime4aTv);
            initVisitState(itemDoctorListVisitTime4pTv);
            initVisitState(itemDoctorListVisitTime5aTv);
            initVisitState(itemDoctorListVisitTime5pTv);
            List<String> isRecommendList = StringUtils.str2ArrayList(item.getIsRecommend());
            List<String> receptionDtList = StringUtils.str2ArrayList(item.getReceptionDt());
            if (LibCollections.isNotEmpty(receptionDtList)) {
                for (int i = 0; i < receptionDtList.size(); i++) {
                    boolean isRecommend = !StringUtils.equals("0", isRecommendList.get(i));
                    switch (receptionDtList.get(i)) {
                        case "1a":
                            showVisitState(itemDoctorListVisitTime1aTv, isRecommend, false);
                            break;
                        case "1p":
                            showVisitState(itemDoctorListVisitTime1pTv, isRecommend, false);
                            break;
                        case "2a":
                            showVisitState(itemDoctorListVisitTime2aTv, isRecommend, false);
                            break;
                        case "2p":
                            showVisitState(itemDoctorListVisitTime2pTv, isRecommend, false);
                            break;
                        case "3a":
                            showVisitState(itemDoctorListVisitTime3aTv, isRecommend, false);
                            break;
                        case "3p":
                            showVisitState(itemDoctorListVisitTime3pTv, isRecommend, false);
                            break;
                        case "4a":
                            showVisitState(itemDoctorListVisitTime4aTv, isRecommend, false);
                            break;
                        case "4p":
                            showVisitState(itemDoctorListVisitTime4pTv, isRecommend, false);
                            break;
                        case "5a":
                            showVisitState(itemDoctorListVisitTime5aTv, isRecommend, false);
                            break;
                        case "5p":
                            showVisitState(itemDoctorListVisitTime5pTv, isRecommend, true);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        FlexboxLayout fblRecommendMeet = helper.getView(R.id.item_doctor_list_recommend_desc_fbl);
        //接诊类型
        if (StringUtils.isTrimEmpty(item.getRecommendMeet()) || StringUtils.equals(",", item.getRecommendMeet())) {
            fblRecommendMeet.setVisibility(View.GONE);
            helper.setVisible(R.id.item_doctor_list_recommend_desc_no_tv, true);
        } else {
            fblRecommendMeet.setVisibility(View.VISIBLE);
            helper.setGone(R.id.item_doctor_list_recommend_desc_no_tv, false);
            fblRecommendMeet.removeAllViews();
            String recommendMeet;
            for (String recommendMeetId : StringUtils.str2ArrayList(item.getRecommendMeet())) {
                // TODO: 2019/6/12 创建处理类
                switch (recommendMeetId) {
                    case "1":
                        recommendMeet = "教学查房";
                        break;
                    case "2":
                        recommendMeet = "批量影像";
                        break;
                    case "3":
                        recommendMeet = "远程会诊";
                        break;
                    case "4":
                        recommendMeet = "远程门诊";
                        break;
                    case "5":
                        recommendMeet = "实地就诊";
                        break;
                    default:
                        recommendMeet = "";
                }
                if (!StringUtils.isTrimEmpty(recommendMeet)) {
                    View view = mLayoutInflater.inflate(R.layout.item_fbl_doctor_list_recommend_meet_label, null);
                    TextView textView = view.findViewById(R.id.item_doctor_list_recommend_label_tv);
                    textView.setText(recommendMeet);
                    fblRecommendMeet.addView(view);
                }
            }
        }
        //医生标签
        FlexboxLayout label = helper.getView(R.id.item_doctor_list_mark_desc_fbl);
        label.removeAllViews();
        if (LibCollections.isNotEmpty(item.getLabelData())) {
            label.setVisibility(View.VISIBLE);
            helper.setGone(R.id.item_doctor_list_mark_desc_no_tv, false);
            for (DoctorItemInfo.LabeData labeData : item.getLabelData()) {
                View view = mLayoutInflater.inflate(R.layout.item_fbl_doctor_list_feed_back_label, null);
                TextView textView = view.findViewById(R.id.item_doctor_list_feed_back_label_tv);
                textView.setText(labeData.getLabelName());
                label.addView(view);
            }
        } else {
            helper.setVisible(R.id.item_doctor_list_mark_desc_no_tv, true);
            label.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.item_doctor_list_photo_iv)
                .addOnClickListener(R.id.item_doctor_list_name_tv);
    }

    private void initVisitState(TextView tv) {
        int defaultBg = ContextCompat.getColor(mContext, R.color.transparent);
        int defaultTextColor = ContextCompat.getColor(mContext, R.color.color_white);
        tv.setTextColor(defaultTextColor);
        tv.setCompoundDrawables(null, null, null, null);
        tv.setBackgroundColor(defaultBg);
    }

    private void showVisitState(TextView textView, boolean isRecommend, boolean isLast) {
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_049eff));
        if (isLast) {
            textView.setBackgroundResource(R.drawable.bg_solid_e5f1ff_br_5);
        } else {
            textView.setBackgroundResource(R.color.color_e5f1ff);
        }
        if (isRecommend) {
            Drawable recommend = ContextCompat.getDrawable(mContext, R.mipmap.ic_doctor_list_has_time_recommend);
            recommend.setBounds(0, 0, DisplayUtil.dp2px(10), DisplayUtil.dp2px(19));
            textView.setCompoundDrawables(null, null, recommend, null);
        }
    }
}
