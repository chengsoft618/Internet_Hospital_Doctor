package cn.longmaster.hospital.doctor.core.requests;

/**
 * @author ABiao_Abiao
 * @date 2019/7/30 10:36
 * @description:
 */
public interface OnResultCallback<D> {
    //通用返回结果定义
    int RESULT_SUCCESS = 0;//请求成功
    int RESULT_FAILED = -1;//请求失败 一般是网络错误
    int RESULT_PARSE_FAILED = -3;//解析失败
    int RESULT_SERVER_CODE_ERROR = -2;//请求失败:服务器返回结果不为200
    int RESULT_AUTH_CODE_ERROR = 1030056;//参数（鉴权key）：c_auth 校验失败

    void onSuccess(D d, BaseResult baseResult);

    void onFail(BaseResult baseResult);

    void onFinish();
}
