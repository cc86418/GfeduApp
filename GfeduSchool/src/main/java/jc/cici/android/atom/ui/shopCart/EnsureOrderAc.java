package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import cn.gfedu.gfeduapp.MainActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.EnsureOrderRecAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.AddressBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.EnsureOrderBean;
import jc.cici.android.atom.bean.ShopSingleCart;
import jc.cici.android.atom.bean.ShopcartBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.login.NoticeActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
//import jc.cici.android.atom.view.MyLinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 确认订单页activity
 * Created by atom on 2017/9/13.
 */

public class EnsureOrderAc extends BaseActivity {

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
    // recyclerView
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 底部布局
    @BindView(R.id.buttonLayout)
    RelativeLayout buttonLayout;
    // 购物车内容为空
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    // 地址布局
    @BindView(R.id.totalReceiverInfo_layout)
    RelativeLayout totalReceiverInfo_layout;
    //收货人
    @BindView(R.id.receiverName_txt)
    TextView receiverName_txt;
    // 详细地址
    @BindView(R.id.receiverAddress_txt)
    TextView receiverAddress_txt;
    // 筛选布局
    @BindView(R.id.goAddress_Img)
    ImageView goAddress_Img;
    // 地址信息布局
    @BindView(R.id.addressInfo_layout)
    LinearLayout addressInfo_layout;
    // 新增收货地址
    @BindView(R.id.addAddress_txt)
    TextView addAddress_txt;
    // 优惠券布局
    @BindView(R.id.value_layout)
    RelativeLayout value_layout;
    // 优惠券内容
    @BindView(R.id.value_txt)
    TextView value_txt;
    // 注意事项选中框
    @BindView(R.id.notice_checkBox)
    CheckBox notice_checkBox;
    // 注意事项内容
    @BindView(R.id.notice_txt)
    TextView notice_txt;
    // 全选布局
    @BindView(R.id.checkLayout)
    RelativeLayout checkLayout;
    // 总价布局
    @BindView(R.id.totalMoney_layout)
    RelativeLayout totalMoney_layout;
    //总价文字
    @BindView(R.id.totalPrice_txt)
    TextView totalPrice_txt;
    // 节省文字
    @BindView(R.id.remainder_txt)
    TextView remainder_txt;
    // 结算布局
    @BindView(R.id.accounts_layout)
    RelativeLayout accounts_layout;
    // 结算文字
    @BindView(R.id.accounts_txt)
    TextView accounts_txt;
    // 结算数量
    @BindView(R.id.accountsCount_txt)
    TextView accountsCount_txt;

    // 购物车id
    private String cartIdStr;
    // 布局管理器
    private LinearLayoutManager linearLayoutManager;
    private EnsureOrderRecAdapter adapter;
    private ArrayList<ShopSingleCart> mData = new ArrayList<>();
    // 进度条
    private Dialog dialog;
    // 1.根据地址ID获取    2.获取默认
    private int addressType = 2;
    // 地址id
    private int addressPKID;
    // 收货人
    private String receiverName;
    // 收货详细地址
    private String expendDetail;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.ensure.changeAddress".equals(intent.getAction())) {
                int mAddressPKID = intent.getIntExtra("addressPKID", 0);
                if (0 == mAddressPKID || mAddressPKID == addressPKID) { // 如果删除同一个情况
                    receiverName = "";
                    expendDetail = "";
                    // 收货人
                    receiverName_txt.setText("");
                    receiverAddress_txt.setText("");
                    addressInfo_layout.setVisibility(View.GONE);
                    addAddress_txt.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ensuer_order;
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
        cartIdStr = getIntent().getStringExtra("cartIdStr");
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        if (NetUtil.isMobileConnected(baseActivity)) {
            // 获取订单信息
            getAddressInfo(addressType);
            // 获取订单信息
            getOrderListInfo();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    /**
     * 获取订单列表信息
     */
    private void getOrderListInfo() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        final RequestBody body = commonPramas();
        Observable<CommonBean<ShopcartBean>> observable = httpPostService.getCartListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<ShopcartBean>>() {
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
                                Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<ShopcartBean> shopcartBeanCommonBean) {

                                if (100 == shopcartBeanCommonBean.getCode()) { // 请求成功
                                    ArrayList<ShopSingleCart> data = shopcartBeanCommonBean.getBody().getCartList();
                                    if (null != data && !"null".equals(data) && data.size() > 0) { // 获取数据
                                        // 显示底部布局
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        // 获取数据
                                        for (ShopSingleCart shopCart : data) {
                                            // 添加到list中
                                            mData.add(shopCart);
                                        }
                                    } else { // 没有订单情况
                                        buttonLayout.setVisibility(View.GONE);
                                    }
                                    // 刷新列表
                                    adapter.notifyDataSetChanged();
                                    if (null != receiverName && !"".equals(receiverName)) {
                                        // 收货人
                                        receiverName_txt.setText("收货人：" + ToolUtils.strReplaceAll(receiverName));
                                    }
                                    if (null != expendDetail && !"".equals(expendDetail)) {
                                        receiverAddress_txt.setText("详细地址：" + ToolUtils.strReplaceAll(expendDetail));
                                    }
                                    if (null != receiverName && !"".equals(receiverName)
                                            && null != expendDetail && !"".equals(expendDetail)) {
                                        addressInfo_layout.setVisibility(View.VISIBLE);
                                        addAddress_txt.setVisibility(View.GONE);
                                    } else {
                                        addressInfo_layout.setVisibility(View.GONE);
                                        addAddress_txt.setVisibility(View.VISIBLE);
                                    }
                                    // 优惠券
                                    value_txt.setText("暂无优惠券");
                                    // 总价
                                    totalPrice_txt.setText("￥" + shopcartBeanCommonBean.getBody().getTotalSalePrice());

                                } else { // 请求失败
                                    Toast.makeText(baseActivity, shopcartBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 获取默认地址信息
     *
     * @param addressType
     */
    private void getAddressInfo(int addressType) {
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            obj.put("getAddressType", addressType);
            if (1 == addressType) { // 修改地址情况
                obj.put("addressPKID", addressPKID);
            } else { // 默认情况
                obj.put("addressPKID", 0);
            }
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<AddressBean>> observable = httpPostService.getAddressSingleInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<AddressBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(CommonBean<AddressBean> addressBeanCommonBean) {
                                if (100 == addressBeanCommonBean.getCode()) {
                                    // 地址id
                                    addressPKID = addressBeanCommonBean.getBody().getAddressPKID();
                                    // 收货人
                                    receiverName = addressBeanCommonBean.getBody().getAddressName();
                                    // 收货详细地址
                                    expendDetail = addressBeanCommonBean.getBody().getAddressProvince()
                                            + addressBeanCommonBean.getBody().getAddressCity()
                                            + addressBeanCommonBean.getBody().getAddressDetail();
                                } else {
                                    Toast.makeText(baseActivity, addressBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 初始化视图，获取视图控件
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("确认订单");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ensure.changeAddress");
        registerReceiver(receiver, filter);
        // 隐藏全选布局
        checkLayout.setVisibility(View.GONE);
        //  隐藏节省价格
        remainder_txt.setVisibility(View.GONE);
        // 隐藏结算
        accountsCount_txt.setVisibility(View.GONE);
        // 设置结算文字
        accounts_txt.setText("提交订单");
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
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new EnsureOrderRecAdapter(baseActivity, mData);
        recyclerView.setEmptyView(empty_layout);
        recyclerView.setAdapter(adapter);
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
            // 用户id
            obj.put("userId", commParam.getUserId());
            // 购物车id
            obj.put("cartIds", cartIdStr);
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


    @OnClick({R.id.back_layout, R.id.totalReceiverInfo_layout, R.id.value_layout, R.id.accounts_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.totalReceiverInfo_layout: // 收货地址
                // 选择地址
                Intent it = new Intent(baseActivity, AddressManagerAc.class);
                it.putExtra("from", "ensureOrder");
                startActivityForResult(it, 1);
                break;
            case R.id.value_layout: // 优惠券
                Toast.makeText(baseActivity, "暂无可用优惠券", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accounts_layout: // 确认订单
                if (null != receiverName && !"".equals(receiverName)
                        && receiverName.length() > 0 && null != expendDetail && !"".equals(expendDetail)) {
                    if (notice_checkBox.isChecked()) { // 如果注意事项选中
                        ensureOrderInfo();
                    } else { // 注意事项未选中情况
                        Toast.makeText(baseActivity, "请先阅读并同意确认《金程协议》", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 创建添加地址对话框
                    createAddress();
                }

                break;
        }
    }

    /**
     * 确认订单信息
     */
    private void ensureOrderInfo() {

        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", commParam.getUserId());
            // 购买id
            obj.put("cartIds", cartIdStr);
            // 地址id
            obj.put("addressId", addressPKID);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<EnsureOrderBean>> observable = httpPostService.getCreateOrderInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<EnsureOrderBean>>() {
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
                        Toast.makeText(baseActivity, "订单确认失败，网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<EnsureOrderBean> commonBean) {
                        if (100 == commonBean.getCode()) {
                            if (null != commonBean.getBody()) {
                                int orderId = commonBean.getBody().getOrderId();
                                Bundle bundle = new Bundle();
                                bundle.putInt("orderId", orderId);
                                // 5 表示订单页面传递
                                bundle.putInt("from", 5);
                                baseActivity.openActivityAndCloseThis(PayDetailActivity.class, bundle);
                            } else { // 零元订单
                                createDialog();
                            }

                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 创建对话框
     */
    private void createDialog() {
        final Dialog mDialog = new Dialog(baseActivity,
                R.style.NormalDialogStyle);
        mDialog.setContentView(R.layout.dialog_shopping_success);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 去学习
        Button goStudy_btn = (Button) mDialog.findViewById(R.id.goStudy_btn);
        goStudy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseActivity, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mDialog.dismiss();

            }
        });
    }

    /**
     * 对话框提醒选择地址
     */
    private void createAddress() {
        final Dialog mDialog = new Dialog(baseActivity,
                R.style.NormalDialogStyle);
        mDialog.setContentView(R.layout.view_addaddress_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 选择地址
        Button continue_Btn = (Button) mDialog.findViewById(R.id.addAddress_Btn);
        // 确认离开
        Button goBack_btn = (Button) mDialog.findViewById(R.id.goBack_btn);

        continue_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                // 选择地址
                Intent it = new Intent(baseActivity, AddressManagerAc.class);
                it.putExtra("from", "ensureOrder");
                startActivityForResult(it, 1);
            }
        });
        goBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                baseActivity.finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (2 == resultCode) { // 如果返回值为2
            if (1 == requestCode) { // 请求值为1
                Bundle bundle = data.getExtras();
                addressPKID = bundle.getInt("addressPKID");
                receiverName = bundle.getString("addressName");
                // 所属省份
                String province = bundle.getString("addressProvince");
                // 所属市
                String city = bundle.getString("addressCity");
                // 具体地址
                String details = bundle.getString("addressDetail");

                expendDetail = province + city + details;
                if (null != receiverName && !"".equals(receiverName)) {
                    // 收货人
                    receiverName_txt.setText("收货人：" + ToolUtils.strReplaceAll(receiverName));
                }
                if (null != expendDetail && !"".equals(expendDetail)) {
                    receiverAddress_txt.setText("详细地址：" + ToolUtils.strReplaceAll(expendDetail));
                }
                if (null != receiverName && !"".equals(receiverName)
                        && null != expendDetail && !"".equals(expendDetail)) {
                    addressInfo_layout.setVisibility(View.VISIBLE);
                    addAddress_txt.setVisibility(View.GONE);
                } else {
                    addressInfo_layout.setVisibility(View.GONE);
                    addAddress_txt.setVisibility(View.VISIBLE);
                }
            }
        }
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
