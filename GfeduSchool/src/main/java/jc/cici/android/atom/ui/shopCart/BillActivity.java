package jc.cici.android.atom.ui.shopCart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;

/**
 * 个人发票类
 * Created by atom on 2017/8/23.
 */

public class BillActivity extends BaseActivity {

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
    // 个人发票布局
    @BindView(R.id.personBill_layout)
    RelativeLayout personBill_layout;
    // 个人发票按钮
    @BindView(R.id.personBill_radioBtn)
    Button personBill_radioBtn;
    // 公司发票布局
    @BindView(R.id.companyBill_layout)
    RelativeLayout companyBill_layout;
    //  公司发票按钮
    @BindView(R.id.companyBill_radioBtn)
    Button companyBill_radioBtn;
    // viewPager
    @BindView(R.id.vp)
    ViewPager vp;
    private ArrayList<Fragment> list;
    // 获取订单id
    private int orderId;
    // 获取来源
    private int mFrom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bill;
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
        // 订单id
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getInt("orderId");
        // 获取进入来源
        mFrom = bundle.getInt("mFrom");
        setContentView(getLayoutId());
        // 添加注解
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 填充内容
        setContent();
    }

    /**
     * 填充内容
     */
    private void setContent() {

        list = new ArrayList<Fragment>();
        Fragment personBillFragment = new PersonBillFragment();
        Fragment companyBillFragment = new CompanyBillFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderId", orderId);
        bundle.putInt("mFrom", mFrom);
        personBillFragment.setArguments(bundle);
        companyBillFragment.setArguments(bundle);
        list.add(personBillFragment);
        list.add(companyBillFragment);
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    personBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_select);
                    companyBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_default);
                    vp.setCurrentItem(0);
                } else {
                    personBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_default);
                    companyBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_select);
                    vp.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取视图id
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("发票信息");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
    }


    @OnClick({R.id.back_layout, R.id.personBill_layout, R.id.companyBill_layout})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.personBill_layout: // 个人发票
                personBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_select);
                companyBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_default);
                vp.setCurrentItem(0);
                break;
            case R.id.companyBill_layout: // 公司发票
                personBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_default);
                companyBill_radioBtn.setBackgroundResource(R.drawable.icon_radio_select);
                vp.setCurrentItem(1);
            default:
                break;
        }
    }

    /**
     * fragment 适配器
     */
    class MyFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mList;

        public MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
