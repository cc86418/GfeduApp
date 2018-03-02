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
import android.widget.Button;
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
import jc.cici.android.atom.adapter.MyNoteRecycleAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Note;
import jc.cici.android.atom.bean.NoteBean;
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
 * 笔记查询Activity
 * Created by atom on 2017/7/6.
 */

public class NoteSearchActivity extends BaseActivity {

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
    // 当前传递课程id
    private int cspkid;
    // 阶段id
    private int childClassTypeId;
    // 笔记类型(1:我的笔记,2:大家的笔记)
    private int type;
    // 视频id
    private int videoId;
    // 试卷id
    private int testQuesId;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Note> list;
    private MyNoteRecycleAdapter adapter;
    private Dialog dialog;
    // 输入内容
    private String content;
    // 定义广播过滤器
    private IntentFilter filter;
    // 定义广播接收器对象
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.jc.delnote".equals(intent.getAction())) { // 删除笔记广播
                Bundle bundle = intent.getExtras();
                int delNoteId = bundle.getInt("noteId");
                for (int i = 0; i < list.size(); i++) {
                    if (delNoteId == (list.get(i).getNTBPkid())) {
                        list.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            } else if ("com.jc.zan".equals(intent.getAction())) { // 点赞广播
                Bundle bundle = intent.getExtras();
                int delNoteId = bundle.getInt("noteId");
                for (int i = 0; i < list.size(); i++) {
                    if (delNoteId == (list.get(i).getNTBPkid())) {
                        Note note = new Note();
                        note.setCheckStatus(list.get(i).getCheckStatus());
                        note.setNTBAddTime(list.get(i).getNTBAddTime());
                        note.setNTBContent(list.get(i).getNTBContent());
                        note.setNTBPkid(list.get(i).getNTBPkid());
                        note.setNTBScreenShots(list.get(i).getNTBScreenShots());
                        note.setNTBTempVal(list.get(i).getNTBTempVal());
                        note.setS_NickName(list.get(i).getS_NickName());
                        note.setSN_Head(list.get(i).getSN_Head());
                        note.setSubJectSName(list.get(i).getSubJectSName());
                        note.setIsZan(list.get(i).getIsZan());
                        if (1 != list.get(i).getIsZan()) { // 如果未点赞
                            note.setZcount(list.get(i).getZcount() + 1);
                        }
                        list.set(i, note);
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    };

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
        cspkid = bundle.getInt("cspkid");
        // 阶段id
        childClassTypeId = bundle.getInt("stageId");
        // 笔记来源
        type = bundle.getInt("type");
        System.out.println("type >>>:" + type);
        // 视频id
        videoId = bundle.getInt("videoId");
        // 试卷id
        testQuesId = bundle.getInt("testQuesId");
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
        // 点赞广播
        filter.addAction("com.jc.zan");
        // 删除数据广播
        filter.addAction("com.jc.delnote");
        // 注册广播
        registerReceiver(receiver, filter);
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
        RequestBody body = commRequest(0, 1);
        Observable<CommonBean<NoteBean>> observable = httpPostService.getNotesListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<NoteBean>>() {
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
                            public void onNext(CommonBean<NoteBean> noteBeanCommonBean) {
                                if (100 == noteBeanCommonBean.getCode()) {
                                    emptyView.setVisibility(View.GONE);
                                    list = noteBeanCommonBean.getBody().getNotesList();
                                    if (null != list && list.size() > 0) {
                                        adapter = new MyNoteRecycleAdapter(baseActivity, list, type);
                                        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                        animationAdapter.setDuration(1000);
                                        recyclerView.setAdapter(adapter);

                                        /**
                                         * item 点击
                                         */
                                        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Intent it = new Intent(baseActivity, NoteDetailActivity.class);
                                                Bundle bundle = new Bundle();
                                                // 笔记标示(1:我的笔记)
                                                bundle.putInt("key", type);
                                                bundle.putInt("noteId", list.get(position).getNTBPkid());
                                                it.putExtras(bundle);
                                                baseActivity.startActivity(it);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(baseActivity, "暂无搜索内容", Toast.LENGTH_SHORT).show();
                                        emptyView.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(baseActivity, noteBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    @OnTextChanged({R.id.search_inputTxt})
    void afterTextChanged(CharSequence text) {
        if (text.length() == 0) {
            searchBtn.setText("取消");
            if (null != list && list.size() > 0) {
                list.clear();
                adapter.notifyDataSetChanged();
            }
        } else {
            searchBtn.setText("搜索");
            content = text.toString();
        }
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
            obj.put("userId", commParam.getUserId());
            // 课表id
            obj.put("cspkid", cspkid);
            // 阶段id
            obj.put("childClassTypeId", childClassTypeId);
            // 第一页
            obj.put("pageIndex", page);
            // 每页个数
            obj.put("pageSize", 10);
            // 笔记类型(1:我的笔记,2:大家的笔记)
            obj.put("type", type);
            // 视频id
            obj.put("vpkid", videoId);
            // 问题id
            obj.put("TestQuesID", testQuesId);
            // 内容关键字
            obj.put("queryWords", content);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
