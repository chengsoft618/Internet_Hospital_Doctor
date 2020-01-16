package cn.longmaster.hospital.doctor.core.entity.common;

/**
 * Created by Yang² on 2017/11/14.
 */

public class ShareItem {
    public int itemId;
    public int resId;
    public String title;

    public ShareItem(int itemId, int resId, String title) {
        this.itemId = itemId;
        this.resId = resId;
        this.title = title;
    }
}
