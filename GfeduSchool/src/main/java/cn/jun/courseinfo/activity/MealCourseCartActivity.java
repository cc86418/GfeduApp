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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.jun.bean.AddClassCartPackageBean;
import cn.jun.bean.Const;
import cn.jun.bean.PackageClassTypeBean;
import cn.jun.bean.PackageProduct;
import cn.jun.courseinfo.course_adapter.J_RecyclerAdapter;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.shopCart.EnsureOrderAc;
import jc.cici.android.atom.ui.shopCart.ShopCartActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.ClickType;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgAddressClick;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgExameClick;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgTypeClick;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.mData;

public class MealCourseCartActivity extends Activity implements View.OnClickListener {
    //套餐ID
    private int packId;
    //套餐选择的班级String
    private String packlist;
    //套餐图片
    private String imageUrl;
    //套餐名称
    private String title;
    //套餐最小价格
    private String priceMin;
    //最大价格
    private String priceMax;
    //用户ID
    private int UserID;
    //套餐类型
    private int packtype;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private ImageView iv_back;
    //listView
    private RecyclerView meal_list;
    //    public static RecyclerView meal_list;
    //适配器
//    private MealCourseAdapter mAdapter;
    //数据源
//    private PackageClassTypeBean packageClassTypeBean = new PackageClassTypeBean();
//    private ArrayList<PackageClassTypeBean> mList;
//    private ArrayList<PackageClassTypeBean> mList;
//        private ArrayList<CommonBean<PackageClassTypeBean>> mList;
    private ArrayList<PackageClassTypeBean.ProductList> bean;
    //适配器
//    private MealCourseAdapter typeAdapter;
    private J_RecyclerAdapter typeAdapter;

    //购物车和立即购买
    private Button addcartBtn, gobuyBtn;
    //数据
    private AddClassCartPackageBean AddClassCartPackage = new AddClassCartPackageBean();
    private ArrayList<AddClassCartPackageBean> AddCartList;
    //返回的数据
    private int ClassType_PKID;
    private int buyway;//购买类型1。加入购物 2。直接购买

    //套餐选中的班级编号
    private PackageProduct packageProduct;
    private ArrayList<PackageProduct> packageProductList = new ArrayList<>();
    //去重
    private Set<PackageProduct> HashSet;
    //标题价格
    private TextView header_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_course_cart);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            packtype = bundle.getInt("packtype");
            packId = bundle.getInt("packId");
            packlist = bundle.getString("packlist");
            imageUrl = bundle.getString("imageUrl");
            title = bundle.getString("title");
            priceMin = bundle.getString("priceMin");
            priceMax = bundle.getString("priceMax");
        }

        Log.i("packtype", "" + packtype);
        if (httpUtils.isNetworkConnected(this)) {
            initData();
        }
    }

    private void initData() {
//        ClassTypeTask classTypeTask = new ClassTypeTask();
//        classTypeTask.execute();
        initDate();
    }

    private void initDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<PackageClassTypeBean>> observable = httpPostService.getPackageClassType(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<PackageClassTypeBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MealCourseCartActivity.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<PackageClassTypeBean> packageClassTypeBean) {
                        if (100 == packageClassTypeBean.getCode()) {
                            if (!"".equals(packageClassTypeBean.getBody().getProductList())
                                    && null != packageClassTypeBean.getBody().getProductList()) {
                                bean = packageClassTypeBean.getBody().getProductList();
//                                mList = new ArrayList<>();
//                                mList.add(packageClassTypeBean);
                                initView();
                            } else {//无数据

                            }
                        }
                    }
                });
    }

    //公共参数
    private RequestBody commParam() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            UserID = LoginPre.getInt("S_ID", 0);
        }

        try {
            // 用户id
            obj.put("userId", UserID);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("packageid", packId);
            if (0 == packtype) {//固定
//                    obj.put("classidlist", classidlist);
            } else {
                if (!"".equals(packlist)) {
                    obj.put("classidlist", packlist);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


//    class ClassTypeTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
//            if (null != LoginPre && !"".equals(LoginPre)) {
//                UserID = LoginPre.getInt("S_ID", 0);
//            }
////            UserID = 26146;
//            if (0 == packtype) {//固定
//                packageClassTypeBean = httpUtils.getpackageClassTypeList(Const.URL + Const.GetPackageClassTypeAPI, UserID, packId, "");
//            } else {
//                packageClassTypeBean = httpUtils.getpackageClassTypeList(Const.URL + Const.GetPackageClassTypeAPI, UserID, packId, packlist);
//            }
//
//            return null;
//        }
//
//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (100 == packageClassTypeBean.getCode()) {
//                if (!"".equals(packageClassTypeBean.getBody().getProductList())
//                        && null != packageClassTypeBean.getBody().getProductList()) {
//                    mList = new ArrayList<>();
//                    mList.add(packageClassTypeBean);
//                    initView();
//                } else {//无数据
//
//                }
//            } else {//接口请求失败
//                Toast.makeText(MealCourseCartActivity.this, packageClassTypeBean.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        meal_list = (RecyclerView) findViewById(R.id.meal_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        meal_list.setLayoutManager(mLayoutManager);
        meal_list.setNestedScrollingEnabled(false);
        meal_list.setHasFixedSize(true);//最重要的这句
//        typeAdapter = new J_RecyclerAdapter(this, mList.get(0).getBody().getProductList(), ReturnId);
        typeAdapter = new J_RecyclerAdapter(this, bean, ReturnId);
        meal_list.setAdapter(typeAdapter);

        setHeader(meal_list);


        //加入购物车
        addcartBtn = (Button) findViewById(R.id.addcartBtn);
        addcartBtn.setOnClickListener(this);
        //立即购买
        gobuyBtn = (Button) findViewById(R.id.gobuyBtn);
        gobuyBtn.setOnClickListener(this);

    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.meal_list_header, view, false);
        typeAdapter.setHeaderView(header);
        ImageView header_im = (ImageView) header.findViewById(R.id.header_im);
        TextView header_content = (TextView) header.findViewById(R.id.header_content);
        header_price = (TextView) header.findViewById(R.id.header_price);
        header_content.setText(title);
        if (priceMin.equals(priceMax)) {
            header_price.setText(priceMax);
        } else {
            header_price.setText(priceMin + " - " + priceMax);
        }
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.pic_kong_banner)
                .into(header_im);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.addcartBtn:
                Gson s = new Gson();
                for (int i = 0; i < mData.size(); i++) {
                    for (int j = 0; j < mData.get(i).getClassTypeList().size(); j++) {
                        if (mData.get(i).getClassTypeList().get(j).isChoose()) {
                            //班级ID
                            int classid = mData.get(i).getClassInfo().getClass_PKID();
                            //套餐产品关联ID
                            int kid = mData.get(i).getLink_PKID();
                            //版型ID
                            int classtypeId = mData.get(i).getClassTypeList().get(j).getClassType_PKID();
                            //限购预购
                            int classBuyType = mData.get(i).getClassTypeList().get(j).getClassType_Type();
                            packageProduct = new PackageProduct();
                            packageProduct.setKid(kid);
                            packageProduct.setClassId(classid);
                            packageProduct.setClassTypeId(classtypeId);
                            packageProduct.setBuyType(classBuyType);
                            packageProductList.add(packageProduct);
                            Log.i("整合数据之后 -- ", "" + s.toJson(packageProductList).toString());
                        }

                    }

                }
                packageProductList = new ArrayList<PackageProduct>(new HashSet<PackageProduct>(packageProductList));
                Log.i("去重复之后 -- ", "" + s.toJson(packageProductList).toString());
                if (J_msgExameClick && J_msgTypeClick) {
                    if ("在线".equals(ClickType)) {
                        if (httpUtils.isNetworkConnected(this)) {
                            if (!"".equals(packageProductList) && null != packageProductList) {
                                //加入购物车
                                addCartAndBuy(1);
                            } else {
                                Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if (J_msgAddressClick) {
                            if (httpUtils.isNetworkConnected(this)) {
                                if (!"".equals(packageProductList) && null != packageProductList) {
                                    //加入购物车
                                    addCartAndBuy(1);
                                } else {
                                    Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(this, "请确认班级信息是否选择完成", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, "请确认班级信息是否选择完成", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.gobuyBtn:
                Gson ss = new Gson();
//                showLogCompletion(s.toJson(mData).toString(), 3000);
                for (int i = 0; i < mData.size(); i++) {
                    for (int j = 0; j < mData.get(i).getClassTypeList().size(); j++) {
                        if (mData.get(i).getClassTypeList().get(j).isChoose()) {
                            //班级ID
                            int classid = mData.get(i).getClassInfo().getClass_PKID();
                            //套餐产品关联ID
                            int kid = mData.get(i).getLink_PKID();
                            //版型ID
                            int classtypeId = mData.get(i).getClassTypeList().get(j).getClassType_PKID();
                            //限购预购
                            int classBuyType = mData.get(i).getClassTypeList().get(j).getClassType_Type();
                            packageProduct = new PackageProduct();
                            packageProduct.setKid(kid);
                            packageProduct.setClassId(classid);
                            packageProduct.setClassTypeId(classtypeId);
                            packageProduct.setBuyType(classBuyType);
                            packageProductList.add(packageProduct);
                            Log.i("整合数据之后 -- ", "" + ss.toJson(packageProductList).toString());
                        }

                    }

                }
                packageProductList = new ArrayList<PackageProduct>(new HashSet<PackageProduct>(packageProductList));
                Log.i("去重复之后 -- ", "" + ss.toJson(packageProductList).toString());

                if (J_msgExameClick && J_msgTypeClick) {
                    if ("在线".equals(ClickType)) {
                        if (httpUtils.isNetworkConnected(this)) {
                            if (!"".equals(packageProductList) && null != packageProductList) {
                                //直接购买
                                addCartAndBuy(2);
                            } else {
                                Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if (J_msgAddressClick) {
                            if (httpUtils.isNetworkConnected(this)) {
                                if (!"".equals(packageProductList) && null != packageProductList) {
                                    //直接购买
                                    addCartAndBuy(2);
                                } else {
                                    Toast.makeText(this, "请确认班级信息是否选择完成!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(this, "请确认班级信息是否选择完成", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, "请确认班级信息是否选择完成", Toast.LENGTH_SHORT).show();
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
            AddClassCartPackage = httpUtils.getAddPackageCart(Const.URL + Const.AddPackageCart, UserID, packId, buyway, packageProductList);
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == AddClassCartPackage.getCode()) {
                if (1 == buyway) {//加入购物车
                    Intent intent = new Intent(MealCourseCartActivity.this, ShopCartActivity.class);
                    startActivity(intent);
                } else if (2 == buyway) {//立即购买
                    int cartIdStr = AddClassCartPackage.getBody().getCartId();
                    Intent intent = new Intent(MealCourseCartActivity.this, EnsureOrderAc.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cartIdStr", cartIdStr + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            } else {//接口请求失败
                Toast.makeText(MealCourseCartActivity.this, AddClassCartPackage.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    private Handler ReturnId = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0:
//                    packageProductList = (ArrayList) msg.obj;
//                    if (!"".equals(packageProductList) && null != packageProductList) {
//                        Gson s = new Gson();
//                        Log.i("传递过来的参数", "" + s.toJson(packageProductList).toString());
//                        packageProductList = new ArrayList<PackageProduct>(new HashSet<PackageProduct>(packageProductList));
//                        Log.i("去重之后的数据111", "" + s.toJson(packageProductList).toString());
//                    }
//                    break;

            }
        }
    };


    public static void showLogCompletion(String log, int showCount) {
        if (log.length() > showCount) {
            String show = log.substring(0, showCount);
//          System.out.println(show);
            Log.i("超过长度 == > ", show + "");
            if ((log.length() - showCount) > showCount) {//剩下的文本还是大于规定长度
                String partLog = log.substring(showCount, log.length());
                showLogCompletion(partLog, showCount);
            } else {
                String surplusLog = log.substring(showCount, log.length());
//              System.out.println(surplusLog);
                Log.i("超过长度 == > ", surplusLog + "");
            }

        } else {
//          System.out.println(log);
            Log.i("超过长度 == > ", log + "");
        }
    }
}
