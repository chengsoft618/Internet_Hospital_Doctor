package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/14.
 */

public class ServiceTypeInfo implements Serializable {
    @JsonField("service_type")//service_type=101007 门诊
    private int serviceType;

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "ServiceTypeInfo{" +
                "serviceType=" + serviceType +
                '}';
    }
}
