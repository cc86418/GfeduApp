package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jun.bean.JsonBean;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by atom on 2017/8/22.
 */

public class AddAddressAc extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // title布局
    @BindView(R.id.title_layout)
    Toolbar title_layout;
    // 返回按钮
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题文字
    @BindView(R.id.title_txt)
    TextView title_txt;
    // 右侧搜索布局
    @BindView(R.id.share_layout)
    RelativeLayout share_layout;
    // 注册按钮屏蔽
    @BindView(R.id.register_txt)
    TextView register_txt;
    // 右侧搜索按钮
    @BindView(R.id.moreBtn)
    Button moreBtn;
    // 更多按钮
    @BindView(R.id.search_Btn)
    Button search_Btn;
    // 收货人编辑框
    @BindView(R.id.receiverName_txt)
    EditText receiverName_txt;
    // 联系方式编辑框
    @BindView(R.id.contactName_txt)
    EditText contactName_txt;
    // 邮箱编辑框
    @BindView(R.id.mailName_txt)
    EditText mailName_txt;
    // 邮编编辑框
    @BindView(R.id.zipCodeName_txt)
    EditText zipCodeName_txt;
    // 所在地区布局
    @BindView(R.id.area_layout)
    RelativeLayout area_layout;
    // 所在地区文字
    @BindView(R.id.areaName_txt)
    TextView areaName_txt;
    // 详细地址
    @BindView(R.id.detailAddressName_txt)
    EditText detailAddressName_txt;
    // 默认布局
    @BindView(R.id.default_layout)
    RelativeLayout default_layout;
    // 默认文字
    @BindView(R.id.default_txt)
    TextView default_txt;
    // 默认按钮
    @BindView(R.id.default_Img)
    ImageView default_Img;
    // 保存按钮
    @BindView(R.id.saveBtn)
    Button saveBtn;
    private int defaultFlag = 2;
    private String strreceiverName, // 收货人名称
            strcontactName, // 手机号
            strmailName, // 邮箱
            strzipCodeName, // 邮编
            strareaName, // 所在区域
            strdetailAddressName; // 详细地址
    private Dialog dialog;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    private Thread thread;
    // 判断加载
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
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(baseActivity, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    /**
     * 初始化地区实体内容
     */
    private void initOptionJsonCity() {
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

    /**
     * 解析json数据
     *
     * @param result
     * @return
     */
    private ArrayList<JsonBean> parseData(String result) {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addaddress;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        AppManager.getInstance().addActivity(this);
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
    }

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("添加收货地址");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);

        //获取地区数据
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }


    @OnClick({R.id.back_layout, R.id.area_layout, R.id.default_layout, R.id.saveBtn})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_layout: // 返回按钮
                this.finish();
                break;
            case R.id.area_layout: // 所在地区布局
                if (isLoaded) {
                    ShowAddressPickerView();
                } else {
                    Toast.makeText(baseActivity, "数据暂未解析成功，请等待", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.default_layout: // 默认布局
                if (2 == defaultFlag) { // 未设置成默认情况
                    default_Img.setBackgroundResource(R.drawable.ic_check_checked);
                    defaultFlag = 1;
                } else if (1 == defaultFlag) { // 已经设置为默认情况
                    default_Img.setBackgroundResource(R.drawable.icon_default_select);
                    defaultFlag = 2;
                }
                break;
            case R.id.saveBtn: // 保存按钮
                // 收货人
                strreceiverName = receiverName_txt.getText().toString().trim();
                if (null != strreceiverName && !"".equals(strreceiverName)) {
                    // 获取手机号首位
                    String firstChar = getEditTextInput();
                    strcontactName = contactName_txt.getText().toString().trim();
                    if (null != strcontactName && !"".equals(strcontactName)) {
                        if ("1".equals(firstChar) && ToolUtils.isMobileNo(strcontactName)) {
                            // 获取邮箱地址
                            strmailName = mailName_txt.getText().toString().trim();
                            if (null != strmailName && !"".equals(strmailName)) {
                                if (ToolUtils.isEmail(strmailName)) {
                                    // 获取邮编
                                    strzipCodeName = zipCodeName_txt.getText().toString().trim();
                                    // 获取所在区域
                                    strareaName = areaName_txt.getText().toString().trim();
                                    if (null != strareaName && !"".equals(strareaName)) {
                                        // 获取详细地址
                                        strdetailAddressName = detailAddressName_txt.getText().toString().trim();
                                        if (null != strdetailAddressName && !"".equals(strdetailAddressName)) {

                                            if (NetUtil.isMobileConnected(baseActivity)) {
                                                // 提交收货地址信息
                                                submitAddressInfo(strreceiverName, strcontactName, strmailName, strzipCodeName, strareaName, strdetailAddressName);
                                            } else {
                                                Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(baseActivity, "详细地址不能为空哦", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(baseActivity, "所在区域不能为空", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(baseActivity, "邮箱格式错误，请输入正确邮箱格式", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(baseActivity, "邮箱内容不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(baseActivity, "非法手机号", Toast.LENGTH_SHORT).show();
                            contactName_txt.setText("");
                        }
                    } else {
                        Toast.makeText(baseActivity, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(baseActivity, "收货人内容不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 保存地址信息
     *
     * @param strreceiverName
     * @param strcontactName
     * @param strmailName
     * @param strzipCodeName
     * @param strareaName
     * @param strdetailAddressName
     */
    private void submitAddressInfo(String strreceiverName, String strcontactName,
                                   String strmailName, String strzipCodeName, String strareaName, String strdetailAddressName) {
        // 加载数据
        showProcessDialog(baseActivity,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            // 收货人姓名
            obj.put("addressName", strreceiverName);
            // 收货人联系方式
            obj.put("addressMobile", strcontactName);
            // 邮编
            obj.put("addressPostcode", strzipCodeName);
            // e-mail
            obj.put("addressEmail", strmailName);
            // 省份
            obj.put("addressProvince", SNProvince);
            // 市份
            obj.put("addressCity", SNCountry);
            // 详细地址
            obj.put("addressDetail", strdetailAddressName);
            // 默认情况
            obj.put("addressIsDefault", defaultFlag);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.getInsertAddressInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {  // 地址添加成功
                            Toast.makeText(baseActivity, "添加成功", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent();
                            setResult(2, it);
                            baseActivity.finish();
                        } else {
                            Toast.makeText(baseActivity, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 显示地址选择器
     */
    private void ShowAddressPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + "-" +
                        options2Items.get(options1).get(options2);
                SNProvince = options1Items.get(options1).getPickerViewText();
                SNCountry = options2Items.get(options1).get(options2);

            }
        })
                .setBgColor(Color.parseColor("#EDEDED"))
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setTitleText("城市选择")
                .setTitleColor(Color.parseColor("#333333"))
                .setTitleSize(18)
                .setSubmitColor(Color.parseColor("#DD5555"))
                .setCancelColor(Color.parseColor("#666666"))
                .setDividerColor(Color.parseColor("#666666"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.show();

        pvOptions.setonClick(new OptionsPickerView.ICoallBack() {
            @Override
            public void onClickOkButton(String s) {
                if ("setcity_ok".equals(s)) {
                    //提交数据(居住地修改)
                    if (!"".equals(SNProvince) && null != SNProvince) {
                        if (httpUtils.isNetworkConnected(baseActivity)) {
                            // TODO 提交数据
//                            Information.ChangeInfoTask ProvinceTask = new Information.ChangeInfoTask();
//                            ProvinceTask.execute("city", SNProvince + "," + SNCountry);
                            //SN_Province
                            //SN_Country
                        }
                    }
                    areaName_txt.setText(SNProvince + "-" + SNCountry);

                }
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }


    /**
     * 自定义进度
     *
     * @param mContext
     * @param layout
     */
    private void showProcessDialog(Activity mContext, int layout) {
        dialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        dialog.show();
        // 注意此处要放在show之后 否则会报异常
        dialog.setContentView(layout);
    }

    private String getEditTextInput() {
        String firstChar = contactName_txt.getText().toString().trim();
        if (null != firstChar && firstChar.length() > 0) {
            char a = firstChar.charAt(0);
            StringBuffer buffer = new StringBuffer();
            return buffer.append(a).toString();
        }
        return null;
    }

}
