package cn.jun.mysetting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.jun.bean.ChangeInfo;
import cn.jun.bean.Const;
import cn.jun.bean.GetUserDetail;
import cn.jun.bean.GongZuoZhangTai;
import cn.jun.bean.JiaoYuChengDu;
import cn.jun.bean.JsonBean;
import cn.jun.bean.XingBie;
import cn.jun.utils.HttpUtils;
import cn.jun.view.SoftKeyBoardListener;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;


/**
 * 个人信息
 */

public class Information extends Activity implements View.OnClickListener {
    //点击模块
    private RelativeLayout zhenshixinming_click;
    private TextView zhenshixinming_textview;
    private RelativeLayout nicheng_click;
    private TextView nicheng_textview;
    private RelativeLayout qianming_click;
    private TextView qianming_textview;
    private RelativeLayout shengri_click;
    private TextView shengri_textview;
    private RelativeLayout xingbie_click;
    private TextView xingbie_textview;
    private ArrayList<XingBie> XingBiewItem = new ArrayList<>();
    private TextView juzhidizhi_textview;
    private RelativeLayout juzhudizhi_click;
    private TextView lianxidizhi_textview;
    private RelativeLayout lianxidizhi_click;
    private TextView youxiang_textview;
    private RelativeLayout youxiang_click;
    private TextView weixin_textview;
    private RelativeLayout weixin_click;
    private TextView biyeyuanxiao_textview;
    private RelativeLayout biyeyuanxiao_click;
    private TextView jiaoyuchengdu_textview;
    private RelativeLayout jiaoyuchengdu_click;
    private ArrayList<JiaoYuChengDu> JiaoYuChengDuItem = new ArrayList<>();
    private RelativeLayout dangqianzhuangtai_click;
    private TextView dangqianzhuangtai_textview;
    private ArrayList<GongZuoZhangTai> GongZuoZhangTaiItem = new ArrayList<>();
    private RelativeLayout suozaidanwei_click;
    private TextView suozaidanwei_textview;
    private RelativeLayout zhiwei_click;
    private TextView zhiwei_textview;

    private PopupWindow popupWindow;
    //签名刷新剩余字数
    private EditText mEditText;
    private TextView mTextView = null;
    private static final int MAX_COUNT = 50;
    //自定义时间选择器
    private TimePickerView pvCustomTime;
    //自定义条件选择器（不联动）
    //性别
    private OptionsPickerView pvCustomOptions;
    //教育
    private OptionsPickerView pvCustomOptions_Jy;
    //状态
    private OptionsPickerView pvCustomOptions_Gz;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private GetUserDetail userDetail = new GetUserDetail();
    //数据源
    private ChangeInfo changeInfo;
    //错误提示
    private TextView tv_error;
    //接口传递数据
    String InfoKey = "";
    String InfoValue = "";
    //
//    private String UpdateKey;

    //用户ID
    private int userID;

    private Thread thread;
    private boolean isLoaded = false;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    //添加三级数据
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String SNProvince = "";
    private String SNCountry = "";
    /**
     * 解析城市数据
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
//                        Toast.makeText(Information.this, "开始解析数据", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initOptionJsonCity();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(Information.this, "解析数据成功", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(Information.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        //获取用户信息
        GetUserSharePreferences();
        //控件
        initView();
        //选择器
        initSoftKeyBoard();
        //获取用户个人信息
        initGetData();
        //自定义时间选择器
        initCustomTimePicker();
        //自定义条件选择器(性别)
        initCustomOptionPicker();
        //自定义条件选择器(教育)
        initCustomOptionPicker_Jy();
        //自定义条件选择器(工作)
        initCustomOptionPicker_Gz();
        //获取性别,教育程度,工作状态数据
        initOptionData();
        //获取地区数据
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);


    }

    private void GetUserSharePreferences(){
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userID = LoginPre.getInt("S_ID", 0);
    }


    public void initView() {
        zhenshixinming_click = (RelativeLayout) findViewById(R.id.zhenshixinming_click);
        zhenshixinming_textview = (TextView) findViewById(R.id.zhenshixinming_textview);
        zhenshixinming_click.setOnClickListener(this);
        nicheng_click = (RelativeLayout) findViewById(R.id.nicheng_click);
        nicheng_textview = (TextView) findViewById(R.id.nicheng_textview);
        nicheng_click.setOnClickListener(this);
        qianming_click = (RelativeLayout) findViewById(R.id.qianming_click);
        qianming_textview = (TextView) findViewById(R.id.qianming_textview);
        qianming_click.setOnClickListener(this);
        shengri_click = (RelativeLayout) findViewById(R.id.shengri_click);
        shengri_textview = (TextView) findViewById(R.id.shengri_textview);
        shengri_click.setOnClickListener(this);
        xingbie_click = (RelativeLayout) findViewById(R.id.xingbie_click);
        xingbie_textview = (TextView) findViewById(R.id.xingbie_textview);
        xingbie_click.setOnClickListener(this);

        juzhudizhi_click = (RelativeLayout) findViewById(R.id.juzhudizhi_click);
        juzhudizhi_click.setOnClickListener(this);
        juzhidizhi_textview = (TextView) findViewById(R.id.juzhidizhi_textview);
        lianxidizhi_click = (RelativeLayout) findViewById(R.id.lianxidizhi_click);
        lianxidizhi_click.setOnClickListener(this);
        lianxidizhi_textview = (TextView) findViewById(R.id.lianxidizhi_textview);
        youxiang_click = (RelativeLayout) findViewById(R.id.youxiang_click);
        youxiang_click.setOnClickListener(this);
        youxiang_textview = (TextView) findViewById(R.id.youxiang_textview);
        weixin_click = (RelativeLayout) findViewById(R.id.weixin_click);
        weixin_click.setOnClickListener(this);
        weixin_textview = (TextView) findViewById(R.id.weixin_textview);
        biyeyuanxiao_click = (RelativeLayout) findViewById(R.id.biyeyuanxiao_click);
        biyeyuanxiao_click.setOnClickListener(this);
        biyeyuanxiao_textview = (TextView) findViewById(R.id.biyeyuanxiao_textview);
        jiaoyuchengdu_click = (RelativeLayout) findViewById(R.id.jiaoyuchengdu_click);
        jiaoyuchengdu_click.setOnClickListener(this);
        jiaoyuchengdu_textview = (TextView) findViewById(R.id.jiaoyuchengdu_textview);
        dangqianzhuangtai_textview = (TextView) findViewById(R.id.dangqianzhuangtai_textview);
        dangqianzhuangtai_click = (RelativeLayout) findViewById(R.id.dangqianzhuangtai_click);
        dangqianzhuangtai_click.setOnClickListener(this);
        suozaidanwei_textview = (TextView) findViewById(R.id.suozaidanwei_textview);
        suozaidanwei_click = (RelativeLayout) findViewById(R.id.suozaidanwei_click);
        suozaidanwei_click.setOnClickListener(this);
        zhiwei_textview = (TextView) findViewById(R.id.zhiwei_textview);
        zhiwei_click = (RelativeLayout) findViewById(R.id.zhiwei_click);
        zhiwei_click.setOnClickListener(this);

    }

    public void initGetData() {
        if (httpUtils.isNetworkConnected(this)) {
            UserDetailTask userDetailTask = new UserDetailTask();
            userDetailTask.execute();
        } else {
            Toast.makeText(this, "网络连接异常!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initOptionData() {
        getXingBieData();
        getJiaoYuChengDuData();
        getGongZuoZhuangTaiData();
    }

    private void getXingBieData() {
//        for (int i = 0; i < 2; i++) {
//            XingBiewItem.add(new XingBie(i, "男"));
//        }
        XingBiewItem.add(new XingBie(0, "保密"));
        XingBiewItem.add(new XingBie(1, "男"));
        XingBiewItem.add(new XingBie(2, "女"));
    }

    private void getJiaoYuChengDuData() {
        JiaoYuChengDuItem.add(new JiaoYuChengDu(1, "大专"));
        JiaoYuChengDuItem.add(new JiaoYuChengDu(2, "学士"));
        JiaoYuChengDuItem.add(new JiaoYuChengDu(3, "硕士"));
        JiaoYuChengDuItem.add(new JiaoYuChengDu(4, "博士"));
        JiaoYuChengDuItem.add(new JiaoYuChengDu(5, "其它"));
    }

    private void getGongZuoZhuangTaiData() {
        GongZuoZhangTaiItem.add(new GongZuoZhangTai(1, "在职"));
        GongZuoZhangTaiItem.add(new GongZuoZhangTai(2, "大学"));
        GongZuoZhangTaiItem.add(new GongZuoZhangTai(3, "考研"));
        GongZuoZhangTaiItem.add(new GongZuoZhangTai(4, "其它"));
    }

    private void initOptionJsonCity() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = httpUtils.getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    public void initSoftKeyBoard() {
        //注册软键盘的监听
        SoftKeyBoardListener.setListener(Information.this,
                new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
//                        Toast.makeText(Information.this,
//                                "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void keyBoardHide(int height) {
//                        Toast.makeText(Information.this,
//                                "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
    }

    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }.start();
    }

    private void showPopupCommnet(final int type, final int mode) {
        //type-0.姓名。1，昵称。2，QQ。3，微信。4，毕业院校。5，所在单位。6，职位。9，其他
        //mode-0,签名。1，联系地址。9，其他。
        if (mode == 0) {
            View view = LayoutInflater.from(Information.this).inflate(
                    R.layout.edittext_qm_dialog, null);
            mEditText = (EditText) view
                    .findViewById(R.id.edittext);
            mEditText.addTextChangedListener(mTextWatcher);
            mEditText.setSelection(mEditText.length()); // 将光标移动最后一个字符后面
            mTextView = (TextView) view.findViewById(R.id.count);
            setLeftCount();
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
            // 设置弹出窗体需要软键盘
            popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // 再设置模式，和Activity的一样，覆盖，调整大小。
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            //设置弹出之后的背景透明度
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.4f;
            getWindow().setAttributes(params);
            // 设置popWindow的显示和消失动画
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            //显示
            popupWindow.update();
            popupInputMethodWindow();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                // 在dismiss中恢复透明度
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().setAttributes(params);
                }
            });

            TextView qxBtn = (TextView) view.findViewById(R.id.qxBtn);
            TextView bcBtn = (TextView) view.findViewById(R.id.bcBtn);
            qxBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            bcBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String InfoValue = mEditText.getText().toString().trim();
//                    String NewValue="";
//                    try {
//                         NewValue =  URLEncoder.encode(InfoValue,"UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
                    Log.d("InfoValue ==== ", "" + InfoValue);
                    //签名修改
                    if (!"".equals(InfoValue) && null != InfoValue) {
                        if (httpUtils.isNetworkConnected(Information.this)) {
                            ChangeInfoTask mailTask = new ChangeInfoTask();
                            mailTask.execute("S_Intro", InfoValue);
                        }
                    }
                }
            });
        } else if (mode == 1) {
            View view = LayoutInflater.from(Information.this).inflate(
                    R.layout.edittext_lianxidizhi_dialog, null);
            mEditText = (EditText) view
                    .findViewById(R.id.edittext);
            mEditText.setSelection(mEditText.length()); // 将光标移动最后一个字符后面
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
            // 设置弹出窗体需要软键盘
            popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // 再设置模式，和Activity的一样，覆盖，调整大小。
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            //设置弹出之后的背景透明度
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.4f;
            getWindow().setAttributes(params);
            // 设置popWindow的显示和消失动画
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            //显示
            popupWindow.update();
            popupInputMethodWindow();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                // 在dismiss中恢复透明度
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().setAttributes(params);
                }
            });
            TextView qxBtn = (TextView) view.findViewById(R.id.qxBtn);
            TextView bcBtn = (TextView) view.findViewById(R.id.bcBtn);
            qxBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            bcBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String InfoValue = mEditText.getText().toString().trim();
                    Log.d("InfoValue ==== ", "" + InfoValue);
                    //联系地址修改
                    if (!"".equals(InfoValue) && null != InfoValue) {
                        if (httpUtils.isNetworkConnected(Information.this)) {
                            ChangeInfoTask mailTask = new ChangeInfoTask();
                            mailTask.execute("SN_Address", InfoValue);
                        }
                    }
                }
            });
        } else {
            View view = LayoutInflater.from(Information.this).inflate(
                    R.layout.edittext_dialog, null);
            final TextView edit_title = (TextView) view
                    .findViewById(R.id.edit_title);
            final EditText EditText = (EditText) view
                    .findViewById(R.id.edittext);
            tv_error = (TextView) view.findViewById(R.id.tv_error);
            final TextView qxBtn = (TextView) view.findViewById(R.id.qx_btn);
            final TextView bcBtn = (TextView) view.findViewById(R.id.bc_btn);
            if (type == 0) {
                edit_title.setText("真实姓名");
            } else if (type == 1) {
                edit_title.setText("昵称");
            } else if (type == 2) {
                edit_title.setText("QQ");
                EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (type == 3) {
                edit_title.setText("微信");
            } else if (type == 4) {
                edit_title.setText("毕业院校");
            } else if (type == 5) {
                edit_title.setText("所在单位");
            } else if (type == 6) {
                edit_title.setText("职位");
            }
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
            // 设置弹出窗体需要软键盘
            popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // 再设置模式，和Activity的一样，覆盖，调整大小。
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            //设置弹出之后的背景透明度
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.4f;
            getWindow().setAttributes(params);
            // 设置popWindow的显示和消失动画
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            //显示
            popupWindow.update();
            popupInputMethodWindow();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                // 在dismiss中恢复透明度
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().setAttributes(params);
                }
            });

            qxBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            bcBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) {
                        String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //姓名修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask mailTask = new ChangeInfoTask();
                                mailTask.execute("S_RealName", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    } else if (type == 1) {
                        String InfoValue = EditText.getText().toString().trim();
                        //昵称修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask nickNameTask = new ChangeInfoTask();
                                nickNameTask.execute("S_NickName", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    } else if (type == 2) {
                       String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //QQ修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                    ChangeInfoTask mailTask = new ChangeInfoTask();
                                    mailTask.execute("SN_QQ", InfoValue);
                                     }
                            } else {
                                tv_error.setText("格式错误");
                                tv_error.setVisibility(View.VISIBLE);
                            }

                    } else if (type == 3) {
                        String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //微信修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask wechatlTask = new ChangeInfoTask();
                                wechatlTask.execute("SN_Wechat", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    } else if (type == 4) {
                        String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //毕业院校修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask graduationSchoollTask = new ChangeInfoTask();
                                graduationSchoollTask.execute("SN_GraduationSchool", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    } else if (type == 5) {
                        String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //所在单位修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask companyNameTask = new ChangeInfoTask();
                                companyNameTask.execute("SN_CompanyName", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    } else if (type == 6) {
                        String InfoValue = EditText.getText().toString().trim();
                        Log.d("InfoValue ==== ", "" + InfoValue);
                        //职位修改
                        if (!"".equals(InfoValue) && null != InfoValue) {
                            if (httpUtils.isNetworkConnected(Information.this)) {
                                ChangeInfoTask positionNameTask = new ChangeInfoTask();
                                positionNameTask.execute("SN_PositionName", InfoValue);
                            }
                        } else {
                            tv_error.setText("格式错误");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    }


                }
            });
        }
    }

    /**
     * 刷新剩余输入字数
     */
    private void setLeftCount() {
        mTextView.setText(String.valueOf((MAX_COUNT - getInputCount())) + "/50");
    }

    /**
     * 获取用户输入内容字数
     */
    private long getInputCount() {
        return calculateLength(mEditText.getText().toString());
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，
     * 一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
                // len++;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * EditText刷新剩余字符事件
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mEditText.getSelectionStart();
            editEnd = mEditText.getSelectionEnd();

            mEditText.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            mEditText.setText(s);
            mEditText.setSelection(editStart);

            // 恢复监听器
            mEditText.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    /**
     * 注意事项：
     * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
     * 具体可参考demo 里面的两个自定义layout布局。
     * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
     */
    private void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1970, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                shengri_textview.setText(getTime(date));
                //提交数据(生日修改)
                if (!"".equals(getTime(date)) && null != getTime(date)) {
                    if (httpUtils.isNetworkConnected(Information.this)) {
                        ChangeInfoTask mailTask = new ChangeInfoTask();
                        mailTask.execute("SN_Birthday", getTime(date));
                        shengri_textview.setText(getTime(date));

                    }
                }
            }
        })
                 /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //自定义不联动的条件选择器(性别)
    private void initCustomOptionPicker() {
        /**
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = XingBiewItem.get(options1).getPickerViewText();
                //提交数据(性别修改)
                if (!"".equals(tx) && null != tx) {
                    if (httpUtils.isNetworkConnected(Information.this)) {
                        ChangeInfoTask mailTask = new ChangeInfoTask();
                        if ("保密".equals(tx)) {
                            mailTask.execute("S_Sex", "0");
                        } else if ("男".equals(tx)) {
                            mailTask.execute("S_Sex", "1");
                        } else if ("女".equals(tx)) {
                            mailTask.execute("S_Sex", "2");
                        }
                        xingbie_textview.setText(tx);

                    }
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        //final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

//                        tvAdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                getCardData();
//                                pvCustomOptions.setPicker(cardItem);
//                            }
//                        });

                    }
                })
                //.isDialog(true)//对话框形式弹出
                .build();

        pvCustomOptions.setPicker(XingBiewItem);//添加数据
    }

    //自定义不联动的条件选择器(教育)
    private void initCustomOptionPicker_Jy() {
        pvCustomOptions_Jy = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = JiaoYuChengDuItem.get(options1).getPickerViewText();
                if (!"".equals(tx) && null != tx) {
                    if (httpUtils.isNetworkConnected(Information.this)) {
                        ChangeInfoTask mailTask = new ChangeInfoTask();
                        if ("大专".equals(tx)) {
                            mailTask.execute("SN_Education", "1");
                        } else if ("学士".equals(tx)) {
                            mailTask.execute("SN_Education", "2");
                        } else if ("硕士".equals(tx)) {
                            mailTask.execute("SN_Education", "3");
                        } else if ("博士".equals(tx)) {
                            mailTask.execute("SN_Education", "4");
                        } else if ("其它".equals(tx)) {
                            mailTask.execute("SN_Education", "5");
                        }
                        jiaoyuchengdu_textview.setText(tx);

                    }
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options_jy, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        //final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions_Jy.returnData();
                                pvCustomOptions_Jy.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions_Jy.dismiss();
                            }
                        });


                    }
                })
                .build();

        pvCustomOptions_Jy.setPicker(JiaoYuChengDuItem);//添加数据
    }

    //自定义不联动的条件选择器(性别)
    private void initCustomOptionPicker_Gz() {
        pvCustomOptions_Gz = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = GongZuoZhangTaiItem.get(options1).getPickerViewText();
                //提交数据(性别修改)
                if (!"".equals(tx) && null != tx) {
                    if (httpUtils.isNetworkConnected(Information.this)) {
                        ChangeInfoTask mailTask = new ChangeInfoTask();
                        if ("在职".equals(tx)) {
                            mailTask.execute("SN_JobStatus", "1");
                        } else if ("大学".equals(tx)) {
                            mailTask.execute("SN_JobStatus", "2");
                        } else if ("考研".equals(tx)) {
                            mailTask.execute("SN_JobStatus", "3");
                        } else if ("其它".equals(tx)) {
                            mailTask.execute("SN_JobStatus", "4");
                        }
                        dangqianzhuangtai_textview.setText(tx);

                    }
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options_gz, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        //final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions_Gz.returnData();
                                pvCustomOptions_Gz.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions_Gz.dismiss();
                            }
                        });

                    }
                })
                .build();
        pvCustomOptions_Gz.setPicker(GongZuoZhangTaiItem);//添加数据
    }


    private void ShowAddressPickerView() {// 弹出居住地选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText() + "-" +
//                        options2Items.get(options1).get(options2) + "-" +
//                        options3Items.get(options1).get(options2).get(options3);
                String tx = options1Items.get(options1).getPickerViewText() + "-" +
                        options2Items.get(options1).get(options2);
                SNProvince = options1Items.get(options1).getPickerViewText();
                SNCountry = options2Items.get(options1).get(options2);

            }
        })
                .setTitleText("城市选择")
                .setSubmitColor(Color.parseColor("#DD5555"))
                .setCancelColor(Color.parseColor("#DD5555"))
                .setDividerColor(Color.parseColor("#999999"))
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

        pvOptions.setonClick(new OptionsPickerView.ICoallBack() {
            @Override
            public void onClickOkButton(String s) {
                if ("setcity_ok".equals(s)) {
                    //提交数据(居住地修改)
                    if (!"".equals(SNProvince) && null != SNProvince) {
                        if (httpUtils.isNetworkConnected(Information.this)) {
                            ChangeInfoTask ProvinceTask = new ChangeInfoTask();
                            ProvinceTask.execute("city", SNProvince + "," + SNCountry);
                            //SN_Province
                            //SN_Country
                        }
                    }
                    juzhidizhi_textview.setText(SNProvince + "-" + SNCountry);

                }
            }
        });


    }

    class UserDetailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            String MD5 = httpUtils.getMD5Str(userID);
            userDetail = httpUtils.getUserDetail(Const.URL + Const.GetRenXinIxAPI
                    , Const.CLIENT, userID, MD5);

            Log.d(" ---- > ", userDetail.getCode() + "");
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userDetail.getCode() == 100) {
                initSetData();
            } else {

            }
        }

    }


    public void initSetData() {
        zhenshixinming_textview.setText(userDetail.getBody().S_RealName);
        nicheng_textview.setText(userDetail.getBody().S_NickName);
        qianming_textview.setText(userDetail.getBody().S_Intro);
        if (userDetail.getBody().S_Sex == 1)
            xingbie_textview.setText("男");
        else if (userDetail.getBody().S_Sex == 2)
            xingbie_textview.setText("女");
        else if (userDetail.getBody().S_Sex == 0)
            xingbie_textview.setText("保密");
        shengri_textview.setText(userDetail.getBody().SN_Birthday);
        juzhidizhi_textview.setText(userDetail.getBody().SN_Province + "-" + userDetail.getBody().SN_Country);
        lianxidizhi_textview.setText(userDetail.getBody().SN_Address);
        youxiang_textview.setText(userDetail.getBody().SN_QQ);
        weixin_textview.setText(userDetail.getBody().SN_Wechat);

        biyeyuanxiao_textview.setText(userDetail.getBody().SN_GraduationSchool);
        if ("1".equals(userDetail.getBody().SN_Education))
            jiaoyuchengdu_textview.setText("大专");
        if ("2".equals(userDetail.getBody().SN_Education))
            jiaoyuchengdu_textview.setText("大学");
        if ("3".equals(userDetail.getBody().SN_Education))
            jiaoyuchengdu_textview.setText("硕士");
        if ("4".equals(userDetail.getBody().SN_Education))
            jiaoyuchengdu_textview.setText("博士");
        if ("5".equals(userDetail.getBody().SN_Education))
            jiaoyuchengdu_textview.setText("其他");
        if ("1".equals(userDetail.getBody().SN_JobStatus))
            dangqianzhuangtai_textview.setText("在职");
        else if ("2".equals(userDetail.getBody().SN_JobStatus))
            dangqianzhuangtai_textview.setText("大学");
        else if ("3".equals(userDetail.getBody().SN_JobStatus))
            dangqianzhuangtai_textview.setText("考研");
        else if ("4".equals(userDetail.getBody().SN_JobStatus))
            dangqianzhuangtai_textview.setText("其他");
        suozaidanwei_textview.setText(userDetail.getBody().SN_CompanyName);
        zhiwei_textview.setText(userDetail.getBody().SN_PositionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhenshixinming_click:
                showPopupCommnet(0, 9);
                break;
            case R.id.nicheng_click:
                showPopupCommnet(1, 9);
                break;
            case R.id.qianming_click:
                showPopupCommnet(9, 0);
                break;
            case R.id.xingbie_click:
                if (pvCustomOptions != null) {
                    pvCustomOptions.show();
                }
                break;
            case R.id.shengri_click:
                if (pvCustomTime != null) {
                    pvCustomTime.show();
                }
                break;
            case R.id.juzhudizhi_click:
                if (isLoaded) {
                    ShowAddressPickerView();
                } else {
                    Toast.makeText(Information.this, "数据暂未解析成功，请等待", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lianxidizhi_click:
                showPopupCommnet(9, 1);
                break;
            case R.id.youxiang_click:
                showPopupCommnet(2, 9);
                break;
            case R.id.weixin_click:
                showPopupCommnet(3, 9);
                break;
            case R.id.biyeyuanxiao_click:
                showPopupCommnet(4, 9);
                break;
            case R.id.jiaoyuchengdu_click:
                if (pvCustomOptions_Jy != null) {
                    pvCustomOptions_Jy.show();
                }
                break;
            case R.id.dangqianzhuangtai_click:
                if (pvCustomOptions_Gz != null) {
                    pvCustomOptions_Gz.show();
                }
                break;
            case R.id.suozaidanwei_click:
                showPopupCommnet(5, 9);
                break;
            case R.id.zhiwei_click:
                showPopupCommnet(6, 9);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume -- ", "  onResume");
    }


    class ChangeInfoTask extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            UpdateKey = params[0];
            InfoKey = params[0];
            InfoValue = params[1];
            String MD5 = httpUtils.getMD5Str(userID);
            changeInfo = httpUtils.GetChangeInfo(Const.URL + Const.ChangeInfoAPI, userID, Const.CLIENT, MD5, InfoKey, InfoValue);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (null != changeInfo && !"".equals(changeInfo)) {
                if (100 == changeInfo.getCode()) {
                    if ("S_RealName".equals(InfoKey)) {
                        popupWindow.dismiss();
                        zhenshixinming_textview.setText(InfoValue);
                    } else if ("S_NickName".equals(InfoKey)) {
                        popupWindow.dismiss();
                        nicheng_textview.setText(InfoValue);
                    } else if ("S_Intro".equals(InfoKey)) {
                        popupWindow.dismiss();
                        qianming_textview.setText(InfoValue);
                    } else if ("SN_Address".equals(InfoKey)) {
                        popupWindow.dismiss();
                        lianxidizhi_textview.setText(InfoValue);
                    } else if ("SN_QQ".equals(InfoKey)) {
                        popupWindow.dismiss();
                        youxiang_textview.setText(InfoValue);
                    } else if ("SN_Wechat".equals(InfoKey)) {
                        popupWindow.dismiss();
                        weixin_textview.setText(InfoValue);
                    } else if ("SN_GraduationSchool".equals(InfoKey)) {
                        popupWindow.dismiss();
                        biyeyuanxiao_textview.setText(InfoValue);
                    } else if ("SN_CompanyName".equals(InfoKey)) {
                        popupWindow.dismiss();
                        suozaidanwei_textview.setText(InfoValue);
                    } else if ("SN_PositionName".equals(InfoKey)) {
                        popupWindow.dismiss();
                        zhiwei_textview.setText(InfoValue);
                    }
                } else {
                    if ("S_RealName".equals(InfoKey)) {
                        tv_error.setText(changeInfo.getMessage());
                        tv_error.setVisibility(View.VISIBLE);
                    } else if ("S_NickName".equals(InfoKey)) {
                        tv_error.setText(changeInfo.getMessage());
                        tv_error.setVisibility(View.VISIBLE);
                    } else if ("S_Intro".equals(InfoKey)) {
                        Toast.makeText(Information.this, "出错啦,稍后再试!", Toast.LENGTH_SHORT).show();
                    } else if ("SN_Address".equals(InfoKey)) {
                        Toast.makeText(Information.this, "出错啦,稍后再试!", Toast.LENGTH_SHORT).show();
                    }

                }
            }


        }
    }




}
