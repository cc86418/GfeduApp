package jc.cici.android.atom.ui.tiku.treeView;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 树形ViewHolder
 * Created by atom on 2018/1/3.
 */

public abstract class TreeViewBinder<VH extends RecyclerView.ViewHolder> implements LayoutItemType {

    // 设置viewHolder
    public abstract VH provideViewHolder(View itemView);
    // 绑定视图
    public abstract void bindView(VH holder,int position,TreeNode node);

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        protected <T extends View> T findViewById(@IdRes int id) {
            return (T) itemView.findViewById(id);
        }
    }
}
