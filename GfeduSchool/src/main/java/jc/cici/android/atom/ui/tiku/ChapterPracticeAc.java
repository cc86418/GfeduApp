package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ExamKnowledgeBean;
import jc.cici.android.atom.bean.NewChapterPracticeBean;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.bean.ChildNode;
import jc.cici.android.atom.ui.tiku.bean.ParentNode;
import jc.cici.android.atom.ui.tiku.newDoExam.TestActivity;
import jc.cici.android.atom.ui.tiku.treeView.TreeNode;
import jc.cici.android.atom.ui.tiku.treeView.TreeViewAdapter;
import jc.cici.android.atom.ui.tiku.viewbind.ChildNodeBinder;
import jc.cici.android.atom.ui.tiku.viewbind.ParentNodeBinder;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.TopMiddlePopup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 章节练习activity
 * Created by atom on 2018/1/3.
 */

public class ChapterPracticeAc extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题布局
    @BindView(R.id.titleName_layout)
    RelativeLayout titleName_layout;
    // 标题
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    // 继续做题布局
    @BindView(R.id.pro_layout)
    RelativeLayout pro_layout;
    // 试卷名称
    @BindView(R.id.paperName_txt)
    TextView paperName_txt;
    // 继续做题按钮
    @BindView(R.id.continueDo_btn)
    Button continueDo_btn;
    // recyclerView
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    // 试卷id
    private int paperId;
    private LinearLayoutManager linearLayoutManager;
    private TreeViewAdapter adapter;
    private List<TreeNode> nodes = new ArrayList<>();
    // 二级项目列表
    private ArrayList<TikuHomeBean.TikuProject.Node> nodeList = new ArrayList<>();
    private TopMiddlePopup topMiddlePopup;
    private Dialog mDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chapter_practice;
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
        projectId = getIntent().getIntExtra("projectId", 0);
        System.out.println("projectId >>>:" + projectId);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            // 初始化数据
            initData();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
        Observable.zip(httpPostService.getExamListInfo(body), httpPostService.examKnowledgeInfo(body), new Func2<CommonBean<TikuHomeBean>, CommonBean<ExamKnowledgeBean>, NewChapterPracticeBean>() {
            @Override
            public NewChapterPracticeBean call(CommonBean<TikuHomeBean> tikuHomeBeanCommonBean, CommonBean<ExamKnowledgeBean> historyExamChoseBeanCommonBean) {
                return new NewChapterPracticeBean(tikuHomeBeanCommonBean, historyExamChoseBeanCommonBean);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewChapterPracticeBean>() {
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
                    public void onNext(NewChapterPracticeBean newChapterPracticeBean) {
                        if (100 == newChapterPracticeBean.tikuHomeBean.getCode() && 100 == newChapterPracticeBean.examKnowledgeBean.getCode()) {
                            ArrayList<TikuHomeBean.TikuProject> parentList = newChapterPracticeBean.tikuHomeBean.getBody().getList();
                            if (null != parentList && parentList.size() > 0) {
                                for (int i = 0; i < parentList.size(); i++) {
                                    ArrayList<TikuHomeBean.TikuProject.Node> secondList = parentList.get(i).getLevelTwo();
                                    if (!"null".equals(secondList) && secondList.size() > 0) {
                                        for (int j = 0; j < secondList.size(); j++) {
                                            if (projectId == secondList.get(j).getProjectId()) {
                                                if (nodeList.size() > 0) {
                                                    nodeList.clear();
                                                }
                                                nodeList.addAll(secondList);
                                                // 设置标题
                                                titleName_txt.setText(secondList.get(j).getProjectName());
                                                break;
                                            }
                                        }

                                    }
                                }
                            }

                            paperId = newChapterPracticeBean.examKnowledgeBean.getBody().getTpaperId();
                            // 试卷名称
                            String tName = newChapterPracticeBean.examKnowledgeBean.getBody().getKnowledgeName();
                            if (0 != paperId) {
                                pro_layout.setVisibility(View.VISIBLE);
                                paperName_txt.setText(tName);
                            } else {
                                pro_layout.setVisibility(View.GONE);
                            }
                            // 获取内容
                            ArrayList<ExamKnowledgeBean.ParentNode> datas = newChapterPracticeBean.examKnowledgeBean.getBody().getList();

                            if (null != datas && !"null".equals(datas) && datas.size() > 0) {
                                for (int i = 0; i < datas.size(); i++) { // 第一层
                                    // 获取pkId
                                    int pkId = datas.get(i).getData().getCoursewareData_PKID();
                                    // 知识点题目
                                    String pkName = datas.get(i).getData().getCoursewareData_Name();
                                    // 获取总数量
                                    int allCount = datas.get(i).getData().getQuesCount();
                                    // 获取已做题数量
                                    int haveDo = datas.get(i).getData().getDoCount();
                                    // 获取正确率
                                    String rightRate = datas.get(i).getData().getRightRatio();
                                    // 获取父类节点
                                    TreeNode<ParentNode> parentNode = new TreeNode<>(new ParentNode(pkId, pkName, allCount, haveDo, rightRate));
                                    nodes.add(parentNode);
                                    ArrayList<ExamKnowledgeBean.ParentNode> childList = datas.get(i).getList();
                                    if (null != childList && !"null".equals(childList) && childList.size() > 0) {
                                        for (int j = 0; j < childList.size(); j++) { // 第二层
                                            ArrayList<ExamKnowledgeBean.ParentNode> threeList = childList.get(j).getList();
                                            if (null != threeList && !"null".equals(threeList) && threeList.size() > 0) { // 有第三层
                                                TreeNode<ChildNode> childNode = new TreeNode<>(
                                                        new ChildNode(
                                                                childList.get(j).getData().getCoursewareData_PKID(),
                                                                childList.get(j).getData().getCoursewareData_Name(),
                                                                childList.get(j).getData().getQuesCount(),
                                                                childList.get(j).getData().getDoCount(),
                                                                childList.get(j).getData().getRightRatio()
                                                        )

                                                );
                                                for (int k = 0; k < threeList.size(); k++) { // 第三层
                                                    childNode.addChild(new TreeNode<>(
                                                            new ChildNode(
                                                                    threeList.get(k).getData().getCoursewareData_PKID(),
                                                                    threeList.get(k).getData().getCoursewareData_Name(),
                                                                    threeList.get(k).getData().getQuesCount(),
                                                                    threeList.get(k).getData().getDoCount(),
                                                                    threeList.get(k).getData().getRightRatio()
                                                            )
                                                    ));
                                                    ArrayList<ExamKnowledgeBean.ParentNode> fourList = threeList.get(k).getList();
                                                    if (null != fourList && !"null".equals(fourList) && fourList.size() > 0) { // 第四层
                                                        for (int p = 0; p < fourList.size(); p++) {
                                                            childNode.addChild(new TreeNode<>(
                                                                    new ChildNode(
                                                                            fourList.get(p).getData().getCoursewareData_PKID(),
                                                                            fourList.get(p).getData().getCoursewareData_Name(),
                                                                            fourList.get(p).getData().getQuesCount(),
                                                                            fourList.get(p).getData().getDoCount(),
                                                                            fourList.get(p).getData().getRightRatio()
                                                                    )
                                                            ));
                                                        }
                                                        parentNode.addChild(childNode);
                                                    }
                                                }
                                                parentNode.addChild(childNode);

                                            } else { // 仅有两层情况
                                                parentNode.addChild(
                                                        new TreeNode<>(
                                                                new ChildNode(
                                                                        childList.get(j).getData().getCoursewareData_PKID(),
                                                                        childList.get(j).getData().getCoursewareData_Name(),
                                                                        childList.get(j).getData().getQuesCount(),
                                                                        childList.get(j).getData().getDoCount(),
                                                                        childList.get(j).getData().getRightRatio()
                                                                )
                                                        )
                                                );
                                            }
                                        }
                                    }
                                }
                                linearLayoutManager = new LinearLayoutManager(baseActivity);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                adapter = new TreeViewAdapter(nodes, Arrays.asList(new ChildNodeBinder(baseActivity, projectId), new ParentNodeBinder(baseActivity, projectId)));
                                adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
                                    @Override
                                    public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {

                                        if (node.isRoot()) {
                                            ParentNodeBinder.ViewHolder parentViewHolder = (ParentNodeBinder.ViewHolder) holder;
                                            if (node.isExpand()) {
                                                parentViewHolder.node_close_img.setBackgroundResource(R.drawable.icon_node_close);
                                            } else {
                                                parentViewHolder.node_close_img.setBackgroundResource(R.drawable.icon_node_open);
                                            }
                                        } else {

                                            if (!node.isLeaf()) {
                                                onToggle(!node.isExpand(), holder);
                                            }
                                        }
                                        return false;
                                    }

                                    @Override
                                    public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                                        ChildNodeBinder.ViewHolder childNodeBinder = (ChildNodeBinder.ViewHolder) holder;
                                        if (isExpand) {
                                            childNodeBinder.child_node_close_img.setBackgroundResource(R.drawable.icon_node_open);
                                        } else {
                                            childNodeBinder.child_node_close_img.setBackgroundResource(R.drawable.icon_node_close);
                                        }
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter = new TreeViewAdapter(nodes, null);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } else if (100 != newChapterPracticeBean.tikuHomeBean.getCode()) { // 项目列表请求错误
                            Toast.makeText(baseActivity, newChapterPracticeBean.tikuHomeBean.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (100 != newChapterPracticeBean.examKnowledgeBean.getCode()) { // 做题列表请求错误
                            Toast.makeText(baseActivity, newChapterPracticeBean.examKnowledgeBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void initView() {
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
    }


    @OnClick({R.id.back_layout, R.id.titleName_layout, R.id.continueDo_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.titleName_layout:
                int screenWidth = ToolUtils.getScreenPixels(baseActivity).widthPixels;
                int screenHeight = ToolUtils.getScreenPixels(baseActivity).heightPixels;
                topMiddlePopup = new TopMiddlePopup(baseActivity, screenWidth, screenHeight, onItemClickListener, nodeList);
                topMiddlePopup.show(titleName_txt);
                break;
            case R.id.continueDo_btn: // 继续做题
                Intent prIt = new Intent(baseActivity, TestActivity.class);
                Bundle prBundle = new Bundle();
                prBundle.putInt("paperId", paperId);
                prIt.putExtras(prBundle);
                baseActivity.startActivity(prIt);
                baseActivity.finish();
                break;
        }
    }


    private BaseRecycleerAdapter.OnItemClickListener onItemClickListener = new BaseRecycleerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            projectId = nodeList.get(position).getProjectId();
            titleName_txt.setText(nodeList.get(position).getProjectName());
            nodes.clear();
            initData();
            topMiddlePopup.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
