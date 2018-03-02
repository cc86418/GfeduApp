package cn.gfedu.gfeduapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhy.autolayout.AutoLayoutActivity;
import java.io.File;
import cn.jun.bean.CheckhAsBindBean;
import cn.jun.bean.Const;
import cn.jun.bean.UpgradeAppBean;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.utils.HttpUtils;
import cn.jun.view.CacheSet;
import cn.jun.view.CheckAppDialog;
import cn.jun.view.WifiSet;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.live.AllLiveFragment;
import jc.cici.android.atom.ui.study.StudyHomeFrament;
import jc.cici.android.atom.ui.tiku.TiKuHomeFragment;
import jc.cici.android.atom.utils.ToolUtils;

//import jc.cici.android.atom.ui.tiku.TiKuHomeFragment;


public class MainActivity extends AutoLayoutActivity implements View.OnClickListener {
    public static MainActivity MainAc = null;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //检测版本
    private UpgradeAppBean updateApp;
    // 是否锁定返回
    private boolean lockKeyOn = false;
    // 是否绑定过设备号
    private CheckhAsBindBean CheckhAsBindBean;
    private static FragmentTransaction ft;
    private LinearLayout xx_click, xk_click, wd_click;
    private LinearLayout zb_click, tk_click;
    private ImageButton xx_image, xk_image, wd_image;
    private ImageButton zb_image, tk_image;
    private TextView xx_lable, xk_lable, wd_lable;
    private TextView zb_lable, tk_lable;
    //    private RadioButton rb_chat, rb_contacts, rb_discovery;
//    private static DiscoverFragment DiscoverFragment = new DiscoverFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //新的隐藏标题方法
        getSupportActionBar().hide();
        MainAc = this;
        initView();
        if (httpUtils.isNetworkConnected(MainActivity.this)) {
            // 检测版本是否需要更新
            CheckInstallApp();
        }
    }


    private void initView() {
        xx_click = (LinearLayout) findViewById(R.id.xx_click);
        xk_click = (LinearLayout) findViewById(R.id.xk_click);
        zb_click = (LinearLayout) findViewById(R.id.zb_click);
        tk_click = (LinearLayout) findViewById(R.id.tk_click);
        wd_click = (LinearLayout) findViewById(R.id.wd_click);
        xx_click.setOnClickListener(this);
        xk_click.setOnClickListener(this);
        zb_click.setOnClickListener(this);
        tk_click.setOnClickListener(this);
        wd_click.setOnClickListener(this);

        xx_image = (ImageButton) findViewById(R.id.xx_image);
        xk_image = (ImageButton) findViewById(R.id.xk_image);
        zb_image = (ImageButton) findViewById(R.id.zb_image);
        tk_image = (ImageButton) findViewById(R.id.tk_image);
        wd_image = (ImageButton) findViewById(R.id.wd_image);
        xx_image.setOnClickListener(this);
        xk_image.setOnClickListener(this);
        zb_image.setOnClickListener(this);
        tk_image.setOnClickListener(this);
        wd_image.setOnClickListener(this);

        xx_lable = (TextView) findViewById(R.id.xx_lable);
        xk_lable = (TextView) findViewById(R.id.xk_lable);
        zb_lable = (TextView) findViewById(R.id.zb_lable);
        tk_lable = (TextView) findViewById(R.id.tk_lable);
        wd_lable = (TextView) findViewById(R.id.wd_lable);


//        rb_chat = (RadioButton) findViewById(rb_chat);
//        rb_contacts = (RadioButton) findViewById(R.id.rb_contacts);
//        rb_discovery = (RadioButton) findViewById(R.id.rb_discovery);
//        rb_chat.setOnClickListener(this);
//        rb_contacts.setOnClickListener(this);
//        rb_discovery.setOnClickListener(this);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new StudyHomeFrament());
        ft.commit();
        xx_image.setBackgroundResource(R.drawable.ic_tab_contact_h);
        xx_lable.setTextColor(Color.parseColor("#dd5555"));
        xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
        xk_lable.setTextColor(Color.parseColor("#666666"));
        wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
        wd_lable.setTextColor(Color.parseColor("#666666"));
        zb_image.setBackgroundResource(R.drawable.zhibo_n);
        zb_lable.setTextColor(Color.parseColor("#666666"));
        tk_image.setBackgroundResource(R.drawable.tiku_n);
        tk_lable.setTextColor(Color.parseColor("#666666"));

    }


    @Override
    public void onClick(View v) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.xx_click:
                ft.replace(R.id.content, new StudyHomeFrament());
                xx_image.setBackgroundResource(R.drawable.ic_tab_contact_h);
                xx_lable.setTextColor(Color.parseColor("#dd5555"));

                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
                xk_lable.setTextColor(Color.parseColor("#666666"));
                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
                wd_lable.setTextColor(Color.parseColor("#666666"));
                tk_image.setBackgroundResource(R.drawable.tiku_n);
                tk_lable.setTextColor(Color.parseColor("#666666"));
                zb_image.setBackgroundResource(R.drawable.zhibo_n);
                zb_lable.setTextColor(Color.parseColor("#666666"));
                break;

            case R.id.xk_click:
                ft.replace(R.id.content, new DiscoverFragment());
                xk_image.setBackgroundResource(R.drawable.ic_tab_moments_h);
                xk_lable.setTextColor(Color.parseColor("#dd5555"));

                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
                xx_lable.setTextColor(Color.parseColor("#666666"));
                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
                wd_lable.setTextColor(Color.parseColor("#666666"));
                tk_image.setBackgroundResource(R.drawable.tiku_n);
                tk_lable.setTextColor(Color.parseColor("#666666"));
                zb_image.setBackgroundResource(R.drawable.zhibo_n);
                zb_lable.setTextColor(Color.parseColor("#666666"));
                break;

            case R.id.wd_click:
                ft.replace(R.id.content, new ProfileFragment());
                wd_image.setBackgroundResource(R.drawable.ic_tab_profile_h);
                wd_lable.setTextColor(Color.parseColor("#dd5555"));

                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
                xx_lable.setTextColor(Color.parseColor("#666666"));
                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
                xk_lable.setTextColor(Color.parseColor("#666666"));
                tk_image.setBackgroundResource(R.drawable.tiku_n);
                tk_lable.setTextColor(Color.parseColor("#666666"));
                zb_image.setBackgroundResource(R.drawable.zhibo_n);
                zb_lable.setTextColor(Color.parseColor("#666666"));
                break;


            case R.id.zb_click:
                ft.replace(R.id.content, new AllLiveFragment());
                zb_image.setBackgroundResource(R.drawable.zhibo_s);
                zb_lable.setTextColor(Color.parseColor("#dd5555"));

                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
                xx_lable.setTextColor(Color.parseColor("#666666"));
                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
                xk_lable.setTextColor(Color.parseColor("#666666"));
                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
                wd_lable.setTextColor(Color.parseColor("#666666"));
                tk_image.setBackgroundResource(R.drawable.tiku_n);
                tk_lable.setTextColor(Color.parseColor("#666666"));
                break;

            case R.id.tk_click:
                ft.replace(R.id.content, new TiKuHomeFragment());
                tk_image.setBackgroundResource(R.drawable.tiku_s);
                tk_lable.setTextColor(Color.parseColor("#dd5555"));

                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
                xx_lable.setTextColor(Color.parseColor("#666666"));
                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
                xk_lable.setTextColor(Color.parseColor("#666666"));
                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
                wd_lable.setTextColor(Color.parseColor("#666666"));
                zb_image.setBackgroundResource(R.drawable.zhibo_n);
                zb_lable.setTextColor(Color.parseColor("#666666"));

                break;


//            case R.id.xx_image:
//                ft.replace(R.id.content, new StudyHomeFrament());
//                xx_image.setBackgroundResource(R.drawable.ic_tab_contact_h);
//                xx_lable.setTextColor(Color.parseColor("#dd5555"));
//
//                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
//                xk_lable.setTextColor(Color.parseColor("#666666"));
//                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
//                wd_lable.setTextColor(Color.parseColor("#666666"));
//                tk_image.setBackgroundResource(R.drawable.tiku_n);
//                tk_lable.setTextColor(Color.parseColor("#666666"));
//                zb_image.setBackgroundResource(R.drawable.zhibo_n);
//                zb_lable.setTextColor(Color.parseColor("#666666"));
//                break;
//
//            case R.id.xk_image:
//                ft.replace(R.id.content, new DiscoverFragment());
//                xk_image.setBackgroundResource(R.drawable.ic_tab_moments_h);
//                xk_lable.setTextColor(Color.parseColor("#dd5555"));
//
//                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
//                xx_lable.setTextColor(Color.parseColor("#666666"));
//                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
//                wd_lable.setTextColor(Color.parseColor("#666666"));
//                tk_image.setBackgroundResource(R.drawable.tiku_n);
//                tk_lable.setTextColor(Color.parseColor("#666666"));
//                zb_image.setBackgroundResource(R.drawable.zhibo_n);
//                zb_lable.setTextColor(Color.parseColor("#666666"));
//                break;
//
//            case R.id.wd_image:
//                ft.replace(R.id.content, new ProfileFragment());
//                wd_image.setBackgroundResource(R.drawable.ic_tab_profile_h);
//                wd_lable.setTextColor(Color.parseColor("#dd5555"));
//
//                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
//                xx_lable.setTextColor(Color.parseColor("#666666"));
//                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
//                xk_lable.setTextColor(Color.parseColor("#666666"));
//                tk_image.setBackgroundResource(R.drawable.tiku_n);
//                tk_lable.setTextColor(Color.parseColor("#666666"));
//                zb_image.setBackgroundResource(R.drawable.zhibo_n);
//                zb_lable.setTextColor(Color.parseColor("#666666"));
//                break;
//
//            case R.id.zb_image:
//                ft.replace(R.id.content, new AllLiveFragment());
//                zb_image.setBackgroundResource(R.drawable.zhibo_s);
//                zb_lable.setTextColor(Color.parseColor("#dd5555"));
//
//                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
//                xx_lable.setTextColor(Color.parseColor("#666666"));
//                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
//                xk_lable.setTextColor(Color.parseColor("#666666"));
//                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
//                wd_lable.setTextColor(Color.parseColor("#666666"));
//                tk_image.setBackgroundResource(R.drawable.tiku_n);
//                tk_lable.setTextColor(Color.parseColor("#666666"));
//                break;
//
//            case R.id.tk_image:
//                ft.replace(R.id.content, new TiKuFragment());
//                tk_image.setBackgroundResource(R.drawable.tiku_s);
//                tk_lable.setTextColor(Color.parseColor("#dd5555"));
//
//                xx_image.setBackgroundResource(R.drawable.ic_tab_contact);
//                xx_lable.setTextColor(Color.parseColor("#666666"));
//                xk_image.setBackgroundResource(R.drawable.ic_tab_moments);
//                xk_lable.setTextColor(Color.parseColor("#666666"));
//                wd_image.setBackgroundResource(R.drawable.ic_tab_profile);
//                wd_lable.setTextColor(Color.parseColor("#666666"));
//                zb_image.setBackgroundResource(R.drawable.zhibo_n);
//                zb_lable.setTextColor(Color.parseColor("#666666"));
//
//                break;

        }
        ft.commit();
    }


    private class ServiceStartErrorBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.e("保利威视--> ", msg);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && KeyEvent.ACTION_DOWN == keyCode) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean getUserID() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int UserID = LoginPre.getInt("S_ID", 0);
            if (0 == UserID) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (httpUtils.isNetworkConnected(MainActivity.this)) {
            // 检测本机是否绑定过
            CheckBindApp();
        } else {
            if (getUserID()) {
                final CacheSet cache_set = new CacheSet(this);
                cache_set.setCanceledOnTouchOutside(true);
                cache_set.show();
                cache_set.setonClick(new CacheSet.ICoallBack() {
                    @Override
                    public void onClickOkButton(String s) {
                        if ("cacheBtn".equals(s)) {
                            Intent new_cache_Intent = new Intent(MainAc, ManagerActivity.class);
                            startActivity(new_cache_Intent);
                            cache_set.dismiss();
                        }
                    }
                });
            } else {
                final WifiSet wifi_iSet = new WifiSet(this);
                wifi_iSet.show();
                wifi_iSet.setonClick(new WifiSet.ICoallBack() {
                    @Override
                    public void onClickOkButton(String s) {
                        if ("x_btn".equals(s)) {
                            wifi_iSet.dismiss();
                        } else if ("ok_btn".equals(s)) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                            wifi_iSet.dismiss();
                        }
                    }
                });
            }
        }


    }


    /**
     * 检测本机是否绑定过
     */
    public void CheckBindApp() {
        Log.i("App-启动自检===> ", " 检测绑定信息");
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int SID = LoginPre.getInt("S_ID", 0);
            System.out.println("检查绑定userid == " + SID);
            if (0 != SID) {
                if (httpUtils.isNetworkConnected(this)) {
//                    CheckhAsTask CheckhAsTask = new CheckhAsTask();
//                    CheckhAsTask.execute(SID);
                }
            }
        }
    }


    /**
     * 查询是否绑定过
     **/
    class CheckhAsTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            int userId = arg0[0];
            // 获取本机UUID
//            String UUID_str = getUUID();
            String UUID_str = ToolUtils.getUUID(MainActivity.this);
            String UNIX = httpUtils.getTimeStamp();
            String MD5 = httpUtils.getMD5Str2(userId + UNIX);
            CheckhAsBindBean = httpUtils.checkhasbind(
                    Const.CheckHasBindAPI, MD5,
                    Const.CLIENT, userId + "", UNIX, Const.VERSION, Const.APPNAME,
                    UUID_str);

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == CheckhAsBindBean.getCode()) {
                System.out.println("查询是否绑定成功!");
                if (0 == CheckhAsBindBean.getBody().getBindStatus()) {// 0：未被任何设备绑定
                    loginEditorClear();
                    DelectPolyvDownload();
                    DelectSQLDateBase();
                } else if (1 == CheckhAsBindBean.getBody().getBindStatus()) {// 1：已被当前设备绑定
                    Log.i("检测绑定信息===> ", " 已被当前设备绑定");
                } else if (2 == CheckhAsBindBean.getBody().getBindStatus()) {// 2：已被其它设备绑定
                    loginEditorClear();
                    DelectPolyvDownload();
                    DelectSQLDateBase();
                }
            } else {
                Log.i("查询是否绑定失败 === > ", "查询是否绑定失败");
            }

        }
    }


    /**
     * 版本更新检测
     */
    public void CheckInstallApp() {
        Log.i("App-启动自检===> ", " 检测版本更新信息");
        if (httpUtils.isNetworkConnected(this)) {
//            CheckVersionApp mytask = new CheckVersionApp();
//            mytask.execute();
        }

    }

    class CheckVersionApp extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                updateApp = httpUtils.checkifupgradeapp(Const.UpgradeAppAPI);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(updateApp.getCode())) {
                //IsUpgrade	是否需要升级  1：是 0：否
                //IsForceUpdate	是否强制更新APP 1：是 0：否
                //H5Link H5说明地址
                //DownloadUrl 最新APP下载地址 【针对安卓】
                String IsUpgrade = updateApp.getBody().getIsUpgrade();
                String IsForceUpdate = updateApp.getBody().getIsForceUpdate();
                String H5Link = updateApp.getBody().getH5Link();
                String DownloadUrl = updateApp.getBody().getDownloadUrl();

                if ("1".equals(IsUpgrade)) {//需要升级
                    if ("1".equals(IsForceUpdate)) {//需要强制更新
                        CheckAppDialog dialog = new CheckAppDialog(MainActivity.this, "1", H5Link, DownloadUrl);
                        dialog.show();
                    } else {//不需要强制更新
                        CheckAppDialog dialog = new CheckAppDialog(MainActivity.this, "0", H5Link, DownloadUrl);
                        dialog.show();
                    }
                } else {//不需要升级

                }
            } else {
                //无网络,或者500错误,不提示任何信息
            }


        }
    }


    //清除用户保存的个人信息/和上次登录的信息
    private void loginEditorClear() {
        SharedPreferences loginPre;
        SharedPreferences.Editor loginEditor;
        loginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        loginEditor = loginPre.edit();
        loginEditor.clear();
        loginEditor.commit();

        SharedPreferences LastloginPre;
        SharedPreferences.Editor LastloginEditor;
        LastloginPre = getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        LastloginEditor = LastloginPre.edit();
        LastloginEditor.clear();
        LastloginEditor.commit();
    }

    //清除本地已下载文件
    private void DelectPolyvDownload() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {// 获取sd卡
            String path = Environment.getExternalStorageDirectory()
                    + "/".concat("polyvdownload");
            File file = new File(path);
            if (file.exists()) {// 文件路径存在
                DelectFile(file);
            }
        }
    }

    private void DelectFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                DelectFile(f);
            }
            file.delete();
        }

    }

    //清空或者删除数据库表
    private void DelectSQLDateBase() {
        VideoDownloadManager.getInstance().deleteClassTable();
        VideoDownloadManager.getInstance().deleteStageTable();
        VideoDownloadManager.getInstance().deleteDownLoadTable();
    }

    /*重新加载布局*/
    public static void reLoadFragView() {
        /*从FragmentManager中移除*/
        MainAc.getSupportFragmentManager().beginTransaction().remove(new DiscoverFragment()).commit();

        ft = MainAc.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new DiscoverFragment());
        ft.commit();
    }

}
