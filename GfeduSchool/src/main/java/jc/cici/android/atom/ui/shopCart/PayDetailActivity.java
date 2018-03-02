package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.alipay.AuthResult;
import jc.cici.android.atom.alipay.OrderInfoUtil2_0;
import jc.cici.android.atom.alipay.PayResult;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.PayInfo;
import jc.cici.android.atom.bean.PayOrderBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.order.OrderHomeActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.weichatpay.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 支付详情页Activity
 * Created by atom on 2017/8/24.
 */

public class PayDetailActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题文字
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 右侧搜索布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 注册按钮屏蔽
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 右侧搜索按钮
    @BindView(R.id.moreBtn)
    Button moreBtn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 提示信息
    @BindView(R.id.tipOrder_txt)
    TextView tipOrder_txt;
    // 金额提示
    @BindView(R.id.unpaid_txt)
    TextView unpaid_txt;
    // 待付金额
    @BindView(R.id.unpaidName_txt)
    TextView unpaidName_txt;
    // 剩余支付金额布局
    @BindView(R.id.orderArr_layout)
    RelativeLayout orderArr_layout;
    // 剩余支付金额
    @BindView(R.id.orderArrName_txt)
    TextView orderArrName_txt;
    // 订单号
    @BindView(R.id.orderName_txt)
    TextView orderName_txt;
    // 商品名称
    @BindView(R.id.goodsName_txt)
    TextView goodsName_txt;
    // 多笔支付布局
    @BindView(R.id.batchPay)
    RelativeLayout batchPay;
    // 闭合布局
    @BindView(R.id.upImg)
    ImageView upImg;
    // 当前支付布局
    @BindView(R.id.nowPay)
    RelativeLayout nowPay;
    // 本次支付金额
    @BindView(R.id.nowPayName_txt)
    EditText nowPayName_txt;
    // 微信支付布局
    @BindView(R.id.weiXinPay_layout)
    RelativeLayout weiXinPay_layout;
    // 微信支付选中图片
    @BindView(R.id.weiXinSel_Img)
    ImageView weiXinSel_Img;
    // 支付宝支付布局
    @BindView(R.id.zhiFuBao_layout)
    RelativeLayout zhiFuBao_layout;
    // 支付宝支付选中图片
    @BindView(R.id.zhiFuBaoSel_Img)
    ImageView zhiFuBaoSel_Img;
    // 支付按钮
    @BindView(R.id.pay_btn)
    Button pay_btn;
    // 订单id
    private int orderId;
    private Dialog dialog;
    // 支付方式标记(2:表示支付宝支付,0:微信支付)
    private int payFlag;
    // 是否允许多笔支付
    private int isMorePay;
    // 当前价格
    private float payPrice;
    // 分批支付剩余价格
    private float arrearagePrice;
    // 最小支付价格
    private float payMinPrice;
    // 订单编号
    private String orderCode;
    // 支付宝同步返回支付金额
    private String totalFee;
    // 支付宝同步返回订单编号
    private String outTradeNo;
    // 支付宝同步返回交易流水号
    private String tradeNo;
    // 进入支付来源
    private int from;
    // 当前订单状态
    private int orderStatus;

    /**
     * 正式上线appid
     */
    public static final String APPID = "2017040806591721";
    /**
     * 正式上线私钥
     */
    public static String RSA2_PRIVATE = "";

//    /**
//     * 沙箱测试appid
//     */
//    public static final String APPID = "2016081900287493";
//    /**
//     * 沙箱测试商户id
//     */
//    public static final String PID = "2088102172081112";
//
//    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDBOqeHoGMg9u08+qcaLcotoPbCfOetGin585Peewih8wG9Xs4C+LeItSm6ZVcJtDwYR9Frt+T1xEgK1JBdzkiJ3thhPBCN2ViuFLP1XF44Ft4z3fDolcInd+8TGFOHLP/Aq/KAyZNzFoT3v/bFDxQ19DQr27tQC5y2lJPoGBKlckWdffka3yT9joBAnl9Tb6tozveJLQ6bi9KJX/gtSAA816ebQNpn+1s1SFmvBce1mws3MejI4jkLwW3sHmaIvCB8BGK6vHuIDQllp11rgD+nHa+uV1drUBczYVORMyBcS5lE225DNofogqBmgfRY6oa0r3WxMUImRhXhyCxwnC0LAgMBAAECggEBAKH3nnHrGOyKznUszWD+PNhecji4JiJNKh5f8/SrQKMvZKftjdWj0YQABwg85eY2c6EOohYewQbZDmI1pwVaYide6hUnGeEs7E6PAAGM+VUepgbn5IWw8lvPBlEFNQ67w20nNFCLptLzb2WUPS8U4qYPGqQgvTPtZ+ELTadgdlDCr/HB8ppxmeGQrao3LR75Ivb9h5WNRBpH4wcjBJI4/C/iAHDK5d0+A7AlNk+ltOExgL+4RUa3haUEGHU5woMWa0Tl4sOF++6RuNmb1A7ogSdjbBMQlzjmv1ObGdJYVvwWP5W/WFAPOAS5qpyy7DLpzrEeHR5fwHLlgYemDgVCL4ECgYEA8+CN1zRkty6JKe50t9FyH51aQULbHaFHUjiqrL+arRQoMw0TVGfEV8e6mz2gAP2utQgLhM8cLblIePEJeUdfY6EFyiWUKsLMbiCLBmIhj/z4RpHBiFEvIRt5MC8d/ijrqVwVumkZRSzWbQuQjYAILyw4zTcfvIpQ5/dkF6X3mSECgYEAytWU0evTvl4hjaHzHXut7FoqaHkq5Bb7wBk0qrjzHPeAW8J+4IcsTNCm6BKXdc4Q/lqAzUfm2ZPMrJMhtyWtPDrBRSs/aeebx7iylZmlJa2vZldddQ+uTV3maqhpoAD3B5eUOvFe3aCDzOCKvnopg21gAjz17hiF/u08TcdoZKsCgYBcx/COkDNE8aJjXoHvMPKvDmk47Nt50VLV+BMOt7J76HBGw9G3yeaL0sOurepClkCyRMZFF5pL1vK4eFM3XazvtWIDfvp4rjWiLCVYH1tPcVlvV6J+XwyEFvSrHTHW8PB/NTZa/gMMKzvErqKnAq+aasONZ0xeqWyQY7EjwhOJoQKBgBM3cvcEcbYqcuFZQ5A6hHrM/BGT0TV02/sSKEQ4a4gGBtoqrhyVkePhLL716/WURVXjuyo3sq5Qa2a6NcqFPkljg6YK9+IPuFmOrRSxTs0oDGMUydzqVGguCkN2mzM2hDDq8nc3IPa8W2BQsbRkHITAGwV7Qw2As0k4rqrrxn8rAoGBAOFgJfITQTe0jN7Xid3GFrJGD3M2nfSkwYxjfhgTWFOKP0B88BORDYeEWCaRO8CiTzk82OPWAd3xhJgjxaYJVTsPLnyRkSXgRR7X4RWE8sU3WUB2Xjchgkfn2CCg4zwvEjP67lc7rwVGXMAlLPxPeUdT7MkitmFkp6nV1PJ7DmKP";
//    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private IWXAPI mIWXAPI;
    public static final String WEICHAT_PAY_FLAG = "weiChat.pay.success";
    public static final String WEICHAT_PAYFAIL_FLAG = "weiChat.pay.fail";
    public static final String WEICHAT_PAYCANCEL_FLAG = "weiChat.pay.cancel";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WEICHAT_PAY_FLAG.equals(intent.getAction())) { // 微信支付成功
                Toast.makeText(baseActivity, "支付成功", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt("from", from);
                bundle.putInt("orderId", orderId);
                baseActivity.openActivityAndCloseThis(FinishPayActivity.class, bundle);
            } else if (WEICHAT_PAYFAIL_FLAG.equals(intent.getAction())) { // 微信支付失败
                if (5 == from) { // 购物车进入支付情况
                    Intent it = new Intent();
                    it.setAction("com.atom.refresh");
                    sendBroadcast(it);
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    baseActivity.openActivityAndCloseThis(OrderHomeActivity.class);
                } else { // 订单页进入支付情况
                    baseActivity.finish();
                }
            } else if (WEICHAT_PAYCANCEL_FLAG.equals(intent.getAction())) { // 取消支付
                if (5 == from) { // 购物车进入支付情况
                    Intent it = new Intent();
                    it.setAction("com.atom.refresh");
                    sendBroadcast(it);
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    baseActivity.openActivityAndCloseThis(OrderHomeActivity.class);
                } else { // 订单页进入支付情况
                    baseActivity.finish();
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    System.out.println("resultInfo >>>:" + resultInfo);
//                    AliPayResult aliPayResult = JSON.parseObject(resultInfo, AliPayResult.class);
//                    totalFee = aliPayResult.getAlipay_trade_app_pay_response().getTotal_amount();
//                    outTradeNo = aliPayResult.getAlipay_trade_app_pay_response().getOut_trade_no();
//                    tradeNo = aliPayResult.getAlipay_trade_app_pay_response().getTrade_no();

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        noticeHttpServer(totalFee, outTradeNo, tradeNo);
                        Toast.makeText(baseActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("orderId", orderId);
                        bundle.putInt("from", from);
                        baseActivity.openActivityAndCloseThis(FinishPayActivity.class, bundle);
                    } else {
                        if (5 == from) { // 购物车进入支付情况
                            Intent it = new Intent();
                            it.setAction("com.atom.refresh");
                            sendBroadcast(it);
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            baseActivity.openActivityAndCloseThis(OrderHomeActivity.class);
                        } else { // 订单页进入支付情况
                            baseActivity.finish();
                        }

                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(baseActivity,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(baseActivity,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

//    /**
//     * 异步通知服务器
//     *
//     * @param totalFee
//     * @param out_trade_no
//     * @param trade_no
//     */
//    private void noticeHttpServer(String totalFee, String out_trade_no, String trade_no) {
//
//        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
//        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
//        JSONObject obj = new JSONObject();
//        CommParam commParam = new CommParam(baseActivity);
//        try {
//            // 订单号
//            obj.put("orderNum", commParam.getUserId());
//            //
//            obj.put("paymentId", orderId);
//            obj.put("totalFee", totalFee);
//            obj.put("outTradeNo", out_trade_no);
//            obj.put("buyerEmail", orderId);
//            obj.put("buyerId", trade_no);
//            obj.put("appName", commParam.getAppname());
//            obj.put("client", commParam.getClient());
//            obj.put("timeStamp", commParam.getTimeStamp());
//            // 测试加密数据
//            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
//
//    }

    private IWXAPI api;
    // 分批价格
    private float batchPrice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paydetail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // 沙箱测试环境
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID);
        baseActivity = this;
        AppManager.getInstance().addActivity(this);
        orderId = getIntent().getIntExtra("orderId", 0);
        from = getIntent().getIntExtra("from", 0);
        orderStatus = getIntent().getIntExtra("orderStatus", 0);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            intData();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    private void intData() {

        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("userId", commParam.getUserId());
            obj.put("orderid", orderId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<PayOrderBean>> observable = httpPostService.getPayOrderInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<PayOrderBean>>() {
                            @Override
                            public void onCompleted() {
                                if (null != dialog && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (null != dialog && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toast.makeText(baseActivity, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<PayOrderBean> payOrderBeanCommonBean) {
                                if (100 == payOrderBeanCommonBean.getCode()) { // 请求成功
                                    if (5 == from) { // 购物车页面进入
                                        tipOrder_txt.setText("提交订单完成，请尽快完成付款！");
                                        unpaid_txt.setText("待付金额：");
                                        // 剩余金额
                                        arrearagePrice = payOrderBeanCommonBean.getBody().getOrder_Money();
                                        // 待付金额
                                        payPrice = payOrderBeanCommonBean.getBody().getOrder_Money();
                                        unpaidName_txt.setText("￥" + String.valueOf(payPrice));
                                    } else { // 订单页面进入情况
                                        tipOrder_txt.setText("等待付款！");
                                        if (1 == orderStatus) { // 分批支付情况
                                            unpaid_txt.setText("已支付金额：");
                                            // 获取已支付金额
                                            float havePayPrice = payOrderBeanCommonBean.getBody().getOrder_Money()
                                                    - payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                            // 转化为小数点后两位
                                            java.text.DecimalFormat myFormat = new java.text.DecimalFormat("0.00");
                                            unpaidName_txt.setText("￥" + String.valueOf(myFormat.format(havePayPrice)));
                                            orderArr_layout.setVisibility(View.VISIBLE);
                                            // 剩余支付金额
                                            arrearagePrice = payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                            payPrice = payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                            orderArrName_txt.setText("￥" + String.valueOf(payPrice));
                                        } else {
                                            unpaid_txt.setText("待付金额：");
                                            // 剩余金额
                                            arrearagePrice = payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                            // 待付金额
                                            payPrice = payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                            unpaidName_txt.setText("￥" + String.valueOf(payPrice));
                                            orderArrName_txt.setText("");
                                            orderArr_layout.setVisibility(View.GONE);
                                        }

                                    }
                                    // 最小支付金额
                                    payMinPrice = payOrderBeanCommonBean.getBody().getOrder_MinPay();
                                    // 订单编号
                                    orderCode = payOrderBeanCommonBean.getBody().getOrder_Code();
                                    orderName_txt.setText(ToolUtils.strReplaceAll(orderCode));
                                    ArrayList<String> arrStr = payOrderBeanCommonBean.getBody().getProductNames();
                                    StringBuffer buffer = new StringBuffer();
                                    if (null != arrStr && !"".equals(arrStr) && arrStr.size() > 0) {
                                        for (int i = 0; i < arrStr.size(); i++) {
                                            buffer.append(arrStr.get(i));
                                            buffer.append(" ");
                                        }
                                        // 商品名称
                                        goodsName_txt.setText(ToolUtils.strReplaceAll(buffer.toString()));
                                    }
                                    // 判断是否需要多笔支付
                                    isMorePay = payOrderBeanCommonBean.getBody().getOrder_IsMPayment();
                                    if (1 == isMorePay) {
                                        batchPay.setVisibility(View.VISIBLE);
                                        if (payPrice > 500) { // 大于500显示多笔支付
                                            nowPay.setVisibility(View.VISIBLE);
                                        } else {// 隐藏多笔支付输入
                                            batchPay.setVisibility(View.GONE);
                                            nowPay.setVisibility(View.GONE);
                                        }
                                    } else {
                                        batchPay.setVisibility(View.GONE);
                                    }

                                } else {
                                    Toast.makeText(baseActivity, payOrderBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 自定义进度
     *
     * @param mContext
     * @param layout
     */
    private void showProcessDialog(Activity mContext, int layout) {
        dialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        dialog.show();
        // 注意此处要放在show之后 否则会报异常
        dialog.setContentView(layout);
    }

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("付款详情");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        mIWXAPI = WXAPIFactory.createWXAPI(baseActivity, Constants.APP_ID);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WEICHAT_PAY_FLAG);
        filter.addAction(WEICHAT_PAYFAIL_FLAG);
        filter.addAction(WEICHAT_PAYCANCEL_FLAG);
        registerReceiver(receiver, filter);
    }

    /**
     * 多笔支付，获取支付金额
     *
     * @param s
     */
    @OnTextChanged({R.id.nowPayName_txt})
    void afterTextChanged(Editable s) {
        String str = nowPayName_txt.getText().toString();
        if (null != str && !"".equals(str)) {
            batchPrice = Float.parseFloat(str);
        }
    }

    /**
     * 按钮设置监听
     *
     * @param view
     */
    @OnClick({R.id.back_layout, R.id.batchPay, R.id.weiXinPay_layout, R.id.zhiFuBao_layout, R.id.pay_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                // 创建对话框
                createDialog();
                break;
            case R.id.batchPay: // 打开伸缩布局
                if (nowPay.getVisibility() == View.GONE) { // 打开
                    nowPay.setVisibility(View.VISIBLE);
                    upImg.setBackgroundResource(R.drawable.icon_down);
                } else { // 收缩
                    nowPay.setVisibility(View.GONE);
                    upImg.setBackgroundResource(R.drawable.icon_up);
                }
                break;
            case R.id.weiXinPay_layout: // 微信支付布局
                payFlag = 1;
                weiXinSel_Img.setBackgroundResource(R.drawable.icon_radio_select);
                zhiFuBaoSel_Img.setBackgroundResource(R.drawable.icon_radio_default);
                break;
            case R.id.zhiFuBao_layout: // 支付宝布局
                payFlag = 2;
                zhiFuBaoSel_Img.setBackgroundResource(R.drawable.icon_radio_select);
                weiXinSel_Img.setBackgroundResource(R.drawable.icon_radio_default);
                break;
            case R.id.pay_btn: // 立即支付按钮
                if (baseActivity.verifyClickTime()) { // 防止重复点击
                    switch (payFlag) {
                        case 0: // 未选择支付方式
                            Toast.makeText(baseActivity, "您还没选择支付方式", Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // 微信支付
                            if (isMorePay == 1) { // 分批支付情况
                                if (batchPrice >= payMinPrice && batchPrice <= arrearagePrice) {
                                    payPrice = batchPrice;
                                    // 请求支付
                                    doWeChatPay();
                                } else {
                                    payPrice = 0;
                                    if (batchPrice > arrearagePrice) {
                                        Toast.makeText(baseActivity, "分批支付金额不能大于支付金额", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if ("".equals(nowPayName_txt.getText().toString())) {
                                            Toast.makeText(baseActivity, "分批支付金额不能为空", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(baseActivity, "分批支付金额不能小于500", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    nowPayName_txt.setText("");
                                }
                            } else { // 普通支付情况
                                // 请求支付
                                doWeChatPay();
                            }
                            break;
                        case 2: // 支付宝支付
                            if (isMorePay == 1) { // 分批支付情况
                                if (batchPrice >= payMinPrice && batchPrice <= arrearagePrice) {
                                    payPrice = batchPrice;
                                    // 请求支付
                                    doAliPay();
                                } else {
                                    payPrice = 0;
                                    if (batchPrice > arrearagePrice) {
                                        Toast.makeText(baseActivity, "分批支付金额不能大于支付金额", Toast.LENGTH_LONG).show();
                                    } else {
                                        if ("".equals(nowPayName_txt.getText().toString())) {
                                            Toast.makeText(baseActivity, "分批支付金额不能为空", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(baseActivity, "分批支付金额不能小于500", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    nowPayName_txt.setText("");
                                }
                            } else { // 普通支付情况
                                // 请求支付
                                doAliPay();
                            }
                            break;
                    }
                }
        }
    }

    /**
     * 微信请求
     */
    private void doWeChatPay() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            String strPayPrice = String.valueOf(payPrice);
            obj.put("userId", commParam.getUserId());
            obj.put("orderid", orderId);
            obj.put("payPrice", strPayPrice);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<PayInfo>> observable = httpPostService.getApplyWeChatPayInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<PayInfo>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(final CommonBean<PayInfo> payInfoCommonBean) {
                                if (100 == payInfoCommonBean.getCode()) {
                                    PayInfo mPayInfo = payInfoCommonBean.getBody();
                                    PayReq req = new PayReq();
                                    req.appId = mPayInfo.getAppId();
                                    req.partnerId = mPayInfo.getPartnerid();
                                    req.prepayId = mPayInfo.getPrepayid();
                                    req.nonceStr = mPayInfo.getNonceStr();
                                    req.timeStamp = mPayInfo.getTimeStamp();
                                    req.packageValue = mPayInfo.getWechatpackage();
                                    req.sign = mPayInfo.getPaySign();
                                    mIWXAPI.sendReq(req);
                                } else {
                                    Toast.makeText(baseActivity, payInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 支付宝请求
     */
    private void doAliPay() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("userId", commParam.getUserId());
            obj.put("orderid", orderId);
            obj.put("payPrice", payPrice);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<PayInfo>> observable = httpPostService.getApplyAliPayInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<PayInfo>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<PayInfo> payInfoCommonBean) {
                                if (100 == payInfoCommonBean.getCode()) {
                                    /****屏蔽线上情况****/
                                    // 获取异步服务器刷新url
                                    String notify_url = payInfoCommonBean.getBody().getNotify_url();
                                    // 获取私钥
                                    RSA2_PRIVATE = payInfoCommonBean.getBody().getMerchant_private_key();
                                    // 获取回传参数
                                    String passback_params = String.valueOf(payInfoCommonBean.getBody().getPaymentId());
                                    // 获取商户id
                                    String pid = payInfoCommonBean.getBody().getPartnerid();
                                    // 订单编号
                                    String orderNum = payInfoCommonBean.getBody().getOrderNum();
                                    /****屏蔽线上情况****/

//                                    /******启用沙箱测试*******/
//                                    String pid = PID;
//                                    String notify_url = payInfoCommonBean.getBody().getNotify_url();
//                                    String passback_params = String.valueOf(payInfoCommonBean.getBody().getPaymentId());
//                                    // 订单编号
//                                    String orderNum = payInfoCommonBean.getBody().getOrderNum();
//                                    /******启用沙箱测试*******/
                                    //秘钥验证的类型 true:RSA2 false:RSA
                                    boolean rsa2 = (RSA2_PRIVATE.length() > 0);
                                    //构造支付订单参数列表
                                    Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, pid, orderNum, payPrice, orderCode, passback_params, notify_url, rsa2);
                                    //构造支付订单参数信息
                                    String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                                    //对支付参数信息进行签名
                                    String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, rsa2);
                                    //订单信息
                                    final String orderInfo = orderParam + "&" + sign;
                                    //异步处理
                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            //新建任务
                                            PayTask alipay = new PayTask(baseActivity);
                                            //获取支付结果
                                            Map<String, String> result = alipay.payV2(orderInfo, true);
                                            Message msg = new Message();
                                            msg.what = SDK_PAY_FLAG;
                                            msg.obj = result;
                                            mHandler.sendMessage(msg);
                                        }
                                    };
                                    // 必须异步调用
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();
                                } else {
                                    Toast.makeText(baseActivity, payInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 创建对话框
     */
    private void createDialog() {
        final Dialog mDialog = new Dialog(baseActivity,
                R.style.NormalDialogStyle);
        mDialog.setContentView(R.layout.dialog_pay);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 继续支付
        Button continue_Btn = (Button) mDialog.findViewById(R.id.continue_Btn);
        // 确认离开
        Button goBack_btn = (Button) mDialog.findViewById(R.id.goBack_btn);

        continue_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                // TODO 调用继续支付
            }
        });
        goBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                Intent it = new Intent();
                it.setAction("com.atom.refresh");
                sendBroadcast(it);
                baseActivity.finish();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 返回按钮设置监听
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 创建对话框
            createDialog();
            return true;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }
}
