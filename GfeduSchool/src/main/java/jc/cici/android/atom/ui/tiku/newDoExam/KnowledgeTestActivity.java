package jc.cici.android.atom.ui.tiku.newDoExam;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.FavorFlag;
import jc.cici.android.atom.ui.tiku.Card;
import jc.cici.android.atom.ui.tiku.CardAnswer;
import jc.cici.android.atom.ui.tiku.CardResultActivity;
import jc.cici.android.atom.ui.tiku.Const;
import jc.cici.android.atom.ui.tiku.FileUtils;
import jc.cici.android.atom.ui.tiku.MyFragmentPageAdapter;
import jc.cici.android.atom.ui.tiku.SubmitQuesAnswer;
import jc.cici.android.atom.ui.tiku.ViewPagerScroller;
import rx.Observable;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.KnowledgeQuesBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by atom on 2018/1/9.
 */

public class KnowledgeTestActivity extends BaseActivity implements TestContentFragment.TimeData {

    private PowerManager pManager;
    private PowerManager.WakeLock mWakeLock;
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
    // 搜索更多布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 搜索按钮
    @BindView(R.id.noteMore_Btn)
    Button noteMore_Btn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 隐藏布局
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 倒计时
    @BindView(R.id.txt_time)
    TextView txt_time;
    // 收藏夹
    @BindView(R.id.txt_favor)
    TextView txt_favor;
    // 标记
    @BindView(R.id.txt_mark)
    TextView txt_mark;
    // 答题卡
    @BindView(R.id.txt_card)
    TextView txt_card;
    // viewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 知识点id
    private int knowledgeId;
    // 项目id
    private int projectId;
    // 1:我的收藏 2：我的错题
    private int searchType;
    // 答案类型1:表示每日一题，2表示知识点做题
    private int answerType;
    // 标题
    private String title;
    // 试卷id
    private int paperId;
    // 进度对话框，弹出对话框
    private Dialog mDialog, dialog;
    private ArrayList<KnowledgeQuesBean.QuestionCard> datas = new ArrayList<>();
    //
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    private MyFragmentPageAdapter mAdapter;
    // 提交答题时间
    private static String mTime;
    // 获取考卷时间
    private String haveTime;
    private int totalTime;
    // 倒计时时间
    public String time;
    // 创建计时器对象
    private Timer timter = new Timer();
    TimerTask timerTask;
    private long i = 0;
    // 考卷时间转化为时间值
    private long t;
    private boolean isStop = false;
    private boolean isGoing = false;
    private static long storeTime;
    //记录上一次滑动的positionOffsetPixels值
    private int lastValue = -1;
    private boolean isLeft = true;
    // 当前页面位置
    private int pos;
    private int currPos;
    // 判断用户是否滑动过
    private boolean isTouchPager = false;
    // 是否添加标记 默认未添加
    private boolean flag = false;
    // 答题卡列表
    private ArrayList<Card> cardList = new ArrayList<Card>();
    // 收藏列表
    private ArrayList<FavorFlag> favorStatusList = new ArrayList<FavorFlag>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 普通跳转
                    int nextPage = (Integer) msg.obj;
                    if (viewPager != null && nextPage >= 0) {
                        viewPager.setCurrentItem(nextPage + 1);
                    }
                    break;
                case 1:// 点击跳转
                    int jumpPage = (Integer) msg.obj;
                    if (viewPager != null && jumpPage > 0) {
                        viewPager.setCurrentItem(jumpPage);
                    } else if (jumpPage == 0) {
                        viewPager.setCurrentItem(0);
                    }
                    break;
                case 2: // 设置时间戳
                    if (haveTime != null && totalTime != 0) {
                        if (t > 0) { // 倒计时
                            t--;
                            int totalSec = (int) t;
                            int min = (totalSec / 60);
                            int sec = (totalSec % 60);
                            time = String.format("%1$02d:%2$02d", min, sec);
                            txt_time.setText(time);
                            String currentTime = txt_time.getText().toString().trim();
                            String[] str = currentTime.split(":");
                            int doTime = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
                            subMitTime = doTime;
                        }
//                        else { // 倒计时后自动提交试卷
//                            // 创建提交试卷提示对话框
//                            txt_time.setText("00:00");
////                            mTime = "00:00";
//                            createSubmitDialog();
//                            if (null != timter) {
//                                timter.cancel();
//                            }
//                            if (null != timerTask) {
//                                timerTask.cancel();
//                            }
//                        }
                    } else {
                        i++;
                        int totalSec = (int) i;
                        int min = (totalSec / 60);
                        int sec = (totalSec % 60);
                        time = String.format("%1$02d:%2$02d", min, sec);
                        txt_time.setText(time);
//                        mTime = time;
                    }
                    break;
                case 3: // 开屏时间
                    if (storeTime > 0) {
                        t = storeTime;
                        if (flag) {
                            timter = new Timer(true);
                            timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(2);
                                }
                            };
                            // 启动定时器
                            timter.schedule(timerTask, 0, 1000);
                        }
                    }
                    break;
                case 4: // 锁屏
                    storeTime = t;
                    flag = true;
                    if (null != timerTask && null != timter) {
                        timter.cancel();
                        timter.purge();
                        timerTask.cancel();
                        timter = null;
                        timerTask = null;
                    }
                    break;
                case 5:
                    break;
                case 9: // 跳转
                    goToCardAnswerActivity();
                    break;
                case 10:
                    if (null != mDialog && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
            }
        }
    };
    public static int subMitTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
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
        knowledgeId = getIntent().getIntExtra("knowledgeId", 0);
        projectId = getIntent().getIntExtra("projectId", 0);
        title = getIntent().getStringExtra("title");
        // 答案类型
        answerType = getIntent().getIntExtra("answerType", 0);
        System.out.println("knowledgeId >>>:" + knowledgeId);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            initData();
        } else {
            Toast.makeText(baseActivity, "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
        Observable<CommonBean<KnowledgeQuesBean>> observable = httpPostService.getKnowledgeQuestInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<KnowledgeQuesBean>>() {
                    @Override
                    public void onCompleted() {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Toast.makeText(baseActivity, "网络加载异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<KnowledgeQuesBean> knowledgeQuesBeanCommonBean) {
                        if (100 == knowledgeQuesBeanCommonBean.getCode()) {
                            paperId = knowledgeQuesBeanCommonBean.getBody().getPaperId();
                            // 获取总时长
                            totalTime = knowledgeQuesBeanCommonBean.getBody().getTpaperTime();
                            // 获取上次做题时间
                            int lastDoTime = knowledgeQuesBeanCommonBean.getBody().getLastDoTime();
                            if (lastDoTime > 0) {
                                haveTime = String.valueOf(lastDoTime);
                                t = lastDoTime;
                            } else {
                                haveTime = String.valueOf(totalTime);
                                t = totalTime;
                            }
                            // 获取试卷列表
                            ArrayList<KnowledgeQuesBean.QuestionCard> mList = knowledgeQuesBeanCommonBean.getBody().getPaperQuesCardList();
                            if (null != mList && !"null".equals(mList) && mList.size() > 0) {
                                if (datas.size() > 0) {
                                    datas.clear();
                                }
                                datas.addAll(mList);
                                for (int i = 0; i < datas.size(); i++) {
                                    Card card = new Card();
                                    // 设置答题卡个数
                                    // 如果已经答题了，设置状态值为true
                                    if (!"".equals(datas.get(i).getQuesUserAnswer())) {
                                        card.setStatus(true);
                                    } else {// 否则设置为false
                                        // 设置点击状态，默认为未点击
                                        card.setStatus(false);
                                    }
                                    card.setSize(i);
                                    // 设置标记，默认为未标记
                                    card.setFlag(false);
                                    // 设置初始状态位置
                                    card.setPosition(i);
                                    cardList.add(card);

                                    FavorFlag favorFlag = new FavorFlag();
                                    favorFlag.setfPos(i);
                                    favorFlag.setFavorFlag(datas.get(i)
                                            .getHasCollect());
                                    favorStatusList.add(favorFlag);

                                    Fragment f = new TestContentFragment(handler);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("userId", userId);
                                    bundle.putString("title", title);
                                    // 试题号
                                    bundle.putInt("quesId", datas.get(i).getQuesId());
                                    // 试题类型(单选，多选，填空，简答等)
                                    bundle.putString("quesType", datas.get(i).getQuesType());
                                    // 选项数量
                                    bundle.putInt("quesOptionCount", datas.get(i).getQuesOptionCount());
                                    //  用户答案
                                    bundle.putString("quesUserAnswer", datas.get(i).getQuesUserAnswer());
                                    //  用户图片数量
                                    bundle.putInt("quesUserAnswerImgsCount", datas.get(i).getQuesUserAnswerImgsCount());
                                    // 用户图片列表
                                    bundle.putStringArrayList("quesUserAnswerImgs", datas.get(i).getQuesUserAnswerImgs());
                                    // 试题链接
                                    bundle.putString("quesUrl", datas.get(i).getQuesUrl());
                                    // 答题卡列表
                                    bundle.putSerializable("cardList", cardList);
                                    // 题目数量
                                    bundle.putInt("size", datas.size());
                                    // 答案类型
                                    bundle.putInt("answerType", answerType);
                                    bundle.putInt("paperId", paperId);
                                    // 当前页
                                    bundle.putInt("currPage", i);
                                    // 在线面授or其他
                                    bundle.putString("studyKey", "");
                                    f.setArguments(bundle);
                                    FragmentList.add(f);
                                    mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), FragmentList);
                                    viewPager.setAdapter(mAdapter);
                                }
                                // 设置是否收藏
                                if (pos > 0) {
                                    if (1 == datas.get(pos).getHasCollect()) {
                                        txt_favor.setSelected(true);
                                    } else {
                                        txt_favor.setSelected(false);
                                    }
                                } else {
                                    if (1 == datas.get(0).getHasCollect()) {
                                        txt_favor.setSelected(true);
                                    }
                                }

                                // 上次做题位置
                                int jumpStr = knowledgeQuesBeanCommonBean.getBody().getLastDoNo();
                                ViewPagerScroller scroller = new ViewPagerScroller(
                                        baseActivity);
                                scroller.initViewPagerScroll(viewPager);
                                viewPager.setAdapter(mAdapter);
                                viewPager.addOnPageChangeListener(onPageChangeListener);
                                title_txt.setText(title);
                                // 启动时间戳
                                start();
                                if (jumpStr != 0) {
                                    viewPager.setCurrentItem(jumpStr - 1);
                                    // 显示当前做题位置
                                    register_txt.setText(jumpStr + "/" + datas.size());
                                }
                            } else {
                                register_txt.setText(1 + "/" + datas.size());

                            }
                            register_txt.setTextColor(Color.parseColor("#333333"));
                        } else {
                            Toast.makeText(baseActivity, knowledgeQuesBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * vp滑动监听
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private boolean flag;

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    //拖的时候才进入下一页
                    flag = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    flag = true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    /**
                     * 判断是不是最后一页，同是是不是拖的状态
                     */
                    if (viewPager.getCurrentItem() == mAdapter.getCount() - 1 && !flag) {
                        goToCardAnswerActivity();
                    }
                    flag = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int positionOffsetPixels) {
            if (arg1 != 0) {
                if (lastValue >= positionOffsetPixels) {
                    //右滑
                    isLeft = false;
                } else if (lastValue < positionOffsetPixels) {
                    //左滑
                    isLeft = true;
                }
            }
            lastValue = positionOffsetPixels;

        }

        @Override
        public void onPageSelected(int arg0) {

            pos = arg0 + 1;
            if (0 == arg0) {
                currPos = 0;
            } else {
                currPos = arg0 - 1;
            }
            // 显示当前做题位置
            register_txt.setText(pos + "/" + datas.size());
            register_txt.setTextColor(Color.parseColor("#333333"));
            // 判断是否收藏
            if (1 == favorStatusList.get(arg0).getFavorFlag()) { // 收藏
                txt_favor.setSelected(true);
            } else { // 未收藏
                txt_favor.setSelected(false);
            }
            if ("6".equals(datas.get(currPos).getQuesType())) {
                if (isLeft) { // 只有向左滑动时候才提交数据
                    jiandaSubmit(TestContentFragment.stringList.get(currPos));
                }
            }
            isTouchPager = true;
            // 答题时间赋值给mTime用于提交后台
            if (totalTime != 0) {
                long aTime = Long.parseLong(haveTime);
                long s = aTime - t;
                int totalSec = (int) s;
                mTime = String.valueOf(totalSec);
                System.out.println("mTime >>>:" + mTime);
            } else {
                mTime = time;
            }
            // 设置标记图片
            if (cardList.get(arg0).isFlag()) { // 标记情况
                txt_mark.setSelected(true);
            } else { // 未标记情况
                txt_mark.setSelected(false);
            }
        }

    };

    private void jiandaSubmit(String s) {
        // 添加网络请求
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Const.URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        try {
            CommParam commParam = new CommParam(baseActivity);
            obj.put("userId", commParam.getUserId());
            obj.put("appName", commParam.getAppname());
            obj.put("oauth", commParam.getOauth());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("version", commParam.getVersion());
            // 试卷id
            obj.put("paperId", paperId);
            // 试题id
            obj.put("quesId", datas.get(currPos).getQuesId());
            // 用户答案
            obj.put("userQuesAnswer", s);
            JSONArray array = new JSONArray(TestContentFragment.imgList.get(currPos));
            obj.put("base64Img", array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<SubmitQuesAnswer>> observable = httpPostService.getSubmitUserqQuesAnswer(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubmitQuesAnswer>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(baseActivity, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<SubmitQuesAnswer> submitQuesAnswerCommonBean) {
                                if (100 == submitQuesAnswerCommonBean.getCode()) {
                                    for (Card c : cardList) {
                                        if (c.getPosition() == currPos) {
                                            c.setStatus(true);
                                            cardList.set(currPos, c);
                                        }
                                    }
                                    // 清除缓存
                                    PictureFileUtils.deleteCacheDirFile(baseActivity);
                                } else {
                                    Toast.makeText(baseActivity, submitQuesAnswerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 最后一页跳转答题卡
     */
    private void goToCardAnswerActivity() {
        Intent cardAswerIntent = new Intent(this,
                CardAnswer.class);
        cardAswerIntent.putExtra("pageStatus", cardList);
        cardAswerIntent.putExtra("userId", userId);
        cardAswerIntent.putExtra("TestPaperID", paperId);
        cardAswerIntent.putExtra("classId", 0);
        cardAswerIntent.putExtra("stageId", 0);
        cardAswerIntent.putExtra("lessonId", 0);
        cardAswerIntent.putExtra("isOnline", 0);
        cardAswerIntent.putExtra("levelId", 0);
        cardAswerIntent.putExtra("LessonChildId", 0);
        // 传递标题
        cardAswerIntent.putExtra("name", title);
        cardAswerIntent.putExtra("studyKey", "");
        if (totalTime != 0) {
            long aTime = Long.parseLong(haveTime);
            long s = aTime - t;
            int totalSec = (int) s;
            mTime = String.valueOf(totalSec);
        }
        cardAswerIntent.putExtra("time", mTime);
        cardAswerIntent.putExtra("CardAnwserType", "KnowledgeTestActivity");
        startActivityForResult(cardAswerIntent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    private RequestBody commonPramas() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("KnowledgeId", knowledgeId);
            obj.put("projectId", projectId);
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

    /**
     * 重新启动计时
     */
    private void restart() {
        if (timter != null) {
            timter.cancel();
            timter = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        isStop = false;
        isGoing = false;
        start();
    }

    /**
     * 计时器暂停
     */
    private void stop() {
        isStop = !isStop;
    }

    /**
     * 开始计时
     */
    private void start() {
        if (isGoing) {
            isStop = false;
            return;
        }
        if (timter == null) {
            timter = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                public void run() {
                    if (isStop) {
                        return;
                    }
                    handler.sendEmptyMessage(2);
                }
            };
        }
        if (timter != null && timerTask != null) {
            timter.schedule(timerTask, 0, 1000);

        }
        isGoing = true;
    }


    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    private void initView() {

        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText(title);
    }

    public String getTime() {
        if (mTime != null) { // 答题时间不为0情况
            return mTime;
        } else {
            return "00:00";
        }
    }


    @OnClick({R.id.back_layout, R.id.txt_time, R.id.txt_favor, R.id.txt_mark, R.id.txt_card})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                stop();
                int doCount = 0;
                dialog = new Dialog(baseActivity,
                        R.style.NormalDialogStyle);
                dialog.setContentView(R.layout.dialog_back);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                TextView txt = (TextView) dialog.findViewById(R.id.txt_dialog);
                // 继续做题
                Button go_Btn = (Button) dialog.findViewById(R.id.go_on);
                // 退出答题
                Button sumbit_btn = (Button) dialog.findViewById(R.id.sumbit_btn);

                for (Card c : cardList) {
                    if (c.isStatus() == true) {
                        doCount++;
                    }
                }

                if (doCount < cardList.size()) {
                    txt.setText("总共" + cardList.size() + "道题,未答" + (cardList.size() - doCount) + "道题");
                } else {
                    txt.setText("总共" + cardList.size() + "道题,未答" + (cardList.size() - doCount) + "道题");
                }
                sumbit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restart();
                        // 提交答案
                        submitTest();
                    }
                });
                go_Btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog.dismiss();
                        baseActivity.finish();

                    }
                });
                break;
            case R.id.txt_time: // 倒计时
                txt_time.setSelected(true);
                stop();
                dialog = new Dialog(baseActivity,
                        R.style.NormalDialogStyle);
                dialog.setContentView(R.layout.pause_dialog);
                dialog.setCanceledOnTouchOutside(false);
                // 继续做题
                Button goOnBtn = (Button) dialog.findViewById(R.id.goOnBtn);
                goOnBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog.dismiss();
                        txt_time.setSelected(false);

                    }
                });
                dialog.show();
                break;
            case R.id.txt_favor: // 收藏夹
                if (pos == 0) {
                    if (favorStatusList.get(0).getFavorFlag() == 0) { // 未收藏情况
                        // 请求接口收藏
                        doCollect(datas.get(0).getQuesId());
                    } else { // 已收藏情况
                        // 取消收藏
                        cancelCollect(datas.get(0).getQuesId());
                    }
                } else {
                    if (favorStatusList.get(pos - 1).getFavorFlag() == 0) { // 未收藏情况
                        // 请求接口收藏
                        doCollect(datas.get(pos - 1).getQuesId());
                    } else { // 已收藏情况
                        // 取消收藏
                        cancelCollect(datas.get(pos - 1).getQuesId());
                    }
                }
                break;
            case R.id.txt_mark: // 标记
                for (Card card : cardList) {
                    boolean f = cardList.get(card.getPosition()).isFlag();
                    if (isTouchPager == false) { // 未滑动情况
                        if (card.getPosition() == 0 && pos == 0) {
                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(0, card);

                        }
                        if (card.getPosition() == pos - 1) {

                            if (f) {
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(false);
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(pos - 1, card);
                        }
                    } else { // 滑动情况
                        if (card.getPosition() == 0 && pos == 1) {
                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(0, card);
                        }

                        if (card.getPosition() == pos - 1) {

                            if (f) {
                                card.setFlag(false);
                                Toast.makeText(this, "取消标记", Toast.LENGTH_LONG)
                                        .show();
                                txt_mark.setSelected(false);
                            } else {
                                Toast.makeText(this, "标记成功", Toast.LENGTH_LONG)
                                        .show();
                                card.setFlag(true);
                                txt_mark.setSelected(true);
                            }
                            cardList.set(pos - 1, card);
                        }
                    }

                }

                break;
            case R.id.txt_card: // 答题卡
                if (cardList != null && cardList.size() > 0) {
                    Intent asIntent = new Intent(this, CardAnswer.class);
                    asIntent.putExtra("pageStatus", cardList);
                    asIntent.putExtra("userId", userId);
                    asIntent.putExtra("TestPaperID", paperId);
                    if (totalTime != 0) {
                        long aTime = Long.parseLong(haveTime);
                        long s = aTime - t;
                        int totalSec = (int) s;
                        mTime = String.valueOf(totalSec);
                    } else {
                        asIntent.putExtra("time", time);
                    }
                    // 传递学习标题
                    asIntent.putExtra("name", title);
                    asIntent.putExtra("studyKey", "");
                    asIntent.putExtra("CardAnwserType", "My_Fragment");
                    // 启动带返回值得Intent 其中 1表示请求的requsetCode
                    startActivityForResult(asIntent, 2);
                    overridePendingTransition(R.anim.slide_bottom_in, 0);
                }
                break;
        }
    }

    /**
     * 取消收藏
     *
     * @param quesId
     */
    private void cancelCollect(int quesId) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonCollectPramas(quesId);
        Observable<CommonBean> observable = httpPostService.cancelCollQuestInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(baseActivity, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            for (FavorFlag f : favorStatusList) {
                                if (pos == 0 && f.getfPos() == pos) {
                                    f.setFavorFlag(0);
                                    favorStatusList.set(0, f);
                                } else if (f.getfPos() == pos - 1) {
                                    f.setFavorFlag(0);
                                    favorStatusList.set(pos - 1, f);
                                }
                            }
                            txt_favor.setSelected(false);
                            Toast.makeText(baseActivity, "取消收藏",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 请求收藏
     *
     * @param quesId
     */
    private void doCollect(int quesId) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonCollectPramas(quesId);
        Observable<CommonBean> observable = httpPostService.getCollQuestInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(baseActivity, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            for (FavorFlag f : favorStatusList) {
                                if (pos == 0 && f.getfPos() == pos) {
                                    f.setFavorFlag(1);
                                    favorStatusList.set(0, f);
                                } else if (f.getfPos() == pos - 1) {
                                    f.setFavorFlag(1);
                                    favorStatusList.set(pos - 1, f);
                                }
                            }
                            txt_favor.setSelected(true);
                            Toast.makeText(baseActivity, "收藏成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonCollectPramas(int quesId) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("quesId", quesId);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {// 获取返回结果
            if (requestCode == 2) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                handler.sendMessage(msg);
            } else if (requestCode == 1) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 返回按钮设置监听
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            stop();
            int doCount = 0;
            dialog = new Dialog(baseActivity,
                    R.style.NormalDialogStyle);
            dialog.setContentView(R.layout.dialog_back);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            TextView txt = (TextView) dialog.findViewById(R.id.txt_dialog);
            // 继续做题
            Button go_Btn = (Button) dialog.findViewById(R.id.go_on);
            // 退出答题
            Button sumbit_btn = (Button) dialog.findViewById(R.id.sumbit_btn);

            for (Card c : cardList) {
                if (c.isStatus() == true) {
                    doCount++;
                }
            }

            if (doCount < cardList.size()) {
                txt.setText("总共" + cardList.size() + "道题,未答" + (cardList.size() - doCount) + "道题");
            } else {
                txt.setText("总共" + cardList.size() + "道题,未答" + (cardList.size() - doCount) + "道题");
            }

            sumbit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restart();
                    // 提交试卷
                    submitTest();

                }
            });
            go_Btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    restart();
                    dialog.dismiss();
                    baseActivity.finish();

                }
            });

            return true;
        }
        return true;
    }

    /**
     * 提交试卷
     */
    private void submitTest() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String currentTime = txt_time.getText().toString().trim();
        String[] str = currentTime.split(":");
        int doTime = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("paperId", paperId);
            obj.put("paperDoneTime", doTime);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.submitPaperInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(baseActivity, "网络异常，试卷提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            Intent cIntent = new Intent(baseActivity,
                                    CardResultActivity.class);
                            // 传递试卷id
                            cIntent.putExtra("TestPPKID", paperId);
                            // 班级id
                            cIntent.putExtra("classId", 0);
                            // 阶段id
                            cIntent.putExtra("stageId", 0);
                            // 课程id
                            cIntent.putExtra("lessonId", 0);
                            // 科目id
                            cIntent.putExtra("levelId", 0);
                            cIntent.putExtra("isOnline", 0);
                            cIntent.putExtra("LessonChildId", 0);
                            // 答题用时
                            cIntent.putExtra("time", mTime);
                            // 试卷名
                            cIntent.putExtra("name", title);
                            cIntent.putExtra("studyKey", "");
//                            TiKuContentFragment.handler.sendEmptyMessage(0);
                            baseActivity.startActivity(cIntent);
                            finish();
                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {

        super.onResume();
        pManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "TAG");
        mWakeLock.acquire();

        if (isAppOnForeground()) { // 判断如果程序回到前台情况

            handler.sendEmptyMessage(3);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (null != mWakeLock) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    }

    /**
     * 判断程序是否在后台运行
     *
     * @return boolean
     */
    private boolean isAppOnForeground() {

        ActivityManager manager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcess = manager
                .getRunningAppProcesses();
        if (null == appProcess) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appPro : appProcess) {

            if (appPro.processName.equals(packageName)
                    && appPro.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != timter) {
            timter.cancel();
        }
        if (null != timerTask) {
            timerTask.cancel();
        }
        FileUtils.deleteDir();
    }


}
