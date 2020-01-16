package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;

/**
 * IM角色基本信息类
 * Created by WangHaiKun on 2017/8/18.
 */

public class RoleBasicInfo implements Serializable {
    private int msgType;
    private int userId;
    private String roleName;
    private String roleLevel;
    private String rolePic;
    private String context;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRolePic() {
        return rolePic;
    }

    public void setRolePic(String rolePic) {
        this.rolePic = rolePic;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "RoleBasicInfo{" +
                "msgType=" + msgType +
                ", userId=" + userId +
                ", roleName='" + roleName + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", rolePic='" + rolePic + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
