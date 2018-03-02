package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.BillBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 发票内容信息Activity
 * Created by atom on 2017/8/24.
 */

public class BillContentActivity extends BaseActivity {

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
    // 注册按钮屏蔽
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 右侧搜索按钮
    @BindView(R.id.moreBtn)
    Button moreBtn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 发票抬头
    @BindView(R.id.personName_txt)
    TextView personName_txt;
    // 发票类型
    @BindView(R.id.billTypeName_txt)
    TextView billTypeName_txt;
    //  个人发票内容布局
    @BindView(R.id.billContent_layout)
    RelativeLayout billContent_layout;
    // 个人发票内容
    @BindView(R.id.billContentName_txt)
    TextView billContentName_txt;
    // 公司发票布局
    @BindView(R.id.company_layout)
    RelativeLayout company_layout;
    // 纳税人识别号
    @BindView(R.id.taxpayerName_txt)
    TextView taxpayerName_txt;
    // 公司地址
    @BindView(R.id.addressName_txt)
    TextView addressName_txt;
    // 公司电话
    @BindView(R.id.phoneName_txt)
    TextView phoneName_txt;
    // 开户银行
    @BindView(R.id.bankName_txt)
    TextView bankName_txt;
    // 开户银行账号
    @BindView(R.id.bankAccountName_txt)
    TextView bankAccountName_txt;
    // 备注
    @BindView(R.id.remarksName_txt)
    TextView remarksName_txt;
    private Dialog dialog;
    // 订单id
    private int orderId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_billcontent;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        AppManager.getInstance().addActivity(this);
        // 获取订单id
        orderId = getIntent().getIntExtra("orderId", 0);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 填充内容
        if (NetUtil.isMobileConnected(baseActivity)) {
            initData();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加内容
     */
    private void initData() {

        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            obj.put("userId", commParam.getUserId());
            // 订单id
            obj.put("order_ID", orderId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<BillBean>> observable = httpPostService.getOrderInvoiceInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<BillBean>>() {
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
                        Toast.makeText(baseActivity, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<BillBean> billBeanCommonBean) {
                        if (100 == billBeanCommonBean.getCode()) {
                            // 获取发票状态
                            int state = billBeanCommonBean.getBody().getOrder_InvoiceState();
                            switch (state) {
                                case 0: // 未开票
                                    register_txt.setText("未开票");
                                    break;
                                case 1: // 已开票
                                    register_txt.setText("已开票");
                                    break;
                            }
                            // 获取发票抬头
                            personName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getInvoice_Title()));
                            // 发票类型
                            int type = billBeanCommonBean.getBody().getInvoice_Kind();
                            switch (type) {
                                case 1: // 专票
                                    billTypeName_txt.setText("增值税专用发票");
                                    break;
                                case 2: // 普通发票
                                    billTypeName_txt.setText("增值税普通发票");
                                    break;
                            }
                            // 纳税人识别号
                            taxpayerName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getTaxpayerID()));
                            // 公司地址
                            addressName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getInvoice_Address()));
                            // 公司电话
                            phoneName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getInvoice_Phone()));
                            // 开户银行
                            bankName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getAccountBank()));
                            // 开户银行账号
                            bankAccountName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getBankAccount()));
                            // 备注内容
                            remarksName_txt.setText(ToolUtils.strReplaceAll(billBeanCommonBean.getBody().getInvoice_ApplyRemark()));

                        } else {
                            Toast.makeText(baseActivity, billBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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


    @OnClick({R.id.back_layout})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            default:
                break;
        }
    }

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("发票信息");
        register_txt.setText("未开票");
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
