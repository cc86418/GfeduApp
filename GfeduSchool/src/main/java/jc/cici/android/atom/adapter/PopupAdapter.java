package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.TikuHomeBean;

/**
 * Created by User on 2017/12/22.
 */

public class PopupAdapter extends BaseRecycleerAdapter<TikuHomeBean.TikuProject.Node, PopupAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<TikuHomeBean.TikuProject.Node> mData;

    public PopupAdapter(Context context, List items) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<TikuHomeBean.TikuProject.Node>) items;
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, TikuHomeBean.TikuProject.Node item, int position) {
        if (null != item.getProjectName()) {
            holder.popup_tv.setText(item.getProjectName());
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_top_popup;
    }


    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.popup_tv)
        TextView popup_tv;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
