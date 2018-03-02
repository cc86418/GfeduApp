package cn.jun.adapter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lling.photopicker.utils.OtherUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.jun.bean.QuesListBean;
import jc.cici.android.R;

import static cn.gfedu.gfeduapp.GfeduApplication.application;
import static jc.cici.android.R.id.image1;
import static jc.cici.android.R.id.image2;
import static jc.cici.android.R.id.image3;

public class WenDaAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Activity ctx;
    private ArrayList<QuesListBean> mList = new ArrayList<>();
    private int mColumnWidth;
    private ArrayList<String> QuesImageUrl = new ArrayList<>();
    private ArrayList<ArrayList<String>> allQuesImageUrl = new ArrayList<ArrayList<String>>();


    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        /**
         * 监听是否加载完，完成则实现动画效果
         */
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);// 动画效果
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public WenDaAdapter(Activity context, ArrayList<QuesListBean> mList) {
        super();
        this.ctx = context;
        this.mList = mList;

        int screenWidth = OtherUtils.getWidthInPx(ctx);
        mColumnWidth = (screenWidth - OtherUtils.dip2px(ctx, 4)) / 3;

    }

    @Override
    public int getCount() {
        return mList.get(0).getBody().getQuesList().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
//        View view = convertView;
//        if (view == null) {
        convertView = LayoutInflater.from(ctx).inflate(
                R.layout.wenda_listview_items, null);
        holder = new ViewHolder();
        convertView.setTag(holder); // 为所须要的组件添加tag标签
        holder.touxiang = (ImageView) convertView.findViewById(R.id.touxiang);
        holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
        holder.user_time = (TextView) convertView.findViewById(R.id.user_time);
        holder.user_content = (TextView) convertView.findViewById(R.id.user_content);
        holder.study_state = (ImageView) convertView.findViewById(R.id.study_state);
        holder.image_linear = (LinearLayout) convertView.findViewById(R.id.image_linear);
        holder.image1 = (ImageView) convertView.findViewById(image1);
        holder.image2 = (ImageView) convertView.findViewById(image2);
        holder.image3 = (ImageView) convertView.findViewById(image3);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
        //头像显示
        String HeadImg = mList.get(0).getBody().getQuesList().get(position).getHeadImg();
        Glide.with(application.getApplicationContext())
                .load(HeadImg)
                .placeholder(R.drawable.pic_kong_banner)
                .into(holder.touxiang);
//        if (!"".equals(HeadImg) && HeadImg != null) {
//            imageLoader.displayImage(HeadImg, holder.touxiang, options,
//                    animateFirstListener);
//        } else {
//            holder.touxiang.setBackgroundResource(R.drawable.touxiang_zhanweitu);
//        }
        //昵称
        String NickName = mList.get(0).getBody().getQuesList().get(position).getNickName();
        if("".equals(NickName) && null== NickName){
            holder.user_name.setText("未知用户");
        }else{
            NickName = NickName.replace("&nbsp;"," ");
            holder.user_name.setText(NickName);
        }
        //问题图片
        QuesImageUrl = mList.get(0).getBody().getQuesList().get(position).getQuesImageUrl();
        //是否删除
        String IsDelete = mList.get(0).getBody().getQuesList().get(position).getIsDelete();
        //删除理由
        String DeleteDetail= mList.get(0).getBody().getQuesList().get(position).getDeleteDetail();
        //添加时间
        String QuesAddTime = mList.get(0).getBody().getQuesList().get(position).getQuesAddTime();
        holder.user_time.setText(QuesAddTime);
        //问题内容
        String QuesContent = mList.get(0).getBody().getQuesList().get(position).getQuesContent();
        //问题状态//问题状态:1.未回答  2.已回答  3.已解决
        String QuesStatus = mList.get(0).getBody().getQuesList().get(position).getQuesStatus();
        if ("1".equals(QuesStatus)) {
            holder.study_state.setBackgroundResource(R.drawable.icon_weihuida);
        } else if ("2".equals(QuesStatus)) {
            holder.study_state.setBackgroundResource(R.drawable.icon_yihuida);
        } else if ("3".equals(QuesStatus)) {
            holder.study_state.setBackgroundResource(R.drawable.icon_yijiejue);
        }
        //问题ID
        String QuesId = mList.get(0).getBody().getQuesList().get(position).getQuesId();

        if("1".equals(IsDelete)){//删除
            DeleteDetail = DeleteDetail.replace("&nbsp;"," ");
            holder.user_content.setText(DeleteDetail);
            holder.image_linear.setVisibility(View.GONE);
        }else if("2".equals(IsDelete)){//未删除
            QuesContent = QuesContent.replace("&nbsp;"," ");
            holder.user_content.setText(QuesContent);
            if (QuesImageUrl != null && !"".equals(QuesImageUrl)) {
                if (1 == QuesImageUrl.size()) {
                    holder.image_linear.setVisibility(View.VISIBLE);
                    String image1 = QuesImageUrl.get(0);
//                    imageLoader.displayImage(image1, holder.image1, options,
//                            animateFirstListener);
                    Glide.with(application.getApplicationContext())
                            .load(image1)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image1);
                } else if (2 == QuesImageUrl.size()) {
                    holder.image_linear.setVisibility(View.VISIBLE);
                    String image1 = QuesImageUrl.get(0);
//                    imageLoader.displayImage(image1, holder.image1, options,
//                            animateFirstListener);
                    Glide.with(application.getApplicationContext())
                            .load(image1)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image1);
                    String image2 = QuesImageUrl.get(1);
//                    imageLoader.displayImage(image2, holder.image2, options,
//                            animateFirstListener);
                    Glide.with(application.getApplicationContext())
                            .load(image2)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image2);
                } else if (3 == QuesImageUrl.size()) {
                    holder.image_linear.setVisibility(View.VISIBLE);
                    String image1 = QuesImageUrl.get(0);
//                    imageLoader.displayImage(image1, holder.image1, options,
//                            animateFirstListener);
                    String image2 = QuesImageUrl.get(1);
//                    imageLoader.displayImage(image2, holder.image2, options,
//                            animateFirstListener);
                    String image3 = QuesImageUrl.get(2);
//                    imageLoader.displayImage(image3, holder.image3, options,
//                            animateFirstListener);
                    Glide.with(application.getApplicationContext())
                            .load(image1)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image1);
                    Glide.with(application.getApplicationContext())
                            .load(image2)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image2);
                    Glide.with(application.getApplicationContext())
                            .load(image3)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image3);
                } else if (QuesImageUrl.size() > 3) {
                    holder.image_linear.setVisibility(View.VISIBLE);
                    String image1 = QuesImageUrl.get(0);
//                    imageLoader.displayImage(image1, holder.image1, options,
//                            animateFirstListener);
                    String image2 = QuesImageUrl.get(1);
//                    imageLoader.displayImage(image2, holder.image2, options,
//                            animateFirstListener);
                    String image3 = QuesImageUrl.get(2);
//                    imageLoader.displayImage(image3, holder.image3, options,
//                            animateFirstListener);
                    Glide.with(application.getApplicationContext())
                            .load(image1)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image1);
                    Glide.with(application.getApplicationContext())
                            .load(image2)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image2);
                    Glide.with(application.getApplicationContext())
                            .load(image3)
                            .placeholder(R.drawable.pic_kong_banner)
                            .into(holder.image3);
                } else if (QuesImageUrl.size() == 0) {
                    holder.image_linear.setVisibility(View.GONE);
                }

            }else{
                holder.image_linear.setVisibility(View.GONE);
            }
        }else{

        }



        return convertView;
    }

    // 视图缓存， 把view存起来
    public final class ViewHolder {
        public ImageView touxiang;
        public TextView user_name;
        public TextView user_time;
        public TextView user_content;
        public ImageView study_state;
        public LinearLayout image_linear;
        public ImageView image1, image2, image3;


    }
}
