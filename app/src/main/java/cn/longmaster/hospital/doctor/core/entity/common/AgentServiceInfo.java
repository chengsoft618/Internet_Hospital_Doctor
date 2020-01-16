package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 代理服务器信息
 * Created by JinKe on 2017-02-20.
 */

public class AgentServiceInfo implements Serializable {
    @JsonField("ip_address")
    private long ipAddress;//ip地址
    @JsonField("ip_port")
    private int ipPort;//ip端口
    @JsonField("is_activation")
    private int isActivation;//是否激活 1：是 0：否
    @JsonField("remark")
    private String remark;//服务器名称及相关备注信息
    @JsonField("url")
    private String url;//域名

    public long getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(long ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getIpPort() {
        return ipPort;
    }

    public void setIpPort(int ipPort) {
        this.ipPort = ipPort;
    }

    public int getIsActivation() {
        return isActivation;
    }

    public void setIsActivation(int isActivation) {
        this.isActivation = isActivation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AgentServiceInfo{" +
                "ipAddress=" + ipAddress +
                ", ipPort=" + ipPort +
                ", isActivation=" + isActivation +
                ", remark=" + remark +
                '}';
    }
}
