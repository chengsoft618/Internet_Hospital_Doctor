package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.ScheduingListInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.SearchScheduingInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitScreenInfo;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.utils.StringUtils;

/**
 * 会诊管理
 * Created by W·H·K on 2017/12/22.
 */
public class SchedulingActivity extends BaseActivity {
    private final int REQUEST_CODE_OPEN_SEARCH = 100;
    private final int REQUEST_CODE_SCREEN = 101;
    private final String FRAGMENT_TAG_ALL = "VisitAllFragment";
    private final String FRAGMENT_TAG_CARRY_ON = "VisitCarryOnFragment";
    private final String FRAGMENT_TAG_FINISH = "VisitFinishFragment";

    @FindViewById(R.id.activity_scheduing_manage_radio_group)
    private RadioGroup mRadioGroup;
    @FindViewById(R.id.activity_scheduing_manage_input_et)
    private TextView mInputEt;

    private SearchScheduingInfo searchScheduingInfo;

    public SearchScheduingInfo getSearchScheduingInfo() {
        return searchScheduingInfo;
    }

    public void setSearchScheduingInfo(SearchScheduingInfo searchScheduingInfo) {
        this.searchScheduingInfo = searchScheduingInfo;
    }

    private VisitScreenInfo mVisitScreenInfo;

    public VisitScreenInfo getmVisitScreenInfo() {
        return mVisitScreenInfo;
    }

    public void setmVisitScreenInfo(VisitScreenInfo mVisitScreenInfo) {
        this.mVisitScreenInfo = mVisitScreenInfo;
    }

    private ConsultantAllFragment mConsultantAllFragment;
    private ConsultantTriageFragment mConsultantTriageFragment;
    private ConsultantCarryOnFragment mConsultantCarryOnFragment;
    private ConsultantFinishFragment mConsultantFinishFragment;
    private VisitAllFragment mVisitAllFragment;
    private VisitCarryOnFragment mVisitCarryOnFragment;
    private VisitFinishFragment mVisitFinishFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        ViewInjecter.inject(this);
        initView();
    }

    private void initView() {
        mRadioGroup.check(R.id.activity_scheduing_manage_radio_group_carry_on);
        initFragmentCarryOn(R.id.activity_scheduing_manage_radio_group_carry_on);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (checkedId) {
                    case R.id.activity_scheduing_manage_radio_group_all:
                        initFragmentAll(checkedId);
                        break;
                    case R.id.activity_scheduing_manage_radio_group_triage:
                        initFragmentTriage(checkedId);
                        break;
                    case R.id.activity_scheduing_manage_radio_group_carry_on:
                        initFragmentCarryOn(checkedId);
                        break;
                    case R.id.activity_scheduing_manage_radio_group_finish:
                        initFragmentFinish(checkedId);
                        break;
                }
                transaction.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_OPEN_SEARCH:
                if (resultCode == RESULT_OK) {
                    SearchScheduingInfo visitScreenInfo = (SearchScheduingInfo) data.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_SCHEDULE_INFO);
                    Logger.logI(Logger.DOCTOR, "onActivityResulted-->scheduingList:" + searchScheduingInfo);
                    if (StringUtils.isEmpty(visitScreenInfo.getKeyWord())) {
                        visitScreenInfo = null;
                    }
                    setSearchScheduingInfo(visitScreenInfo);
                    onActivityResultSetView();
                }
                break;

            case REQUEST_CODE_SCREEN:
                if (resultCode == RESULT_OK && data != null) {
                    mVisitScreenInfo = (VisitScreenInfo) data.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCREEN);
                    Logger.logI(Logger.DOCTOR, "onActivityResult-->mVisitScreenInfo:" + mVisitScreenInfo);
                    setmVisitScreenInfo(mVisitScreenInfo);
                    onActivityResultSetView();
                }
                break;
        }
    }

    private void onActivityResultSetView() {
        mInputEt.setText(getSearchScheduingInfo() == null ? "" : getSearchScheduingInfo().getKeyWord());
        mRadioGroup.check(R.id.activity_scheduing_manage_radio_group_all);
        initFragmentAll(R.id.activity_scheduing_manage_radio_group_all);
    }

    @OnClick({R.id.activity_scheduing_manage_back,
            R.id.activity_scheduing_manage_input_et,
            R.id.activity_scheduing_screen})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.activity_scheduing_manage_back:
                goBack();
                break;
            case R.id.activity_scheduing_manage_input_et:
                intent.setClass(getActivity(), ConsultationSearchActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_SCHEDULE_INFO, getSearchScheduingInfo());
                startActivityForResult(intent, REQUEST_CODE_OPEN_SEARCH);
                break;
            case R.id.activity_scheduing_screen:
                VisitScreenActivity.startScreenActivityForResult(getActivity(), mVisitScreenInfo, REQUEST_CODE_SCREEN);
                break;
        }
    }

    private void initFragmentAll(int tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mConsultantAllFragment == null) {
            mConsultantAllFragment = new ConsultantAllFragment();
            transaction.add(R.id.main_fragment, mConsultantAllFragment, tag + "");
        }
        hideFragment(transaction);
        transaction.show(mConsultantAllFragment);
        transaction.commit();
        mCurrentFragment = mConsultantAllFragment;
        mConsultantAllFragment.setOnSwitchClickListener(ScheduingListInfo -> {
            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            hideFragment(transaction1);
            mVisitAllFragment = new VisitAllFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_LIST, ScheduingListInfo);
            mVisitAllFragment.setArguments(bundle);
            transaction1.add(R.id.main_fragment, mVisitAllFragment, FRAGMENT_TAG_ALL);
            transaction1.commit();
            mCurrentFragment = mVisitAllFragment;
        });
        mConsultantAllFragment.setOnTipsTvClickListener(view -> {
            mRadioGroup.check(R.id.activity_scheduing_manage_radio_group_triage);
            initFragmentTriage(R.id.activity_scheduing_manage_radio_group_triage);
        });
    }

    private void initFragmentTriage(int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mConsultantTriageFragment == null) {
            mConsultantTriageFragment = new ConsultantTriageFragment();
            transaction.add(R.id.main_fragment, mConsultantTriageFragment, checkedId + "");
        }
        hideFragment(transaction);
        transaction.show(mConsultantTriageFragment);
        transaction.commit();
        mCurrentFragment = mConsultantTriageFragment;
    }

    private void initFragmentCarryOn(int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mConsultantCarryOnFragment == null) {
            mConsultantCarryOnFragment = new ConsultantCarryOnFragment();
            transaction.add(R.id.main_fragment, mConsultantCarryOnFragment, checkedId + "");
        }
        hideFragment(transaction);
        transaction.show(mConsultantCarryOnFragment);
        transaction.commit();
        mCurrentFragment = mConsultantCarryOnFragment;
        mConsultantCarryOnFragment.setOnSwitchClickListener(ScheduingListInfo -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            hideFragment(transaction1);
            mVisitCarryOnFragment = new VisitCarryOnFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_LIST, ScheduingListInfo);
            mVisitCarryOnFragment.setArguments(bundle);
            transaction1.add(R.id.main_fragment, mVisitCarryOnFragment, FRAGMENT_TAG_CARRY_ON);
            transaction1.commit();
            mCurrentFragment = mVisitCarryOnFragment;
        });
    }

    private void initFragmentFinish(int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mConsultantFinishFragment == null) {
            mConsultantFinishFragment = new ConsultantFinishFragment();
            transaction.add(R.id.main_fragment, mConsultantFinishFragment, checkedId + "");
        }
        hideFragment(transaction);
        transaction.show(mConsultantFinishFragment);
        transaction.commit();
        mCurrentFragment = mConsultantFinishFragment;
        mConsultantFinishFragment.setOnSwitchClickListener(new ConsultantFinishFragment.OnSwitchClickListener() {
            @Override
            public void onTSwitchClicked(ScheduingListInfo ScheduingListInfo) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                hideFragment(transaction);
                mVisitFinishFragment = new VisitFinishFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_LIST, ScheduingListInfo);
                mVisitFinishFragment.setArguments(bundle);
                transaction.add(R.id.main_fragment, mVisitFinishFragment, FRAGMENT_TAG_FINISH);
                transaction.commit();
                mCurrentFragment = mVisitFinishFragment;
            }
        });
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (mConsultantAllFragment != null) {
            transaction.hide(mConsultantAllFragment);
        }
        if (mConsultantTriageFragment != null) {
            transaction.hide(mConsultantTriageFragment);
        }
        if (mConsultantCarryOnFragment != null) {
            transaction.hide(mConsultantCarryOnFragment);
        }
        if (mConsultantFinishFragment != null) {
            transaction.hide(mConsultantFinishFragment);
        }
        if (mVisitAllFragment != null) {
            transaction.hide(mVisitAllFragment);
        }
        if (mVisitCarryOnFragment != null) {
            transaction.hide(mVisitCarryOnFragment);
        }
        if (mVisitFinishFragment != null) {
            transaction.hide(mVisitFinishFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.logI(Logger.DOCTOR, "onActivityResult-->onKeyDown-->:mCurrentFragment:" + mCurrentFragment);
        goBack();
        return false;
    }

    private void goBack() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment.getTag().equals(FRAGMENT_TAG_ALL)) {
            initFragmentAll(R.id.activity_scheduing_manage_radio_group_all);
            transaction.remove(mVisitAllFragment);
        } else if (mCurrentFragment.getTag().equals(FRAGMENT_TAG_CARRY_ON)) {
            initFragmentCarryOn(R.id.activity_scheduing_manage_radio_group_carry_on);
            transaction.remove(mVisitCarryOnFragment);
        } else if (mCurrentFragment.getTag().equals(FRAGMENT_TAG_FINISH)) {
            initFragmentFinish(R.id.activity_scheduing_manage_radio_group_finish);
            transaction.remove(mVisitFinishFragment);
        } else {
            this.finish();
        }
        transaction.commit();
    }
}
