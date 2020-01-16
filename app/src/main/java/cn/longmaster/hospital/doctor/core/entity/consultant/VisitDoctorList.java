package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 销售代表自己所管理的医生列表
 * Created by Yang² on 2018/1/3.
 */

public class VisitDoctorList implements Serializable {
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("data")
    private List<VisitDoctorInfo> visitDoctorList;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<VisitDoctorInfo> getVisitDoctorList() {
        return visitDoctorList;
    }

    public void setVisitDoctorList(List<VisitDoctorInfo> visitDoctorList) {
        this.visitDoctorList = visitDoctorList;
    }

    @Override
    public String toString() {
        return "VisitDoctorList{" +
                "isFinish=" + isFinish +
                ", visitDoctorList=" + visitDoctorList +
                '}';
    }
}
