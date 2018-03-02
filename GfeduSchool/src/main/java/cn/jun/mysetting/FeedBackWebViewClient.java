package cn.jun.mysetting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class FeedBackWebViewClient extends WebViewClient {
    private Activity mActivity;
    private WebView wb;


    public FeedBackWebViewClient(Activity mActivity, WebView wb) {
        this.mActivity = mActivity;
        this.wb = wb;

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;

    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    @Deprecated
    public void onReceivedError(final WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        view.loadData(
                "<html><body><h1>网络已断开，请返回重新加载!</h1></body></html>",
                "text/html", "UTF-8");
//        reloadBtn.setVisibility(View.VISIBLE);
//        reloadBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                view.loadUrl(url);
//                reloadBtn.setVisibility(View.GONE);
//
//            }
//        });
    }


}
