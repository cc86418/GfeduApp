package cn.jun.indexmain.interfaces;


import cn.jun.indexmain.viewholder.CommonViewHolder;


public interface OnMultiTypeItemClickListeners<T> {
    void onItemClick(CommonViewHolder viewHolder, T data, int position, int viewType);
}
