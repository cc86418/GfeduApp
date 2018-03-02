package cn.jun.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import jc.cici.android.R;

public class SynchronizationDialog_Fail extends Dialog  {
    private Context context;

    public SynchronizationDialog_Fail(Context context, String content) {
        super(context, R.style.SynchronizationDialog);

    }

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.synchornization_progressbar);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Button againBtn = (Button) findViewById(R.id.again_btn);
        ImageView xBtn = (ImageView) findViewById(R.id.xBtn);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickOkButton("againProgress");
            }
        });

    }




}
