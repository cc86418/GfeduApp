package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.jun.bean.UnBindPhone;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.ui.login.NormalActivity;

public class LoginDialog extends Dialog {
    //Button
    private Button qx_btn, go_study;

    //上下文
    private Activity activity;
    private String message;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private SharedPreferences loginPre;
    private SharedPreferences.Editor loginEditor;
    private UnBindPhone unBindPhone;

    public LoginDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public LoginDialog(Activity context, int theme) {
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
        setContentView(R.layout.login_dialog);
        setCanceledOnTouchOutside(false);
        qx_btn = (Button) findViewById(R.id.qx_btn);
        go_study = (Button) findViewById(R.id.go_study);
        qx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //去登陆
        go_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, NormalActivity.class);
                activity.startActivity(i);
            }
        });

    }

}
