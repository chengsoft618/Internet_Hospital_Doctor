package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import cn.longmaster.hospital.doctor.core.entity.consult.MaterialClassify;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.MedicalAuxiliaryInspectRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.view.ClassifyMaterialView;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * 患者信息，辅助检查fragment
 */
public class RoundsAuxiliaryAssistExamineFragment extends NewBaseFragment implements FileLoadListener {
    @FindViewById(R.id.fg_assist_examine_classify_rg)
    private RadioGroup fgAssistExamineClassifyRg;
    @FindViewById(R.id.fg_assist_examine_by_type_rb)
    private RadioButton fgAssistExamineByTypeRb;
    @FindViewById(R.id.fg_assist_examine_by_time_rb)
    private RadioButton fgAssistExamineByTimeRb;
    @FindViewById(R.id.fg_assist_examine_ll)
    private LinearLayout fgAssistExamineLl;

    //所有材料列表
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos;
    //所有图片材料
    private List<AuxiliaryMaterialInfo> mAuxilirayMaterialPicList = new ArrayList<>();
    private final int TYPE_CLASSI_FIES = 1;
    private final int TIME_CLASSI_FIES = 2;
    private int mClassiFies = TYPE_CLASSI_FIES;
    //类型分组
    private List<MaterialClassify> mTypeClassifies;
    //时间分组
    private List<MaterialClassify> mTimeClassifies;
    //是否从视频诊室进入
    private boolean mIsVideoRoomEnter = false;

    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;

    private Map<ClassifyMaterialView, List<String>> mClassifyMaterialViewMap = new HashMap<>();

    public static RoundsAuxiliaryAssistExamineFragment getInstance(RoundsMedicalDetailsInfo medicalDetailsInfo) {
        RoundsAuxiliaryAssistExamineFragment fragment = new RoundsAuxiliaryAssistExamineFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO, medicalDetailsInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_rounds_assist_examine;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    public void initViews(View rootView) {
        mMediaDownloadManager.regLoadListener(this);
        fgAssistExamineClassifyRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.fg_assist_examine_by_type_rb) {
                mClassiFies = TYPE_CLASSI_FIES;
            } else {
                mClassiFies = TIME_CLASSI_FIES;
            }
            addMaterial(mClassiFies, mAuxiliaryMaterialInfos);
        });
        getMedicalAuxiliaryInspect(getBasicMedicalInfo().getMedicalId());
    }

    @Override
    public void onDestroy() {
        mMediaDownloadManager.unRegLoadListener(this);
        super.onDestroy();
    }

    private void getMedicalAuxiliaryInspect(int medicalId) {
        MedicalAuxiliaryInspectRequester requester = new MedicalAuxiliaryInspectRequester(new DefaultResultCallback<List<AuxiliaryMaterialInfo>>() {
            @Override
            public void onSuccess(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, BaseResult baseResult) {
                List<AuxiliaryMaterialInfo> checkAuxiliaryMaterialInfos;
                if (LibCollections.isNotEmpty(auxiliaryMaterialInfos)) {
                    fgAssistExamineClassifyRg.setVisibility(View.VISIBLE);
                    checkAuxiliaryMaterialInfos = extractCheckSuccessMaterials(auxiliaryMaterialInfos);
                    mAuxiliaryMaterialInfos = checkAuxiliaryMaterialInfos;
                    mAuxilirayMaterialPicList = getAllMaterialPicList(checkAuxiliaryMaterialInfos);
                    addMaterial(mClassiFies, mAuxiliaryMaterialInfos);
                } else {
                    fgAssistExamineClassifyRg.setVisibility(View.GONE);
                }
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    /**
     * 提取审核通过的材料
     *
     * @param materialInfos 所有材料
     * @return 审核通过的材料
     */
    private List<AuxiliaryMaterialInfo> extractCheckSuccessMaterials(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        for (int i = 0; i < LibCollections.size(materialInfos); i++) {
            AuxiliaryMaterialInfo materialInfo = materialInfos.get(i);
            Logger.logD(TAG + "#extractCheckSuccessMaterials:", materialInfo.getMaterialName());
            if (materialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS
                    && !StringUtils.equals(materialInfo.getMaterialCfgName(), "病历")
                    && !StringUtils.equals(materialInfo.getMaterialCfgName(), "医嘱单")) {
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
    private void addMaterial(int type, @NonNull List<AuxiliaryMaterialInfo> materialInfos) {
        for (int i = 0; i < LibCollections.size(materialInfos); i++) {
            Logger.logD(TAG + "#addMaterial:", materialInfos.get(i).getMaterialName());
        }
        if (getActivity() == null || materialInfos.isEmpty()) {
            return;
        }
        fgAssistExamineLl.removeAllViews();
        if (mTypeClassifies == null) {
            mTypeClassifies = classifyMaterialByType(materialInfos);
        }
        if (mTimeClassifies == null) {
            mTimeClassifies = classifyMaterialByTime(materialInfos);
        }
        mClassifyMaterialViewMap.clear();
        if (type == TIME_CLASSI_FIES) {
            populateAssistExamine(mTimeClassifies);
        } else {
            populateAssistExamine(mTypeClassifies);
        }
    }

    private void populateAssistExamine(List<MaterialClassify> materialClassifyList) {
        for (MaterialClassify classify : materialClassifyList) {
            LinearLayout typeLl = (LinearLayout) View.inflate(getActivity(), R.layout.layout_assist_examine_type, null);
            TextView title = typeLl.findViewById(R.id.layout_assist_examine_type_title);
            title.setText(classify.classifyName);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DisplayUtil.dip2px(10), DisplayUtil.dip2px(10), DisplayUtil.dip2px(10), 0);
            typeLl.setLayoutParams(params);
            for (MaterialClassify typeClassify : classifyMaterialByType(classify.materialCheckInfos)) {
                ClassifyMaterialView classifyMaterialView = new ClassifyMaterialView(getActivity());
                classifyMaterialView.setData(typeClassify, mIsVideoRoomEnter, mAuxilirayMaterialPicList);
                if (typeLl.getChildCount() == 1) {
                    classifyMaterialView.showDivider(false);
                }
                typeLl.addView(classifyMaterialView);
                List<String> fileNames = new ArrayList<>();
                for (AuxiliaryMaterialInfo info : typeClassify.materialCheckInfos) {
                    fileNames.add(info.getMaterialPic());
                }
                mClassifyMaterialViewMap.put(classifyMaterialView, fileNames);
            }
            fgAssistExamineLl.addView(typeLl);
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

    /**
     * 按时间划分材料
     *
     * @param materialInfos 所有材料
     * @return 时间分组材料
     */
    private List<MaterialClassify> classifyMaterialByTime(List<AuxiliaryMaterialInfo> materialInfos) {
        Collections.sort(materialInfos, (lhs, rhs) -> {
            int lDate;
            int rDate;
            if (!TextUtils.isEmpty(lhs.getMaterialDt())) {
                lDate = Integer.valueOf(lhs.getMaterialDt().substring(0, 10).replace("-", ""));
            } else {
                lDate = 0;
            }

            if (!TextUtils.isEmpty(rhs.getMaterialDt())) {
                rDate = Integer.valueOf(rhs.getMaterialDt().substring(0, 10).replace("-", ""));
            } else {
                rDate = 0;
            }

            int res = 0;
            if (lDate != rDate) {
                res = lDate > rDate ? 1 : -1;
            }
            return res;
        });
        List<MaterialClassify> classifyList = new ArrayList<>();
        String date = "";
        MaterialClassify classify = null;
        for (AuxiliaryMaterialInfo materialInfo : materialInfos) {
            String materialDate;
            if (!TextUtils.isEmpty(materialInfo.getMaterialDt())) {
                materialDate = materialInfo.getMaterialDt().substring(0, 10);
            } else {
                materialDate = "0";
            }

            if (TextUtils.isEmpty(date)) {
                classify = new MaterialClassify();
                classify.materialCheckInfos = new ArrayList<>();
                classify.classifyName = materialDate;
                classify.materialCheckInfos.add(materialInfo);
                date = materialDate;
            } else if (TextUtils.equals(date, materialDate)) {
                if (classify != null) {
                    classify.classifyName = materialDate;
                    classify.materialCheckInfos.add(materialInfo);
                }
            } else if (!TextUtils.equals(date, materialDate)) {
                classifyList.add(classify);
                classify = new MaterialClassify();
                classify.materialCheckInfos = new ArrayList<>();
                classify.classifyName = materialDate;
                classify.materialCheckInfos.add(materialInfo);
                date = materialDate;
            }
        }
        classifyList.add(classify);
        return classifyList;
    }

    private List<AuxiliaryMaterialInfo> getAllMaterialPicList(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos = new ArrayList<>();
        List<MaterialClassify> materialClassifyList = classifyMaterialByType(materialInfos);
        for (MaterialClassify materialClassify : materialClassifyList) {
            for (MaterialClassify material : classifyMaterialByTime(materialClassify.materialCheckInfos)) {
                for (AuxiliaryMaterialInfo materialInfo : material.materialCheckInfos) {
                    if (materialInfo.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                        auxiliaryMaterialInfos.add(materialInfo);
                    }
                }
            }
        }
        return auxiliaryMaterialInfos;
    }

    @Override
    public void onStartDownload(String filePath) {
        Logger.logD(Logger.APPOINTMENT, "onStartDownload--》filePath:" + filePath);
        for (Map.Entry<ClassifyMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onStartDownload(filePath);
            }
        }
    }

    @Override
    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
        for (Map.Entry<ClassifyMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadProgressChange(filePath, totalSize, currentSize);
            }
        }
    }

    @Override
    public void onLoadFailed(String filePath, String reason) {
        for (Map.Entry<ClassifyMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadFailed(filePath, reason);
            }
        }
    }

    @Override
    public void onLoadSuccessful(String filePath) {
        for (Map.Entry<ClassifyMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadSuccessful(filePath);
            }
        }
    }

    @Override
    public void onLoadStopped(String filePath) {
        for (Map.Entry<ClassifyMaterialView, List<String>> entry : mClassifyMaterialViewMap.entrySet()) {
            if (entry.getValue().contains(mMediaDownloadManager.getFileName(filePath))) {
                entry.getKey().onLoadStopped(filePath);
            }
        }
    }

    private RoundsMedicalDetailsInfo getBasicMedicalInfo() {
        return (RoundsMedicalDetailsInfo) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO);
    }

    private boolean ismIsVideoRoomEnter() {
        return getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_ROOM, false);
    }
}
