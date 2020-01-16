package cn.longmaster.hospital.doctor.core;

import android.support.annotation.IntDef;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.utils.SPUtils;

/**
 * 应用配置类
 * Created by yangyong on 2016/4/14.
 */
public class AppConfig {
    //北京服务器
    public static final int SERVER_BEIJING = 1000;
    //北京发布服务器
    public static final int SERVER_ISSUE = 1002;
    //测试服务器
    public static final int SERVER_TEST = 1004;
    //109测试服务器
    public static final int SERVER_SANDBOX = 1006;
    //云谷备用机房
    public static final int SERVER_YG = 1008;
    private static final String TAG = AppConfig.class.getSimpleName();

    @IntDef({SERVER_BEIJING, SERVER_ISSUE, SERVER_TEST, SERVER_SANDBOX, SERVER_YG})
    public @interface ServerName {
    }

    //服务器名称
    @ServerName
    public static int serverName = SERVER_BEIJING;
    //当前是否为调试模式，正式打包时需要置为false
    public static final boolean IS_DEBUG_MODE = true;
    //客户端版本
    public static final int CLIENT_VERSION = 9009;

    //服务器地址
    private static String serverAddress;
    //服务器端口
    private static int serverPort;
    //dws服务器路径
    private static String dwsUrl;
    //ndws服务器路径
    private static String ndwsUrl;
    //dfs服务器路径
    private static String dfsUrl;
    //duws服务器路径
    private static String duwsUrl;
    //ivws服务器路径
    private static String ivwsUrl;
    //adws服务器路径
    private static String adwsUrl;
    //caws服务器路径
    private static String cawsUrl;
    //医学院视频服务器地址
    private static String mediaUrl;
    //clientApi服务器路径
    private static String clientApiUrl;
    //banner拉取路径
    private static String bannerDownloadUrl;
    //辅助资料拉取路径
    public static String materialDownloadUrl;
    //群组消息图片路径
    public static String imPictureDownloadUrl;
    //群组消息语音路径
    public static String imVoiceDownloadUrl;
    //首程图片
    public static String firstJourneyUrl;

    //版本更新url地址
    private static String upgradeXmlUrl;
    //nginx上传url
    private static String nginxUploadUrl;

    //常见问题url
    private static String commonProblemUrl;
    //服务条款url
    private static String agreenUrl;
    //服务说明url
    private static String serverDescUrl;
    //新手引导url
    private static String newbieGuideUrl;
    //资格认证url
    private static String qualificationUrl;
    //首页无权限跳转地址
    private static String noAuthorityUrl;
    //顾问发起预约
    private static String adviserApplyUrl;
    //查询数据url
    private static String searchDataUrl;
    //门诊管理url
    private static String consultManageUrl;
    private static String apiUrl;

    @ServerName
    public static int getServerName() {
        return serverName;
    }

    /**
     * 设置地址
     */
    public static void setUrl() {
        if (IS_DEBUG_MODE) {
            serverName = SPUtils.getInstance().getInt(AppPreference.KEY_SERVICE_ADDRESS, SERVER_TEST);
        }
        Logger.logD(TAG, "serverName:" + serverName);
        switch (serverName) {
            case SERVER_BEIJING:
                serverPort = 15000;
                serverAddress = "entry.39hospital.com";
                dwsUrl = "http://dws.39hospital.com/";
                ndwsUrl = "http://ndws.39hospital.com/";
                dfsUrl = "http://dfs.39hospital.com/";
                duwsUrl = "http://duws.39hospital.com/";
                ivwsUrl = "http://ivws.39hospital.com/";
                adwsUrl = "http://adws.39hospital.com/";
                cawsUrl = "http://caws.39hospital.com/";
                mediaUrl = "http://media.39hospital.com/";
                clientApiUrl = "http://clientapi.39hospital.com/";
                bannerDownloadUrl = "http://dfs.39hospital.com/3003/";
                materialDownloadUrl = "http://dfs.39hospital.com/3004/2/";
                imPictureDownloadUrl = "http://dfs.39hospital.com/3024/1/";
                firstJourneyUrl = "http://dfs.39hospital.com/3035/1/";
                imVoiceDownloadUrl = "http://dfs.39hospital.com/3025/1/";
                upgradeXmlUrl = "https://dl.39hospital.com/android/doctor/";
                nginxUploadUrl = "http://dfs.39hospital.com:81/upload";

                commonProblemUrl = "http://help.39hospital.com/question_pad.html";
                agreenUrl = "http://help.39hospital.com/serviceTerms_pad.html";
                serverDescUrl = "http://help.39hospital.com/serviceDes_doctor.html";
                newbieGuideUrl = "http://help.39hospital.com/novice_guide.html";
                qualificationUrl = "http://adws.39hospital.com/Settled/main.html";
                noAuthorityUrl = "http://adws.39hospital.com/Settled/main.html";
                adviserApplyUrl = "http://dgws.39hospital.com/client/submitapp?user_id=";
                searchDataUrl = "http://sqs.39hospital.com/index.php/Home/DataStatistics/index?user_id=";
                consultManageUrl = "http://dgws.39hospital.com/Clinic/admin?user_id=";
                apiUrl = "http://api.39hospital.com/v1/";
                break;

            case SERVER_ISSUE:
                serverPort = 15000;
                serverAddress = "issue-entry.39hospital.com";
                dwsUrl = "http://issue-dws.39hospital.com/";
                ndwsUrl = "http://issue-ndws.39hospital.com/";
                dfsUrl = "http://issue-dfs.39hospital.com/";
                duwsUrl = "http://issue-duws.39hospital.com/";
                ivwsUrl = "http://issue-ivws.39hospital.com/";
                adwsUrl = "http://issue-adws.39hospital.com/";
                cawsUrl = "http://issue-caws.39hospital.com/";
                mediaUrl = "http://issue-media.39hospital.com/";
                clientApiUrl = "http://issue-clientapi.39hospital.com/";
                bannerDownloadUrl = "http://issue-dfs.39hospital.com/3003/";
                materialDownloadUrl = "http://issue-dfs.39hospital.com/3004/2/";
                imPictureDownloadUrl = "http://issue-dfs.39hospital.com/3024/1/";
                firstJourneyUrl = "http://issue-dfs.39hospital.com/3035/1/";
                imVoiceDownloadUrl = "http://issue-dfs.39hospital.com/3025/1/";
                upgradeXmlUrl = "https://issue-dl.39hospital.com/android/doctor/";
                nginxUploadUrl = "http://issue-dfs.39hospital.com:81/upload/";

                commonProblemUrl = "http://issue-help.39hospital.com/question_pad.html";
                agreenUrl = "http://issue-help.39hospital.com/serviceTerms_pad.html";
                serverDescUrl = "http://issue-help.39hospital.com/serviceDes_doctor.html";
                newbieGuideUrl = "http://issue-help.39hospital.com/novice_guide.html";
                qualificationUrl = "http://issue-adws.39hospital.com/Settled/main.html";
                noAuthorityUrl = "http://issue-adws.39hospital.com/Settled/main.html";
                adviserApplyUrl = "http://issue-dgws.39hospital.com/client/submitapp?user_id=";
                searchDataUrl = "http://issue-sqs.39hospital.com/index.php/Home/DataStatistics/index?user_id=";
                consultManageUrl = "http://issue-dgws.39hospital.com/Clinic/admin?user_id=";
                apiUrl = "http://issue-api.39hospital.com/v1/";
                break;

            case SERVER_TEST:
                serverPort = 25000;
                serverAddress = "test-entry.39hospital.com";
                dwsUrl = "http://test-dws.39hospital.com/";
                ndwsUrl = "http://test-ndws.39hospital.com/";
                dfsUrl = "http://test-dfs.39hospital.com/";
                duwsUrl = "http://test-duws.39hospital.com/";
                ivwsUrl = "http://test-ivws.39hospital.com/";
                adwsUrl = "http://test-adws.39hospital.com/";
                cawsUrl = "http://test-caws.39hospital.com/";
                mediaUrl = "http://test-media.39hospital.com/";
                clientApiUrl = "http://test-clientapi.39hospital.com/";
                bannerDownloadUrl = "http://test-dfs.39hospital.com/3003/";
                materialDownloadUrl = "http://test-dfs.39hospital.com/3004/2/";
                imPictureDownloadUrl = "http://test-dfs.39hospital.com/3024/1/";
                firstJourneyUrl = "http://test-dfs.39hospital.com/3035/1/";
                imVoiceDownloadUrl = "http://test-dfs.39hospital.com/3025/1/";
                upgradeXmlUrl = "https://test-dl.39hospital.com/android/doctor/";
                nginxUploadUrl = "http://test-dfs.39hospital.com:83/upload";
                commonProblemUrl = "http://test-help.39hospital.com/question_pad.html";
                agreenUrl = "http://test-help.39hospital.com/serviceTerms_pad.html";
                serverDescUrl = "http://test-help.39hospital.com/serviceDes_doctor.html";
                newbieGuideUrl = "http://test-help.39hospital.com/novice_guide.html";
                qualificationUrl = "http://test-adws.39hospital.com/Settled/main.html";
                noAuthorityUrl = "http://test-adws.39hospital.com/Settled/main.html";
                adviserApplyUrl = "http://test-dgws.39hospital.com/client/submitapp?user_id=";
                searchDataUrl = "http://test-sqs.39hospital.com/index.php/Home/DataStatistics/index?user_id=";
                consultManageUrl = "http://test-dgws.39hospital.com/Clinic/admin?user_id=";
                apiUrl = "http://test-api.39hospital.com/v1/";
                break;

            case SERVER_SANDBOX:
                serverPort = 15000;
                serverAddress = "10.254.33.109";
                dwsUrl = "http://10.254.33.109/dws/";
                ndwsUrl = "http://10.254.33.109/ndws/";
                dfsUrl = "http://10.254.33.109/dfs/";
                duwsUrl = "http://10.254.33.109/duws/";
                ivwsUrl = "http://10.254.33.109/ivws/";
                adwsUrl = "http://10.254.33.109/adws/";
                cawsUrl = "http://10.254.33.109/caws/";
                mediaUrl = "http://10.254.33.109/media/";
                clientApiUrl = "http://10.254.33.109/client_api/";
                bannerDownloadUrl = "http://10.254.33.109/dfs/3003/";
                materialDownloadUrl = "http://10.254.33.109/dfs/3004/2/";
                imPictureDownloadUrl = "http://10.254.33.109/dfs/3024/1/";
                firstJourneyUrl = "http://10.254.33.109/dfs/3035/1/";
                imVoiceDownloadUrl = "http://10.254.33.109/dfs/3025/1/";
                upgradeXmlUrl = "https://10.254.33.109/dl/android/doctor/";
                nginxUploadUrl = "http://10.254.33.109:81/upload";

                commonProblemUrl = "http://10.254.33.109/help/question_pad.html";
                agreenUrl = "http://10.254.33.109/help/serviceTerms_pad.html";
                serverDescUrl = "http://10.254.33.109/help/serviceDes_doctor.html";
                newbieGuideUrl = "http://10.254.33.109/help/novice_guide.html";
                qualificationUrl = "http://10.254.33.109/adws/Settled/main.html";
                noAuthorityUrl = "http://10.254.33.109/adws/Settled/main.html";
                adviserApplyUrl = "http://10.254.33.109/dgws/client/submitapp?user_id=";
                searchDataUrl = "http://10.254.33.109/sbi/index.php/Home/DataStatistics/index?user_id=";
                consultManageUrl = "http://10.254.33.109/dgws/Clinic/admin?user_id=";
                break;

            case SERVER_YG:
                serverPort = 15000;
                serverAddress = "yg-entry.39hospital.com";
                ndwsUrl = "http://yg-ndws.39hospital.com/";
                dfsUrl = "http://yg-dfs.39hospital.com/";
                duwsUrl = "http://yg-duws.39hospital.com/";
                ivwsUrl = "http://yg-ivws.39hospital.com/";
                adwsUrl = "http://yg-adws.39hospital.com/";
                cawsUrl = "http://yg-caws.39hospital.com/";
                mediaUrl = "http://yg-media.39hospital.com/";
                clientApiUrl = "http://yg-clientapi.39hospital.com/";
                bannerDownloadUrl = "http://yg-dfs.39hospital.com/3003/";
                materialDownloadUrl = "http://yg-dfs.39hospital.com/3004/2/";
                imPictureDownloadUrl = "http://yg-dfs.39hospital.com/3024/1/";
                firstJourneyUrl = "http://yg-dfs.39hospital.com/3035/1/";
                imVoiceDownloadUrl = "http://yg-dfs.39hospital.com/3025/1/";
                upgradeXmlUrl = "https://yg-dl.39hospital.com/android/doctor/";
                nginxUploadUrl = "http://yg-dfs.39hospital.com:81/upload";

                commonProblemUrl = "http://yg-help.39hospital.com/question_pad.html";
                agreenUrl = "http://yg-help.39hospital.com/serviceTerms_pad.html";
                serverDescUrl = "http://yg-help.39hospital.com/serviceDes_doctor.html";
                newbieGuideUrl = "http://yg-help.39hospital.com/novice_guide.html";
                qualificationUrl = "http://yg-adws.39hospital.com/Settled/main.html";
                noAuthorityUrl = "http://yg-adws.39hospital.com/Settled/main.html";
                adviserApplyUrl = "http://yg-dgws.39hospital.com/client/submitapp?user_id=";
                searchDataUrl = " http://yg-sqs.39hospital.com/index.php/Home/DataStatistics/index?user_id=";
                consultManageUrl = "http://yg-dgws.39hospital.com/Clinic/admin?user_id=";
                break;

            default:
                break;
        }
    }

    public static String getFirstJourneyUrl() {
        return firstJourneyUrl;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getDwsUrl() {
        return dwsUrl;
    }

    public static String getNdwsUrl() {
        return ndwsUrl;
    }

    public static String getDfsUrl() {
        return dfsUrl;
    }

    public static String getDuwsUrl() {
        return duwsUrl;
    }

    public static String getIvwsUrl() {
        return ivwsUrl;
    }

    public static String getAdwsUrl() {
        return adwsUrl;
    }

    public static String getCawsUrl() {
        return cawsUrl;
    }

    public static String getMediaUrl() {
        return mediaUrl;
    }

    public static String getClientApiUrl() {
        return clientApiUrl;
    }

    public static String getBannerDownloadUrl() {
        return bannerDownloadUrl;
    }

    public static String getMaterialDownloadUrl() {
        return materialDownloadUrl;
    }

    public static String getUpgradeXmlUrl() {
        return upgradeXmlUrl;
    }

    public static String getNginxUploadUrl() {
        return nginxUploadUrl;
    }

    public static String getCommonProblemUrl() {
        return commonProblemUrl;
    }

    public static String getAgreenUrl() {
        return agreenUrl;
    }

    public static String getServerDescUrl() {
        return serverDescUrl;
    }

    public static String getNewbieGuideUrl() {
        return newbieGuideUrl;
    }

    public static String getQualificationUrl() {
        return qualificationUrl;
    }

    public static String getNoAuthorityUrl() {
        return noAuthorityUrl;
    }

    public static String getSearchDataUrl() {
        return searchDataUrl;
    }

    public static String getAdviserApplyUrl() {
        return adviserApplyUrl;
    }

    public static String getConsultManageUrl() {
        return consultManageUrl;
    }

    public static String getImPictureDownloadUrl() {
        return imPictureDownloadUrl;
    }

    public static String getImVoiceDownloadUrl() {
        return imVoiceDownloadUrl;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static void setApiUrl(String apiUrl) {
        AppConfig.apiUrl = apiUrl;
    }
}