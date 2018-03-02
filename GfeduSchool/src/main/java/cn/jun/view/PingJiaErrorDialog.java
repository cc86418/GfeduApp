package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.jun.bean.UnBindPhone;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;

public class PingJiaErrorDialog extends Dialog {
    //Button
//    private Button qx_btn, go_study;
        private ImageView dialog_titie;
    //上下文
    private Activity activity;
    private String message;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private SharedPreferences loginPre;
    private SharedPreferences.Editor loginEditor;
    private UnBindPhone unBindPhone;

    public PingJiaErrorDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public PingJiaErrorDialog(Activity context, int theme) {
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
        setContentView(R.layout.pj_error_dialog);
        setCanceledOnTouchOutside(false);
        dialog_titie = (ImageView) findViewById(R.id.dialog_titie);
        dialog_titie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });


    }

}
