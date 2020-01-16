package cn.longmaster.hospital.doctor.core.manager.consult;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.OMMap;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.AppointmentContract.AppointmentEntry;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointRelateInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentItemInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentOrderInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.LaunchConsultInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForSelectInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AppointmentStatisticDataInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.AppointmentByIdRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.AppointmentListByDoctorRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.AppointmentOrderRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.LaunchConsultRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleAppointmentRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScreenAppointmentRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.UserRecordRequester;
import cn.longmaster.hospital.doctor.core.requests.config.PatientBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.doctor.AppointmentStatisticDataRequester;

/**
 * 患者管理
 * Created by JinKe on 2016-08-02.
 */
public class ConsultManager extends BaseManager {
    private static final String TAG = ConsultManager.class.getSimpleName();
    private OMMap<Integer, OnResultListener> mAppointmentRequester = new OMMap<>();
    private SparseArray<PatientInfo> patientInfoSparseArray = new SparseArray<>(100);

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    /**
     * 新增会诊
     *
     * @param formForConsult
     * @param onResultCallback
     */
    public void addConsult(FormForConsult formForConsult, OnResultCallback<LaunchConsultInfo> onResultCallback) {
        LaunchConsultRequester requester = new LaunchConsultRequester(onResultCallback);
        requester.setFormForConsult(formForConsult);
        requester.doPost();
    }

    /**
     * 获取患者相关的预约
     *
     * @param patientId 患者id
     * @param symbol    分页参数
     * @param pageSize  分页尺寸
     * @param listener  回调
     */
    public void getPatientRecord(int patientId, int symbol, int pageSize, OnResultCallback<List<AppointmentItemForSelectInfo>> listener) {
        UserRecordRequester recorderRequester = new UserRecordRequester(listener);
        recorderRequester.userNumId = patientId;
        recorderRequester.userType = 1;
        recorderRequester.symbol = symbol;
        recorderRequester.pageSize = pageSize;
        recorderRequester.doPost();
    }

    /**
     * 筛选患者预约ID
     *
     * @param doctorId    医生id
     * @param promoter    发起人类型
     * @param serviceType 就诊类型
     * @param statNum     预约状态
     * @param recure      是否复诊
     * @param keyWords    关键字
     * @param symbol      分页参数
     * @param pageSize    分页尺寸
     * @param listener    回调
     */
    public void getScreenAppointmentIDs(int doctorId, int promoter, int scheduingType, int serviceType, String statNum, String receiveStatNum, int recure, String keyWords, int symbol, int pageSize, int sameDep, final OnResultCallback<List<AppointmentItemInfo>> listener) {
        ScreenAppointmentRequester requester = new ScreenAppointmentRequester(listener);
        requester.setDoctorId(doctorId);
        requester.setIsSale(AppConstant.IsSale.NO);
        requester.setPromoter(promoter);
        requester.setScheduingType(scheduingType);
        requester.setServiceType(serviceType);
        requester.setStatNum(statNum);
        requester.setReceiveStatNum(receiveStatNum);
        requester.setRecure(recure);
        requester.setKeyWords(keyWords);
        requester.setSymbol(symbol);
        requester.setPageSize(pageSize);
        requester.setSameDep(sameDep);
        requester.doPost();
    }

    /**
     * 获取排班预约列表
     *
     * @param scheduingId
     * @param symbol
     * @param pageSize
     * @param listener
     */
    public void getScheduleAppointIDs(int scheduingId, int symbol, int pageSize, final OnResultCallback<List<Integer>> listener) {
        ScheduleAppointmentRequester requester = new ScheduleAppointmentRequester(listener);
        requester.scheduingId = scheduingId;
        requester.symbol = symbol;
        requester.pageSize = pageSize;
        requester.doPost();
    }

    /**
     * 根据预约id获取预约信息
     * 先获取本地数据的数据,如果没有为null就从服务器拉取
     *
     * @param appointmentId 预约id
     * @param listener
     */
    public void getAppointmentInfo(final int appointmentId, final OnResultListener<AppointmentInfo> listener) {
        AppointmentByIdRequester requester = new AppointmentByIdRequester(new DefaultResultCallback<AppointmentInfo>() {
            @Override
            public void onSuccess(AppointmentInfo appointmentDetail, BaseResult baseResult) {
                listener.onResult(baseResult, appointmentDetail);
            }
        });
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    /**
     * 根据预约id获取预约信息
     *
     * @param appointmentId
     * @param listener
     */
    public void getAppointmentInfo(final int appointmentId, final OnAppointmentInfoLoadListener listener) {
        AppointmentByIdRequester requester = new AppointmentByIdRequester(new DefaultResultCallback<AppointmentInfo>() {
            @Override
            public void onSuccess(AppointmentInfo appointmentDetail, BaseResult baseResult) {
                listener.onSuccess(appointmentDetail);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFailed(result.getCode(), result.getMsg());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listener.onFinish();
            }
        });
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    /**
     * 根据预约id获取预约信息
     * 先获取本地数据的数据,如果没有为null就从服务器拉取
     *
     * @param appointmentId 预约id
     * @param force
     * @param listener
     */
    public void getAppointmentInfo(final int appointmentId, final boolean force, final OnResultListener<AppointmentInfo> listener) {
        getAppointmentForDB(appointmentId, appointmentInfo -> {
            if (appointmentInfo != null) {
                BaseResult baseResult = new BaseResult();
                baseResult.setCode(0);
                listener.onResult(baseResult, appointmentInfo);
                if (!force) {
                    return;
                }
            }

            if (mAppointmentRequester.containsKey(appointmentId)) {
                mAppointmentRequester.put(appointmentId, listener);
                return;
            }

            mAppointmentRequester.put(appointmentId, listener);
            AppointmentByIdRequester requester = new AppointmentByIdRequester(new DefaultResultCallback<AppointmentInfo>() {
                @Override
                public void onSuccess(AppointmentInfo appointmentDetail, BaseResult baseResult) {
                    if (baseResult.getCode() == RESULT_SUCCESS) {
                        saveAppointment(appointmentId, appointmentInfo);
                    }

                    List<OnResultListener> listeners = mAppointmentRequester.remove(appointmentId);
                    for (OnResultListener resultListener : listeners) {
                        resultListener.onResult(baseResult, appointmentInfo);
                    }
                }
            });
            requester.appointmentId = appointmentId;
            requester.doPost();

        });
    }


    /**
     * 预约信息存入数据库
     *
     * @param appointmentId   预约id
     * @param appointmentInfo 预约的信息
     */
    public void saveAppointment(final int appointmentId, final AppointmentInfo appointmentInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->saveAppointment()->appointmentId:" + appointmentId + ", appointmentInfo:" + appointmentInfo);
        if (appointmentInfo == null) {
            return;
        }
        DatabaseTask<Void> task = new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(AppointmentEntry.COLUMN_NAME_CONTENT, JsonHelper.toJSONObject(appointmentInfo).toString());
                    if (appointmentInfo.getBaseInfo() != null) {
                        values.put(AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID, appointmentInfo.getBaseInfo().getAppointmentId());
                        values.put(AppointmentEntry.COLUMN_NAME_APPOINTMENT_STATE, appointmentInfo.getBaseInfo().getAppointmentStat());
                        values.put(AppointmentEntry.COLUMN_NAME_STATE_REASON, appointmentInfo.getBaseInfo().getStatReason());
                        values.put(AppointmentEntry.COLUMN_NAME_CHECK_STATE, appointmentInfo.getBaseInfo().getIsMaterialPass());
                    }
                    if (appointmentInfo.getExtendsInfo() != null) {
                        values.put(AppointmentEntry.COLUMN_NAME_SERVICE_TYPE, appointmentInfo.getExtendsInfo().getServiceType());
                        values.put(AppointmentEntry.COLUMN_NAME_SCHEDULE_TYPE, appointmentInfo.getExtendsInfo().getScheduingType());
                        values.put(AppointmentEntry.COLUMN_NAME_PAY_DT, appointmentInfo.getExtendsInfo().getRecureRemindDt());
                    }
                    database.delete(AppointmentEntry.TABLE_NAME, AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID + " =?", new String[]{String.valueOf(appointmentId)});
                    Logger.logD(Logger.APPOINTMENT, TAG + "saveAppointment->AppointmentInfo存入数据->库基本配置信息:" + values.toString());
                    database.insert(AppointmentEntry.TABLE_NAME, null, values);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }

                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {
            }
        };
        getManager(DBManager.class).submitDatabaseTask(task);
    }

    /**
     * 数据库获取预约信息
     *
     * @param appointmentId 预约id
     * @param listener      回调
     */
    public void getAppointmentForDB(final int appointmentId, final OnGetDateListener listener) {
        DatabaseTask<AppointmentInfo> task = new DatabaseTask<AppointmentInfo>() {
            @Override
            public AsyncResult<AppointmentInfo> runOnDBThread(AsyncResult<AppointmentInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                AppointmentInfo appointmentInfo = null;
                try {
                    String sql = "SELECT * FROM " + AppointmentEntry.TABLE_NAME + " WHERE " + AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(appointmentId)});
                    Logger.logD(Logger.APPOINTMENT, TAG + "getBaseConfigFormDB数据消息总条数：" + cursor.getCount());
                    while (cursor.moveToNext()) {
                        String content = cursor.getString(cursor.getColumnIndex(AppointmentEntry.COLUMN_NAME_CONTENT));
                        appointmentInfo = JsonHelper.toObject(new JSONObject(content), AppointmentInfo.class);
                    }
                    asyncResult.setData(appointmentInfo);
                    database.setTransactionSuccessful();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<AppointmentInfo> asyncResult) {
                listener.onGetDate(asyncResult.getData());
            }
        };
        getManager(DBManager.class).submitDatabaseTask(task);
    }


    /**
     * 获取患者信息
     * 先获取本地数据的数据,如果没有为null就从服务器拉取
     *
     * @param appointmentId 预约id
     * @param listener      回调
     */
    public void getPatientInfo(final int appointmentId, final OnPatientInfoLoadListener listener) {
        PatientBaseRequester requester = new PatientBaseRequester(new DefaultResultCallback<PatientInfo>() {
            @Override
            public void onSuccess(PatientInfo patientInfo, BaseResult baseResult) {
                listener.onSuccess(patientInfo);
                patientInfoSparseArray.put(appointmentId, patientInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                listener.onFailed(result.getCode(), result.getMsg());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listener.onFinish();
            }
        });
        requester.token = "0";
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    public void clearPatientInfo() {
        patientInfoSparseArray.clear();
    }

    public void refreshPatientInfo(int appointmentId) {
        patientInfoSparseArray.remove(appointmentId);
    }

    /**
     * 获取预约统计数据
     *
     * @param date     日期
     * @param listener 回调接口
     */
    public void getAppointmentStatisticsData(String date, int filterType, String filterDate, OnResultListener<AppointmentStatisticDataInfo> listener) {
        AppointmentStatisticDataRequester requester = new AppointmentStatisticDataRequester(listener);
        requester.doctorId = getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        requester.filterType = filterType;
        requester.filterDate = filterDate;
        requester.doPost();
    }

    /**
     * 根据医生ID和日期拉取医生的预约列表
     */
    public void getAppointmentListByFilterAndDate(int doctorId, int filterType, String datetime, int promoter, int scheduingType, final OnResultListener<AppointRelateInfo> listener) {
        AppointmentListByDoctorRequester requester = new AppointmentListByDoctorRequester(listener);
        requester.doctorId = doctorId;
        requester.filterType = filterType;
        requester.datetime = datetime;
        requester.promoter = promoter;
        requester.scheduingType = scheduingType;
        requester.doPost();

    }

    /**
     * 获取就诊总量
     *
     * @param listener
     */
    public void getAppointmentStatisticsData(final OnResultListener<AppointmentStatisticDataInfo> listener) {
        AppointmentStatisticDataRequester requester = new AppointmentStatisticDataRequester(listener);
        requester.doctorId = getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        requester.filterType = AppointmentStatisticDataRequester.FILTER_TYPE_ALL;
        requester.filterDate = "0";
        requester.doPost();
    }

    /**
     * 根据预约ID拉取预约订单信息
     *
     * @param listener
     */
    public void getAppointmentOrder(int appointmentId, final OnResultListener<AppointmentOrderInfo> listener) {
        AppointmentOrderRequester requester = new AppointmentOrderRequester(listener);
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    public interface OnGetDateListener {
        void onGetDate(AppointmentInfo appointmentInfoList);
    }

    public interface OnPatientInfoLoadListener {
        void onSuccess(PatientInfo patientInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnAppointmentInfoLoadListener {
        void onSuccess(AppointmentInfo appointmentInfo);

        void onFailed(int code, String msg);

        void onFinish();
    }

    public interface OnRelateRecordInfoListLoadListener {
        void onSuccess(List<AppointmentItemForSelectInfo> appointmentItemForSelectInfos, boolean isFinish);

        void onFailed(int code, String msg);

        void onFinish();
    }
}
