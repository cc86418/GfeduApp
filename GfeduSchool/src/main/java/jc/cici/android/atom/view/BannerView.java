package jc.cici.android.atom.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.BannerAdapter;
import jc.cici.android.atom.bean.LiveAds;
import jc.cici.android.atom.utils.ToolUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 直播banner
 * Created by atom on 2017/11/14.
 */

public class BannerView extends RelativeLayout implements BannerAdapter.ViewPagerOnItemClickListener {
    @BindView(R.id.layout_banner_viewpager)
    ViewPager viewPager;
    @BindView(R.id.layout_banner_points_group)
    LinearLayout points;
    // 直播时间
    @BindView(R.id.freeTime_txt)
    TextView freeTime_txt;
    //  直播标题
    @BindView(R.id.freeCourseName_txt)
    TextView freeCourseName_txt;
    // 免费提示
    @BindView(R.id.courseName_txt)
    TextView courseName_txt;
    private CompositeSubscription compositeSubscription;
    //默认轮播时间，10s
    private int delayTime = 10;
    private List<ImageView> imageViewList;
    private List<LiveAds> bannerList;
    //选中显示Indicator
    private int selectRes = R.drawable.dot_selected;
    //非选中显示Indicator
    private int unSelcetRes = R.drawable.dot_unselected;
    //当前页的下标
    private int currentPos;
    private Animator animatorToLarge;
    private Animator animatorToSmall;
    private Context context;
    private SparseBooleanArray isLarge;


    public BannerView(Context context) {
        this(context, null);
    }


    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_banner, this, true);
        ButterKnife.bind(this);
        initAnimator();
        imageViewList = new ArrayList<>();
    }

    private void initAnimator() {
        animatorToLarge = AnimatorInflater.loadAnimator(context, R.animator.scale_to_large);
        animatorToSmall = AnimatorInflater.loadAnimator(context, R.animator.scale_to_small);
    }

    /**
     * 设置轮播间隔时间
     *
     * @param time 轮播间隔时间，单位秒
     */
    public BannerView delayTime(int time) {
        this.delayTime = time;
        return this;
    }


    /**
     * 设置Points资源 Res
     *
     * @param selectRes   选中状态
     * @param unselcetRes 非选中状态
     */
    public void setPointsRes(int selectRes, int unselcetRes) {
        this.selectRes = selectRes;
        this.unSelcetRes = unselcetRes;
    }


    /**
     * 图片轮播需要传入参数
     */
    public void build(List<LiveAds> list) {
        isLarge = new SparseBooleanArray();
        destroy();
        if (list.size() == 0) {
            this.setVisibility(GONE);
            return;
        }
        bannerList = new ArrayList<>();
        bannerList.addAll(list);
        final int pointSize;
        pointSize = bannerList.size();
        if (pointSize == 2) {
            bannerList.addAll(list);
        }
        //判断是否清空 指示器点
        if (points.getChildCount() != 0) {
            points.removeAllViewsInLayout();
        }
        //初始化与个数相同的指示器点
        for (int i = 0; i < pointSize; i++) {
            View dot = new View(getContext());
            dot.setBackgroundResource(unSelcetRes);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ToolUtils.dip2px(getContext(), 6),
                    ToolUtils.dip2px(getContext(), 6));
            params.leftMargin = 10;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            points.addView(dot);
            isLarge.put(i, false);
        }
        points.getChildAt(0).setBackgroundResource(selectRes);
        animatorToLarge.setTarget(points.getChildAt(0));
        animatorToLarge.start();
        isLarge.put(0, true);
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView mImageView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(bannerList.get(i).getImgUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.item_studyhome_img)
                    .dontAnimate()
                    .into(mImageView);
            imageViewList.add(mImageView);
            // 直播时间
            freeTime_txt.setText(bannerList.get(i).getDate() + " "
                    + bannerList.get(i).getBeginTime() + "-"
                    + bannerList.get(i).getEndTime());
            // 直播标题
            freeCourseName_txt.setText(bannerList.get(i).getTitle());
        }
        //监听图片轮播，改变指示器状态
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                pos = pos % pointSize;
                currentPos = pos;
                for (int i = 0; i < points.getChildCount(); i++) {
                    points.getChildAt(i).setBackgroundResource(unSelcetRes);
                    if (isLarge.get(i)) {
                        animatorToSmall.setTarget(points.getChildAt(i));
                        animatorToSmall.start();
                        isLarge.put(i, false);
                    }
                }
                points.getChildAt(pos).setBackgroundResource(selectRes);
                if (!isLarge.get(pos)) {
                    animatorToLarge.setTarget(points.getChildAt(pos));
                    animatorToLarge.start();
                    isLarge.put(pos, true);
                }

                // 直播时间
                freeTime_txt.setText(bannerList.get(currentPos).getDate() + " "
                        + bannerList.get(currentPos).getBeginTime() + "-"
                        + bannerList.get(currentPos).getEndTime());
                // 直播标题
                freeCourseName_txt.setText(bannerList.get(currentPos).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (isStopScroll) {
                            startScroll();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stopScroll();
                        compositeSubscription.unsubscribe();
                        break;
                }
            }
        });
        BannerAdapter bannerAdapter = new BannerAdapter(imageViewList);
        viewPager.setAdapter(bannerAdapter);
        bannerAdapter.notifyDataSetChanged();
        bannerAdapter.setmViewPagerOnItemClickListener(this);
        //图片开始轮播
        startScroll();
    }

    private boolean isStopScroll = false;


    /**
     * 图片开始轮播
     */
    private void startScroll() {
        compositeSubscription = new CompositeSubscription();
        isStopScroll = false;
        Subscription subscription = Observable.timer(delayTime, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (isStopScroll) {
                            return;
                        }
                        isStopScroll = true;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        // 直播时间
                        freeTime_txt.setText(bannerList.get(viewPager.getCurrentItem() + 1).getDate() + " "
                                + bannerList.get(viewPager.getCurrentItem() + 1).getBeginTime() + "-"
                                + bannerList.get(viewPager.getCurrentItem() + 1).getEndTime());
                        // 直播标题
                        freeCourseName_txt.setText(bannerList.get(viewPager.getCurrentItem() + 1).getTitle());
                    }
                });
        compositeSubscription.add(subscription);
    }


    /**
     * 图片停止轮播
     */
    private void stopScroll() {
        isStopScroll = true;
    }


    public void destroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }


    /**
     * 设置ViewPager的Item点击回调事件
     */
    @Override
    public void onItemClick() {
        // 产品类型
        int productFrom = bannerList.get(currentPos).getClass_Form();
        int productID = bannerList.get(currentPos).getProductId();
        if (2 == productFrom) { // 单课程
//            Intent intent = new Intent(this.getContext(), OnlineCourseDetailsAloneActivityTwo.class);
            Intent intent = new Intent(this.getContext(), LiveClassActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Class_PKID", productID);
//            bundle.putInt("Product_PKID", productID);
            intent.putExtras(bundle);
            this.getContext().startActivity(intent);
        } else if (1 == productFrom) { // 系列课程
//            Intent intent = new Intent(this.getContext(), OnlineCourseDetailsActivity.class);
            Intent intent = new Intent(this.getContext(), LiveClassXiLieActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putInt("Product_PKID", productID);
            bundle.putInt("Class_PKID", productID);
            intent.putExtras(bundle);
            this.getContext().startActivity(intent);
        }
    }
}
