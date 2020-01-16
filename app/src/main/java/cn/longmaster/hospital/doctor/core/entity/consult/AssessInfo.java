package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by yangyong on 2019-07-16.
 */
public class AssessInfo implements Serializable {
    @JsonField("order_id")
    private int orderId;
    @JsonField("attdoc_id")
    private int attDocId;
    @JsonField("course_name")
    private String courseName;
    @JsonField("stage_serial")
    private String stageSerial;
    @JsonField("attdoc_name")
    private String attDocName;
    @JsonField("direction")
    private String direction;
    @JsonField("doctor_name")
    private String doctorName;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAttDocId() {
        return attDocId;
    }

    public void setAttDocId(int attDocId) {
        this.attDocId = attDocId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStageSerial() {
        return stageSerial;
    }

    public void setStageSerial(String stageSerial) {
        this.stageSerial = stageSerial;
    }

    public String getAttDocName() {
        return attDocName;
    }

    public void setAttDocName(String attDocName) {
        this.attDocName = attDocName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String toString() {
        return "AssessInfo{" +
                "orderId=" + orderId +
                ", attDocId=" + attDocId +
                ", courseName='" + courseName + '\'' +
                ", stageSerial='" + stageSerial + '\'' +
                ", attDocName='" + attDocName + '\'' +
                ", direction='" + direction + '\'' +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }
}
