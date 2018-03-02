package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.ChildCart;
import jc.cici.android.atom.bean.Gift;
import jc.cici.android.atom.bean.ShopSingleCart;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 确认订单适配器
 * Created by atom on 2017/9/13.
 */

public class EnsureOrderRecAdapter extends BaseRecycleerAdapter<ShopSingleCart, EnsureOrderRecAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ShopSingleCart> mItems;
    private ChildCartRecAdapter childAdapter;

    public EnsureOrderRecAdapter(Context context, ArrayList<ShopSingleCart> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, ShopSingleCart item, int position) {

        if (1 == item.getProductModule()) { // 班型情况
            // 考试时间
            holder.classExam_layout.setVisibility(View.VISIBLE);
            // 上课类型
            holder.classType_layout.setVisibility(View.VISIBLE);
            // 学习时间
            holder.studyTime_layout.setVisibility(View.VISIBLE);
            // 班级图片
            Glide.with(mCtx).load(mItems.get(position).getModel().getClass_MobileImage())
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(holder.courseImg);
            // 产品名称
            holder.courseName_txt.setText(ToolUtils.strReplaceAll(mItems.get(position).getModel().getClassType_Name()));
            // 产品数量
            holder.count_txt.setText("x1");
            // 课程类型
            int classType = mItems.get(position).getModel().getClassType_Type();
            switch (classType) {
                case 1: // 面授
                    holder.classAddress_layout.setVisibility(View.VISIBLE);
                    // 考试时间
                    holder.classExam_txt.setText("考试时间：" + mItems.get(position).getModel().getClassType_ExamDateName());
                    holder.courseType_txt.setText("课程类型：面授");
                    holder.courseAddress_txt.setText("上课地点：" + ToolUtils.strReplaceAll(mItems.get(position).getModel().getClassType_PlaceName()));
                    holder.studyDate_txt.setText("课程有效期：" + mItems.get(position).getModel().getClassType_Date());
                    break;
                case 2: // 在线
                    holder.classAddress_layout.setVisibility(View.GONE);
                    holder.classExam_txt.setText("考试时间：" + mItems.get(position).getModel().getClassType_ExamDateName());
                    holder.courseType_txt.setText("课程类型：在线");
                    if (1 == mItems.get(position).getModel().getClassType_Mode()) { // 固定时间
                        holder.studyDate_txt.setText("课程有效期：" + mItems.get(position).getModel().getClassType_Date());
                    } else if (2 == mItems.get(position).getModel().getClassType_Mode()) { // 售后即开
                        holder.studyDate_txt.setText("课程有效期：" + mItems.get(position).getModel().getClassType_StudyDay());
                    }
                    break;
                case 4: // 直播
                    holder.classAddress_layout.setVisibility(View.GONE);
                    holder.courseType_txt.setText("直播");
                    break;
            }
            // 单班型不包含子内容
            holder.doubleClass_layout.setVisibility(View.GONE);
            holder.doubleRecyclerView.setVisibility(View.GONE);

            // 是否含有赠品
            ArrayList<Gift> giftList = mItems.get(position).getGiftList();
            if (null != giftList && !"null".equals(giftList) && giftList.size() > 0) { // 有赠品情况
                // 显示第一行赠送名称
                holder.presentName_txt.setText("包含赠品：" + ToolUtils.strReplaceAll(mItems.get(position).getModel().getClassType_Name()) + "  (" + giftList.size() + ")");
                for (int i = 0; i < giftList.size(); i++) {
                    TextView tv = new TextView(mCtx);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setText((i + 1) + "：" + ToolUtils.strReplaceAll(giftList.get(i).getName()));
                    tv.setTextSize(12);
                    tv.setPadding(30, 20, 30, 0);
                    tv.setTextColor(Color.parseColor("#333333"));
                    holder.presentCon_layout.addView(tv);
                }
                holder.lineImg.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
                holder.present_layout.setVisibility(View.VISIBLE);
                holder.presentCon_layout.setVisibility(View.GONE);
                holder.llImg.setVisibility(View.VISIBLE);
                // 展开收缩监听
                holder.present_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.presentCon_layout.getVisibility() == View.GONE) { // 展开布局隐藏
                            holder.expand_Img.setBackgroundResource(R.drawable.icon_down);
                            holder.presentCon_layout.setVisibility(View.VISIBLE);
                        } else {
                            holder.expand_Img.setBackgroundResource(R.drawable.icon_up);
                            holder.presentCon_layout.setVisibility(View.GONE);
                        }
                    }
                });
            } else { // 没有赠品情况
                holder.lineImg.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.GONE);
                holder.present_layout.setVisibility(View.GONE);
                holder.presentCon_layout.setVisibility(View.GONE);
                holder.llImg.setVisibility(View.GONE);
            }

            // 售价
            holder.price_txt.setText("￥" + mItems.get(position).getModel().getClassType_SalePrice());
            // 原价
            holder.origPrice_txt.setText("￥" + mItems.get(position).getModel().getClassType_Price());
            holder.origPrice_txt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (5 == item.getProductModule()) { // 套餐情况

            // 考试时间
            holder.classExam_layout.setVisibility(View.GONE);
            // 上课类型
            holder.classType_layout.setVisibility(View.GONE);
            // 授课地址布局
            holder.classAddress_layout.setVisibility(View.GONE);
            // 学习时间
            holder.studyTime_layout.setVisibility(View.GONE);
            // 班级图片
            Glide.with(mCtx).load(mItems.get(position).getModel().getPackage().getPackage_MobileImage())
                    .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                    .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                    .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                    .override(280, 186) // 设置最终显示图片大小
                    .centerCrop() // 中心剪裁
                    .skipMemoryCache(true) // 跳过缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                    .into(holder.courseImg);
            // 产品名称
            holder.courseName_txt.setText(ToolUtils.strReplaceAll(mItems.get(position).getModel().getPackage().getPackage_Name()));
            // 产品数量
            holder.count_txt.setText("x1");
            //        // 产品支付价
            holder.price_txt.setText("￥" + mItems.get(position).getModel().getSalePrice());
            // 产品原价
            holder.origPrice_txt.setText("￥" + mItems.get(position).getModel().getPrice());
            holder.origPrice_txt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            // 获取套餐下子班型
            ArrayList<ChildCart> childList = mItems.get(position).getModel().getList();
            if (null != childList && !"null".equals(childList) && childList.size() > 0) { // 有套餐子班型数据

                holder.doubleRecyclerView.setLayoutManager(new LinearLayoutManager(mCtx) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                childAdapter = new ChildCartRecAdapter(mCtx, childList);
                holder.doubleRecyclerView.setAdapter(childAdapter);
                holder.doubleRecyclerView.setNestedScrollingEnabled(false);
                holder.doubleClass_layout.setVisibility(View.VISIBLE);
                holder.doubleRecyclerView.setVisibility(View.VISIBLE);
            } else { // 没有套餐子班型数据
                holder.doubleClass_layout.setVisibility(View.GONE);
                holder.doubleRecyclerView.setVisibility(View.GONE);
            }
            // 是否含有赠品
            ArrayList<Gift> giftList = mItems.get(position).getModel().getGiftList();
            if (null != giftList && !"null".equals(giftList) && giftList.size() > 0) { // 有赠品情况
                // 显示第一行赠送名称
                holder.presentName_txt.setText("包含赠品：" + ToolUtils.strReplaceAll(mItems.get(position).getModel().getPackage().getPackage_Name()) + "  (" + giftList.size() + ")");
                for (int i = 0; i < giftList.size(); i++) {
                    TextView tv = new TextView(mCtx);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setText((i + 1) + "：" + ToolUtils.strReplaceAll(giftList.get(i).getName()));
                    tv.setTextSize(12);
                    tv.setPadding(30, 20, 30, 0);
                    tv.setTextColor(Color.parseColor("#333333"));
                    holder.presentCon_layout.addView(tv);
                }
                holder.line.setVisibility(View.VISIBLE);
                holder.present_layout.setVisibility(View.VISIBLE);
                holder.presentCon_layout.setVisibility(View.GONE);
                holder.llImg.setVisibility(View.VISIBLE);
                // 展开收缩监听
                holder.present_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.presentCon_layout.getVisibility() == View.GONE) { // 展开布局隐藏
                            holder.expand_Img.setBackgroundResource(R.drawable.icon_down);
                            holder.presentCon_layout.setVisibility(View.VISIBLE);
                        } else {
                            holder.expand_Img.setBackgroundResource(R.drawable.icon_up);
                            holder.presentCon_layout.setVisibility(View.GONE);
                        }
                    }
                });
            } else { // 没有赠品情况
                holder.line.setVisibility(View.GONE);
                holder.present_layout.setVisibility(View.GONE);
                holder.presentCon_layout.setVisibility(View.GONE);
                holder.llImg.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ensureorder_view;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 课程图片
        @BindView(R.id.courseImg)
        ImageView courseImg;
        // 班型名称
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;
        // 班型数量
        @BindView(R.id.count_txt)
        TextView count_txt;
        // 考试时间布局
        @BindView(R.id.classExam_layout)
        RelativeLayout classExam_layout;
        // 考试时间
        @BindView(R.id.classExam_txt)
        TextView classExam_txt;
        // 上课类型
        @BindView(R.id.classType_layout)
        RelativeLayout classType_layout;
        // 上课类型文字
        @BindView(R.id.courseType_txt)
        TextView courseType_txt;
        // 授课地址布局
        @BindView(R.id.classAddress_layout)
        RelativeLayout classAddress_layout;
        // 上课地址
        @BindView(R.id.courseAddress_txt)
        TextView courseAddress_txt;
        // 学习时间布局
        @BindView(R.id.studyTime_layout)
        RelativeLayout studyTime_layout;
        // 学习时间
        @BindView(R.id.studyDate_txt)
        TextView studyDate_txt;
        // 价格布局
        @BindView(R.id.price_layout)
        RelativeLayout price_layout;
        // 售价
        @BindView(R.id.price_txt)
        TextView price_txt;
        // 原价
        @BindView(R.id.origPrice_txt)
        TextView origPrice_txt;
        // 套餐班类型
        @BindView(R.id.doubleClass_layout)
        RelativeLayout doubleClass_layout;
        // 套餐班子类型列表
        @BindView(R.id.doubleRecyclerView)
        RecyclerView doubleRecyclerView;
        // 赠送收缩布局
        @BindView(R.id.present_layout)
        RelativeLayout present_layout;
        // 赠送文字
        @BindView(R.id.presentName_txt)
        TextView presentName_txt;
        // 收缩图片
        @BindView(R.id.expand_Img)
        ImageView expand_Img;
        // 动态赠送内容布局
        @BindView(R.id.presentCon_layout)
        LinearLayout presentCon_layout;
        // 动态下划线
        @BindView(R.id.llImg)
        ImageView llImg;
        // 虚线
        @BindView(R.id.line)
        ImageView line;
        // 实线
        @BindView(R.id.lineImg)
        ImageView lineImg;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
