package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 15:30
 * @description: 医生用户反馈
 */
public class DoctorFeedBackInfo {

    /**
     * total_score : 9.7
     * medical_score : 9
     * service_score : 9.5
     * medical_label : [{"label_str":"服务态度好","label_num":"5"},{"label_str":"专业知识强","label_num":"4"},{"label_str":"有责任心","label_num":"4"}]
     */
    //综合评分
    @JsonField("total_score")
    private float totalScore;
    //医疗水平
    @JsonField("medical_score")
    private ScoreBean medicalScore;
    //服务态度
    @JsonField("service_score")
    private ScoreBean serviceScore;
    //用户反馈标签
    @JsonField("medical_label")
    private List<MedicalLabelBean> medicalLabel;

    public float getTotalScore() {
        return totalScore;
    }

    public String getTotalScoreStr() {
        return totalScore == 0 ? "暂无" : totalScore + "分";
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public ScoreBean getMedicalScore() {
        return medicalScore;
    }

    public void setMedicalScore(ScoreBean medicalScore) {
        this.medicalScore = medicalScore;
    }

    public ScoreBean getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(ScoreBean serviceScore) {
        this.serviceScore = serviceScore;
    }

    public List<MedicalLabelBean> getMedicalLabel() {
        return medicalLabel;
    }

    public void setMedicalLabel(List<MedicalLabelBean> medicalLabel) {
        this.medicalLabel = medicalLabel;
    }

    public boolean isEmpty() {
        return totalScore == 0.0 && medicalScore.isEmpty() && serviceScore.isEmpty() && LibCollections.isEmpty(medicalLabel);
    }

    public static class MedicalLabelBean {
        /**
         * label_str : 服务态度好
         * label_num : 5
         */
        @JsonField("label_str")
        private String labelStr;
        @JsonField("label_num")
        private int labelNum;

        public String getLabelStr() {
            return labelStr;
        }

        public void setLabelStr(String labelStr) {
            this.labelStr = labelStr;
        }

        public float getLabelNum() {
            return labelNum;
        }

        public void setLabelNum(int labelNum) {
            this.labelNum = labelNum;
        }

        public String getLabelNumStr() {
            return labelNum == 0 ? "暂无" : labelNum + "";
        }
    }


    public static class ScoreBean {

        /**
         * score_name : 服务态度
         * score : 0
         * score_grade :
         */

        @JsonField("score_name")
        private String scoreName;
        @JsonField("score")
        private float score;
        @JsonField("score_grade")
        private String scoreGrade;

        public String getScoreName() {
            return scoreName;
        }

        public void setScoreName(String scoreName) {
            this.scoreName = scoreName;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public String setScoreStr() {
            return score == 0 ? "暂无" : score + "";
        }

        public String getScoreGrade() {
            return scoreGrade;
        }

        public void setScoreGrade(String scoreGrade) {
            this.scoreGrade = scoreGrade;
        }

        public boolean isEmpty() {
            return StringUtils.isTrimEmpty(scoreGrade) && score == 0.0;
        }
    }
}
