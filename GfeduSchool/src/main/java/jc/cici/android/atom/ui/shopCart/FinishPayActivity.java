package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.PayOrderBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.order.OrderHomeActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 完成支付Activity
 * Created by atom on 2017/8/24.
 */

public class FinishPayActivity extends BaseActivity {

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
    // 已支付金额
    @BindView(R.id.paidName_txt)
    TextView paidName_txt;
    // 待支付金额布局
    @BindView(R.id.remain_layout)
    RelativeLayout remain_layout;
    // 待支付金额
    @BindView(R.id.remainName_txt)
    TextView remainName_txt;
    // 查看订单按钮
    @BindView(R.id.checkOrder_btn)
    Button checkOrder_btn;
    // 去开票 or 继续支付
    @BindView(R.id.goBill_btn)
    TextView goBill_btn;
    // 去首页
    @BindView(R.id.goHome_layout)
    RelativeLayout goHome_layout;
    // 移步隐藏布局
    @BindView(R.id.notice_layout)
    RelativeLayout notice_layout;
    // 订单id
    private int orderId;
    private Dialog dialog;
    // 已支付价格
    private float payPrice;
    // 剩余支付金额
    private float unPayPrice;
    private int mFrom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_finishpay;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        AppManager.getInstance().addActivity(this);
        orderId = getIntent().getIntExtra("orderId", 0);
        mFrom = getIntent().getIntExtra("from", 0);
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
                                    // 已支付金额
                                    payPrice = payOrderBeanCommonBean.getBody().getOrder_Money() - payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                    java.text.DecimalFormat myFormat = new java.text.DecimalFormat("0.00");
                                    paidName_txt.setText("￥" + String.valueOf((myFormat.format(payPrice))));
                                    // 订单编号
                                    unPayPrice = payOrderBeanCommonBean.getBody().getOrder_Arrearage();
                                    if (unPayPrice > 0) { // 含有剩余支付情况
                                        remain_layout.setVisibility(View.VISIBLE);
                                        remainName_txt.setText("￥" + String.valueOf(unPayPrice));
                                        goBill_btn.setText("继续支付");
                                    } else { // 不含有剩余支付情况
                                        remain_layout.setVisibility(View.GONE);
                                        remainName_txt.setText("");
                                        goBill_btn.setText("开发票");
                                    }
//                                    // 商品名称
//                                    goodsName_txt.setText(ToolUtils.strReplaceAll(payOrderBeanCommonBean.getBody().));
                                } else {
                                    Toast.makeText(baseActivity, payOrderBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
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
    }

    @OnClick({R.id.back_layout, R.id.checkOrder_btn, R.id.goBill_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                if (5 == mFrom) {
                    baseActivity.openActivityAndCloseThis(OrderHomeActivity.class);
                } else {
                    Intent it = new Intent();
                    switch (mFrom) {
                        case 0: // 全部情况
                            it.setAction("com.allOrder.refresh");
                            break;
                        case 2: // 未支付来源
                            it.setAction("com.unpaidOrder.refresh");
                            break;
                        case 3: // 欠费来源
                            it.setAction("com.debtOrder.refresh");
                            break;
                        case 4: // 已取消来源
                            it.setAction("com.cancelOrder.refresh");
                            break;
                    }
                    sendBroadcast(it);
                    baseActivity.finish();
                }
                break;
            case R.id.checkOrder_btn: // 查看订单
                switch (mFrom) {
                    case 0: // 全部订单来源
                        Intent allIt = new Intent();
                        allIt.setAction("com.allOrder.refresh");
                        sendBroadcast(allIt);
                        baseActivity.finish();
                        break;
                    case 2: // 未支付来源
                        Intent unPaidIt = new Intent();
                        unPaidIt.setAction("com.unpaidOrder.refresh");
                        sendBroadcast(unPaidIt);
                        baseActivity.finish();
                        break;
                    case 3: // 欠费来源
                        Intent debtIt = new Intent();
                        debtIt.setAction("com.debtOrder.refresh");
                        sendBroadcast(debtIt);
                        baseActivity.finish();
                        break;
                    case 4: // 已取消来源
                        Intent cancelIt = new Intent();
                        cancelIt.setAction("com.cancelOrder.refresh");
                        sendBroadcast(cancelIt);
                        baseActivity.finish();
                        break;
                    case 5: // 购物车来源
                        baseActivity.openActivityAndCloseThis(OrderHomeActivity.class);
                        break;
                }
                break;
            case R.id.goBill_btn: // 去开票Or继续支付
                if (unPayPrice > 0) { // 继续支付
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId", orderId);
                    baseActivity.openActivityAndCloseThis(PayDetailActivity.class, bundle);
                } else { // 去开发票
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId", orderId);
                    // 来源
                    bundle.putInt("mFrom", mFrom);
                    baseActivity.openActivityAndCloseThis(BillActivity.class, bundle);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
