package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 省份城市信息配置
 * Created by JinKe on 2016-07-26.
 */
public class ProvinceCityInfo implements Serializable {
    @JsonField("province")
    private String province;//省份名称
    @JsonField("city")
    private String city;//城市名称
    @JsonField("upd_dt")
    private String updDt;//插入时间

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    @Override
    public String toString() {
        return "ProvinceCityInfo{" +
                "province=" + province +
                ", city=" + city +
                ", updDt=" + updDt +
                '}';
    }
}
