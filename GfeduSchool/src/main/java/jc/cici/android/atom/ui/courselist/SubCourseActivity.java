package jc.cici.android.atom.ui.courselist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.SubCourseRecAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.Ads;
import jc.cici.android.atom.bean.AdsInfo;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Product;
import jc.cici.android.atom.bean.ProductInfo;
import jc.cici.android.atom.bean.SubCourseBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 分类课程列表
 * Created by atom on 2017/7/31.
 */

public class SubCourseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
    // 搜索更多布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 搜索按钮
    @BindView(R.id.noteMore_Btn)
    Button noteMore_Btn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 隐藏布局
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 刷新头布局
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    // recyclerView 列表
    @BindView(R.id.swipe_target)
    RecyclerView swipe_target;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // 获取传递标题
    private String title;
    // 项目id
    private int projectId;
    // 当前默认页数
    private int page = 1;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;
    private ArrayList<Ads> adsList;
    private ArrayList<Product> recommendList;
    private ArrayList<Product> hotProducts;
    private SubCourseRecAdapter adapter;
    private boolean isLoading;
    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subcourse;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        // 获取传递参数
        title = getIntent().getStringExtra("CT_NAME");
        // 获取传递项目id
        projectId = getIntent().getIntExtra("CT_ID", 0);
        System.out.println("projectId >>>:" + projectId + ",title >>>:" + title);
        // 测试
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 添加数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            addData(page);
        } else {
            dialog.dismiss();
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 网络请求获取分项目列表内容
     */
    private void addData(int page) {
        // 获取信息
        getReCommendInfo(page);
    }

    /**
     * 获取推荐信息
     */
    private void getReCommendInfo(int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page);
        Observable<CommonBean<SubCourseBean>> observable = httpPostService.getProductListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubCourseBean>>() {
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
                            public void onNext(CommonBean<SubCourseBean> subCourseBeanCommonBean) {
                                if (100 == subCourseBeanCommonBean.getCode()) {
                                    if (emptyView.getVisibility() == View.VISIBLE) {
                                        emptyView.setVisibility(View.GONE);
                                    }
                                    // 广告列表
                                    adsList = subCourseBeanCommonBean.getBody().getAdsList();
                                    // 获取我的推荐
                                    recommendList = subCourseBeanCommonBean.getBody().getRecommentList();
                                    hotProducts = subCourseBeanCommonBean.getBody().getHotList();
                                    if (null != hotProducts && hotProducts.size() > 0) {
                                        if (emptyView.getVisibility() == View.VISIBLE) {
                                            emptyView.setVisibility(View.GONE);
                                        }
                                        if (null == adapter) {
                                            adapter = new SubCourseRecAdapter(baseActivity, hotProducts, adsList, recommendList, projectId, title);
                                            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                            animationAdapter.setDuration(1000);
                                            swipe_target.setAdapter(animationAdapter);
                                            setHeader(swipe_target);
                                        }

                                        adapter.setClickListener(new SubCourseRecAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position) {
                                                int courseType = hotProducts.get(position - 1).getType();
                                                if (5 == courseType) { // 套餐
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("Product_PKID", hotProducts.get(position - 1).getProduct_PKID());
                                                    bundle.putInt("Class_OutlineFreeState", hotProducts.get(position - 1).getProduct_OutlineFreeState());
                                                    baseActivity.openActivity(OnlineCourseDetailsActivity.class, bundle);
                                                } else if (2 == courseType) { // 班级
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("Product_PKID", hotProducts.get(position - 1).getProduct_PKID());
                                                    bundle.putInt("Class_OutlineFreeState", hotProducts.get(position - 1).getProduct_OutlineFreeState());
                                                    baseActivity.openActivity(OnlineCourseDetailsAloneActivityTwo.class, bundle);
                                                }
                                            }
                                        });

                                    } else {
                                        emptyView.setVisibility(View.VISIBLE);
                                        Toast.makeText(baseActivity, "暂无热门内容", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
                                    Toast.makeText(baseActivity, subCourseBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 添加头布局
     *
     * @param view
     */
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.item_head, view, false);
        adapter.setHeaderView(header);
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
     * 公共请求参数
     *
     * @param page
     * @return
     */
    private RequestBody commRequest(int page) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("projectId", projectId);
            // 当前页
            obj.put("pageIndex", page);
            // 默认显示条数
            obj.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    /**
     * 初始化视图操作
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText(title);
        noteMore_Btn.setBackgroundResource(R.drawable.icon_note_search);
        noteMore_Btn.setVisibility(View.VISIBLE);
        search_Btn.setBackgroundResource(R.drawable.icon_note_more);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        // 设置刷新样式
        swipeToLoadLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipeToLoadLayout.setOnRefreshListener(this);
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        swipe_target.setLayoutManager(linearLayoutManager);
        showProcessDialog(baseActivity, R.layout.loading_process_dialog_color);


        swipe_target.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = swipeToLoadLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) { // 加载更多
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page += 1;
                                loadingMore(page);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                refreshData(page);
            }
        }, 2000);
    }

    private void refreshData(int page) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page);
        Observable<CommonBean<SubCourseBean>> observable = httpPostService.getProductListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubCourseBean>>() {
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
                            public void onNext(CommonBean<SubCourseBean> subCourseBeanCommonBean) {
                                if (100 == subCourseBeanCommonBean.getCode()) {
                                    if (emptyView.getVisibility() == View.VISIBLE) {
                                        emptyView.setVisibility(View.GONE);
                                    }
                                    if (null != hotProducts && hotProducts.size() > 0) {
                                        hotProducts.clear();
                                    }
                                    ArrayList<Product> refreshList = subCourseBeanCommonBean.getBody().getHotList();
                                    if (null != refreshList && refreshList.size() > 0) {
                                        hotProducts.addAll(refreshList);
                                        adapter.changeState(1);
                                        swipeToLoadLayout.setRefreshing(false);
                                    } else {
                                        adapter.changeState(3);
                                        swipeToLoadLayout.setRefreshing(false);
                                        Toast.makeText(baseActivity, "暂无热门内容", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    swipeToLoadLayout.setRefreshing(false);
                                    emptyView.setVisibility(View.VISIBLE);
                                    Toast.makeText(baseActivity, subCourseBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }


    @OnClick({R.id.back_layout, R.id.noteMore_Btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.noteMore_Btn: // 搜索按钮
                baseActivity.openActivity(SearchCourseActivity.class);
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
        if (null != adapter) {
            adapter.stopScheduledExecutorService();
        }
    }


    /**
     * 加载更多
     *
     * @param page
     */
    private void loadingMore(int page) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("projectIdArr", projectId);
            // 当前页
            obj.put("pageIndex", page);
            // 默认显示条数
            obj.put("pageSize", 10);
            obj.put("ordertype", 4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<ProductInfo>> observable = httpPostService.getSearchProductListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<ProductInfo>>() {
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
                            public void onNext(CommonBean<ProductInfo> productInfoCommonBean) {
                                if (100 == productInfoCommonBean.getCode()) {
                                    if (emptyView.getVisibility() == View.VISIBLE) {
                                        emptyView.setVisibility(View.GONE);
                                    }
                                    ArrayList<Product> moreList = productInfoCommonBean.getBody().getList();
                                    if (null != moreList && moreList.size() > 0) {
                                        hotProducts.addAll(moreList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.changeState(2);
                                    }
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
                                    Toast.makeText(baseActivity, productInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

}
