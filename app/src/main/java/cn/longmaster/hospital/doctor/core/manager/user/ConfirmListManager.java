package cn.longmaster.hospital.doctor.core.manager.user;

import java.util.List;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.user.ConfirmInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ConfirmListItem;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.user.ConfirmAddRequest;
import cn.longmaster.hospital.doctor.core.requests.user.ConfirmDetailGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.ConfirmListGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.ConfirmModRequest;
import cn.longmaster.hospital.doctor.core.requests.user.ConfrimDeleteRequest;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/15 21:29
 * @description: 确认单
 */
public class ConfirmListManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    /**
     * 100569 获取当前用户上传的确认单列表
     *
     * @param pageIndex
     * @param pageSize
     * @param hospital
     * @param platformListLoadListener
     */
    public void getConfirmList(int pageIndex, int pageSize, String hospital, OnResultCallback<List<ConfirmListItem>> platformListLoadListener) {
        ConfirmListGetRequest request = new ConfirmListGetRequest(platformListLoadListener);
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setKeyWords(hospital);
        request.start();
    }

    /**
     * 100570
     */
    public void getConfirmById(String id, int type, OnConfirmLoadListener onConfirmLoadListener) {
        ConfirmDetailGetRequest request = new ConfirmDetailGetRequest(new DefaultResultCallback<ConfirmInfo>() {
            @Override
            public void onSuccess(ConfirmInfo confirmInfo, BaseResult baseResult) {
                onConfirmLoadListener.onSuccess(confirmInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                onConfirmLoadListener.onFail("获取确认单详情失败，请稍后重试");
            }
        });
        request.setId(id);
        request.setType(type);
        request.start();
    }

    /**
     * 100571
     */
    public void deleteConfirm(String id, int type, OnConfirmChangeListener listener) {
        ConfrimDeleteRequest request = new ConfrimDeleteRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("删除确认单成功");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onSuccess("删除确认单失败");
            }
        });
        request.setId(id);
        request.setType(type);
        request.start();
    }

    public void saveConfirm(ConfirmInfo info, OnConfirmChangeListener listener) {
        if (StringUtils.isTrimEmpty(info.getId())) {
            createNewPlatform(info, listener);
        } else {
            updatePlatform(info, listener);
        }
    }

    /**
     * 100572
     *
     * @param info
     * @param listener
     */
    private void updatePlatform(ConfirmInfo info, OnConfirmChangeListener listener) {
        ConfirmModRequest request = new ConfirmModRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                listener.onSuccess("修改确认单成功");

            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == 102) {
                    listener.onFail("该就诊已上传垫付单");
                } else if (result.getCode() == -102) {
                    listener.onFail("该就诊编号不存在");
                } else {
                    listener.onFail("修改确认单失败");
                }
            }
        });
        request.setId(info.getId());
        request.setType(info.getType());
        request.setPayValue(info.getPayValue());
        request.setFileName(info.getFileName());
        request.start();
    }

    /**
     * 100573
     *
     * @param info
     * @param listener
     */
    private void createNewPlatform(ConfirmInfo info, OnConfirmChangeListener listener) {
        ConfirmAddRequest request = new ConfirmAddRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult msg) {
                listener.onSuccess("添加确认单成功");
            }

            @Override
            public void onFail(BaseResult baseResult) {
                super.onFail(baseResult);
                if (baseResult.getCode() == 102) {
                    listener.onFail("该就诊已上传垫付单");
                } else if (baseResult.getCode() == -102) {
                    listener.onFail("该就诊编号不存在");
                } else {
                    listener.onFail("添加确认单失败");
                }
            }
        });
        request.setAppointmentId(info.getAppointmentId());
        request.setHospitalId(info.getAttHospitalId());
        request.setPayValue(info.getPayValue());
        request.setFileName(info.getFileName());
        request.start();
    }

    public interface OnConfirmLoadListener {
        void onSuccess(ConfirmInfo confirmInfo);

        void onFail(String msg);
    }

    public interface OnConfirmChangeListener {
        void onSuccess(String msg);

        void onFail(String msg);
    }
}
