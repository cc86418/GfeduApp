package jc.cici.android.atom.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

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
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.BindJCInfo;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.User;
import jc.cici.android.atom.bean.UserInfo;
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
 * 金程已注册账户登录(普通登录)
 * Created by atom on 2017/4/10.
 */

public class NormalActivity extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private Context mCtx;
    // 标题
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 注册布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 注册文字
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 用户名输入布局
    @BindView(R.id.userLogin_layout)
    RelativeLayout userLogin_layout;
    // 用户名输入框
    @BindView(R.id.userName_txt)
    EditText userName_txt;
    // 用户名错误图标
    @BindView(R.id.hint_tag_img)
    ImageView hint_tag_img;
    // 用户名错误提示
    @BindView(R.id.errorUser_tap_txt)
    TextView errorUser_tap_txt;
    // 密码输入布局
    @BindView(R.id.psd_layout)
    RelativeLayout psd_layout;
    // 密码输入框
    @BindView(R.id.passWord_txt)
    EditText passWord_txt;
    // 密码错误图标
    @BindView(R.id.hint_PWDTag_img)
    CheckBox hint_PWDTag_img;
    // 密码错误提示
    @BindView(R.id.errorPsd_tap_txt)
    TextView errorPsd_tap_txt;
    // 登录按钮
    @BindView(R.id.login_Btn)
    Button login_Btn;
    // 快速免密登录
    @BindView(R.id.fastLogin_txt)
    TextView fastLogin_txt;
    // 忘记密码
    @BindView(R.id.findPWD_txt)
    TextView findPWD_txt;
    // QQ登录
    @BindView(R.id.QQ_Button)
    ImageButton QQ_Button;
    // 微信登录
    @BindView(R.id.WeChat_Button)
    ImageButton WeChat_Button;
    // 微博登录
    @BindView(R.id.WeiBo_Button)
    ImageButton WeiBo_Button;
    // 输入用户名,密码
    private String userName, psd;
    // 进度对话框
    private SweetAlertDialog dialog;
    // 返回值
    private String code;

    private int sID; // 学员id
    private String sName, //学员用户名
            sRealName, // 学员真实姓名
            sHead; // 学员图像
    /**
     * 本地账户配置
     **/
    private SharedPreferences userInfoPrueferences;
    // 上次登录信息
    private SharedPreferences lastUserInfoPreference;
    private Dialog mDialog;
    // 跳转页面
    private String jumpTo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_normal;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 入栈
        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        // 去标题
        baseActivity.requestNoTitle();
        // 获取传递跳转信息
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            jumpTo = bundle.getString("Details");
        }
        // 添加视图布局
        setContentView(getLayoutId());
        mCtx = this;
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 添加文字
        initView();
        // 获取输入内容
        userName = getEditTextInput();
    }

    /**
     * 视图初始显示
     */
    private void initView() {
        title_txt.setText("账号密码登录");
        share_layout.setVisibility(View.VISIBLE);
        back_layout.setVisibility(View.VISIBLE);

    }


    /**
     * 按钮监听
     *
     * @param v
     */
    @OnClick({R.id.back_layout, R.id.login_Btn, R.id.register_txt, R.id.fastLogin_txt, R.id.findPWD_txt, R.id.QQ_Button, R.id.WeChat_Button, R.id.WeiBo_Button})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                Intent intent = new Intent(NormalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register_txt: // 注册文字监听
                if (baseActivity.verifyClickTime()) { // 添加防止快速点击
                    baseActivity.openActivity(RegisterActivity.class);
                }
                break;
            case R.id.login_Btn: // 登录按钮监听
                if (baseActivity.verifyClickTime()) { // 添加防止快速点击
                    userName = userName_txt.getText().toString().trim();
                    psd = passWord_txt.getText().toString().trim();
                    if (null != userName && userName.length() > 0) { // 用户名不空情况
                        if (null != psd && psd.length() > 0) {
                            if (NetUtil.isMobileConnected(mCtx)) { // 判断当前有网络连接
                                try {
                                    // 网络获取请求数据
                                    getUserInfoForHttpRequest(userName, psd);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else { // 无网络连接情况
                                setErrorInfo(errorPsd_tap_txt, "登录失败，网络连接已断开，请检查网络");
                            }
                        } else {
                            setErrorInfo(errorPsd_tap_txt, "密码不能为空");
                        }

                    } else { // 用户名为空
                        setErrorInfo(errorUser_tap_txt, "手机号不能为空");
                    }
                }
                break;
            case R.id.fastLogin_txt: // 快速免密登录监听
                if (baseActivity.verifyClickTime()) {
                    baseActivity.openActivity(FastLoginActivity.class);
                }
                break;
            case R.id.findPWD_txt: // 忘记密码监听
                if (baseActivity.verifyClickTime()) {
                    baseActivity.openActivity(ResetPwdActivity.class);
                }
                break;
            case R.id.QQ_Button: // QQ账号登录
                if (baseActivity.verifyClickTime()) {
                    doLogin(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.WeChat_Button: // 微信账号登录
                if (baseActivity.verifyClickTime()) {
                    doLogin(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.WeiBo_Button: // 微博账号登录
                if (baseActivity.verifyClickTime()) {
                    doLogin(SHARE_MEDIA.SINA);
                }
                break;
            default:
                break;
        }
    }

    private void doLogin(final SHARE_MEDIA type) {
        UMAuthListener umAuthListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //授权开始的回调
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {

                Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.OLD_BASE_URL);
                dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitle("");
                dialog.setTitleText("");

                HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                String str = String.valueOf(type);
                if ("WEIXIN".equals(str)) {
                    str = "WECHAT";
                }
                String jsonStr = "{'OpenID':'" + data.get("uid")
                        + "','ThirdType':'" + str + "'}";
                // 添加MD5加密字符串
                final String mD5Result = ToolUtils.getMD5Str(jsonStr + Global.MD5_KEY);
                Observable<BindJCInfo> observable = httpPostService.checkBindJCInfo(jsonStr, mD5Result);
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BindJCInfo>() {
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
                                    Toast.makeText(mCtx, "验证失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(final BindJCInfo bindJCInfo) {
                                // 获取请求成功后返回字符串
                                String statusStr = bindJCInfo.getResultState();
                                int status = Integer.parseInt(statusStr);
                                switch (status) {
                                    case 1: // 直接登录
                                        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
                                        // 获取上次登录保存的用户id
                                        int lastLoginSID = sharedPreferences.getInt("S_ID", 0);
                                        // 获取当前设备绑定情况
                                        final String bindStatus = bindJCInfo.getDeviceId();
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
                                                        bindDeiver(bindJCInfo.getResultData().getS_PassWord(), bindJCInfo.getResultData().getS_Name(), mDialog, bindJCInfo.getResultData());
                                                    }
                                                });
                                            } else if (bindStatus.equals(ToolUtils.getUUID(baseActivity))) { // 已被当前设备绑定
                                                // 保存用户信息
                                                saveUserSharePreferences(bindJCInfo.getResultData());
                                                Intent intent = new Intent(baseActivity, MainActivity.class);
                                                MainActivity.MainAc.finish();
                                                startActivity(intent);
                                                finish();
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

                                        } else if (lastLoginSID == bindJCInfo.getResultData().getS_ID()) { // 如果有登录信息，则对比两次登录id
                                            if (0 == bindJCInfo.getResultData().getBindStatus()) { // 未被任何设备绑定
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
                                                        bindDeiver(bindJCInfo.getResultData().getS_PassWord(), bindJCInfo.getResultData().getS_Name(), mDialog, bindJCInfo.getResultData());
                                                    }
                                                });
                                            } else if (1 == bindJCInfo.getResultData().getBindStatus()) { // 已被当前设备绑定
                                                // 保存用户信息
                                                saveUserSharePreferences(bindJCInfo.getResultData());
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
                                                    setBindDiever(bindJCInfo.getResultData().getBindStatus(), bindJCInfo.getResultData());
                                                }
                                            });

                                        }
                                        break;
                                    case 0: // 绑定账号
                                        Toast.makeText(mCtx, "您还没绑定金程网校账号，2秒后自动跳转绑定页面", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Bundle bundle = new Bundle();
                                                // 唯一识别码
                                                bundle.putString("uid", data.get("uid"));
                                                // 传递用户昵称
                                                bundle.putString("name", data.get("name"));
                                                // 传递用户图像
                                                bundle.putString("iconurl", data.get("iconurl"));
                                                bundle.putString("type", String.valueOf(type));
                                                baseActivity.openActivity(OtherLoginActivity.class, bundle);
                                            }
                                        }, 2000);
                                        break;
                                    default:
                                        break;
                                }
//                                String bindStatus = bindJCInfo.getResultState();
//                                if ("1".equals(bindStatus)) { // 直接登录
//                                    SharedPreferences lastLogin_preferences;
//                                    lastLogin_preferences = getSharedPreferences(Global.LAST_LOGIN_FLAG,
//                                            Activity.MODE_PRIVATE);
//                                    int lastSID = lastLogin_preferences.getInt("S_ID", 0);
//                                    int status  = bindJCInfo.getResultData().getS_ID();
//                                    if (lastSID != status) { // 两次登录第三方账户不一致情况
//                                        mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
//                                        mDialog.setContentView(R.layout.login_mulbind_dialog);
//                                        RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
//                                        tipBtn_layout.setVisibility(View.VISIBLE);
//                                        RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
//                                        mulTipBtn_layout.setVisibility(View.GONE);
//                                        TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
//                                        Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
//                                        Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
//                                        tipMoreContent_txt.setText("登录第三方账号和上次不一致,确定登录将清空之前缓存!");
//                                        ensureBtn.setText("确定");
//                                        mDialog.show();
//                                        // 取消按钮
//                                        cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                mDialog.dismiss();
//                                            }
//                                        });
//                                        // 绑定按钮
//                                        ensureBtn.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                // 清除上次登录信息
//                                                clearLastLoginInfo(mDialog);
//                                                if (!"".equals(bindJCInfo.getDeviceId())
//                                                        && null != bindJCInfo.getDeviceId()) { // 已获取设备id情况
//                                                    if (ToolUtils.getUUID(mCtx).equals(bindJCInfo.getDeviceId())) { // 当前设备号和上次登录一致情况
//                                                        // 保存用户信息
//                                                        saveUserInfo(bindJCInfo.getResultData());
//                                                        // TODO 跳转处理
//                                                        if ("10001".equals(jumpTo)) {
//                                                            Intent it = new Intent();
//                                                            setResult(20001, it);
//                                                            finish();
//                                                        } else {
//                                                            Intent intent = new Intent(NormalActivity.this, MainActivity.class);
//                                                            MainActivity.MainAc.finish();
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//
//                                                    } else { // 当前登录第三方与上次登录第三方账号设备不一致
//                                                        mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
//                                                        mDialog.setContentView(R.layout.login_mulbind_dialog);
//                                                        RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
//                                                        tipBtn_layout.setVisibility(View.GONE);
//                                                        RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
//                                                        mulTipBtn_layout.setVisibility(View.VISIBLE);
//                                                        TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
//                                                        Button mulEnsureBtn = (Button) mDialog.findViewById(R.id.mulEnsureBtn);
//                                                        tipMoreContent_txt.setText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!");
//                                                        mulEnsureBtn.setText("确定");
//                                                        mDialog.show();
//                                                        mulEnsureBtn.setOnClickListener(new View.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                mDialog.dismiss();
//                                                            }
//                                                        });
//                                                    }
//                                                } else { // 当前还未绑定设备号情况
//                                                    mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
//                                                    mDialog.setContentView(R.layout.login_bind_dialog);
//                                                    TextView tipContent_txt = (TextView) mDialog.findViewById(R.id.tipContent_txt);
//                                                    Button cancelBtn = (Button) mDialog.findViewById(R.id.cancelBtn);
//                                                    Button ensureBtn = (Button) mDialog.findViewById(R.id.ensureBtn);
//                                                    tipContent_txt.setText("该设备还未绑定,请绑定该设备");
//                                                    ensureBtn.setText("绑定");
//                                                    mDialog.show();
//                                                    // 取消按钮
//                                                    cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            mDialog.dismiss();
//                                                        }
//                                                    });
//                                                    // 绑定按钮
//                                                    ensureBtn.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            // 提示绑定设备
//                                                            bindDeiver(psd, bindJCInfo.getResultData().getS_Name(), mDialog, bindJCInfo.getResultData());
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                        });
//                                    } else { // 两次登录一致情况
//                                        if ("".equals(bindJCInfo.getDeviceId())) { // 判断是否有设备号
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
//                                                    bindDeiver(psd, bindJCInfo.getResultData().getS_Name(), mDialog, bindJCInfo.getResultData());
//                                                }
//                                            });
//                                        } else { // 获取设备号不空情况
//                                            if (ToolUtils.getUUID(mCtx).equals(bindJCInfo.getDeviceId())) { // 设备号一致情况
//                                                // 保存登录信息
//                                                saveUserInfo(bindJCInfo.getResultData());
//                                                // TODO 跳转处理
//                                                if ("10001".equals(jumpTo)) {
//                                                    Intent it = new Intent();
//                                                    setResult(20001, it);
//                                                    finish();
//                                                } else {
//                                                    Intent intent = new Intent(NormalActivity.this, MainActivity.class);
//                                                    MainActivity.MainAc.finish();
//                                                    startActivity(intent);
//                                                    finish();
//                                                }
//                                            } else if (!ToolUtils.getUUID(mCtx).equals(bindJCInfo.getDeviceId())
//                                                    && !"".equals(bindJCInfo.getDeviceId())) { // 设备号不一致情况
//                                                mDialog = new Dialog(baseActivity, R.style.NormalDialogStyle);
//                                                mDialog.setContentView(R.layout.login_mulbind_dialog);
//                                                RelativeLayout tipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.tipBtn_layout);
//                                                tipBtn_layout.setVisibility(View.GONE);
//                                                RelativeLayout mulTipBtn_layout = (RelativeLayout) mDialog.findViewById(R.id.mulTipBtn_layout);
//                                                mulTipBtn_layout.setVisibility(View.VISIBLE);
//                                                TextView tipMoreContent_txt = (TextView) mDialog.findViewById(R.id.tipMoreContent_txt);
//                                                Button mulEnsureBtn = (Button) mDialog.findViewById(R.id.mulEnsureBtn);
//                                                tipMoreContent_txt.setText("当前账号已被其他设备绑定,请先解绑账号!或者联系客服进行人工解绑!");
//                                                mulEnsureBtn.setText("确定");
//                                                mDialog.show();
//                                                mulEnsureBtn.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        // 清除数据
//                                                        clearLastLoginInfo(dialog);
//                                                        mDialog.dismiss();
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                } else if ("0".equals(bindStatus)) { // 需要绑定账号
//                                    Toast.makeText(mCtx, "您还没绑定金程网校账号，2秒后自动跳转绑定页面", Toast.LENGTH_SHORT).show();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Bundle bundle = new Bundle();
//                                            // 唯一识别码
//                                            bundle.putString("uid", data.get("uid"));
//                                            // 传递用户昵称
//                                            bundle.putString("name", data.get("name"));
//                                            // 传递用户图像
//                                            bundle.putString("iconurl", data.get("iconurl"));
//                                            bundle.putString("type", String.valueOf(type));
//                                            baseActivity.openActivity(OtherLoginActivity.class, bundle);
//                                        }
//                                    }, 2000);
//                                } else {
//                                    dialog = new SweetAlertDialog(mCtx, SweetAlertDialog.WARNING_TYPE);
//                                    dialog.setTitle("温馨提示");
//                                    dialog.setTitleText("第三方登录失败,请重新登录");
//                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            sweetAlertDialog.dismissWithAnimation();
//                                        }
//                                    });
//                                    dialog.show();
//                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                if (dialog != null && !dialog.isShowing()) {
                                    dialog.show();
                                }
                            }
                        });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(getApplicationContext(), "第三方授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        };
        UMShareAPI.get(this).getPlatformInfo(this, type, umAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 网络请求获取登录信息
     */
    private void getUserInfoForHttpRequest(String userName, final String password) {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitle("");
        dialog.setTitleText("");

        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("oauth", ToolUtils.getMD5Str(userName + password + "android!%@%$@#$"));
            obj.put("appname", commParam.getAppname());
            obj.put("loginname", userName);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());

        Observable<UserInfo> observable = httpPostService.getUserInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<UserInfo>() {

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
                                    Toast.makeText(NormalActivity.this, "登录异常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(final UserInfo userInfo) { // 请求成功处理
                                SharedPreferences loginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
                                // 学员id
                                sID = loginPre.getInt("S_ID", 0);
                                // 学员用户名
                                sName = loginPre.getString("S_Name", "");
                                // 学员真实姓名
                                sRealName = loginPre.getString("S_RealName", "");
                                // 学员图像
                                sHead = loginPre.getString("S_Head", "");
                                // 获取最近一次登录
                                SharedPreferences lastLoginPre = getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
                                int last_SID = lastLoginPre.getInt("S_ID", 0);
                                // 获取登录返回状态
                                code = userInfo.getCode();
                                if ("100".equals(code)) { // 登录成功
                                    // 获取当前设备绑定情况
                                    final int status = userInfo.getBody().getBindStatus();
                                    // 判断是否有上次登陆账号的记录
                                    if (0 == last_SID) { // 无上次登录信息
                                        if (0 == status) { // 无绑定记录
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
                                                    bindDeiver(psd, userInfo.getBody().getS_Name(), mDialog, userInfo.getBody());
                                                }
                                            });
                                        } else if (1 == status) { // 已被当前设备绑定
                                            // 保存用户信息
                                            saveUserSharePreferences(userInfo.getBody());
                                            // TODO 登录成功跳转
                                            Log.i("已被当前设备绑定", " === >");
                                            if ("10001".equals(jumpTo)) {
                                                Intent it = new Intent();
                                                setResult(20001, it);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(NormalActivity.this, MainActivity.class);
                                                MainActivity.MainAc.finish();
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else if (2 == status) { // 当前账号已在其他设备上登录
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
                                    } else if (last_SID == userInfo.getBody().getS_ID()) { // 如果有登录信息，则对比两次登录id
                                        if (0 == status) { // 未被任何设备绑定
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
                                                    bindDeiver(psd, userInfo.getBody().getS_Name(), mDialog, userInfo.getBody());
                                                }
                                            });
//
                                        } else if (1 == status) { // 已被当前设备绑定
                                            // 保存用户信息
                                            saveUserSharePreferences(userInfo.getBody());
                                            // TODO 登录成功跳转
                                            if ("10001".equals(jumpTo)) {
                                                Intent it = new Intent();
                                                setResult(20001, it);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(NormalActivity.this, MainActivity.class);
                                                MainActivity.MainAc.finish();
                                                startActivity(intent);
                                                finish();
                                            }
                                            Log.i("已被当前设备绑定", " === >");
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
                                    } else { // 两次登录账户不一致
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
                                                setBindDiever(status, userInfo.getBody());
                                            }
                                        });
                                    }
                                } else if ("102".equals(code)) {
                                    Toast.makeText(mCtx, userInfo.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 清除上次登录保存信息
     */
    private void clearLastLoginInfo(Dialog dialog) {

        dialog.dismiss();
        // 清除保存本地用户信息
        userInfoPrueferences = getSharedPreferences(Global.LOGIN_FLAG,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor userInfo_editor = userInfoPrueferences.edit();
        userInfo_editor.clear();
        userInfo_editor.commit();

        // 清除上次登录信息
        lastUserInfoPreference = getSharedPreferences(Global.LAST_LOGIN_FLAG,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor lastUserInfo_editor = lastUserInfoPreference.edit();
        lastUserInfo_editor.clear();
        lastUserInfo_editor.commit();

        // 清除缓存视频信息
        DelectPolyvDownload();
        // 清除本地下载数据
        DelectSQLDateBase();

    }

    /**
     * 绑定信息
     *
     * @param status
     */
    private void setBindDiever(int status, final User userInfo) {

        switch (status) {
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
                        bindDeiver(psd, userInfo.getS_Name(), mDialog, userInfo);
                    }
                });
                break;
            case 1: // 已被当前设备绑定
                Toast.makeText(mCtx, "登录成功", Toast.LENGTH_SHORT).show();
                // 保存用户信息
                saveUserSharePreferences(userInfo);
                // TODO 登录成功跳转处理
                if ("10001".equals(jumpTo)) {
                    Intent it = new Intent();
                    setResult(20001, it);
                    finish();
                } else {
                    Intent intent = new Intent(NormalActivity.this, MainActivity.class);
                    MainActivity.MainAc.finish();
                    startActivity(intent);
                    finish();
                }
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
     * 保存当前登录信息并保存登录成功后用户信息
     *
     * @param user
     */
    private void saveUserSharePreferences(User user) {
        // 获取最近登录preferences
        SharedPreferences lastSharePre = getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", user.getS_ID());
        editor.commit();
        // 保存用户信息
        saveUserInfo(user);

    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    private void saveUserInfo(User user) {
        // 存储登录信息
        SharedPreferences loginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginPre.edit();
        loginEditor.putInt("S_ID", user.getS_ID());
        loginEditor.putString("S_Name", user.getS_Name());
        loginEditor.putString("S_PassWord", psd);
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
     * 绑定设备信息
     *
     * @param password
     */
    private void bindDeiver(String password, String userName, final Dialog sweetAlertDialog, final User user) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("deviceid", commParam.getDeviceid());
            obj.put("oauth", ToolUtils.getMD5Str(userName + password + "android!%@%$@#$"));
            obj.put("appname", commParam.getAppname());
            obj.put("loginname", userName);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<DeiverInfo> observable = httpPostService.getDeiverInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<DeiverInfo>() {

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
                                    Toast.makeText(NormalActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(DeiverInfo deiverInfo) {
                                if ("100".equals(deiverInfo.getCode())) {
                                    Toast.makeText(NormalActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                    // 保存本地信息
                                    SaveSharePreferences(user);
                                    // TODO 跳转处理
                                    if ("10001".equals(jumpTo)) {
                                        Intent it = new Intent();
                                        setResult(20001, it);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(NormalActivity.this, MainActivity.class);
                                        MainActivity.MainAc.finish();
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(NormalActivity.this, deiverInfo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
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
        SharedPreferences lastSharePre = getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = lastSharePre.edit();
        editor.putInt("S_ID", user.getS_ID());
        editor.commit();

        // 存储登录信息
        saveUserInfo(user);

        // 绑定UUID
        SharedPreferences uuidPre = getSharedPreferences(Global.UUID_FLAG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor uuidEditor = uuidPre.edit();
        uuidEditor.putString("DeviceId", ToolUtils.getUUID(mCtx));
        uuidEditor.commit();
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
    @OnTextChanged({R.id.userName_txt, R.id.passWord_txt})
    void afterTextChanged(CharSequence text) {
        if (errorUser_tap_txt.getVisibility() == View.VISIBLE) {
            errorUser_tap_txt.setVisibility(View.GONE);
        } else if (errorPsd_tap_txt.getVisibility() == View.VISIBLE) {
            errorPsd_tap_txt.setVisibility(View.GONE);
        }
        userName = getEditTextInput();
        if (ToolUtils.isEmail(userName) && "1".equals(userName)) {
            if (!ToolUtils.isMobileNo(userName_txt.getText().toString().trim())) {
                setErrorInfo(errorUser_tap_txt, "非法手机号");
                userLogin_layout.setBackgroundResource(R.drawable.ic_error_box_input);
                hint_tag_img.setBackgroundResource(R.drawable.icon_input_error);
                hint_tag_img.setVisibility(View.VISIBLE);
            } else {
                userLogin_layout.setBackgroundResource(R.drawable.ic_login_box_input);
                hint_tag_img.setBackgroundResource(R.drawable.icon_input_correct);
                hint_tag_img.setVisibility(View.VISIBLE);
            }
        } else {
            userLogin_layout.setBackgroundResource(R.drawable.ic_login_box_input);
            hint_tag_img.setVisibility(View.GONE);
        }
    }

    @OnCheckedChanged({R.id.hint_PWDTag_img})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) { // 文本可见
            setShowOrHidePwd(passWord_txt, true);
            hint_PWDTag_img.setBackgroundResource(R.drawable.icon_show);
        } else {
            setShowOrHidePwd(passWord_txt, false);
            hint_PWDTag_img.setBackgroundResource(R.drawable.icon_hide);
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    private String getEditTextInput() {
        String firstChar = userName_txt.getText().toString().trim();
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
