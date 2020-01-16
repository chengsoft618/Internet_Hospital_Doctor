package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2017/12/28.
 */

public class ConsultantSchedulingInfo implements Serializable {
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("data")
    private List<ScheduingListInfo> scheduingList;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<ScheduingListInfo> getScheduingList() {
        return scheduingList;
    }

    public void setScheduingList(List<ScheduingListInfo> scheduingList) {
        this.scheduingList = scheduingList;
    }

    @Override
    public String toString() {
        return "ConsultantSchedulingInfo{" +
                "isFinish=" + isFinish +
                ", scheduingList=" + scheduingList +
                '}';
    }
}
