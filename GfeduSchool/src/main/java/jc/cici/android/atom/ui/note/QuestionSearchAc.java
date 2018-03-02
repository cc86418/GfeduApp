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
import jc.cici.android.R;
import jc.cici.android.atom.adapter.QuestionRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.bean.QuestionBean;
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
 * 问题搜索类
 * Created by atom on 2017/7/6.
 */

public class QuestionSearchAc extends BaseActivity {

    private Unbinder unbinder;
    private BaseActivity baseActivity;
    // 输入框
    @BindView(R.id.search_inputTxt)
    EditText search_inputTxt;
    // 搜索按钮布局
    @BindView(R.id.searchBtn_layout)
    RelativeLayout searchBtn_layout;
    // 搜索按钮
    @BindView(R.id.searchBtn)
    TextView searchBtn;
    // RecyclerView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 空情况
    @BindView(R.id.emptyView)
    ImageView emptyView;
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
    // 类型(1:我的问题,2:大家的问题)
    private int type;
    // 输入内容
    private String content;
    private int page = 1;

    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;
    private QuestionRecyclerAdapter adapter;
    // 获取数据类
    private ArrayList<Question> dataList;
    // 定义广播接收器对象
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.jc.setBest".equals(intent.getAction())) {
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
    // 定义广播过滤器
    private IntentFilter filter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_notesearch;
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
        // 去标题
        baseActivity.requestNoTitle();
        Bundle bundle = getIntent().getExtras();
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
        // 搜索来源
        type = bundle.getInt("type");
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {
        // 注册广播
        filter = new IntentFilter();
//        filter.addAction("com.jc.delQues");
        filter.addAction("com.jc.setBest");
        // 注册广播
        registerReceiver(receiver, filter);
        // 搜索问题提示
        search_inputTxt.setHint("请输入问题关键字");
        // 添加布局
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
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


    @OnClick({R.id.searchBtn_layout})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.searchBtn_layout: // 搜索按钮或返回按钮
                if ("取消".equals(searchBtn.getText().toString())) {
                    baseActivity.finish();
                } else if ("搜索".equals(searchBtn.getText().toString())) {
                    if (NetUtil.isMobileConnected(baseActivity)) {
                        // 网络请求获取数据
                        getResultFromHttp();
                    } else {
                        Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 网络请求获取数据
     */
    private void getResultFromHttp() {

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
                            Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(CommonBean<QuestionBean> questionBeanCommonBean) {
                        if (100 == questionBeanCommonBean.getCode()) {
                            dataList = questionBeanCommonBean.getBody().getQuesList();
                            if (null != dataList && dataList.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                adapter = new QuestionRecyclerAdapter(baseActivity, dataList, type);  // 1:代表我的问题
                                SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                animationAdapter.setDuration(1000);
                                recyclerView.setAdapter(adapter);
                                // 加载更多
                                setLoadingMore();
                                /**
                                 * item 点击
                                 */
                                adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent it = new Intent(baseActivity, QuestionDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("quesId", dataList.get(position).getQuesId());
                                        bundle.putInt("childClassTypeId", childClassTypeId);
                                        bundle.putInt("page", 1);
                                        // 我的问题跳转标识
                                        bundle.putInt("jumpFlag", type);
                                        it.putExtras(bundle);
                                        baseActivity.startActivity(it);
                                    }
                                });
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(baseActivity, questionBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        if (NetUtil.isMobileConnected(baseActivity)) {
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
                            Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(baseActivity, "亲爱的小主,已经没有更多数据了哦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(baseActivity, questionBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }


    @OnTextChanged({R.id.search_inputTxt})
    void afterTextChanged(CharSequence text) {
        if (text.length() == 0) {
            searchBtn.setText("取消");
            if (null != dataList && dataList.size() > 0) {
                dataList.clear();
                adapter.notifyDataSetChanged();
            }
        } else {
            searchBtn.setText("搜索");
            content = text.toString();
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
            showProcessDialog(baseActivity,
                    R.layout.loading_process_dialog_color);
        }
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
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
            obj.put("searchContent", content);
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }
}
