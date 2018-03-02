package jc.cici.android.atom.ui.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.ChildOrderRecyclerAdapter;
import jc.cici.android.atom.adapter.DoubleClassRecAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.AllOrderBean;
import jc.cici.android.atom.bean.ChildOrder;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.OrderDetailBean;
import jc.cici.android.atom.bean.PayMent;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.login.NoticeActivity;
import jc.cici.android.atom.ui.shopCart.PayDetailActivity;
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
 * 订单详情页Activity
 * Created by atom on 2017/8/25.
 */

public class OrderDetailActivity extends BaseActivity {

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
    // 订单完成情况
    @BindView(R.id.condition_txt)
    TextView condition_txt;
    // 支付金额与剩余金额
    @BindView(R.id.havePaidAndUnPaid_txt)
    TextView havePaidAndUnPaid_txt;
    // 订单生成日期
    @BindView(R.id.paidTime_txt)
    TextView paidTime_txt;
    // 当前订单图片
    @BindView(R.id.finishCondition_img)
    ImageView finishCondition_img;
    // 订单号
    @BindView(R.id.orderNumber_txt)
    TextView orderNumber_txt;
    // 完成情况
    @BindView(R.id.finishCondition_txt)
    TextView finishCondition_txt;
    // 列表布局
    @BindView(R.id.typeRecyclerView)
    RecyclerView typeRecyclerView;
    // 收货人布局
    @BindView(R.id.totalReceiverInfo_layout)
    RelativeLayout totalReceiverInfo_layout;
    // 修改收货人图片
    @BindView(R.id.goAddress_Img)
    ImageView goAddress_Img;
    // 收货人
    @BindView(R.id.receiverName_txt)
    TextView receiverName_txt;
    // 收货人电话
    @BindView(R.id.phoneNum_txt)
    TextView phoneNum_txt;
    // 收货人地址
    @BindView(R.id.receiverAddress_txt)
    TextView receiverAddress_txt;
    // 分批支付标题
    @BindView(R.id.batchPayTitle_txt)
    TextView batchPayTitle_txt;
    // 动态分批支付信息
    @BindView(R.id.batchPay_layout)
    LinearLayout batchPay_layout;
    // 总额价格
    @BindView(R.id.totalPrice_txt)
    TextView totalPrice_txt;
    // 优惠券
    @BindView(R.id.value_txt)
    TextView value_txt;
    // 实付金额
    @BindView(R.id.realPay_txt)
    TextView realPay_txt;
    // 注意事项内容
    @BindView(R.id.notice_txt)
    TextView notice_txt;
    // 支付按钮
    @BindView(R.id.accounts_btn)
    Button accounts_btn;
    // 支付布局
    @BindView(R.id.pay_layout)
    RelativeLayout pay_layout;
    // 取消支付按钮
    @BindView(R.id.cancelPay_btn)
    Button cancelPay_btn;
    // 需要支付布局
    @BindView(R.id.needPay_layout)
    RelativeLayout needPay_layout;
    // 提示语
    @BindView(R.id.needPay)
    TextView needPay;
    // 需要支付金额
    @BindView(R.id.needPay_txt)
    TextView needPay_txt;
    // 详情来源
    private int from;
    // 获取订单id
    private int orderId;
    // 进度条对话框
    private Dialog dialog;
    // 单班型适配器
    private ChildOrderRecyclerAdapter childTypeAdapter;
    // 套餐班型适配器
    private DoubleClassRecAdapter doubleClassAdapter;
    private AllOrderBean allOrder;
    private int orderStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderdetail;
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
        // 详情来源
        from = getIntent().getIntExtra("from", 0);
        // 获取订单id
        orderId = getIntent().getIntExtra("orderId", 0);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        initData();

    }

    /**
     * 获取数据
     */
    private void initData() {
        // 添加数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            getData();
        } else {
            dialog.dismiss();
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取订单详情数据
     */
    private void getData() {
        showProcessDialog(baseActivity, R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        final RequestBody body = commonPramas();
        Observable<CommonBean<OrderDetailBean>> observable = httpPostService.getOrderDetailInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<OrderDetailBean>>() {
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
                            Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(CommonBean<OrderDetailBean> orderDetailBeanCommonBean) {
                        if (100 == orderDetailBeanCommonBean.getCode()) {
                            // 获取订单列表
                            allOrder = orderDetailBeanCommonBean.getBody().getOrder();
                            // 订单号
                            orderNumber_txt.setText(allOrder.getOrder_Code());
                            // 订单状态情况
                            orderStatus = allOrder.getOrder_State();
                            switch (orderStatus) {
                                case -1: // 已取消
                                    condition_txt.setText("已取消");
                                    paidTime_txt.setText("下单时间：" + allOrder.getOrder_CreateTime());
                                    finishCondition_img.setBackgroundResource(R.drawable.icon_order_cancer);
                                    // 提示语
                                    needPay.setText("合计：");
                                    // 还需支付布局
                                    needPay_txt.setText("￥" + String.valueOf(allOrder.getOrder_Money()));
                                    needPay_layout.setVisibility(View.VISIBLE);
                                    // 支付布局
                                    pay_layout.setVisibility(View.GONE);
                                    break;
                                case 0: // 未支付
                                    condition_txt.setText("未支付");
                                    paidTime_txt.setText("下单时间：" + allOrder.getOrder_CreateTime());
                                    finishCondition_img.setBackgroundResource(R.drawable.icon_order_unpaid);
                                    // 提示语
                                    needPay.setText("需付款：");
                                    // 还需支付金额
                                    needPay_txt.setText("￥" + allOrder.getOrder_Arrearage());
                                    // 还需支付布局
                                    needPay_layout.setVisibility(View.VISIBLE);
                                    // 支付布局
                                    pay_layout.setVisibility(View.VISIBLE);

                                    break;
                                case 1: // 欠费
                                    condition_txt.setText("已欠费");
                                    float havePaid = orderDetailBeanCommonBean.getBody().getPaymentMoney();
                                    havePaidAndUnPaid_txt.setText("已支付" + String.valueOf(havePaid) + ", 还需支付" + allOrder.getOrder_Arrearage());
                                    paidTime_txt.setText("下单时间：" + allOrder.getOrder_CreateTime());
                                    finishCondition_img.setBackgroundResource(R.drawable.icon_order_debt);
                                    // 提示语
                                    needPay.setText("已支付:￥" + String.valueOf(havePaid) + " 需付款：");
                                    // 还需支付金额
                                    needPay_txt.setText("￥" + allOrder.getOrder_Arrearage());
                                    // 还需支付布局
                                    needPay_layout.setVisibility(View.VISIBLE);
                                    // 支付布局
                                    pay_layout.setVisibility(View.VISIBLE);
                                    cancelPay_btn.setVisibility(View.GONE);
                                    break;
                                case 2: // 已完成
                                    condition_txt.setText("已完成");
                                    paidTime_txt.setText("下单时间：" + allOrder.getOrder_CreateTime());
                                    finishCondition_img.setBackgroundResource(R.drawable.icon_order_finish);
                                    // 还需支付布局
                                    needPay_layout.setVisibility(View.GONE);
                                    // 支付布局
                                    pay_layout.setVisibility(View.GONE);
                                    break;
                            }
                            // 获取订单类型
                            ArrayList<ChildOrder> childOrderList = orderDetailBeanCommonBean.getBody().getChildOrderList();
                            if (null != childOrderList && childOrderList.size() > 0) {
                                for (ChildOrder childOrder : childOrderList) {
                                    if (1 == childOrder.getProduct_Module()) { // 单班型情况
                                        typeRecyclerView.setVisibility(View.VISIBLE);
                                        typeRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity) {
                                            @Override
                                            public boolean canScrollVertically() {
                                                return false;
                                            }
                                        });
                                        childTypeAdapter = new ChildOrderRecyclerAdapter(baseActivity, childOrderList, orderStatus);
                                        typeRecyclerView.setAdapter(childTypeAdapter);
                                        typeRecyclerView.setNestedScrollingEnabled(false);
                                    } else if (5 == childOrder.getProduct_Module()) { // 套餐类型
                                        typeRecyclerView.setVisibility(View.VISIBLE);
                                        typeRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity) {
                                            @Override
                                            public boolean canScrollVertically() {
                                                return false;
                                            }
                                        });
                                        doubleClassAdapter = new DoubleClassRecAdapter(baseActivity, childOrderList, orderStatus);
                                        typeRecyclerView.setAdapter(doubleClassAdapter);
                                        typeRecyclerView.setNestedScrollingEnabled(false);
                                    }
                                }
                            }
                            // 收货人
                            receiverName_txt.setText(ToolUtils.strReplaceAll(allOrder.getOrder_Addressee()));
                            // 收货电话
                            phoneNum_txt.setText(ToolUtils.strReplaceAll(allOrder.getOrder_Phone()));
                            // 收货地址
                            receiverAddress_txt.setText(ToolUtils.strReplaceAll(allOrder.getOrder_Address()));
                            // 总价
                            totalPrice_txt.setText("￥" + String.valueOf(allOrder.getOrder_Money()));
                            // 优惠券
                            value_txt.setText("暂无优惠券");
                            // 实付金额
//                            realPay_txt.setText("￥" + (allOrder.getOrder_Money() - allOrder.getOrder_Arrearage()));
                            realPay_txt.setText("￥" + String.valueOf(allOrder.getOrder_Money()));
                            // 获取分批次支付信息
                            ArrayList<PayMent> payMentList = orderDetailBeanCommonBean.getBody().getPaymentList();
                            if (null != payMentList && !"null".equals(payMentList) && payMentList.size() > 0) { // 含有分批次支付信息情况
                                for (int i = 0; i < payMentList.size(); i++) {
                                    TextView tv = new TextView(baseActivity);
                                    tv.setTextSize(12);
                                    tv.setTextColor(Color.parseColor("#8d8d8d"));
                                    tv.setPadding(30, 15, 15, 15);
                                    if (0 == payMentList.get(i).getPayment_Type()) { // 线下支付情况
                                        tv.setText(payMentList.get(i).getPayment_Date() + "    线下支付" + "    " + payMentList.get(i).getPayment_Money());
                                    } else if (1 == payMentList.get(i).getPayment_Type()) { // 在线支付情况
                                        tv.setText(payMentList.get(i).getPayment_Date() + "    在线支付" + "    " + payMentList.get(i).getPayment_Money());
                                    }
                                    batchPay_layout.addView(tv);
                                }

                                batchPayTitle_txt.setVisibility(View.VISIBLE);
                                batchPay_layout.setVisibility(View.VISIBLE);
                            } else {
                                batchPayTitle_txt.setVisibility(View.GONE);
                                batchPay_layout.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(baseActivity, orderDetailBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("订单详情");
        register_txt.setVisibility(View.GONE);
        String str = "我已阅读并同意《金程网校协议》";
        SpannableString spanText = new SpannableString(str);
        spanText.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#dd5555"));       //设置文件颜色
                ds.setUnderlineText(false); // 不设置下划线
            }

            @Override
            public void onClick(View view) {
                Intent it = new Intent(baseActivity, NoticeActivity.class);
                it.putExtra("from", "pay");
                baseActivity.startActivity(it);
            }
        }, spanText.length() - 8, spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice_txt.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        notice_txt.setText(spanText);
        notice_txt.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
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


    @OnClick({R.id.back_layout, R.id.cancelPay_btn, R.id.accounts_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.cancelPay_btn: // 取消订单
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
                        // 取消订单
                        doCancelPay();
                    }
                });
                break;
            case R.id.accounts_btn: // 去支付
                Intent debtIt = new Intent(baseActivity, PayDetailActivity.class);
                Bundle debtBundle = new Bundle();
                if (1 == orderStatus) { // 欠费情况下
                    debtBundle.putInt("orderStatus", orderStatus);
                }
                debtBundle.putInt("orderId", allOrder.getOrder_PKID());
                debtBundle.putInt("from", from);

                debtIt.putExtras(debtBundle);
                startActivity(debtIt);
                baseActivity.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 取消订单操作
     */
    private void doCancelPay() {

        showProcessDialog(baseActivity, R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        final RequestBody body = commonPramas();
        Observable<CommonBean> observable = httpPostService.getCancelOrderInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
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
                        Toast.makeText(baseActivity, "网络请求异常,请重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) { // 请求成功
                            condition_txt.setText("已取消");
                            finishCondition_img.setBackgroundResource(R.drawable.icon_order_cancer);
                            pay_layout.setVisibility(View.GONE);
                            Intent it = new Intent();
                            switch (from) {
                                case 0: // 全部来源
                                    it.setAction("com.allOrder.refresh");
                                    baseActivity.sendBroadcast(it);
                                    break;
                                case 2: // 未支付来源
                                    it.setAction("com.unpaidOrder.refresh");
                                    baseActivity.sendBroadcast(it);
                                    break;
                            }
                        } else { // 请求失败
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commonPramas() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            // 订单id
            obj.put("orderId", orderId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
