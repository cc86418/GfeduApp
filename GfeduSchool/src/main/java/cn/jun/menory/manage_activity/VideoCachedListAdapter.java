package cn.jun.menory.manage_activity;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jun.menory.bean.SectionItemBean;
import jc.cici.android.R;



public class VideoCachedListAdapter extends BaseAdapter {
    private final static int TYPE_HEADER = 0;
    private final static int TYPE_ITEM = 1;
    private List<SectionItemBean> mData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public VideoCachedListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
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
        int type = getItemViewType(position);
        final SectionItemBean itemBean = mData.get(position);
        if( type == TYPE_HEADER ) {
            if( convertView == null ) {
                convertView = mLayoutInflater.inflate(R.layout.item_header_for_right_list_view, parent, false);
                convertView.setTag(new VH(convertView));
            }
            VH vh = (VH) convertView.getTag();
            TextView tv = vh.getView(R.id.tv_header);
            tv.setText(itemBean.videoClassStageBean.getStageName());
        } else if( type == TYPE_ITEM ) {
            if( convertView == null ) {
                convertView = mLayoutInflater.inflate(R.layout.item_for_right_list_view, parent, false);
                convertView.setTag(new VH(convertView));
            }
            VH vh = (VH) convertView.getTag();

            final CheckedTextView ctv = vh.getView(R.id.ctv_check);
            ctv.setChecked(itemBean.videoItemBean.checked);
            ctv.setVisibility(itemBean.editMode ? View.VISIBLE : View.INVISIBLE);

            ImageView iv = vh.getView(R.id.iv_learn_state);
            if( itemBean.videoItemBean.learnStatus == 0 ) {
                iv.setImageResource(R.drawable.ic_learn_state_1);
            } else if( itemBean.videoItemBean.learnStatus == 1 ) {
                iv.setImageResource(R.drawable.ic_learn_state_2);
            } else if( itemBean.videoItemBean.learnStatus == 2 ) {
                iv.setImageResource(R.drawable.ic_learn_state_3);
            }
            TextView tv = vh.getView(R.id.tv_title);
            tv.setText(itemBean.videoItemBean.subjectname);
            tv = vh.getView(R.id.tv_sub_title);
            tv.setText(itemBean.videoItemBean.lessonname);

        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).isSectionHeader ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setNewData(List<SectionItemBean> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public List<SectionItemBean> getData() {
        return mData;
    }

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
