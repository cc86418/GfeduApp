package jc.cici.android.atom.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.gfedu.gfeduapp.MainActivity;
import cn.jun.menory.service.VideoDownloadManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jc.cici.android.R;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.base.BaseFragment;
import jc.cici.android.atom.bean.BindedInfo;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.User;
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
 * 有金程张洪刚的第三方登录
 * Created by atom on 2017/4/14.
 */

public class HaveCardFragment extends BaseFragment {

    private Unbinder unbinder;
    private Activity mCtx;
    private BaseActivity baseActivity;
    // 用户名输入布局
    @BindView(R.id.haveCard_layout)
    RelativeLayout haveCard_layout;
    // 用户名输入框
    @BindView(R.id.haveCard_txt)
    EditText haveCard_txt;
    // 用户名错误图标
    @BindView(R.id.hint_haveCardTag_img)
    ImageView hint_haveCardTag_img;
    // 用户名错误提示
    @BindView(R.id.errorUser_haveCardTap_txt)
    TextView errorUser_haveCardTap_txt;
    // 密码输入布局
    @BindView(R.id.psd_haveCard_layout)
    RelativeLayout psd_haveCard_layout;
    // 密码输入框
    @BindView(R.id.passWord_haveCard_txt)
    EditText passWord_haveCard_txt;
    // 密码错误图标
    @BindView(R.id.hint_haveCardPWDTag_img)
    CheckBox hint_haveCardPWDTag_img;
    // 密码错误提示
    @BindView(R.id.errorPsd_haveCardTap_txt)
    TextView errorPsd_haveCardTap_txt;
    // 登录按钮
    @BindView(R.id.haveCardBind_Btn)
    Button haveCardBind_Btn;
    // 提示信息
    @BindView(R.id.noCardTip_Txt)
    TextView noCardTip_Txt;
    // 输入用户名,密码
    private String userName, psd;
    // 登录进度
    private SweetAlertDialog dialog;
    // 对话框
    private Dialog mDialog;
    // handler
    private Handler handler;
    // 保存用户信息对象
    private SharedPreferences userInfoPrueferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this.getActivity();
        baseActivity = (BaseActivity) mCtx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view, null);
        return view;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        String str = OtherLoginActivity.type;
        if ("WEIXIN".equals(str)) {
            str = "WECHAT";
        }
        if ("QQ".equals(str)) {
            noCardTip_Txt.setText("点击绑定后，今后可以直接用QQ账号登录");
        } else if ("SINA".equals(str)) {
            noCardTip_Txt.setText("点击绑定后，今后可以直接用新浪账号登录");
        } else {
            noCardTip_Txt.setText("点击绑定后，今后可以直接用微信账号登录");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_havecard;
    }

    /**
     * 按钮监听
     *
     * @param v
     */
    @OnClick({R.id.haveCardBind_Btn})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.haveCardBind_Btn: // 登录按钮监听
                if (baseActivity.verifyClickTime()) { // 添加防止快速点击
                    userName = haveCard_txt.getText().toString().trim();
                    psd = passWord_haveCard_txt.getText().toString().trim();
                    if (null != userName && userName.length() > 0) { // 用户名不空情况
                        if (null != psd && psd.length() > 0) {
                            if (NetUtil.isMobileConnected(mCtx)) { // 判断当前有网络连接
                                getCardLoginFromHttp(userName, psd);
                            } else { // 无网络连接情况
                                setErrorInfo(errorPsd_haveCardTap_txt, "登录失败，网络连接已断开，请检查网络");
                            }
                        } else {
                            setErrorInfo(errorPsd_haveCardTap_txt, "密码不能为空");
                        }

                    } else { // 用户名为空
                        setErrorInfo(errorUser_haveCardTap_txt, "手机号不能为空");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求第三方已有金程账号登录信息
     *
     * @param userName
     * @param psd
     */
    private void getCardLoginFromHttp(String userName, String psd) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
        dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        String str = String.valueOf(OtherLoginActivity.type);
        if ("WEIXIN".equals(str)) {
            str = "WECHAT";
        }
        String strUrl = "{'UserName':'" + userName
                + "','UserPwd':'" + psd
                + "','OpenID':'" + OtherLoginActivity.uid
                + "','ThirdType':'" + str + "'}";
        // 添加MD5加密字符串
        String mD5Result = ToolUtils.getMD5Str(strUrl + Global.MD5_KEY);
        Observable<BindedInfo> observable = httpPostService.getBindedInfo(strUrl, mD5Result);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BindedInfo>() {
                            @Override
                            public void onCompleted() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismissWithAnimation();
                                    Toast.makeText(mCtx, "绑定登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(final BindedInfo bindedInfo) {

                                // 获取请求成功后返回字符串
                                String statusStr = bindedInfo.getResultState();
                                int status = Integer.parseInt(statusStr);
                                switch (status) {
                                    case 1: // 直接登录
                                        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
                                        // 获取上次登录保存的用户id
                                        int lastLoginSID = sharedPreferences.getInt("S_ID", 0);
                                        // 获取当前设备绑定情况
                                        final String bindStatus = bindedInfo.getDeviceId();
                                        if (0 == lastLoginSID) { // 无绑定设备信息
                                            if ("".equals(bindStatus)) { // 无绑定记录
                                                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                                                mDialog.setContentView(R.layout.login_bind_dialog);
                                                TextView tipContent_txt = (TextView) mDialog.findViewById(R.id.tipContent_txt);
                                                Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
                                                Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
                                                tipContent_txt.setText("该设备还未绑定,请绑定该设备");
                                                ensureBtn.setText("绑定");
                                                mDialog.show();
                                                // 取消按钮
                                                cancelBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mDialog.dismiss();
                                                    }
                                                });
                                                // 绑定按钮
                                                ensureBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // 提示绑定设备
                                                        bindDeiver(bindedInfo.getResultData().getS_PassWord(), bindedInfo.getResultData().getS_Name(), mDialog, bindedInfo.getResultData());
                                                    }
                                                });
                                            } else if (bindStatus.equals(ToolUtils.getUUID(baseActivity))) { // 已被当前设备绑定
                                                // 保存用户信息
                                                saveUserSharePreferences(bindedInfo.getResultData());
                                                Intent intent = new Intent(baseActivity, MainActivity.class);
                                                MainActivity.MainAc.finish();
                                                startActivity(intent);
                                                mCtx.finish();
                                            } else if (!bindStatus.equals(ToolUtils.getUUID(baseActivity))) { // 当前账号已在其他设备上登录
                                                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                                                mDialog.setContentView(R.layout.login_mulbind_dialog);
                                                RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
                                                tipBtn_layout.setVisibility(View.GONE);
                                                RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
                                                mulTipBtn_layout.setVisibility(View.VISIBLE);
                                                TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
                                                Button mulEnsureBtn = (Button) mDialog.findViewById(R.id.mulEnsureBtn);
                                                tipMoreContent_txt.setText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!");
                                                mulEnsureBtn.setText("确定");
                                                mDialog.show();
                                                mulEnsureBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mDialog.dismiss();
                                                    }
                                                });
                                            }
//                                            mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
//                                            mDialog.setContentView(R.layout.login_bind_dialog);
//                                            TextView tipContent_txt = (TextView) mDialog.findViewById(R.id.tipContent_txt);
//                                            Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
//                                            Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
//                                            tipContent_txt.setText("该设备还未绑定,请绑定该设备");
//                                            ensureBtn.setText("绑定");
//                                            mDialog.show();
//                                            // 取消按钮
//                                            cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    mDialog.dismiss();
//                                                }
//                                            });
//                                            // 绑定按钮
//                                            ensureBtn.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    // 提示绑定设备
//                                                    bindDeiver(bindedInfo.getResultData().getS_PassWord(), bindedInfo.getResultData().getS_Name(), mDialog, bindedInfo.getResultData());
//                                                }
//                                            });

                                        } else if (lastLoginSID == bindedInfo.getResultData().getS_ID()) { // 如果有登录信息，则对比两次登录id
                                            if (0 == bindedInfo.getResultData().getBindStatus()) { // 未被任何设备绑定
                                                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                                                mDialog.setContentView(R.layout.login_bind_dialog);
                                                TextView tipContent_txt = (TextView) mDialog.findViewById(R.id.tipContent_txt);
                                                Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
                                                Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
                                                tipContent_txt.setText("该设备还未绑定,请绑定该设备");
                                                ensureBtn.setText("绑定");
                                                mDialog.show();
                                                // 取消按钮
                                                cancelBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mDialog.dismiss();
                                                    }
                                                });
                                                // 绑定按钮
                                                ensureBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // 提示绑定设备
                                                        bindDeiver(bindedInfo.getResultData().getS_PassWord(), bindedInfo.getResultData().getS_Name(), mDialog, bindedInfo.getResultData());
                                                    }
                                                });
                                            } else if (1 == bindedInfo.getResultData().getBindStatus()) { // 已被当前设备绑定
                                                // 保存用户信息
                                                saveUserSharePreferences(bindedInfo.getResultData());
                                                // TODO 登录成功跳转
                                                Intent intent = new Intent(baseActivity, MainActivity.class);
                                                MainActivity.MainAc.finish();
                                                startActivity(intent);
                                                baseActivity.finish();

                                            } else if (2 == status) {
                                                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                                                mDialog.setContentView(R.layout.login_mulbind_dialog);
                                                RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
                                                tipBtn_layout.setVisibility(View.GONE);
                                                RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
                                                mulTipBtn_layout.setVisibility(View.VISIBLE);
                                                TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
                                                Button mulEnsureBtn = (Button) mDialog.findViewById(R.id.mulEnsureBtn);
                                                tipMoreContent_txt.setText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!");
                                                mulEnsureBtn.setText("确定");
                                                mDialog.show();
                                                mulEnsureBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mDialog.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                                            mDialog.setContentView(R.layout.login_mulbind_dialog);
                                            RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
                                            tipBtn_layout.setVisibility(View.VISIBLE);
                                            RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
                                            mulTipBtn_layout.setVisibility(View.GONE);
                                            TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
                                            Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
                                            Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
                                            tipMoreContent_txt.setText("登录账号和上次不一致,确定登录将清空之前缓存!");
                                            ensureBtn.setText("确定");
                                            mDialog.show();
                                            // 取消按钮
                                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDialog.dismiss();
                                                }
                                            });
                                            // 绑定按钮
                                            ensureBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // 清除上次登录信息
                                                    clearLastLoginInfo(mDialog);
                                                    // 绑定设备信息
                                                    setBindDiever(bindedInfo.getResultData().getBindStatus(), bindedInfo.getResultData());
                                                }
                                            });

                                        }
                                        break;
                                    case 0: // 绑定账号
                                        Toast.makeText(mCtx, "您还没绑定金程网校账号", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }

                            public void onStart() {
                                super.onStart();
                                if (dialog != null && !dialog.isShowing()) {
                                    dialog.show();
                                }
                            }
                        }
                );
    }


    private void setBindDiever(int bindStatus, final User user) {

        switch (bindStatus) {
            case 0: // 未被任何设备绑定
                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                mDialog.setContentView(R.layout.login_bind_dialog);
                TextView tipContent_txt = (TextView) mDialog.findViewById(R.id.tipContent_txt);
                Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
                Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
                tipContent_txt.setText("该设备还未绑定,请绑定该设备");
                ensureBtn.setText("绑定");
                mDialog.show();
                // 取消按钮
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                // 绑定按钮
                ensureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 提示绑定设备
                        bindDeiver(user.getS_PassWord(), user.getS_Name(), mDialog, user);
                    }
                });
                break;
            case 1: // 已被当前设备绑定
                Toast.makeText(mCtx, "登录成功", Toast.LENGTH_SHORT).show();
                // 保存用户信息
                saveUserSharePreferences(user);
                // TODO 登录成功跳转处理
                Intent intent = new Intent(baseActivity, MainActivity.class);
                MainActivity.MainAc.finish();
                startActivity(intent);
                baseActivity.finish();
                break;
            case 2: // 已被其他设备绑定
                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
                mDialog.setContentView(R.layout.login_mulbind_dialog);
                RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
                tipBtn_layout.setVisibility(View.GONE);
                RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
                mulTipBtn_layout.setVisibility(View.VISIBLE);
                TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
                Button mulEnsureBtn = (Button) mDialog.findViewById(R.id.mulEnsureBtn);
                tipMoreContent_txt.setText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!");
                mulEnsureBtn.setText("确定");
                mDialog.show();
                mulEnsureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 清除上次登录保存信息
     */
    private void clearLastLoginInfo(Dialog dialog) {

        dialog.dismiss();
        // 清除保存本地用户信息
        userInfoPrueferences = mCtx.getSharedPreferences(Global.LOGIN_FLAG,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor userInfo_editor = userInfoPrueferences.edit();
        userInfo_editor.clear();
        userInfo_editor.commit();
        // 清除缓存视频信息
        DelectPolyvDownload();
        // 清除本地下载数据
        DelectSQLDateBase();
    }

    /**
     * 绑定设备信息
     *
     * @param s_passWord
     * @param s_name
     * @param sweetAlertDialog
     */
    private void bindDeiver(String s_passWord, String s_name, final Dialog sweetAlertDialog, final User user) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("oauth", ToolUtils.getMD5Str(s_name + s_passWord + "android!%@%$@#$"));
            obj.put("appname", commParam.getAppname());
            obj.put("loginname", s_name);
            obj.put("password", s_passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<DeiverInfo> observable = httpPostService.getDeiverInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeiverInfo>() {
                    @Override
                    public void onCompleted() {
                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismiss();
                            Toast.makeText(mCtx, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(DeiverInfo deiverInfo) {
                        // 保存本地信息
                        SaveSharePreferences(user);
                        // TODO 跳转处理
                        Intent intent = new Intent(baseActivity, MainActivity.class);
                        MainActivity.MainAc.finish();
                        startActivity(intent);
                        baseActivity.finish();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (sweetAlertDialog != null && !sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.show();
                        }
                    }
                });
    }

    /**
     * 保存绑定后用户信息
     *
     * @param user
     */
    private void SaveSharePreferences(User user) {
        // 获取最近登录preferences
        SharedPreferences lastSharePre = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", user.getS_ID());
        editor.commit();

        // 存储登录信息
        saveUserInfo(user);

        // 绑定UUID
        SharedPreferences uuidPre = mCtx.getSharedPreferences(Global.UUID_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor uuidEditor = uuidPre.edit();
        uuidEditor.putString("DeviceId", ToolUtils.getUUID(mCtx));
        uuidEditor.commit();
    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    private void saveUserInfo(User user) {
        // 存储登录信息
        SharedPreferences loginPre = mCtx.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginPre.edit();
        loginEditor.putInt("S_ID", user.getS_ID());
        loginEditor.putString("S_Name", user.getS_Name());
        loginEditor.putString("S_PassWord",psd);
        loginEditor.putString("S_NickName", user.getS_NickName());
        loginEditor.putString("S_RealName", user.getS_RealName());
        loginEditor.putString("S_Sex", user.getS_Sex() + "");
        loginEditor.putString("S_Email", user.getS_Email());
        loginEditor.putString("S_Telephone", user.getS_Telephone());
        loginEditor.putString("S_Address", user.getS_Address());
        loginEditor.putString("S_MailCode", user.getS_MailCode()
                + "");
        loginEditor.putString("S_RegDate", user.getS_RegDate());
        loginEditor.putString("S_LastLogin", user.getS_LastLogin());
        loginEditor.putString("S_Type", user.getS_Type() + "");
        loginEditor.putString("S_State", user.getS_State() + "");
        loginEditor.putString("S_Money", user.getS_Money() + "");
        if (!"".equals(user.getS_Head())) {
            loginEditor.putString("S_Head", user.getS_Head());
        } else if ("null".equals(user.getS_Head())) {
            loginEditor.putString("S_Head", "");
        } else {
            loginEditor.putString("S_Head", "");
        }
        loginEditor.commit();

    }

    /**
     * 保存当前登录信息并保存登录成功后用户信息
     *
     * @param user
     */
    private void saveUserSharePreferences(User user) {

        // 获取最近登录preferences
        SharedPreferences lastSharePre = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", user.getS_ID());
        editor.commit();
        // 保存用户信息
        saveUserInfo(user);

    }


    /**
     * 输出错误信息
     *
     * @param tv
     * @param str
     */
    private void setErrorInfo(TextView tv, String str) {
        if (null != tv) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 输入错误提示显示
     *
     * @param text
     */
    @OnTextChanged({R.id.haveCard_txt, R.id.passWord_haveCard_txt})
    void afterTextChanged(CharSequence text) {
        if (errorUser_haveCardTap_txt.getVisibility() == View.VISIBLE) {
            errorUser_haveCardTap_txt.setVisibility(View.GONE);
        } else if (errorPsd_haveCardTap_txt.getVisibility() == View.VISIBLE) {
            errorPsd_haveCardTap_txt.setVisibility(View.GONE);
        }
        userName = getEditTextInput();
        if (ToolUtils.isEmail(userName) && "1".equals(userName)) {
            if (!ToolUtils.isMobileNo(haveCard_txt.getText().toString().trim())) {
                setErrorInfo(errorUser_haveCardTap_txt, "非法手机号");
                haveCard_layout.setBackgroundResource(R.drawable.ic_error_box_input);
                hint_haveCardTag_img.setBackgroundResource(R.drawable.icon_input_error);
                hint_haveCardTag_img.setVisibility(View.VISIBLE);
            } else {
                haveCard_layout.setBackgroundResource(R.drawable.ic_login_box_input);
                hint_haveCardTag_img.setBackgroundResource(R.drawable.icon_input_correct);
                hint_haveCardTag_img.setVisibility(View.VISIBLE);
            }
        } else {
            haveCard_layout.setBackgroundResource(R.drawable.ic_login_box_input);
            hint_haveCardTag_img.setVisibility(View.GONE);
        }
    }

    @OnCheckedChanged({R.id.hint_haveCardPWDTag_img})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) { // 文本可见
            setShowOrHidePwd(passWord_haveCard_txt, true);
            hint_haveCardPWDTag_img.setBackgroundResource(R.drawable.icon_show);
        } else {
            setShowOrHidePwd(passWord_haveCard_txt, false);
            hint_haveCardPWDTag_img.setBackgroundResource(R.drawable.icon_hide);
        }
    }

    /**
     * 密码显示与隐藏
     *
     * @param tv
     * @param flag
     */
    private void setShowOrHidePwd(TextView tv, boolean flag) {
        if (null != tv) { // 文本可见
            if (flag) { // 密码可见
                tv.setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
            } else { // 密码不可见
                tv.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
            }
            // 切换后将EditText光标置于末尾
            CharSequence charSequence = tv.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText,
                        charSequence.length());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    private String getEditTextInput() {
        String firstChar = haveCard_txt.getText().toString().trim();
        if (null != firstChar && firstChar.length() > 0) {
            char a = firstChar.charAt(0);
            StringBuffer buffer = new StringBuffer();
            return buffer.append(a).toString();
        }
        return null;
    }

    //清除本地已下载文件
    private void DelectPolyvDownload() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {// 获取sd卡
            String path = Environment.getExternalStorageDirectory()
                    + "/".concat("polyvdownload");
            File file = new File(path);
            if (file.exists()) {// 文件路径存在
                DelectFile(file);
            }
        }
    }

    private void DelectFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                DelectFile(f);
            }
            file.delete();
        }

    }

    //清空或者删除数据库表
    private void DelectSQLDateBase() {
        VideoDownloadManager.getInstance().deleteClassTable();
        VideoDownloadManager.getInstance().deleteStageTable();
        VideoDownloadManager.getInstance().deleteDownLoadTable();

    }
}
