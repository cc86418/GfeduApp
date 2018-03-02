package jc.cici.android.atom.ui.courselist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.CourseSearchRecyclerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Product;
import jc.cici.android.atom.bean.ProductInfo;
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
 * 课程搜索acitivity
 * Created by atom on 2017/8/7.
 */

public class SearchCourseActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 父类布局
    @BindView(R.id.parentLayout)
    RelativeLayout parentLayout;
    // 搜索框
    @BindView(R.id.sc_layout)
    RelativeLayout sc_layout;
    // 查询view
    @BindView(R.id.editSearch_Txt)
    EditText editSearch_Txt;
    // 取消文字
    @BindView(R.id.cancel_Txt)
    TextView cancel_Txt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;
    // 当前页码
    private int page = 1;
    // 搜索关键字
    private String keyWord;
    private ArrayList<Product> productList;
    private CourseSearchRecyclerAdapter adapter;
    private boolean isLoading;
    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {

        parentLayout.setBackgroundColor(Color.parseColor("#f5f5f5"));
        editSearch_Txt.setHint("请输入查询关键字");
        sc_layout.setBackgroundResource(R.drawable.bg_search_edit);
        emptyView.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    if (!isLoading) { // 加载更多
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Loading();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

    }

    /**
     * 加载更多
     */
    private void Loading() {
        page += 1;
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {

            obj.put("pageIndex", page);
            obj.put("pageSize", 10);
            obj.put("keywords", keyWord);
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
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
                                    Toast.makeText(baseActivity, "网络请求异常，请重新查询", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<ProductInfo> productInfoCommonBean) {
                                if (100 == productInfoCommonBean.getCode()) {
                                    ArrayList<Product> moreList = productInfoCommonBean.getBody().getList();
                                    if (null != moreList && moreList.size() > 0) {
                                        productList.addAll(moreList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.changeState(2);
                                    }
                                } else {
                                    Toast.makeText(baseActivity, productInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.cancel_Txt})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancel_Txt: // 取消按钮
                if ("提交".equals(cancel_Txt.getText().toString())) { // 提交按钮
                    if (NetUtil.isMobileConnected(this)) {
                        initData();
                    } else {
                        Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else { // 取消按钮监听
                    baseActivity.finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求获取查询数据
     */
    private void initData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {

            obj.put("pageIndex", page);
            obj.put("pageSize", 10);
            obj.put("keywords", keyWord);
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("keyword", keyWord);
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
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
                                    Toast.makeText(baseActivity, "网络请求异常，请重新查询", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<ProductInfo> productInfoCommonBean) {
                                cancel_Txt.setText("取消");
                                if (100 == productInfoCommonBean.getCode()) {
                                    productList = productInfoCommonBean.getBody().getList();
                                    if (null != productList && productList.size() > 0) {
                                        emptyView.setVisibility(View.GONE);
                                        adapter = new CourseSearchRecyclerAdapter(baseActivity, productList);
                                        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                        animationAdapter.setDuration(1000);
                                        recyclerView.setAdapter(animationAdapter);
                                        // 点击事件
                                        adapter.setClickListener(new CourseSearchRecyclerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position) {
                                                int courseType = productList.get(position).getType();
                                                if (5 == courseType) { // 套餐
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("Product_PKID", productList.get(position).getProduct_PKID());
                                                    baseActivity.openActivity(OnlineCourseDetailsActivity.class, bundle);
                                                } else if (2 == courseType) { // 班级
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("Product_PKID", productList.get(position).getProduct_PKID());
                                                    baseActivity.openActivity(OnlineCourseDetailsAloneActivityTwo.class, bundle);
                                                }
                                            }
                                        });
                                    } else {
                                        productList = new ArrayList<>();
                                        adapter = new CourseSearchRecyclerAdapter(baseActivity, productList);
                                        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                        animationAdapter.setDuration(1000);
                                        recyclerView.setAdapter(animationAdapter);
                                        emptyView.setVisibility(View.VISIBLE);
                                        Toast.makeText(baseActivity, "暂无该课程", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(baseActivity, productInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );

    }


    @OnTextChanged(R.id.editSearch_Txt)
    void afterTextChanged(CharSequence text) {
        if (text.length() > 0) {
            keyWord = text.toString();
            System.out.println("keyWord >>>:" + keyWord);
            cancel_Txt.setText("提交");
        } else {
            page = 1;
            cancel_Txt.setText("取消");
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

