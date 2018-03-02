package cn.jun.courseinfo.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaychan.library.ExpandableLinearLayout;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.bean.Const;
import cn.jun.bean.EaluateBean;
import cn.jun.bean.PackageDetailBean;
import cn.jun.bean.ProductBean;
import cn.jun.bean.ProductCollection;
import cn.jun.courseinfo.adapter.SimpleFragmentPagerAdapter;
import cn.jun.courseinfo.fragment.CourseDetailsFragment;
import cn.jun.courseinfo.fragment.SyllabusFragment;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.utils.HttpUtils;
import cn.jun.view.CacheSet;
import cn.jun.view.LoginDialog;
import cn.jun.view.WifiSet;
import cn.jun.view.ZeroBuyDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.NormalActivity;

import static cn.gfedu.gfeduapp.MainActivity.MainAc;


/**
 * 套餐详情
 **/
public class OnlineCourseDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //顶部H5或者图片显示
    private ImageView topIm;
    private WebView topWb;
    private String LoaclH5 = "";
    private String LoaclImageUrl = "";
    private String Package_MobileIsVideo;
    //进度
    private static Dialog mDialog;
    //分享
    private LinearLayout good_cshare_llyt;
    //收藏
    private LinearLayout good_collection_llyt;
    private ImageView collect_icon;
    private TextView collect_tv;
    private ProductCollection productCollection = new ProductCollection();
    private int CollectionType;
    private int type;
    //客服
    private LinearLayout server_linear;
    private ImageView server_im;
    private TextView server_tv;
    //购物车
    private LinearLayout addshopClick;
    //购买
    private LinearLayout gobuyClick;
    private PopupWindow popupWindow;
    private ArrayList<Fragment> list_fragment;  //定义要装fragment的列表
    public static ArrayList<String> list_title;  //定义要装fragment的列表
    //课程详情/数据源
//    private CourseDetailsFragment mOneFragment;
    public static String CourseDetailsFragmentContent = "";
    public String data = "";
    public ArrayList<PackageDetailBean.Body.ClassList> dataList;
    //课程大纲/数据源
//    private SyllabusFragment mTwoFragment;
    public static ArrayList<PackageDetailBean.Body.ClassList> SyllabusDetailsFragmentContent;
    //常见问题/数据源
//    private SyllabusConsultationFragment mThreeFragment;
    private EaluateBean ealuateBean = new EaluateBean();
    public static ArrayList<EaluateBean> ealuateList;
    //Fragemet适配器导航
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private PackageDetailBean packageDetailBean = new PackageDetailBean();
    private ArrayList<PackageDetailBean> packageDetailList = new ArrayList<>();
    private ArrayList<PackageDetailBean.Body.ClassList> classLists = new ArrayList<>();
    private ViewHolder viewHolder;
    private ProductBean productBean;
    private ArrayList<ProductBean> productList = new ArrayList<>();
    //传递的套餐ID
    private int Product_PKID;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //是否有免费试听
    private int Class_OutlineFreeState;

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (httpUtils.isNetworkConnected(this)) {
//            showProcessDialog(LiveDetailsActivity.this,
//                    R.layout.loading_show_dialog_color);
//            initData();
//        } else {//无网络不能进入
//            finish();
//        }
        if (httpUtils.isNetworkConnected(this)) {
            if (!"".equals(Package_MobileIsVideo) && null != Package_MobileIsVideo) {
                //H5视频播放地址或者图片显示地址
                topIm = (ImageView) findViewById(R.id.course_details_top_im);
                topWb = (WebView) findViewById(R.id.course_details_top_wb);
                final TextView topTv = (TextView) findViewById(R.id.course_details_top_tv);
                topWb.getSettings().setJavaScriptEnabled(true);
                topWb.getSettings()
                        .setDefaultTextEncodingName("UTF-8");
                topWb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                topWb.getSettings().setSupportMultipleWindows(true);
                Package_MobileIsVideo = packageDetailList.get(0).getBody().getPackage().getPackage_MobileIsVideo();
                if ("1".equals(Package_MobileIsVideo)) {//是视频
                    topWb.setVisibility(View.VISIBLE);
                    topIm.setVisibility(View.GONE);
                    String H5Likn = packageDetailList.get(0).getBody().getPackage().getH5VideoLink();
                    Log.i("H5Likn == ", "" + H5Likn);
                    topWb.loadUrl(H5Likn);
                    topTv.setVisibility(View.VISIBLE);
                    topWb.setWebViewClient(new WebViewClient() {
                        //覆盖shouldOverrideUrlLoading 方法
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    topWb.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            topTv.setVisibility(View.GONE);
                        }
                    });
                } else if ("0".equals(Package_MobileIsVideo)) {//不是视频,加载图片
                    topIm.setVisibility(View.VISIBLE);
                    topWb.setVisibility(View.GONE);
                    topTv.setVisibility(View.GONE);
                    String Package_MobileValue = packageDetailList.get(0).getBody().getPackage().getPackage_MobileValue();
                    Log.i("Image_Likn == ", "" + Package_MobileValue);
                    Glide.with(OnlineCourseDetailsActivity.this)
                            .load(Package_MobileValue)
                            .placeholder(R.drawable.pic_kong)
                            .into(topIm);
                }
            }
        } else {//无网络不能进入
            if (getUserID_boolean()) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details_activity);
        //新的隐藏标题方法
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            Product_PKID = bundle.getInt("Product_PKID", 0);
        }

        if (httpUtils.isNetworkConnected(this)) {
            showProcessDialog(OnlineCourseDetailsActivity.this,
                    R.layout.loading_show_dialog_color);
            initData();
        } else {//无网络不能进入
            finish();
            Toast.makeText(this, "网络连接中断,稍后再试!", Toast.LENGTH_SHORT).show();
        }

//        UltimateBar ultimateBar = new UltimateBar(this);
//        ultimateBar.setImmersionBar();
    }


    private void initData() {
        PackageDetailTask packageDetail = new PackageDetailTask();
        packageDetail.execute(Product_PKID);

        //常见问题请求
        EaluateListTask ealuateTask = new EaluateListTask();
        ealuateTask.execute(Product_PKID);
    }

    class PackageDetailTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int packageId = params[0];
            try {
                packageDetailBean = httpUtils.getPackageDetailBean(Const.URL + Const.GetPackageDetailAPI, getUserID(), packageId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == packageDetailBean.getCode()) {
//                Log.i("数量======== ", "" + packageDetailBean.getBody().getPackage().getClassListCount());
//                Log.i("ID ======== ", "" + packageDetailBean.getBody().getPackage().getPackage_PKID());
                if (0 == packageDetailBean.getBody().getClassListCount() || 0 == packageDetailBean.getBody().getPackage().getPackage_PKID()) {
                    //过期下架的套餐
                    Toast.makeText(OnlineCourseDetailsActivity.this, "该套餐已经下架!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    packageDetailList.add(packageDetailBean);
                    initTop_Include();
                    initBottom_IncludeView();
                    initBottomView();
                }

            }
            mDialog.dismiss();

        }
    }

    class EaluateListTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int packageId = params[0];
            try {
                ealuateBean = httpUtils.getEaluateList(Const.URL + Const.GetEaluateAPI, getUserID(), 0, packageId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == ealuateBean.getCode()) {
                if (ealuateBean.getBody().getECount() > 0) {
                    ealuateList = new ArrayList<>();
                    ealuateList.add(ealuateBean);
                }

            }


        }
    }

    private void initTop_Include() {
        //功能返回键
        ImageView top_back = (ImageView) findViewById(R.id.top_back);
        top_back.setOnClickListener(this);
        RelativeLayout top_back_RelativeLayout = (RelativeLayout) findViewById(R.id.top_back_RelativeLayout);
        top_back_RelativeLayout.setOnClickListener(this);
        //H5视频播放地址或者图片显示地址
        topIm = (ImageView) findViewById(R.id.course_details_top_im);
        topWb = (WebView) findViewById(R.id.course_details_top_wb);
        final TextView topTv = (TextView) findViewById(R.id.course_details_top_tv);
        topWb.getSettings().setJavaScriptEnabled(true);
        topWb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        topWb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        topWb.getSettings().setSupportMultipleWindows(true);
        Package_MobileIsVideo = packageDetailList.get(0).getBody().getPackage().getPackage_MobileIsVideo();

        if ("1".equals(Package_MobileIsVideo)) {//是视频
            topWb.setVisibility(View.VISIBLE);
            topIm.setVisibility(View.GONE);
            String H5Likn = packageDetailList.get(0).getBody().getPackage().getH5VideoLink();
            LoaclH5 = H5Likn;
            Log.i("H5Likn == ", "" + H5Likn);
            topWb.loadUrl(H5Likn);
            topTv.setVisibility(View.VISIBLE);
            topWb.setWebViewClient(new WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            topWb.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    topTv.setVisibility(View.GONE);
                }
            });
        } else if ("0".equals(Package_MobileIsVideo)) {//不是视频,加载图片
            topIm.setVisibility(View.VISIBLE);
            topWb.setVisibility(View.GONE);
            topTv.setVisibility(View.GONE);
            String Package_MobileValue = packageDetailList.get(0).getBody().getPackage().getPackage_MobileValue();
            LoaclImageUrl = Package_MobileValue;
            Log.i("Image_Likn == ", "" + Package_MobileValue);
            Glide.with(OnlineCourseDetailsActivity.this)
                    .load(Package_MobileValue)
                    .placeholder(R.drawable.pic_kong)
                    .into(topIm);
        }

        //标题
        String titleList = packageDetailList.get(0).getBody().getPackage().getPackage_Name();
        titleList = titleList.replace("&nbsp;", " ");
        TextView course_details_title = (TextView) findViewById(R.id.course_details_title);
        course_details_title.setText(titleList);
        //子标题
        TextView course_details_intoName = (TextView) findViewById(R.id.course_details_intoName);
        String intoNameList = packageDetailList.get(0).getBody().getPackage().getPackage_IntroTitle();
        String intoList = packageDetailList.get(0).getBody().getPackage().getPackage_Intro();
        Log.i("intoNameList ", "" + intoNameList);
        Log.i("intoList", "" + intoList);
        if (("".equals(intoNameList) || null == intoNameList) && ("".equals(intoList) || null == intoList)) {
            course_details_intoName.setVisibility(View.GONE);
        } else {
            course_details_intoName.setVisibility(View.VISIBLE);
        }
        intoNameList = intoNameList.replace("&nbsp;", " ");
        intoList = intoList.replace("&nbsp;", " ");
        if (!"".equals(intoNameList) && null != intoNameList) {
            intoNameList = "<font color='#DD5555'>" + "[ " + intoNameList + " ]"
                    + "</font>";
        } else {
            intoNameList = "";
        }
        if (!"".equals(intoList) && null != intoList) {
            intoList = "<font color='#999999'>" + intoList + "</font>";
        } else {
            intoList = "";
        }
        course_details_intoName.setText(Html.fromHtml(intoNameList + "  " + intoList));
//        if (!"".equals(intoList) && null != intoList) {
//            String htmlText = "<font color='#DD5555'>" + "[ " + intoNameList + " ]"
//                    + "</font>" + "  " + "<font color='#999999'>" + intoList + "</font>";
//            TextView course_details_intoName = (TextView) findViewById(R.id.course_details_intoName);
//            course_details_intoName.setText(Html.fromHtml(htmlText));
//        } else {
//            TextView course_details_intoName = (TextView) findViewById(R.id.course_details_intoName);
//            course_details_intoName.setText("");
//        }

        //现价
        String MinSalePrice = packageDetailList.get(0).getBody().getPackage().getPackage_MinSalePrice();
        String MaxSalePrice = packageDetailList.get(0).getBody().getPackage().getPackage_MaxSalePrice();
        TextView course_details_newprice = (TextView) findViewById(R.id.course_details_newprice);
        if (MinSalePrice.equals(MaxSalePrice)) {
            course_details_newprice.setText("¥" + MinSalePrice);
        } else {
            course_details_newprice.setText("¥" + MinSalePrice + " - " + MaxSalePrice);
        }
        //原价
        String MinPrice = packageDetailList.get(0).getBody().getPackage().getPackage_MinPrice();
        String MaxPrice = packageDetailList.get(0).getBody().getPackage().getPackage_MaxPrice();
        TextView course_details_oldprice = (TextView) findViewById(R.id.course_details_oldprice);
        if (MinPrice.equals(MaxPrice)) {
            course_details_oldprice.setText("¥" + MinPrice);
        } else {
            course_details_oldprice.setText("¥" + MinPrice + " - " + MaxPrice);
        }
        course_details_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        //四选一布局
        LinearLayout CutList_Relative = (LinearLayout) findViewById(R.id.CutList_Relative);
        ArrayList<String> CutList = packageDetailList.get(0).getBody().getCutList();
        TextView course_details_choose_tv = (TextView) findViewById(R.id.course_details_choose_tv);
        String CutString = "";
        if (!"".equals(CutList) && null != CutList) {
            for (int i = 0; i < CutList.size(); i++) {
                CutString = CutString + CutList.get(i) + "   ";
            }
            CutList_Relative.setVisibility(View.VISIBLE);
            course_details_choose_tv.setText(CutString);
            Log.i("CutString ", "" + CutString);
        } else {
            CutList_Relative.setVisibility(View.GONE);
        }
        //餐套班级选择
        //先查看是固定，还是自由组合(0：固定套餐 1：自由组合套餐)
        int Package_Type = packageDetailList.get(0).getBody().getPackage().getPackage_Type();
        final ExpandableLinearLayout ellProduct = (ExpandableLinearLayout) findViewById(R.id.ell_product);
        ellProduct.removeAllViews();//清除所有的子View（避免重新刷新数据时重复添加）
        if (!"".equals(packageDetailList.get(0).getBody().getClassList()) && null != packageDetailList.get(0).getBody().getClassList()) {
            classLists = packageDetailList.get(0).getBody().getClassList();
            int classListSize = packageDetailList.get(0).getBody().getClassList().size();
            for (int i = 0; i < classListSize; i++) {
                View view = View.inflate(this, R.layout.item_product, null);
                productBean = new ProductBean(classLists.get(i).getClass_Name(), classLists.get(i).getLink_ProductID());
                viewHolder = new ViewHolder(view, productBean);
                viewHolder.refreshUI();
                ellProduct.addItem(view);//添加子条目
                productList.add(productBean);
            }

        }
        //0：固定套餐
        if (0 == Package_Type) {
            Log.i("固定 === ", "不可点击--");
            int size = productList.size();
            for (int i = 0; i < size; i++) {
                ProductBean pb = productList.get(i);
                pb.ischeck = false;
                ellProduct.getChildAt(i).findViewById(R.id.checkbox_im).setBackgroundResource(R.drawable.ellproduct_s);
            }
            ellProduct.setOnItemClickListener(new ExpandableLinearLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            });
        } else if (1 == Package_Type) {
            Log.i("自由组合套餐 === ", "可点击--");
            //1：自由组合套餐
            ellProduct.setOnItemClickListener(new ExpandableLinearLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                Toast.makeText(LiveDetailsActivity.this, classLists.get(position).getClass_Name(), Toast.LENGTH_SHORT).show();
                    Log.i("点击1", "" + classLists.get(position).getClass_Name());
                    Log.i("点击2", "" + classLists.get(position).getClass_PKID());
                    Gson s = new Gson();
                    Log.i("ProductBean", "" + s.toJson(productList).toString());
                    int productPKID = productList.get(position).getPkid();
                    // 更改选中状态并
                    int size = productList.size();
                    int classPKID = classLists.get(position).getLink_ProductID();
                    for (int i = 0; i < size; i++) {
                        ProductBean pb = productList.get(i);
                        if (pb != null && pb.getPkid() == classPKID) {
                            if (pb.ischeck) {
                                pb.ischeck = false;
                                ellProduct.getChildAt(position).findViewById(R.id.checkbox_im).setBackgroundResource(R.drawable.ellproduct_n);
                            } else {
                                pb.ischeck = true;
                                ellProduct.getChildAt(position).findViewById(R.id.checkbox_im).setBackgroundResource(R.drawable.ellproduct_s);
                            }
                        }
                    }
                    Log.i("点击完毕", "" + s.toJson(productList).toString());
                }
            });
        }

        //折惠含（暂时不做）
        //套餐特点
        LinearLayout Package_Feature_RelativeLayout = (LinearLayout) findViewById(R.id.Package_Feature_RelativeLayout);
        final int WEIGHT = 3;
        if (!"".equals(packageDetailList.get(0).getBody().getPackage().getPackage_Feature()) && null != packageDetailList.get(0).getBody().getPackage().getPackage_Feature()) {
            Package_Feature_RelativeLayout.setVisibility(View.VISIBLE);
            ArrayList<String> Package_Feature = packageDetailList.get(0).getBody().getPackage().getPackage_Feature();
            Log.i("Package_Feature == ", "" + Package_Feature.size());
            int size = Package_Feature.size();
            // 动态添加控件，首先添加第一行布局
            LinearLayout rowLayout = null;
            for (int i = 0; i < size; i++) {
                if (i % WEIGHT == 0) {
                    // LinearLayout不能换行,每3个添加一个布局，并加入到外层布局中
                    rowLayout = createImageLayout();
                    Package_Feature_RelativeLayout.addView(rowLayout);
                }
                View columnLinearLayout = createImage(WEIGHT, Package_Feature.get(i));
                rowLayout.addView(columnLinearLayout);
            }
        } else {
            Package_Feature_RelativeLayout.setVerticalGravity(View.GONE);
        }
        //课程星级评价
        ImageView course_details_kcpjxx_im1 = (ImageView) findViewById(R.id.course_details_kcpjxx_im);
        ImageView course_details_kcpjxx_im2 = (ImageView) findViewById(R.id.course_details_kcpjxx_im2);
        ImageView course_details_kcpjxx_im3 = (ImageView) findViewById(R.id.course_details_kcpjxx_im3);
        ImageView course_details_kcpjxx_im4 = (ImageView) findViewById(R.id.course_details_kcpjxx_im4);
        ImageView course_details_kcpjxx_im5 = (ImageView) findViewById(R.id.course_details_kcpjxx_im5);
        int Package_EvaluateStar = packageDetailList.get(0).getBody().getPackage().getPackage_EvaluateStar();
        if (Package_EvaluateStar == 0) {
            course_details_kcpjxx_im1.setVisibility(View.GONE);
            course_details_kcpjxx_im2.setVisibility(View.GONE);
            course_details_kcpjxx_im3.setVisibility(View.GONE);
            course_details_kcpjxx_im4.setVisibility(View.GONE);
            course_details_kcpjxx_im5.setVisibility(View.GONE);
        } else if (Package_EvaluateStar == 1) {
            course_details_kcpjxx_im1.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im2.setVisibility(View.GONE);
            course_details_kcpjxx_im3.setVisibility(View.GONE);
            course_details_kcpjxx_im4.setVisibility(View.GONE);
            course_details_kcpjxx_im5.setVisibility(View.GONE);
        } else if (Package_EvaluateStar == 2) {
            course_details_kcpjxx_im1.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im2.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im3.setVisibility(View.GONE);
            course_details_kcpjxx_im4.setVisibility(View.GONE);
            course_details_kcpjxx_im5.setVisibility(View.GONE);
        } else if (Package_EvaluateStar == 3) {
            course_details_kcpjxx_im1.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im2.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im3.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im4.setVisibility(View.GONE);
            course_details_kcpjxx_im5.setVisibility(View.GONE);
        } else if (Package_EvaluateStar == 4) {
            course_details_kcpjxx_im1.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im2.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im3.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im4.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im5.setVisibility(View.GONE);
        } else if (Package_EvaluateStar == 5) {
            course_details_kcpjxx_im1.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im2.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im3.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im4.setVisibility(View.VISIBLE);
            course_details_kcpjxx_im5.setVisibility(View.VISIBLE);
        }
        //课程人数
        TextView course_details_xxrs = (TextView) findViewById(R.id.details_xxrs);
        int Package_StudyNum = packageDetailList.get(0).getBody().getPackage().getPackage_StudyNum();
        course_details_xxrs.setText(Package_StudyNum + "");


        data = packageDetailList.get(0).getBody().getPackage().getH5DetailLink();
        dataList = packageDetailList.get(0).getBody().getClassList();
        Log.i("加载底部 ==== >", "" + data);
        Log.i("SetData的数据 ", "" + data);

        //fragment底部数据
        CourseDetailsFragmentContent = packageDetailList.get(0).getBody().getPackage().getH5DetailLink();
        SyllabusDetailsFragmentContent = packageDetailList.get(0).getBody().getClassList();
    }

    class ViewHolder {
        @BindView(R.id.title_name)
        TextView tvName;
        @BindView(R.id.checkbox_im)
        ImageView checkBox_im;
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        ProductBean productBean;

        public ViewHolder(View view, ProductBean productBean) {
            ButterKnife.bind(this, view);
            this.productBean = productBean;
        }

        private void refreshUI() {
//            Glide.with(LiveDetailsActivity.this)
//                    .load(productBean.getImg())
//                    .placeholder(R.mipmap.ic_default)
//                    .into(ivImg);
            tvName.setText(productBean.getName());

        }
    }

    private void initBottom_IncludeView() {
        list_fragment = new ArrayList<>();  //定义要装fragment的列表
        list_title = new ArrayList<>();  //定义要装fragment的列表
        //初始化各fragment
//        CourseDetailsFragment mOneFragment = new CourseDetailsFragment();
        CourseDetailsFragment mOneFragment = CourseDetailsFragment.newInstance();
        SyllabusFragment mTwoFragment = new SyllabusFragment();
//        SyllabusConsultationFragment mThreeFragment = new SyllabusConsultationFragment();
        //将fragment装进列表中
        list_fragment.add(mOneFragment);
        list_fragment.add(mTwoFragment);
//        list_fragment.add(mThreeFragment);
        list_title.add("课程详情");
        list_title.add("课程大纲");
//        list_title.add("常见问题");

        pagerAdapter = new SimpleFragmentPagerAdapter(OnlineCourseDetailsActivity.this.getSupportFragmentManager(), this, list_fragment, list_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }


    private void initBottomView() {
//        addshopClick = (LinearLayout) findViewById(R.id.add_shop_car_llyt);
//        addshopClick.setOnClickListener(this);
        //分享功能
        good_cshare_llyt = (LinearLayout) findViewById(R.id.good_cshare_llyt);
        good_cshare_llyt.setOnClickListener(this);
        //购买/拨打电话
        gobuyClick = (LinearLayout) findViewById(R.id.go_buy_Click);
        gobuyClick.setOnClickListener(this);
        //收藏
        good_collection_llyt = (LinearLayout) findViewById(R.id.good_collection_llyt);
        collect_icon = (ImageView) findViewById(R.id.collect_icon);
        collect_tv = (TextView) findViewById(R.id.collect_tv);
        // 收藏情况
        int HasCollection = packageDetailList.get(0).getBody().getPackage().getHasCollection();
        if (1 == HasCollection) {
            CollectionType = 1;
            collect_icon.setBackgroundResource(R.drawable.taocan_shoucang_s);
            collect_tv.setTextColor(Color.parseColor("#dd5555"));
        } else if (0 == HasCollection) {
            CollectionType = 0;
            collect_icon.setBackgroundResource(R.drawable.taocan_shoucang_n);
            collect_tv.setTextColor(Color.parseColor("#666666"));
        }
        good_collection_llyt.setOnClickListener(this);
        //客服
        server_linear = (LinearLayout) findViewById(R.id.server_linear);
        server_linear.setOnClickListener(this);

    }

    private void showPopupCommnet() {
        View view = LayoutInflater.from(OnlineCourseDetailsActivity.this).inflate(
                R.layout.online_course_info_dialog, null);
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
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
//        // 设置弹出窗体需要软键盘
//        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 100, 100);
        //设置弹出之后的背景透明度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //显示
        popupWindow.update();
//        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
    }


    class CollectionTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            type = params[0];
            Log.i("type == ", "" + type);
            int class_pid = packageDetailList.get(0).getBody().getPackage().getPackage_PKID();
            try {
                productCollection = httpUtils.getProductCollection(Const.URL + Const.GetProductCollectionAPI, getUserID(), class_pid, 0, type);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == productCollection.getCode()) {
                Log.i("CollectionType == ", "" + CollectionType);
                if (0 == type) {
                    collect_icon.setBackgroundResource(R.drawable.taocan_shoucang_s);
                    collect_tv.setTextColor(Color.parseColor("#dd5555"));
                    CollectionType = 1;
                } else if (1 == type) {
                    collect_icon.setBackgroundResource(R.drawable.taocan_shoucang_n);
                    collect_tv.setTextColor(Color.parseColor("#666666"));
                    CollectionType = 0;
                }
            }else{
                Toast.makeText(OnlineCourseDetailsActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void Collection(int type) {
        if (httpUtils.isNetworkConnected(this)) {
            CollectionTask collectionTask = new CollectionTask();
            collectionTask.execute(type);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //分享
            case R.id.good_cshare_llyt:
                //判断读写权限
                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 无权限----
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                } else {
                    //文本
//                    new ShareAction(this)
//                            .withText("hello")
//                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                            .setCallback(umShareListener)
//                            .open();
                    //连接
                    UMWeb web = new UMWeb("http://mp.gfedu.cn/html/app.html");
                    UMImage thumb = new UMImage(this, R.mipmap.ic_launcher);
                    web.setTitle("金程网校");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription("随时随地想学就学，随堂检测学习效果，支持高清视频下载缓存，您的口袋学习工具。");//描述
                    new ShareAction(this)
                            .withMedia(web)
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                            .setCallback(umShareListener)
                            .open();


                }
                break;
            //收藏
            case R.id.good_collection_llyt:
                if (0 != getUserID()) {
                    Collection(CollectionType);
                } else {//去登陆
                    Intent layoutIntent = new Intent(this, NormalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Details", "10001");
                    layoutIntent.putExtras(bundle);
                    startActivityForResult(layoutIntent, 10001);
                }
                break;

            case R.id.server_linear:
//                Intent serverIntent = new Intent(this, ServerActivity.class);
//                startActivity(serverIntent);
                Intent ServerIntent = new Intent();
                ServerIntent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Const.ServerH5);
                ServerIntent.setData(content_url);
                startActivity(ServerIntent);
                break;

            //立即购买
            case R.id.go_buy_Click:
                if (isSign()) {
                    //判断价格是否为0
                    String MinSalePrice = packageDetailList.get(0).getBody().getPackage().getPackage_MinSalePrice();
                    String MaxSalePrice = packageDetailList.get(0).getBody().getPackage().getPackage_MaxSalePrice();
                    if ("0".equals(MinSalePrice) && "0".equals(MaxSalePrice)) {
                        ZeroBuyDialog dialog = new ZeroBuyDialog(this);
                        dialog.show();
                    } else {
                        String packlist = "";
                        Gson s = new Gson();
                        Log.i("productList -- ", "" + s.toJson(productList).toString());
                        //0：固定套餐 1：自由组合套餐
                        int packtype = packageDetailList.get(0).getBody().getPackage().getPackage_Type();
                        for (int i = 0; i < productList.size(); i++) {
                            if (productList.get(i).ischeck) {
                                packlist = productList.get(i).getPkid() + "," + packlist;

                            }
                        }
                        if (!"".equals(packlist) && null != packlist) {
                            StringBuffer str = new StringBuffer(packlist);
                            if (',' == str.charAt(str.length() - 1))
                                str = str.deleteCharAt(str.length() - 1);
                            packlist = str.toString();
                        }

                        Intent Mealintent = new Intent(this, MealCourseCartActivity.class);
                        Bundle bundle = new Bundle();
                        int packId = packageDetailList.get(0).getBody().getPackage().getPackage_PKID();
                        String imageUrl = packageDetailList.get(0).getBody().getPackage().getPackage_MobileImage();
                        String title = packageDetailList.get(0).getBody().getPackage().getPackage_Name();
                        String priceMin = packageDetailList.get(0).getBody().getPackage().getPackage_MinSalePrice();
                        String priceMax = packageDetailList.get(0).getBody().getPackage().getPackage_MaxSalePrice();
                        bundle.putInt("packtype", packtype);
                        bundle.putInt("packId", packId);
                        bundle.putString("packlist", packlist);
                        bundle.putString("imageUrl", imageUrl);
                        bundle.putString("title", title);
                        bundle.putString("priceMin", priceMin);
                        bundle.putString("priceMax", priceMax);
                        Mealintent.putExtras(bundle);
                        startActivity(Mealintent);
                    }
                } else {
                    LoginDialog dialog = new LoginDialog(this);
                    dialog.show();
//                    Toast.makeText(LiveDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }


//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-700-9596"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                break;

            case R.id.top_back_RelativeLayout:
                finish();
                break;

            case R.id.top_back:
                finish();
                break;
        }
    }

    private boolean isSign() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
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

    private int getUserID() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int UserID = LoginPre.getInt("S_ID", 0);
            return UserID;
        } else {
            return 0;
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.i("platform == ", "" + platform);
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.i("分享 == > ", "成功了");
//            Toast.makeText(LiveDetailsActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.i("分享 == > ", "失败");
//            Toast.makeText(LiveDetailsActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.i("分享 == > ", "取消");
//            Toast.makeText(LiveDetailsActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != topWb) {
            topWb.destroy();
        }
    }

    public View createImage(int weight, String text) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(0, 20, 0, 0);
        // 获取屏幕宽度
        int W = this.getWindowManager().getDefaultDisplay().getWidth();
        // 根据每行个数设置布局大小
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(W / weight, LinearLayout.LayoutParams.WRAP_CONTENT));
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // 设置图片大小
        int cricleRadius = W / (weight + 2);
        ImageView circleImageView = new ImageView(this);
        circleImageView.setLayoutParams(new LinearLayout.LayoutParams(36, 36));
        if (!"".equals(text) && null != text) {
            Bitmap map = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_baoku);
            circleImageView.setImageBitmap(map);
        }
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(10, 0, 0, 0);
        linearLayout.setTag(textView.getText());
        linearLayout.addView(circleImageView);
        linearLayout.addView(textView);
        return linearLayout;
    }

    // 每4个图片一行
    public LinearLayout createImageLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        return linearLayout;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }


    private boolean getUserID_boolean() {
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


}
