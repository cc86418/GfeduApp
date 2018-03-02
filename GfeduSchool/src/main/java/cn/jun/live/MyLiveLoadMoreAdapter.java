//package cn.jun.live;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.jun.bean.CustomBean;
//import cn.jun.bean.GetMyLiveBean;
//import cn.jun.bean.Model;
//import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
//import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
//import cn.jun.indexmain.GridViewAdapter;
//import cn.jun.indexmain.ImageSlideshow.ImageSlideshow;
//import cn.jun.indexmain.RecyclerviewViewPagerAdapter;
//import cn.jun.indexmain.base.MultiTypeBaseAdapter;
//import cn.jun.indexmain.viewholder.CommonViewHolder;
//import cn.jun.utils.HttpUtils;
//import cn.jun.view.LoadMoreViewPager;
//import jc.cici.android.R;
//import jc.cici.android.atom.ui.courselist.AllCourseAcitivity;
//import jc.cici.android.atom.ui.courselist.SubCourseActivity;
//
//import static cn.gfedu.gfeduapp.DiscoverFragment.NoAllList;
//import static cn.gfedu.gfeduapp.DiscoverFragment.pager;
//import static cn.gfedu.gfeduapp.DiscoverFragment.projectListBean;
//import static cn.gfedu.gfeduapp.GfeduApplication.application;
//import static cn.jun.indexmain.NewsFragment.AdsList;
//
//
//public class MyLiveLoadMoreAdapter extends MultiTypeBaseAdapter<GetMyLiveBean> {
//    //    Handler mHandler = new Handler();
//    //总的页数
//    private int pageCount;
//    //每一页显示的个数
//    private int pageSize = 8;
//    //当前显示的是第几页
//    private int curIndex = 0;
//    //    private ViewPager mPager;
//    private LoadMoreViewPager mPager;
//    private List<View> mPagerList;
//    private List<Model> mDatas;
//    public LinearLayout mLlDot;
//    private Context context;
//    private LayoutInflater inflater;
//    private boolean setdotView = true;//设置圆点标识符
//    //顶部轮播图片
//    private ImageSlideshow imageSlideshow;
//
//
//
//
//    public MyLiveLoadMoreAdapter(Context context, List<GetMyLiveBean> datas, boolean isOpenLoadMore) {
//        super(context, datas, isOpenLoadMore);
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//        Log.i("LoadMoreAdapter", "LoadMoreAdapter");
//    }
//
//    protected void setHeaderView(CommonViewHolder holder) {
//        Log.i("setHeaderView", "setHeaderView");
//        /**顶部广告位**/
//        initViewPager(holder);
//        /**中间圆点项目**/
//        initDatas(holder);
//        /**列表页更多**/
//        initMoreClick(holder);
//    }
//
//    private void initMoreClick(CommonViewHolder holder) {
//        TextView MoreTv = (TextView) holder.getView(R.id.gengduo_tv);
//        MoreTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("热门班级==>", "点击更多");
//                int CT_ID = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_ID();
//                String CT_NAME = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_Name();
//                Log.i("热门传递的参数 --- ",""+ CT_ID);
//                Log.i("热门传递的参数 --- ",""+ CT_NAME);
//
//                Intent mPager = new Intent(context, AllCourseAcitivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("CT_ID", CT_ID);
//                bundle.putString("CT_NAME", CT_NAME);
//                mPager.putExtras(bundle);
//                mContext.startActivity(mPager);
//            }
//        });
//    }
//
//    private void initViewPager(CommonViewHolder holder) {
//        imageSlideshow = (ImageSlideshow) holder.getView(R.id.is_gallery);
//        // 为ImageSlideshow设置数据
//        imageSlideshow.setDotSpace(12);
//        imageSlideshow.setDotSize(12);
//        imageSlideshow.setDelay(3000);
//        if (setdotView) {
//            if(!"".equals(AdsList.get(0).getBody().getList()) && null!= AdsList.get(0).getBody().getList()){
//                for (int i = 0; i < AdsList.get(0).getBody().getList().size(); i++) {
//                    imageSlideshow.addImageUrl(AdsList.get(0).getBody().getList().get(i).getImgUrl());
//                }
//            }
//            imageSlideshow.commit();
//            setdotView = false;
//        }
//        imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Log.i("轮播图下标==> ", " " + position);
//                //2-班级 5-套餐
//                int ProductType = AdsList.get(0).getBody().getList().get(position).getProductType();
//                int ProductId = AdsList.get(0).getBody().getList().get(position).getProductId();
//                if (2 == ProductType) {
//                    Intent intent = new Intent(context, OnlineCourseDetailsAloneActivityTwo.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("Product_PKID", ProductId);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                } else if (5 == ProductType) {
//                    Intent intent = new Intent(context, OnlineCourseDetailsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("Product_PKID", ProductId);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//
//            }
//        });
//
//    }
//
//    /**
//     * 初始化数据源
//     */
//    private void initDatas(CommonViewHolder holder) {
//        mDatas = new ArrayList<Model>();
//        Log.i("数据 ---- ", "" + NoAllList.get(0).getBody().getProjectList().size());
//        if (!"".equals(NoAllList) && null != NoAllList) {
//            for (int i = 0; i < NoAllList.get(0).getBody().getProjectList().size(); i++) {
//                //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
//                int imageId = context.getResources().getIdentifier("ic_category_" + i, "mipmap", context.getPackageName());
//                String CT_NAME = NoAllList.get(0).getBody().getProjectList().get(i).getCT_Name();
//                int CT_ID = NoAllList.get(0).getBody().getProjectList().get(i).getCT_ID();
//                mDatas.add(new Model(CT_NAME, imageId, CT_ID));
//            }
//            //总的页数=总数/每页数量，并取整
//            mPager = (LoadMoreViewPager) holder.getView(R.id.viewpager);
//            mLlDot = (LinearLayout) holder.getView(R.id.ll_dot);
//            pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
//            mPagerList = new ArrayList<View>();
//            for (int i = 0; i < pageCount; i++) {
//                //每个页面都是inflate出一个新实例
//                GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
//                gridView.setAdapter(new GridViewAdapter(context, mDatas, i, pageSize));
//                mPagerList.add(gridView);
//                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        int pos = position + curIndex * pageSize;
//                        int CT_ID = mDatas.get(pos).getCt_id();
//                        String CT_NAME = mDatas.get(pos).getName();
////                        Toast.makeText(context, "下标" + pos + "项目ID=>" + CT_ID + "项目名字=>" + CT_NAME, Toast.LENGTH_SHORT).show();
//                        Intent mPager = new Intent(context, SubCourseActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("CT_ID", CT_ID);
//                        bundle.putString("CT_NAME", CT_NAME);
//                        mPager.putExtras(bundle);
//                        mContext.startActivity(mPager);
//
//                    }
//                });
//            }
//            //设置适配器
////        Activity activity = (Activity) context;
//            mPager.setAdapter(new RecyclerviewViewPagerAdapter(mPagerList));
//            //设置圆点
//            setOvalLayout();
//        } else {//无数据处理
//
//        }
//
//
//    }
//
//    /**
//     * 设置圆点
//     */
//    public void setOvalLayout() {
//        if (setdotView) {
//            for (int i = 0; i < pageCount; i++) {
//                mLlDot.addView(inflater.inflate(R.layout.dot, null));
//            }
//            // 默认显示第一页
//            mLlDot.getChildAt(0).findViewById(R.id.v_dot)
//                    .setBackgroundResource(R.drawable.shape_point_selected);
//        } else {
//            curIndex = 0;
//            mLlDot.removeAllViews();
//            for (int i = 0; i < pageCount; i++) {
//                mLlDot.addView(inflater.inflate(R.layout.dot, null));
//            }
//            // 默认显示第一页
//            mLlDot.getChildAt(curIndex).findViewById(R.id.v_dot)
//                    .setBackgroundResource(R.drawable.shape_point_selected);
//        }
//        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageSelected(int position) {
//                // 取消圆点选中
//                mLlDot.getChildAt(curIndex)
//                        .findViewById(R.id.v_dot)
//                        .setBackgroundResource(R.drawable.shape_point_normal);
//                // 圆点选中
//                mLlDot.getChildAt(position)
//                        .findViewById(R.id.v_dot)
//                        .setBackgroundResource(R.drawable.shape_point_selected);
//                curIndex = position;
//                Log.i("二次滑动 --- ", "二次滑动 ---");
//                Log.i("curIndex --- ", "" + curIndex);
//                Log.i("position --- ", "" + position);
//
//
//            }
//
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });
//        setdotView = false;
//    }
//
//
//    @Override
//    protected void convert(CommonViewHolder holder, final GetMyLiveBean data, int viewType) {
//        if (viewType == 0) {//设置头部
//            setHeaderView(holder);
//        } else {
//            String IntroName = data.Product_IntroName.replace("&nbsp;", "");
//            String Intro = data.Product_Intro.replace("&nbsp;", "");
//            String htmlText = "<font color='#DD5555'>" + IntroName
//                    + "</font>" + "  " + "<font color='#666666'>" + Intro + "</font>";
//            holder.setText(R.id.text1, data.Product_Name.replace("&nbsp;", ""));
//            holder.setText(R.id.red_tv, htmlText);
//            holder.setText(R.id.price_saleregion, data.Product_PriceSaleRegion.replace("&nbsp;", ""));
//            //免费试听
//            Button mfst_tv = (Button) holder.getView(R.id.mfst_tv);
//            mfst_tv.getBackground().setAlpha(150);
//            int Product_OutlineFreeState = data.Product_OutlineFreeState;
//            Log.i("OutlineFreeState ",""+ Product_OutlineFreeState);
//            if(1==Product_OutlineFreeState){
//                mfst_tv.setVisibility(View.VISIBLE);
//            }else if(0==Product_OutlineFreeState){
//                mfst_tv.setVisibility(View.GONE);
//            }
//            //设置图片
//            ImageView list_im = (ImageView) holder.getView(R.id.list_im);
//            String im_url = data.Product_MobileImage;
//            HttpUtils httpUtils = new HttpUtils();
//            if(httpUtils.isNetworkConnected(context)){
//                Log.i("im_url -- ",""+ im_url);
//                Glide.with(application.getApplicationContext())
//                        .load(im_url)
//                        .placeholder(R.drawable.pic_kong_xiangqing)
//                        .into(list_im);
//            }
//
//
//
//
//        }
//    }
//
//    @Override
//    protected int getItemLayoutId(int viewType) {
//        if (viewType == 0) {
//            return R.layout.recycleview_header;
//        }
//        return R.layout.my_recycleview_item;
//    }
//
//    @Override
//    protected int getViewType(int position, GetMyLiveBean data) {
//        if (data == null) {
//            return 0;
//        } else {
//            return 1;
//        }
//
//
//    }
//
//}
