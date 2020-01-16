package cn.longmaster.hospital.doctor.ui.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.UsercenterMenu;
import cn.longmaster.utils.LibCollections;

/**
 * @author ABiao_Abiao
 * @date 2019/9/26 17:28
 * @description:
 */
public class UsercenterMenuAdapter extends BaseQuickAdapter<UsercenterMenu, BaseViewHolder> {
    public UsercenterMenuAdapter(int layoutResId, @Nullable List<UsercenterMenu> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UsercenterMenu item) {
        if (item.isHasMsg()) {
            if (item.getId() == R.string.user_center_menu_duty_clinic) {
                helper.setVisible(R.id.item_user_center_menu_dutying_iv, true);
            } else {
                helper.setVisible(R.id.item_user_center_menu_msg_v, true);
            }
        } else {
            helper.setGone(R.id.item_user_center_menu_msg_v, false);
            helper.setGone(R.id.item_user_center_menu_dutying_iv, false);
        }
        helper.setText(R.id.item_user_center_menu_tv, item.getName());
        helper.setImageResource(R.id.item_user_center_menu_iv, item.getPic());
    }

    private void showDot(@UsercenterMenuHelper.UserCenterMenu int menuId, boolean isShow) {
        List<UsercenterMenu> menus = getData();
        for (int i = 0; i < LibCollections.size(menus); i++) {
            UsercenterMenu menu = menus.get(i);
            if (menu.getId() == menuId) {
                menu.setHasMsg(isShow);
            }
        }
        notifyDataSetChanged();
    }

    public void showDot(@UsercenterMenuHelper.UserCenterMenu int menuId) {
        showDot(menuId, true);
    }

    public void hideDot(@UsercenterMenuHelper.UserCenterMenu int menuId) {
        showDot(menuId, false);
    }

    public void hideMenu(@UsercenterMenuHelper.UserCenterMenu int menuId) {

    }
}
