package jc.cici.android.atom.ui.shopCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.AddressManagerRecAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.AddressBean;
import jc.cici.android.atom.bean.AddressManagerBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 地址管理器
 * Created by atom on 2017/8/23.
 */

public class AddressManagerAc extends BaseActivity {

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
    // 列表
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    // 空视图
    @BindView(R.id.emptyView)
    ImageView emptyView;
    // 添加新地址按钮
    @BindView(R.id.addAddress_btn)
    Button addAddress_btn;
    // 判断跳转来源
    private String from;
    private Dialog dialog;
    private ArrayList<AddressBean> mData;
    private AddressManagerRecAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.update.address".equals(intent.getAction())) { // 获取修改广播
                mData.clear();
                // 重新请求数据
                initData();
            }
        }
    };
    private IntentFilter filter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addressmanager;
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
        from = getIntent().getStringExtra("from");
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 获取内容
        if (NetUtil.isMobileConnected(baseActivity)) {
            initData();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {

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
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean<AddressManagerBean>> observable = httpPostService.getAddressListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<AddressManagerBean>>() {
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
                        Toast.makeText(baseActivity, "网络请求异常,请退出重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<AddressManagerBean> addressManagerBeanCommonBean) {
                        if (100 == addressManagerBeanCommonBean.getCode()) {
                            ArrayList<AddressBean> data = addressManagerBeanCommonBean.getBody().getAddressList();
                            if (null != data && !"null".equals(data) && data.size() > 0) {
                                for (AddressBean address : data) {
                                    mData.add(address);
                                }
                                // 设置默认选中项
                                for (int i = 0; i < mData.size(); i++) {
                                    if (1 == mData.get(i).getAddressIsDefault()) {
                                        adapter.setSelected(i);
                                        break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (null != from && "ensureOrder".equals(from)) { // 订单详情页来源情况
                                            Intent it = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("addressPKID", mData.get(position).getAddressPKID());
                                            // 收货人名称
                                            bundle.putString("addressName", mData.get(position).getAddressName());
                                            // 收货人所在省份
                                            bundle.putString("addressProvince", mData.get(position).getAddressProvince());
                                            // 收货人所在市份
                                            bundle.putString("addressCity", mData.get(position).getAddressCity());
                                            // 详细地址
                                            bundle.putString("addressDetail", mData.get(position).getAddressDetail());
                                            it.putExtras(bundle);
                                            setResult(2, it);
                                            baseActivity.finish();
                                        } else { // 其他情况
                                            Intent it = new Intent(baseActivity, EditAddressAc.class);
                                            Bundle bundle = new Bundle();
                                            // 地址id
                                            bundle.putInt("addressPKID", mData.get(position).getAddressPKID());
                                            // 收货人名称
                                            bundle.putString("addressName", mData.get(position).getAddressName());
                                            // 收货人联系方式
                                            bundle.putString("addressMobile", mData.get(position).getAddressMobile());
                                            // 收货人所在省份
                                            bundle.putString("addressProvince", mData.get(position).getAddressProvince());
                                            // 收货人所在市份
                                            bundle.putString("addressCity", mData.get(position).getAddressCity());
                                            // 详细地址
                                            bundle.putString("addressDetail", mData.get(position).getAddressDetail());
                                            it.putExtras(bundle);
                                            baseActivity.startActivity(it);
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(baseActivity, addressManagerBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("管理收货地址");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        // 创建list对象
        mData = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AddressManagerRecAdapter(baseActivity, mData);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);

        // 注册广播
        filter = new IntentFilter();
        filter.addAction("com.update.address");
        registerReceiver(receiver, filter);
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

    @OnClick({R.id.back_layout, R.id.addAddress_btn})
    void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                if (null != from && "ensureOrder".equals(from)) {
//                    if (mData.size() == 0) { // 没有地址信息情况
//                        Intent it = new Intent();
//                        Bundle bundle = new Bundle();
//                        // 地址id
//                        bundle.putInt("addressPKID", 0);
//                        it.putExtras(bundle);
//                        setResult(2, it);
//                    }
                    this.finish();
                }
                break;
            case R.id.addAddress_btn: // 添加新地址
                Intent it = new Intent(baseActivity, AddAddressAc.class);
                startActivityForResult(it, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (2 == resultCode) {
                mData.clear();
                initData();
            }
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 返回按钮设置监听
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            if (mData.size() == 0) { // 没有地址信息情况
//                Intent it = new Intent();
//                Bundle bundle = new Bundle();
//                // 地址id
//                bundle.putInt("addressPKID", 0);
//                it.putExtras(bundle);
//                setResult(2, it);
//            }
//            this.finish();
//            return true;
//        }
//        return true;
//    }
}
