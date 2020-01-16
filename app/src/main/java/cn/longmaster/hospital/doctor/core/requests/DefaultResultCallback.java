package cn.longmaster.hospital.doctor.core.requests;

import android.support.annotation.CallSuper;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/30 10:48
 * @description:
 */
public abstract class DefaultResultCallback<D> implements OnResultCallback<D> {

    @CallSuper
    @Override
    public void onFail(BaseResult result) {
        switch (result.getCode()) {
            case RESULT_FAILED:
                ToastUtils.showShort(R.string.no_network_connection);
                return;
            case RESULT_AUTH_CODE_ERROR:
                ToastUtils.showShort(R.string.request_auth_code_error);
                return;
            case RESULT_PARSE_FAILED:
                ToastUtils.showShort(R.string.request_parse_fail);
                return;
            case RESULT_SERVER_CODE_ERROR:
                ToastUtils.showShort(R.string.request_connection_fail);
                return;
            default:
                break;
        }
    }

    @Override
    public void onFinish() {

    }
}
