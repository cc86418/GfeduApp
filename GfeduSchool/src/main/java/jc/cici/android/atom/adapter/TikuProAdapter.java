package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.TikuHomeBean;

/**
 * Created by atom on 2017/12/20.
 */

public class TikuProAdapter extends BaseRecycleerAdapter<TikuHomeBean.TikuProject, TikuProAdapter.MyHolder> {


    private Context mCtx;
    private List<TikuHomeBean.TikuProject> mDatas;

    public TikuProAdapter(Context context, List<TikuHomeBean.TikuProject> items) {
        super(context, items);
        this.mCtx = context;
        this.mDatas = items;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, TikuHomeBean.TikuProject item, int position) {
        if (!"".equals(item.getProjectName())) {
            holder.examProject_txt.setText(item.getProjectName());
        }
//        if(==item.getLevelOne().getProjectId()){
//
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dialog_tiku;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.examProject_txt)
        TextView examProject_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

