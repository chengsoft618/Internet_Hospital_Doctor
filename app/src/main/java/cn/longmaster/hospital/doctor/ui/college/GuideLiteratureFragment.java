package cn.longmaster.hospital.doctor.ui.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.GuideDataInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.college.GetGuideDataRequester;
import cn.longmaster.hospital.doctor.ui.PDFViewActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.college.adapter.GuideLiteratureAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.ToastUtils;

/**
 * 指南文库fragment
 * Created by W·H·K on 2018/3/20.
 */
public class GuideLiteratureFragment extends NewBaseFragment {
    public final static String MODULE_TITLE = "module_title";
    @FindViewById(R.id.fragment_guide_literature_rv)
    private RecyclerView mRecyclerView;
    private GuideLiteratureAdapter mAdapter;
    private boolean mFragmentStop = false;

    public static GuideLiteratureFragment newInstance(String moduleTitle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MODULE_TITLE, moduleTitle);
        GuideLiteratureFragment blankFragment = new GuideLiteratureFragment();
        blankFragment.setArguments(bundle);
        return blankFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_guide_literature;
    }

    @Override
    public void initViews(View rootView) {
        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GuideLiteratureAdapter(R.layout.item_guide_literature, new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GuideDataInfo guideDataInfo = (GuideDataInfo) adapter.getItem(position);
            if (null == guideDataInfo) {
                return;
            }
            if (guideDataInfo.getContentUrl().endsWith(".pdf")) {
                PDFViewActivity.startActivity(getActivity(), guideDataInfo.getContentUrl(), guideDataInfo.getMaterialName());
            } else {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, guideDataInfo.getMaterialName());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, guideDataInfo.getContentUrl());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        final String moduleTitle = getArguments().getString(MODULE_TITLE);
        GetGuideDataRequester requester = new GetGuideDataRequester(new DefaultResultCallback<List<GuideDataInfo>>() {
            @Override
            public void onSuccess(List<GuideDataInfo> guideDataInfos, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "GuideLiteratureFragment->GetGuideDataRequester()->baseResult:" + baseResult + ",guideDataInfos:" + guideDataInfos);
                if (mFragmentStop) {
                    return;
                }
                mAdapter.setNewData(guideDataInfos);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_college_foot, mRecyclerView, false);
                view.setPadding(0, 0, 0, DisplayUtil.dip2px(25));
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), GuideLiteratureActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COLLEGE_TAB_NAME, moduleTitle);
                    startActivity(intent);
                });
                mAdapter.addFooterView(view);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.departmentId = 0;
        requester.pageIndex = MIN_PAGE_INDEX_1;
        requester.pageSize = PAGE_SIZE;
        requester.doPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentStop = true;
    }
}
