package jc.cici.android.atom.ui.order;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.AllOrderRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.AllOrderBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.OrderBean;
import jc.cici.android.atom.bean.OrderList;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
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
 * 已支付订单内容
 * Created by atom on 2017/8/25.
 */

public class PaidFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    // 获取环境对象
    private Activity mCtx;
    // 刷新头布局
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // 列表
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // 线性布局
    private LinearLayoutManager linearLayoutManager;
    // 数据列表
    private ArrayList<OrderList> mData = new ArrayList<>();
    // 适配器
    private AllOrderRecyclerAdapter adapter;
    private Handler handler = new Handler();
    private int page = 1;
    private boolean isLoading;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.paidOrder.refresh".equals(intent.getAction())){
                swipeRefreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        mData.clear();
                        refreshData(page);
                    }
                }, 2000);
            }
        }
    };
    private IntentFilter filter;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        // 设置刷新样式
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(mCtx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(emptyView);
        adapter = new AllOrderRecyclerAdapter(mCtx, mData,1);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                page = 1;
                mData.clear();
                mData.clear();
                swipeRefreshLayout.setRefreshing(true);
                refreshData(page);
            }
        });
        // 滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    if (!isLoading) { // 加载更多
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                refreshData(page);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

        // 注册广播监听器
        filter = new IntentFilter();
        filter.addAction("com.paidOrder.refresh");
        mCtx.registerReceiver(receiver, filter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = getActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_orderfragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        initView(view, null);
        return view;
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                page = 1;
                refreshData(page);
            }
        }, 2000);
    }

    /**
     * 刷新数据
     *
     * @param page
     */
    private void refreshData(int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(page);
        final Observable<CommonBean<OrderBean>> observable = httpPostService.getOrderListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<OrderBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mCtx, "网络请求异常，请下拉刷新重试", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(CommonBean<OrderBean> orderBeanCommonBean) {

                        if (100 == orderBeanCommonBean.getCode()) {

                            ArrayList<OrderList> data = orderBeanCommonBean.getBody().getList();
                            if (null != data && !"null".equals(data) && data.size() > 0) {
                                // 添加数据并刷新列表
                                for (OrderList orderList : data) {
                                    mData.add(orderList);
                                }
                                adapter.notifyDataSetChanged();
                                // item 设置监听
                                adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent it = new Intent(mCtx, OrderDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("from",1);
                                        bundle.putInt("orderId", mData.get(position).getOrder().getOrder_PKID());
                                        it.putExtras(bundle);
                                        mCtx.startActivity(it);
                                    }
                                });
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        } else { // 请求失败提示
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(mCtx, orderBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 公共请求参数
     *
     * @param page
     * @return
     */
    private RequestBody commonPramas(int page) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            // 订单状态 2表示已支付
            obj.put("orderStatus", 2);
            obj.put("pageIndex", page);
            obj.put("pageSize", 10);
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
    public void onDestroy() {
        super.onDestroy();
        // 销毁广播
        if (null != receiver) {
            mCtx.unregisterReceiver(receiver);
        }
    }
}
