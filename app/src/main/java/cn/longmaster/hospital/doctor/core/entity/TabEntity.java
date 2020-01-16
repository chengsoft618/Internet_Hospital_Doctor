package cn.longmaster.hospital.doctor.core.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * @author ABiao_Abiao
 * @date 2019/9/23 15:43
 * @description:
 */
public class TabEntity implements CustomTabEntity {
    private String title;
    private int selectedIcon;
    private int unSelectedIcon;
    private String selectedIconStr;
    private String unSelectedIconStr;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getUnSelectedIcon() {
        return unSelectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.unSelectedIcon = unSelectedIcon;
    }

    public String getSelectedIconStr() {
        return selectedIconStr;
    }

    public void setSelectedIconStr(String selectedIconStr) {
        this.selectedIconStr = selectedIconStr;
    }

    public String getUnSelectedIconStr() {
        return unSelectedIconStr;
    }

    public void setUnSelectedIconStr(String unSelectedIconStr) {
        this.unSelectedIconStr = unSelectedIconStr;
    }
}
