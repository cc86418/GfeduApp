package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaygoo.widget.RangeSeekBar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.bean.GetLiveDetailBean;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.DialogKnowledgeAdapter;
import jc.cici.android.atom.adapter.StageAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ContentExamHomeBean;
import jc.cici.android.atom.bean.ExamChoseBean;
import jc.cici.android.atom.bean.ExamChosePaper;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.TestActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 智能组卷activity
 * Created by atom on 2017/12/21.
 */

public class OrganExamActivity extends BaseActivity {

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
    // 继续做题布局
    @BindView(R.id.continue_do_layout)
    RelativeLayout continue_do_layout;
    // 继续做题试卷文字
    @BindView(R.id.paperName_txt)
    TextView paperName_txt;
    // 继续做题按钮
    @BindView(R.id.continueDo_btn)
    Button continueDo_btn;
    // 试卷名称布局
    @BindView(R.id.firstDo_layout)
    RelativeLayout firstDo_layout;
    // 试卷名称
    @BindView(R.id.examName_txt)
    EditText examName_txt;
    // 默认题目数量
    @BindView(R.id.countHintDef)
    TextView countHintDef;
    // 滑块
    @BindView(R.id.seekbar1)
    RangeSeekBar rangeBar;
    // 阶段GridView
    @BindView(R.id.gridType)
    GridView gridType;
    // 所有知识点按钮
    @BindView(R.id.all_knowledge_btn)
    Button all_knowledge_btn;
    // 指定知识点按钮
    @BindView(R.id.specify_know_btn)
    Button specify_know_btn;
    // 随即按钮
    @BindView(R.id.random_knowledge_btn)
    Button random_knowledge_btn;
    // 已做按钮
    @BindView(R.id.have_finish_btn)
    Button have_finish_btn;
    // 全新题
    @BindView(R.id.new_exam_btn)
    Button new_exam_btn;
    // 错题
    @BindView(R.id.error_exam_btn)
    Button error_exam_btn;
    // 题型随即
    @BindView(R.id.random_Type_btn)
    Button random_Type_btn;
    // 单选题
    @BindView(R.id.single_knowledge_btn)
    Button single_knowledge_btn;
    // 多选
    @BindView(R.id.mul_knowledge_btn)
    Button mul_knowledge_btn;
    // 判断
    @BindView(R.id.judge_knowledge_btn)
    Button judge_knowledge_btn;
    // 填空
    @BindView(R.id.tiankong_knowledge_btn)
    Button tiankong_knowledge_btn;
    // 综合
    @BindView(R.id.collect_knowledge_btn)
    Button collect_knowledge_btn;
    // 简答
    @BindView(R.id.jianda_knowledge_btn)
    Button jianda_knowledge_btn;
    // 难度随机
    @BindView(R.id.random_diff_btn)
    Button random_diff_btn;
    // 难度难
    @BindView(R.id.difficulty_btn)
    Button difficulty_btn;
    // 难度中
    @BindView(R.id.middle_btn)
    Button middle_btn;
    // 难度易
    @BindView(R.id.easy_btn)
    Button easy_btn;
    // 时间滑块
    @BindView(R.id.time_seekBar)
    RangeSeekBar time_seekBar;
    // 默认时间
    @BindView(R.id.timeExam_def_txt)
    TextView timeExam_def_txt;
    // 确认按钮
    @BindView(R.id.submit_exam_btn)
    Button submit_exam_btn;

    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    private Dialog mDialog;
    // 试卷id
    private int paperId;
    // 知识点列表
    private ArrayList<ExamChoseBean.Knowledge> knowledgeList = new ArrayList<>();
    private DialogKnowledgeAdapter dialogAdapter;
    // 阶段列表
    private ArrayList<ExamChoseBean.Stage> stageList = new ArrayList<>();
    private StageAdapter stageAdapter;
    // 问题数量
    private int quesCount;
    // 阶段id
    private int stage;
    // 知识点id
    private String knowledgeID;
    // 试卷名称
    private String paperName;
    // 练习类型
    private int exerciseType;
    // 问题类型
    private int quesType;
    // 难度系数
    private int diffcultyId = -1;
    // 考试时长
    private int paperTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organexam;
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
        // 获取传递二级项目id
        projectId = getIntent().getIntExtra("projectId", 0);
        System.out.println("projectId >>>:" + projectId);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jc.cici.android.atom.utils.NetUtil.isMobileConnected(baseActivity)) {
            // 初始化数据
            initData();
        } else {
            Toast.makeText(baseActivity, "网络连接异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        if (NetUtil.isMobileConnected(baseActivity)) {
            showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = commonPramas();
            Observable<CommonBean<ExamChoseBean>> observable = httpPostService.getExamChoseInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CommonBean<ExamChoseBean>>() {
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
                            Toast.makeText(baseActivity, "网络异常，获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(CommonBean<ExamChoseBean> examChoseBeanCommonBean) {
                            if (100 == examChoseBeanCommonBean.getCode()) {
                                // 获取试卷id
                                paperId = examChoseBeanCommonBean.getBody().getTpaperId();
                                if (0 != paperId) {
                                    // 继续做题布局显示
                                    continue_do_layout.setVisibility(View.VISIBLE);
                                    // 名称隐藏
                                    firstDo_layout.setVisibility(View.VISIBLE);
                                    // 获取试卷名称
                                    paperName = examChoseBeanCommonBean.getBody().getTpaperName();
                                    paperName_txt.setText(paperName);
                                    examName_txt.setText(ToolUtils.getPayTime() + " 组卷");
                                } else {
                                    // 继续做题布局隐藏
                                    continue_do_layout.setVisibility(View.GONE);
                                    // 名称显示
                                    firstDo_layout.setVisibility(View.VISIBLE);
                                    examName_txt.setText(ToolUtils.getPayTime() + " 组卷");
                                }
                                // 获取知识点列表
                                knowledgeList = examChoseBeanCommonBean.getBody().getKnowledges();
                                if (null != knowledgeList) {
                                    knowledgeID = "";
                                    for (int i = 0; i < knowledgeList.size(); i++) {
                                        knowledgeID += knowledgeList.get(i).getKnowledgeId() + ",";
                                    }
                                }
                                // 获取阶段列表
                                if (null != stageList) { // 阶段列表不为空
                                    stageList.clear();
                                    ArrayList<ExamChoseBean.Stage> stages = examChoseBeanCommonBean.getBody().getStages();
                                    if (null != stages && stages.size() > 0) {
                                        for (int i = 0; i < stages.size(); i++) {
                                            if (i == 0) {
                                                stages.get(i).setSelected(true);
                                                stage = stages.get(i).getStageID();
                                            } else {
                                                stages.get(i).setSelected(false);
                                            }
                                        }
                                        stageList.addAll(stages);
                                    }
                                    stageAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(baseActivity, examChoseBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(baseActivity, "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
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
        register_txt.setText("组卷历史");
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("智能组卷");
        noteMore_Btn.setBackgroundResource(R.drawable.icon_note_search);
        noteMore_Btn.setVisibility(View.GONE);
        search_Btn.setBackgroundResource(R.drawable.icon_note_more);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);

        // 填充阶段内容
        stageAdapter = new StageAdapter(baseActivity, stageList);
        gridType.setAdapter(stageAdapter);
        // 设置监听
        gridType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < stageList.size(); i++) {
                    if (i == position) {
                        stageList.get(i).setSelected(true);
                        stage = stageList.get(i).getStageID();
                    } else {
                        stageList.get(i).setSelected(false);
                    }
                }
                stageAdapter.notifyDataSetChanged();
            }
        });

        rangeBar.setMinimumHeight(20);
        rangeBar.setValue(20);
        // 默认20道选中
        quesCount = 20;
        rangeBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                quesCount = (int) min;
                countHintDef.setText("选中(" + (int) min + ")题");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 默认设置10分钟
        time_seekBar.setValue(10);
        // 默认时长
        paperTime = 10;
        time_seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                paperTime = (int) min;
                timeExam_def_txt.setText("选中(" + (int) min + ")分钟");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }


    @OnClick({R.id.back_layout,
            R.id.all_knowledge_btn,
            R.id.specify_know_btn,
            R.id.random_knowledge_btn,
            R.id.have_finish_btn,
            R.id.new_exam_btn,
            R.id.error_exam_btn,
            R.id.random_Type_btn,
            R.id.single_knowledge_btn,
            R.id.mul_knowledge_btn,
            R.id.judge_knowledge_btn,
            R.id.tiankong_knowledge_btn,
            R.id.collect_knowledge_btn,
            R.id.jianda_knowledge_btn,
            R.id.random_diff_btn,
            R.id.difficulty_btn,
            R.id.middle_btn,
            R.id.easy_btn,
            R.id.submit_exam_btn,
            R.id.register_txt,
            R.id.continueDo_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.register_txt: // 组卷历史
                Bundle bundle = new Bundle();
                bundle.putInt("projectId", projectId);
                baseActivity.openActivity(HistoryOrganExamActivity.class, bundle);
                break;
            case R.id.all_knowledge_btn: // 所有知识点按钮
                if (null != knowledgeList) {
                    knowledgeID = "";
                    for (int i = 0; i < knowledgeList.size(); i++) {
                        knowledgeID += knowledgeList.get(i).getKnowledgeId() + ",";
                    }
                }
                // 所有知识点按钮设置选中
                setSelectedBackGroundResource(all_knowledge_btn);
                // 指定知识点按钮设置未选中
                setUnselectBackGroundResource(specify_know_btn);
                break;
            case R.id.specify_know_btn: // 指定知识点按钮

                // 指定知识点按钮设置选中
                setSelectedBackGroundResource(specify_know_btn);
                // 所有知识点按钮设置未选中
                setUnselectBackGroundResource(all_knowledge_btn);
                // 创建对话框
                createDialog();
                break;
            case R.id.random_knowledge_btn: // 针对练习随机
                exerciseType = 0;
                // 选中
                setSelectedBackGroundResource(random_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(have_finish_btn);
                setUnselectBackGroundResource(new_exam_btn);
                setUnselectBackGroundResource(error_exam_btn);
                break;
            case R.id.have_finish_btn: // 已做
                exerciseType = 1;
                // 选中
                setSelectedBackGroundResource(have_finish_btn);
                // 未选中
                setUnselectBackGroundResource(random_knowledge_btn);
                setUnselectBackGroundResource(new_exam_btn);
                setUnselectBackGroundResource(error_exam_btn);
                break;
            case R.id.new_exam_btn: // 全新题
                exerciseType = 2;
                // 选中
                setSelectedBackGroundResource(new_exam_btn);
                // 未选中
                setUnselectBackGroundResource(have_finish_btn);
                setUnselectBackGroundResource(random_knowledge_btn);
                setUnselectBackGroundResource(error_exam_btn);
                break;
            case R.id.error_exam_btn: // 错题
                exerciseType = 3;
                // 选中
                setSelectedBackGroundResource(error_exam_btn);
                // 未选中
                setUnselectBackGroundResource(have_finish_btn);
                setUnselectBackGroundResource(new_exam_btn);
                setUnselectBackGroundResource(random_knowledge_btn);
                break;
            case R.id.random_Type_btn: // 题型 随机
                quesType = 0;
                // 选中
                setSelectedBackGroundResource(random_Type_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.single_knowledge_btn: // 题型 单选
                quesType = 2;
                // 选中
                setSelectedBackGroundResource(single_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(random_Type_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.mul_knowledge_btn:// 题型 多选
                quesType = 3;
                // 选中
                setSelectedBackGroundResource(mul_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(random_Type_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.judge_knowledge_btn:// 题型 判断
                quesType = 6;
                // 选中
                setSelectedBackGroundResource(judge_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(random_Type_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.tiankong_knowledge_btn:// 题型 填空
                // 选中
                setSelectedBackGroundResource(tiankong_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(random_Type_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.collect_knowledge_btn: // 题型 综合
                // 选中
                setSelectedBackGroundResource(collect_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(random_Type_btn);
                setUnselectBackGroundResource(jianda_knowledge_btn);
                break;
            case R.id.jianda_knowledge_btn: // 题型 简答
                // 选中
                quesType = 6;
                setSelectedBackGroundResource(jianda_knowledge_btn);
                // 未选中
                setUnselectBackGroundResource(single_knowledge_btn);
                setUnselectBackGroundResource(mul_knowledge_btn);
                setUnselectBackGroundResource(judge_knowledge_btn);
                setUnselectBackGroundResource(tiankong_knowledge_btn);
                setUnselectBackGroundResource(collect_knowledge_btn);
                setUnselectBackGroundResource(random_Type_btn);
                break;
            case R.id.random_diff_btn: // 难度系数 随机
                diffcultyId = -1;
                // 选中
                setSelectedBackGroundResource(random_diff_btn);
                // 未选中
                setUnselectBackGroundResource(difficulty_btn);
                setUnselectBackGroundResource(middle_btn);
                setUnselectBackGroundResource(easy_btn);
                break;
            case R.id.difficulty_btn: // 难度系数 难
                diffcultyId = 2;
                // 选中
                setSelectedBackGroundResource(difficulty_btn);
                // 未选中
                setUnselectBackGroundResource(random_diff_btn);
                setUnselectBackGroundResource(middle_btn);
                setUnselectBackGroundResource(easy_btn);
                break;
            case R.id.middle_btn: // 难度系数 中
                diffcultyId = 1;
                // 选中
                setSelectedBackGroundResource(middle_btn);
                // 未选中
                setUnselectBackGroundResource(difficulty_btn);
                setUnselectBackGroundResource(random_diff_btn);
                setUnselectBackGroundResource(easy_btn);
                break;
            case R.id.easy_btn: // 难度系数 易
                diffcultyId = 0;
                // 选中
                setSelectedBackGroundResource(easy_btn);
                // 未选中
                setUnselectBackGroundResource(difficulty_btn);
                setUnselectBackGroundResource(middle_btn);
                setUnselectBackGroundResource(random_diff_btn);
                break;
            case R.id.submit_exam_btn: // 创建试卷
                if (!"".equals(examName_txt.getText())) {
                    // 提交请求创建试卷
                    createSubmitData();
                } else {
                    Toast.makeText(baseActivity, "试卷名称不能为空，请指定试卷名称", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.continueDo_btn: // 继续做题按钮监听
                // 获取试卷id
                Bundle cBundle = new Bundle();
                cBundle.putInt("paperId", paperId);
                baseActivity.openActivity(TestActivity.class, cBundle);
                break;
        }
    }

    private void createSubmitData() {

        if (NetUtil.isMobileConnected(baseActivity)) {
            showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
            Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
            HttpPostService httpPostService = retrofit.create(HttpPostService.class);
            RequestBody body = submitPramas();
            Observable<CommonBean<ExamChosePaper>> observable = httpPostService.setExamChosePagerInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CommonBean<ExamChosePaper>>() {
                        @Override
                        public void onCompleted() {
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(baseActivity, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(CommonBean<ExamChosePaper> examChosePaperCommonBean) {
                            if (100 == examChosePaperCommonBean.getCode()) {
                                // 获取试卷id
                                int paperId = examChosePaperCommonBean.getBody().getTpaperId();
                                Bundle bundle = new Bundle();
                                bundle.putInt("paperId", paperId);
                                baseActivity.openActivity(TestActivity.class, bundle);
                            } else {
                                Toast.makeText(baseActivity, examChosePaperCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(baseActivity, "网络异常，创建失败", Toast.LENGTH_SHORT).show();
        }

    }

    private RequestBody submitPramas() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectId", projectId);
            if (0 == paperId) { // 表示第一次进入情况
                obj.put("TpaperName", examName_txt.getText().toString().trim());
            } else {
                obj.put("TpaperName", examName_txt.getText().toString().trim());
            }
            obj.put("QuesCount", quesCount);
            obj.put("Stage", stage);
            obj.put("KnowledgeIds", knowledgeID);
            obj.put("ExerciseType", exerciseType);
            obj.put("QuestionType", quesType);
            obj.put("Difficulty", diffcultyId);
            obj.put("TpaperTime", paperTime);
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
     * 创建弹出框
     */
    private void createDialog() {
        final Dialog dialog = new Dialog(baseActivity,
                R.style.dialog_knowledge);
        dialog.setContentView(R.layout.dialog_knowledge_select);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = -20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        Button sel_knowledge_btn = (Button) dialog.findViewById(R.id.sel_knowledge_btn);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        dialogAdapter = new DialogKnowledgeAdapter(baseActivity, knowledgeList);
        recyclerView.setAdapter(dialogAdapter);

        sel_knowledge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != knowledgeList) {
                    knowledgeID = "";
                    for (int i = 0; i < knowledgeList.size(); i++) {
                        if (knowledgeList.get(i).isSelected()) {
                            knowledgeID += knowledgeList.get(i).getKnowledgeId() + ",";
                        }
                    }
                }
                dialog.dismiss();
            }
        });

    }

    /**
     * 按钮选中设置
     *
     * @param btn
     */
    private void setSelectedBackGroundResource(Button btn) {
        btn.setBackgroundResource(R.drawable.login_button_bg);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    /**
     * 按钮设置未选中
     *
     * @param btn
     */
    private void setUnselectBackGroundResource(Button btn) {
        btn.setBackgroundResource(R.drawable.button_register_bg);
        btn.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    /**
     * 请求body体
     *
     * @return
     */
    private RequestBody commonPramas() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
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

}
