package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.view.SlidingButtonView;


/**
 * Created by W·H·K on 2018/5/9.
 * Mod by Biao on 2019/7/1
 */
public class WaitRoundsPatientNewAdapter extends BaseQuickAdapter<WaitRoundsPatientInfo, BaseViewHolder> {
    private IonSlidingViewClickListener ionSlidingViewClickListener;

    public WaitRoundsPatientNewAdapter(int layoutResId, @Nullable List<WaitRoundsPatientInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaitRoundsPatientInfo item) {
        SlidingButtonView slidingButtonView = helper.getView(R.id.item_my_data_delete_view_sbv);

        slidingButtonView.setSlidingButtonListener(new SlidingButtonView.IonSlidingButtonListener() {
            @Override
            public void onMenuIsOpen(SlidingButtonView view) {
                Logger.logE(Logger.COMMON, "onMenuIsOpen");
            }

            @Override
            public void onDownOrMove(SlidingButtonView slidingButtonView) {
                Logger.logE(Logger.COMMON, "onDownOrMove");
//                if (menuIsOpen()) {
//                    if (mMenu != slidingButtonView) {
//                        closeMenu();
//                    }
//                }
            }
        });
        if (slidingButtonView.isOpen()) {
            slidingButtonView.closeMenu();
        }
        LinearLayout itemWaitRoundsPatientView = helper.getView(R.id.item_wait_rounds_patient_view);
        itemWaitRoundsPatientView.getLayoutParams().width = getScreenWidth(mContext);
        helper.addOnClickListener(R.id.item_wait_rounds_patient_view);
        helper.addOnClickListener(R.id.item_wait_rounds_patient_relation);
        if (allopen) {
            Logger.logI(Logger.COMMON, "打开？");
            slidingButtonView.openMenu();
            slidingButtonView.setCanTouch(false);
        } else {
            slidingButtonView.closeMenu();
            slidingButtonView.setCanTouch(true);
        }
        helper.setText(R.id.item_wait_rounds_patient_info_tv, item.getGender() == 1 ? "男" : "女" + item.getAge());
        helper.setText(R.id.item_wait_rounds_patient_appeal_tv, item.getPatientIllness() + "\n");
        helper.setText(R.id.item_wait_rounds_patient_num_tv, mContext.getString(R.string.rounds_medical_id, item.getMedicalId() + ""));
        helper.setText(R.id.item_wait_rounds_patient_time_tv, item.getInsertDt());
        int userId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        if (userId != item.getAttdocId()) {
            helper.setVisible(R.id.item_wait_rounds_patient_alike_department_im, true);
            helper.setVisible(R.id.item_wait_rounds_patient_doctor_tv, true);
            helper.setText(R.id.item_wait_rounds_patient_doctor_tv, item.getAttdocName());
        } else {
            helper.setGone(R.id.item_wait_rounds_patient_alike_department_im, false);
            helper.setGone(R.id.item_wait_rounds_patient_doctor_tv, false);
        }
        if (item.getImportant() == 1) {
            helper.setVisible(R.id.item_wait_rounds_patient_import, true);
        } else {
            helper.setGone(R.id.item_wait_rounds_patient_import, false);
        }
    }

    public void setIonSlidingViewClickListener(IonSlidingViewClickListener ionSlidingViewClickListener) {
        this.ionSlidingViewClickListener = ionSlidingViewClickListener;
    }

    boolean allopen = false;

    public void setAllopen(boolean allopen) {
        this.allopen = allopen;
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onRelationClick(View view, int position);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
