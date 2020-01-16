package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 根据医生ID获取服务权限
 * Created by JinKe on 2016-07-27.
 */
public class ServiceAuthorityInfo implements Serializable {
    @JsonField("user_id")
    private int userId;//医生ID
    @JsonField("service_type")
    private int serviceType;//服务权限，1远程会诊，2影像服务，3首诊服务
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "ServiceAuthorityInfo{" +
                "userId=" + userId +
                ", serviceType=" + serviceType +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
