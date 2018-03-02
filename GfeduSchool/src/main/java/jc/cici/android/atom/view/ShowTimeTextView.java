package jc.cici.android.atom.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 自定义倒计时
 * Created by atom on 2017/11/16.
 */

public class ShowTimeTextView extends AppCompatTextView implements Runnable {

    private boolean run = false; //觉得是否执行run方法
    private int time;
    private Dialog mDialog;
    private Handler handler;

    public ShowTimeTextView(Context context) {
        super(context);
    }

    public ShowTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTime(int time, Dialog dialog, Handler handler) {  //设定初始值
        this.time = time;
        this.mDialog = dialog;
        this.handler = handler;
    }

    public boolean isRun() {
        return run;
    }

    public void beginRun() {
        this.run = true;
        run();
    }

    public void stopRun() {
        this.run = false;
        mDialog.dismiss();
        // 倒计时结束后 发送handle
        handler.sendEmptyMessage(0);
    }

    @Override
    public void run() {
        if (run) {
            ComputeTime();
            this.setText(time + "秒后自动关闭");
            postDelayed(this, 1000);
        } else {
            removeCallbacks(this);
        }
    }

    private void ComputeTime() {
        time--;
        if (time == 0)
            stopRun();
    }
}
