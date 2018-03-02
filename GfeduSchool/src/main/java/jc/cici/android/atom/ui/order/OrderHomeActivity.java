package jc.cici.android.atom.ui.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
 * 订单列表页Activity
 * Created by atom on 2017/8/25.
 */

public class OrderHomeActivity extends BaseActivity {

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
    // 全部布局
    @BindView(R.id.allOrder_layout)
    LinearLayout allOrder_layout;
    // 全部文字
    @BindView(R.id.allOrder_txt)
    TextView allOrder_txt;
    // 已支付布局
    @BindView(R.id.paid_layout)
    LinearLayout paid_layout;
    // 已支付文字
    @BindView(R.id.paid_txt)
    TextView paid_txt;
    // 未支付布局
    @BindView(R.id.unpaid_layout)
    LinearLayout unpaid_layout;
    // 未支付文字
    @BindView(R.id.unpaid_txt)
    TextView unpaid_txt;
    // 欠费布局
    @BindView(R.id.debt_layout)
    LinearLayout debt_layout;
    // 欠费文字
    @BindView(R.id.debt_txt)
    TextView debt_txt;
    // 已取消布局
    @BindView(R.id.cancer_layout)
    LinearLayout cancer_layout;
    // 欠费文字
    @BindView(R.id.cancer_txt)
    TextView cancer_txt;
    // 滑动viewPager
    @BindView(R.id.vp)
    ViewPager vp;
    private ArrayList<Fragment> list;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderhome;
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
        // 填充内容
        setContent();
    }

    private void setContent() {
        list = new ArrayList<>();
        final Fragment allOrderFragment = new AllOrderFragment();
        Fragment paidFragment = new PaidFragment();
        Fragment unPaidFragment = new UnPaidFragment();
        Fragment debtFragment = new DebtFragment();
        Fragment cancerFragment = new CancerFragment();
        list.add(allOrderFragment);
        list.add(paidFragment);
        list.add(unPaidFragment);
        list.add(debtFragment);
        list.add(cancerFragment);
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: // 全部
                        vp.setCurrentItem(0);
                        // 全部选中
                        setSelectPositionColor(allOrder_txt);
                        // 其他正常
                        setNormalPositionColor(paid_txt);
                        setNormalPositionColor(unpaid_txt);
                        setNormalPositionColor(debt_txt);
                        setNormalPositionColor(cancer_txt);
                        break;
                    case 1: // 已支付
                        vp.setCurrentItem(1);
                        // 已支付选中
                        setSelectPositionColor(paid_txt);
                        // 其他正常
                        setNormalPositionColor(allOrder_txt);
                        setNormalPositionColor(unpaid_txt);
                        setNormalPositionColor(debt_txt);
                        setNormalPositionColor(cancer_txt);
                        break;
                    case 2: // 未支付
                        vp.setCurrentItem(2);
                        // 未支付选中
                        setSelectPositionColor(unpaid_txt);
                        // 其他正常
                        setNormalPositionColor(allOrder_txt);
                        setNormalPositionColor(paid_txt);
                        setNormalPositionColor(debt_txt);
                        setNormalPositionColor(cancer_txt);
                        break;
                    case 3: // 欠费
                        vp.setCurrentItem(3);
                        // 欠费选中
                        setSelectPositionColor(debt_txt);
                        // 其他正常
                        setNormalPositionColor(allOrder_txt);
                        setNormalPositionColor(paid_txt);
                        setNormalPositionColor(unpaid_txt);
                        setNormalPositionColor(cancer_txt);
                        break;
                    case 4: // 已取消
                        vp.setCurrentItem(4);
                        // 欠费选中
                        setSelectPositionColor(cancer_txt);
                        // 其他正常
                        setNormalPositionColor(allOrder_txt);
                        setNormalPositionColor(paid_txt);
                        setNormalPositionColor(unpaid_txt);
                        setNormalPositionColor(debt_txt);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @OnClick({R.id.back_layout,R.id.allOrder_txt, R.id.paid_txt, R.id.unpaid_txt, R.id.debt_txt, R.id.cancer_txt})
    void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.allOrder_txt: // 全部
                vp.setCurrentItem(0);
                // 全部选中
                setSelectPositionColor(allOrder_txt);
                // 其他正常
                setNormalPositionColor(paid_txt);
                setNormalPositionColor(unpaid_txt);
                setNormalPositionColor(debt_txt);
                setNormalPositionColor(cancer_txt);
                break;
            case R.id.paid_txt: // 已支付
                vp.setCurrentItem(1);
                // 已支付选中
                setSelectPositionColor(paid_txt);
                // 其他正常
                setNormalPositionColor(allOrder_txt);
                setNormalPositionColor(unpaid_txt);
                setNormalPositionColor(debt_txt);
                setNormalPositionColor(cancer_txt);
                break;
            case R.id.unpaid_txt: // 未支付
                vp.setCurrentItem(2);
                // 未支付选中
                setSelectPositionColor(unpaid_txt);
                // 其他正常
                setNormalPositionColor(allOrder_txt);
                setNormalPositionColor(paid_txt);
                setNormalPositionColor(debt_txt);
                setNormalPositionColor(cancer_txt);
                break;
            case R.id.debt_txt: // 欠费
                vp.setCurrentItem(3);
                // 欠费选中
                setSelectPositionColor(debt_txt);
                // 其他正常
                setNormalPositionColor(allOrder_txt);
                setNormalPositionColor(paid_txt);
                setNormalPositionColor(unpaid_txt);
                setNormalPositionColor(cancer_txt);
                break;
            case R.id.cancer_txt: // 取消
                vp.setCurrentItem(4);
                // 欠费选中
                setSelectPositionColor(cancer_txt);
                // 其他正常
                setNormalPositionColor(allOrder_txt);
                setNormalPositionColor(paid_txt);
                setNormalPositionColor(unpaid_txt);
                setNormalPositionColor(debt_txt);
                break;
            default:
                break;
        }
    }

    /**
     * 选中颜色
     *
     * @param tv
     */
    private void setSelectPositionColor(TextView tv) {
        tv.setTextColor(Color.parseColor("#dd5555"));
    }

    /**
     * 未选中颜色
     */
    private void setNormalPositionColor(TextView tv) {
        tv.setTextColor(Color.parseColor("#333333"));
    }

    /**
     * 创建初始视图
     */
    private void initView() {

        title_layout.setBackgroundResource(R.color.input_bg_color);
        back_layout.setVisibility(View.VISIBLE);
        share_layout.setVisibility(View.VISIBLE);
        register_txt.setVisibility(View.VISIBLE);
        title_txt.setText("我的订单");
        register_txt.setVisibility(View.GONE);
        // 第一期屏蔽功能
        search_Btn.setVisibility(View.GONE);
        allOrder_txt.setTextColor(Color.parseColor("#dd5555"));
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
