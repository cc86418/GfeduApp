package jc.cici.android.atom.ui.live;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.LiveListAdapter;
import jc.cici.android.atom.adapter.OneRecyclerViewAdapter;
import jc.cici.android.atom.adapter.RePlayAdapter;
import jc.cici.android.atom.adapter.SecondRecAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LiveCallBack;
import jc.cici.android.atom.bean.LiveProduct;
import jc.cici.android.atom.bean.LiveSelectBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyFootRecyclerView;
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
 * 直播列表页
 * Created by atom on 2017/11/10.
 */

public class LiveListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
    // 时间
    @BindView(R.id.time_txt)
    TextView time_txt;
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
    EmptyFootRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // 布局管理器
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<LiveProduct.Live> data = new ArrayList<>();
    private LiveListAdapter adapter;

    // 学科头布局
    private RelativeLayout studyLayout;
    // 学科箭头图片
    private ImageView studyUpOrDownImg;
    // 学科筛选列表
    private RecyclerView subjectRecyclerView;

    // 科目头布局
    private RelativeLayout subjectLayout;
    // 科目箭头图片
    private ImageView subjectUpOrDownImg;
    // 科目筛选列表
    private RecyclerView studyRecyclerView;

    // 回放头布局
    private RelativeLayout typeLayout;
    // 回放箭头图片
    private ImageView typeUpOrDownImg;
    // 回放筛选列表
    private RecyclerView typeRecyclerView;

    // 所有筛选列表内容
    ArrayList<LiveSelectBean.SelectList> allSelList;
    // 筛选第一层(学科)
    private ArrayList<LiveSelectBean.SelectList.OneModel> fristSelList = new ArrayList<>();
    // 学科适配器
    private OneRecyclerViewAdapter fristAdapter;
    // 筛选第二层(科目)
    private ArrayList<LiveSelectBean.SelectList.TwoModel> secodSelList = new ArrayList<>();
    // 科目适配器
    private SecondRecAdapter secondAdapter;
    // 筛选第三层(有无回放)
    private ArrayList<LiveCallBack> thridSelList = new ArrayList<>();
    // 有无回放适配器
    private RePlayAdapter rePlayAdapter;
    // 当前项目id
    private int projectId;
    // 当前项目名称
    private String title;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id集合
    private String projectIdList = "0";
    // 当前页
    private int page = 1;
    // 1：单直播 2：系列直播 10：最新直播 20：热门直播 40 前期回顾
    private int searchType;
    // 2:无回放 1：有回放 0:全部
    private int isPlayBack;
    // 参数传入的类型
    private int orgSearchType;
    // 排序类型：1：时间 2：人气
    private int sort = 1;
    private boolean isLoading;
    // 0：降序 1：升序
    private int direction = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 学科
                    projectId = (int) msg.obj;
                    projectIdList = String.valueOf(projectId);
                    RefreshSlectData();
                    String str = "";
                    for (int i = 0; i < fristSelList.size(); i++) {
                        if (projectId == fristSelList.get(i).getProjectId()) {
                            str = fristSelList.get(i).getProjectName();
                            break;
                        }
                    }
                    title_txt.setText(str);
                    break;
                case 1: // 具体科目
                    ArrayList<Integer> projectID = (ArrayList<Integer>) msg.obj;
                    projectIdList = "";
                    if (null != projectID && projectID.size() > 0) {
                        for (int i = 0; i < projectID.size(); i++) {
                            projectIdList += String.valueOf(projectID.get(i)) + ",";
                        }
                    }
                    if (!"".equals(projectIdList)) {
                        projectIdList = projectIdList.substring(0, projectIdList.lastIndexOf(","));
                        if ("0".equals(projectIdList)) { // 全部按钮被取消选中情况
                            projectIdList = String.valueOf(projectId);
                        }
                    }
                    RefreshSlectData();
                    break;
                case 2: // 有无回放
                    isPlayBack = (int) msg.obj;
                    break;
            }
        }
    };

    private void RefreshSlectData() {

        fristSelList.clear();
        secodSelList.clear();
        for (LiveSelectBean.SelectList selList : allSelList) {
            LiveSelectBean.SelectList.OneModel oneModel = selList.getLevelOne();
            fristSelList.add(oneModel);
            if (0 == projectId) {
                secodSelList.addAll(selList.getLevelTwo());
            } else if (projectId == oneModel.getProjectId()) {
                secodSelList.addAll(selList.getLevelTwo());
            }
        }

        if (projectId == 0) {
            fristSelList.add(0, new LiveSelectBean.SelectList.OneModel(0, "全部", 4, true));
            secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 4, true));
            for (int i = 0; i < secodSelList.size(); i++) {
                if (i != 0 && secodSelList.get(i).isSelect()) {
                    secodSelList.get(i).setSelect(false);
                }
            }
            for (int i = 0; i < thridSelList.size(); i++) {
                if (i == 0) {
                    thridSelList.get(i).setSelect(true);
                } else {
                    thridSelList.get(i).setSelect(false);
                }
            }
        } else {
            fristSelList.add(0, new LiveSelectBean.SelectList.OneModel(0, "全部", 4, false));
            boolean flag = false;
            for (int i = 0; i < secodSelList.size(); i++) {
                if (secodSelList.get(i).isSelect()) {
                    flag = true;
                    continue;
                }
            }

            if (flag) { // 表示有子项目选中
                secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 4, false));
            } else { // 全部按钮选中
                secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 4, true));
            }

        }
        if (null != secondAdapter) {
            secondAdapter.notifyDataSetChanged();
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_livelist;
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
        projectId = getIntent().getIntExtra("projectId", 0);
        // 类型转换
        projectIdList = String.valueOf(projectId);
        // 项目名称
        title = getIntent().getStringExtra("title");
        // 类型
        searchType = getIntent().getIntExtra("searchType", 0);
        // 保留原始传递类型
        orgSearchType = searchType;
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();

    }

    private void initData(final int page) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commParam(page);
        Observable<CommonBean<LiveProduct>> observable = httpPostService.getLiveListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveProduct>>() {
                    @Override
                    public void onCompleted() {

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(baseActivity, "网络加载异常，请下拉刷新", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<LiveProduct> liveListBeanLiveProduct) {
                        if (100 == liveListBeanLiveProduct.getCode()) {
                            ArrayList<LiveProduct.Live> list = liveListBeanLiveProduct.getBody().getLiveList();
                            if (null != list && list.size() > 0) {
                                data.addAll(list);
                            }
                            adapter.notifyDataSetChanged();
                            if (sort == 1) { // 时间排序
                                setAscOrDescImg(price_img);
                            } else if (sort == 2) { // 人气排序
                                setAscOrDescImg(hotMan_img);
                            }
                            // 点击事件
                            adapter.setClickListener(new LiveListAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    // 判断是系列直播还是正常直播
                                    int courseType = data.get(position).getClass_Form();
                                    if (1 == courseType) { // 系列直播
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("Class_PKID", data.get(position).getClass_PKID());
                                        baseActivity.openActivity(LiveClassXiLieActivity.class, bundle);
                                    } else if (2 == courseType) { // 正常直播
                                        Bundle bundle = new Bundle();
//                                        bundle.putInt("Product_PKID", data.get(position).getClass_PKID());
//                                        baseActivity.openActivity(OnlineCourseDetailsAloneActivityTwo.class, bundle);
                                        bundle.putInt("Class_PKID", data.get(position).getClass_PKID());
                                        baseActivity.openActivity(LiveClassActivity.class, bundle);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(baseActivity, liveListBeanLiveProduct.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 公共参数
     *
     * @param pager
     * @return
     */
    private RequestBody commParam(int pager) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectIdList", projectIdList);
            obj.put("sort", sort);
            obj.put("direction", direction);
            obj.put("searchType", searchType);
            obj.put("isPlayBack", isPlayBack);
            obj.put("pageIndex", pager);
            obj.put("keywords", "");
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
        // 获取用户id
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        // 设置刷新样式
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                // 初始化数据
                if (NetUtil.isMobileConnected(baseActivity)) {
                    initData(page);
                    // 获取筛选列表信息
                    getSelectData();
                } else {
                    Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(emptyView);
        adapter = new LiveListAdapter(baseActivity, data);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {  //向下滚动
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        isLoading = true;
                        page += 1;
                        loadingMore(page);
                        isLoading = false;
                    }
                }

            }
        });

        thridSelList.add(new LiveCallBack(0, "全部", true));
        thridSelList.add(new LiveCallBack(1, "有回放", false));
        thridSelList.add(new LiveCallBack(2, "无回放", false));
    }

    private void getSelectData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<LiveSelectBean>> observable = httpPostService.getSelectProListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveSelectBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean<LiveSelectBean> liveSelectBeanCommonBean) {
                        if (100 == liveSelectBeanCommonBean.getCode()) {
                            allSelList = liveSelectBeanCommonBean.getBody().getList();
                            if (null != allSelList && allSelList.size() > 0) {
                                for (LiveSelectBean.SelectList sel : allSelList) {
                                    LiveSelectBean.SelectList.OneModel oneModel = sel.getLevelOne();
                                    fristSelList.add(oneModel);
                                    if (0 == projectId) { // 全部学科情况
                                        secodSelList.addAll(sel.getLevelTwo());
                                    } else if (projectId == oneModel.getProjectId()) { // 具体学科情况
                                        secodSelList.addAll(sel.getLevelTwo());
                                    }
                                }
                                if (0 == projectId) { // 新增含有全部且选中情况
                                    fristSelList.add(0, new LiveSelectBean.SelectList.OneModel(0, "全部", 1, true));
                                    secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 1, true));
                                } else { // 新增含有全部且未选中情况
                                    fristSelList.add(0, new LiveSelectBean.SelectList.OneModel(0, "全部", 1, false));
                                    secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 1, true));
                                    // 学科设置
                                    for (int i = 0; i < fristSelList.size(); i++) {
                                        if (projectId != 0 && projectId == fristSelList.get(i).getProjectId()) { // 设置选中学科为true
                                            fristSelList.get(i).setSelect(true);
                                        } else { // 其他未选中学科设置为false
                                            fristSelList.get(i).setSelect(false);
                                        }
                                    }
                                    // 科目设置
                                    for (int i = 0; i < secodSelList.size(); i++) {
                                        if (projectId == secodSelList.get(i).getProjectId()) { // 设置选中科目为true
                                            secodSelList.get(i).setSelect(true);
                                        } else { // 其他未选中科目设置为false
                                            secodSelList.get(i).setSelect(false);
                                        }
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(baseActivity, liveSelectBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 加载更多
     *
     * @param page
     */
    private void loadingMore(int page) {
        initData(page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        data.clear();
        swipeRefreshLayout.setRefreshing(true);
        initData(page);
    }


    @OnClick({R.id.back_layout, R.id.noteMore_Btn, R.id.time_txt,
            R.id.hotMan_txt, R.id.experience_txt, R.id.select_txt, R.id.select_img})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回布局
                baseActivity.finish();
                break;
            case R.id.noteMore_Btn: // 搜索按钮
                baseActivity.openActivity(LiveSearchActivity.class);
                break;
            case R.id.time_txt: // 时间排序
                page = 1;
                sort = 1;
                if (0 == direction) { // 如果当前为降序
                    direction = 1; // 设置为升序
                } else { // 如果当前排序为升序
                    direction = 0; // 设置为降序
                }
                data.clear();
                swipeRefreshLayout.setRefreshing(true);
                initData(page);
                break;
            case R.id.hotMan_txt: // 人气排序
                page = 1;
                sort = 2;
                if (0 == direction) { // 如果当前为降序
                    direction = 1; // 设置为升序
                } else { // 如果当前排序为升序
                    direction = 0; // 设置为降序
                }
                data.clear();
                swipeRefreshLayout.setRefreshing(true);
                initData(page);
                break;
            case R.id.experience_txt: // 系列排序
                page = 1;
                if(searchType == 1){
                    searchType = orgSearchType;
                    experience_txt.setTextColor(Color.parseColor("#666666"));
                }else{
                    searchType = 1;
                    experience_txt.setTextColor(Color.parseColor("#dd5555"));
                }
                data.clear();
                swipeRefreshLayout.setRefreshing(true);
                initData(page);
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

    private void createDialog() {

        // 弹出对话框
        final Dialog mDialog = new Dialog(baseActivity,
                R.style.nextTaskStyle);
        mDialog.setContentView(R.layout.dialog_live_select);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.show();
        // mDialog根布局
        RelativeLayout subDialog_layout = (RelativeLayout) mDialog.findViewById(R.id.subDialog_layout);
        // 重置布局
        Button resetBtn = (Button) mDialog.findViewById(R.id.resetBtn);
        // 完成布局
        final Button finishBtn = (Button) mDialog.findViewById(R.id.finishBtn);
        // 学科
        studyLayout = (RelativeLayout) mDialog.findViewById(R.id.studyLayout);
        studyUpOrDownImg = (ImageView) mDialog.findViewById(R.id.studyUpOrDownImg);
        studyRecyclerView = (RecyclerView) mDialog.findViewById(R.id.studyRecyclerView);
        studyRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        studyRecyclerView.setNestedScrollingEnabled(false);
        // 科目
        subjectLayout = (RelativeLayout) mDialog.findViewById(R.id.subjectLayout);
        subjectUpOrDownImg = (ImageView) mDialog.findViewById(R.id.subjectUpOrDownImg);
        subjectRecyclerView = (RecyclerView) mDialog.findViewById(R.id.subjectRecyclerView);
        subjectRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        subjectRecyclerView.setNestedScrollingEnabled(false);
        // 回放
        typeLayout = (RelativeLayout) mDialog.findViewById(R.id.typeLayout);
        typeUpOrDownImg = (ImageView) mDialog.findViewById(R.id.typeUpOrDownImg);
        typeRecyclerView = (RecyclerView) mDialog.findViewById(R.id.typeRecyclerView);
        typeRecyclerView.setLayoutManager(new MyGridLayoutManager(baseActivity, 3, GridLayoutManager.VERTICAL, false));
        typeRecyclerView.setNestedScrollingEnabled(false);

        // 学科适配
        fristAdapter = new OneRecyclerViewAdapter(baseActivity, fristSelList, handler);
        studyRecyclerView.setAdapter(fristAdapter);
        // 科目适配
        secondAdapter = new SecondRecAdapter(baseActivity, secodSelList, handler);
        subjectRecyclerView.setAdapter(secondAdapter);
        // 有无回放
        rePlayAdapter = new RePlayAdapter(baseActivity, thridSelList, handler);
        typeRecyclerView.setAdapter(rePlayAdapter);

        // 学科收缩监听
        setOnClick(studyLayout, studyUpOrDownImg, studyRecyclerView);
        // 科目收缩监听
        setOnClick(subjectLayout, subjectUpOrDownImg, subjectRecyclerView);
        // 回放收缩监听
        setOnClick(typeLayout, typeUpOrDownImg, typeRecyclerView);

        // 确定按钮监听
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                data.clear();
                initData(page);
                mDialog.dismiss();
            }
        });
        // 重置按钮监听
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectId = 0;
                projectIdList = "";
                searchType = 0;
                page = 1;
                data.clear();
                initData(page);
                title_txt.setText("全部");
                fristSelList.clear();
                secodSelList.clear();
                for (LiveSelectBean.SelectList selList : allSelList) {
                    LiveSelectBean.SelectList.OneModel oneModel = selList.getLevelOne();
                    fristSelList.add(oneModel);
                    if (0 == projectId) {
                        secodSelList.addAll(selList.getLevelTwo());
                    }
                }
                fristSelList.add(0, new LiveSelectBean.SelectList.OneModel(0, "全部", 1, true));
                secodSelList.add(0, new LiveSelectBean.SelectList.TwoModel(0, "全部", 1, true));
                for (int i = 0; i < fristSelList.size(); i++) {
                    if (i == 0) {
                        fristSelList.get(i).setSelect(true);
                    } else {
                        fristSelList.get(i).setSelect(false);
                    }
                }
                for (int i = 0; i < secodSelList.size(); i++) {
                    if (i == 0) {
                        secodSelList.get(i).setSelect(true);
                    } else {
                        secodSelList.get(i).setSelect(false);
                    }
                }
                for (int i = 0; i < thridSelList.size(); i++) {
                    if (i == 0) {
                        thridSelList.get(i).setSelect(true);
                    } else {
                        thridSelList.get(i).setSelect(false);
                    }
                }
                if (null != fristAdapter) {
                    fristAdapter.notifyDataSetChanged();
                }
                if (null != secondAdapter) {
                    secondAdapter.notifyDataSetChanged();
                }
                if (null != rePlayAdapter) {
                    rePlayAdapter.notifyDataSetChanged();
                }
                mDialog.dismiss();
            }
        });

        // mDialog 设置监听
        subDialog_layout.setOnClickListener(new View.OnClickListener() {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    private void setAscOrDescImg(ImageView view) {
        if (direction == 0) { // 降序
            view.setBackgroundResource(R.drawable.icon_desc);
        } else { // 升序
            view.setBackgroundResource(R.drawable.icon_asc);
        }
    }
}
