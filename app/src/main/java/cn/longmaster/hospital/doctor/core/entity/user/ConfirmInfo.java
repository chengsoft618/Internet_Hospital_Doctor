package cn.longmaster.hospital.doctor.core.entity.user;

import android.os.Parcel;
import android.os.Parcelable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/7/15 21:33
 * @description: 确认单详情
 */
public class ConfirmInfo implements Parcelable {
    /**
     * id : 2738
     * appointment_id : 18877,18955,18868,18878,18902,18781
     * pay_value : 898.00
     * att_hospital_id : 6
     * att_hospital_name : 北京协和医院
     * att_department_name : 血液科
     * doctor_name : 王大锤
     * hospital_name : 贵阳中医一附院
     * department_name : 整形外科
     * type : 1
     * insert_dt : 2019-07-10 10:02:30
     * file_name : 201907.2019071010022827388vXD9Z.png
     */
    //查房为确认单ID；会诊为排班ID
    @JsonField("id")
    private String id;
    //会诊为全部就诊编号，逗号隔开；查房为订单编号
    @JsonField("appointment_id")
    private String appointmentId;
    //确认单金额
    @JsonField("pay_value")
    private String payValue;
    //首诊医院ID
    @JsonField("att_hospital_id")
    private String attHospitalId;
    //首诊医院名称 确认单详情显示
    @JsonField("att_hospital_name")
    private String attHospitalName;
    //首诊科室名称
    @JsonField("att_department_name")
    private String attDepartmentName;
    //上级专家名称
    @JsonField("doctor_name")
    private String doctorName;
    //上级专家医院
    @JsonField("hospital_name")
    private String hospitalName;
    //上级专家科室
    @JsonField("department_name")
    private String departmentName;
    //类型：会诊1；查房2
    @JsonField("type")
    private int type;
    @JsonField("insert_dt")
    private String insertDt;
    //确认单文件名称 dfs用3026
    @JsonField("file_name")
    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }

    public String getAttHospitalId() {
        return attHospitalId;
    }

    public void setAttHospitalId(String attHospitalId) {
        this.attHospitalId = attHospitalId;
    }

    public String getAttHospitalName() {
        return attHospitalName;
    }

    public void setAttHospitalName(String attHospitalName) {
        this.attHospitalName = attHospitalName;
    }

    public String getAttDepartmentName() {
        return attDepartmentName;
    }

    public void setAttDepartmentName(String attDepartmentName) {
        this.attDepartmentName = attDepartmentName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.appointmentId);
        dest.writeString(this.payValue);
        dest.writeString(this.attHospitalId);
        dest.writeString(this.attHospitalName);
        dest.writeString(this.attDepartmentName);
        dest.writeString(this.doctorName);
        dest.writeString(this.hospitalName);
        dest.writeString(this.departmentName);
        dest.writeInt(this.type);
        dest.writeString(this.insertDt);
        dest.writeString(this.fileName);
    }

    public ConfirmInfo() {
    }

    protected ConfirmInfo(Parcel in) {
        this.id = in.readString();
        this.appointmentId = in.readString();
        this.payValue = in.readString();
        this.attHospitalId = in.readString();
        this.attHospitalName = in.readString();
        this.attDepartmentName = in.readString();
        this.doctorName = in.readString();
        this.hospitalName = in.readString();
        this.departmentName = in.readString();
        this.type = in.readInt();
        this.insertDt = in.readString();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<ConfirmInfo> CREATOR = new Parcelable.Creator<ConfirmInfo>() {
        @Override
        public ConfirmInfo createFromParcel(Parcel source) {
            return new ConfirmInfo(source);
        }

        @Override
        public ConfirmInfo[] newArray(int size) {
            return new ConfirmInfo[size];
        }
    };
}
