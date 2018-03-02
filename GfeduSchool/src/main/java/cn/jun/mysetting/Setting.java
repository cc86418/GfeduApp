package cn.jun.mysetting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.gfedu.gfeduapp.MainActivity;
import cn.jun.bean.BindUser;
import cn.jun.bean.Const;
import cn.jun.bean.GetThridBing;
import cn.jun.bean.UnBindUser;
import cn.jun.utils.HttpUtils;
import cn.jun.utils.PublicFunc;
import cn.jun.view.BingThridDialog;
import cn.jun.view.QuiteDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

/**
 * 设置中心
 */
public class Setting extends Activity implements View.OnClickListener {
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //点击事件
    private RelativeLayout xinxi_click;
    private RelativeLayout dizhi_click;
    private RelativeLayout xingqu_click;
    private RelativeLayout genghuanshouji_click;
    private RelativeLayout mail_click;
    private RelativeLayout weixin_click;
    private RelativeLayout qq_click;
    private RelativeLayout weibo_click;
    private RelativeLayout password_click;
    private Button quite_btn;
    //绑定
    private TextView tel_textview;
    private TextView mail_textview;
    private String mailString = "";
    private TextView mailbd_textview;
    private TextView weixinbd_textview;
    private TextView qqbd_textview;
    private TextView weibobd_textview;
    //数据源
    private GetThridBing GetThridBing = new GetThridBing();
    //第三方授权
//    private ProgressDialog UMAuthDialog;
    //第三方返回的openId
    private String OpenID;
    // 参数
    private String BindUserPrarms;
    //老版本MD5加密
    private PublicFunc MD5 = new PublicFunc();
    private String MD5Result;
    private String MD5_Code = "&MD5code=";
    // 第三方绑定新账号返回数据
    private BindUser mBindUser;
    //解除绑定返回数据
    private UnBindUser unBindUser;
    //用户ID
    private int userID;
    private String BindType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

    }


    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            userID = LoginPre.getInt("S_ID", 0);
//            UserID = Integer.toString(SID);
        }

    }


    public void initView() {
        xinxi_click = (RelativeLayout) findViewById(R.id.xinxi_click);
        dizhi_click = (RelativeLayout) findViewById(R.id.dizhi_click);
        xingqu_click = (RelativeLayout) findViewById(R.id.xingqu_click);
        genghuanshouji_click = (RelativeLayout) findViewById(R.id.genghuanshouji_click);
        mail_click = (RelativeLayout) findViewById(R.id.mail_click);
        weixin_click = (RelativeLayout) findViewById(R.id.weixin_click);
        qq_click = (RelativeLayout) findViewById(R.id.qq_click);
        weibo_click = (RelativeLayout) findViewById(R.id.weibo_click);
        password_click = (RelativeLayout) findViewById(R.id.password_click);
        xinxi_click.setOnClickListener(this);
        dizhi_click.setOnClickListener(this);
        xingqu_click.setOnClickListener(this);
        genghuanshouji_click.setOnClickListener(this);
        mail_click.setOnClickListener(this);
        weixin_click.setOnClickListener(this);
        qq_click.setOnClickListener(this);
        weibo_click.setOnClickListener(this);
        password_click.setOnClickListener(this);

        quite_btn = (Button) findViewById(R.id.quite_btn);
        quite_btn.setOnClickListener(this);

        tel_textview = (TextView) findViewById(R.id.tel_textview);
        mail_textview = (TextView) findViewById(R.id.mail_textview);
        mailbd_textview = (TextView) findViewById(R.id.mailbd_textview);
        weixinbd_textview = (TextView) findViewById(R.id.weixinbd_textview);
        qqbd_textview = (TextView) findViewById(R.id.qqbd_textview);
        weibobd_textview = (TextView) findViewById(R.id.weibobd_textview);

    }

    public void initGetDate() {
        if (httpUtils.isNetworkConnected(this)) {
            GetThridBingTask thridBingTask = new GetThridBingTask();
            thridBingTask.execute();
        } else {
            Toast.makeText(this, "网络连接异常!", Toast.LENGTH_SHORT).show();
        }

    }


    class GetThridBingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            GetThridBing = httpUtils.getbindinfo(Const.URL + Const.GetThridBingAPI
                    , userID);
//            Log.d(" ---- > ", "第三方返回的code --- "+GetThridBing.getCode());
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (GetThridBing.getCode() == 100) {
                tel_textview.setText(GetThridBing.getBody().getMobile());
                if (GetThridBing.getBody().getBindEail() == 1) {
                    mail_textview.setText(GetThridBing.getBody().getEmail() + "");
                    mailbd_textview.setText("已绑定");
                    mailbd_textview.setTextColor(Color.parseColor("#DD5555"));
                    mailString = GetThridBing.getBody().getEmail();
                } else {
                    mail_textview.setText("");
                    mailbd_textview.setText("未绑定");
                    mailbd_textview.setTextColor(Color.parseColor("#999999"));
                }
                if (GetThridBing.getBody().getBindWECHAT() == 1) {
                    weixinbd_textview.setText("已认证");
                    weixinbd_textview.setTextColor(Color.parseColor("#DD5555"));
                } else {
                    weixinbd_textview.setText("未认证");
                    weixinbd_textview.setTextColor(Color.parseColor("#999999"));
                }
                if (GetThridBing.getBody().getBindQQ() == 1) {
                    qqbd_textview.setText("已认证");
                    qqbd_textview.setTextColor(Color.parseColor("#DD5555"));
                } else {
                    qqbd_textview.setText("未认证");
                    qqbd_textview.setTextColor(Color.parseColor("#999999"));
                }
                if (GetThridBing.getBody().getBindSINA() == 1) {
                    weibobd_textview.setText("已认证");
                    weibobd_textview.setTextColor(Color.parseColor("#DD5555"));
                } else {
                    weibobd_textview.setText("未认证");
                    weibobd_textview.setTextColor(Color.parseColor("#999999"));
                }

            } else {

            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 退出当前账号
            case R.id.quite_btn:
                QuiteDialog quiteDialog = new QuiteDialog(this);
                quiteDialog.show();
                break;
            //个人信息
            case R.id.xinxi_click:
                Intent xinxi_intent = new Intent(Setting.this, Information.class);
                startActivity(xinxi_intent);
                break;
            //收货地址
            case R.id.dizhi_click:
                Toast.makeText(Setting.this, "暂未开放!", Toast.LENGTH_LONG).show();
                break;
            //我的兴趣
            case R.id.xingqu_click:
                Intent xingqu_Intent = new Intent(this, Interests.class);
                startActivity(xingqu_Intent);
                break;
            //更换手机
            case R.id.genghuanshouji_click:
                Intent tel_Intent = new Intent(this, ChangeTelPhone.class);
                startActivity(tel_Intent);
                break;
            //邮箱
            case R.id.mail_click:
                Intent mailIntent = new Intent(this, BingMail.class);
                Bundle mailBundle = new Bundle();
                mailBundle.putString("mail", mailString);
                mailIntent.putExtras(mailBundle);
                startActivity(mailIntent);
                break;
            //绑定微信
            case R.id.weixin_click:
                if (httpUtils.isNetworkConnected(this)) {
                    initUMAuth(SHARE_MEDIA.WEIXIN, "WX");
                }
                break;
            //绑定QQ
            case R.id.qq_click:
                if (httpUtils.isNetworkConnected(this)) {
                    initUMAuth(SHARE_MEDIA.QQ, "QQ");
                }
                break;
            //绑定微博
            case R.id.weibo_click:
                if (httpUtils.isNetworkConnected(this)) {
                    initUMAuth(SHARE_MEDIA.SINA, "SINA");
                }
                break;
            //修改密码
            case R.id.password_click:
                Intent password_intent = new Intent(this, SetPassWord.class);
                startActivity(password_intent);
                break;

        }
    }

    public void initUMAuth(final SHARE_MEDIA SHARE_MEDIA, String Type) {
        boolean isauth = UMShareAPI.get(Setting.this).isAuthorize(Setting.this, SHARE_MEDIA);
        Log.d("---> ", "是否已经绑定过SHARE_MEDIA -- " + SHARE_MEDIA);
        Log.d("---> ", "是否已经绑定过 -- " + isauth);
        if (isauth) {
            final BingThridDialog dialog = new BingThridDialog(this);
            if ("WX".equals(Type)) {
                dialog.setTitie("确定解绑微信账号?");
                dialog.setMassage("解绑后，您将不能通过微信一键登录，请慎重操作！");
            } else if ("QQ".equals(Type)) {
                dialog.setTitie("确定解绑QQ账号");
                dialog.setMassage("解绑后，您将不能通过QQ一键登录，请慎重操作！");
            } else if ("SINA".equals(Type)) {
                dialog.setTitie("确定解绑微博账号");
                dialog.setMassage("解绑后，您将不能通过微博一键登录，请慎重操作！");
            }
            dialog.show();
            dialog.setonClick(new BingThridDialog.ICoallBack() {
                @Override
                public void onClickOkButton(String s) {
                    if ("deleteOauth".equals(s)) {
                        UMShareAPI.get(Setting.this).deleteOauth(Setting.this, SHARE_MEDIA, InfoauthListener);
                        dialog.dismiss();
                        //解除绑定信息
                    }
                }
            });
        } else {
//            UMShareAPI.get(Setting.this).doOauthVerify(Setting.this, SHARE_MEDIA, authListener);
            Log.d("走绑定方法 --- 》 ", "");
            UMShareAPI.get(this).getPlatformInfo(Setting.this, SHARE_MEDIA, InfoauthListener);
        }
//        Log.i("1111 == >", "" + GetThridBing.getBody().getBindWECHAT());
//        Log.i("2222 ==> ", "" + GetThridBing.getBody().getBindQQ());
//        Log.i("3333 ==> ", "" + GetThridBing.getBody().getBindSINA());
//
//        if (1 == GetThridBing.getBody().getBindWECHAT()) {
//            final BingThridDialog dialog = new BingThridDialog(this);
//            if ("WX".equals(Type)) {
//                dialog.setTitie("确定解绑微信账号?");
//                dialog.setMassage("解绑后，您将不能通过微信一键登录，请慎重操作！");
//            } else if ("QQ".equals(Type)) {
//                dialog.setTitie("确定解绑QQ账号");
//                dialog.setMassage("解绑后，您将不能通过QQ一键登录，请慎重操作！");
//            } else if ("SINA".equals(Type)) {
//                dialog.setTitie("确定解绑微博账号");
//                dialog.setMassage("解绑后，您将不能通过微博一键登录，请慎重操作！");
//            }
//            dialog.show();
//            dialog.setonClick(new BingThridDialog.ICoallBack() {
//                @Override
//                public void onClickOkButton(String s) {
//                    if ("deleteOauth".equals(s)) {
//                        UMShareAPI.get(Setting.this).deleteOauth(Setting.this, SHARE_MEDIA, InfoauthListener);
//                        dialog.dismiss();
//                        //解除绑定信息
//                    }
//                }
//            });
//        } else if (1 == GetThridBing.getBody().getBindQQ()) {
//            final BingThridDialog dialog = new BingThridDialog(this);
//            if ("WX".equals(Type)) {
//                dialog.setTitie("确定解绑微信账号?");
//                dialog.setMassage("解绑后，您将不能通过微信一键登录，请慎重操作！");
//            } else if ("QQ".equals(Type)) {
//                dialog.setTitie("确定解绑QQ账号");
//                dialog.setMassage("解绑后，您将不能通过QQ一键登录，请慎重操作！");
//            } else if ("SINA".equals(Type)) {
//                dialog.setTitie("确定解绑微博账号");
//                dialog.setMassage("解绑后，您将不能通过微博一键登录，请慎重操作！");
//            }
//            dialog.show();
//            dialog.setonClick(new BingThridDialog.ICoallBack() {
//                @Override
//                public void onClickOkButton(String s) {
//                    if ("deleteOauth".equals(s)) {
//                        UMShareAPI.get(Setting.this).deleteOauth(Setting.this, SHARE_MEDIA, InfoauthListener);
//                        dialog.dismiss();
//                        //解除绑定信息
//                    }
//                }
//            });
//        } else if ((1 == GetThridBing.getBody().getBindSINA())) {
//            final BingThridDialog dialog = new BingThridDialog(this);
//            if ("WX".equals(Type)) {
//                dialog.setTitie("确定解绑微信账号?");
//                dialog.setMassage("解绑后，您将不能通过微信一键登录，请慎重操作！");
//            } else if ("QQ".equals(Type)) {
//                dialog.setTitie("确定解绑QQ账号");
//                dialog.setMassage("解绑后，您将不能通过QQ一键登录，请慎重操作！");
//            } else if ("SINA".equals(Type)) {
//                dialog.setTitie("确定解绑微博账号");
//                dialog.setMassage("解绑后，您将不能通过微博一键登录，请慎重操作！");
//            }
//            dialog.show();
//            dialog.setonClick(new BingThridDialog.ICoallBack() {
//                @Override
//                public void onClickOkButton(String s) {
//                    if ("deleteOauth".equals(s)) {
//                        UMShareAPI.get(Setting.this).deleteOauth(Setting.this, SHARE_MEDIA, InfoauthListener);
//                        dialog.dismiss();
//                        //解除绑定信息
//                    }
//                }
//            });
//        } else {
//            UMShareAPI.get(this).getPlatformInfo(Setting.this, SHARE_MEDIA, InfoauthListener);
//        }


    }


    UMAuthListener InfoauthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            SocializeUtils.safeShowDialog(UMAuthDialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(Setting.this, "成功了", Toast.LENGTH_LONG).show();
            Log.d("action=========== ", " " + action);
            //0绑定//1删除授权//2获得资料
            if (ACTION_GET_PROFILE == action) {
                Log.i("platform == ", "" + platform.toString());
                if (platform == SHARE_MEDIA.SINA) {
                    String Type = "SINA";
                    OpenID = data.get("id");
                    Log.d("用户信息 新浪 ", " " + data.get("id"));
                    if (!"".equals(OpenID) && null != OpenID) {
                        if (httpUtils.isNetworkConnected(Setting.this)) {
                            BindAccountTask BindAccountTask = new BindAccountTask();
                            BindAccountTask.execute(Type);
                        }
                    }
                } else {
                    OpenID = data.get("openid");
                    Log.d("用户信息 openid ", " " + data.get("openid"));
//                    String temp = "";
//                    for (String key : data.keySet()) {
//                        temp = temp + key + " : " + data.get(key) + "\n";
//                    }
                    if (!"".equals(OpenID) && null != OpenID) {
                        if (httpUtils.isNetworkConnected(Setting.this)) {
                            String Type = null;
                            if (platform == SHARE_MEDIA.WEIXIN) {
                                Type = "WECHAT";
                                BindAccountTask BindAccountTask = new BindAccountTask();
                                BindAccountTask.execute(Type);
                            } else if (platform == SHARE_MEDIA.QQ) {
                                Type = "QQ";
                                BindAccountTask BindAccountTask = new BindAccountTask();
                                BindAccountTask.execute(Type);
                            }

                        }
                    }
                }
            } else if (ACTION_DELETE == action) {
                String Type = null;
                if (httpUtils.isNetworkConnected(Setting.this)) {
                    if (platform == SHARE_MEDIA.WEIXIN) {
                        Type = "WECHAT";
                    } else if (platform == SHARE_MEDIA.QQ) {
                        Type = "QQ";
                    } else if (platform == SHARE_MEDIA.SINA) {
                        Type = "SINA";
                    }
                    UnBindAccountTask UnBindAccountTask = new UnBindAccountTask();
                    UnBindAccountTask.execute(Type);

                }
            }

//            SocializeUtils.safeCloseDialog(UMAuthDialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.i("onError == > ", "onError");
//            SocializeUtils.safeCloseDialog(UMAuthDialog);
            Toast.makeText(Setting.this, "请稍后再试!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.i("onCancel == > ", "onCancel");
//            SocializeUtils.safeCloseDialog(UMAuthDialog);
        }
    };

    /**
     * 绑定账号(老版本接口)
     **/
    class BindAccountTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int SID = LoginPre.getInt("S_ID", 0);
            String RealName = LoginPre.getString("S_Name", "");
            String passWord = LoginPre.getString("S_PassWord", "");
            //type-QQ,SINA,WECHAT
//            String nameEditString = "xiaoyang";
//            String telEditString = "111111";
//            String ThirdType = "WECHAT";
            String ThirdType = arg0[0];
            BindType = arg0[0];
            BindUserPrarms = "{'UserName':'" + RealName + "','UserPwd':'"
                    + passWord + "','OpenID':'" + OpenID
                    + "','ThirdType':'" + ThirdType + "'}";
            try {
                String urlEncoding = URLEncoder.encode(BindUserPrarms, "UTF-8");
                MD5Result = MD5.getMD5Str(BindUserPrarms + MD5.MD5_KEY);
                mBindUser = httpUtils.BindUserList(Const.BindUserAPI + urlEncoding
                        + MD5_Code + MD5Result);
                Log.i("绑定第三方请求 == > ", "" + Const.BindUserAPI + BindUserPrarms + MD5_Code + MD5Result);
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String state = mBindUser.getResultState();
            if (!"".equals(state) || null != state) {
                if ("1".equals(state)) {
                    Toast.makeText(Setting.this,
                            "绑定成功", Toast.LENGTH_SHORT).show();
                    //type-QQ,SINA,WECHAT
                    if ("QQ".equals(BindType)) {
                        qqbd_textview.setText("已认证");
                        qqbd_textview.setTextColor(Color.parseColor("#DD5555"));
                    } else if ("SINA".equals(BindType)) {
                        weibobd_textview.setText("已认证");
                        weibobd_textview.setTextColor(Color.parseColor("#DD5555"));
                    } else if ("WECHAT".equals(BindType)) {
                        weixinbd_textview.setText("已认证");
                        weixinbd_textview.setTextColor(Color.parseColor("#DD5555"));

                    }

                } else if ("0".equals(state)) {
                    Toast.makeText(Setting.this,
                            "绑定失败: " + mBindUser.getResultStr(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Setting.this,
                            "绑定失败: " + mBindUser.getResultStr(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    /**
     * 解除绑定账号(新版本接口)
     **/
    class UnBindAccountTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            //type-QQ,SINA,WECHAT
            String Type = arg0[0];
            BindType = arg0[0];
            String MD5 = httpUtils.getMD5Str(userID);
            unBindUser = httpUtils.GetUnBindUser(Const.URL + Const.UnBingAPI
                    , Const.CLIENT, userID, MD5, Type);
            Log.d(" ---- > ", unBindUser.getCode() + "");

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            int Code = unBindUser.getCode();
            if (100 == Code) {
                Toast.makeText(Setting.this,
                        "解绑成功", Toast.LENGTH_SHORT).show();
                if ("QQ".equals(BindType)) {
                    qqbd_textview.setText("未认证");
                    qqbd_textview.setTextColor(Color.parseColor("#999999"));
                } else if ("SINA".equals(BindType)) {
                    weibobd_textview.setText("未认证");
                    weibobd_textview.setTextColor(Color.parseColor("#999999"));
                } else if ("WECHAT".equals(BindType)) {
                    weixinbd_textview.setText("未认证");
                    weixinbd_textview.setTextColor(Color.parseColor("#999999"));
                }
            } else {
                Toast.makeText(Setting.this,
                        "解绑失败: " + unBindUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        //onActivityResult--requestCode: 11101 | resultCode: -1data = null ? false
//        Log.d("返回的数据 -- >data ", "" + data.getStringExtra("openid"));
//        SocializeUtils.safeCloseDialog(UMAuthDialog);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (httpUtils.isNetworkConnected(Setting.this)) {
            //获取用户信息
            GetUserSharePreferences();
//            UMAuthDialog = new ProgressDialog(Setting.this);
            initView();
            initGetDate();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent refreshIntent = new Intent(this, MainActivity.class);
            startActivity(refreshIntent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
