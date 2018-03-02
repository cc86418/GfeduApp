package cn.jun.live;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jun.mysetting.FeedBackWebViewClient;
import jc.cici.android.R;

public class LiveActivity extends Activity implements View.OnClickListener{
    private RelativeLayout backLayout;
    private WebView wb;
    private String LiveH5;
    private String LiveStatus;
    private TextView class_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);

        Bundle bundle = getIntent().getExtras();
        LiveH5 = bundle.getString("LiveH5");
        LiveStatus = bundle.getString("LiveStatus");
        Log.i("LiveH5 -- ",""+ LiveH5);
        Log.i("LiveStatus -- ",""+ LiveStatus);
        initView();

    }

    private void initView(){
        class_title = (TextView) findViewById(R.id.class_title);
        if("0".equals(LiveStatus)){
            class_title.setText("直播");
        }else if("1".equals(LiveStatus)){
            class_title.setText("直播");
        }else if("2".equals(LiveStatus)){
            class_title.setText("回放");
        }else{
            class_title.setText("直播");
        }
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        wb = (WebView) findViewById(R.id.wb);
        wb.loadUrl(LiveH5);
        wb.getSettings().setJavaScriptEnabled(
                true);
        wb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        wb.setWebViewClient(new FeedBackWebViewClient(LiveActivity.this, wb));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wb.canGoBack()) {
            wb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wb.destroy();
    }
}
