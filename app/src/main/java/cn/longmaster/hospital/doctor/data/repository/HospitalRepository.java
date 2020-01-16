package cn.longmaster.hospital.doctor.data.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;
import cn.longmaster.hospital.doctor.data.HospitalDataSource;
import cn.longmaster.hospital.doctor.data.local.HospitalLocalDataSource;
import cn.longmaster.hospital.doctor.data.remote.HospitalRemoteDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/7/29 15:05
 * @description:
 */
public class HospitalRepository implements HospitalDataSource {
    private static HospitalRepository INSTANCE;
    private HospitalDataSource remoteDataSource;
    private HospitalDataSource localDataSource;
    private Map<Integer, HospitalInfo> hospitalDetailInfo = new HashMap<>(100);

    private HospitalRepository(HospitalDataSource remoteDataSource, HospitalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    boolean mCacheIsDirty = false;

    public static HospitalRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new HospitalRepository(new HospitalRemoteDataSource(), new HospitalLocalDataSource());
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void loadHospitals(int pageIndex, int pageSize, String keyWords, OnResultCallback<List<HospitalInfo>> listener) {
        localDataSource.loadHospitals(pageIndex, pageSize, keyWords, new OnResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos) {
                listener.onSuccess(hospitalInfos);
            }

            @Override
            public void onFail(String msg) {
                remoteDataSource.loadHospitals(pageIndex, pageSize, keyWords, listener);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void getHospital(int hospitalId, OnResultCallback<HospitalInfo> listener) {
        localDataSource.getHospital(hospitalId, new OnResultCallback<HospitalInfo>() {
            @Override
            public void onSuccess(HospitalInfo hospitalInfo) {
                listener.onSuccess(hospitalInfo);
            }

            @Override
            public void onFail(String msg) {
                remoteDataSource.getHospital(hospitalId, listener);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void loadDepartments(int pageIndex, int pageSize, int hospitalId, OnResultCallback<List<DepartmentItem>> listener) {
        localDataSource.loadDepartments(pageIndex, pageSize, hospitalId, new OnResultCallback<List<DepartmentItem>>() {
            @Override
            public void onSuccess(List<DepartmentItem> departmentItems) {
                listener.onSuccess(departmentItems);
            }

            @Override
            public void onFail(String msg) {
                remoteDataSource.loadDepartments(pageIndex, pageSize, hospitalId, listener);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void loadDepartments(int hospitalId, String departmentId, OnResultCallback<List<DepartmentItem>> listener) {

    }
}
