package jc.cici.android.atom.view;


import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 加载完善信息页面
 * Created by atom on 2017/7/19.
 */

public class TestWebViewClient extends WebViewClient {

    private Context mCtx;
    private Handler mHandler;

    public TestWebViewClient(Context ctx, Handler handler) {
        this.mCtx = ctx;
        this.mHandler = handler;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (parseScheme(url)) { // 科目测试提交成功跳转
            mHandler.sendEmptyMessage(1);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 提交成功跳转url
     *
     * @param url
     * @return
     */
    public boolean parseScheme(String url) {
        if (url.contains("http://mapi.gfedu.cn/html/goback")) {
            return true;
        } else {
            return false;
        }
    }
}
