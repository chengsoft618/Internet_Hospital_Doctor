package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AssessInfo;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.GetAssessInfoRequester;

/**
 * 我的评估
 * Created by yangyong on 2019-07-16.
 */
public class MyAssessAdapter extends BaseQuickAdapter<BaseMessageInfo, BaseViewHolder> {
    private Map<String, AssessInfo> assessInfoMap = new HashMap<>();

    public MyAssessAdapter(int layoutResId, @Nullable List<BaseMessageInfo> data) {
        super(layoutResId, data);
    }

    public AssessInfo getAssessInfo(BaseMessageInfo messageInfo) {
        return assessInfoMap.get(messageInfo.getAppointmentId() + "_" + messageInfo.getAssessUid());
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseMessageInfo item) {
        if (item.getMsgState() == MessageProtocol.MSG_STATE_READED) {
            helper.setTextColor(R.id.item_my_assess_course_name_tv, mContext.getResources().getColor(R.color.color_666666));
            helper.setTextColor(R.id.item_my_assess_stage_serial_tv, mContext.getResources().getColor(R.color.color_666666));
            helper.setBackgroundRes(R.id.item_my_assess_stage_serial_tv, R.drawable.bg_assess_stage_serial_gray);
            helper.setTextColor(R.id.item_my_assess_doctor_name_tv, mContext.getResources().getColor(R.color.color_666666));

        } else {
            helper.setTextColor(R.id.item_my_assess_course_name_tv, mContext.getResources().getColor(R.color.color_333333));
            helper.setTextColor(R.id.item_my_assess_stage_serial_tv, mContext.getResources().getColor(R.color.color_45aef8));
            helper.setBackgroundRes(R.id.item_my_assess_stage_serial_tv, R.drawable.bg_assess_stage_serial_blue);
            helper.setTextColor(R.id.item_my_assess_doctor_name_tv, mContext.getResources().getColor(R.color.color_333333));
        }

        if (DateUtil.isSameYear(item.getSendDt() * 1000, System.currentTimeMillis())) {
            helper.setText(R.id.item_my_assess_date_tv, DateUtil.millisecondToFormatDate("MM-dd HH:mm", item.getSendDt() * 1000));
        } else {
            helper.setText(R.id.item_my_assess_date_tv, DateUtil.millisecondToFormatDate("yyyy-MM-dd HH:mm", item.getSendDt() * 1000));
        }

        if (assessInfoMap.containsKey(item.getAppointmentId() + "_" + item.getAssessUid())) {
            if (null != assessInfoMap.get(item.getAppointmentId() + "_" + item.getAssessUid())) {
                displayAssessInfo(helper, assessInfoMap.get(item.getAppointmentId() + "_" + item.getAssessUid()));
            }
        } else {
            assessInfoMap.put(item.getAppointmentId() + "_" + item.getAssessUid(), null);
            getAssessInfo(helper, item);
        }
    }

    private void getAssessInfo(BaseViewHolder helper, BaseMessageInfo item) {
        GetAssessInfoRequester requester = new GetAssessInfoRequester(new OnResultListener<AssessInfo>() {
            @Override
            public void onResult(BaseResult baseResult, AssessInfo assessInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS && null != assessInfo) {
                    assessInfoMap.put(item.getAppointmentId() + "_" + item.getAssessUid(), assessInfo);
                    displayAssessInfo(helper, assessInfo);
                } else {
                    assessInfoMap.remove(item.getAppointmentId() + "_" + item.getAssessUid());
                }
            }
        });
        requester.orderId = item.getAppointmentId();
        requester.attDocId = item.getAssessUid();
        requester.doPost();
    }

    private void displayAssessInfo(BaseViewHolder helper, AssessInfo assessInfo) {
        helper.setText(R.id.item_my_assess_course_name_tv, assessInfo.getCourseName());
        helper.setText(R.id.item_my_assess_stage_serial_tv, assessInfo.getStageSerial());
        helper.setText(R.id.item_my_assess_doctor_name_tv, assessInfo.getDoctorName());
        helper.setText(R.id.item_my_assess_attending_doctor_name_tv, assessInfo.getAttDocName());
        helper.setText(R.id.item_my_assess_direction_tv, assessInfo.getDirection());
    }
}
