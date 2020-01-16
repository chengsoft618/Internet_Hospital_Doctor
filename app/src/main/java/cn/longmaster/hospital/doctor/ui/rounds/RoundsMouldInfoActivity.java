package cn.longmaster.hospital.doctor.ui.rounds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.google.android.flexbox.FlexboxLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.IntentionTimeInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAppeal;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.department.DepartmentListRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetConfigureFileRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetIntentionTimeConfigRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.AddDepartmentAdapter;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.AddIntentionTimeAdapter;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.AddRoundPatientAdapter;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.hospital.doctor.util.WheelViewHelper;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 查房预约信息填写界面
 */
public class RoundsMouldInfoActivity extends NewBaseActivity {
    private final int REQUEST_CODE_WRITE_INFO = 302; //添加患者
    private final int REQUEST_CODE_MODIFY_EXPERT = 204; //修改医生
    private final int REQUEST_CODE_ROUNDS_MODIFY_TIME = 206; //修改时间

    @FindViewById(R.id.act_rounds_add_info)
    private LinearLayout actRoundsAddInfo;
    @FindViewById(R.id.tool_bar_base)
    private Toolbar toolBarBase;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.act_rounds_add_expert_info_rl)
    private RelativeLayout actRoundsAddExpertInfoRl;
    @FindViewById(R.id.act_rounds_add_expert_tv)
    private TextView actRoundsAddExpertTv;
    @FindViewById(R.id.act_rounds_add_expert_name_tv)
    private TextView actRoundsAddExpertNameTv;
    @FindViewById(R.id.act_rounds_add_expert_hospital_tv)
    private TextView actRoundsAddExpertHospitalTv;
    @FindViewById(R.id.act_rounds_mould_modify_expert_iv)
    private ImageView actRoundsMouldModifyExpertIv;
    @FindViewById(R.id.act_rounds_add_choose_type_rl)
    private RelativeLayout actRoundsAddChooseTypeRl;
    @FindViewById(R.id.act_rounds_add_choose_type_note_tv)
    private TextView actRoundsAddChooseTypeNoteTv;
    @FindViewById(R.id.act_rounds_add_choose_type_note_desc_tv)
    private TextView actRoundsAddChooseTypeNoteDescTv;
    @FindViewById(R.id.act_rounds_add_expert_ll)
    private LinearLayout actRoundsAddExpertLl;
    @FindViewById(R.id.act_rounds_add_expert_mod_time_ll)
    private LinearLayout actRoundsAddExpertModTimeLl;
    @FindViewById(R.id.act_rounds_add_intention_time_rv)
    private RecyclerView actRoundsAddIntentionTimeRv;
    @FindViewById(R.id.act_rounds_add_department_ll)
    private LinearLayout actRoundsAddDepartmentLl;
    @FindViewById(R.id.act_rounds_add_triage_ll)
    private LinearLayout actRoundsAddTriageLl;
    @FindViewById(R.id.act_rounds_add_triage_add_time_desc_tv)
    private TextView actRoundsAddTriageAddTimeDescTv;
    @FindViewById(R.id.act_rounds_add_intention_add_time_stv)
    private SuperTextView actRoundsAddIntentionAddTimeStv;
    @FindViewById(R.id.act_rounds_add_triage_add_department_stv)
    private SuperTextView actRoundsAddTriageAddDepartmentStv;
    @FindViewById(R.id.act_rounds_add_triage_departments_rv)
    private RecyclerView actRoundsAddTriageDepartmentsRv;
    @FindViewById(R.id.act_rounds_add_triage_remark_et)
    private EditText actRoundsAddTriageRemarkEt;
    @FindViewById(R.id.act_rounds_add_lecture_topics_et)
    private EditText actRoundsAddLectureTopicsEt;
    @FindViewById(R.id.act_rounds_add_appeal_fbl)
    private FlexboxLayout actRoundsAddAppealFbl;
    @FindViewById(R.id.act_rounds_add_appeal_et)
    private EditText actRoundsAddAppealEt;
    @FindViewById(R.id.act_rounds_add_radio_group)
    private RadioGroup actRoundsAddRadioGroup;
    @FindViewById(R.id.act_rounds_add_radio_group_need)
    private RadioButton actRoundsAddRadioGroupNeed;
    @FindViewById(R.id.act_rounds_add_radio_group_no_need)
    private RadioButton actRoundsAddRadioGroupNoNeed;
    @FindViewById(R.id.act_rounds_add_intention_cast_time_ll)
    private LinearLayout actRoundsAddIntentionCastTimeLl;
    @FindViewById(R.id.act_rounds_add_time_tv)
    private TextView actRoundsAddTimeTv;
    @FindViewById(R.id.activity_not_receive_img)
    private ImageView activityNotReceiveImg;
    @FindViewById(R.id.act_rounds_add_patients_rv)
    private RecyclerView actRoundsAddPatientsRv;
    @FindViewById(R.id.act_rounds_add_add_patient_stv)
    private SuperTextView actRoundsAddAddPatientStv;
    @FindViewById(R.id.act_rounds_add_submission_btn)
    private TextView actRoundsAddSubmissionBtn;
    @FindViewById(R.id.act_rounds_add_choose_triage_tv)
    private TextView actRoundsAddChooseTriageTv;
    @FindViewById(R.id.act_rounds_add_choose_triage_iv)
    private ImageView actRoundsAddChooseTriageIv;
    @FindViewById(R.id.act_rounds_add_choose_expert_tv)
    private TextView actRoundsAddChooseExpertTv;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    //是否需要ppt
    private boolean isNeedPpt = true;
    private int mDoctorId = 0;
    private List<String> mIntentionTimeInfos = new ArrayList<>();
    private List<DepartmentListInfo> mIntentionDepartmentInfos = new ArrayList<>();

    private List<String> mAddIntentionTimeList;
    private List<DepartmentListInfo> mAddIntentionDepartmentList;
    //提交参数
    private RoundsAppointmentInfo mRoundsAppointmentInfo = new RoundsAppointmentInfo();
    //是否分诊0否1是-1什么都不是
    private final int ROUNDS_TYPE_IS_TRIAGE = 1;
    private final int ROUNDS_TYPE_IS_EXPERT = 0;
    private String mTimeText = "";

    private List<RoundsMedicalInfo> mRoundsMedicalList = new ArrayList<>();
    private List<WaitRoundsPatientInfo> mWaitRoundsPatientInfoMap = new ArrayList<>();
    private List<String> roundsAppealList = new ArrayList<>();
    private AddIntentionTimeAdapter mAddIntentionTimeAdapter;
    private AddDepartmentAdapter addDepartmentAdapter;
    private AddRoundPatientAdapter addRoundPatientAdapter;
    private String mDisplayHour;

    @Override
    protected void initDatas() {
        mDoctorId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
        mAddIntentionTimeList = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST);
        if (null == mAddIntentionTimeList) {
            mAddIntentionTimeList = new ArrayList<>();
        }
        if (null == mAddIntentionDepartmentList) {
            mAddIntentionDepartmentList = new ArrayList<>();
        }
        mAddIntentionTimeAdapter = new AddIntentionTimeAdapter(R.layout.item_add_intention_time, mAddIntentionTimeList);
        mAddIntentionTimeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            String item = (String) adapter.getItem(position);
            if (null == item) {
                return;
            }
            if (view.getId() == R.id.item_add_intention_delete) {
                adapter.remove(position);
            }
        });
        addDepartmentAdapter = new AddDepartmentAdapter(R.layout.item_add_intention_department, mAddIntentionDepartmentList);
        addDepartmentAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DepartmentListInfo info = (DepartmentListInfo) adapter.getItem(position);
            if (null == info) {
                return;
            }
            if (view.getId() == R.id.item_add_intention_department_del_iv) {
                adapter.remove(position);
            }
        });
        addRoundPatientAdapter = new AddRoundPatientAdapter(R.layout.layout_rounds_mould_info_medical_item, mWaitRoundsPatientInfoMap);
        addRoundPatientAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            new CommonDialog.Builder(this)
                    .setMessage(R.string.rounds_medical_record_module_is_delete)
                    .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                    })
                    .setNegativeButton(R.string.fill_consult_dialog_ok, () -> adapter.remove(position))
                    .show();

        });
        addRoundPatientAdapter.setOnPicClickListener((picAdapter, view, serverUrls, position) -> {
            getDisplay().startFirstJourneyPicBrowseActivity(serverUrls, null, false, position, 0, 0);
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_mould_info;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText(getString(R.string.rounds_mould_info));
        actRoundsAddIntentionTimeRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actRoundsAddIntentionTimeRv.setAdapter(mAddIntentionTimeAdapter);
        actRoundsAddPatientsRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actRoundsAddPatientsRv.setAdapter(addRoundPatientAdapter);
        initRoundsView(mDoctorId);
        initListener();
        getRoundsAppeal();
    }

    @Override
    public void onBackPressed() {
        showFinishDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.APPOINTMENT, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_WRITE_INFO:
                    List<WaitRoundsPatientInfo> waitRoundsPatientInfoMap = (ArrayList<WaitRoundsPatientInfo>) data.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_WAIT_ROUNDS_PATIENT_INFO);
                    displayPatientInfo(waitRoundsPatientInfoMap);
                    break;
                case REQUEST_CODE_MODIFY_EXPERT:
                    int doctorId = 0;
                    List<String> addIntentionTimeList = null;
                    if (null != data) {
                        doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
                        addIntentionTimeList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST);
                    }
                    displayExpertInfo(doctorId, addIntentionTimeList);
                    break;
                case REQUEST_CODE_ROUNDS_MODIFY_TIME:
                    mAddIntentionTimeList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST);
                    if (null == mAddIntentionTimeList) {
                        mAddIntentionTimeList = new ArrayList<>();
                    }
                    mAddIntentionTimeAdapter.setCanDelete(false);
                    mAddIntentionTimeAdapter.setNewData(mAddIntentionTimeList);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayExpertInfo(int doctorId, List<String> addIntentionTimeList) {
        mDoctorId = doctorId;
        initRoundsView(mDoctorId);
        mAddIntentionDepartmentList.clear();
        mAddIntentionTimeList.clear();
        actRoundsAddTriageRemarkEt.setText(null);
        if (LibCollections.isNotEmpty(addIntentionTimeList)) {
            mAddIntentionTimeList = addIntentionTimeList;
        }
        mAddIntentionTimeAdapter.setCanDelete(false);
        mAddIntentionTimeAdapter.setNewData(mAddIntentionTimeList);

    }

    private void displayPatientInfo(List<WaitRoundsPatientInfo> waitRoundsPatientInfoMap) {
        addRoundPatientAdapter.setNewData(waitRoundsPatientInfoMap);
        mWaitRoundsPatientInfoMap = waitRoundsPatientInfoMap;

    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        actRoundsAddRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.act_rounds_add_radio_group_need:
                    isNeedPpt = true;//需要ppt
                    break;
                case R.id.act_rounds_add_radio_group_no_need:
                    isNeedPpt = false;//不需要ppt
                    break;
                default:
                    break;
            }
            Logger.logI(Logger.COMMON, "RoundsMouldInfoActivity-->onCheckedChanged:" + checkedId);
        });
        actRoundsAddChooseTriageTv.setOnClickListener(v -> {
            setTriageButton(true);
            mDoctorId = 0;
            initRoundsView(mDoctorId);
        });

        actRoundsAddChooseExpertTv.setOnClickListener(v -> getDisplay().startRoundsChoiceDoctorActivity(REQUEST_CODE_MODIFY_EXPERT));
        actRoundsMouldModifyExpertIv.setOnClickListener(v -> {
            getDisplay().startRoundsChoiceDoctorActivity(REQUEST_CODE_MODIFY_EXPERT);
        });
        actRoundsAddIntentionAddTimeStv.setOnClickListener(v -> {
            if (mDoctorId <= 0) {
                showIntentionTimeDialog(v, createItemsForIntentionTimeLeft(), createItemsForIntentionTimeRight());
            } else {
                getDisplay().startSelectionTimeActivity(mDoctorId, true, (ArrayList<String>) mAddIntentionTimeList, REQUEST_CODE_ROUNDS_MODIFY_TIME);
            }
        });
        actRoundsAddTriageAddDepartmentStv.setOnClickListener(v -> {
            if (LibCollections.isEmpty(mIntentionDepartmentInfos)) {
                getDepartmentList(v);
            } else {
                showIntentionDepartmentDialog(v, createItemsForIntentionDepartment(mIntentionDepartmentInfos));
            }
        });
        actRoundsAddIntentionCastTimeLl.setOnClickListener(view -> {
            if (LibCollections.isEmpty(mIntentionTimeInfos)) {
                getIntentionTime(view);
            } else {
                showIntentionCastTimeDialog(view, mIntentionTimeInfos);
            }
        });
        actRoundsAddAddPatientStv.setOnClickListener(v -> {
            getDisplay().startWaitRoundsPatientActivity(0, (ArrayList<WaitRoundsPatientInfo>) mWaitRoundsPatientInfoMap, REQUEST_CODE_WRITE_INFO);
        });
        actRoundsAddSubmissionBtn.setOnClickListener(v -> {
            submitRounds();
        });

    }

    private void initRoundsView(int doctorId) {
        if (doctorId > 0) {
            initExpertView(doctorId);
        } else {
            initTriageView(doctorId);
        }
    }

    /**
     * 选择专家初始化View
     */
    private void initExpertView(int doctorId) {
        actRoundsAddExpertInfoRl.setVisibility(View.VISIBLE);
        actRoundsAddTriageLl.setVisibility(View.GONE);
        actRoundsAddChooseTypeRl.setVisibility(View.GONE);
        actRoundsAddDepartmentLl.setVisibility(View.GONE);
        actRoundsAddTriageAddTimeDescTv.setVisibility(View.GONE);
        if (doctorId != 0) {
            getDoctorInfo(doctorId);
        }
    }

    /**
     * 分诊中心初始化View
     */
    private void initTriageView(int doctorId) {
        actRoundsAddTriageLl.setVisibility(View.VISIBLE);
        actRoundsAddDepartmentLl.setVisibility(View.VISIBLE);
        actRoundsAddTriageAddTimeDescTv.setVisibility(View.VISIBLE);
        actRoundsAddChooseTypeRl.setVisibility(View.VISIBLE);
        actRoundsAddExpertInfoRl.setVisibility(View.GONE);
        setTriageButton(true);
        actRoundsAddTriageDepartmentsRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actRoundsAddTriageDepartmentsRv.setAdapter(addDepartmentAdapter);
        if (doctorId != 0) {
            getDoctorInfo(doctorId);
        }
    }

    private void setTriageButton(boolean isTriage) {
        if (isTriage) {
            actRoundsAddChooseTriageTv.setTextColor(ContextCompat.getColor(getThisActivity(), R.color.color_049eff));
            actRoundsAddChooseTriageTv.setBackgroundResource(R.drawable.bg_solid_ffffff_radius_45);
            actRoundsAddChooseTriageIv.setVisibility(View.VISIBLE);
        } else {
            actRoundsAddChooseTriageIv.setVisibility(View.GONE);
            actRoundsAddChooseTriageTv.setTextColor(ContextCompat.getColor(getThisActivity(), R.color.color_white));
            actRoundsAddChooseTriageTv.setBackgroundResource(R.drawable.bg_solid_0290ef_radius_45);
        }
    }

    /**
     * 填充查房诉求
     *
     * @param list
     */
    private void displayRoundsAppeal(List<RoundsAppeal> list) {
        for (RoundsAppeal appeal : list) {
            View view = getLayoutInflater().inflate(R.layout.view_appeals_standard, null);
            RelativeLayout viewAppealsStandardRl = view.findViewById(R.id.view_appeals_standard_rl);
            ImageView viewAppealsStandardIv = view.findViewById(R.id.view_appeals_standard_iv);
            final CheckBox appealCb = view.findViewById(R.id.view_appeals_standard_tv);
            appealCb.setText(appeal.getName());
            appealCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewAppealsStandardRl.setBackgroundResource(R.drawable.bg_solid_ecf1fd_radius_8);
                    viewAppealsStandardIv.setVisibility(View.VISIBLE);
                    roundsAppealList.add(getString(appealCb));
                } else {
                    viewAppealsStandardRl.setBackgroundResource(R.drawable.bg_solid_f0f0f0_radius_8);
                    viewAppealsStandardIv.setVisibility(View.GONE);
                    roundsAppealList.remove(getString(appealCb));
                }
            });
            actRoundsAddAppealFbl.addView(view);
        }
    }

    /**
     * 填充医生信息
     *
     * @param doctorBaseInfo
     */
    private void displayDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        mRoundsAppointmentInfo.setDoctorName(doctorBaseInfo.getRealName());
        mRoundsAppointmentInfo.setDoctorHospital(doctorBaseInfo.getHospitalName());
        mRoundsAppointmentInfo.setDoctorDepartment(doctorBaseInfo.getDepartmentName());
        mRoundsAppointmentInfo.setDoctorLevel(doctorBaseInfo.getDoctorLevel());

        actRoundsAddExpertNameTv.setText(doctorBaseInfo.getRealName());
        actRoundsAddExpertHospitalTv.setText(doctorBaseInfo.getHospitalName() + " " + doctorBaseInfo.getDepartmentName() + " " + doctorBaseInfo.getDoctorLevel());
    }

    private void displayNoEnterDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        StringBuilder remark = new StringBuilder();
        remark.append("您当前选择的医生：");
        remark.append(doctorBaseInfo.getHospitalName());
        remark.append(doctorBaseInfo.getDepartmentName());
        remark.append(doctorBaseInfo.getRealName());
        remark.append("（未入驻），我们将尽快与该医生进行沟通，并在两小时内以电话的形式给您回复");
        actRoundsAddTriageRemarkEt.setText(remark);
    }

    /**
     * 选择意向时间
     */
    private void showIntentionTimeDialog(View view, List<String> leftItems, List<String> rightItems) {
        new WheelViewHelper.RoundsChooseTimeBuilder().setActivity(getThisActivity())
                .setParent(view)
                .setLeftItems(leftItems)
                .setRightItems(rightItems)
                .setOnCommitListener((leftIndex, rightIndex, result) -> {
                    if (LibCollections.size(mAddIntentionTimeList) >= 5) {
                        ToastUtils.showShort(R.string.rounds_most_five);
                        return;
                    }
                    if (mAddIntentionTimeList.contains(result)) {
                        ToastUtils.showShort(R.string.rounds_time_repetition);
                        return;
                    }
                    mAddIntentionTimeList.add(result);
                    mAddIntentionTimeAdapter.setCanDelete(true);
                    mAddIntentionTimeAdapter.setNewData(mAddIntentionTimeList);
                }).build();
    }

    private String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + (24 * past));
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String result = CommonlyUtil.getWeek(today, getThisActivity()) + " " + format.format(today);
        Logger.logI(Logger.COMMON, "shwDatePopupWindow-当前的时间是：result" + result);
        if (past == 0) {
            SimpleDateFormat f = new SimpleDateFormat("HH");
            mDisplayHour = f.format(today);
        }
        return result;
    }

    private void showIntentionDepartmentDialog(View parent, List<String> items) {
        new WheelViewHelper.OneWheelViewBuilder().setActivity(getThisActivity())
                .setParent(parent)
                .setTitle(items.get(0))
                .setItems(items)
                .setOnCommitListener((selectedIndex, item) -> {
                    DepartmentListInfo info = mIntentionDepartmentInfos.get(selectedIndex);
                    if (mAddIntentionDepartmentList.contains(info)) {
                        ToastUtils.showShort(R.string.rounds_department_election);
                        return;
                    }
                    addDepartmentAdapter.addData(mIntentionDepartmentInfos.get(selectedIndex));
                }).build();
    }

    /**
     * 意向时长
     *
     * @param parent
     */
    private void showIntentionCastTimeDialog(View parent, List<String> items) {
        new WheelViewHelper.OneWheelViewBuilder().setActivity(getThisActivity())
                .setParent(parent)
                .setTitle(items.get(0))
                .setItems(items)
                .setOnCommitListener((selectedIndex, item) -> {
                    actRoundsAddTimeTv.setText(item);
                    actRoundsAddTimeTv.setTextColor(getCompatColor(R.color.color_1a1a1a));
                    mTimeText = StringUtils.substringBefore(item, "小时");
                }).build();
    }

    /**
     * 提交查房
     */
    private void submitRounds() {
        if (LibCollections.isEmpty(mAddIntentionTimeList)) {
            ToastUtils.showShort(R.string.rounds_intention_time_not_empty);
            return;
        }
        if (LibCollections.isEmpty(mAddIntentionDepartmentList) && mDoctorId <= 0) {
            ToastUtils.showShort(R.string.rounds_intention_department_not_empty);
            return;
        }
        StringBuilder appeal = new StringBuilder();
        for (String appealItem : roundsAppealList) {
            appeal.append(appealItem);
            appeal.append("。");
        }
        appeal.append(getString(actRoundsAddAppealEt));
        if (StringUtils.isTrimEmpty(appeal)) {
            ToastUtils.showShort(R.string.rounds_appeal_not_empty);
            return;
        }
        if (StringUtils.isEmpty(mTimeText)) {
            ToastUtils.showShort(R.string.rounds_intention_time_cast_not_empty);
            return;
        }
        List<Integer> mMedicalRecords = createMedicalIds();
        mRoundsAppointmentInfo.setDoctorId(mDoctorId);
        mRoundsAppointmentInfo.setLectureTopics(getString(actRoundsAddLectureTopicsEt));
        mRoundsAppointmentInfo.setAppeal(appeal.toString());
        mRoundsAppointmentInfo.setLengthTime(mTimeText);
        mRoundsAppointmentInfo.setNeedPPT(isNeedPpt);
        mRoundsAppointmentInfo.setMedicalInfoList(addRoundPatientAdapter.getRoundsMedicalDetailsInfos());
        mRoundsAppointmentInfo.setIntentionTimeList(mAddIntentionTimeList);
        mRoundsAppointmentInfo.setIntentionDepartmentList(mAddIntentionDepartmentList);
        mRoundsAppointmentInfo.setDepartmentIdList(getDepartmentIdList(mAddIntentionDepartmentList));
        mRoundsAppointmentInfo.setMedicalList(mMedicalRecords);
        mRoundsAppointmentInfo.setRemarks(getString(actRoundsAddTriageRemarkEt));
        getDisplay().startRoundsInfoConfirmActivity(mRoundsAppointmentInfo, 0);
    }

    private List<Integer> createMedicalIds() {
        if (null == addRoundPatientAdapter) {
            return new ArrayList<>(0);
        }
        List<Integer> patientIds = new ArrayList<>();
        for (WaitRoundsPatientInfo info : addRoundPatientAdapter.getData()) {
            patientIds.add(info.getMedicalId());
        }
        return patientIds;
    }

    private List<Integer> getDepartmentIdList(List<DepartmentListInfo> departmentListInfos) {
        if (null == departmentListInfos) {
            return new ArrayList<>();
        }
        List<Integer> departmentIdList = new ArrayList<>(LibCollections.size(departmentListInfos));
        for (DepartmentListInfo info : departmentListInfos) {
            departmentIdList.add(info.getDepartmentId());
        }
        return departmentIdList;
    }

    /**
     * 显示结束预约弹框
     */
    private void showFinishDialog() {
        new CommonDialog.Builder(this)
                .setMessage(R.string.rounds_info_fill_break)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, () -> {
                    finish();
                })
                .show();
    }


    /**
     * 获取查房诉求
     */
    private void getRoundsAppeal() {
        GetConfigureFileRequester requester = new GetConfigureFileRequester((baseResult, list) -> {
            Logger.logI(Logger.COMMON, "GetConfigureFileRequester-->baseResult:" + baseResult + ".list：" + list);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && list != null) {
                displayRoundsAppeal(list);
            }
        });
        requester.doPost();
    }

    /**
     * 获取医生信息
     *
     * @param mDoctorId
     */
    private void getDoctorInfo(int mDoctorId) {
        mDoctorManager.getDoctorInfo(mDoctorId, false, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null != doctorBaseInfo) {
                    if (mDoctorId > 0) {
                        displayDoctorInfo(doctorBaseInfo);
                    } else {
                        displayNoEnterDoctorInfo(doctorBaseInfo);
                    }
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void getDepartmentList(View view) {
        DepartmentListRequester requester = new DepartmentListRequester((baseResult, departments) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mIntentionDepartmentInfos = departments;
                showIntentionDepartmentDialog(view, createItemsForIntentionDepartment(mIntentionDepartmentInfos));
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.doPost();
    }

    private void getIntentionTime(View view) {
        GetIntentionTimeConfigRequester requester = new GetIntentionTimeConfigRequester(new DefaultResultCallback<List<IntentionTimeInfo>>() {
            @Override
            public void onSuccess(List<IntentionTimeInfo> intentionTimeInfos, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "GetIntentionTimeConfigRequester-->baseResult:" + baseResult + ".intentionTimeInfos：" + intentionTimeInfos);
                if (LibCollections.isNotEmpty(intentionTimeInfos)) {
                    mIntentionTimeInfos = createItemsIntentionTimeCast(intentionTimeInfos);
                    showIntentionCastTimeDialog(view, mIntentionTimeInfos);
                }
            }
        });
        requester.start();
    }

    /**
     * 意向时间
     *
     * @return
     */
    private List<String> createItemsForIntentionTimeLeft() {
        List<String> items = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            items.add(getFetureDate(i));
        }
        return items;
    }

    /**
     * 意向时间
     *
     * @return
     */
    private List<String> createItemsForIntentionTimeRight() {
        List<String> hourList = Arrays.asList(getResources().getStringArray(R.array.all_day_hour_desc));
        for (int i = 0; i < hourList.size(); i++) {
            if (hourList.get(i).equals(mDisplayHour)) {
                break;
            }
        }
        return hourList;
    }

    /**
     * 意向时长
     *
     * @param infors
     * @return
     */
    private List<String> createItemsIntentionTimeCast(List<IntentionTimeInfo> infors) {
        List<String> items = new ArrayList<>(LibCollections.size(infors));
        for (IntentionTimeInfo info : infors) {
            items.add(info.getDuration() + info.getUnit());
        }
        return items;
    }

    /**
     * 意向科室
     *
     * @param infos
     * @return
     */
    private List<String> createItemsForIntentionDepartment(@NonNull List<DepartmentListInfo> infos) {
        List<String> items = new ArrayList<>(LibCollections.size(infos));
        for (DepartmentListInfo info : infos) {
            items.add(info.getDepartmentName());
        }
        return items;
    }
}
