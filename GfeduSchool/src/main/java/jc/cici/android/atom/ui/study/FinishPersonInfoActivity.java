package jc.cici.android.atom.ui.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.TestWebViewClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 判断完善信息
 * Created by atom on 2017/7/19.
 */

public class FinishPersonInfoActivity extends BaseActivity {

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
    // 右侧搜索布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    // webView
    @BindView(R.id.webView)
    WebView webView;
    // 班级id
    private int classId;
    // 标题内容
    private String strTitle;
    private CommParam commParam;
    private Dialog dialog;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 加载完成
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1: // 提交成功
                    // 初始数据
                    if (NetUtil.isMobileConnected(baseActivity)) {
                        // 加载数据
                        showProcessDialog(baseActivity,
                                R.layout.loading_process_dialog_color);
                        Intent it = new Intent();
                        it.setAction("com.jc.reload");
                        baseActivity.sendBroadcast(it);
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("contentName", strTitle);
                        bundle.putInt("classId", classId);
                        baseActivity.openActivityAndCloseThis(StudyDetailActivity.class, bundle);
                        if(null !=webView){
                            webView.destroy();
                        }
//                        initData();
                    } else {
                        Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_finishinfo;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        // 班级id
        classId = getIntent().getIntExtra("classId", 0);
        // 获取标题内容
        strTitle = getIntent().getStringExtra("contentName");
        // 添加视图
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        // 加载完善信息内容
        if (NetUtil.isMobileConnected(baseActivity)) {
            String md5 = ToolUtils.getMD5Str(commParam.getUserId() + "android!%@%$@#$");
            String url = "http://mapi.gfedu.cn/html/CompleteUserInfo.html?userid=" + commParam.getUserId() + "&client=" + commParam.getClient() + "&md5=" + md5;
            webView.loadUrl(url);
            webView.setWebViewClient(new TestWebViewClient(
                    baseActivity, handler));
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载显示数据
     */
    private void initData() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();

        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("userId", commParam.getUserId());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.checkUserCompleteInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean>() {
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
                                    Toast.makeText(baseActivity, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                if (100 == commonBean.getCode()) {

                                } else {
                                    Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                );
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

    private void initView() {
        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.GONE);
        title_txt.setText("完善信息");
        share_layout.setVisibility(View.GONE);
        titleName_txt.setText("欢迎参加" + strTitle + "学习");
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDefaultTextEncodingName("utf-8");
//
//        webView.requestFocus();
        commParam = new CommParam(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != webView) {
            webView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.goBack();
                return true;
            } else {
                Toast.makeText(baseActivity, "完善信息才能继续哦，亲", Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        return false;
    }
}
