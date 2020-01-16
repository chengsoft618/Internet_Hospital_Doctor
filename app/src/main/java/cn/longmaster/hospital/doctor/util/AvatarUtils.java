package cn.longmaster.hospital.doctor.util;

import cn.longmaster.hospital.doctor.core.AppConfig;

/**
 * @author Mloong_Abiao
 * @date 2019/6/5 15:29
 * @description:
 */
public class AvatarUtils {
    public static String getAvatar(boolean isVisualize, int userId, String avatarToken) {
        String url;
        if (isVisualize) {//形象照
            //filePath = SdManager.getInstance().getAppointAvatarFilePath(userId + "") + "_a";
            url = AppConfig.getDfsUrl() + "3001/" + avatarToken + "/" + userId + "_a" + ".png";
        } else {
//            if (isAssistant) {
//                url = AppConfig.getDfsUrl() + "3009/" + avatarToken + "/" + displayParams.getAssistantName() + "/" + userId;
//            } else {
//                url = AppConfig.getDfsUrl() + "3001/" + avatarToken + "/" + userId + ".png";
//            }
            url = AppConfig.getDfsUrl() + "3001/" + avatarToken + "/" + userId + ".png";
        }
        return url;
    }
}
