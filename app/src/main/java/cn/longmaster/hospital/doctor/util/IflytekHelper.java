package cn.longmaster.hospital.doctor.util;

import android.content.Context;
import android.os.Environment;

import com.google.common.base.Preconditions;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/24 10:26
 * @description: 讯飞语音识别
 */
public class IflytekHelper {
    private final String ZH_CN = "zh_cn";
    private String appID;
    private InitListener initListener;
    //引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    //结果返回方式
    private String resultType = "json";
    //语言类型
    private String languageType = ZH_CN;
    //中文语言类型
    private String languageAccent = "mandarin";
    //设置标点符号
    private boolean showASRPTT = true;
    //语音存储地址
    private String audioPath;

    private void start() {

    }

    public IflytekHelper setAppID(String appID) {
        this.appID = appID;
        return this;
    }

    public IflytekHelper setInitListener(InitListener initListener) {
        this.initListener = initListener;
        return this;
    }

    public SpeechRecognizer init(Context context) {
        this.appID = Preconditions.checkNotNull(appID, "appID is not init");
        this.initListener = Preconditions.checkNotNull(initListener, "initListener is not init");
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + appID);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(context, initListener);
        setSpeechParams(mIat);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        //mIatDialog = new RecognizerDialog(getApplicationContext(), mInitListener);
        return mIat;
    }

    private void setSpeechParams(SpeechRecognizer speechRecognizer) {
        // 清空参数
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, resultType);

        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, languageType);
        if (StringUtils.equals(languageType, ZH_CN)) {
            // 设置语言区域
            speechRecognizer.setParameter(SpeechConstant.ACCENT, languageAccent);
        }

        //此处用于设置dialog中不显示错误码信息
        //speechRecognizer.setParameter("view_tips_plain","false");
        //speechRecognizer.setParameter(AudioDetector.VAD_ENGINE, AudioDetector.TYPE_META);
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "180000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "180000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, showASRPTT ? "1" : "0");

        // 设置音频保存路径，保存音频格式支持pcm、DCDutyVisitPlantTempItem，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }
}
