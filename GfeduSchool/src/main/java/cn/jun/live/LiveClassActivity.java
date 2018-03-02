package cn.jun.live;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.gfedu.gfeduapp.NetActivity;
import cn.jun.bean.AddLiveBook;
import cn.jun.bean.ClassOutLineBean;
import cn.jun.bean.Const;
import cn.jun.bean.GetLiveDetailBean;
import cn.jun.bean.ProductCollection;
import cn.jun.courseinfo.activity.ClassCourseCartActivity;
import cn.jun.courseinfo.adapter.ExpandableListAdapter;
import cn.jun.courseinfo.polyvplayer.C_IjkVideoActicity;
import cn.jun.live.vod.VodRoomActivity;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.utils.HttpUtils;
import cn.jun.view.CacheSet;
import cn.jun.view.LoginDialog;
import cn.jun.view.ScrollViewExpandableListView;
import cn.jun.view.WifiSet;
import cn.jun.view.ZeroBuyDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.NormalActivity;

import static cn.gfedu.gfeduapp.MainActivity.MainAc;
import static jc.cici.android.R.id.top_back;
import static jc.cici.android.R.id.top_back_RelativeLayout;

/**
 * 直播(非系列)
 **/
public class LiveClassActivity extends NetActivity implements View.OnClickListener, ExpandableListView.OnGroupExpandListener,
        ExpandableListAdapter.OnChildTreeViewClickListener {
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
    public static String CourseDetailsFragmentAloneContent;//课程详情数据源
    private ClassOutLineBean classOutLineBean = new ClassOutLineBean();
    //课程大纲数据源
    public static ArrayList<ClassOutLineBean> SyllabusDetailsFragmentAloneContent;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();

    private ScrollViewExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    //    private View vHead;
    private WebView wb;//课程详情WebView
    private int WebView_W, WebView_H;
    private LinearLayout no_header_layout;
    private RelativeLayout noHeader_btn1;
    private RelativeLayout noHeader_btn2;
    private TextView noHeader_btn_tv1;
    private TextView noHeader_btn_tv2;
    private RelativeLayout header_btn1;
    private RelativeLayout header_btn2;
    private TextView header_btn_tv1;
    private TextView header_btn_tv2;
    //传递过来的直播ID
    private int Class_PKID;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private WebView topWb;
    //是否有免费试听
    private int Class_OutlineFreeState;
    //免费试听图片显示
    private ImageView noHeader_imageView;
    private ImageView Header_imageView;

    private GetLiveDetailBean getLiveDetailBean = new GetLiveDetailBean();
    private ArrayList<GetLiveDetailBean> LiveDetailList = new ArrayList<>();
    //立即购买or立即预约
    private RelativeLayout go_buy_layout;

    private final static int LiJiYuYue = 100000;//立即预约
    private final static int LiJiGouMai = 100007;//立即购买
    private final static int YiYuYue = 100001;//已预约
    private final static int JinRuZB = 100003;//进入直播
    private final static int ChaKanHuiFang = 100005;//查看回放
    private final static int WuHuiFang = 100006;//无回放
    private final static int YiGouMai = 100002;//已购买
    private final static int ZBJieShu = 100004;//直播结束

    private int goBuyStatus = -1;

    private AddLiveBook addLiveBook = new AddLiveBook();

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.live_class_other);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            Class_PKID = bundle.getInt("Class_PKID", 0);
        }
        Log.i("Class_PKID -- ","" + Class_PKID);

        go_buy_layout = (RelativeLayout) findViewById(R.id.go_buy_layout);
        no_header_layout = (LinearLayout) findViewById(R.id.no_header_layout);
        wb = (WebView) findViewById(R.id.CourseDetailsWb);
        expandableListView = (ScrollViewExpandableListView) findViewById(R.id.myList);

        if (httpUtils.isNetworkConnected(this)) {
            showProcessDialog(LiveClassActivity.this,
                    R.layout.loading_show_dialog_color);
            initData();
        } else {//无网络不能进入
            finish();
            Toast.makeText(this, "网络连接中断,稍后再试!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        // 头布局
//        vHead = View.inflate(this, R.layout.class_headerview, null);
        //详情
        PackageDetailTask packageDetail = new PackageDetailTask();
        packageDetail.execute(Class_PKID);
//        //获取大纲
        ClassOutLineTask classoutTask = new ClassOutLineTask();
        classoutTask.execute(Class_PKID);
    }

    //展开一项，关闭其他项，保证每次只能展开一项
    @Override
    public void onGroupExpand(int groupPosition) {
//        for (int i = 0; i < parents.size(); i++) {
//            if (i != groupPosition) {
//                eList.collapseGroup(i);
//            }
//        }
    }

    @Override
    public void onClickPosition(int parentPosition, int groupPosition, int childPosition) {
        //名称
        String childName = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getLevel_ShowName();
        //视频VID
        String childVID = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getVID();
        //类型
        int childKeyType = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getKeyType();
        //文件ID
        int childKeyID = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getKeyID();
        //是否免费试听
        int isFree = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getIsFree();
        //1:视频 2:试卷 3:直播 4:资料
        if (1 == childKeyType) {
            if (!"".equals(childVID) && null != childVID) {
                if (1 == isFree) {
                    C_IjkVideoActicity.intentTo(LiveClassActivity.this, C_IjkVideoActicity.PlayMode.landScape, C_IjkVideoActicity.PlayType.vid, childVID,
                            true, childName);
                } else {
                    Toast.makeText(LiveClassActivity.this, "该节课程无试听视频", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LiveClassActivity.this, "视频暂时无法播放", Toast.LENGTH_SHORT).show();
            }
        } else if (3 == childKeyType) {//直播

        } else {
            Toast.makeText(LiveClassActivity.this, "暂无试听内容", Toast.LENGTH_SHORT).show();
        }

    }

    class PackageDetailTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int Class_id = params[0];
//            Class_id = 157;
            try {
                getLiveDetailBean = httpUtils.getLiveDetail(Const.URL + Const.GetLiveDetailAPI, getUserID(), Class_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == getLiveDetailBean.getCode()) {
                LiveDetailList.add(getLiveDetailBean);
                initTop_Include_NoHeader();
//                initTop_IncludeHeader();
                initBottomView();
            }


        }
    }

    class ClassOutLineTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int Class_id = params[0];
            Class_id = 99;
            try {
                classOutLineBean = httpUtils.getClassOutLineBean(Const.URL + Const.GetClassOutLineAPI, getUserID(), Class_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == classOutLineBean.getCode()) {
                if (classOutLineBean.getBody().getOutLineCount() > 0) {
                    SyllabusDetailsFragmentAloneContent = new ArrayList<>();
                    SyllabusDetailsFragmentAloneContent.add(classOutLineBean);
                }
//                initBottom_Include2();
            }
            mDialog.dismiss();

        }
    }

//    private void initTop_IncludeHeader() {
//        Class_OutlineFreeState = LiveDetailList.get(0).getBody().getClassInfo().getClass_OutlineFreeState();
//        //免费试听图片
//        Header_imageView = (ImageView) vHead.findViewById(R.id.Header_imageView);
//        if (1 == Class_OutlineFreeState) {
//            Header_imageView.setVisibility(View.VISIBLE);
//        } else if (0 == Class_OutlineFreeState) {
//            Header_imageView.setVisibility(View.GONE);
//        }
//        //功能返回键
//        ImageView ListHeader_top_back = (ImageView) vHead.findViewById(R.id.header_top_back);
//        ListHeader_top_back.setOnClickListener(this);
//        RelativeLayout ListHeader_top_back_RelativeLayout = (RelativeLayout) vHead.findViewById(R.id.header_top_back_RelativeLayout);
//        ListHeader_top_back_RelativeLayout.setOnClickListener(this);
//        //H5视频播放地址或者图片显示地址
//        ImageView ListHeader_topIm = (ImageView) vHead.findViewById(R.id.header_course_details_top_im);
//        WebView ListHeader_topWb = (WebView) vHead.findViewById(R.id.header_course_details_top_wb);
//        final TextView ListHeader_topTv = (TextView) vHead.findViewById(R.id.header_course_details_top_tv);
//        ListHeader_topWb.getSettings().setJavaScriptEnabled(true);
//        ListHeader_topWb.getSettings()
//                .setDefaultTextEncodingName("UTF-8");
//        ListHeader_topWb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        ListHeader_topWb.getSettings().setSupportMultipleWindows(true);
//        String Package_MobileIsVideo = LiveDetailList.get(0).getBody().getClassInfo().getClass_MobileIsVideo();
//        if ("1".equals(Package_MobileIsVideo)) {//是视频
////            ListHeader_topWb.setVisibility(View.VISIBLE);
////            ListHeader_topIm.setVisibility(View.GONE);
////            String H5Likn = ClassDetailList.get(0).getBody().getH5VideoLink();
////            String H5Likn = "http://pc.gfedu.cn/home/video?vid=3a5I1XNolzT5ATYf25XBpw==";
////            Log.i("List头部H5页面 == ", "" + H5Likn);
////            ListHeader_topWb.loadUrl(H5Likn);
////            ListHeader_topTv.setVisibility(View.VISIBLE);
////            ListHeader_topWb.setWebViewClient(new WebViewClient() {
////                //覆盖shouldOverrideUrlLoading 方法
////                @Override
////                public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                    view.loadUrl(url);
////                    return true;
////                }
////            });
////            ListHeader_topWb.setWebViewClient(new WebViewClient() {
////                @Override
////                public void onPageFinished(WebView view, String url) {
////                    ListHeader_topTv.setVisibility(View.GONE);
////                }
////            });
//        } else if ("0".equals(Package_MobileIsVideo)) {//不是视频,加载图片
//            ListHeader_topIm.setVisibility(View.VISIBLE);
//            ListHeader_topWb.setVisibility(View.GONE);
//            ListHeader_topTv.setVisibility(View.GONE);
//            String Package_MobileValue = LiveDetailList.get(0).getBody().getClassInfo().getClass_MobileValue();
//            Log.i("List头部Image_Likn == ", "" + Package_MobileValue);
//            Glide.with(LiveClassActivity.this)
//                    .load(Package_MobileValue)
//                    .placeholder(R.drawable.pic_kong)
//                    .into(ListHeader_topIm);
//        }
//        //标题
//        String titleList = LiveDetailList.get(0).getBody().getClassInfo().getClass_Name();
//        titleList = titleList.replace("&nbsp;", " ");
//        TextView course_details_title = (TextView) vHead.findViewById(R.id.course_details_title);
//        course_details_title.setText(titleList);
//        //现价
//        String MinSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MinSalePrice();
//        String MaxSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MaxSalePrice();
//        TextView course_details_newprice = (TextView) vHead.findViewById(R.id.course_details_intoName);
//        Log.i("MinSalePrice  ", "" + MinSalePrice);
//        Log.i("MaxSalePrice ", "" + MaxSalePrice);
//        if ("0.00".equals(MinSalePrice) && "0.00".equals(MaxSalePrice)) {
//            course_details_newprice.setText("免费");
//        } else {
//            if (MinSalePrice.equals(MaxSalePrice)) {
//                course_details_newprice.setText("¥" + MinSalePrice);
//            } else {
//                course_details_newprice.setText("¥" + MinSalePrice + " - " + MaxSalePrice);
//            }
//        }
//        //日期
//        String c_date = LiveDetailList.get(0).getBody().getSchedule().getCS_Date();
//        TextView course_intoName2 = (TextView) vHead.findViewById(R.id.course_details_intoName2);
//        course_intoName2.setText(c_date);
//        //课程人数
//        TextView course_details_xxrs = (TextView) vHead.findViewById(R.id.course_details_intoName3);
//        int Package_StudyNum = LiveDetailList.get(0).getBody().getClassInfo().getClass_StudyNum();
//        course_details_xxrs.setText("购买人数 : " + Package_StudyNum);
//        //课程详情WebView数据
//        CourseDetailsFragmentAloneContent = LiveDetailList.get(0).getBody().getClassInfo().getH5DetailLink();
//        //解决顶部空白过多-（暂时处理）
//        wb.getSettings().setJavaScriptEnabled(true);
//        wb.loadUrl(CourseDetailsFragmentAloneContent);
//        wb.setVisibility(View.VISIBLE);
//    }

    private void initTop_Include_NoHeader() {
        Class_OutlineFreeState = LiveDetailList.get(0).getBody().getClassInfo().getClass_OutlineFreeState();
        //返回键
        ImageView top_back = (ImageView) findViewById(R.id.top_back);
        top_back.setOnClickListener(this);
        RelativeLayout top_back_RelativeLayout = (RelativeLayout) findViewById(R.id.top_back_RelativeLayout);
        top_back_RelativeLayout.setOnClickListener(this);
        //H5视频播放地址或者图片显示地址
        ImageView topIm = (ImageView) findViewById(R.id.course_details_top_im);
        topWb = (WebView) findViewById(R.id.course_details_top_wb);
        final TextView topTv = (TextView) findViewById(R.id.course_details_top_tv);
        topWb.getSettings().setJavaScriptEnabled(true);
        topWb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        topWb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        topWb.getSettings().setSupportMultipleWindows(true);
        String Package_MobileIsVideo = LiveDetailList.get(0).getBody().getClassInfo().getClass_MobileIsVideo();
        if ("1".equals(Package_MobileIsVideo)) {//是视频
            topWb.setVisibility(View.VISIBLE);
            topIm.setVisibility(View.GONE);
            String H5Likn = LiveDetailList.get(0).getBody().getClassInfo().getH5VideoLink();
//            String H5Likn = "http://pc.gfedu.cn/home/video?vid=3a5I1XNolzT5ATYf25XBpw==";
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
            String Package_MobileValue = LiveDetailList.get(0).getBody().getClassInfo().getClass_MobileValue();
            Log.i("Image_Likn == ", "" + Package_MobileValue);
            Glide.with(LiveClassActivity.this)
                    .load(Package_MobileValue)
                    .placeholder(R.drawable.pic_kong)
                    .into(topIm);
        }
        //标题
        String titleList = LiveDetailList.get(0).getBody().getClassInfo().getClass_Name();
        titleList = titleList.replace("&nbsp;", " ");
        TextView course_details_title = (TextView) findViewById(R.id.course_details_title);
        course_details_title.setText(titleList);
        //现价
        String MinSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MinSalePrice();
        String MaxSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MaxSalePrice();
        TextView course_details_newprice = (TextView) findViewById(R.id.course_details_intoName);
        //课程人数
        TextView course_details_xxrs = (TextView) findViewById(R.id.course_details_intoName3);
        int Package_StudyNum = LiveDetailList.get(0).getBody().getClassInfo().getClass_StudyNum();
        int BookCount = LiveDetailList.get(0).getBody().getSchedule().getBookCount();
        course_details_xxrs.setText("购买人数 : " + BookCount);
        if ("0.00".equals(MinSalePrice) && "0.00".equals(MaxSalePrice)) {
            course_details_newprice.setText("免费");
            course_details_xxrs.setText("预约人数 : " + BookCount);
        } else {
            course_details_xxrs.setText("购买人数 : " + BookCount);
            if (MinSalePrice.equals(MaxSalePrice)) {
                course_details_newprice.setText("¥" + MinSalePrice);
            } else {
                course_details_newprice.setText("¥" + MinSalePrice + " - " + MaxSalePrice);
            }
        }
        //日期
        String c_date = LiveDetailList.get(0).getBody().getSchedule().getCS_Date();
        TextView course_intoName2 = (TextView) findViewById(R.id.course_details_intoName2);
        course_intoName2.setText(c_date);

        //课程详情WebView数据
        CourseDetailsFragmentAloneContent = LiveDetailList.get(0).getBody().getClassInfo().getH5DetailLink();
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(CourseDetailsFragmentAloneContent);

//        wb.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                Log.i("VISIBLE == ", "VISIBLE");
//                wb.setVisibility(View.VISIBLE);
//                expandableListView.setVisibility(View.GONE);
//                wb.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
//                super.onPageFinished(view, url);
//            }
//        });
//        wb.addJavascriptInterface(this, "App");
//        //重新测量
//        wb.measure(WebView_W, WebView_H);
        wb.setVisibility(View.VISIBLE);

//        noHeader_btn1 = (RelativeLayout) findViewById(R.id.btn1);
//        noHeader_btn2 = (RelativeLayout) findViewById(R.id.btn2);
//        noHeader_btn_tv1 = (TextView) findViewById(R.id.btn_tv1);
//        noHeader_btn_tv2 = (TextView) findViewById(R.id.btn_tv2);
//        noHeader_btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                noHeader_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
//                noHeader_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
//                noHeader_btn2.setBackground(getResources().getDrawable(R.color.white));
//                noHeader_btn_tv2.setTextColor(Color.parseColor("#666666"));
//                no_header_layout.setVisibility(View.VISIBLE);
//                wb.setVisibility(View.VISIBLE);
//                expandableListView.setVisibility(View.GONE);
//
//            }
//        });
//        noHeader_btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                noHeader_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
//                noHeader_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
//                noHeader_btn1.setBackground(getResources().getDrawable(R.color.white));
//                noHeader_btn_tv1.setTextColor(Color.parseColor("#666666"));
//                no_header_layout.setVisibility(View.GONE);
//                wb.setVisibility(View.GONE);
////                wb.setVisibility(View.VISIBLE);
//
//                expandableListView.setVisibility(View.VISIBLE);
//
//                header_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
//                header_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
//                header_btn1.setBackground(getResources().getDrawable(R.color.white));
//                header_btn_tv1.setTextColor(Color.parseColor("#666666"));
//            }
//        });
    }

//    private void initBottom_Include2() {
//        expandableListView.setDivider(null);
//        expandableListView.setGroupIndicator(null);
////        头布局放入listView中
//        expandableListView.addHeaderView(vHead);
////        if (!"".equals(SyllabusDetailsFragmentAloneContent) && null != SyllabusDetailsFragmentAloneContent) {
//        Gson s = new Gson();
//        Log.i("mList == 222", "" + s.toJson(SyllabusDetailsFragmentAloneContent).toString());
//        adapter = new ExpandableListAdapter(LiveClassActivity.this, SyllabusDetailsFragmentAloneContent);
//        expandableListView.setAdapter(adapter);
//        expandableListView.expandGroup(0);
//        adapter.setOnChildTreeViewClickListener(LiveClassActivity.this);
////        }
//
//        header_btn1 = (RelativeLayout) vHead.findViewById(R.id.btn1);
//        header_btn2 = (RelativeLayout) vHead.findViewById(R.id.btn2);
//        header_btn_tv1 = (TextView) vHead.findViewById(R.id.btn_tv1);
//        header_btn_tv2 = (TextView) vHead.findViewById(R.id.btn_tv2);
//        header_btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                header_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
//                header_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
//                header_btn2.setBackground(getResources().getDrawable(R.color.white));
//                header_btn_tv2.setTextColor(Color.parseColor("#666666"));
//                no_header_layout.setVisibility(View.VISIBLE);
//                wb.setVisibility(View.VISIBLE);
//                expandableListView.setVisibility(View.GONE);
//
//                noHeader_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
//                noHeader_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
//                noHeader_btn2.setBackground(getResources().getDrawable(R.color.white));
//                noHeader_btn_tv2.setTextColor(Color.parseColor("#666666"));
//            }
//        });
//        header_btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                header_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
//                header_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
//                header_btn1.setBackground(getResources().getDrawable(R.color.white));
//                header_btn_tv1.setTextColor(Color.parseColor("#666666"));
//                no_header_layout.setVisibility(View.GONE);
//                wb.setVisibility(View.GONE);
//                expandableListView.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }


    private void initBottomView() {
//        addshopClick = (LinearLayout) findViewById(R.id.add_shop_car_llyt);
//        addshopClick.setOnClickListener(this);
        //分享
        good_cshare_llyt = (LinearLayout) findViewById(R.id.good_cshare_llyt);
        good_cshare_llyt.setOnClickListener(this);
        //购买/拨打电话
        gobuyClick = (LinearLayout) findViewById(R.id.go_buy_Click);
        //是否购买过
//        int HasBuy = LiveDetailList.get(0).getBody().getSchedule().getHasBuy();
//        //是否预约过
//        int HasBook = LiveDetailList.get(0).getBody().getSchedule().getHasBook();
        //是否已预约或者已购买
        int HasBookOrBuy = LiveDetailList.get(0).getBody().getSchedule().getHasBookOrBuy();
        //是否有回放
//        int HasWatch = LiveDetailList.get(0).getBody().getSchedule().getHasWatch();
        int HasWatch = LiveDetailList.get(0).getBody().getSchedule().getCS_IsPlayback();
        //直播状态
        int LiveStatus = -1;
        //是否开始
        int isStart = LiveDetailList.get(0).getBody().getSchedule().getIsLiveBegin();
        //是否结束
        int isEnd = LiveDetailList.get(0).getBody().getSchedule().getIsLiveEnd();
        if (1 == isStart) {
            LiveStatus = 1;
        } else if (1 == isEnd) {
            LiveStatus = 2;
        } else {
            LiveStatus = 0;
        }
        Log.i("直播状态LiveStatus ==  ", "" + LiveStatus);
        Log.i("是否已预约或者已购买 ==  ", "" + HasBookOrBuy);
//        Log.i("是否购买 ==  ", "" + HasBuy);
//        Log.i("是否预约 ==  ", "" + HasBook);
        //价格
        String MinSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MinSalePrice();
        String MaxSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MaxSalePrice();
        Log.i("MinSalePrice ==  ", "" + MinSalePrice);
        Log.i("MaxSalePrice ==  ", "" + MaxSalePrice);
        if ("0.00".equals(MinSalePrice) && "0.00".equals(MaxSalePrice)) {//免费
            Log.i("免费 === ", " 免费");
            if (1 == HasBookOrBuy) {//预约过
                Log.i("预约过 === ", " 预约过");
                if (0 == LiveStatus) {//直播未开始
                    Log.i("直播未开始 === ", " 直播未开始");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     已预约");
                    buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                    goBuyStatus = YiYuYue;
                } else if (1 == LiveStatus) {//直播开始
                    Log.i("直播开始 === ", " 直播开始");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     进入直播");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = JinRuZB;
                } else if (2 == LiveStatus) {//直播结束
                    Log.i("直播结束 === ", " 直播结束");
                    if (1 == HasWatch) {//有回放
                        Log.i("直播结束 有回放 === ", " 直播结束 有回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     观看回放");
                        buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                        goBuyStatus = ChaKanHuiFang;
                    } else {//无回访
                        Log.i("直播结束 无回放 === ", " 直播结束 无回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     已结束");
                        buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                        goBuyStatus = WuHuiFang;
                    }
                }
            } else {//未预约
                if (2 == LiveStatus) {
                    if (1 == HasWatch) {//有回放
                        Log.i("直播结束 有回放 === ", " 直播结束 有回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     观看回放");
                        buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                        goBuyStatus = ChaKanHuiFang;
                    } else {//无回访
                        Log.i("直播结束 无回放 === ", " 直播结束 无回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     已结束");
                        buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                        goBuyStatus = WuHuiFang;
                    }
                } else {
                    Log.i("未预约 === ", " 未预约");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     立即预约");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = LiJiYuYue;
                }

            }
        } else {//有价格（需预约）
            Log.i("有价格 === ", " 有价格");
            if (1 == HasBookOrBuy) {//预约过或者购买过
                Log.i("有价格 预约过或者购买过=== ", " 有价格 预约过或者购买过");
                if (0 == LiveStatus) {//直播未开始
                    Log.i("有价格 直播未开始=== ", " 有价格 直播未开始");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     已购买");
                    buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                    goBuyStatus = YiYuYue;
                } else if (1 == LiveStatus) {//直播开始
                    Log.i("有价格 直播开始=== ", " 有价格 直播开始");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     进入直播");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = JinRuZB;
                } else if (2 == LiveStatus) {//直播结束
                    Log.i("有价格 直播结束=== ", " 有价格 直播结束");
                    if (1 == HasWatch) {//有回放
                        Log.i("有价格 直播结束 有回放=== ", " 有价格 直播结束 有回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#FF8E1E"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     观看回放");
                        buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                        goBuyStatus = ChaKanHuiFang;
                    } else {//无回访
                        Log.i("有价格 直播结束 无回放=== ", " 有价格 直播结束 无回放");
                        go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                        tel_im.setVisibility(View.GONE);
                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                        buy_immediately_bt.setText("     已结束");
                        buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                        goBuyStatus = WuHuiFang;
                    }
                }
            } else {//未购买或未预约
                if (2 == LiveStatus && 1 == HasWatch) {
                    Log.i("未购买或未预约=== ", " 未购买或未预约");
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DD5555"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.VISIBLE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("立即购买");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = LiJiGouMai;
                } else if (1 == LiveStatus) {
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DD5555"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.VISIBLE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("立即购买");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = LiJiGouMai;
                } else if (2 == LiveStatus && 0 == HasWatch) {
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.GONE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("     已结束");
                    buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                    goBuyStatus = WuHuiFang;
                } else {
                    go_buy_layout.setBackgroundColor(Color.parseColor("#DD5555"));
                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                    tel_im.setVisibility(View.VISIBLE);
                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                    buy_immediately_bt.setText("立即购买");
                    buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
                    goBuyStatus = LiJiGouMai;
//                    if(){
//                        go_buy_layout.setBackgroundColor(Color.parseColor("#DD5555"));
//                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
//                        tel_im.setVisibility(View.VISIBLE);
//                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
//                        buy_immediately_bt.setText("立即购买");
//                        buy_immediately_bt.setTextColor(Color.parseColor("#ffffff"));
//                        goBuyStatus = LiJiGouMai;
//                    }else{
//                        Log.i("有价格 直播结束 无回放=== ", " 有价格 直播结束 无回放");
//                        go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
//                        ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
//                        tel_im.setVisibility(View.GONE);
//                        TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
//                        buy_immediately_bt.setText("     已结束");
//                        buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
//                        goBuyStatus = WuHuiFang;
//                    }
                }

            }
        }


        gobuyClick.setOnClickListener(this);
        //收藏
        good_collection_llyt = (LinearLayout) findViewById(R.id.good_collection_llyt);
        collect_icon = (ImageView) findViewById(R.id.collect_icon);
        collect_tv = (TextView) findViewById(R.id.collect_tv);
        // 收藏情况
        int HasCollection = LiveDetailList.get(0).getBody().getClassInfo().getHasCollection();
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


    class CollectionTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            type = params[0];
            Log.i("type == ", "" + type);
            int class_pid = LiveDetailList.get(0).getBody().getClassInfo().getClass_PKID();
            try {
                productCollection = httpUtils.getProductCollection(Const.URL + Const.GetProductCollectionAPI, getUserID(), class_pid, 1, type);
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
            } else {
                Toast.makeText(LiveClassActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
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
                Intent ServerIntent = new Intent();
                ServerIntent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Const.ServerH5);
                ServerIntent.setData(content_url);
                startActivity(ServerIntent);
                break;

            case R.id.go_buy_Click:
                if (0 != getUserID()) {
                    if (goBuyStatus == LiJiGouMai) {
                        Log.i(" LiJiGouMai ", " ==== ");
                        if (isSign()) {
                            //判断价格是否为0
                            String MinSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MinSalePrice();
                            String MaxSalePrice = LiveDetailList.get(0).getBody().getClassInfo().getClass_MaxSalePrice();
                            int ClassId = LiveDetailList.get(0).getBody().getClassInfo().getClass_PKID();
                            String imageUrl = LiveDetailList.get(0).getBody().getClassInfo().getClass_MobileImage();
                            String minPr = LiveDetailList.get(0).getBody().getClassInfo().getClass_MinSalePrice();
                            String maxPr = LiveDetailList.get(0).getBody().getClassInfo().getClass_MaxSalePrice();
                            String price = minPr + " - " + maxPr;
                            String title = LiveDetailList.get(0).getBody().getClassInfo().getClass_Name();
                            if ("0".equals(MinSalePrice) && "0".equals(MaxSalePrice)) {
                                ZeroBuyDialog dialog = new ZeroBuyDialog(this);
                                dialog.show();
                            } else {
                                Intent addcart = new Intent(this, ClassCourseCartActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("type", "live");
                                bundle.putInt("ClassId", ClassId);
                                bundle.putString("imageUrl", imageUrl);
                                bundle.putString("title", title);
                                bundle.putString("price", price);
                                addcart.putExtras(bundle);
                                startActivity(addcart);
                            }
                        } else {
                            LoginDialog dialog = new LoginDialog(this);
                            dialog.show();
                        }
                    } else if (goBuyStatus == LiJiYuYue) {
                        Log.i(" LiJiYuYue ", " ==== ");
                        if (httpUtils.isNetworkConnected(this)) {
                            LJYYTask collectionTask = new LJYYTask();
                            collectionTask.execute();
                        }
                    } else if (goBuyStatus == JinRuZB) {
                        Log.i(" JinRuZB ", " ==== ");
                        int classid = LiveDetailList.get(0).getBody().getClassInfo().getClass_PKID();
                        int scheduleId = LiveDetailList.get(0).getBody().getSchedule().getCS_PKID();
//                    Intent intent = new Intent(this, LiveH5Activity.class);
                        Intent intent = new Intent(this, LiveRoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("scheduleId", scheduleId);
                        bundle.putInt("classid", classid);
                        bundle.putInt("searchType", 0);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (goBuyStatus == ChaKanHuiFang) {
                        Log.i(" ChaKanHuiFang ", " ==== ");
                        int classid = LiveDetailList.get(0).getBody().getClassInfo().getClass_PKID();
                        int scheduleId = LiveDetailList.get(0).getBody().getSchedule().getCS_PKID();
                        Log.i("classid ",""+ classid);
                        Log.i("scheduleId ","" + scheduleId);

//                    Intent intent = new Intent(this, LiveH5Activity.class);
                        Intent intent = new Intent(this, VodRoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("scheduleId", scheduleId);
                        bundle.putInt("classId", classid);
                        bundle.putInt("searchType", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {//去登陆
                    Intent layoutIntent = new Intent(this, NormalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Details", "10001");
                    layoutIntent.putExtras(bundle);
                    startActivityForResult(layoutIntent, 10001);
                }




                break;

            case top_back_RelativeLayout:
                finish();
                break;

            case top_back:
                finish();
                break;
        }
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


    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.i("分享 == > ", "成功了");
//            Toast.makeText(LiveClassActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.i("分享 == > ", "失败");
//            Toast.makeText(LiveClassActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.i("分享 == > ", "取消");
//            Toast.makeText(LiveClassActivity.this, "取消了", Toast.LENGTH_LONG).show();

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
        linearLayout.setPadding(0, 50, 0, 0);
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
    protected void onResume() {
        super.onResume();
        if (httpUtils.isNetworkConnected(this)) {

        } else {
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


    class LJYYTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int class_pid = LiveDetailList.get(0).getBody().getClassInfo().getClass_PKID();
            try {
                addLiveBook = httpUtils.addlivebook(Const.URL + Const.AddLiveBookAPI, getUserID(), class_pid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == addLiveBook.getCode()) {
                go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
                ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
                tel_im.setVisibility(View.GONE);
                TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
                buy_immediately_bt.setText("已预约");
                buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
                goBuyStatus = YiYuYue;
            }

        }
    }

}

