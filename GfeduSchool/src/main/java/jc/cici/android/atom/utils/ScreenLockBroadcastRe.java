package jc.cici.android.atom.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;

/**
 * 监听锁屏,电话，后台广播
 * Created by atom on 2017/7/6.
 */

public class ScreenLockBroadcastRe extends BroadcastReceiver {
    private String action;
    private Context mCtx;
    private Handler mHandler;

    public ScreenLockBroadcastRe(Context ctx, Handler handler) {
        this.mCtx = ctx;
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        action = intent.getAction();
        switch (action) {
            case Intent.ACTION_SCREEN_ON: // 开屏
                mHandler.sendEmptyMessage(3);
                break;
            case Intent.ACTION_SCREEN_OFF: // 锁屏
                mHandler.sendEmptyMessage(4);
                break;
            case TelephonyManager.ACTION_PHONE_STATE_CHANGED: // 电话来点
                doReceivePhone(context,intent);
                break;
            case "com.atom.testexit": // 退出广播
                mHandler.sendEmptyMessage(5);
                break;
            case "com.atom.testleave": // 暂时离广播
                mHandler.sendEmptyMessage(6);
                break;
            case "com.atom.testsuccess": // 试卷成功提交得分后关闭试卷广播
                mHandler.sendEmptyMessage(6);
                break;
            default:
                break;
        }
    }

    /**
     * 处理电话广播.
     *
     * @param context
     * @param intent
     */
    public void doReceivePhone(Context context, Intent intent) {

        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int state = telephony.getCallState();
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING: // 等待接电话状态
                mHandler.sendEmptyMessage(2);
                break;
            case TelephonyManager.CALL_STATE_IDLE: // 电话挂断状态
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: // 通话状态

                break;
        }
    }
}
