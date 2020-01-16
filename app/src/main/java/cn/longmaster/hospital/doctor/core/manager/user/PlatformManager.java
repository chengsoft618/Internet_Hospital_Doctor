package cn.longmaster.hospital.doctor.core.manager.user;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.user.PlatformInfo;
import cn.longmaster.hospital.doctor.core.entity.user.PlatformListItem;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.user.PlatformCostAddRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PlatformCostDelRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PlatformCostDetailGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PlatformCostListGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PlatformCostModRequest;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 20:58
 * @description: 平台服务费
 */
public class PlatformManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    /**
     * 100563
     */
    public void getPlatformList(int pageIndex, int pageSize, String hospital, OnResultCallback<List<PlatformListItem>> onResultListener) {
        PlatformCostListGetRequest request = new PlatformCostListGetRequest(onResultListener);
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setHospital(hospital);
        request.start();
    }

    /**
     * 100564
     */
    public void getPlatformById(String id, OnPlatformLoadListener onPlatformLoadListener) {
        PlatformCostDetailGetRequest request = new PlatformCostDetailGetRequest(new DefaultResultCallback<PlatformInfo>() {
            @Override
            public void onSuccess(PlatformInfo info, BaseResult baseResult) {
                onPlatformLoadListener.onSuccess(info);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                onPlatformLoadListener.onFail(result.getMsg());
            }
        });
        request.setId(id);
        request.start();
    }

    /**
     * 100565
     */
    public void deletePlatform(String id, OnPlatformChangeListener listener) {
        PlatformCostDelRequest request = new PlatformCostDelRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("删除平台服务费成功");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFail("删除平台服务费失败");
            }
        });
        request.setId(id);
        request.start();
    }

    public void savePlatform(PlatformInfo info, OnPlatformChangeListener listener) {
        if (StringUtils.isTrimEmpty(info.getId())) {
            createNewPlatform(info, listener);
        } else {
            updatePlatform(info, listener);
        }
    }

    private void updatePlatform(PlatformInfo info, OnPlatformChangeListener listener) {
        PlatformCostModRequest request = new PlatformCostModRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("修改平台服务费成功");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFail("修改平台服务费失败，请稍后重试");
            }
        });
        request.setId(info.getId());
        request.setAdvanceValue(info.getAdvanceValue());
        request.setAmtDt(info.getAmtDt());
        request.setFileName(info.getFileName());
        request.start();
    }

    private void createNewPlatform(PlatformInfo info, OnPlatformChangeListener listener) {
        PlatformCostAddRequest request = new PlatformCostAddRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("添加平台服务费成功");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFail("添加平台服务费失败，请稍后重试");
            }
        });
        request.setHospitalId(info.getHospitalId());
        request.setAdvanceValue(info.getAdvanceValue());
        request.setDepartmentId(info.getDepartmentId());
        request.setAmtDt(info.getAmtDt());
        request.setFileName(info.getFileName());
        request.start();
    }

    public interface OnPlatformListLoadListener {
        void onSuccess(List<PlatformListItem> platformListItems, boolean isFinish);

        void onFail(String msg);
    }

    public interface OnPlatformLoadListener {
        void onSuccess(PlatformInfo platformInfo);

        void onFail(String msg);
    }

    public interface OnPlatformChangeListener {
        void onSuccess(String msg);

        void onFail(String msg);
    }
}
