package cn.longmaster.hospital.doctor.core.manager;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;

/**
 * 百度定位
 * Created by Yang² on 2017/2/16.
 */

public class LocationManager extends BaseManager {
    private static final String TAG = LocationManager.class.getSimpleName();
    private AppApplication mApplication;
    private int mAppointmentId;
    private LocationClient mLocationClient = null;//百度定位核心类
    //    private int mCount;//失败计数，连续失败3次内，从新获取位置
    private boolean mGetLocation;//退出诊室后，置为false
    private OnGetLocationListener mOnGetLocationListener;

    @Override
    public void onManagerCreate(AppApplication application) {
        mApplication = application;
        initLocation();
        regListener();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationClient = new LocationClient(mApplication);
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);//定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认不需要
        option.setIgnoreKillProcess(false);//默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);
    }

    private void regListener() {
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mLocationClient.stop();
                if (bdLocation != null) {
                    uploadPositionInfo(bdLocation);
                }
            }
        });
    }

    public void start(int appointmentId, OnGetLocationListener onGetLocationListener) {
        mOnGetLocationListener = onGetLocationListener;
        Logger.logD(Logger.COMMON, TAG + "->start()->");
        this.mAppointmentId = appointmentId;
//        mCount = 0;
        mGetLocation = true;
        mLocationClient.start();
    }

    public void start(int appointmentId) {
        start(appointmentId, null);
    }

    /**
     * 是否要获取地理位置信息
     * 退出诊室后设置为false
     *
     * @param getLocation
     */
    public void setGetLocation(boolean getLocation) {
        this.mGetLocation = getLocation;
    }

    private void uploadPositionInfo(BDLocation bdLocation) {
        if (!mGetLocation) {
            return;
        }
        if (TextUtils.isEmpty(bdLocation.getProvince()) && TextUtils.isEmpty(bdLocation.getCity()) &&
                TextUtils.isEmpty(bdLocation.getDistrict()) && TextUtils.isEmpty(bdLocation.getStreet())) {
//            mCount++;
//            Logger.logD(Logger.COMMON, TAG + "->uploadPositionInfo()->地理位置为空->mCount:" + mCount);
//            //如果没有获取到位置信息，重试两次
//            if (mCount < 3) {
//                AppHandlerProxy.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLocationClient.start();
//                    }
//                }, 5000);
//            }

            //获取不到位置，则回传空
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.onLocationGet(null);
            }
            return;
        }
//        mCount = 0;

        Logger.logD(Logger.COMMON, TAG + "->uploadPositionInfo()->bdLocation:" + bdLocation.getCountry()
                + bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getDistrict() + bdLocation.getStreet()
                + bdLocation.getAddrStr());

        if (getManager(DcpManager.class).getDcpInterface() == null || mAppointmentId == 0) {
            Logger.logD(Logger.COMMON, TAG + "->uploadPositionInfo()->dcp接口未初始化!!");
            return;
        }

        if (mOnGetLocationListener != null) {
            mOnGetLocationListener.onLocationGet(bdLocation);
        }

        try {
            JSONObject locationJson = new JSONObject();
            locationJson.put("_appID", mAppointmentId);
            locationJson.put("_latitude", String.valueOf(bdLocation.getLatitude()));
            locationJson.put("_longitude", String.valueOf(bdLocation.getLongitude()));
            Logger.logD(Logger.COMMON, TAG + "->uploadPositionInfo()->locationJson:" + locationJson.toString());
            getManager(DcpManager.class).getDcpInterface().Request(DcpFuncConfig.FUN_NAME_UPLOAD_LOCATION_INFO, locationJson.toString());

            JSONObject positionJson = new JSONObject();
            positionJson.put("_appID", mAppointmentId);
            positionJson.put("_province", bdLocation.getProvince());
            positionJson.put("_city", bdLocation.getCity());
            positionJson.put("_district", bdLocation.getDistrict());
            positionJson.put("_streets", bdLocation.getStreet());
            Logger.logD(Logger.COMMON, TAG + "->uploadPositionInfo()->positionJson:" + positionJson.toString());
            getManager(DcpManager.class).getDcpInterface().Request(DcpFuncConfig.FUN_NAME_UPLOAD_POSITION_INFO, positionJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnGetLocationListener {
        void onLocationGet(BDLocation bdLocation);
    }

}
