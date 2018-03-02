package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.OnLineRecycleAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Day;
import jc.cici.android.atom.bean.DayLesson;
import jc.cici.android.atom.bean.FaceCourseBean;
import jc.cici.android.atom.bean.OnLineBean;
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
 * 课程列表activity
 * Created by atom on 2017/10/16.
 */

public class CourseFormActivity extends BaseActivity {

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
    // 右侧历史文字布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 右侧历史文字
    @BindView(R.id.register_txt)
    TextView register_txt;
    // RecycleView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    ImageView emptyView;

    // 班级名称
    private String className;
    // 标题内容
    private String titleName;
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
    private Dialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<OnLineBean> itemList = new ArrayList<>();
    private OnLineRecycleAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_courseform;
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
        // 获取班级名
        className = getIntent().getStringExtra("className");
        // 获取标题
        titleName = getIntent().getStringExtra("titleName");
        // 获取班型id
        classId = getIntent().getIntExtra("classId", 0);
        // 获取阶段id
        stageId = getIntent().getIntExtra("stageId", 0);
        // 笔记解锁
        stageNote = getIntent().getIntExtra("stageNote", 0);
        //问题解锁
        stageProblem = getIntent().getIntExtra("stageProblem", 0);
        // 资料解锁
        stageInformation = getIntent().getIntExtra("stageInformation", 0);
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
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


    private void initData() {
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("classId", classId);
            obj.put("stageId", stageId);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<FaceCourseBean>> observable = httpPostService.getClassLessListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<FaceCourseBean>>() {
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
                        }
                        Toast.makeText(baseActivity, "网络异常，请刷新重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<FaceCourseBean> faceCourseBeanCommonBean) {
                        if (100 == faceCourseBeanCommonBean.getCode()) {
                            ArrayList<OnLineBean> list = faceCourseBeanCommonBean.getBody().getList();
                            if (null != list && list.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                for (int i = 0; i < list.size(); i++) {
                                    ArrayList<Day> daysList = list.get(i).getDays();
                                    ArrayList<DayLesson> mLesson = new ArrayList<DayLesson>();
                                    if (null != daysList && daysList.size() > 0) {
                                        for (int j = 0; j < daysList.size(); j++) {
                                            ArrayList<DayLesson> lessonList = daysList.get(j).getLessonData();
                                            if (null != lessonList && lessonList.size() > 0) {
                                                for (DayLesson less : lessonList) {
                                                    less.setDate(daysList.get(j).getDate());
                                                    mLesson.add(less);
                                                }
                                            }
                                        }
                                    }
                                    list.get(i).setmLessData(mLesson);
                                }
                                itemList.addAll(list);
                                adapter = new OnLineRecycleAdapter(baseActivity, itemList, classId, className, titleName, stageId, stageNote, stageProblem, stageInformation);
                                SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                animationAdapter.setDuration(1000);
                                recyclerView.setAdapter(adapter);
                            }else{
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(baseActivity, faceCourseBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 初始化视图控件
     */
    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setText("历史");
        title_txt.setText(titleName);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.register_txt: // 历史按钮
                Bundle bundle = new Bundle();
                bundle.putString("className", className);
                bundle.putString("titleName", titleName);
                bundle.putInt("classId", classId);
                bundle.putInt("stageId", stageId);
                bundle.putInt("stageNote", stageNote);
                bundle.putInt("stageProblem", stageProblem);
                bundle.putInt("stageInformation", stageInformation);
                baseActivity.openActivity(HistoryActivity.class, bundle);
                break;
            default:
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
}
