package cn.jun.menory.manage_activity;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;



public abstract class HnBaseAdapter<T> extends BaseAdapter {
    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    private OnItemClickListener<T> mOnItemClickListener;
    private List<T> mData;
    private int mLayoutId;

    public HnBaseAdapter(int layoutId) {
        this.mLayoutId = layoutId;
    }

    public HnBaseAdapter(int layoutId, List<T> mData) {
        this.mData = mData;
        this.mLayoutId = layoutId;
    }

    public void setNewData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if( mData == null ) {
            mData = new ArrayList<>();
        }
        if( data != null ) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
            convertView.setTag(new VH(convertView));
        }
        VH vh = (VH) convertView.getTag();
        final T item = mData.get(position);
        convert(vh, item, position);
        final int pos = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mOnItemClickListener != null ) {
                    mOnItemClickListener.onItemClick(item, pos);
                }
            }
        });
        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public abstract void convert(VH holder, T item, int position);

    public static class VH {
        SparseArray<View> views = new SparseArray<>();
        public final View itemView;
        public VH(View itemView) {
            this.itemView = itemView;
        }

        public <V> V getView(@IdRes int id) {
            View view = views.get(id);
            if( view == null ) {
                view = itemView.findViewById(id);
                views.put(id, view);
            }
            return (V) view;
        }
    }
}
