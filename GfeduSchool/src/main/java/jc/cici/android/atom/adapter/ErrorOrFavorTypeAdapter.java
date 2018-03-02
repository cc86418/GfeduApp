package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.ErrorOrFavorTypeBean;

/**
 * Created by User on 2018/1/8.
 */

public class ErrorOrFavorTypeAdapter extends BaseRecycleerAdapter<ErrorOrFavorTypeBean.ErrorOrFavorType, ErrorOrFavorTypeAdapter.MyHolder> {


    private Context mCtx;
    private ArrayList<ErrorOrFavorTypeBean.ErrorOrFavorType> mDatas;

    public ErrorOrFavorTypeAdapter(Context context, List<ErrorOrFavorTypeBean.ErrorOrFavorType> items) {
        super(context, items);
        this.mCtx = context;
        this.mDatas = (ArrayList<ErrorOrFavorTypeBean.ErrorOrFavorType>) items;
    }

    public void notifyView(int position){
        for(int i = 0; i < mDatas.size(); i++){
            if(i == position){
                mDatas.get(i).setSelected(true);
            }else{
                mDatas.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, ErrorOrFavorTypeBean.ErrorOrFavorType item, int position) {

        holder.name_tabs.setText(item.getTypeName());
        holder.count_tabs.setText(item.getCount() + "");
        if (item.isSelected()) {
            holder.name_tabs.setTextColor(Color.parseColor("#dd5555"));
        }else{
            holder.name_tabs.setTextColor(Color.parseColor("#333333"));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_tabs;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_tabs)
        TextView name_tabs;
        @BindView(R.id.count_tabs)
        TextView count_tabs;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
