package cn.jun.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static android.R.attr.startX;
import static android.R.attr.startY;

public class LoadMoreViewPager extends ViewPager {
    public LoadMoreViewPager(Context context) {
        super(context);
    }

    public LoadMoreViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("dispatchTouchEvent", "===MyViewPager MotionEvent.ACTION_DOWN===");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("dispatchTouchEvent", "===MyViewPager MotionEvent.ACTION_MOVE===");
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {//左右滑动
                    if (endX > startX) {//右滑
//                        Log.i("dispatchTouchEvent", "===MyViewPager 右滑");
                        if (getCurrentItem() == 0) {
                            //第一个页面，需要父控件拦截
                            //getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {//左滑
//                        Log.i("dispatchTouchEvent", "===MyViewPager 左滑");
                        if (getCurrentItem() == getAdapter().getCount() - 1) {
                            //最后一个页面，不拦截滑动事件，这样父类不在滑动到下一个页面
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                } else {//上下滑动，需要父控件拦截
//                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
//                Log.i("dispatchTouchEvent", "===MyViewPager MotionEvent.ACTION_UP===");
                break;
            case MotionEvent.ACTION_CANCEL:
//                Log.i("dispatchTouchEvent", "===MyViewPager MotionEvent.ACTION_CANCEL===");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
