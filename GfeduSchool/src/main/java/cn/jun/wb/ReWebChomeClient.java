package cn.jun.wb;

import android.net.Uri;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by administrato on 2017/7/5.
 */

public class ReWebChomeClient extends WebChromeClient {


    private OpenFileChooserCallBack mOpenFileChooserCallBack;


    public ReWebChomeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }


    //For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
        Log.i("3.0+ ","3.0+ ");
    }


    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
        Log.i("< 3.0 ","< 3.0 ");
    }


    // For Android  > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
        Log.i("4.1.1+ ","4.1.1+");
    }


    // For Android 5.0+
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams
            fileChooserParams) {
        mOpenFileChooserCallBack.showFileChooserCallBack(filePathCallback);
        Log.i("5.0+ ","5.0+ ");
        return true;
    }


    public interface OpenFileChooserCallBack {
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);
        void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback);


    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        Log.i("onJsAlert","message ==== > "+ message);
        Log.i("onJsAlert","view ==== > "+ view);
        Log.i("onJsAlert","url ==== > "+ url);
        Log.i("onJsAlert","result ==== > "+ result);
//        AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivity.this)
//                .setTitle("提示").setMessage(message)
//                .setPositiveButton("ok",
//                        new AlertDialog.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                                result.confirm();
//                            }
//                        });
//
//        b2.setCancelable(false);
//        b2.create();
//        b2.show();
        return super.onJsAlert(view, url, message, result);
    }
}
