package cn.jun.courseinfo.activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.jun.bean.AddClassCartPackageBean;
import cn.jun.bean.ClassClassTypeBean;
import cn.jun.bean.Const;
import cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter;
import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.courseinfo.ui.PublicStaticClass;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.shopCart.EnsureOrderAc;
import jc.cici.android.atom.ui.shopCart.ShopCartActivity;

import static cn.gfedu.gfeduapp.GfeduApplication.application;
import static cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter.ClickType;
import static cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter.msgAddressClick;
import static cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter.msgExameClick;
import static cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter.msgTypeClick;

public class ClassCourseCartActivity extends Activity implements View.OnClickListener {
    //班级ID
    private int classId;
    //班级下版型图片
    private String imageUrl;
    //班级下版型名称
    private String title;
    //班级下版型价格
    private String price;
    //用户ID
    private int UserID;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private ImageView iv_back;
    //listView
    private RecyclerView class_list;
    //listView头布局
    private View HeaderView;
    //适配器
//    private ClassCourseAdapter mAdapter;
    //数据源
    private ClassClassTypeBean classTypeBean = new ClassClassTypeBean();
    private ArrayList<ClassClassTypeBean> mList;
    //加入购物车
    private Button addcartBtn;
    //立即购买
    private Button gobuyBtn;
    //数据
    private AddClassCartPackageBean AddClassCartPackage = new AddClassCartPackageBean();
    private ArrayList<AddClassCartPackageBean> AddCartList;
    //返回的数据
    private int ClassType_PKID;
    private int buyway;//购买类型1。加入购物 2。直接购买
    //标题价格
    private TextView header_price;
    //传递过来的价格
    private String Sale_price;
    //传递过来的类型
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_course_cart2);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            classId = bundle.getInt("ClassId");
            Log.i("ClassId ---- >  ", "" + classId);
            imageUrl = bundle.getString("imageUrl");
            title = bundle.getString("title");
            price = bundle.getString("price");
            type = bundle.getString("type");
        }

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        MyScrollView twoScrollView = (MyScrollView) findViewById(R.id.twoScrollview);
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

        if (httpUtils.isNetworkConnected(this)) {
            initData();
        }
    }

    private void initData() {
        ClassTypeTask classTypeTask = new ClassTypeTask();
        classTypeTask.execute();
    }

    class ClassTypeTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
//                UserID = Integer.toString(SID);
            }
//            classId = 35;
//            classId = 36;
            classTypeBean = httpUtils.getclassclasstypelist(Const.URL + Const.GetClassClassTypeAPI, UserID, classId);
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == classTypeBean.getCode()) {
                if (!"".equals(classTypeBean.getBody().getClassTypeList())
                        && null != classTypeBean.getBody().getClassTypeList()) {
                    mList = new ArrayList<>();
                    mList.add(classTypeBean);
                    initView();
                } else {//无数据

                }
            } else {//接口请求失败
                Toast.makeText(ClassCourseCartActivity.this, classTypeBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initView() {
        ImageView header_im = (ImageView) findViewById(R.id.header_im);
        Glide.with(application.getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.pic_kong_xiangqing)
                .into(header_im);
        TextView header_content = (TextView) findViewById(R.id.header_content);
        header_price = (TextView) findViewById(R.id.header_price);
        header_content.setText(title);
        header_price.setText(price);
        class_list = (RecyclerView) findViewById(R.id.class_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        class_list.setLayoutManager(mLayoutManager);
        class_list.setNestedScrollingEnabled(false);
        ClassCourseRecyclerAdapter typeAdapter = new ClassCourseRecyclerAdapter(this, mList, ReturnId);
        class_list.setAdapter(typeAdapter);

        //加入购物车
        addcartBtn = (Button) findViewById(R.id.addcartBtn);
        addcartBtn.setOnClickListener(this);
        //立即购买
        gobuyBtn = (Button) findViewById(R.id.gobuyBtn);
        gobuyBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.addcartBtn:
                if ("live".equals(type)) {
                    Toast.makeText(this, "直播课程无法加入购物车", Toast.LENGTH_SHORT).show();
                } else {
                    if (msgExameClick && msgTypeClick) {
                        if ("在线".equals(ClickType)) {
                            if (httpUtils.isNetworkConnected(this)) {
                                Log.i("最后确认的classid == ", "" + ClassType_PKID);
                                if (0 != ClassType_PKID) {
                                    //加入购物车
                                    addCartAndBuy(1);
                                } else {
                                    Toast.makeText(this, "请确认班级信息是否选择完成~", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.i("type ==== ","" + type);
                            if ("live".equals(type)) {
                                if (httpUtils.isNetworkConnected(this)) {
                                    Log.i("最后确认的classid == 222 ", "" + ClassType_PKID);
                                    if (0 != ClassType_PKID) {
                                        //加入购物车
                                        addCartAndBuy(1);
                                    } else {
                                        Toast.makeText(this, "请确认班级信息是否选择完成~~", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Log.i("type ==== 2 ","" + type);
                                Log.i("type ==== 3 ","" + msgAddressClick);
//                                if (msgAddressClick) {
                                    if (httpUtils.isNetworkConnected(this)) {
                                        Log.i("最后确认的classid == 222 ", "" + ClassType_PKID);
                                        if (0 != ClassType_PKID) {
                                            //加入购物车
                                            addCartAndBuy(1);
                                        } else {
                                            Toast.makeText(this, "请确认班级信息是否选择完成~~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
//                                } else {
//                                    Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
//                                }
                            }

                        }

                    } else {
                        Toast.makeText(this, "请确认班级信息是否选择完成!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.gobuyBtn:
                if (msgExameClick && msgTypeClick) {
                    if ("在线".equals(ClickType)) {
                        if (httpUtils.isNetworkConnected(this)) {
                            Log.i("最后确认的classid == ", "" + ClassType_PKID);
                            if (0 != ClassType_PKID) {
                                //立即购买
                                addCartAndBuy(2);
                            } else {
                                Toast.makeText(this, "请确认班级信息是否选择完成~", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if ("live".equals(type)) {
                            if (httpUtils.isNetworkConnected(this)) {
                                Log.i("最后确认的classid == 222 ", "" + ClassType_PKID);
                                if (0 != ClassType_PKID) {
                                    //加入购物车
                                    addCartAndBuy(2);
                                } else {
                                    Toast.makeText(this, "请确认班级信息是否选择完成~~", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.i("live -- "," dizhi "+ msgAddressClick);
//                            if (msgAddressClick) {
                                if (httpUtils.isNetworkConnected(this)) {
                                    Log.i("最后确认的classid == 222 ", "" + ClassType_PKID);
                                    if (0 != ClassType_PKID) {
                                        //加入购物车
                                        addCartAndBuy(2);
                                    } else {
                                        Toast.makeText(this, "请确认班级信息是否选择完成~~", Toast.LENGTH_SHORT).show();
                                    }
                                }
//                            } else {
//                                Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "请确认班级信息是否选择完成!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addCartAndBuy(int buyway) {
        AddCartAndBuyTask addCartTask = new AddCartAndBuyTask();
        addCartTask.execute(buyway);
    }

    class AddCartAndBuyTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            buyway = arg0[0];
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
//            UserID = 26146;
//            classId = 35;
//            classId = 36;
            AddClassCartPackage = httpUtils.getAddClassCart(Const.URL + Const.AddClassCartAPI, UserID, classId, ClassType_PKID, buyway);
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == AddClassCartPackage.getCode()) {
                if (1 == buyway) {//加入购物车
                    Intent intent = new Intent(ClassCourseCartActivity.this, ShopCartActivity.class);
                    startActivity(intent);
                } else if (2 == buyway) {//立即购买
                    int cartIdStr = AddClassCartPackage.getBody().getCartId();
                    Intent intent = new Intent(ClassCourseCartActivity.this, EnsureOrderAc.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cartIdStr", cartIdStr + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            } else {//接口请求失败
                Toast.makeText(ClassCourseCartActivity.this, AddClassCartPackage.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Handler ReturnId = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ClassType_PKID = (Integer) msg.obj;
                    Log.i("ClassType_PKID === > ", "" + ClassType_PKID);
                    break;

                case 1:
                    Sale_price = (String) msg.obj;
                    if (!"".equals(Sale_price) && null != Sale_price) {
                        header_price.setText(Sale_price);
                    }
                    break;
            }
        }
    };


}
