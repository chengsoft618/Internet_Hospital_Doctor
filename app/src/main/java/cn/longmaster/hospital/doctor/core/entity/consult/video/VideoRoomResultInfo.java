package cn.longmaster.hospital.doctor.core.entity.consult.video;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频聊天室回调数据
 * Created by yangyong on 2015/7/28.
 */
public class VideoRoomResultInfo implements Serializable {
    public static final String RESULT_KEY_ROOMID = "_roomID";
    public static final String RESULT_KEY_CHANNELID = "_channelID";
    public static final String RESULT_KEY_VCODEC = "_vcodec";
    public static final String RESULT_KEY_ACODEC = "_acodec";
    public static final String RESULT_KEY_PROTOCOLID = "_protocolID";
    public static final String RESULT_KEY_VIDEOPORT = "_videoPort";
    public static final String RESULT_KEY_VIDEOSSRC = "_videoSsrc";
    public static final String RESULT_KEY_VIDEORTPPT = "_videoRTPPT";
    public static final String RESULT_KEY_AUDIOPORT = "_audioPort";
    public static final String RESULT_KEY_AUDIOSSRC = "_audioSsrc";
    public static final String RESULT_KEY_AUDIORTPPT = "_audioRTPPT";
    public static final String RESULT_KEY_MEMBERNUM = "_memberNum";
    public static final String RESULT_KEY_MEMBERLIST = "_memberList";
    public static final String RESULT_KEY_PVSIP = "_pvsIP";
    public static final String RESULT_KEY_AVTYPE = "_avType";
    public static final String RESULT_KEY_USERID = "_userID";
    public static final String RESULT_KEY_USERNAME = "_userName";
    public static final String RESULT_KEY_USERTYPE = "_userType";
    public static final String RESULT_KEY_MSG = "_msg";
    public static final String RESULT_KEY_SHUTDOWNREASON = "_shutdownReason";
    public static final String RESULT_KEY_RESERVEID = "_reserveID";
    public static final String RESULT_KEY_STARTDT = "_startDT";
    public static final String RESULT_KEY_ENDDT = "_endDT";
    public static final String RESULT_KEY_RESERVED = "_reserved";
    public static final String RESULT_KEY_SUBSCRIBER_ID = "_subscriberID";
    public static final String RESULT_KEY_PUBLISHER_ID = "_publisherID";
    public static final String RESULT_KEY_OP_TYPE = "_opType";
    public static final String RESULT_KEY_FORBID_TYPE = "_forbidType";
    public static final String RESULT_KEY_SEPARATE_TYPE = "_separateType";
    public static final String RESULT_KEY_SSRCLIST = "_list";
    public static final String RESULT_KEY_SSRC = "ssrc";
    public static final String RESULT_KEY_UID = "uid";
    public static final String RESULT_KEY_USER_SEAT = "_userSeat";
    public static final String RESULT_KEY_SETSTATE = "_seatState";
    public static final String RESULT_KEY_STATUS = "_status";
    public static final String RESULT_KEY_OPERATION = "_operation";
    public static final String RESULT_KEY_TYPE = "type";
    public static final String RESULT_KEY_VIDEO_TYPE = "_type";
    public static final String RESULT_KEY_SEQ_ID = "_seqId";
    public static final String RESULT_KEY_DURATION = "_duration";
    public static final String RESULT_KEY_PVS_URL = "_pvsUrl";
    public static final String RESULT_KEY_PCS_URL = "_pcsUrl";
    public static final String RESULT_KEY_MSG_CONTENT = "_msgContent";
    private int roomID;//房间ID
    private long channelID;//频道ID
    private byte vcodec;//视频编码
    private byte acodec;//音频编码
    private long protocolID;//协议ID
    private int videoPort;//接收视频数据包的端口
    private int videoSsrc;//视频数据的SSRC
    private byte videoRTPPT;//	视频编码格式的RTP payload类型
    private int audioPort;//接收音频数据包的端口
    private int audioSsrc;//音频数据的SSRC
    private byte audioRTPPT;//音频编码数据格式的RTP payload类型
    private byte memberNum;//成员列表大小
    private List<VideoRoomMember> memberList;//成员列表;
    private String pvsIP;//Pvs的IP地址
    private byte avType;//音视频标志
    private int userID;//用户id
    private String userName;//用户姓名
    private byte userType;//用户类型0、患者；1、医生
    private int forbidTypeMember;
    private int separateTypeMember;
    private String msg;//用户名称
    //消息内容
    private String msgContent;
    private byte shutdownReason;//原因
    private int reserveID;//预约ID
    private long startDT;//开始时间
    private long endDT;//结束时间
    private int subscriberID;//订阅者ID
    private int publisherID;//发布者ID
    private int opType;
    private int forbidTypeSelf;
    private int separateTypeSelf;
    private List<VideoInfo> ssrcList;
    private byte userSeat;
    private int seatSate;
    private int operation;
    private int duration;
    private long seqId;
    private String pvsUrl;
    private String pcsUrl;
    private int videoType;//0摄像头 1同屏 2播放视频文件，设置或取消主屏的时候用

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public long getChannelID() {
        return channelID;
    }

    public void setChannelID(long channelID) {
        this.channelID = channelID;
    }

    public byte getVcodec() {
        return vcodec;
    }

    public void setVcodec(byte vcodec) {
        this.vcodec = vcodec;
    }

    public byte getAcodec() {
        return acodec;
    }

    public void setAcodec(byte acodec) {
        this.acodec = acodec;
    }

    public long getProtocolID() {
        return protocolID;
    }

    public void setProtocolID(long protocolID) {
        this.protocolID = protocolID;
    }

    public int getVideoPort() {
        return videoPort;
    }

    public void setVideoPort(int videoPort) {
        this.videoPort = videoPort;
    }

    public int getVideoSsrc() {
        return videoSsrc;
    }

    public void setVideoSsrc(int videoSsrc) {
        this.videoSsrc = videoSsrc;
    }

    public byte getVideoRTPPT() {
        return videoRTPPT;
    }

    public void setVideoRTPPT(byte videoRTPPT) {
        this.videoRTPPT = videoRTPPT;
    }

    public int getAudioPort() {
        return audioPort;
    }

    public void setAudioPort(int audioPort) {
        this.audioPort = audioPort;
    }

    public int getAudioSsrc() {
        return audioSsrc;
    }

    public void setAudioSsrc(int audioSsrc) {
        this.audioSsrc = audioSsrc;
    }

    public byte getAudioRTPPT() {
        return audioRTPPT;
    }

    public void setAudioRTPPT(byte audioRTPPT) {
        this.audioRTPPT = audioRTPPT;
    }

    public byte getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(byte memberNum) {
        this.memberNum = memberNum;
    }

    public List<VideoRoomMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<VideoRoomMember> memberList) {
        this.memberList = memberList;
    }

    public String getPvsIP() {
        return pvsIP;
    }

    public void setPvsIP(String pvsIP) {
        this.pvsIP = pvsIP;
    }

    public byte getAvType() {
        return avType;
    }

    public void setAvType(byte avType) {
        this.avType = avType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte getUserType() {
        return userType;
    }

    public void setUserType(byte userType) {
        this.userType = userType;
    }

    public int getForbidTypeMember() {
        return forbidTypeMember;
    }

    public void setForbidTypeMember(int forbidTypeMember) {
        this.forbidTypeMember = forbidTypeMember;
    }

    public int getSeparateTypeMember() {
        return separateTypeMember;
    }

    public void setSeparateTypeMember(int separateTypeMember) {
        this.separateTypeMember = separateTypeMember;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public byte getShutdownReason() {
        return shutdownReason;
    }

    public void setShutdownReason(byte shutdownReason) {
        this.shutdownReason = shutdownReason;
    }

    public int getReserveID() {
        return reserveID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }

    public long getStartDT() {
        return startDT;
    }

    public void setStartDT(long startDT) {
        this.startDT = startDT;
    }

    public long getEndDT() {
        return endDT;
    }

    public void setEndDT(long endDT) {
        this.endDT = endDT;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getForbidTypeSelf() {
        return forbidTypeSelf;
    }

    public void setForbidTypeSelf(int forbidTypeSelf) {
        this.forbidTypeSelf = forbidTypeSelf;
    }

    public int getSeparateTypeSelf() {
        return separateTypeSelf;
    }

    public void setSeparateTypeSelf(int separateTypeSelf) {
        this.separateTypeSelf = separateTypeSelf;
    }

    public List<VideoInfo> getSsrcList() {
        return ssrcList;
    }

    public void setSsrcList(List<VideoInfo> ssrcList) {
        this.ssrcList = ssrcList;
    }

    public byte getUserSeat() {
        return userSeat;
    }

    public void setUserSeat(byte userSeat) {
        this.userSeat = userSeat;
    }

    public int getSeatSate() {
        return seatSate;
    }

    public void setSeatSate(int seatSate) {
        this.seatSate = seatSate;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getPvsUrl() {
        return pvsUrl;
    }

    public void setPvsUrl(String pvsUrl) {
        this.pvsUrl = pvsUrl;
    }

    public String getPcsUrl() {
        return pcsUrl;
    }

    public void setPcsUrl(String pcsUrl) {
        this.pcsUrl = pcsUrl;
    }

    @Override
    public String toString() {
        return "VideoRoomResultInfo{" +
                "roomID=" + roomID +
                ", channelID=" + channelID +
                ", vcodec=" + vcodec +
                ", acodec=" + acodec +
                ", protocolID=" + protocolID +
                ", videoPort=" + videoPort +
                ", videoSsrc=" + videoSsrc +
                ", videoRTPPT=" + videoRTPPT +
                ", audioPort=" + audioPort +
                ", audioSsrc=" + audioSsrc +
                ", audioRTPPT=" + audioRTPPT +
                ", memberNum=" + memberNum +
                ", memberList=" + memberList +
                ", pvsIP='" + pvsIP + '\'' +
                ", avType=" + avType +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", forbidTypeMember=" + forbidTypeMember +
                ", separateTypeMember=" + separateTypeMember +
                ", msg='" + msg + '\'' +
                ", shutdownReason=" + shutdownReason +
                ", reserveID=" + reserveID +
                ", startDT=" + startDT +
                ", endDT=" + endDT +
                ", subscriberID=" + subscriberID +
                ", publisherID=" + publisherID +
                ", opType=" + opType +
                ", forbidTypeSelf=" + forbidTypeSelf +
                ", separateTypeSelf=" + separateTypeSelf +
                ", ssrcList=" + ssrcList +
                ", userSeat=" + userSeat +
                ", seatSate=" + seatSate +
                ", operation=" + operation +
                ", duration=" + duration +
                ", seqId=" + seqId +
                ", videoType=" + videoType +
                '}';
    }

    public static VideoRoomResultInfo parseJson(String json) {
        VideoRoomResultInfo resultInfo = new VideoRoomResultInfo();
        try {
            if (TextUtils.isEmpty(json)) {
                return resultInfo;
            }
            JSONObject jsonObject = new JSONObject(json);
            resultInfo.setRoomID(jsonObject.optInt(RESULT_KEY_ROOMID, 0));
            resultInfo.setChannelID(jsonObject.optLong(RESULT_KEY_CHANNELID, 0));
            resultInfo.setVcodec((byte) jsonObject.optInt(RESULT_KEY_VCODEC, 0));
            resultInfo.setAcodec((byte) jsonObject.optInt(RESULT_KEY_ACODEC, 0));
            resultInfo.setProtocolID(jsonObject.optLong(RESULT_KEY_PROTOCOLID, 0));
            resultInfo.setVideoPort(jsonObject.optInt(RESULT_KEY_VIDEOPORT, 0));
            resultInfo.setVideoSsrc(jsonObject.optInt(RESULT_KEY_VIDEOSSRC, 0));
            resultInfo.setVideoRTPPT((byte) jsonObject.optInt(RESULT_KEY_VIDEORTPPT, 0));
            resultInfo.setAudioPort(jsonObject.optInt(RESULT_KEY_AUDIOPORT, 0));
            resultInfo.setAudioSsrc(jsonObject.optInt(RESULT_KEY_AUDIOSSRC, 0));
            resultInfo.setAudioRTPPT((byte) jsonObject.optInt(RESULT_KEY_AUDIORTPPT, 0));
            resultInfo.setMemberNum((byte) jsonObject.optInt(RESULT_KEY_MEMBERNUM, 0));
            resultInfo.setSubscriberID(jsonObject.optInt(RESULT_KEY_SUBSCRIBER_ID, 0));
            resultInfo.setPublisherID(jsonObject.optInt(RESULT_KEY_PUBLISHER_ID, 0));
            resultInfo.setMsgContent(jsonObject.optString(RESULT_KEY_MSG_CONTENT));
            List<VideoRoomMember> memberList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.optJSONArray(RESULT_KEY_MEMBERLIST);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject memberJsonObject = jsonArray.getJSONObject(i);
                    VideoRoomMember videoRoomMember = new VideoRoomMember();
                    videoRoomMember.setAvType((byte) memberJsonObject.optInt(RESULT_KEY_AVTYPE, 0));
                    videoRoomMember.setUserId(memberJsonObject.optInt(RESULT_KEY_USERID));
                    videoRoomMember.setUserName(memberJsonObject.optString(RESULT_KEY_USERNAME, ""));
                    videoRoomMember.setUserType(memberJsonObject.optInt(RESULT_KEY_USERTYPE, 0));
                    videoRoomMember.setForbidType(memberJsonObject.optInt(RESULT_KEY_FORBID_TYPE, 0));
                    videoRoomMember.setSeparateType(memberJsonObject.optInt(RESULT_KEY_SEPARATE_TYPE, 0));
                    videoRoomMember.setSeat((byte) memberJsonObject.optInt(RESULT_KEY_USER_SEAT, 0));
                    videoRoomMember.setStatus((byte) memberJsonObject.optInt(RESULT_KEY_STATUS, 0));
                    memberList.add(videoRoomMember);
                }
            }
            resultInfo.setMemberList(memberList);
            resultInfo.setPvsIP(jsonObject.optString(RESULT_KEY_PVSIP, ""));
            resultInfo.setAvType((byte) jsonObject.optInt(RESULT_KEY_AVTYPE, 0));
            resultInfo.setMsg(jsonObject.optString(RESULT_KEY_MSG, ""));
            resultInfo.setShutdownReason((byte) jsonObject.optInt(RESULT_KEY_SHUTDOWNREASON, 0));
            resultInfo.setReserveID(jsonObject.optInt(RESULT_KEY_RESERVEID, 0));
            resultInfo.setStartDT(jsonObject.optLong(RESULT_KEY_STARTDT, 0));
            resultInfo.setEndDT(jsonObject.optInt(RESULT_KEY_ENDDT, 0));
            resultInfo.setUserID(jsonObject.optInt(RESULT_KEY_USERID, 0));
            resultInfo.setSeqId(jsonObject.optLong(RESULT_KEY_SEQ_ID, 0));
            resultInfo.setDuration(jsonObject.optInt(RESULT_KEY_DURATION, 0));
            resultInfo.setUserName(jsonObject.optString(RESULT_KEY_USERNAME, ""));

            resultInfo.setOpType(jsonObject.optInt(RESULT_KEY_OP_TYPE, 0));
            String reserved = jsonObject.optString(RESULT_KEY_RESERVED, "");
            if (!TextUtils.isEmpty(reserved)) {
                JSONObject reservedJsonObject = new JSONObject(reserved);
                resultInfo.setUserType((byte) reservedJsonObject.optInt(RESULT_KEY_USERTYPE, 0));
                resultInfo.setForbidTypeSelf(reservedJsonObject.optInt(RESULT_KEY_FORBID_TYPE, 0));
                resultInfo.setSeparateTypeSelf(reservedJsonObject.optInt(RESULT_KEY_SEPARATE_TYPE, 0));
                resultInfo.setPvsUrl(reservedJsonObject.optString(RESULT_KEY_PVS_URL, ""));
                resultInfo.setPcsUrl(reservedJsonObject.optString(RESULT_KEY_PCS_URL, ""));
            } else {
                resultInfo.setUserType((byte) jsonObject.optInt(RESULT_KEY_USERTYPE, 0));
                resultInfo.setForbidTypeMember(jsonObject.optInt(RESULT_KEY_FORBID_TYPE, 0));
                resultInfo.setSeparateTypeMember(jsonObject.optInt(RESULT_KEY_SEPARATE_TYPE, 0));
            }
            List<VideoInfo> ssrcList = new ArrayList<>();
            JSONArray ssrcJsonArray = jsonObject.optJSONArray(RESULT_KEY_SSRCLIST);
            if (ssrcJsonArray != null && ssrcJsonArray.length() > 0) {
                for (int i = 0; i < ssrcJsonArray.length(); i++) {
                    JSONObject ssrcJsonObject = ssrcJsonArray.getJSONObject(i);
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setSsrc(ssrcJsonObject.optLong(RESULT_KEY_SSRC, 0));
                    videoInfo.setUseId(ssrcJsonObject.optInt(RESULT_KEY_UID, 0));
                    videoInfo.setType(ssrcJsonObject.optInt(RESULT_KEY_TYPE, 0));
                    ssrcList.add(videoInfo);
                }
            }
            resultInfo.setSsrcList(ssrcList);
            resultInfo.setUserSeat((byte) jsonObject.optInt(RESULT_KEY_USER_SEAT, 0));
            resultInfo.setSeatSate(jsonObject.optInt(RESULT_KEY_SETSTATE, 0));
            resultInfo.setOperation(jsonObject.optInt(RESULT_KEY_OPERATION, 0));
            resultInfo.setVideoType(jsonObject.optInt(RESULT_KEY_VIDEO_TYPE, 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultInfo;
    }
}
