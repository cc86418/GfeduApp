package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import jc.cici.android.R;
import jc.cici.android.atom.bean.Ads;
import jc.cici.android.atom.bean.Product;
import jc.cici.android.atom.ui.courselist.AllCourseAcitivity;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 子项目适配器
 * Created by atom on 2017/7/31.
 */

public class SubCourseRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    private ArrayList<Ads> adsList;
    private ArrayList<Product> mData;
    private ArrayList<Product> recommendList;
    private View mHeaderView;
    // 头布局
    private static final int TYPE_HEADER = 0xff01;
    // 热门班型类型内容
    private static final int TYPE_HOTCLASS_CONTENT = 0xff02;
    // 底部布局
    private static final int TYPE_FOOT = 0xff03;
    // 广告点数组
    private ImageView[] imgViews;
    // 广告点
    private ImageView dots_img;
    private ImageView ads;
    // 广告图片
    private ArrayList<View> adsViews = new ArrayList<View>();
    // 项目id
    private int mProjectId;
    // 项目名称
    private String mTitle;
    //当前轮播页
    private int currentItem = 0;
    //自动轮播启用
    private boolean isAutoPlay = true;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //正在加载更多
    static final int LOADING_MORE = 1;
    //没有更多
    static final int NO_MORE = 2;
    // 没有数据
    static final int NO_DATA = 3;
    //脚布局当前的状态,默认为没有更多
    int footer_state = 1;

    public SubCourseRecAdapter(Context context, ArrayList<Product> data, ArrayList<Ads> AdsList, ArrayList<Product> RecommedList, int projectId, String title) {

        this.mCtx = context;
        this.mData = data;
        this.adsList = AdsList;
        this.recommendList = RecommedList;
        this.mProjectId = projectId;
        this.mTitle = title;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(mCtx).inflate(R.layout.item_head, parent, false);
                return new TypeHeaderHolder(view);
            case TYPE_HOTCLASS_CONTENT: // 热门班级内容
                view = LayoutInflater.from(mCtx).inflate(R.layout.item_hotcourse_view, parent, false);
                return new TypeHotContentHolder(view);
            case TYPE_FOOT: // 底部加载更多布局
                view = LayoutInflater.from(mCtx).inflate(R.layout.item_loading_more, parent, false);
                return new TypeFootHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int pos = getRealPosition(holder);
        if (holder instanceof TypeHotContentHolder) {
            // 热门班级内容
            bindTypeHotContentHolder((TypeHotContentHolder) holder, pos);
        } else if (holder instanceof TypeHeaderHolder) {
            // 头布局
            bindTypeHeaderHolder((TypeHeaderHolder) holder, pos);
        } else if (holder instanceof TypeFootHolder) {
            TypeFootHolder footHolder = (TypeFootHolder) holder;
            if (position == 0) {//如果第一个就是脚布局,,那就让他隐藏
                footHolder.progressBar.setVisibility(View.GONE);
                footHolder.loadingMore_txt.setText("");
            }
            switch (footer_state) {//根据状态来让脚布局发生改变
                case LOADING_MORE:
                    footHolder.progressBar.setVisibility(View.VISIBLE);
                    footHolder.loadingMore_txt.setText("加载更多");
                    footHolder.loadingMore_txt.setVisibility(View.VISIBLE);
                    break;
                case NO_MORE:
                    footHolder.progressBar.setVisibility(View.GONE);
                    footHolder.loadingMore_txt.setText("已经到底部");
                    footHolder.loadingMore_txt.setVisibility(View.VISIBLE);
                    break;
                case NO_DATA:
                    footHolder.progressBar.setVisibility(View.GONE);
                    footHolder.loadingMore_txt.setText("");
                    break;

            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    /**
     * 头布局
     *
     * @param holder
     * @param position
     */
    private void bindTypeHeaderHolder(TypeHeaderHolder holder, int position) {
        // 获取广告列表
        if (null != adsList && adsList.size() > 0) {
            if (isAutoPlay) {
                // 设置只加载一次
                isAutoPlay = false;
                // 创建广告点数组
                imgViews = new ImageView[adsList.size()];
                for (int i = 0; i < adsList.size(); i++) {
                    View view = LayoutInflater.from(mCtx)
                            .inflate(R.layout.adsview, null);
                    view.setId(i);
                    view.setOnClickListener(holder);
                    ads = (ImageView) view.findViewById(R.id.adsImage);
                    Glide.with(mCtx).load(adsList.get(i).getImgUrl())
                            .placeholder(R.drawable.binner_loading_img) //加载中显示的图片
                            .error(R.drawable.binner_loading_img) //加载失败时显示的图片
                            .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                            .centerCrop() // 中心剪裁
                            .skipMemoryCache(true) // 跳过缓存
                            .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                            .into(ads);
                    adsViews.add(view);
                    // 添加白点
                    dots_img = new ImageView(mCtx);
                    dots_img.setLayoutParams(new ViewGroup.LayoutParams(20, 5));
                    imgViews[i] = dots_img;
                    if (i == 0) { // 第一张默认加载
                        imgViews[i]
                                .setBackgroundResource(R.drawable.shape_dot_orage);
                    } else {
                        imgViews[i]
                                .setBackgroundResource(R.drawable.shape_dot_normal);
                    }
                    holder.point_layout.addView(imgViews[i]);
                    holder.ads_vp.setAdapter(new MyPagerAdapter());
                    holder.ads_vp.addOnPageChangeListener(new MyPageChangeListener(holder.ads_vp));
                    holder.startPlay();
                }
            }
        } else {
            Toast.makeText(mCtx, "暂无广告内容", Toast.LENGTH_SHORT).show();
        }

        if (null != recommendList && recommendList.size() > 0) {
            Glide.with(mCtx).load(recommendList.get(0).getProduct_MobileImage())
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(holder.recommendImg1);
            if (recommendList.size() > 1) {
                Glide.with(mCtx).load(recommendList.get(1).getProduct_MobileImage())
                        .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                        .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                        .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                        .override(280, 186) // 设置最终显示图片大小
                        .centerCrop() // 中心剪裁
                        .skipMemoryCache(true) // 跳过缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                        .into(holder.recommendImg2);
            }

            holder.recommendImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int courseType = recommendList.get(0).getType();
                    if (5 == courseType) { // 套餐
                        Intent it = new Intent(mCtx, OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", recommendList.get(0).getProduct_PKID());
                        it.putExtras(bundle);
                        mCtx.startActivity(it);
                    } else if (2 == courseType) { // 班级
                        Intent it = new Intent(mCtx, OnlineCourseDetailsAloneActivityTwo.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", recommendList.get(0).getProduct_PKID());
                        it.putExtras(bundle);
                        mCtx.startActivity(it);
                    }
                }
            });
            if (recommendList.size() > 1) {
                holder.recommendImg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int courseType = recommendList.get(1).getType();
                        if (5 == courseType) { // 套餐
                            Intent it = new Intent(mCtx, OnlineCourseDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", recommendList.get(1).getProduct_PKID());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        } else if (2 == courseType) { // 班级
                            Intent it = new Intent(mCtx, OnlineCourseDetailsAloneActivityTwo.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", recommendList.get(1).getProduct_PKID());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }
                    }
                });
            }

        } else {
            Toast.makeText(mCtx, "暂无推荐内容", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 填充热门班级内容
     *
     * @param holder
     * @param position
     */
    private void bindTypeHotContentHolder(TypeHotContentHolder holder, int position) {

        Glide.with(mCtx).load(mData.get(position).getProduct_MobileImage())
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(holder.courseImg);
        // 热门班级名称
        holder.courseName_txt.setText(ToolUtils.strReplaceAll(mData.get(position).getProduct_Name()));
        // 打折内容
        String charStr = ToolUtils.strReplaceAll(mData.get(position).getProduct_IntroName());
        String saleStr = ToolUtils.strReplaceAll(mData.get(position).getProduct_Intro());
        if (null != charStr && !"".equals(charStr)) { // 优惠部分红色显示
            SpannableString spannableString = new SpannableString(charStr + saleStr);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#dd5555")), 0, charStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.courseSale_txt.setText(spannableString);
        } else {
            holder.courseSale_txt.setText(saleStr);
        }
        // 价格
        holder.coursePrice_txt.setText("￥" + ToolUtils.strReplaceAll(mData.get(position).getProduct_PriceSaleRegion()));
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mData.size() : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position + 1 == getItemCount()) { // 底部控件
            return TYPE_FOOT;
        } else if (mHeaderView == null) {
            return TYPE_HOTCLASS_CONTENT;
        } else { // 热门班级
            return TYPE_HOTCLASS_CONTENT;
        }
    }

    /**
     * 热门内容holder
     */
    class TypeHotContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // 课程图片
        @BindView(R.id.courseImg)
        ImageView courseImg;
        // 课程名
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;
        // 优惠内容
        @BindView(R.id.courseSale_txt)
        TextView courseSale_txt;
        // 价格
        @BindView(R.id.coursePrice_txt)
        TextView coursePrice_txt;

        public TypeHotContentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != clickListener) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    /**
     * 头布局
     */
    class TypeHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.ads_vp)
        ViewPager ads_vp;
        @BindView(R.id.point_layout)
        LinearLayout point_layout;
        // 推荐更多布局
        @BindView(R.id.moreLayout)
        RelativeLayout moreLayout;
        // 热门更过布局
        @BindView(R.id.hot_moreLayout)
        RelativeLayout hot_moreLayout;
        // 推荐图片1
        @BindView(R.id.recommendImg1)
        ImageView recommendImg1;
        // 推荐图片2
        @BindView(R.id.recommendImg2)
        ImageView recommendImg2;
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ads_vp.setCurrentItem(currentItem);
            }
        };

        public TypeHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.moreLayout, R.id.hot_moreLayout})
        void onclick(View view) {
            switch (view.getId()) {
                case R.id.moreLayout: // 推荐更多布局监听
                    Intent it = new Intent(mCtx, AllCourseAcitivity.class);
                    it.putExtra("orderType", 1);
                    it.putExtra("CT_ID", mProjectId);
                    it.putExtra("CT_NAME", mTitle);
                    mCtx.startActivity(it);
                    break;
                case R.id.hot_moreLayout: // 热门布局监听
                    Intent hotIt = new Intent(mCtx, AllCourseAcitivity.class);
                    hotIt.putExtra("orderType", 4);
                    hotIt.putExtra("CT_ID", mProjectId);
                    hotIt.putExtra("CT_NAME", mTitle);
                    mCtx.startActivity(hotIt);
                    break;
                default:
                    break;
            }
        }

        /**
         * 自动轮播功能
         */
        private void startPlay() {
            if (scheduledExecutorService == null) {
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 4, 4, TimeUnit.SECONDS);
            }

        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < adsList.size(); i++) {
                if (i == v.getId()) {
                    int courseType = adsList.get(i).getProductType();
                    if (5 == courseType) { // 套餐
                        Intent it = new Intent(mCtx, OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", adsList.get(i).getProductId());
                        it.putExtras(bundle);
                        mCtx.startActivity(it);
                    } else if (2 == courseType) { // 班级
                        Intent it = new Intent(mCtx, OnlineCourseDetailsAloneActivityTwo.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", adsList.get(i).getProductId());
                        it.putExtras(bundle);
                        mCtx.startActivity(it);
                    }
                    break;
                }
            }
        }

        /**
         * 执行轮播图，切换任务
         */
        private class SlideShowTask implements Runnable {

            @Override
            public void run() {
                synchronized (ads_vp) {
                    currentItem = (currentItem + 1) % adsViews.size();
                    handler.obtainMessage().sendToTarget();
                }
            }
        }
    }

    /**
     * 底部加载更多布局
     */
    class TypeFootHolder extends RecyclerView.ViewHolder {

        // 进度条
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        // 提示文字
        @BindView(R.id.loadingMore_txt)
        TextView loadingMore_txt;

        public TypeFootHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return adsViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(adsViews.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(adsViews.get(position));
            return adsViews.get(position);
        }


    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private ViewPager viewPager;
        boolean isAutoPlay = false;

        public MyPageChangeListener(ViewPager ads_vp) {
            this.viewPager = ads_vp;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    if (isAutoPlay) {
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                            viewPager.setCurrentItem(0);
                        }
                        // 当前为第一张，此时从左向右滑，则切换到最后一张
                        else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                            viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {

            currentItem = pos;

            for (int i = 0; i < imgViews.length; i++) {
                if (i == pos) {
                    imgViews[pos].setBackgroundResource(R.drawable.shape_dot_orage);
                } else {
                    imgViews[i].setBackgroundResource(R.drawable.shape_dot_normal);
                }
            }
        }

    }

    /**
     * 关闭自动播放,销毁图片占用资源
     */
    public void stopScheduledExecutorService() {

        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 改变脚布局的状态的方法,在activity根据请求数据的状态来改变这个状态
     *
     * @param state
     */
    public void changeState(int state) {
        this.footer_state = state;
        notifyDataSetChanged();
    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public int getFooter_state() {
        return footer_state;
    }
}
