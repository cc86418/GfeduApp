package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ScreenLockBroadcastRe;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MyQuestionActivity extends BaseActivity implements
        OnClickListener, TiKuContentFragment.TimeData {

    private PowerManager pManager;
    private PowerManager.WakeLock mWakeLock;
    private ScreenLockBroadcastRe broadReceiver;
    private MyFragmentPageAdapter mAdapter;
    // 数据请求task
    private MyQuestionTask myQuestionmTask;
    // 真题内容list
    private ContentZhenTi ContentZhenTi;
    private ArrayList<ContentZhenTi> contentZTList = new ArrayList<ContentZhenTi>();
    //
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // 工具类
    private HttpUtils httpUtils = new HttpUtils();
    // fragment管理器
    private FragmentManager fm;

    // 创建计时器对象
    private Timer timter = new Timer();
    TimerTask timerTask;
    // 创建Handler 对象
    private Handler handler;
    private long i = 0;
    private boolean isStop = false;
    private boolean isGoing = false;
    private Dialog dialog;
    // 用户id
    private int userId;
    // 试卷id
    private int paperId;
    // 班级id
    private int classid;
    // 阶段id
    private int stageid;
    // 课程id
    private int lessonid;
    // 科目id
    private int subjectid;
    // 学习进度
    private String studyKey;
    // 在线来源判断
    private int isOnline;
    // 面授来源
    private int PaperLessonChildId;

    private Dialog mDialog;

    // 返回按钮
    private Button backBtn;
    // 标题文字
    private TextView title_txt;
    // 网络提示文字
    private TextView netMobileTV;
    //    // 试题数量
//    private TextView tv_papersCount;
    // viewPgaer 内容
    private ViewPager viewPager;
    // 标记按钮
    private TextView txt_mark;
    // 答题卡按钮
    private TextView txt_card;
    // 时间按钮
    private TextView txt_time;
    // 填空题的当前位置
    private int currPos;
    private static long storeTime;
    //记录上一次滑动的positionOffsetPixels值
    private int lastValue = -1;
    private boolean isLeft = true;

    private Handler changeHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
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
                    if (haveTime != null) {
                        t--;
                        int totalSec = (int) t;
                        int min = (totalSec / 60);
                        int sec = (totalSec % 60);
                        time = String.format("%1$02d:%2$02d", min, sec);
                        txt_time.setText(time);
                    } else {
                        i++;
                        int totalSec = (int) i;
                        int min = (totalSec / 60);
                        int sec = (totalSec % 60);
                        time = String.format("%1$02d:%2$02d", min, sec);
                        txt_time.setText(time);
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
                                    changeHandler.sendEmptyMessage(2);
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
                case 10:
                    if (null != mDialog && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private ArrayList<Card> cardList = new ArrayList<Card>();
    // 当前页面位置
    private int pos;
    // 是否添加标记 默认未添加
    private boolean flag = false;
    public String time;
    // 答题时间字符串
    private String title;
    // 提交答题时间
    private static String mTime;
    private String CardAnwserType;
    // 获取考卷时间
    private String haveTime;
    // 考卷时间转化为时间值
    private long t;
    // 试卷总题数
    private String count;
    // 填空题提价返回结果
    private String callBackStr;
    // 试题id
    private int questionPKID;
    private CommParam param;
    // 判断用户是否滑动过
    private boolean isTouchPager = false;
    // 甜心对话框
    private SweetAlertDialog sweetAlertDialog;
    // 提交试卷
    private SubmitQuesAnswer submitQuesAnswer;


    @Override
    protected int getLayoutId() {
        return R.layout.startdo_papers;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Bundle startBundle = getIntent().getExtras();
        // 获取试卷id
        paperId = startBundle.getInt("paperId");
        classid = startBundle.getInt("classid");
        stageid = startBundle.getInt("stageid");
        lessonid = startBundle.getInt("lessonid");
        isOnline = startBundle.getInt("online");
        subjectid = startBundle.getInt("subjectid");
        studyKey = getIntent().getStringExtra("studyKey");
        PaperLessonChildId = startBundle.getInt("PaperLessonChildId");
        // 获取标题
        title = startBundle.getString("title");
        // 加载数据
        showProcessDialog(MyQuestionActivity.this,
                R.layout.loading_process_dialog_color);
        // 初始化view
        initSetting();
        // 获取真题数据
        GetZhenTiData();
    }

    /**
     * 获取view中各个控件id
     */
    private void initSetting() {
        backBtn = (Button) findViewById(R.id.backBtn);
        title_txt = (TextView) findViewById(R.id.title_txt);
        // 网络提示文字
        netMobileTV = (TextView) findViewById(R.id.netMobileTV);
//        tv_papersCount = (TextView) findViewById(R.id.tv_papersCount);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        txt_mark = (TextView) findViewById(R.id.txt_mark);
        txt_card = (TextView) findViewById(R.id.txt_card);
        txt_time = (TextView) findViewById(R.id.txt_time);

        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_time_nomal);
        drawable1.setBounds(0, 0, 34, 38);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_time.setCompoundDrawables(null, drawable1, null, null);//只放左边

        Drawable drawable2 = getResources().getDrawable(R.drawable.ic_card_nomal);
        drawable2.setBounds(0, 0, 36, 32);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_card.setCompoundDrawables(null, drawable2, null, null);//只放左边

        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_menu_bg);
        drawable3.setBounds(0, 0, 36, 36);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        txt_mark.setCompoundDrawables(null, drawable3, null, null);//只放左边

        // 创建广播过滤器
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        // 注册广播
        registerReceiver(broadReceiver, filter);

        // 关闭预加载，默认一次只加载一个Fragment
        viewPager.setOffscreenPageLimit(0);
        // 获取fragment管理器
        fm = getSupportFragmentManager();
        // 设置监听
        backBtn.setOnClickListener(this);
        txt_mark.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        // 创建参数对象
        param = new CommParam(this);
        userId = param.getUserId();

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
                    changeHandler.sendEmptyMessage(2);
                }
            };
        }
        if (timter != null && timerTask != null) {
            timter.schedule(timerTask, 0, 1000);

        }
        isGoing = true;
    }

    /**
     * 自定义进度条
     *
     * @param mContext
     * @param layout
     */

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    public void GetZhenTiData() {
        if (httpUtils.isNetworkConnected(this)) {
            myQuestionmTask = new MyQuestionTask();
            myQuestionmTask.execute();
        } else {
            netMobileTV.setVisibility(View.VISIBLE);
        }

    }

    class MyQuestionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            try {

                obj.put("userId", param.getUserId());
                obj.put("oauth", param.getOauth());
                obj.put("client", param.getClient());
                obj.put("timeStamp", param.getTimeStamp());
                obj.put("version", param.getVersion());
                obj.put("paperId", paperId);
                obj.put("classId", classid);
                obj.put("stageId", stageid);
                obj.put("lessonId", lessonid);
                if (1 == isOnline) {
                    // 目录id
                    obj.put("levelId", subjectid);
                } else {
                    obj.put("LessonChildId", PaperLessonChildId);
                }
                obj.put("studyKey", studyKey);
                obj.put("appName", param.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ContentZhenTi = HttpUtils.getInstance().getTestResultInfo(
                    Const.URL + Const.ExamPaperInfo, obj);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(ContentZhenTi.getCode())) {
                contentZTList.add(ContentZhenTi);
                if (null != contentZTList && contentZTList.size() > 0) {
                    if (null != contentZTList.get(0).getBody().getPaperQuesCardList()
                            && contentZTList.get(0).getBody().getPaperQuesCardList().size() > 0) {
                        int size = contentZTList.get(0).getBody().getPaperQuesCardList().size();
                        for (int i = 0; i < size; i++) {
                            Card card = new Card();
                            // 设置答题卡个数
                            // 如果已经答题了，设置状态值为true
                            if (!"".equals(contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUserAnswer())) {
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
                            /** update by atom 2016/12/21 **/
                            Fragment f = new TiKuContentFragment(
                                    MyQuestionActivity.this, changeHandler);
                            /** update by atom 2016/12/21 **/
                            Bundle b = new Bundle();
                            b.putString("type", "zhenTiType");
                            b.putString("AnswerType", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesType());
                            questionPKID = contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesId();
                            // 问题id
                            b.putInt("num", questionPKID);
                            // 班级id
                            b.putInt("classId", classid);
                            // 阶段id
                            b.putInt("stageId", stageid);
                            // 课程id
                            b.putInt("lessonId", lessonid);
                            b.putInt("isOnline", isOnline);
                            // 在线情况传递levelId
                            b.putInt("levelId", subjectid);
                            // 面授情况传递课程id
                            b.putInt("LessonChildId", PaperLessonChildId);
                            // 传递标题
                            b.putString("name", title);
                            // 学习进度
                            b.putString("studyKey", studyKey);
                            /** update by atom 2016/12/21 **/
                            b.putString("title", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUrl());
                            b.putInt("optionCount", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesOptionCount());
                            /** update by atom 2016/12/21 **/
//                            b.putString("content", contentZTList.get(0).getBody().getQuesAnswerChoice());
                            // 传递图片列表
                            b.putStringArrayList("mImageList", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUserAnswerImgs());
                            b.putInt("userId", userId);
                            b.putInt("testPaKID", paperId);
                            b.putInt("currPage", i);
                            b.putInt("size", size);
                            b.putSerializable("cardList", cardList);
                            b.putSerializable("CardAnwserType", CardAnwserType);
                            // 传递用户答案
                            b.putString("QuesUserAnswer", contentZTList.get(0).getBody().getPaperQuesCardList().get(i).getQuesUserAnswer());
                            f.setArguments(b);
                            FragmentList.add(f);
                        }

                        String jumpStr = contentZTList.get(0).getBody().getLastDoNo();
                        int jAns = Integer.parseInt(jumpStr);
                        mAdapter = new MyFragmentPageAdapter(fm, FragmentList);
                        ViewPagerScroller scroller = new ViewPagerScroller(
                                MyQuestionActivity.this);
                        scroller.initViewPagerScroll(viewPager);
                        viewPager.setAdapter(mAdapter);
                        viewPager.setOnPageChangeListener(onPageChangeListener);
                        // 获取试题总数量
                        count = String.valueOf(size);
                        title_txt.setText(title);
//                tv_papersCount.setText("1/" + count);
                        // 启动时间戳
                        start();
                        if (jAns != 0) {
                            viewPager.setCurrentItem(jAns - 1);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "暂无试题",
                                Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        MyQuestionActivity.this.finish();
                    }
                } else {
                    // 滚动条消失
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "试题暂未上传",
                            Toast.LENGTH_SHORT).show();
                    MyQuestionActivity.this.finish();
                }

            } else {
                // 滚动条消失
                mDialog.dismiss();
                Toast.makeText(MyQuestionActivity.this, "暂无练习内容", Toast.LENGTH_SHORT).show();
                MyQuestionActivity.this.finish();
            }

        }

        /**
         * vp滑动监听
         */
        OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
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

                if (0 == arg0) {
                    currPos = 0;
                } else {
                    currPos = arg0 - 1;
                }
                if ("6".equals(contentZTList.get(0).getBody().getPaperQuesCardList().get(currPos).getQuesType())) {
                    if (isLeft) { // 只有向左滑动时候才提交数据
                        jiandaSubmit(TiKuContentFragment.stringList.get(currPos));
                    }

                }
                isTouchPager = true;
                pos = arg0 + 1;
//                tv_papersCount.setText(per + "/" + count);
                // 答题时间赋值给mTime用于提交后台
                if (haveTime != null) {
                    long aTime = Long.parseLong(haveTime);
                    long s = aTime - t;
                    int totalSec = (int) s;
                    int min = (totalSec / 60);
                    int sec = (totalSec % 60);
                    String submitTime = String
                            .format("%1$02d:%2$02d", min, sec);
                    mTime = submitTime;
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
        cardAswerIntent.putExtra("classId", classid);
        cardAswerIntent.putExtra("stageId", stageid);
        cardAswerIntent.putExtra("lessonId", lessonid);
        cardAswerIntent.putExtra("isOnline", isOnline);
        cardAswerIntent.putExtra("levelId", subjectid);
        cardAswerIntent.putExtra("LessonChildId", PaperLessonChildId);
        // 传递标题
        cardAswerIntent.putExtra("name", title);
        cardAswerIntent.putExtra("studyKey", studyKey);
        cardAswerIntent.putExtra("time", time);
        cardAswerIntent.putExtra("CardAnwserType", "My_Fragment");
        startActivityForResult(cardAswerIntent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    /**
     * 重置所有文本选中状态
     */
    public void selected() {
        txt_mark.setSelected(false);
        txt_card.setSelected(false);
        txt_time.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:// 返回按钮
                stop();
                int doCount = 0;
                dialog = new Dialog(MyQuestionActivity.this,
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
                sumbit_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restart();
                        SubmitTask task = new SubmitTask();
                        task.execute();
                    }
                });
                go_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog.dismiss();
                        MyQuestionActivity.this.finish();

                    }
                });
                break;
            case R.id.txt_mark:// 标记按钮
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
            case R.id.txt_card:// 答题卡按钮
                if (cardList != null && cardList.size() > 0) {
                    Intent asIntent = new Intent(this, CardAnswer.class);
                    asIntent.putExtra("pageStatus", cardList);
                    asIntent.putExtra("userId", userId);
                    asIntent.putExtra("TestPaperID", paperId);
                    asIntent.putExtra("classId", classid);
                    asIntent.putExtra("stageId", stageid);
                    asIntent.putExtra("lessonId", lessonid);
                    asIntent.putExtra("isOnline", isOnline);
                    asIntent.putExtra("levelId", subjectid);
                    asIntent.putExtra("LessonChildId", PaperLessonChildId);
                    if (haveTime != null) {
                        asIntent.putExtra("time", mTime);
                    } else {
                        asIntent.putExtra("time", time);
                    }
                    // 传递学习标题
                    asIntent.putExtra("name", title);
                    asIntent.putExtra("studyKey", studyKey);
                    asIntent.putExtra("CardAnwserType", "My_Fragment");


                    System.out.println("答题卡传递 : " + CardAnwserType);
                    // 启动带返回值得Intent 其中 1表示请求的requsetCode
                    startActivityForResult(asIntent, 2);
                    overridePendingTransition(R.anim.slide_bottom_in, 0);
                }
                break;
            case R.id.txt_time: // 计时按钮监听
                txt_time.setSelected(true);
                stop();
                dialog = new Dialog(MyQuestionActivity.this,
                        R.style.NormalDialogStyle);
                dialog.setContentView(R.layout.pause_dialog);
                dialog.setCanceledOnTouchOutside(false);
                // 继续做题
                Button goOnBtn = (Button) dialog.findViewById(R.id.goOnBtn);
                goOnBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restart();
                        dialog.dismiss();
                        txt_time.setSelected(false);

                    }
                });
                dialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("请求的requsetCode相同 ");
        if (resultCode == 1) {// 获取返回结果
            if (requestCode == 2) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                changeHandler.sendMessage(msg);
            } else if (requestCode == 1) { // 请求的requsetCode相同
                int pageId = data.getIntExtra("pageID", 0);
                flag = data.getBooleanExtra("flag", false);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = pageId;
                changeHandler.sendMessage(msg);
            }
        }
    }


    public String getTime() {
        if (mTime != null) { // 答题时间不为0情况
            return mTime;
        } else {
            return "00:00";
        }
    }

    protected void onDestroy() {

        super.onDestroy();
        if (null != broadReceiver) {
            unregisterReceiver(broadReceiver);
        }
        if (null != timter) {
            timter.cancel();
        }
        if (null != timerTask) {
            timerTask.cancel();
        }
        FileUtils.deleteDir();
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 返回按钮设置监听
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            stop();
            int doCount = 0;
            dialog = new Dialog(MyQuestionActivity.this,
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

            sumbit_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    restart();
                    SubmitTask submitTask = new SubmitTask();
                    submitTask.execute();

                }
            });
            go_Btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    restart();
                    dialog.dismiss();
                    MyQuestionActivity.this.finish();

                }
            });

            return true;
        }
        return true;
    }

    @Override
    public void onNetChange(int netMobile) {
        // 网络状态变化时的操作
        if (netMobile == NetUtil.NETWORK_NONE) {
            netMobileTV.setVisibility(View.VISIBLE);
        } else {
            netMobileTV.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        pManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "TAG");
        mWakeLock.acquire();

        if (isAppOnForeground()) { // 判断如果程序回到前台情况

            changeHandler.sendEmptyMessage(3);
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


    class SubmitTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... param) {
            CommParam commParam = new CommParam(MyQuestionActivity.this);
            String[] str = time.split(":");
            int doTime = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
            JSONObject obj = new JSONObject();
            try {
                obj.put("userId", commParam.getUserId());
                obj.put("appName", commParam.getAppname());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("paperId", paperId);
                obj.put("classId", classid);
                obj.put("stageId", stageid);
                obj.put("lessonId", lessonid);
                obj.put("paperDoneTime", doTime);
                if (1 == isOnline) { // 在线情况传递目录id
                    obj.put("levelId", subjectid);
                } else { // 面授情况
                    obj.put("LessonChildId", PaperLessonChildId);
                }
                obj.put("studyKey", studyKey);

            } catch (Exception e) {
                e.printStackTrace();
            }
            submitQuesAnswer = HttpUtils.getInstance().getSubmitQuesAnswer(
                    Const.URL + Const.SubmitQuesanswerAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
            if ("100".equals(submitQuesAnswer.getCode())) {
                // 答案提交成功
                Toast.makeText(MyQuestionActivity.this, "答案提交成功", Toast.LENGTH_SHORT)
                        .show();
                Intent cIntent = new Intent(MyQuestionActivity.this,
                        CardResultActivity.class);
                // 传递试卷id
                cIntent.putExtra("TestPPKID", paperId);
                // 班级id
                cIntent.putExtra("classId", classid);
                // 阶段id
                cIntent.putExtra("stageId", stageid);
                // 课程id
                cIntent.putExtra("lessonId", lessonid);
                // 科目id
                cIntent.putExtra("levelId", subjectid);
                cIntent.putExtra("isOnline", isOnline);
                cIntent.putExtra("LessonChildId", PaperLessonChildId);
                // 答题用时
                cIntent.putExtra("time", mTime);
                // 试卷名
                cIntent.putExtra("name", title);
                cIntent.putExtra("studyKey", studyKey);
                TiKuContentFragment.handler.sendEmptyMessage(0);
                MyQuestionActivity.this.startActivity(cIntent);
                finish();
            } else {
                // 答案提交失败
                Toast.makeText(MyQuestionActivity.this, "答案提交失败", Toast.LENGTH_SHORT)
                        .show();
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 简单题提交答案
     *
     * @param str
     */
    private void jiandaSubmit(String str) {
        // 添加网络请求
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Const.URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        try {
            CommParam commParam = new CommParam(MyQuestionActivity.this);
            obj.put("userId", commParam.getUserId());
            obj.put("appName", commParam.getAppname());
            obj.put("oauth", commParam.getOauth());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("version", commParam.getVersion());
            // 试卷id
            obj.put("paperId", paperId);
            // 试题id
            obj.put("quesId", contentZTList.get(0).getBody().getPaperQuesCardList().get(currPos).getQuesId());
            // 用户答案
            obj.put("userQuesAnswer", str);
            System.out.println("currPos1111 >>>:" + currPos);
            JSONArray array = new JSONArray(TiKuContentFragment.imgList.get(currPos));
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
                                Toast.makeText(MyQuestionActivity.this, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
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
                                    PictureFileUtils.deleteCacheDirFile(MyQuestionActivity.this);
                                } else {
                                    Toast.makeText(MyQuestionActivity.this, submitQuesAnswerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

}
