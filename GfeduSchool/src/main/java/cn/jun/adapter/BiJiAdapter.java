package cn.jun.adapter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.jun.bean.NotesListBean;
import jc.cici.android.R;

import static cn.gfedu.gfeduapp.GfeduApplication.application;

public class BiJiAdapter extends BaseAdapter {
    DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Activity ctx;
    private ArrayList<NotesListBean> mList = new ArrayList<>();


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

    public BiJiAdapter(Activity context, ArrayList<NotesListBean> mList) {
        super();
        this.ctx = context;
        this.mList = mList;

    }

    @Override
    public int getCount() {
        return mList.get(0).getBody().getNotesList().size();
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
        convertView = LayoutInflater.from(ctx).inflate(
                R.layout.biji_listview_items, null);
        holder = new ViewHolder();
        convertView.setTag(holder); // 为所须要的组件添加tag标签

        holder.touxiang = (ImageView) convertView.findViewById(R.id.touxiang);
        holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
        holder.user_time = (TextView) convertView.findViewById(R.id.user_time);
        holder.user_content = (TextView) convertView.findViewById(R.id.user_content);
        holder.biji_time = (TextView) convertView.findViewById(R.id.biji_time);
        holder.biji_dianzan = (TextView) convertView.findViewById(R.id.biji_dianzan);
        holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);

        //头像显示
        String HeadImg = mList.get(0).getBody().getNotesList().get(position).getSN_Head();
        Glide.with(application.getApplicationContext())
                .load(HeadImg)
                .placeholder(R.drawable.pic_kong_banner)
                .into(holder.touxiang);
//        if (!"".equals(HeadImg) && HeadImg != null) {
//            imageLoader.displayImage(HeadImg, holder.touxiang, options,
//                    animateFirstListener);
//        }
        String name = mList.get(0).getBody().getNotesList().get(position).getS_NickName();
        if(!"".equals(name) && null!= name){
            name = name.replace("&nbsp;"," ");
            holder.user_name.setText(name);
        }else{
            holder.user_name.setText("未知用户");
        }


        String user_time = mList.get(0).getBody().getNotesList().get(position).getNTBAddTime();
        holder.user_time.setText(user_time);

        String user_content = mList.get(0).getBody().getNotesList().get(position).getNTBContent();
        holder.user_content.setText(user_content);

        String biji_dianzan = mList.get(0).getBody().getNotesList().get(position).getZcount();
        holder.biji_dianzan.setText(biji_dianzan);

        String biji_time = mList.get(0).getBody().getNotesList().get(position).getNTBAddTime();
        holder.biji_time.setText(biji_time);
        //内容显示
        String NTBScreenShots = mList.get(0).getBody().getNotesList().get(position).getNTBScreenShots();
//        if (!"".equals(NTBScreenShots) && NTBScreenShots != null) {
//            imageLoader.displayImage(NTBScreenShots, holder.image_view, options,
//                    animateFirstListener);
//        }
        Glide.with(application.getApplicationContext())
                .load(NTBScreenShots)
                .placeholder(R.drawable.pic_kong_banner)
                .into(holder.image_view);

        return convertView;
    }

    // 视图缓存， 把view存起来
    public final class ViewHolder {
        public ImageView touxiang;
        public TextView user_name;
        public TextView user_time;
        public TextView user_content;
        public TextView biji_time;
        public TextView biji_dianzan;
        public ImageView image_view;

    }
}
