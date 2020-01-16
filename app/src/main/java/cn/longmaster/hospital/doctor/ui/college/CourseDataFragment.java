package cn.longmaster.hospital.doctor.ui.college;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialResult;
import cn.longmaster.hospital.doctor.core.entity.consult.MaterialClassify;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.appointment.AuxiliaryMaterialRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * 课程详情资料Fragment
 * Created by Yang² on 2018/3/28.
 */

public class CourseDataFragment extends NewBaseFragment implements FileLoadListener {
    @FindViewById(R.id.fragment_course_data_main_ll)
    private LinearLayout mMainLl;
    @FindViewById(R.id.fragment_course_data_no_data_tv)
    private TextView mNoDataTv;

    private int mAppointId;
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos;//所有材料列表
    private ArrayList<AuxiliaryMaterialInfo> mAuxilirayMaterialPicList = new ArrayList<>();//所有图片材料
    private List<MaterialClassify> mTypeClassifies;//类型分组

    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;
    private Map<CourseMaterialView, List<String>> mClassifyMaterialViewMap = new HashMap<>();

    @Override
    protected void initDatas() {
        super.initDatas();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mAppointId = bundle.getInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID);
            Logger.logD(Logger.APPOINTMENT, "appointmentId:" + mAppointId);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_course_data;
    }

    @Override
    public void initViews(View rootView) {
        checkByType();
        mMediaDownloadManager.regLoadListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaDownloadManager.unRegLoadListener(this);
    }

    /**
     * 按类型查看
     */
    private void checkByType() {
        if (mAuxiliaryMaterialInfos != null) {
            addMaterialByType(mAuxiliaryMaterialInfos);
            return;
        }
        AuxiliaryMaterialRequester requester = new AuxiliaryMaterialRequester(new DefaultResultCallback<AuxiliaryMaterialResult>() {
            @Override
            public void onSuccess(AuxiliaryMaterialResult auxiliaryMaterialResult, BaseResult baseResult) {
                if (auxiliaryMaterialResult.getAuxiliaryMaterialInfos() != null && !auxiliaryMaterialResult.getAuxiliaryMaterialInfos().isEmpty()) {
                    getAllMaterialPicList(extractCheckSuccessMaterials(auxiliaryMaterialResult.getAuxiliaryMaterialInfos()));
                    mAuxiliaryMaterialInfos = new ArrayList<>();
                    mAuxiliaryMaterialInfos.addAll(extractCheckSuccessMaterials(auxiliaryMaterialResult.getAuxiliaryMaterialInfos()));
                    addMaterialByType(mAuxiliaryMaterialInfos);
                }
                if (LibCollections.isEmpty(mAuxiliaryMaterialInfos)) {
                    mNoDataTv.setVisibility(View.VISIBLE);
                } else {
                    mNoDataTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.appointmentId = mAppointId;
        requester.doPost();
    }

    /**
     * 提取审核通过的图片
     *
     * @param materialInfos 所有材料
     * @return 审核通过的材料
     */
    private List<AuxiliaryMaterialInfo> extractCheckSuccessMaterials(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : materialInfos) {
            if (materialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                newMaterialInfos.add(materialInfo);
            }
        }
        return newMaterialInfos;
    }

    /**
     * 添加材料按类型
     *
     * @param materialInfos 所有材料
     */
    private void addMaterialByType(List<AuxiliaryMaterialInfo> materialInfos) {
        if (getActivity() == null || materialInfos == null || materialInfos.isEmpty()) {
            return;
        }
        mMainLl.removeAllViews();
        if (mTypeClassifies == null) {
            mTypeClassifies = classifyMaterialByType(materialInfos);
        }
        mClassifyMaterialViewMap.clear();
        for (MaterialClassify classify : mTypeClassifies) {
            CourseMaterialView courseMaterialView = new CourseMaterialView(getActivity());
            courseMaterialView.setData(classify, mAuxilirayMaterialPicList);
            List<String> fileNames = new ArrayList<>();
            for (AuxiliaryMaterialInfo info : classify.materialCheckInfos) {
                fileNames.add(info.getMaterialPic());
            }
            mClassifyMaterialViewMap.put(courseMaterialView, fileNames);
            mMainLl.addView(courseMaterialView);
        }
    }

    /**
     * 按类型划分材料
     *
     * @param materialInfos 所有材料
     * @return 类型分组材料
     */
    private List<MaterialClassify> classifyMaterialByType(List<AuxiliaryMaterialInfo> materialInfos) {
        //根据是否dicom图片将所有图片分成两组
        List<AuxiliaryMaterialInfo> dicomMaterialInfos = new ArrayList<>();
        List<AuxiliaryMaterialInfo> unDicomMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : materialInfos) {
            if (materialInfo.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_DICOM) {
                dicomMaterialInfos.add(materialInfo);
            } else {
                unDicomMaterialInfos.add(materialInfo);
            }
        }
        //根据id大小（id小的在前）排序非dicom图片
        Collections.sort(unDicomMaterialInfos, (lhs, rhs) -> {
            int lId = lhs.getMaterialId();
            int rId = rhs.getMaterialId();
            int res = 0;
            if (lId != rId) {
                res = lId > rId ? 1 : -1;
            }
            return res;
        });
        List<MaterialClassify> classifyList = new ArrayList<>();
        MaterialClassify classify = new MaterialClassify();
        //构建dicom类型的图片
        if (!dicomMaterialInfos.isEmpty()) {
            classify = new MaterialClassify();
            classify.classifyName = "DICOM";
            classify.materialCheckInfos = dicomMaterialInfos;
            classifyList.add(classify);
        }
        //构建非dicom类型的图片
        int id = -1;
        for (AuxiliaryMaterialInfo materialInfo : unDicomMaterialInfos) {
            int materialId = materialInfo.getMaterialId();
            if (id == -1) {
                classify = new MaterialClassify();
                classify.materialCheckInfos = new ArrayList<>();
                classify.classifyName = materialInfo.getMaterialCfgName();
                classify.materialCheckInfos.add(materialInfo);
                id = materialId;
            } else if (id == materialId) {
                classify.classifyName = materialInfo.getMaterialCfgName();
                classify.materialCheckInfos.add(materialInfo);
            } else {
                classifyList.add(classify);
                classify = new MaterialClassify();
                classify.materialCheckInfos = new ArrayList<>();
                classify.classifyName = materialInfo.getMaterialCfgName();
                classify.materialCheckInfos.add(materialInfo);
                id = materialId;
            }
        }
        if (!unDicomMaterialInfos.isEmpty()) {
            classifyList.add(classify);
        }
        return classifyList;
    }

    private void getAllMaterialPicList(List<AuxiliaryMaterialInfo> materialInfos) {
        mAuxilirayMaterialPicList.clear();
        List<MaterialClassify> materialClassifyList = classifyMaterialByType(materialInfos);
        for (MaterialClassify materialClassify : materialClassifyList) {
            for (AuxiliaryMaterialInfo materialInfo : materialClassify.materialCheckInfos) {
                if (materialInfo.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                    mAuxilirayMaterialPicList.add(materialInfo);
                }
            }
        }
    }

    @Override
    public void onStartDownload(String filePath) {
        for (Map.Entry<CourseMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onStartDownload(filePath);
            }
        }
    }

    @Override
    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
        for (Map.Entry<CourseMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadProgressChange(filePath, totalSize, currentSize);
            }
        }
    }

    @Override
    public void onLoadFailed(String filePath, String reason) {
        for (Map.Entry<CourseMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadFailed(filePath, reason);
            }
        }
    }

    @Override
    public void onLoadSuccessful(String filePath) {
        for (Map.Entry<CourseMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadSuccessful(filePath);
            }
        }
    }

    @Override
    public void onLoadStopped(String filePath) {
        for (Map.Entry<CourseMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadStopped(filePath);
            }
        }
    }
}
