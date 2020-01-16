package cn.longmaster.hospital.doctor.ui.dutyclinic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDoctorListInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCRoomCallDoctorListAdapter;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * 值班门诊-医生选择框
 * Created by yangyong on 2019-12-02.
 */
public class DoctorChoiceDialog extends Dialog {
    @FindViewById(R.id.layout_dcroom_call_doctor_list_dialog_dcdoctor_list_rv)
    private RecyclerView recyclerView;
    @FindViewById(R.id.layout_dcroom_call_doctor_list_dialog_dcdoctor_list_srl)
    private SmartRefreshLayout layoutDcroomCallDoctorListDialogDcdoctorListSrl;
    private Context context;
    private int itemId;
    private DCRoomCallDoctorListAdapter dcDoctorListAdapter;

    private DoctorChoiceDialogStateChangeListener listener;
    private List<Integer> memberList = new ArrayList<>();

    public DoctorChoiceDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void setListener(DoctorChoiceDialogStateChangeListener listener) {
        this.listener = listener;
    }

    public void setMemberList(List<Integer> memberList) {
        this.memberList = memberList;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
        getDoctorList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dcroom_call_doctor_list_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutDcroomCallDoctorListDialogDcdoctorListSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDoctorList();
                refreshLayout.finishRefresh();
            }
        });
        layoutDcroomCallDoctorListDialogDcdoctorListSrl.setEnableLoadMore(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        dcDoctorListAdapter = new DCRoomCallDoctorListAdapter(R.layout.item_dcroom_call_dcotor_list, R.layout.layout_dcroom_call_doctor_list_header, new ArrayList<>(0));
        recyclerView.setAdapter(dcDoctorListAdapter);
    }

    @OnClick({R.id.layout_dcroom_call_doctor_list_dialog_close_iv,
            R.id.layout_dcroom_call_doctor_list_dialog_call_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dcroom_call_doctor_list_dialog_close_iv:
                dismiss();
                break;

            case R.id.layout_dcroom_call_doctor_list_dialog_call_btn:
                List<DCDoctorSectionInfo> doctorSectionInfos = new ArrayList<>();
                for (DCDoctorSectionInfo info : dcDoctorListAdapter.getData()) {
                    if (info.isChecked()) {
                        doctorSectionInfos.add(info);
                    }
                }
                if (LibCollections.isEmpty(doctorSectionInfos)) {
                    ToastUtils.showShort("请选择要呼叫的医生");
                    return;
                }
                listener.onChoicedDoctorList(doctorSectionInfos);
                dismiss();
                break;
            default:
                break;
        }
    }

    private void getDoctorList() {
        AppApplication.getInstance().getManager(DCManager.class).getDoctorList(itemId, 0, new DefaultResultCallback<List<DCProjectDoctorListInfo>>() {
            @Override
            public void onSuccess(List<DCProjectDoctorListInfo> doctorListInfos, BaseResult baseResult) {
                List<DCDoctorSectionInfo> dcDoctorSectionInfos = new ArrayList<>();
                for (DCProjectDoctorListInfo listInfo : doctorListInfos) {
                    List<DCDoctorSectionInfo> groupDoctorList = new ArrayList<>();
                    for (DCDoctorInfo doctorInfo : listInfo.getDoctorInfos()) {
                        if (memberList.contains(doctorInfo.getUserId())) {
                            continue;
                        }
                        groupDoctorList.add(new DCDoctorSectionInfo(false, "").initWithDoctorInfo(doctorInfo));
                    }
                    if (groupDoctorList.size() > 0) {
                        DCDoctorSectionInfo headerInfo = new DCDoctorSectionInfo(true, listInfo.getRoleName());
                        dcDoctorSectionInfos.add(headerInfo);
                        dcDoctorSectionInfos.addAll(groupDoctorList);
                    }
                }
                dcDoctorListAdapter.setNewData(dcDoctorSectionInfos);
            }
        });
    }

    public interface DoctorChoiceDialogStateChangeListener {
        void onChoicedDoctorList(List<DCDoctorSectionInfo> doctorSectionInfos);
    }
}
