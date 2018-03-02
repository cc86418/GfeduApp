package jc.cici.android.atom.weichatpay;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import jc.cici.android.R;
import jc.cici.android.atom.bean.PayInfo;

/**
 * Created by User on 2017/9/18.
 */

public class WeChatPayTask extends AsyncTask<Void, Void, Void> implements IWXAPIEventHandler {

    /**
     * 微信支付错误检测 提示语
     */
    private static final String WX_PAY_ERRMSG_1 = "您没有安装微信...";
    private static final String WX_PAY_ERRMSG_2 = "当前版本不支持支付功能...";
    private static final String WX_PAY_ERRMSG_3 = "微信支付失败...";
    private IWXAPI mIWXAPI;
    private Context mCtx;
    private PayInfo mPayInfo;
    private Handler mHandler;

    public WeChatPayTask(Context context, PayInfo payInfo, Handler handler) {
        this.mCtx = context;
        this.mIWXAPI = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        this.mPayInfo = payInfo;
        this.mHandler = handler;
//        Log.d("mPayInfo", new Gson().toJson(mPayInfo).toString());
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setTitle("提示");
            builder.setMessage(mCtx.getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();
        } else {
            Toast.makeText(mCtx, "哈哈 支付失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        PayReq req = new PayReq();
        req.appId = mPayInfo.getAppId();
        req.partnerId = mPayInfo.getPartnerid();
        req.prepayId = mPayInfo.getPrepayid();
        req.nonceStr = mPayInfo.getNonceStr();
        req.timeStamp = mPayInfo.getTimeStamp();
        req.packageValue = mPayInfo.getWechatpackage();
        req.sign = mPayInfo.getPaySign();
        mIWXAPI.sendReq(req);
        return null;
    }
}
