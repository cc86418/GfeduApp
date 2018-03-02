package jc.cici.android.atom.ui.tiku.newDoExam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gfedu.home_pager.BaseFragment;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.ImageRecyclerAdapter;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.QuesAnalysisBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.Const;
import jc.cici.android.atom.ui.tiku.NetUtil;
import jc.cici.android.atom.ui.tiku.SubmitQuesAnswer;
import jc.cici.android.atom.ui.tiku.TestWebViewClient;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.FullGridLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static cn.gfedu.login.CodeMainActivity.TAG;

/**
 * 每日一题or每月一测fragment
 * Created by atom on 2018/1/16.
 */

public class EveryOrMothFragment extends BaseFragment {

    private Context mCtx;
    private ScrollView judgescrollView;
    private ScrollView danxuanscrollView;
    private ScrollView mulscrollView;
    private ScrollView jiandascrollView;
    // 简答webView
    private WebView jianda_webView;
    // 简答editText
    private static EditText edit_answer;
    // 简答添加图片
    private RecyclerView recyclerView;
    private RelativeLayout add_icon_layout, grid_layout;
    // 填空题未上传图片情况
    private ImageView icon_img;
    // 多选webView
    private WebView double_webView;
    // 单选webView
    private WebView test_webView;
    // 按钮布局
    private RelativeLayout answer_layout;
    // 解析按钮布局
    private RelativeLayout submit_btn_layout;
    // 判断题webView
    private WebView judge_webView;
    // 单选答案选项布局
    private LinearLayout btnA_layout;
    private LinearLayout btnB_layout;
    private LinearLayout btnC_layout;
    private LinearLayout btnD_layout;
    private LinearLayout btnE_layout;
    // 多选答案选项布局
    private LinearLayout mul_btnA_layout;
    private LinearLayout mul_btnB_layout;
    private LinearLayout mul_btnC_layout;
    private LinearLayout mul_btnD_layout;
    private LinearLayout mul_btnE_layout;
    // 答案选项A
    private Button btn_A, mul_btn_A;
    // 答案选项B
    private Button btn_B, mul_btn_B;
    // 答案选项C
    private Button btn_C, mul_btn_C;
    // 答案选项D
    private Button btn_D, mul_btn_D;
    // 答案选项E
    private Button btn_E, mul_btn_E;
    // 用户id
    private int userId;
    // 试题号
    private int quesId;
    private int isDoQues;
    // 试卷类型
    private int quesType;
    private int searchType;
    // 答案个数
    private int quesOptionCount;
    // 试卷链接
    private String quesUrl;
    // 试卷名称
    private String title;
    // 试卷id
    private int paperId;
    // 判断题正确选项按钮
    private Button btnA_judge;
    // 判断题错误选项按钮
    private Button btnB_judge;
    // 多选题按钮是否选中
    private boolean multiselect_btnA = false;
    private boolean multiselect_btnB = false;
    private boolean multiselect_btnC = false;
    private boolean multiselect_btnD = false;
    private boolean multiselect_btnE = false;
    private boolean multiselect_btnF = false;
    // 多选按钮设置传递给服务器
    private String multiselect_btnA_String = "";
    private String multiselect_btnB_String = "";
    private String multiselect_btnC_String = "";
    private String multiselect_btnD_String = "";
    private String multiselect_btnE_String = "";
    private String multiselect_btnF_String = "";

    // 判断/单选/多选题/简答题解析按钮
    private Button judge_submit_btn, danxuan_submit_btn, double_submit_btn, jianda_submit_btn;
    // 判断/单选/多选/简答解析布局
    private RelativeLayout judge_answer_webView_layout, danxuan_answer_webView_layout, double_answer_webView_layout, jianda_answer_webView_layout;
    // 判断/ 单选/多选/简答题解析webView
    private WebView judge_answer_webView, danxuan_answer_webView, double_answer_webView, jianda_answer_webView;

    private View view;
    private Handler mHandler;

    // 当前选中图片个数
    private TextView selTxt;
    // 默认最大选中个数
    private int maxSelectNum = 3;
    // 图片适配器
    private FullGridLayoutManager gridLayoutManager;
    // 图片适配器对象
    private ImageRecyclerAdapter adapter;
    // 图片列表
    private List<LocalMedia> itemList = new ArrayList<>();
    // 上传图片列表
    public static Map<Integer, ArrayList<String>> imgList = new HashMap<>();
    // 输入框内容列表
    public static Map<Integer, String> stringList = new HashMap<>();
    private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                selTxt.setText("已选中" + itemList.size() + "个，当前可以选中" + (maxSelectNum - (itemList.size())));
                ArrayList<String> strImg = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    String imgUrl = itemList.get(i).getCompressPath();
                    try {
                        strImg.add(ToolUtils.ioToBase64(imgUrl));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                imgList.put(0, strImg);
            }
        }
    };

    public EveryOrMothFragment(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = getActivity();
        userId = getArguments().getInt("userId", 0);
        // 试卷题目
        title = getArguments().getString("title");
        // 试题号
        quesId = getArguments().getInt("quesId", 0);
        paperId = getArguments().getInt("paperId", 0);
        isDoQues = getArguments().getInt("isDoQues", 0);
        // 试题类型(单选，多选，填空，简答等)
        quesType = getArguments().getInt("quesType", 0);
        // 选项数量
        quesOptionCount = getArguments().getInt("quesOptionCount", 0);
        // 试题链接
        quesUrl = getArguments().getString("quesUrl");
        // 解析来源
        searchType = getArguments().getInt("searchType", 0);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (quesType == 1) { // 判断答题布局
            if (null == view) {
                view = inflater.inflate(R.layout.everyday_rightorerror_view,
                        null);
                judge_webView = (WebView) view
                        .findViewById(R.id.judge_webView);
                judgescrollView = (ScrollView) view
                        .findViewById(R.id.judgescrollView);
                answer_layout = (RelativeLayout) view.findViewById(R.id.answer_layout);
                submit_btn_layout = (RelativeLayout) view.findViewById(R.id.submit_btn_layout);
                btnA_judge = (Button) view.findViewById(R.id.btnA_judge);
                btnB_judge = (Button) view.findViewById(R.id.btnB_judge);
                judge_submit_btn = (Button) view.findViewById(R.id.judge_submit_btn);
                judge_answer_webView_layout = (RelativeLayout) view.findViewById(R.id.judge_answer_webView_layout);
                judge_answer_webView = (WebView) view.findViewById(R.id.judge_answer_webView);
                if (searchType == 2 || isDoQues != 0) { // 查看解析
                    getAnalysisContent();
                } else {
                    // webView基本设置
                    webViewSetting(judge_webView, quesUrl);
                    judgescrollView.scrollTo(0, 0);
                    // 正确判断按钮设置监听
                    setOnclick(btnA_judge);
                    // 错误判断按钮设置监听
                    setOnclick(btnB_judge);
                    // 查看解析按钮监听
                    setOnclick(judge_submit_btn);
                }
            }
            // 清除父类缓存中已存在的状态
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        } else if (quesType == 2) { // 单选答题布局
            if (null == view) {
                view = inflater
                        .inflate(R.layout.everyday_danxuan_view, null);
                test_webView = (WebView) view
                        .findViewById(R.id.test_webView);
                danxuanscrollView = (ScrollView) view
                        .findViewById(R.id.danxuanscrollView);
                answer_layout = (RelativeLayout) view.findViewById(R.id.answer_layout);
                submit_btn_layout = (RelativeLayout) view.findViewById(R.id.submit_btn_layout);
                btnA_layout = (LinearLayout) view
                        .findViewById(R.id.btnA_layout);
                btnB_layout = (LinearLayout) view
                        .findViewById(R.id.btnB_layout);
                btnC_layout = (LinearLayout) view
                        .findViewById(R.id.btnC_layout);
                btnD_layout = (LinearLayout) view
                        .findViewById(R.id.btnD_layout);
                btnE_layout = (LinearLayout) view
                        .findViewById(R.id.btnE_layout);

                btn_A = (Button) view.findViewById(R.id.btn_A);
                btn_B = (Button) view.findViewById(R.id.btn_B);
                btn_C = (Button) view.findViewById(R.id.btn_C);
                btn_D = (Button) view.findViewById(R.id.btn_D);
                btn_E = (Button) view.findViewById(R.id.btn_E);

                danxuan_submit_btn = (Button) view.findViewById(R.id.danxuan_submit_btn);
                danxuan_answer_webView_layout = (RelativeLayout) view.findViewById(R.id.danxuan_answer_webView_layout);
                danxuan_answer_webView = (WebView) view.findViewById(R.id.danxuan_answer_webView);
                if (searchType == 2 || isDoQues != 0) {
                    getAnalysisContent();
                } else {
                    webViewSetting(test_webView, quesUrl);
                    danxuanscrollView.scrollTo(0, 0);
                    // 答案选项设置是否可显示
                    switch (quesOptionCount) {
                        case 1:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.GONE);
                            btnC_layout.setVisibility(View.GONE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 2:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.GONE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 3:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.GONE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 4:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.VISIBLE);
                            btnE_layout.setVisibility(View.GONE);
                            break;
                        case 5:
                            btnA_layout.setVisibility(View.VISIBLE);
                            btnB_layout.setVisibility(View.VISIBLE);
                            btnC_layout.setVisibility(View.VISIBLE);
                            btnD_layout.setVisibility(View.VISIBLE);
                            btnE_layout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                    // 按钮设置监听
                    setOnclick(btn_A);
                    setOnclick(btn_B);
                    setOnclick(btn_C);
                    setOnclick(btn_D);
                    setOnclick(btn_E);
                    setOnclick(danxuan_submit_btn);
                }
            }
            // 清除父类缓存中已存在的状态
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        } else if (quesType == 3) { // 多选题布局
            if (null == view) {
                view = inflater
                        .inflate(R.layout.everyday_double_view, null);
                double_webView = (WebView) view
                        .findViewById(R.id.double_webView);
                mulscrollView = (ScrollView) view
                        .findViewById(R.id.mulscrollView);
                answer_layout = (RelativeLayout) view.findViewById(R.id.answer_layout);
                submit_btn_layout = (RelativeLayout) view.findViewById(R.id.submit_btn_layout);
                // 多选答案选项获取id
                mul_btnA_layout = (LinearLayout) view
                        .findViewById(R.id.mul_btnA_layout);
                mul_btnB_layout = (LinearLayout) view
                        .findViewById(R.id.mul_btnB_layout);
                mul_btnC_layout = (LinearLayout) view
                        .findViewById(R.id.mul_btnC_layout);
                mul_btnD_layout = (LinearLayout) view
                        .findViewById(R.id.mul_btnD_layout);
                mul_btnE_layout = (LinearLayout) view
                        .findViewById(R.id.mul_btnE_layout);
                // 选项按钮获取id
                mul_btn_A = (Button) view.findViewById(R.id.mul_btn_A);
                mul_btn_B = (Button) view.findViewById(R.id.mul_btn_B);
                mul_btn_C = (Button) view.findViewById(R.id.mul_btn_C);
                mul_btn_D = (Button) view.findViewById(R.id.mul_btn_D);
                mul_btn_E = (Button) view.findViewById(R.id.mul_btn_E);

                double_submit_btn = (Button) view.findViewById(R.id.double_submit_btn);
                double_answer_webView_layout = (RelativeLayout) view.findViewById(R.id.double_answer_webView_layout);
                double_answer_webView = (WebView) view.findViewById(R.id.double_answer_webView);

                if (searchType == 2 || isDoQues != 0) {
                    getAnalysisContent();
                } else {
                    // webView基本设置
                    webViewSetting(double_webView, quesUrl);
                    mulscrollView.scrollTo(0, 0);
                    // 多选按钮显示设置
                    switch (quesOptionCount) {
                        case 1:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.GONE);
                            mul_btnC_layout.setVisibility(View.GONE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 2:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.GONE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 3:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.GONE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 4:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.VISIBLE);
                            mul_btnE_layout.setVisibility(View.GONE);
                            break;
                        case 5:
                            mul_btnA_layout.setVisibility(View.VISIBLE);
                            mul_btnB_layout.setVisibility(View.VISIBLE);
                            mul_btnC_layout.setVisibility(View.VISIBLE);
                            mul_btnD_layout.setVisibility(View.VISIBLE);
                            mul_btnE_layout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }

                    // 按钮设置监听
                    setOnclick(mul_btn_A);
                    setOnclick(mul_btn_B);
                    setOnclick(mul_btn_C);
                    setOnclick(mul_btn_D);
                    setOnclick(mul_btn_E);
                }
            }
            // 清除父类缓存中已存在的状态
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        } else if (quesType == 6) { // 简答题
            if (null == view) {
                view = inflater.inflate(R.layout.evertdat_jianda_view, null);
                jianda_webView = (WebView) view
                        .findViewById(R.id.jianda_webView);
                jiandascrollView = (ScrollView) view
                        .findViewById(R.id.jiandascrollView);
                edit_answer = (EditText) view
                        .findViewById(R.id.edit_answer);
                icon_img = (ImageView) view.findViewById(R.id.icon_img);
                recyclerView = (RecyclerView) view
                        .findViewById(R.id.recyclerView);
                selTxt = (TextView) view.findViewById(R.id.selTxt);
                answer_layout = (RelativeLayout) view.findViewById(R.id.answer_layout);
                grid_layout = (RelativeLayout) view.findViewById(R.id.grid_layout);
                add_icon_layout = (RelativeLayout) view.findViewById(R.id.add_icon_layout);
                submit_btn_layout = (RelativeLayout) view.findViewById(R.id.submit_btn_layout);
                jianda_submit_btn = (Button) view.findViewById(R.id.jianda_submit_btn);
                jianda_answer_webView_layout = (RelativeLayout) view.findViewById(R.id.jianda_answer_webView_layout);
                jianda_answer_webView = (WebView) view.findViewById(R.id.jianda_answer_webView);
                if (searchType == 2 || isDoQues != 0) {
                    getAnalysisContent();
                } else {
                    // 填空题webView基本设置
                    webViewSetting(jianda_webView, quesUrl);
                    jiandascrollView.scrollTo(0, 0);
                    selTxt.setText("已选中0个，当前可以选中" + maxSelectNum);
                    // 创建GridLayoutManger 管理器对象 并设置每行显示3个
                    gridLayoutManager = new FullGridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
                    // 添加管理器
                    recyclerView.setLayoutManager(gridLayoutManager);
                    // 创建适配器
                    adapter = new ImageRecyclerAdapter(mCtx, onPicClickListener, cHandler);
                    // 添加数据
                    adapter.setList(itemList);
                    // 添加默认个数
                    adapter.setSelectMax(3);
                    // 加入适配器
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new ImageRecyclerAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(int position, View v) {

                            if (itemList.size() > 0) {
                                LocalMedia localMedia = itemList.get(position);
                                String pictureType = localMedia.getPictureType();
                                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                                switch (mediaType) {
                                    case 1: // 预览照片
                                        PictureSelector.create((Activity) mCtx).externalPicturePreview(position, itemList);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
                    edit_answer.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s,
                                                      int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String strCon = s.toString().trim();
                            stringList.put(0, strCon);
                        }
                    });
                }
                jianda_submit_btn.setEnabled(true);
                jianda_submit_btn.setClickable(true);
                setOnclick(jianda_submit_btn);
            }
            // 清除父类缓存中已存在的状态
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        return null;
    }

    //     创建图片点击时间
    private ImageRecyclerAdapter
            .onAddPicClickListener onPicClickListener = new ImageRecyclerAdapter
            .onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(EveryOrMothFragment.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .selectionMedia(itemList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    itemList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(itemList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + itemList.size());
                    selTxt.setText("已选中" + itemList.size() + "个，当前可以选中" + (maxSelectNum - (itemList.size())));
                    // 获取上传图片列表
                    if (itemList.size() > 0) {
                        ArrayList<String> imgStrList = new ArrayList<>();
                        for (int i = 0; i < itemList.size(); i++) { // 遍历并转化为Base64
                            String imgUrl = itemList.get(i).getCompressPath();
                            try {
                                imgStrList.add(ToolUtils.ioToBase64(imgUrl));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        imgList.put(0, imgStrList);
                    }
                    break;
            }
        }
    }

    /**
     * webView进本设置
     *
     * @param webView
     * @param url
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSetting(final WebView webView, String url) {
        System.out.println("h5Str >>>:" + url);
        // 设置编码格式
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        // 设置
        webView.getSettings().setBuiltInZoomControls(false);
        // 支持js
        webView.getSettings().setJavaScriptEnabled(true);
        // 水平不显示
        webView.setHorizontalScrollBarEnabled(false);
        // 垂直不显示
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // 添加webView
        if (NetUtil.isMobileConnected(mCtx)) {
            webView.loadUrl(url);
            webView.setWebViewClient(new TestWebViewClient(mCtx, mHandler));
        } else {
            Toast.makeText(mCtx, "加载本地图片", Toast.LENGTH_SHORT).show();
        }

        /**
         * 设置取消复制黏贴功能
         */
        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    /**
     * 按钮设置监听
     *
     * @param btn
     */
    private void setOnclick(final Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnA_judge: // 正确判断按钮A监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("A");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        setSelectedButtonBackgroundResource(btnA_judge, "A");
                        setCommButtonBackgroudResource(btnB_judge, "B");

                        break;
                    case R.id.btnB_judge: // 错误判断按钮B监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("B");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        setSelectedButtonBackgroundResource(btnB_judge, "B");
                        setCommButtonBackgroudResource(btnA_judge, "A");
                        break;
                    case R.id.btn_A: // 选项按钮A监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("A");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色A选中，B、C、D、E 正常
                        setSelectedButtonBackgroundResource(btn_A, "A");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_B: // 选项按钮B监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("B");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色B选中，A、C、D、E 正常
                        setSelectedButtonBackgroundResource(btn_B, "B");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_C: // 选项按钮C监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("C");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色C选中，A、B、D、E 正常
                        setSelectedButtonBackgroundResource(btn_C, "C");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_D, "D");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_D: // 选项按钮D监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("D");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色D选中，A、B、C、E 正常
                        setSelectedButtonBackgroundResource(btn_D, "D");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_E, "E");
                        break;
                    case R.id.btn_E: // 选项按钮E监听
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitSingleAnswer("E");
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 设置选中颜色E选中，A、B、C、D 正常
                        setSelectedButtonBackgroundResource(btn_E, "E");
                        // 未选中按钮颜色设置
                        setCommButtonBackgroudResource(btn_A, "A");
                        setCommButtonBackgroudResource(btn_B, "B");
                        setCommButtonBackgroudResource(btn_C, "C");
                        setCommButtonBackgroudResource(btn_D, "D");
                        break;
                    case R.id.mul_btn_A: // 多选按钮A设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnA == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_A);
                            multiselect_btnA = true;
                            multiselect_btnA_String = "A、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_A);
                            multiselect_btnA = false;
                            multiselect_btnA_String = "";
                        }
                        // 设置提交答案
                        if (NetUtil.isMobileConnected(mCtx)) {
                            // 多选提交
                            submitMulAnswer();
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_B: // 多选按钮B设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnB == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_B);
                            multiselect_btnB = true;
                            multiselect_btnB_String = "B、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_B);
                            multiselect_btnB = false;
                            multiselect_btnB_String = "";
                        }
                        // 设置提交答案
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitMulAnswer();
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_C: // 多选按钮C设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnC == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_C);
                            multiselect_btnC = true;
                            multiselect_btnC_String = "C、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_C);
                            multiselect_btnC = false;
                            multiselect_btnC_String = "";
                        }
                        // 设置提交答案
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitMulAnswer();
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_D: // 多选按钮D设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnD == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_D);
                            multiselect_btnD = true;
                            multiselect_btnD_String = "D、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_D);
                            multiselect_btnD = false;
                            multiselect_btnD_String = "";
                        }
                        // 设置提交答案
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitMulAnswer();
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mul_btn_E: // 多选按钮E设置监听
                        // 按钮(未)选中状态设置
                        if (multiselect_btnE == false) {
                            setMulSelectButtonBackgroudResource(mul_btn_E);
                            multiselect_btnE = true;
                            multiselect_btnE_String = "E、";
                        } else {
                            setMulCommButtonBackgroundResource(mul_btn_E);
                            multiselect_btnE = false;
                            multiselect_btnE_String = "";
                        }
                        // 设置提交答案
                        if (NetUtil.isMobileConnected(mCtx)) {
                            submitMulAnswer();
                        } else {
                            Toast.makeText(mCtx, "网络断开连接无法提交,请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.judge_submit_btn: //判断查看解析监听
                        getAnalysisContent();
                        break;
                    case R.id.danxuan_submit_btn: //单选查看解析监听
                        getAnalysisContent();
                        break;
                    case R.id.double_submit_btn: //多选查看解析监听
                        getAnalysisContent();
                        break;
                    case R.id.jianda_submit_btn: // 简答题
                        jiandaSubmit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void jiandaSubmit() {
        // 添加网络请求
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Const.URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        try {
            CommParam commParam = new CommParam(getActivity());
            obj.put("userId", commParam.getUserId());
            obj.put("appName", commParam.getAppname());
            obj.put("oauth", commParam.getOauth());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("version", commParam.getVersion());
            // 试卷id
            obj.put("paperId", paperId);
            // 试题id
            obj.put("quesId", quesId);
            // 用户答案
            obj.put("userQuesAnswer", stringList.get(0));
            JSONArray array = new JSONArray(imgList.get(0));
            obj.put("base64Img", array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<SubmitQuesAnswer>> observable = httpPostService.getSubmitUserqQuesAnswer(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubmitQuesAnswer>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mCtx, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<SubmitQuesAnswer> submitQuesAnswerCommonBean) {
                                if (100 == submitQuesAnswerCommonBean.getCode()) {
                                    getAnalysisContent();
                                    // 清除缓存
                                    PictureFileUtils.deleteCacheDirFile(mCtx);
                                } else {
                                    Toast.makeText(mCtx, submitQuesAnswerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 多选提交答案
     */
    private void submitMulAnswer() {
        String answerStr = multiselect_btnA_String
                + multiselect_btnB_String + multiselect_btnC_String
                + multiselect_btnD_String + multiselect_btnE_String
                + multiselect_btnF_String;
        // 定义答案字符串
        String strAns = "";
        if (null != answerStr && !"".equals(answerStr)) {
            strAns = answerStr.substring(0, answerStr.lastIndexOf("、"));
            System.out.println("strAns >>>:" + strAns);
        }
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Const.URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonParams(strAns);
        Observable<CommonBean<SubmitQuesAnswer>> observable = httpPostService.getSubmitUserqQuesAnswer(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubmitQuesAnswer>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mCtx, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<SubmitQuesAnswer> submitQuesAnswerCommonBean) {
                                if (100 == submitQuesAnswerCommonBean.getCode()) {
                                    switch (quesType) {
                                        case 1: // 判断类
                                            judge_submit_btn.setEnabled(true);
                                            judge_submit_btn.setClickable(true);
                                            break;
                                        case 2: // 单选类
                                            danxuan_submit_btn.setEnabled(true);
                                            danxuan_submit_btn.setClickable(true);
                                            break;
                                        case 3: // 多选类
                                            double_submit_btn.setEnabled(true);
                                            double_submit_btn.setClickable(true);
                                            break;
                                    }
                                } else {
                                    Toast.makeText(mCtx, submitQuesAnswerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    /**
     * 查看解析请求
     */
    private void getAnalysisContent() {

        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("paperId", paperId);
            obj.put("quesId", quesId);
            obj.put("searchType", searchType);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<QuesAnalysisBean>> observable = httpPostService.getQuesAnalysisInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<QuesAnalysisBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean<QuesAnalysisBean> quesAnalysisBeanCommonBean) {

                        if (100 == quesAnalysisBeanCommonBean.getCode()) {
                            int noteCount = quesAnalysisBeanCommonBean.getBody().getQuesNoteCount();
                            if (0 != noteCount) { // 笔记个数
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = noteCount;
                                mHandler.sendMessage(msg);
                            }
                            int quesCount = quesAnalysisBeanCommonBean.getBody().getQuesProblemCount();
                            if (0 != quesCount) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = quesCount;
                                mHandler.sendMessage(msg);
                            }
                            switch (quesType) {
                                case 1: // 判断类
                                    if (null != judge_answer_webView_layout) {
                                        if (judge_answer_webView_layout.getVisibility() == View.GONE) {
                                            judge_answer_webView_layout.setVisibility(View.VISIBLE);
                                            // webView基本设置
                                            webViewSetting(judge_answer_webView, quesAnalysisBeanCommonBean.getBody().getQuesUrl());
                                            submit_btn_layout.setVisibility(View.GONE);
                                            // 试卷题目隐藏
                                            judge_webView.setVisibility(View.GONE);
                                            // 试卷答案布局隐藏
                                            answer_layout.setVisibility(View.GONE);
                                            judge_answer_webView_layout.scrollBy(0, 0);
                                        }
                                    }
                                    break;
                                case 2: // 单选类
                                    if (danxuan_answer_webView_layout.getVisibility() == View.GONE) {
                                        danxuan_answer_webView_layout.setVisibility(View.VISIBLE);
                                        // webView基本设置
                                        webViewSetting(danxuan_answer_webView, quesAnalysisBeanCommonBean.getBody().getQuesUrl());
                                        submit_btn_layout.setVisibility(View.GONE);
                                        // 试卷题目隐藏
                                        test_webView.setVisibility(View.GONE);
                                        // 试卷答案布局隐藏
                                        answer_layout.setVisibility(View.GONE);
                                        danxuan_answer_webView_layout.scrollBy(0, 0);
                                    }
                                    break;
                                case 3: // 多选类
                                    if (double_answer_webView_layout.getVisibility() == View.GONE) {
                                        double_answer_webView_layout.setVisibility(View.VISIBLE);
                                        // webView基本设置
                                        webViewSetting(double_answer_webView, quesAnalysisBeanCommonBean.getBody().getQuesUrl());
                                        submit_btn_layout.setVisibility(View.GONE);
                                        // 试卷题目隐藏
                                        double_webView.setVisibility(View.GONE);
                                        // 试卷答案布局隐藏
                                        answer_layout.setVisibility(View.GONE);
                                        double_answer_webView_layout.scrollBy(0, 0);
                                    }
                                    break;
                                case 6: // 简答
                                    if (jianda_answer_webView_layout.getVisibility() == View.GONE) {
                                        submit_btn_layout.setVisibility(View.GONE);
                                        // 试卷题目隐藏
                                        jianda_webView.setVisibility(View.GONE);
                                        // 试卷答案布局隐藏
                                        answer_layout.setVisibility(View.GONE);
                                        grid_layout.setVisibility(View.GONE);
                                        add_icon_layout.setVisibility(View.GONE);
                                        jianda_answer_webView_layout.setVisibility(View.VISIBLE);
                                        // webView基本设置
                                        webViewSetting(jianda_answer_webView, quesAnalysisBeanCommonBean.getBody().getQuesUrl());
                                        jianda_answer_webView_layout.scrollBy(0, 0);
                                    }
                                    break;
                            }
                        } else {
                            Toast.makeText(mCtx, quesAnalysisBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 单题提交用户答案
     *
     * @param singleAnswer
     */
    private void submitSingleAnswer(String singleAnswer) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Const.URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonParams(singleAnswer);
        Observable<CommonBean<SubmitQuesAnswer>> observable = httpPostService.getSubmitUserqQuesAnswer(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<CommonBean<SubmitQuesAnswer>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mCtx, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(CommonBean<SubmitQuesAnswer> submitQuesAnswerCommonBean) {
                                if (100 == submitQuesAnswerCommonBean.getCode()) {
                                    switch (quesType) {
                                        case 1: // 判断类
                                            judge_submit_btn.setEnabled(true);
                                            judge_submit_btn.setClickable(true);
                                            break;
                                        case 2: // 单选类
                                            danxuan_submit_btn.setEnabled(true);
                                            danxuan_submit_btn.setClickable(true);
                                            break;
                                    }
                                } else {
                                    Toast.makeText(mCtx, submitQuesAnswerCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }

    private RequestBody commonParams(String singleAnswer) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("paperId", paperId);
            obj.put("quesId", quesId);
            obj.put("userQuesAnswer", singleAnswer);
            obj.put("answerType", searchType);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    /**
     * 设置选中按钮颜色
     *
     * @param btn
     * @param str
     */
    private void setSelectedButtonBackgroundResource(Button btn, String str) {
        btn.setBackgroundResource(R.drawable.yuan_btn_bg);
        btn.setText(str);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    /**
     * 设置未选中按钮颜色
     *
     * @param btn
     * @param str
     */
    private void setCommButtonBackgroudResource(Button btn, String str) {
        btn.setBackgroundResource(R.drawable.kongxinyuan_btn_bg);
        btn.setText(str);
        btn.setTextColor(Color.parseColor("#666666"));

    }


    /**
     * 多选按钮点击设置
     *
     * @param btn 选中按钮
     */
    private void setMulSelectButtonBackgroudResource(Button btn) {
        btn.setBackgroundResource(R.drawable.bg_mul_btn_select);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    private void setMulCommButtonBackgroundResource(Button btn) {
        btn.setBackgroundResource(R.drawable.bg_mul_btn_normal);
        btn.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    public void fetchData() {

    }
}
