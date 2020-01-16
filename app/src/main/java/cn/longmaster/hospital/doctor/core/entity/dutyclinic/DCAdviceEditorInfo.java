package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-医嘱单编辑人信息
 * Created by yangyong on 2019-12-03.
 */
public class DCAdviceEditorInfo implements Serializable {
    @JsonField("edit_uid")
    private int editUid;
    @JsonField("edit_name")
    private String editName;

    public int getEditUid() {
        return editUid;
    }

    public void setEditUid(int editUid) {
        this.editUid = editUid;
    }

    public String getEditName() {
        return editName;
    }

    public void setEditName(String editName) {
        this.editName = editName;
    }

    @Override
    public String toString() {
        return "DCAdviceEditorInfo{" +
                "editUid=" + editUid +
                ", editName=" + editName +
                '}';
    }
}
