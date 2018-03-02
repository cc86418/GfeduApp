package jc.cici.android.atom.ui.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.LiveListAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LiveProduct;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyFootRecyclerView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 直播搜索Activity
 * Created by atom on 2017/11/10.
 */

public class LiveSearchActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 父类布局
    @BindView(R.id.parentLayout)
    RelativeLayout parentLayout;
    // 搜索框
    @BindView(R.id.sc_layout)
    RelativeLayout sc_layout;
    // 查询view
    @BindView(R.id.editSearch_Txt)
    EditText editSearch_Txt;
    // 取消文字
    @BindView(R.id.cancel_Txt)
    TextView cancel_Txt;
    @BindView(R.id.recyclerView)
    EmptyFootRecyclerView recyclerView;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    private LinearLayoutManager linearLayoutManager;
    private LiveListAdapter adapter;
    private ArrayList<LiveProduct.Live> data = new ArrayList<>();
    private Dialog dialog;
    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id集合
    private String projectIdList = "0";
    // 1：单直播 2：系列直播 10：最新直播 20：热门直播 40 前期回顾
    private int searchType;
    // 当前页码
    private int page = 1;
    // 搜索关键字
    private String keyWord;
    // 排序类型：1：时间 2：人气
    private int sort = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_search;
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
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {
        parentLayout.setBackgroundColor(Color.parseColor("#f5f5f5"));
        editSearch_Txt.setHint("请输入查询关键字");
        sc_layout.setBackgroundResource(R.drawable.bg_search_edit);
        // 获取用户id
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);

        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LiveListAdapter(baseActivity, data);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);
        emptyView.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.cancel_Txt})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancel_Txt: // 取消按钮
                if ("提交".equals(cancel_Txt.getText().toString())) { // 提交按钮
                    if (NetUtil.isMobileConnected(this)) {
                        if (null != data && data.size() > 0) {
                            data.clear();
                        }
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

    /**
     * 初始化数据
     */
    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commParam(page);
        Observable<CommonBean<LiveProduct>> observable = httpPostService.getLiveListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveProduct>>() {
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
                        }
                        Toast.makeText(baseActivity, "网络加载异常，请下拉刷新", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<LiveProduct> liveListBeanLiveProduct) {
                        if (100 == liveListBeanLiveProduct.getCode()) {
                            ArrayList<LiveProduct.Live> list = liveListBeanLiveProduct.getBody().getLiveList();
                            if (null != list && list.size() > 0) {
                                data.addAll(list);
                            }
                            adapter.notifyDataSetChanged();
                            // 点击事件
                            adapter.setClickListener(new LiveListAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    // 判断是系列直播还是正常直播
                                    int courseType = data.get(position).getClass_Form();
                                    if (1 == courseType) { // 系列直播
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("Class_PKID", data.get(position).getClass_PKID());
                                        baseActivity.openActivity(LiveClassXiLieActivity.class, bundle);
                                    } else if (2 == courseType) { // 正常直播
                                        Bundle bundle = new Bundle();
//                                        bundle.putInt("Product_PKID", data.get(position).getClass_PKID());
//                                        baseActivity.openActivity(OnlineCourseDetailsAloneActivityTwo.class, bundle);
                                        bundle.putInt("Class_PKID", data.get(position).getClass_PKID());
                                        baseActivity.openActivity(LiveClassActivity.class, bundle);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(baseActivity, liveListBeanLiveProduct.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @OnTextChanged(R.id.editSearch_Txt)
    void afterTextChanged(CharSequence text) {
        if (text.length() > 0) {
            keyWord = text.toString();
            System.out.println("keyWord >>>:" + keyWord);
            cancel_Txt.setText("提交");
        } else {
            page = 1;
            cancel_Txt.setText("取消");
        }
    }

    /**
     * 公共参数
     *
     * @param pager
     * @return
     */
    private RequestBody commParam(int pager) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("projectIdList", projectIdList);
            obj.put("sort", sort);
            obj.put("searchType", searchType);
            obj.put("isPlayBack", 0);
            obj.put("pageIndex", pager);
            obj.put("keywords", keyWord);
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
