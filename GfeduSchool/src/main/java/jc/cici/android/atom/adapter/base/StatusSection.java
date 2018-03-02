package jc.cici.android.atom.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 封装模块
 * Created by atom on 2017/11/13.
 */

public abstract class StatusSection extends Section {

    public StatusSection(int itemResourceId) {
        super();
        this.itemResourceId = itemResourceId;
    }

    public StatusSection(int headerResourceId, int itemResourceId) {
        this(itemResourceId);
        this.headerResourceId = headerResourceId;
        this.hasHeader = true;
    }

    public StatusSection(int headerResourceId, int footerResourceId, int itemResourceId) {
        this(headerResourceId, itemResourceId);
        this.footerResourceId = footerResourceId;
        this.hasFooterView = true;
    }

    @Override
    public final void onBindLoadingViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindLoadingViewHolder(holder);
    }


    @Override
    public final RecyclerView.ViewHolder getLoadingViewHolder(View view) {
        return super.getLoadingViewHolder(view);
    }


    @Override
    public final void onBindFailedViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindFailedViewHolder(holder);
    }


    @Override
    public final RecyclerView.ViewHolder getFailedViewHolder(View view) {
        return super.getFailedViewHolder(view);
    }

}
