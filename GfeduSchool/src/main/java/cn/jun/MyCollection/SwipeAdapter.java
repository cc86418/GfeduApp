package cn.jun.MyCollection;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.jun.bean.MyCollectionBean;
import jc.cici.android.R;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    //    private PackageManager packageManager;
//    private List<ApplicationInfo> mAppList;
//    private List<CustomBean> mlist;
    private ArrayList<MyCollectionBean> mlist;

    public SwipeAdapter(Context context, ArrayList<MyCollectionBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.get(0).getBody().getCollectionList().size();
    }

    @Override
    public MyCollectionBean.Body.CollectionList getItem(int position) {
        return mlist.get(0).getBody().getCollectionList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_app, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        MyCollectionBean.Body.CollectionList item = getItem(position);

        holder.tv_name.setText(item.getClass_Name());
        holder.tv_content.setText(item.getClass_Intro());
        holder.tv_price.setText(item.getClass_PriceSaleRegion());
        Glide.with(context)
                .load(item.getClass_MobileImage())
                .placeholder(R.drawable.pic_kong_banner)
                .into(holder.iv_icon);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_price;
        TextView tv_content;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_price= (TextView) view.findViewById(R.id.tv_price);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            view.setTag(this);
        }
    }

}
