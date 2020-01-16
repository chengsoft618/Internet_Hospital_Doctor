package cn.longmaster.hospital.doctor.core.requests.config;

/**
 * Created by Yang² on 2016/7/26.
 */
public class RequestParams {
    private int type;
    private String token;//同步token标识
    private int doctorId;//医生ID
    private int hospitalId;//医院ID
    private int materialId;//材料ID
    private int departmentId;//部门ID
    private int appointmentId;//预约ID
    private int bannerType;//banner类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "type=" + type +
                ", token='" + token + '\'' +
                ", hospitalId=" + hospitalId +
                ", materialId=" + materialId +
                ", departmentId=" + departmentId +
                ", appointmentId=" + appointmentId +
                ", bannerType=" + bannerType +
                '}';
    }
}
