package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/2/19.
 */

public class HospitalIntroductionInfo implements Serializable {
    @JsonField("hospital_id")
    private int hospitalId;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("hospital_desc")//医院介绍
    private String hospitalDesc;
    @JsonField("hospital_address")
    private String hospitalAddress;

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

    @Override
    public String toString() {
        return "HospitalIntroductionInfo{" +
                "hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalDesc='" + hospitalDesc + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                '}';
    }
}
