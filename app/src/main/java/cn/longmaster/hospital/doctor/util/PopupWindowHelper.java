package cn.longmaster.hospital.doctor.util;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorVisitFilterAdapter;
import cn.longmaster.hospital.doctor.view.picktime.WheelView;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ABiao_Abiao
 * @date 2019/6/21 14:30
 * @description:
 */
public class PopupWindowHelper {
    private @LayoutRes
    int resource;
    private View contentView;
    private Activity activity;
    private int width;
    private int height;

    public PopupWindow create() {
        contentView = LayoutInflater.from(activity).inflate(resource, null);
        return new PopupWindow(contentView, width, height, true);
    }

    public View getContentView() {
        return contentView;
    }

    //医院复旦排名
    public static final int FUDAN_TOP_3 = 3;
    public static final int FUDAN_TOP_5 = 5;
    public static final int FUDAN_TOP_10 = 10;

    @IntDef({FUDAN_TOP_3, FUDAN_TOP_5, FUDAN_TOP_10})
    public @interface FilterFudanSort {

    }

    //教学查房
    public static final int FILTER_BY_ROUNDS = 1;
    //批量摄影
    public static final int FILTER_BY_IMAGE = 2;
    //远程会诊
    public static final int FILTER_BY_CONSULTATION = 3;
    //远程门诊
    public static final int FILTER_BY_REMOTE = 4;
    //实地就诊
    public static final int FILTER_BY_LOCAL = 5;

    @IntDef({FILTER_BY_ROUNDS, FILTER_BY_IMAGE, FILTER_BY_CONSULTATION, FILTER_BY_REMOTE, FILTER_BY_LOCAL})
    public @interface FilterReceptionType {

    }

    /**
     * 用于医生筛选 筛选
     */
    public static class DoctorFilterBuilder {
        private boolean isIdentity;
        private int filterMinPrice;
        private int filterMaxPrice;
        @FilterReceptionType
        private int filterReceptionType;
        @FilterFudanSort
        private int filterFudanSort;
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private int height;
        private OnFilterChangeListener onFilterChangeListener;
        private View.OnClickListener onHospitalChooseListener;
        private View.OnClickListener onDepartmentChooseListener;
        private View.OnClickListener onTimeChooseListener;
        private PopupWindow.OnDismissListener dismissListener;
        private List<String> mHospitals = new ArrayList<>();
        private List<String> mDepartments = new ArrayList<>();
        private List<String> mReceptionTime = new ArrayList<>();
        private FlexboxLayout popWinDoctorListFilterByHospitalFbl;
        private TextView popWinDoctorListClearHospitalTv;
        private FlexboxLayout popWinDoctorListFilterByDepartmentFbl;
        private TextView popWinDoctorListClearDepartmentTv;
        private FlexboxLayout popWinDoctorListFilterByReceptionTimeFbl;
        private TextView popWinDoctorListClearReceptionTimeTv;
        private LayoutInflater mLayoutInflater;
        private final String KEY_NO_FILTER = "不限";

        public DoctorFilterBuilder setIdentity(boolean identity) {
            isIdentity = identity;
            return this;
        }

        public DoctorFilterBuilder setOnDismissListener(PopupWindow.OnDismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public DoctorFilterBuilder setFilterMinPrice(int filterMinPrice) {
            this.filterMinPrice = filterMinPrice;
            return this;
        }

        public DoctorFilterBuilder setFilterMaxPrice(@FilterReceptionType int filterMaxPrice) {
            this.filterMaxPrice = filterMaxPrice;
            return this;
        }

        public DoctorFilterBuilder setFilterReceptionTime(List<String> receptionTime) {
            this.mReceptionTime.addAll(receptionTime);
            return this;
        }

        public DoctorFilterBuilder setFilterHospitals(List<String> hospitals) {
            this.mHospitals.addAll(hospitals);
            return this;
        }

        public DoctorFilterBuilder setFilterDepartments(List<String> departments) {
            this.mDepartments.addAll(departments);
            return this;
        }

        public DoctorFilterBuilder setFilterReceptionType(int filterReceptionType) {
            this.filterReceptionType = filterReceptionType;
            return this;
        }

        public DoctorFilterBuilder setFilterFudanSort(@FilterFudanSort int filterFudanSort) {
            this.filterFudanSort = filterFudanSort;
            return this;
        }

        public DoctorFilterBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public DoctorFilterBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public DoctorFilterBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public DoctorFilterBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public DoctorFilterBuilder setOnFilterChangeListener(OnFilterChangeListener onFilterChangeListener) {
            this.onFilterChangeListener = onFilterChangeListener;
            return this;
        }

        public DoctorFilterBuilder setOnHospitalChooseListener(View.OnClickListener listener) {
            this.onHospitalChooseListener = listener;
            return this;
        }

        public DoctorFilterBuilder setOnDepartmentChooseListener(View.OnClickListener listener) {
            this.onDepartmentChooseListener = listener;
            return this;
        }

        public DoctorFilterBuilder setOnTimeChooseListener(View.OnClickListener listener) {
            this.onTimeChooseListener = listener;
            return this;
        }

        public void addHospital(String hospitalName) {
            if (mHospitals.contains(hospitalName)) {
                ToastUtils.showShort("医院重复");
                return;
            }
            mHospitals.add(hospitalName);
            displayHospital(mHospitals);
        }

        private void displayHospital(List<String> hospitals) {
            if (null != popWinDoctorListFilterByHospitalFbl) {
                popWinDoctorListFilterByHospitalFbl.removeAllViews();
                for (String hospital : hospitals) {
                    View view = mLayoutInflater.inflate(R.layout.item_fbl_doctor_list_filter_hospital, null);
                    TextView textView = view.findViewById(R.id.item_doctor_list_filter_hospital_tv);
                    ImageView ivDel = view.findViewById(R.id.item_doctor_list_filter_hospital_del_iv);
                    if (StringUtils.equals(KEY_NO_FILTER, hospital)) {
                        popWinDoctorListClearHospitalTv = textView;
                        initNoFilter(textView, LibCollections.size(hospitals) > 1);
                        ivDel.setVisibility(View.GONE);
                        textView.setOnClickListener(v -> {
                            for (int i1 = LibCollections.size(hospitals) - 1; i1 > 0; i1--) {
                                hospitals.remove(i1);
                                popWinDoctorListFilterByHospitalFbl.removeViewAt(i1);
                            }
                            initNoFilter(textView, LibCollections.size(hospitals) > 1);
                        });
                    } else {
                        ivDel.setVisibility(View.VISIBLE);
                    }
                    ivDel.setOnClickListener(v -> {
                        popWinDoctorListFilterByHospitalFbl.removeView(view);
                        hospitals.remove(hospital);
                        initNoFilter(popWinDoctorListClearHospitalTv, LibCollections.size(hospitals) > 1);
                    });
                    textView.setText(hospital);
                    popWinDoctorListFilterByHospitalFbl.addView(view);
                }
            }
        }

        public void addDepartment(String departmentName) {
            if (mDepartments.contains(departmentName)) {
                ToastUtils.showShort("科室重复");
                return;
            }
            mDepartments.add(departmentName);
            displayDepartments(mDepartments);
        }

        private void displayDepartments(List<String> departments) {
            if (null != popWinDoctorListFilterByDepartmentFbl) {
                popWinDoctorListFilterByDepartmentFbl.removeAllViews();
                for (int i = 0; i < LibCollections.size(departments); i++) {
                    String department = departments.get(i);
                    View view = mLayoutInflater.inflate(R.layout.item_fbl_doctor_list_filter_department, null);
                    TextView textView = view.findViewById(R.id.item_doctor_list_filter_department_tv);
                    ImageView ivDel = view.findViewById(R.id.item_doctor_list_filter_department_del_iv);
                    if (StringUtils.equals(KEY_NO_FILTER, department)) {
                        popWinDoctorListClearDepartmentTv = textView;
                        initNoFilter(textView, LibCollections.size(departments) > 1);
                        ivDel.setVisibility(View.GONE);
                        textView.setOnClickListener(v -> {
                            for (int i1 = LibCollections.size(departments) - 1; i1 > 0; i1--) {
                                departments.remove(i1);
                                popWinDoctorListFilterByDepartmentFbl.removeViewAt(i1);
                            }
                            initNoFilter(textView, LibCollections.size(departments) > 1);
                        });
                    } else {
                        ivDel.setVisibility(View.VISIBLE);
                    }
                    ivDel.setOnClickListener(v -> {
                        popWinDoctorListFilterByDepartmentFbl.removeView(view);
                        departments.remove(department);
                        initNoFilter(popWinDoctorListClearDepartmentTv, LibCollections.size(departments) > 1);
                    });
                    textView.setText(department);
                    popWinDoctorListFilterByDepartmentFbl.addView(view, i);
                }
            }
        }

        public void addReceptionTime(String time) {
            if (mReceptionTime.contains(time)) {
                ToastUtils.showShort("选择时间重复");
                return;
            }
            mReceptionTime.add(time);
            displayReceptionTime(mReceptionTime);
        }

        private void displayReceptionTime(List<String> receptionTime) {
            if (null != popWinDoctorListFilterByReceptionTimeFbl) {
                popWinDoctorListFilterByReceptionTimeFbl.removeAllViews();
                for (String tempTime : receptionTime) {
                    View view = mLayoutInflater.inflate(R.layout.item_fbl_doctor_list_filter_time, null);
                    TextView textView = view.findViewById(R.id.item_doctor_list_filter_time_tv);
                    ImageView ivDel = view.findViewById(R.id.item_doctor_list_filter_time_del_iv);
                    if (StringUtils.equals(KEY_NO_FILTER, tempTime)) {
                        popWinDoctorListClearReceptionTimeTv = textView;
                        initNoFilter(textView, LibCollections.size(receptionTime) > 1);
                        ivDel.setVisibility(View.GONE);
                        textView.setOnClickListener(v -> {
                            for (int i1 = LibCollections.size(receptionTime) - 1; i1 > 0; i1--) {
                                receptionTime.remove(i1);
                                popWinDoctorListFilterByReceptionTimeFbl.removeViewAt(i1);
                            }
                            initNoFilter(textView, LibCollections.size(receptionTime) > 1);
                        });
                    } else {
                        ivDel.setVisibility(View.VISIBLE);
                    }
                    ivDel.setOnClickListener(v -> {
                        popWinDoctorListFilterByReceptionTimeFbl.removeView(view);
                        receptionTime.remove(tempTime);
                        initNoFilter(popWinDoctorListClearReceptionTimeTv, LibCollections.size(receptionTime) > 1);
                    });
                    textView.setText(tempTime);
                    popWinDoctorListFilterByReceptionTimeFbl.addView(view);
                }
            }
        }

        private void initNoFilter(TextView textView, boolean hasData) {
            if (null == textView) {
                return;
            }
            if (hasData) {
                textView.setBackgroundResource(R.drawable.bg_solid_faf9f9_radius_8);
                textView.setTextColor(ContextCompat.getColor(activity, R.color.color_121415));
            } else {
                textView.setBackgroundResource(R.drawable.bg_solid_edf1fd_radius_8);
                textView.setTextColor(ContextCompat.getColor(activity, R.color.color_049eff));
            }
        }

        public PopupWindow build() {
            if (!mHospitals.contains(KEY_NO_FILTER)) {
                mHospitals.add(0, KEY_NO_FILTER);
            }
            if (!mDepartments.contains(KEY_NO_FILTER)) {
                mDepartments.add(0, KEY_NO_FILTER);
            }
            if (!mReceptionTime.contains(KEY_NO_FILTER)) {
                mReceptionTime.add(0, KEY_NO_FILTER);
            }
            mLayoutInflater = LayoutInflater.from(activity);
            View contentView = mLayoutInflater.inflate(R.layout.pop_win_doctor_list_filter, null);
            final PopupWindow mPopWindow = new PopupWindow(contentView);
            mPopWindow.setFocusable(true);
            mPopWindow.setOutsideTouchable(true);
            mPopWindow.update();
            mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            if (0 != height) {
                mPopWindow.setHeight(height);
            } else {
                mPopWindow.setHeight(parent.getHeight());
            }
            mPopWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            final int[] mFilterType = new int[1];
            final int[] mFudanSort = new int[1];
            mPopWindow.setOnDismissListener(dismissListener);
            LinearLayout popWinDoctorListHospitalLl = contentView.findViewById(R.id.pop_win_doctor_list_hospital_ll);
            LinearLayout popWinDoctorListChooseHospitalLl = contentView.findViewById(R.id.pop_win_doctor_list_choose_hospital_ll);
            popWinDoctorListFilterByHospitalFbl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_hospital_fbl);
            LinearLayout popWinDoctorListDepartmentLl = contentView.findViewById(R.id.pop_win_doctor_list_department_ll);
            LinearLayout popWinDoctorListChooseDepartmentLl = contentView.findViewById(R.id.pop_win_doctor_list_choose_department_ll);
            popWinDoctorListFilterByDepartmentFbl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_department_fbl);
            LinearLayout popWinDoctorListReceptionTimeLl = contentView.findViewById(R.id.pop_win_doctor_list_reception_time_ll);
            LinearLayout popWinDoctorListChooseReceptionTimeLl = contentView.findViewById(R.id.pop_win_doctor_list_choose_reception_time_ll);
            popWinDoctorListFilterByReceptionTimeFbl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_reception_time_fbl);
            LinearLayout popWinDoctorListFilterByTypeLl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_type_ll);
            TextView popWinDoctorListFilterByTypeTv = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_type_tv);
            FlexboxLayout popWinDoctorListFilterByTypeFbl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_type_fbl);
            CheckBox popWinDoctorListFilterNoCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_no_cb);
            CheckBox popWinDoctorListFilterRoundsCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_rounds_cb);
            CheckBox popWinDoctorListFilterImageCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_image_cb);
            CheckBox popWinDoctorListFilterConsultationCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_consultation_cb);
            CheckBox popWinDoctorListFilterRemoteCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_remote_cb);
            CheckBox popWinDoctorListFilterLocalCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_local_cb);
            LinearLayout popWinDoctorListFilterByPriceRl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_rl);
            TextView popWinDoctorListFilterByPriceTv = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_tv);
            TextView popWinDoctorListFilterByPriceIntroTv = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_intro_tv);
            LinearLayout popWinDoctorListFilterByPriceLl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_ll);
            EditText popWinDoctorListFilterByPriceMinEt = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_min_et);
            EditText popWinDoctorListFilterByPriceMaxEt = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_price_max_et);
            LinearLayout popWinDoctorListFilterByOrderLl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_order_ll);
            TextView popWinDoctorListFilterByOrderTv = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_order_tv);
            FlexboxLayout popWinDoctorListFilterByOrderFbl = contentView.findViewById(R.id.pop_win_doctor_list_filter_by_order_fbl);
            CheckBox popWinDoctorListFilterTop3Cb = contentView.findViewById(R.id.pop_win_doctor_list_filter_top_3_cb);
            CheckBox popWinDoctorListFilterTop5Cb = contentView.findViewById(R.id.pop_win_doctor_list_filter_top_5_cb);
            CheckBox popWinDoctorListFilterTop10Cb = contentView.findViewById(R.id.pop_win_doctor_list_filter_top_10_cb);
            CheckBox popWinDoctorListFilterTopNoCb = contentView.findViewById(R.id.pop_win_doctor_list_filter_top_no_cb);
            LinearLayout dialogDoctorListFilterOperationLl = contentView.findViewById(R.id.dialog_doctor_list_filter_operation_ll);
            TextView dialogDoctorListFilterCancel = contentView.findViewById(R.id.dialog_doctor_list_filter_cancel);
            TextView dialogDoctorListFilterCommit = contentView.findViewById(R.id.dialog_doctor_list_filter_commit);

            popWinDoctorListChooseHospitalLl.setOnClickListener(onHospitalChooseListener);
            popWinDoctorListChooseDepartmentLl.setOnClickListener(onDepartmentChooseListener);
            popWinDoctorListChooseReceptionTimeLl.setOnClickListener(onTimeChooseListener);
            if (LibCollections.isNotEmpty(mHospitals)) {
                displayHospital(mHospitals);
            }
            if (LibCollections.isNotEmpty(mDepartments)) {
                displayDepartments(mDepartments);
            }
            if (LibCollections.isNotEmpty(mReceptionTime)) {
                displayReceptionTime(mReceptionTime);
            }
            if (isIdentity) {
                popWinDoctorListFilterByPriceRl.setVisibility(View.VISIBLE);
            } else {
                popWinDoctorListFilterByPriceRl.setVisibility(View.GONE);
            }
            if (filterMinPrice != 0) {
                popWinDoctorListFilterByPriceMinEt.setText(filterMinPrice + "");
            }
            if (filterMaxPrice != 0) {
                popWinDoctorListFilterByPriceMaxEt.setText(filterMaxPrice + "");
            }
            popWinDoctorListFilterRoundsCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterImageCb.setChecked(false);
                    popWinDoctorListFilterConsultationCb.setChecked(false);
                    popWinDoctorListFilterRemoteCb.setChecked(false);
                    popWinDoctorListFilterLocalCb.setChecked(false);
                    popWinDoctorListFilterNoCb.setChecked(false);
                    popWinDoctorListFilterByPriceMinEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMinEt.requestFocus();
                    mFilterType[0] = FILTER_BY_ROUNDS;
                } else {
                    mFilterType[0] = 0;
                    popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMinEt.setText(null);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMaxEt.setText(null);
                }
            });
            popWinDoctorListFilterImageCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterRoundsCb.setChecked(false);
                    popWinDoctorListFilterConsultationCb.setChecked(false);
                    popWinDoctorListFilterRemoteCb.setChecked(false);
                    popWinDoctorListFilterLocalCb.setChecked(false);
                    popWinDoctorListFilterNoCb.setChecked(false);
                    mFilterType[0] = FILTER_BY_IMAGE;
                } else {
                    mFilterType[0] = 0;
                }
                popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                popWinDoctorListFilterByPriceMinEt.setText(null);
                popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                popWinDoctorListFilterByPriceMaxEt.setText(null);
            });
            popWinDoctorListFilterConsultationCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterRoundsCb.setChecked(false);
                    popWinDoctorListFilterImageCb.setChecked(false);
                    popWinDoctorListFilterRemoteCb.setChecked(false);
                    popWinDoctorListFilterLocalCb.setChecked(false);
                    popWinDoctorListFilterNoCb.setChecked(false);
                    popWinDoctorListFilterByPriceMinEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMinEt.requestFocus();
                    mFilterType[0] = FILTER_BY_CONSULTATION;
                } else {
                    mFilterType[0] = 0;
                    popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMinEt.setText(null);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMaxEt.setText(null);
                }
            });
            popWinDoctorListFilterRemoteCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterRoundsCb.setChecked(false);
                    popWinDoctorListFilterImageCb.setChecked(false);
                    popWinDoctorListFilterConsultationCb.setChecked(false);
                    popWinDoctorListFilterLocalCb.setChecked(false);
                    popWinDoctorListFilterNoCb.setChecked(false);
                    mFilterType[0] = FILTER_BY_REMOTE;
                } else {
                    mFilterType[0] = 0;
                }
                popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                popWinDoctorListFilterByPriceMinEt.setText(null);
                popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                popWinDoctorListFilterByPriceMaxEt.setText(null);
            });
            popWinDoctorListFilterLocalCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterRoundsCb.setChecked(false);
                    popWinDoctorListFilterImageCb.setChecked(false);
                    popWinDoctorListFilterConsultationCb.setChecked(false);
                    popWinDoctorListFilterRemoteCb.setChecked(false);
                    popWinDoctorListFilterNoCb.setChecked(false);
                    popWinDoctorListFilterByPriceMinEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(true);
                    popWinDoctorListFilterByPriceMinEt.requestFocus();
                    mFilterType[0] = FILTER_BY_LOCAL;
                } else {
                    mFilterType[0] = 0;
                    popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMinEt.setText(null);
                    popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                    popWinDoctorListFilterByPriceMaxEt.setText(null);
                }
            });
            popWinDoctorListFilterNoCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterRoundsCb.setChecked(false);
                    popWinDoctorListFilterImageCb.setChecked(false);
                    popWinDoctorListFilterConsultationCb.setChecked(false);
                    popWinDoctorListFilterRemoteCb.setChecked(false);
                    popWinDoctorListFilterLocalCb.setChecked(false);
                    mFilterType[0] = 0;
                }
                popWinDoctorListFilterByPriceMinEt.setEnabled(false);
                popWinDoctorListFilterByPriceMinEt.setText(null);
                popWinDoctorListFilterByPriceMaxEt.setEnabled(false);
                popWinDoctorListFilterByPriceMaxEt.setText(null);
            });

            switch (filterReceptionType) {
                case FILTER_BY_ROUNDS:
                    popWinDoctorListFilterRoundsCb.setChecked(true);
                    break;
                case FILTER_BY_IMAGE:
                    popWinDoctorListFilterImageCb.setChecked(true);
                    break;
                case FILTER_BY_CONSULTATION:
                    popWinDoctorListFilterConsultationCb.setChecked(true);
                    break;
                case FILTER_BY_REMOTE:
                    popWinDoctorListFilterRemoteCb.setChecked(true);
                    break;
                case FILTER_BY_LOCAL:
                    popWinDoctorListFilterLocalCb.setChecked(true);
                    break;
                default:
                    break;
            }
            switch (filterFudanSort) {
                case FUDAN_TOP_3:
                    popWinDoctorListFilterTop3Cb.setChecked(true);
                    break;
                case FUDAN_TOP_5:
                    popWinDoctorListFilterTop5Cb.setChecked(true);
                    break;
                case FUDAN_TOP_10:
                    popWinDoctorListFilterTop10Cb.setChecked(true);
                    break;
                default:
                    break;
            }
            popWinDoctorListFilterTop3Cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterTop5Cb.setChecked(false);
                    popWinDoctorListFilterTop10Cb.setChecked(false);
                    popWinDoctorListFilterTopNoCb.setChecked(false);
                    mFudanSort[0] = FUDAN_TOP_3;
                } else {
                    mFudanSort[0] = 0;
                }
            });
            popWinDoctorListFilterTop5Cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterTop3Cb.setChecked(false);
                    popWinDoctorListFilterTop10Cb.setChecked(false);
                    popWinDoctorListFilterTopNoCb.setChecked(false);
                    mFudanSort[0] = FUDAN_TOP_5;
                } else {
                    mFudanSort[0] = 0;
                }
            });
            popWinDoctorListFilterTop10Cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterTop5Cb.setChecked(false);
                    popWinDoctorListFilterTop3Cb.setChecked(false);
                    popWinDoctorListFilterTopNoCb.setChecked(false);
                    mFudanSort[0] = FUDAN_TOP_10;
                } else {
                    mFudanSort[0] = 0;
                }
            });
            popWinDoctorListFilterTopNoCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    popWinDoctorListFilterTop5Cb.setChecked(false);
                    popWinDoctorListFilterTop10Cb.setChecked(false);
                    popWinDoctorListFilterTop3Cb.setChecked(false);
                }
                mFudanSort[0] = 0;
            });
            dialogDoctorListFilterCancel.setOnClickListener(v -> {
                popWinDoctorListFilterRoundsCb.setChecked(false);
                popWinDoctorListFilterImageCb.setChecked(false);
                popWinDoctorListFilterConsultationCb.setChecked(false);
                popWinDoctorListFilterRemoteCb.setChecked(false);
                popWinDoctorListFilterLocalCb.setChecked(false);
                popWinDoctorListFilterNoCb.setChecked(false);
                popWinDoctorListFilterTop3Cb.setChecked(false);
                popWinDoctorListFilterTop5Cb.setChecked(false);
                popWinDoctorListFilterTop10Cb.setChecked(false);
                popWinDoctorListFilterTopNoCb.setChecked(false);
                clearFilter(mHospitals);
                clearFilter(mDepartments);
                clearFilter(mReceptionTime);
                displayHospital(mHospitals);
                displayDepartments(mDepartments);
                displayReceptionTime(mReceptionTime);
            });
            dialogDoctorListFilterCommit.setOnClickListener(v -> {
                filterMaxPrice = StringUtils.str2Integer(popWinDoctorListFilterByPriceMaxEt.getText().toString().trim());
                filterMinPrice = StringUtils.str2Integer(popWinDoctorListFilterByPriceMinEt.getText().toString().trim());
                if (filterMinPrice != 0 && filterMaxPrice != 0) {
                    if (filterMaxPrice < filterMinPrice) {
                        ToastUtils.showShort("最高分成应大于最低分成");
                        return;
                    }
                }
                filterReceptionType = mFilterType[0];
                filterFudanSort = mFudanSort[0];
                List<String> tempHospital = Lists.newArrayList(mHospitals);
                tempHospital.remove(KEY_NO_FILTER);
                List<String> tempDepartments = Lists.newArrayList(mDepartments);
                tempDepartments.remove(KEY_NO_FILTER);
                List<String> tempReceptionTime = Lists.newArrayList(mReceptionTime);
                tempReceptionTime.remove(KEY_NO_FILTER);
                onFilterChangeListener.onConfirm(filterMinPrice, filterMaxPrice, filterReceptionType, filterFudanSort, tempHospital, tempDepartments, tempReceptionTime);
                mPopWindow.dismiss();
            });
            return mPopWindow;
        }

        private void clearFilter(List<String> filter) {
            filter.clear();
            filter.add(KEY_NO_FILTER);
        }
    }

    /**
     * 用于医生筛选 接诊时间
     */
    public static class DoctorTimeFilterBuilder {
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private int height;
        private List<String> receptionDateTime;
        private OnTimeFilterChangeListener onTimeFilterChangeListener;

        public DoctorTimeFilterBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public DoctorTimeFilterBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public DoctorTimeFilterBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public DoctorTimeFilterBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public DoctorTimeFilterBuilder setOnTimeFilterChangeListener(OnTimeFilterChangeListener onTimeFilterChangeListener) {
            this.onTimeFilterChangeListener = onTimeFilterChangeListener;
            return this;
        }

        public DoctorTimeFilterBuilder setReceptionDateTime(List<String> receptionDateTime) {
            this.receptionDateTime = receptionDateTime;
            return this;
        }

        public PopupWindow build() {
            checkNotNull(parent, "parent can not be null");
            View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_win_doctor_list_filter_time, null);
            final PopupWindow mPopWindow = new PopupWindow(contentView);
            mPopWindow.setFocusable(true);
            mPopWindow.setOutsideTouchable(true);
            mPopWindow.update();
            mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            if (0 != height) {
                mPopWindow.setHeight(height);
            } else {
                mPopWindow.setHeight(parent.getHeight());
            }
            List<String> receptionDateTime = Lists.newArrayList(this.receptionDateTime);
            final DoctorVisitFilterAdapter doctorVisitFilterAdapter = new DoctorVisitFilterAdapter(R.layout.item_doctor_list_filter_time, receptionDateTime);
            AtomicBoolean noFilter = new AtomicBoolean(false);
            CheckBox cbSelectAll = contentView.findViewById(R.id.dialog_doctor_list_filter_time_all_cb);
            ImageView ivClose = contentView.findViewById(R.id.dialog_doctor_list_filter_time_close_iv);
            TextView tvSelectTime = contentView.findViewById(R.id.dialog_doctor_list_filter_time_select_tv);
            TextView tvCancel = contentView.findViewById(R.id.dialog_doctor_list_filter_cancel);
            TextView tvCommit = contentView.findViewById(R.id.dialog_doctor_list_filter_commit);
            RecyclerView rvSelectedTime = contentView.findViewById(R.id.dialog_doctor_list_filter_time_all_rv);
            cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
                noFilter.set(isChecked);
                if (isChecked) {
                    doctorVisitFilterAdapter.setNewData(null);
                }
            });
            ivClose.setOnClickListener(v -> {
                onTimeFilterChangeListener.onCancel();
                mPopWindow.dismiss();
            });
            tvCancel.setOnClickListener(v -> {
                onTimeFilterChangeListener.onCancel();
                mPopWindow.dismiss();
            });
            tvCommit.setOnClickListener(v -> {
                onTimeFilterChangeListener.onCommit(doctorVisitFilterAdapter.getData());
                mPopWindow.dismiss();
            });

            tvSelectTime.setOnClickListener(v -> {
                if (cbSelectAll.isChecked()) {
                    ToastUtils.showShort("您已选择不限");
                } else {
                    new DateSelectBuilder()
                            .setActivity(activity)
                            .setDayItems(Arrays.asList(activity.getResources().getStringArray(R.array.week_no_weekend)))
                            .setTimeItems(Arrays.asList(activity.getResources().getStringArray(R.array.all_day_desc)))
                            .setOnDateSelectListener(new OnDateSelectListener() {
                                @Override
                                public void onCancel() {

                                }

                                @Override
                                public void onCommit(String selectDate, String selectTime) {
                                    List<String> selectTimeList = doctorVisitFilterAdapter.getData();
                                    if (selectTimeList.contains(selectDate + " " + selectTime)) {
                                        ToastUtils.showShort("选择时间重复");
                                    } else {
                                        selectTimeList.add(selectDate + " " + selectTime);
                                    }
                                    doctorVisitFilterAdapter.setNewData(selectTimeList);
                                }
                            })
                            .build()
                            .showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            });
            rvSelectedTime.setLayoutManager(new GridLayoutManager(activity, 3));
            rvSelectedTime.setAdapter(doctorVisitFilterAdapter);
            doctorVisitFilterAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                adapter.remove(position);
            });
            return mPopWindow;
        }
    }

    public static class DateSelectBuilder {
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private int height;
        private List<String> receptionDateTime;
        private OnDateSelectListener onDateSelectListener;
        private List<String> dayItems;
        private List<String> timeItems;

        public DateSelectBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public DateSelectBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public DateSelectBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public DateSelectBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public DateSelectBuilder setReceptionDateTime(List<String> receptionDateTime) {
            this.receptionDateTime = receptionDateTime;
            return this;
        }

        public DateSelectBuilder setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
            this.onDateSelectListener = onDateSelectListener;
            return this;
        }

        public DateSelectBuilder setDayItems(List<String> dayItems) {
            this.dayItems = dayItems;
            return this;
        }

        public DateSelectBuilder setTimeItems(List<String> timeItems) {
            this.timeItems = timeItems;
            return this;
        }

        public PopupWindow build() {
            View contentView = LayoutInflater.from(activity).inflate(R.layout.choice_date_pop_wind_two, null);
            final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            final TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
            final TextView commit = (TextView) contentView.findViewById(R.id.tv_commit);
            final TextView title = (TextView) contentView.findViewById(R.id.tv_choice_title);
            final WheelView mWvDay = (WheelView) contentView.findViewById(R.id.wv_day);
            final WheelView mWvTime = (WheelView) contentView.findViewById(R.id.wv_time);
            mWvDay.setItems(dayItems, 0);
            mWvTime.setItems(timeItems, 0);
            cancel.setOnClickListener(v -> {
                onDateSelectListener.onCancel();
                mPopupWindow.dismiss();
            });
            commit.setOnClickListener(v -> {
                onDateSelectListener.onCommit(mWvDay.getSelectedItem(), mWvTime.getSelectedItem());
                mPopupWindow.dismiss();
            });
            mWvDay.setOnItemSelectedListener((selectedIndex, item) -> title.setText(mWvDay.getSelectedItem() + " " + mWvTime.getSelectedItem()));
            mWvTime.setOnItemSelectedListener((selectedIndex, item) -> title.setText(mWvDay.getSelectedItem() + " " + mWvTime.getSelectedItem()));
            return mPopupWindow;
        }
    }

    public interface OnFilterChangeListener {
        /**
         * @param filterMinPrice      最低价格
         * @param filterMaxPrice      最高价格
         * @param filterReceptionType 接诊类型
         * @param filterFudanSort     医院复旦排名
         * @param hospitals           医院
         * @param departments         科室
         * @param receptionTime       接诊时间
         */
        void onConfirm(int filterMinPrice, int filterMaxPrice, int filterReceptionType, int filterFudanSort, List<String> hospitals, List<String> departments, List<String> receptionTime);
    }

    public interface OnTimeFilterChangeListener {
        void onCancel();

        /**
         * @param receptionDateTime 已选择时间
         */
        void onCommit(List<String> receptionDateTime);
    }

    public interface OnDateSelectListener {
        void onCancel();

        /**
         * @param selectDate 选择日期
         * @param selectTime 选择时间
         */
        void onCommit(String selectDate, String selectTime);
    }
}
