package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.HistoryRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.HistoryLesson;
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
 * 课程历史列表activity
 * Created by atom on 2017/5/12.
 */

public class HistoryActivity extends BaseActivity {

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
    // 右侧搜索布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 注册按钮屏蔽
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 右侧搜索按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 完成学时
    @BindView(R.id.finishTime_Txt)
    TextView finishTime_Txt;
    // 出勤率
    @BindView(R.id.attendenceTime_Txt)
    TextView attendenceTime_Txt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 标题内容
    private String titleName;
    // 进度
    private Dialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private List<HistoryLesson> mData;
    private HistoryRecyclerAdapter adapter;
    // 学员id
    private int userId;
    private String className;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;
    // 笔记解锁
    private int stageNote;
    // 问题解锁
    private int stageProblem;
    // 资料解锁
    private int stageInformation;
    // 完成学时
    private String finishedPeriod;
    // 出勤率
    private String attendanceRate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
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
        // 获取班级名称
        className = getIntent().getStringExtra("className");
        // 获取标题内容
        titleName = getIntent().getStringExtra("titleName");
        classId = getIntent().getIntExtra("classId", 0);
        stageId = getIntent().getIntExtra("stageId", 0);
        // 笔记解锁
        stageNote = getIntent().getIntExtra("stageNote", 0);
        //问题解锁
        stageProblem = getIntent().getIntExtra("stageProblem", 0);
        // 资料解锁
        stageInformation = getIntent().getIntExtra("stageInformation", 0);
        // 添加内容
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始话视图
        initView();
        // 初始数据
        if (NetUtil.isMobileConnected(this)) {
            initData();
        } else {
            Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
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
     * 网络获取数据
     */
    private void initData() {

        Retrofit retorfit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retorfit.create(HttpPostService.class);

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId && 0 != stageId) {
            try {
                obj.put("client", commParam.getClient());
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("stageId", stageId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("keyword", "");
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<CommonBean<HistoryInfo>> observable = httpPostService.getHistoryLessonInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<HistoryInfo>>() {
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
                                public void onStart() {
                                    super.onStart();
                                    if (dialog != null && !dialog.isShowing()) {
                                        dialog.show();
                                    }
                                }

                                @Override
                                public void onNext(CommonBean<HistoryInfo> historyInfoBean) {
                                    if (100 == historyInfoBean.getCode()) {
                                        finishedPeriod = historyInfoBean.getBody().getFinishedPeriod();
                                        attendanceRate = historyInfoBean.getBody().getAttendanceRate();
                                        mData = historyInfoBean.getBody().getHistoryLessonList();

                                        if (null != mData && mData.size() > 0) {
                                            attendenceTime_Txt.setText(ToolUtils.strReplaceAll(attendanceRate));
                                            finishTime_Txt.setText(ToolUtils.strReplaceAll(finishedPeriod));
                                            adapter = new HistoryRecyclerAdapter(baseActivity, mData);
                                            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                            animationAdapter.setDuration(1000);
                                            recyclerView.setAdapter(animationAdapter);

                                            /**
                                             * 加载更多
                                             */
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

                                            /**
                                             * item 点击
                                             */
                                            adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("className", className);
                                                    bundle.putString("titleName", mData.get(position).getLessonName());
                                                    bundle.putInt("classId", classId);
                                                    bundle.putInt("stageId", stageId);
                                                    bundle.putInt("lessonId", mData.get(position).getLessonId());
                                                    bundle.putInt("stageNote", stageNote);
                                                    bundle.putInt("stageProblem", stageProblem);
                                                    bundle.putInt("stageInformation", stageInformation);
                                                    baseActivity.openActivity(ChapterActivity.class, bundle);
                                                }
                                            });
                                        } else {
                                            attendenceTime_Txt.setText(ToolUtils.strReplaceAll("- -"));
                                            finishTime_Txt.setText(ToolUtils.strReplaceAll("0H"));
                                            Toast.makeText(baseActivity, "没有数据", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(baseActivity, historyInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
        } else {
            Toast.makeText(baseActivity, "用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载更多
     */
    private void loading() {

    }

    /**
     * c初始化基础视图
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        search_Btn.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText("历史");
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    /**
     * 视图监听
     *
     * @param view
     */
    @OnClick({R.id.back_layout, R.id.search_Btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.search_Btn: // 搜索按钮
                Bundle bundle = new Bundle();
                bundle.putInt("classId", classId);
                bundle.putInt("stageId", stageId);
                bundle.putInt("stageNote", stageNote);
                bundle.putInt("stageProblem", stageProblem);
                bundle.putInt("stageInformation", stageInformation);
                baseActivity.openActivity(SearchActivity.class, bundle);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
