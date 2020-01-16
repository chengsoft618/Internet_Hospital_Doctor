package cn.longmaster.hospital.doctor.core.manager.rounds;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetOrderListRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;

/**
 * @author ABiao_Abiao
 * @date 2019/10/29 10:56
 * @description: 查房相关
 */
public class RoundsManager extends BaseManager {
    @Override
    public void onManagerCreate(AppApplication application) {

    }

    public void getOrderList(int pageIndex, int pageSize, String orderState, boolean isSameDep, String keyWords, OnResultCallback<OrderListInfo> callback) {
        GetOrderListRequester requester = new GetOrderListRequester(callback);
        requester.setPageindex(pageIndex);
        requester.setPagesize(pageSize);
        requester.setOrderState(orderState);
        requester.setSameDep(isSameDep ? 1 : 0);
        requester.setKeyWords(keyWords);
        requester.doPost();
    }

    /**
     * 获取患者详细信息
     *
     * @param medicalId      病例ID
     * @param resultCallback 接口回调
     */
    public void getPatientDetail(int medicalId, OnResultCallback<RoundsMedicalDetailsInfo> resultCallback) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(resultCallback);
        requester.setMedicalId(medicalId);
        requester.doPost();
    }
}
