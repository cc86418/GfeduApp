package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.HistoryRecyclerAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.HistoryLesson;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程搜索
 * Created by atom on 2017/5/13.
 */

public class SearchActivity extends BaseActivity {

    // 查询view
    @BindView(R.id.editSearch_Txt)
    EditText editSearch_Txt;
    // 取消文字
    @BindView(R.id.cancel_Txt)
    TextView cancel_Txt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    ImageView emptyView;

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Dialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<HistoryLesson> mData;
    private HistoryRecyclerAdapter adapter;
    // 班型id
    private int userId;
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
    // 搜索关键字
    private String keyWord;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
        classId = getIntent().getIntExtra("classId", 0);
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

    private void initView() {
        emptyView.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    /**
     * 网络请求获取数组
     */
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
                obj.put("keyword", keyWord);
                obj.put("oauth", ToolUtils.getMD5Str(userId + commParam.getTimeStamp() + "android!%@%$@#$"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
            Observable<CommonBean<HistoryInfo>> observable = httpPostService.getHistoryLessonInfo(body);
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new rx.Subscriber<CommonBean<HistoryInfo>>() {
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
                                        Toast.makeText(baseActivity, "网络请求错误", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNext(CommonBean<HistoryInfo> historyInfoBean) {

                                    if (100 == historyInfoBean.getCode()) {
                                        emptyView.setVisibility(View.GONE);
                                        mData = (ArrayList<HistoryLesson>) historyInfoBean.getBody().getHistoryLessonList();
                                        if (null != mData && mData.size() > 0) {
                                            adapter = new HistoryRecyclerAdapter(baseActivity, mData);
                                            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
                                            animationAdapter.setDuration(1000);
                                            recyclerView.setAdapter(animationAdapter);

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
                                                            loading();
                                                        }
                                                    }
                                                }
                                            });
                                            /**
                                             * item 点击
                                             */
                                            adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("titleName", mData.get(position).getLessonName());
                                                    bundle.putInt("classId", classId);
                                                    bundle.putInt("stageId", stageId);
                                                    bundle.putInt("lessonId", mData.get(position).getLessonId());
                                                    bundle.putInt("stageNote", stageNote);
                                                    bundle.putInt("stageProblem", stageProblem);
                                                    bundle.putInt("stageInformation", stageInformation);
                                                    baseActivity.openActivity(ChapterActivity.class, bundle);
                                                }
                                            });
                                        } else {
                                            emptyView.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        Toast.makeText(baseActivity, historyInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(baseActivity, "用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载更多
     */
    private void loading() {

    }

    @OnClick({R.id.cancel_Txt})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancel_Txt: // 取消按钮
                if ("提交".equals(cancel_Txt.getText().toString())) { // 提交按钮
                    if (NetUtil.isMobileConnected(this)) {
                        initData();
                    } else {
                        Toast.makeText(this, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } else { // 取消按钮监听
                    baseActivity.finish();
                }
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.editSearch_Txt)
    void afterTextChanged(CharSequence text) {
        if (text.length() > 0) {
            keyWord = text.toString();
            System.out.println("keyWord >>>:" + keyWord);
            cancel_Txt.setText("提交");
        } else {
            cancel_Txt.setText("取消");
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
