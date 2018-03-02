package jc.cici.android.atom.ui.study;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.live.LiveRoomActivity;
import cn.jun.live.vod.VodRoomActivity;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.mysetting.ShiZiPingYi;
import cn.jun.polyv.IjkVideoActicity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.MoreAdapter;
import jc.cici.android.atom.adapter.TestRecyclerAdapter;
import jc.cici.android.atom.adapter.VideoRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LessonChildInfo;
import jc.cici.android.atom.bean.LessonInfo;
import jc.cici.android.atom.bean.ReplayInfo;
import jc.cici.android.atom.bean.TestPagerInfo;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.NoteAllActivity;
import jc.cici.android.atom.ui.note.QuestionAllActivity;
import jc.cici.android.atom.ui.tiku.CardResultActivity;
import jc.cici.android.atom.ui.tiku.MyQuestionActivity;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import jc.cici.android.google.zxing.activity.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程对应上课阶段
 * Created by atom on 2017/5/13.
 */

public class ChapterActivity extends BaseActivity {

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
    @BindView(R.id.moreBtn)
    Button moreBtn;
    // 二维码扫描布局
    @BindView(R.id.qr_layout)
    RelativeLayout qr_layout;
    // 出勤图片
    @BindView(R.id.attendanceImg)
    ImageView attendanceImg;
    // 课程名
    @BindView(R.id.chapterCourseName_Txt)
    TextView chapterCourseName_Txt;
    // 授课时间
    @BindView(R.id.schoolTime_Txt)
    TextView schoolTime_Txt;
    // 类型图片
    @BindView(R.id.type_img)
    ImageView type_img;
    // 授课地点
    @BindView(R.id.chapterAddress_Txt)
    TextView chapterAddress_Txt;
    // 授课老师
    @BindView(R.id.lineTeach_img)
    ImageView lineTeach_img;
    // 做题
    @BindView(R.id.test_img)
    ImageView test_img;
    // 老师信息布局
    @BindView(R.id.teachInfo_layout)
    RelativeLayout teachInfo_layout;
    @BindView(R.id.teach_txt)
    TextView teach_txt;
    // 授课老师图片
    @BindView(R.id.teachImg)
    ImageView teachImg;
    // 授课老师姓名
    @BindView(R.id.teachName_Txt)
    TextView teachName_Txt;
    // 老师介绍
    @BindView(R.id.instruction_txt)
    TextView instruction_txt;
    // 面授试题布局
    @BindView(R.id.faceTestLayout)
    RelativeLayout faceTestLayout;
    // 面授视频布局
    @BindView(R.id.faceVideoLayout)
    RelativeLayout faceVideoLayout;
    // 视频列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 做题列表
    @BindView(R.id.testRecycler)
    RecyclerView testRecycler;
    // 浮动布局
    @BindView(R.id.floatLayout)
    RelativeLayout floatLayout;
    // 浮动按钮
    @BindView(R.id.huancun_icon_redBtn)
    Button huancun_icon_redBtn;
    // 标题名称
    private String titleName;
    // 学员id
    private int userId;
    // 班型id
    private int classId;
    // 班型名称
    private String className;
    // 阶段id
    private int stageId;
    // 课程id
    private int lessonId;
    // 课程名称
    private String lessName;
    // 笔记解锁
    private int stageNote;
    // 问题解锁
    private int stageProblem;
    // 资料解锁
    private int stageInformation;
    private Dialog dialog;
    private MoreAdapter adapter;
    private List<String> mLists = new ArrayList<>();
    private List<Integer> mICons = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private VideoRecyclerAdapter videoAdapter;
    private TestRecyclerAdapter testAdapter;
    private ArrayList<ReplayInfo> dataList;
    private ArrayList<TestPagerInfo> testList;
    // 整在下载数量
    private int downloadCount;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                int count = (int) msg.obj;
                downloadCount += count;
                // 设置缓存数量
                huancun_icon_redBtn.setText(String.valueOf(downloadCount));
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chapter;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        className = getIntent().getStringExtra("className");
        titleName = getIntent().getStringExtra("titleName");
        classId = getIntent().getIntExtra("classId", 0);
        lessonId = getIntent().getIntExtra("lessonId", 0);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId && 0 != lessonId) {
            try {
                obj.put("client", commParam.getClient());
                obj.put("userId", userId);
                obj.put("classId", classId);
                obj.put("stageId", stageId);
                obj.put("lessonId", lessonId);
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<CommonBean<LessonInfo>> observable = httpPostService.getLessonDetailInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<CommonBean<LessonInfo>>() {
                                @Override
                                public void onCompleted() {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (dialog != null && dialog.isShowing()) {
                                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(final CommonBean<LessonInfo> lessonInfoCommonBean) {

                                    lessName = lessonInfoCommonBean.getBody().getLessonName();
                                    // 课程名
                                    chapterCourseName_Txt.setText(lessName);
                                    // 授课时段
                                    int type = lessonInfoCommonBean.getBody().getLessonDateType();
                                    String[] arr = lessonInfoCommonBean.getBody().getLessonDate().split(" ");

                                    String str = arr[0].replaceAll("/", "-");
                                    // 课程开始时间
                                    String startTime = lessonInfoCommonBean.getBody().getLessonStartTime();
                                    // 课程结束时间
                                    String endTime = lessonInfoCommonBean.getBody().getLessonEndTime();
                                    if (type == 1) { // 上午
                                        schoolTime_Txt.setText(str
                                                + " 上午 " + startTime
                                                + " - " + endTime);
                                        title_txt.setText(arr[0] + "上午课程详情");

                                    } else if (type == 2) { // 中午
                                        schoolTime_Txt.setText(str
                                                + " 中午 " + startTime
                                                + " - " + endTime);
                                        title_txt.setText(arr[0] + "中午课程详情");
                                    } else { // 下午
                                        schoolTime_Txt.setText(str
                                                + " 下午 " + startTime
                                                + " - " + endTime);
                                        title_txt.setText(arr[0] + "下午课程详情");
                                    }
                                    if (1 == lessonInfoCommonBean.getBody().getLessonType()) { // 面授情况才有扫码签到功能
                                        // 设置时间段内扫描显示
                                        if (ToolUtils.isCurrentTimeDuring(str, startTime, endTime)) { // 当前时间段
                                            qr_layout.setVisibility(View.VISIBLE);
                                        } else { // 非当前时间段
                                            qr_layout.setVisibility(View.GONE);
                                        }
                                    } else {
                                        qr_layout.setVisibility(View.GONE);
                                    }


                                    // 判断是否出勤
                                    int attStatus = lessonInfoCommonBean.getBody().getAttendanceStatus();
                                    if (attStatus == 0) { // 表示缺勤
                                        attendanceImg.setBackgroundResource(R.drawable.icon_queqin);
                                        attendanceImg.setVisibility(View.VISIBLE);
                                    } else if (attStatus == 1) { // 出勤
                                        attendanceImg.setBackgroundResource(R.drawable.icon_chuqin);
                                        attendanceImg.setVisibility(View.VISIBLE);
                                    } else if (attStatus == 2) { // 未登记
                                        attendanceImg.setVisibility(View.GONE);
                                    }
                                    // 判断当前课程类型
                                    int lessonType = lessonInfoCommonBean.getBody().getLessonType();
                                    switch (lessonType) {
                                        case 1: // 面授
                                            teach_txt.setText("授课老师");
                                            lineTeach_img.setVisibility(View.VISIBLE);
                                            test_img.setVisibility(View.GONE);
                                            // 授课地址
                                            chapterAddress_Txt.setText(lessonInfoCommonBean.getBody().getLessonPlace());
                                            if (null != lessonInfoCommonBean.getBody().getTestPaperList()
                                                    && lessonInfoCommonBean.getBody().getTestPaperList().size() > 0) { // 试题是有内容
                                                faceTestLayout.setVisibility(View.VISIBLE);
                                                // 获取试卷内容
                                                testList = lessonInfoCommonBean.getBody().getTestPaperList();
                                                // 有答题情况，则答题
                                                if (null != testList && testList.size() > 0) {
                                                    testAdapter = new TestRecyclerAdapter(baseActivity, testList);
                                                    testRecycler.setAdapter(testAdapter);

                                                    // 测试列表item监听
                                                    testAdapter.setOnItemClickListener(
                                                            new BaseRecycleerAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(View view, int position) {
                                                                    // 学习状态改变设置
                                                                    changeDoTestStatus(testList.get(position).getPaperLessonChildId(), testList.get(position).getPaperStudyKey(), position);
                                                                }
                                                            }
                                                    );
                                                } else {
                                                    Toast.makeText(baseActivity, "暂无试题内容", Toast.LENGTH_SHORT).show();
                                                }
                                            } else { // 无试题情况
                                                faceTestLayout.setVisibility(View.GONE);
                                            }
                                            if (null != lessonInfoCommonBean.getBody().getReplayVideoList()
                                                    && lessonInfoCommonBean.getBody().getReplayVideoList().size() > 0) { // 有视频
                                                faceVideoLayout.setVisibility(View.VISIBLE);
                                                floatLayout.setVisibility(View.VISIBLE);

                                                dataList = lessonInfoCommonBean.getBody().getReplayVideoList();
                                                if (null != dataList && dataList.size() > 0) {
                                                    videoAdapter = new VideoRecyclerAdapter(baseActivity, dataList, handler, classId, className, stageId, stageNote, stageProblem, stageInformation);
                                                    recyclerView.setAdapter(videoAdapter);

                                                    // item 设置监听
                                                    videoAdapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(View view, int position) {
                                                            // 学习状态改变设置
                                                            changeDoTestStatus(dataList.get(position).getVideoLessonChildId(), dataList.get(position).getVideoStudyKey(), position);
                                                        }
                                                    });

                                                    // 获取当前缓存视频列表
                                                    List<VideoItemBean> bufferingList = VideoDownloadManager.getInstance().getBufferingVideos();
                                                    if (null != bufferingList && bufferingList.size() > 0) {
                                                        downloadCount = bufferingList.size();
                                                    } else {
                                                        downloadCount = 0;
                                                    }
                                                    // 缓存中数量
                                                    huancun_icon_redBtn.setText(String.valueOf(downloadCount));
                                                    // 查看缓存情况
                                                    floatLayout.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            baseActivity.openActivity(ManagerActivity.class);
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(baseActivity, "暂无下载内容", Toast.LENGTH_SHORT).show();
                                                }


                                            } else { // 无视频情况
                                                faceVideoLayout.setVisibility(View.GONE);
                                                floatLayout.setVisibility(View.GONE);
                                            }
                                            break;
                                        case 2: // 直播
                                            teach_txt.setText("授课老师");
                                            lineTeach_img.setVisibility(View.VISIBLE);
                                            test_img.setVisibility(View.GONE);
                                            type_img.setBackgroundResource(R.drawable.icon_zhibo);
                                            if (1 == lessonInfoCommonBean.getBody().getLiveStatus()) { // 正在直播
                                                chapterAddress_Txt.setText("观看直播");
                                                chapterAddress_Txt.setTextColor(Color.parseColor("#dd5555"));
                                                chapterAddress_Txt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                                chapterAddress_Txt.getPaint().setAntiAlias(true);//抗锯齿
                                                chapterAddress_Txt.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent zhiboIt = new Intent(baseActivity, LiveRoomActivity.class);
                                                        Bundle zbBundle = new Bundle();
                                                        zbBundle.putInt("scheduleId", lessonId);
                                                        zbBundle.putInt("classid", classId);
                                                        zhiboIt.putExtras(zbBundle);
                                                        baseActivity.startActivity(zhiboIt);
                                                    }
                                                });
                                            } else if (2 == lessonInfoCommonBean.getBody().getLiveStatus()) { // 直播已结束
                                                if (1 == lessonInfoCommonBean.getBody().getCS_IsPlayback()) {
                                                    String charStr = "您已错过本场直播，观看回放";
                                                    SpannableString spanText = new SpannableString(charStr);
                                                    spanText.setSpan(new ClickableSpan() {

                                                        @Override
                                                        public void updateDrawState(TextPaint ds) {
                                                            super.updateDrawState(ds);
                                                            ds.setColor(Color.parseColor("#dd5555"));       //设置文件颜色
                                                            ds.setUnderlineText(true);      //设置下划线
                                                        }

                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent zhiboIt = new Intent(baseActivity, VodRoomActivity.class);
                                                            Bundle zbBundle = new Bundle();
                                                            zbBundle.putInt("scheduleId", lessonId);
                                                            zbBundle.putInt("classid", classId);
                                                            zhiboIt.putExtras(zbBundle);
                                                            baseActivity.startActivity(zhiboIt);
                                                        }
                                                    }, spanText.length() - 4, spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    chapterAddress_Txt.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
                                                    chapterAddress_Txt.setText(spanText);
                                                    chapterAddress_Txt.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
                                                } else { // 无回放情况
                                                    chapterAddress_Txt.setText("你已经错过本场直播，无回放 ");
                                                }
                                            } else if (0 == lessonInfoCommonBean.getBody().getLiveStatus()) { // 直播未开始
                                                chapterAddress_Txt.setText("直播暂未开始");
                                            }

                                            if (null != lessonInfoCommonBean.getBody().getTestPaperList()
                                                    && lessonInfoCommonBean.getBody().getTestPaperList().size() > 0) { // 试题有内容
                                                faceTestLayout.setVisibility(View.VISIBLE);
                                                // 获取试卷内容
                                                testList = lessonInfoCommonBean.getBody().getTestPaperList();
                                                // 有答题情况，则答题
                                                if (null != testList && testList.size() > 0) {
                                                    testAdapter = new TestRecyclerAdapter(baseActivity, testList);
                                                    testRecycler.setAdapter(testAdapter);

                                                    // 测试列表item监听
                                                    testAdapter.setOnItemClickListener(
                                                            new BaseRecycleerAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(View view, int position) {
                                                                    // 学习状态改变设置
                                                                    changeDoTestStatus(testList.get(position).getPaperLessonChildId(), testList.get(position).getPaperStudyKey(), position);
                                                                }
                                                            }
                                                    );
                                                } else {
                                                    Toast.makeText(baseActivity, "暂无试题内容", Toast.LENGTH_SHORT).show();
                                                }
                                            } else { // 试题无内容
                                                faceTestLayout.setVisibility(View.GONE);
                                            }

                                            break;
                                        case 3: // 测试
                                            lineTeach_img.setVisibility(View.GONE);
                                            if (null != lessonInfoCommonBean.getBody().getTestPaperList()
                                                    && lessonInfoCommonBean.getBody().getTestPaperList().size() > 0) { // 试题是有内容
                                                faceTestLayout.setVisibility(View.VISIBLE);
                                                // 获取试卷内容
                                                testList = lessonInfoCommonBean.getBody().getTestPaperList();
                                                // 有答题情况，则答题
                                                if (null != testList && testList.size() > 0) {
                                                    testAdapter = new TestRecyclerAdapter(baseActivity, testList);
                                                    testRecycler.setAdapter(testAdapter);

                                                    // 测试列表item监听
                                                    testAdapter.setOnItemClickListener(
                                                            new BaseRecycleerAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(View view, int position) {
                                                                    // 学习状态改变设置
                                                                    changeDoTestStatus(testList.get(position).getPaperLessonChildId(), testList.get(position).getPaperStudyKey(), position);
                                                                }
                                                            }
                                                    );
                                                } else {
                                                    Toast.makeText(baseActivity, "暂无试题内容", Toast.LENGTH_SHORT).show();
                                                }
                                            } else { // 无试题情况
                                                faceTestLayout.setVisibility(View.GONE);
                                            }

                                            break;
                                        default:
                                            break;
                                    }

                                    // 授课老师姓名
                                    teachName_Txt.setText(ToolUtils.strReplaceAll(lessonInfoCommonBean.getBody().getTeacherName()));
                                    // 授课老师图片
                                    // 设置item中Image布局
                                    Glide.with(baseActivity).load(lessonInfoCommonBean.getBody().getTeacherImg())
                                            .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                                            .error(R.drawable.icon_banzhuren) //加载失败时显示的图片
                                            .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                                            .override(280, 186) // 设置最终显示图片大小
                                            .centerCrop() // 中心剪裁
                                            .skipMemoryCache(true) // 跳过缓存
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                                            .transform(new GlideCircleTransform(baseActivity)) // 设置圆角
                                            .into(teachImg);
                                    instruction_txt.setText("讲师");
                                }
                            }
                    );

        } else {
            Toast.makeText(baseActivity, "当前用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 网络请求监听答题情况
     */
    private void changeDoTestStatus(int lessChildId, String studyKey, final int position) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", userId);
            obj.put("lessonChildId", lessChildId);
            obj.put("studyKey", studyKey);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<LessonChildInfo>> observable = httpPostService.getClassLessonChildInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<LessonChildInfo>>() {
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
                                    Toast.makeText(baseActivity, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<LessonChildInfo> lessonChildInfoCommonBean) {
                                if (100 == lessonChildInfoCommonBean.getCode()) {
                                    // 判断是视频还是试卷
                                    int type = lessonChildInfoCommonBean.getBody().getServerType();
                                    switch (type) {
                                        case 1: // 视频
                                            String vid = lessonChildInfoCommonBean.getBody().getVideoVID();
                                            String videoId = String.valueOf(lessonChildInfoCommonBean.getBody().getVideoID());
                                            String StudyKey = dataList.get(position).getVideoStudyKey();
                                            String title = lessonChildInfoCommonBean.getBody().getVideoName();
                                            String strClassId = String.valueOf(classId);
                                            String strStageId = String.valueOf(stageId);
                                            String strStageNote = String.valueOf(stageNote);
                                            String strStageProblem = String.valueOf(stageProblem);
                                            String strStageInfo = String.valueOf(stageInformation);
                                            if (null != vid && !"".equals(vid)) {
                                                IjkVideoActicity.intentTo(baseActivity, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, vid,
                                                        true, title, strClassId, strStageId, videoId, StudyKey, vid, "0", strStageProblem, strStageNote, strStageInfo);
                                            }
                                            break;
                                        case 2: // 试卷
                                            if (1 == testList.get(position).getPaperStatus()) { // 答题已完成
                                                Intent cIntent = new Intent(baseActivity,
                                                        CardResultActivity.class);
                                                // 传递试卷id
                                                cIntent.putExtra("TestPPKID", lessonChildInfoCommonBean.getBody().getPaperID());
                                                // 班级id
                                                cIntent.putExtra("classId", classId);
                                                // 阶段id
                                                cIntent.putExtra("stageId", stageId);
                                                // 课程id
                                                cIntent.putExtra("lessonId", lessonId);
                                                cIntent.putExtra("isOnline", 0);
                                                cIntent.putExtra("LessonChildId", testList.get(position).getPaperLessonChildId());
                                                // 试卷名
                                                cIntent.putExtra("name", lessonChildInfoCommonBean.getBody().getPaperName());
                                                baseActivity.startActivity(cIntent);
                                            } else { // 答题未完成状态
                                                Intent it = new Intent(baseActivity, MyQuestionActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("paperId", lessonChildInfoCommonBean.getBody().getPaperID());
                                                bundle.putInt("classid", classId);
                                                bundle.putInt("stageid", stageId);
                                                bundle.putInt("lessonid", lessonId);
                                                bundle.putString("title", lessonChildInfoCommonBean.getBody().getPaperName());
                                                // 面授情况传递
                                                bundle.putInt("PaperLessonChildId", testList.get(position).getPaperLessonChildId());
                                                it.putExtras(bundle);
                                                startActivity(it);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    Toast.makeText(baseActivity, lessonChildInfoCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 初始视图内容
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        if (0 == stageNote && 0 == stageProblem) { // 笔记答疑均为锁定状态
            moreBtn.setVisibility(View.GONE);
        } else {
            moreBtn.setBackgroundResource(R.drawable.icon_more);
            moreBtn.setVisibility(View.VISIBLE);
        }
        register_txt.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 测试列表
        testRecycler.setLayoutManager(new LinearLayoutManager(baseActivity));
    }

    @OnClick({R.id.back_layout, R.id.qr_layout, R.id.share_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.qr_layout: // 二维码扫描
                if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.CAMERA}, 1);
//                    startActivityForResult(new Intent(baseActivity, CaptureActivity.class), 1);
                } else {
                    startActivityForResult(new Intent(baseActivity, CaptureActivity.class), 1);
                }

                break;
            case R.id.share_layout: // 更多布局监听
                mLists.clear();
                mICons.clear();
                if (1 == stageNote) {
                    mLists.add("本课笔记");
                    mICons.add(R.drawable.icon_note);
                }
                if (1 == stageProblem) {
                    mLists.add("本课提问");
                    mICons.add(R.drawable.icon_que);
                }
                adapter = new MoreAdapter(this, mLists, mICons, 0);
                /** 获取宽度 **/
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                final IndicatorDialog dialog = new IndicatorBuilder(baseActivity)
                        .width(width / 2 - 25)
                        .height(-1)
                        .animator(R.style.popAnimation)
                        .ArrowDirection(IndicatorBuilder.TOP) // 箭头方向
                        .bgColor(Color.WHITE) // 背景色
                        .gravity(IndicatorBuilder.GRAVITY_RIGHT)
                        .radius(10)
                        .ArrowRectage(0.9f)
                        .layoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                        .adapter(adapter).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show(share_layout);

                adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0: // 笔记监听
                                if ("本课笔记".equals(mLists.get(0).toString())) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("cspkid", lessonId);
                                    bundle.putInt("classId", classId);
                                    bundle.putInt("stageId", stageId);
                                    baseActivity.openActivity(NoteAllActivity.class, bundle);
                                    dialog.dismiss();
                                } else if ("本课提问".equals(mLists.get(0).toString())) {
                                    Bundle quesBundle = new Bundle();
                                    quesBundle.putInt("classId", classId);
                                    quesBundle.putInt("lessonId", lessonId);
                                    quesBundle.putInt("stageId", stageId);
                                    baseActivity.openActivity(QuestionAllActivity.class, quesBundle);
                                    dialog.dismiss();
                                }

                                break;
                            case 1: // 问题监听
                                if ("本课笔记".equals(mLists.get(1).toString())) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("cspkid", lessonId);
                                    bundle.putInt("classId", classId);
                                    bundle.putInt("stageId", stageId);
                                    baseActivity.openActivity(NoteAllActivity.class, bundle);
                                    dialog.dismiss();
                                } else if ("本课提问".equals(mLists.get(1).toString())) {
                                    Bundle quesBundle = new Bundle();
                                    quesBundle.putInt("classId", classId);
                                    quesBundle.putInt("lessonId", lessonId);
                                    quesBundle.putInt("stageId", stageId);
                                    baseActivity.openActivity(QuestionAllActivity.class, quesBundle);
                                    dialog.dismiss();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
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
                    Toast.makeText(this, "无扫描结果", Toast.LENGTH_SHORT).show();
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
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        userId = commParam.getUserId();
        if (0 != userId && 0 != classId) {
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
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(CommonBean commonBean) {
                                    if (100 == commonBean.getCode()) {
                                        // 判断返回body内容
                                        String status = String.valueOf(commonBean.getBody());
                                        if ("1.0".equals(status)) {
                                            final Dialog dd = new Dialog(baseActivity,
                                                    R.style.NormalDialogStyle);
                                            dd.setContentView(R.layout.dialog_sign_up);
                                            dd.setCanceledOnTouchOutside(false);
                                            dd.show();
                                            // 取消按钮
                                            Button goBack_btn = (Button) dd.findViewById(R.id.goBack_btn);
                                            // 前往师资评议
                                            Button sign_Btn = (Button) dd.findViewById(R.id.sign_Btn);
                                            // 前往师资评议
                                            sign_Btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent it = new Intent(baseActivity, ShiZiPingYi.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("type", 0);
                                                    it.putExtras(bundle);
                                                    baseActivity.startActivityForResult(it, 1);
                                                    dd.dismiss();
                                                }
                                            });
                                            goBack_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dd.dismiss();
                                                }
                                            });
                                        } else {
                                            final Dialog nd = new Dialog(baseActivity,
                                                    R.style.NormalDialogStyle);
                                            nd.setContentView(R.layout.dialog_sign_down);
                                            nd.setCanceledOnTouchOutside(false);
                                            nd.show();
                                            Button go_Btn = (Button) nd.findViewById(R.id.go_Btn);
                                            go_Btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    nd.dismiss();
                                                }
                                            });
                                        }
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
                                }
                            }
                    );
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitle("");
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
