package cn.jun.danmu;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jc.cici.android.R;

public class BarrageView extends RelativeLayout{
    /**
     * 最大的移动速度
     */
    private int maxSpeed;
    /**
     * 最小的移动速度
     */
    private int minSpeed;
    /**
     * 最大的字体尺寸
     */
    private float maxTextSize;
    /**
     * 最小的字体尺寸
     */
    private float minTextSize;
    /**
     * 最大的时间间隔
     */
    private int maxInterval;
    /**
     * 最小的时间间隔
     */
    private int minInterval;
    /**
     * 用于随机设置字体颜色
     */
    private Random random = new Random();
    /**
     * 控制是否弹幕
     */
    private boolean barrageState = true;

    private List<String> barrageContents = new ArrayList<>();

    private static final int MESSAGE_SHOWITEM = 1;

    private Handler barrageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (barrageState) {
                    addBarrageItem();
//                    //随机获取时间间隔
//                    int interval = (int) (Math.random() * (maxInterval - minInterval) + minInterval);
//                    sendEmptyMessageDelayed(MESSAGE_SHOWITEM, interval);
                    //固定时间显示弹幕
                    sendEmptyMessageDelayed(MESSAGE_SHOWITEM, 30000);
                }
            }
        }
    };

    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarrageView, defStyleAttr, 0);
//        minSpeed =a.getInt(R.styleable.BarrageView_minSpeed,7000);
//        maxSpeed =a.getInt(R.styleable.BarrageView_maxSpeed,4000);
        minSpeed = a.getInt(R.styleable.BarrageView_minSpeed, 5000);
        maxSpeed = a.getInt(R.styleable.BarrageView_maxSpeed, 5000);
        maxTextSize = a.getDimension(R.styleable.BarrageView_maxTextSize, 5);
        minTextSize = a.getDimension(R.styleable.BarrageView_minTextSize, 5);
        maxInterval = a.getInteger(R.styleable.BarrageView_maxInterval, 30000);
        minInterval = a.getInteger(R.styleable.BarrageView_minInterval, 30000);
        a.recycle();
    }

    /**
     * 开始弹幕
     */
    public void startBarraging() {
        barrageState = true;
        if (!barrageHandler.hasMessages(MESSAGE_SHOWITEM)) {
            barrageHandler.sendEmptyMessage(MESSAGE_SHOWITEM);
        }
    }

    /**
     * 停止弹幕
     */
    public void stopBarraging() {
        barrageState = false;
        barrageHandler.removeMessages(MESSAGE_SHOWITEM);
    }


    /**
     * 设置弹幕内容
     *
     * @param barrageContents 弹幕内容
     */
    public void setBarrageContents(List<String> barrageContents) {
        this.removeAllViews();//移除之前的弹幕
        barrageHandler.removeMessages(MESSAGE_SHOWITEM);///移除之前的消息

        this.barrageContents = barrageContents;
//        barrageHandler.sendEmptyMessageDelayed(MESSAGE_SHOWITEM, 0);
    }

    /**
     * 添加弹幕
     */
    private void addBarrageItem() {
        final TextView textView = new TextView(getContext());
//        int textSize = (int) (Math.random() * (maxTextSize - minTextSize) + minTextSize);
//        textView.setTextSize(textSize);
        textView.setSingleLine(true);
        //随机设置字体颜色
//        textView.setTextColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setTextColor(Color.parseColor("#b3ffcccc"));
//        textView.setTextColor(Color.parseColor("#4dffcccc"));
        textView.setText(barrageContents.get((int) (Math.random() * barrageContents.size())));
        //调用measure确保measuredHeight和measuredWidth能拿到值
        textView.measure(0, 0);
        int speed = (int) (Math.random() * (maxSpeed - minSpeed) + minSpeed);
        int topMargin = (int) (Math.random() * (this.getHeight() - textView.getMeasuredHeight()));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = topMargin;
        this.addView(textView, params);
        TranslateAnimation anim = new TranslateAnimation(this.getWidth(), -textView.getMeasuredWidth(), 0, 0);
        anim.setDuration(speed);
        anim.setFillAfter(true);
        textView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.clearAnimation();
                BarrageView.this.removeView(textView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}
