package jc.cici.android.atom.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import jc.cici.android.atom.adapter.LiveContentAdapter;

/**
 * 创建抽象区块类
 * Created by atom on 2017/11/13.
 */

public abstract class Section {
    // 使用枚举显示当前加载状态
    public enum Status {
        LOADING, LOADED, FAILED
    }

    // 设置默认状态为加载状态
    public Status status = Status.LOADED;
    // 默认显示
    boolean visiable = true;
    // 默认无头部布局
    boolean hasHeader = false;
    // 默认无尾部布局
    boolean hasFooterView = false;
    // 头部资源id
    Integer headerResourceId;
    // 尾部资源id
    Integer footerResourceId;
    // item 资源id
    int itemResourceId;
    // 加载视图id
    private Integer loadingResourceId;
    // 加载失败视图id
    private Integer failedResourceId;

    public final Status getStatus() {
        return status;
    }

    public final void setStatus(Status status) {
        this.status = status;
    }

    public final boolean isVisiable() {
        return visiable;
    }

    public final void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    public final boolean isHasHeader() {
        return hasHeader;
    }

    public final void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }


    public boolean isHasFooterView() {
        return hasFooterView;
    }

    public final void setHasFooterView(boolean hasFooterView) {
        this.hasFooterView = hasFooterView;
    }

    public final Integer getHeaderResourceId() {
        return headerResourceId;
    }


    public final Integer getFooterResourceId() {
        return footerResourceId;
    }

    public final int getItemResourceId() {
        return itemResourceId;
    }


    public final Integer getLoadingResourceId() {
        return loadingResourceId;
    }

    public final Integer getFailedResourceId() {
        return failedResourceId;
    }


    public Section() {

    }

    /**
     * 无头部和尾部布局
     *
     * @param itemResourceId
     * @param loadingResourceId
     * @param failedResourceId
     */
    public Section(int itemResourceId, int loadingResourceId, int failedResourceId) {
        this.itemResourceId = itemResourceId;
        this.loadingResourceId = loadingResourceId;
        this.failedResourceId = failedResourceId;
    }

    /**
     * 有头部布局
     *
     * @param headerResourceId
     * @param itemResourceId
     * @param loadingResourceId
     * @param failedResourceId
     */
    public Section(int headerResourceId, int itemResourceId, int loadingResourceId, int failedResourceId) {
        this(itemResourceId, loadingResourceId, failedResourceId);
        this.headerResourceId = headerResourceId;
        hasHeader = true;
    }

    /**
     * 含有头尾布局
     *
     * @param headerResourceId
     * @param footerResourceId
     * @param itemResourceId
     * @param loadingResourceId
     * @param failedResourceId
     */
    public Section(int headerResourceId, int footerResourceId, int itemResourceId, int loadingResourceId, int failedResourceId) {
        this(headerResourceId, itemResourceId, loadingResourceId, failedResourceId);
        this.footerResourceId = footerResourceId;
        hasFooterView = true;
    }

    public final void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (status) {
            case LOADING:
                onBindLoadingViewHolder(holder);
                break;
            case LOADED:
                onBindItemViewHolder(holder, position);
                break;
            case FAILED:
                onBindFailedViewHolder(holder);
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }
    }

    public final int getSectionItemsTotal() {
        int contentItemsTotal;
        switch (status) {
            case LOADING:
                contentItemsTotal = 1;
                break;
            case LOADED:
                contentItemsTotal = getContentItemsTotal();
                break;
            case FAILED:
                contentItemsTotal = 1;
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }
        return contentItemsTotal + (hasHeader ? 1 : 0) + (hasFooterView ? 1 : 0);
    }

    public abstract int getContentItemsTotal();

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new LiveContentAdapter.EmptyViewHolder(view);
    }

    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        return new LiveContentAdapter.EmptyViewHolder(view);
    }

    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    public abstract RecyclerView.ViewHolder getItemViewHolder(View view);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    public RecyclerView.ViewHolder getFailedViewHolder(View view) {
        return new LiveContentAdapter.EmptyViewHolder(view);
    }

    public void onBindFailedViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    public RecyclerView.ViewHolder getLoadingViewHolder(View view) {
        return new LiveContentAdapter.EmptyViewHolder(view);
    }

    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

}
