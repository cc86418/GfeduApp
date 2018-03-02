package cn.gfedu.gfeduapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lling.photopicker.PhotoPickerActivity;
import com.lling.photopicker.utils.OtherUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.gfedu.home_pager.BaseFragment;
import cn.gfedu.home_pager.ITabClickListener;
import cn.jun.MyCollection.MyCollection;
import cn.jun.NotificationCenter.NotificatioCenterActivity;
import cn.jun.bean.Const;
import cn.jun.bean.UserPhotoBean;
import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.courseinfo.ui.PublicStaticClass;
import cn.jun.live.MyLiveActivity;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.mysetting.About;
import cn.jun.mysetting.FeedBack;
import cn.jun.mysetting.HelpCenter;
import cn.jun.mysetting.OverClassListActivity;
import cn.jun.mysetting.Setting;
import cn.jun.mysetting.ShiZiPingYi;
import cn.jun.pushMessage.PushMessage_Service;
import cn.jun.utils.Bimp;
import cn.jun.utils.FileUtils;
import cn.jun.utils.HttpUtils;
import cn.jun.utils.PublicFunc;
import cn.jun.view.CircleImageDrawable;
import cn.jun.view.SynchronizationDialog;
import cn.jun.view.SynchronizationDialog_Fail;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.NormalActivity;
import jc.cici.android.atom.ui.order.OrderHomeActivity;
import jc.cici.android.atom.ui.shopCart.ShopCartActivity;
import jc.cici.android.atom.utils.ToolUtils;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends BaseFragment implements ITabClickListener, View.OnClickListener {
    private ImageView name_icon;
    // 创建popWindows 对象
    private PopupWindow pop;
    private LinearLayout ll_popup;
    public static final int TAKE_PICTURE = 1;
    private static final int PICK_PHOTO = 1;
    private int mColumnWidth;
    //点击事件
    private RelativeLayout dingdan_click;
    private RelativeLayout kaquanclick;
    private RelativeLayout gouwuche_click;
    private RelativeLayout zhibo_click;
    private RelativeLayout tiku_click;
    private RelativeLayout huancun_click;
    private RelativeLayout synchronization_click;
    private RelativeLayout yijieshu_click;
    private RelativeLayout jianyifankui_click;
    private RelativeLayout help_click;
    private RelativeLayout about_click;
    private RelativeLayout shoucang_click;
    private RelativeLayout shizipingyi_click;
    //设置
    private ImageButton my_setting;
    //私信
    private ImageButton my_message;
    //同步按钮
    private PopupWindow popupWindow;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //上传头像返回参数
    private UserPhotoBean UserPhoto;
    //进度
    private Dialog mDialog;
    // 处理用户头像
    private Bitmap Head_bitmap;
    //用户信息
    private String userID;
    private String userRealName;
    private String gxqm_str;
    private String userHead;
    private TextView name;
    private TextView gexm;
    //点击登录
    private RelativeLayout sj_Relative;
    private ImageButton sj_icon;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void fetchData() {

    }

    private void initScroll(View view) {
        MyScrollView twoScrollView = (MyScrollView) view.findViewById(R.id.Scrollview);
        twoScrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClass.IsTop = true;
                } else {
                    PublicStaticClass.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_layout, container, false);

        initScroll(view);

        name_icon = (ImageView) view.findViewById(R.id.name_icon);
        name_icon.setOnClickListener(this);
        int screenWidth = OtherUtils.getWidthInPx(getActivity());
        mColumnWidth = (screenWidth - OtherUtils.dip2px(getActivity(), 4)) / 3;
        //控件
        dingdan_click = (RelativeLayout) view.findViewById(R.id.dingdan_click);
        kaquanclick = (RelativeLayout) view.findViewById(R.id.kaquanclick);
        gouwuche_click = (RelativeLayout) view.findViewById(R.id.gouwuche_click);
        huancun_click = (RelativeLayout) view.findViewById(R.id.huancun_click);
        zhibo_click = (RelativeLayout) view.findViewById(R.id.zhibo_click);
        tiku_click = (RelativeLayout) view.findViewById(R.id.tiku_click);
        synchronization_click = (RelativeLayout) view.findViewById(R.id.synchronization_click);
        yijieshu_click = (RelativeLayout) view.findViewById(R.id.yijieshu_click);
        jianyifankui_click = (RelativeLayout) view.findViewById(R.id.jianyifankui_click);
        help_click = (RelativeLayout) view.findViewById(R.id.help_click);
        about_click = (RelativeLayout) view.findViewById(R.id.about_click);
        shoucang_click = (RelativeLayout) view.findViewById(R.id.shoucang_click);
        shizipingyi_click = (RelativeLayout) view.findViewById(R.id.shizipingyi_click);

        //模块点击事件
        dingdan_click.setOnClickListener(this);
        kaquanclick.setOnClickListener(this);
        gouwuche_click.setOnClickListener(this);
        huancun_click.setOnClickListener(this);
        tiku_click.setOnClickListener(this);
        zhibo_click.setOnClickListener(this);
        synchronization_click.setOnClickListener(this);
        yijieshu_click.setOnClickListener(this);
        jianyifankui_click.setOnClickListener(this);
        help_click.setOnClickListener(this);
        about_click.setOnClickListener(this);
        shoucang_click.setOnClickListener(this);
        shizipingyi_click.setOnClickListener(this);

        //姓名
        name = (TextView) view.findViewById(R.id.name);

        gexm = (TextView) view.findViewById(R.id.gexm);

        //设置
        my_setting = (ImageButton) view.findViewById(R.id.my_setting);
        my_setting.setOnClickListener(this);

        //私信
        my_message = (ImageButton) view.findViewById(R.id.my_message);
        if (PushMessage_Service.isNewPush) {
            Log.i("红点新通知", " === ");
            my_message.setBackgroundResource(R.drawable.my_imessage_red);
        } else {
            my_message.setBackgroundResource(R.drawable.my_imessage);
            Log.i("红点", " 没有 ");
        }
        my_message.setOnClickListener(this);

        //登录
        sj_Relative = (RelativeLayout) view.findViewById(R.id.sj_Relative);
        sj_icon = (ImageButton) view.findViewById(R.id.sj_icon);


        //获取用户信息
        GetUserSharePreferences();
        initView();
        return view;
    }

    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        Log.i("LoginPre -- > ", "" + LoginPre);
        Log.i("userID -- > ", "" + ToolUtils.getUserID(getActivity()));

        if (null != LoginPre && !"".equals(LoginPre)) {
            int SID = LoginPre.getInt("S_ID", 0);
            userID = Integer.toString(SID);
            userRealName = LoginPre.getString("S_RealName", "");
            userRealName = userRealName.replace("&nbsp;", " ");
            userHead = LoginPre.getString("S_Head", "");

            gxqm_str = LoginPre.getString("S_Telephone", "");
            gxqm_str = gxqm_str.replace("&nbsp;", " ");
        }

    }

    private void initView() {
        if (!"".equals(userRealName) && null != userRealName) {
            name.setText(userRealName);

            if (!"".equals(gxqm_str) && null != gxqm_str) {
                gexm.setText("");
            } else {
                gexm.setText(gxqm_str);
            }

            sj_Relative.setOnClickListener(null);
            sj_icon.setOnClickListener(null);
            sj_icon.setVisibility(View.GONE);
        } else {
            name.setText("请先登录");
            gexm.setText("");
            sj_Relative.setOnClickListener(this);
            sj_icon.setOnClickListener(this);
            sj_icon.setVisibility(View.VISIBLE);
        }

        if (httpUtils.isNetworkConnected(getActivity())) {
            Head_ImageTask headImg = new Head_ImageTask();
            headImg.execute(userHead);
        }
    }


    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_message:
//                Toast.makeText(getActivity(), "暂时没有私信!", Toast.LENGTH_SHORT).show();
                Intent messageIntent = new Intent(getActivity(), NotificatioCenterActivity.class);
                startActivity(messageIntent);
                break;

            case R.id.my_setting:
                if (isSign()) {
                    Intent settingIntent = new Intent(getActivity(), Setting.class);
                    startActivity(settingIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.name_icon:
                if (isSign()) {
                    //判断读写权限
                    int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // 无权限----
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
                        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, 0);
                        //intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                        startActivityForResult(intent, PICK_PHOTO);
                    }
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            //师资评议
            case R.id.shizipingyi_click:
                Intent sz_intent = new Intent(getActivity(), ShiZiPingYi.class);
                Bundle bundle_sz = new Bundle();
                bundle_sz.putInt("type",1);
                sz_intent.putExtras(bundle_sz);
                startActivity(sz_intent);
                break;

            //我的订单
            case R.id.dingdan_click:
                if (isSign()) {
                    Intent dingdan = new Intent(getActivity(), OrderHomeActivity.class);
                    startActivity(dingdan);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();

                }
                break;

            //我的收藏
            case R.id.shoucang_click:
                if (isSign()) {
                    Intent intentshoucang = new Intent(getActivity(), MyCollection.class);
                    startActivity(intentshoucang);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getActivity(), "暂未开放!", Toast.LENGTH_SHORT).show();
                break;

            //我的卡券
            case R.id.kaquanclick:
//                if(isSign()){
//
//                }else{
//                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
//
//                }
                Toast.makeText(getActivity(), "暂未开放!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), LiveDetailsActivity.class);
//                Intent intent = new Intent(getActivity(), OnlineCourseDetailsAloneActivityTwo.class);
//                startActivity(intent);
                break;

            //我的购物车
            case R.id.gouwuche_click:
                if (isSign()) {
                    Intent intent_gouwuche = new Intent(getActivity(), ShopCartActivity.class);
                    startActivity(intent_gouwuche);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }

                break;

            //我的缓存
            case R.id.huancun_click:
                if (isSign()) {
                    Intent new_cache_Intent = new Intent(getActivity(), ManagerActivity.class);
                    startActivity(new_cache_Intent);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;

            //同步课程
            case R.id.synchronization_click:
//                Synchronization_Dialog();
//                if(isSign()){
//
//                }else{
//                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
//
//                }
                Toast.makeText(getActivity(), "暂未开放!", Toast.LENGTH_SHORT).show();
                break;


            //已结束课程
            case R.id.yijieshu_click:
                if (isSign()) {
                    Intent overClassIntetn = new Intent(getActivity(), OverClassListActivity.class);
                    startActivity(overClassIntetn);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;


            //建议反馈
            case R.id.jianyifankui_click:
                if (isSign()) {
                    Intent FeedBcakIntent = new Intent(getActivity(), FeedBack.class);
                    startActivity(FeedBcakIntent);
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;

            //帮助中心
            case R.id.help_click:
//                Toast.makeText(getActivity(), "暂未开放!", Toast.LENGTH_SHORT).show();
                Intent HelpCenterIntent = new Intent(getActivity(), HelpCenter.class);
                startActivity(HelpCenterIntent);
                break;

            //关于我们
            case R.id.about_click:
                Intent AboutIntent = new Intent(getActivity(), About.class);
                startActivity(AboutIntent);

                break;

            //点击登录布局
            case R.id.sj_Relative:
                Intent layoutIntent = new Intent(getActivity(), NormalActivity.class);
                startActivity(layoutIntent);
                getActivity().finish();
                break;

            //点击登录箭头
            case R.id.sj_icon:
                Intent iconIntent = new Intent(getActivity(), NormalActivity.class);
                startActivity(iconIntent);
                getActivity().finish();
                break;

            //我的直播
            case R.id.zhibo_click:
                Intent zhiboIntent = new Intent(getActivity(), MyLiveActivity.class);
                startActivity(zhiboIntent);
                break;
            //我的题库
            case R.id.tiku_click:
                Intent tikuintent = getActivity().getPackageManager().getLaunchIntentForPackage("jc.cici.android.gfedu");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (tikuintent != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
//                    intent.putExtra("name", "Liu xiang");
//                    intent.putExtra("birthday", "1983-7-13");
                    startActivity(tikuintent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    Toast.makeText(getActivity(), "哟，赶紧下载安装金程学习APP吧", Toast.LENGTH_LONG).show();
                }

//                Intent dianboIntent = new Intent(getActivity(), KLineView.class);
//                startActivity(dianboIntent);

                break;

        }
    }

    private void Synchronization_Dialog() {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.synchronization_class_dialog, null);
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
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //设置弹出之后的背景透明度
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.4f;
        getActivity().getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //显示
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        TextView tb_tv = (TextView) view.findViewById(R.id.tb_tv);
        Button synchronization_btn = (Button) view.findViewById(R.id.synchronization_btn);
        RelativeLayout qx_title = (RelativeLayout) view.findViewById(R.id.qx_relative);
        qx_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        synchronization_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
//                SynchronizationDialog synchronizationDialog = new SynchronizationDialog(getActivity(),"");
//                synchronizationDialog.show();

                final SynchronizationDialog_Fail synchronizationDialog_Fail = new SynchronizationDialog_Fail(getActivity(), "");
                synchronizationDialog_Fail.show();
                synchronizationDialog_Fail.setonClick(new SynchronizationDialog_Fail.ICoallBack() {
                    @Override
                    public void onClickOkButton(String s) {
                        if ("againProgress".equals(s)) {
                            synchronizationDialog_Fail.dismiss();
                            SynchronizationDialog synchronizationDialog = new SynchronizationDialog(getActivity(), "");
                            synchronizationDialog.show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("requestCode = ","" +requestCode);
        Log.i("resultCode = ",""+ resultCode);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                Log.d("返回的图片Url", "" + result.get(0));
                try {
                    Bimp.revitionImageSize(result.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap revitionImageBitmap;
                String revitionImageString = null;
                if (!"".equals(result.get(0)) && null != result.get(0)) {
                    //先做图片处理然后上传
                    try {
                        revitionImageBitmap = Bimp.revitionImageSize(result.get(0));
                        String fileName = String.valueOf(System.currentTimeMillis());
                        FileUtils.saveBitmap(revitionImageBitmap, fileName);
                        revitionImageString = FileUtils.SDPATH + fileName + ".JPEG";
                        Log.i("Bimp === ", "" + revitionImageString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (httpUtils.isNetworkConnected(getActivity())) {
                        showProcessDialog(getActivity(),
                                R.layout.loading_process_dialog_color);
                        UpdateUserPhotoTask Update = new UpdateUserPhotoTask();
                        Update.execute(revitionImageString);
                    }
                }
            }
        }
    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

    }

    class UpdateUserPhotoTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            try {
                String UserDataUrl = arg0[0];
                String S_ID = "";
                SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
                if (null != LoginPre && !"".equals(LoginPre)) {
                    int SID = LoginPre.getInt("S_ID", 0);
                    S_ID = Integer.toString(SID);
                }
                String params = "{" + "'" + "StudentID" + "'" + ":" + "'"
                        + S_ID + "'" + "," + "'" + "func" + "'" + ":"
                        + "'" + "SetUserHeadImg" + "'" + "}";
                String ParamsEncodingString = URLEncoder
                        .encode(params, "utf-8");
                // 参数加密
                String strMD5 = PublicFunc.getMD5Str(params
                        + PublicFunc.MD5_KEY);
                // http请求
                String url = Const.UserPhotoAPI + ParamsEncodingString
                        + "&MD5code=" + strMD5;
                Log.i("UserDataUrl == ","" + UserDataUrl);
                if (!"".equals(UserDataUrl) && null != UserDataUrl) {
                    UserPhoto = httpUtils.getAddPhoto(url, UserDataUrl);
                } else {
                    UserPhoto = httpUtils.getAddPhoto(url, null);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("上传头像返回-----> ", "" + UserPhoto.getResultState() + " , " + UserPhoto.getResultStr());
            if ("1".equals(UserPhoto.getResultState())) {
                Toast.makeText(getActivity(), "保存头像成功", Toast.LENGTH_SHORT).show();
//                ImageLoader.getInstance().display(UserPhoto.getResultStr(), name_icon, mColumnWidth, mColumnWidth);

                // 存储登录信息
                SharedPreferences loginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPre.edit();
                if (!"".equals(UserPhoto.getResultStr())) {
                    loginEditor.putString("S_Head", UserPhoto.getResultStr());
                } else if ("null".equals(UserPhoto.getResultStr())) {
                    loginEditor.putString("S_Head", "");
                } else {
                    loginEditor.putString("S_Head", "");
                }
                loginEditor.commit();

                if (httpUtils.isNetworkConnected(getActivity())) {
                    Head_ImageTask headImg = new Head_ImageTask();
                    headImg.execute(UserPhoto.getResultStr());
                }

            } else {
                Toast.makeText(getActivity(), "保存头像失败,请稍后再试!", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }


    class Head_ImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String Headurl = arg0[0];
            Head_bitmap = Bimp.returnBitMap(Headurl);

            return null;
        }

        protected void onPostExecute(Void result) {
            if (null != Head_bitmap) {
                name_icon.setImageDrawable(new CircleImageDrawable(Head_bitmap));
            }
            super.onPostExecute(result);

        }
    }


    private boolean isSign() {
        SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        Log.i("LoginPre-----> ", "LoginPre " + LoginPre);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int SID = LoginPre.getInt("S_ID", 0);
            Log.i("SID----->  ", "SID " + SID);
            if (0 != SID) {
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

}

