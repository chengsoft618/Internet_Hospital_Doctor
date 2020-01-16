package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/28.
 */

public class RecommendInfo implements Serializable {
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("symbol")
    private int symbol;
    @JsonField("data")
    private List<RecommendDoctorInfo> recommendDoctorInfos;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public List<RecommendDoctorInfo> getRecommendDoctorInfos() {
        return recommendDoctorInfos;
    }

    public void setRecommendDoctorInfos(List<RecommendDoctorInfo> recommendDoctorInfos) {
        this.recommendDoctorInfos = recommendDoctorInfos;
    }

    @Override
    public String toString() {
        return "RecommendInfo{" +
                "isFinish=" + isFinish +
                ", symbol=" + symbol +
                ", recommendDoctorInfos=" + recommendDoctorInfos +
                '}';
    }
}
