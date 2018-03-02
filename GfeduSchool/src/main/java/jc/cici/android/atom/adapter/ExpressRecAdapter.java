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
import jc.cici.android.atom.bean.ExpressBean;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * Created by User on 2017/9/20.
 */

public class ExpressRecAdapter extends BaseRecycleerAdapter<ExpressBean, ExpressRecAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ExpressBean> mItems;

    public ExpressRecAdapter(Context context, ArrayList<ExpressBean> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, ExpressBean item, int position) {
        // 发货公司
        holder.name_company.setText("发货公司：" + ToolUtils.strReplaceAll(item.getExpress_Company()));
        // 快递单号
        holder.name_express.setText("快递单号：" + ToolUtils.strReplaceAll(item.getExpress_Code()));
        // 发货时间
        holder.name_time.setText("发货时间：" + ToolUtils.strReplaceAll(item.getExpress_Date()));
        // 发货名称
        String[] str = ToolUtils.strReplaceAll(item.getExpress_GoodsName()).split("###");
        StringBuffer buffer = new StringBuffer();
        if (null != str && str.length > 0) {
            for (int i = 0; i < str.length; i++) {
                buffer.append(i + 1 + "、");
                buffer.append(str[i]);
                buffer.append(" ");
            }
        }
        holder.name_good.setText("发货清单：" + buffer.toString());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_express_view;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 快递公司
        @BindView(R.id.name_company)
        TextView name_company;
        // 单号
        @BindView(R.id.name_express)
        TextView name_express;
        // 发货时间
        @BindView(R.id.name_time)
        TextView name_time;
        // 货物名称
        @BindView(R.id.name_good)
        TextView name_good;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
