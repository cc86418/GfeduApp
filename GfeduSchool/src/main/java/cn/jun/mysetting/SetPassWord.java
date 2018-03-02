package cn.jun.mysetting;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import cn.jun.utils.PublicFunc;
import cn.jun.view.SetPassDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.ResetPwdActivity;

public class SetPassWord extends Activity implements View.OnClickListener {
    //返回
    private RelativeLayout backLayout;
    //输入框
    private EditText pass_ed, newpass_ed, newpass_ed2;
    private String oldPass = "", newPass = "", newPassAgain = "";
    //错误信息
    private TextView tv_error, tv_error2;
    //确认按钮
    private Button okBtn;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    // MD5
    private PublicFunc MD5 = new PublicFunc();
    private String MD5Result;
    private String MD5_Code = "&MD5code=";
    // 重设密码返回码
    private String SetPass_ResultCode;
    //本地信息
    private String userID;
    private String passWord;
    private String S_Telephone;
    //忘记密码tv
    private TextView forget_pass_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword);

        initView();
//        initGetDate();
        //获取用户信息
        GetUserSharePreferences();
    }

    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        int SID = LoginPre.getInt("S_ID", 0);
        userID = Integer.toString(SID);
        passWord = LoginPre.getString("S_PassWord", "");
        S_Telephone = LoginPre.getString("S_Telephone", "");
//        userHead = LoginPre.getString("S_Head", "");
    }


    public void initView() {
        pass_ed = (EditText) findViewById(R.id.pass_ed);
        newpass_ed = (EditText) findViewById(R.id.newpass_ed);
        newpass_ed2 = (EditText) findViewById(R.id.newpass_ed2);

        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_error2 = (TextView) findViewById(R.id.tv_error2);

        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);

        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);

        forget_pass_tv = (TextView) findViewById(R.id.forget_pass_tv);
        forget_pass_tv.setOnClickListener(this);
    }

    public void initEditData() {
        oldPass = pass_ed.getText().toString().trim();
        newPass = newpass_ed.getText().toString().trim();
        newPassAgain = newpass_ed2.getText().toString().trim();

        if (!"".equals(oldPass) && !"".equals(newPass) && !"".equals(newPassAgain) && newPass.equals(newPassAgain)) {
            SharedPreferences passW = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            String passString = passW.getString("S_PassWord", "");
            if (oldPass.equals(passString)) {
                tv_error.setVisibility(View.GONE);
                tv_error2.setVisibility(View.GONE);
                //修改密码
                if (httpUtils.isNetworkConnected(SetPassWord.this)) {
                    Set_PassTask SetPassTask = new Set_PassTask();
                    SetPassTask.execute(newPass);
                }
            } else {
                tv_error.setText("原密码不正确");
                tv_error.setVisibility(View.VISIBLE);
            }


        } else {
            if ("".equals(oldPass)) {
                tv_error.setText("原密码不能为空");
                tv_error.setVisibility(View.VISIBLE);
            } else if ("".equals(newPass) || "".equals(newPassAgain)) {
                tv_error2.setText("新密码不能为空");
                tv_error2.setVisibility(View.VISIBLE);
            } else if (!newPass.equals(newPassAgain)) {
                tv_error2.setText("请确认两次密码输入是否一致");
                tv_error2.setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * 重设密码异步
     **/
    class Set_PassTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String Spassword = arg0[0];
            try {
                String UrlFor = "{'Type':'S_Telephone','Code':'" + S_Telephone
                        + "','SPassword':'" + Spassword + "'}";
                MD5Result = MD5.getMD5Str(UrlFor + MD5.MD5_KEY);
                String urlEncoding = URLEncoder.encode(UrlFor, "UTF-8");
                SetPass_ResultCode = httpUtils
                        .ChangePassWordMsg(Const.ChangePassAPI + urlEncoding
                                + MD5_Code + MD5Result);
                System.out.println("发送的地址 : " + Const.ChangePassAPI
                        + UrlFor + MD5_Code + MD5Result);
                System.out.println("返回的请求参数 : " + SetPass_ResultCode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("-1".equals(SetPass_ResultCode)) {
                Toast.makeText(SetPassWord.this, "密码长度为6-20位。", Toast.LENGTH_LONG).show();
            } else if ("-2".equals(SetPass_ResultCode)) {
                Toast.makeText(SetPassWord.this, "服务器错误，请稍后再试。", Toast.LENGTH_LONG).show();
            } else if ("1".equals(SetPass_ResultCode)) {
                final SetPassDialog setPassDialog = new SetPassDialog(SetPassWord.this);
                setPassDialog.setTitie("提示");
                setPassDialog.setMassage("重置密码成功");
                setPassDialog.show();
                setPassDialog.setonClick(new SetPassDialog.ICoallBack() {
                    @Override
                    public void onClickOkButton(String s) {
                        if ("setpass_ok".equals(s)) {
                            setPassDialog.dismiss();
                            SetPassWord.this.finish();

                        }
                    }
                });

            } else {
                Toast.makeText(SetPassWord.this, "其他错误。", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okBtn:
                initEditData();
                break;

            case R.id.backLayout:
                finish();
                break;

            case R.id.forget_pass_tv:
                Intent intent = new Intent(this, ResetPwdActivity.class);
                startActivity(intent);
                break;
        }
    }
}
