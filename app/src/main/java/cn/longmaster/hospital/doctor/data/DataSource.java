package cn.longmaster.hospital.doctor.data;

/**
 * @author ABiao_Abiao
 * @date 2019/7/31 13:52
 * @description:
 */
public interface DataSource {
    interface OnResultCallback<D> {
        void onSuccess(D d);

        void onFail(String msg);

        void onFinish();
    }
}
