package cn.jun.LogoPager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.gfedu.gfeduapp.MainActivity;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class ViewPagerAdapter extends PagerAdapter {
    // 界面列表
    private List<View> views;
    private Activity activity;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    public ViewPagerAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    // 销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1), 0);
//        if (arg1 == views.size() - 1) {
            ImageView mStartWeiboImageButton = (ImageView) arg0
                    .findViewById(R.id.iv_start_weibo);
            mStartWeiboImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 设置已经引导
                    setGuided();
                    goHome();

                }

            });
//        }
        return views.get(arg1);
    }

    private void goHome() {
        // 跳转
        // Intent intent = new Intent(activity, IndexActivity_two.class);
        // activity.startActivity(intent);
        // activity.finish();

        SharedPreferences Preferences = activity.getSharedPreferences(
                Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        int S_ID = Preferences.getInt("S_ID", 0);
        String S_Name = Preferences.getString("S_Name", "");
        String S_RealName = Preferences.getString("S_RealName", "");
        String S_Head = Preferences.getString("S_Head", "");

        if (0!=S_ID) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
//            Intent intent = new Intent(activity, LogingActivit.class);
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
