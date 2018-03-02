package cn.jun.indexmain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.jun.bean.CustomBean;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import cn.jun.indexmain.ImageSlideshow.ImageSlideshow;
import cn.jun.indexmain.base.MultiTypeBaseAdapter;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import jc.cici.android.R;
import jc.cici.android.atom.ui.courselist.AllCourseAcitivity;

import static cn.gfedu.gfeduapp.DiscoverFragment.pager;
import static cn.gfedu.gfeduapp.DiscoverFragment.projectListBean;
import static cn.gfedu.gfeduapp.GfeduApplication.application;
import static cn.jun.indexmain.NewsFragment.AdsList;
import static cn.jun.indexmain.NewsFragment.ForUcustomList;


public class LoadMoreAdapter_NoAll extends MultiTypeBaseAdapter<CustomBean> {
    private String[] titles;
    // 图片资源
//    private int[] mImages = {R.mipmap.demobg_1, R.mipmap.demobg_2, R.mipmap.demobg_1, R.mipmap.demobg_2};
    private List<ImageView> mList;
    //    Handler mHandler = new Handler();
    private Context context;
    private LayoutInflater inflater;
    private boolean setdotView = true;//设置圆点标识符
    //顶部轮播图片
    private ImageSlideshow imageSlideshow;

    public LoadMoreAdapter_NoAll(Context context, List<CustomBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    protected void setHeaderView(CommonViewHolder holder) {
        /**顶部广告位**/
        initViewPager(holder);
        setdotView = false;
        /**更多监听**/
        initMoreClick(holder);
    }

    private void initMoreClick(CommonViewHolder holder) {
        TextView MoreTv = (TextView) holder.getView(R.id.gengduo_tv);
        MoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("热门班级==>", "点击更多");

                int CT_ID = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_ID();
                String CT_NAME = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_Name();
                Log.i("热门传递的参数 --- ", "" + CT_ID);
                Log.i("热门传递的参数 --- ", "" + CT_NAME);

                Intent mPager = new Intent(context, AllCourseAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CT_ID", CT_ID);
                bundle.putString("CT_NAME", CT_NAME);
                mPager.putExtras(bundle);
                mContext.startActivity(mPager);

            }
        });

        ImageView forU_im1 = (ImageView) holder.getView(R.id.forU_im1);
        ImageView forU_im2 = (ImageView) holder.getView(R.id.forU_im2);
        String product_mobileImage = "";
        String product_mobileImage2 = "";
        if (!"".equals(ForUcustomList) && null != ForUcustomList) {
            if (ForUcustomList.size() < 2) {
                product_mobileImage = ForUcustomList.get(0).Product_MobileImage;
//                product_mobileImage2 = ForUcustomList.get(1).Product_MobileImage;
            } else {
                product_mobileImage = ForUcustomList.get(0).Product_MobileImage;
                product_mobileImage2 = ForUcustomList.get(1).Product_MobileImage;
                forU_im1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2-班级 5-套餐
                        int type = ForUcustomList.get(0).Type;
                        int Product_PKID = ForUcustomList.get(0).Product_PKID;
                        if (2 == type) {
                            Intent intent = new Intent(context, OnlineCourseDetailsAloneActivityTwo.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", Product_PKID);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else if (5 == type) {
                            Intent intent = new Intent(context, OnlineCourseDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", Product_PKID);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
                forU_im2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2-班级 5-套餐
                        int type = ForUcustomList.get(0).Type;
                        int Product_PKID = ForUcustomList.get(0).Product_PKID;
                        if (2 == type) {
                            Intent intent = new Intent(context, OnlineCourseDetailsAloneActivityTwo.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", Product_PKID);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else if (5 == type) {
                            Intent intent = new Intent(context, OnlineCourseDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Product_PKID", Product_PKID);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
        Glide.with(context)
                .load(product_mobileImage)
                .placeholder(R.drawable.pic_kong_xiangqing)
                .into(forU_im1);
        Glide.with(context)
                .load(product_mobileImage2)
                .placeholder(R.drawable.pic_kong_xiangqing)
                .into(forU_im2);


        TextView FouUMoreTv = (TextView) holder.getView(R.id.forU_more);
        FouUMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("为你推荐==>", "点击更多");

                int CT_ID = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_ID();
                String CT_NAME = projectListBean.getBody().getProjectList().get(pager.getCurrentItem()).getCT_Name();
                Log.i("为你推荐传递的参数 --- ", "" + CT_ID);
                Log.i("为你推荐传递的参数 --- ", "" + CT_NAME);

                Intent mPager = new Intent(context, AllCourseAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CT_ID", CT_ID);
                bundle.putString("CT_NAME", CT_NAME);
                mPager.putExtras(bundle);
                mContext.startActivity(mPager);
            }
        });
    }

    private void initViewPager(CommonViewHolder holder) {
        imageSlideshow = (ImageSlideshow) holder.getView(R.id.is_gallery);
        // 为ImageSlideshow设置数据
        imageSlideshow.setDotSpace(12);
        imageSlideshow.setDotSize(12);
        imageSlideshow.setDelay(3000);
        if (setdotView) {
            if(!"".equals(AdsList.get(0).getBody().getList()) && null!= AdsList.get(0).getBody().getList()){
                for (int i = 0; i < AdsList.get(0).getBody().getList().size(); i++) {
                    imageSlideshow.addImageUrl(AdsList.get(0).getBody().getList().get(i).getImgUrl());
                }
            }
            imageSlideshow.commit();
            setdotView = false;
        }
        imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("OnItemClick下标 === > ", "" + position);
                //2-班级 5-套餐
                int ProductType = AdsList.get(0).getBody().getList().get(position).getProductType();
                int ProductId = AdsList.get(0).getBody().getList().get(position).getProductId();
                if (2 == ProductType) {
                    Intent intent = new Intent(context, OnlineCourseDetailsAloneActivityTwo.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Product_PKID", ProductId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (5 == ProductType) {
                    Intent intent = new Intent(context, OnlineCourseDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Product_PKID", ProductId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void convert(CommonViewHolder holder, final CustomBean data, int viewType) {
        if (viewType == 0) {//设置头部
            setHeaderView(holder);
        } else {
            String IntroName = data.Product_IntroName.replace("&nbsp;", "");
            String Intro = data.Product_Intro.replace("&nbsp;", "");
            String htmlText = "<font color='#DD5555'>" + IntroName
                    + "</font>" + "  " + "<font color='#666666'>" + Intro + "</font>";
            holder.setText(R.id.text1, data.Product_Name.replace("&nbsp;", ""));
            holder.setText(R.id.red_tv, htmlText);
            holder.setText(R.id.price_saleregion, data.Product_PriceSaleRegion.replace("&nbsp;", ""));
            //免费试听
            Button mfst_tv = (Button) holder.getView(R.id.mfst_tv);
            mfst_tv.getBackground().setAlpha(60);
            int Product_OutlineFreeState = data.Product_OutlineFreeState;
            if(1==Product_OutlineFreeState){
                mfst_tv.setVisibility(View.VISIBLE);
            }else if(0==Product_OutlineFreeState){
                mfst_tv.setVisibility(View.GONE);
            }
            //设置图片
            ImageView list_im = (ImageView) holder.getView(R.id.list_im);
            String im_url = data.Product_MobileImage;

            Glide.with(application.getApplicationContext())
                    .load(im_url)
                    .placeholder(R.drawable.pic_kong_xiangqing)
                    .into(list_im);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.recycleview_header_noall;
        }
        return R.layout.recycleview_item;
    }

    @Override
    protected int getViewType(int position, CustomBean data) {
        if (data == null) {
            return 0;
        } else {
            return 1;
        }


    }

}
