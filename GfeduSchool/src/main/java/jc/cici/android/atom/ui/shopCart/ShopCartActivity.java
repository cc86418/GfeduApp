package jc.cici.android.atom.ui.shopCart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.ShopCartRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ShopSingleCart;
import jc.cici.android.atom.bean.ShopcartBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.Tool;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物车首页
 * Created by atom on 2017/7/28.
 */

public class ShopCartActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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
    // 头布局
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // recyclerView
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 底部布局
    @BindView(R.id.buttonLayout)
    RelativeLayout buttonLayout;
    // 全选布局
    @BindView(R.id.checkLayout)
    RelativeLayout checkLayout;
    // checkBox 选中框
    @BindView(R.id.totalCheckBox)
    ImageView totalCheckBox;
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

    // 购物车内容为空
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    // 布局管理器
    private LinearLayoutManager linearLayoutManager;
    private ShopCartRecyclerAdapter adapter;
    private ArrayList<ShopSingleCart> mData = new ArrayList<>();
    // 设置
    public final static int NORMAL_STATUS = 0;
    public final static int DELETE_STATUS = 1;
    // 设置默认为正常情况
    private int mEditModel = NORMAL_STATUS;
    private StringBuffer cartIdStr = new StringBuffer();
    // 总价
    private float totalPrice;
    // 节省价格
    private float remainder;
    // 结算总数
    private int allCount;
    // 全选标记
    private boolean isAllSelect = false;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 全选
                    // 获取选中价格信息
                    getOrderMoneyInfo();
                    // 总价
                    totalPrice_txt.setText("￥" + String.valueOf(totalPrice));
                    // 节省价格
                    remainder_txt.setText("为你节省￥" + String.valueOf(remainder));
                    //结算数量
                    accountsCount_txt.setText("(" + allCount + ")");
                    totalCheckBox.setBackgroundResource(R.drawable.checkbox_pressed);
                    adapter.notifyDataSetChanged();
                    break;
                case 1: // 取消全选
                    // 获取选中价格信息
                    getOrderMoneyInfo();
                    // 总价
                    totalPrice_txt.setText("￥" + String.valueOf(totalPrice));
                    // 节省价格
                    remainder_txt.setText("为你节省￥" + String.valueOf(remainder));
                    //结算数量
                    accountsCount_txt.setText("(" + allCount + ")");
                    totalCheckBox.setBackgroundResource(R.drawable.checkbox_normal);
                    adapter.notifyDataSetChanged();
                    break;
                case 2: // 单个选中
                    // 获取选中价格信息
                    getOrderMoneyInfo();
                    // 总价
                    totalPrice_txt.setText("￥" + String.valueOf(totalPrice));
                    // 节省价格
                    remainder_txt.setText("为你节省￥" + String.valueOf(remainder));
                    //结算数量
                    accountsCount_txt.setText("(" + allCount + ")");
                    boolean flag = false;
                    for (ShopSingleCart shopSingleCart : mData) {
                        if (!shopSingleCart.isSelect()) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        isAllSelect = true;
                        totalCheckBox.setBackgroundResource(R.drawable.checkbox_normal);
                    } else {
                        isAllSelect = false;
                        totalCheckBox.setBackgroundResource(R.drawable.checkbox_pressed);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * 广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.atom.refresh".equals(intent.getAction())) {
                swipeRefreshLayout.setRefreshing(true);
                // 刷新请求数据
                onRefresh();
            }
        }
    };
    private IntentFilter filter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopcart;
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
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    /**
     * 初始化视图操作
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("购物车");
        register_txt.setText("编辑");
        register_txt.setTextColor(Color.parseColor("#333333"));
        register_txt.setVisibility(View.VISIBLE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        // 设置刷新样式
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
        // 注册广播
        filter = new IntentFilter();
        filter.addAction("com.atom.refresh");
        registerReceiver(receiver, filter);
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShopCartRecyclerAdapter(baseActivity, mData, handler, mEditModel);
        recyclerView.setEmptyView(empty_layout);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    /**
     * 网络请求获取购物车信息
     */
    private void getData() {
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

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onNext(CommonBean<ShopcartBean> shopcartBeanCommonBean) {
                                if (100 == shopcartBeanCommonBean.getCode()) {
                                    ArrayList<ShopSingleCart> data = shopcartBeanCommonBean.getBody().getCartList();
                                    if (null != data && !"null".equals(data) && data.size() > 0) { // 获取数据
                                        // 显示底部布局
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        // 获取数据
                                        for (ShopSingleCart shopCart : data) {
                                            // 添加到list中
                                            mData.add(shopCart);
                                        }
                                        if (mEditModel == NORMAL_STATUS) { // 正常情况
                                            // 标记设置为全选状态
                                            isAllSelect = false;
                                            // 全部选中
                                            totalCheckBox.setBackgroundResource(R.drawable.checkbox_pressed);
                                            for (int i = 0; i < mData.size(); i++) {
                                                mData.get(i).setSelect(true);
                                            }
                                            register_txt.setText("编辑");
                                            // 隐藏总价布局
                                            totalMoney_layout.setVisibility(View.VISIBLE);
                                            // 隐藏节约文字
                                            remainder_txt.setVisibility(View.VISIBLE);
                                            // 改变结算文字
                                            accounts_txt.setText("结算");
                                            // 隐藏结算数量
                                            accountsCount_txt.setVisibility(View.VISIBLE);
                                            // 总价
                                            totalPrice = Float.parseFloat(shopcartBeanCommonBean.getBody().getTotalSalePrice());
                                            totalPrice_txt.setText("￥" + String.valueOf(totalPrice));
                                            // 节省价格
                                            remainder = Float.parseFloat(shopcartBeanCommonBean.getBody().getTotalMinus());
                                            remainder_txt.setText("为你节省￥" + String.valueOf(remainder));
                                            //结算数量
                                            allCount = shopcartBeanCommonBean.getBody().getCartCount();
                                            accountsCount_txt.setText("(" + allCount + ")");
                                        } else if (mEditModel == DELETE_STATUS) { // 编辑情况
                                            // 标记设置为非全选状态
                                            isAllSelect = true;
                                            // 全部取消选中
                                            totalCheckBox.setBackgroundResource(R.drawable.checkbox_normal);
                                            for (int i = 0; i < mData.size(); i++) {
                                                mData.get(i).setSelect(false);
                                            }
                                            register_txt.setText("取消");
                                            // 隐藏总价布局
                                            totalMoney_layout.setVisibility(View.GONE);
                                            // 隐藏节约文字
                                            remainder_txt.setVisibility(View.GONE);
                                            // 改变结算文字
                                            accounts_txt.setText("删除");
                                            // 隐藏结算数量
                                            accountsCount_txt.setVisibility(View.GONE);
                                        }
                                    } else { // 没有订单情况
                                        buttonLayout.setVisibility(View.GONE);
                                    }
                                    // 初始化选中状态，刷新列表
                                    adapter.initSelect(mData);
                                    swipeRefreshLayout.setRefreshing(false);
                                    // 设置监听
                                    adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            switch (mEditModel) { // 判断当前状态
                                                case NORMAL_STATUS: // 正常状态
                                                    // 获取班级类型(单版型 or 套餐班型)
                                                    int model = mData.get(position).getProductModule();
                                                    switch (model) {
                                                        case 1: // 单版型情况
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt("Product_PKID", mData.get(position).getModel().getClass_PKID());
                                                            baseActivity.openActivity(OnlineCourseDetailsAloneActivityTwo.class, bundle);
                                                            break;
                                                        case 5: // 套餐班型情况
                                                            Bundle doubleBundle = new Bundle();
                                                            doubleBundle.putInt("Product_PKID", mData.get(position).getModel().getPackage().getPackage_PKID());
                                                            baseActivity.openActivity(OnlineCourseDetailsActivity.class, doubleBundle);
                                                            break;
                                                    }
                                                    break;
                                                case DELETE_STATUS: // 编辑状态
                                                    if (mData.get(position).isSelect()) { // 当前被选中情况
                                                        adapter.singleSelect(position, false);
                                                    } else { // 当前取消选中情况
                                                        adapter.singleSelect(position, true);
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                } else {
                                    buttonLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(baseActivity, shopcartBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    @OnClick({R.id.back_layout, R.id.checkLayout, R.id.accounts_layout, R.id.register_txt})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮监听
                baseActivity.finish();
                break;
            case R.id.checkLayout: // 全选监听
                if (!isAllSelect) {
                    if (null != adapter) { // 取消所有选中
                        adapter.neverAll();
                        // 全选标记设计为true
                        isAllSelect = true;
                    }
                } else { // 全部选中选中
                    if (null != adapter) {
                        adapter.selectAll();
                        // 全选标记设计为false
                        isAllSelect = false;
                    }
                }
                break;
            case R.id.accounts_layout: // 结算或删除监听
                switch (mEditModel) {
                    case NORMAL_STATUS:
                        if (null != mData && !"".equals(mData) && mData.size() > 0) {
                            // 取消已保存的id
                            cartIdStr.setLength(0);
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).isSelect()) {
                                    cartIdStr.append(mData.get(i).getCartId());
                                    cartIdStr.append(",");
                                }
                            }
                            if (null != cartIdStr && !"".equals(cartIdStr) && cartIdStr.length() > 0) {
                                Intent it = new Intent(baseActivity, EnsureOrderAc.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("cartIdStr", cartIdStr.toString());
                                it.putExtras(bundle);
                                startActivity(it);
                            }
                        } else {
                            return;
                        }
                        break;
                    case DELETE_STATUS:
                        if (null != mData && !"".equals(mData) && mData.size() > 0) {
                            // 取消已保存的id
                            cartIdStr.setLength(0);
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).isSelect()) {
                                    cartIdStr.append(mData.get(i).getCartId());
                                    cartIdStr.append(",");
                                }
                            }
                            // 至少有一个选中情况才执行删除操作
                            if (null != cartIdStr && cartIdStr.length() > 0) {
                                // 删除选中内容
                                deleteItem();
                            }
                        } else { // 如果数据为空的情况
                            return;
                        }

                        break;
                }
                break;
            case R.id.register_txt: // 编辑按钮
                // 设置加载进度
                swipeRefreshLayout.setRefreshing(true);
                // 修改当前状态值
                mEditModel = mEditModel == NORMAL_STATUS ? DELETE_STATUS : NORMAL_STATUS;
                // 刷新列表
                onRefresh();
                break;
        }
    }

    /**
     * 执行删除操作
     */
    private void deleteItem() {

        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 测试数据
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
        Observable<CommonBean> observable = httpPostService.getRemoveCartInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            cartIdStr.setLength(0);
                            mData.clear();
                            getData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            // 购物车id
            obj.put("cartIds", 0);
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

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                getData();
            }
        }, 2000);
    }

    /**
     * 遍历获取选中支付价格，优惠价格，和支付数量
     */
    private void getOrderMoneyInfo() {
        // 重置总价
        totalPrice = 0;
        // 重置节省价格
        remainder = 0;
        // 重置结算数量
        allCount = 0;
        // 遍历获取所有选中总价格
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelect()) {
                // 结算总额加1
                allCount++;
                if (1 == mData.get(i).getProductModule()) { // 单版型情况
                    totalPrice += Float.parseFloat(mData.get(i).getModel().getClassType_SalePrice());
                    // 获取单班型
                    remainder += (Float.parseFloat(mData.get(i).getModel().getClassType_Price())
                            - (Float.parseFloat(mData.get(i).getModel().getClassType_SalePrice())));
                } else if (5 == mData.get(i).getProductModule()) { // 套餐班型情况
                    totalPrice += Float.parseFloat(mData.get(i).getModel().getSalePrice());
                    // 获取单班型
                    remainder += (Float.parseFloat(mData.get(i).getModel().getPrice())
                            - (Float.parseFloat(mData.get(i).getModel().getSalePrice())));
                }
            }
        }
    }
}
