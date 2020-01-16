package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 销售代表功能
 * Created by Yang² on 2017/11/27.
 */

public class RepresentFunctionInfo implements Serializable {
    @JsonField("id")
    private int id;
    @JsonField("name")
    private String name;
    @JsonField("icon")
    private String icon;
    @JsonField("serial")
    private int serial;
    @JsonField("url")
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RepresentFunctionInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", serial=" + serial +
                ", url='" + url + '\'' +
                '}';
    }
}
