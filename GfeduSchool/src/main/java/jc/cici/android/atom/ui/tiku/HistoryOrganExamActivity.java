package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import jc.cici.android.atom.adapter.HistoryOrganExamAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.HistoryExamChoseBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 历史组卷activity
 * Created by atom on 2018/1/2.
 */

public class HistoryOrganExamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题文字
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    // 标记
    @BindView(R.id.tag_img)
    ImageView tag_img;
    // 创建试卷
    @BindView(R.id.createTest_txt)
    TextView createTest_txt;
    // 刷新
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    //列表
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    // 试卷类型
    private int tPaperType;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<HistoryExamChoseBean.TestPaper> data = new ArrayList<>();
    private HistoryOrganExamAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_organ;
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
        tPaperType = getIntent().getIntExtra("tPaperType", 0);
        System.out.println("projectId >>>:" + projectId + ",tPaperType >>>:" + tPaperType);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {
        // 获取用户id
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        titleName_txt.setText("智能组卷");
        tag_img.setVisibility(View.GONE);
        createTest_txt.setVisibility(View.VISIBLE);
        // 设置刷新样式
        swipe_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipe_layout.setOnRefreshListener(this);
        swipe_layout.setRefreshing(true);
        swipe_layout.post(new Runnable() {
            @Override
            public void run() {
                // 初始化数据
                if (NetUtil.isMobileConnected(baseActivity)) {
                    // 初始化数据
                    initData();
                } else {
                    Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(emptyView);
        adapter = new HistoryOrganExamAdapter(this, data);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
        Observable<CommonBean<HistoryExamChoseBean>> observable = httpPostService.examChoseHistoryInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<HistoryExamChoseBean>>() {
                    @Override
                    public void onCompleted() {
                        if (swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                        Toast.makeText(baseActivity, "网络请求异常，请刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<HistoryExamChoseBean> historyExamChoseBeanCommonBean) {
                        if (100 == historyExamChoseBeanCommonBean.getCode()) {
                            // 获取历史列表内容
                            ArrayList<HistoryExamChoseBean.TestPaper> tPapers = historyExamChoseBeanCommonBean.getBody().getTpapers();
                            if (null != tPapers && !"".equals(tPapers) && tPapers.size() > 0) {
                                if (data.size() > 0) {
                                    data.clear();
                                }
                                data.addAll(tPapers);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(baseActivity, historyExamChoseBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonPramas() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
            obj.put("tpaperType", tPaperType);
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
            R.id.createTest_txt
    })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.createTest_txt: // 创建试卷
                baseActivity.finish();
                break;
        }
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
        data.clear();
        initData();

    }
}
