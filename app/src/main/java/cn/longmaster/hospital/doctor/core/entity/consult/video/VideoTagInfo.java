package cn.longmaster.hospital.doctor.core.entity.consult.video;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * 视频标识信息
 * Created by YY on 17/12/12.
 */

public class VideoTagInfo implements Serializable {
    //摄像头
    public static final int VIDEO_TYPE_CAMERA = 0;
    //同屏
    public static final int VIDEO_TYPE_SHARE = 1;
    //播放视频文件
    public static final int VIDEO_TYPE_VIDEO_FILE = 2;

    //用户id
    private int userId;
    //0摄像头 1同屏 2播放视频文件
    private int type;

    public VideoTagInfo(int userId, int type) {
        this.userId = userId;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, type);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VideoTagInfo) {
            return ((VideoTagInfo) obj).getType() == type && ((VideoTagInfo) obj).getUserId() == userId;
        }
        return false;
    }

    @Override
    public String toString() {
        return "VideoTagInfo{" +
                "userId=" + userId +
                ", type=" + type +
                '}';
    }
}
