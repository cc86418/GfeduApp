package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jc.cici.android.R;

public class CoustemDialog extends Dialog {
    //Button
    private Button okBtn,backBtn;
    //上下文
    private Activity activity;

    private String message;


    public CoustemDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public CoustemDialog(Activity context, int theme) {
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
        setContentView(R.layout.coustem_dialog);
        setCanceledOnTouchOutside(false);
        okBtn = (Button) findViewById(R.id.okBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        TextView edit_massage = (TextView) findViewById(R.id.edit_massage);
        edit_massage.setText("系统已将绑定连接发送到"+getMessage()+"中，请到邮箱中点击链接完成绑定操作");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                activity.finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



}
