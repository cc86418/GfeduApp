package cn.gfedu.home_pager;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jc.cici.android.R;


public class TabView extends LinearLayout implements View.OnClickListener{

    private ImageView mTabImage;
    private TextView mTabLable;

    public TabView(Context context) {
        super(context);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }


    private void initView(Context context){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.home_page_tabview_button,this,true);
        mTabImage=(ImageView)findViewById(R.id.tab_image);
        mTabLable=(TextView)findViewById(R.id.tab_lable);

    }

    public void initData(TabItem tabItem){
        mTabImage.setImageResource(tabItem.imageResId);
        mTabLable.setText(tabItem.lableResId);
    }

    public void initData_Ac(TabItem_Ac tabItem){
        mTabImage.setImageResource(tabItem.imageResId);
        mTabLable.setText(tabItem.lableResId);
    }


    @Override
    public void onClick(View v) {

    }


    public void onDataChanged(int badgeCount) {
        //  TODO notify new message, change the badgeView
    }
}
