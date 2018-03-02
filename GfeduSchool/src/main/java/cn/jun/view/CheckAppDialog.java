package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import cn.jun.mysetting.FeedBackWebViewClient;
import jc.cici.android.R;

public class CheckAppDialog extends Dialog {
    //Button
    private Button xc_Btn, goUpdateBtn, qzUpBtn;
    private ImageButton xBtn;
    //WebView
    private WebView wb;
    //上下文
    private Activity activity;
    private String IsForceUpdate;
    private String H5Like;
    private String DownloadUrl;
    private String message;
    //布局判断
    private RelativeLayout up_relative, qz_relative;

    public CheckAppDialog(Activity context, String IsForceUpdate, String H5Like, String DownloadUrl) {
        super(context);
        this.activity = context;
        this.IsForceUpdate = IsForceUpdate;
        this.H5Like = H5Like;
        this.DownloadUrl = DownloadUrl;
    }

    public CheckAppDialog(Activity context, int theme) {
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
        setContentView(R.layout.checkapp_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        up_relative = (RelativeLayout) findViewById(R.id.up_relative);
        qz_relative = (RelativeLayout) findViewById(R.id.qz_relative);
        wb = (WebView) findViewById(R.id.wb);

        xBtn = (ImageButton) findViewById(R.id.xBtn);
        xc_Btn = (Button) findViewById(R.id.xc_Btn);
        goUpdateBtn = (Button) findViewById(R.id.goUpdateBtn);
        qzUpBtn = (Button) findViewById(R.id.qzUpBtn);

        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.finish();
                dismiss();
            }
        });

        if ("1".equals(IsForceUpdate)) {// 需要强制更新
            qz_relative.setVisibility(View.VISIBLE);
            up_relative.setVisibility(View.GONE);
            xBtn.setVisibility(View.GONE);
        } else if ("0".equals(IsForceUpdate)) {//不需要强制更新
            qz_relative.setVisibility(View.VISIBLE);
            up_relative.setVisibility(View.GONE);
            xBtn.setVisibility(View.VISIBLE);
        }

        //强制更新
        qzUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                if(!"".equals(DownloadUrl) && null != DownloadUrl){
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse(DownloadUrl));
                    activity.startActivity(intent1);
                }


            }
        });

//        qzUpBtn.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    finish();
//                    return true;
//                } else {
//                    return false; // 默认返回
//                    // false，这里false不能屏蔽返回键，改成true就可以了
//                }
//            }
//        });

        //下次再说
        xc_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        //去更新
        goUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                if(!"".equals(DownloadUrl) && null != DownloadUrl){
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse(DownloadUrl));
                    activity.startActivity(intent1);
                }

            }
        });

        initView();
    }


    private void initView() {
        wb.loadUrl(H5Like);
        Log.i("H5Like",""+ H5Like);
        wb.getSettings().setJavaScriptEnabled(
                true);
        wb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        wb.setWebViewClient(new FeedBackWebViewClient(activity, wb));
    }

}
