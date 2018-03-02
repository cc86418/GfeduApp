package cn.jun.logistics.adapter;

import android.app.Activity;
import android.widget.ImageView;

import java.util.List;

import cn.jun.bean.CustomBean;
import cn.jun.indexmain.base.MultiTypeBaseAdapter;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import jc.cici.android.R;


public class AllLogisticsAdapter extends MultiTypeBaseAdapter<CustomBean> {
    private Activity context;

    public AllLogisticsAdapter(Activity context, List<CustomBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        this.context = context;
    }

    @Override
    protected void convert(CommonViewHolder holder, final CustomBean data, int viewType) {
        if (viewType == 0) {//设置头部

        } else {
            holder.setText(R.id.allshipp_title, data.Product_Name );
            holder.setText(R.id.allshipp_price, data.Product_Intro);
            holder.setText(R.id.allshipp_kdgs, data.Product_IntroName);
            holder.setText(R.id.allshipp_kddh, data.Product_IntroName);
            holder.setText(R.id.allshipp_kdsj, data.Product_IntroName);

            //设置图片
            ImageView allshipp_im = (ImageView) holder.getView(R.id.allshipp_im);
//            String im_url = data.Product_MobileImage;
//            HttpUtils httpUtils = new HttpUtils();
//            if (httpUtils.isNetworkConnected(context)) {
//                Log.i("im_url -- ", "" + im_url);
//                Glide.with(application.getApplicationContext())
//                        .load(im_url)
//                        .placeholder(R.drawable.pic_kong_xiangqing)
//                        .into(list_im);
//            }


        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.logistics_recycleview_header;
        }
        return R.layout.logistics_recycleview_item;
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
