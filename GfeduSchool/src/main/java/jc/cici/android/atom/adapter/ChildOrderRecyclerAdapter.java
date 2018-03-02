package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseAdapter;
import jc.cici.android.atom.bean.ChildOrder;
import jc.cici.android.atom.bean.GiftBeam;
import jc.cici.android.atom.bean.StudyBean;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 班型内容适配器
 * Created by atom on 2017/8/31.
 */

public class ChildOrderRecyclerAdapter extends RecyclerView.Adapter<ChildOrderRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ChildOrder> mItems;
    // 订单状态
    private int mOrderStatus;
    // 创建监听器对象
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ChildOrderRecyclerAdapter(Context context, ArrayList<ChildOrder> items, int orderStatus) {
        this.mCtx = context;
        this.mItems = items;
        this.mOrderStatus = orderStatus;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_order_singleview, null, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        // 班级图片
        Glide.with(mCtx).load(mItems.get(position).getImage())
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(holder.courseImg);
        //转退延设置
        if (-1 != mOrderStatus && 0 != mOrderStatus) { // 已取消和未支付情况，不显示开班情况
            if(!"".equals(mItems.get(position).getOpearteDesc())){
                holder.orderFrom_layout.setVisibility(View.VISIBLE);
                holder.orderFrom_txt.setText(mItems.get(position).getOpearteDesc());
            }else{
                holder.orderFrom_layout.setVisibility(View.GONE);
                holder.orderFrom_txt.setText("");
            }

//            int status = mItems.get(position).getOrderProduct_State();
//            switch (status) {
//                case -1: // 已关闭
//                    holder.orderFrom_layout.setVisibility(View.VISIBLE);
//                    holder.orderFrom_txt.setText("已关闭");
//                    break;
//                case 0: // 未开班
//                    holder.orderFrom_layout.setVisibility(View.VISIBLE);
//                    holder.orderFrom_txt.setText("未开班");
//                    break;
//                case 1: // 已锁定
//                    holder.orderFrom_layout.setVisibility(View.VISIBLE);
//                    holder.orderFrom_txt.setText("已锁定");
//                    break;
//                case 2: // 已开班
//                    holder.orderFrom_layout.setVisibility(View.VISIBLE);
//                    holder.orderFrom_txt.setText("已开班");
//                    break;
//                default: // 默认情况
//                    holder.orderFrom_layout.setVisibility(View.GONE);
//                    holder.orderFrom_txt.setText("");
//                    break;
//            }
        }
        // 产品名称
        holder.courseName_txt.setText(ToolUtils.strReplaceAll(mItems.get(position).getProduct_Name()));
        // 产品数量
        holder.count_txt.setText("x " + mItems.get(position).getProduct_Count() + "");
        // 课程类型
        int classType = mItems.get(position).getClassType().getClassType_Type();
        switch (classType) {
            case 1: // 面授
                holder.classAddress_layout.setVisibility(View.VISIBLE);
                // 考试时间
                holder.courseTime_txt.setText("考试时间：" + ToolUtils.strReplaceAll(mItems.get(position).getClassType().getExamDate()));
                holder.courseType_txt.setText("课程类型：面授");
                holder.courseAddress_txt.setText("上课地点：" + ToolUtils.strReplaceAll(mItems.get(position).getClassType().getStudyPlace()));
                holder.studyDate_txt.setText("课程有效期：" + ToolUtils.strReplaceAll(mItems.get(position).getClassType().getExpireDate()));
                break;
            case 2: // 在线
                holder.classAddress_layout.setVisibility(View.GONE);
                holder.courseTime_txt.setText("考试时间：" + ToolUtils.strReplaceAll(mItems.get(position).getClassType().getExamDate()));
                holder.courseType_txt.setText("课程类型：在线");
                if (1 == mItems.get(position).getClassType().getClassType_Mode()) { // 固定时间
                    holder.studyDate_txt.setText("课程有效期：" + ToolUtils.strReplaceAll(mItems.get(position).getClassType().getExpireDate()));
                } else if (2 == mItems.get(position).getClassType().getClassType_Mode()) { // 售后即开
                    holder.studyDate_txt.setText("课程有效期：" + mItems.get(position).getClassType().getClassType_StudyDay());
                }
                break;
            case 4: // 直播
                holder.classAddress_layout.setVisibility(View.GONE);
                holder.courseType_txt.setText("课程类型：直播");
                break;
        }
        // 是否含有赠品
        ArrayList<GiftBeam> giftList = mItems.get(position).getGiftList();
        if (null != giftList && !"null".equals(giftList) && giftList.size() > 0) { // 有赠品情况
            // 显示第一行赠送名称
            holder.presentName_txt.setText("包含赠品：" + ToolUtils.strReplaceAll(mItems.get(position).getProduct_Name()) + "  (" + giftList.size() + ")");
            for (int i = 0; i < giftList.size(); i++) {
                TextView tv = new TextView(mCtx);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setText((i + 1) + "：" + ToolUtils.strReplaceAll(giftList.get(i).getProduct_Name()));
                tv.setTextSize(12);
                tv.setPadding(35, 20, 30, 0);
                tv.setTextColor(Color.parseColor("#333333"));
                holder.presentCon_layout.addView(tv);
            }
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
            holder.present_layout.setVisibility(View.GONE);
            holder.presentCon_layout.setVisibility(View.GONE);
            holder.llImg.setVisibility(View.GONE);
        }

        // 判断当前列表是否含有未支付情况
        if (0 == mOrderStatus) { // 含有未支付情况
            // 售价
            holder.studyPrice_txt.setText("￥" + String.valueOf(mItems.get(position).getProduct_Money()));
            // 原价
            holder.studyOrigPrice_txt.setText("￥" + String.valueOf(mItems.get(position).getProduct_OriginalPrice()));
            holder.studyOrigPrice_txt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.studyPrice_layout.setVisibility(View.VISIBLE);
        } else { // 不含有未支付情况
            // 售价
            holder.studyPrice_txt.setText("");
            // 原价
            holder.studyOrigPrice_txt.setText("");
            holder.studyPrice_layout.setVisibility(View.GONE);
        }

        /**
         * item 设置监听
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        // 课程图片
        @BindView(R.id.courseImg)
        ImageView courseImg;
        // 转退延布局
        @BindView(R.id.orderFrom_layout)
        RelativeLayout orderFrom_layout;
        // 转退延文字
        @BindView(R.id.orderFrom_txt)
        TextView orderFrom_txt;
        // 班型名称
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;
        // 班型数量
        @BindView(R.id.count_txt)
        TextView count_txt;
        // 上课时间
        @BindView(R.id.courseTime_txt)
        TextView courseTime_txt;
        // 课程类型
        @BindView(R.id.courseType_txt)
        TextView courseType_txt;
        // 授课地址布局
        @BindView(R.id.classAddress_layout)
        RelativeLayout classAddress_layout;
        // 上课地址
        @BindView(R.id.courseAddress_txt)
        TextView courseAddress_txt;
        // 上课日期
        @BindView(R.id.studyDate_txt)
        TextView studyDate_txt;
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
        // 布局下划线
        @BindView(R.id.llImg)
        ImageView llImg;
        // 未支付显示价格布局
        @BindView(R.id.studyPrice_layout)
        RelativeLayout studyPrice_layout;
        // 未支付显示售价
        @BindView(R.id.studyPrice_txt)
        TextView studyPrice_txt;
        // 未支付显示原价
        @BindView(R.id.studyOrigPrice_txt)
        TextView studyOrigPrice_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}