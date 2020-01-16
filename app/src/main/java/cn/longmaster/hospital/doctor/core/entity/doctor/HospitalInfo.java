package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by lm-pc on 16/7/26.
 */
public class HospitalInfo implements Serializable {
    @JsonField("hospital_id")
    private int hospitalId;//医院id
    @JsonField("hospital_name")
    private String hospitalName;//医院名称
    @JsonField("hospital_desc")
    private String hospitalDesc;//医院描述
    @JsonField("hospital_address")
    private String hospitalAddress;//医院地址
    @JsonField("hospital_phone")
    private String hospitalPhone;//医院联系电话
    @JsonField("hospital_url")
    private String hospitalUrl;//医院网站地址
    @JsonField("hospital_type_id")
    private int hospitalTypeId;//医院类型id
    @JsonField("hospital_province")
    private String hospitalProvince;//医院所属省
    @JsonField("hospital_city")
    private String hospitalCity;//医院所属城市
    @JsonField("insert_dt")
    private String insertDt;//数据插入时间

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalDesc() {
        return hospitalDesc;
    }

    public void setHospitalDesc(String hospitalDesc) {
        this.hospitalDesc = hospitalDesc;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalPhone() {
        return hospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        this.hospitalPhone = hospitalPhone;
    }

    public String getHospitalUrl() {
        return hospitalUrl;
    }

    public void setHospitalUrl(String hospitalUrl) {
        this.hospitalUrl = hospitalUrl;
    }

    public int getHospitalTypeId() {
        return hospitalTypeId;
    }

    public void setHospitalTypeId(int hospitalTypeId) {
        this.hospitalTypeId = hospitalTypeId;
    }

    public String getHospitalProvince() {
        return hospitalProvince;
    }

    public void setHospitalProvince(String hospitalProvince) {
        this.hospitalProvince = hospitalProvince;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "HospitalInfo{" +
                "hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalDesc='" + hospitalDesc + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                ", hospitalPhone='" + hospitalPhone + '\'' +
                ", hospitalUrl='" + hospitalUrl + '\'' +
                ", hospitalTypeId=" + hospitalTypeId +
                ", hospitalProvince='" + hospitalProvince + '\'' +
                ", hospitalCity='" + hospitalCity + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
