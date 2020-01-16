package cn.longmaster.hospital.doctor.core.entity.user;

import cn.longmaster.hospital.doctor.ui.main.UsercenterMenuHelper;

/**
 * @author ABiao_Abiao
 * @date 2019/9/26 17:29
 * @description:
 */
public class UsercenterMenu {
    @UsercenterMenuHelper.UserCenterMenu
    private int id;
    private String name;
    private int pic;
    private boolean hasMsg;
    private boolean isHide;

    public UsercenterMenu(int id, String name, int pic) {
        this.id = id;
        this.name = name;
        this.pic = pic;
    }

    @UsercenterMenuHelper.UserCenterMenu
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

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public boolean isHasMsg() {
        return hasMsg;
    }

    public void setHasMsg(boolean hasMsg) {
        this.hasMsg = hasMsg;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }
}
