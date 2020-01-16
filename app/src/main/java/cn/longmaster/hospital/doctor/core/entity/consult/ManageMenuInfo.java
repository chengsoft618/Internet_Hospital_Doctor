package cn.longmaster.hospital.doctor.core.entity.consult;

/**
 * 管理菜单信息
 * Created by JinKe on 2016-09-07.
 */
public class ManageMenuInfo {
    private int itemId;//条目id
    private String itemName;//条目名称
    private boolean isClickable;//是否可以点击

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }
}
