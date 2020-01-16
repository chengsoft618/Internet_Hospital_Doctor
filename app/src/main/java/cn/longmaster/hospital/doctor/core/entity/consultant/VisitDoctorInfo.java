package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 销售代表管理的医生列表实体
 * Created by Yang² on 2018/1/3.
 */

public class VisitDoctorInfo implements Serializable {
    @JsonField("user_id")
    private int userId;
    @JsonField("real_name")
    private String realName;
    @JsonField("recmd_user_id")
    private int recmdUserId;
    @JsonField("start_dt")
    private String startDt;
    @JsonField("app_count")
    private int appCount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getRecmdUserId() {
        return recmdUserId;
    }

    public void setRecmdUserId(int recmdUserId) {
        this.recmdUserId = recmdUserId;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    @Override
    public String toString() {
        return "VisitDoctorInfo{" +
                "userId=" + userId +
                ", realName='" + realName + '\'' +
                ", recmdUserId=" + recmdUserId +
                ", startDt='" + startDt + '\'' +
                ", appCount=" + appCount +
                '}';
    }
}
