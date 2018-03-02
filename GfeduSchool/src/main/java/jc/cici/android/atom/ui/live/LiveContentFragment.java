package jc.cici.android.atom.ui.live;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gfedu.home_pager.BaseFragment;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.BannerStatusSection;
import jc.cici.android.atom.adapter.ExpiredLiveStatusSection;
import jc.cici.android.atom.adapter.HotLiveStatusSection;
import jc.cici.android.atom.adapter.LiveContentAdapter;
import jc.cici.android.atom.adapter.SeriesLiveStatusSection;
import jc.cici.android.atom.adapter.LateLiveStatusSection;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LiveAds;
import jc.cici.android.atom.bean.LiveContent;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
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
 * 直播首页适配器
 * Created by atom on 2017/11/8.
 */

public class LiveContentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Activity baseActivity;
    private Unbinder unbinder;
    private View view;
    // 下拉刷新
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    // 列表
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    private GridLayoutManager gridLayoutManager;
    private boolean mIsRefreshing = false;
    // 项目id
    private int projectId;
    // 项目名称
    private String projectName;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 广告列表
    private ArrayList<LiveAds> adsList = new ArrayList<>();
    // 获取最近直播列表
    private ArrayList<LiveContent.LateContent> LastestLive = new ArrayList<>();
    // 获取热门直播列表
    private ArrayList<LiveContent.HotContent> HotLive = new ArrayList<>();
    //  获取系列直播列表
    private ArrayList<LiveContent.SerContent> SeriesLive = new ArrayList<>();
    //  获取回放列表
    private ArrayList<LiveContent.Content> ExpiredLive = new ArrayList<>();
    // 适配器对象
    private LiveContentAdapter adapter;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                swipe.setRefreshing(true);
                clearData();
                fetchData();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this.getActivity();
        // 获取项目id
        projectId = getArguments().getInt("projectId", 0);
        // 获取项目名称
        projectName = getArguments().getString("projectName");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        // 设置刷新样式
        swipe.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipe.setOnRefreshListener(this);
        adapter = new LiveContentAdapter();
        gridLayoutManager = new GridLayoutManager(baseActivity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getSectionItemViewType(position)) {
                    case LiveContentAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setEmptyView(empty_layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
    }

    private int getLayoutId() {
        return R.layout.fragment_live_content;
    }

    @Override
    public void fetchData() {
        System.out.println("fetchData()");
        if (NetUtil.isMobileConnected(baseActivity)) {
            swipe.post(new Runnable() {
                @Override
                public void run() {
                    swipe.setRefreshing(true);
                    mIsRefreshing = true;
                    initData();
                }
            });
        } else {
            Toast.makeText(baseActivity, "网络连接异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commParams();
        Observable<CommonBean<LiveContent>> observable = httpPostService.getLiveIndexInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveContent>>() {
                    @Override
                    public void onCompleted() {
                        if (swipe.isRefreshing()) {
                            swipe.setRefreshing(false);
                            mIsRefreshing = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (swipe.isRefreshing()) {
                            swipe.setRefreshing(false);
                            mIsRefreshing = false;
                        }
                        Toast.makeText(baseActivity, "网络请求异常，请刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<LiveContent> liveContentCommonBean) {
                        if (100 == liveContentCommonBean.getCode()) {
                            // 获取广告列表
                            adsList = liveContentCommonBean.getBody().getAdList();
                            // 获取最近直播列表
                            LastestLive = liveContentCommonBean.getBody().getLastestLive();
                            // 获取热门直播列表
                            HotLive = liveContentCommonBean.getBody().getHotLive();
                            //  获取系列直播列表
                            SeriesLive = liveContentCommonBean.getBody().getSeriesLive();
                            //  获取回放列表
                            ExpiredLive = liveContentCommonBean.getBody().getExpiredLive();
                            if (null != adsList && adsList.size() > 0) {
                                // 添加banner
                                adapter.addSection(new BannerStatusSection(baseActivity, adsList));
                            }
                            if (null != LastestLive && LastestLive.size() > 0) {
                                // 添加最近直播
                                adapter.addSection(new LateLiveStatusSection(baseActivity, LastestLive, mHandler, projectId, projectName));
                            }
                            if (null != HotLive && HotLive.size() > 0) {
                                // 添加热门直播
                                adapter.addSection(new HotLiveStatusSection(baseActivity, HotLive, projectId, projectName));
                            }
                            if (null != SeriesLive && SeriesLive.size() > 0) {
                                if (null == LastestLive || LastestLive.size() == 0) { // 如果不存在最近和热门情况
                                    // 添加系列直播
                                    adapter.addSection(new SeriesLiveStatusSection(baseActivity, SeriesLive, projectId, projectName, 1));
                                } else { // 正常情况
                                    // 添加系列直播
                                    adapter.addSection(new SeriesLiveStatusSection(baseActivity, SeriesLive, projectId, projectName, 0));
                                }
                            }
                            if (null != ExpiredLive && ExpiredLive.size() > 0) {
                                if ((null == LastestLive && null == SeriesLive)) { // 当前页面第一条为精彩回放情况
                                    // 添加回放
                                    adapter.addSection(new ExpiredLiveStatusSection(baseActivity, ExpiredLive, projectId, projectName, 1));
                                } else { // 第一条非精彩回放情况
                                    // 添加回放
                                    adapter.addSection(new ExpiredLiveStatusSection(baseActivity, ExpiredLive, projectId, projectName, 0));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(baseActivity, liveContentCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commParams() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
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
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        clearData();
        fetchData();
    }

    private void clearData() {
        if (null != adsList) {
            adsList.clear();
        }
        if (null != LastestLive) {
            LastestLive.clear();
        }
        if (null != HotLive) {
            HotLive.clear();
        }
        if (null != SeriesLive) {
            SeriesLive.clear();
        }
        if (null != ExpiredLive) {
            ExpiredLive.clear();
        }
        mIsRefreshing = true;
        if (null != adapter) {
            adapter.removeAllSections();
        }
    }
}
