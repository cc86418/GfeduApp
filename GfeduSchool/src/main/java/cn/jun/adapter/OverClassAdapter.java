package cn.jun.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.jun.bean.OverClassListBean;
import jc.cici.android.R;


public class OverClassAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Activity ctx;
    private ArrayList<OverClassListBean> mList = new ArrayList<>();

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

    public OverClassAdapter(Activity context, ArrayList<OverClassListBean> mList) {
        super();
        this.ctx = context;
        this.mList = mList;

    }

    @Override
    public int getCount() {
        return mList.get(0).getBody().getEndList().size();
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
                R.layout.over_list_items, null);
        holder = new ViewHolder();
        convertView.setTag(holder); // 为所须要的组件添加tag标签
        holder.im = (ImageView) convertView.findViewById(R.id.im);
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.content = (TextView) convertView.findViewById(R.id.content);

        String im_s = mList.get(0).getBody().getEndList().get(position).getClassImg();
        if(!"".equals(im_s) && null!= im_s){
            imageLoader.displayImage(im_s, holder.im, options,
                    animateFirstListener);
        }

        String title = mList.get(0).getBody().getEndList().get(position).getClassName();
        holder.title.setText(title);

        String time = mList.get(0).getBody().getEndList().get(position).getClassEndTime();
        try {
            Date date = stringToDate(time, "yyyy-MM-dd");
            time = dateToString(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(time);

        String content = mList.get(0).getBody().getEndList().get(position).getCloseType();
        holder.content.setText(content);


        return convertView;
    }

    // 视图缓存， 把view存起来
    public final class ViewHolder {
        public ImageView im;
        public TextView title;
        public TextView time;
        public TextView content;
    }


    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
}

