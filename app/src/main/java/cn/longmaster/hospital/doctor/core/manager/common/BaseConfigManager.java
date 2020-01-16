package cn.longmaster.hospital.doctor.core.manager.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.BaseConfigContract;
import cn.longmaster.hospital.doctor.core.entity.common.BannerAndQuickEnterInfo;
import cn.longmaster.hospital.doctor.core.entity.common.BaseConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.common.NationConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.common.ProvinceCityInfo;
import cn.longmaster.hospital.doctor.core.entity.common.RutineConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.MaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AreaGradeInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.GradePriceInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.PackageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.TitleGradeInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.config.AreaGradeRequester;
import cn.longmaster.hospital.doctor.core.requests.config.BannerQuickEnterRequester;
import cn.longmaster.hospital.doctor.core.requests.config.DepartmentRequester;
import cn.longmaster.hospital.doctor.core.requests.config.DoctorBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.config.GradePriceRequester;
import cn.longmaster.hospital.doctor.core.requests.config.HospitalRequester;
import cn.longmaster.hospital.doctor.core.requests.config.MaterialRequester;
import cn.longmaster.hospital.doctor.core.requests.config.NationConfigRequester;
import cn.longmaster.hospital.doctor.core.requests.config.PackageRequester;
import cn.longmaster.hospital.doctor.core.requests.config.PatientBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.config.ProvinceCityConfigRequester;
import cn.longmaster.hospital.doctor.core.requests.config.RequestParams;
import cn.longmaster.hospital.doctor.core.requests.config.RutineConfigRequester;
import cn.longmaster.hospital.doctor.core.requests.config.TitleGradeRequester;
import cn.longmaster.utils.StringUtils;

import static cn.longmaster.hospital.doctor.core.manager.user.AvatarManager.VISUALIZE_AVATAR_SUFFIX;

/**
 * 基本配置管理
 * Created by Yang² on 2016/7/26.
 */
public class BaseConfigManager extends BaseManager {
    private final String TAG = BaseConfigManager.class.getSimpleName();

    @Override
    public void onManagerCreate(AppApplication application) {

    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    /**
     * 获取基本配置信息
     * <p/>
     * RequestParams中配置不同的类型可以请求以下几种配置信息
     * 医院信息
     * 系统材料配置
     * 科室信息
     * 医生职称等级配置
     * 医生区域等级配置
     * 套餐信息配置
     * 价格档位配置
     * 常规配置
     * 医生基本信息
     * 患者基本信息
     * 客户端Banner与快捷入口配置
     * 民族信息配置
     * 省份城市信息配置
     *
     * @param params
     * @param listener
     */
    public void getBaseConfigFromNet(RequestParams params, OnResultListener listener) {
        Logger.logD(Logger.COMMON, TAG + "->getBaseConfigFromNet()->params:" + params);
        switch (params.getType()) {
            case OpTypeConfig.CLIENTAPI_OPTYE_HOSPITAL_INFO:
                getHospitalInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_MATERIAL_INFO:
                getMaterialInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_DEPARTMENT_INFO:
                getDepartmentInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_TITLE_GRADE_INFO:
                getTitleGradeInfoList(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_AREA_GRADE_INFO:
                getAreaGradeInfoList(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PACKAGE_INFO:
                getPackageInfoList(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_GRADE_PRICE_INFO:
                getGradePriceInfoList(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_RUTINE_CONFIG:
                getRutineConfigInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_BASE_INFO:
                getDoctorBaseInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PATIENT_BASE_INFO:
                getPatientBaseInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_BANNER_AND_QUICK_ENTRY:
                getBannerQuickEnterInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_NATION_INFO_CONFIG:
                getNationConfigInfo(params, listener);
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PRIVINCE_CITY_INFO_CONFIG:
                getProvinceCityConfigInfo(params, listener);
                break;

            default:
                if (AppConfig.IS_DEBUG_MODE) {
                    throw new RuntimeException("不支持的类型");
                }
        }
    }

    /**
     * 医院信息
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getHospitalInfo(final RequestParams params, final OnResultListener listener) {
        HospitalRequester requester = new HospitalRequester(new OnResultListener<HospitalInfo>() {
            @Override
            public void onResult(BaseResult baseResult, HospitalInfo hospitalInfo) {
                listener.onResult(baseResult, hospitalInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && hospitalInfo != null) {
                    saveData(baseResult.getOpType(), params.getHospitalId(), baseResult.getToken(), JsonHelper.toJSONObject(hospitalInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.hospitalId = params.getHospitalId();
        requester.doPost();
    }

    /**
     * 系统材料配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getMaterialInfo(final RequestParams params, final OnResultListener listener) {
        MaterialRequester requester = new MaterialRequester(new OnResultListener<MaterialInfo>() {
            @Override
            public void onResult(BaseResult baseResult, MaterialInfo materialInfo) {
                listener.onResult(baseResult, materialInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && materialInfo != null) {
                    saveData(baseResult.getOpType(), params.getMaterialId(), baseResult.getToken(), JsonHelper.toJSONObject(materialInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.materialId = params.getHospitalId();
        requester.doPost();
    }

    /**
     * 科室信息
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getDepartmentInfo(final RequestParams params, final OnResultListener listener) {
        DepartmentRequester requester = new DepartmentRequester(new OnResultListener<DepartmentInfo>() {
            @Override
            public void onResult(BaseResult baseResult, DepartmentInfo departmentInfo) {
                listener.onResult(baseResult, departmentInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && departmentInfo != null) {
                    saveData(baseResult.getOpType(), params.getDepartmentId(), baseResult.getToken(), JsonHelper.toJSONObject(departmentInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.departmentId = params.getDepartmentId();
        requester.doPost();
    }

    /**
     * 医生职称等级配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getTitleGradeInfoList(final RequestParams params, final OnResultListener listener) {
        TitleGradeRequester requester = new TitleGradeRequester(new OnResultListener<List<TitleGradeInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<TitleGradeInfo> titleGradeInfoList) {
                listener.onResult(baseResult, titleGradeInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && titleGradeInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(titleGradeInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 医生区域等级配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getAreaGradeInfoList(final RequestParams params, final OnResultListener listener) {
        AreaGradeRequester requester = new AreaGradeRequester(new OnResultListener<List<AreaGradeInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<AreaGradeInfo> areaGradeInfoList) {
                listener.onResult(baseResult, areaGradeInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && areaGradeInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(areaGradeInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 套餐信息配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getPackageInfoList(final RequestParams params, final OnResultListener listener) {
        PackageRequester requester = new PackageRequester(new OnResultListener<List<PackageInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<PackageInfo> packageInfoList) {
                listener.onResult(baseResult, packageInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && packageInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(packageInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 价格档位配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getGradePriceInfoList(final RequestParams params, final OnResultListener listener) {
        GradePriceRequester requester = new GradePriceRequester(new OnResultListener<List<GradePriceInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<GradePriceInfo> gradePriceInfoList) {
                listener.onResult(baseResult, gradePriceInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && gradePriceInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(gradePriceInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 常规配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getRutineConfigInfo(final RequestParams params, final OnResultListener listener) {
        RutineConfigRequester requester = new RutineConfigRequester(new OnResultListener<RutineConfigInfo>() {
            @Override
            public void onResult(BaseResult baseResult, RutineConfigInfo rutineConfigInfo) {
                listener.onResult(baseResult, rutineConfigInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && rutineConfigInfo != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONObject(rutineConfigInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 医生基本信息
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getDoctorBaseInfo(final RequestParams params, final OnResultListener listener) {
        DoctorBaseRequester requester = new DoctorBaseRequester(new OnResultListener<DoctorBaseInfo>() {
            @Override
            public void onResult(BaseResult baseResult, DoctorBaseInfo doctorBaseInfo) {
                listener.onResult(baseResult, doctorBaseInfo);
                if (baseResult.getCode() == RESULT_SUCCESS && doctorBaseInfo != null) {
                    saveData(baseResult.getOpType(), params.getDoctorId(), baseResult.getToken(), JsonHelper.toJSONObject(doctorBaseInfo).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doctorId = params.getDoctorId();
        requester.doPost();
    }

    /**
     * 患者基本信息
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getPatientBaseInfo(final RequestParams params, final OnResultListener listener) {
        PatientBaseRequester requester = new PatientBaseRequester(new DefaultResultCallback<PatientInfo>() {
            @Override
            public void onSuccess(PatientInfo patientInfo, BaseResult baseResult) {
                listener.onResult(baseResult, patientInfo);
                if (patientInfo != null) {
                    saveData(baseResult.getOpType(), params.getAppointmentId(), baseResult.getToken(), JsonHelper.toJSONObject(patientInfo).toString());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //listener.onResult(null, null);
            }
        });
        requester.token = params.getToken();
        requester.appointmentId = params.getAppointmentId();
        requester.doPost();
    }

    /**
     * 客户端Banner与快捷入口配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getBannerQuickEnterInfo(final RequestParams params, final OnResultListener listener) {
        BannerQuickEnterRequester requester = new BannerQuickEnterRequester(new OnResultListener<List<BannerAndQuickEnterInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<BannerAndQuickEnterInfo> bannerAndQuickEnterInfoList) {
                listener.onResult(baseResult, bannerAndQuickEnterInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && bannerAndQuickEnterInfoList != null) {
                    saveData(baseResult.getOpType(), params.getBannerType(), baseResult.getToken(), JsonHelper.toJSONArray(bannerAndQuickEnterInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.bannerType = params.getBannerType();
        requester.doPost();
    }

    /**
     * 民族信息配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getNationConfigInfo(final RequestParams params, final OnResultListener listener) {
        NationConfigRequester requester = new NationConfigRequester(new OnResultListener<List<NationConfigInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<NationConfigInfo> nationConfigInfoList) {
                listener.onResult(baseResult, nationConfigInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && nationConfigInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(nationConfigInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    /**
     * 省份城市信息配置
     *
     * @param params   请求参数
     * @param listener 回调接口
     */
    private void getProvinceCityConfigInfo(final RequestParams params, final OnResultListener listener) {
        ProvinceCityConfigRequester requester = new ProvinceCityConfigRequester(new OnResultListener<List<ProvinceCityInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<ProvinceCityInfo> provinceCityInfoList) {
                listener.onResult(baseResult, provinceCityInfoList);
                if (baseResult.getCode() == RESULT_SUCCESS && provinceCityInfoList != null) {
                    saveData(baseResult.getOpType(), 0, baseResult.getToken(), JsonHelper.toJSONArray(provinceCityInfoList).toString());
                }
            }
        });
        requester.token = params.getToken();
        requester.doPost();
    }

    private void saveData(final int type, final int dataId, final String token, final String content) {
        if (!TextUtils.isEmpty(content)) {
            DatabaseTask<Void> task = new DatabaseTask<Void>() {
                @Override
                public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    database.beginTransaction();
                    try {
                        ContentValues values = new ContentValues();
                        values.put(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE, type);
                        values.put(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_DATE_ID, dataId);
                        values.put(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TOKEN, token);
                        values.put(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_CONTENT, content);
                        database.delete(BaseConfigContract.BaseConfigEntry.TABLE_NAME, BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE + " =? AND " +
                                BaseConfigContract.BaseConfigEntry.COLUMN_NAME_DATE_ID + " =?", new String[]{String.valueOf(type), String.valueOf(dataId)});
                        Logger.logD(Logger.COMMON, TAG + "存入数据->库基本配置信息:" + values.toString());
                        database.insert(BaseConfigContract.BaseConfigEntry.TABLE_NAME, null, values);
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
    }

    /**
     * 读取基本配置信息数据库数据
     *
     * @param type     数据类型
     * @param listener 回调
     */
    public void getBaseConfigFromDB(final int type, final OnGetBaseConfigStateChangeListener listener) {
        DatabaseTask<BaseConfigInfo> task = new DatabaseTask<BaseConfigInfo>() {
            @Override
            public AsyncResult<BaseConfigInfo> runOnDBThread(AsyncResult<BaseConfigInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                try {
                    String sql = "SELECT * FROM " + BaseConfigContract.BaseConfigEntry.TABLE_NAME + " WHERE "
                            + BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE + " = ?";
                    cursor = database.rawQuery(sql, new String[]{String.valueOf(type)});
                    Logger.logD(Logger.COMMON, TAG + "getBaseConfigFormDB数据消息总条数：" + cursor.getCount());
                    BaseConfigInfo baseConfigInfo = new BaseConfigInfo();
                    String content = "";
                    while (cursor.moveToNext()) {
                        baseConfigInfo.setType(cursor.getInt(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE)));
                        baseConfigInfo.setDataId(cursor.getInt(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_DATE_ID)));
                        baseConfigInfo.setToken(cursor.getString(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TOKEN)));
                        content = cursor.getString(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_CONTENT));
                    }
                    baseConfigInfo.setData(parseDataByType(type, content));
                    Logger.logI(Logger.COMMON, TAG + "BaseConfigInfo查询到的数据:" + baseConfigInfo.toString());
                    asyncResult.setData(baseConfigInfo);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
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
            public void runOnUIThread(AsyncResult<BaseConfigInfo> asyncResult) {
                listener.onGetBaseConfigStateChanged(asyncResult.getData());
            }
        };
        getManager(DBManager.class).submitDatabaseTask(task);
    }

    /**
     * 获取基本配置
     *
     * @param type     数据类型
     * @param dataId   数据id
     * @param listener 回调接口
     */
    public void getBaseConfigFromDB(final int type, final int dataId, final OnGetBaseConfigStateChangeListener listener) {
        DatabaseTask<BaseConfigInfo> task = new DatabaseTask<BaseConfigInfo>() {
            @Override
            public AsyncResult<BaseConfigInfo> runOnDBThread(AsyncResult<BaseConfigInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                Cursor cursor = null;
                try {
                    cursor = database.rawQuery(BaseConfigContract.BaseConfigEntry.SQL, new String[]{String.valueOf(type), String.valueOf(dataId)});
                    Logger.logD(Logger.COMMON, TAG + "getBaseConfigByType数据消息总条数：" + cursor.getCount());
                    BaseConfigInfo baseConfigInfo = null;
                    while (cursor.moveToNext()) {
                        baseConfigInfo = new BaseConfigInfo();
                        baseConfigInfo.setType(cursor.getInt(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE)));
                        baseConfigInfo.setDataId(cursor.getInt(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_DATE_ID)));
                        baseConfigInfo.setToken(cursor.getString(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TOKEN)));
                        String content = cursor.getString(cursor.getColumnIndex(BaseConfigContract.BaseConfigEntry.COLUMN_NAME_CONTENT));
                        baseConfigInfo.setData(parseDataByType(type, content));
                    }

                    Logger.logI(Logger.COMMON, TAG + "BaseConfigInfo查询到的数据:" + baseConfigInfo);
                    asyncResult.setData(baseConfigInfo);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
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
            public void runOnUIThread(AsyncResult<BaseConfigInfo> asyncResult) {
                listener.onGetBaseConfigStateChanged(asyncResult.getData());
            }
        };
        getManager(DBManager.class).submitDatabaseTask(task);
    }

    private Object parseDataByType(int type, String content) {
        Logger.logD(Logger.COMMON, TAG + "->parseDataByType()->type:" + type + ", content:" + content);
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        switch (type) {
            case OpTypeConfig.CLIENTAPI_OPTYE_HOSPITAL_INFO:
                try {
                    return JsonHelper.toObject(new JSONObject(content), HospitalInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_MATERIAL_INFO:
                try {
                    return JsonHelper.toObject(new JSONObject(content), MaterialInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_DEPARTMENT_INFO:
                try {
                    return JsonHelper.toObject(new JSONObject(content), DepartmentInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_TITLE_GRADE_INFO:
                try {
                    return JsonHelper.toList(new JSONArray(content), TitleGradeInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_AREA_GRADE_INFO:
                try {
                    return JsonHelper.toList(new JSONArray(content), AreaGradeInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PACKAGE_INFO:
                try {
                    return JsonHelper.toList(new JSONArray(content), PackageInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_GRADE_PRICE_INFO:
                try {
                    return JsonHelper.toList(new JSONArray(content), GradePriceInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_RUTINE_CONFIG:
                try {
                    return JsonHelper.toObject(new JSONObject(content), RutineConfigInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_BASE_INFO:
                try {
                    return JsonHelper.toObject(new JSONObject(content), DoctorBaseInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PATIENT_BASE_INFO:
                try {
                    return JsonHelper.toObject(new JSONObject(content), PatientInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_BANNER_AND_QUICK_ENTRY:
                try {
                    return JsonHelper.toList(new JSONArray(content), BannerAndQuickEnterInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_NATION_INFO_CONFIG:
                try {
                    return JsonHelper.toList(new JSONArray(content), NationConfigInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case OpTypeConfig.CLIENTAPI_OPTYE_PRIVINCE_CITY_INFO_CONFIG:
                try {
                    return JsonHelper.toList(new JSONArray(content), ProvinceCityInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 删除信息
     *
     * @param type 数据类型
     */
    public void deleteData(int type) {
        deleteData(type, -1);
    }

    /**
     * 删除信息
     *
     * @param type 数据类型
     * @param id   数据id
     */
    public void deleteData(final int type, final int id) {
        DatabaseTask<Void> task = new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    if (id == -1) {
                        database.delete(BaseConfigContract.BaseConfigEntry.TABLE_NAME, BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE + " =? ", new String[]{String.valueOf(type)});
                    } else {
                        database.delete(BaseConfigContract.BaseConfigEntry.TABLE_NAME, BaseConfigContract.BaseConfigEntry.COLUMN_NAME_TYPE + " =?AND " +
                                BaseConfigContract.BaseConfigEntry.COLUMN_NAME_DATE_ID + " =?", new String[]{String.valueOf(type), String.valueOf(id)});
                    }
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
     * 清除缓存和本地文件
     *
     * @param id
     */
    public void deleteAvatar(int id) {
        Logger.logI(Logger.COMMON, "deleteAvatar-->清除头像缓存和本地文件");
        //清除缓存和本地文件
        String avatarFilePath = SdManager.getInstance().getAppointAvatarFilePath(id + "");
        //删除头像缓存
        FileUtil.deleteFile(avatarFilePath);
        //删除形象照
        FileUtil.deleteFile(avatarFilePath + VISUALIZE_AVATAR_SUFFIX);

    }


    public interface OnGetBaseConfigStateChangeListener {
        void onGetBaseConfigStateChanged(BaseConfigInfo baseConfigInfo);
    }
}
