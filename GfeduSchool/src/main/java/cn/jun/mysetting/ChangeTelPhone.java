package cn.jun.mysetting;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jun.bean.CheckPwdBean;
import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.ResetPwdActivity;

public class ChangeTelPhone extends Activity implements View.OnClickListener {
    //返回
    private RelativeLayout backLayout;
    //输入框
    private EditText tel_edit;
    private String telString = "";
    //错误提示
    private TextView tv_error;
    private RelativeLayout edit_layout_bg;
    //忘记密码
    private TextView for_pass;
    //下一步
    private Button nextBtn;
    //JCLogin
    //S_PassWord
    //数据源
    private CheckPwdBean checkPwdBean;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //本地信息
    private String userID;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_telphone);
        initView();
    }

    private void GetUserSharePreferences(){
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        int SID = LoginPre.getInt("S_ID", 0);
        userID = Integer.toString(SID);
//        userRealName = LoginPre.getString("S_RealName", "");
//        userHead = LoginPre.getString("S_Head", "");
    }


    public void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        tel_edit = (EditText) findViewById(R.id.tel_edit);
        tv_error = (TextView) findViewById(R.id.tv_error);
        edit_layout_bg = (RelativeLayout) findViewById(R.id.edit_layout_bg);
        for_pass = (TextView) findViewById(R.id.for_pass);
        for_pass.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    public void initEditData() {
        telString = tel_edit.getText().toString().trim();
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        int SID = LoginPre.getInt("S_ID", 0);
        userID = Integer.toString(SID);
        Log.i("telString ","" + telString);
        Log.i("userID",""+ userID);

        if (!"".equals(telString)) {
//            if (telString.equals(passWord)) {
                CheckPwdTask checkPwdTask = new CheckPwdTask();
                checkPwdTask.execute(telString);

//            } else {
//                edit_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
//                tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
//                tv_error.setVisibility(View.VISIBLE);
//                tv_error.setText("密码错误");
//            }
        } else {
            edit_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
            tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
            tv_error.setVisibility(View.VISIBLE);
            tv_error.setText("请输入密码");
        }


    }


    //校检密码
    class CheckPwdTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String pws = arg0[0];
            String MD5String = httpUtils.getMD5Str2(userID + pws);
            checkPwdBean = httpUtils.getCheckPwdBean(Const.URL + Const.CheckPwdAPI
                    , userID, Const.CLIENT, pws, MD5String);
            Log.d(" ---- > ", checkPwdBean.getCode() + "");
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == checkPwdBean.getCode()) {
                edit_layout_bg.setBackgroundResource(R.drawable.edit_background_all);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all);
                tv_error.setVisibility(View.GONE);
                //跳转下一步
                Intent intent = new Intent(ChangeTelPhone.this, ChangeTelNext.class);
                startActivity(intent);
                finish();
            } else {
                edit_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText(checkPwdBean.getMessage());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;

            case R.id.nextBtn:
                initEditData();
//                Intent intent = new Intent(this,ChangeTelNext.class);
//                startActivity(intent);
//                finish();
                break;


            case R.id.for_pass:
                Intent for_Intent = new Intent(this, ResetPwdActivity.class);
                startActivity(for_Intent);
                break;
        }

    }


}
