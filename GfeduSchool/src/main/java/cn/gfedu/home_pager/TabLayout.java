package cn.gfedu.home_pager;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TabLayout extends LinearLayout implements View.OnClickListener {

    private ArrayList<TabItem> tabs;
    private ArrayList<TabItem_Ac> tabs_ac;
    private OnTabClickListener listener;
    private View selectView;
    private int tabCount;


    public TabLayout(Context context) {
        super(context);
        initView();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
    }

    public void setCurrentTab(int i) {
        if (i < tabCount && i >= 0) {
            View view = getChildAt(i);
            if (selectView != view) {
                view.setSelected(true);
                if (selectView != null) {
                    selectView.setSelected(false);
                }
                selectView = view;
            }
        }
    }

    public void onDataChanged(int i, int badgeCount) {
        if (i < tabCount && i >= 0) {
            TabView view = (TabView) getChildAt(i);
            view.onDataChanged(badgeCount);
        }
    }


    public void initData(ArrayList<TabItem> tabs, OnTabClickListener listener,String type) {
        this.tabs = tabs;
        this.listener = listener;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if (tabs != null && tabs.size() > 0) {
            tabCount = tabs.size();
            TabView mTabView = null;
            TabView_Two mTabView_two = null;
            if("0".equals(type)){
                for (int i = 0; i < tabs.size(); i++) {
                    mTabView = new TabView(getContext());
                    mTabView.setTag(tabs.get(i));
                    mTabView.initData(tabs.get(i));
                    mTabView.setOnClickListener(this);
                    addView(mTabView, params);
                }
            }else if("1".equals(type)){
                for (int i = 0; i < tabs.size(); i++) {
                    mTabView_two = new TabView_Two(getContext());
                    mTabView_two.setTag(tabs.get(i));
                    mTabView_two.initData(tabs.get(i));
                    mTabView_two.setOnClickListener(this);
                    addView(mTabView_two, params);
                }
            }


        } else {
            throw new IllegalArgumentException("tabs can not be empty");
        }
    }

    public void initData_Ac(ArrayList<TabItem_Ac> tabs_ac, OnTabClickListener listener,String type) {
        this.tabs_ac = tabs_ac;
        this.listener = listener;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if (tabs_ac != null && tabs_ac.size() > 0) {
            tabCount = tabs_ac.size();
            TabView mTabView = null;
            TabView_Two mTabView_two = null;
            if("0".equals(type)){
                for (int i = 0; i < tabs_ac.size(); i++) {
                    mTabView = new TabView(getContext());
                    mTabView.setTag(tabs_ac.get(i));
                    mTabView.initData_Ac(tabs_ac.get(i));
                    mTabView.setOnClickListener(this);
                    addView(mTabView, params);
                }
            }


        } else {
            throw new IllegalArgumentException("tabs can not be empty");
        }
    }

    @Override
    public void onClick(View v) {
        listener.onTabClick((TabItem) v.getTag());
    }

    public interface OnTabClickListener {

        void onTabClick(TabItem tabItem);
    }
}
