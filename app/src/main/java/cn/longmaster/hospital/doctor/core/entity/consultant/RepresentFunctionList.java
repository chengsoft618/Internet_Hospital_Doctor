package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 销售代表功能列表
 * Created by Yang² on 2017/11/27.
 */

public class RepresentFunctionList implements Serializable {
    @JsonField("token")
    private String token;
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("data")
    private List<RepresentFunctionInfo> representFunctionList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<RepresentFunctionInfo> getRepresentFunctionList() {
        return representFunctionList;
    }

    public void setRepresentFunctionList(List<RepresentFunctionInfo> representFunctionList) {
        this.representFunctionList = representFunctionList;
    }

    @Override
    public String toString() {
        return "RepresentFunctionList{" +
                "token='" + token + '\'' +
                ", isFinish=" + isFinish +
                ", representFunctionList=" + representFunctionList +
                '}';
    }
}
