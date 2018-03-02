package cn.jun.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jc.cici.android.R;


public class CacheSet extends Dialog {
    //Button
    private Button cacheBtn;
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


    public CacheSet(Activity context) {
        super(context);
        this.activity = context;

    }

    public CacheSet(Activity context, int theme) {
        super(context, theme);
        this.activity = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caech_set);
        setCanceledOnTouchOutside(false);

        cacheBtn = (Button) findViewById(R.id.cacheBtn);


        cacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickOkButton("cacheBtn");

            }
        });


    }


}

