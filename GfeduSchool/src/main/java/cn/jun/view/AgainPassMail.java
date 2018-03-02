package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.jun.bean.ChangeInfo;
import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;


public class AgainPassMail extends Dialog {
    //进度
    private Dialog mDialog;
    //Button
    private Button quitebtn;
    private ImageButton xBtn;
    private EditText edit_massage;
    private String EditPass;
    //上下文
    private Activity activity;
    private String message;

    private int userID;
    private String passWord;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private ChangeInfo changeInfo;
    private String editMailString;

    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onClickOkButton(String s);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack 接口类型
     */
    public void setonClick(ICoallBack iBack) {
        icallBack = iBack;
    }

    public AgainPassMail(Activity context, String editMailString) {
        super(context);
        this.activity = context;
        this.editMailString = editMailString;
    }

    public AgainPassMail(Activity context, int theme) {
        super(context, theme);
        this.activity = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.again_pass);
        setCanceledOnTouchOutside(false);
        GetUserSharePreferences();
        edit_massage = (EditText) findViewById(R.id.edit_massage);
        ImageButton xBtn = (ImageButton) findViewById(R.id.xBtn);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        quitebtn = (Button) findViewById(R.id.backBtn);
        quitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPass = edit_massage.getText().toString().trim();
                if (!"".equals(EditPass) && null != EditPass) {
                    SendMailTask sendMailTask = new SendMailTask();
                    sendMailTask.execute();
                } else {
                    Toast.makeText(activity, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userID = LoginPre.getInt("S_ID", 0);
        passWord = LoginPre.getString("S_PassWord", "");
//        userID = Integer.toString(SID);
//        userRealName = LoginPre.getString("S_RealName", "");
//        userHead = LoginPre.getString("S_Head", "");
    }


    class SendMailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String MD5 = httpUtils.getMD5Str2(userID + editMailString);
            changeInfo = httpUtils.SendMailInfo(Const.URL + Const.SendBindeMailAPI, userID, Const.CLIENT, passWord, editMailString, MD5);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (null != changeInfo && !"".equals(changeInfo)) {
                if (100 == changeInfo.getCode()) {
                } else {
                    dismiss();
                    Toast.makeText(activity, changeInfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
