package jc.cici.android.atom.ui.tiku;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.gfedu.home_pager.BaseFragment;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.TikuProAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ContentExamHomeBean;
import jc.cici.android.atom.bean.QrBean;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.EveryDayTestAc;
import jc.cici.android.atom.ui.tiku.newDoExam.OrganTestActivity;
import jc.cici.android.atom.ui.tiku.newDoExam.RecordDoExamAc;
import jc.cici.android.atom.ui.tiku.newDoExam.TestActivity;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.TopMiddlePopup;
import jc.cici.android.google.zxing.activity.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by atom on 2017/12/20.
 */

public class TiKuHomeFragment extends BaseFragment {

    private Activity mCtx;
    private Unbinder unbinder;
    // 左边菜单
    @BindView(R.id.nav_btn)
    Button nav_btn;
    // 中间布局
    @BindView(R.id.titleName_layout)
    RelativeLayout titleName_layout;
    // 标题
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    @BindView(R.id.down_img)
    ImageView down_img;
    // 右边人工
    @BindView(R.id.custom_btn)
    Button custom_btn;
    // 历史正确率
    @BindView(R.id.his_rateRight_count)
    TextView his_rateRight_count;
    // 当前正确率
    @BindView(R.id.now_rateRight_count)
    TextView now_rateRight_count;
    // 历史作对个数
    @BindView(R.id.his_Right_count)
    TextView his_Right_count;
    // 当前作对个数
    @BindView(R.id.now_Right_count)
    TextView now_Right_count;
    // 总题数
    @BindView(R.id.all_exam_count)
    TextView all_exam_count;
    // 组卷布局
    @BindView(R.id.organize_layout)
    LinearLayout organize_layout;
    // 组卷个数
    @BindView(R.id.numberOrganTime_txt)
    TextView numberOrganTime_txt;
    // 章节布局
    @BindView(R.id.chapterP1_layout)
    RelativeLayout chapterP1_layout;
    // 章节练习正确率
    @BindView(R.id.rightRateCount_txt)
    TextView rightRateCount_txt;
    // 章节练习百分比
    @BindView(R.id.progress_count_txt)
    TextView progress_count_txt;
    // 章节进度
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    // 每日一题布局
    @BindView(R.id.pr1_layout)
    RelativeLayout pr1_layout;
    // 每日一题提示
    @BindView(R.id.dayNotice_img)
    ImageView dayNotice_img;
    // 每日一天坚持天数
    @BindView(R.id.continuePratice_txt)
    TextView continuePratice_txt;
    // 每月一测布局
    @BindView(R.id.pr2_layout)
    RelativeLayout pr2_layout;
    // 每月一测提示
    @BindView(R.id.monthNotice_img)
    ImageView monthNotice_img;
    // 每月一测最高得分
    @BindView(R.id.monthPratice_txt)
    TextView monthPratice_txt;
    // 模拟组卷布局
    @BindView(R.id.modelOrg_layout)
    RelativeLayout modelOrg_layout;
    // 模拟测试布局
    @BindView(R.id.modelExam_layout)
    RelativeLayout modelExam_layout;
    // 历年真题
    @BindView(R.id.hisExam_layout)
    RelativeLayout hisExam_layout;
    // 错题集
    @BindView(R.id.errorList_txt)
    TextView errorList_txt;
    // 收藏夹
    @BindView(R.id.collect_txt)
    TextView collect_txt;
    // 二维码扫描
    @BindView(R.id.qr_txt)
    TextView qr_txt;
    // 做题记录
    @BindView(R.id.record_txt)
    TextView record_txt;
    //能力评估
    @BindView(R.id.evaluate_txt)
    TextView evaluate_txt;

    private TopMiddlePopup topMiddlePopup;
    private View view;
    private Dialog mDialog;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    ArrayList<TikuHomeBean.TikuProject.Node> nodeList = new ArrayList<TikuHomeBean.TikuProject.Node>();
    private LinearLayoutManager linarLayoutManger;
    private ArrayList<TikuHomeBean.TikuProject> mDatas = new ArrayList<>();
    private TikuProAdapter adapter;
    // 一级项目id
    private int parentNodeId;
    // 二级项目id
    private int projectId;
    private String title;
    // 每日一题id
    private int everyDayQueId;
    // 每月一测id
    private int everyMonthExamId;

    public TiKuHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this.getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
//        projectId = nodeList.get(position).getProjectId();
//        title = nodeList.get(position).getProjectName();
//        titleName_txt.setText(title);
        int defaultProID = sp.getInt("defChildNodeId", 0);
        if (0 != defaultProID && projectId != defaultProID) {
            SharedPreferences.Editor old_editor = sp.edit();
            old_editor.clear();
            old_editor.commit();
            sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_CHILDID, Activity.MODE_PRIVATE);
            SharedPreferences.Editor new_editor = sp.edit();
            new_editor.putInt("defChildNodeId", projectId);
            new_editor.commit();
            getContentDataFromHttp(true, projectId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tiku_home, null, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        if (NetUtil.isMobileConnected(mCtx)) {
            if (0 != userId) {
                if (0 != parentNodeId) { // 本地已经存在一级项目id
                    initData(true, parentNodeId);
                } else { // 本地不存在一级项目id,从网上取出默认
                    initData(false, 0);
                }
            } else {
                Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(mCtx, "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void initData(boolean flag, final int pNodeId) {

        showProcessDialog(mCtx, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(flag, pNodeId);
        Observable<CommonBean<TikuHomeBean>> observable = httpPostService.getExamListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<TikuHomeBean>>() {
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
                        e.printStackTrace();
                        Toast.makeText(mCtx, "网络请求异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<TikuHomeBean> tikuHomeBeanCommonBean) {

                        if (100 == tikuHomeBeanCommonBean.getCode()) {
                            if (0 == parentNodeId) {
                                // 默认一级项目id
                                parentNodeId = tikuHomeBeanCommonBean.getBody().getDefProjectId();
                                sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_PARENTID, Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("defParentNodeId", parentNodeId);
                                editor.commit();
                            }

                            if (0 == projectId) {
                                // 默认一级项目对应二级项目id
                                projectId = tikuHomeBeanCommonBean.getBody().getDefChildProjectId();
                                sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_CHILDID, Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("defChildNodeId", projectId);
                                editor.commit();
                            }
                            ArrayList<TikuHomeBean.TikuProject> datas = tikuHomeBeanCommonBean.getBody().getList();
                            if (null != datas && datas.size() > 0) {
                                // 清除父类数据
                                mDatas.clear();
                                // 重新添加子类数据
                                mDatas.addAll(datas);
                                for (int i = 0; i < mDatas.size(); i++) {
                                    // 获取二级项目数据
                                    if (parentNodeId == datas.get(i).getProjectId()) {
                                        // 获取子列表数据
                                        ArrayList<TikuHomeBean.TikuProject.Node> nodes = datas.get(i).getLevelTwo();
                                        projectId = nodes.get(0).getProjectId();
                                        nodeList.clear();
                                        nodeList.addAll(nodes);
                                    }

                                }
                                // 获取默认显示的二级项目名称
                                for (int i = 0; i < nodeList.size(); i++) {
                                    if (projectId == nodeList.get(i).getProjectId()) {
                                        title = nodeList.get(i).getProjectName();
                                        titleName_txt.setText(title);
                                        break;
                                    }
                                }
                                // 请求填充数据
                                getContentDataFromHttp(false, projectId);
                                System.out.println("projectId >>>>>>>>" + projectId);
                            }

                        } else {
                            Toast.makeText(mCtx, tikuHomeBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    /**
     * 获取内容数据
     *
     * @param flag
     * @param projectId
     */

    private void getContentDataFromHttp(boolean flag, int projectId) {
        if (flag) {
//            mDialog.show();
            showProcessDialog(mCtx, R.layout.loading_show_dialog_color);
        }
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(true, projectId);
        Observable<CommonBean<ContentExamHomeBean>> observable = httpPostService.getExamIndexInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ContentExamHomeBean>>() {
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
                        e.printStackTrace();
                        Toast.makeText(mCtx, "请求获取数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ContentExamHomeBean> contentExamHomeBeanCommonBean) {
                        if (100 == contentExamHomeBeanCommonBean.getCode()) {
                            // 获取正确率
                            String rightRatio = contentExamHomeBeanCommonBean.getBody().getRightRatio();
                            if (!rightRatio.isEmpty()) {
                                SpannableString styledText = new SpannableString(rightRatio);
                                styledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style1_exam), 0, rightRatio.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                styledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style2_exam), rightRatio.length() - 1, rightRatio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                his_rateRight_count.setText(styledText, TextView.BufferType.SPANNABLE);

//                                String strR = "正确率";
//                                SpannableString nowStyledText = new SpannableString(strR + " " + rightRatio);
//                                nowStyledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style3_exam), 0, nowStyledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                nowStyledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style5_exam), strR.length()+1, strR.length() + 1 + rightRatio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                now_rateRight_count.setText(nowStyledText, TextView.BufferType.SPANNABLE);
                            }
                            // 获取作对个数
                            int rightCount = contentExamHomeBeanCommonBean.getBody().getRightCount();
                            String strC = "道";
                            String strCount = rightCount + " " + strC;
                            SpannableString styled2Text = new SpannableString(strCount);
                            styled2Text.setSpan(new TextAppearanceSpan(mCtx, R.style.style1_exam), 0, strCount.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            styled2Text.setSpan(new TextAppearanceSpan(mCtx, R.style.style2_exam), strCount.length() - 1, strCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            his_Right_count.setText(styled2Text, TextView.BufferType.SPANNABLE);

//                            String strRC = "作对";
//                            SpannableString nowStyledText = new SpannableString(strRC + " " + rightCount + "道");
//                            nowStyledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style6_exam), 0, strRC.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            nowStyledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style4_exam), strRC.length() + 1, strRC.length() + 1 + String.valueOf(rightCount).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            now_Right_count.setText(nowStyledText, TextView.BufferType.SPANNABLE);

                            // 总做题数量
                            int allDoCount = contentExamHomeBeanCommonBean.getBody().getTotalCount();
                            String strCt = "道";
                            String allCount = allDoCount + " " + strCt;
                            SpannableString totalStyleedText = new SpannableString(allCount);
                            totalStyleedText.setSpan(new TextAppearanceSpan(mCtx, R.style.style1_exam), 0, allCount.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            totalStyleedText.setSpan(new TextAppearanceSpan(mCtx, R.style.style2_exam), allCount.length() - 1, allCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            all_exam_count.setText(totalStyleedText, TextView.BufferType.SPANNABLE);
                            // 组卷数量
                            int organExamCount = contentExamHomeBeanCommonBean.getBody().getZuJuanCount();
                            numberOrganTime_txt.setText("已组卷" + organExamCount + "次");
                            //  章节练习正确率
                            String chapterRatio = contentExamHomeBeanCommonBean.getBody().getZhangRatio();
                            if (!chapterRatio.isEmpty()) {
                                rightRateCount_txt.setText(chapterRatio);
                            }
                            // 章节练习百分比
                            String percentChapter = contentExamHomeBeanCommonBean.getBody().getZhangPercent();
                            if (!percentChapter.isEmpty()) {
                                progress_count_txt.setText(percentChapter);
                            }
                            String[] str = percentChapter.split("/");
                            String doStr = str[0].replaceAll(" ", "");
                            String unDoStr = str[1].replaceAll(" ", "");
                            if (!"0".equals(doStr) && !"0".equals(unDoStr)) {
                                int progress = (Math.round(Integer.parseInt(doStr) * 100 / Integer.parseInt(unDoStr)));
                                progressBar.setProgress(progress);
                            } else {
                                progressBar.setProgress(0);
                            }

                            // 坚持做题天数
                            int coutinueDay = contentExamHomeBeanCommonBean.getBody().getEveryDayCount();
                            continuePratice_txt.setText("坚持做题" + coutinueDay + "天");
                            // 最高得分
                            int maxScore = contentExamHomeBeanCommonBean.getBody().getMonthlyScore();
                            monthPratice_txt.setText("最高得分" + maxScore + "分");
                            // 获取每日一题id
                            everyDayQueId = contentExamHomeBeanCommonBean.getBody().getEveryDayQuesId();
                            if (everyDayQueId == 0) {
                                dayNotice_img.setVisibility(View.VISIBLE);
                            } else {
                                dayNotice_img.setVisibility(View.GONE);
                            }
                            // 每月一测id
                            everyMonthExamId = contentExamHomeBeanCommonBean.getBody().getMonthlyTpaperId();
                            // 每月一测做题状态
                            int status = contentExamHomeBeanCommonBean.getBody().getMonthlyStatus();
                            if (0 == status) {
                                monthNotice_img.setVisibility(View.VISIBLE);
                            } else if (1 == status) {
                                monthNotice_img.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(mCtx, contentExamHomeBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * 创建自定义对话框
     */
    private void createDialog() {
        final Dialog dialog = new Dialog(mCtx,
                R.style.exam_dialog);
        dialog.setContentView(R.layout.dialog_tiku);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // 根布局
        RelativeLayout rootDialog_layout = (RelativeLayout) dialog.findViewById(R.id.rootDialog_layout);
        //  项目列表
        RecyclerView proRecyclerView = (RecyclerView) dialog.findViewById(R.id.proRecyclerView);
        // 称赞布局
        LinearLayout praise_layout = (LinearLayout) dialog.findViewById(R.id.praise_layout);
        // 称赞图片
        ImageView praiseImg = (ImageView) dialog.findViewById(R.id.praiseImg);
        // 更多功能按钮
        Button moreFunction_Btn = (Button) dialog.findViewById(R.id.moreFunction_Btn);
        // 关注微信按钮
        Button followBtn = (Button) dialog.findViewById(R.id.followBtn);
        linarLayoutManger = new LinearLayoutManager(mCtx);
        proRecyclerView.setLayoutManager(linarLayoutManger);
        adapter = new TikuProAdapter(mCtx, mDatas);
        proRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 一级项目id
                parentNodeId = mDatas.get(position).getProjectId();
                int defaultParentNodeID = sp.getInt("defParentNodeId", 0);
                if (0 != parentNodeId && parentNodeId != defaultParentNodeID) {
                    SharedPreferences.Editor old_editor = sp.edit();
                    old_editor.clear();
                    old_editor.commit();
                    sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_PARENTID, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor new_editor = sp.edit();
                    new_editor.putInt("defParentNodeId", parentNodeId);
                    new_editor.commit();
                    // 重新请求获取数据
                    initData(true, parentNodeId);
                }
                dialog.dismiss();
            }
        });
        // 根布局监听
        setOnClick(rootDialog_layout, dialog);
        // 点赞布局监听
        setOnClick(praise_layout, dialog);
        // 更多按钮监听
        setOnClick(moreFunction_Btn, dialog);
        // 微信公众号按钮监听
        setOnClick(followBtn, dialog);
    }

    /**
     * dialog按钮监听
     *
     * @param view
     * @param dialog
     */
    private void setOnClick(View view, final Dialog dialog) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rootDialog_layout:
                        dialog.dismiss();
                        break;
                    case R.id.praise_layout: // 点赞布局
                        Toast.makeText(mCtx, "点赞布局", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.moreFunction_Btn: // 更多功能按钮
                        Intent it = new Intent(mCtx, MoreActivity.class);
                        mCtx.startActivity(it);
                        dialog.dismiss();
                        break;
                    case R.id.followBtn: // 关注微信公众号
                        Intent it2 = new Intent(mCtx, FollowWechatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("projectId", projectId);
                        it2.putExtras(bundle);
                        mCtx.startActivity(it2);
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    private void initView() {

        sp = mCtx.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        if (0 == userId) {
            down_img.setVisibility(View.VISIBLE);
        }
        // 取出存放本地的一级项目id
        sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_PARENTID, Activity.MODE_PRIVATE);
        parentNodeId = sp.getInt("defParentNodeId", 0);
        // 取出存放本地的二级项目id
        sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_CHILDID, Activity.MODE_PRIVATE);
        projectId = sp.getInt("defChildNodeId", 0);

    }

    @Override
    public void fetchData() {

    }

    /**
     * 请求body体
     *
     * @param flag
     * @param projectId
     * @return
     */
    private RequestBody commonPramas(boolean flag, int projectId) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 用户id
            obj.put("userId", userId);
            if (flag == true) {
                // 0 表示全部
                obj.put("projectId", projectId);
            }
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

    @OnClick({R.id.nav_btn,
            R.id.custom_btn,
            R.id.titleName_txt,
            R.id.down_img,
            R.id.organize_layout,
            R.id.chapterP1_layout,
            R.id.pr1_layout,
            R.id.pr2_layout,
            R.id.modelOrg_layout,
            R.id.modelExam_layout,
            R.id.hisExam_layout,
            R.id.errorList_txt,
            R.id.collect_txt,
            R.id.qr_txt,
            R.id.record_txt,
            R.id.evaluate_txt})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_btn: // nav按钮
                createDialog();
                break;
            case R.id.custom_btn: // 客服监听
                if (0 != userId) {
                    Uri uri = Uri.parse("http://looyuoms7623.looyu.com/chat/chat/p.do?c=20000653&f=10050794&g=10056807&refer=zjfbaidu&u=f0c5d80117937320b3a9ed0d734b3c1816&v=e3f1cb37f77f53b2b2ea5b03e123beeb21&command=&_d=1502417453270");
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.titleName_txt: // 标题布局
                if (0 != userId) {
                    int screenWidth = ToolUtils.getScreenPixels(mCtx).widthPixels;
                    int screenHeight = ToolUtils.getScreenPixels(mCtx).heightPixels;
                    topMiddlePopup = new TopMiddlePopup(mCtx, screenWidth, screenHeight, onItemClickListener, nodeList);
                    topMiddlePopup.show(titleName_txt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.organize_layout: // 智能组卷
                if (0 != userId) {
                    Intent it = new Intent(mCtx, OrganExamActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("projectId", projectId);
                    it.putExtras(bundle);
                    mCtx.startActivity(it);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.chapterP1_layout: // 章节练习
                if (0 != userId) {
                    Intent cIt = new Intent(mCtx, ChapterPracticeAc.class);
                    Bundle cBundle = new Bundle();
                    cBundle.putInt("projectId", projectId);
                    cIt.putExtras(cBundle);
                    mCtx.startActivity(cIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.pr1_layout: // 每日一题
                if (0 != userId) {
                    Intent pIt = new Intent(mCtx, EveryDayTestAc.class);
                    Bundle pBundle = new Bundle();
                    pBundle.putInt("paperId", 0);
                    pBundle.putInt("isDoQues", everyDayQueId);
                    pBundle.putInt("searchType", 1);
                    pBundle.putInt("projectId", projectId);
                    pIt.putExtras(pBundle);
                    mCtx.startActivity(pIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.pr2_layout: // 每月一测
                if (0 != userId) {
                    if (0 != everyMonthExamId) {
                        Intent prIt = new Intent(mCtx, TestActivity.class);
                        Bundle prBundle = new Bundle();
                        prBundle.putInt("paperId", everyMonthExamId);
                        prIt.putExtras(prBundle);
                        mCtx.startActivity(prIt);
                    } else {
                        Toast.makeText(mCtx, "暂无测试内容", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.modelOrg_layout: // 模拟组卷
                if (0 != userId) {
                    Intent oIt = new Intent(mCtx, OrganTestActivity.class);
                    Bundle oBundle = new Bundle();
                    oBundle.putInt("projectId", projectId);
                    oBundle.putInt("tPaperType", 2);
                    oIt.putExtras(oBundle);
                    mCtx.startActivity(oIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modelExam_layout: // 模拟测试
                if (0 != userId) {
                    Intent mIt = new Intent(mCtx, OrganTestActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("projectId", projectId);
                    mBundle.putInt("tPaperType", 4);
                    mIt.putExtras(mBundle);
                    mCtx.startActivity(mIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.hisExam_layout: // 历年真题
                if (0 != userId) {
                    Toast.makeText(mCtx, "暂未开放", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.errorList_txt: // 错题集
                if (0 != userId) {
                    Intent errIt = new Intent(mCtx, ErrorSetActivity.class);
                    Bundle errBundle = new Bundle();
                    errBundle.putInt("searchType", 2);
                    errBundle.putInt("projectId", projectId);
                    errIt.putExtras(errBundle);
                    mCtx.startActivity(errIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.collect_txt: // 收藏夹
                if (0 != userId) {
                    Intent collIt = new Intent(mCtx, ErrorSetActivity.class);
                    Bundle collBundle = new Bundle();
                    collBundle.putInt("searchType", 1);
                    collBundle.putInt("projectId", projectId);
                    collIt.putExtras(collBundle);
                    mCtx.startActivity(collIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.qr_txt: // 扫题
                if (0 != userId) {
                    if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mCtx, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        startActivityForResult(new Intent(mCtx, CaptureActivity.class), 1);
                    }
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.record_txt: // 做题记录
                if (0 != userId) {
                    Intent rIt = new Intent(mCtx, RecordDoExamAc.class);
                    Bundle rBundle = new Bundle();
                    rBundle.putInt("projectId", projectId);
                    rBundle.putInt("tPaperType", 0);
                    rIt.putExtras(rBundle);
                    mCtx.startActivity(rIt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.evaluate_txt: // 能力评估
                if (0 != userId) {
                    Toast.makeText(mCtx, "能力评估", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.down_img:
                if (0 != userId) {
                    int screenWidth1 = ToolUtils.getScreenPixels(mCtx).widthPixels;
                    int screenHeight1 = ToolUtils.getScreenPixels(mCtx).heightPixels;
                    topMiddlePopup = new TopMiddlePopup(mCtx, screenWidth1, screenHeight1, onItemClickListener, nodeList);
                    topMiddlePopup.show(titleName_txt);
                } else {
                    Toast.makeText(mCtx, "亲，登录后才能做题哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private BaseRecycleerAdapter.OnItemClickListener onItemClickListener = new BaseRecycleerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            projectId = nodeList.get(position).getProjectId();
            title = nodeList.get(position).getProjectName();
            titleName_txt.setText(title);
            int defaultProID = sp.getInt("defChildNodeId", 0);
            if (0 != defaultProID && projectId != defaultProID) {
                SharedPreferences.Editor old_editor = sp.edit();
                old_editor.clear();
                old_editor.commit();
                sp = mCtx.getSharedPreferences(Global.EXAM_DEFAULT_CHILDID, Activity.MODE_PRIVATE);
                SharedPreferences.Editor new_editor = sp.edit();
                new_editor.putInt("defChildNodeId", projectId);
                new_editor.commit();
                getContentDataFromHttp(true, projectId);
            }
            topMiddlePopup.dismiss();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (2 == resultCode) { // 二维码扫描
                String result = data.getStringExtra("result");
                System.out.println("back result >>>:" + result);
                if (null != result && !"null".equals(result)) {
                    QrBean qrBean = JSON.parseObject(result, QrBean.class);
                    // 试卷类型
                    String qrType = qrBean.getQRCodeType();
                    // 获取试卷id/试题id
                    int tPaperID = qrBean.getID();
                    if ("42523077".equals(qrType)) { // 试卷类型
                        Intent it = new Intent(mCtx, TestActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("paperId", tPaperID);
                        bundle.putInt("classid", 0);
                        bundle.putInt("stageid", 0);
                        bundle.putInt("lessonid", 0);
                        bundle.putString("title", "");
                        bundle.putString("studyKey", "");
                        it.putExtras(bundle);
                        startActivity(it);
                    } else if ("42524466".equals(qrType)) { // 试题类型
                        Intent pIt = new Intent(mCtx, EveryDayTestAc.class);
                        Bundle pBundle = new Bundle();
                        pBundle.putInt("paperId", tPaperID);
                        pBundle.putInt("quesId", qrBean.getID());
                        pBundle.putInt("searchType", 3);
                        pBundle.putInt("projectId", 0);
                        pIt.putExtras(pBundle);
                        mCtx.startActivity(pIt);
                    }
                } else {
                    Toast.makeText(mCtx, "扫码失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
