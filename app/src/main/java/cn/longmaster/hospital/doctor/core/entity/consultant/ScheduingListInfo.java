package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2017/12/28.
 */

public class ScheduingListInfo implements Serializable {
    @JsonField("scheduing_id")
    private int scheduingId;
    @JsonField("att_doctor")
    private List<AttDoctorInfo> attDoctorList;

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public List<AttDoctorInfo> getAttDoctorList() {
        return attDoctorList;
    }

    public void setAttDoctorList(List<AttDoctorInfo> attDoctorList) {
        this.attDoctorList = attDoctorList;
    }

    @Override
    public String toString() {
        return "ScheduingListInfo{" +
                "scheduingId=" + scheduingId +
                ", attDoctorList=" + attDoctorList +
                '}';
    }
}
