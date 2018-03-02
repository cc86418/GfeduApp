package cn.jun.live;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jun.bean.Const;
import cn.jun.bean.GetLiveUrlBean;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class LiveH5Activity extends Activity implements View.OnClickListener {
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private RelativeLayout backLayout;
    private WebView wb;
    private String LiveStatus;
    private TextView class_title;

    //课程id
    private int classid;
    //传递的课表ID
    private int scheduleId;
    //类型--0：直播URL  1:点播URL
    private int searchType;
    //用户id
    private int UserID;

    //数据源
    private GetLiveUrlBean LiveUrlBean = new GetLiveUrlBean();
    //H5地址
    private String LiveH5;
    //标题
    private TextView live_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_h5);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            scheduleId = bundle.getInt("scheduleId");
            classid = bundle.getInt("classid");
            searchType = bundle.getInt("searchType");
        }

        initDate();
    }

    public void initDate() {
        if (httpUtils.isNetworkConnected(this)) {
            LiveH5Task task = new LiveH5Task();
            task.execute();
        }

    }

    class LiveH5Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
                if (null != LoginPre && !"".equals(LoginPre)) {
                    UserID = LoginPre.getInt("S_ID", 0);
                }
                LiveUrlBean = httpUtils.getLiveUrl(Const.URL + Const.GetLiveUrlAPI, UserID, scheduleId, classid, searchType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == LiveUrlBean.getCode()) {
                LiveH5 = LiveUrlBean.getBody().getLinkURL();
                initView(LiveH5);
            }
        }
    }


    public void initView(String LiveH5) {
        Log.i("LiveH5 - url ",""+LiveH5);
        class_title = (TextView) findViewById(R.id.class_title);
        if (0 == searchType) {
            class_title.setText("直播");
        } else {
            class_title.setText("点播");
        }
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        wb = (WebView) findViewById(R.id.wb);
        //aish666
        if (!"".equals(LiveH5) && null != LiveH5) {
            wb.getSettings().setJavaScriptEnabled(true);
            wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wb.getSettings().setSupportMultipleWindows(true);
            wb.getSettings()
                    .setDefaultTextEncodingName("UTF-8");
            wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            wb.setWebChromeClient(new ReWebChomeClient(this));
//            wb.setWebViewClient(new FinishWebViewClient(this, handler));
//            wb.loadUrl(LiveH5);
            //设置Web视图
            wb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
            wb.loadUrl(LiveH5);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
        }
    }
}
