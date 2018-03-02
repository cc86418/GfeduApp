package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import jc.cici.android.R;
import jc.cici.android.atom.adapter.ErrorOrFavorRecAdapter;
import jc.cici.android.atom.adapter.ErrorOrFavorTypeAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ErrorOrFavorBean;
import jc.cici.android.atom.bean.ErrorOrFavorTypeBean;
import jc.cici.android.atom.bean.NewErrorOrFavorBean;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.EveryDayTestAc;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
import jc.cici.android.atom.view.TopMiddlePopup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 错题集activity
 * Created by atom on 2018/1/5.
 */

public class ErrorSetActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题布局
    @BindView(R.id.titleName_layout)
    RelativeLayout titleName_layout;
    // 标题
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    // viewPager
    @BindView(R.id.courseRecyclerView)
    RecyclerView courseRecyclerView;
    @BindView(R.id.line_img)
    ImageView line_img;
    // 标题文字
    @BindView(R.id.courseName_txt)
    TextView courseName_txt;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    // recyclerView
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.empty_img)
    ImageView empty_img;

    private ArrayList<ErrorOrFavorTypeBean.ErrorOrFavorType> couresDatas = new ArrayList<>();
    private ErrorOrFavorTypeAdapter errorTypeAdapter;

    private LinearLayoutManager linearLayoutManager;
    private ErrorOrFavorRecAdapter adapter;
    private ArrayList<ErrorOrFavorBean.ErrorOrFavor> datas = new ArrayList<>();

    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    // 1:我的收藏 2：我的错题
    private int searchType;
    private int quesType;
    // 当前页码
    private int pageIndex = 1;
    private Dialog mDialog;
    // 二级项目列表
    private ArrayList<TikuHomeBean.TikuProject.Node> nodeList = new ArrayList<>();
    private TopMiddlePopup topMiddlePopup;
    private boolean isLoading;
    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_errorset;
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
        projectId = getIntent().getIntExtra("projectId", 0);
        searchType = getIntent().getIntExtra("searchType", 0);
        System.out.println("projectId >>>:" + projectId);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        initData();
    }


    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body1 = commonPramas1();
        RequestBody body2 = commonPramas2();
        Observable.zip(httpPostService.getExamListInfo(body1), httpPostService.getMyQuestTypeListInfo(body2), new Func2<CommonBean<TikuHomeBean>, CommonBean<ErrorOrFavorTypeBean>, NewErrorOrFavorBean>() {
            @Override
            public NewErrorOrFavorBean call(CommonBean<TikuHomeBean> tikuHomeBeanCommonBean, CommonBean<ErrorOrFavorTypeBean> errorOrFavorBeanCommonBean) {
                return new NewErrorOrFavorBean(tikuHomeBeanCommonBean, errorOrFavorBeanCommonBean);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewErrorOrFavorBean>() {
                    @Override
                    public void onCompleted() {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(NewErrorOrFavorBean newErrorOrFavorBean) {
                        if (100 == newErrorOrFavorBean.tikuHomeBean.getCode() && 100 == newErrorOrFavorBean.errorOrFavorTypeBean.getCode()) {
                            ArrayList<TikuHomeBean.TikuProject> parentList = newErrorOrFavorBean.tikuHomeBean.getBody().getList();
                            if (null != parentList && parentList.size() > 0) {
                                for (int i = 0; i < parentList.size(); i++) {
                                    ArrayList<TikuHomeBean.TikuProject.Node> secondList = parentList.get(i).getLevelTwo();
                                    if (!"null".equals(secondList) && secondList.size() > 0) {
                                        for (int j = 0; j < secondList.size(); j++) {
                                            if (projectId == secondList.get(j).getProjectId()) {
                                                if (nodeList.size() > 0) {
                                                    nodeList.clear();
                                                }
                                                nodeList.addAll(secondList);
                                                if (1 == searchType) { // 我的收藏
                                                    // 设置标题
                                                    titleName_txt.setText(secondList.get(j).getProjectName() + "收藏");
                                                } else if (2 == searchType) {
                                                    // 设置标题
                                                    titleName_txt.setText(secondList.get(j).getProjectName() + "错题集");
                                                }
                                                break;
                                            }
                                        }

                                    }
                                }
                            }

                            // 获取内容信息
                            ArrayList<ErrorOrFavorTypeBean.ErrorOrFavorType> typeList = newErrorOrFavorBean.errorOrFavorTypeBean.getBody().getList();
                            if (null != typeList && !"null".equals(typeList) && typeList.size() > 0) {
                                if (couresDatas.size() > 0) {
                                    couresDatas.clear();
                                }
                                couresDatas.addAll(typeList);
                            }
                            errorTypeAdapter.notifyDataSetChanged();
                            if (null != couresDatas && !"null".equals(couresDatas) && couresDatas.size() > 0) {
                                quesType = couresDatas.get(0).getQues_Type();
                                courseName_txt.setText(couresDatas.get(0).getTypeName());
                                line_img.setVisibility(View.VISIBLE);
                            } else {
                                courseName_txt.setText("");
                                line_img.setVisibility(View.GONE);
                            }
                            // 获取内容
                            getContent(pageIndex);

                        } else if (100 != newErrorOrFavorBean.tikuHomeBean.getCode()) {
                            Toast.makeText(baseActivity, newErrorOrFavorBean.tikuHomeBean.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (100 != newErrorOrFavorBean.errorOrFavorTypeBean.getCode()) {
                            Toast.makeText(baseActivity, newErrorOrFavorBean.errorOrFavorTypeBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonPramas1() {

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

    private RequestBody commonPramas2() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
            // 1:我的收藏 2：我的错题
            obj.put("searchType", searchType);
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

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    private void initView() {

        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);

        LinearLayoutManager horLinearLayoutManager = new LinearLayoutManager(baseActivity);
        horLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        courseRecyclerView.setLayoutManager(horLinearLayoutManager);
        errorTypeAdapter = new ErrorOrFavorTypeAdapter(baseActivity, couresDatas);
        courseRecyclerView.setAdapter(errorTypeAdapter);

        errorTypeAdapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                errorTypeAdapter.notifyView(position);
                courseName_txt.setText(couresDatas.get(position).getTypeName());
                line_img.setVisibility(View.VISIBLE);
                quesType = couresDatas.get(position).getQues_Type();
                pageIndex = 1;
                getContent(pageIndex);
            }
        });

        // 设置刷新样式
        swipe_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipe_layout.setOnRefreshListener(this);
        swipe_layout.setRefreshing(true);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(empty_img);
        adapter = new ErrorOrFavorRecAdapter(baseActivity, datas);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent pIt = new Intent(baseActivity, EveryDayTestAc.class);
                Bundle pBundle = new Bundle();
                pBundle.putInt("paperId", 0);
                pBundle.putInt("quesId", datas.get(position).getQues_PKID());
                pBundle.putInt("searchType", 2);
                pBundle.putInt("projectId", projectId);
                pIt.putExtras(pBundle);
                baseActivity.startActivityForResult(pIt, 1);
            }
        });

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
                    boolean isRefreshing = swipe_layout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) { // 加载更多
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageIndex += 1;
                                LoadingMore(pageIndex);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    private void LoadingMore(int pageIndex) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(pageIndex);
        Observable<CommonBean<ErrorOrFavorBean>> observable = httpPostService.getMyQuestListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ErrorOrFavorBean>>() {
                    @Override
                    public void onCompleted() {
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                        Toast.makeText(baseActivity, "网络加载异常，请下拉刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ErrorOrFavorBean> errorOrFavorBeanCommonBean) {
                        if (100 == errorOrFavorBeanCommonBean.getCode()) {
                            ArrayList<ErrorOrFavorBean.ErrorOrFavor> typeList = errorOrFavorBeanCommonBean.getBody().getList();
                            if (null != typeList && typeList.size() > 0) {
                                datas.addAll(typeList);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(baseActivity, errorOrFavorBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 错题or收藏内容
     */
    private void getContent(int page) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(page);
        Observable<CommonBean<ErrorOrFavorBean>> observable = httpPostService.getMyQuestListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ErrorOrFavorBean>>() {
                    @Override
                    public void onCompleted() {
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                        Toast.makeText(baseActivity, "网络加载异常，请下拉刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ErrorOrFavorBean> errorOrFavorBeanCommonBean) {
                        if (100 == errorOrFavorBeanCommonBean.getCode()) {
                            ArrayList<ErrorOrFavorBean.ErrorOrFavor> typeList = errorOrFavorBeanCommonBean.getBody().getList();
                            if (null != typeList && typeList.size() > 0) {
                                if (datas.size() > 0) {
                                    datas.clear();
                                }
                                datas.addAll(typeList);
                            } else {
                                couresDatas.clear();
                                datas.clear();
                                pageIndex =1;
                                courseName_txt.setText("");
                                line_img.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(baseActivity, errorOrFavorBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonPramas(int pageIndex) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
            // 1:我的收藏 2：我的错题
            obj.put("searchType", searchType);
            obj.put("quesType", quesType);
            obj.put("pageIndex", pageIndex);
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

    @OnClick({R.id.back_layout,
            R.id.titleName_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.titleName_layout: // 标题布局
                int screenWidth = ToolUtils.getScreenPixels(baseActivity).widthPixels;
                int screenHeight = ToolUtils.getScreenPixels(baseActivity).heightPixels;
                topMiddlePopup = new TopMiddlePopup(baseActivity, screenWidth, screenHeight, onItemClickListener, nodeList);
                topMiddlePopup.show(titleName_txt);
                break;

        }
    }


    private BaseRecycleerAdapter.OnItemClickListener onItemClickListener = new BaseRecycleerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            projectId = nodeList.get(position).getProjectId();
            if (1 == searchType) { // 我的收藏
                // 设置标题
                titleName_txt.setText(nodeList.get(position).getProjectName() + "收藏");
            } else if (2 == searchType) {
                // 设置标题
                titleName_txt.setText(nodeList.get(position).getProjectName() + "错题集");
            }
            pageIndex = 1;
            initData();
            topMiddlePopup.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(true);
        datas.clear();
        pageIndex = 1;
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (2 == resultCode) {
            swipe_layout.setRefreshing(true);
            couresDatas.clear();
            datas.clear();
            pageIndex = 1;
            initData();
        }
    }
}
