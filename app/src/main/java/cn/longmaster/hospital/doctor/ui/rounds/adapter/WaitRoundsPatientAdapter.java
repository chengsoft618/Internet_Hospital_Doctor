package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.view.SlidingButtonView;


/**
 * Created by W·H·K on 2018/5/9.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class WaitRoundsPatientAdapter extends RecyclerView.Adapter implements SlidingButtonView.IonSlidingButtonListener {
    private Context context;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private List<WaitRoundsPatientInfo> mDatas = new ArrayList<>();
    private SlidingButtonView mMenu = null;

    public void setmIDeleteBtnClickListener(IonSlidingViewClickListener iDeleteBtnClickListener) {
        mIDeleteBtnClickListener = iDeleteBtnClickListener;
    }

    public WaitRoundsPatientAdapter(Context context, List<WaitRoundsPatientInfo> date) {
        this.context = context;
        this.mDatas = date;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wait_rounds_patient, parent, false);
        return new MyViewHolder(view);
    }

    public List<WaitRoundsPatientInfo> getData() {
        return mDatas;
    }

    boolean allopen = false;

    public void setAllopen(boolean allopen) {
        this.allopen = allopen;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        Logger.logI(Logger.COMMON, "UMyDataAdapter：onBindViewHolder--：");
        displayView(viewHolder, position);
        viewHolder.slidingButtonView.setSlidingButtonListener(this);
        //设置内容布局的宽为屏幕宽度
        viewHolder.layout_content.getLayoutParams().width = getScreenWidth(context);
        viewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = viewHolder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }
            }
        });

        viewHolder.btnRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onRelationClick(v, n);
            }
        });
        Logger.logI(Logger.COMMON, "UsercenterFragment：getLaunchAuthority：position--：" + position + "是否开:" + allopen);
        if (allopen) {
            Logger.logI(Logger.COMMON, "打开？");
            viewHolder.slidingButtonView.openMenu();
            viewHolder.slidingButtonView.setCanTouch(false);
        } else {
            viewHolder.slidingButtonView.closeMenu();
            viewHolder.slidingButtonView.setCanTouch(true);
        }
    }

    private void displayView(MyViewHolder holder, int position) {
        holder.patientInfoTv.setText(mDatas.get(position).getPatientName() + " " + (mDatas.get(position).getGender() == 1 ? "男 " : "女 ") + mDatas.get(position).getAge());
        holder.patientAppealTv.setText(mDatas.get(position).getPatientIllness() + "\n");
        holder.patientNumTv.setText(context.getString(R.string.rounds_medical_id, mDatas.get(position).getMedicalId() + ""));
        holder.patientTimeTv.setText(mDatas.get(position).getInsertDt());
        int userId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        if (userId != mDatas.get(position).getAttdocId()) {
            holder.alikeDepartmentIm.setVisibility(View.VISIBLE);
            holder.doctorTv.setVisibility(View.VISIBLE);
            holder.doctorTv.setText(mDatas.get(position).getAttdocName());
        } else {
            holder.alikeDepartmentIm.setVisibility(View.GONE);
            holder.doctorTv.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getImportant() == 1) {
            holder.importIm.setVisibility(View.VISIBLE);
        } else {
            holder.importIm.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(SlidingButtonView view) {
        mMenu = view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        return mMenu != null;
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onRelationClick(View view, int position);
    }

    public void addData(List<WaitRoundsPatientInfo> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<WaitRoundsPatientInfo> list) {
        mDatas.clear();
        mDatas = list;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView alikeDepartmentIm;
        public ImageView importIm;
        public TextView patientInfoTv;
        public TextView patientAppealTv;
        public TextView btnRelation;
        public TextView patientNumTv;
        public TextView patientTimeTv;
        public TextView doctorTv;
        public ViewGroup layout_content;
        public SlidingButtonView slidingButtonView;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnRelation = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_relation);
            alikeDepartmentIm = (ImageView) itemView.findViewById(R.id.item_wait_rounds_patient_alike_department_im);
            importIm = (ImageView) itemView.findViewById(R.id.item_wait_rounds_patient_import);
            patientInfoTv = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_info_tv);
            patientAppealTv = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_appeal_tv);
            patientNumTv = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_num_tv);
            patientTimeTv = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_time_tv);
            doctorTv = (TextView) itemView.findViewById(R.id.item_wait_rounds_patient_doctor_tv);
            layout_content = (ViewGroup) itemView.findViewById(R.id.item_wait_rounds_patient_view);
            slidingButtonView = (SlidingButtonView) itemView;
        }
    }
}
