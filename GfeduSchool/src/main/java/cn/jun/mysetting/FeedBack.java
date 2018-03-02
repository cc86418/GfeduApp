package cn.jun.mysetting;


import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import cn.gfedu.text.FinishWebViewClient;
import cn.jun.utils.ImageUtil;
import cn.jun.wb.ReWebChomeClient;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

import static android.content.ContentValues.TAG;

public class FeedBack extends Activity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack {
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsg5Plus;

    private RelativeLayout backLayout;
    private WebView wb;
    private TextView error_tv;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        initView();
        initData();
    }

    private void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        wb = (WebView) findViewById(R.id.wb);
        error_tv = (TextView) findViewById(R.id.error_tv);
    }

    private void initData() {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.getSettings().setSupportMultipleWindows(true);
        wb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.setWebChromeClient(new ReWebChomeClient(this));
//        wb.setWebViewClient(new ReWebViewClient());
        wb.setWebViewClient(new FinishWebViewClient(this, handler));
//        wb.loadUrl(Const.FeedBackAPI);
        String url = "http://mapi.gfedu.cn/html/feedback.html?client=android&version=5.0.1&userid="+getUserID();
        wb.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
        }
    }


    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
        mUploadMsg5Plus = filePathCallback;
        showOptions();
    }

    private void showPopupCommnet() {
        View view = LayoutInflater.from(FeedBack.this).inflate(
                R.layout.edit_feed, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setFocusable(true);
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置弹出窗体需要软键盘
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //设置弹出之后的背景透明度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //显示
        popupWindow.update();
        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        RelativeLayout choose = (RelativeLayout) view.findViewById(R.id.choose_im);
        RelativeLayout choose_qx = (RelativeLayout) view.findViewById(R.id.choose_qx);
        choose_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相册
                mSourceIntent = ImageUtil.choosePicture();
                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
            }
        });
    }

    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }.start();
    }


    public void showOptions() {
//        showPopupCommnet();
        //打开相册
        mSourceIntent = ImageUtil.choosePicture();
        startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
    }


    private class ReOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsg5Plus != null) {
                mUploadMsg5Plus.onReceiveValue(null);
                mUploadMsg5Plus = null;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                Log.w("mUploadMsg", " == " + mUploadMsg);
                Log.w("mUploadMsg5Plus", " == " + mUploadMsg5Plus);
                try {
                    if (mUploadMsg != null) {
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.w(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);
                    } else if (mUploadMsg5Plus != null) {
                        Uri[] uris = new Uri[1];
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.w(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        File file = new File(ImageUtil.bitmapToPath(sourcePath));
                        uris[0] = Uri.fromFile(file);
                        if (uris[0] != null) {
                            mUploadMsg5Plus.onReceiveValue(uris);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }


    private int getUserID() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int UserID = LoginPre.getInt("S_ID", 0);
            return UserID;
        } else {
            return 0;
        }

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
