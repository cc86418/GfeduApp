package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.jun.bean.UnBindPhone;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;

public class ZeroBuyDialog extends Dialog {
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

    public ZeroBuyDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public ZeroBuyDialog(Activity context, int theme) {
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
        setContentView(R.layout.zero_buy_dialog);
        setCanceledOnTouchOutside(false);
        qx_btn = (Button) findViewById(R.id.qx_btn);
        go_study = (Button) findViewById(R.id.go_study);
        qx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //去学习
        go_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
