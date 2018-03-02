package jc.cici.android.atom.ui.courselist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.CourseSearchRecyclerAdapter;
import jc.cici.android.atom.adapter.FristRecyclerViewAdapter;
import jc.cici.android.atom.adapter.SubjectRecyclerViewAdapter;
import jc.cici.android.atom.adapter.TypeRecyclerViewAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.ChildEntity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Parent;
import jc.cici.android.atom.bean.ParentEntity;
import jc.cici.android.atom.bean.Product;
import jc.cici.android.atom.bean.ProductInfo;
import jc.cici.android.atom.bean.SelectBean;
import jc.cici.android.atom.bean.TypeBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.MyGridLayoutManager;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 全部课程列表
 * Created by aom on 2017/8/8.
 */

public class AllCourseAcitivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
    // 热门
    @BindView(R.id.price_txt)
    TextView price_txt;
    // 热门排序
    @BindView(R.id.price_img)
    ImageView price_img;
    // 人气
    @BindView(R.id.hotMan_txt)
    TextView hotMan_txt;
    // 人气排序
    @BindView(R.id.hotMan_img)
    ImageView hotMan_img;
    // 体验课
    @BindView(R.id.experience_txt)
    TextView experience_txt;
    // 体验课排序
    @BindView(R.id.experience_img)
    ImageView experience_img;
    // 筛选
    @BindView(R.id.select_txt)
    TextView select_txt;
    // 筛选图片
    @BindView(R.id.select_img)
    ImageView select_img;
    // 头布局
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // recyclerView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // 布局管理器
    private LinearLayoutManager linearLayoutManager;
    // 进度条对话框
    private Dialog dialog;
    // 数据适配器
    private CourseSearchRecyclerAdapter adapter;
    private boolean isLoading;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 第一层内容刷新
                    projectId = (int) msg.obj;
                    projectIdArr = String.valueOf(projectId);
                    page = 0;
                    getRefreshData(page);
                    RefreshSlectData();
                    String str = "";
                    for (int i = 0; i < firstList.size(); i++) {
                        if (projectId == firstList.get(i).getProjectId()) {
                            str = firstList.get(i).getProjectName();
                            break;
                        }
                    }
                    title_txt.setText(str);
                    break;
                case 1: // 第二层内容刷新
                    ArrayList<Integer> arrType = (ArrayList<Integer>) msg.obj;
                    classTypeArr = "";
                    if (null != arrType && arrType.size() > 0) {
                        for (int i = 0; i < arrType.size(); i++) {
                            classTypeArr += String.valueOf(arrType.get(i)) + ",";
                        }
                    }
                    if (!"".equals(classTypeArr)) {
                        classTypeArr = classTypeArr.substring(0, classTypeArr.lastIndexOf(","));
                        if ("0".equals(classTypeArr)) { // 全部按钮被取消选中情况
                            classTypeArr = "";
                        }
                    }
                    page = 0;
                    getRefreshData(page);
                    RefreshSlectData();
                    break;
                case 2: // 第三层内容刷新
                    ArrayList<Integer> projectID = (ArrayList<Integer>) msg.obj;
                    projectIdArr = "";
                    if (null != projectID && projectID.size() > 0) {
                        for (int i = 0; i < projectID.size(); i++) {
                            projectIdArr += String.valueOf(projectID.get(i)) + ",";
                        }
                    }
                    if (!"".equals(projectIdArr)) {
                        projectIdArr = projectIdArr.substring(0, projectIdArr.lastIndexOf(","));
                        if ("0".equals(projectIdArr)) { // 全部按钮被取消选中情况
                            projectIdArr = String.valueOf(projectId);
                        }
                    }
                    page = 0;
                    getRefreshData(page);
                    RefreshSlectData();
                    break;
            }
        }
    };
    // 当前项目id
    private int projectId;
    // 当前项目名称
    private String titltle;
    // 所有课程产品列表
    private ArrayList<Product> productList;
    // 当前页
    private int page = 1;
    // 1：推荐排序 2：价格 3：人气 4：热门
    private int orderType;
    // 0：降序 1：升序
    private int direction = 0;
    // 项目ID 多个逗号隔开
    private String projectIdArr = "";
    // 班型类型：1:面授 2:在线  4:直播多个逗号隔开
    private String classTypeArr = "";

    // 学科头布局
    private RelativeLayout studyLayout;
    // 学科箭头图片
    private ImageView studyUpOrDownImg;
    // 学科筛选列表
    private RecyclerView subjectRecyclerView;

    // 类型头布局
    private RelativeLayout typeLayout;
    // 类型箭头图片
    private ImageView typeUpOrDownImg;
    // 类型筛选列表
    private RecyclerView typeRecyclerView;

    // 科目头布局
    private RelativeLayout subjectLayout;
    // 科目箭头图片
    private ImageView subjectUpOrDownImg;
    // 科目筛选列表
    private RecyclerView studyRecyclerView;

    // 分类筛选数据
    private ArrayList<ParentEntity> parentList;
    private ArrayList<Parent> firstList = new ArrayList<>();
    // 学科适配器
    private FristRecyclerViewAdapter fristAdapter;
    // 类型
    private ArrayList<TypeBean> typeList = new ArrayList<>();
    // 类型适配器
    private TypeRecyclerViewAdapter typeAdapter;
    // 学科
    private ArrayList<ChildEntity> childList = new ArrayList<>();
    // 学科适配器
    private SubjectRecyclerViewAdapter subjectAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_allcourse;
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
        // 获取当前projectId
        projectId = getIntent().getIntExtra("CT_ID", 0);
        projectIdArr = String.valueOf(projectId);
        // 项目名称
        titltle = getIntent().getStringExtra("CT_NAME");
        // 获取当前排序类型
        orderType = getIntent().getIntExtra("orderType", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 添加数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            addData(page);
            // 获取筛选列表信息
            getSlectData();
        } else {
            dialog.dismiss();
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取数据
     *
     * @param page
     */
    private void addData(final int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page, orderType, direction, projectIdArr, classTypeArr);
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

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText(titltle);
        noteMore_Btn.setBackgroundResource(R.drawable.icon_note_search);
        noteMore_Btn.setVisibility(View.VISIBLE);
        search_Btn.setBackgroundResource(R.drawable.icon_note_more);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        // 设置刷新样式
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        showProcessDialog(baseActivity, R.layout.loading_process_dialog_color);

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
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
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
        typeList.add(0, new TypeBean(0, "全部", true));
        typeList.add(1, new TypeBean(1, "面授", false));
        typeList.add(2, new TypeBean(2, "在线", false));
        typeList.add(3, new TypeBean(4, "直播", false));

    }

    /**
     * 加载更多
     *
     * @param page
     */
    private void loadingMore(int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page, orderType, direction, projectIdArr, classTypeArr);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
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

    /**
     * 刷新数据
     *
     * @param page
     */
    private void refreshData(int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page, orderType, direction, projectIdArr, classTypeArr);
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
                                    ArrayList<Product> refreshList = productInfoCommonBean.getBody().getList();
                                    if (null != refreshList && refreshList.size() > 0) {
                                        productList.clear();
                                        productList.addAll(refreshList);
                                        adapter.changeState(1);
                                        swipeRefreshLayout.setRefreshing(false);
                                    } else {
                                        swipeRefreshLayout.setRefreshing(false);
                                        adapter.changeState(2);
                                        Toast.makeText(baseActivity, "暂无内容", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(baseActivity, productInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 公共请求参数
     *
     * @param page         当前页
     * @param orderType    排序内容 1：推荐排序 2：价格 3：人气 4：热门
     * @param direction    升序or降序 0：降序 1：升序
     * @param projectIdArr 项目ID 多个逗号隔开
     * @param classTypeArr 班型类型 1:面授 2:在线  4:直播多个逗号隔开
     * @return
     */
    private RequestBody commRequest(int page, int orderType, int direction, String projectIdArr, String classTypeArr) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("pageIndex", page);
            obj.put("pageSize", 10);
            obj.put("orderType", orderType);
            obj.put("direction", direction);
            obj.put("ProjectIdArr", projectIdArr);
            obj.put("classType_TypeArr", classTypeArr);
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    @OnClick({R.id.back_layout, R.id.noteMore_Btn, R.id.price_txt,
            R.id.hotMan_txt, R.id.experience_txt, R.id.select_txt, R.id.select_img})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回布局
                baseActivity.finish();
                break;
            case R.id.noteMore_Btn: // 搜索按钮
                baseActivity.openActivity(SearchCourseActivity.class);
                break;
            case R.id.price_txt: // 价格排序
                page = 1;
                orderType = 2;
                if (0 == direction) { // 如果当前为降序
                    direction = 1; // 设置为升序
                } else { // 如果当前排序为升序
                    direction = 0; // 设置为降序
                }
                getRefreshData(page);
                break;
            case R.id.hotMan_txt: // 人气排序
                page = 1;
                orderType = 3;
                if (0 == direction) { // 如果当前为降序
                    direction = 1; // 设置为升序
                } else { // 如果当前排序为升序
                    direction = 0; // 设置为降序
                }
                getRefreshData(page);
                break;
            case R.id.experience_txt: // 体验课排序

                break;
            case R.id.select_txt: // 筛选按钮
                // 创建dialog
                createDialog();
                break;
            case R.id.select_img: // 筛选图片
                // 创建dialog
                createDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 创建dialog
     */
    private void createDialog() {
        // 弹出对话框
        final Dialog mDialog = new Dialog(baseActivity,
                R.style.nextTaskStyle);
        mDialog.setContentView(R.layout.dialog_select_view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.show();

        // 学科
        studyLayout = (RelativeLayout) mDialog.findViewById(R.id.studyLayout);
        studyUpOrDownImg = (ImageView) mDialog.findViewById(R.id.studyUpOrDownImg);
        studyRecyclerView = (RecyclerView) mDialog.findViewById(R.id.studyRecyclerView);
        studyRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        studyRecyclerView.setNestedScrollingEnabled(false);
        // 类型
        typeLayout = (RelativeLayout) mDialog.findViewById(R.id.typeLayout);
        typeUpOrDownImg = (ImageView) mDialog.findViewById(R.id.typeUpOrDownImg);
        typeRecyclerView = (RecyclerView) mDialog.findViewById(R.id.typeRecyclerView);
        typeRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        typeRecyclerView.setNestedScrollingEnabled(false);
        // 科目
        subjectLayout = (RelativeLayout) mDialog.findViewById(R.id.subjectLayout);
        subjectUpOrDownImg = (ImageView) mDialog.findViewById(R.id.subjectUpOrDownImg);
        subjectRecyclerView = (RecyclerView) mDialog.findViewById(R.id.subjectRecyclerView);
        subjectRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        subjectRecyclerView.setNestedScrollingEnabled(false);

        Button resetBtn = (Button) mDialog.findViewById(R.id.resetBtn);
        final Button finishBtn = (Button) mDialog.findViewById(R.id.finishBtn);

        fristAdapter = new FristRecyclerViewAdapter(baseActivity, firstList, handler);
        studyRecyclerView.setAdapter(fristAdapter);


        typeAdapter = new TypeRecyclerViewAdapter(baseActivity, typeList, handler);
        typeRecyclerView.setAdapter(typeAdapter);

        subjectAdapter = new SubjectRecyclerViewAdapter(baseActivity, childList, handler);
        subjectRecyclerView.setAdapter(subjectAdapter);
        // 学科收缩监听
        setOnClick(studyLayout, studyUpOrDownImg, studyRecyclerView);
        // 类型收缩监听
        setOnClick(typeLayout, typeUpOrDownImg, typeRecyclerView);
        // 科目收缩监听
        setOnClick(subjectLayout, subjectUpOrDownImg, subjectRecyclerView);
        // 重置按钮
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                projectIdArr = String.valueOf(projectId);
                classTypeArr = "";
                page = 0;
                getRefreshData(page);
                for (int i = 0; i < firstList.size(); i++) {
                    Parent parent = firstList.get(i);
                    if (projectId == parent.getProjectId()) {
                        parent.setSelected(true);
                        str = parent.getProjectName();
                    } else {
                        parent.setSelected(false);
                    }
                    firstList.set(i, parent);
                }
                for (int i = 0; i < typeList.size(); i++) {
                    if (typeList.get(i).getTypeID() == 0) {
                        TypeBean typeBean = typeList.get(i);
                        typeBean.setChecked(true);
                        typeList.set(i, typeBean);
                    } else {
                        TypeBean typeBean = typeList.get(i);
                        typeBean.setChecked(false);
                        typeList.set(i, typeBean);
                    }

                }
                for (int i = 0; i < childList.size(); i++) {
                    ChildEntity childEntity = childList.get(i);
                    if (i == 0) {
                        childEntity.setmChecked(true);
                    } else {
                        childEntity.setmChecked(false);
                    }
                    childList.set(i, childEntity);
                }
                if (null != subjectAdapter) {
                    subjectAdapter.notifyDataSetChanged();
                }
                title_txt.setText(str);
                mDialog.dismiss();
            }
        });
        // 完成按钮
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
            }
        });
    }

    private void setOnClick(RelativeLayout layout, final ImageView img, final RecyclerView recyclerView) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    img.setBackgroundResource(R.drawable.icon_down);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    img.setBackgroundResource(R.drawable.icon_up);
                }
            }
        });

    }

    /**
     * 请求加载筛选数据
     */
    private void getSlectData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(0 + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<SelectBean>> observable = httpPostService.getSelectProjectListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SelectBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<SelectBean> selectBeanCommonBean) {
                                if (100 == selectBeanCommonBean.getCode()) {
                                    parentList = selectBeanCommonBean.getBody().getList();
                                    if (null != parentList && parentList.size() > 0) {
                                        for (ParentEntity parentEntity : parentList) {
                                            Parent parent = parentEntity.getLevelOne();
                                            firstList.add(parent);
                                            if (0 == projectId) {
                                                childList.addAll(parentEntity.getLevelTwo());
                                            } else if (projectId == parent.getProjectId()) {
                                                childList.addAll(parentEntity.getLevelTwo());
                                            }
                                        }
                                        if (projectId == 0) {
                                            firstList.add(0, new Parent(0, "全部", 4, true));
                                            childList.add(0, new ChildEntity(0, "全部", 4, true));
                                        } else {
                                            firstList.add(0, new Parent(0, "全部", 4, false));
                                            for (int i = 0; i < firstList.size(); i++) {
                                                if (projectId == firstList.get(i).getProjectId()) {
                                                    Parent parent = firstList.get(i);
                                                    parent.setSelected(true);
                                                    firstList.set(i, parent);
                                                    break;
                                                }
                                            }
                                            for (int i = 0; i < childList.size(); i++) {
                                                if (projectId == childList.get(i).getProjectId()) {
                                                    ChildEntity childEntity = childList.get(i);
                                                    childEntity.setmChecked(true);
                                                    childList.set(i, childEntity);
                                                    break;
                                                }
                                            }
                                            childList.add(0, new ChildEntity(0, "全部", 4, true));
                                        }
                                    }
                                } else {
                                    Toast.makeText(baseActivity, selectBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 请求加载筛选数据
     */
    private void RefreshSlectData() {
        firstList.clear();
        childList.clear();
        for (ParentEntity parentEntity : parentList) {
            Parent parent = parentEntity.getLevelOne();
            firstList.add(parent);
            if (0 == projectId) {
                childList.addAll(parentEntity.getLevelTwo());
            } else if (projectId == parent.getProjectId()) {
                childList.addAll(parentEntity.getLevelTwo());
            }
        }

        if (projectId == 0 && "".equals(projectIdArr)) {
            firstList.add(0, new Parent(0, "全部", 4, true));
            childList.add(0, new ChildEntity(0, "全部", 4, true));
        } else {
            firstList.add(0, new Parent(0, "全部", 4, false));
            boolean flag = false;
            for (int i = 0; i < childList.size(); i++) {
                if (childList.get(i).ismChecked()) {
                    flag = true;
                    continue;
                }
            }

            if (flag) { // 表示有子项目选中
                childList.add(0, new ChildEntity(0, "全部", 4, false));
            } else { // 全部按钮选中
                childList.add(0, new ChildEntity(0, "全部", 4, true));
            }

        }
        if (null != subjectAdapter) {
            subjectAdapter.notifyDataSetChanged();
        }

    }


    /**
     * 获取刷新数据
     *
     * @param page
     */
    private void getRefreshData(int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        final HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        showProcessDialog(baseActivity, R.layout.loading_process_dialog_color);
        RequestBody body = commRequest(page, orderType, direction, projectIdArr, classTypeArr);
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
                                    if (emptyView.getVisibility() == View.VISIBLE) {
                                        emptyView.setVisibility(View.GONE);
                                    }
                                    if (null != productList && productList.size() > 0) {
                                        productList.clear();
                                    }
                                    ArrayList<Product> refreshList = productInfoCommonBean.getBody().getList();
                                    if (null != refreshList && refreshList.size() > 0) {
                                        productList.addAll(refreshList);
                                        adapter.notifyDataSetChanged();
                                        if (orderType == 2) { // 价格排序
                                            setAscOrDescImg(price_img);
                                        } else if (orderType == 3) { // 人气排序
                                            setAscOrDescImg(hotMan_img);
                                        }
                                    } else {
                                        adapter.changeState(1);
                                        emptyView.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(baseActivity, productInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    private void setAscOrDescImg(ImageView view) {
        if (direction == 0) { // 降序
            view.setBackgroundResource(R.drawable.icon_desc);
        } else { // 升序
            view.setBackgroundResource(R.drawable.icon_asc);
        }
    }
}
