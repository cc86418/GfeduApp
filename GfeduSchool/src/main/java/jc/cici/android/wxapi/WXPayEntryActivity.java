package jc.cici.android.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import jc.cici.android.atom.weichatpay.Constants;

import static jc.cici.android.atom.ui.shopCart.PayDetailActivity.WEICHAT_PAYCANCEL_FLAG;
import static jc.cici.android.atom.ui.shopCart.PayDetailActivity.WEICHAT_PAYFAIL_FLAG;
import static jc.cici.android.atom.ui.shopCart.PayDetailActivity.WEICHAT_PAY_FLAG;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode){
                case 0: // 支付成功
                    Intent it = new Intent();
                    it.setAction(WEICHAT_PAY_FLAG);
                    sendBroadcast(it);
                    this.finish();
                    break;
                case -1: // //支付失败
                    Intent failIt = new Intent();
                    failIt.setAction(WEICHAT_PAYFAIL_FLAG);
                    sendBroadcast(failIt);
                    this.finish();
                    break;
                case -2: // 取消支付
                    Intent cancelIt = new Intent();
                    cancelIt.setAction(WEICHAT_PAYCANCEL_FLAG);
                    sendBroadcast(cancelIt);
                    this.finish();
                    break;
            }
        }
    }
}