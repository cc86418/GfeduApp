package jc.cici.android.atom.ui.tiku.newDoExam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.EveryDayAndMothBean;
import jc.cici.android.atom.bean.FavorFlag;
import jc.cici.android.atom.bean.NoteOrQuesStatus;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.NoteAllActivity;
import jc.cici.android.atom.ui.note.QuestionAllActivity;
import jc.cici.android.atom.ui.tiku.ErrorSetActivity;
import jc.cici.android.atom.ui.tiku.MyFragmentPageAdapter;
import jc.cici.android.atom.ui.tiku.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 每日一题Ac
 * Created by atom on 2018/1/15.
 */

public class EveryDayTestAc extends BaseActivity {

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
    // 收藏夹
    @BindView(R.id.txt_favor)
    TextView txt_favor;
    // 笔记
    @BindView(R.id.txt_node)
    TextView txt_node;
    // 笔记tip
    @BindView(R.id.noteTip)
    TextView noteTip;
    // 提问
    @BindView(R.id.txt_ques)
    TextView txt_ques;
    // 答疑tip
    @BindView(R.id.quesTip)
    TextView quesTip;
    // viewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 标题
    private String title;
    private Dialog mDialog;
    // 试卷id
    private int paperId;
    // 问题id
    private int quesId;
    // 是否已做，非零表示未做，0表示已做
    private int isDoQues;
    // 1 表示每日一题，其他可以不传
    private int searchType;
    // 项目id 如果为为每日一题，则必传
    private int projectId;
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    private MyFragmentPageAdapter mAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 笔记个数设置
                    int noteCount = (int) msg.obj;
                    noteTip.setText(noteCount + "");
                    noteTip.setVisibility(View.VISIBLE);
                    break;
                case 2: // 答疑个数显示
                    int quesCount = (int) msg.obj;
                    quesTip.setText(quesCount + "");
                    quesTip.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    if (null != mDialog && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
            }
        }
    };
    // 每日一题个数
    private int strNoteTip;
    // 问题个数
    private int strQuesTip;
    private int isCollect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_everyday;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        paperId = getIntent().getIntExtra("paperId", 0);
        isDoQues = getIntent().getIntExtra("isDoQues", 0);
        quesId = getIntent().getIntExtra("quesId", 0);
        searchType = getIntent().getIntExtra("searchType", 0);
        projectId = getIntent().getIntExtra("projectId", 0);
        System.out.println("paperId >>>:" + paperId);
        System.out.println("quesId >>>:" + quesId);
        System.out.println("searchType >>>:" + searchType);
        System.out.println("projectId >>>:" + projectId);
        baseActivity = this;
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
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
        Observable<CommonBean<EveryDayAndMothBean>> observable = httpPostService.getQuesInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<EveryDayAndMothBean>>() {
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
                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<EveryDayAndMothBean> everyDayAndMothBeanCommonBean) {
                        if (100 == everyDayAndMothBeanCommonBean.getCode()) {
                            // 收藏id
                            isCollect = everyDayAndMothBeanCommonBean.getBody().getHasCollect();
                            if (1 == isCollect) { // 显示收藏
                                txt_favor.setSelected(true);
                            } else if (0 == isCollect) {
                                txt_favor.setSelected(false);
                            }
                            Fragment fragment = new EveryOrMothFragment(mHandler);
                            Bundle bundle = new Bundle();
                            bundle.putInt("userId", userId);
                            // 标题
                            if (searchType == 1) {
                                title = "每日一题";
                            } else if (searchType == 2) {
                                title = "查看解析";
                            } else {
                                title = "扫题";
                            }
                            bundle.putString("title", title);
                            // 试题号
                            quesId = everyDayAndMothBeanCommonBean.getBody().getQuesId();
                            bundle.putInt("quesId", quesId);
                            if (searchType == 1) { // 每日一题
                                bundle.putInt("searchType", searchType);
                            } else if (searchType == 2) {
                                bundle.putInt("searchType", searchType);
                            } else if (searchType == 3) { // 扫题
                                bundle.putInt("searchType", 3);
                            }
                            bundle.putInt("isDoQues", isDoQues);
                            bundle.putInt("paperId", paperId);
                            // 试题类型(单选，多选，填空，简答等)
                            bundle.putInt("quesType", everyDayAndMothBeanCommonBean.getBody().getQuesType());
                            // 选项数量
                            bundle.putInt("quesOptionCount", everyDayAndMothBeanCommonBean.getBody().getQuesOptionCount());
                            // 试卷内容
                            bundle.putString("quesUrl", everyDayAndMothBeanCommonBean.getBody().getQuesUrl());
                            //  用户答案
                            bundle.putString("quesUserAnswer", everyDayAndMothBeanCommonBean.getBody().getQuesUserAnswer());
                            //  用户答案
                            fragment.setArguments(bundle);
                            FragmentList.add(fragment);
                            mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), FragmentList);
                            viewPager.setAdapter(mAdapter);

                            // 设置当前笔记个数tip
                            strNoteTip = everyDayAndMothBeanCommonBean.getBody().getEveryDayQuesId();
                            System.out.println("strNoteTip >>>:" + strNoteTip);
                            if (0 != strNoteTip) {
                                noteTip.setText(strNoteTip + "");
                                noteTip.setVisibility(View.VISIBLE);
                            }
                            // 设置当前问题个数tip
                            strQuesTip = everyDayAndMothBeanCommonBean.getBody().getQuesProblemCount();
                            if (0 != strQuesTip) {
                                quesTip.setText(strQuesTip);
                                quesTip.setVisibility(View.VISIBLE);
                            }
                            // 设置标题名称
                            title_txt.setText(title);
                            // 题目个数
                            register_txt.setText(1 + "/" + 1);
                            register_txt.setTextColor(Color.parseColor("#333333"));
                        } else {
                            Toast.makeText(baseActivity, everyDayAndMothBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private RequestBody commonPramas() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("paperId", paperId);
            obj.put("quesId", quesId);
            obj.put("projectId", projectId);
            obj.put("searchType", searchType);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));  // 测试加密数据
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
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
    }

    @OnClick({R.id.back_layout, R.id.txt_favor, R.id.txt_ques, R.id.txt_node})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                Intent it = new Intent(baseActivity, ErrorSetActivity.class);
                setResult(2, it);
                this.finish();
                break;
            case R.id.txt_favor: // 收藏按钮
                if (isCollect == 0) { // 未收藏
                    // 请求接口收藏
                    doCollect(quesId);

                } else if (isCollect == 1) { // 已收藏
                    // 取消收藏
                    cancelCollect(quesId);
                }
                break;
            case R.id.txt_ques: // 问题答疑
                Intent quesIntent = new Intent(this, QuestionAllActivity.class);
                Bundle quesBundle = new Bundle();
//                quesBundle.putInt("testPaperId", paperId);
                quesBundle.putInt("testQuesId", quesId);
                quesBundle.putInt("videoOrTestFlag", 7);
                quesBundle.putInt("classId", 0);
                quesBundle.putInt("stageId", 0);
                quesBundle.putInt("lessonId", 0);
                quesBundle.putInt("subjectId", 0);
                quesBundle.putString("name", title);
                quesIntent.putExtras(quesBundle);
                startActivity(quesIntent);
                break;
            case R.id.txt_node: // 笔记
                Intent noteIntent = new Intent(this, NoteAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("testQuesId", quesId);
                bundle.putInt("videoOrTestFlag", 7);
                bundle.putInt("classId", 0);
                bundle.putInt("stageId", 0);
                bundle.putInt("lessonId", 0);
                bundle.putInt("subjectId", 0);
                bundle.putString("name", title);
                noteIntent.putExtras(bundle);
                startActivity(noteIntent);
                break;
        }
    }

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
                            txt_favor.setSelected(true);
                            isCollect = 1;
                            Toast.makeText(baseActivity, "收藏成功", Toast.LENGTH_SHORT).show();
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
                            txt_favor.setSelected(false);
                            isCollect = 0;
                            Toast.makeText(baseActivity, "取消收藏", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Intent it = new Intent(baseActivity, ErrorSetActivity.class);
            setResult(2, it);
        }
        return super.onKeyDown(keyCode, event);
    }


}
