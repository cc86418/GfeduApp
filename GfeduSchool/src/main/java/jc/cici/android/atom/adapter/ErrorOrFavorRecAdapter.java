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
import jc.cici.android.atom.bean.ErrorOrFavorBean;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 错题集or收藏夹适配器
 * Created by atom on 2018/1/8.
 */

public class ErrorOrFavorRecAdapter extends BaseRecycleerAdapter<ErrorOrFavorBean.ErrorOrFavor, ErrorOrFavorRecAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ErrorOrFavorBean.ErrorOrFavor> mDatas;

    public ErrorOrFavorRecAdapter(Context context, List<ErrorOrFavorBean.ErrorOrFavor> items) {
        super(context, items);
        this.mCtx = context;
        this.mDatas = (ArrayList<ErrorOrFavorBean.ErrorOrFavor>) items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, ErrorOrFavorBean.ErrorOrFavor item, int position) {

        holder.name_txt.setText(ToolUtils.strReplaceAll(mDatas.get(position).getQues_Content()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_error_or_favor;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_txt)
        TextView name_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
