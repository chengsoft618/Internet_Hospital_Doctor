package cn.longmaster.hospital.doctor.core.manager.common;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.ProvinceInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.config.ProvinceCityRequester;
import cn.longmaster.utils.LibCollections;

/**
 * @author ABiao_Abiao
 * @date 2019/11/11 17:50
 * @description:
 */
public class LocalManager extends BaseManager {
    private List<ProvinceInfo> mProvinceInfos;

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    public void getLocal(OnProvinceLoadListener listener) {
        if (LibCollections.isNotEmpty(mProvinceInfos)) {
            listener.onSuccess(mProvinceInfos);
        } else {
            ProvinceCityRequester requester = new ProvinceCityRequester(new DefaultResultCallback<List<ProvinceInfo>>() {
                @Override
                public void onSuccess(List<ProvinceInfo> provinceInfos, BaseResult baseResult) {
                    mProvinceInfos = provinceInfos;
                    listener.onSuccess(provinceInfos);
                }

                @Override
                public void onFail(BaseResult result) {
                    super.onFail(result);
                    listener.onFail(result.getCode(), result.getMsg());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    listener.onFinish();
                }
            });
            requester.doPost();
        }
    }

    public interface OnProvinceLoadListener {
        void onSuccess(List<ProvinceInfo> provinceInfos);

        void onFail(int code, String msg);

        void onFinish();
    }
}
