package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-值班医生信息
 * Created by yangyong on 2019-11-27.
 */
public class DCProjectDoctorListInfo implements Serializable {
    @JsonField("role_id")
    private int roleId;
    @JsonField("role_name")
    private String roleName;
    @JsonField("type")
    private int type;
    @JsonField("member_list")
    private List<DCDoctorInfo> doctorInfos;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DCDoctorInfo> getDoctorInfos() {
        return doctorInfos;
    }

    public void setDoctorInfos(List<DCDoctorInfo> doctorInfos) {
        this.doctorInfos = doctorInfos;
    }

    @Override
    public String toString() {
        return "DCProjectDoctorListInfo{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", type=" + type +
                ", doctorInfos=" + doctorInfos +
                '}';
    }
}
