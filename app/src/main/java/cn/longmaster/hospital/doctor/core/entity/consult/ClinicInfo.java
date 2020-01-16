package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/14.
 */

public class ClinicInfo implements Serializable {
    @JsonField("clinic_id")//门诊ID
    private int clinicId;
    @JsonField("clinic_hospital_id")//门诊医院ID
    private int clinicHospitalId;

    @JsonField("clinic_title")//门诊标题
    private String clinicTitle;
    @JsonField("clinic_begin_dt")//门诊开始时间
    private String clinicBeginDt;

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getClinicHospitalId() {
        return clinicHospitalId;
    }

    public void setClinicHospitalId(int clinicHospitalId) {
        this.clinicHospitalId = clinicHospitalId;
    }

    public String getClinicTitle() {
        return clinicTitle;
    }

    public void setClinicTitle(String clinicTitle) {
        this.clinicTitle = clinicTitle;
    }

    public String getClinicBeginDt() {
        return clinicBeginDt;
    }

    public void setClinicBeginDt(String clinicBeginDt) {
        this.clinicBeginDt = clinicBeginDt;
    }

    @Override
    public String toString() {
        return "ClinicInfo{" +
                "clinicId=" + clinicId +
                ", clinicHospitalId=" + clinicHospitalId +
                ", clinicTitle='" + clinicTitle + '\'' +
                ", clinicBeginDt='" + clinicBeginDt + '\'' +
                '}';
    }
}
