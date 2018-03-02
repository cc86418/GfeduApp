package cn.jun.logistics;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.jun.utils.HttpUtils;
import jc.cici.android.R;


public class LogisticsActivity extends AppCompatActivity implements View.OnClickListener {
    //all
    private AllLogisticsFragment mAllLogistics;
    //已发货
    private ShippLogisticsFragment mShippLogistics;
    //未发货
    private NotShippLogisticsFragment mNotShippLogistics;
    private ArrayList<Fragment> list_fragment;  //定义要装fragment的列表
    private ArrayList<String> list_title;  //定义要装fragment的列表
    //Fragemet适配器导航
    private LogisticsAdapter pagerAdapter;
    private NoSrcollViewPager viewPager;
    private TabLayout tabLayout;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private ImageView iv_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_activity);
        //新的隐藏标题方法
        getSupportActionBar().hide();
        if (httpUtils.isNetworkConnected(this)) {
            initView();
        }
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        list_fragment = new ArrayList<>();  //定义要装fragment的列表
        list_title = new ArrayList<>();  //定义要装fragment的列表
        //初始化各fragment
        mAllLogistics = new AllLogisticsFragment();
        mShippLogistics = new ShippLogisticsFragment();
        mNotShippLogistics = new NotShippLogisticsFragment();
        //将fragment装进列表中
        list_fragment.add(mAllLogistics);
        list_fragment.add(mShippLogistics);
        list_fragment.add(mNotShippLogistics);
        list_title.add("全部");
        list_title.add("已发货");
        list_title.add("未发货");

        pagerAdapter = new LogisticsAdapter(LogisticsActivity.this.getSupportFragmentManager(), this, list_fragment, list_title);
        viewPager = (NoSrcollViewPager) findViewById(R.id.viewpager);
        viewPager.setScanScroll(false);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
