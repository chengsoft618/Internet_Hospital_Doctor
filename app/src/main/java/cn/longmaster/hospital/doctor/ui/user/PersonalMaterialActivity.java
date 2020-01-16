package cn.longmaster.hospital.doctor.ui.user;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.PersonalMaterialContract;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialInfo;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.PersonalMaterialAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;

public class PersonalMaterialActivity extends BaseActivity implements PersonalMaterialManager.UploadStateChangeListener {
    private final String TAG = PersonalMaterialActivity.class.getSimpleName();
    @FindViewById(R.id.activity_personal_material_recycler_view)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.activity_personal_material_empty_layout)
    private LinearLayout mEmptyLayout;

    @AppApplication.Manager
    private PersonalMaterialManager mPersonalMaterialManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DBManager mDBManager;

    private String mLocalPath;
    private int mDoctorId;//资料关联医生id
    private List<PersonalMaterialInfo> mPersonalMaterialInfos = new ArrayList<>();
    private PersonalMaterialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_material);
        ViewInjecter.inject(this);
        initData();
        initView();
        regListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initData() {
        mLocalPath = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH);
        mDoctorId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonalMaterialAdapter(this, mPersonalMaterialInfos);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.getItemAnimator().setSupportsChangeAnimations(false);
        mAdapter.setOnItemDeleteClickListener(new PersonalMaterialAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(View view, int position) {
                showDeleteFileDialog(position);
            }
        });
        mAdapter.setOnItemRetryClickListener(new PersonalMaterialAdapter.OnItemRetryClickListener() {
            @Override
            public void onItemRetryClick(View view, int position) {
                if (!NetStateReceiver.hasNetConnected(getActivity())) {
                    Logger.logD(Logger.APPOINTMENT, TAG + "->executeNewTask()->网络断开！");
                    showToast(R.string.no_network_connection);
                    return;
                }
                mPersonalMaterialManager.retryTask(mPersonalMaterialInfos.get(position));
                mPersonalMaterialInfos.get(position).setUploadState(UploadState.NOT_UPLOADED);
                mAdapter.notifyDataSetChanged();
            }
        });
        int mUserId = mUserInfoManager.getCurrentUserInfo().getUserId();
        Logger.logI(Logger.APPOINTMENT, TAG + "->initView()--->mUesrId:" + mUserId);
        mPersonalMaterialManager.getUploadingMaterialFiles(mUserId, new PersonalMaterialManager.GetMaterialFileFlagCallback() {
            @Override
            public void onGetMaterialFileFlagStateChanged(final List<PersonalMaterialInfo> personalMaterialInfos) {
                Logger.logI(Logger.APPOINTMENT, TAG + "->initView()->onGetMaterialFileFlagStateChanged-->personalMaterialInfos:" + personalMaterialInfos);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (personalMaterialInfos != null && personalMaterialInfos.size() > 0) {
                            mPersonalMaterialInfos.addAll(personalMaterialInfos);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showEmptyLayoutView();
                        }
                        mPersonalMaterialManager.startUpload(mLocalPath, mDoctorId);
                    }
                });
            }
        });
    }

    private void showDeleteFileDialog(final int position) {
        new CommonDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(R.string.user_sure_delete_personal_material)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                    }
                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        mPersonalMaterialManager.deleteTask(mPersonalMaterialInfos.get(position));
                        mPersonalMaterialInfos.remove(position);
                        mAdapter.notifyDataSetChanged();
                        showEmptyLayoutView();
                    }
                })
                .show();
    }

    private void regListener() {
        mPersonalMaterialManager.registerUploadStateChangeListener(this, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mLocalPath = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH);
        mDoctorId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
        Logger.logI(Logger.APPOINTMENT, TAG + "->initData()->onNewIntent-->mPicturePaths:" + mLocalPath);
        mPersonalMaterialManager.startUpload(mLocalPath, mDoctorId);
    }

    public void leftClick(View view) {
        Intent intent = new Intent(PersonalMaterialActivity.this, PatientMaterialManagerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PersonalMaterialActivity.this, PatientMaterialManagerActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonalMaterialManager.registerUploadStateChangeListener(this, false);
    }

    @Override
    public void onNewTask(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->onNewTask:" + taskInfo);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mEmptyLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mPersonalMaterialInfos.add(taskInfo);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onTaskStart(PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->onTaskStart:" + taskInfo);
    }

    @Override
    public void onDeleteTask(PersonalMaterialInfo taskInfo) {

    }

    @Override
    public void onFileUploadProgressChange(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->onFileUploadProgressChange:" + taskInfo + ", mPersonalMaterialInfos:" + mPersonalMaterialInfos);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (taskInfo.getUploadState() == UploadState.UPLOAD_FAILED) {
                    updateMaterialTaskState(taskInfo);
                }
                for (int i = 0; i < mPersonalMaterialInfos.size(); i++) {
                    if (mPersonalMaterialInfos.get(i).getTaskId().equals(taskInfo.getTaskId())) {
                        mPersonalMaterialInfos.get(i).setUploadProgress(taskInfo.getUploadProgress());
                        mPersonalMaterialInfos.get(i).setUploadState(UploadState.UPLOADING);
                        mAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onFileUploadStateChanged(final PersonalMaterialInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->onFileUploadStateChanged:" + taskInfo);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (taskInfo.getUploadState() == UploadState.UPLOAD_SUCCESS) {
                    for (int i = 0; i < mPersonalMaterialInfos.size(); i++) {
                        if (mPersonalMaterialInfos.get(i).getTaskId().equals(taskInfo.getTaskId())) {
                            mPersonalMaterialInfos.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < mPersonalMaterialInfos.size(); i++) {
                        if (mPersonalMaterialInfos.get(i).getTaskId().equals(taskInfo.getTaskId())) {
                            mPersonalMaterialInfos.get(i).setUploadState(taskInfo.getUploadState());
                            break;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                showEmptyLayoutView();
            }
        });
    }

    @Override
    public void onFileBindingRepetition(final PersonalMaterialInfo taskInfo) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mPersonalMaterialInfos.size(); i++) {
                    if (mPersonalMaterialInfos.get(i).getTaskId().equals(taskInfo.getTaskId())) {
                        mPersonalMaterialInfos.get(i).setUploadState(UploadState.UPLOAD_SUCCESS);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mPersonalMaterialInfos.size(); i++) {
                            if (mPersonalMaterialInfos.get(i).getTaskId().equals(taskInfo.getTaskId())) {
                                mPersonalMaterialInfos.remove(i);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        showEmptyLayoutView();
                    }
                }, 100);
            }
        });
    }

    private void showEmptyLayoutView() {
        if (mPersonalMaterialInfos.size() == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新任务状态
     */
    private void updateMaterialTaskState(final PersonalMaterialInfo taskInfo) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, UploadState.UPLOADING);
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.TASK_ID + "=?";
                    String[] whereArgs = new String[]{taskInfo.getTaskId() + ""};
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
}
