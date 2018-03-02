package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jc.cici.android.R;

public class SetPassDialog extends Dialog {
    //Button
    private Button okBtn;
    //上下文
    private Activity activity;
    //标题
    String titie;
    //内容
    String massage;

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


    public SetPassDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public SetPassDialog(Activity context, int theme) {
        super(context, theme);
        this.activity = context;
    }

    public String getTitie() {
        return titie;
    }

    public void setTitie(String titie) {
        this.titie = titie;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpass_dialog);
        setCanceledOnTouchOutside(false);
        TextView dialog_titie = (TextView) findViewById(R.id.dialog_titie);
        TextView edit_massage = (TextView) findViewById(R.id.edit_massage);
        dialog_titie.setText(getTitie());
        edit_massage.setText(getMassage());
        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回这个自定义控件中计算出的值，使用回调实现
                icallBack.onClickOkButton("setpass_ok");

            }
        });

    }


}
