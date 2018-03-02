package cn.jun.menory.manage_activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jc.cici.android.R;

import static jc.cici.android.R.id.tv_manager;

public class ManagerActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView title;
    private TextView tvManager;
    private ImageView ivDelete;
    private RadioButton rbCaching;
    private RadioButton rbCached;
    private ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<>();
    //删除视频按钮
    private PopupWindow popupWindow;
    //进度
    private static Dialog mDialog;
    private static final int DeleteVideoOK = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity);
        getSupportActionBar().hide();
        initView();

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.title);
        tvManager = (TextView) findViewById(tv_manager);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        rbCaching = (RadioButton) findViewById(R.id.rb_caching);
        rbCached = (RadioButton) findViewById(R.id.rb_cached);
        vp = (ViewPager) findViewById(R.id.vp);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                if (ivDelete.getVisibility() == View.VISIBLE) {
                    // 取消
                    ivDelete.setVisibility(View.GONE);
                    text = "管理";
                    cancelCacheManager();
                } else {
                    // 管理
                    ivDelete.setVisibility(View.VISIBLE);
                    text = "取消";
                    doCacheManager();
                }
                tvManager.setText(text);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteCacheView();
            }
        });

        rbCaching.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vp.setCurrentItem(0);
                }
            }
        });
        rbCached.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vp.setCurrentItem(1);
                }
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (0 == position) {
                    tvManager.setVisibility(View.GONE);
                } else if (1 == position) {
                    tvManager.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (!rbCaching.isChecked()) {
                    rbCaching.setChecked(position == 0);
                }
                if (!rbCached.isChecked()) {
                    rbCached.setChecked(position == 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentList = new ArrayList<>();
        fragmentList.add(new VideoCachingFragment());
        fragmentList.add(new VideoCachedFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList == null ? 0 : fragmentList.size();
            }
        });
    }

    // 显示删除视图
    private void showDeleteCacheView() {
        final VideoCachedFragment videoCachedFragment = (VideoCachedFragment) fragmentList.get(1);
        videoCachedFragment.getTotalVideosToDelete();
        int sum = videoCachedFragment.getTotalVideosToDelete();
        if (sum == 0) {
            Toast.makeText(this, "请选中要删除的视频", Toast.LENGTH_LONG).show();
            return;
        }

        //底部弹出对话框
        View view = LayoutInflater.from(this).inflate(
                R.layout.cache_bottom_dialog, null);
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
        // 设置点击窗口外边窗口不消失
        popupWindow.setOutsideTouchable(false);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
       //  update by atom 2017-8-29
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(view, 0, 0);
        } else {
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, view.getHeight() + getStatusBarHeight());
        }
        //设置弹出之后的背景透明度
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.4f;
        this.getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //显示
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        TextView bottom_delete_tv = (TextView) view.findViewById(R.id.bottom_delete_tv);
        bottom_delete_tv.setText(getString(R.string.delete_tip, sum));
        RelativeLayout bottom_delete_relative = (RelativeLayout) view.findViewById(R.id.bottom_delete_relative);
        RelativeLayout qx_relative = (RelativeLayout) view.findViewById(R.id.qx_relative);
        bottom_delete_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProcessDialog(ManagerActivity.this,
                        R.layout.loading_show_dialog_color);
                videoCachedFragment.doDeleteVideos();
                popupWindow.dismiss();
            }
        });
        qx_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }


    /**
     * 获取状态栏高度
     *  update by atom 2017-8-29
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Resources resource = getApplication().getResources();
            int resourceId = resource.getIdentifier("status_bar_height", "dimen", "Android");
            if (resourceId != 0) {
                return resource.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
        }
        return 0;
    }


    // 显示管理试图
    private void doCacheManager() {
        VideoCachedFragment videoCachedFragment = (VideoCachedFragment) fragmentList.get(1);
        videoCachedFragment.showManagerView(true);
    }

    // 取消管理
    private void cancelCacheManager() {
        VideoCachedFragment videoCachedFragment = (VideoCachedFragment) fragmentList.get(1);
        videoCachedFragment.showManagerView(false);
    }


    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }


    public static Handler DeleteVideoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DeleteVideoOK:
                    mDialog.dismiss();
                    break;

            }
        }
    };
}
