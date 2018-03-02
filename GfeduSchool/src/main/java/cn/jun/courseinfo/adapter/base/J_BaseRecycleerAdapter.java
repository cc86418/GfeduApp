//package cn.jun.courseinfo.adapter.base;
//
//import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jc.cici.android.R;
//
///**
// * 封装RecycleAdapter
// * Created by atom on 2017/5/9.
// */
//
//public abstract class J_BaseRecycleerAdapter<DATA, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
//    public static final int TYPE_HEADER = 0;  //说明是带有Header的
//    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
//    private View mHeaderView;
//
//    protected Context context;
//    protected List<DATA> items = new ArrayList<>();
//
//    public J_BaseRecycleerAdapter(Context context) {
//        this.context = context;
//    }
//
//    public J_BaseRecycleerAdapter(Context context, List<DATA> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    public View getHeaderView() {
//        return mHeaderView;
//    }
//
//    public void setHeaderView(View headerView) {
//        mHeaderView = headerView;
//        notifyItemInserted(0);
//    }
//
//
//    /**
//     * 设置数据源
//     */
//    public void setItems(List<DATA> items) {
//        if (items != null) {
//            this.items = items;
//        }
//    }
//
//    /**
//     * 添加一个数据集
//     */
//    public void addItems(List<DATA> items) {
//        if (items != null) {
//            this.items.addAll(items);
//        }
//    }
//
//    /**
//     * 获取一条数据
//     */
//    public DATA getItem(int position) {
//        return items.get(position);
//    }
//
//    /**
//     * 清空数据
//     */
//    public void clear() {
//        items.clear();
//    }
//
//    @Override
//    public void onBindViewHolder(final VH holder, int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClick(v, holder.getAdapterPosition());
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
//                }
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onItemLongClick(v, holder.getAdapterPosition());
//                if (mOnItemLongClickListener != null) {
//                    mOnItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
//                }
//                return true;
//            }
//        });
//        onBindViewHolder(holder, items.get(position), position);
//    }
//
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
////        return onCreateViewHolder(view, viewType);
//        if(mHeaderView != null && viewType == TYPE_HEADER) {
//            return new ListHolder(mHeaderView);
//        }
//
//        View layout = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
//        return new ListHolder(layout);
//    }
//
//    //在这里面加载ListView中的每个item的布局
//    class ListHolder extends RecyclerView.ViewHolder{
//        TextView tv;
//        public ListHolder(View itemView) {
//            super(itemView);
//            //如果是headerview或者是footerview,直接返回
//            if (itemView == mHeaderView){
//                return;
//            }
//            if (itemView == mFooterView){
//                return;
//            }
//            tv = (TextView)itemView.findViewById(R.id.item);
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    /**
//     * 抽象创建viewHolder
//     *
//     * @param view
//     * @return
//     */
//    public abstract VH onCreateViewHolder(View view, int viewType);
//
//    /**
//     * @param item 为当前 item 对应的数据
//     */
//    public abstract void onBindViewHolder(VH holder, DATA item, int position);
//
//    @LayoutRes
//    public abstract int getLayoutId();
//
//    /**
//     * 会先调用此方法，再调用 OnItemClickListener 中的 onItemClick。
//     */
//    protected void onItemClick(View view, int position) {
//    }
//
//    /**
//     * 会先调用此方法，再调用 setOnItemLongClickListener 中的 onItemLongClick。
//     */
//    protected void onItemLongClick(View view, int position) {
//    }
//
//    protected OnItemClickListener mOnItemClickListener;
//    protected OnItemLongClickListener mOnItemLongClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mOnItemClickListener = listener;
//    }
//
//    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
//        mOnItemLongClickListener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    public interface OnItemLongClickListener {
//        void onItemLongClick(View view, int position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (mHeaderView == null) {
//            return TYPE_NORMAL;
//        }
//        if (position == 0) {
//            //第一个item应该加载Header
//            return TYPE_HEADER;
//        }
//
//        return TYPE_NORMAL;
//
//    }
//}
