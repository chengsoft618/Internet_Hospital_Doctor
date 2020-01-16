package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.DisplayUtil;

/**
 * Dicom分页显示适配器
 * Created by Tengshuxiang on 2016-08-18.
 */
public class DicomPageAdapter extends PagerAdapter {

    private Context mContext;
    private List<List<AuxiliaryMaterialInfo>> groupList;
    private int mLength;

    public DicomPageAdapter(Context context, List<List<AuxiliaryMaterialInfo>> groupList) {
        this.mContext = context;
        this.groupList = groupList;
        //根据布局中的padding spacing计算item的（正方形）边长
        mLength = (ScreenUtil.getScreenWidth() - DisplayUtil.dip2px(15 * 2 + 10f * 2 + 6f * 3)) / 4;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_image_medical_dcm_pic, null);
        LinearLayout oneLl = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_one_ll);
        LinearLayout twoLl = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_two_ll);
        LinearLayout threeLl = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_three_ll);
        LinearLayout fourLl = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_four_ll);

        ImageView oneAiv = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_one_aiv);
        ImageView twoAiv = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_two_aiv);
        ImageView threeAiv = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_three_aiv);
        ImageView fourAiv = linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_four_aiv);

        ViewGroup.LayoutParams itemOneLp = oneAiv.getLayoutParams();
        itemOneLp.height = mLength;
        itemOneLp.width = mLength;
        oneAiv.setLayoutParams(itemOneLp);

        ViewGroup.LayoutParams itemTwoLp = twoAiv.getLayoutParams();
        itemTwoLp.height = mLength;
        itemTwoLp.width = mLength;
        twoAiv.setLayoutParams(itemTwoLp);

        ViewGroup.LayoutParams itemThreeLp = threeAiv.getLayoutParams();
        itemThreeLp.height = mLength;
        itemThreeLp.width = mLength;
        threeAiv.setLayoutParams(itemThreeLp);

        ViewGroup.LayoutParams itemFourLp = fourAiv.getLayoutParams();
        itemFourLp.height = mLength;
        itemFourLp.width = mLength;
        fourAiv.setLayoutParams(itemFourLp);

        TextView nameOneTv = (TextView) linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_name_one_tv);
        TextView nameTwoTv = (TextView) linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_name_two_tv);
        TextView nameThreeTv = (TextView) linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_name_three_tv);
        TextView nameFourTv = (TextView) linearLayout.findViewById(R.id.layout_image_medical_dcm_pic_name_four_tv);


        final List<AuxiliaryMaterialInfo> checkInfos = groupList.get(position);
        loadPic(oneAiv, nameOneTv, checkInfos.get(0));
        regListener(oneLl, position, 0);

        if (checkInfos.size() >= 2) {
            twoLl.setVisibility(View.VISIBLE);
            loadPic(twoAiv, nameTwoTv, checkInfos.get(1));
            regListener(twoLl, position, 1);
        }

        if (checkInfos.size() >= 3) {
            threeLl.setVisibility(View.VISIBLE);
            loadPic(threeAiv, nameThreeTv, checkInfos.get(2));
            regListener(threeLl, position, 2);
        }

        if (checkInfos.size() >= 4) {
            fourLl.setVisibility(View.VISIBLE);
            loadPic(fourAiv, nameFourTv, checkInfos.get(3));
            regListener(fourLl, position, 3);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(linearLayout, lp);
        return linearLayout;
    }

    private void loadPic(ImageView asyncImageView, TextView name, AuxiliaryMaterialInfo checkInfo) {
        name.setText(checkInfo.getMaterialName());
    }

    private void regListener(View view, final int position, final int index) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuxiliaryMaterialInfo> materialCheckInfoList = new ArrayList<>();
                for (int i = 0; i < groupList.size(); i++) {
                    materialCheckInfoList.addAll(groupList.get(i));
                }
                AuxiliaryMaterialInfo checkInfo = materialCheckInfoList.get(4 * position + index);
                Intent intent = new Intent(mContext, BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, checkInfo.getDicom());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, checkInfo.getMaterialName());
                mContext.startActivity(intent);
            }
        });
    }
}
