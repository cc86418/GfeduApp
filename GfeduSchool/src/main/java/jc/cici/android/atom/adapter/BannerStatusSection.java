package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.StatusSection;
import jc.cici.android.atom.bean.LiveAds;
import jc.cici.android.atom.view.BannerView;

/**
 * 直播首页banner模块
 * Created by atom on 2017/11/13.
 */

public class BannerStatusSection extends StatusSection {

    private Context mCtx;
    private ArrayList<LiveAds> mAdsList;

    public BannerStatusSection(Context context, ArrayList<LiveAds> adsList) {
        super(R.layout.header_content_live, R.layout.empty_live);
        this.mCtx = context;
        this.mAdsList = adsList;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EmptyViewHolder(view);
    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.home_recommended_banner.delayTime(3).build(mAdsList);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        // 课程图片
        @BindView(R.id.home_recommended_banner)
        BannerView home_recommended_banner;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
