package cn.longmaster.hospital.doctor.data.repository;

import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.data.DoctorDataSource;
import cn.longmaster.hospital.doctor.data.local.DoctorLocalDataSource;
import cn.longmaster.hospital.doctor.data.remote.DoctorRemoteDataSource;

/**
 * @author ABiao_Abiao
 * @date 2019/8/15 14:35
 * @description:
 */
public class DoctorRepository implements DoctorDataSource {
    private static DoctorRepository INSTANCE;
    private DoctorDataSource remoteDataSource;
    private DoctorDataSource localDataSource;

    private DoctorRepository(DoctorDataSource remoteDataSource, DoctorDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    boolean mCacheIsDirty = false;

    public static DoctorRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new DoctorRepository(new DoctorRemoteDataSource(), new DoctorLocalDataSource());
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getDoctorBaseInfo(int id, String token, OnResultCallback<DoctorBaseInfo> onResultCallback) {
        localDataSource.getDoctorBaseInfo(id, token, new OnResultCallback<DoctorBaseInfo>() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                onResultCallback.onSuccess(doctorBaseInfo);
                localDataSource.saveDoctorBaseInfo(doctorBaseInfo);
            }

            @Override
            public void onFail(String msg) {
                remoteDataSource.getDoctorBaseInfo(id, token, onResultCallback);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void saveDoctorBaseInfo(DoctorBaseInfo doctorBaseInfo) {

    }
}
