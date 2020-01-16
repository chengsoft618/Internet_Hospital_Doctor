package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskResultInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.UploadPictureAdapter;
import cn.longmaster.hospital.doctor.ui.consult.adapter.UploadPictureResultGridViewAdapter;
import cn.longmaster.hospital.doctor.ui.home.MyPatientActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDataManagerActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 上传队列
 */
public class UploadPictureActivity extends NewBaseActivity implements MaterialTaskManager.UploadStateChangeListener, MaterialTaskManager.MaterialTaskResultStateChangeListener {

    @FindViewById(R.id.act_upload_picture_aab)
    private AppActionBar actUploadPictureAab;
    @FindViewById(R.id.act_upload_picture_pause_note_tv)
    private TextView actUploadPicturePauseNoteTv;
    @FindViewById(R.id.act_upload_picture_upload_result_content_ll)
    private LinearLayout actUploadPictureUploadResultContentLl;
    @FindViewById(R.id.act_upload_picture_upload_result_close_tv)
    private TextView actUploadPictureUploadResultCloseTv;
    @FindViewById(R.id.act_upload_picture_upload_result_rv)
    private RecyclerView actUploadPictureUploadResultRv;
    @FindViewById(R.id.act_upload_picture_v)
    private View actUploadPictureV;
    @FindViewById(R.id.act_upload_picture_no_data_ll)
    private LinearLayout actUploadPictureNoDataLl;
    @FindViewById(R.id.act_upload_picture_list_rv)
    private RecyclerView actUploadPictureListRv;
    @FindViewById(R.id.act_upload_picture_pause_tv)
    private TextView actUploadPicturePauseTv;
    @FindViewById(R.id.act_upload_picture_add_tv)
    private TextView actUploadPictureAddTv;
    @FindViewById(R.id.act_upload_picture_clear_tv)
    private TextView actUploadPictureClearTv;

    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultManager mConsultManager;

    private boolean mIsPause;
    private ArrayList<String> mPicturePaths;
    private int mAppointmentId;
    private UploadPictureAdapter mUploadAdapter;
    private List<MaterialFileInfo> mFileInfos;
    private boolean isShowing = false;
    private UploadPictureResultGridViewAdapter mGridViewAdapter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean mIsHomePage = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_HOME_PAGE, false);
        if (mIsHomePage) {
            return;
        }
        mPicturePaths = intent.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        Logger.logI(Logger.APPOINTMENT, TAG + "->initData()->onNewIntent-->mPicturePaths:" + mPicturePaths + ",mAppointmentId:" + mAppointmentId);
        startUpload();
    }

    @Override
    protected void initDatas() {
        mPicturePaths = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
        mAppointmentId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        Logger.logI(Logger.APPOINTMENT, TAG + "->initData()->mPicturePaths:" + mPicturePaths + ", mAppointmentId:" + mAppointmentId);
        mIsPause = SPUtils.getInstance().getBoolean(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_picture;
    }

    @Override
    protected void initViews() {
        actUploadPictureListRv.setLayoutManager(new LinearLayoutManager(this));
        // mResultListView.setLayoutManager(new FullyLayoutManager(this));
        if (mIsPause) {
            actUploadPicturePauseTv.setText(getString(R.string.data_upload_recovery));
            actUploadPicturePauseNoteTv.setVisibility(View.VISIBLE);
        } else {
            actUploadPicturePauseTv.setText(getString(R.string.upload_queue_pause));
            actUploadPicturePauseNoteTv.setVisibility(View.GONE);
        }
        regListener();
        initAdapter();
        getAllMaterialTaskResults();
        initClickListener();
        startUpload();
    }

    @Override
    protected void onDestroy() {
        mMaterialTaskManager.clearAllTasks();
        mMaterialTaskManager.registerUploadStateChangeListener(this, false);
        mMaterialTaskManager.registerMaterialTaskResultStateChangeListener(this, false);
        super.onDestroy();
    }


    private void setPauseView() {
        List<MaterialFileInfo> data = mUploadAdapter.getData();
        boolean isAllSuccss = true;
        for (MaterialFileInfo info : data) {
            if (info.getState() == UploadState.NOT_UPLOADED || info.getState() == UploadState.UPLOADING || info.getState() == UploadState.UPLOAD_PAUSE) {
                isAllSuccss = false;
            }
        }
        if (isAllSuccss) {
            ToastUtils.showShort("暂无可上传任务,暂停无效");
            return;
        }
        if (mIsPause) {
            mIsPause = false;
            actUploadPicturePauseTv.setText(getString(R.string.upload_queue_pause));
            actUploadPicturePauseNoteTv.setVisibility(View.GONE);
            mMaterialTaskManager.recoverAllTasks();
        } else {
            mIsPause = true;
            actUploadPicturePauseTv.setText(getString(R.string.data_upload_recovery));
            actUploadPicturePauseNoteTv.setVisibility(View.VISIBLE);
            mMaterialTaskManager.pauseAllTasks();
        }
        for (MaterialFileInfo fileInfo : mUploadAdapter.getData()) {
            if (mIsPause) {
                if (fileInfo.getState() == UploadState.NOT_UPLOADED || fileInfo.getState() == UploadState.UPLOADING) {
                    fileInfo.setState(UploadState.UPLOAD_PAUSE);
                }
            } else {
                if (fileInfo.getState() == UploadState.UPLOAD_PAUSE) {
                    fileInfo.setState(UploadState.NOT_UPLOADED);
                }
            }
        }
        mUploadAdapter.notifyDataSetChanged();
    }

    private void showEmptyDialog() {
        if (mUploadAdapter.getData().size() == 0) {
            ToastUtils.showShort(getString(R.string.data_upload_no_record));
            return;
        }
        new CommonDialog.Builder(getThisActivity())
                .setMessage(R.string.data_queue_determine_empty)
                .setPositiveButton(R.string.video_dialog_cancel, () -> {
                })
                .setNegativeButton(R.string.upload_queue_empty, () -> {
                    List<MaterialFileInfo> list = new ArrayList<>();
                    List<MaterialFileInfo> data = mUploadAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getState() == UploadState.UPLOAD_SUCCESS) {
                            list.add(data.get(i));
                        }
                    }
                    data.removeAll(list);
                    mUploadAdapter.notifyDataSetChanged();
                    if (mUploadAdapter.getItemCount() == 0) {
                        actUploadPictureNoDataLl.setVisibility(View.VISIBLE);
                        actUploadPictureV.setVisibility(View.GONE);
                    }
                    mMaterialTaskManager.clearAllTasks();
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    private void closeAllTaskResult() {
        actUploadPictureUploadResultContentLl.setVisibility(View.GONE);
        if (mGridViewAdapter.getItemCount() > 0) {
            mMaterialTaskManager.clearMaterialTaskResultInfo();
        }
    }

    @Override
    public void onNewTask(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onNewTask()->taskInfo:" + taskInfo);
        if (mUploadAdapter.getItemCount() == 0) {
            actUploadPictureNoDataLl.setVisibility(View.GONE);
            actUploadPictureV.setVisibility(View.VISIBLE);
        }
        mUploadAdapter.addData(taskInfo.getMaterialFileInfos());
    }

    @Override
    public void onTaskStart(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskStart()->taskInfo:" + taskInfo);
    }

    @Override
    public void onTaskProgressChange(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskProgressChange()->taskInfo:" + taskInfo);
    }

    @Override
    public void onTaskFailed(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskFailed()->taskInfo:" + taskInfo);
    }

    @Override
    public void onTaskSuccessful(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskSuccessful()->taskInfo:" + taskInfo);
    }

    @Override
    public void onTaskDelete(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskDelete()->taskInfo:" + taskInfo);
    }

    @Override
    public void onTaskCancle(MaterialTaskInfo taskInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onTaskCancle()->taskInfo:" + taskInfo);
    }

    @Override
    public void onFileUploadProgressChange(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onFileUploadProgressChange()->taskInfo:" + taskInfo + ", fileInfo:" + fileInfo);
        for (MaterialFileInfo info : mUploadAdapter.getData()) {
            if (info.getSessionId().equals(fileInfo.getSessionId()) && info.getState() != UploadState.UPLOAD_PAUSE) {
                notifyItemChanged(fileInfo);
            }
        }
    }

    @Override
    public void onFileUploadStateChanged(MaterialTaskInfo taskInfo, MaterialFileInfo fileInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onFileUploadStateChanged()->taskInfo:" + taskInfo + ", fileInfo:" + fileInfo);
        notifyItemChanged(fileInfo);
    }

    @Override
    public void onNewMaterialTaskResult(final MaterialTaskResultInfo resultInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onNewMaterialTaskResult()->resultInfo:" + resultInfo);
        actUploadPictureUploadResultContentLl.setVisibility(View.VISIBLE);
        mGridViewAdapter.addData(resultInfo);
    }

    @Override
    public void onMaterialTaskResultStateChanged(final MaterialTaskResultInfo resultInfo) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onMaterialTaskResultStateChanged()->resultInfo:" + resultInfo);

        mGridViewAdapter.addData(resultInfo);
    }

    private void notifyItemChanged(MaterialFileInfo fileInfo) {
        for (int i = 0; i < mFileInfos.size(); i++) {
            if ((mFileInfos.get(i).getSessionId()).equals(fileInfo.getSessionId()) && mFileInfos.get(i).getState() != UploadState.UPLOAD_PAUSE) {
                mUploadAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    public boolean isShowing() {
        return isShowing;
    }


    private void regListener() {
        mMaterialTaskManager.registerUploadStateChangeListener(this, true);
        mMaterialTaskManager.registerMaterialTaskResultStateChangeListener(this, true);
    }

    private void initAdapter() {
        List<MaterialTaskInfo> uploadTaskList = mMaterialTaskManager.getAllMaterialTasks();
        mFileInfos = new ArrayList<>();
        for (MaterialTaskInfo taskInfo : uploadTaskList) {
            mFileInfos.addAll(taskInfo.getMaterialFileInfos());
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->initAdapter()->mMaterialFileInfos:" + mFileInfos);
        mUploadAdapter = new UploadPictureAdapter(R.layout.item_upload_queue, mFileInfos);
        mGridViewAdapter = new UploadPictureResultGridViewAdapter(R.layout.item_upload_result, new ArrayList<>(0));
        actUploadPictureUploadResultRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actUploadPictureUploadResultRv.setAdapter(mGridViewAdapter);
        //mUploadListView.getItemAnimator().setSupportsChangeAnimations(false);
        actUploadPictureListRv.setAdapter(mUploadAdapter);
        if (mUploadAdapter.getItemCount() == 0) {
            actUploadPictureNoDataLl.setVisibility(View.VISIBLE);
            actUploadPictureV.setVisibility(View.GONE);
        } else {
            actUploadPictureNoDataLl.setVisibility(View.GONE);
            actUploadPictureV.setVisibility(View.VISIBLE);
        }

        mUploadAdapter.setOnItemRemoveClickListener((materialFileInfo, position) -> removeItem(materialFileInfo, position));

        mUploadAdapter.setOnItemUploadFailClickListener((materialFileInfo, position) -> retryTask(materialFileInfo, position));

        mUploadAdapter.setOnItemPauseClickListener((materialFileInfo, position) -> pauseTask(materialFileInfo, position));

        mUploadAdapter.setOnItemRecoveryPauseClickListener((materialFileInfo, position) -> recoverTask(materialFileInfo, position));
        mGridViewAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MaterialTaskResultInfo taskResultInfo = (MaterialTaskResultInfo) adapter.getItem(position);
            if (null != taskResultInfo) {
                adapter.remove(position);
                if (taskResultInfo.getFailedCount() == 0) {
                    startDataManageActivity(taskResultInfo);
                } else {
                    itemRetryTask(taskResultInfo);
                }
            }
        });
    }

    private void getAllMaterialTaskResults() {
        List<MaterialTaskResultInfo> materialTaskResults = mMaterialTaskManager.getAllMaterialTaskResultInfos();
        Logger.logD(Logger.APPOINTMENT, TAG + "->getAllMaterialTaskResults()->materialTaskResults:" + materialTaskResults);
        mGridViewAdapter.setNewData(materialTaskResults);

        if (materialTaskResults.size() == 0) {
            actUploadPictureUploadResultContentLl.setVisibility(View.GONE);
        } else {
            actUploadPictureUploadResultContentLl.setVisibility(View.VISIBLE);
        }
    }

    private void initClickListener() {
        actUploadPicturePauseTv.setOnClickListener(v -> {
            setPauseView();
        });
        actUploadPictureClearTv.setOnClickListener(v -> {
            showEmptyDialog();
        });
        actUploadPictureAddTv.setOnClickListener(v -> {
            Intent intent = new Intent(UploadPictureActivity.this, MyPatientActivity.class);
            startActivity(intent);
        });
        actUploadPictureUploadResultCloseTv.setOnClickListener(v -> {
            closeAllTaskResult();
        });
    }

    private void pauseTask(MaterialFileInfo materialFileInfo, int position) {
        mUploadAdapter.getItem(position).setState(UploadState.UPLOAD_PAUSE);
        mUploadAdapter.notifyDataSetChanged();
        mMaterialTaskManager.pauseTask(materialFileInfo.getTaskId());
        List<MaterialFileInfo> data = mUploadAdapter.getData();
        boolean isAllPause = true;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getState() == UploadState.UPLOADING || data.get(i).getState() == UploadState.NOT_UPLOADED) {
                isAllPause = false;
                break;
            }
        }
        if (isAllPause && !mIsPause) {
            mIsPause = true;
            SPUtils.getInstance().put(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), true);
            actUploadPicturePauseTv.setText(getString(R.string.data_upload_recovery));
            actUploadPicturePauseNoteTv.setVisibility(View.VISIBLE);
        }
    }

    private void removeItem(MaterialFileInfo materialFileInfo, int position) {
        Logger.logI(Logger.APPOINTMENT, TAG + "->removeItem()->position:" + position);
        if (position >= mUploadAdapter.getItemCount()) {
            return;
        }
        mUploadAdapter.remove(position);
        mUploadAdapter.notifyDataSetChanged();
        mMaterialTaskManager.deleteTask(materialFileInfo.getTaskId());
        if (mUploadAdapter.getItemCount() == 0) {
            actUploadPictureNoDataLl.setVisibility(View.VISIBLE);
            actUploadPictureV.setVisibility(View.GONE);
        }
    }

    private void retryTask(MaterialFileInfo materialFileInfo, int position) {
        mUploadAdapter.getItem(position).setState(UploadState.NOT_UPLOADED);
        mUploadAdapter.notifyDataSetChanged();
        mMaterialTaskManager.retryTask(materialFileInfo.getTaskId());
    }

    private void recoverTask(MaterialFileInfo materialFileInfo, int position) {
        mUploadAdapter.getItem(position).setState(UploadState.NOT_UPLOADED);
        mUploadAdapter.notifyDataSetChanged();
        mMaterialTaskManager.recoverTask(materialFileInfo.getTaskId());

        List<MaterialFileInfo> data = mUploadAdapter.getData();
        boolean isAllRecover = true;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getState() == UploadState.UPLOAD_PAUSE) {
                isAllRecover = false;
                break;
            }
        }
        if (isAllRecover && mIsPause) {
            mIsPause = false;
            SPUtils.getInstance().put(AppPreference.KEY_UPLOAD_PAUSE + mUserInfoManager.getCurrentUserInfo().getUserId(), false);
            actUploadPicturePauseTv.setText(getString(R.string.upload_queue_pause));
            actUploadPicturePauseNoteTv.setVisibility(View.GONE);
        }
    }

    private void startDataManageActivity(MaterialTaskResultInfo taskResultInfo) {
        if (mGridViewAdapter.getItemCount() == 1) {
            actUploadPictureUploadResultContentLl.setVisibility(View.GONE);
        }
        mMaterialTaskManager.deleteMaterialTaskResultInfo(taskResultInfo);

        Logger.logI(Logger.APPOINTMENT, TAG + "->startDataManageActivity()->getAppointmentId():" + taskResultInfo.getAppointmentId());
        if (taskResultInfo.getAppointmentId() >= 500000) {
            Intent intent = new Intent(UploadPictureActivity.this, RoundsDataManagerActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, taskResultInfo.getAppointmentId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(UploadPictureActivity.this, ConsultDataManageActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, taskResultInfo.getAppointmentId());
            startActivity(intent);
        }
        finish();
    }

    private void itemRetryTask(MaterialTaskResultInfo taskResultInfo) {
        if (mGridViewAdapter.getItemCount() == 1) {
            actUploadPictureUploadResultContentLl.setVisibility(View.GONE);
        }
        mMaterialTaskManager.deleteMaterialTaskResultInfo(taskResultInfo);
        mMaterialTaskManager.retryTask(taskResultInfo.getAppointmentId());
    }

    private void startUpload() {
        Logger.logI(Logger.APPOINTMENT, TAG + "->startUpload()->mAppointmentId:" + mAppointmentId);
        if (mPicturePaths != null) {
            mMaterialTaskManager.startUpload(mAppointmentId, mPicturePaths, false);
        }
    }

}
