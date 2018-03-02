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
import android.widget.CheckBox;
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
 * 公司发票Activity
 * Created by atom on 2017/8/23.
 */

public class CompanyBillFragment extends BaseFragment {
    private Unbinder unbinder;
    private Activity mAc;
    // 公司名称
    @BindView(R.id.companyName_txt)
    EditText companyName_txt;
    // 增值税普通发票
    @BindView(R.id.billNormalTypeName_txt)
    CheckBox billNormalTypeName_txt;
    // 增值税专用发票
    @BindView(R.id.billTypeName_txt)
    CheckBox billTypeName_txt;
    // 纳税人识别号
    @BindView(R.id.taxpayerName_txt)
    EditText taxpayerName_txt;
    // 公司地址
    @BindView(R.id.addressName_txt)
    EditText addressName_txt;
    // 电话
    @BindView(R.id.phoneName_txt)
    EditText phoneName_txt;
    // 开户银行
    @BindView(R.id.bankName_txt)
    EditText bankName_txt;
    // 银行账户
    @BindView(R.id.bankAccountName_txt)
    EditText bankAccountName_txt;
    // 备注
    @BindView(R.id.remarksName_txt)
    EditText remarksName_txt;
    // 确认按钮
    @BindView(R.id.sureBtn)
    Button sureBtn;
    // 公司名称字符串
    private String companyNameStr;
    // 纳税人识别码字符串
    private String taxpayerNameStr;
    // 公司地址字符串
    private String addressNameStr;
    // 公司电话字符串
    private String phoneNameStr;
    // 开户银行字符串
    private String bankNameStr;
    // 开户银行账户字符串
    private String bankAccountNameStr;
    // 备注字符串
    private String remarksNameStr;
    // 订单id
    private int orderId;
    // 获取来源
    private int mFrom;
    private Dialog dialog;
    private int billType;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_companybill;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAc = getActivity();
        Bundle bundle = getArguments();
        orderId = bundle.getInt("orderId", 0);
        // 获取进入来源
        mFrom = bundle.getInt("mFrom", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        initView(view, null);
        return view;
    }

    @OnTextChanged({R.id.companyName_txt, R.id.taxpayerName_txt,
            R.id.addressName_txt, R.id.phoneName_txt, R.id.bankName_txt, R.id.bankAccountName_txt, R.id.remarksName_txt})
    void afterTextChanged(CharSequence text) {
        companyNameStr = companyName_txt.getText().toString().trim();
        taxpayerNameStr = taxpayerName_txt.getText().toString().trim();
        addressNameStr = addressName_txt.getText().toString().trim();
        phoneNameStr = phoneName_txt.getText().toString().trim();
        bankNameStr = bankName_txt.getText().toString().trim();
        bankAccountNameStr = bankAccountName_txt.getText().toString().trim();
        remarksNameStr = remarksName_txt.getText().toString().trim();
    }


    @OnClick({R.id.sureBtn, R.id.billNormalTypeName_txt, R.id.billTypeName_txt})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.billNormalTypeName_txt: // 普通发票情况
                if (billNormalTypeName_txt.isChecked()) {
                    billType = 2;
                    billTypeName_txt.setChecked(false);
                } else {
                    billTypeName_txt.setChecked(true);
                }
                break;
            case R.id.billTypeName_txt: // 专票情况
                if (billTypeName_txt.isChecked()) {
                    billType = 1;
                    billNormalTypeName_txt.setChecked(false);
                } else {
                    billNormalTypeName_txt.setChecked(true);
                }
                break;
            case R.id.sureBtn: // 确认按钮
                // 公司名称
                companyNameStr = companyName_txt.getText().toString().trim();
                // 纳税人识别号
                taxpayerNameStr = taxpayerName_txt.getText().toString().trim();
                // 公司地址
                addressNameStr = addressName_txt.getText().toString().trim();
                // 公司电话
                phoneNameStr = phoneName_txt.getText().toString().trim();
                // 开户银行
                bankNameStr = bankName_txt.getText().toString().trim();
                // 开户账号
                bankAccountNameStr = bankAccountName_txt.getText().toString().trim();
                // 备注
                remarksNameStr = remarksName_txt.getText().toString().trim();

                if (null != companyNameStr && !"".equals(companyNameStr)) {
                    if (null != taxpayerNameStr && !"".equals(taxpayerNameStr)) {
                        if (null != addressNameStr && !"".equals(addressNameStr)) {
                            if (null != phoneNameStr && !"".equals(phoneNameStr)) {
                                if (null != bankNameStr && !"".equals(bankNameStr)) {
                                    if (null != bankAccountNameStr && !"".equals(bankAccountNameStr)) {
                                        if (NetUtil.isMobileConnected(mAc)) {
                                            // 提交开票信息
                                            submitBillInfo(companyNameStr, taxpayerNameStr, addressNameStr, phoneNameStr, bankNameStr, bankAccountNameStr, remarksNameStr);
                                        } else {
                                            Toast.makeText(mAc, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mAc, "公司开户银行账号不能为空", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mAc, "公司开户银行不能为空", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mAc, "公司联系方式不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mAc, "公司地址不能为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mAc, "纳税人识别号不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mAc, "公司名称不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 提交发票信息
     *
     * @param companyNameStr
     * @param taxpayerNameStr
     * @param addressNameStr
     * @param phoneNameStr
     * @param bankNameStr
     * @param bankAccountNameStr
     * @param remarksNameStr
     */
    private void submitBillInfo(String companyNameStr, String taxpayerNameStr,
                                String addressNameStr, String phoneNameStr, String bankNameStr,
                                String bankAccountNameStr, String remarksNameStr) {

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
            obj.put("invoice_Type", 1);
            // 发票类型(1.专票   2.普票)
            obj.put("invoice_Kind", billType);
            // 发票抬头
            obj.put("invoice_Title", companyNameStr);
            // 发票内容
            obj.put("invoice_Item", "");
            // 纳税人识别码
            obj.put("taxpayerID", taxpayerNameStr);
            // 公司地址
            obj.put("invoice_Address", addressNameStr);
            // 公司电话
            obj.put("invoice_Phone", phoneNameStr);
            // 开户行名称
            obj.put("accountBank", bankNameStr);
            // 开户行
            obj.put("BankAccount", bankAccountNameStr);
            // 备注
            obj.put("invoice_ApplyRemark", remarksNameStr);
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
                            switch (mFrom) {
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
