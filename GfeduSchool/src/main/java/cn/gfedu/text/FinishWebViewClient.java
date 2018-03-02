package cn.gfedu.text;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by administrato on 2017/8/18.
 */

public class FinishWebViewClient extends WebViewClient {

    private Context mCtx;
    private Handler mHandler;

    public FinishWebViewClient(Context ctx, Handler handler) {
        this.mCtx = ctx;
        this.mHandler = handler;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("url ", "" + url);
//        if (parseScheme(url)) { // 返回
//            mHandler.sendEmptyMessage(1);
//        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i("onPageFinished  ", "" + url);
        if (parseScheme(url)) { // 返回
            mHandler.sendEmptyMessage(1);
        }
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
