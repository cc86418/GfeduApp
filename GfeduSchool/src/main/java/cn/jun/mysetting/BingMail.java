package cn.jun.mysetting;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jun.bean.ChangeInfo;
import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import cn.jun.view.AgainPassMail;
import cn.jun.view.CoustemDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class BingMail extends Activity implements View.OnClickListener {
    //返回键
    private RelativeLayout backLayout;
    private ImageButton backBtn;
    //绑定按钮
    private Button bingBtn;
    //文字提示
    private TextView tv_error;
    private RelativeLayout edit_layout_bg;
    //编辑框
    private EditText tv_mail;
    private String editMailString;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private ChangeInfo changeInfo;
    //获取到的输入的邮箱
    private String mailedit;
    //获取传递过来的邮箱
    private String mailString;
    private int userID;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bingmail);
        //获取用户信息
        GetUserSharePreferences();
        initView();
        initDate();
    }

    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userID = LoginPre.getInt("S_ID", 0);
        passWord = LoginPre.getString("S_PassWord", "");
//        userID = Integer.toString(SID);
//        userRealName = LoginPre.getString("S_RealName", "");
//        userHead = LoginPre.getString("S_Head", "");
    }

    public void initDate() {
        Bundle bundle = getIntent().getExtras();
        mailString = bundle.getString("mail");
        if (!"".equals(mailString))
            tv_mail.setText(mailString);
        else
            tv_mail.setHint("请输入邮箱");

    }

    public void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        bingBtn = (Button) findViewById(R.id.bingBtn);
        bingBtn.setOnClickListener(this);
        tv_error = (TextView) findViewById(R.id.tv_error);

        edit_layout_bg = (RelativeLayout) findViewById(R.id.edit_layout_bg);
        tv_mail = (EditText) findViewById(R.id.tv_mail);

    }

    public void initEdit() {
        editMailString = tv_mail.getText().toString().trim();
        Log.d("验证邮箱 -- ", "" + isEmail(editMailString));
        if (true == isEmail(editMailString)) {
            edit_layout_bg.setBackgroundResource(R.drawable.edit_background_all);
            tv_mail.setBackgroundResource(R.drawable.edit_bg_all);
            tv_error.setVisibility(View.GONE);
            if (httpUtils.isNetworkConnected(BingMail.this)) {
                AgainPassMail againPass = new AgainPassMail(BingMail.this, editMailString);
                againPass.show();
            }

//            if (httpUtils.isNetworkConnected(BingMail.this)) {
//                ChangeMailTask mailTask = new ChangeMailTask();
//                mailTask.execute(editMailString);
//            }
        } else {
            edit_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
            tv_mail.setBackgroundResource(R.drawable.edit_bg_all_error);
            tv_error.setText("邮箱格式不正确!");
            tv_error.setVisibility(View.VISIBLE);

        }

    }

    class ChangeMailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            mailedit = params[0];
            String MD5 = httpUtils.getMD5Str(userID);
            changeInfo = httpUtils.GetChangeInfo(Const.URL + Const.ChangeInfoAPI, userID, Const.CLIENT, MD5, "S_Email", mailedit);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (null != changeInfo && !"".equals(changeInfo)) {
                if (100 == changeInfo.getCode()) {
                    CoustemDialog dialog = new CoustemDialog(BingMail.this);
                    dialog.setMessage(mailedit);
                    dialog.show();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;

            case R.id.backBtn:
                finish();
                break;

            case R.id.bingBtn:
                initEdit();
//                CoustemDialog dialog = new CoustemDialog(BingMail.this);
//                dialog.setMessage(mailedit);
//                dialog.show();
                break;
        }
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


}
