package cn.jun.mysetting;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import cn.gfedu.text.FinishWebViewClient;
import cn.jun.bean.Const;
import jc.cici.android.R;

public class HelpCenter extends Activity {
    private RelativeLayout backLayout;
    private WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
//        backLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        wb = (WebView) findViewById(R.id.wb);

        initData();
    }

    private void initData() {
        String LinkH5 = Const.HelpCenterAPI;
        wb.loadUrl(LinkH5);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDefaultTextEncodingName("UTF-8");
//        wb.setWebViewClient(new FeedBackWebViewClient(HelpCenter.this, wb));
        wb.setWebViewClient(new FinishWebViewClient(HelpCenter.this, handler));

//        wb.loadUrl("javascript:/html/goback");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wb.canGoBack()) {
            wb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: //提交成功
                    finish();
                    break;
//                case 2: // 返回按钮
//                    finish();
//                    break;
                default:
                    break;
            }
        }
    };

}
