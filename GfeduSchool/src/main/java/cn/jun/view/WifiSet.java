package cn.jun.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jc.cici.android.R;


public class WifiSet extends Dialog {
    //Button
    private Button okBtn;
    private Button xBtn;
    private Activity activity;

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


    public WifiSet(Activity context) {
        super(context);
        this.activity = context;

    }

    public WifiSet(Activity context, int theme) {
        super(context, theme);
        this.activity = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_set);
        setCanceledOnTouchOutside(false);

        okBtn = (Button) findViewById(R.id.wiftBtn);
        xBtn = (Button) findViewById(R.id.xtBtn);

        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickOkButton("x_btn");

            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickOkButton("ok_btn");

            }
        });

    }


}

