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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.MyAnswerRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
//import jc.cici.android.atom.adapter.base.LoadingMore;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.MyAnswer;
import jc.cici.android.atom.bean.MyAnswerBean;
import jc.cici.android.atom.bean.Question;
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
 * 我的回答Fragment
 * Created by atom on 2017/6/12.
 */

public class MyAnswerFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_refresh_header)
    CommonHeader swipe_refresh_header;
    @BindView(R.id.swipe_target)
    RecyclerView swipe_target;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    private Activity mCtx;
    private LinearLayoutManager linearLayoutManager;
    private MyAnswerRecyclerAdapter adapter;
    // 获取数据类
    private ArrayList<MyAnswer> dataList;
    private Dialog dialog;
    // 当前页
    private int page = 1;
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
    // 定义广播接收器对象
    private BroadcastReceiver receiver;
    // 定义广播过滤器
    private IntentFilter filter;

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
        // 课表id
        classScheduleId = bundle.getInt("classScheduleId");

        System.out.println("childClassTypeId >>>:" + childClassTypeId);
        // 初始化广播接受器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.jc.answerZan".equals(intent.getAction())) {
                    int changerId = intent.getIntExtra("answerId", 0);
                    for (int i = 0; i < dataList.size(); i++) {
                        if (changerId == dataList.get(i).getAnswerId()) {
                            MyAnswer answer = new MyAnswer();
                            answer.setUserPraiseStatus(1);
                            answer.setImageCount(dataList.get(i).getImageCount());
                            answer.setCommentsCount(dataList.get(i).getCommentsCount());
                            answer.setAfterQuesCount(dataList.get(i).getAfterQuesCount());
                            answer.setAnswerAddTime(dataList.get(i).getAnswerAddTime());
                            answer.setAnswerContent(dataList.get(i).getAnswerContent());
                            answer.setAnswerId(dataList.get(i).getAnswerId());
                            answer.setAnswerImageUrl(dataList.get(i).getAnswerImageUrl());
                            // 获取原有点赞数量
                            int count = dataList.get(i).getPraiseCount();
                            // 点赞数量加1
                            answer.setPraiseCount(count + 1);
                            answer.setAnswerStatus(dataList.get(i).getAnswerStatus());
                            dataList.set(i, answer);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                } else if ("com.jc.commentCount".equals(intent.getAction())) { // 评论数量
                    // 评论数量
                    Bundle bundle = intent.getExtras();
                    int answerId = bundle.getInt("answerId");
                    int count = bundle.getInt("count");
                    for (int i = 0; i < dataList.size(); i++) {
                        if (answerId == dataList.get(i).getAnswerId()) {
                            MyAnswer answer = new MyAnswer();
                            answer.setUserPraiseStatus(dataList.get(i).getUserPraiseStatus());
                            answer.setImageCount(dataList.get(i).getImageCount());
                            int oriCount = dataList.get(i).getCommentsCount();
                            answer.setCommentsCount(oriCount + count);
                            answer.setAfterQuesCount(dataList.get(i).getAfterQuesCount());
                            answer.setAnswerAddTime(dataList.get(i).getAnswerAddTime());
                            answer.setAnswerContent(dataList.get(i).getAnswerContent());
                            answer.setAnswerId(dataList.get(i).getAnswerId());
                            answer.setAnswerImageUrl(dataList.get(i).getAnswerImageUrl());
                            // 点赞数量加1
                            answer.setPraiseCount(dataList.get(i).getPraiseCount());
                            answer.setAnswerStatus(dataList.get(i).getAnswerStatus());
                            dataList.set(i, answer);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        };
        // 注册广播
        filter = new IntentFilter();
        filter.addAction("com.jc.answerZan");
        filter.addAction("com.jc.commentCount");
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
        // 刷新加载数据
        setRefresh();
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

    private void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (emptyView.getVisibility() == View.VISIBLE) {
                    emptyView.setVisibility(View.GONE);
                }
                page = 1;
                // 添加数据
                if (NetUtil.isMobileConnected(mCtx)) {
                    Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
                    HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                    RequestBody body = commRequest(page, 0);
                    Observable<CommonBean<MyAnswerBean>> observable = httpPostService.getAnswerListInfo(body);
                    observable.subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    new Subscriber<CommonBean<MyAnswerBean>>() {
                                        @Override
                                        public void onCompleted() {
                                            swipeToLoadLayout.setRefreshing(false);//收头
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onNext(CommonBean<MyAnswerBean> myAnswerBeanCommonBean) {
                                            if (100 == myAnswerBeanCommonBean.getCode()) {
                                                emptyView.setVisibility(View.GONE);
                                                if (null != myAnswerBeanCommonBean.getBody().getAnswerList() && myAnswerBeanCommonBean.getBody().getAnswerList().size() > 0) {
                                                    dataList.clear();
                                                    dataList.addAll(myAnswerBeanCommonBean.getBody().getAnswerList());
                                                    adapter.notifyDataSetChanged();
                                                    // 加载更多
                                                    setLoadingMore();
                                                } else {
                                                    emptyView.setVisibility(View.VISIBLE);
                                                }

                                            } else {
                                                Toast.makeText(mCtx, myAnswerBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                } else {
                    Toast.makeText(mCtx, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commRequest(page, 1);
        Observable<CommonBean<MyAnswerBean>> observable = httpPostService.getAnswerListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<MyAnswerBean>>() {
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
                            public void onNext(CommonBean<MyAnswerBean> myAnswerBeanCommonBean) {
                                if (100 == myAnswerBeanCommonBean.getCode()) {
                                    emptyView.setVisibility(View.GONE);
                                    dataList = myAnswerBeanCommonBean.getBody().getAnswerList();
                                    if (null != dataList && dataList.size() > 0) {
                                        adapter = new MyAnswerRecyclerAdapter(mCtx, dataList, 3);
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

                                                if (1 == dataList.get(position).getIsDelete()) {
                                                    final Dialog tipDialog = new Dialog(mCtx, R.style.NormalDialogStyle);
                                                    tipDialog.setContentView(R.layout.dialog_tip);
                                                    tipDialog.setCanceledOnTouchOutside(true);
                                                    TextView delTxt = (TextView) tipDialog.findViewById(R.id.delTxt);
                                                    delTxt.setText("该问题已被管理员删除,删除理由:" + dataList.get(position).getDeleteDetail());
                                                    // 确定按钮
                                                    Button goOnBtn = (Button) tipDialog.findViewById(R.id.goOnBtn);
                                                    goOnBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            tipDialog.dismiss();
                                                        }
                                                    });
                                                    tipDialog.show();

                                                } else {
                                                    Intent it = new Intent(mCtx, AnswerDetailActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("answerId", dataList.get(position).getAnswerId());
                                                    bundle.putInt("afterType", 2);
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
                                    Toast.makeText(mCtx, myAnswerBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                if (null != dialog && !dialog.isShowing()) {
                                    dialog.show();
                                }
                            }
                        }
                );
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
                        Loading();
                    }
                }
            }
        });
    }

    /**
     * 请求加载更多数据
     */
    private void Loading() {
        page++;
        // 添加数据
        if (NetUtil.isMobileConnected(mCtx)) {
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commRequest(page, 0);
            Observable<CommonBean<MyAnswerBean>> observable = httpPostService.getAnswerListInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<MyAnswerBean>>() {
                                @Override
                                public void onCompleted() {
                                    swipeToLoadLayout.setRefreshing(false);//收头
                                }

                                @Override
                                public void onError(Throwable e) {
                                    dialog.dismiss();
                                    Toast.makeText(mCtx, "网络请求错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(CommonBean<MyAnswerBean> myAnswerBeanCommonBean) {
                                    if (100 == myAnswerBeanCommonBean.getCode()) {
                                        if (null != myAnswerBeanCommonBean.getBody().getAnswerList() && myAnswerBeanCommonBean.getBody().getAnswerList().size() > 0) {
                                            ArrayList<MyAnswer> moreList = new ArrayList<MyAnswer>();
                                            moreList = myAnswerBeanCommonBean.getBody().getAnswerList();
                                            dataList.addAll(moreList);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(mCtx, "亲爱的小主,已经没有更多数据了哦", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mCtx, myAnswerBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (1 == flag) {
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
            // 班型id
            obj.put("classTypeId", classTypeId);
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
