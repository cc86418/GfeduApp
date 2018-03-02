package cn.jun.indexmain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.gfedu.gfeduapp.BaseFragment;
import cn.gfedu.gfeduapp.DiscoverFragment;
import cn.jun.bean.AdsListBean;
import cn.jun.bean.Const;
import cn.jun.bean.CustomBean;
import cn.jun.bean.ProductListBean;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import cn.jun.indexmain.interfaces.OnLoadMoreListener;
import cn.jun.indexmain.interfaces.OnMultiTypeItemClickListeners;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;

import static cn.gfedu.gfeduapp.DiscoverFragment.projectListBean;
import static cn.gfedu.gfeduapp.DiscoverFragment.projectList_noAllBean;

public class NewsFragment extends BaseFragment {
    //	private static final String ARG_POSITION = "position";
    //进度
    private Dialog mDialog;
    private int FragmentPosition;
    private String IsAllOption;
    public static int CT_ID;
    public static String CT_NAME;
    //    public static ArrayList<ProjectList_NoAllBean> NoAllList = new ArrayList<>();
    private View view;

    private LoadMoreAdapter mAdapter;
    private LoadMoreAdapter_NoAll mAdapter_no;
    private RecyclerView mRecyclerView;
    private boolean isFailed = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HttpUtils httpUtils = new HttpUtils();
    //广告位数据源
    private AdsListBean AdsListBean = new AdsListBean();
    public static List<AdsListBean> AdsList;
    //为你推荐
    private ProductListBean ForUListBean = new ProductListBean();
    public static ArrayList<ProductListBean> ForUList = new ArrayList<>();
    public static ArrayList<CustomBean> ForUcustomList;
    //列表数据源
    private ProductListBean ProductListBean = new ProductListBean();
    private ArrayList<ProductListBean> ProductList;
    private CustomBean customBean = new CustomBean();
    private ArrayList<CustomBean> CustomList;

    private int PageIndex;
    private static final int PageSizse = 10;
    private static int isEndList;//加载状态 0,完成 1,失败

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private String isAll;

//	public static NewsFragment newInstance(int position) {
//		NewsFragment f = new NewsFragment();
//		Bundle b = new Bundle();
//		b.putInt(ARG_POSITION, position);
//		f.setArguments(b);
//		return f;
//	}

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		FragmentPosition = getArguments().getInt(ARG_POSITION);
        FragmentPosition = getArguments().getInt("FragmentPosition");
        IsAllOption = getArguments().getString("IsAllOption");
//        NoAllList = (ArrayList<ProjectList_NoAllBean>) getArguments().getSerializable("NoAllList");
        CT_ID = getArguments().getInt("CT_ID");
        CT_NAME = getArguments().getString("CT_NAME");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            if ("全部".equals(IsAllOption)) {
                isAll = "1";
                view = inflater.inflate(R.layout.all_option_layout, container, false);
//                isPrepared = true;
//                lazyLoad();
                initDatas(view, isAll, CT_ID, "");
            } else {
                isAll = "0";
                view = inflater.inflate(R.layout.no_all_option_layout, container, false);
//                isPrepared = true;
//                lazyLoad();
                initDatas(view, isAll, CT_ID, CT_NAME);

            }
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    private void initDatas(View view, String isAll, int CT_ID, String CT_NAME) {
        if ("1".equals(isAll)) {
//            HaveHeaderView(view);
            Log.i("initDatas", "initDatas");
            if (httpUtils.isNetworkConnected(getActivity())) {
                RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
                no_date_realtive.setVisibility(View.GONE);
                GetIndexPagerTask IndexPagerTask = new GetIndexPagerTask();
                IndexPagerTask.execute(0);
                HaveHeaderView(view);
            } else {
                RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
                no_date_realtive.setVisibility(View.VISIBLE);
            }
//            课程更新中,敬请期待
        } else if ("0".equals(isAll)) {
//            NoHaveHeaderView(view, CT_NAME);
            if (httpUtils.isNetworkConnected(getActivity())) {
                RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
                no_date_realtive.setVisibility(View.GONE);
                GetIndexPagerTask IndexPagerTask = new GetIndexPagerTask();
                IndexPagerTask.execute(CT_ID);
                NoHaveHeaderView(view);
            } else {
                RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
                no_date_realtive.setVisibility(View.VISIBLE);
            }

        }
    }

    private void HaveHeaderView(View view) {
        Log.i("HaveHeaderView", "HaveHeaderView");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        //初始化adapter
        mAdapter = new LoadMoreAdapter(getActivity(), null, true);
        mAdapter.setAddHaed(true);//设置有头部

        //全部首页列表点击事件
        mAdapter.setOnMultiTypeItemClickListener(new OnMultiTypeItemClickListeners<CustomBean>() {
            @Override
            public void onItemClick(CommonViewHolder viewHolder, CustomBean data, int position, int viewType) {
                if (data == null) {
//                    viewHolder.getView(R.id.pointgroup).setBackgroundResource(R.drawable.ic_camera);
                } else {
//                    Toast.makeText(getActivity(), data.Product_Name, Toast.LENGTH_SHORT).show();
                    int Type = data.Type;
                    //产品类型： 2-班级 5-套餐
                    if (2 == Type) {
                        Intent intent = new Intent(getActivity(), OnlineCourseDetailsAloneActivityTwo.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", data.Product_PKID);

                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (5 == Type) {
                        Intent intent = new Intent(getActivity(), OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", data.Product_PKID);

                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

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
                Log.i("加载更多事件 ---------- ", "--ALL--");
                loadMore();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        loadData();//初始化数据

    }

    private void NoHaveHeaderView(View view) {
        Log.i("NoHaveHeaderView -- ", " -- NoHaveHeaderView");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        //初始化adapter
        mAdapter_no = new LoadMoreAdapter_NoAll(getActivity(), null, true);
        mAdapter_no.setAddHaed(true);//设置有头部

        //剩余项目列表点击事件
        mAdapter_no.setOnMultiTypeItemClickListener(new OnMultiTypeItemClickListeners<CustomBean>() {
            @Override
            public void onItemClick(CommonViewHolder viewHolder, CustomBean data, int position, int viewType) {
                if (data == null) {
//                    viewHolder.getView(R.id.pointgroup).setBackgroundResource(R.drawable.ic_camera);
                } else {
//                    Toast.makeText(getActivity(), data.Product_Name, Toast.LENGTH_SHORT).show();
                    int Type = data.Type;
                    //产品类型： 2-班级 5-套餐
                    if (2 == Type) {
                        Intent intent = new Intent(getActivity(), OnlineCourseDetailsAloneActivityTwo.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", data.Product_PKID);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (5 == Type) {
                        Intent intent = new Intent(getActivity(), OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", data.Product_PKID);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        //刷新数据监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadDataNoAll();
                loadDataNoAllRefresh();

            }
        });
        //加载更多事件
        mAdapter_no.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                Log.i("加载更多事件 ---------- ", "----");
                loadMoreNoAll();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter_no);

        loadDataNoAll();//初始化数据




    }

    /**
     * 初始化列表数据
     */
    private void loadData() {
        Log.i("loadData", "loadData");
//        Log.i("refrsh_id", "" + DiscoverFragment.tabs.getPagerPosition());
        PageIndex = 1;
//        ProductListInfo(0, PageIndex);
        int id = projectListBean.getBody().getProjectList().get(DiscoverFragment.tabs.getPagerPosition()).getCT_ID();
        ProductListInfo(id, PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!"".equals(CustomList) && null != CustomList) {
                    mAdapter.setInitData(CustomList);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.setLoadingView(R.layout.load_loading);
                } else {//无数据处理

                }

            }
        }, 1000);
    }

    /**
     * 加载更多数据
     */
    private void loadMore() {
        PageIndex = PageIndex + 1;
        ProductListInfo(0, PageIndex);
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
                } else {
                    mAdapter.setLoadMoreData(CustomList);
                }
            }
        }, 1000);
    }

    /**
     * 初始化列表数据
     */
    private void loadDataNoAll() {
        PageIndex = 1;
        ProductListInfo(CT_ID, PageIndex);
//        int id = projectListBean.getBody().getProjectList().get(DiscoverFragment.tabs.getPagerPosition()).getCT_ID();
//        ProductListInfo(id, PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (!"".equals(CustomList) && null != CustomList) {
                mAdapter_no.setInitData(CustomList);
                mSwipeRefreshLayout.setRefreshing(false);
                //下拉刷新 努力加载中显示..暂时取消
                mAdapter_no.setLoadingView(R.layout.load_loading);
//                } else {//无数据处理
//                    Log.i("fragment == > ","无数据 ");
//                }
            }
        }, 1000);
    }

    /**
     * 初始化列表数据
     */
    private void loadDataNoAllRefresh() {
        Log.i("refrsh_id", "" + DiscoverFragment.tabs.getPagerPosition());
        PageIndex = 1;
//        ProductListInfo(CT_ID, PageIndex);
        int id = projectListBean.getBody().getProjectList().get(DiscoverFragment.tabs.getPagerPosition()).getCT_ID();
        ProductListInfo(id, PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (!"".equals(CustomList) && null != CustomList) {
                mAdapter_no.setInitData(CustomList);
                mSwipeRefreshLayout.setRefreshing(false);
                //下拉刷新 努力加载中显示..暂时取消
//                mAdapter_no.setLoadingView(R.layout.load_loading);
//                } else {//无数据处理
//                    Log.i("fragment == > ","无数据 ");
//                }
            }
        }, 1000);
    }

    /**
     * 加载更多数据
     */
    private void loadMoreNoAll() {
        Log.i("loadMoreNoAll ---- ", "loadMoreNoAll");
        PageIndex = PageIndex + 1;
        int id = 0;
//        ProductListInfo(CT_ID, PageIndex);
        if (!"".equals(projectList_noAllBean.getBody().getProjectList()) && null != projectList_noAllBean.getBody().getProjectList()) {
            id = projectList_noAllBean.getBody().getProjectList().get(DiscoverFragment.tabs.getPagerPosition()-1).getCT_ID();
            Log.i("加载更多ID ====> ",""+ id);
            Gson s = new Gson();
            Log.i("数据 === 》 ",""+ s.toJson(projectList_noAllBean.getBody().getProjectList()).toString());
        }

        ProductListInfo(id, PageIndex);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (1 == isEndList && isFailed) {
                    isFailed = false;
                    //加载失败
                    mAdapter_no.setLoadFailedView(R.layout.load_failed);
                } else if (0 == isEndList) {
                    //加载完成
                    mAdapter_no.setLoadEndView(R.layout.load_end);
                } else {
                    mAdapter_no.setLoadMoreData(CustomList);
                }
            }
        }, 1000);
    }

    private void ProductListInfo(int CT_ID, int PageIndex) {
        if (httpUtils.isNetworkConnected(getActivity())) {
            Log.i("ProductListInfo - CTID ", "" + CT_ID);
            ProductListTask productTask = new ProductListTask();
            productTask.execute(CT_ID, PageIndex);

            ForUListTask forUListTask = new ForUListTask();
            forUListTask.execute(CT_ID, PageIndex);
        }

    }

    class ForUListTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int CT_ID = params[0];
            int PageIndex = params[1];
            try {
                ForUListBean = httpUtils.getProductListBean(Const.URL + Const.GetProductListAPI, PageIndex, PageSizse, CT_ID, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(ForUListBean.getCode())) {
                ForUList.add(ForUListBean);
                if (null != ForUListBean.getBody().getList() && !"".equals(ForUListBean.getBody().getList())) {
                    ForUcustomList = new ArrayList<>();
                    for (int i = 0; i < ForUListBean.getBody().getList().size(); i++) {
                        CustomBean bean = new CustomBean();
                        bean.Product_PKID = ForUListBean.getBody().getList().get(i).Product_PKID;
                        bean.Type = ForUListBean.getBody().getList().get(i).Type;
                        bean.Product_Name = ForUListBean.getBody().getList().get(i).Product_Name;
                        bean.Product_IntroName = ForUListBean.getBody().getList().get(i).Product_IntroName;
                        bean.Product_Intro = ForUListBean.getBody().getList().get(i).Product_Intro;
                        bean.Product_PriceSaleRegion = ForUListBean.getBody().getList().get(i).Product_PriceSaleRegion;
                        bean.Product_MobileImage = ForUListBean.getBody().getList().get(i).Product_MobileImage;
                        bean.Product_OutlineFreeState = ForUListBean.getBody().getList().get(i).Product_OutlineFreeState;
                        ForUcustomList.add(bean);
                    }
                }
            }
        }
    }

    class ProductListTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int CT_ID = params[0];
            int PageIndex = params[1];
            try {
                ProductListBean = httpUtils.getProductListBean(Const.URL + Const.GetProductListAPI, PageIndex, PageSizse, CT_ID, 4);
//                ForUListBean = httpUtils.getProductListBean(Const.URL + Const.GetProductListAPI, PageIndex, PageSizse, CT_ID, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if ("100".equals(ForUListBean.getCode())) {
//                ForUList.add(ForUListBean);
//                if (null != ForUListBean.getBody().getList() && !"".equals(ForUListBean.getBody().getList())) {
//                    ForUcustomList = new ArrayList<>();
//                    for (int i = 0; i < ForUListBean.getBody().getList().size(); i++) {
//                        CustomBean bean = new CustomBean();
//                        bean.Product_PKID = ForUListBean.getBody().getList().get(i).Product_PKID;
//                        bean.Type = ForUListBean.getBody().getList().get(i).Type;
//                        bean.Product_Name = ForUListBean.getBody().getList().get(i).Product_Name;
//                        bean.Product_IntroName = ForUListBean.getBody().getList().get(i).Product_IntroName;
//                        bean.Product_Intro = ForUListBean.getBody().getList().get(i).Product_Intro;
//                        bean.Product_PriceSaleRegion = ForUListBean.getBody().getList().get(i).Product_PriceSaleRegion;
//                        bean.Product_MobileImage = ForUListBean.getBody().getList().get(i).Product_MobileImage;
//                        bean.Product_OutlineFreeState = ForUListBean.getBody().getList().get(i).Product_OutlineFreeState;
//                        ForUcustomList.add(bean);
//                    }
//                }
//            }
            if ("100".equals(ProductListBean.getCode())) {
                ProductList = new ArrayList<>();
                ProductList.add(ProductListBean);
                isEndList = ProductList.get(0).getBody().getProductCount();
                if (ProductList.get(0).getBody().getProductCount() > 0) {
                    if (null != ProductListBean.getBody().getList() && !"".equals(ProductListBean.getBody().getList())) {
                        CustomList = new ArrayList<>();
                        for (int i = 0; i < ProductListBean.getBody().getList().size(); i++) {
                            CustomBean bean = new CustomBean();
                            bean.Product_PKID = ProductListBean.getBody().getList().get(i).Product_PKID;
                            bean.Type = ProductListBean.getBody().getList().get(i).Type;
                            bean.Product_Name = ProductListBean.getBody().getList().get(i).Product_Name;
                            bean.Product_IntroName = ProductListBean.getBody().getList().get(i).Product_IntroName;
                            bean.Product_Intro = ProductListBean.getBody().getList().get(i).Product_Intro;
                            bean.Product_PriceSaleRegion = ProductListBean.getBody().getList().get(i).Product_PriceSaleRegion;
                            bean.Product_MobileImage = ProductListBean.getBody().getList().get(i).Product_MobileImage;
                            bean.Product_OutlineFreeState = ProductListBean.getBody().getList().get(i).Product_OutlineFreeState;
                            CustomList.add(bean);
                        }

                    }
                }
            } else {
                isEndList = 1;
            }
        }
    }

//    private void GetIndexPagerInfo(int CT_ID) {
//        GetIndexPagerTask IndexPagerTask = new GetIndexPagerTask();
//        IndexPagerTask.execute(CT_ID);
//    }

    class GetIndexPagerTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
//            showProcessDialog(getActivity(), R.layout.loading_show_dialog_color);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int CT_ID = params[0];
            try {
                AdsListBean = httpUtils.getAdsListBean(Const.URL + Const.GetAdsListAPI, CT_ID);
//                if ("100".equals(AdsListBean.getCode())) {
//                    AdsList.add(AdsListBean);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(AdsListBean.getCode())) {
                if (!"".equals(AdsListBean.getBody().getList()) && null != AdsListBean.getBody().getList()) {
                    AdsList = new ArrayList<>();
                    AdsList.add(AdsListBean);

//                    if("0".equals(isAll)){
//                        HaveHeaderView(view);
//                    }else {
//                        NoHaveHeaderView(view);
//                    }
                }
            } else {
                RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
                no_date_realtive.setVisibility(View.VISIBLE);
            }
//            mDialog.dismiss();
//            DiscoverFragment.mDialog.dismiss();
        }
    }


}