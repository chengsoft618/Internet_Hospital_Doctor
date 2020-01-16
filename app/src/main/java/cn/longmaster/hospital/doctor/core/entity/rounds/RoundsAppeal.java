package cn.longmaster.hospital.doctor.core.entity.rounds;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/10/18 9:19
 * @description: 查房诉求
 */
public class RoundsAppeal {
    @JsonField("id")
    private String id;
    @JsonField("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
