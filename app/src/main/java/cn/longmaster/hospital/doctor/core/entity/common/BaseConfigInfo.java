package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

/**
 * 基本配置信息
 * Created by Yang² on 2016/7/27.
 */
public class BaseConfigInfo<Data> implements Serializable {
    private int type;
    private int dataId;
    private String token = "0";
    private Data data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseConfigInfo{" +
                "type=" + type +
                ", dataId=" + dataId +
                ", token='" + token + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
