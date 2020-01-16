package cn.longmaster.hospital.doctor.core.manager.user;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.entity.consultant.RepresentFunctionInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.RepresentFunctionList;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.RepresentFunctionRequester;
import cn.longmaster.utils.SPUtils;

import static cn.longmaster.hospital.doctor.core.AppPreference.KEY_REPRESENT_FUNCTION_CONTENT;
import static cn.longmaster.hospital.doctor.core.AppPreference.KEY_REPRESENT_FUNCTION_TOKEN;

/**
 * 销售代表功能管理类
 * Created by Yang² on 2017/11/28.
 */

public class RepresentFunctionManager extends BaseManager {

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    public void getRepresentFunction(OnResultListener<List<RepresentFunctionInfo>> listener) {
        String token = SPUtils.getInstance().getString(KEY_REPRESENT_FUNCTION_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            try {
                JSONArray data = new JSONArray(SPUtils.getInstance().getString(KEY_REPRESENT_FUNCTION_CONTENT));
                List<RepresentFunctionInfo> list = JsonHelper.toList(data, RepresentFunctionInfo.class);
                if (list != null) {
                    BaseResult baseResult = new BaseResult();
                    baseResult.setCode(0);
                    listener.onResult(baseResult, list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getRepresentFunctionFromNet(token, listener);
    }

    public void getRepresentFunctionFromNet(final String token, final OnResultListener<List<RepresentFunctionInfo>> listener) {
        RepresentFunctionRequester representFunctionRequester = new RepresentFunctionRequester(new OnResultListener<RepresentFunctionList>() {
            @Override
            public void onResult(BaseResult baseResult, RepresentFunctionList representFunctionList) {
                Logger.logI(Logger.COMMON, "getRepresentFunctionFromNet-->representFunctionList:" + representFunctionList);
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (!TextUtils.equals(token, representFunctionList.getToken())) {
                        SPUtils.getInstance().put(AppPreference.KEY_REPRESENT_FUNCTION_TOKEN, representFunctionList.getToken());
                        SPUtils.getInstance().put(AppPreference.KEY_REPRESENT_FUNCTION_CONTENT, JsonHelper.toJSONArray(representFunctionList.getRepresentFunctionList()).toString());
                        listener.onResult(baseResult, representFunctionList.getRepresentFunctionList());
                    }
                } else if (baseResult.getCode() != -100) {
                    listener.onResult(baseResult, null);
                }
            }
        });
        representFunctionRequester.token = token;
        representFunctionRequester.doPost();
    }
}
