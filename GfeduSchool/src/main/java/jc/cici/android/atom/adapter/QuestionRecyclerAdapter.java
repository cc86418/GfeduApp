package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.ui.note.GalleryActivity;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;

/**
 * 问题适配器
 * Created by atom on 2017/6/12.
 */

public class QuestionRecyclerAdapter extends BaseRecycleerAdapter<Question, QuestionRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<Question> mItems;
    private int mStatus;
    private ArrayList<String> imgList;

    public QuestionRecyclerAdapter(Context context, ArrayList items, int status) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mStatus = status;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_questionfragment_view;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Question item, int position) {

        switch (mStatus) {
            case 1: // 我的问题
                holder.dateTxt.setText(item.getQuesAddTime());
                holder.dateTxt.setVisibility(View.VISIBLE);
                holder.myShotScreen_layout.setVisibility(View.VISIBLE);
                holder.shotScreen_layout.setVisibility(View.GONE);
                break;
            case 2: // 大家的问题
                Glide.with(mCtx).load(item.getHeadImg())
                        .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                        .error(R.drawable.icon_avatar) //加载失败时显示的图片
                        .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                        .override(80, 80) // 设置最终显示图片大小
                        .centerCrop() // 中心剪裁
                        .skipMemoryCache(true) // 跳过缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                        .transform(new GlideCircleTransform(mCtx)) // 设置圆角
                        .into(holder.userImg_ques);
                holder.userName_ques.setText(ToolUtils.strReplaceAll(item.getNickName()));
                holder.ourQuesDate_txt.setText(item.getQuesAddTime());
                holder.userImg_ques.setVisibility(View.VISIBLE);
                holder.userName_ques.setVisibility(View.VISIBLE);
                holder.ourQuesDate_txt.setVisibility(View.VISIBLE);
                holder.ourQues_layout.setVisibility(View.VISIBLE);
                holder.dateTxt.setVisibility(View.GONE);
                holder.myShotScreen_layout.setVisibility(View.GONE);
                holder.shotScreen_layout.setVisibility(View.VISIBLE);
                break;
        }
        // 判断问题是否删除( 1.是    2.否)
        if (1 == item.getIsDelete()) {  // 问题删除
            holder.ques_txt.setText("该问题已被管理员删除,删除理由:" + item.getDeleteDetail());
            holder.ques_txt.setTextColor(Color.parseColor("#c8c8c8"));
            holder.relationAndStatus_layout.setVisibility(View.GONE);
            holder.divLine.setVisibility(View.GONE);
        } else {
            holder.ques_txt.setText(item.getQuesContent());
            holder.ques_txt.setTextColor(Color.parseColor("#333333"));
            holder.relationAndStatus_layout.setVisibility(View.VISIBLE);
            holder.divLine.setVisibility(View.VISIBLE);
            // 获取上传图片
            imgList = item.getQuesImageUrl();
            if (null != imgList && imgList.size() > 0) {
                // 获取图片个数
                int size = imgList.size();
                if (1 == mStatus) { // 我的问题
                    switch (size) {
                        case 1: // 一张图片
                            // 加载显示第一张图片
                            loadImg(holder.myShotScreen1_img, imgList.get(0));
                            setImageVisible(holder.myShotScreen1_img, true);
                            setImageVisible(holder.myShotScreen2_img, false);
                            setImageVisible(holder.myShotScreen3_img, false);
                            // 第一张图片设置监听
                            setOnclick(holder.myShotScreen1_img, imgList, 0);
                            break;
                        case 2: // 两张图片
                            // 加载显示第一张图片
                            loadImg(holder.myShotScreen1_img, imgList.get(0));
                            // 加载显示第二张图片
                            loadImg(holder.myShotScreen2_img, imgList.get(1));

                            setImageVisible(holder.myShotScreen1_img, true);
                            setImageVisible(holder.myShotScreen2_img, true);
                            setImageVisible(holder.myShotScreen3_img, false);

                            // 第一张图片设置监听
                            setOnclick(holder.myShotScreen1_img, imgList, 0);
                            // 第二张图片设置监听
                            setOnclick(holder.myShotScreen2_img, imgList, 1);
                            break;
                        case 3: // 三张图片
                            // 加载显示第一张图片
                            loadImg(holder.myShotScreen1_img, imgList.get(0));
                            // 加载显示第二张图片
                            loadImg(holder.myShotScreen2_img, imgList.get(1));
                            // 加载显示第三张图片
                            loadImg(holder.myShotScreen3_img, imgList.get(2));
                            setImageVisible(holder.myShotScreen1_img, true);
                            setImageVisible(holder.myShotScreen2_img, true);
                            setImageVisible(holder.myShotScreen3_img, true);

                            // 第一张图片设置监听
                            setOnclick(holder.myShotScreen1_img, imgList, 0);
                            // 第二张图片设置监听
                            setOnclick(holder.myShotScreen2_img, imgList, 1);
                            // 第三张图片设置监听
                            setOnclick(holder.myShotScreen3_img, imgList, 2);
                            break;
                        default:
                            break;
                    }
                } else if (2 == mStatus) { // 大家的问题
                    switch (size) {
                        case 1: // 一张图片
                            // 加载显示第一张图片
                            loadImg(holder.shotScreen1_img, imgList.get(0));
                            setImageVisible(holder.shotScreen1_img, true);
                            setImageVisible(holder.shotScreen2_img, false);
                            setImageVisible(holder.shotScreen3_img, false);
                            // 第一张图片设置监听
                            setOnclick(holder.shotScreen1_img, imgList, 0);
                            break;
                        case 2: // 两张图片
                            // 加载显示第一张图片
                            loadImg(holder.shotScreen1_img, imgList.get(0));
                            // 加载显示第二张图片
                            loadImg(holder.shotScreen2_img, imgList.get(1));
                            setImageVisible(holder.shotScreen1_img, true);
                            setImageVisible(holder.shotScreen2_img, true);
                            setImageVisible(holder.shotScreen3_img, false);

                            // 第一张图片设置监听
                            setOnclick(holder.shotScreen1_img, imgList, 0);
                            // 第二张图片设置监听
                            setOnclick(holder.shotScreen2_img, imgList, 1);
                            break;
                        case 3: // 三张图片
                            // 加载显示第一张图片
                            loadImg(holder.shotScreen1_img, imgList.get(0));
                            // 加载显示第二张图片
                            loadImg(holder.shotScreen2_img, imgList.get(1));
                            // 加载显示第三张图片
                            loadImg(holder.shotScreen3_img, imgList.get(2));
                            setImageVisible(holder.shotScreen1_img, true);
                            setImageVisible(holder.shotScreen2_img, true);
                            setImageVisible(holder.shotScreen3_img, true);

                            // 第一张图片设置监听
                            setOnclick(holder.shotScreen1_img, imgList, 0);
                            // 第二张图片设置监听
                            setOnclick(holder.shotScreen2_img, imgList, 1);
                            // 第三张图片设置监听
                            setOnclick(holder.shotScreen3_img, imgList, 2);
                            break;
                        default:
                            break;
                    }
                }

            } else {
                // 我的问题
                setImageVisible(holder.myShotScreen1_img, false);
                setImageVisible(holder.myShotScreen2_img, false);
                setImageVisible(holder.myShotScreen3_img, false);
                // 大家的问题
                setImageVisible(holder.shotScreen1_img, false);
                setImageVisible(holder.shotScreen2_img, false);
                setImageVisible(holder.shotScreen3_img, false);
            }

            // 关联科目
            if (!"".equals(item.getQuesSubjectName()) && item.getQuesSubjectName().length() > 0) {
                holder.relativeQues_txt.setText(ToolUtils.strReplaceAll(item.getQuesSubjectName()));
                holder.relativeQues_txt.setVisibility(View.VISIBLE);
            } else {
                holder.relativeQues_txt.setVisibility(View.GONE);
            }
            // 问题状态
            int quesStatus = item.getQuesStatus();
            switch (quesStatus) {
                case 1: // 未回答状态
                    holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_no_answer_bg);
                    break;
                case 2: // 未解决状态
                    holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_have_answer_bg);
                    break;
                case 3: // 已解决状态
                    holder.quesStatus_txt.setBackgroundResource(R.drawable.flag_have_abslove_bg);
                    break;
                default:
                    break;
            }
        }
    }

    private void setOnclick(ImageView img, final ArrayList<String> list, final int currentPos) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, GalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", currentPos);
                bundle.putStringArrayList("imgList", list);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });
    }

    /**
     * 加载显示图片
     *
     * @param img
     * @param url
     */
    private void loadImg(ImageView img, String url) {
        Glide.with(mCtx).load(url)
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(img);
    }

    /**
     * 设置图片显示隐藏
     *
     * @param img
     * @param flag
     */
    private void setImageVisible(ImageView img, boolean flag) {
        if (flag) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 大家答疑用户布局
        @BindView(R.id.ourQues_layout)
        RelativeLayout ourQues_layout;
        // 用户图像
        @BindView(R.id.userImg_ques)
        ImageView userImg_ques;
        //  用户名
        @BindView(R.id.userName_ques)
        TextView userName_ques;
        // 大家答疑上传日期
        @BindView(R.id.ourQuesDate_txt)
        TextView ourQuesDate_txt;
        // 答疑内容
        @BindView(R.id.ques_txt)
        TextView ques_txt;
        // 我的截屏布局
        @BindView(R.id.myShotScreen_layout)
        RelativeLayout myShotScreen_layout;
        // 截屏图片1
        @BindView(R.id.myShotScreen1_img)
        ImageView myShotScreen1_img;
        // 截屏图片2
        @BindView(R.id.myShotScreen2_img)
        ImageView myShotScreen2_img;
        // 截屏图片3
        @BindView(R.id.myShotScreen3_img)
        ImageView myShotScreen3_img;
        // 大家的截屏布局
        @BindView(R.id.shotScreen_layout)
        RelativeLayout shotScreen_layout;
        // 截屏图片1
        @BindView(R.id.shotScreen1_img)
        ImageView shotScreen1_img;
        // 截屏图片2
        @BindView(R.id.shotScreen2_img)
        ImageView shotScreen2_img;
        // 截屏图片3
        @BindView(R.id.shotScreen3_img)
        ImageView shotScreen3_img;
        // 截屏当前播放时间
        @BindView(R.id.dateTxt)
        TextView dateTxt;
        // 关联布局
        @BindView(R.id.relationAndStatus_layout)
        RelativeLayout relationAndStatus_layout;
        // 关联视频
        @BindView(R.id.relativeQues_txt)
        TextView relativeQues_txt;
        // 问题状态
        @BindView(R.id.quesStatus_txt)
        ImageView quesStatus_txt;
        // 追问布局
        @BindView(R.id.response_layout)
        RelativeLayout response_layout;
        // 追问按钮
        @BindView(R.id.responseCount_txt)
        Button responseCount_txt;
        // 点赞布局
        @BindView(R.id.zan_layout)
        LinearLayout zan_layout;
        // 点赞数量
        @BindView(R.id.zanResponseCount)
        TextView zanResponseCount;
        // 评论布局
        @BindView(R.id.commit_layout)
        LinearLayout commit_layout;
        // 评论按钮
        @BindView(R.id.commit_Btn)
        Button commit_Btn;
        // 评论数量
        @BindView(R.id.commitCount_txt)
        TextView commitCount_txt;
        // 最佳答案标记
        @BindView(R.id.bestAnswer_Img)
        ImageView bestAnswer_Img;
        // 分行线
        @BindView(R.id.divLine)
        ImageView divLine;


        public MyHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
