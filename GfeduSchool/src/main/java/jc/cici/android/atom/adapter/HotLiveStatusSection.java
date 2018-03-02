package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.StatusSection;
import jc.cici.android.atom.bean.LiveContent;
import jc.cici.android.atom.ui.live.LiveListActivity;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 热门直播模块
 * Created by atom on 2017/11/13.
 */

public class HotLiveStatusSection extends StatusSection {

    private Context mCtx;
    private ArrayList<LiveContent.HotContent> mHotLiveList;
    // 当前项目id
    private int mProjectId;
    // 当前项目名称
    private String mTitle;

    public HotLiveStatusSection(Context context, ArrayList<LiveContent.HotContent> hotLive,int projectId,String title) {
        super(R.layout.item_title_live, R.layout.item_section_content_live);
        this.mCtx = context;
        this.mHotLiveList = hotLive;
        this.mProjectId = projectId;
        this.mTitle = title;
    }

    @Override
    public int getContentItemsTotal() {
        return null != mHotLiveList ? mHotLiveList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        LiveContent.HotContent hotContent = mHotLiveList.get(position);
        // 直播课程背景图片
        Glide.with(mCtx).load(hotContent.getClass_MobileImage())
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(itemViewHolder.product_course_img);
        //  判断是否含有公开课
        String salePrice = hotContent.getClass_PriceSaleRegion();
        if ("￥0".equals(salePrice)) { // 公开课情况
            itemViewHolder.pubic_flag_Img.setVisibility(View.VISIBLE);
        } else { // 非公开课情况
            itemViewHolder.pubic_flag_Img.setVisibility(View.GONE);
        }
        // 直播课程时间
        itemViewHolder.courseTime_txt.setText(
                hotContent.getCS_DateShort() + " "
                        + hotContent.getCS_StartTime() + "-"
                        + hotContent.getCS_EndTime());
        // 直播课程标题
        itemViewHolder.courseName_txt.setText(ToolUtils.strReplaceAll(hotContent.getClass_Name()));

        // 图片监听
        itemViewHolder.product_course_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取班级id
                int classPkID = mHotLiveList.get(position).getClass_PKID();
                // 获取系列直播or普通直播
                int classFrom = mHotLiveList.get(position).getClass_Form();
                switch (classFrom) {
                    case 1: // 系列直播
//                        Intent intent = new Intent(mCtx, OnlineCourseDetailsActivity.class);
                        Intent intent = new Intent(mCtx, LiveClassXiLieActivity.class);
                        Bundle bundle = new Bundle();
//                        bundle.putInt("Product_PKID", classPkID);
                        bundle.putInt("Class_PKID", classPkID);
                        intent.putExtras(bundle);
                        mCtx.startActivity(intent);
                        break;
                    case 2: // 正常直播
//                        Intent normalIt = new Intent(mCtx, OnlineCourseDetailsActivity.class);
                        Intent normalIt = new Intent(mCtx, LiveClassActivity.class);
                        Bundle norBundle = new Bundle();
//                        norBundle.putInt("Product_PKID", classPkID);
                        norBundle.putInt("Class_PKID", classPkID);
                        normalIt.putExtras(norBundle);
                        mCtx.startActivity(normalIt);

                        break;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.lineImg.setBackgroundResource(R.drawable.icon_hot);
        headerHolder.headerName_txt.setText("热门直播");
        headerHolder.moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, LiveListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("projectId", mProjectId);
                bundle.putString("title",mTitle);
                bundle.putInt("searchType", 0);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        // 最近直播图标
        @BindView(R.id.lineImg)
        ImageView lineImg;
        // 最近直播文字
        @BindView(R.id.headerName_txt)
        TextView headerName_txt;
        // 更多布局
        @BindView(R.id.moreLayout)
        RelativeLayout moreLayout;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        // 课程背景图
        @BindView(R.id.product_course_img)
        ImageView product_course_img;
        // 公开课标记
        @BindView(R.id.pubic_flag_Img)
        Button pubic_flag_Img;
        // 课程时间
        @BindView(R.id.courseTime_txt)
        TextView courseTime_txt;
        // 课程名称
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
