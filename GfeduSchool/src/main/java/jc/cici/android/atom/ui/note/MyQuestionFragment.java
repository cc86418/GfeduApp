package jc.cici.android.atom.ui.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.QuestionRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.bean.QuestionBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.CommonHeader;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的问题fragment
 * Created by atom on 2017/6/12.
 */

public class MyQuestionFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_refresh_header)
    CommonHeader swipe_refresh_header;
    @BindView(R.id.swipe_target)
    RecyclerView swipe_target;
    private Activity mCtx;
    private LinearLayoutManager linearLayoutManager;
    private QuestionRecyclerAdapter adapter;
    // 获取数据类
    private ArrayList<Question> dataList;
    // 定义广播接收器对象
    private BroadcastReceiver receiver;
    // 定义广播过滤器
    private IntentFilter filter;
    private Dialog dialog;
    // 当前页
    private int page = 1;
    // 问题类型
    private int type = 1;
    // 班型id
    private int classTypeId;
    // 子班型id
    private int childClassTypeId;
    // 试卷id
    private int testPaperId;
    // 试题id
    private int testQuesId;
    // 视频id
    private int videoId;
    // 课表id
    private int classScheduleId;

    @BindView(R.id.emptyView)
    ImageView emptyView;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        // 添加刷新视图
        swipeToLoadLayout.setRefreshHeaderView(view);
        //添加过渡滑动
        swipeToLoadLayout.setRefreshCompleteDelayDuration(800);

        linearLayoutManager = new LinearLayoutManager(mCtx);
        swipe_target.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.notefragment_view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this.getActivity();
        Bundle bundle = getArguments();
        // 班型id
        classTypeId = bundle.getInt("classTypeId");
        // 子班型id
        childClassTypeId = bundle.getInt("childClassTypeId");
        // 试卷id
        testPaperId = bundle.getInt("testPaperId");
        // 试题id
        testQuesId = bundle.getInt("testQuesId");
        // 视频id
        videoId = bundle.getInt("videoId");
        // 课程id
        classScheduleId = bundle.getInt("classScheduleId");
        System.out.println("childClassTypeId >>>:" + childClassTypeId);
        // 初始化广播接受器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.jc.addQues".equals(intent.getAction()) // 添加问题广播
                        || "com.jc.quesChange".equals(intent.getAction()) // 全部解析添加问题
                        || "com.jc.wroQuesChange".equals(intent.getAction())) {  // 错题解析添加问题
                    if (NetUtil.isMobileConnected(mCtx)) {
                        if (null != dataList && dataList.size() > 0) {
                            // 刷新数据
                            setRefresh();
                        } else {
                            addData();
                        }
                    } else {
                        Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else if ("com.jc.delQues".equals(intent.getAction())) { // 删除问题广播
                    Bundle bundle = intent.getExtras();
                    int delNoteId = bundle.getInt("quesId");
                    for (int i = 0; i < dataList.size(); i++) {
                        if (delNoteId == (dataList.get(i).getQuesId())) {
                            dataList.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                } else if ("com.jc.setBest".equals(intent.getAction())) {
                    int quesId = intent.getIntExtra("quesId", 0);
                    for (int i = 0; i < dataList.size(); i++) {
                        if (quesId == dataList.get(i).getQuesId()) {
                            Question question = new Question();
                            question.setNickName(dataList.get(i).getNickName());
                            question.setImageCount(dataList.get(i).getImageCount());
                            question.setHeadImg(dataList.get(i).getHeadImg());
                            question.setQuesAddTime(dataList.get(i).getQuesAddTime());
                            question.setQuesContent(dataList.get(i).getQuesContent());
                            question.setQuesId(dataList.get(i).getQuesId());
                            question.setQuesImageUrl(dataList.get(i).getQuesImageUrl());
                            question.setQuesSubjectName(dataList.get(i).getQuesSubjectName());
                            question.setQuesStatus(3);
                            dataList.set(i, question);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        };
        // 注册广播
        filter = new IntentFilter();
        // 添加问题
        filter.addAction("com.jc.addQues");
        filter.addAction("com.jc.delQues");
        filter.addAction("com.jc.setBest");
        filter.addAction("com.jc.quesChange");
        filter.addAction("com.jc.wroQuesChange");
        // 注册广播
        mCtx.registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(swipe_refresh_header, null);
        // 添加数据
        if (NetUtil.isMobileConnected(mCtx)) {
            addData();
        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新加载数据
                setRefresh();
            }
        });
        return view;
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
     * 刷新数据
     */
    private void setRefresh() {
        if (emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }
        page = 1;
        // 添加数据
        if (NetUtil.isMobileConnected(mCtx)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page, 0);
            Observable<CommonBean<QuestionBean>> observable = httpPostService.getQuesListInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CommonBean<QuestionBean>>() {
                        @Override
                        public void onCompleted() {
                            swipeToLoadLayout.setRefreshing(false);//收头
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(CommonBean<QuestionBean> questionBeanCommonBean) {
                            if (100 == questionBeanCommonBean.getCode()) {
                                if (null != questionBeanCommonBean.getBody().getQuesList() && questionBeanCommonBean.getBody().getQuesList().size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                    dataList.clear();
                                    dataList.addAll(questionBeanCommonBean.getBody().getQuesList());
                                    adapter.notifyDataSetChanged();
                                    // 加载更多
                                    setLoadingMore();
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(mCtx, questionBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 添加数据
     */
    private void addData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page, 1);
        Observable<CommonBean<QuestionBean>> observable = httpPostService.getQuesListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<QuestionBean>>() {
                    @Override
                    public void onCompleted() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(CommonBean<QuestionBean> questionBeanCommonBean) {
                        if (100 == questionBeanCommonBean.getCode()) {
                            dataList = questionBeanCommonBean.getBody().getQuesList();
                            if (null != dataList && dataList.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                adapter = new QuestionRecyclerAdapter(mCtx, dataList, 1);  // 1:代表我的问题
                                SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                animationAdapter.setDuration(1000);
                                swipe_target.setAdapter(adapter);
                                // 加载更多
                                setLoadingMore();
                                /**
                                 * item 点击
                                 */
                                adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (1 != dataList.get(position).getIsDelete()) { // 如果没有删除，则点击进入详情
                                            Intent it = new Intent(mCtx, QuestionDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("quesId", dataList.get(position).getQuesId());
                                            bundle.putInt("childClassTypeId", childClassTypeId);
                                            bundle.putInt("classId", classTypeId);
                                            bundle.putInt("lessonId", classScheduleId);
                                            bundle.putInt("page", 1);
                                            // 我的问题跳转标识
                                            bundle.putInt("jumpFlag", 1);
                                            it.putExtras(bundle);
                                            mCtx.startActivity(it);
                                        }
                                    }
                                });
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(mCtx, questionBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (null != dialog && !dialog.isShowing()) {
                            dialog.show();
                        }
                    }
                });
    }


    /**
     * 加载更多
     */
    private void setLoadingMore() {
        swipe_target.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        loading();
                    }
                }
            }
        });
    }

    /**
     * 请求加载更多数据
     */
    private void loading() {
        page++;
        // 添加数据
        if (NetUtil.isMobileConnected(mCtx)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page, 0);
            Observable<CommonBean<QuestionBean>> observable = httpPostService.getQuesListInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CommonBean<QuestionBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(CommonBean<QuestionBean> questionBeanCommonBean) {
                            if (100 == questionBeanCommonBean.getCode()) {
                                ArrayList<Question> moreList = new ArrayList<>();
                                moreList = questionBeanCommonBean.getBody().getQuesList();
                                if (null != moreList && moreList.size() > 0) {
                                    dataList.addAll(moreList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mCtx, "亲爱的小主,已经没有更多数据了哦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mCtx, questionBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commRequest(int page, int flag) {
        if (flag == 1) {
            // 加载数据
            showProcessDialog(mCtx,
                    R.layout.loading_process_dialog_color);
        }
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("appname", commParam.getAppname());
            obj.put("userId", commParam.getUserId());
//            // 班型id
//            obj.put("classTypeId", classTypeId);
            // 子班型id(阶段id)
            obj.put("childClassTypeId", childClassTypeId);
            // 试卷id
            obj.put("testPaperId", testPaperId);
            // 试题id
            obj.put("testQuesId", testQuesId);
            // 视频id
            obj.put("videoId", videoId);
            // 课表id
            obj.put("classScheduleId", classScheduleId);
            // 搜索内容
            obj.put("searchContent", "");
            // 第一页
            obj.put("pageIndex", page);
            // 每页个数
            obj.put("pageSize", 10);
            // 问题类型(1:我的问题,2:大家的问题)
            obj.put("quesType", type);
            obj.put("timeStamp", commParam.getTimeStamp());
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
        if (null != receiver) {
            mCtx.unregisterReceiver(receiver);
        }
    }
}
