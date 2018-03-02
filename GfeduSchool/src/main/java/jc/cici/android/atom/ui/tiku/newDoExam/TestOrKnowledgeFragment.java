package jc.cici.android.atom.ui.tiku.newDoExam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gfedu.home_pager.BaseFragment;
import cn.jun.bean.GetLiveDetailBean;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.TestOrKnowledgeAdapter;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.HistoryExamChoseBean;
import jc.cici.android.atom.bean.TestPaperBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.NetUtil;
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
 * 试卷做题历史 or 知识点做题历史
 * Created by atom on 2018/1/15.
 */

public class TestOrKnowledgeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context mCtx;
    private View view;
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
    private LinearLayoutManager linearLayoutManager;
    private TestOrKnowledgeAdapter adapter;
    private ArrayList<HistoryExamChoseBean.TestPaper> data = new ArrayList<>();
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    //
    private int tPaperType;

    @Override
    public void fetchData() {

    }

    public TestOrKnowledgeFragment() {
    }

    public TestOrKnowledgeFragment(int projectId, int tPaperType) {
        this.projectId = projectId;
        this.tPaperType = tPaperType;
        System.out.println("projectId >>>:" + projectId + ",tPaperType >>>:" + tPaperType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_test_or_knowledge, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        // 获取用户id
        sp = mCtx.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        // 设置刷新样式
        swipe_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        // 设置刷新监听
        swipe_layout.setOnRefreshListener(this);
        swipe_layout.setRefreshing(true);
        swipe_layout.post(new Runnable() {
            @Override
            public void run() {
                // 初始化数据
                if (NetUtil.isMobileConnected(mCtx)) {
                    // 初始化数据
                    initData();
                } else {
                    Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(mCtx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(emptyView);
        adapter = new TestOrKnowledgeAdapter(mCtx, data);
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
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != swipe_layout && swipe_layout.isRefreshing()) {
                            swipe_layout.setRefreshing(false);
                        }
                        Toast.makeText(mCtx, "网络请求异常，请下拉刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<HistoryExamChoseBean> testPaperBeanCommonBean) {

                        if (100 == testPaperBeanCommonBean.getCode()) {
                            ArrayList<HistoryExamChoseBean.TestPaper> list = testPaperBeanCommonBean.getBody().getTpapers();
                            if (null != list && !"".equals(list) && list.size() > 0) {
                                if (data.size() > 0) {
                                    data.clear();
                                }
                                data.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mCtx, testPaperBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private RequestBody commonPramas() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
            obj.put("searchType", tPaperType);
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
    public void onRefresh() {
        data.clear();
        initData();
    }
}
