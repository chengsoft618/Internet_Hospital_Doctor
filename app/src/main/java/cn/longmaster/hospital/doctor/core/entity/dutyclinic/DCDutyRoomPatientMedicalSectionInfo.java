package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @author ABiao_Abiao
 * @date 2020/1/4 16:12
 * @description:
 */
public class DCDutyRoomPatientMedicalSectionInfo extends SectionEntity<DCDutyRoomPatientMedical> {
    private String date;

    public DCDutyRoomPatientMedicalSectionInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public DCDutyRoomPatientMedicalSectionInfo(DCDutyRoomPatientMedical dcDutyRoomPatientMedical) {
        super(dcDutyRoomPatientMedical);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
