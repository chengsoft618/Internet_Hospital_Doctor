package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.utils.StringUtils;

/**
 * 消息adapter
 * Created by Yang² on 2016/5/30.
 */
public class MessageCenterAdapter extends BaseQuickAdapter<BaseMessageInfo, BaseViewHolder> {

    public MessageCenterAdapter(int layoutResId, @Nullable List<BaseMessageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseMessageInfo item) {
        if (MessageProtocol.MSG_STATE_READED == item.getMsgState()) {
            helper.setImageResource(R.id.item_message_icon_iv, R.drawable.ic_message_read);
            helper.setTextColor(R.id.item_message_title_tv, ContextCompat.getColor(mContext, R.color.color_b2b2b2));
            helper.setTextColor(R.id.item_message_content_tv, ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        } else {
            helper.setImageResource(R.id.item_message_icon_iv, R.drawable.ic_message_unread);
            helper.setTextColor(R.id.item_message_title_tv, ContextCompat.getColor(mContext, R.color.color_333333));
            helper.setTextColor(R.id.item_message_content_tv, ContextCompat.getColor(mContext, R.color.color_333333));
        }
        if (DateUtil.isSameDay(item.getSendDt() * 1000, System.currentTimeMillis())) {
            helper.setText(R.id.item_message_time_tv, DateUtil.millisecondToFormatDate("HH:mm", item.getSendDt() * 1000));
        } else {
            helper.setText(R.id.item_message_time_tv, DateUtil.millisecondToFormatDate("yyyy-MM-dd HH:mm", item.getSendDt() * 1000));
        }
        disPlayMsg(helper, item);
        helper.addOnLongClickListener(R.id.item_message_layout_ll);
        helper.addOnClickListener(R.id.item_message_layout_ll);
    }

    private void disPlayMsg(BaseViewHolder helper, final BaseMessageInfo baseMessageInfo) {
        try {
            JSONObject json = new JSONObject(baseMessageInfo.getMsgContent());
            switch (baseMessageInfo.getMsgType()) {
                case MessageProtocol.SMS_TYPE_DOCTOR_BALANCE_CHANGE://83 余额变更
                    helper.setText(R.id.item_message_content_tv, R.string.message_balance_change_tips);
                    if (json.optInt("ar") == MessageProtocol.SMS_BALANCE_CHANGE_WITHDRAW || json.optInt("ar") == MessageProtocol.SMS_BALANCE_CHANGE_DOCTOR_WITHDRAW) {
                        helper.setText(R.id.item_message_title_tv, R.string.message_withdraw);
                        helper.setText(R.id.item_message_content_tv, R.string.message_withdraw_tips);
                    } else {
                        helper.setText(R.id.item_message_title_tv, R.string.message_balance_change);
                        helper.setText(R.id.item_message_content_tv, R.string.message_balance_change_tips);
                    }
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_APPOINTMENT_TODAY://84 今日预约
                    helper.setText(R.id.item_message_title_tv, R.string.message_appointment_today);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.message_appointment_today_tips, json.optInt("ac") + ""));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_39_ACTIVITY://30 39活动
                    helper.setText(R.id.item_message_title_tv, R.string.message_39_activity);
                    helper.setText(R.id.item_message_content_tv, json.optString("c"));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS://分诊
                case MessageProtocol.SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS://
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_waiting_diagnosis);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_diagnosis, decodeUnicode(json.getString("hn")), decodeUnicode(json.getString("dpn"))));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_FAST_DIAGNOSIS://尽快分诊
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_possible_diagnosis);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_possible_diagnosis, decodeUnicode(json.getString("hn"))));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH://分诊完成
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_finish_diagnosis);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_finish_diagnosis, decodeUnicode(json.getString("hn")), decodeUnicode(json.getString("dpn"))));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE://等待接诊
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_wit_receive);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_have_new_order, json.getInt("aid") + ""));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH://接诊完成：接诊医生
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_finish_receive);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_receive, json.getInt("aid") + ",", decodeUnicode(json.getString("dt"))));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH://接诊完成：发起医生
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_finish_receive);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_expert_receive, json.getInt("aid") + "", decodeUnicode(json.getString("dt"))));
                    break;
                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE://发起医生关联代表。等待接诊
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_message_title_wit_receive);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_representative_wait_receive_content, decodeUnicode(json.getString("dcn")) + "", json.getInt("aid") + ""));
                    break;
                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE://发起医生关联代表。等待分诊
                    helper.setText(R.id.item_message_title_tv, R.string.rounds_waiting_diagnosis);
                    helper.setText(R.id.item_message_content_tv, mContext.getString(R.string.rounds_message_representative_wait_diagnosis_content, decodeUnicode(json.getString("dcn")) + "", json.getInt("aid") + ""));
                    break;
                case MessageProtocol.SMS_TYPE_MATERIAL_UPLOAD_NOTE:
                    helper.setText(R.id.item_message_title_tv, "材料上传提醒");
                    helper.setText(R.id.item_message_content_tv, "就诊编号为：" + json.getString("aid") + "的【远程查房】中，未发现病例" + createMedicalId(json.getString("mid")) + "的病案首页，请尽快处理！");
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 【21213】、【12133】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】、【13131】
     *
     * @param medicalIds
     * @return
     */
    private String createMedicalId(String medicalIds) {
        StringBuilder medicalIdSb = new StringBuilder();
        List<String> medicalIdList = StringUtils.str2ArrayList(medicalIds);
        for (String medicalId : medicalIdList) {
            medicalIdSb.append("【");
            medicalIdSb.append(medicalId);
            medicalIdSb.append("】、");
        }
        return StringUtils.substringBeforeLast(medicalIdSb.toString(), "、");
    }

    private String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }
}
