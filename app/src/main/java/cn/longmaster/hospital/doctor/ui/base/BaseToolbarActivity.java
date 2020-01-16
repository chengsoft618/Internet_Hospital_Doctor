package cn.longmaster.hospital.doctor.ui.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/17 13:42
 * @description:
 */
public abstract class BaseToolbarActivity extends NewBaseActivity {
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

    @Override
    protected void initViews() {
        if (hasTitle()) {
            if (hiddenTitle()) {
                toolBarBase.setVisibility(View.GONE);
                return;
            }
            String title = getToolBarTitle();
            tvToolBarTitle.setText(title);
            if (StringUtils.length(title) > 10) {
                tvToolBarTitle.postDelayed(() -> tvToolBarTitle.setSelected(true), 1900);
            }
            if (hasBackIcon()) {
                ivToolBarBack.setVisibility(View.VISIBLE);
                ivToolBarBack.setOnClickListener(setToolBarBackListener());
            } else {
                ivToolBarBack.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(setToolbarSubTitle())) {
                tvToolBarSub.setVisibility(View.GONE);
            } else {
                tvToolBarSub.setVisibility(View.VISIBLE);
                tvToolBarSub.setText(setToolbarSubTitle());
                tvToolBarSub.setOnClickListener(setToolBarSubOnClickListener());
            }
            if (0 == setToolbarSubIcon()) {
                ivToolBarSub.setVisibility(View.GONE);
            } else {
                ivToolBarSub.setVisibility(View.VISIBLE);
                ivToolBarSub.setImageResource(setToolbarSubIcon());
                ivToolBarSub.setOnClickListener(setToolBarSubIconOnClickListener());
            }
        }
    }

    /**
     * @return void
     * @description: 设置ToolbarTitle
     * @author Abiao
     * @date 2018/12/4 16:01
     */
    protected void setToolbarTitle(String title) {
        if (hasTitle()) {
            tvToolBarTitle.setText(title);
        } else {
            throw new RuntimeException("You must do this after initViews");
        }
    }

    protected void setToolbarTitle(@StringRes int title) {
        if (hasTitle()) {
            tvToolBarTitle.setText(title);
        } else {
            throw new RuntimeException("You must do this after initViews");
        }
    }

    protected String getToolBarTitle() {
        return "标题";
    }

    /**
     * @return void
     * @description: 设置ToolbarSubTitle
     * @author Abiao
     * @date 2018/12/4 16:01
     */
    protected void setToolbarSubTitle(String subTitle) {
        if (hasTitle()) {
            tvToolBarSub.setText(subTitle);
        } else {
            throw new RuntimeException("you must do this after initViews");
        }
    }

    protected void setToolbarSubTitle(@StringRes int subTitle) {
        if (hasTitle()) {
            tvToolBarSub.setText(subTitle);
        } else {
            throw new RuntimeException("you must do this after initViews");
        }
    }

    protected String setToolbarSubTitle() {
        return null;
    }

    protected void setToolbarSubIcon(@DrawableRes int sunIcon) {
        if (hasTitle()) {
            ivToolBarSub.setImageResource(sunIcon);
        } else {
            throw new RuntimeException("you must do this after initViews");
        }
    }

    protected int setToolbarSubIcon() {
        return 0;
    }

    /**
     * @param
     * @return android.view.View.OnClickListener
     * @description: 设置副标题图片点击事件
     * @author Abiao
     * @date 2018/12/4 15:59
     */
    protected View.OnClickListener setToolBarSubIconOnClickListener() {
        return null;
    }

    /**
     * @param
     * @return android.view.View.OnClickListener
     * @description: 设置副标题点击事件
     * @author Abiao
     * @date 2018/12/4 15:59
     */
    protected View.OnClickListener setToolBarSubOnClickListener() {
        return null;
    }

    /**
     * @param
     * @return boolean
     * @description: 是否有返回键
     * @author Abiao
     * @date 2018/12/4 16:00
     */
    protected boolean hasBackIcon() {
        return false;
    }

    /**
     * @param
     * @return android.view.View.OnClickListener
     * @description: 设置返回键点击事件
     * @author Abiao
     * @date 2018/12/4 16:00
     */
    public View.OnClickListener setToolBarBackListener() {
        return v -> onBackPressed();
    }

    /**
     * @param
     * @return boolean
     * @description: 是否有标题
     * @author Abiao
     * @date 2019/1/28 14:19
     */
    public boolean hasTitle() {
        return null != toolBarBase;
    }

    /**
     * @param
     * @return boolean
     * @description: 是否隐藏标题
     * @author Abiao
     * @date 2019/1/28 14:19
     */
    protected boolean hiddenTitle() {
        return false;
    }

}
