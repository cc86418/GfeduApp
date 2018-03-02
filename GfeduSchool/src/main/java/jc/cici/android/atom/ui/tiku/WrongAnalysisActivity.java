package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.jun.live.LiveActivity;
import cn.jun.polyv.IjkVideoActicity;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.FavorFlag;
import jc.cici.android.atom.bean.NextTaskBean;
import jc.cici.android.atom.bean.NoteOrQuesStatus;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.NoteAllActivity;
import jc.cici.android.atom.ui.note.QuestionAllActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WrongAnalysisActivity extends jc.cici.android.atom.base.BaseActivity implements
        OnClickListener {

    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 试卷id
    private int testPPKID;
    // 班级id
    private int classId;
    // 阶段id
    private int stageId;
    // 课程id
    private int lessonId;
    // 科目id
    private int subjectId;
    private int isOnline;
    // 标题名
    private String name;
    private LinearLayout txt_error_layout;
    private LinearLayout txt_favor_layout;
    // 返回按钮
    private Button backBtn;
    // 收藏按钮
    private TextView txt_favor;
    // 答题卡按钮
    private TextView txt_card;
    // 纠错文字
    private TextView txt_error;
    // 标题文字
    private TextView title_txt;
    private TextView txt_collect;
    // 页面数
    private TextView tv_papersCount;
    // 创建viewPager 对象
    private ViewPager viewpager_wrongAnalysis;
    // 初始化fragmentList 对象
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // 定义FragmentManager管理器对象
    private FragmentManager fm;
    // 自定义定义进度条
    private AlertDialog mDialog;
    // 初始化适配器对象
    private MyFragmentPageAdapter wAnalysisAdapter;
    // 创建获取解析数据list
    private ArrayList<WrongAnalysisContent> wAnalyList = new ArrayList<WrongAnalysisContent>();
    // 类型
    private String CardAnwserType;
    protected int pos;
    // 收藏位置
    private int collectPos;
    // 总个数
    private String count;
    private CommParam commParam;
    private WrongAnalysisContent wrongAnalysisBean;
    // 笔记tips
    private TextView noteTip;
    // 答疑tips
    private TextView quesTip;
    // 笔记个数
    private String strNoteTip;
    // 答疑个数
    private String strQuesTip;
    // 收藏列表
    private ArrayList<FavorFlag> favorStatusList = new ArrayList<FavorFlag>();

    private Handler changeHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    // 定义广播接受器对象
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.jc.wroNoteChange".equals(intent.getAction())) { // 笔记数量改变
                int count = intent.getIntExtra("count", 0);
                if (null != strNoteTip && !"".equals(strNoteTip)) {
                    // 原有笔记数量
                    int noteCount = Integer.parseInt(strNoteTip);
                    // 新增笔记数量
                    int noteAllCount = count + noteCount;
                    noteTip.setText(String.valueOf(noteAllCount));
                    noteTip.setVisibility(View.VISIBLE);
                } else {
                    noteTip.setText(String.valueOf(count));
                    noteTip.setVisibility(View.VISIBLE);
                }


            } else if ("com.jc.wroQuesChange".equals(intent.getAction())) { // 答疑数量改变
                int count = intent.getIntExtra("count", 0);
                if (null != strQuesTip && !"".equals(strQuesTip)) {
                    // 原有笔记数量
                    int quesCount = Integer.parseInt(strQuesTip);
                    // 新增笔记数量
                    int quesAllCount = count + quesCount;
                    quesTip.setText(String.valueOf(quesAllCount));
                    quesTip.setVisibility(View.VISIBLE);
                } else {
                    quesTip.setText(String.valueOf(count));
                    quesTip.setVisibility(View.VISIBLE);
                }

            }
        }
    };
    // 定义广播过滤器
    private IntentFilter filter;
    // 笔记状态
    private int stageNote;
    // 问答状态
    private int stageProblem;
    // 资料状态
    private int stageInformation;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.wrong_analysis_layout;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        testPPKID = getIntent().getIntExtra("testPPKID", 0);
        classId = getIntent().getIntExtra("classId", 0);
        stageId = getIntent().getIntExtra("stageId", 0);
        lessonId = getIntent().getIntExtra("lessonId", 0);
        subjectId = getIntent().getIntExtra("levelId", 0);
        isOnline = getIntent().getIntExtra("isOnline", 0);
        name = getIntent().getStringExtra("name");
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");
        // 初始化操作
        initSetting();
        // 加载数据
        showProcessDialog(WrongAnalysisActivity.this,
                R.layout.loading_process_dialog_color);
        // 按钮设置监听
        backBtn.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_favor_layout.setOnClickListener(this);
        txt_error_layout.setOnClickListener(this);
        txt_collect.setOnClickListener(this);

        // 获取加载数据
        getwAnalysisData();
        if (0 != stageId) {
            // 获取笔记答疑开放情况
            setOpenOrCloseNoteOrQues();
        }
    }

    private void setOpenOrCloseNoteOrQues() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            obj.put("stageId", stageId);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<NoteOrQuesStatus>> observable = httpPostService.getClassStageServiceStatus(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<NoteOrQuesStatus>>() {
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
                                    Toast.makeText(WrongAnalysisActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<NoteOrQuesStatus> noteOrQuesStatusCommonBean) {
                                if (100 == noteOrQuesStatusCommonBean.getCode()) {
                                    stageNote = noteOrQuesStatusCommonBean.getBody().getStageNote();
                                    stageProblem = noteOrQuesStatusCommonBean.getBody().getStageProblem();
                                    stageInformation = noteOrQuesStatusCommonBean.getBody().getStageInformation();
                                    if (1 != stageNote) {
                                        noteTip.setTextColor(Color.parseColor("#ffffff"));
                                        noteTip.setBackgroundResource(R.drawable.tip_normal_bg);
                                    }
                                    if (1 != stageProblem) {
                                        quesTip.setTextColor(Color.parseColor("#ffffff"));
                                        quesTip.setBackgroundResource(R.drawable.tip_normal_bg);
                                    }

                                } else {
                                    Toast.makeText(WrongAnalysisActivity.this, noteOrQuesStatusCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }


    /**
     * 异步task获取解析数据
     */
    private void getwAnalysisData() {
        // 创建任务对象
        WAnalysisTask task = new WAnalysisTask();
        task.execute();

    }

    /**
     * 自定义进度条
     */
    private void showProcessDialog(WrongAnalysisActivity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

    }

    /**
     * 获取控件id
     */
    private void initSetting() {

        sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        // 初始化按钮
        backBtn = (Button) findViewById(R.id.backBtn);
        txt_error_layout = (LinearLayout) findViewById(R.id.txt_error_layout);
        txt_favor_layout = (LinearLayout) findViewById(R.id.txt_favor_layout);
        title_txt = (TextView) findViewById(R.id.title_txt);
        // 笔记卡
        txt_error = (TextView) findViewById(R.id.txt_error);
        // 提问 id
        txt_favor = (TextView) findViewById(R.id.txt_favor);
        // 答题卡id
        txt_card = (TextView) findViewById(R.id.txt_card);
        txt_collect = (TextView) findViewById(R.id.txt_collect);

        noteTip = (TextView) findViewById(R.id.noteTip);
        quesTip = (TextView) findViewById(R.id.quesTip);
        // 页个数
        tv_papersCount = (TextView) findViewById(R.id.tv_papersCount);
        // 获取viewpager id
        viewpager_wrongAnalysis = (ViewPager) findViewById(R.id.viewpagerAnalysis_wrong);
        // 获取fragmentManger管理器
        fm = getSupportFragmentManager();
        commParam = new CommParam(this);

        filter = new IntentFilter();
        filter.addAction("com.jc.wroNoteChange");
        filter.addAction("com.jc.wroQuesChange");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn: // 返回按钮
                this.finish();
                break;
            case R.id.txt_card: // 答题卡按钮
                this.finish();
                break;
            case R.id.txt_favor_layout: // 提问按钮
                Intent quesIntent = new Intent(this, QuestionAllActivity.class);
                Bundle quesBundle = new Bundle();
                quesBundle.putInt("testQuesId", wAnalyList.get(0).getBody().get(pos)
                        .getQuesId());
                System.out.println("testPaperId >>>:" + testPPKID + ",testQuesId >>>:" + wAnalyList.get(0).getBody().get(pos)
                        .getQuesId());
                quesBundle.putInt("videoOrTestFlag", 8);
                quesBundle.putInt("classId", classId);
                quesBundle.putInt("stageId", stageId);
                quesBundle.putInt("lessonId", lessonId);
                quesBundle.putInt("subjectId", subjectId);
                quesBundle.putString("name", name);
                quesIntent.putExtras(quesBundle);
                startActivity(quesIntent);
                break;
            case R.id.txt_collect: // 收藏按钮
                if (collectPos == 0) {
                    if (favorStatusList.get(0).getFavorFlag() == 0) { // 未收藏情况
                        // 请求接口收藏
                        doCollect(wAnalyList.get(0).getBody().get(0).getQuesId());
                    } else { // 已收藏情况
                        // 取消收藏
                        cancelCollect(wAnalyList.get(0).getBody().get(0).getQuesId());
                    }
                } else {
                    if (favorStatusList.get(collectPos - 1).getFavorFlag() == 0) { // 未收藏情况
                        // 请求接口收藏
                        doCollect(wAnalyList.get(0).getBody().get(collectPos - 1).getQuesId());
                    } else { // 已收藏情况
                        // 取消收藏
                        cancelCollect(wAnalyList.get(0).getBody().get(collectPos - 1).getQuesId());
                    }
                }

                break;
            case R.id.txt_error_layout: // 笔记按钮
                Intent noteIntent = new Intent(this, NoteAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("testQuesId", wAnalyList.get(0).getBody().get(pos)
                        .getQuesId());
                bundle.putInt("classId", classId);
                bundle.putInt("stageId", stageId);
                bundle.putInt("lessonId", lessonId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("videoOrTestFlag", 8);
                bundle.putString("name", name);
                System.out.println(",testQuesId >>>:" + wAnalyList.get(0).getBody().get(pos).getQuesId());
                noteIntent.putExtras(bundle);
                startActivity(noteIntent);
                break;
            default:
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
                        Toast.makeText(WrongAnalysisActivity.this, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            for (FavorFlag f : favorStatusList) {
                                if (collectPos == 0 && f.getfPos() == collectPos) {
                                    f.setFavorFlag(0);
                                    favorStatusList.set(0, f);
                                } else if (f.getfPos() == collectPos - 1) {
                                    f.setFavorFlag(0);
                                    favorStatusList.set(collectPos - 1, f);
                                }
                            }
                            Drawable drawable0 = getResources().getDrawable(R.drawable.icon_test_favor_normal);
                            drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                            txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
                            Toast.makeText(WrongAnalysisActivity.this, "取消收藏",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WrongAnalysisActivity.this, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WrongAnalysisActivity.this, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            for (FavorFlag f : favorStatusList) {
                                if (collectPos == 0 && f.getfPos() == collectPos) {
                                    f.setFavorFlag(1);
                                    favorStatusList.set(0, f);
                                } else if (f.getfPos() == collectPos - 1) {
                                    f.setFavorFlag(1);
                                    favorStatusList.set(collectPos - 1, f);
                                }
                            }
                            Drawable drawable0 = getResources().getDrawable(R.drawable.ic_test_favor_focus);
                            drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                            txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
                            Toast.makeText(WrongAnalysisActivity.this, "收藏成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WrongAnalysisActivity.this, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonCollectPramas(int quesId) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(WrongAnalysisActivity.this);
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

    class WAnalysisTask extends AsyncTask<Void, Void, Void> {
        // 获取试题总个数
        private int size;

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("UserId", commParam.getUserId());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("TimeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("PaperId", testPPKID);
                obj.put("appName", commParam.getAppname());
            } catch (Exception e) {
                e.printStackTrace();
            }
            wrongAnalysisBean = HttpUtils.getInstance().getuserpapercarderror(
                    Const.URL + Const.GetUserPaperCardErrorAPI, obj);

            if ("100".equals(wrongAnalysisBean.getCode())) {
                if (wrongAnalysisBean.getBody() != null && !"".equals(wrongAnalysisBean)) {
                    wAnalyList.add(wrongAnalysisBean);
                }
            } else {
                Toast.makeText(WrongAnalysisActivity.this, "错题解析加载异常",
                        Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (wAnalyList != null && wAnalyList.get(0).getBody().size() > 0) {
                size = wAnalyList.get(0).getBody().size();
                for (int i = 0; i < size; i++) {

                    FavorFlag favorFlag = new FavorFlag();
                    favorFlag.setfPos(i);
                    favorFlag.setFavorFlag(wAnalyList.get(0).getBody().get(i)
                            .getHasCollect());
                    favorStatusList.add(favorFlag);

                    // 创建fragment对象
                    /** update by atom 2016/12/22 **/
                    Fragment f = new TiKuContentFragment(
                            WrongAnalysisActivity.this, changeHandler);
                    Bundle b = new Bundle();
                    // 题库类型
                    b.putString("type", "zhenTiAnalysisType");
                    // 试题答案类型
                    b.putString("AnswerType", wAnalyList.get(0).getBody().get(i)
                            .getQuesType());
                    b.putInt("num", wAnalyList.get(0).getBody().get(i)
                            .getQuesId());
                    // 传递标题
                    b.putString("name", name);
                    // 传递H5
                    b.putString("title", wAnalyList.get(0).getBody().get(i).getQuesUrl());
                    // 试题总数
                    b.putInt("size", size);
                    b.putString("CardAnwserType", CardAnwserType);
                    f.setArguments(b);
                    FragmentList.add(f);
                }
            }
            if (null != wAnalyList && wAnalyList.get(0).getBody().size() > 0) {
                // fragement 添加适配器
                wAnalysisAdapter = new MyFragmentPageAdapter(fm, FragmentList);
                ViewPagerScroller scroller = new ViewPagerScroller(
                        WrongAnalysisActivity.this);
                scroller.initViewPagerScroll(viewpager_wrongAnalysis);
                viewpager_wrongAnalysis.setAdapter(wAnalysisAdapter);
                viewpager_wrongAnalysis
                        .setOnPageChangeListener(onPageChangeListener);

                // 设置当前笔记个数tip
                strNoteTip = wAnalyList.get(0).getBody().get(pos).getQuesNoteCount();
                if (null != strNoteTip && !"0".equals(strNoteTip)) {
                    noteTip.setText(strNoteTip);
                    noteTip.setVisibility(View.VISIBLE);
                }
                // 设置当前问题个数tip
                strQuesTip = wAnalyList.get(0).getBody().get(pos).getQuesProblemCount();
                if (null != strQuesTip && !"0".equals(strQuesTip)) {
                    quesTip.setText(strQuesTip);
                    quesTip.setVisibility(View.VISIBLE);
                }
                // 设置标题名称
                title_txt.setText(name);
                // 获取总题数
                count = String.valueOf(size);
                // 设置是否收藏
                if (collectPos > 0) {
                    if (1 == wAnalyList.get(0).getBody().get(collectPos).getHasCollect()) {
                        Drawable drawable0 = getResources().getDrawable(R.drawable.ic_test_favor_focus);
                        drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                        txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
                    } else {
                        Drawable drawable0 = getResources().getDrawable(R.drawable.icon_test_favor_normal);
                        drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                        txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
                    }
                } else {
                    if (1 == wAnalyList.get(0).getBody().get(0).getHasCollect()) {
                        Drawable drawable0 = getResources().getDrawable(R.drawable.ic_test_favor_focus);
                        drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                        txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
                    }
                }
                if (pos > 0) {
                    tv_papersCount.setText(pos + "/" + count);
                } else {
                    tv_papersCount.setText(1 + "/" + count);
                }
                tv_papersCount.setTextColor(Color.parseColor("#333333"));

            } else {
                Toast.makeText(WrongAnalysisActivity.this, "没有错误内容",
                        Toast.LENGTH_SHORT).show();
                // 滚动条消失
                mDialog.dismiss();
                WrongAnalysisActivity.this.finish();
            }
        }

    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private boolean flag;

        @Override
        public void onPageSelected(int arg0) {
            pos = arg0;
            collectPos = arg0 + 1;
            // 显示当前做题位置
            tv_papersCount.setText((pos + 1) + "/" + count);
            tv_papersCount.setTextColor(Color.parseColor("#333333"));

            // 判断是否收藏
            if (1 == favorStatusList.get(arg0).getFavorFlag()) { // 收藏
                Drawable drawable0 = getResources().getDrawable(R.drawable.ic_test_favor_focus);
                drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
            } else { // 未收藏
                Drawable drawable0 = getResources().getDrawable(R.drawable.icon_test_favor_normal);
                drawable0.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                txt_collect.setCompoundDrawables(null, drawable0, null, null);//只放左边
            }

            // 笔记
            if (null != wAnalyList.get(0).getBody().get(pos).getQuesNoteCount() && !"0".equals(wAnalyList.get(0).getBody().get(pos).getQuesNoteCount())) {
                noteTip.setText(wAnalyList.get(0).getBody().get(pos).getQuesNoteCount());
                noteTip.setVisibility(View.VISIBLE);
            } else {
                noteTip.setVisibility(View.GONE);
            }
            // 问题
            if (null != wAnalyList.get(0).getBody().get(pos).getQuesProblemCount() && !"0".equals(wAnalyList.get(0).getBody().get(pos).getQuesProblemCount())) {
                quesTip.setText(wAnalyList.get(0).getBody().get(pos).getQuesProblemCount());
                quesTip.setVisibility(View.VISIBLE);
            } else {
                quesTip.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

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
                    if (viewpager_wrongAnalysis.getCurrentItem() == wAnalysisAdapter.getCount() - 1 && !flag) {
                        if (1 == isOnline) { // 只有在线情况(不判断面授)
                            // 弹出对话框
                            dialog = new Dialog(WrongAnalysisActivity.this,
                                    R.style.nextTaskStyle);
                            dialog.setContentView(R.layout.next_task_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            mDialog.getWindow().setGravity(Gravity.CENTER);
                            dialog.show();
                            TextView current_Txt = (TextView) dialog.findViewById(R.id.current_Txt);
                            Button doAgainBtn = (Button) dialog.findViewById(R.id.doAgainBtn);
                            Button doNextBtn = (Button) dialog.findViewById(R.id.doNextBtn);
                            RelativeLayout checkML_layout = (RelativeLayout) dialog.findViewById(R.id.checkML_layout);

                            // 重新答题
                            doAgainBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent broadIt = new Intent();
                                    broadIt.setAction("com.jc.finish");
                                    WrongAnalysisActivity.this.sendBroadcast(broadIt);
                                    Intent it = new Intent(WrongAnalysisActivity.this, MyQuestionActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("paperId", testPPKID);
                                    bundle.putInt("classid", classId);
                                    bundle.putInt("stageid", stageId);
                                    bundle.putInt("lessonid", lessonId);

                                    bundle.putInt("levelId", subjectId);

//                bundle.putString("title",title);
                                    it.putExtras(bundle);
                                    WrongAnalysisActivity.this.startActivity(it);
                                    dialog.dismiss();
                                    WrongAnalysisActivity.this.finish();
                                }
                            });
                            // 做下一个任务
                            doNextBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 获取下一个任务
                                    getNextTask();
                                    dialog.dismiss();
                                }
                            });
                            // 查看目录
                            checkML_layout.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    WrongAnalysisActivity.this.finish();
                                }
                            });
                        }
                    }
                    flag = true;
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }


    /**
     * 网络请求，获取下一个任务
     */
    private void getNextTask() {
        showProcessDialog(this, R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("userId", commParam.getUserId());
            // 班型id
            obj.put("classTypeID", classId);
            // 阶段id
            obj.put("level_ParentID", stageId);
            // 课程id
            obj.put("childClassTypeID", lessonId);
            // 科目id
            obj.put("level_PKID", subjectId);
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<NextTaskBean>> observable = httpPostService.getDownStudyInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<NextTaskBean>>() {
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
                                    Toast.makeText(WrongAnalysisActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean<NextTaskBean> nextTaskBeanCommonBean) {
                                if (100 == nextTaskBeanCommonBean.getCode()) { // 请求成功
                                    // 获取下一个任务类型(1:视频 2:试卷 3:资料 4:直播)
                                    int type = nextTaskBeanCommonBean.getBody().getKeyType();
                                    switch (type) {
                                        case 0: // 没有下一任务
                                            Toast.makeText(WrongAnalysisActivity.this, "没有下一个任务", Toast.LENGTH_SHORT).show();
                                            WrongAnalysisActivity.this.finish();
                                            break;
                                        case 1: // 视频
                                            if (!"0".equals(nextTaskBeanCommonBean.getBody().getVID())) {
                                                String vid = nextTaskBeanCommonBean.getBody().getVID();
                                                String videoId = String.valueOf(nextTaskBeanCommonBean.getBody().getVPKID());
                                                String strClassId = String.valueOf(classId);
                                                String strStageId = String.valueOf(stageId);
                                                String strStageNote = String.valueOf(stageNote);
                                                String strStageProblem = String.valueOf(stageProblem);
                                                String strStageInfo = String.valueOf(stageInformation);
                                                IjkVideoActicity.intentTo(WrongAnalysisActivity.this, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, vid,
                                                        true, nextTaskBeanCommonBean.getBody().getLevel_ShowName(), strClassId, strStageId, videoId, "isStudy", vid, "0", strStageProblem, strStageNote, strStageInfo);
                                            }
                                            WrongAnalysisActivity.this.finish();
                                            break;
                                        case 2: // 试卷类型
                                            if (0 != nextTaskBeanCommonBean.getBody().getTestPaper_PKID()) {
                                                Intent broadIt = new Intent();
                                                broadIt.setAction("com.jc.finish");
                                                WrongAnalysisActivity.this.sendBroadcast(broadIt);
                                                Intent it = new Intent(WrongAnalysisActivity.this, MyQuestionActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("paperId", nextTaskBeanCommonBean.getBody().getTestPaper_PKID());
                                                bundle.putInt("classid", nextTaskBeanCommonBean.getBody().getClassType());
                                                bundle.putInt("stageid", stageId);
                                                bundle.putInt("lessonid", lessonId);
                                                bundle.putInt("levelId", Integer.parseInt(nextTaskBeanCommonBean.getBody().getParentLevelID()));
                                                //                bundle.putString("title",title);
                                                it.putExtras(bundle);
                                                WrongAnalysisActivity.this.startActivity(it);
                                            }
                                            WrongAnalysisActivity.this.finish();
                                            break;
                                        case 3: // 资料类型
                                            // TODO 下载
                                            break;
                                        case 4: // 直播类型
                                            // 获取当前直播状态(0 未开始 1 直播中 2 已结束)
                                            int liveStatus = nextTaskBeanCommonBean.getBody().getLiveStatus();
                                            switch (liveStatus) {
                                                case 0:
                                                    Toast.makeText(WrongAnalysisActivity.this, "直播暂未开始", Toast.LENGTH_SHORT).show();
                                                    WrongAnalysisActivity.this.finish();
                                                    break;
                                                case 1:
                                                    Intent zhiboIt = new Intent(WrongAnalysisActivity.this, LiveActivity.class);
                                                    Bundle zbBundle = new Bundle();
                                                    zbBundle.putString("LiveH5", nextTaskBeanCommonBean.getBody().getLiveUrl());
                                                    zhiboIt.putExtras(zbBundle);
                                                    WrongAnalysisActivity.this.startActivity(zhiboIt);
                                                    WrongAnalysisActivity.this.finish();
                                                    break;
                                                case 2:
                                                    Intent replayIt = new Intent(WrongAnalysisActivity.this, LiveActivity.class);
                                                    Bundle replayBundle = new Bundle();
                                                    replayBundle.putString("LiveH5", nextTaskBeanCommonBean.getBody().getSelectPlayUrl());
                                                    replayIt.putExtras(replayBundle);
                                                    WrongAnalysisActivity.this.startActivity(replayIt);
                                                    WrongAnalysisActivity.this.finish();
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    Toast.makeText(WrongAnalysisActivity.this, nextTaskBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }
}
