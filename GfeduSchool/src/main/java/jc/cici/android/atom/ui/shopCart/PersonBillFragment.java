package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.BaseFragment;
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
 * 个人发票fragment
 * Created by atom on 2017/8/23.
 */

public class PersonBillFragment extends BaseFragment {

    private Unbinder unbinder;
    private Activity mAc;
    // 个人名称
    @BindView(R.id.personName_txt)
    EditText personName_txt;
    //培训内容
    @BindView(R.id.billContentName_txt)
    EditText billContentName_txt;
    // 确认按钮
    @BindView(R.id.sureBtn)
    Button sureBtn;
    // 个人名称字符串
    private String personNameStr;
    // 培训内容字符串
    private String billContentNameStr;
    // 订单id
    private int orderId;
    // 列表来源
    private int mFrom;
    private Dialog dialog;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_personbill;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAc = getActivity();
        Bundle bundle = getArguments();
        orderId = bundle.getInt("orderId", 0);
        // 获取进入来源
        mFrom = bundle.getInt("mFrom",0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnTextChanged({R.id.personName_txt, R.id.billContentName_txt})
    void afterTextChanged(CharSequence text) {
        personNameStr = personName_txt.getText().toString().trim();
        billContentNameStr = billContentName_txt.getText().toString().trim();
    }

    @OnClick({R.id.sureBtn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.sureBtn: // 确认按钮
                // 获取个人名称
                personNameStr = personName_txt.getText().toString().trim();
                // 获取培训内容
                billContentNameStr = billContentName_txt.getText().toString().trim();
                if (null != personNameStr && !"".equals(personNameStr)) {
                    if (null != billContentNameStr && !"".equals(billContentNameStr)) {
                        if (NetUtil.isMobileConnected(mAc)) {
                            // 提交开票信息
                            submitBillInfo(personNameStr, billContentNameStr);
                        } else {
                            Toast.makeText(mAc, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mAc, "发票内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mAc, "个人名称不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 提交发票信息
     *
     * @param personNameStr
     * @param billContentNameStr
     */
    private void submitBillInfo(String personNameStr, String billContentNameStr) {

        // 加载数据
        showProcessDialog(mAc,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mAc);
        try {
            obj.put("userId", commParam.getUserId());
            // 订单id
            obj.put("order_ID", orderId);
            // 发票名称类型(0:个人，1:机构)
            obj.put("invoice_Type", 0);
            // 发票类型(1.专票   2.普票)
            obj.put("invoice_Kind", 2);
            // 发票抬头
            obj.put("invoice_Title", personNameStr);
            // 发票内容
            obj.put("invoice_Item", billContentNameStr);
            // 纳税人识别码
            obj.put("taxpayerID", "");
            // 公司地址
            obj.put("invoice_Address", "");
            // 公司电话
            obj.put("invoice_Phone", "");
            // 开户行名称
            obj.put("accountBank", "");
            // 开户行
            obj.put("BankAccount", "");
            // 备注
            obj.put("invoice_ApplyRemark", "");
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<BillBean>> observable = httpPostService.addOrderInvoiceInfo(body);
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
                        Toast.makeText(mAc, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<BillBean> billBeanCommonBean) {
                        if (100 == billBeanCommonBean.getCode()) {
                            Toast.makeText(mAc, "发票申请提交成功", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent();
                            switch (mFrom){
                                case 0: // 来源全部
                                    it.setAction("com.allOrder.refresh");
                                    mAc.sendBroadcast(it);
                                    break;
                                case 1: // 来源已支付
                                    it.setAction("com.paidOrder.refresh");
                                    mAc.sendBroadcast(it);
                                    break;
                            }
                            mAc.finish();
                        } else {
                            Toast.makeText(mAc, billBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
