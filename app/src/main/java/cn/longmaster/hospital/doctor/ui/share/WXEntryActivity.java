
package cn.longmaster.hospital.doctor.ui.share;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logI(Logger.COMMON, "Share WeChat" + "onCreate");
        if (mApi == null) {
            mApi = ShareManager.getWeChatApi();
        }
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.logI(Logger.COMMON, "Share WeChat" + "onNewIntent");
        if (mApi == null) {
            mApi = ShareManager.getWeChatApi();
        }
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.logI(Logger.COMMON, "Share WeChat" + "onReq");
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;

            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;

            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        finish();
    }
}
