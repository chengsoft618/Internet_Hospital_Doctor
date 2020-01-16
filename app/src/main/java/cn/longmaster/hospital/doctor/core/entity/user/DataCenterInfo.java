package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/10/9.
 */
public class DataCenterInfo implements Serializable {
    @JsonField("user_id")
    private int userId;
    @JsonField("type")
    private int type;

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
    public String toString() {
        return "DataCenterInfo{" +
                "userId=" + userId +
                ", type=" + type +
                '}';
    }
}
