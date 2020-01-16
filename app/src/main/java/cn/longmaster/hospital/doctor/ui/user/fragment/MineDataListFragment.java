package cn.longmaster.hospital.doctor.ui.user.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.PersonalMaterialContract;
import cn.longmaster.hospital.doctor.core.entity.user.PersonalDataInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialInfo;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.DeletePersonalDataRequester;
import cn.longmaster.hospital.doctor.core.requests.user.PersonDataModRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PersonalDataRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.ui.LoginActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.user.MyDataBingDoctorActivity;
import cn.longmaster.hospital.doctor.ui.user.PDFActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientMaterialManagerActivity;
import cn.longmaster.hospital.doctor.ui.user.PersonalMaterialActivity;
import cn.longmaster.hospital.doctor.ui.user.RelationActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.MyDataAdapter;
import cn.longmaster.hospital.doctor.view.dialog.ChangeMyDataNameDialog;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author ABiao_Abiao
 * @date 2019/7/1 9:44
 * @description: 我的资料Fragment
 */
public class MineDataListFragment extends NewBaseFragment implements PersonalMaterialManager.UploadStateChangeListener {
    @FindViewById(R.id.activity_my_data)
    private RelativeLayout activityMyData;
    @FindViewById(R.id.fragment_my_data_srl)
    private SmartRefreshLayout fragmentMyDataSrl;
    @FindViewById(R.id.fragment_my_data_rv)
    private RecyclerView fragmentMyDataRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @FindViewById(R.id.fragment_mine_data_add_stv)
    private SuperTextView fragmentMineDataAddStv;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private PersonalMaterialManager mPersonalMaterialManager;
    @AppApplication.Manager
    private DBManager mDBManager;

    private MyDataAdapter myAdapter;
    private String mLocalPath;
    private boolean mIsShowFloatWindow = false;
    private ImageView mImageView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private boolean mExsitMianActivity;
    private static final String EXTRA_DATA_KEY_LOCAL_PATH = "_EXTRA_DATA_KEY_LOCAL_PATH_";
    private final int REQUEST_CODE_DELETE_FILE = 100; //删除
    public final int UP_DATE_TYPE_SET_VISIBLE = 1;
    public final int UP_DATE_TYPE_MOD_NAME = 2;

    public static MineDataListFragment getInstance(String stringExtra) {
        MineDataListFragment mineDataListFragment = new MineDataListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA_KEY_LOCAL_PATH, stringExtra);
        mineDataListFragment.setArguments(bundle);
        return mineDataListFragment;
    }

    @Override
    protected void initDatas() {
        myAdapter = new MyDataAdapter(R.layout.item_my_data, new ArrayList<>(0));
        myAdapter.setOnItemClickListener((adapter, view, position) -> {
            PersonalDataInfo info = (PersonalDataInfo) adapter.getItem(position);
            if (null != info) {
                openMaterial(info);
            }
        });
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PersonalDataInfo info = (PersonalDataInfo) adapter.getItem(position);
            if (null != info) {
                switch (view.getId()) {
                    case R.id.item_my_data_visible_tv:
                        setVisibility(position, info);
                        break;
                    case R.id.item_my_data_edit_name_tv:
                        modMaterialName(position, info);
                        break;
                    case R.id.item_my_data_relation_tv:
                        openRelation(info);
                        break;
                    case R.id.item_my_data_delete_tv:
                        showDeleteFileDialog(position, info);
                        break;
                    case R.id.item_my_data_sl:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void modMaterialName(final int position, PersonalDataInfo info) {
        ChangeMyDataNameDialog dataNameDialog = ChangeMyDataNameDialog.getInstance(info.getMaterialName());
        dataNameDialog.setOnDialogClickListener(new ChangeMyDataNameDialog.OnDialogClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(String msg) {
                updateMaterialName(position, info.getMaterialId(), msg);
            }
        });
        dataNameDialog.show(getFragmentManager(), "ChangeMyDataNameDialog");
    }

    private void setVisibility(final int position, PersonalDataInfo info) {
        updateMaterialDisplay(position, info.getMaterialId(), info.getSelfVisible());
    }

    private void openRelation(PersonalDataInfo info) {
        Intent intent = new Intent(getBaseActivity(), RelationActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, info.getMaterialId());
        startActivity(intent);
    }

    private void openMaterial(PersonalDataInfo info) {
        if (info.getType() == 3) {
            if (StringUtils.isEmpty(info.getContent())) {
                ToastUtils.showShort(info.getPpjResult());
                return;
            }
        }
        if (info.getContent().endsWith(".pdf")) {
            Intent intent = new Intent(getActivity(), PDFActivity.class);
            intent.putExtra("pdf_url", info.getContent());
            intent.putExtra("title", info.getMaterialName());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MY_DATA, true);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, info.getMaterialId());
            startActivityForResult(intent, REQUEST_CODE_DELETE_FILE);
        } else {
            getDisplay().startBrowserActivity(info.getContent(), info.getMaterialName()
                    , true, true, info.getMaterialId(), REQUEST_CODE_DELETE_FILE);
        }
    }

    /**
     * @param materialId   材料ID
     * @param materialName 新材料名
     * @param updateType   更新类型
     * @param selfVisible  当前可看状态
     */
    private void updateMaterial(int position, int materialId, String materialName, int updateType, int selfVisible) {
        PersonDataModRequest request = new PersonDataModRequest(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                if (updateType == UP_DATE_TYPE_SET_VISIBLE) {
                    myAdapter.setSelfVisible(position, selfVisible);
                } else {
                    myAdapter.setNewName(position, materialName);
                }
                ToastUtils.showShort(baseResult.getMsg());
            }
        });
        request.setMaterialId(materialId);
        request.setUpdateType(updateType);
        if (updateType == UP_DATE_TYPE_SET_VISIBLE) {
            request.setSelfVisible(selfVisible);
        } else {
            request.setMaterialName(materialName);
        }
        request.start();
    }

    private void updateMaterialDisplay(int position, int materialId, int selfVisible) {
        updateMaterial(position, materialId, null, UP_DATE_TYPE_SET_VISIBLE, selfVisible == 0 ? 1 : 0);
    }

    private void updateMaterialName(int position, int materialId, String materialName) {
        updateMaterial(position, materialId, materialName, UP_DATE_TYPE_MOD_NAME, 0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine_data;
    }

    @Override
    public void initViews(View rootView) {
        includeNewNoDataLl.setVisibility(View.VISIBLE);
        fragmentMyDataSrl.setVisibility(View.GONE);
        fragmentMyDataRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));

        fragmentMyDataRv.setAdapter(myAdapter);
        fragmentMyDataRv.setItemAnimator(new DefaultItemAnimator());
        fragmentMyDataSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX++;
                getPersonalData(PAGE_INDEX, false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX = MIN_PAGE_INDEX_1;
                getPersonalData(PAGE_INDEX, true, refreshLayout);
            }
        });
        fragmentMineDataAddStv.setOnClickListener(v -> {
            getDisplay().startBrowserActivity(AppConfig.getAdwsUrl() + "OpInstruction/index", "操作说明", false, false, 0, 0);
        });
        if (!StringUtils.isEmpty(mLocalPath)) {
            showUploadDialog(mLocalPath);
        }
        mPersonalMaterialManager.registerUploadStateChangeListener(this, true);
        initWindow();
        checkIsExsitMianActivity();
    }

    private void initWindow() {
        mImageView = new ImageView(getBaseActivity());
        mImageView.setImageResource(R.drawable.ic_upload_my_data_window);
        mImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalMaterialActivity.class);
            startActivity(intent);
            mWindowManager.removeView(mImageView);
            mIsShowFloatWindow = false;
        });
        mWindowManager = (WindowManager) getBaseActivity().getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.width = DisplayUtil.dip2px(80);
        mParams.height = DisplayUtil.dip2px(80);
        mParams.gravity = Gravity.TOP | Gravity.RIGHT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.format = 1;
    }

    private void checkIsExsitMianActivity() {
        mExsitMianActivity = isExsitMianActivity(MainActivity.class);
        Logger.logI(Logger.APPOINTMENT, "MineDataListFragment->exsitMianActivity()->" + mExsitMianActivity);
        if (mExsitMianActivity) {
            fragmentMyDataSrl.autoRefresh();
            getUrl();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateMaterialTaskState();
                    fragmentMyDataSrl.autoRefresh();
                    getUrl();
                }
            }, 300);
        }
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    private boolean isExsitMianActivity(Class<?> cls) {
        Intent intent = new Intent(getBaseActivity(), cls);
        ComponentName cmpName = intent.resolveActivity(getBaseActivity().getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getBaseActivity().getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    private void getUrl() {
        int userId = mUserInfoManager.getCurrentUserInfo().getUserId();
        Logger.logI(Logger.COMMON, "MineDataListFragment：postDelayed-->userId:" + userId);
        String str = "";
        Intent intent = getBaseActivity().getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (Build.VERSION.SDK_INT >= 24) {
                str = getFilePathFromURI(getBaseActivity(), uri);
            } else {
                str = getPath(getBaseActivity(), uri);
            }
            if (!StringUtils.isEmpty(str)) {
                if (userId == 0) {
                    Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    SPUtils.getInstance().put("action_view", str);
                    getBaseActivity().finish();
                } else {
                    showUploadDialog(str);
                }
            }
            Logger.logI(Logger.COMMON, "MineDataListFragment：action：" + action + ", uri:" + uri + ", str:" + str + ", userId:" + userId);
        }
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                Logger.logI(Logger.COMMON, "MineDataListFragment：column_index-->:" + column_index + ",ursor.moveToFirst(): " + cursor.moveToFirst());
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getFilePathFromURI(Context context, Uri contentUri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) {
            return null;
        }
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) {
                return;
            }
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }

    private void showUploadDialog(final String s) {
        new CommonDialog.Builder(getActivity())
                .setTitle(getString(R.string.user_confirm_upload))
                .setMessage(R.string.user_determine_upload_to_system)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> breakMainActivity())
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> {
                    Intent intent = new Intent(getActivity(), MyDataBingDoctorActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH, s);
                    startActivity(intent);
                    getBaseActivity().finish();
                })
                .setCancelable(false)
                .show();
    }

    private void showFloatWindow() {
        mPersonalMaterialManager.getUploadingMaterialFiles(mUserInfoManager.getCurrentUserInfo().getUserId(), new PersonalMaterialManager.GetMaterialFileFlagCallback() {
            @Override
            public void onGetMaterialFileFlagStateChanged(final List<PersonalMaterialInfo> personalMaterialInfos) {
                Logger.logI(Logger.APPOINTMENT, "MineDataListFragment->showFloatWindow()->onGetMaterialFileFlagStateChanged-->personalMaterialInfos:" + personalMaterialInfos);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (personalMaterialInfos != null && personalMaterialInfos.size() > 0) {
                            if (!mIsShowFloatWindow) {
                                mWindowManager.addView(mImageView, mParams);
                            }
                            mIsShowFloatWindow = true;
                        } else {
                            if (mIsShowFloatWindow) {
                                mWindowManager.removeView(mImageView);
                                mIsShowFloatWindow = false;
                            }
                        }
                    }
                });
            }
        });
    }

    private void showDeleteFileDialog(final int position, PersonalDataInfo info) {
        new CommonDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(R.string.user_sure_delete_file)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                    }
                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        deleteFile(position, info);
                    }
                })
                .show();
    }

    private void deleteFile(int position, PersonalDataInfo info) {
        DeletePersonalDataRequester requester = new DeletePersonalDataRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                Logger.logD(Logger.COMMON, "DeletePersonalDataRequester()->BaseResult:" + baseResult);
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    myAdapter.remove(position);
                    if (myAdapter.getItemCount() <= 0) {
                        includeNewNoDataLl.setVisibility(View.VISIBLE);
                        fragmentMyDataSrl.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            }
        });
        requester.materialId = info.getMaterialId();
        requester.doPost();
    }

    public void leftClick(View view) {
        breakMainActivity();
    }

    private void breakMainActivity() {
        if (isExistMainActivity(MainActivity.class)) {
            Intent i = new Intent(getBaseActivity(), MainActivity.class);
            startActivity(i);
        }
        getBaseActivity().finish();
    }

    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(getBaseActivity(), activity);
        ComponentName cmpName = intent.resolveActivity(getBaseActivity().getPackageManager());
        boolean flag = false;
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) getBaseActivity().getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.APPOINTMENT, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DELETE_FILE:
                    fragmentMyDataSrl.autoRefresh();
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNewTask(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onTaskStart(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onDeleteTask(PersonalMaterialInfo taskInfo) {
        AppHandlerProxy.runOnUIThread(() -> mPersonalMaterialManager.getUploadingMaterialFiles(mUserInfoManager.getCurrentUserInfo().getUserId(), personalMaterialInfos -> {
            Logger.logI(Logger.APPOINTMENT, "MineDataListFragment->showFloatWindow()->onGetMaterialFileFlagStateChanged-->personalMaterialInfos:" + personalMaterialInfos);
            if (!isMainActivityTop()) {
                return;
            }
            if (personalMaterialInfos != null && personalMaterialInfos.size() > 0) {
                if (!mIsShowFloatWindow) {
                    mWindowManager.addView(mImageView, mParams);
                }
                mIsShowFloatWindow = true;
            } else {
                if (mIsShowFloatWindow) {
                    mWindowManager.removeView(mImageView);
                    mIsShowFloatWindow = false;
                }
            }
            fragmentMyDataSrl.autoRefresh();
        }));
    }

    @Override
    public void onFileUploadProgressChange(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onFileUploadStateChanged(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onFileBindingRepetition(PersonalMaterialInfo taskInfo) {

    }

    private boolean isMainActivityTop() {
        ActivityManager manager = (ActivityManager) getBaseActivity().getSystemService(ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(PatientMaterialManagerActivity.class.getName());
    }

    private void getPersonalData(int pageIndex, boolean isRefresh, RefreshLayout refreshLayout) {
        PersonalDataRequester requester = new PersonalDataRequester(new DefaultResultCallback<List<PersonalDataInfo>>() {
            @Override
            public void onSuccess(List<PersonalDataInfo> personalDataInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    fragmentMyDataSrl.finishLoadMoreWithNoMoreData();
                }
                if (PAGE_INDEX == MIN_PAGE_INDEX_1) {
                    if (LibCollections.isEmpty(personalDataInfos)) {
                        includeNewNoDataLl.setVisibility(View.VISIBLE);
                        fragmentMyDataSrl.setVisibility(View.GONE);
                    } else {
                        includeNewNoDataLl.setVisibility(View.GONE);
                        fragmentMyDataSrl.setVisibility(View.VISIBLE);
                    }
                    myAdapter.setNewData(personalDataInfos);
                } else {
                    myAdapter.addData(personalDataInfos);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
        requester.pageindex = pageIndex;
        requester.pagesize = PAGE_SIZE;
        requester.doPost();
    }

    /**
     * 更新任务状态
     */
    private void updateMaterialTaskState() {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, UploadState.UPLOAD_FAILED);
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.USER_ID + "=?";
                    String[] whereArgs = new String[]{mUserInfoManager.getCurrentUserInfo().getUserId() + ""};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
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
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPersonalMaterialManager.registerUploadStateChangeListener(this, false);
    }

    private String getLocalPath() {
        return getArguments().getString(EXTRA_DATA_KEY_LOCAL_PATH);
    }
}
