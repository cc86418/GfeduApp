package cn.jun.logistics;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cn.jun.bean.CustomBean;
import cn.jun.indexmain.interfaces.OnLoadMoreListener;
import cn.jun.indexmain.interfaces.OnMultiTypeItemClickListeners;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import cn.jun.logistics.adapter.AllLogisticsAdapter;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;


public class AllLogisticsFragment extends Fragment {
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private View mView;
    //listview
    private ListView mListView;
    private RecyclerView mRecyclerView;
    private boolean isFailed = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AllLogisticsAdapter mAdapter;
    private static int isEndList = 2;//加载状态 0,完成 1,失败
    private ArrayList<CustomBean> CustomList;
    private CustomBean customBean ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.alllogistics_layout, container, false);
            initView(mView);
//            initData();
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    private void initView(View mView) {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);

        //初始化adapter
        mAdapter = new AllLogisticsAdapter(getActivity(), null, true);
        mAdapter.setAddHaed(false);//设置有头部

        //全部首页列表点击事件
        mAdapter.setOnMultiTypeItemClickListener(new OnMultiTypeItemClickListeners<CustomBean>() {
            @Override
            public void onItemClick(CommonViewHolder viewHolder, CustomBean data, int position, int viewType) {
                if (data == null) {
//                    viewHolder.getView(R.id.pointgroup).setBackgroundResource(R.drawable.ic_camera);
                } else {
//                    Toast.makeText(getActivity(), data.Product_Name, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //刷新数据监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });
        //加载更多事件
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        loadData();//初始化数据
    }

    //初始化数据
    private void loadData() {
//        PageIndex = 1;
//        ProductListInfo(0, PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (!"".equals(CustomList) && null != CustomList) {
                CustomList = new ArrayList<CustomBean>();
                for (int i = 0; i < 25; i++) {
                    customBean = new CustomBean();
                    customBean.Product_Name = "1A -- " + i;
                    customBean.Product_Intro = "1B -- " + i;
                    customBean.Product_IntroName = "1C -- " + i;
                    CustomList.add(customBean);
                }
                mAdapter.setInitData(CustomList);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setLoadingView(R.layout.load_loading);
//                } else {//无数据处理
//
//                }

            }
        }, 1000);
    }


    /**
     * 加载更多数据
     */
    private void loadMore() {
//        PageIndex = PageIndex + 1;
//        ProductListInfo(0, PageIndex);
//        CustomList = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (mAdapter.getItemCount() > 30 && isFailed) {
                if (1 == isEndList && isFailed) {
                    isFailed = false;
                    //加载失败
                    mAdapter.setLoadFailedView(R.layout.load_failed);
                } else if (0 == isEndList) {
                    //加载完成
                    mAdapter.setLoadEndView(R.layout.load_end);
                } else if (2 == isEndList) {
                    CustomList = new ArrayList<CustomBean>();
                    for (int i = 26; i < 50; i++) {
                        customBean.Product_Name = "1A -- " + i;
                        customBean.Product_Intro = "1B -- " + i;
                        customBean.Product_IntroName = "1C -- " + i;
                        CustomList.add(customBean);
                    }
                    mAdapter.setLoadMoreData(CustomList);
                }
            }
        }, 1000);
    }

}
