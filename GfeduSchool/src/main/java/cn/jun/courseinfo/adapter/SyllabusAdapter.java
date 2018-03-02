package cn.jun.courseinfo.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.jun.bean.PackageDetailBean;
import cn.jun.indexmain.base.MultiTypeBaseAdapter;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import jc.cici.android.R;


public class SyllabusAdapter extends MultiTypeBaseAdapter<PackageDetailBean.Body.ClassList> {
    private Context context;

    public SyllabusAdapter(Context context, List<PackageDetailBean.Body.ClassList> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        this.context = context;
    }

    @Override
    protected void convert(CommonViewHolder holder, final PackageDetailBean.Body.ClassList data, int viewType) {
        if (viewType == 0) {//设置头部
//            setHeaderView(holder);
        } else {
            String title = data.getClass_Name();
            Log.i("title","title"+title);
            title = title.replace("&nbsp;", " ");
            String imageview = data.getClass_MobileImage();
            String Class_MinSalePrice = data.getClass_MinSalePrice();
            String Class_MaxSalePrice = data.getClass_MaxSalePrice();
            holder.setText(R.id.title, title);
            holder.setText(R.id.price_saleregion, "¥" + Class_MinSalePrice + " - " + Class_MaxSalePrice);
            //继续设置图片
            ImageView im = (ImageView) holder.getView(R.id.holder_image);
            Glide.with(context)
                    .load(imageview)
                    .placeholder(R.drawable.pic_kong)
                    .into(im);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.recycleview_header;
        }
        return R.layout.syllabus_recycleview_item;
    }

    @Override
    protected int getViewType(int position, PackageDetailBean.Body.ClassList data) {
        if (data == null) {
            return 0;
        } else {
            return 1;
        }


    }

}
