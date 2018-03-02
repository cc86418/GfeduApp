package jc.cici.android.atom.ui.tiku;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jc.cici.android.R;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.ui.tiku.newDoExam.TestActivity;
import jc.cici.android.atom.view.ScoreProgressBar;


public class CardResultActivity extends jc.cici.android.atom.base.BaseActivity implements OnClickListener {

    private static final int MSG_PROGRESS_UPDATE = 0x110;
    // 返回按钮
    private RelativeLayout backResultImgBtn;
    private TextView reset_txt;

    // 做题用时
    private TextView sckd_tv;
    // 交卷时间
    private TextView tv_time;
    // 答题情况
    private GridView view_gridResult;
    // 错题解析
    private Button wrongAnalysisBtn;
    // 全部解析
    private Button allAnalysisBtn;

    // 试卷id
    private int testPPKID;
    // 班级id
    private int classid;
    // 阶段id
    private int stageid;
    // 课程id
    private int lessonid;
    // 在线标识
    private int isOnline;
    // 面授传递参数
    private int lessonChildId;
    // 科目id
    private int subjectid;
    private String studyKey;
    // 创建工具类对象
    private HttpUtils httpUtils = new HttpUtils();
    // 返回答案及解析数据list
    private ArrayList<CardStatus> resultList;
    // 创建适配器对象
    private ResultAdaper adapter;
    // 创建hashMap 用于记录位置
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private String name; // 当前学习标题
    // 自定义进度条
    private Dialog mDialog;
    // 进度
    private ScoreProgressBar progress_bar;
    // 获取正确率字符串
    private int progress;
    // 类型区分考研数学
    private String CardAnwserType;

    private String itemName;

    //新增
    private CardResultAdapter cardResultAdapter;
    private CommParam commonParams;
    private CardStatus cardStatus;
    private ArrayList<CardStatus> cardStatusList = new ArrayList<>();
    private ExpandableListView class_lv;
    private ObjectAnimator objectAnimator;
    private String time;
    // 定义广播接受器对象
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.jc.finish".equals(intent.getAction())) { // 笔记数量改变
                CardResultActivity.this.finish();
            }

        }
    };
    // 定义广播过滤器
    private IntentFilter filter;


    @Override
    protected int getLayoutId() {
        return R.layout.answer_card;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        testPPKID = getIntent().getIntExtra("TestPPKID", 0);
        classid = getIntent().getIntExtra("classId", 0);
        stageid = getIntent().getIntExtra("stageId", 0);
        lessonid = getIntent().getIntExtra("lessonId", 0);
        subjectid = getIntent().getIntExtra("levelId", 0);
        isOnline = getIntent().getIntExtra("isOnline", 0);
        lessonChildId = getIntent().getIntExtra("LessonChildId", 0);
        studyKey = getIntent().getStringExtra("studyKey");
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");
        // 答题用时
        time = getIntent().getStringExtra("time");
        // 获取试卷标题
        name = getIntent().getStringExtra("name");
        System.out.println("提交答案之后的界面 : " + CardAnwserType);

        // 加载数据
        showProcessDialog(CardResultActivity.this,
                R.layout.loading_process_dialog_color);
        // 初始化操作
        initViewSetting();
        // 获取真题数据
        GetZhenTiData();
    }

    private void GetZhenTiData() {
        // 创建任务对象
        AnswerDataTask task = new AnswerDataTask();
        task.execute();
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

    /**
     * 给控件设置id
     */
    private void initViewSetting() {
        class_lv = (ExpandableListView) findViewById(R.id.class_lv);
        // 返回按钮获取id
        backResultImgBtn = (RelativeLayout) findViewById(R.id.backLayout);
        progress_bar = (ScoreProgressBar) findViewById(R.id.progress_bar);

        sckd_tv = (TextView) findViewById(R.id.sckd_tv);
        // 重做
        reset_txt = (TextView) findViewById(R.id.reset_txt);
        // 花费时间获取id
        tv_time = (TextView) findViewById(R.id.time_txt);
        // 错题解析获取id
        wrongAnalysisBtn = (Button) findViewById(R.id.wrongAnalytical_btn);
        // 全部解析获取id
        allAnalysisBtn = (Button) findViewById(R.id.allAnalytical_btn);
        // 设置监听
        backResultImgBtn.setOnClickListener(this);
        wrongAnalysisBtn.setOnClickListener(this);
        allAnalysisBtn.setOnClickListener(this);
        reset_txt.setOnClickListener(this);

        // 初始化公共参数
        commonParams = new CommParam(this);

        filter = new IntentFilter();
        filter.addAction("com.jc.finish");
        registerReceiver(receiver, filter);
    }

    class AnswerDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... param) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("userId", commonParams.getUserId());
                obj.put("oauth", commonParams.getOauth());
                obj.put("client", commonParams.getClient());
                obj.put("timeStamp", commonParams.getTimeStamp());
                obj.put("version", commonParams.getVersion());
                obj.put("paperId", testPPKID);
                obj.put("appName", commonParams.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            cardStatus = HttpUtils.getInstance().getuserpaperreport(
                    Const.URL + Const.GetUserPaperReportAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if ("100".equals(cardStatus.getCode())) {
                cardStatusList.add(cardStatus);
                // 答题时间
                String time = cardStatus.getBody().getUserDoneTime();
                if (time != null) {
                    Double s = Double.parseDouble(time);
                    String format;
                    Object[] array;
                    Integer hours = (int) (s / (60 * 60));
                    Integer minutes = (int) (s / 60 - hours * 60);
                    Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
                    if (hours > 0) {
                        format = "%1$02d:%2$02d:分%3$02d";
                        array = new Object[]{hours, minutes, seconds};
                    } else if (minutes > 0) {
                        format = "%1$02d:%2$02d";
                        array = new Object[]{minutes, seconds};
                    } else {
                        format = "%1$02d:%2$02d";
                        array = new Object[]{minutes, seconds};
                    }
                    tv_time.setText(String.format(format, array));
                }
                // 正确答案率
                String countRightStr = cardStatus.getBody().getUserRightPercent();
                String subStr = countRightStr.substring(0, countRightStr.lastIndexOf("."));
                progress = Integer.parseInt(subStr);

                objectAnimator = ObjectAnimator.ofInt(progress_bar, "progress", 0, progress);
                objectAnimator.setDuration(2000);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.start();
                // 交卷时间
                String strSubmit = cardStatus.getBody().getUserSubTime();
                if (strSubmit != null && !strSubmit.equals("")) {
                    sckd_tv.setText("交卷时间:" + strSubmit);
                }

                cardResultAdapter = new CardResultAdapter(CardResultActivity.this,
                        cardStatusList, classid, stageid, lessonid, subjectid, isOnline, lessonChildId, studyKey, name, testPPKID);
                class_lv.setAdapter(cardResultAdapter);
                if(null != cardStatus.getBody().getPaperQuesGroupList()){
                    // 设置全部展开
                    for (int i = 0; i < cardStatus.getBody().getPaperQuesGroupList().size(); i++) {
                        class_lv.expandGroup(i);
                    }
                }
                // 设置不可点击收缩
                class_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
                // 滚动条消失
                mDialog.dismiss();
            }
            super.onPostExecute(result);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout: // 返回按钮
                finish();
                break;
            case R.id.wrongAnalytical_btn: // 错题解析
                //TODO
                Intent wrongIntent = new Intent(CardResultActivity.this,
                        WrongAnalysisActivity.class);
                // 试卷id
                wrongIntent.putExtra("testPPKID", testPPKID);
                // 班级id
                wrongIntent.putExtra("classId", classid);
                // 阶段id
                wrongIntent.putExtra("stageId", stageid);
                // 课程id
                wrongIntent.putExtra("lessonId", lessonid);
                // 科目id
                wrongIntent.putExtra("levelId", subjectid);
                wrongIntent.putExtra("isOnline", isOnline);
                wrongIntent.putExtra("lessonChildId", lessonChildId);
                wrongIntent.putExtra("name", name);
                // 类型
                wrongIntent.putExtra("CardAnwserType", CardAnwserType);
                CardResultActivity.this.startActivity(wrongIntent);
                break;
            case R.id.allAnalytical_btn: // 全部解析
                Intent allIntent = new Intent(CardResultActivity.this,
                        AnalysisActivity.class);
                // 试卷id
                allIntent.putExtra("testPPKID", testPPKID);
                // 班级id
                allIntent.putExtra("classId", classid);
                // 阶段id
                allIntent.putExtra("stageId", stageid);
                // 课程id
                allIntent.putExtra("lessonId", lessonid);
                // 科目id
                allIntent.putExtra("levelId", subjectid);
                allIntent.putExtra("isOnline", isOnline);
                allIntent.putExtra("lessonChildId", lessonChildId);
                allIntent.putExtra("name", name);
                allIntent.putExtra("itemName", itemName);
                // 类型
                allIntent.putExtra("CardAnwserType", CardAnwserType);
                CardResultActivity.this.startActivity(allIntent);
                break;
            case R.id.reset_txt: // 重做按钮监听
//                // 重新答题
                if ("".equals(studyKey)) {
                    Intent it = new Intent(this, TestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("paperId", testPPKID);
                    bundle.putInt("classid", classid);
                    bundle.putInt("stageid", stageid);
                    bundle.putInt("lessonid", lessonid);
                    if (1 == isOnline) { // 在线重做情况
                        bundle.putInt("levelId", subjectid);
                        bundle.putInt("online", isOnline);
                    } else {
                        bundle.putInt("PaperLessonChildId", lessonChildId);
                    }
                    bundle.putString("title", name);
                    bundle.putString("studyKey", studyKey);
                    it.putExtras(bundle);
                    startActivity(it);
                    this.finish();
                } else {
                    Intent it = new Intent(this, MyQuestionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("paperId", testPPKID);
                    bundle.putInt("classid", classid);
                    bundle.putInt("stageid", stageid);
                    bundle.putInt("lessonid", lessonid);
                    if (1 == isOnline) { // 在线重做情况
                        bundle.putInt("levelId", subjectid);
                        bundle.putInt("online", isOnline);
                    } else {
                        bundle.putInt("PaperLessonChildId", lessonChildId);
                    }
                    bundle.putString("title", name);
                    bundle.putString("studyKey", studyKey);
                    it.putExtras(bundle);
                    startActivity(it);
                    this.finish();
                }

                break;
            default:
                break;
        }

    }

    private void doAgain() {
//       CommParam param = new CommParam(this);
//        userId = param.getUserId();
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("userId", param.getUserId());
//            obj.put("oauth", param.getOauth());
//            obj.put("client", param.getClient());
//            obj.put("timeStamp", param.getTimeStamp());
//            obj.put("version", param.getVersion());
//            obj.put("paperId", paperId);
//            obj.put("appName", param.getAppname());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ContentZhenTi = HttpUtils.getInstance().getTestResultInfo(
//                Const.URL + Const.ExamPaperInfo, obj);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        // 如果动画还在运行，则结束
        if (objectAnimator.isRunning()) {
            objectAnimator.end();
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

}
