package jc.cici.android.atom.ui.tiku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import jc.cici.android.atom.adapter.MoreFunctionAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.MoreBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * 更多功能
 * Created by atom on 2017/12/21.
 */

public class MoreActivity extends BaseActivity {

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
    // 列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // item布局
    @BindView(R.id.item_layout)
    RelativeLayout item_layout;
    private ArrayList<MoreBean> mDatas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MoreFunctionAdapter adapter;
    private Dialog dialog;
    private SharedPreferences sp;
    // 用户id
    private int userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more;
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
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 添加数据
        if (jc.cici.android.atom.utils.NetUtil.isMobileConnected(baseActivity)) {
            initData();
        } else {
            dialog.dismiss();
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {

        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        dialog.dismiss();
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
//        Observable<CommonBean<TikuHomeBean>> observable = httpPostService.getExamListInfo(body);
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CommonBean<TikuHomeBean>>() {
//                    @Override
//                    public void onCompleted() {
//                        if (null != dialog && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (null != dialog && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CommonBean<TikuHomeBean> tikuHomeBeanCommonBean) {
//
//                        if (100 == tikuHomeBeanCommonBean.getCode()) {
//
//                        } else {
//                            Toast.makeText(baseActivity, tikuHomeBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private RequestBody commonPramas() {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
//            // 0 表示全部
//            obj.put("projectId", 0);
//            // 添加全部
//            obj.put("addAllOption", 1);
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

    private void showProcessDialog(Activity mContext, int layout) {
        dialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // 注意此处要放在show之后 否则会报异常
        dialog.setContentView(layout);
    }

    private void initView() {

        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.GONE);
        title_txt.setText("更多功能");
        noteMore_Btn.setBackgroundResource(R.drawable.icon_note_search);
        noteMore_Btn.setVisibility(View.GONE);
        search_Btn.setBackgroundResource(R.drawable.icon_note_more);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MoreFunctionAdapter(baseActivity, mDatas);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
    }

    @OnClick({R.id.back_layout, R.id.item_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.item_layout: // item布局监听
                Uri uri = Uri.parse("http://app.gfedu.cn/GfeduKY/GfeduKaoyan_V1.0.0.apk");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
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
