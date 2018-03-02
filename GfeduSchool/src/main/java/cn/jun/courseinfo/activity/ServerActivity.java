package cn.jun.courseinfo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import jc.cici.android.R;


public class ServerActivity extends Activity {
    private WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_activity);
        wb = (WebView) findViewById(R.id.wb);
        wb.loadUrl("http://image.gfedu.cn//UploadFile/Images/20170810120006369.jpg");
        //http://image.gfedu.cn//UploadFile/Images/20170810120006369.jpg
        wb.getSettings().setJavaScriptEnabled(true);
    }
}
