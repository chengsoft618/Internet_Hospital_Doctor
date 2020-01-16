package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2017/12/28.
 */

public class VisitInfo implements Serializable {
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("data")
    private List<VisitDetailsInfo> visitDetailsInfos;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<VisitDetailsInfo> getVisitDetailsInfos() {
        return visitDetailsInfos;
    }

    public void setVisitDetailsInfos(List<VisitDetailsInfo> visitDetailsInfos) {
        this.visitDetailsInfos = visitDetailsInfos;
    }

    @Override
    public String toString() {
        return "VisitInfo{" +
                ", visitDetailsInfos=" + visitDetailsInfos +
                '}';
    }
}
