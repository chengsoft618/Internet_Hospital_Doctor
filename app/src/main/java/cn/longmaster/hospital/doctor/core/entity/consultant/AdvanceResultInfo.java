package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/1/2.
 */

public class AdvanceResultInfo implements Serializable {
    @JsonField("success")
    private List<Integer> advanceSuccesss;
    @JsonField("fail")
    private List<Integer> advanceFails;

    public List<Integer> getAdvanceSuccesss() {
        return advanceSuccesss;
    }

    public void setAdvanceSuccesss(List<Integer> advanceSuccesss) {
        this.advanceSuccesss = advanceSuccesss;
    }

    public List<Integer> getAdvanceFails() {
        return advanceFails;
    }

    public void setAdvanceFails(List<Integer> advanceFails) {
        this.advanceFails = advanceFails;
    }

    @Override
    public String toString() {
        return "AdvanceResultInfo{" +
                "advanceSuccesss=" + advanceSuccesss +
                ", advanceFails=" + advanceFails +
                '}';
    }
}
