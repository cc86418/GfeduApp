package jc.cici.android.atom.ui.study;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.CourseRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Course;
import jc.cici.android.atom.bean.CourseInfo;
import jc.cici.android.atom.bean.LessInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.google.zxing.activity.CaptureActivity;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static jc.cici.android.atom.utils.ToolUtils.isCurrentDay;

/**
 * 体课程具详情列表页
 * Created by atom on 2017/5/11.
 */

public class CourseDetialActivity extends BaseActivity {

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
    @BindView(R.id.header_title_layout)
    RelativeLayout header_item_course;
    @BindView(R.id.today_Txt)
    TextView today_Txt;
    @BindView(R.id.qr_layout)
    RelativeLayout qr_layout;
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
    // 学员id
    private int userId;
    private Dialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Course> dataList;
    private ArrayList<CourseInfo> info;
    private CourseRecyclerAdapter adapter;

    private int mCurrentPosition = 0;

    private int mSuspensionHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coursedetial;
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

    // 网络请求获取详情数据
    private void initData() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
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
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<LessInfo> observable = httpPostService.getLessInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<LessInfo>() {
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
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(LessInfo lessInfo) {
                                    int code = lessInfo.getCode();
                                    if (code == 100) { // 请求成功
                                        emptyView.setVisibility(View.GONE);
                                        dataList = (ArrayList<Course>) lessInfo.getBody();
                                        if (null != dataList && dataList.size() > 0) {
                                            info = new ArrayList<CourseInfo>();
                                            int i = 0;
                                            for (Course course : dataList) {
                                                List<CourseInfo> lessons = course.getLessonData();
                                                for (CourseInfo couseInfo : lessons) {
                                                    couseInfo.setIndex(i);
                                                    couseInfo.setDate(course.getDate());
                                                    info.add(couseInfo);
                                                }
                                                i++;
                                            }

                                            String strDate = info.get(mCurrentPosition).getDate();
                                            String[] str = strDate.split("日");
                                            String str1 = str[1];
                                            String mouth = str1.substring(0, str1.lastIndexOf("月"));
                                            System.out.println("mouth >>>:" + mouth);
                                            String str2 = str[0];
                                            Calendar now = Calendar.getInstance();
                                            int year = now.get(Calendar.YEAR);
                                            String strYear = String.valueOf(year);
                                            String date = strYear + "-" + mouth + "-" + str2;
                                            if (ToolUtils.isCurrentDay(date)) { // 表示今日
                                                String today = "今日";
                                                today_Txt.setText(ToolUtils.setTextSize(baseActivity, today, today.length() - 1, today.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                                            } else { // 非今日情况
                                                String dateStr = info.get(mCurrentPosition).getDate();
                                                String newStr = dateStr.replace("日", "");
                                                today_Txt.setText(ToolUtils.setTextSize(baseActivity, newStr, newStr.length() - 4, newStr.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                                            }
                                            String dateStr[] = info.get(mCurrentPosition).getLessonDate().split(" ");
                                            String currentDate = dateStr[0].replaceAll("/", "-");
                                            // 课程开始时间
                                            String startTime = info.get(mCurrentPosition).getLessonStartTime();
                                            // 课程结束时间
                                            String endTime = info.get(mCurrentPosition).getLessonEndTime();
                                            if (ToolUtils.isCurrentTimeDuring(currentDate, startTime, endTime)) { // 当前时间段
                                                qr_layout.setVisibility(View.VISIBLE);
                                            } else { // 非当前时间段
                                                qr_layout.setVisibility(View.GONE);
                                            }
                                            adapter = new CourseRecyclerAdapter(baseActivity, info);
                                            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                            animationAdapter.setDuration(1000);
                                            recyclerView.setAdapter(animationAdapter);

                                            /**
                                             * item 点击
                                             */
                                            adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("className", className);
                                                    bundle.putString("titleName", titleName);
                                                    bundle.putInt("classId", classId);
                                                    bundle.putInt("stageId", stageId);
                                                    bundle.putInt("lessonId", info.get(position).getLessonId());
                                                    bundle.putInt("stageNote", stageNote);
                                                    bundle.putInt("stageProblem", stageProblem);
                                                    bundle.putInt("stageInformation", stageInformation);
                                                    baseActivity.openActivity(ChapterActivity.class, bundle);
                                                }
                                            });
                                        } else {
                                            emptyView.setVisibility(View.VISIBLE);
                                        }

                                    } else { // 请求失败
                                        Toast.makeText(baseActivity, "网络请求失败", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    if (dialog != null && !dialog.isShowing()) {
                                        dialog.show();
                                    }
                                }
                            }
                    );
        } else {
            Toast.makeText(this, "用户未登陆或班级不存在", Toast.LENGTH_SHORT).show();
        }

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
//                        loading();
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = header_item_course.getHeight();

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        header_item_course.setY(-(mSuspensionHeight - view.getTop()));
                    } else {
                        header_item_course.setY(0);
                        String strDate = info.get(mCurrentPosition).getDate();
                        String[] str = strDate.split("日");
                        String str1 = str[1];
                        String mouth = str1.substring(0, str1.lastIndexOf("月"));
                        String str2 = str[0];
                        Calendar now = Calendar.getInstance();
                        int year = now.get(Calendar.YEAR);
                        String strYear = String.valueOf(year);
                        String date = strYear + "-" + mouth + "-" + str2;
                        if (ToolUtils.isCurrentDay(date)) { // 表示今日
                            String today = "今日";
                            today_Txt.setText(ToolUtils.setTextSize(baseActivity, today, today.length() - 1, today.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                            // 课程开始时间
                            String startTime = info.get(mCurrentPosition).getLessonStartTime();
                            // 课程结束时间
                            String endTime = info.get(mCurrentPosition).getLessonEndTime();
                            String dateStr[] = info.get(mCurrentPosition).getLessonDate().split(" ");
                            String currentDate = dateStr[0].replaceAll("/", "-");
                            if (ToolUtils.isCurrentTimeDuring(currentDate, startTime, endTime)) { // 当前时间段
                                qr_layout.setVisibility(View.VISIBLE);
                            } else { // 非当前时间段
                                qr_layout.setVisibility(View.GONE);
                            }
                        } else { // 非今日情况
                            String dateStr = info.get(mCurrentPosition).getDate();
                            String newStr = dateStr.replace("日", "");
                            today_Txt.setText(ToolUtils.setTextSize(baseActivity, newStr, newStr.length() - 4, newStr.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                            qr_layout.setVisibility(View.GONE);
                        }
                    }
                }
                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    header_item_course.setY(0);
                    String strDate = info.get(mCurrentPosition).getDate();
                    String[] str = strDate.split("日");
                    String str1 = str[1];
                    String mouth = str1.substring(0, str1.lastIndexOf("月"));
                    System.out.println("mouth >>>:" + mouth);
                    String str2 = str[0];
                    Calendar now = Calendar.getInstance();
                    int year = now.get(Calendar.YEAR);
                    String strYear = String.valueOf(year);
                    String date = strYear + "-" + mouth + "-" + str2;
                    if (ToolUtils.isCurrentDay(date)) { // 表示今日
                        String today = "今日";
                        today_Txt.setText(ToolUtils.setTextSize(baseActivity, today, today.length() - 1, today.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                    } else { // 非今日情况
                        String dateStr = info.get(mCurrentPosition).getDate();
                        String newStr = dateStr.replace("日", "");
                        today_Txt.setText(ToolUtils.setTextSize(baseActivity, newStr, newStr.length() - 4, newStr.length(), R.style.style2, R.style.style3), TextView.BufferType.SPANNABLE);
                    }
                }
            }
        });
    }


    /**
     * 网络请求获取加载更多数据
     */
    private void loading() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
//            HttpPostService httpPostServie = retrofit.create(HttpPostService.class);
//            Observable<StudyBean> obserable = httpPostServie.get
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Load Finished!", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setText("历史");
        title_txt.setText(titleName);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.back_layout, R.id.register_txt, R.id.qr_layout})
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
            case R.id.qr_layout: // 二维码扫码监听
                if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.CAMERA}, 1);
//                    startActivityForResult(new Intent(baseActivity, CaptureActivity.class), 1);
                } else {
                    startActivityForResult(new Intent(baseActivity, CaptureActivity.class), 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (2 == resultCode) {
                String result = data.getStringExtra("result");
                System.out.println("back result >>>:" + result);
                if (null != result && !"null".equals(result)) {
                    if (NetUtil.isMobileConnected(this)) {
                        getSignInfo(result);
                    } else {
                        Toast.makeText(this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // TODO 扫码无返回值
                }
            }
        }
    }

    /**
     * 网络请求获取签到结果
     *
     * @param result
     */
    private void getSignInfo(String result) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", userId);
            obj.put("classId", classId);
            obj.put("lessonId", result);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.getSignInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
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
                                    Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                if (100 == commonBean.getCode()) {
                                    new SweetAlertDialog(baseActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                            .setCustomImage(R.drawable.icon_have_qrcourse)
                                            .setContentText("签到成功")
                                            .setConfirmText("确定")
                                            .setTitleText("")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            }).show();
                                } else {
                                    new SweetAlertDialog(baseActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                            .setCustomImage(R.drawable.icon_no_qrcourse)
                                            .setContentText("很抱歉你现在没有需要签到的课程")
                                            .setConfirmText("确定")
                                            .setTitleText("")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            }).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
