package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.MoreBean;

/**
 * 更多功能适配器
 * Created by atom on 2017/12/21.
 */

public class MoreFunctionAdapter extends BaseRecycleerAdapter<MoreBean, MoreFunctionAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<MoreBean> mDatas;

    public MoreFunctionAdapter(Context context, List<MoreBean> items) {
        super(context, items);
        this.mCtx = context;
        this.mDatas = (ArrayList<MoreBean>) items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, MoreBean item, int position) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_more;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 图标
        @BindView(R.id.iconMore_img)
        ImageView iconMore_img;
        // 标题
        @BindView(R.id.moreTitle_txt)
        TextView moreTitle_txt;
        // 详情
        @BindView(R.id.moreDetail_txt)
        TextView moreDetail_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
