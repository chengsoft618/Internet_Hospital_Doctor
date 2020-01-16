package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;

/**
 * 分享页面
 * Created by Yang² on 2017/9/4.
 */

public class ShareDialog extends Dialog {
    @FindViewById(R.id.share_dialog_share_gv)
    private GridView mShareGv;

    private ShareDialogAdapter mShareDialogAdapter;
    private OnShareClickListener onShareClickListener;
    private List<ShareItem> mShareItemList = new ArrayList<>();

    public ShareDialog(Context context, List<ShareItem> shareItemList) {
        this(context, R.style.customAnimation_noActionbar_window_style);
        mShareItemList = shareItemList;
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View containerView = getLayoutInflater().inflate(R.layout.layout_share_dialog, null);
        containerView.setMinimumWidth(10000);
        setContentView(containerView);
        ViewInjecter.inject(this);
        initView();
    }

    private void initView() {
        mShareGv.setNumColumns(mShareItemList.size() >= 4 ? 4 : mShareItemList.size());
        mShareDialogAdapter = new ShareDialogAdapter(getContext());
        mShareDialogAdapter.setData(mShareItemList);
        mShareGv.setAdapter(mShareDialogAdapter);
        mShareDialogAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<ShareItem>() {
            @Override
            public void onAdapterItemClick(int position, ShareItem shareItem) {
                switch (shareItem.itemId) {
                    case ShareManager.ITEM_WEXIN:
                        onShareClickListener.onWeiChatClick();
                        break;

                    case ShareManager.ITEM_WEXIN_CIRCLE:
                        onShareClickListener.onFriendCircleClick();
                        break;

                    case ShareManager.ITEM_QQ:
                        onShareClickListener.onQqClick();
                        break;

                    case ShareManager.ITEM_MY_CONSULT:
                        onShareClickListener.onMyConsultClick();
                        break;

                    case ShareManager.ITEM_COPY_LINK:
                        onShareClickListener.onCopyLinkClick();
                        break;

                    case ShareManager.ITEM_QR_CODE:
                        onShareClickListener.onQrCodeClick();
                        break;

                    case ShareManager.ITEM_SAVE_IMG:
                        onShareClickListener.onSaveImgClick();
                        break;
                }
                dismiss();
            }
        });
    }

    @OnClick({R.id.share_dialog_cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_dialog_cancel_tv:
                dismiss();
                break;
        }
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public interface OnShareClickListener {
        void onWeiChatClick();

        void onFriendCircleClick();

        void onQqClick();

        void onMyConsultClick();

        void onCopyLinkClick();

        void onQrCodeClick();

        void onSaveImgClick();
    }
}
