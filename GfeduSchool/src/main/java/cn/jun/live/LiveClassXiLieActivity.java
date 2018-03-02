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
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
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
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.gfedu.gfeduapp.NetActivity;
import cn.jun.bean.AddLiveBook;
import cn.jun.bean.ClassDetailBean;
import cn.jun.bean.ClassOutLineBean;
import cn.jun.bean.Const;
import cn.jun.bean.GetLiveDetailBean;
import cn.jun.bean.ProductCollection;
import cn.jun.courseinfo.activity.ClassCourseCartActivity;
import cn.jun.courseinfo.adapter.ExpandableListAdapter;
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
 * 直播(系列)
 **/
public class LiveClassXiLieActivity extends NetActivity implements View.OnClickListener, ExpandableListView.OnGroupExpandListener,
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
    //数据源
    private ClassDetailBean classDetailBean = new ClassDetailBean();
    private ArrayList<ClassDetailBean> ClassDetailList;
    private ScrollViewExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private View vHead;
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
    //传递的班级ID
    private int Product_PKID;
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


    private int class_idALL;
    private int ClassTypeIdALL;
    private int ChildClassTypeIdALL;
    private int ScheduleIdALL;
    private int LevelIdALL;


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
        setContentView(R.layout.course_details_alone_activity_two);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            Product_PKID = bundle.getInt("Class_PKID", 0);
        }
//
//
//        //新的隐藏标题方法
//        getSupportActionBar().hide();

        no_header_layout = (LinearLayout) findViewById(R.id.no_header_layout);
        wb = (WebView) findViewById(R.id.CourseDetailsWb);
        expandableListView = (ScrollViewExpandableListView) findViewById(R.id.myList);

        if (httpUtils.isNetworkConnected(this)) {
            showProcessDialog(LiveClassXiLieActivity.this,
                    R.layout.loading_show_dialog_color);
            initData();
        } else {//无网络不能进入
            finish();
            Toast.makeText(this, "网络连接中断,稍后再试!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        // 头布局
        vHead = View.inflate(this, R.layout.alone_headerview, null);
        //获取班级详情
        PackageDetailTask packageDetail = new PackageDetailTask();
        packageDetail.execute(Product_PKID);
        //获取班级大纲
        ClassOutLineTask classoutTask = new ClassOutLineTask();
        classoutTask.execute(Product_PKID);
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
//        //名称
//        String childName = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getLevel_ShowName();
//        //视频VID
//        String childVID = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getVID();
//        //类型
////        int childKeyType = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getKeyType();
//        int childKeyType = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getChildClassTypeType();
//        //文件ID
//        int childKeyID = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getKeyID();
//        //是否免费试听
//        int isFree = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getIsFree();
//        if (1 == childKeyType) {//1:视频 2:试卷 3:直播 4:资料
//            if (!"".equals(childVID) && null != childVID) {
//                if (1 == isFree) {
//                    C_IjkVideoActicity.intentTo(LiveClassXiLieActivity.this, C_IjkVideoActicity.PlayMode.landScape, C_IjkVideoActicity.PlayType.vid, childVID,
//                            true, childName);
//                } else {
//                    Toast.makeText(LiveClassXiLieActivity.this, "该节课程无试听视频", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(LiveClassXiLieActivity.this, "视频暂时无法播放", Toast.LENGTH_SHORT).show();
//            }
//        } else if (4 == childKeyType) {
//            int class_id = SyllabusDetailsFragmentAloneContent.get(0).getBody().getClassId();
//            int ClassTypeId = SyllabusDetailsFragmentAloneContent.get(0).getBody().getClassTypeId();
//            int ChildClassTypeId = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getChildClassTypeId();
//            int ScheduleId = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getKeyID();
//            String Level_Id = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition).getLevelTwo().get(groupPosition).getList().get(childPosition).getLevel_PKID();
//            int LevelId = Integer.parseInt(Level_Id);
//            if (httpUtils.isNetworkConnected(this)) {
//                class_idALL = class_id;
//                ClassTypeIdALL = ClassTypeId;
//                ChildClassTypeIdALL = ChildClassTypeId;
//                ScheduleIdALL = ScheduleId;
//                LevelIdALL = LevelId;
//                LJYYXL(class_id, ClassTypeId, ChildClassTypeId, ScheduleId, LevelId);
//            }
//        } else {
//            Toast.makeText(LiveClassXiLieActivity.this, "暂无试听内容", Toast.LENGTH_SHORT).show();
//        }
////        Toast.makeText(this,
////                "点击的下标为： parentPosition=" + parentPosition
////                        + "   groupPosition=" + groupPosition
////                        + "   childPosition=" + childPosition + "\n点击的是："
////                        + childName, Toast.LENGTH_SHORT).show();
    }

    class PackageDetailTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int packageId = params[0];
            try {
                classDetailBean = httpUtils.getClassDetailBean(Const.URL + Const.GetClassDetailAPI, getUserID(), packageId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == classDetailBean.getCode()) {
                if (!"".equals(classDetailBean.getBody()) && null != classDetailBean.getBody()) {
                    ClassDetailList = new ArrayList<>();
                    ClassDetailList.add(classDetailBean);
                    initTop_Include_NoHeader();
                    initTop_IncludeHeader();
                    initBottomView();
                } else {
                    Toast.makeText(LiveClassXiLieActivity.this, "暂无课程信息", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }

        }
    }

    class ClassOutLineTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int classid = params[0];
            try {
                classOutLineBean = httpUtils.getClassOutLineBean(Const.URL + Const.GetLiveClassOutLineAPI, getUserID(), classid);
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
//                initBottom_Include();
                }
                initBottom_Include2();
            }
            mDialog.dismiss();

        }
    }

    private void initTop_IncludeHeader() {
        Class_OutlineFreeState = ClassDetailList.get(0).getBody().getClass_OutlineFreeState();
        //免费试听图片
        Header_imageView = (ImageView) vHead.findViewById(R.id.Header_imageView);
        if (1 == Class_OutlineFreeState) {
            Header_imageView.setVisibility(View.VISIBLE);
        } else if (0 == Class_OutlineFreeState) {
            Header_imageView.setVisibility(View.GONE);
        }
        //功能返回键
        ImageView ListHeader_top_back = (ImageView) vHead.findViewById(R.id.header_top_back);
        ListHeader_top_back.setOnClickListener(this);
        RelativeLayout ListHeader_top_back_RelativeLayout = (RelativeLayout) vHead.findViewById(R.id.header_top_back_RelativeLayout);
        ListHeader_top_back_RelativeLayout.setOnClickListener(this);
        //H5视频播放地址或者图片显示地址
        ImageView ListHeader_topIm = (ImageView) vHead.findViewById(R.id.header_course_details_top_im);
        WebView ListHeader_topWb = (WebView) vHead.findViewById(R.id.header_course_details_top_wb);
        final TextView ListHeader_topTv = (TextView) vHead.findViewById(R.id.header_course_details_top_tv);
        ListHeader_topWb.getSettings().setJavaScriptEnabled(true);
        ListHeader_topWb.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        ListHeader_topWb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        ListHeader_topWb.getSettings().setSupportMultipleWindows(true);
        String Package_MobileIsVideo = ClassDetailList.get(0).getBody().getClass_MobileIsVideo();
        if ("1".equals(Package_MobileIsVideo)) {//是视频
//            ListHeader_topWb.setVisibility(View.VISIBLE);
//            ListHeader_topIm.setVisibility(View.GONE);
////            String H5Likn = ClassDetailList.get(0).getBody().getH5VideoLink();
//            String H5Likn = "http://pc.gfedu.cn/home/video?vid=3a5I1XNolzT5ATYf25XBpw==";
//            Log.i("List头部H5页面 == ", "" + H5Likn);
//            ListHeader_topWb.loadUrl(H5Likn);
//            ListHeader_topTv.setVisibility(View.VISIBLE);
//            ListHeader_topWb.setWebViewClient(new WebViewClient() {
//                //覆盖shouldOverrideUrlLoading 方法
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
//            ListHeader_topWb.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    ListHeader_topTv.setVisibility(View.GONE);
//                }
//            });
        } else if ("0".equals(Package_MobileIsVideo)) {//不是视频,加载图片
            ListHeader_topIm.setVisibility(View.VISIBLE);
            ListHeader_topWb.setVisibility(View.GONE);
            ListHeader_topTv.setVisibility(View.GONE);
            String Package_MobileValue = ClassDetailList.get(0).getBody().getClass_MobileValue();
            Log.i("List头部Image_Likn == ", "" + Package_MobileValue);
            Glide.with(LiveClassXiLieActivity.this)
                    .load(Package_MobileValue)
                    .placeholder(R.drawable.pic_kong)
                    .into(ListHeader_topIm);
        }
        //标题
        String titleList = ClassDetailList.get(0).getBody().getClass_Name();
        titleList = titleList.replace("&nbsp;", " ");
        TextView course_details_title = (TextView) vHead.findViewById(R.id.course_details_title);
        course_details_title.setText(titleList);
        //子标题
        TextView course_details_intoName = (TextView) vHead.findViewById(R.id.course_details_intoName);
        String intoNameList = ClassDetailList.get(0).getBody().getClass_IntroName();
        String intoList = ClassDetailList.get(0).getBody().getClass_Intro();
        intoNameList = intoNameList.replace("&nbsp;", " ");
        intoList = intoList.replace("&nbsp;", " ");
        if (("".equals(intoNameList) || null == intoNameList) && ("".equals(intoList) || null == intoList)) {
            course_details_intoName.setVisibility(View.GONE);
        } else {
            course_details_intoName.setVisibility(View.VISIBLE);
        }
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
        //现价
        String MinSalePrice = ClassDetailList.get(0).getBody().getClass_MinSalePrice();
        String MaxSalePrice = ClassDetailList.get(0).getBody().getClass_MaxSalePrice();
        TextView course_details_newprice = (TextView) vHead.findViewById(R.id.course_details_newprice);
        if (MinSalePrice.equals(MaxSalePrice)) {
            course_details_newprice.setText("¥" + MinSalePrice);
        } else {
            course_details_newprice.setText("¥" + MinSalePrice + " - " + MaxSalePrice);
        }
        //原价
        String MinPrice = ClassDetailList.get(0).getBody().getClass_MinPrice();
        String MaxPrice = ClassDetailList.get(0).getBody().getClass_MaxPrice();
        TextView course_details_oldprice = (TextView) vHead.findViewById(R.id.course_details_oldprice);
        if (MinPrice.equals(MaxPrice)) {
            course_details_oldprice.setText("¥" + MinPrice);
        } else {
            course_details_oldprice.setText("¥" + MinPrice + " - " + MaxPrice);
        }
        course_details_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        //课程特色
        LinearLayout Package_FeatureLayout = (LinearLayout) vHead.findViewById(R.id.Package_Feature_RelativeLayout);
        final int WEIGHT = 3;
        if (!"".equals(ClassDetailList.get(0).getBody().getClass_Feature()) && null != ClassDetailList.get(0).getBody().getClass_Feature()) {
            Package_FeatureLayout.setVerticalGravity(View.VISIBLE);
            int size1 = ClassDetailList.get(0).getBody().getClass_Feature().size();
//            int size1 = txstList.size();
            Log.i("size1 === ", "" + size1);
            if (size1 > 0) {
                // 动态添加控件，首先添加第一行布局
                LinearLayout rowLayout = null;
                for (int i = 0; i < size1; i++) {
                    if (i % WEIGHT == 0) {
                        // LinearLayout不能换行,每3个添加一个布局，并加入到外层布局中
                        rowLayout = createImageLayout();
                        Package_FeatureLayout.addView(rowLayout);
                    }
                    View columnLinearLayout = createImage(WEIGHT, ClassDetailList.get(0).getBody().getClass_Feature().get(i));
                    rowLayout.addView(columnLinearLayout);
                }
            }

        } else {
            Package_FeatureLayout.setVerticalGravity(View.GONE);
        }
        //课程星级评价
        ImageView course_details_kcpjxx_im1 = (ImageView) vHead.findViewById(R.id.course_details_kcpjxx_im);
        ImageView course_details_kcpjxx_im2 = (ImageView) vHead.findViewById(R.id.course_details_kcpjxx_im2);
        ImageView course_details_kcpjxx_im3 = (ImageView) vHead.findViewById(R.id.course_details_kcpjxx_im3);
        ImageView course_details_kcpjxx_im4 = (ImageView) vHead.findViewById(R.id.course_details_kcpjxx_im4);
        ImageView course_details_kcpjxx_im5 = (ImageView) vHead.findViewById(R.id.course_details_kcpjxx_im5);
        int Package_EvaluateStar = ClassDetailList.get(0).getBody().getClass_EvaluateStar();
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
        TextView course_details_xxrs = (TextView) vHead.findViewById(R.id.course_details_xxrs);
        int Package_StudyNum = ClassDetailList.get(0).getBody().getClass_StudyNum();
        course_details_xxrs.setText(Package_StudyNum + "");
        //课程详情WebView数据
//        CourseDetailsFragmentAloneContent = ClassDetailList.get(0).getBody().getClass_MobileContent();
        CourseDetailsFragmentAloneContent = ClassDetailList.get(0).getBody().getH5DetailLink();
        //解决顶部空白过多-（暂时处理）
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(CourseDetailsFragmentAloneContent);
        wb.setVisibility(View.VISIBLE);
    }

    private void initTop_Include_NoHeader() {
        Class_OutlineFreeState = ClassDetailList.get(0).getBody().getClass_OutlineFreeState();
        //免费试听图片
        noHeader_imageView = (ImageView) findViewById(R.id.noHeader_imageView);
        if (1 == Class_OutlineFreeState) {
            noHeader_imageView.setVisibility(View.VISIBLE);
        } else if (0 == Class_OutlineFreeState) {
            noHeader_imageView.setVisibility(View.GONE);
        }
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
        String Package_MobileIsVideo = ClassDetailList.get(0).getBody().getClass_MobileIsVideo();
        if ("1".equals(Package_MobileIsVideo)) {//是视频
            topWb.setVisibility(View.VISIBLE);
            topIm.setVisibility(View.GONE);
            String H5Likn = ClassDetailList.get(0).getBody().getH5VideoLink();
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
            String Package_MobileValue = ClassDetailList.get(0).getBody().getClass_MobileValue();
            Log.i("Image_Likn == ", "" + Package_MobileValue);
            Glide.with(LiveClassXiLieActivity.this)
                    .load(Package_MobileValue)
                    .placeholder(R.drawable.pic_kong)
                    .into(topIm);
        }
        //标题
        String titleList = ClassDetailList.get(0).getBody().getClass_Name();
        titleList = titleList.replace("&nbsp;", " ");
        TextView course_details_title = (TextView) findViewById(R.id.course_details_title);
        course_details_title.setText(titleList);
        //子标题
        TextView course_details_intoName = (TextView) findViewById(R.id.course_details_intoName);
        String intoNameList = ClassDetailList.get(0).getBody().getClass_IntroName();
        String intoList = ClassDetailList.get(0).getBody().getClass_Intro();
        intoNameList = intoNameList.replace("&nbsp;", " ");
        intoList = intoList.replace("&nbsp;", " ");
        if (("".equals(intoNameList) || null == intoNameList) && ("".equals(intoList) || null == intoList)) {
            course_details_intoName.setVisibility(View.GONE);
        } else {
            course_details_intoName.setVisibility(View.VISIBLE);
        }
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
        //现价
        String MinSalePrice = ClassDetailList.get(0).getBody().getClass_MinSalePrice();
        String MaxSalePrice = ClassDetailList.get(0).getBody().getClass_MaxSalePrice();
        TextView course_details_newprice = (TextView) findViewById(R.id.course_details_newprice);
        if (MinSalePrice.equals(MaxSalePrice)) {
            course_details_newprice.setText("¥" + MinSalePrice);
        } else {
            course_details_newprice.setText("¥" + MinSalePrice + " - " + MaxSalePrice);
        }
        //原价
        String MinPrice = ClassDetailList.get(0).getBody().getClass_MinPrice();
        String MaxPrice = ClassDetailList.get(0).getBody().getClass_MaxPrice();
        TextView course_details_oldprice = (TextView) findViewById(R.id.course_details_oldprice);
        if (MinPrice.equals(MaxPrice)) {
            course_details_oldprice.setText("¥" + MinPrice);
        } else {
            course_details_oldprice.setText("¥" + MinPrice + " - " + MaxPrice);
        }
        course_details_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        //课程特色
        LinearLayout Package_FeatureLayout = (LinearLayout) findViewById(R.id.Package_Feature_RelativeLayout);
        final int WEIGHT = 3;
        Gson s = new Gson();
        Log.i("dadad ", "" + s.toJson(ClassDetailList.get(0).getBody().getClass_Feature()).toString());
        if (!"".equals(ClassDetailList.get(0).getBody().getClass_Feature()) && null != ClassDetailList.get(0).getBody().getClass_Feature()) {
            Package_FeatureLayout.setVisibility(View.VISIBLE);
            int size = ClassDetailList.get(0).getBody().getClass_Feature().size();
//            int size = txstList.size();
            if (size > 0) {
                // 动态添加控件，首先添加第一行布局
                LinearLayout rowLayout = null;
                for (int i = 0; i < size; i++) {
                    if (i % WEIGHT == 0) {
                        // LinearLayout不能换行,每3个添加一个布局，并加入到外层布局中
                        rowLayout = createImageLayout();
                        Package_FeatureLayout.addView(rowLayout);
                    }
                    View columnLinearLayout = createImage(WEIGHT, ClassDetailList.get(0).getBody().getClass_Feature().get(i));
                    rowLayout.addView(columnLinearLayout);
                }
            }
        } else {
            Package_FeatureLayout.setVisibility(View.GONE);
        }
        //课程星级评价
        ImageView course_details_kcpjxx_im1 = (ImageView) findViewById(R.id.course_details_kcpjxx_im);
        ImageView course_details_kcpjxx_im2 = (ImageView) findViewById(R.id.course_details_kcpjxx_im2);
        ImageView course_details_kcpjxx_im3 = (ImageView) findViewById(R.id.course_details_kcpjxx_im3);
        ImageView course_details_kcpjxx_im4 = (ImageView) findViewById(R.id.course_details_kcpjxx_im4);
        ImageView course_details_kcpjxx_im5 = (ImageView) findViewById(R.id.course_details_kcpjxx_im5);
        int Package_EvaluateStar = ClassDetailList.get(0).getBody().getClass_EvaluateStar();
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
        TextView course_details_xxrs = (TextView) findViewById(R.id.course_details_xxrs);
        int Package_StudyNum = ClassDetailList.get(0).getBody().getClass_StudyNum();
        course_details_xxrs.setText(Package_StudyNum + "");
        //课程详情WebView数据
        CourseDetailsFragmentAloneContent = ClassDetailList.get(0).getBody().getH5DetailLink();
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

        noHeader_btn1 = (RelativeLayout) findViewById(R.id.btn1);
        noHeader_btn2 = (RelativeLayout) findViewById(R.id.btn2);
        noHeader_btn_tv1 = (TextView) findViewById(R.id.btn_tv1);
        noHeader_btn_tv2 = (TextView) findViewById(R.id.btn_tv2);
        noHeader_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noHeader_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
                noHeader_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
                noHeader_btn2.setBackground(getResources().getDrawable(R.color.white));
                noHeader_btn_tv2.setTextColor(Color.parseColor("#666666"));
                no_header_layout.setVisibility(View.VISIBLE);
                wb.setVisibility(View.VISIBLE);
                expandableListView.setVisibility(View.GONE);

            }
        });
        noHeader_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noHeader_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
                noHeader_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
                noHeader_btn1.setBackground(getResources().getDrawable(R.color.white));
                noHeader_btn_tv1.setTextColor(Color.parseColor("#666666"));
                no_header_layout.setVisibility(View.GONE);
                wb.setVisibility(View.GONE);
//                wb.setVisibility(View.VISIBLE);

                expandableListView.setVisibility(View.VISIBLE);

                header_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
                header_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
                header_btn1.setBackground(getResources().getDrawable(R.color.white));
                header_btn_tv1.setTextColor(Color.parseColor("#666666"));
            }
        });
    }

    private void initBottom_Include2() {
//        wb.getSettings().setJavaScriptEnabled(true);
//        wb.loadUrl(CourseDetailsFragmentAloneContent);
//        wb.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                wb.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
//                super.onPageFinished(view, url);
//            }
//        });
//        wb.addJavascriptInterface(this, "App");
//        //重新测量
//        wb.measure(WebView_W, WebView_H);

        expandableListView.setDivider(null);
        expandableListView.setGroupIndicator(null);
//        头布局放入listView中
        expandableListView.addHeaderView(vHead);
//        if (!"".equals(SyllabusDetailsFragmentAloneContent) && null != SyllabusDetailsFragmentAloneContent) {
        Gson s = new Gson();
        Log.i("mList == 222", "" + s.toJson(SyllabusDetailsFragmentAloneContent).toString());
        adapter = new ExpandableListAdapter(LiveClassXiLieActivity.this, SyllabusDetailsFragmentAloneContent);
        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        adapter.setOnChildTreeViewClickListener(LiveClassXiLieActivity.this);
//        }

        header_btn1 = (RelativeLayout) vHead.findViewById(R.id.btn1);
        header_btn2 = (RelativeLayout) vHead.findViewById(R.id.btn2);
        header_btn_tv1 = (TextView) vHead.findViewById(R.id.btn_tv1);
        header_btn_tv2 = (TextView) vHead.findViewById(R.id.btn_tv2);
        header_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
                header_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
                header_btn2.setBackground(getResources().getDrawable(R.color.white));
                header_btn_tv2.setTextColor(Color.parseColor("#666666"));
                no_header_layout.setVisibility(View.VISIBLE);
                wb.setVisibility(View.VISIBLE);
                expandableListView.setVisibility(View.GONE);

                noHeader_btn1.setBackgroundResource(R.drawable.relative_bg_shape);
                noHeader_btn_tv1.setTextColor(Color.parseColor("#dd5555"));
                noHeader_btn2.setBackground(getResources().getDrawable(R.color.white));
                noHeader_btn_tv2.setTextColor(Color.parseColor("#666666"));
            }
        });
        header_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_btn2.setBackgroundResource(R.drawable.relative_bg_shape);
                header_btn_tv2.setTextColor(Color.parseColor("#dd5555"));
                header_btn1.setBackground(getResources().getDrawable(R.color.white));
                header_btn_tv1.setTextColor(Color.parseColor("#666666"));
                no_header_layout.setVisibility(View.GONE);
                wb.setVisibility(View.GONE);
                expandableListView.setVisibility(View.VISIBLE);
            }
        });

    }


    private void initBottomView() {
//        addshopClick = (LinearLayout) findViewById(R.id.add_shop_car_llyt);
//        addshopClick.setOnClickListener(this);
        //分享
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
        int HasCollection = ClassDetailList.get(0).getBody().getHasCollection();
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
            int class_pid = ClassDetailList.get(0).getBody().getClass_PKID();
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
                Toast.makeText(LiveClassXiLieActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
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
                if (isSign()) {
                    //判断价格是否为0
                    String MinSalePrice = ClassDetailList.get(0).getBody().getClass_MinSalePrice();
                    String MaxSalePrice = ClassDetailList.get(0).getBody().getClass_MaxSalePrice();
                    int ClassId = ClassDetailList.get(0).getBody().getClass_PKID();
                    String imageUrl = ClassDetailList.get(0).getBody().getClass_MobileImage();
                    String minPr = ClassDetailList.get(0).getBody().getClass_MinSalePrice();
                    String maxPr = ClassDetailList.get(0).getBody().getClass_MaxSalePrice();
                    String price = minPr + " - " + maxPr;
                    String title = ClassDetailList.get(0).getBody().getClass_Name();
                    if ("0".equals(MinSalePrice) && "0".equals(MaxSalePrice)) {
                        ZeroBuyDialog dialog = new ZeroBuyDialog(this);
                        dialog.show();
                    } else {
                        Intent addcart = new Intent(this, ClassCourseCartActivity.class);
                        Bundle bundle = new Bundle();
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
//                    Toast.makeText(LiveClassActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-700-9596"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
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


//    public static String getNewContent(String htmltext) {
//        try {
//            Document doc = Jsoup.parse(htmltext);
//            Elements elements = doc.getElementsByTag("img");
//            for (Element element : elements) {
//                element.attr("width", "100%").attr("height", "auto");
//            }
//
//            return doc.toString();
//        } catch (Exception e) {
//            return htmltext;
//        }
//    }

    @JavascriptInterface
    public void resize(final float height) {
        Log.i("resize --- > ", " resize ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(LiveClassActivity.this, height + "", Toast.LENGTH_LONG).show();
                wb.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));

                WebView_W = getResources().getDisplayMetrics().widthPixels;
                WebView_H = (int) (height * getResources().getDisplayMetrics().density);

            }
        });
    }


    private void showPopupCommnet() {
        View view = LayoutInflater.from(LiveClassXiLieActivity.this).inflate(
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


    private AddLiveBook addLiveBook = new AddLiveBook();
    private ArrayList<GetLiveDetailBean> LiveDetailList = new ArrayList<>();

    public void LJYYXL(final int classid, final int ClassTypeId, final int ChildClassTypeId, final int ScheduleId, final int LevelId) {
        LJYYTask collectionTask = new LJYYTask();
        collectionTask.execute();
    }


    class LJYYTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                addLiveBook = httpUtils.addlivebookXL(Const.URL + Const.ClassLiveBookAPI, getUserID(), class_idALL, ClassTypeIdALL, ChildClassTypeIdALL, ScheduleIdALL, LevelIdALL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == addLiveBook.getCode()) {
//                    expandableListView.expandGroup(0);
                //获取班级大纲
                ClassOutLineTask classoutTask = new ClassOutLineTask();
                classoutTask.execute(Product_PKID);
                Toast.makeText(LiveClassXiLieActivity.this, addLiveBook.getMessage(), Toast.LENGTH_SHORT).show();
//                adapter.notifyDataSetChanged();
//                    go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
//                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
//                    tel_im.setVisibility(View.GONE);
//                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
//                    buy_immediately_bt.setText("已预约");
//                    buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
//                    goBuyStatus = YiYuYue;
            }else{
                Toast.makeText(LiveClassXiLieActivity.this, addLiveBook.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


}

