package cn.longmaster.hospital.doctor.ui.main;

import android.content.Context;
import android.support.annotation.IntDef;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.UsercenterMenu;

/**
 * @author ABiao_Abiao
 * @date 2019/9/27 15:51
 * @description:
 */
public class UsercenterMenuHelper {

    private final UsercenterMenu message;
    private final UsercenterMenu assess;
    private final UsercenterMenu patientManager;
    private final UsercenterMenu liveVideo;
    private final UsercenterMenu dataCenter;
    private final UsercenterMenu myAccount;
    private final UsercenterMenu set;
    private final UsercenterMenu represent;
    private final UsercenterMenu meetingRoom;
    private final UsercenterMenu uploadMaterial;
    private final UsercenterMenu dutyClinic;
    private final UsercenterMenu dutyManager;
    private static UsercenterMenuHelper INSTANCE;

    @IntDef({R.string.user_center_menu_message,
            R.string.user_center_menu_assess,
            R.string.user_center_menu_patient_manager,
            R.string.user_center_menu_live_video,
            R.string.user_center_menu_data_center,
            R.string.user_center_menu_my_account,
            R.string.user_center_menu_set,
            R.string.user_center_menu_represent,
            R.string.user_center_menu_meeting_room,
            R.string.user_center_menu_upload_material,
            R.string.user_center_menu_duty_clinic,
            R.string.user_center_menu_duty_manager})
    public @interface UserCenterMenu {
    }

    private UsercenterMenuHelper(Context context) {
        message = new UsercenterMenu(R.string.user_center_menu_message, context.getString(R.string.user_center_menu_message), R.mipmap.ic_user_center_menu_message);
        assess = new UsercenterMenu(R.string.user_center_menu_assess, context.getString(R.string.user_center_menu_assess), R.mipmap.ic_user_center_menu_assess);
        patientManager = new UsercenterMenu(R.string.user_center_menu_patient_manager, context.getString(R.string.user_center_menu_patient_manager), R.mipmap.ic_user_center_menu_patient_manager);
        liveVideo = new UsercenterMenu(R.string.user_center_menu_live_video, context.getString(R.string.user_center_menu_live_video), R.mipmap.ic_user_center_menu_live_video);
        dataCenter = new UsercenterMenu(R.string.user_center_menu_data_center, context.getString(R.string.user_center_menu_data_center), R.mipmap.ic_user_center_menu_data_center);
        myAccount = new UsercenterMenu(R.string.user_center_menu_my_account, context.getString(R.string.user_center_menu_my_account), R.mipmap.ic_user_center_menu_my_account);
        set = new UsercenterMenu(R.string.user_center_menu_set, context.getString(R.string.user_center_menu_set), R.mipmap.ic_user_center_menu_set);
        represent = new UsercenterMenu(R.string.user_center_menu_represent, context.getString(R.string.user_center_menu_represent), R.mipmap.ic_user_center_menu_represent);
        meetingRoom = new UsercenterMenu(R.string.user_center_menu_meeting_room, context.getString(R.string.user_center_menu_meeting_room), R.mipmap.ic_user_center_menu_meeting_room);
        uploadMaterial = new UsercenterMenu(R.string.user_center_menu_upload_material, context.getString(R.string.user_center_menu_upload_material), R.mipmap.ic_user_center_menu_upload_material);
        dutyClinic = new UsercenterMenu(R.string.user_center_menu_duty_clinic, context.getString(R.string.user_center_menu_duty_clinic), R.mipmap.ic_user_center_menu_duty_clinic);
        dutyManager = new UsercenterMenu(R.string.user_center_menu_duty_manager, context.getString(R.string.user_center_menu_duty_manager), R.mipmap.ic_user_center_menu_duty_manage);
    }

    public static UsercenterMenuHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UsercenterMenuHelper(context);
        }
        return INSTANCE;
    }

    public UsercenterMenu getMenuById(@UserCenterMenu int menuId) {
        switch (menuId) {
            case R.string.user_center_menu_message:
                return message;
            case R.string.user_center_menu_assess:
                return assess;
            case R.string.user_center_menu_patient_manager:
                return patientManager;
            case R.string.user_center_menu_live_video:
                return liveVideo;
            case R.string.user_center_menu_data_center:
                return dataCenter;
            case R.string.user_center_menu_my_account:
                return myAccount;
            case R.string.user_center_menu_set:
                return set;

            case R.string.user_center_menu_represent:
                return represent;
            case R.string.user_center_menu_meeting_room:
                return meetingRoom;
            case R.string.user_center_menu_upload_material:
                return uploadMaterial;
            case R.string.user_center_menu_duty_clinic:
                return dutyClinic;
            case R.string.user_center_menu_duty_manager:
                return dutyManager;
            default:
                return null;
        }
    }
}
