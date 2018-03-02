package cn.gfedu.home_pager;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TabLayout_Two extends LinearLayout implements View.OnClickListener {

    private ArrayList<TabItem> tabs;
    private OnTabClickListener listener;
    private View selectView;
    private int tabCount;


    public TabLayout_Two(Context context) {
        super(context);
        initView();
    }

    public TabLayout_Two(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabLayout_Two(Context context, AttributeSet attrs, int defStyle) {
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
            TabView_Two view = (TabView_Two) getChildAt(i);
            view.onDataChanged(badgeCount);
        }
    }


    public void initData(ArrayList<TabItem> tabs, OnTabClickListener listener) {
        this.tabs = tabs;
        this.listener = listener;
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if (tabs != null && tabs.size() > 0) {
            tabCount = tabs.size();
            TabView_Two mTabView = null;
            for (int i = 0; i < tabs.size(); i++) {
                mTabView = new TabView_Two(getContext());
                mTabView.setTag(tabs.get(i));
                mTabView.initData(tabs.get(i));
                mTabView.setOnClickListener(this);
                addView(mTabView, params);
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
