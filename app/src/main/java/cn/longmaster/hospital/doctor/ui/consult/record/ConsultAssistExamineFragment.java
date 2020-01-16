package cn.longmaster.hospital.doctor.ui.consult.record;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import cn.longmaster.hospital.doctor.view.ClassifyMaterialView;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * 患者信息，辅助检查fragment
 * Created by Yang² on 2016/7/19.
 */
public class ConsultAssistExamineFragment extends NewBaseFragment implements FileLoadListener {
    @FindViewById(R.id.fg_assist_examine_classify_rg)
    private RadioGroup fgAssistExamineClassifyRg;
    @FindViewById(R.id.fg_assist_examine_by_type_rb)
    private RadioButton fgAssistExamineByTypeRb;
    @FindViewById(R.id.fg_assist_examine_by_time_rb)
    private RadioButton fgAssistExamineByTimeRb;
    @FindViewById(R.id.fg_assist_examine_ll)
    private LinearLayout fgAssistExamineLl;
    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;
    private Map<ClassifyMaterialView, List<String>> mClassifyMaterialViewMap = new HashMap<>();

    //所有材料列表
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos;
    //所有按类型查看图片
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialTypePicList = new ArrayList<>();
    //所有按时间查看图片
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialTimePicList = new ArrayList<>();
    //类型分组
    private List<MaterialClassify> mTypeClassifies;
    //时间分组
    private List<MaterialClassify> mTimeClassifies;
    //按类型查看
    private final int TYPE_CHECK = 4;
    //按时间查看
    private final int TIME_CHECK = 2;
    private int mAppointId;

    public static ConsultAssistExamineFragment getInstance(int appointmentId, boolean isFromVideoRoom, List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        ConsultAssistExamineFragment consultAssistExamineFragment = new ConsultAssistExamineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_AUXILIARY_MATERIAL_INFO, (Serializable) auxiliaryMaterialInfos);
        bundle.putBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, isFromVideoRoom);
        consultAssistExamineFragment.setArguments(bundle);
        return consultAssistExamineFragment;
    }

    private int getAppointmentId() {
        if (mAppointId == 0) {
            mAppointId = getArguments() == null ? 0 : getArguments().getInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        }
        return mAppointId;
    }

    private boolean isVideoRoomEnter() {
        return getArguments() != null && getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, false);
    }

    private List<AuxiliaryMaterialInfo> getAuxiliaryMaterialInfos() {
        return getArguments() == null ? null : (List<AuxiliaryMaterialInfo>) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_AUXILIARY_MATERIAL_INFO);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mMediaDownloadManager.regLoadListener(this);
        mAuxiliaryMaterialInfos = getAuxiliaryMaterialInfos();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_assist_examine;
    }

    @Override
    public void initViews(View rootView) {
        fgAssistExamineClassifyRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.fg_assist_examine_by_type_rb) {
                //按类型查看
                checkSuccessMaterial(mAuxiliaryMaterialTypePicList, mTypeClassifies);
            } else if (checkedId == R.id.fg_assist_examine_by_time_rb) {
                //按时间查看
                checkSuccessMaterial(mAuxiliaryMaterialTimePicList, mTimeClassifies);
            }
        });
        if (LibCollections.isEmpty(mAuxiliaryMaterialInfos)) {
            getAuxiliaryAssistExamine(getAppointmentId(), TYPE_CHECK);
        } else {
            displayMaterial(mAuxiliaryMaterialInfos, TYPE_CHECK);
        }
    }

    private void displayMaterial(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, int checkType) {
        if (checkType == TYPE_CHECK) {
            if (LibCollections.isEmpty(mAuxiliaryMaterialTypePicList)) {
                formatAllMaterial(auxiliaryMaterialInfos, checkType);
            }
        } else {
            if (LibCollections.isEmpty(mAuxiliaryMaterialTimePicList)) {
                formatAllMaterial(auxiliaryMaterialInfos, checkType);
            }
        }
    }

    private void checkSuccessMaterial(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, List<MaterialClassify> materialClassifies) {
        fgAssistExamineLl.removeAllViews();
        mClassifyMaterialViewMap.clear();
        for (MaterialClassify classify : materialClassifies) {
            LinearLayout typeLl = (LinearLayout) View.inflate(getActivity(), R.layout.layout_assist_examine_type, null);
            TextView title = typeLl.findViewById(R.id.layout_assist_examine_type_title);
            title.setText(classify.classifyName);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DisplayUtil.dip2px(10), DisplayUtil.dip2px(10), DisplayUtil.dip2px(10), 0);
            typeLl.setLayoutParams(params);
            for (MaterialClassify typeClassify : classifyMaterialByTime(classify.materialCheckInfos)) {
                ClassifyMaterialView classifyMaterialView = new ClassifyMaterialView(getActivity());
                classifyMaterialView.setData(typeClassify, isVideoRoomEnter(), auxiliaryMaterialInfos);
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

    @Override
    public void onDestroy() {
        mMediaDownloadManager.unRegLoadListener(this);
        super.onDestroy();
    }

    /**
     * 提取审核通过的图片
     *
     * @param materialInfos 所有材料
     * @return 审核通过的材料
     */
    private List<AuxiliaryMaterialInfo> extractCheckSuccessMaterials(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        for (int i = LibCollections.size(materialInfos) - 1; i >= 0; i--) {
            AuxiliaryMaterialInfo materialInfo = materialInfos.get(i);
            if (materialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                newMaterialInfos.add(materialInfo);
            }
        }
        return newMaterialInfos;
    }

    private void getAuxiliaryAssistExamine(int appointmentId, int checkType) {
        AuxiliaryMaterialRequester requester = new AuxiliaryMaterialRequester(new DefaultResultCallback<AuxiliaryMaterialResult>() {
            @Override
            public void onSuccess(AuxiliaryMaterialResult auxiliaryMaterialResult, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "initAuxiliaryAssistExamineFragment-->auxiliaryMaterialResult:" + auxiliaryMaterialResult);
                if (null != auxiliaryMaterialResult) {
                    formatAllMaterial(auxiliaryMaterialResult.getAuxiliaryMaterialInfos(), checkType);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    private void formatAllMaterial(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, int checkType) {
        List<AuxiliaryMaterialInfo> auxiliaryMaterialInfosTemp = extractCheckSuccessMaterials(auxiliaryMaterialInfos);
        mAuxiliaryMaterialTypePicList = formatSuccessMaterial(TYPE_CHECK, auxiliaryMaterialInfosTemp);
        mAuxiliaryMaterialTimePicList = formatSuccessMaterial(TIME_CHECK, auxiliaryMaterialInfosTemp);
        mTimeClassifies = classifyMaterialByTime(auxiliaryMaterialInfosTemp);
        mTypeClassifies = classifyMaterialByType(auxiliaryMaterialInfosTemp);
        if (checkType == TYPE_CHECK) {
            checkSuccessMaterial(mAuxiliaryMaterialTypePicList, mTypeClassifies);
        } else {
            checkSuccessMaterial(mAuxiliaryMaterialTimePicList, mTimeClassifies);
        }
    }

    private List<AuxiliaryMaterialInfo> formatSuccessMaterial(int typeCheck, List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        List<AuxiliaryMaterialInfo> auxiliaryMaterialInfosResult = new ArrayList<>();
        List<MaterialClassify> materialClassifyList = new ArrayList<>();
        if (typeCheck == TYPE_CHECK) {
            materialClassifyList = classifyMaterialByType(auxiliaryMaterialInfos);
        } else {
            materialClassifyList = classifyMaterialByTime(auxiliaryMaterialInfos);
        }
        for (MaterialClassify materialClassify : materialClassifyList) {
            for (MaterialClassify material : classifyMaterialByTime(materialClassify.materialCheckInfos)) {
                for (AuxiliaryMaterialInfo materialInfo : material.materialCheckInfos) {
                    if (materialInfo.getMaterialType() == AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                        auxiliaryMaterialInfosResult.add(materialInfo);
                    }
                }
            }
        }
        return auxiliaryMaterialInfosResult;
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
        Collections.sort(unDicomMaterialInfos, new Comparator<AuxiliaryMaterialInfo>() {
            @Override
            public int compare(AuxiliaryMaterialInfo lhs, AuxiliaryMaterialInfo rhs) {
                int lId = lhs.getMaterialId();
                int rId = rhs.getMaterialId();
                int res = 0;
                if (lId != rId) {
                    res = lId > rId ? 1 : -1;
                }
                return res;
            }
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
        for (int i = LibCollections.size(materialInfos) - 1; i >= 0; i--) {
            AuxiliaryMaterialInfo materialInfo = materialInfos.get(i);
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

    @Override
    public void onStartDownload(String filePath) {
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
}
