package cn.longmaster.hospital.doctor.ui.hospital;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.ProvinceInfo;
import cn.longmaster.hospital.doctor.core.manager.common.LocalManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.hospital.GetHospitalByCityRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.CityAdapter;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.HospitalFilterAdapter;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.ProvinceAdapter;
import cn.longmaster.hospital.doctor.view.MyStatusBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author lm_Abiao
 * @date 2019/5/29 20:00
 * @description: 通过城市筛选医院界面
 */
public class HospitalFilterByCityActivity extends NewBaseActivity {
    @FindViewById(R.id.act_hospital_filter_by_city_msb)
    private MyStatusBar actHospitalFilterByCityMsb;
    @FindViewById(R.id.doctor_list_search_title_back_iv)
    private ImageView doctorListSearchTitleBackIv;
    @FindViewById(R.id.fragment_doctor_list_search_stv)
    private SuperTextView fragmentDoctorListSearchStv;
    @FindViewById(R.id.act_hospital_filter_by_city_province_tv)
    private TextView actHospitalFilterByCityProvinceTv;
    @FindViewById(R.id.act_hospital_filter_by_city_city_tv)
    private TextView actHospitalFilterByCityCityTv;
    @FindViewById(R.id.act_hospital_filter_by_city_hospital_tv)
    private TextView actHospitalFilterByCityHospitalTv;
    @FindViewById(R.id.act_hospital_filter_by_city_result_indicator_v)
    private View actHospitalFilterByCityResultIndicatorV;
    @FindViewById(R.id.act_hospital_filter_by_city_province_nsv)
    private NestedScrollView actHospitalFilterByCityProvinceNsv;
    @FindViewById(R.id.act_hospital_filter_by_city_hot_province_rv)
    private RecyclerView actHospitalFilterByCityHotProvinceRv;
    @FindViewById(R.id.act_hospital_filter_by_city_province_rv)
    private RecyclerView actHospitalFilterByCityProvinceRv;
    @FindViewById(R.id.act_hospital_filter_by_city_city_rv)
    private RecyclerView actHospitalFilterByCityCityRv;
    @FindViewById(R.id.act_hospital_filter_by_city_hospital_srl)
    private SmartRefreshLayout actHospitalFilterByCityHospitalSrl;
    @FindViewById(R.id.act_hospital_filter_by_city_hospital_rv)
    private RecyclerView actHospitalFilterByCityHospitalRv;

    private ProvinceAdapter provinceAdapter;
    private ProvinceAdapter hotProvinceAdapter;
    private CityAdapter cityAdapter;
    private HospitalFilterAdapter hospitalFilterAdapter;
    @AppApplication.Manager
    LocalManager localManager;
    private final int REQUEST_CODE_FOR_SEARCH_HOSPITAL = 1024;
    private final int REQUEST_CODE_FOR_FILTER_BY_CITY_HOSPITAL = 1026;
    private String provinceName;
    private String cityName;
    private final int TAB_INDEX_PROVINCE = 0;
    private final int TAB_INDEX_CITY = 1;
    private final int TAB_INDEX_HOSPITAL = 3;
    private int tabIndex = TAB_INDEX_PROVINCE;

    @Override
    protected void initDatas() {
        provinceAdapter = new ProvinceAdapter(R.layout.item_double_click_province, new ArrayList<>(0));
        hotProvinceAdapter = new ProvinceAdapter(R.layout.item_double_click_province, new ArrayList<>());
        cityAdapter = new CityAdapter(R.layout.item_double_click_city, new ArrayList<>());

        provinceAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProvinceInfo provinceInfo = (ProvinceInfo) adapter.getItem(position);
            hotProvinceAdapter.clearSelected();
            clickProvince(provinceAdapter, position, provinceInfo);
        });
        hotProvinceAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProvinceInfo provinceInfo = (ProvinceInfo) adapter.getItem(position);
            provinceAdapter.clearSelected();
            clickProvince(hotProvinceAdapter, position, provinceInfo);
        });
        cityAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProvinceInfo.CityListBean cityListBean = (ProvinceInfo.CityListBean) adapter.getItem(position);
            clickCity(position, cityListBean);
        });
        hospitalFilterAdapter = new HospitalFilterAdapter(R.layout.item_hospital_list_filter_layout, new ArrayList<>(0));
        hospitalFilterAdapter.setOnItemClickListener((adapter, view, position) -> {
            HospitalInfo hospitalInfo = (HospitalInfo) adapter.getItem(position);
            if (null != hospitalInfo) {
                Intent intent = new Intent();
                intent.putExtra("hospital", hospitalInfo.getHospitalName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        getProvinces();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_list_filter_hospital;
    }

    @Override
    protected void initViews() {
        actHospitalFilterByCityHotProvinceRv.setNestedScrollingEnabled(false);
        actHospitalFilterByCityProvinceRv.setNestedScrollingEnabled(false);
        actHospitalFilterByCityHotProvinceRv.setAdapter(hotProvinceAdapter);
        actHospitalFilterByCityProvinceRv.setAdapter(provinceAdapter);
        actHospitalFilterByCityCityRv.setAdapter(cityAdapter);
        actHospitalFilterByCityHospitalRv.setAdapter(hospitalFilterAdapter);
        actHospitalFilterByCityHotProvinceRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actHospitalFilterByCityProvinceRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actHospitalFilterByCityCityRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actHospitalFilterByCityHospitalRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        initListener();
        updateTabVisibility();
        updateContentVisibility();
        updateIndicator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE_FOR_FILTER_BY_CITY_HOSPITAL) {
                String hospitalName = data.getStringExtra("hospital");
                if (null != hospitalName) {
                    Intent intent = new Intent();
                    intent.putExtra("hospital", hospitalName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else if (requestCode == REQUEST_CODE_FOR_SEARCH_HOSPITAL) {
                String hospitalName = data.getStringExtra("hospital");
                if (null != hospitalName) {
                    Intent intent = new Intent();
                    intent.putExtra("hospital", hospitalName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    private void initListener() {
        doctorListSearchTitleBackIv.setOnClickListener(v -> onBackPressed());
        fragmentDoctorListSearchStv.setOnClickListener(v -> {
            Intent intent = new Intent(HospitalFilterByCityActivity.this, HospitalSearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FOR_SEARCH_HOSPITAL);
        });
        actHospitalFilterByCityProvinceTv.setOnClickListener(v -> {
            cityAdapter.clearSelected();
            tabIndex = TAB_INDEX_PROVINCE;
            updateTabVisibility();
            updateContentVisibility();
            updateIndicator();
        });
        actHospitalFilterByCityCityTv.setOnClickListener(v -> {
            tabIndex = TAB_INDEX_CITY;
            updateTabVisibility();
            updateContentVisibility();
            updateIndicator();
        });
        actHospitalFilterByCityHospitalTv.setOnClickListener(v -> {
            tabIndex = TAB_INDEX_HOSPITAL;
            updateTabVisibility();
            updateContentVisibility();
            updateIndicator();
        });
        actHospitalFilterByCityHospitalSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX++;
                getHospitalList(refreshLayout, false, provinceName, cityName);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                hospitalFilterAdapter.setNewData(null);
                PAGE_INDEX = MIN_PAGE_INDEX_1;
                getHospitalList(refreshLayout, true, provinceName, cityName);
            }
        });
    }

    private void clickCity(int position, ProvinceInfo.CityListBean cityListBean) {
        cityAdapter.setSelected(position);
        tabIndex = TAB_INDEX_HOSPITAL;
        updateTabVisibility();
        updateContentVisibility();
        updateIndicator();
        if (null != cityListBean) {
            cityName = cityListBean.getCity();
            actHospitalFilterByCityCityTv.setText(cityName);
            actHospitalFilterByCityHospitalTv.setVisibility(View.VISIBLE);
            actHospitalFilterByCityHospitalSrl.autoRefresh();
        }
    }

    private void clickProvince(ProvinceAdapter adapter, int position, ProvinceInfo provinceInfo) {
        tabIndex = TAB_INDEX_CITY;
        updateTabVisibility();
        updateContentVisibility();
        updateIndicator();
        adapter.setSelected(position);
        if (null != provinceInfo) {
            provinceName = provinceInfo.getProvince();
            actHospitalFilterByCityProvinceTv.setText(provinceName);
            actHospitalFilterByCityCityTv.setVisibility(View.VISIBLE);
            if (LibCollections.isNotEmpty(provinceInfo.getCityList())) {
                cityAdapter.setNewData(provinceInfo.getCityList());
            }
        }
    }

    /**
     * 获取医院
     *
     * @param refreshLayout
     * @param isRefresh
     * @param provinceName
     * @param cityName
     */
    private void getHospitalList(RefreshLayout refreshLayout, boolean isRefresh, String provinceName, String cityName) {
        GetHospitalByCityRequester requester = new GetHospitalByCityRequester(new DefaultResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos, BaseResult baseResult) {
                if (PAGE_INDEX == MIN_PAGE_INDEX_1) {
                    if (LibCollections.isEmpty(hospitalInfos)) {
                        hospitalFilterAdapter.setEmptyView(createEmptyListView("暂无相关数据"));
                    }
                    hospitalFilterAdapter.setNewData(hospitalInfos);
                } else if (LibCollections.isNotEmpty(hospitalInfos)) {
                    hospitalFilterAdapter.addData(hospitalInfos);
                }
                if (baseResult.isFinish()) {
                    actHospitalFilterByCityHospitalSrl.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
        requester.setProvince(provinceName);
        requester.setCity(cityName);
        requester.setPageIndex(PAGE_INDEX);
        requester.setPageSize(PAGE_SIZE);
        requester.start();
    }

    private void getProvinces() {
        localManager.getLocal(new LocalManager.OnProvinceLoadListener() {
            @Override
            public void onSuccess(List<ProvinceInfo> provinceInfos) {
                hotProvinceAdapter.setNewData(createHotProvinceInfo(provinceInfos));
                provinceAdapter.setNewData(provinceInfos);
            }

            @Override
            public void onFail(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private List<ProvinceInfo> createHotProvinceInfo(List<ProvinceInfo> provinceInfos) {
        List<ProvinceInfo> hotProvinceInfos = new ArrayList<>();
        for (ProvinceInfo provinceInfo : provinceInfos) {
            String provinceName = provinceInfo.getProvince();
            if (provinceName.contains("北京") || provinceName.contains("上海")
                    || provinceName.contains("广东") || provinceName.contains("重庆")
                    || provinceName.contains("四川")) {
                hotProvinceInfos.add(provinceInfo);
            }
        }
        return hotProvinceInfos;
    }

    private void updateTabVisibility() {
        int provinceVisibility;
        int cityVisibility;
        int hospitalVisibility;
        int province;
        int city;
        int hospital;
        if (tabIndex == TAB_INDEX_PROVINCE) {
            actHospitalFilterByCityCityTv.setText("请选择");
            provinceVisibility = View.VISIBLE;
            cityVisibility = View.GONE;
            hospitalVisibility = View.GONE;
            province = 18;
            city = 16;
            hospital = 16;
        } else if (tabIndex == TAB_INDEX_CITY) {
            provinceVisibility = View.VISIBLE;
            cityVisibility = View.VISIBLE;
            hospitalVisibility = View.GONE;
            province = 16;
            city = 18;
            hospital = 16;
        } else if (tabIndex == TAB_INDEX_HOSPITAL) {
            provinceVisibility = View.VISIBLE;
            cityVisibility = View.VISIBLE;
            hospitalVisibility = View.VISIBLE;
            province = 16;
            city = 16;
            hospital = 18;
        } else {
            provinceVisibility = View.VISIBLE;
            cityVisibility = View.GONE;
            hospitalVisibility = View.GONE;
            province = 18;
            city = 16;
            hospital = 16;
        }
        actHospitalFilterByCityProvinceTv.setVisibility(provinceVisibility);
        actHospitalFilterByCityCityTv.setVisibility(cityVisibility);
        actHospitalFilterByCityHospitalTv.setVisibility(hospitalVisibility);
        actHospitalFilterByCityProvinceTv.setTextSize(province);
        actHospitalFilterByCityCityTv.setTextSize(city);
        actHospitalFilterByCityHospitalTv.setTextSize(hospital);
    }

    private void updateContentVisibility() {
        int province;
        int city;
        int hospital;
        if (tabIndex == TAB_INDEX_PROVINCE) {
            province = View.VISIBLE;
            city = View.GONE;
            hospital = View.GONE;
        } else if (tabIndex == TAB_INDEX_CITY) {
            province = View.GONE;
            city = View.VISIBLE;
            hospital = View.GONE;
        } else if (tabIndex == TAB_INDEX_HOSPITAL) {
            province = View.GONE;
            city = View.GONE;
            hospital = View.VISIBLE;
        } else {
            province = View.VISIBLE;
            city = View.GONE;
            hospital = View.GONE;
        }
        actHospitalFilterByCityProvinceNsv.setVisibility(province);
        actHospitalFilterByCityCityRv.setVisibility(city);
        actHospitalFilterByCityHospitalSrl.setVisibility(hospital);
    }

    private void updateIndicator() {
        actHospitalFilterByCityResultIndicatorV.post(() -> {
            switch (tabIndex) {
                case TAB_INDEX_PROVINCE: //省份
                    buildIndicatorAnimatorTowards(actHospitalFilterByCityProvinceTv).start();
                    break;
                case TAB_INDEX_CITY: //城市
                    buildIndicatorAnimatorTowards(actHospitalFilterByCityCityTv).start();
                    break;
                case TAB_INDEX_HOSPITAL: //乡镇
                    buildIndicatorAnimatorTowards(actHospitalFilterByCityHospitalTv).start();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * tab 来回切换的动画
     *
     * @param tab
     * @return
     */
    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(actHospitalFilterByCityResultIndicatorV, "X", actHospitalFilterByCityResultIndicatorV.getX(), tab.getX());
        final ViewGroup.LayoutParams params = actHospitalFilterByCityResultIndicatorV.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(animation -> {
            params.width = (int) animation.getAnimatedValue();
            actHospitalFilterByCityResultIndicatorV.setLayoutParams(params);
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);
        return set;
    }
}
