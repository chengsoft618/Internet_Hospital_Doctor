package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/2/19.
 */

public class HospitalAndDepartmentInfo implements Serializable {
    @JsonField("hopital_info")//医院信息
    private HospitalIntroductionInfo hospitalIntroductionInfo;
    @JsonField("department_info")//科室信息
    private DepartmentIntroductionInfo departmentIntroductionInfo;

    public HospitalIntroductionInfo getHospitalIntroductionInfo() {
        return hospitalIntroductionInfo;
    }

    public void setHospitalIntroductionInfo(HospitalIntroductionInfo hospitalIntroductionInfo) {
        this.hospitalIntroductionInfo = hospitalIntroductionInfo;
    }

    public DepartmentIntroductionInfo getDepartmentIntroductionInfo() {
        return departmentIntroductionInfo;
    }

    public void setDepartmentIntroductionInfo(DepartmentIntroductionInfo departmentIntroductionInfo) {
        this.departmentIntroductionInfo = departmentIntroductionInfo;
    }

    @Override
    public String toString() {
        return "HospitalAndDepartmentInfo{" +
                "hospitalIntroductionInfo=" + hospitalIntroductionInfo +
                ", departmentIntroductionInfo=" + departmentIntroductionInfo +
                '}';
    }
}
