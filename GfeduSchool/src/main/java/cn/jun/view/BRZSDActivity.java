package cn.jun.view;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.jun.adapter.KLineAdapter;
import cn.jun.bean.WeakCourseWare;
import cn.jun.indexmain.interfaces.OnLoadMoreListener;
import cn.jun.indexmain.interfaces.OnMultiTypeItemClickListeners;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BRZSDActivity extends Activity implements View.OnClickListener {
    private HttpUtils httpUtils = new HttpUtils();
    private KLineAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private boolean isFailed = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static int isEndList;//加载状态 0,完成 1,失败

    //数据源
//    private CommonBean<WeakCourseWare> mDatas;
    private ArrayList<WeakCourseWare.CoursewareList> mDatas;
    //用户ID
    private int userId;
    private int ProjectID;
    private int PageIndex;
    //本地存储
    private SharedPreferences sp;
    //返回
    private ImageView iv_back;

    @Override
    protected void onResume() {
        super.onResume();
        if (httpUtils.isNetworkConnected(this)) {
//            PageIndex = 1;
//            initWeakDate(PageIndex);
            initView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k_line);
        sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            ProjectID = bundle.getInt("ProjectID");
        }

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    //薄弱知识点
    private void initWeakDate(int PageIndex) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam(PageIndex);
        Observable<CommonBean<WeakCourseWare>> observable = httpPostService.getWeakCourseWare(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<WeakCourseWare>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(BRZSDActivity.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<WeakCourseWare> weakCourseWare) {
                        if (100 == weakCourseWare.getCode()) {
                            if (weakCourseWare.getBody().getCoursewareCount() > 0) {
                                mDatas = weakCourseWare.getBody().getCoursewareList();
                            }
                        } else {
                            Toast.makeText(BRZSDActivity.this, weakCourseWare.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                            isEndList = 1;
                        }
                    }
                });
    }

    //公共参数
    private RequestBody commParam(int PageIndex) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("ProjectID", ProjectID);
            obj.put("PageIndex", PageIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        //初始化adapter
        mAdapter = new KLineAdapter(BRZSDActivity.this, null, true);
        mAdapter.setAddHaed(false);//设置有头部
        //全部首页列表点击事件
        mAdapter.setOnMultiTypeItemClickListener(new OnMultiTypeItemClickListeners<WeakCourseWare.CoursewareList>() {
            @Override
            public void onItemClick(CommonViewHolder viewHolder, WeakCourseWare.CoursewareList data, int position, int viewType) {
                if (data == null) {
//                    viewHolder.getView(R.id.pointgroup).setBackgroundResource(R.drawable.ic_camera);
                } else {
                    Intent cK2_it = new Intent(BRZSDActivity.this, KnowledgeTestActivity.class);
                    Bundle bundle_ck2 = new Bundle();
                    bundle_ck2.putInt("knowledgeId", data.getKnowledgeId());
                    bundle_ck2.putInt("projectId", ProjectID);
                    // 答案类型1:表示每日一题，2表示知识点做题
                    bundle_ck2.putInt("answerType", 2);
                    bundle_ck2.putString("title", data.getKnowledgeName());
                    cK2_it.putExtras(bundle_ck2);
                    startActivity(cK2_it);
                }
            }
        });
        //刷新数据监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });
        //加载更多事件
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        loadData();//初始化数据
    }


    /**
     * 初始化列表数据
     */
    private void loadData() {
        PageIndex = 1;
        initWeakDate(PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("loadDataloadData"," loadDataloadData ");
                if (!"".equals(mDatas) && null != mDatas) {
                    Log.i("mDatas"," mDatas ");
                    mAdapter.setInitData(mDatas);
                    mSwipeRefreshLayout.setRefreshing(false);
//                    mAdapter.setLoadingView(R.layout.load_loading);
                }else{
                    Log.i("mDatas"," null == null  ");
                }

            }
        }, 1000);
    }


    /**
     * 加载更多数据
     */
    private void loadMore() {
        PageIndex = PageIndex + 1;
        initWeakDate(PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (1 == isEndList && isFailed) {
                    Log.i(" 111  ====== > "," 111 ");
                    isFailed = false;
                    //加载失败
                    mAdapter.setLoadFailedView(R.layout.load_failed);
                } else if (0 == isEndList) {
                    Log.i(" 222  ====== > "," 222 ");
                    //加载完成
//                    mAdapter.setLoadEndView(R.layout.load_end);
                } else {
                    Log.i(" 333  ====== > "," 333 ");
                    mAdapter.setLoadMoreData(mDatas);
                }
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;


        }
    }
}
