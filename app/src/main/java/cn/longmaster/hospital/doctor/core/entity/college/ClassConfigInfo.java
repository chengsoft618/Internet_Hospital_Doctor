package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/26.
 */

public class ClassConfigInfo implements Serializable {
    @JsonField("module_id")
    private int moduleId;
    @JsonField("module_title")
    private String moduleTitle;
    @JsonField("module_type")
    private int moduleType;
    @JsonField("label_id")
    private int labelId;
    @JsonField("is_display")
    private int isDisplay;
    @JsonField("sort_num")
    private int sortNum;

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    @Override
    public String toString() {
        return "ClassConfigInfo{" +
                "moduleId=" + moduleId +
                ", moduleTitle='" + moduleTitle + '\'' +
                ", moduleType=" + moduleType +
                ", labelId=" + labelId +
                ", isDisplay=" + isDisplay +
                ", sortNum=" + sortNum +
                '}';
    }
}
